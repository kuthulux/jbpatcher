package com.mobileread.ixtab.patch.tapzones;

import java.util.Map;

import serp.bytecode.BCClass;
import serp.bytecode.Code;
import serp.bytecode.ConstantInstruction;

import com.amazon.kindle.kindlet.input.keyboard.OnscreenKeyboardUtil;
import com.mobileread.ixtab.jbpatch.Environment;
//import com.mobileread.ixtab.jbpatch.Log;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSetting;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSettings;
import com.mobileread.ixtab.jbpatch.conf.ui.RadioButtonsSettingPanel;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingChangeListener;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingEntry;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingPanel;
import com.mobileread.ixtab.jbpatch.conf.ui.TextSettingPanel;

public class TapZonesPatch extends Patch {

	private static final String CLASS_B_534 = "com.amazon.ebook.booklet.reader.impl.b";
	public static final String MD5_B_534_BEFORE = "3b20330c95dd274ce70e87e963ac8ef7";
	private static final String MD5_B_534_AFTER = "2e3bb0e8e94f0431adb766e3ed079678";

	private static final int ZONE_MIN = 5;
	private static final int ZONE_MAX = 95;
	private static final String CONF_ZONE_KEY = "backzone";
	private static final String CONF_ZONE_DEFAULT = "15";
	private static final String CONF_ZONE_I18N_NAME = "backzone.name";
	private static final String CONF_ZONE_I18N_DESC = "backzone.description";
	private static final String CONF_ZONE_I18N_HINT = "backzone.hint";

	private static final String CONF_FLIP_KEY = "flip";
	private static final String CONF_FLIP_I18N_NAME = "flip.name";
	private static final String CONF_FLIP_I18N_DESC = "flip.description";
	private static final String CONF_FLIP_I18N_HINT = "flip.hint";
	private static final String CONF_FLIP_VALUE_NEVER = "flip.never";
	private static final String CONF_FLIP_VALUE_ALWAYS = "flip.always";
	private static final String CONF_FLIP_VALUE_LTR = "flip.ltr";
	private static final String CONF_FLIP_VALUE_RTL = "flip.rtl";
	private static final String CONF_FLIP_DEFAULT = CONF_FLIP_VALUE_NEVER;

	static TapZonesPatch instance;

	public TapZonesPatch() {
		instance = this;
	}

	public int getVersion() {
		return 20130912;
	}
	
	public boolean isAvailable() {
		if (Environment.getJBPatchVersionDate() < 20130328) {
			return false;
		}
		String fw = Environment.getFirmware();
		if ("5.3.4".equals(fw)) return true;
		return false;
	}

	protected void initLocalization(String locale, Map map) {
		if (RESOURCE_ID_ENGLISH.equals(locale)) {
			map.put(I18N_JBPATCH_NAME, "Modify Reader Tap Zones");
			map.put(I18N_JBPATCH_DESCRIPTION,
					"This patch allows to change the tap zones for the reader application.");

			map.put(CONF_ZONE_I18N_NAME, "Page-back Tap Zone Width");
			map.put(CONF_ZONE_I18N_DESC,
					"Width of the zone that will trigger a page-back action");
			map.put(CONF_ZONE_I18N_HINT,
					"When tapping towards the edge of the screen, a page-back action is triggered, instead of a page-forward action. You can define how large you want that area to be (in percent of the width of the screen). Valid values are between 5 and 95.");
			
			map.put(CONF_FLIP_I18N_NAME, "Swap Tap Zone Actions");
			map.put(CONF_FLIP_I18N_DESC,
					"Invert the effect of the page-forward/page-back tap zones");
			map.put(CONF_FLIP_I18N_HINT,
					"If you want, you can invert the effect of the tap zones, so that instead of going a page forward, the reader will go a page back, and vice-versa.");
			map.put(CONF_FLIP_VALUE_NEVER, "Never swap actions");
			map.put(CONF_FLIP_VALUE_ALWAYS, "Always swap actions");
			map.put(CONF_FLIP_VALUE_RTL, "Swap only for RTL (e.g., arabic) books");
			map.put(CONF_FLIP_VALUE_LTR, "Swap only for LTR (e.g., english) books");
		}
	}

	public PatchMetadata getMetadata() {
		PatchMetadata meta = new PatchMetadata(this);
		fillMetaData(meta);
		return meta;
	}

	private void fillMetaData(PatchMetadata pd) {
		String firmware = Environment.getFirmware();
		if ("5.3.4".equals(firmware)) {
			pd.withClass(new PatchableClass(CLASS_B_534)
					.withChecksums(MD5_B_534_BEFORE,
							MD5_B_534_AFTER));
		}
	}

	String getResource(String key) {
		return getConfigured(key);
	}

	protected ConfigurableSettings initConfigurableSettings() {
		ConfigurableSettings settings = new ConfigurableSettings();
		settings.add(new ZoneSetting());
		settings.add(new FlipSetting());
		return settings;
	}

	private class ZoneSetting extends ConfigurableSetting {

		public ZoneSetting() {
			super(localize(CONF_ZONE_I18N_NAME), localize(CONF_ZONE_I18N_DESC), localize(CONF_ZONE_I18N_HINT), CONF_ZONE_KEY, CONF_ZONE_DEFAULT);
		}

		public final SettingPanel getPanel(SettingChangeListener listener) {
			return new TextSettingPanel(listener,
					OnscreenKeyboardUtil.KEYBOARD_MODE_NUMBERS_AND_SYMBOLS,
					true);
		}

		public final boolean isValid(String value) {
			return isIntegerBetween(value, ZONE_MIN, ZONE_MAX);
		}
	}

	private static boolean isIntegerBetween(String value, int minInclusive,
			int maxInclusive) {
		if (value == null) {
			return false;
		}
		try {
			int i = Integer.parseInt(value);
			return i >= minInclusive && i <= maxInclusive;
		} catch (Throwable t) {
			return false;
		}
	}

	private class FlipSetting extends ConfigurableSetting {

		private final SettingEntry[] entries;
		public FlipSetting() {
			super(localize(CONF_FLIP_I18N_NAME), localize(CONF_FLIP_I18N_DESC), localize(CONF_FLIP_I18N_HINT), CONF_FLIP_KEY , CONF_FLIP_DEFAULT);
			entries = new SettingEntry[4];
			entries[0] = new SettingEntry(CONF_FLIP_VALUE_NEVER, localize(CONF_FLIP_VALUE_NEVER));
			entries[1] = new SettingEntry(CONF_FLIP_VALUE_ALWAYS, localize(CONF_FLIP_VALUE_ALWAYS));
			entries[2] = new SettingEntry(CONF_FLIP_VALUE_LTR, localize(CONF_FLIP_VALUE_LTR));
			entries[3] = new SettingEntry(CONF_FLIP_VALUE_RTL, localize(CONF_FLIP_VALUE_RTL));
		}

		public SettingPanel getPanel(SettingChangeListener listener) {
			return new RadioButtonsSettingPanel(listener, entries);
		}

		public boolean isValid(String value) {
			return CONF_FLIP_VALUE_ALWAYS.equals(value) || CONF_FLIP_VALUE_NEVER.equals(value) || CONF_FLIP_VALUE_LTR.equals(value) || CONF_FLIP_VALUE_RTL.equals(value);
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

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_B_534_BEFORE)) {
			return patchB534(clazz);
		}
		return "Unexpected error: unknown MD5 " + md5;
	}

	private String patchB534(BCClass clazz) throws Throwable {
		Code c = clazz.getDeclaredMethod("Zhc").getCode(false);
		// flip?
		if (shouldFlipLtr()) {
			c.before(41);
			((ConstantInstruction) c.next()).setValue(2);
			c.before(44);
			((ConstantInstruction) c.next()).setValue(3);
		}
		if (shouldFlipRtl()) {
			c.before(59);
			((ConstantInstruction) c.next()).setValue(3);
			c.before(61);
			((ConstantInstruction) c.next()).setValue(2);
		}
		// back zone
		c.after(24);
		c.invokestatic().setMethod(TapZonesPatch.class.getMethod("getBackZone", new Class[] {float.class}));
		c.calculateMaxLocals();
        c.calculateMaxStack();
		return null;
	}

	private boolean shouldFlipLtr() {
		String conf = getConfigured(CONF_FLIP_KEY);
		return CONF_FLIP_VALUE_ALWAYS.equals(conf) || CONF_FLIP_VALUE_LTR.equals(conf);
	}

	private boolean shouldFlipRtl() {
		String conf = getConfigured(CONF_FLIP_KEY);
		return CONF_FLIP_VALUE_ALWAYS.equals(conf) || CONF_FLIP_VALUE_RTL.equals(conf);
	}

	private static Float cachedBackZone = null;
	public static float getBackZone(float original) {
		if (cachedBackZone == null) {
			int value = Integer.parseInt(CONF_ZONE_DEFAULT);
			try {
				int configured = Integer.parseInt(instance.getConfigured(CONF_ZONE_KEY));
				if (configured < ZONE_MIN || configured > ZONE_MAX) {
					throw new IllegalArgumentException();
				}
				value = configured;
			} catch (Throwable t) {
			}
			cachedBackZone = new Float(((float)value) / 100.0f);
		}
		return cachedBackZone.floatValue();
	}

}
