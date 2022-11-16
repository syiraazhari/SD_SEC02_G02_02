package com.koolearn.android.kooreader.libraryService;

import com.koolearn.android.util.LogInfo;
import com.koolearn.kooreader.book.Author;
import com.koolearn.kooreader.book.IBookCollection;
import com.koolearn.kooreader.book.Tag;

abstract class Util {
	static String authorToString(Author author) {
		LogInfo.I("");

		return author.DisplayName + "\000" + author.SortKey;
	}

	static Author stringToAuthor(String string) {
		LogInfo.I("");

		if (string == null) {
			return Author.NULL;
		}

		final String[] split = string.split("\000");
		if (split.length != 2) {
			return Author.NULL;
		}

		return new Author(split[0], split[1]);
	}

	static String tagToString(Tag tag) {
		LogInfo.I("");

		return tag.toString("\000");
	}

	static Tag stringToTag(String string) {
		LogInfo.I("");

		if (string == null) {
			return Tag.NULL;
		}

		final String[] split = string.split("\000");
		if (split.length == 0) {
			return Tag.NULL;
		}

		return Tag.getTag(split);
	}

	static String formatDescriptorToString(IBookCollection.FormatDescriptor descriptor) {
		LogInfo.I("");

		return descriptor.Id + "\000" + descriptor.Name + "\000" + (descriptor.IsActive ? 1 : 0);
	}

	static IBookCollection.FormatDescriptor stringToFormatDescriptor(String string) {
		LogInfo.I("");

		if (string == null) {
			throw new IllegalArgumentException();
		}

		final String[] split = string.split("\000");
		if (split.length != 3) {
			throw new IllegalArgumentException();
		}

		final IBookCollection.FormatDescriptor descriptor = new IBookCollection.FormatDescriptor();
		descriptor.Id = split[0];
		descriptor.Name = split[1];
		descriptor.IsActive = "1".equals(split[2]);
		return descriptor;
	}
}