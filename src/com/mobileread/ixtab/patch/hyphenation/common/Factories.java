package com.mobileread.ixtab.patch.hyphenation.common;

public abstract class Factories {
	public static final Factories INSTANCE = instantiate();
	
	private static Factories instantiate() {
		String className = "com.mobileread.ixtab.patch.hyphenation.fw512.Factories512";
		try {
			// this class exists in FW 5.1.2, but thanks to obfuscation, not in 5.3.x firmwares.
			Class.forName("com.amazon.ebook.util.text.LanguageTag");
		} catch (Throwable t) {
			className = "com.mobileread.ixtab.patch.hyphenation.fw531.Factories531";
			try {
				Class.forName("com.amazon.kindle.booklet.ad.resources.AdResources_sq");
				// exists in 5.3.2, but not 5.3.1
				className = "com.mobileread.ixtab.patch.hyphenation.fw532.Factories532";
			} catch (Throwable t2) {
				try {
					Class.forName("com.amazon.ebook.booklet.topazreader.impl.A");
					// exists in 5.3.2, but not 5.3.1
					className = "com.mobileread.ixtab.patch.hyphenation.fw533.Factories533";
				} catch (Throwable t3) {}
			}
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
