package com.mobileread.ixtab.patch.hyphenation.fw512;

import com.mobileread.ixtab.patch.hyphenation.common.Factories;
import com.mobileread.ixtab.patch.hyphenation.common.Hyphenation;
import com.mobileread.ixtab.patch.hyphenation.common.UniversalHyphenationEngine;

public class Factories512 extends Factories {

	public Hyphenation newHyphenation(String word) {
		return new Hyphenation512(word);
	}

	public UniversalHyphenationEngine newHyphenationEngine() {
		return new HyphenationEngine512();
	}

}
