package com.mobileread.ixtab.patch.tts;

import java.util.Locale;
import java.util.Map;

import serp.bytecode.BCClass;
import serp.bytecode.Code;
import serp.bytecode.ConstantInstruction;
import serp.bytecode.lowlevel.Entry;
import serp.bytecode.lowlevel.UTF8Entry;

import com.amazon.kindle.kindlet.input.keyboard.OnscreenKeyboardUtil;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSetting;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSettings;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingChangeListener;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingPanel;
import com.mobileread.ixtab.jbpatch.conf.ui.TextSettingPanel;

public class TTSPatch extends Patch {

	private static final String KEY_MALE = "male";
	private static final String KEY_FEMALE = "female";

	public static final String MD5_EN_BEFORE = "c92781d51b2ad1951e2c6d5279afe6d5";
	public static final String MD5_DE_BEFORE = "cd9041a3105c19c2de0f61dd012872d3";
	public static final String MD5_ES_BEFORE = "af8a8c3d465f54c79d7b1bc51fed018a";
	public static final String MD5_FR_BEFORE = "17a930c0e35f606ce087a468bbe3da31";
	public static final String MD5_IT_BEFORE = "1458ab0b2c750163f7f51b28cba26306";
	public static final String MD5_PT_BEFORE = "4e2abf106695447de436b6b4bc2ccad9";

	public static final String MD5_EN_AFTER = "5d57fed161e28bcf074dc9da75074a67";
	public static final String MD5_DE_AFTER = "cd9041a3105c19c2de0f61dd012872d3";
	public static final String MD5_ES_AFTER = "?";
	public static final String MD5_FR_AFTER = "?";
	public static final String MD5_IT_AFTER = "?";
	public static final String MD5_PT_AFTER = "?";

	public static final String MD5_TTSACTION_BEFORE = "7342c90af8a837f4632d62d74ea86242";
	public static final String MD5_TTSACTION_AFTER = "?";

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
	
	private static final String UI_HINT_M = "male.hint";
	private static final String UI_NAME_M = "male.name";
	private static final String UI_DESC_M = "male.desc";

	private static final String UI_HINT_F = "female.hint";
	private static final String UI_NAME_F = "female.name";
	private static final String UI_DESC_F = "female.desc";

	public int getVersion() {
		return 20120803;
	}

	protected void initLocalization(String id, Map m) {
		if ("en".equals(id)) {
			m.put(I18N_JBPATCH_NAME, "Customize Text-to-Speech");
			m.put(I18N_JBPATCH_DESCRIPTION, "This patch unconditionally enables Text-to-Speech (regardless of language or DRM), and allows to customize the description of the voices.");
			m.put(KEY_FEMALE, ORIGINAL_EN[0]);
			m.put(KEY_MALE, ORIGINAL_EN[1]);
			
			m.put(UI_NAME_F, "Female Speaker");
			m.put(UI_DESC_F, "Description of the female voice.");
			m.put(UI_HINT_F, "This is the text that is shown for the female voice in the Text-to-Speech Options.");
			m.put(UI_NAME_M, "Male Speaker");
			m.put(UI_DESC_M, "Description of the male voice.");
			m.put(UI_HINT_M, "This is the text that is shown for the male voice in the Text-to-Speech Options.");
		}
	}

	public PatchMetadata getMetadata() {
		PatchMetadata meta = new PatchMetadata(this);
		fillMetadata(meta);
		return meta;
	}

	private void fillMetadata(PatchMetadata pd) {
		pd.withClass(new PatchableClass(
				"com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources")
				.withChecksums(MD5_EN_BEFORE, MD5_EN_AFTER));
		pd.withClass(new PatchableClass(
				"com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources_de")
				.withChecksums(MD5_DE_BEFORE, MD5_DE_AFTER));
		pd.withClass(new PatchableClass(
				"com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources_es")
				.withChecksums(MD5_ES_BEFORE, MD5_ES_AFTER));
		pd.withClass(new PatchableClass(
				"com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources_fr")
				.withChecksums(MD5_FR_BEFORE, MD5_FR_AFTER));
		pd.withClass(new PatchableClass(
				"com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources_it")
				.withChecksums(MD5_IT_BEFORE, MD5_IT_AFTER));
		pd.withClass(new PatchableClass(
				"com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources_pt")
				.withChecksums(MD5_PT_BEFORE, MD5_PT_AFTER));
		pd.withClass(new PatchableClass("com.amazon.ebook.booklet.reader.plugin.tts.TTSProvider$TTSAction").withChecksums(MD5_TTSACTION_BEFORE, MD5_TTSACTION_AFTER));
	}

	
	protected ConfigurableSettings initConfigurableSettings() {
		ConfigurableSettings settings = new ConfigurableSettings();
		String[] original = determineOriginalValues(Locale.getDefault());
		settings.add(new DescriptionSetting(UI_NAME_F, UI_DESC_F, UI_HINT_F, KEY_FEMALE, original[0]));
		settings.add(new DescriptionSetting(UI_NAME_M, UI_DESC_M, UI_HINT_M, KEY_MALE, original[1]));
		return settings;
	}

	private String[] determineOriginalValues(Locale l) {
		String s = l.toString();
		if (s.startsWith("de")) {
			return ORIGINAL_DE;
		} else if (s.startsWith("es")) {
			return ORIGINAL_ES;
		} else if (s.startsWith("fr")) {
			return ORIGINAL_FR;
		} else if (s.startsWith("it")) {
			return ORIGINAL_IT;
		} else if (s.startsWith("pt")) {
			return ORIGINAL_PT;
		}
		return ORIGINAL_EN;
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_EN_BEFORE))
			return patchDescription(clazz, ORIGINAL_EN);
		if (md5.equals(MD5_DE_BEFORE))
			return patchDescription(clazz, ORIGINAL_DE);
		if (md5.equals(MD5_ES_BEFORE))
			return patchDescription(clazz, ORIGINAL_ES);
		if (md5.equals(MD5_FR_BEFORE))
			return patchDescription(clazz, ORIGINAL_FR);
		if (md5.equals(MD5_IT_BEFORE))
			return patchDescription(clazz, ORIGINAL_IT);
		if (md5.equals(MD5_PT_BEFORE))
			return patchDescription(clazz, ORIGINAL_PT);
		if (md5.equals(MD5_TTSACTION_BEFORE))
			return patchTTSAction(clazz);
		return "unexpected error: unsupported MD5 " + md5;
	}

	private String patchDescription(BCClass clazz, String[] original) {
		String[] replacement = new String[] { getConfigured(KEY_FEMALE),
				getConfigured(KEY_MALE) };
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

	private class DescriptionSetting extends ConfigurableSetting {

		public DescriptionSetting(String name, String description,
				String hint, String key, String defaultValue) {
			super(localize(name), localize(description), localize(hint), key, defaultValue);
		}

		public SettingPanel getPanel(SettingChangeListener listener) {
			return new TextSettingPanel(listener, OnscreenKeyboardUtil.KEYBOARD_MODE_INIT_CAP_ONCE, true);
		}

		public boolean isValid(String value) {
			return value.trim().length() >= 1;
		}
		
	}
}
