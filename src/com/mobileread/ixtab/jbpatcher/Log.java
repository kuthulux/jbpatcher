package com.mobileread.ixtab.jbpatcher;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public final class Log {
	
	private Log() {}
	private static final String LOGFILE = "/tmp/jbpatcher.log";
	
	public static final PrintStream INSTANCE = instantiate();

	private static PrintStream instantiate() {
		if (Environment.isKindle()) {
			try {
				return new PrintStream(new FileOutputStream(LOGFILE));
			} catch (IOException e) {}
		}
		return System.err;
	}

}
