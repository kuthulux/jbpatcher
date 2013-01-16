package com.mobileread.ixtab.patch.dictionaries;

import java.security.AllPermission;
import java.security.Permission;
import java.util.Map;

import serp.bytecode.BCClass;
import serp.bytecode.Code;

import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;

public class DictionariesPatch extends Patch {

	private static final String CLASS_510 = "com.amazon.ebook.booklet.reader.plugin.systemcards.DictionaryCard";
	public static final String MD5_BEFORE_510 = "cc8be7129f5b45d370f5b3fd41cd069f";
	private static final String MD5_AFTER_510 = "4dd81f926a035bd62f628f3734bb7131";

	private static final String CLASS_531 = "com.amazon.ebook.booklet.reader.plugin.systemcards.G";
	public static final String MD5_BEFORE_531 = "f19fe224aae57e9a972dd439127418bd";
	private static final String MD5_AFTER_531 = "7dab5acc5467969add653deb3e7824ce";

	private static final String CLASS_532 = "com.amazon.ebook.booklet.reader.plugin.systemcards.G";
	public static final String MD5_BEFORE_532 = "e4889ee1b7c3aad259e60c9414d584d4";
	private static final String MD5_AFTER_532 = "8a211a40494ab8f62c347e82c4e9b67a";

	private static Boolean isFW53x;
	private static Boolean isFW532;
	
	public int getVersion() {
		return 20130115;
	}
	
	static boolean isFW53x() {
		if (isFW53x == null) {
			synchronized (DictionariesPatch.class) {
				if (isFW53x == null) {
					try {
						Class.forName("com.amazon.ebook.util.lang.UUID");
						isFW53x = Boolean.FALSE;
					} catch (Throwable t) {
						isFW53x = Boolean.TRUE;
					}
				}
			}
		}
		return isFW53x.booleanValue();
	}
	
	static boolean isFW532() {
		if (!isFW53x()) {
			return false;
		}
		synchronized (DictionariesPatch.class) {
			if (isFW532 == null) {
				try {
					Class.forName("com.amazon.kindle.booklet.ad.resources.AdResources_sq");
					isFW532 = Boolean.TRUE;
				} catch (Throwable t) {
					isFW532 = Boolean.FALSE;
				}
			}
		}
		return isFW532.booleanValue();
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
		PatchMetadata md = new PatchMetadata(this);
		if (isFW532()) {
			md.withClass(new PatchableClass(CLASS_532).withChecksums(MD5_BEFORE_532,
					MD5_AFTER_532));
		}
		else if (isFW53x()) {
			md.withClass(new PatchableClass(CLASS_531).withChecksums(MD5_BEFORE_531,
					MD5_AFTER_531));
		} else {
			md.withClass(new PatchableClass(CLASS_510).withChecksums(MD5_BEFORE_510,
					MD5_AFTER_510));
		}
		return md;
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_BEFORE_510)) {
			patchMethodK510(clazz);
			return null;
		} else if (md5.equals(MD5_BEFORE_531)) {
			patchMethod_cO_531(clazz);
			return null;
		} else if (md5.equals(MD5_BEFORE_532)) {
			patchMethod_ON_532(clazz);
			return null;
		}
		return "unsupported MD5: "+md5;
	}

	private void patchMethodK510(BCClass clazz) throws Exception {
		Code c = clazz.getDeclaredMethod("K", new Class[] {int.class}).getCode(false);
		c.before(192);
		c.dup();
		c.iload().setLocal(1);
		c.invokestatic().setMethod(DictionariesPatch.class.getDeclaredMethod("onGetCard", new Class[]{Object.class, int.class}));
		c.calculateMaxLocals();
		c.calculateMaxStack();
		
	}
	
	private void patchMethod_cO_531(BCClass clazz) throws Exception {
		Code c = clazz.getDeclaredMethod("cO", new Class[] {int.class}).getCode(false);
		c.before(189);
		c.dup();
		c.iload().setLocal(1);
		c.invokestatic().setMethod(DictionariesPatch.class.getDeclaredMethod("onGetCard", new Class[]{Object.class, int.class}));
		c.calculateMaxLocals();
		c.calculateMaxStack();
		
	}
	
	private void patchMethod_ON_532(BCClass clazz) throws Exception {
		Code c = clazz.getDeclaredMethod("ON", new Class[] {int.class}).getCode(false);
		c.before(189);
		c.dup();
		c.iload().setLocal(1);
		c.invokestatic().setMethod(DictionariesPatch.class.getDeclaredMethod("onGetCard", new Class[]{Object.class, int.class}));
		c.calculateMaxLocals();
		c.calculateMaxStack();
		
	}
	
	public static void onGetCard(Object o, int currentIndex) {
		if (o != null) {
			Backend.INSTANCE.modifyPanel(o, currentIndex);
		}
	}
	
}
