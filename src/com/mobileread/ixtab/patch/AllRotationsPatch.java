package com.mobileread.ixtab.patch;

import java.util.Map;

import serp.bytecode.BCClass;
import serp.bytecode.Code;

import com.mobileread.ixtab.jbpatch.Environment;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;

public class AllRotationsPatch extends Patch {

	private static final String CLASS = "com.amazon.kindle.restricted.device.impl.ScreenRotationServiceImpl";
	private static final String MD5_BEFORE = "ee50633a567ab87e2521df075d5fd9db";
	private static final String MD5_AFTER = "c72c7094a534cefc440b792363024b7f";

	public int getVersion() {
		return 20130413;
	}
	
	public boolean isAvailable() {
		int jb = Environment.getJBPatchVersionDate();
		String fw = Environment.getFirmware();
		return jb >= 20130328 && "5.1.0".equals(fw);
	}


	protected void initLocalization(String locale, Map map) {
		if (RESOURCE_ID_ENGLISH.equals(locale)) {
			map.put(I18N_JBPATCH_NAME, "Enable all Screen Rotations");
			map.put(I18N_JBPATCH_DESCRIPTION, "This patch allows all screen rotations to be used for the Java-based parts of the Kindle Touch UI. Note that it will not affect the browser and other WAF applications.");
		}
	}

	public PatchMetadata getMetadata() {
		PatchableClass pc = new PatchableClass(CLASS).withChecksums(MD5_BEFORE,
				MD5_AFTER);
		return new PatchMetadata(this).withClass(pc);
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_BEFORE)) {
			patchSetDefaultApplicationLock(clazz);
			patchSetApplicationLock(clazz);
			return null;
		}
		return "unsupported MD5: "+md5;
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
