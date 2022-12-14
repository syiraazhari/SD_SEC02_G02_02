package com.koolearn.android.kooreader.api;

import android.content.Intent;

import com.koolearn.kooreader.book.AbstractBook;
import com.koolearn.kooreader.book.AbstractSerializer;
import com.koolearn.kooreader.book.Book;
import com.koolearn.kooreader.book.Bookmark;
import com.koolearn.kooreader.book.SerializerUtil;

public abstract class KooReaderIntents {
	public static final String DEFAULT_PACKAGE = "com.koolearn.klibrary.ui.android";

	public interface Action {
		String API                              = "android.kooreader.action.API";
		String API_CALLBACK                     = "android.kooreader.action.API_CALLBACK";
		String VIEW                             = "android.kooreader.action.VIEW";
		String CANCEL_MENU                      = "android.kooreader.action.CANCEL_MENU";
		String CONFIG_SERVICE                   = "android.kooreader.action.CONFIG_SERVICE";
		String LIBRARY_SERVICE                  = "android.kooreader.action.LIBRARY_SERVICE";
		String LIBRARY                          = "android.kooreader.action.LIBRARY";
		String ERROR                            = "android.kooreader.action.ERROR";
		String CRASH                            = "android.kooreader.action.CRASH";
		String PLUGIN                           = "android.kooreader.action.PLUGIN";
		String CLOSE                            = "android.kooreader.action.CLOSE";
		String PLUGIN_CRASH                     = "android.kooreader.action.PLUGIN_CRASH";
		String PLUGIN_VIEW                      = "android.kooreader.action.plugin.VIEW";
		String PLUGIN_KILL                      = "android.kooreader.action.plugin.KILL";
		String PLUGIN_CONNECT_COVER_SERVICE     = "android.kooreader.action.plugin.CONNECT_COVER_SERVICE";


		String BOOKMARKS                        = "android.kooreader.action.BOOKMARKS";
		String EXTERNAL_BOOKMARKS               = "android.kooreader.action.EXTERNAL_BOOKMARKS";
	}

	public interface Event {
		String CONFIG_OPTION_CHANGE             = "kooreader.config_service.option_change_event";
		String LIBRARY_BOOK                     = "kooreader.library_service.book_event";
		String LIBRARY_BUILD                    = "kooreader.library_service.build_event";

		String SYNC_UPDATED                     = "android.kooreader.event.sync.UPDATED";
	}

	public interface Key {
		String BOOKMARK                         = "kooreader.bookmark";
		String BOOK                             = "kooreader.book";
		String TYPE                             = "kooreader.type";
	}

	public static Intent defaultInternalIntent(String action) {
		return internalIntent(action).addCategory(Intent.CATEGORY_DEFAULT);
	}

	public static Intent internalIntent(String action) {
//		1.???????????????
//		2.receiver????????????????????????????????????????????????
//		3.???????????????????????????????????????????????????????????????????????????????????????
//
//		?????????
//		1.??????????????????????????????????????????????????????????????????
//		2.????????????????????????????????????????????????
//		3.?????????????????????????????????????????????????????????????????????
		return new Intent(action).setPackage(DEFAULT_PACKAGE);
	}

	public static void putBookExtra(Intent intent, String key, Book book) {
		intent.putExtra(key, SerializerUtil.serialize(book));
	}

	public static void putBookExtra(Intent intent, Book book) {
		putBookExtra(intent, Key.BOOK, book);
	}

	public static <B extends AbstractBook> B getBookExtra(Intent intent, String key, AbstractSerializer.BookCreator<B> creator) {
		return SerializerUtil.deserializeBook(intent.getStringExtra(key), creator);
	}

	public static <B extends AbstractBook> B getBookExtra(Intent intent, AbstractSerializer.BookCreator<B> creator) {
		return getBookExtra(intent, Key.BOOK, creator);
	}

	public static void putBookmarkExtra(Intent intent, String key, Bookmark bookmark) {
		intent.putExtra(key, SerializerUtil.serialize(bookmark));
	}

	public static void putBookmarkExtra(Intent intent, Bookmark bookmark) {
		putBookmarkExtra(intent, Key.BOOKMARK, bookmark);
	}

	public static Bookmark getBookmarkExtra(Intent intent, String key) {
		return SerializerUtil.deserializeBookmark(intent.getStringExtra(key));
	}

	public static Bookmark getBookmarkExtra(Intent intent) {
		return getBookmarkExtra(intent, Key.BOOKMARK);
	}
}
