package com.mobileread.ixtab.patch.margins;

import com.mobileread.ixtab.jbpatch.Environment;

/* This is a really ugly way to define constants. Don't do it this way. Really, don't! */

interface MarginsPatchKeys {
	
	static class Env {
		
		private static Boolean isPaperwhite;
		
		public static boolean isKPW() {
			if (isPaperwhite == null) {
				synchronized (Env.class) {
					if (isPaperwhite == null) {
						String fw = Environment.getFirmware();
						isPaperwhite = Boolean.valueOf(fw.equals("5.3.1") || fw.equals("5.3.3") || fw.equals("5.3.4"));
					}
				}
			}
			return isPaperwhite.booleanValue();
		}
	}
	
	final String KEY_READER_CONTENT_BOTTOM_MARGIN = "reader.content.bottomMargin";
	final String KEY_READER_CONTENT_LEFT_MARGIN = "reader.content.leftMargin";
	final String KEY_READER_CONTENT_TOP_MARGIN = "reader.content.topMargin";
	final String KEY_READER_CONTENT_TOP_MARGIN_OFFSET = "reader.content.topMarginOffset";
	final String KEY_READER_CONTENT_RIGHT_MARGIN = "reader.content.rightMargin";
	final String KEY_READER_PROGRESSBAR_FOOTER_HEIGHT = "reader.progressbar.footer.height";
	final String KEY_FONT_WORDSPERLINE_MARGIN_LIST_FEWEST = "font.wordsperline.margin.list.fewest";
	final String KEY_FONT_WORDSPERLINE_MARGIN_LIST_FEWER = "font.wordsperline.margin.list.fewer";
	final String KEY_FONT_WORDSPERLINE_MARGIN_LIST_DEFAULT = "font.wordsperline.margin.list.default";

	final String DEFAULT_READER_CONTENT_BOTTOM_MARGIN = "30";
	final String DEFAULT_READER_CONTENT_LEFT_MARGIN = Env.isKPW()? "50" : "40";
	final String DEFAULT_READER_CONTENT_TOP_MARGIN = Env.isKPW()? "40" :"30";
	final String DEFAULT_READER_CONTENT_TOP_MARGIN_OFFSET = Env.isKPW()? "12" :"10";
	final String DEFAULT_READER_CONTENT_RIGHT_MARGIN = Env.isKPW()? "50" : "40";
	final String DEFAULT_READER_PROGRESSBAR_FOOTER_HEIGHT = "30";
	final String DEFAULT_FONT_WORDSPERLINE_MARGIN_LIST_FEWEST = Env.isKPW()? "150" : "120";
	final String DEFAULT_FONT_WORDSPERLINE_MARGIN_LIST_FEWER = Env.isKPW()? "100" : "80";
	final String DEFAULT_FONT_WORDSPERLINE_MARGIN_LIST_DEFAULT = Env.isKPW()? "50" : "40";

	final String NAME_READER_CONTENT_BOTTOM_MARGIN = "reader.content.bottomMargin.name";
	final String NAME_READER_CONTENT_LEFT_MARGIN = "reader.content.leftMargin.name";
	final String NAME_READER_CONTENT_TOP_MARGIN = "reader.content.topMargin.name";
	final String NAME_READER_CONTENT_TOP_MARGIN_OFFSET = "reader.content.topMarginOffset.name";
	final String NAME_READER_CONTENT_RIGHT_MARGIN = "reader.content.rightMargin.name";
	final String NAME_READER_PROGRESSBAR_FOOTER_HEIGHT = "reader.progressbar.footer.height.name";
	final String NAME_FONT_WORDSPERLINE_MARGIN_LIST_FEWEST = "font.wordsperline.margin.list.fewest.name";
	final String NAME_FONT_WORDSPERLINE_MARGIN_LIST_FEWER = "font.wordsperline.margin.list.fewer.name";
	final String NAME_FONT_WORDSPERLINE_MARGIN_LIST_DEFAULT = "font.wordsperline.margin.list.default.name";

	final String DESC_READER_CONTENT_BOTTOM_MARGIN = "reader.content.bottomMargin.desc";
	final String DESC_READER_CONTENT_LEFT_MARGIN = "reader.content.leftMargin.desc";
	final String DESC_READER_CONTENT_TOP_MARGIN = "reader.content.topMargin.desc";
	final String DESC_READER_CONTENT_TOP_MARGIN_OFFSET = "reader.content.topMarginOffset.desc";
	final String DESC_READER_CONTENT_RIGHT_MARGIN = "reader.content.rightMargin.desc";
	final String DESC_READER_PROGRESSBAR_FOOTER_HEIGHT = "reader.progressbar.footer.height.desc";
	final String DESC_FONT_WORDSPERLINE_MARGIN_LIST_FEWEST = "font.wordsperline.margin.list.fewest.desc";
	final String DESC_FONT_WORDSPERLINE_MARGIN_LIST_FEWER = "font.wordsperline.margin.list.fewer.desc";
	final String DESC_FONT_WORDSPERLINE_MARGIN_LIST_DEFAULT = "font.wordsperline.margin.list.default.desc";
	
	final String HINT_READER_CONTENT_BOTTOM_MARGIN = "reader.content.bottomMargin.hint";
	final String HINT_READER_CONTENT_LEFT_MARGIN = "reader.content.leftMargin.hint";
	final String HINT_READER_CONTENT_TOP_MARGIN = "reader.content.topMargin.hint";
	final String HINT_READER_CONTENT_TOP_MARGIN_OFFSET = "reader.content.topMarginOffset.hint";
	final String HINT_READER_CONTENT_RIGHT_MARGIN = "reader.content.rightMargin.hint";
	final String HINT_READER_PROGRESSBAR_FOOTER_HEIGHT = "reader.progressbar.footer.height.hint";
	final String HINT_FONT_WORDSPERLINE_MARGIN_LIST_FEWEST = "font.wordsperline.margin.list.fewest.hint";
	final String HINT_FONT_WORDSPERLINE_MARGIN_LIST_FEWER = "font.wordsperline.margin.list.fewer.hint";
	final String HINT_FONT_WORDSPERLINE_MARGIN_LIST_DEFAULT = "font.wordsperline.margin.list.default.hint";
}
