package com.mobileread.ixtab.patch;

import java.io.File;
import java.io.FilePermission;
import java.security.Permission;

import serp.bytecode.BCClass;
import serp.bytecode.Code;
import serp.bytecode.ConstantInstruction;

import com.mobileread.ixtab.jbpatch.KindleDevice;
import com.mobileread.ixtab.jbpatch.Log;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableDevice;

public class NoAdsPatch extends Patch {


	private static final String ADS_FOLDER = "/var/local/adunits";
	
	private static final String CLASS = "com.amazon.kindle.restricted.ad.manager.AdManagerImpl";
	public static final String MD5_ADMANAGERIMPL_BEFORE = "c1c94a1e2924c89baac9ad0811589d07";
	private static final String MD5_ADMANAGERIMPL_AFTER = "ef65c12bd004ebfebb59a64595f0753e";

	public String getPatchName() {
		return "Enable all rotations";
	}

	protected int getPatchVersion() {
		return 20120605;
	}

	public PatchMetadata getMetadata() {
		PatchableClass pc = new PatchableClass(CLASS).withChecksums(MD5_ADMANAGERIMPL_BEFORE, MD5_ADMANAGERIMPL_AFTER);
		PatchableDevice pd = new PatchableDevice(KindleDevice.KT_510_1557760049).withClass(pc);
		return new PatchMetadata(this).withDevice(pd);
	}

	
	public Permission[] getRequiredPermissions() {
		return new Permission[] {
				new FilePermission(ADS_FOLDER, "read,delete"),
				new FilePermission(ADS_FOLDER+"/-", "read,delete"),
		};
	}


	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_ADMANAGERIMPL_BEFORE)) {
			return patchAdManagerImpl(clazz);
		}
		return "Unexpected error: unknown MD5 "+md5;
	}

	private String patchAdManagerImpl(BCClass clazz) throws Throwable {
		// make the UI (home screen) believe that there are no ads:
		// always return false in m()
		Code c = clazz.getDeclaredMethod("m").getCode(false);
		c.beforeFirst();
		c.constant().setValue(false);
		for (int i = 0; i < 7; ++i) {
			c.next();
			c.remove();
		}
		c.calculateMaxLocals();
		c.calculateMaxStack();
		
		// on instantiation, delete /var/local/adunits if it exists
		c = clazz.getDeclaredMethod("<init>").getCode(false);
		c.before(2);
		c.invokestatic().setMethod(NoAdsPatch.class.getMethod("onAdManagerInstantiated", new Class[0]));
		
		// just in case: reverse load/unload logic of method c(),
		// i.e. make it behave like method l().
		c = clazz.getDeclaredMethod("c").getCode(false);
		c.before(20);
		((ConstantInstruction)c.next()).setValue("screensaver");
		c.before(24);
		((ConstantInstruction)c.next()).setValue("ad_screensaver");
		return null;
	}

	public static void onAdManagerInstantiated() {
		// This accounts for the method of manually touching
		// /var/local/adunits as a file (instead of a directory).
		try {
			deleteRecursively(new File(ADS_FOLDER), false);
		} catch (Throwable t) {
			t.printStackTrace(Log.INSTANCE);
		}
	}

	private static void deleteRecursively(File dir, boolean deleteIfIsFile) {
		if (dir.exists()) {
			if (dir.isDirectory()) {
				File[] files = dir.listFiles();
				for (int i=0; i < files.length; ++i) {
					deleteRecursively(files[i], true);
				}
				dir.delete();
			} else if (deleteIfIsFile) {
				dir.delete();
			}
		}
	}
}
