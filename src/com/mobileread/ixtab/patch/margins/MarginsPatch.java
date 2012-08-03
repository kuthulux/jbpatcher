package com.mobileread.ixtab.patch.margins;

import java.util.Map;

import serp.bytecode.BCClass;
import serp.bytecode.Code;
import serp.bytecode.ConstantInstruction;

import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSetting;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSettings;
import com.mobileread.ixtab.jbpatch.conf.ui.IntegerSettingPanel;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingChangeListener;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingPanel;

public class MarginsPatch extends Patch implements MarginsPatchKeys {

	private static final String CLASS_PROGRESSBARIMPL_510 = "com.amazon.ebook.booklet.reader.impl.ui.ProgressBarImpl";
	private static final String CLASS_FONTDIALOG_510 = "com.amazon.ebook.booklet.reader.impl.ui.FontDialog";
	private static final String CLASS_READERSTATEDATA_510 = "com.amazon.ebook.booklet.reader.impl.u";
	private static final String CLASS_READERUIIMPL_510 = "com.amazon.ebook.booklet.reader.impl.n";

	public static final String MD5_READERUIIMPL_510_BEFORE = "e2cd2f0631d4a75bf278c9c3a1008216";
	public static final String MD5_READERSTATEDATA_510_BEFORE = "62eae0a2e91163cdbe392de862598955";
	public static final String MD5_FONTDIALOG_510_BEFORE = "ac4fc5fad698a4c27e7fd495513b1ed1";
	public static final String MD5_PROGRESSBARIMPL_510_BEFORE = "4b168e63797ccaf1f642872b71c09ef9";

	private static final String MD5_READERUIIMPL_510_AFTER = "913a4740558cc6b9e5a446bf52b850f6";
	private static final String MD5_READERSTATEDATA_510_AFTER = "9b95e1a3b4e2d34d8c9491ae20ee03dc";
	private static final String MD5_FONTDIALOG_510_AFTER = "5907949458a3f666e68b41981b47c0c7";
	private static final String MD5_PROGRESSBARIMPL_510_AFTER = "c2b3eb58a2bc09a6948c5ef2984cb106";

	private static final int MAX_SENSIBLE_X_VALUE = 250;
	private static final int MAX_SENSIBLE_Y_VALUE = 350;

	static MarginsPatch instance;

	public MarginsPatch() {
		instance = this;
	}

	public int getVersion() {
		return 20120723;
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

			map.put(NAME_FONT_WORDSPERLINE_MARGIN_LIST_FEWEST, "Fewest Words Per Line Margin (Non-PDF)");
			map.put(DESC_FONT_WORDSPERLINE_MARGIN_LIST_FEWEST, "Margin applied for normal E-books for displaying fewest words per line");
			map.put(HINT_FONT_WORDSPERLINE_MARGIN_LIST_FEWEST, "This margin is applied on both sides for non-PDF documents when the fewest words per line setting is selected.");
			
			map.put(NAME_FONT_WORDSPERLINE_MARGIN_LIST_FEWER, "Fewer Words Per Line Margin (Non-PDF)");
			map.put(DESC_FONT_WORDSPERLINE_MARGIN_LIST_FEWER, "Margin applied for normal E-books for displaying fewer words per line");
			map.put(HINT_FONT_WORDSPERLINE_MARGIN_LIST_FEWER, "This margin is applied on both sides for non-PDF documents when the fewer words per line setting is selected.");
			
			map.put(NAME_FONT_WORDSPERLINE_MARGIN_LIST_DEFAULT, "Default Words Per Line Margin (Non-PDF)");
			map.put(DESC_FONT_WORDSPERLINE_MARGIN_LIST_DEFAULT, "Margin applied for normal E-books for displaying the default words per line");
			map.put(HINT_FONT_WORDSPERLINE_MARGIN_LIST_DEFAULT, "This margin is applied on both sides for non-PDF documents when the default words per line setting is selected.");
			

		}
	}

	public PatchMetadata getMetadata() {
		PatchMetadata meta = new PatchMetadata(this);
		fillMetaData(meta);
		return meta;
	}

	private void fillMetaData(PatchMetadata pd) {
		pd.withClass(new PatchableClass(CLASS_PROGRESSBARIMPL_510)
				.withChecksums(MD5_PROGRESSBARIMPL_510_BEFORE,
						MD5_PROGRESSBARIMPL_510_AFTER));
		pd.withClass(new PatchableClass(CLASS_FONTDIALOG_510).withChecksums(
				MD5_FONTDIALOG_510_BEFORE, MD5_FONTDIALOG_510_AFTER));
		pd.withClass(new PatchableClass(CLASS_READERSTATEDATA_510)
				.withChecksums(MD5_READERSTATEDATA_510_BEFORE,
						MD5_READERSTATEDATA_510_AFTER));
		pd.withClass(new PatchableClass(CLASS_READERUIIMPL_510).withChecksums(
				MD5_READERUIIMPL_510_BEFORE, MD5_READERUIIMPL_510_AFTER));
	}

	String getResource(String key) {
		return getConfigured(key);
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_READERUIIMPL_510_BEFORE)) {
			return patchReaderUiImpl510(clazz);
		}
		if (md5.equals(MD5_READERSTATEDATA_510_BEFORE)) {
			return patchReaderStateData510(clazz);
		}
		if (md5.equals(MD5_FONTDIALOG_510_BEFORE)) {
			return patchFontDialog510(clazz);
		}
		if (md5.equals(MD5_PROGRESSBARIMPL_510_BEFORE)) {
			return patchProgressBarImpl510(clazz);
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

	private String patchReaderStateData510(BCClass clazz) {
		Code c = clazz.getDeclaredMethod("I").getCode(false);
		c.before(1);
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

	private String patchProgressBarImpl510(BCClass clazz) {
		Code c = clazz.getDeclaredMethod("<init>").getCode(false);
		c.before(12);
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
			return new IntegerSettingPanel(listener);
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
