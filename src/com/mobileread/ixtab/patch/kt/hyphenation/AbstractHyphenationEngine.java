package com.mobileread.ixtab.patch.kt.hyphenation;

import com.mobipocket.common.library.reader.hyphenation.HyphenationEngine;
import com.mobipocket.common.library.reader.hyphenation.e;

public abstract class AbstractHyphenationEngine implements HyphenationEngine {

	public final int[] B() {
		return getSupportedLanguageIds();
	}

	public final String D() {
		return getHyphenSymbol();
	}

	public final e D(String s) {
		return hyphenate(s);
	}

    public abstract int[] getSupportedLanguageIds();

    public abstract String getHyphenSymbol();

    public abstract e hyphenate(String s);

}
