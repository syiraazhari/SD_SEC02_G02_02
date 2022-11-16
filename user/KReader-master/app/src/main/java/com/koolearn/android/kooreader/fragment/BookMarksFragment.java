package com.koolearn.android.kooreader.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.koolearn.android.kooreader.KooReader;
import com.koolearn.android.kooreader.api.KooReaderIntents;
import com.koolearn.android.kooreader.bookmark.EditBookmarkActivity;
import com.koolearn.android.kooreader.libraryService.BookCollectionShadow;
import com.koolearn.android.util.OrientationUtil;
import com.koolearn.android.util.UIMessageUtil;
import com.koolearn.android.util.ViewUtil;
import com.koolearn.klibrary.core.util.ZLColor;
import com.koolearn.klibrary.ui.android.R;
import com.koolearn.klibrary.ui.android.library.UncaughtExceptionHandler;
import com.koolearn.klibrary.ui.android.util.ZLAndroidColorUtil;
import com.koolearn.kooreader.book.Book;
import com.koolearn.kooreader.book.BookEvent;
import com.koolearn.kooreader.book.Bookmark;
import com.koolearn.kooreader.book.BookmarkQuery;
import com.koolearn.kooreader.book.HighlightingStyle;
import com.koolearn.kooreader.book.IBookCollection;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import yuku.ambilwarna.widget.AmbilWarnaPrefWidgetView;

/**
 * ******************************************
 * 作    者 ：  杨越
 * 版    本 ：  1.0
 * 创建日期 ：  2016/3/30
 * 描    述 ：
 * 修订历史 ：
 * ******************************************
 */
public class BookMarksFragment extends Fragment implements IBookCollection.Listener<Book> {
    private static final int OPEN_ITEM_ID = 0;
    private static final int EDIT_ITEM_ID = 1;
    private static final int DELETE_ITEM_ID = 2;

//    private TabHost myTabHost;

    private final Map<Integer, HighlightingStyle> myStyles =
            Collections.synchronizedMap(new HashMap<Integer, HighlightingStyle>());

    private final BookCollectionShadow myCollection = new BookCollectionShadow();

    private final Comparator<Bookmark> myComparator = new Bookmark.ByTimeComparator();

    private volatile BookmarksAdapter myAllBooksAdapter;
//    private volatile BookmarksAdapter mySearchResultsAdapter;

    //    private final ZLResource myResource = ZLResource.resource("bookmarksView");
//    private final ZLStringOption myBookmarkSearchPatternOption =
//            new ZLStringOption("BookmarkSearch", "Pattern", "");
//    private ListView listView;
    private ListView allBookListView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(getActivity()));
//        getActivity().setDefaultKeyMode(Activity.DEFAULT_KEYS_SEARCH_LOCAL);
//        final SearchManager manager = (SearchManager) getActivity().getSystemService(Activity.SEARCH_SERVICE);
//        manager.setOnCancelListener(null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_bookmarks, null);
        allBookListView = (ListView) v.findViewById(R.id.bookmarks_all_books);
//        listView = (ListView) v.findViewById(R.id.bookmarks_search_results);
//        myTabHost = (TabHost) v.findViewById(R.id.bookmarks_tabhost);
//        myTabHost.setup();

//        myTabHost.addTab(myTabHost.newTabSpec("allBooks").setIndicator("所有图书").setContent(R.id.bookmarks_all_books));
//        myTabHost.addTab(myTabHost.newTabSpec("search").setIndicator("搜索").setContent(R.id.bookmarks_search));

//        myTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            public void onTabChanged(String tabId) {
//                if ("search".equals(tabId)) {
//                    listView.setVisibility(View.GONE);
//                    onSearchRequested();
//                }
//            }
//        });
        myCollection.bindToService(getActivity(), new Runnable() {
            public void run() {
                if (myAllBooksAdapter != null) {
                    return;
                }
                myAllBooksAdapter = new BookmarksAdapter(allBookListView, false);
                myCollection.addListener(BookMarksFragment.this);
                updateStyles();
                loadBookmarks();
            }
        });
        return v;
    }

    private void updateStyles() {
        synchronized (myStyles) {
            myStyles.clear();
            for (HighlightingStyle style : myCollection.highlightingStyles()) {
                myStyles.put(style.Id, style);
            }
        }
    }

    private final Object myBookmarksLock = new Object();

    private void loadBookmarks() {
        new Thread(new Runnable() {
            public void run() {
                synchronized (myBookmarksLock) {
//                    for (BookmarkQuery query = new BookmarkQuery(myBook, 50); ; query = query.next()) {
//                        final List<Bookmark> thisBookBookmarks = myCollection.bookmarks(query);
//                        if (thisBookBookmarks.isEmpty()) {
//                            break;
//                        }
//                        myThisBookAdapter.addAll(thisBookBookmarks);
//                        myAllBooksAdapter.addAll(thisBookBookmarks);
//                    }
                    for (BookmarkQuery query = new BookmarkQuery(50); ; query = query.next()) {
                        final List<Bookmark> allBookmarks = myCollection.bookmarks(query);
                        if (allBookmarks.isEmpty()) {
                            break;
                        }
                        myAllBooksAdapter.addAll(allBookmarks);
                    }
                }
            }
        }).start();
    }

    private void updateBookmarks(final Book book) {
        new Thread(new Runnable() {
            public void run() {
                synchronized (myBookmarksLock) {
//                    final boolean flagThisBookTab = book.getId() == myBook.getId();
//                    final boolean flagSearchTab = mySearchResultsAdapter != null;

                    final Map<String, Bookmark> oldBookmarks = new HashMap<String, Bookmark>();
//                    if (flagThisBookTab) {
//                        for (Bookmark b : myThisBookAdapter.bookmarks()) {
//                            oldBookmarks.put(b.Uid, b);
//                        }
//                    } else {
                    for (Bookmark b : myAllBooksAdapter.bookmarks()) {
                        if (b.BookId == book.getId()) {
                            oldBookmarks.put(b.Uid, b);
                        }
                    }
//                    }
//                    final String pattern = myBookmarkSearchPatternOption.getValue().toLowerCase();

                    for (BookmarkQuery query = new BookmarkQuery(book, 50); ; query = query.next()) {
                        final List<Bookmark> loaded = myCollection.bookmarks(query);
                        if (loaded.isEmpty()) {
                            break;
                        }
                        for (Bookmark b : loaded) {
                            final Bookmark old = oldBookmarks.remove(b.Uid);
                            myAllBooksAdapter.replace(old, b);
//                            if (flagThisBookTab) {
//                                myThisBookAdapter.replace(old, b);
//                            }
//                            if (flagSearchTab && MiscUtil.matchesIgnoreCase(b.getText(), pattern)) {
//                                mySearchResultsAdapter.replace(old, b);
//                            }
                        }
                    }
                    myAllBooksAdapter.removeAll(oldBookmarks.values());
//                    if (flagThisBookTab) {
//                        myThisBookAdapter.removeAll(oldBookmarks.values());
//                    }
//                    if (flagSearchTab) {
//                        mySearchResultsAdapter.removeAll(oldBookmarks.values());
//                    }
                }
            }
        }).start();
    }

//    public static void onNewIntent(Intent intent) {
//        OrientationUtil.setOrientation(getActivity(), intent);
//
//        if (!Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            return;
//        }
//        String pattern = intent.getStringExtra(SearchManager.QUERY);
//        myBookmarkSearchPatternOption.setValue(pattern);
//
//        final LinkedList<Bookmark> bookmarks = new LinkedList<Bookmark>();
//        pattern = pattern.toLowerCase();
//        for (Bookmark b : myAllBooksAdapter.bookmarks()) {
//            if (MiscUtil.matchesIgnoreCase(b.getText(), pattern)) {
//                bookmarks.add(b);
//            }
//        }
//        if (!bookmarks.isEmpty()) {
//            final ListView resultsView = (ListView) findViewById(com.koolearn.klibrary.ui.android.R.id.bookmarks_search_results);
//            resultsView.setVisibility(View.VISIBLE);
//            if (mySearchResultsAdapter == null) {
//                mySearchResultsAdapter = new BookmarksAdapter(resultsView, false);
//            } else {
//                mySearchResultsAdapter.clear();
//            }
//            mySearchResultsAdapter.addAll(bookmarks);
//        } else {
//            UIMessageUtil.showErrorMessage(getActivity(), "bookmarkNotFound");
//        }
//    }

    @Override
    public void onDestroy() {
        myCollection.unbind();
        super.onDestroy();
    }

//    public boolean onSearchRequested() {
////		if (DeviceType.Instance().hasStandardSearchDialog()) {
////        getActivity().startSearch(myBookmarkSearchPatternOption.getValue(), true, null, false);
////		} else {
//        SearchDialogUtil.showDialog(getActivity(), BookmarksActivity.class, myBookmarkSearchPatternOption.getValue(), null);
////		}
//        return true;
//    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final int position = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
//        final String tag = myTabHost.getCurrentTabTag();
        final BookmarksAdapter adapter;
//        if ("allBooks".equals(tag)) {
        adapter = myAllBooksAdapter;
//        }
//        else if ("search".equals(tag)) {
//            adapter = mySearchResultsAdapter;
//        }
//        else {
//            throw new RuntimeException("Unknown tab tag: " + tag);
//        }

        final Bookmark bookmark = adapter.getItem(position);
        switch (item.getItemId()) {
            case OPEN_ITEM_ID:
                gotoBookmark(bookmark);
                return true;
            case EDIT_ITEM_ID:
                final Intent intent = new Intent(getActivity(), EditBookmarkActivity.class);
                KooReaderIntents.putBookmarkExtra(intent, bookmark);
                OrientationUtil.startActivity(getActivity(), intent);
                return true;
            case DELETE_ITEM_ID:
                myCollection.deleteBookmark(bookmark);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void gotoBookmark(Bookmark bookmark) {
        bookmark.markAsAccessed();
        myCollection.saveBookmark(bookmark);
        final Book book = myCollection.getBookById(bookmark.BookId);
        if (book != null) {
            KooReader.openBookActivity(getActivity(), book, bookmark);
        } else {
            UIMessageUtil.showErrorMessage(getActivity(), "cannotOpenBook");
        }
    }

    private final class BookmarksAdapter extends BaseAdapter implements AdapterView.OnItemClickListener, View.OnCreateContextMenuListener {
        private final List<Bookmark> myBookmarksList =
                Collections.synchronizedList(new LinkedList<Bookmark>());
        private volatile boolean myShowAddBookmarkItem;

        BookmarksAdapter(ListView listView, boolean showAddBookmarkItem) {
            myShowAddBookmarkItem = showAddBookmarkItem;
            listView.setAdapter(this);
            listView.setOnItemClickListener(this);
            listView.setOnCreateContextMenuListener(this);
        }

        public List<Bookmark> bookmarks() {
            return Collections.unmodifiableList(myBookmarksList);
        }

        public void addAll(final List<Bookmark> bookmarks) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    synchronized (myBookmarksList) {
                        for (Bookmark b : bookmarks) {
                            final int position = Collections.binarySearch(myBookmarksList, b, myComparator);
                            if (position < 0) {
                                myBookmarksList.add(-position - 1, b);
                            }
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }

        private boolean areEqualsForView(Bookmark b0, Bookmark b1) {
            return
                    b0.getStyleId() == b1.getStyleId() &&
                            b0.getText().equals(b1.getText()) &&
                            b0.getTimestamp(Bookmark.DateType.Latest).equals(b1.getTimestamp(Bookmark.DateType.Latest));
        }

        public void replace(final Bookmark old, final Bookmark b) {
            if (old != null && areEqualsForView(old, b)) {
                return;
            }
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    synchronized (myBookmarksList) {
                        if (old != null) {
                            myBookmarksList.remove(old);
                        }
                        final int position = Collections.binarySearch(myBookmarksList, b, myComparator);
                        if (position < 0) {
                            myBookmarksList.add(-position - 1, b);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }

        public void removeAll(final Collection<Bookmark> bookmarks) {
            if (bookmarks.isEmpty()) {
                return;
            }
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    myBookmarksList.removeAll(bookmarks);
                    notifyDataSetChanged();
                }
            });
        }

        public void clear() {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    myBookmarksList.clear();
                    notifyDataSetChanged();
                }
            });
        }

        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
            final int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
            if (getItem(position) != null) {
                menu.add(0, OPEN_ITEM_ID, 0, "Open Note");
                menu.add(0, EDIT_ITEM_ID, 0, "Edit Note");
                menu.add(0, DELETE_ITEM_ID, 0, "Delete Note");
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View view = (convertView != null) ? convertView :
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item, parent, false);
            final ImageView imageView = ViewUtil.findImageView(view, R.id.bookmark_item_icon);
            final View colorContainer = ViewUtil.findView(view, R.id.bookmark_item_color_container);
            final AmbilWarnaPrefWidgetView colorView =
                    (AmbilWarnaPrefWidgetView) ViewUtil.findView(view, R.id.bookmark_item_color);
            final TextView textView = ViewUtil.findTextView(view, R.id.bookmark_item_text);
            final TextView bookTitleView = ViewUtil.findTextView(view, R.id.bookmark_item_booktitle);

            final Bookmark bookmark = getItem(position);
            if (bookmark == null) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.ic_list_plus);
                colorContainer.setVisibility(View.GONE);
                textView.setText("Add");
                bookTitleView.setVisibility(View.GONE);
            } else {
                imageView.setVisibility(View.GONE);
                colorContainer.setVisibility(View.VISIBLE);
                setupColorView(colorView, myStyles.get(bookmark.getStyleId()));
                textView.setText(bookmark.getText());
                if (myShowAddBookmarkItem) {
                    bookTitleView.setVisibility(View.GONE);
                } else {
                    bookTitleView.setVisibility(View.VISIBLE);
                    bookTitleView.setText(bookmark.BookTitle);
                }
            }
            return view;
        }

        @Override
        public final boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public final boolean isEnabled(int position) {
            return true;
        }

        @Override
        public final long getItemId(int position) {
            final Bookmark item = getItem(position);
            return item != null ? item.getId() : -1;
        }

        @Override
        public final Bookmark getItem(int position) {
            if (myShowAddBookmarkItem) {
                --position;
            }
            return position >= 0 ? myBookmarksList.get(position) : null;
        }

        @Override
        public final int getCount() {
            return myShowAddBookmarkItem ? myBookmarksList.size() + 1 : myBookmarksList.size();
        }

        public final void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Bookmark bookmark = getItem(position);
            if (bookmark != null) {
                gotoBookmark(bookmark);
            } else if (myShowAddBookmarkItem) {
                myShowAddBookmarkItem = false;
//                myCollection.saveBookmark(myBookmark);
            }
        }
    }

    // method from IBookCollection.Listener
    public void onBookEvent(BookEvent event, Book book) {
        switch (event) {
            default:
                break;
            case BookmarkStyleChanged:
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        updateStyles();
                        myAllBooksAdapter.notifyDataSetChanged();
//                        if (mySearchResultsAdapter != null) {
//                            mySearchResultsAdapter.notifyDataSetChanged();
//                        }
                    }
                });
                break;
            case BookmarksUpdated:
                updateBookmarks(book);
                break;
        }
    }

    // method from IBookCollection.Listener
    public void onBuildEvent(IBookCollection.Status status) {
    }

    static void setupColorView(AmbilWarnaPrefWidgetView colorView, HighlightingStyle style) {
        Integer rgb = null;
        if (style != null) {
            final ZLColor color = style.getBackgroundColor();
            if (color != null) {
                rgb = ZLAndroidColorUtil.rgb(color);
            }
        }

        if (rgb != null) {
//y			colorView.showCross(false);
            colorView.setBackgroundColor(rgb);
        } else {
//y			colorView.showCross(true);
            colorView.setBackgroundColor(0);
        }
    }
}