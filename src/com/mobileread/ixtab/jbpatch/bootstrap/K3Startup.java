package com.mobileread.ixtab.jbpatch.bootstrap;

import java.lang.reflect.Method;

public class K3Startup {

	public static void main(String[] args) {
		try {
			// set up JBPatch classloader
			PatchingClassLoader.inject();
			new Stage2().run();
			
			// invoke "real" startup class
			Class startup = Class.forName("com.lab126.linux.arm.LuigiServiceProvider");
			Method main = startup.getMethod("main", new Class[] {String[].class});
			main.invoke(null, new Object[] {args});
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
	}

}
