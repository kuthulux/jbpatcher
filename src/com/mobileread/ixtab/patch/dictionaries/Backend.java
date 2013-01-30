package com.mobileread.ixtab.patch.dictionaries;

import com.mobileread.ixtab.jbpatch.Log;

public interface Backend {
	public static final Backend INSTANCE = Init.getInstance();

	static class Init {
		private static Backend getInstance() {
			String className = DictionariesPatch.getFirmware();
			className = "com.mobileread.ixtab.patch.dictionaries.fw"+className+".Backend"+className;
			try {
				return (Backend) Class.forName(className).newInstance();
			} catch (Throwable t) {
				Log.INSTANCE
						.println("E: dictionaries patch failed to initialize, and will not work. Stacktrace below.");
				t.printStackTrace(Log.INSTANCE);
				return new FailedBackend();
			}
		}
	}

	void modifyPanel(Object o, int currentIndex);

	public static class FailedBackend implements Backend {
		public void modifyPanel(Object o, int currentIndex) {
		}
	}
}
