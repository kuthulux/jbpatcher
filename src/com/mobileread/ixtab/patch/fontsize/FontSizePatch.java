package com.mobileread.ixtab.patch.fontsize;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import serp.bytecode.BCClass;
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

import edu.emory.mathcs.backport.java.util.Arrays;

public class FontSizePatch extends Patch {

	// As great as the ResourceBundle internationalization normally is: in this case, it requires to know
	// all relevant files beforehand. This means that unfortunately, we cannot account for "unofficial" localizations.
	
	// "short" locales, without country
	private static final String[] SUPPORTED_LOCALES = new String[]{"en","de","es","fr","it","pt"};
	

	private static final String[] CLASS = new String[] {
		"com.amazon.ebook.booklet.reader.resources.ReaderResources",
		"com.amazon.ebook.booklet.reader.resources.ReaderResources_de",
		"com.amazon.ebook.booklet.reader.resources.ReaderResources_en",
		"com.amazon.ebook.booklet.reader.resources.ReaderResources_en_GB",
		"com.amazon.ebook.booklet.reader.resources.ReaderResources_es",
		"com.amazon.ebook.booklet.reader.resources.ReaderResources_fr",
		"com.amazon.ebook.booklet.reader.resources.ReaderResources_it",
		"com.amazon.ebook.booklet.reader.resources.ReaderResources_pt",
		"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources",
		"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_de",
		"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_en_GB",
		"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_es",
		"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_fr",
		"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_it",
		"com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_pt",
		"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources",
		"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_de",
		"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_en_GB",
		"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_es",
		"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_fr",
		"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_it",
		"com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources_pt",
	};
	
	// MD5 sums are in the same order as the CLASS entries
	public static final String[] MD5_BEFORE = new String[] {
		// ReaderResources
		"1a92381dcf76ba5640e1484e6d3528e3",
		"5b2b068bfdfe075b02a5401191543860",
		"4c46534179c65e364faf125a0bfdd35a",
		"408ee5536be3eda70c939899394ff980",
		"b7c900e8fd34cca90d81c46ea53ec645",
		"1a9ccd3e1dfd979a4532c37eba42eeb0",
		"9b8c4887eed56ddd26a0465cf32f718b",
		"b716ea26fb223daaaecb2f97d252419c",
		// MobiReaderImplResources
		"848a34e10a878cdf75f8e657a496018b",
		"19c03c7da3fe5e79729347282aa6ad23",
		"7b1820a2f0cd4e54c163307868a0846d",
		"9f1c5fd275af4aa1701d28e4bcbadc3f",
		"1ea92dfbf367c4db1f0fd58e1724c703",
		"a142c505c40ce963c5e8c28002bf3368",
		"13d864b7fae715f570cec46dfd356239",
		// ReaderUtilsResources
		"2af2e83e6065ce43a2150508de566942",
		"b080da9e40006e85fd757a559d82c6ca",
		"1f999abeaf3fdd8abc1e44c7301cd45b",
		"a1ba01510ea1e8bda434e72a48f45bd7",
		"f066279d800e85a7c537d41c1483bbe2",
		"3191e538b1f75846750a0bc65851570b",
		"b16a895f7bf9bf0a9ee50b69f1cafbe1",
	};
	
	private static final String[] MD5_AFTER = new String[] {
		//
		"213c8870afd8e52cafa33893db85de5e",
		"5c833f8d70695ca1d3aef3da29234511",
		"25b1b4c381c20626100ca16446645121",
		"e9059bbd30f92894bdf83b64ff295a67",
		"b70417f1aaaa14e23abcb6f49fb9d53a",
		"990858ab29f17f68d3bf8ceec8cfd6f4",
		"3e08021b1ec2a8e96ccdaee18e93fc88",
		"b8a1e61b240583ef63c99680565410e9",
		//
		"ff456df6df7ea17a6fb1270766ab07bf",
		"5d66106dd3274913dce024fbcf0ff12b",
		"faa09f981738392092b34a8c82d8ed5b",
		"8cdbc4d86d49378ab6448879a9db5974",
		"612134ed6bb2696a4423a84a7248dccb",
		"0ae0749814883bb200a914f603fd5c87",
		"f5ff8ca492ebaff1ec7950d50a0acf18",
		//
		"612dc4f3cf5abc7230436930902784d5",
		"38d78672a6e6bc4266c224423ffa47ab",
		"014bf384326a0c31a717277e4857858b",
		"d567d06460d55e09ef6ecf8068560903",
		"a2dd94e9421dca4a844ad5a17d8822af",
		"cf9e82b35c5dcd15a3fdc2aefafc384a",
		"cf4c12d538d246ee7160905a021f0897",
	};


	private static final String CONF_KEY = "sizes";
	private static final String CONF_DEFAULT = "7 8 9 11 13 16 25 37";
	private static final String UI_NAME = "name";
	private static final String UI_DESCRIPTION = "description";
	private static final String UI_HINT = "hint";
	static final String UI_UNSUPPORTED_LOCALE = "unsupportedlocale";


	static final int MAX_SETTINGS = 8;
	static final int MIN_SETTINGS = 2;

	static FontSizePatch instance = null;
	
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
	public int getVersion() {
		return 20120925;
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
		String fieldName = "CONTENTS_DEFAULT";
		String methodName = "<init>";
		boolean isInstance = true;
		if (clazz.getClassName().startsWith("MobiReaderImplResources") || clazz.getClassName().startsWith("ReaderUtilsResources")) {
			fieldName = "f";
			methodName = "<clinit>";
			isInstance = false;
		}
		Code c = clazz.getDeclaredMethod(methodName).getCode(false);
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
		System.err.println("MD5 after patching: " + md5(clazz));
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
