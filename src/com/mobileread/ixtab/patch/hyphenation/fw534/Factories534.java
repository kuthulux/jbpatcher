package com.mobileread.ixtab.patch.hyphenation.fw534;

import com.mobileread.ixtab.patch.hyphenation.common.Factories;
import com.mobileread.ixtab.patch.hyphenation.common.Hyphenation;
import com.mobileread.ixtab.patch.hyphenation.common.UniversalHyphenationEngine;

public class Factories534 extends Factories {

	public Hyphenation newHyphenation(String word) {
		return new Hyphenation534(word);
	}

	public UniversalHyphenationEngine newHyphenationEngine() {
		return new HyphenationEngine534();
	}

}
