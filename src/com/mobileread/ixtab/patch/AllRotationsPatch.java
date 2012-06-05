package com.mobileread.ixtab.patch;

import serp.bytecode.BCClass;
import serp.bytecode.Code;

import com.mobileread.ixtab.jbpatch.KindleDevice;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableDevice;

public class AllRotationsPatch extends Patch {

	private static final String CLASS = "com.amazon.kindle.restricted.device.impl.ScreenRotationServiceImpl";
	private static final String MD5_BEFORE = "ee50633a567ab87e2521df075d5fd9db";
	private static final String MD5_AFTER = "c72c7094a534cefc440b792363024b7f";

	public String getPatchName() {
		return "Enable all rotations";
	}

	protected int getPatchVersion() {
		return 20120605;
	}

	public PatchMetadata getMetadata() {
		PatchableClass pc = new PatchableClass(CLASS).withChecksums(MD5_BEFORE, MD5_AFTER);
		PatchableDevice pd = new PatchableDevice(KindleDevice.KT_510_1557760049).withClass(pc);
		return new PatchMetadata(this).withDevice(pd);
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
