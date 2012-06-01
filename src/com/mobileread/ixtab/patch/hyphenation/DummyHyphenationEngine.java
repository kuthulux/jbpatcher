package com.mobileread.ixtab.patch.hyphenation;

import com.mobileread.ixtab.jbpatch.Log;
import com.mobipocket.common.library.reader.hyphenation.e;

public class DummyHyphenationEngine extends AbstractHyphenationEngine {

	public int[] getSupportedLanguageIds() {
		log("getSupportedLanguageIds()");
		return new int[] {0};
	}

	public String getHyphenSymbol() {
		return "@";
	}

	public e hyphenate(String s) {
		return new Hyphenation(s);
	}

	private void log(String msg) {
		Log.INSTANCE.println(msg);
	}

	public DummyHyphenationEngine() {
		super();
		log("new DummyHyphenationEngine()");
	}
	
	public static DummyHyphenationEngine instantiate() {
		return new DummyHyphenationEngine();
	}
}
