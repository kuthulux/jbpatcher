package com.mobileread.ixtab.patch.hyphenation.fw512;

import com.amazon.ebook.util.text.LanguageTag;
import com.mobileread.ixtab.patch.hyphenation.common.UniversalHyphenationEngine;
import com.mobipocket.common.library.reader.hyphenation.HyphenationEngine;
import com.mobipocket.common.library.reader.hyphenation.e;

public class HyphenationEngine512 extends UniversalHyphenationEngine implements HyphenationEngine {

	public final int[] B() {
		return getSupportedLanguageIds();
	}

	public final String D() {
		return getHyphenSymbol();
	}

	public final e D(String s) {
		return (e) hyphenate(s);
	}

	protected String fromLCID(int id) {
		return LanguageTag.fromLCID(id);
	}
}
