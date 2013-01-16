package com.mobileread.ixtab.patch.fontsize;


public abstract class FWAdapter {
	public static final FWAdapter INSTANCE = getAdapterInstance();

	private static FWAdapter getAdapterInstance() {
		String className = "com.mobileread.ixtab.patch.fontsize.FWAdapter512";
		try {
			// this class exists in FW 5.1.2, but thanks to obfuscation, not in
			// PW firmwares.
			Class.forName("com.amazon.ebook.util.text.LanguageTag");
		} catch (Throwable t) {
			className = "com.mobileread.ixtab.patch.fontsize.FWAdapter531";
			try {
				Class.forName("com.amazon.kindle.booklet.ad.resources.AdResources_sq");
				// exists in 5.3.2, but not 5.3.1
				className="com.mobileread.ixtab.patch.fontsize.FWAdapter532";
			} catch (Throwable t2) {}
		}
		try {
			Class clazz = Class.forName(className);
			return (FWAdapter) clazz.newInstance();
		} catch (Throwable t) {
			throw new IllegalStateException("Unable to instantiate class: "
					+ className);
		}
	}

	public abstract String[] getSupportedLocales();

	public abstract String[] getClasses();

	public abstract String[] getMd5Before();

	public abstract String[] getMd5After();

	public abstract String getFieldName();
}
