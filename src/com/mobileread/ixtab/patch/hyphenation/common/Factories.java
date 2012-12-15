package com.mobileread.ixtab.patch.hyphenation.common;

public abstract class Factories {
	public static final Factories INSTANCE = instantiate();
	
	private static Factories instantiate() {
		String className = "com.mobileread.ixtab.patch.hyphenation.fw512.Factories512";
		try {
			// this class exists in FW 5.1.2, but thanks to obfuscation, not in PW firmwares.
			Class.forName("com.amazon.ebook.util.text.LanguageTag");
		} catch (Throwable t) {
			className = "com.mobileread.ixtab.patch.hyphenation.fw531.Factories531";
		}
		try {
			Class clazz = Class.forName(className);
			return (Factories) clazz.newInstance();
		} catch (Throwable t) {
			throw new IllegalStateException("Unable to instantiate class: "+ className);
		}
	}

	public abstract Hyphenation newHyphenation(String word);

	public abstract UniversalHyphenationEngine newHyphenationEngine();
}
