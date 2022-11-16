package com.koolearn.android.kooreader.libraryService;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.FileObserver;
import android.os.IBinder;

import com.koolearn.android.kooreader.api.KooReaderIntents;
import com.koolearn.android.util.LogInfo;
import com.koolearn.klibrary.core.options.Config;
import com.koolearn.klibrary.text.view.ZLTextFixedPosition;
import com.koolearn.klibrary.text.view.ZLTextPosition;
import com.koolearn.kooreader.Paths;
import com.koolearn.kooreader.book.Author;
import com.koolearn.kooreader.book.BookCollection;
import com.koolearn.kooreader.book.BookEvent;
import com.koolearn.kooreader.book.Bookmark;
import com.koolearn.kooreader.book.BooksDatabase;
import com.koolearn.kooreader.book.DbBook;
import com.koolearn.kooreader.book.IBookCollection;
import com.koolearn.kooreader.book.SerializerUtil;
import com.koolearn.kooreader.book.Tag;
import com.koolearn.kooreader.book.UID;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 服务
 * 数据库
 * 封面、bookid
 */

/**
 * 把服务看成一个领导，服务中有一个banZheng方法，如何才能访问？
 * 绑定服务时，会触发服务的onBind方法，此方法会返回一个Ibinder的对象给MainActivity，通过这个对象访问服务中的方法
 */

/**
 *
 * 作用：跨进程通信
 * 应用场景：远程服务中的中间人对象，其他应用是拿不到的，那么在通过绑定服务获取中间人对象时，就无法强制转换，使用aidl，就可以在其他应用中拿到中间人类所实现的接口（按原先方法可以得到IBinder service，但无法强转，也就无法访问中间人的方法）
 */
public class LibraryService extends Service {
    private static SQLiteBooksDatabase ourDatabase;
    private static final Object ourDatabaseLock = new Object();

    private static final class Observer extends FileObserver {
        private static final int MASK =
                MOVE_SELF | MOVED_TO | MOVED_FROM | DELETE_SELF | DELETE | CLOSE_WRITE | ATTRIB;

        private final String myPrefix;
        private final BookCollection myCollection;

        public Observer(String path, BookCollection collection) {
            super(path, MASK);
            myPrefix = path + '/';
            myCollection = collection;
        }

        @Override
        public void onEvent(int event, String path) {
            event = event & ALL_EVENTS;
            System.err.println("Event " + event + " on " + path);
            switch (event) {
                case MOVE_SELF:
                    // TODO: File(path) removed; stop watching (?)
                    break;
                case MOVED_TO:
                    myCollection.rescan(myPrefix + path);
                    break;
                case MOVED_FROM:
                case DELETE:
                    myCollection.rescan(myPrefix + path);
                    break;
                case DELETE_SELF:
                    // TODO: File(path) removed; watching is stopped automatically (?)
                    break;
                case CLOSE_WRITE:
                case ATTRIB:
                    myCollection.rescan(myPrefix + path);
                    break;
                default:
                    System.err.println("Unexpected event " + event + " on " + myPrefix + path);
                    break;
            }
        }
    }

    /**
     * 对应LibraryInterface.aidl
     */
    /**
     * 中间人对象
     * 在服务中定义一个类实现Ibinder接口，以在onBind方法中返回
     */
    public final class LibraryImplementation extends LibraryInterface.Stub {
        private final BooksDatabase myDatabase;
        private final List<FileObserver> myFileObservers = new LinkedList<FileObserver>();
        private BookCollection myCollection;

        LibraryImplementation(BooksDatabase db) {
            myDatabase = db;
            myCollection = new BookCollection(Paths.systemInfo(LibraryService.this), myDatabase, Paths.bookPath()
            );
            reset(true);
        }

        public void reset(final boolean force) {
            Config.Instance().runOnConnect(new Runnable() {
                public void run() {
                    resetInternal(force);
                }
            });
        }

        private void resetInternal(boolean force) {
            final List<String> bookDirectories = Paths.bookPath();
            if (!force &&
                    myCollection.status() != BookCollection.Status.NotStarted &&
                    bookDirectories.equals(myCollection.BookDirectories)
                    ) {
                return;
            }

            deactivate();
            myFileObservers.clear();

            myCollection = new BookCollection(
                    Paths.systemInfo(LibraryService.this), myDatabase, bookDirectories
            );
            for (String dir : bookDirectories) {
                final Observer observer = new Observer(dir, myCollection);
                observer.startWatching();
                myFileObservers.add(observer);
            }

            myCollection.addListener(new BookCollection.Listener<DbBook>() {
                public void onBookEvent(BookEvent event, DbBook book) {
                    final Intent intent = new Intent(KooReaderIntents.Event.LIBRARY_BOOK);
                    intent.putExtra("type", event.toString());
                    intent.putExtra("book", SerializerUtil.serialize(book));
                    sendBroadcast(intent);
                }

                public void onBuildEvent(BookCollection.Status status) {
                    final Intent intent = new Intent(KooReaderIntents.Event.LIBRARY_BUILD);
                    intent.putExtra("type", status.toString());
                    sendBroadcast(intent);
                }
            });
            myCollection.startBuild();
        }

        public void deactivate() {
            for (FileObserver observer : myFileObservers) {
                observer.stopWatching();
            }
        }

        public String status() {
            return myCollection.status().toString();
        }

        public int size() {
            return myCollection.size();
        }

        public List<String> books(String query) {
            return SerializerUtil.serializeBookList(
                    myCollection.books(SerializerUtil.deserializeBookQuery(query))
            );
        }

        public boolean hasBooks(String query) {
            return myCollection.hasBooks(SerializerUtil.deserializeBookQuery(query).Filter);
        }

        public List<String> recentBooks() {
            return recentlyOpenedBooks(12);
        }

        public List<String> recentlyOpenedBooks(int count) { // 进一步调用BookCllection中的方法
            // 得到DbBook后进行序列化
            return SerializerUtil.serializeBookList(myCollection.recentlyOpenedBooks(count));
        }

        public List<String> recentlyAddedBooks(int count) {
            return SerializerUtil.serializeBookList(myCollection.recentlyAddedBooks(count));
        }

        public String getRecentBook(int index) {
            return SerializerUtil.serialize(myCollection.getRecentBook(index));
        }

        public String getBookByFile(String path) {
            return SerializerUtil.serialize(myCollection.getBookByFile(path));
        }

        public String getBookById(long id) {
            return SerializerUtil.serialize(myCollection.getBookById(id));
        }

        public String getBookByUid(String type, String id) {
            return SerializerUtil.serialize(myCollection.getBookByUid(new UID(type, id)));
        }

        public String getBookByHash(String hash) {
            return SerializerUtil.serialize(myCollection.getBookByHash(hash));
        }

        public List<String> authors() {
            LogInfo.I("");

            final List<Author> authors = myCollection.authors();
            final List<String> strings = new ArrayList<String>(authors.size());
            for (Author a : authors) {
                strings.add(Util.authorToString(a));
            }
            return strings;
        }

        public boolean hasSeries() {
            return myCollection.hasSeries();
        }

        public List<String> series() {
            return myCollection.series();
        }

        public List<String> tags() {
            final List<Tag> tags = myCollection.tags();
            final List<String> strings = new ArrayList<String>(tags.size());
            for (Tag t : tags) {
                strings.add(Util.tagToString(t));
            }
            return strings;
        }

        public List<String> titles(String query) {
            return myCollection.titles(SerializerUtil.deserializeBookQuery(query));
        }

        public List<String> firstTitleLetters() {
            return myCollection.firstTitleLetters();
        }

        public boolean saveBook(String book) {
            return myCollection.saveBook(SerializerUtil.deserializeBook(book, myCollection));
        }

        public boolean canRemoveBook(String book, boolean deleteFromDisk) {
            return myCollection.canRemoveBook(SerializerUtil.deserializeBook(book, myCollection), deleteFromDisk);
        }

        public void removeBook(String book, boolean deleteFromDisk) {
            myCollection.removeBook(SerializerUtil.deserializeBook(book, myCollection), deleteFromDisk);
        }

        public void addToRecentlyOpened(String book) {
            myCollection.addToRecentlyOpened(SerializerUtil.deserializeBook(book, myCollection));
        }

        public void removeFromRecentlyOpened(String book) {
            myCollection.removeFromRecentlyOpened(SerializerUtil.deserializeBook(book, myCollection));
        }

        public List<String> labels() {
            return myCollection.labels();
        }

        public PositionWithTimestamp getStoredPosition(long bookId) {
            final ZLTextPosition position = myCollection.getStoredPosition(bookId);
            return position != null ? new PositionWithTimestamp(position) : null;
        }

        public void storePosition(long bookId, PositionWithTimestamp pos) {
            if (pos == null) {
                return;
            }
            myCollection.storePosition(bookId, new ZLTextFixedPosition.WithTimestamp(
                    pos.ParagraphIndex, pos.ElementIndex, pos.CharIndex, pos.Timestamp
            ));
        }

        @Override
        public boolean isHyperlinkVisited(String book, String linkId) {
            return myCollection.isHyperlinkVisited(SerializerUtil.deserializeBook(book, myCollection), linkId);
        }

        @Override
        public void markHyperlinkAsVisited(String book, String linkId) {
            myCollection.markHyperlinkAsVisited(SerializerUtil.deserializeBook(book, myCollection), linkId);
        }

//        @Override
//        public String getCoverUrl(String path) {
////			return DataUtil.buildUrl(DataConnection, "cover", path);
//            return null;
//        }
//
//        @Override
//        public String getDescription(String book) {
//            return BookUtil.getAnnotation(SerializerUtil.deserializeBook(book, myCollection), myCollection.PluginCollection);
//        }

        @Override
        public Bitmap getCover(final String bookString, final int maxWidth, final int maxHeight, boolean[] delayed) {
            // this method kept for compatibility
            delayed[0] = false;
            return null;
        }

        private Bitmap getResizedBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
            if (maxWidth <= 0 || maxHeight <= 0) {
                return null;
            }

            final int bWidth = bitmap.getWidth();
            final int bHeight = bitmap.getHeight();
            if (maxWidth > bWidth && maxHeight > bHeight) {
                return null;
            }

            final int w, h;
            if (bWidth * maxHeight > bHeight * maxWidth) {
                w = maxWidth;
                h = Math.max(1, (int) (bHeight * (w + .5f) / bWidth));
            } else {
                h = maxHeight;
                w = Math.max(1, (int) (bWidth * (h + .5f) / bHeight));
            }
            if (2 * w <= bWidth && 2 * h <= bHeight) {
                return bitmap;
            }
            return Bitmap.createScaledBitmap(bitmap, w, h, false);
        }

        public List<String> bookmarks(String query) {
            return SerializerUtil.serializeBookmarkList(myCollection.bookmarks(
                    SerializerUtil.deserializeBookmarkQuery(query, myCollection)
            ));
        }

        public String saveBookmark(String serialized) {
            final Bookmark bookmark = SerializerUtil.deserializeBookmark(serialized);
            myCollection.saveBookmark(bookmark);
            return SerializerUtil.serialize(bookmark);
        }

        public void deleteBookmark(String serialized) {
            myCollection.deleteBookmark(SerializerUtil.deserializeBookmark(serialized));
        }

        public List<String> deletedBookmarkUids() {
            return myCollection.deletedBookmarkUids();
        }

        public void purgeBookmarks(List<String> uids) {
            myCollection.purgeBookmarks(uids);
        }

        public String getHighlightingStyle(int styleId) {
            return SerializerUtil.serialize(myCollection.getHighlightingStyle(styleId));
        }

        public List<String> highlightingStyles() {
            return SerializerUtil.serializeStyleList(myCollection.highlightingStyles());
        }

        public void saveHighlightingStyle(String style) {
            myCollection.saveHighlightingStyle(SerializerUtil.deserializeStyle(style));
        }

        public int getDefaultHighlightingStyleId() {
            return myCollection.getDefaultHighlightingStyleId();
        }

        public void setDefaultHighlightingStyleId(int styleId) {
            myCollection.setDefaultHighlightingStyleId(styleId);
        }

        public void rescan(String path) {
            myCollection.rescan(path);
        }

        public String getHash(String book, boolean force) {
            return myCollection.getHash(SerializerUtil.deserializeBook(book, myCollection), force);
        }

        public void setHash(String book, String hash) {
            myCollection.setHash(SerializerUtil.deserializeBook(book, myCollection), hash);
        }

        public List<String> formats() {
            final List<IBookCollection.FormatDescriptor> descriptors = myCollection.formats();
            final List<String> serialized = new ArrayList<String>(descriptors.size());
            for (IBookCollection.FormatDescriptor d : descriptors) {
                serialized.add(Util.formatDescriptorToString(d));
            }
            return serialized;
        }

        public boolean setActiveFormats(List<String> formatIds) {
            if (myCollection.setActiveFormats(formatIds)) {
                reset(true);
                return true;
            } else {
                return false;
            }
        }
    }

    private volatile LibraryImplementation myLibrary;

    @Override
    public void onStart(Intent intent, int startId) {
        onStartCommand(intent, 0, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    /**
     * 返回一个Binder对象,这个对象就是中间人对象
     * startService不会调用到onBind方法,所以不能返回中间人对象
     */
    @Override
    public IBinder onBind(Intent intent) {
        return myLibrary;
        /**
         * 1.谁调用返回给谁,是由系统调用的,因此系统拿到中间人对象
         * 2.系统拿到后就会放到onServiceConnected(ComponentName name, IBinder service)中
         */
    }

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (ourDatabaseLock) {
            if (ourDatabase == null) {
                ourDatabase = new SQLiteBooksDatabase(LibraryService.this);
            }
        }
        myLibrary = new LibraryImplementation(ourDatabase);
    }

    @Override
    public void onDestroy() {
        if (myLibrary != null) {
            final LibraryImplementation l = myLibrary;
            myLibrary = null;
            l.deactivate();
        }
        super.onDestroy();
    }
}