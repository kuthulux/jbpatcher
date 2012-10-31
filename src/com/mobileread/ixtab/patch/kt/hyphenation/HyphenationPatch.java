package com.mobileread.ixtab.patch.kt.hyphenation;

import java.security.AllPermission;
import java.security.Permission;
import java.util.Map;

import serp.bytecode.BCClass;
import serp.bytecode.BCMethod;
import serp.bytecode.Code;
import serp.bytecode.ConstantInstruction;

import com.amazon.kindle.kindlet.input.keyboard.OnscreenKeyboardUtil;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSetting;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSettings;
import com.mobileread.ixtab.jbpatch.conf.ui.ComboBoxSettingPanel;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingChangeListener;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingEntry;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingPanel;
import com.mobileread.ixtab.jbpatch.conf.ui.TextSettingPanel;

public class HyphenationPatch extends Patch {

	private static final String CLASS_HYPHENATIONMANAGER_510 = "com.mobipocket.common.library.reader.hyphenation.j";
	private static final String CLASS_FRAMECONSTRUCTOR_510 = "com.mobipocket.common.library.reader.db";

	public static final String MD5_HYPHENATIONMANAGER_510_BEFORE = "630a7704149435140c4ef8406749160d";
	public static final String MD5_HYPHENATIONMANAGER_510_AFTER = "7d974db6e5a3d1e6d649f39f746cc3bf";
	
	public static final String MD5_FRAMECONSTRUCTOR_510_BEFORE = "cbbba07fab3a4e1be58a9553f8a2e700";
	public static final String MD5_FRAMECONSTRUCTOR_510_AFTER = "efb227b84d4fdf4f58d551634d6fcdb9";
	
	static final String MODE_JUSTIFY_KEY = "Justify";
	static final String MODE_DUMB_KEY = "Dumb";
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

	static HyphenationPatch INSTANCE;
	
	public HyphenationPatch() {
		INSTANCE = this;
	}

	public int getVersion() {
		return 20120823;
	}

	public PatchMetadata getMetadata() {
		return new PatchMetadata(this).withClass(
				new PatchableClass(CLASS_FRAMECONSTRUCTOR_510).withChecksums(
						MD5_FRAMECONSTRUCTOR_510_BEFORE, MD5_FRAMECONSTRUCTOR_510_AFTER)).withClass(
				new PatchableClass(CLASS_HYPHENATIONMANAGER_510).withChecksums(
						MD5_HYPHENATIONMANAGER_510_BEFORE, MD5_HYPHENATIONMANAGER_510_AFTER));
	}


	protected void initLocalization(String locale, Map map) {
		if (RESOURCE_ID_ENGLISH.equals(locale)) {
			map.put(I18N_JBPATCH_NAME, "Fix Reader Layout and Hyphenation");
			map.put(I18N_JBPATCH_DESCRIPTION,
					"This patch fixes the layout of the E-Book reader, and allows for hyphenation.");
			map.put(CONF_MODE_I18N_NAME, "Adjustment mode");
			map.put(CONF_MODE_I18N_DESC, "Select how you want the reader behavior to be modified.");
			map.put(CONF_MODE_I18N_HINT, "If you are having trouble with hyphenation, ensure that the book has the correct language set, and that hyphenation rules for that language are available. Please refer to the patch documentation on the Wiki page for further information.");
			
			map.put(CONF_MODE_JUSTIFY_LONG_I18N, "Justify: only justify text to block layout");
			map.put(CONF_MODE_DUMB_LONG_I18N, "Dumb: justification and dumb hyphenation");
			map.put(CONF_MODE_SMART_LONG_I18N, "Smart: justification and language-specific hyphenation");
			
			map.put(CONF_MODE_JUSTIFY_SHORT_I18N, MODE_JUSTIFY_KEY);
			map.put(CONF_MODE_DUMB_SHORT_I18N, MODE_DUMB_KEY);
			map.put(CONF_MODE_SMART_SHORT_I18N, MODE_SMART_KEY);
			
			
			map.put(CONF_MINPREFIX_I18N_NAME, "Minimal First Syllable Length");
			map.put(CONF_MINPREFIX_I18N_DESC, "Minimum number of leading characters before a word is hyphenated");
			map.put(CONF_MINPREFIX_I18N_HINT, "This determines the minimum amount of characters that the first portion of a hyphenated word must contain. In other words, no word will be hyphenated unless the first part of the hyphenated word contains at least that many characters. Acceptable values for this setting are between 1 and 10.");
			
			map.put(CONF_MINSUFFIX_I18N_NAME, "Minimal Last Syllable Length");
			map.put(CONF_MINSUFFIX_I18N_DESC, "Minimum number of trailing characters when a word is hyphenated");
			map.put(CONF_MINSUFFIX_I18N_HINT, "This determines the minimum amount of characters that the last portion of a hyphenated word must contain. In other words, no word will be hyphenated unless the last part of the hyphenated word contains at least that many characters. Acceptable values for this setting are between 1 and 10.");
		}
	}

	protected ConfigurableSettings initConfigurableSettings() {
		ConfigurableSettings map = new ConfigurableSettings();
		map.add(new ModeSetting());
		map.add(new LengthSetting(localize(CONF_MINPREFIX_I18N_NAME), localize(CONF_MINPREFIX_I18N_DESC), localize(CONF_MINPREFIX_I18N_HINT), CONF_MINPREFIX_KEY, SYLLABLE_LENGTH_DEFAULT+""));
		map.add(new LengthSetting(localize(CONF_MINSUFFIX_I18N_NAME), localize(CONF_MINSUFFIX_I18N_DESC), localize(CONF_MINSUFFIX_I18N_HINT), CONF_MINSUFFIX_KEY, SYLLABLE_LENGTH_DEFAULT+""));
		return map;
	}
	
	int getMinimumFirstSyllableLength() {
		int length = SYLLABLE_LENGTH_DEFAULT;
		try {
			length = Integer.parseInt(getConfigured(CONF_MINPREFIX_KEY));
		} catch  (Throwable t) {};
		return length;
	}
	
	int getMinimumLastSyllableLength() {
		int length = SYLLABLE_LENGTH_DEFAULT;
		try {
			length = Integer.parseInt(getConfigured(CONF_MINSUFFIX_KEY));
		} catch  (Throwable t) {};
		return length;
	}
	
	public String getMode() {
		return getConfigured(CONF_MODE_KEY);
	}
	
	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_HYPHENATIONMANAGER_510_BEFORE)) {
			return patchHyphenationManager510(clazz);
		}
		if (md5.equals(MD5_FRAMECONSTRUCTOR_510_BEFORE)) {
			return patchFrameConstructor510(clazz);
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
	
	private class ModeSetting extends ConfigurableSetting {
		private final SettingEntry[] entries;

		public ModeSetting() {
			super(localize(CONF_MODE_I18N_NAME), localize(CONF_MODE_I18N_DESC), localize(CONF_MODE_I18N_HINT), CONF_MODE_KEY, CONF_MODE_DEFAULT);
			entries = new SettingEntry[3];
			entries[0] = new SettingEntry(MODE_JUSTIFY_KEY, localize(CONF_MODE_JUSTIFY_LONG_I18N));
			entries[1] = new SettingEntry(MODE_DUMB_KEY, localize(CONF_MODE_DUMB_LONG_I18N));
			entries[2] = new SettingEntry(MODE_SMART_KEY, localize(CONF_MODE_SMART_LONG_I18N));
		}

		public SettingPanel getPanel(SettingChangeListener listener) {
			return new ComboBoxSettingPanel(listener, entries);
		}

		public boolean isValid(String v) {
			for (int i=0; i < entries.length; ++i) {
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
			return new TextSettingPanel(listener, OnscreenKeyboardUtil.KEYBOARD_MODE_NUMBERS_AND_SYMBOLS, true);
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
}
