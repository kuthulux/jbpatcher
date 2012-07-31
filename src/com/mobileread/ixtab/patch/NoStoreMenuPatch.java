package com.mobileread.ixtab.patch;

import java.util.Map;

import serp.bytecode.BCClass;
import serp.bytecode.Code;

import com.amazon.agui.swing.MenuDialog;
import com.amazon.kindle.control.DCBooklet;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;

// THIS IS WORK IN PROGRESS. IT IS NOT A WORKING PATCH YET (AND MAYBE IT WILL NEVER BE).
public class NoStoreMenuPatch extends Patch {
	public static final String CLASS_Q = "com.amazon.kindle.control.util.q";
	public static final String MD5_Q_BEFORE = "efa3ff5db03ca19c52aa9e5b4e0e4aa3";
	public static final String MD5_Q_AFTER = "?";
	
	public static final String CLASS_ACTIONMANAGER = "com.amazon.kindle.swing.actions.ActionManager";
	public static final String MD5_ACTIONMANAGER_BEFORE = "128cd63b5502be0913c9e71b97edd764";
	public static final String MD5_ACTIONMANAGER_AFTER = "?";

	protected void initLocalization(String locale, Map map) {
		if (RESOURCE_ID_ENGLISH.equals(locale)) {
			map.put(I18N_JBPATCH_NAME, "Remove Kindle Store from Menus");
			map.put(I18N_JBPATCH_DESCRIPTION, "blabla");
		}
	}

	public int getVersion() {
		return 20120728;
	}

	public PatchMetadata getMetadata() {
		return new PatchMetadata(this).withClass(
				new PatchableClass(CLASS_Q).withChecksums(MD5_Q_BEFORE, MD5_Q_AFTER)
		).withClass(new PatchableClass(CLASS_ACTIONMANAGER).withChecksums(MD5_ACTIONMANAGER_BEFORE, MD5_ACTIONMANAGER_AFTER));
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_Q_BEFORE)) {
			return patchQ(clazz);
		}
		if (md5.equals(MD5_ACTIONMANAGER_BEFORE)) {
			return patchActionManager(clazz);
		}
		return "Unknown MD5: "+md5;
	}

	private String patchQ(BCClass clazz) {
		Code c = clazz.getDeclaredMethod("D", new Class[] {DCBooklet.class, MenuDialog.class, boolean.class, boolean.class, boolean.class}).getCode(false);
		c.before(25);
		for (int i = 0; i < 4; ++i) {
			c.next();
			c.remove();
		}
		c.calculateMaxLocals();
		c.calculateMaxStack();
		dump(c);
		return null;
	}

	private String patchActionManager(BCClass clazz) {
//		Code c = clazz.getDeclaredMethod("createSystemActions").getCode(false);
//		c.before(13);
//		for (int i = 0; i < 6; ++i) {
//			c.next();
//			c.remove();
//		}
//		c.calculateMaxLocals();
//		c.calculateMaxStack();
//		dump(c);
		return null;
	}

}
