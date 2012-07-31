package com.mobileread.ixtab.patch.hyphenation;

import java.security.AllPermission;
import java.security.Permission;
import java.util.Map;

import serp.bytecode.BCClass;
import serp.bytecode.BCMethod;
import serp.bytecode.Code;
import serp.bytecode.ConstantInstruction;

import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;

public class HyphenationPatch extends Patch {

	
	private static final String CLASS_HYPHENATIONMANAGER_510 = "com.mobipocket.common.library.reader.hyphenation.j";
	private static final String CLASS_FRAMECONSTRUCTOR_510 = "com.mobipocket.common.library.reader.db";
	
	public static final String MD5_HYPHENATIONMANAGER_510 = "630a7704149435140c4ef8406749160d";
	public static final String MD5_FRAMECONSTRUCTOR_510 = "cbbba07fab3a4e1be58a9553f8a2e700";

	
	protected void initLocalization(String locale, Map map) {
		if (RESOURCE_ID_ENGLISH.equals(locale)) {
			map.put(I18N_JBPATCH_NAME, "Fix Reader Layout and Hyphenation");
			map.put(I18N_JBPATCH_DESCRIPTION, "This patch fixes the block layout of the E-Book reader. Moreover, books in languages for which you installed hyphenation support will be properly hyphenated. Please refer to the plugin documentation on the Wiki page for further information.");
		}
	}

	public int getVersion() {
		return 20120731;
	}

	public PatchMetadata getMetadata() {
		return new PatchMetadata(this)
				.withClass(
						new PatchableClass(CLASS_FRAMECONSTRUCTOR_510)
								.withChecksums(MD5_FRAMECONSTRUCTOR_510,
										"?"))
				.withClass(
						new PatchableClass(CLASS_HYPHENATIONMANAGER_510)
								.withChecksums(MD5_HYPHENATIONMANAGER_510, "?"))
				;
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_HYPHENATIONMANAGER_510)) {
			return patchHyphenationManager510(clazz);
		}
		if (md5.equals(MD5_FRAMECONSTRUCTOR_510)) {
			return patchFrameConstructor510(clazz);
		}
		return "Unexpected error: unknown MD5 " + md5;
	}

	
	public Permission[] getRequiredPermissions() {
		return new Permission[] {new AllPermission()};
	}

	private String patchFrameConstructor510(BCClass clazz) throws Throwable {
		BCMethod m = clazz
				.getDeclaredMethod("e");
		Code c = m.getCode(false);
		c.before(47);
		// percentage of a word's width that must be available in the current line, for it to be considered for hyphenation
		// original is 25%
		((ConstantInstruction) c.next()).setValue(-1);
		
		// this fixes the weird behavior that some lines do not get justified
		// it essentially always returns a "false" value for the following condition
		// (by turning it into "if (0 > 1)")
		c = clazz.getDeclaredMethod("L").getCode(false);
		c.before(45);
		c.pop();
		c.pop();
		c.constant().setValue(0);
		c.constant().setValue(1);
		
		return null;
	}
	
	private String patchHyphenationManager510(BCClass clazz) throws Throwable {
		
		BCMethod m = clazz
				.getDeclaredMethod(
						"D",
						new String[] { "int" });
		Code c = m.getCode(false);

		c.beforeFirst();
		c.iload().setLocal(1);
		c.invokestatic().setMethod(UniversalHyphenationEngine.class.getMethod("configure", new Class[] {int.class}));
		c.areturn();
		
		return null;

	}
}
