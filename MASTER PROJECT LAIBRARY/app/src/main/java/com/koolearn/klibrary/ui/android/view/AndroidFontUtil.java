package com.koolearn.klibrary.ui.android.view;

import android.graphics.Typeface;

import com.koolearn.android.util.LogUtil;
import com.koolearn.klibrary.core.filesystem.ZLFile;
import com.koolearn.klibrary.core.fonts.FileInfo;
import com.koolearn.klibrary.core.fonts.FontEntry;
import com.koolearn.klibrary.core.util.SystemInfo;
import com.koolearn.klibrary.core.util.XmlUtil;
import com.koolearn.klibrary.core.util.ZLTTFInfoDetector;
import com.koolearn.klibrary.ui.android.library.ZLAndroidLibrary;
import com.koolearn.kooreader.Paths;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public final class AndroidFontUtil {
	private static volatile Map<String,String[]> ourFontAssetMap;
	private static volatile Map<String,File[]> ourFontFileMap;
	private static volatile Set<File> ourFileSet;
	private static volatile long ourTimeStamp;

	private static Map<String,String[]> getFontAssetMap() {
		if (ourFontAssetMap == null) {
			ourFontAssetMap = new HashMap<String,String[]>();
			XmlUtil.parseQuietly(
					ZLFile.createFileByPath("fonts/fonts.xml"),
					new DefaultHandler() {
						@Override
						public void startElement(String uri, String localName, String qName, Attributes attributes) {
							if ("font".equals(localName)) {
								ourFontAssetMap.put(attributes.getValue("family"), new String[]{
										"fonts/" + attributes.getValue("regular"),
										"fonts/" + attributes.getValue("bold"),
										"fonts/" + attributes.getValue("italic"),
										"fonts/" + attributes.getValue("boldItalic")
								});
							}
						}
					}
			);
		}
		return ourFontAssetMap;
	}

	private static synchronized Map<String,File[]> getFontFileMap(boolean forceReload) {
		final long timeStamp = System.currentTimeMillis();
		if (forceReload && timeStamp < ourTimeStamp + 1000) {
			forceReload = false;
		}
		ourTimeStamp = timeStamp;
		if (ourFileSet == null || forceReload) {
			final HashSet<File> fileSet = new HashSet<File>();
			final FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (name.startsWith(".")) {
						return false;
					}
					final String lcName = name.toLowerCase();
					return lcName.endsWith(".ttf") || lcName.endsWith(".otf");
				}
			};
			for (String dir : Paths.FontPathOption.getValue()) {
				final File[] fileList = new File(dir).listFiles(filter);
				if (fileList != null) {
					fileSet.addAll(Arrays.asList(fileList));
				}
			}
			if (!fileSet.equals(ourFileSet)) {
				ourFileSet = fileSet;
				ourFontFileMap = new ZLTTFInfoDetector().collectFonts(fileSet);
			}
		}
		return ourFontFileMap;
	}

	public static String realFontFamilyName(String fontFamily) {
		for (String name : getFontAssetMap().keySet()) {
			if (name.equalsIgnoreCase(fontFamily)) {
				return name;
			}
		}
		for (String name : getFontFileMap(false).keySet()) {
			if (name.equalsIgnoreCase(fontFamily)) {
				return name;
			}
		}
		if ("serif".equalsIgnoreCase(fontFamily) || "droid serif".equalsIgnoreCase(fontFamily)) {
			return "serif";
		}
		if ("sans-serif".equalsIgnoreCase(fontFamily) || "sans serif".equalsIgnoreCase(fontFamily) || "droid sans".equalsIgnoreCase(fontFamily)) {
			return "sans-serif";
		}
		if ("monospace".equalsIgnoreCase(fontFamily) || "droid mono".equalsIgnoreCase(fontFamily)) {
			return "monospace";
		}
		return "sans-serif";
	}

	public static void fillFamiliesList(ArrayList<String> families) {
		final TreeSet<String> familySet = new TreeSet<String>(getFontFileMap(true).keySet());
		familySet.addAll(getFontAssetMap().keySet());
		familySet.add("默认字体");
//		familySet.add("Droid Serif");
//		familySet.add("Droid Mono");
		families.addAll(familySet);
	}

	private static final HashMap<String,Typeface[]> ourTypefaces = new HashMap<String,Typeface[]>();

	private static Typeface createTypefaceFromAsset(Typeface[] typefaces, String family, int style) {
		final String[] assets = getFontAssetMap().get(family);
		if (assets == null) {
			return null;
		}
		try {
			return Typeface.createFromAsset(
				((ZLAndroidLibrary)ZLAndroidLibrary.Instance()).getAssets(), assets[style]
			);
		} catch (Throwable t) {
		}
		return null;
	}

	private static Typeface createTypefaceFromFile(Typeface[] typefaces, String family, int style) {
		final File[] files = getFontFileMap(false).get(family);
		if (files == null) {
			return null;
		}
		try {
			if (files[style] != null) {
				return Typeface.createFromFile(files[style]);
			}
			for (int i = 0; i < 4; ++i) {
				if (files[i] != null) {
					if (typefaces[i] == null) {
						typefaces[i] = Typeface.createFromFile(files[i]);
					}
					return typefaces[i];
				}
			}
		} catch (Throwable e) {
		}
		return null;
	}

	public static Typeface typeface(SystemInfo systemInfo, FontEntry entry, boolean bold, boolean italic) {
		if (entry.isSystem()) {
			return systemTypeface(entry.Family, bold, italic);
		} else {
			return embeddedTypeface(systemInfo, entry, bold, italic);
		}
	}

	public static Typeface systemTypeface(String family, boolean bold, boolean italic) {
		family = realFontFamilyName(family);
		final int style = (bold ? Typeface.BOLD : 0) | (italic ? Typeface.ITALIC : 0);
		Typeface[] typefaces = ourTypefaces.get(family);
		LogUtil.i6("systemTypeface");
		if (typefaces == null) {
			LogUtil.i6("systemTypeface");
			typefaces = new Typeface[4];
			ourTypefaces.put(family, typefaces);
		}
		LogUtil.i6("systemTypeface");
		Typeface tf = typefaces[style];
		if (tf == null) {
			LogUtil.i6("systemTypeface");

			tf = createTypefaceFromFile(typefaces, family, style);
		}
		if (tf == null) {
			LogUtil.i6("systemTypeface");

			tf = createTypefaceFromAsset(typefaces, family, style);
		}
		if (tf == null) {
			LogUtil.i6("systemTypeface");

			tf = Typeface.create(family, style);
		}
		LogUtil.i6("systemTypeface");

		typefaces[style] = tf;
		return tf;
	}

	private static final class Spec {
		FontEntry Entry;
		boolean Bold;
		boolean Italic;

		Spec(FontEntry entry, boolean bold, boolean italic) {
			Entry = entry;
			Bold = bold;
			Italic = italic;
		}

		@Override
		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}

			if (!(other instanceof Spec)) {
				return false;
			}

			final Spec spec = (Spec)other;
			return Bold == spec.Bold && Italic == spec.Italic && Entry.equals(spec.Entry);
		}

		@Override
		public int hashCode() {
			return 4 * Entry.hashCode() + (Bold ? 2 : 0) + (Italic ? 1 : 0);
		}
	}

	private static final Map<Spec,Object> ourCachedEmbeddedTypefaces = new HashMap<Spec,Object>();
	private static final Object NULL_OBJECT = new Object();

	private static String alias(SystemInfo systemInfo, String family, boolean bold, boolean italic) {
		final StringBuilder builder = new StringBuilder(systemInfo.tempDirectory());
		builder.append("/");
		builder.append(family);
		if (bold) {
			builder.append("-bold");
		}
		if (italic) {
			builder.append("-italic");
		}
		return builder.append(".font").toString();
	}

	private static boolean copy(FileInfo from, String to) {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = ZLFile.createFileByPath(from.Path).getInputStream(from.EncryptionInfo);
			os = new FileOutputStream(to);
			final byte[] buffer = new byte[8192];
			while (true) {
				final int len = is.read(buffer);
				if (len <= 0) {
					break;
				}
				os.write(buffer, 0, len);
			}
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			try {
				os.close();
			} catch (Throwable t) {
				// ignore
			}
			try {
				is.close();
			} catch (Throwable t) {
				// ignore
			}
		}
	}

	private static Typeface getOrCreateEmbeddedTypeface(SystemInfo systemInfo, FontEntry entry, boolean bold, boolean italic) {
		final Spec spec = new Spec(entry, bold, italic);
		Object cached = ourCachedEmbeddedTypefaces.get(spec);
		if (cached == null) {
			final FileInfo fileInfo = entry.fileInfo(bold, italic);
			if (fileInfo != null) {
				final String realFileName = alias(systemInfo, entry.Family, bold, italic);
				if (copy(fileInfo, realFileName)) {
					try {
						cached = Typeface.createFromFile(realFileName);
					} catch (Throwable t) {
						// ignore
					}
				}
				new File(realFileName).delete();
			}
			ourCachedEmbeddedTypefaces.put(spec, cached != null ? cached : NULL_OBJECT);
		}
		return cached instanceof Typeface ? (Typeface)cached : null;
	}

	private static Typeface embeddedTypeface(SystemInfo systemInfo, FontEntry entry, boolean bold, boolean italic) {
		{
			final int index = (bold ? 1 : 0) + (italic ? 2 : 0);
			final Typeface tf = getOrCreateEmbeddedTypeface(systemInfo, entry, bold, italic);
			if (tf != null) {
				return tf;
			}
		}
		for (int i = 0; i < 4; ++i) {
			final Typeface tf = getOrCreateEmbeddedTypeface(systemInfo, entry, (i & 1) == 1, (i & 2) == 2);
			if (tf != null) {
				return tf;
			}
		}
		return null;
	}

	public static void clearFontCache() {
		ourTypefaces.clear();
		ourFileSet = null;
		ourCachedEmbeddedTypefaces.clear();
	}
}
