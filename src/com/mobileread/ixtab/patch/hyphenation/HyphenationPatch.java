package com.mobileread.ixtab.patch.hyphenation;

import java.security.AllPermission;
import java.security.Permission;
import java.util.Map;

import serp.bytecode.BCClass;
import serp.bytecode.BCMethod;
import serp.bytecode.Code;
import serp.bytecode.ConstantInstruction;

import com.amazon.kindle.kindlet.input.keyboard.OnscreenKeyboardUtil;
import com.mobileread.ixtab.jbpatch.Environment;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSetting;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSettings;
import com.mobileread.ixtab.jbpatch.conf.ui.ComboBoxSettingPanel;
import com.mobileread.ixtab.jbpatch.conf.ui.RadioButtonsSettingPanel;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingChangeListener;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingEntry;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingPanel;
import com.mobileread.ixtab.jbpatch.conf.ui.TextSettingPanel;
import com.mobileread.ixtab.patch.hyphenation.common.Factories;
import com.mobileread.ixtab.patch.hyphenation.common.UniversalHyphenationEngine;

public class HyphenationPatch extends Patch {

	private static final String CLASS_HYPHENATIONMANAGER_510 = "com.mobipocket.common.library.reader.hyphenation.j";
	private static final String CLASS_FRAMECONSTRUCTOR_510 = "com.mobipocket.common.library.reader.db";
	
	private static final String CLASS_HYPHENATIONMANAGER_531 = "com.mobipocket.common.library.reader.hyphenation.b";
	private static final String CLASS_FRAMECONSTRUCTOR_531 = "com.mobipocket.common.library.reader.R";

	private static final String CLASS_HYPHENATIONMANAGER_532 = "com.mobipocket.common.library.reader.hyphenation.b";
	private static final String CLASS_FRAMECONSTRUCTOR_532 = "com.mobipocket.common.library.reader.S";

	private static final String CLASS_HYPHENATIONMANAGER_533 = "com.mobipocket.common.library.reader.hyphenation.b";
	private static final String CLASS_FRAMECONSTRUCTOR_533 = "com.mobipocket.common.library.reader.R";

	private static final String CLASS_HYPHENATIONMANAGER_534 = "com.mobipocket.common.library.reader.hyphenation.b";
	private static final String CLASS_FRAMECONSTRUCTOR_534 = "com.mobipocket.common.library.reader.R";

	public static final String MD5_HYPHENATIONMANAGER_510_BEFORE = "630a7704149435140c4ef8406749160d";
	public static final String MD5_HYPHENATIONMANAGER_510_AFTER = "f78ae7d487e2001a998783e673439c72";
	public static final String MD5_FRAMECONSTRUCTOR_510_BEFORE = "cbbba07fab3a4e1be58a9553f8a2e700";
	public static final String MD5_FRAMECONSTRUCTOR_510_AFTER = "efb227b84d4fdf4f58d551634d6fcdb9";

	public static final String MD5_HYPHENATIONMANAGER_531_BEFORE = "1b885f49c4d0acb0220b73498e29ac67";
	public static final String MD5_HYPHENATIONMANAGER_531_AFTER = "d7cd8b9799e225039f3610ae73fc0a84";
	public static final String MD5_FRAMECONSTRUCTOR_531_BEFORE = "66334a7964ec053ac723aaaf7036bca6";
	public static final String MD5_FRAMECONSTRUCTOR_531_AFTER = "d92ba1327e652df1a6f897a3a7ce6b15";

	public static final String MD5_HYPHENATIONMANAGER_532_BEFORE = "db219868c4ef027887d5ff947a3c1ecf";
	public static final String MD5_HYPHENATIONMANAGER_532_AFTER = "fcb02281d501fcf4d97dc0c7cf10bbab";
	public static final String MD5_FRAMECONSTRUCTOR_532_BEFORE = "b8b4fea2c1e5c1670dd4986e5eaa02b2";
	public static final String MD5_FRAMECONSTRUCTOR_532_AFTER = "5bbb373277913837a1824baaee897375";

	public static final String MD5_HYPHENATIONMANAGER_533_BEFORE = "107be2db2ac56d3088ce503459f808d2";
	public static final String MD5_HYPHENATIONMANAGER_533_AFTER = "bcbd58585d3b76c63b54d4973bc1a756";
	public static final String MD5_FRAMECONSTRUCTOR_533_BEFORE = "4b45f0779bb02a22c5c0512640905ae9";
	public static final String MD5_FRAMECONSTRUCTOR_533_AFTER = "f49446a3059d256f97491ced746fbf56";

	public static final String MD5_HYPHENATIONMANAGER_534_BEFORE = "3a697ce0206d79d4e2b798ab1df2709d";
	public static final String MD5_HYPHENATIONMANAGER_534_AFTER = "d9133186bdcd7f494d3d2ae51a1a558f";
	public static final String MD5_FRAMECONSTRUCTOR_534_BEFORE = "7962b9cc9464937fc46b9ca12d6c1cf8";
	public static final String MD5_FRAMECONSTRUCTOR_534_AFTER = "094ea9a31fff3f257ec5819a87c23b50";

	public static final String MODE_JUSTIFY_KEY = "Justify";
	public static final String MODE_DUMB_KEY = "Dumb";
	static final String MODE_SMART_KEY = "Smart";

	private static final int SYLLABLE_LENGTH_MIN = 1;
	private static final int SYLLABLE_LENGTH_MAX = 10;
	private static final int SYLLABLE_LENGTH_DEFAULT = 3;

	private static final String CONF_MODE_KEY = "mode";
	private static final String CONF_MODE_DEFAULT = MODE_SMART_KEY;
	private static final String CONF_MODE_I18N_NAME = "mode.name";
	private static final String CONF_MODE_I18N_DESC = "mode.description";
	private static final String CONF_MODE_I18N_HINT = "mode.hint";

	private static final String CONF_MODE_JUSTIFY_LONG_I18N = "mode.justify";
	private static final String CONF_MODE_DUMB_LONG_I18N = "mode.dumb";
	private static final String CONF_MODE_SMART_LONG_I18N = "mode.smart";

	private static final String CONF_MODE_JUSTIFY_SHORT_I18N = "mode.justify.short";
	private static final String CONF_MODE_DUMB_SHORT_I18N = "mode.dumb.short";
	private static final String CONF_MODE_SMART_SHORT_I18N = "mode.smart.short";

	private static final String CONF_MINPREFIX_KEY = "minprefix";
	private static final String CONF_MINPREFIX_I18N_NAME = "minprefix.name";
	private static final String CONF_MINPREFIX_I18N_DESC = "minprefix.description";
	private static final String CONF_MINPREFIX_I18N_HINT = "minprefix.hint";

	private static final String CONF_MINSUFFIX_KEY = "minsuffix";
	private static final String CONF_MINSUFFIX_I18N_NAME = "minsuffix.name";
	private static final String CONF_MINSUFFIX_I18N_DESC = "minsuffix.description";
	private static final String CONF_MINSUFFIX_I18N_HINT = "minsuffix.hint";
	
	private static final String CONF_RAGGED_KEY = "ragged";
	private static final String CONF_RAGGED_I18N_NAME = "ragged.name";
	private static final String CONF_RAGGED_I18N_DESC = "ragged.description";
	private static final String CONF_RAGGED_I18N_HINT = "ragged.hint";
	
	private static final String CONF_RAGGED_VALUE_ALWAYS = "ragged.always";
	private static final String CONF_RAGGED_VALUE_NEVER = "ragged.never";
	private static final String CONF_RAGGED_DEFAULT = CONF_RAGGED_VALUE_NEVER;
	

	public static HyphenationPatch INSTANCE;

	public HyphenationPatch() {
		INSTANCE = this;
	}

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

	public PatchMetadata getMetadata() {
		PatchMetadata md = new PatchMetadata(this);
		String factoryClassName = Factories.INSTANCE.getClass().getName();
		if (factoryClassName.endsWith("512")) {
			md.withClass(
					new PatchableClass(CLASS_FRAMECONSTRUCTOR_510)
							.withChecksums(MD5_FRAMECONSTRUCTOR_510_BEFORE,
									MD5_FRAMECONSTRUCTOR_510_AFTER))
			.withClass(
					new PatchableClass(CLASS_HYPHENATIONMANAGER_510)
							.withChecksums(
									MD5_HYPHENATIONMANAGER_510_BEFORE,
									MD5_HYPHENATIONMANAGER_510_AFTER));
		} else if (factoryClassName.endsWith("531")) {
			md.withClass(
					new PatchableClass(CLASS_FRAMECONSTRUCTOR_531)
							.withChecksums(MD5_FRAMECONSTRUCTOR_531_BEFORE,
									MD5_FRAMECONSTRUCTOR_531_AFTER))
			.withClass(
					new PatchableClass(CLASS_HYPHENATIONMANAGER_531)
							.withChecksums(
									MD5_HYPHENATIONMANAGER_531_BEFORE,
									MD5_HYPHENATIONMANAGER_531_AFTER));
		} else if (factoryClassName.endsWith("532")) {
			md.withClass(
					new PatchableClass(CLASS_FRAMECONSTRUCTOR_532)
							.withChecksums(MD5_FRAMECONSTRUCTOR_532_BEFORE,
									MD5_FRAMECONSTRUCTOR_532_AFTER))
			.withClass(
					new PatchableClass(CLASS_HYPHENATIONMANAGER_532)
							.withChecksums(
									MD5_HYPHENATIONMANAGER_532_BEFORE,
									MD5_HYPHENATIONMANAGER_532_AFTER));
		} else if (factoryClassName.endsWith("533")) {
			md.withClass(
					new PatchableClass(CLASS_FRAMECONSTRUCTOR_533)
							.withChecksums(MD5_FRAMECONSTRUCTOR_533_BEFORE,
									MD5_FRAMECONSTRUCTOR_533_AFTER))
			.withClass(
					new PatchableClass(CLASS_HYPHENATIONMANAGER_533)
							.withChecksums(
									MD5_HYPHENATIONMANAGER_533_BEFORE,
									MD5_HYPHENATIONMANAGER_533_AFTER));
		} else if (factoryClassName.endsWith("534")) {
			md.withClass(
					new PatchableClass(CLASS_FRAMECONSTRUCTOR_534)
							.withChecksums(MD5_FRAMECONSTRUCTOR_534_BEFORE,
									MD5_FRAMECONSTRUCTOR_534_AFTER))
			.withClass(
					new PatchableClass(CLASS_HYPHENATIONMANAGER_534)
							.withChecksums(
									MD5_HYPHENATIONMANAGER_534_BEFORE,
									MD5_HYPHENATIONMANAGER_534_AFTER));
		}
		return md;
	}

	protected void initLocalization(String locale, Map map) {
		if (RESOURCE_ID_ENGLISH.equals(locale)) {
			map.put(I18N_JBPATCH_NAME, "Fix Reader Layout and Hyphenation");
			map.put(I18N_JBPATCH_DESCRIPTION,
					"This patch fixes the layout of the E-Book reader, and allows for hyphenation.");
			
			map.put(CONF_MODE_I18N_NAME, "Adjustment mode");
			map.put(CONF_MODE_I18N_DESC,
					"Select how you want the reader behavior to be modified.");
			map.put(CONF_MODE_I18N_HINT,
					"If you are having trouble with hyphenation, ensure that the book has the correct language set, and that hyphenation rules for that language are available. Please refer to the patch documentation on the Wiki page for further information.");

			map.put(CONF_MODE_JUSTIFY_LONG_I18N,
					"Justify: only justify text to block layout");
			map.put(CONF_MODE_DUMB_LONG_I18N,
					"Dumb: justification and dumb hyphenation");
			map.put(CONF_MODE_SMART_LONG_I18N,
					"Smart: justification and language-specific hyphenation");

			map.put(CONF_MODE_JUSTIFY_SHORT_I18N, MODE_JUSTIFY_KEY);
			map.put(CONF_MODE_DUMB_SHORT_I18N, MODE_DUMB_KEY);
			map.put(CONF_MODE_SMART_SHORT_I18N, MODE_SMART_KEY);

			map.put(CONF_MINPREFIX_I18N_NAME, "Minimal First Syllable Length");
			map.put(CONF_MINPREFIX_I18N_DESC,
					"Minimum number of leading characters before a word is hyphenated");
			map.put(CONF_MINPREFIX_I18N_HINT,
					"This determines the minimum amount of characters that the first portion of a hyphenated word must contain. In other words, no word will be hyphenated unless the first part of the hyphenated word contains at least that many characters. Acceptable values for this setting are between 1 and 10.");

			map.put(CONF_MINSUFFIX_I18N_NAME, "Minimal Last Syllable Length");
			map.put(CONF_MINSUFFIX_I18N_DESC,
					"Minimum number of trailing characters when a word is hyphenated");
			map.put(CONF_MINSUFFIX_I18N_HINT,
					"This determines the minimum amount of characters that the last portion of a hyphenated word must contain. In other words, no word will be hyphenated unless the last part of the hyphenated word contains at least that many characters. Acceptable values for this setting are between 1 and 10.");
			
			map.put(CONF_RAGGED_I18N_NAME, "Ragged justification");
			map.put(CONF_RAGGED_I18N_DESC,
					"Allow some lines to be left-aligned, instead of fully justified");
			map.put(CONF_RAGGED_I18N_HINT,
					"Regardless of your justification and hyphenation settings, some lines cannot be layed out perfectly. If you prefer to see such lines left-aligned, instead of justified, check this option.");
			map.put(CONF_RAGGED_VALUE_ALWAYS, "Allow left-aligned lines");
			map.put(CONF_RAGGED_VALUE_NEVER, "Force justified lines");
		}
	}

	protected ConfigurableSettings initConfigurableSettings() {
		ConfigurableSettings map = new ConfigurableSettings();
		map.add(new ModeSetting());
		map.add(new LengthSetting(localize(CONF_MINPREFIX_I18N_NAME),
				localize(CONF_MINPREFIX_I18N_DESC),
				localize(CONF_MINPREFIX_I18N_HINT), CONF_MINPREFIX_KEY,
				SYLLABLE_LENGTH_DEFAULT + ""));
		map.add(new LengthSetting(localize(CONF_MINSUFFIX_I18N_NAME),
				localize(CONF_MINSUFFIX_I18N_DESC),
				localize(CONF_MINSUFFIX_I18N_HINT), CONF_MINSUFFIX_KEY,
				SYLLABLE_LENGTH_DEFAULT + ""));
		
		map.add(new LayoutSetting(localize(CONF_RAGGED_I18N_NAME), localize(CONF_RAGGED_I18N_DESC), localize(CONF_RAGGED_I18N_HINT), CONF_RAGGED_KEY, CONF_RAGGED_DEFAULT));
		
//		map.add(new FeaturesSetting(localize(CONF_FEATURES_I18N_NAME), localize(CONF_FEATURES_I18N_DESC), localize(CONF_FEATURES_I18N_HINT), CONF_FEATURES_KEY, "3"));
		return map;
	}

	public int getMinimumFirstSyllableLength() {
		int length = SYLLABLE_LENGTH_DEFAULT;
		try {
			length = Integer.parseInt(getConfigured(CONF_MINPREFIX_KEY));
		} catch (Throwable t) {
		}
		;
		return length;
	}

	public int getMinimumLastSyllableLength() {
		int length = SYLLABLE_LENGTH_DEFAULT;
		try {
			length = Integer.parseInt(getConfigured(CONF_MINSUFFIX_KEY));
		} catch (Throwable t) {
		}
		;
		return length;
	}

	public String getMode() {
		return getConfigured(CONF_MODE_KEY);
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_HYPHENATIONMANAGER_510_BEFORE)) {
			return patchHyphenationManager510(clazz);
		}
		else if (md5.equals(MD5_FRAMECONSTRUCTOR_510_BEFORE)) {
			return patchFrameConstructor510(clazz);
		}
		else if (md5.equals(MD5_HYPHENATIONMANAGER_531_BEFORE)) {
			return patchHyphenationManager53X(clazz, "YV");
		}
		else if (md5.equals(MD5_HYPHENATIONMANAGER_532_BEFORE)) {
			return patchHyphenationManager53X(clazz, "dX");
		}
		else if (md5.equals(MD5_HYPHENATIONMANAGER_533_BEFORE)) {
			return patchHyphenationManager53X(clazz, "dX");
		}
		else if (md5.equals(MD5_HYPHENATIONMANAGER_534_BEFORE)) {
			return patchHyphenationManager53X(clazz, "ox");
		}
		else if (md5.equals(MD5_FRAMECONSTRUCTOR_531_BEFORE)) {
			return patchFrameConstructor53X(clazz, "KDA", "mbA");
		}
		else if (md5.equals(MD5_FRAMECONSTRUCTOR_532_BEFORE)) {
			return patchFrameConstructor53X(clazz, "hBA", "rdA");
		}
		else if (md5.equals(MD5_FRAMECONSTRUCTOR_533_BEFORE)) {
			return patchFrameConstructor53X(clazz, "hBA", "rdA");
		}
		else if (md5.equals(MD5_FRAMECONSTRUCTOR_534_BEFORE)) {
			return patchFrameConstructor53X(clazz, "TBA", "ibA");
		}
		return "Unexpected error: unknown MD5 " + md5;
	}

	public Permission[] getRequiredPermissions() {
		return new Permission[] { new AllPermission() };
	}
	
	private String patchFrameConstructor510(BCClass clazz) throws Throwable {
		BCMethod m = clazz.getDeclaredMethod("e");
		Code c = m.getCode(false);
		
		c.before(47);
		// percentage of a word's width that must be available in the current
		// line, for it to be considered for hyphenation
		// original is 25%
		((ConstantInstruction) c.next()).setValue(-1);

		if (!CONF_RAGGED_VALUE_ALWAYS.equals(getConfigured(CONF_RAGGED_KEY))) {
			// this fixes the weird behavior that some lines do not get justified
			// it essentially always returns a "false" value for the following
			// condition
			// (by turning it into "if (0 > 1)")
			c = clazz.getDeclaredMethod("L").getCode(false);
			c.before(45);
			c.pop();
			c.pop();
			c.constant().setValue(0);
			c.constant().setValue(1);
		}
		return null;
	}

	private String patchFrameConstructor53X(BCClass clazz, String method1, String method2) throws Throwable {
		BCMethod m = clazz.getDeclaredMethod(method1);
		Code c = m.getCode(false);
		c.before(50);
		// percentage of a word's width that must be available in the current
		// line, for it to be considered for hyphenation
		// original is 25%
		((ConstantInstruction) c.next()).setValue(-1);

		if (!CONF_RAGGED_VALUE_ALWAYS.equals(getConfigured(CONF_RAGGED_KEY))) {
			// this fixes the weird behavior that some lines do not get justified
			// it essentially always returns a "false" value for the following
			// condition
			// (by turning it into "if (0 > 1)")
			c = clazz.getDeclaredMethod(method2).getCode(false);
			c.before(49);
			c.pop();
			c.pop();
			c.constant().setValue(0);
			c.constant().setValue(1);
		}
		return null;
	}
	
	private String patchHyphenationManager510(BCClass clazz) throws Throwable {

		BCMethod m = clazz.getDeclaredMethod("D", new String[] { "int" });
		Code c = m.getCode(false);

		c.beforeFirst();
		c.iload().setLocal(1);
		c.invokestatic().setMethod(
				UniversalHyphenationEngine.class.getMethod("configure",
						new Class[] { int.class }));
		c.areturn();

		return null;

	}

	private String patchHyphenationManager53X(BCClass clazz, String methodName) throws Throwable {

		BCMethod m = clazz.getDeclaredMethod(methodName, new String[] { "int" });
		Code c = m.getCode(false);

		c.beforeFirst();
		c.iload().setLocal(1);
		c.invokestatic().setMethod(
				UniversalHyphenationEngine.class.getMethod("configure",
						new Class[] { int.class }));
		c.areturn();

		return null;

	}

	private class ModeSetting extends ConfigurableSetting {
		private final SettingEntry[] entries;

		public ModeSetting() {
			super(localize(CONF_MODE_I18N_NAME), localize(CONF_MODE_I18N_DESC),
					localize(CONF_MODE_I18N_HINT), CONF_MODE_KEY,
					CONF_MODE_DEFAULT);
			entries = new SettingEntry[3];
			entries[0] = new SettingEntry(MODE_JUSTIFY_KEY,
					localize(CONF_MODE_JUSTIFY_LONG_I18N));
			entries[1] = new SettingEntry(MODE_DUMB_KEY,
					localize(CONF_MODE_DUMB_LONG_I18N));
			entries[2] = new SettingEntry(MODE_SMART_KEY,
					localize(CONF_MODE_SMART_LONG_I18N));
		}

		public SettingPanel getPanel(SettingChangeListener listener) {
			return new ComboBoxSettingPanel(listener, entries);
		}

		public boolean isValid(String v) {
			for (int i = 0; i < entries.length; ++i) {
				if (entries[i].key.equals(v)) {
					return true;
				}
			}
			return false;
		}

		public String getLocalized(String value) {
			if (value.equals(MODE_JUSTIFY_KEY)) {
				return localize(CONF_MODE_JUSTIFY_SHORT_I18N);
			}
			if (value.equals(MODE_DUMB_KEY)) {
				return localize(CONF_MODE_DUMB_SHORT_I18N);
			}
			if (value.equals(MODE_SMART_KEY)) {
				return localize(CONF_MODE_SMART_SHORT_I18N);
			}
			return value;
		}
	}

	private static class LengthSetting extends ConfigurableSetting {

		public LengthSetting(String name, String description, String hint,
				String key, String defaultValue) {
			super(name, description, hint, key, defaultValue);
		}

		public SettingPanel getPanel(SettingChangeListener listener) {
			return new TextSettingPanel(listener,
					OnscreenKeyboardUtil.KEYBOARD_MODE_NUMBERS_AND_SYMBOLS,
					true);
		}

		public boolean isValid(String value) {
			if (value == null || value.length() < 1) {
				return false;
			}
			try {
				int i = Integer.parseInt(value);
				return i >= SYLLABLE_LENGTH_MIN && i <= SYLLABLE_LENGTH_MAX;
			} catch (Throwable t) {
			}
			return false;
		}

	}
	
	private class LayoutSetting extends ConfigurableSetting {

		private final SettingEntry[] entries;
		public LayoutSetting(String name, String description, String hint,
				String key, String defaultValue) {
			super(localize(name), localize(description), localize(hint), key , defaultValue);
			entries = new SettingEntry[2];
			entries[0] = new SettingEntry(CONF_RAGGED_VALUE_ALWAYS, localize(CONF_RAGGED_VALUE_ALWAYS));
			entries[1] = new SettingEntry(CONF_RAGGED_VALUE_NEVER, localize(CONF_RAGGED_VALUE_NEVER));
		}

		public SettingPanel getPanel(SettingChangeListener listener) {
			return new RadioButtonsSettingPanel(listener, entries);
		}

		public boolean isValid(String value) {
			return CONF_RAGGED_VALUE_ALWAYS.equals(value) || CONF_RAGGED_VALUE_NEVER.equals(value);
		}

		public String getLocalized(String value) {
			for (int i=0; i < entries.length; ++i) {
				if (entries[i].key.equals(value)) {
					return entries[i].displayValue;
				}
			}
			return value;
		}
		
		
	}
}
