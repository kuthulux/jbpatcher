package com.mobileread.ixtab.patch.fontsize;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import serp.bytecode.BCClass;
import serp.bytecode.BCMethod;
import serp.bytecode.Code;
import serp.bytecode.Instruction;
import serp.bytecode.PutFieldInstruction;

import com.mobileread.ixtab.jbpatch.Log;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSetting;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSettings;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingChangeListener;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingPanel;

public class FontSizePatch extends Patch {

	// As great as the ResourceBundle internationalization normally is: in this case, it requires to know
	// all relevant files beforehand. This means that unfortunately, we cannot account for "unofficial" localizations.
	
	// "short" locales, without country
	private static final String[] SUPPORTED_LOCALES = FWAdapter.INSTANCE.getSupportedLocales();
	

	private static final String[] CLASS = FWAdapter.INSTANCE.getClasses();
	
	// MD5 sums are in the same order as the CLASS entries
	public static final String[] MD5_BEFORE = FWAdapter.INSTANCE.getMd5Before();
	
	private static final String[] MD5_AFTER = FWAdapter.INSTANCE.getMd5After();


	private static final String CONF_KEY = "sizes";
	private static final String CONF_DEFAULT = "7 8 9 11 13 16 25 37";
	private static final String UI_NAME = "name";
	private static final String UI_DESCRIPTION = "description";
	private static final String UI_HINT = "hint";
	static final String UI_UNSUPPORTED_LOCALE = "unsupportedlocale";


	static final int MAX_SETTINGS = 8;
	static final int MIN_SETTINGS = 2;

	static FontSizePatch instance = null;
	
	public int getVersion() {
		return 20130115;
	}

	public FontSizePatch() {
		boolean isSupportedLocale = isDeviceRunningASupportedLocale();
		if (!isSupportedLocale) {
			Log.INSTANCE.println("W: The Font Size Patch does not support locale \""+Locale.getDefault()+"\". This patch may not work as expected.");
		}
		instance = this;
	}
	
	static boolean isDeviceRunningASupportedLocale() {
		boolean isSupportedLocale = false;
		String currentLocale = Locale.getDefault().toString();
		int underscore = currentLocale.indexOf("_");
		if (underscore >= 0) {
			currentLocale = currentLocale.substring(0, underscore);
		}
		for (int i=0; i < SUPPORTED_LOCALES.length; ++i) {
			if (SUPPORTED_LOCALES[i].equals(currentLocale)) {
				isSupportedLocale = true;
				break;
			}
		}
		return isSupportedLocale;
	}
	
	protected void initLocalization(String locale, Map map) {
		if (RESOURCE_ID_ENGLISH.equals(locale)) {
			map.put(I18N_JBPATCH_NAME, "Modify Reader Font Sizes");
			map.put(I18N_JBPATCH_DESCRIPTION, "This patch allows to define the font sizes available in the reader application.");
			map.put(UI_NAME, "Reader Font Sizes");
			map.put(UI_DESCRIPTION, "Available font sizes");
			map.put(UI_HINT, "You can define between 2 and 8 font sizes to select from. Sizes must be in strictly ascending order.");
			map.put(UI_UNSUPPORTED_LOCALE, "IMPORTANT: Your device seems to be set to a language that is not supported by this patch. It is very likely that this patch will NOT work properly on your device.");
		}
	}

	public PatchMetadata getMetadata() {
		PatchMetadata m = new PatchMetadata(this);
		for (int i=0; i < CLASS.length; ++i) {
			m.withClass(new PatchableClass(CLASS[i]).withChecksums(MD5_BEFORE[i],
					MD5_AFTER[i]));
		}
		return m;
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		for (int i=0; i < MD5_BEFORE.length; ++i) {
			if (md5.equals(MD5_BEFORE[i])) {
				patchResourceClass(clazz);
				return null;
			}
		}
		return "unsupported MD5: "+md5;
	}

	private void patchResourceClass(BCClass clazz) throws Exception {
		String defaultFieldName = "CONTENTS_DEFAULT";
		String defaultMethodName = "<init>";
		String fieldName = defaultFieldName;
		String methodName = defaultMethodName;
		boolean isInstance = true;
		if (clazz.getClassName().startsWith("MobiReaderImplResources") || clazz.getClassName().startsWith("ReaderUtilsResources")) {
			fieldName = FWAdapter.INSTANCE.getFieldName();
			methodName = "<clinit>";
			isInstance = false;
		}
		
		// of course, that would have been too easy. Newer firmware versions (5.3.2) have
		// switched back to declaring everything as instance methods, instead of static.
		
		BCMethod bcMethod = clazz.getDeclaredMethod(methodName);
		if (bcMethod == null && !isInstance) {
			isInstance = true;
			fieldName = defaultFieldName;
			methodName = defaultMethodName;
			bcMethod = clazz.getDeclaredMethod(methodName);
		}
		
		Code c = bcMethod.getCode(false);
		// Yes, this is ugly, but I didn't find a better way yet
		Integer contentsIndex = null;
		c.beforeFirst();
		while (true) {
			try {
				Instruction instr = c.next();
				if (instr instanceof PutFieldInstruction) {
					PutFieldInstruction put = (PutFieldInstruction) instr;
					if (fieldName.equals(put.getFieldName())) {
						contentsIndex = new Integer(put.getFieldIndex());
						break;
					}
				}
			} catch (Throwable t) {
				break;
			}
		}
		if (contentsIndex == null) {
			Log.INSTANCE.println("W: Field \""+fieldName+"\" not found in class, bailing out without patching");
			return;
		}
		c.afterLast();
		c.previous();
		if (isInstance) {
			c.aload().setThis();
			c.getfield().setFieldIndex(contentsIndex.intValue());
		} else {
			c.getstatic().setFieldIndex(contentsIndex.intValue());
		}
		c.invokestatic().setMethod(FontSizePatch.class.getDeclaredMethod("patchDefaultContents", new Class[] {Object[][].class}));
		//System.err.println("MD5 after patching: " + md5(clazz));
	}
	
	public static void patchDefaultContents(Object[][] contents) {
		for (int i=0; i < contents.length; ++i) {
			Object[] kv = contents[i];
			if ("font.menu.size.list".equals(kv[0]) || "mobireader.default.font.size.list".equals(kv[0])) {
				boolean alternativePatch = "mobireader.default.font.size.list".equals(kv[0]);
				Object value = kv[1];
				if (value instanceof int[][]) {
					int[][] array = (int[][]) value;
					array[0] = getConfiguredFontSizes(alternativePatch);
				} else if (value instanceof int[]) {
					kv[1] = getConfiguredFontSizes(alternativePatch);
				}
			}
		}
	}

	private static int[] getConfiguredFontSizes(boolean alt) {
		int[] result = instance.getConfiguredSizes();
		return alt ? createFilledArray(result) : result;
	}
	
	private static int[] createFilledArray(int[] org) {
		int base = org[0];
		// this does not have any effect on the "selecting
		// the wrong font size in the Aa dialog" bug. So we
		// just keep it at the stock values (between 7 and 43).
		int min = Math.min(base, 7);
		int max = Math.max(org[org.length-1], 43);
		int[] alt = new int[max-min+1];
		for (int i=0; i < alt.length; ++i) {
			alt[i] = base++;
		}
//		Log.INSTANCE.println("ALT: "+Arrays.toString(alt));
		return alt;
	}

	private int[] cachedSizes = null;
	
	private int[] getConfiguredSizes() {
		if (cachedSizes == null) {
			cachedSizes = deserializeConfiguration(getConfigured(CONF_KEY));
			if (cachedSizes == null) {
				// this one is guaranteed to succeed
				cachedSizes = deserializeConfiguration(CONF_DEFAULT);
			}
		}
		return cachedSizes;
	}
	
	static int[] deserializeConfiguration(String conf) {
		try {
			List list = new ArrayList();
			StringTokenizer tokens = new StringTokenizer(conf);
			int count = 0;
			while (tokens.hasMoreTokens()) {
				if (++count > MAX_SETTINGS) {
					break;
				}
				list.add(new Integer(Integer.parseInt(tokens.nextToken())));
			}
			int[] result = new int[list.size()];
			for (int i=0; i < result.length; ++i) {
				result[i] = ((Integer)list.get(i)).intValue();
			}
			return result;
		} catch (Throwable t) {
		}
		return null;
	}

	protected ConfigurableSettings initConfigurableSettings() {
		ConfigurableSettings map = new ConfigurableSettings();
		map.add(new FontSizesSetting());
		return map;
	}
	
	private class FontSizesSetting extends ConfigurableSetting {

		public FontSizesSetting() {
			super(localize(UI_NAME), localize(UI_DESCRIPTION), localize(UI_HINT), CONF_KEY, CONF_DEFAULT);
		}

		public SettingPanel getPanel(SettingChangeListener listener) {
			return new FontSizesSettingPanel(listener);
		}

		public boolean isValid(String value) {
			return deserializeConfiguration(value) != null;
		}
		
	}
}
