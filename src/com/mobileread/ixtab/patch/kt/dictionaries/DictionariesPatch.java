package com.mobileread.ixtab.patch.kt.dictionaries;

import java.security.AllPermission;
import java.security.Permission;
import java.util.Map;

import serp.bytecode.BCClass;
import serp.bytecode.Code;

import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;

public class DictionariesPatch extends Patch {

	private static final String CLASS = "com.amazon.ebook.booklet.reader.plugin.systemcards.DictionaryCard";
	public static final String MD5_BEFORE = "cc8be7129f5b45d370f5b3fd41cd069f";
	private static final String MD5_AFTER = "b81427199453be5c3077e81b1c3f893b";

	public int getVersion() {
		return 20120913;
	}

	public Permission[] getRequiredPermissions() {
		return new Permission[] {new AllPermission()};
	}

	protected void initLocalization(String locale, Map map) {
		if (RESOURCE_ID_ENGLISH.equals(locale)) {
			map.put(I18N_JBPATCH_NAME, "Enhance Dictionary Lookup");
			map.put(I18N_JBPATCH_DESCRIPTION, "This patch allows to consult all dictionaries, instead of only one.");
		}
	}

	public PatchMetadata getMetadata() {
		PatchableClass pc = new PatchableClass(CLASS).withChecksums(MD5_BEFORE,
				MD5_AFTER);
		return new PatchMetadata(this).withClass(pc);
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_BEFORE)) {
			patchMethodK(clazz);
			return null;
		}
		return "unsupported MD5: "+md5;
	}

	private void patchMethodK(BCClass clazz) throws Exception {
		Code c = clazz.getDeclaredMethod("K", new Class[] {int.class}).getCode(false);
		c.before(192);
		c.dup();
		c.iload().setLocal(1);
		c.invokestatic().setMethod(DictionariesPatch.class.getDeclaredMethod("onMethodK", new Class[]{Object.class, int.class}));
		c.calculateMaxLocals();
		c.calculateMaxStack();
		
	}
	
	public static void onMethodK(Object o, int currentIndex) {
		if (o == null) {
			// should never happen
			return;
		}
		Backend.modifyPanel(o, currentIndex);
	}
	
}
