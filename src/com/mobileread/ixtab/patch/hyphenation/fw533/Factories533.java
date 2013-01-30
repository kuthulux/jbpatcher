package com.mobileread.ixtab.patch.hyphenation.fw533;

import com.mobileread.ixtab.patch.hyphenation.common.Factories;
import com.mobileread.ixtab.patch.hyphenation.common.Hyphenation;
import com.mobileread.ixtab.patch.hyphenation.common.UniversalHyphenationEngine;

public class Factories533 extends Factories {

	public Hyphenation newHyphenation(String word) {
		return new Hyphenation533(word);
	}

	public UniversalHyphenationEngine newHyphenationEngine() {
		return new HyphenationEngine533();
	}

}
