package com.mobileread.ixtab.patch.ads;

import serp.bytecode.BCClass;
import serp.bytecode.Code;

import com.mobileread.ixtab.jbpatch.Descriptor;
import com.mobileread.ixtab.jbpatch.Log;
import com.mobileread.ixtab.jbpatch.Patch;

public class NoAdsPatch extends Patch {

	public static final String MD5_ADMANAGERIMPL = "c1c94a1e2924c89baac9ad0811589d07";

	protected Descriptor[] getDescriptors() {
		return new Descriptor[] {
		new Descriptor("com.amazon.kindle.restricted.ad.manager.AdManagerImpl",
				new String[] { MD5_ADMANAGERIMPL }), };
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_ADMANAGERIMPL)) {
			return patchAdManagerImpl(clazz);
		}
		return "Unexpected error: unknown MD5 "+md5;
	}

	private String patchAdManagerImpl(BCClass clazz) throws Throwable {
		// make the UI (home screen) believe that there are no ads.
		Code c = clazz.getDeclaredMethod("m").getCode(false);
		c.beforeFirst();
		c.constant().setValue(false);
		for (int i = 0; i < 7; ++i) {
			c.next();
			c.remove();
		}
		c.calculateMaxLocals();
		c.calculateMaxStack();
		
		// intercept attempts to set the screensaver to the ad-displaying one.
		// this setting may also happen from outside of the Java framework
		// (e.g.: /etc/upstart/framework_setup.conf), so we track such requests,
		// and revert the settings if needed.
		c = clazz.getDeclaredMethod("<init>").getCode(false);
		c.before(2);
		c.invokestatic().setMethod(NoAdsPatch.class.getMethod("onAdManagerInstantiated", new Class[0]));
		
		return null;
	}

	public static void onAdManagerInstantiated() {
		try {
			NoAdsWatchdog.enable();
		} catch (Throwable t) {
			t.printStackTrace(Log.INSTANCE);
		}
	}
}
