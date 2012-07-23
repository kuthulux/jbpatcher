package com.mobileread.ixtab.patch.tts;

import java.util.Map;

import serp.bytecode.BCClass;
import serp.bytecode.Code;
import serp.bytecode.ConstantInstruction;
import serp.bytecode.lowlevel.Entry;
import serp.bytecode.lowlevel.UTF8Entry;

import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;

public class TTSPatch extends Patch {

	public static final int PATCH_VERSION = 20120723;

	private static final String KEY_MALE = "male";
	private static final String KEY_FEMALE = "female";

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

	protected void initLocalization(String id, Map m) {
		if ("en".equals(id)) {
			m.put(I18N_JBPATCH_NAME, "Enable Text-to-Speech");
			m.put(I18N_JBPATCH_DESCRIPTION, "Unconditionally enable Text-to-Speech, regardless of language and DRM.");
			m.put(KEY_FEMALE, ORIGINAL_EN[0]);
			m.put(KEY_MALE, ORIGINAL_EN[1]);
		} else if ("de".equals(id)) {
			m.put(I18N_JBPATCH_NAME, "Text-to-Speech einschalten");
			m.put(I18N_JBPATCH_DESCRIPTION, "Text-to-Speech immer einschalten, unabhängig von der Sprache und DRM.");
			m.put(KEY_FEMALE, "Weiblich (englisch)");
			m.put(KEY_MALE, "Männlich (englisch)");
		} else if ("es".equals(id)) {
			m.put(I18N_JBPATCH_NAME, "Activar Texto a voz");
			m.put(KEY_FEMALE, "Femenina (inglés)");
			m.put(KEY_MALE, "Masculina (inglés)");
		} else if ("fr".equals(id)) {
			m.put(I18N_JBPATCH_NAME, "Activer la synth\350se vocale");
			m.put(KEY_FEMALE, "Femme (anglais)");
			m.put(KEY_MALE, "Homme (anglais)");
		} else if ("it".equals(id)) {
			m.put(I18N_JBPATCH_NAME, "Attiva Da Testo a Voce");
			m.put(KEY_FEMALE, "Femminile (inglese)");
			m.put(KEY_MALE, "Maschile (inglese)");
		} else if ("pt".equals(id)) {
			m.put(I18N_JBPATCH_NAME, "Ativar texto-voz");
			m.put(KEY_FEMALE, "Femino (inglês)");
			m.put(KEY_MALE, "Masculino (inglês)");
		}
	}

	public int getVersion() {
		return PATCH_VERSION;
	}

	public PatchMetadata getMetadata() {
		PatchMetadata meta = new PatchMetadata(this);
		fillMetadata(meta);
		return meta;
	}

	private void fillMetadata(PatchMetadata pd) {
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
		pd.withClass(new PatchableClass("com.amazon.ebook.booklet.reader.plugin.tts.TTSProvider$TTSAction").withChecksums(MD5_TTSACTION, "FIXME"));
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
		String[] replacement = new String[] { localize(KEY_FEMALE),
				localize(KEY_MALE) };
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
