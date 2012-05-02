package com.mobileread.ixtab.patch;

import serp.bytecode.BCClass;
import serp.bytecode.Code;

import com.mobileread.ixtab.jbpatch.Descriptor;
import com.mobileread.ixtab.jbpatch.Patch;

public class AllRotationsPatch extends Patch {

	protected Descriptor[] getDescriptors() {
		return new Descriptor[] { new Descriptor(
				"com.amazon.kindle.restricted.device.impl.ScreenRotationServiceImpl",
				new String[] { "ee50633a567ab87e2521df075d5fd9db" }) };
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		patchSetDefaultApplicationLock(clazz);
		patchSetApplicationLock(clazz);
		return null;
	}

	private void patchSetDefaultApplicationLock(BCClass clazz) {
		Code c = clazz.getDeclaredMethods("setDefaultApplicationLock")[0]
				.getCode(false);
		c.after(44);
		c.remove();
		c.constant().setValue("UDRL");
		c.calculateMaxLocals();
		c.calculateMaxStack();
		// dump(c);
	}

	private void patchSetApplicationLock(BCClass clazz) {
		Code c = clazz.getDeclaredMethods("setApplicationLock")[0]
				.getCode(false);
		c.after(9);
		c.remove();
		c.constant().setValue("UDRL");
		c.calculateMaxLocals();
		c.calculateMaxStack();
		// dump(c);
	}

}
