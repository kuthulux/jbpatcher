package com.mobileread.ixtab.patch.dictionaries;

import java.security.AllPermission;
import java.security.Permission;
import java.util.Map;

import serp.bytecode.BCClass;
import serp.bytecode.Code;

import com.mobileread.ixtab.jbpatch.Environment;
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

	private static final String CLASS_533 = "com.amazon.ebook.booklet.reader.plugin.systemcards.G";
	public static final String MD5_BEFORE_533 = "f98fb6be90c495368441f88e492fbc73";
	private static final String MD5_AFTER_533 = "af5785909e701eb8acbbf659a621c15d";

	private static final String CLASS_534 = "com.amazon.ebook.booklet.reader.plugin.systemcards.H";
	public static final String MD5_BEFORE_534 = "dad443c4fb8cbe43214b04a791c2e85e";
	private static final String MD5_AFTER_534 = "8c9f325b5320f5cb7a93c69f95ed0a1f";

	public int getVersion() {
		return 20130413;
	}
	
	public boolean isAvailable() {
		if (Environment.getJBPatchVersionDate() < 20130328) {
			return false;
		}
		String fw = Environment.getFirmware();
		if ("5.1.0".equals(fw)) return true;
		if ("5.3.1".equals(fw)) return true;
		if ("5.3.2".equals(fw)) return true;
		if ("5.3.3".equals(fw)) return true;
		if ("5.3.4".equals(fw)) return true;
		return false;
	}



	static String getFirmware() {
		String fw = Environment.getFirmware();
		if ("5.1.0".equals(fw)) return "512";
		if ("5.3.1".equals(fw)) return "531";
		if ("5.3.2".equals(fw)) return "532";
		if ("5.3.3".equals(fw)) return "533";
		if ("5.3.4".equals(fw)) return "534";
		return "unknown";
	}

	public Permission[] getRequiredPermissions() {
		return new Permission[] { new AllPermission() };
	}

	protected void initLocalization(String locale, Map map) {
		if (RESOURCE_ID_ENGLISH.equals(locale)) {
			map.put(I18N_JBPATCH_NAME, "Enhance Dictionary Lookup");
			map.put(I18N_JBPATCH_DESCRIPTION,
					"This patch allows to consult all dictionaries, instead of only one.");
		}
	}

	public PatchMetadata getMetadata() {
		PatchMetadata md = new PatchMetadata(this);
		String fw = getFirmware();
		if (fw.equals("534")) {
		md.withClass(new PatchableClass(CLASS_534).withChecksums(
				MD5_BEFORE_534, MD5_AFTER_534));
		} else if (fw.equals("533")) {
			md.withClass(new PatchableClass(CLASS_533).withChecksums(
					MD5_BEFORE_533, MD5_AFTER_533));
		} else if (fw.equals("532")) {
				md.withClass(new PatchableClass(CLASS_532).withChecksums(
						MD5_BEFORE_532, MD5_AFTER_532));
		} else if (fw.equals("531")) {
			md.withClass(new PatchableClass(CLASS_531).withChecksums(
					MD5_BEFORE_531, MD5_AFTER_531));
		} else if (fw.equals("512")) {
			md.withClass(new PatchableClass(CLASS_510).withChecksums(
					MD5_BEFORE_510, MD5_AFTER_510));
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
		} else if (md5.equals(MD5_BEFORE_533)) {
			patchMethod_zo_533(clazz);
			return null;
		} else if (md5.equals(MD5_BEFORE_534)) {
			patchMethod_So_534(clazz);
			return null;
		}
		return "unsupported MD5: " + md5;
	}

	private void patchMethodK510(BCClass clazz) throws Exception {
		Code c = clazz.getDeclaredMethod("K", new Class[] { int.class })
				.getCode(false);
		c.before(192);
		c.dup();
		c.iload().setLocal(1);
		c.invokestatic().setMethod(
				DictionariesPatch.class.getDeclaredMethod("onGetCard",
						new Class[] { Object.class, int.class }));
		c.calculateMaxLocals();
		c.calculateMaxStack();

	}

	private void patchMethod_cO_531(BCClass clazz) throws Exception {
		Code c = clazz.getDeclaredMethod("cO", new Class[] { int.class })
				.getCode(false);
		c.before(189);
		c.dup();
		c.iload().setLocal(1);
		c.invokestatic().setMethod(
				DictionariesPatch.class.getDeclaredMethod("onGetCard",
						new Class[] { Object.class, int.class }));
		c.calculateMaxLocals();
		c.calculateMaxStack();

	}

	private void patchMethod_ON_532(BCClass clazz) throws Exception {
		Code c = clazz.getDeclaredMethod("ON", new Class[] { int.class })
				.getCode(false);
		c.before(189);
		c.dup();
		c.iload().setLocal(1);
		c.invokestatic().setMethod(
				DictionariesPatch.class.getDeclaredMethod("onGetCard",
						new Class[] { Object.class, int.class }));
		c.calculateMaxLocals();
		c.calculateMaxStack();

	}

	private void patchMethod_zo_533(BCClass clazz) throws Exception {
		Code c = clazz.getDeclaredMethod("zo", new Class[] { int.class })
				.getCode(false);
		c.before(189);
		c.dup();
		c.iload().setLocal(1);
		c.invokestatic().setMethod(
				DictionariesPatch.class.getDeclaredMethod("onGetCard",
						new Class[] { Object.class, int.class }));
		c.calculateMaxLocals();
		c.calculateMaxStack();

	}

	private void patchMethod_So_534(BCClass clazz) throws Exception {
		Code c = clazz.getDeclaredMethod("So", new Class[] { int.class })
				.getCode(false);
		c.before(173);
		c.dup();
		c.iload().setLocal(1);
		c.invokestatic().setMethod(
				DictionariesPatch.class.getDeclaredMethod("onGetCard",
						new Class[] { Object.class, int.class }));
		c.calculateMaxLocals();
		c.calculateMaxStack();

	}

	public static void onGetCard(Object o, int currentIndex) {
		if (o != null) {
			Backend.INSTANCE.modifyPanel(o, currentIndex);
		}
	}

}
