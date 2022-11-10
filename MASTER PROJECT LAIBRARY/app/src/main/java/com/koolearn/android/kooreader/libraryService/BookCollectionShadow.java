package com.koolearn.android.kooreader.libraryService;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.koolearn.android.kooreader.api.KooReaderIntents;
import com.koolearn.android.util.LogInfo;
import com.koolearn.android.util.LogUtil;
import com.koolearn.klibrary.core.options.Config;
import com.koolearn.klibrary.text.view.ZLTextFixedPosition;
import com.koolearn.klibrary.text.view.ZLTextPosition;
import com.koolearn.kooreader.book.AbstractBookCollection;
import com.koolearn.kooreader.book.Author;
import com.koolearn.kooreader.book.Book;
import com.koolearn.kooreader.book.BookEvent;
import com.koolearn.kooreader.book.BookQuery;
import com.koolearn.kooreader.book.Bookmark;
import com.koolearn.kooreader.book.BookmarkQuery;
import com.koolearn.kooreader.book.Filter;
import com.koolearn.kooreader.book.HighlightingStyle;
import com.koolearn.kooreader.book.SerializerUtil;
import com.koolearn.kooreader.book.Tag;
import com.koolearn.kooreader.book.UID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BookCollectionShadow extends AbstractBookCollection<Book> implements ServiceConnection {
	private volatile Context myContext;
	private volatile LibraryInterface myInterface;
	private final List<Runnable> myOnBindActions = new LinkedList<Runnable>();

	private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if (!hasListeners()) {
				return;
			}

			try {
				final String type = intent.getStringExtra("type");
				if (KooReaderIntents.Event.LIBRARY_BOOK.equals(intent.getAction())) {
					final Book book = SerializerUtil.deserializeBook(intent.getStringExtra("book"), BookCollectionShadow.this);
					fireBookEvent(BookEvent.valueOf(type), book);
				} else {
					fireBuildEvent(Status.valueOf(type));
				}
			} catch (Exception e) {
				// ignore
			}
		}
	};

	public synchronized boolean bindToService(Context context, Runnable onBindAction) {
		if (myInterface != null && myContext == context) { // 中间人对象
			if (onBindAction != null) {
				Config.Instance().runOnConnect(onBindAction);
			}
			return true;
		} else {
			if (onBindAction != null) {
				synchronized (myOnBindActions) {
					myOnBindActions.add(onBindAction);
				}
			}
			/**
			 * 本地服务：指的是服务和启动服务的activity在同一个进程中（服务和启动它的组件在同一个进程 ）
			 * 远程服务：指的是服务和启动服务的activity不在同一个进程中（服务和启动它的组件不在同一个进程 ）
			 * 远程服务只能隐式启动，类似隐式启动Activity，在清单文件中配置Service标签时，必须配置intent-filter子节点，并指定action子节点
			 * 只绑定一次,start可以多次
			 * 绑定需解绑才能停止
			 * 5.0之后需要加包名
			 */
			final boolean result = context.bindService(
				KooReaderIntents.internalIntent(KooReaderIntents.Action.LIBRARY_SERVICE), // 启动远程服务,只有该应用可以接收到
				this, // 自己就是conn
				Service.BIND_AUTO_CREATE
			);
			if (result) {
				myContext = context;
			}
			return result;
		}
	}

	public synchronized void unbind() {
		if (myContext != null && myInterface != null) {
			try {
				myContext.unregisterReceiver(myReceiver);
			} catch (IllegalArgumentException e) {
				// called before regisration, that's ok
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				myContext.unbindService(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			myInterface = null;
			myContext = null;
		}
	}

	public synchronized void reset(boolean force) {
		if (myInterface != null) {
			try {
				myInterface.reset(force);
			} catch (RemoteException e) {
			}
		}
	}

	public synchronized int size() {
		if (myInterface == null) {
			return 0;
		}
		try {
			return myInterface.size();
		} catch (RemoteException e) {
			return 0;
		}
	}

	public synchronized Status status() {
		if (myInterface == null) {
			return Status.NotStarted;
		}
		try {
			return Status.valueOf(myInterface.status());
		} catch (Throwable t) {
			return Status.NotStarted;
		}
	}

	public List<Book> books(final BookQuery query) {
		return listCall(new ListCallable<Book>() {
			public List<Book> call() throws RemoteException {
				return SerializerUtil.deserializeBookList(
					myInterface.books(SerializerUtil.serialize(query)), BookCollectionShadow.this
				);
			}
		});
	}

	public synchronized boolean hasBooks(Filter filter) {
		if (myInterface == null) {
			return false;
		}
		try {
			return myInterface.hasBooks(SerializerUtil.serialize(new BookQuery(filter, 1)));
		} catch (RemoteException e) {
			return false;
		}
	}

	public List<Book> recentlyAddedBooks(final int count) {

		return listCall(new ListCallable<Book>() {
			public List<Book> call() throws RemoteException {
				return SerializerUtil.deserializeBookList(
					myInterface.recentlyAddedBooks(count), BookCollectionShadow.this
				);
			}
		});
	}

	public List<Book> recentlyOpenedBooks(final int count) {

		return listCall(new ListCallable<Book>() {
			public List<Book> call() throws RemoteException {
				// 继续前一步得到的序列化集合List<String>序列化为List<Book>
				// Book String DbBook String
				return SerializerUtil.deserializeBookList(
					myInterface.recentlyOpenedBooks(count), BookCollectionShadow.this);  // 调用远程服务中的方法
			}
		});
	}

	public synchronized Book getRecentBook(int index) { //y 最近看过的书
		if (myInterface == null) {
			return null;
		}
		try {
			return SerializerUtil.deserializeBook(myInterface.getRecentBook(index), this);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

	public synchronized Book getBookByFile(String path) {
		if (myInterface == null) {
			return null;
		}
		try {
			return SerializerUtil.deserializeBook(myInterface.getBookByFile(path), this);
		} catch (RemoteException e) {
			return null;
		}
	}

	public synchronized Book getBookById(long id) {
		if (myInterface == null) {
			return null;
		}
		try {
			return SerializerUtil.deserializeBook(myInterface.getBookById(id), this);
		} catch (RemoteException e) {
			return null;
		}
	}

	public synchronized Book getBookByUid(UID uid) {
		if (myInterface == null) {
			return null;
		}
		try {
			return SerializerUtil.deserializeBook(myInterface.getBookByUid(uid.Type, uid.Id), this);
		} catch (RemoteException e) {
			return null;
		}
	}

	public synchronized Book getBookByHash(String hash) {
		LogInfo.I("");

		if (myInterface == null) {
			return null;
		}
		try {
			return SerializerUtil.deserializeBook(myInterface.getBookByHash(hash), this);
		} catch (RemoteException e) {
			return null;
		}
	}

	@Override
	protected boolean hasListeners() {
		return super.hasListeners();
	}

	@Override
	public void removeListener(Listener listener) {
		super.removeListener(listener);
	}

	public List<Author> authors() {
		return listCall(new ListCallable<Author>() {
			public List<Author> call() throws RemoteException {
				final List<String> strings = myInterface.authors();
				final List<Author> authors = new ArrayList<Author>(strings.size());
				for (String s : strings) {
					authors.add(Util.stringToAuthor(s));
				}
				return authors;
			}
		});
	}

	public List<Tag> tags() {
		return listCall(new ListCallable<Tag>() {
			public List<Tag> call() throws RemoteException {
				final List<String> strings = myInterface.tags();
				final List<Tag> tags = new ArrayList<Tag>(strings.size());
				for (String s : strings) {
					tags.add(Util.stringToTag(s));
				}
				return tags;
			}
		});
	}

	public synchronized boolean hasSeries() {
		if (myInterface != null) {
			try {
				return myInterface.hasSeries();
			} catch (RemoteException e) {
			}
		}
		return false;
	}

	public List<String> series() {
		return listCall(new ListCallable<String>() {
			public List<String> call() throws RemoteException {
				return myInterface.series();
			}
		});
	}

	public List<String> titles(final BookQuery query) {
		return listCall(new ListCallable<String>() {
			public List<String> call() throws RemoteException {
				return myInterface.titles(SerializerUtil.serialize(query));
			}
		});
	}

	public List<String> firstTitleLetters() {
		return listCall(new ListCallable<String>() {
			public List<String> call() throws RemoteException {
				return myInterface.firstTitleLetters();
			}
		});
	}

	public synchronized boolean saveBook(Book book) {
		if (myInterface == null) {
			return false;
		}
		try {
			return myInterface.saveBook(SerializerUtil.serialize(book));
		} catch (RemoteException e) {
			return false;
		}
	}

	public synchronized boolean canRemoveBook(Book book, boolean deleteFromDisk) {
		if (myInterface == null) {
			return false;
		}
		try {
			return myInterface.canRemoveBook(SerializerUtil.serialize(book), deleteFromDisk);
		} catch (RemoteException e) {
			return false;
		}
	}

	public synchronized void removeBook(Book book, boolean deleteFromDisk) {
		if (myInterface != null) {
			try {
				myInterface.removeBook(SerializerUtil.serialize(book), deleteFromDisk);
			} catch (RemoteException e) {
			}
		}
	}

	public synchronized void addToRecentlyOpened(Book book) {
		if (myInterface != null) {
			try {
				myInterface.addToRecentlyOpened(SerializerUtil.serialize(book));
			} catch (RemoteException e) {
			}
		}
	}

	public synchronized void removeFromRecentlyOpened(Book book) {
		if (myInterface != null) {
			try {
				myInterface.removeFromRecentlyOpened(SerializerUtil.serialize(book));
			} catch (RemoteException e) {
			}
		}
	}

	public List<String> labels() {
		return listCall(new ListCallable<String>() {
			public List<String> call() throws RemoteException {
				return myInterface.labels();
			}
		});
	}

	public String getHash(Book book, boolean force) {
		if (myInterface == null) {
			return null;
		}
		try {
			return myInterface.getHash(SerializerUtil.serialize(book), force);
		} catch (RemoteException e) {
			return null;
		}
	}

	public void setHash(Book book, String hash) {
		if (myInterface == null) {
			return;
		}
		try {
			myInterface.setHash(SerializerUtil.serialize(book), hash);
		} catch (RemoteException e) {
		}
	}

	public synchronized ZLTextFixedPosition.WithTimestamp getStoredPosition(long bookId) {
		if (myInterface == null) {
			return null;
		}

		try {
			final PositionWithTimestamp pos = myInterface.getStoredPosition(bookId);
			if (pos == null) {
				return null;
			}

			return new ZLTextFixedPosition.WithTimestamp(
				pos.ParagraphIndex, pos.ElementIndex, pos.CharIndex, pos.Timestamp
			);
		} catch (RemoteException e) {
			return null;
		}
	}

	public synchronized void storePosition(long bookId, ZLTextPosition position) {
		LogUtil.i18("BC");
		if (position != null && myInterface != null) {
			try {
				myInterface.storePosition(bookId, new PositionWithTimestamp(position));
			} catch (RemoteException e) {
			}
		}
	}

	public synchronized boolean isHyperlinkVisited(Book book, String linkId) {
		if (myInterface == null) {
			return false;
		}

		try {
			return myInterface.isHyperlinkVisited(SerializerUtil.serialize(book), linkId);
		} catch (RemoteException e) {
			return false;
		}
	}

	public synchronized void markHyperlinkAsVisited(Book book, String linkId) {
		if (myInterface != null) {
			try {
				myInterface.markHyperlinkAsVisited(SerializerUtil.serialize(book), linkId);
			} catch (RemoteException e) {
			}
		}
	}

//	@Override
//	public String getCoverUrl(Book book) {
//		LogUtil.i18("BC");
//		if (myInterface == null) {
//			return null;
//		}
//		try {
//			return myInterface.getCoverUrl(book.getPath());
//		} catch (RemoteException e) {
//			return null;
//		}
//	}

//	@Override
//	public String getDescription(Book book) {
//		LogUtil.i18("BC");
//		if (myInterface == null) {
//			return null;
//		}
//		try {
//			return myInterface.getDescription(SerializerUtil.serialize(book));
//		} catch (RemoteException e) {
//			return null;
//		}
//	}

	@Override
	public List<Bookmark> bookmarks(final BookmarkQuery query) {
		return listCall(new ListCallable<Bookmark>() {
			public List<Bookmark> call() throws RemoteException {
				return SerializerUtil.deserializeBookmarkList(
					myInterface.bookmarks(SerializerUtil.serialize(query))
				);
			}
		});
	}

	public synchronized void saveBookmark(Bookmark bookmark) {

		if (myInterface != null) {
			try {
				bookmark.update(SerializerUtil.deserializeBookmark(
					myInterface.saveBookmark(SerializerUtil.serialize(bookmark))
				));
			} catch (RemoteException e) {
			}
		}
	}

	public synchronized void deleteBookmark(Bookmark bookmark) {
		if (myInterface != null) {
			try {
				myInterface.deleteBookmark(SerializerUtil.serialize(bookmark));
			} catch (RemoteException e) {
			}
		}
	}

	public synchronized List<String> deletedBookmarkUids() {
		return listCall(new ListCallable<String>() {
			public List<String> call() throws RemoteException {
				return myInterface.deletedBookmarkUids();
			}
		});
	}

	public void purgeBookmarks(List<String> uids) {
		if (myInterface != null) {
			try {
				myInterface.purgeBookmarks(uids);
			} catch (RemoteException e) {
			}
		}
	}

	public synchronized HighlightingStyle getHighlightingStyle(int styleId) {
		if (myInterface == null) {
			return null;
		}
		try {
			return SerializerUtil.deserializeStyle(myInterface.getHighlightingStyle(styleId));
		} catch (RemoteException e) {
			return null;
		}
	}

	public List<HighlightingStyle> highlightingStyles() {
		return listCall(new ListCallable<HighlightingStyle>() {
			public List<HighlightingStyle> call() throws RemoteException {
				return SerializerUtil.deserializeStyleList(myInterface.highlightingStyles());
			}
		});
	}

	public synchronized void saveHighlightingStyle(HighlightingStyle style) {
		if (myInterface != null) {
			try {
				myInterface.saveHighlightingStyle(SerializerUtil.serialize(style));
			} catch (RemoteException e) {
				// ignore
			}
		}
	}

	public int getDefaultHighlightingStyleId() {
		if (myInterface == null) {
			return 1;
		}
		try {
			return myInterface.getDefaultHighlightingStyleId();
		} catch (RemoteException e) {
			return 1;
		}
	}

	public void setDefaultHighlightingStyleId(int styleId) {
		if (myInterface != null) {
			try {
				myInterface.setDefaultHighlightingStyleId(styleId);
			} catch (RemoteException e) {
				// ignore
			}
		}
	}

	public synchronized void rescan(String path) {
		if (myInterface != null) {
			try {
				myInterface.rescan(path);
			} catch (RemoteException e) {
				// ignore
			}
		}
	}

	public List<FormatDescriptor> formats() {
		return listCall(new ListCallable<FormatDescriptor>() {
			public List<FormatDescriptor> call() throws RemoteException {
				final List<String> serialized = myInterface.formats();
				final List<FormatDescriptor> formats =
					new ArrayList<FormatDescriptor>(serialized.size());
				for (String s : serialized) {
					formats.add(Util.stringToFormatDescriptor(s));
				}
				return formats;
			}
		});
	}

	public synchronized boolean setActiveFormats(List<String> formats) {
		if (myInterface != null) {
			try {
				return myInterface.setActiveFormats(formats);
			} catch (RemoteException e) {
			}
		}
		return false;
	}

	private interface ListCallable<T> {
		List<T> call() throws RemoteException;
	}

	private synchronized <T> List<T> listCall(ListCallable<T> callable) {
		if (myInterface == null) {
			return Collections.emptyList();
		}
		try {
			return callable.call();
		} catch (Exception e) {
			return Collections.emptyList();
		} catch (Throwable e) {
			// TODO: report problem
			return Collections.emptyList();
		}
	}

	/**
	 * 服务连接成功此方法调用
	 * @param name
	 * @param service
	 */
	// method from ServiceConnection interface
	public void onServiceConnected(ComponentName name, IBinder service) {
		synchronized (this) {
			myInterface = LibraryInterface.Stub.asInterface(service);
		}

		final List<Runnable> actions;
		synchronized (myOnBindActions) {
			actions = new ArrayList<Runnable>(myOnBindActions);
			myOnBindActions.clear();
		}
		for (Runnable a : actions) {
			Config.Instance().runOnConnect(a);
		}

		if (myContext != null) {
			myContext.registerReceiver(myReceiver, new IntentFilter(KooReaderIntents.Event.LIBRARY_BOOK));
			myContext.registerReceiver(myReceiver, new IntentFilter(KooReaderIntents.Event.LIBRARY_BUILD));
		}
	}

	// method from ServiceConnection interface
	public synchronized void onServiceDisconnected(ComponentName name) {

	}

	public Book createBook(long id, String url, String title, String encoding, String language) {
//		LogUtil.i8("createBook:" + url.substring("file://".length()));
		return new Book(id, url.substring("file://".length()), title, encoding, language);
	}
}
