package com.mobileread.ixtab.jbpatch;

public final class Environment {
	private Environment() {
	}
	
	public static boolean isKindle() {
		// trivial check, but should work
		return System.getProperty("os.arch", "unknown").toLowerCase().startsWith("arm");
	}

}
