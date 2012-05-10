package com.mobileread.ixtab.patch;

import serp.bytecode.BCClass;
import serp.bytecode.BCMethod;
import serp.bytecode.Code;

import com.mobileread.ixtab.jbpatch.Descriptor;
import com.mobileread.ixtab.jbpatch.Patch;

public class NoAdsPatch extends Patch {

	private static final String MD5_HOMEBOOKLET = "83836d8792099cdf7c2dac9866ae845d";
	private static final String MD5_IMAGEBANNER = "34253b8581da00581fb0d13d43ec9f39";
	private static final String MD5_ADMANAGERIMPL = "c1c94a1e2924c89baac9ad0811589d07";

	protected Descriptor[] getDescriptors() {
		return new Descriptor[] {
		// new Descriptor("com.amazon.kindle.home.HomeBooklet", new String[]
		// {MD5_HOMEBOOKLET}),
		// new Descriptor("com.amazon.kindle.home.view.browse.ImageBanner", new
		// String[] {MD5_IMAGEBANNER}),
		new Descriptor("com.amazon.kindle.restricted.ad.manager.AdManagerImpl",
				new String[] { MD5_ADMANAGERIMPL }), };
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_HOMEBOOKLET)) {
			patchHomeBooklet(clazz);
		} else if (md5.equals(MD5_IMAGEBANNER)) {
			patchImageBanner(clazz);
		} else if (md5.equals(MD5_ADMANAGERIMPL)) {
			patchAdManagerImpl(clazz);
		}
		return null;
	}

	private void patchAdManagerImpl(BCClass clazz) {
		BCMethod m = clazz.getDeclaredMethod("m");
		Code c = m.getCode(false);
		c.beforeFirst();
		c.constant().setValue(false);
		for (int i = 0; i < 7; ++i) {
			c.next();
			c.remove();
		}
		c.calculateMaxLocals();
		c.calculateMaxStack();
	}

	private void patchHomeBooklet(BCClass clazz) {
		BCMethod m = clazz.getDeclaredMethod("D",
				new String[] { "com.amazon.kindle.ad.event.AdEvent" });
		Code c = m.getCode(false);
		c.beforeFirst();
		for (int i = 0; i < 35; ++i) {
			c.next();
			c.remove();
		}
		c.calculateMaxLocals();
		c.calculateMaxStack();
	}

	private void patchImageBanner(BCClass clazz) {
		patchGetPreferredSize(clazz);
		patchPaintComponent(clazz);
	}

	private void patchGetPreferredSize(BCClass clazz) {
		BCMethod m = clazz.getDeclaredMethod("getPreferredSize");
		Code c = m.getCode(false);
		c.before(4);
		for (int i = 0; i < 74; ++i) {
			c.next();
			c.remove();
		}
		c.calculateMaxLocals();
		c.calculateMaxStack();
	}

	private void patchPaintComponent(BCClass clazz) {
		BCMethod m = clazz.getDeclaredMethod("paintComponent");
		Code c = m.getCode(false);
		c.beforeFirst();
		for (int i = 0; i < 153; ++i) {
			c.next();
			c.remove();
		}
		c.calculateMaxLocals();
		c.calculateMaxStack();
	}

}
