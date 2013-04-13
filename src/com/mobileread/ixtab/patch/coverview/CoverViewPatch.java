package com.mobileread.ixtab.patch.coverview;

import java.security.AllPermission;
import java.security.Permission;
import java.util.Map;

import serp.bytecode.BCClass;
import serp.bytecode.Code;
import serp.bytecode.ConstantInstruction;

import com.amazon.kindle.content.catalog.MutableItem;
import com.amazon.kindle.home.HomeBooklet;
import com.mobileread.ixtab.jbpatch.Environment;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSetting;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSettings;
import com.mobileread.ixtab.jbpatch.conf.ui.RadioButtonsSettingPanel;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingChangeListener;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingEntry;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingPanel;

public class CoverViewPatch extends Patch {

	private static final String CLASS_U = "com.amazon.kindle.home.view.browse.u";
	public static final String MD5_U_BEFORE = "7514fc95c491b7688ab7ead1c5786fdd";
	private static final String MD5_U_AFTER = "0600f28d4d862dd78bb22d84c31c6240";

	private static final String CLASS_J = "com.amazon.kindle.home.view.c.j";
	public static final String MD5_J_BEFORE = "d19abbac19cd0c6afd26320594b70e75";
	private static final String MD5_J_AFTER = "655d426e27352326c8fcda6d03889314";

	private static final String CLASS_HOMEBOOKLET = "com.amazon.kindle.home.HomeBooklet";
	public static final String MD5_HOMEBOOKLET_BEFORE = "83836d8792099cdf7c2dac9866ae845d";
	private static final String MD5_HOMEBOOKLET_AFTER = "5fbb7d25312338f5d931cd102dd8f366";

	private static final String CLASS_MOBI7EXTRACTOR = "com.amazon.kindle.extractor.mobi.Mobi7Extractor";
	public static final String MD5_MOBI7EXTRACTOR_BEFORE = "55caf19a2d1bd8accd0daa6a01813317";
	private static final String MD5_MOBI7EXTRACTOR_AFTER = "5d9fcabf3ec2becd4e82a2bca3671013";

	private static final String COVER_VIEW_MODE = "COVER_VIEW_MODE";
	private static final String LIST_VIEW_MODE = "LIST_VIEW_MODE";
	
	private static final String CONF_KEY = "mode";

	private static final String UI_NAME = "name";
	private static final String UI_DESC = "description";
	private static final String UI_HINT = "hint";
	private static final String UI_MODE_LIST = "list";
	private static final String UI_MODE_COVER = "cover";
	
	private static CoverViewPatch instance = null;
	private static String currentViewMode = null;
	
	public int getVersion() {
		return 20130413;
	}
	
	public boolean isAvailable() {
		int jb = Environment.getJBPatchVersionDate();
		String fw = Environment.getFirmware();
		return jb >= 20130328 && "5.1.0".equals(fw);
	}


	protected void initLocalization(String locale, Map map) {
		if (RESOURCE_ID_ENGLISH.equals(locale)) {
			map.put(I18N_JBPATCH_NAME, "Enable Cover View Mode");
			map.put(I18N_JBPATCH_DESCRIPTION,
					"This patch allows to switch between the default list mode, and a cover view mode, while browsing the home screen and collections.");
			map.put(UI_NAME, "Default Display Mode");
			map.put(UI_DESC, "Display mode activated when the device starts up.");
			map.put(UI_HINT, "The cover view mode is only available for the Home screen and for displaying collections. It does not apply to other views such as the archive, or search results.");
			map.put(UI_MODE_LIST, "List items");
			map.put(UI_MODE_COVER, "Show covers");
		}
	}

	public PatchMetadata getMetadata() {
		return new PatchMetadata(this)
		.withClass(
				new PatchableClass(CLASS_U).withChecksums(MD5_U_BEFORE,
						MD5_U_AFTER))
				.withClass(
						new PatchableClass(CLASS_J).withChecksums(MD5_J_BEFORE,
								MD5_J_AFTER))
				.withClass(
						new PatchableClass(CLASS_MOBI7EXTRACTOR).withChecksums(MD5_MOBI7EXTRACTOR_BEFORE,
								MD5_MOBI7EXTRACTOR_AFTER))
				.withClass(
						new PatchableClass(CLASS_HOMEBOOKLET).withChecksums(
								MD5_HOMEBOOKLET_BEFORE, MD5_HOMEBOOKLET_AFTER));
	}

	public Permission[] getRequiredPermissions() {
		return new Permission[] { new AllPermission() };
	}

	public CoverViewPatch() {
		super();
		instance = this;
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_U_BEFORE)) {
			return patchMethodL(clazz);
		}
		if (md5.equals(MD5_J_BEFORE)) {
			return patchMethodL(clazz);
		}
		if (md5.equals(MD5_HOMEBOOKLET_BEFORE)) {
			return patchHomeBooklet(clazz);
		}
		if (md5.equals(MD5_MOBI7EXTRACTOR_BEFORE)) {
			return patchMobi7Extractor(clazz);
		}
		return "Unsupported MD5: " + md5;
	}

	private String patchMobi7Extractor(BCClass clazz) throws Exception {
		Code c = clazz.getDeclaredMethod("D", new String[]{"com.amazon.kindle.content.catalog.MutableItem","java.lang.String"}).getCode(false);
		c.before(545);
		c.iload().setLocal(11);
		c.aload().setLocal(1);
		c.invokestatic().setMethod(CoverViewPatch.class.getMethod("isInvalidASIN", new Class[]{boolean.class, Object.class}));
		c.istore().setLocal(11);
		return null;
	}
	
	public static boolean isInvalidASIN(boolean flag, Object item) {
		if (flag) return true;
		MutableItem m = (MutableItem) item;
		String cde = m.getCDEKey();
		if (cde == null || cde.length() != 10) {
			return true;
		}
		char[] cs = cde.toCharArray();
		for (int i=0; i < cs.length; ++i) {
			char c = cs[i];
			if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z')) {
				continue;
			}
			return true;
		}
		return false;
	}

	private String patchMethodL(BCClass clazz) {
		Code c = clazz.getDeclaredMethod("L").getCode(false);
		c.beforeFirst();
		((ConstantInstruction) c.next()).setValue(true);
		return null;
	}

	private String patchHomeBooklet(BCClass clazz) throws Exception {
		Code c = clazz.getDeclaredMethod("F").getCode(false);
		c.beforeFirst();
		c.next();
		c.remove();
		c.invokestatic().setMethod(
				CoverViewPatch.class.getDeclaredMethod("getViewMode",
						new Class[0]));

		c = clazz.getDeclaredMethod("H", new Class[] { String.class }).getCode(
				false);
		c.beforeFirst();
		c.aload().setThis();
		c.aload().setLocal(1);
		c.invokestatic().setMethod(
				CoverViewPatch.class.getDeclaredMethod("setViewMode",
						new Class[] { Object.class, String.class }));
		c.calculateMaxLocals();
		c.calculateMaxStack();

		c = clazz.getDeclaredMethod("B", new Class[0]).getCode(false);
		c.beforeFirst();
		c.next();
		c.remove();
		c.invokestatic().setMethod(
				CoverViewPatch.class.getDeclaredMethod("getViewModeIndex",
						new Class[0]));
		c.calculateMaxLocals();
		c.calculateMaxStack();
		return null;
	}

	public static void setViewMode(Object homeBooklet, String mode) {
		currentViewMode = mode;
		((HomeBooklet)homeBooklet).getActiveController().load();
	}

	public static String getViewMode() {
		if (currentViewMode == null) {
			currentViewMode = instance.getConfigured(CONF_KEY);
		}
		return currentViewMode;
	}

	public static int getViewModeIndex() {
		return getViewMode().equals(COVER_VIEW_MODE) ? 1 : 0;
	}

	protected ConfigurableSettings initConfigurableSettings() {
		ConfigurableSettings map = new ConfigurableSettings();
		map.add(new CoverViewSetting());
		return map;
	}
	
	private class CoverViewSetting extends ConfigurableSetting {

		private final SettingEntry[] entries;
		public CoverViewSetting() {
			super(localize(UI_NAME), localize(UI_DESC), localize(UI_HINT), CONF_KEY , LIST_VIEW_MODE);
			entries = new SettingEntry[2];
			entries[0] = new SettingEntry(LIST_VIEW_MODE, localize(UI_MODE_LIST));
			entries[1] = new SettingEntry(COVER_VIEW_MODE, localize(UI_MODE_COVER));
		}

		public SettingPanel getPanel(SettingChangeListener listener) {
			return new RadioButtonsSettingPanel(listener, entries);
		}

		public boolean isValid(String value) {
			return LIST_VIEW_MODE.equals(value) || COVER_VIEW_MODE.equals(value);
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
