package com.mobileread.ixtab.patch.hyphenation.fw532;

import com.mobileread.ixtab.patch.hyphenation.common.Factories;
import com.mobileread.ixtab.patch.hyphenation.common.Hyphenation;
import com.mobileread.ixtab.patch.hyphenation.common.UniversalHyphenationEngine;

public class Factories532 extends Factories {

	public Hyphenation newHyphenation(String word) {
		return new Hyphenation532(word);
	}

	public UniversalHyphenationEngine newHyphenationEngine() {
		return new HyphenationEngine532();
	}

}
