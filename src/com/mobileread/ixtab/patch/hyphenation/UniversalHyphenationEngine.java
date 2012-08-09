package com.mobileread.ixtab.patch.hyphenation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.amazon.ebook.util.text.LanguageTag;
import com.mobileread.ixtab.jbpatch.Log;
import com.mobileread.ixtab.patch.hyphenation.itextpdf.HyphenationTree;
import com.mobipocket.common.library.reader.hyphenation.e;

public class UniversalHyphenationEngine extends AbstractHyphenationEngine {

	private static final int MODE_NO_HYPHENATION = 0;
	private static final int MODE_DUMB_HYPHENATION = 1;
	private static final int MODE_COMPLETE_HYPHENATION = 2;

	private static final int ID_ENGLISH = 9;
	private static final UniversalHyphenationEngine instance = new UniversalHyphenationEngine();

	private final int[] languageId = new int[1];

	private final int mode;

	private HyphenationTree tree = null;
	private boolean treeInitialized = false;
	
	private int minPrefix = 3;
	private int minSuffix = 3;

	public UniversalHyphenationEngine() {
		super();
		String confMode = HyphenationPatch.INSTANCE.getMode();
		if (HyphenationPatch.MODE_JUSTIFY_KEY.equals(confMode)) {
			mode = MODE_NO_HYPHENATION;
		} else if (HyphenationPatch.MODE_DUMB_KEY.equals(confMode)) {
			mode = MODE_DUMB_HYPHENATION;
		} else {
			mode = MODE_COMPLETE_HYPHENATION;
		}
	}

	public int[] getSupportedLanguageIds() {
		return languageId;
	}

	public String getHyphenSymbol() {
		return "-";
	}

	private synchronized void configureFor(int lcid) {
		if (languageId[0] == lcid) {
			return;
		} else {
			languageId[0] = lcid;
			treeInitialized = false;
			tree = null;
			minPrefix = HyphenationPatch.INSTANCE.getMinimumFirstSyllableLength();
			minSuffix = HyphenationPatch.INSTANCE.getMinimumLastSyllableLength();
		}
	}

	public synchronized e hyphenate(String s) {
		Hyphenation h = new Hyphenation(s);
		
		if (mode == MODE_NO_HYPHENATION) {
			return h;
		}

		if (mode == MODE_DUMB_HYPHENATION) {
			for (int i = minPrefix; i < s.length() - (1 + minSuffix); ++i) {
				h.addHyphenationPoint(i);
			}
			return h;
		}

		int id = languageId[0];
		if (id == 0) {
			id = ID_ENGLISH;
		}
		if (!treeInitialized) {
			setupTree(id);
		}
		if (tree != null) {
			com.mobileread.ixtab.patch.hyphenation.itextpdf.Hyphenation source = tree
					.hyphenate(s, minPrefix, minSuffix);
			if (source != null) {
				h.setHyphenationPoints(source.getHyphenationPoints());
			}
		}
		return h;
	}

	private void setupTree(int id) {
		treeInitialized = true;
		String name = LanguageTag.fromLCID(id);
		if (name == null || name.equals("")) {
			Log.INSTANCE.println("E: Language code for LCID " + id
					+ " not found!");
			return;
		}
		Log.INSTANCE.println("I: Loading Hyphenations for language ID " + id
				+ " (" + name + ")");
		String def = getDefinitionFor(name);
		if (def == null) {
			Log.INSTANCE.println("W: No Hyphenation definitions found for "
					+ name + ", hyphenation disabled for this language.");
			return;
		}

		InputStream is = null;
		try {
			is = new FileInputStream(def);
		} catch (IOException e) {
			e.printStackTrace(Log.INSTANCE);
			is = null;
		}
		if (is != null) {
			try {
				tree = new HyphenationTree();
				tree.loadSimplePatterns(is);
				Log.INSTANCE.println("I: Hyphenation definitions loaded from "
						+ def);
			} catch (Throwable t) {
				t.printStackTrace(Log.INSTANCE);
				tree = null;
			}
		}
	}

	public static UniversalHyphenationEngine configure(int lcid) {
		instance.configureFor(lcid);
		return instance;
	}

	private static String getDefinitionFor(String name) {
		int dash = name.indexOf('-');
		if (dash != -1) {
			name = name.substring(0, dash) + "_" + name.substring(dash + 1);
		}
		String def = getExactDefinitionFor(name);
		if (def != null) {
			return def;
		}
		if (dash != -1) {
			name = name.substring(0, dash);
			return getExactDefinitionFor(name);
		}
		return null;
	}

	private static String getExactDefinitionFor(String name) {
		String xml = name + ".xml";
		File f = new File(
				"/mnt/us/opt/jbpatch/com.mobileread.ixtab.patch.hyphenation/"
						+ xml);
		if (f.exists() && f.isFile() && f.canRead()) {
			return f.getAbsolutePath();
		}
		return null;
	}
}
