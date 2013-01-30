package com.mobileread.ixtab.patch.margins;

import java.util.Map;
import java.util.ResourceBundle;

import serp.bytecode.BCClass;
import serp.bytecode.Code;
import serp.bytecode.ConstantInstruction;

import com.amazon.kindle.kindlet.input.keyboard.OnscreenKeyboardUtil;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSetting;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSettings;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingChangeListener;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingPanel;
import com.mobileread.ixtab.jbpatch.conf.ui.TextSettingPanel;

public class MarginsPatch extends Patch implements MarginsPatchKeys {

	private static final String CLASS_PROGRESSBARIMPL_510 = "com.amazon.ebook.booklet.reader.impl.ui.ProgressBarImpl";
	private static final String CLASS_FONTDIALOG_510 = "com.amazon.ebook.booklet.reader.impl.ui.FontDialog";
	private static final String CLASS_READERSTATEDATA_510 = "com.amazon.ebook.booklet.reader.impl.u";
	private static final String CLASS_READERUIIMPL_510 = "com.amazon.ebook.booklet.reader.impl.n";

	private static final String CLASS_READERUIIMPL_531 = "com.amazon.ebook.booklet.reader.impl.E";
	private static final String CLASS_READERSTATEDATA_531 = "com.amazon.ebook.booklet.reader.impl.L";
	private static final String CLASS_FONTDIALOG_531 = "com.amazon.ebook.booklet.reader.impl.ui.yd";
	private static final String CLASS_PROGRESSBARIMPL_531 = "com.amazon.ebook.booklet.reader.impl.ui.x";

	private static final String CLASS_READERUIIMPL_532 = "com.amazon.ebook.booklet.reader.impl.E";
	private static final String CLASS_READERSTATEDATA_532 = "com.amazon.ebook.booklet.reader.impl.L";
	private static final String CLASS_FONTDIALOG_532 = "com.amazon.ebook.booklet.reader.impl.ui.yd";
	private static final String CLASS_PROGRESSBARIMPL_532 = "com.amazon.ebook.booklet.reader.impl.ui.x";

	private static final String CLASS_READERUIIMPL_533 = "com.amazon.ebook.booklet.reader.impl.E";
	private static final String CLASS_READERSTATEDATA_533 = "com.amazon.ebook.booklet.reader.impl.L";
	private static final String CLASS_FONTDIALOG_533 = "com.amazon.ebook.booklet.reader.impl.ui.GC";
	private static final String CLASS_PROGRESSBARIMPL_533 = "com.amazon.ebook.booklet.reader.impl.ui.x";

	public static final String MD5_READERUIIMPL_510_BEFORE = "e2cd2f0631d4a75bf278c9c3a1008216";
	public static final String MD5_READERSTATEDATA_510_BEFORE = "62eae0a2e91163cdbe392de862598955";
	public static final String MD5_FONTDIALOG_510_BEFORE = "ac4fc5fad698a4c27e7fd495513b1ed1";
	public static final String MD5_PROGRESSBARIMPL_510_BEFORE = "4b168e63797ccaf1f642872b71c09ef9";

	private static final String MD5_READERUIIMPL_510_AFTER = "913a4740558cc6b9e5a446bf52b850f6";
	private static final String MD5_READERSTATEDATA_510_AFTER = "9b95e1a3b4e2d34d8c9491ae20ee03dc";
	private static final String MD5_FONTDIALOG_510_AFTER = "5907949458a3f666e68b41981b47c0c7";
	private static final String MD5_PROGRESSBARIMPL_510_AFTER = "c2b3eb58a2bc09a6948c5ef2984cb106";

	public static final String MD5_READERUIIMPL_531_BEFORE = "ded3ee5ccaa41d4adb3518c291fd25b3";
	public static final String MD5_READERSTATEDATA_531_BEFORE = "a460da0d561d8beddfb7e191f7750a70";
	public static final String MD5_FONTDIALOG_531_BEFORE = "254d26503d6c8e6fee22c535ffb22faa";
	public static final String MD5_PROGRESSBARIMPL_531_BEFORE = "b4a619322d95604b80f868042ab99b46";

	private static final String MD5_READERUIIMPL_531_AFTER = "aeeedb4b464fc0e1dd60a3496971c3d9";
	private static final String MD5_READERSTATEDATA_531_AFTER = "a6431ccb0ea66d745a51b4f4a6e8323b";
	private static final String MD5_FONTDIALOG_531_AFTER = "28ef8af25d0c834e0e2c92d5b3bda6f5";
	private static final String MD5_PROGRESSBARIMPL_531_AFTER = "7fc026b0b14fbf8a845dc9691978ebbd";

	public static final String MD5_READERUIIMPL_532_BEFORE = "832c7afede3e49363b79fb1d7d4a2dc3";
	public static final String MD5_READERSTATEDATA_532_BEFORE = "7e069d664af593e9fc51409d738131c4";
	public static final String MD5_FONTDIALOG_532_BEFORE = "6ab6ae3bd1d91ddfcff3da9660b3d15f";
	public static final String MD5_PROGRESSBARIMPL_532_BEFORE = "c6b1b3fcc19feeea0dc198c1d94bcc67";

	private static final String MD5_READERUIIMPL_532_AFTER = "f2dff2d86e5acdb0b0e521de3c64b534";
	private static final String MD5_READERSTATEDATA_532_AFTER = "4c39003a9b538f981fbcebf418953a55";
	private static final String MD5_FONTDIALOG_532_AFTER = "8171393d59671f8e5de4f65390b0dc7d";
	private static final String MD5_PROGRESSBARIMPL_532_AFTER = "2fe074113dc95f1a442f8465a21e2767";

	public static final String MD5_READERUIIMPL_533_BEFORE = "8a350e4f1f4355adb8536440753ed4bb";
	public static final String MD5_READERSTATEDATA_533_BEFORE = "b7535de8f59d1cd842939aa9d43e30e8";
	public static final String MD5_FONTDIALOG_533_BEFORE = "6a3aea0e0049ca768a0e3aeb40c9fe93";
	public static final String MD5_PROGRESSBARIMPL_533_BEFORE = "78b1f5c9a77f2f9eb47a41fb818ec698";

	private static final String MD5_READERUIIMPL_533_AFTER = "3c0dbfc778c5c3b250f59b156de76bc0";
	private static final String MD5_READERSTATEDATA_533_AFTER = "402cef0f32ec833ee52fb1eefbdaa65f";
	private static final String MD5_FONTDIALOG_533_AFTER = "0406178dc0cbc455858f6e61080a4cd4";
	private static final String MD5_PROGRESSBARIMPL_533_AFTER = "ffc9e3c7b144050036028c3a03362c0d";

	private static final int MAX_SENSIBLE_X_VALUE = 250;
	private static final int MAX_SENSIBLE_Y_VALUE = 350;

	static MarginsPatch instance;

	public MarginsPatch() {
		instance = this;
	}

	public int getVersion() {
		return 20130128;
	}

	protected void initLocalization(String locale, Map map) {
		if (RESOURCE_ID_ENGLISH.equals(locale)) {
			map.put(I18N_JBPATCH_NAME, "Modify Reader Margins");
			map.put(I18N_JBPATCH_DESCRIPTION,
					"This patch allows to change the margins around reader content.");

			map.put(NAME_READER_CONTENT_BOTTOM_MARGIN, "Bottom Margin");
			map.put(NAME_READER_CONTENT_LEFT_MARGIN, "Left Margin (PDF only)");
			map.put(NAME_READER_CONTENT_TOP_MARGIN, "Top Margin");
			map.put(NAME_READER_CONTENT_TOP_MARGIN_OFFSET, "Top Margin Offset");
			map.put(NAME_READER_CONTENT_RIGHT_MARGIN, "Right Margin (PDF only)");
			map.put(NAME_READER_PROGRESSBAR_FOOTER_HEIGHT, "Footer Height");

			map.put(DESC_READER_CONTENT_BOTTOM_MARGIN,
					"Total bottom margin height");
			map.put(DESC_READER_CONTENT_LEFT_MARGIN,
					"Left margin width for PDF documents");
			map.put(DESC_READER_CONTENT_TOP_MARGIN, "Top margin");
			map.put(DESC_READER_CONTENT_TOP_MARGIN_OFFSET, "Top margin offset");
			map.put(DESC_READER_CONTENT_RIGHT_MARGIN,
					"Right margin width for PDF documents");
			map.put(DESC_READER_PROGRESSBAR_FOOTER_HEIGHT,
					"Footer (informational display) height");

			map.put(HINT_READER_CONTENT_BOTTOM_MARGIN,
					"This indicates the total height of the bottom margin, including the footer. ");
			map.put(HINT_READER_CONTENT_LEFT_MARGIN,
					"This value determines the left margin for PDF files.");
			map.put(HINT_READER_CONTENT_TOP_MARGIN,
					"This value determines the top margin. It should not be less than the Top margin offset value.");
			map.put(HINT_READER_CONTENT_TOP_MARGIN_OFFSET,
					"The top margin is sometimes adjusted by this value (probably to account for the title bar). DO NOT CHANGE THIS SETTING UNLESS YOU KNOW WHAT YOU ARE DOING.");
			map.put(HINT_READER_CONTENT_RIGHT_MARGIN,
					"This value determines the right margin for PDF files.");
			map.put(HINT_READER_PROGRESSBAR_FOOTER_HEIGHT,
					"This value determines the height of the footer (displaying the current location, percentage, etc.), and is included in the total bottom margin height. You can completely hide the footer by setting this value to 0.");

			map.put(NAME_FONT_WORDSPERLINE_MARGIN_LIST_FEWEST,
					"Fewest Words Per Line Margin (Non-PDF)");
			map.put(DESC_FONT_WORDSPERLINE_MARGIN_LIST_FEWEST,
					"Margin applied for normal E-books for displaying fewest words per line");
			map.put(HINT_FONT_WORDSPERLINE_MARGIN_LIST_FEWEST,
					"This margin is applied on both sides for non-PDF documents when the fewest words per line setting is selected.");

			map.put(NAME_FONT_WORDSPERLINE_MARGIN_LIST_FEWER,
					"Fewer Words Per Line Margin (Non-PDF)");
			map.put(DESC_FONT_WORDSPERLINE_MARGIN_LIST_FEWER,
					"Margin applied for normal E-books for displaying fewer words per line");
			map.put(HINT_FONT_WORDSPERLINE_MARGIN_LIST_FEWER,
					"This margin is applied on both sides for non-PDF documents when the fewer words per line setting is selected.");

			map.put(NAME_FONT_WORDSPERLINE_MARGIN_LIST_DEFAULT,
					"Default Words Per Line Margin (Non-PDF)");
			map.put(DESC_FONT_WORDSPERLINE_MARGIN_LIST_DEFAULT,
					"Margin applied for normal E-books for displaying the default words per line");
			map.put(HINT_FONT_WORDSPERLINE_MARGIN_LIST_DEFAULT,
					"This margin is applied on both sides for non-PDF documents when the default words per line setting is selected.");

		}
	}

	public PatchMetadata getMetadata() {
		PatchMetadata meta = new PatchMetadata(this);
		fillMetaData(meta);
		return meta;
	}

	private void fillMetaData(PatchMetadata pd) {
		String firmware = MarginsPatchKeys.Env.getFirmware();
		if ("512".equals(firmware)) {
				pd.withClass(new PatchableClass(CLASS_PROGRESSBARIMPL_510)
						.withChecksums(MD5_PROGRESSBARIMPL_510_BEFORE,
								MD5_PROGRESSBARIMPL_510_AFTER));
				pd.withClass(new PatchableClass(CLASS_FONTDIALOG_510)
						.withChecksums(MD5_FONTDIALOG_510_BEFORE,
								MD5_FONTDIALOG_510_AFTER));
				pd.withClass(new PatchableClass(CLASS_READERSTATEDATA_510)
						.withChecksums(MD5_READERSTATEDATA_510_BEFORE,
								MD5_READERSTATEDATA_510_AFTER));
				pd.withClass(new PatchableClass(CLASS_READERUIIMPL_510)
						.withChecksums(MD5_READERUIIMPL_510_BEFORE,
								MD5_READERUIIMPL_510_AFTER));
		} else if ("531".equals(firmware)) {
			pd.withClass(new PatchableClass(CLASS_PROGRESSBARIMPL_531)
					.withChecksums(MD5_PROGRESSBARIMPL_531_BEFORE,
							MD5_PROGRESSBARIMPL_531_AFTER));
			pd.withClass(new PatchableClass(CLASS_FONTDIALOG_531)
					.withChecksums(MD5_FONTDIALOG_531_BEFORE,
							MD5_FONTDIALOG_531_AFTER));
			pd.withClass(new PatchableClass(CLASS_READERSTATEDATA_531)
					.withChecksums(MD5_READERSTATEDATA_531_BEFORE,
							MD5_READERSTATEDATA_531_AFTER));
			pd.withClass(new PatchableClass(CLASS_READERUIIMPL_531)
					.withChecksums(MD5_READERUIIMPL_531_BEFORE,
							MD5_READERUIIMPL_531_AFTER));
		} else if ("532".equals(firmware)) {
			pd.withClass(new PatchableClass(CLASS_PROGRESSBARIMPL_532)
					.withChecksums(MD5_PROGRESSBARIMPL_532_BEFORE,
							MD5_PROGRESSBARIMPL_532_AFTER));
			pd.withClass(new PatchableClass(CLASS_FONTDIALOG_532)
					.withChecksums(MD5_FONTDIALOG_532_BEFORE,
							MD5_FONTDIALOG_532_AFTER));
			pd.withClass(new PatchableClass(CLASS_READERSTATEDATA_532)
					.withChecksums(MD5_READERSTATEDATA_532_BEFORE,
							MD5_READERSTATEDATA_532_AFTER));
			pd.withClass(new PatchableClass(CLASS_READERUIIMPL_532)
					.withChecksums(MD5_READERUIIMPL_532_BEFORE,
							MD5_READERUIIMPL_532_AFTER));
		} else if ("533".equals(firmware)) {
			pd.withClass(new PatchableClass(CLASS_PROGRESSBARIMPL_533)
					.withChecksums(MD5_PROGRESSBARIMPL_533_BEFORE,
							MD5_PROGRESSBARIMPL_533_AFTER));
			pd.withClass(new PatchableClass(CLASS_FONTDIALOG_533)
					.withChecksums(MD5_FONTDIALOG_533_BEFORE,
							MD5_FONTDIALOG_533_AFTER));
			pd.withClass(new PatchableClass(CLASS_READERSTATEDATA_533)
					.withChecksums(MD5_READERSTATEDATA_533_BEFORE,
							MD5_READERSTATEDATA_533_AFTER));
			pd.withClass(new PatchableClass(CLASS_READERUIIMPL_533)
					.withChecksums(MD5_READERUIIMPL_533_BEFORE,
							MD5_READERUIIMPL_533_AFTER));
		}
	}

	String getResource(String key) {
		return getConfigured(key);
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_READERUIIMPL_510_BEFORE)) {
			return patchReaderUiImpl510(clazz);
		} else if (md5.equals(MD5_READERSTATEDATA_510_BEFORE)) {
			return patchReaderStateData510(clazz);
		} else if (md5.equals(MD5_FONTDIALOG_510_BEFORE)) {
			return patchFontDialog510(clazz);
		} else if (md5.equals(MD5_PROGRESSBARIMPL_510_BEFORE)) {
			return patchProgressBarImpl510(clazz);
		} else if (md5.equals(MD5_READERUIIMPL_531_BEFORE)) {
			return patchReaderUiImpl53x(clazz, "MR");
		} else if (md5.equals(MD5_READERUIIMPL_532_BEFORE)) {
			return patchReaderUiImpl53x(clazz, "vr");
		} else if (md5.equals(MD5_READERUIIMPL_533_BEFORE)) {
			return patchReaderUiImpl53x(clazz, "Yp");
		} else if (md5.equals(MD5_READERSTATEDATA_531_BEFORE)) {
			return patchReaderStateData53x(clazz, "CW");
		} else if (md5.equals(MD5_READERSTATEDATA_532_BEFORE)) {
			return patchReaderStateData53x(clazz, "oV");
		} else if (md5.equals(MD5_READERSTATEDATA_533_BEFORE)) {
			return patchReaderStateData53x(clazz, "oV");
		} else if (md5.equals(MD5_FONTDIALOG_531_BEFORE) || md5.equals(MD5_FONTDIALOG_532_BEFORE) || md5.equals(MD5_FONTDIALOG_533_BEFORE)) {
			return patchFontDialog53x(clazz);
		} else if (md5.equals(MD5_PROGRESSBARIMPL_531_BEFORE)
				|| md5.equals(MD5_PROGRESSBARIMPL_532_BEFORE) || md5.equals(MD5_PROGRESSBARIMPL_533_BEFORE)) {
			return patchProgressBarImpl53x(clazz);
		}
		return "Unexpected error: unknown MD5 " + md5;
	}

	private String patchReaderUiImpl510(BCClass clazz) {
		Code c = clazz.getDeclaredMethod("<init>").getCode(false);
		c.before(42);
		((ConstantInstruction) c.next()).setValue(ReaderResources.class
				.getName());
		return null;
	}

	private String patchReaderUiImpl53x(BCClass clazz, String methodName) throws Throwable {
		Code c = clazz.getDeclaredMethod("<init>").getCode(false);
		c.before(11);
		((ConstantInstruction) c.next()).setValue(ReaderResources.class
				.getName());

		// resetMargins method on the PW. For some reason, it actually switches
		// instances at runtime to the localized
		// version, so the first thing (instruction 0) to do is get the original
		// (patched) version back in.
		// Then, it also ties the horizontal margins to be at least the vertical
		// margins (instructions 10-17), and vice-
		// versa (instructions 41-48). These conditions are all disabled with
		// the following code. Note that for
		// sanity reasons, the patches are defined backwards, so that the
		// offsets mentioned in this code match the original
		// offsets.
		c = clazz.getDeclaredMethod(methodName).getCode(false);

		// Ignore the "bottom = top = max(left, top)" instruction.
		// I actually don't know when this is really used, but it
		// seems to make sense to prevent it anyway.
		c.before(46);
		c.pop(); // discard result of max() calculation
		c.before(43);
		c.dup(); // instead, just use the top value.

		// Ignore the "left = right = max(left, top)" instruction.
		c.before(14);
		c.pop(); // discard top margin
		c.dup(); // use left margin instead. max(x,x) = x ;)

		// Force the reader back to our patched resource file, in case it
		// decided to go for a localized one instead. This
		// shouldn't really have any implication: the localized resources
		// contain the same values as the non-localized
		// one by default. And the generic resource (including the patched one)
		// will delegate all requests for
		// properties it doesn't know about to the localized one anyway.
		c.before(0);
		c.aload().setThis(); // for putfield
		c.aload().setThis(); // for getfield
		c.getfield().setFieldIndex(2);
		c.invokestatic().setMethod(
				MarginsPatch.class.getDeclaredMethod(
						"fixupReaderUIImplResources",
						new Class[] { Object.class }));
		c.putfield().setFieldIndex(2);

		return null;
	}

	public static ResourceBundle fixupReaderUIImplResources(
			Object resourcesInstance) {
		if (resourcesInstance != null
				&& !resourcesInstance.getClass().getName()
						.startsWith("com.mobileread")) {
			resourcesInstance = ReaderResources.getInstance();
			// Log.INSTANCE.println("replaced ReaderResources instance with " +
			// resourcesInstance);
		}
		return (ResourceBundle) resourcesInstance;
	}

	private String patchReaderStateData510(BCClass clazz) {
		Code c = clazz.getDeclaredMethod("I").getCode(false);
		c.before(1);
		((ConstantInstruction) c.next()).setValue(ReaderResources.class
				.getName());
		return null;
	}

	private String patchReaderStateData53x(BCClass clazz, String methodName) throws Throwable {
		Code c = clazz.getDeclaredMethod(methodName).getCode(false);
		c.before(5);
		((ConstantInstruction) c.next()).setValue(ReaderResources.class
				.getName());

		return null;
	}

	private String patchFontDialog510(BCClass clazz) {
		Code c = clazz.getDeclaredMethod("<init>").getCode(false);
		c.before(15);
		((ConstantInstruction) c.next()).setValue(ReaderResources.class
				.getName());
		return null;
	}

	private String patchFontDialog53x(BCClass clazz) {
		Code c = clazz.getDeclaredMethod("<init>").getCode(false);
		c.before(0);
		((ConstantInstruction) c.next()).setValue(ReaderResources.class
				.getName());
		return null;
	}

	private String patchProgressBarImpl510(BCClass clazz) {
		Code c = clazz.getDeclaredMethod("<init>").getCode(false);
		c.before(12);
		((ConstantInstruction) c.next()).setValue(ReaderResources.class
				.getName());
		return null;
	}

	private String patchProgressBarImpl53x(BCClass clazz) {
		Code c = clazz.getDeclaredMethod("<init>").getCode(false);
		c.before(2);
		((ConstantInstruction) c.next()).setValue(ReaderResources.class
				.getName());
		return null;
	}

	protected ConfigurableSettings initConfigurableSettings() {
		ConfigurableSettings settings = new ConfigurableSettings();
		settings.add(new TopMarginSetting());
		settings.add(new TopMarginOffsetSetting());
		settings.add(new BottomMarginSetting());
		settings.add(new FooterHeightSetting());
		settings.add(new LeftMarginSetting());
		settings.add(new RightMarginSetting());

		settings.add(new DefaultWordsMarginSetting());
		settings.add(new FewerWordsMarginSetting());
		settings.add(new FewestWordsMarginSetting());
		return settings;
	}

	private class TopMarginSetting extends YMarginSetting {

		private TopMarginSetting() {
			super(localize(NAME_READER_CONTENT_TOP_MARGIN),
					localize(DESC_READER_CONTENT_TOP_MARGIN),
					localize(HINT_READER_CONTENT_TOP_MARGIN),
					KEY_READER_CONTENT_TOP_MARGIN,
					DEFAULT_READER_CONTENT_TOP_MARGIN);
		}
	}

	private class TopMarginOffsetSetting extends YMarginSetting {

		private TopMarginOffsetSetting() {
			super(localize(NAME_READER_CONTENT_TOP_MARGIN_OFFSET),
					localize(DESC_READER_CONTENT_TOP_MARGIN_OFFSET),
					localize(HINT_READER_CONTENT_TOP_MARGIN_OFFSET),
					KEY_READER_CONTENT_TOP_MARGIN_OFFSET,
					DEFAULT_READER_CONTENT_TOP_MARGIN_OFFSET);
		}
	}

	private class BottomMarginSetting extends YMarginSetting {

		private BottomMarginSetting() {
			super(localize(NAME_READER_CONTENT_BOTTOM_MARGIN),
					localize(DESC_READER_CONTENT_BOTTOM_MARGIN),
					localize(HINT_READER_CONTENT_BOTTOM_MARGIN),
					KEY_READER_CONTENT_BOTTOM_MARGIN,
					DEFAULT_READER_CONTENT_BOTTOM_MARGIN);
		}
	}

	private class FooterHeightSetting extends YMarginSetting {

		private FooterHeightSetting() {
			super(localize(NAME_READER_PROGRESSBAR_FOOTER_HEIGHT),
					localize(DESC_READER_PROGRESSBAR_FOOTER_HEIGHT),
					localize(HINT_READER_PROGRESSBAR_FOOTER_HEIGHT),
					KEY_READER_PROGRESSBAR_FOOTER_HEIGHT,
					DEFAULT_READER_PROGRESSBAR_FOOTER_HEIGHT);
		}
	}

	private class LeftMarginSetting extends XMarginSetting {

		private LeftMarginSetting() {
			super(localize(NAME_READER_CONTENT_LEFT_MARGIN),
					localize(DESC_READER_CONTENT_LEFT_MARGIN),
					localize(HINT_READER_CONTENT_LEFT_MARGIN),
					KEY_READER_CONTENT_LEFT_MARGIN,
					DEFAULT_READER_CONTENT_LEFT_MARGIN);
		}
	}

	private class RightMarginSetting extends XMarginSetting {

		private RightMarginSetting() {
			super(localize(NAME_READER_CONTENT_RIGHT_MARGIN),
					localize(DESC_READER_CONTENT_RIGHT_MARGIN),
					localize(HINT_READER_CONTENT_RIGHT_MARGIN),
					KEY_READER_CONTENT_RIGHT_MARGIN,
					DEFAULT_READER_CONTENT_RIGHT_MARGIN);
		}
	}

	private class DefaultWordsMarginSetting extends XMarginSetting {

		private DefaultWordsMarginSetting() {
			super(localize(NAME_FONT_WORDSPERLINE_MARGIN_LIST_DEFAULT),
					localize(DESC_FONT_WORDSPERLINE_MARGIN_LIST_DEFAULT),
					localize(HINT_FONT_WORDSPERLINE_MARGIN_LIST_DEFAULT),
					KEY_FONT_WORDSPERLINE_MARGIN_LIST_DEFAULT,
					DEFAULT_FONT_WORDSPERLINE_MARGIN_LIST_DEFAULT);
		}
	}

	private class FewerWordsMarginSetting extends XMarginSetting {

		private FewerWordsMarginSetting() {
			super(localize(NAME_FONT_WORDSPERLINE_MARGIN_LIST_FEWER),
					localize(DESC_FONT_WORDSPERLINE_MARGIN_LIST_FEWER),
					localize(HINT_FONT_WORDSPERLINE_MARGIN_LIST_FEWER),
					KEY_FONT_WORDSPERLINE_MARGIN_LIST_FEWER,
					DEFAULT_FONT_WORDSPERLINE_MARGIN_LIST_FEWER);
		}
	}

	private class FewestWordsMarginSetting extends XMarginSetting {

		private FewestWordsMarginSetting() {
			super(localize(NAME_FONT_WORDSPERLINE_MARGIN_LIST_FEWEST),
					localize(DESC_FONT_WORDSPERLINE_MARGIN_LIST_FEWEST),
					localize(HINT_FONT_WORDSPERLINE_MARGIN_LIST_FEWEST),
					KEY_FONT_WORDSPERLINE_MARGIN_LIST_FEWEST,
					DEFAULT_FONT_WORDSPERLINE_MARGIN_LIST_FEWEST);
		}
	}

	private abstract class MarginSetting extends ConfigurableSetting {

		public MarginSetting(String name, String description, String hint,
				String key, String defaultValue) {
			super(name, description, hint, key, defaultValue);
		}

		public final SettingPanel getPanel(SettingChangeListener listener) {
			return new TextSettingPanel(listener,
					OnscreenKeyboardUtil.KEYBOARD_MODE_NUMBERS_AND_SYMBOLS,
					true);
		}

		public final boolean isValid(String value) {
			return isIntegerBetween(value, 0, getMaxValue());
		}

		protected abstract int getMaxValue();
	}

	private class XMarginSetting extends MarginSetting {

		public XMarginSetting(String name, String description, String hint,
				String key, String defaultValue) {
			super(name, description, hint, key, defaultValue);
		}

		protected int getMaxValue() {
			return MAX_SENSIBLE_X_VALUE;
		}

	}

	private class YMarginSetting extends MarginSetting {

		public YMarginSetting(String name, String description, String hint,
				String key, String defaultValue) {
			super(name, description, hint, key, defaultValue);
		}

		protected int getMaxValue() {
			return MAX_SENSIBLE_Y_VALUE;
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

}
