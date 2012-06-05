package com.mobileread.ixtab.patch.tts;

import java.net.URL;
import java.security.AllPermission;
import java.security.Permission;

import serp.bytecode.BCClass;
import serp.bytecode.Code;
import serp.bytecode.ConstantInstruction;
import serp.bytecode.lowlevel.Entry;
import serp.bytecode.lowlevel.UTF8Entry;

import com.mobileread.ixtab.jbpatch.KindleDevice;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableDevice;

public class TTSPatch extends Patch {

	public static final int PATCH_VERSION = 20120605;

	public static final String MD5_EN = "c92781d51b2ad1951e2c6d5279afe6d5";
	public static final String MD5_DE = "cd9041a3105c19c2de0f61dd012872d3";
	public static final String MD5_ES = "af8a8c3d465f54c79d7b1bc51fed018a";
	public static final String MD5_FR = "17a930c0e35f606ce087a468bbe3da31";
	public static final String MD5_IT = "1458ab0b2c750163f7f51b28cba26306";
	public static final String MD5_PT = "4e2abf106695447de436b6b4bc2ccad9";

	public static final String MD5_TTSACTION = "7342c90af8a837f4632d62d74ea86242";

	private static final String[] ORIGINAL_EN = new String[] { "Female", "Male" };
	private static final String[] ORIGINAL_DE = new String[] { "Weiblich",
			"Männlich" };
	private static final String[] ORIGINAL_ES = new String[] { "Femenina",
			"Masculina" };
	private static final String[] ORIGINAL_FR = new String[] { "Femme", "Homme" };
	private static final String[] ORIGINAL_IT = new String[] { "Femminile",
			"Maschile" };
	private static final String[] ORIGINAL_PT = new String[] { "Femino",
			"Masculino" };

	public String getPatchName() {
		return "Enable Text-to-Speech";
	}

	protected int getPatchVersion() {
		return PATCH_VERSION;
	}

	public PatchMetadata getMetadata() {
		PatchableDevice pd = new PatchableDevice(KindleDevice.KT_510_1557760049);
		pd.withClass(new PatchableClass(
				"com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources")
				.withChecksums(MD5_EN, "FIXME"));
		pd.withClass(new PatchableClass(
				"com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources_de")
				.withChecksums(MD5_DE, "FIXME"));
		pd.withClass(new PatchableClass(
				"com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources_es")
				.withChecksums(MD5_ES, "FIXME"));
		pd.withClass(new PatchableClass(
				"com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources_fr")
				.withChecksums(MD5_FR, "FIXME"));
		pd.withClass(new PatchableClass(
				"com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources_it")
				.withChecksums(MD5_IT, "FIXME"));
		pd.withClass(new PatchableClass(
				"com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources_pt")
				.withChecksums(MD5_PT, "FIXME"));
		return new PatchMetadata(this).withDevice(pd);
	}

	// FIXME
	public Permission[] getRequiredPermissions() {
		return new Permission[] { new AllPermission() };
	}

	public int getResourceRequirements() {
		return RESOURCE_REQUIREMENT_LOCALIZATION;
	}

	protected URL getResourcesUrl() {
		return getClass().getResource("/ttspatch.txt");
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_EN))
			return patchDescription(clazz, ORIGINAL_EN);
		if (md5.equals(MD5_DE))
			return patchDescription(clazz, ORIGINAL_DE);
		if (md5.equals(MD5_ES))
			return patchDescription(clazz, ORIGINAL_ES);
		if (md5.equals(MD5_FR))
			return patchDescription(clazz, ORIGINAL_FR);
		if (md5.equals(MD5_IT))
			return patchDescription(clazz, ORIGINAL_IT);
		if (md5.equals(MD5_PT))
			return patchDescription(clazz, ORIGINAL_PT);
		if (md5.equals(MD5_TTSACTION))
			return patchTTSAction(clazz);
		return "unexpected error: unsupported MD5 " + md5;
	}

	private String patchDescription(BCClass clazz, String[] original) {
		String[] replacement = new String[] { localize("female"),
				localize("male") };
		Entry[] entries = clazz.getPool().getEntries();
		for (int e = 0; e < entries.length; ++e) {
			if (entries[e] instanceof UTF8Entry) {
				UTF8Entry entry = (UTF8Entry) entries[e];
				for (int r = 0; r < original.length; ++r) {
					if (original[r].equals(entry.getValue())) {
						entry.setValue(replacement[r]);
						break;
					}
				}
			}
		}
		return null;
	}

	private String patchTTSAction(BCClass clazz) throws Throwable {
		Code c = clazz.getDeclaredMethod("<init>").getCode(false);

		c.after(22);
		((ConstantInstruction) c.next()).setValue(true);

		c.calculateMaxLocals();
		c.calculateMaxStack();
		return null;
	}

}
