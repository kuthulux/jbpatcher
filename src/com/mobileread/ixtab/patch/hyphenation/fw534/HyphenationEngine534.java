package com.mobileread.ixtab.patch.hyphenation.fw534;

import com.amazon.ebook.util.text.i;
import com.mobileread.ixtab.patch.hyphenation.common.UniversalHyphenationEngine;
import com.mobipocket.common.library.reader.hyphenation.HyphenationEngine;
import com.mobipocket.common.library.reader.hyphenation.g;

public class HyphenationEngine534 extends UniversalHyphenationEngine implements HyphenationEngine {

	public final int[] K() {
		return getSupportedLanguageIds();
	}

	public final String I() {
		return getHyphenSymbol();
	}

	public final g e(String s) {
		return (g) hyphenate(s);
	}

	protected String fromLCID(int id) {
		return i.PJB(id);
	}
}
