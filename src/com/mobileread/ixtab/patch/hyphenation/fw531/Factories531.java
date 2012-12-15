package com.mobileread.ixtab.patch.hyphenation.fw531;

import com.mobileread.ixtab.patch.hyphenation.common.Factories;
import com.mobileread.ixtab.patch.hyphenation.common.Hyphenation;
import com.mobileread.ixtab.patch.hyphenation.common.UniversalHyphenationEngine;

public class Factories531 extends Factories {

	public Hyphenation newHyphenation(String word) {
		return new Hyphenation531(word);
	}

	public UniversalHyphenationEngine newHyphenationEngine() {
		return new HyphenationEngine531();
	}

}
