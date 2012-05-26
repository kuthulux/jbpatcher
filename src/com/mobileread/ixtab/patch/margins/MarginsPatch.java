package com.mobileread.ixtab.patch.margins;

import java.net.URL;

import serp.bytecode.BCClass;
import serp.bytecode.Code;
import serp.bytecode.ConstantInstruction;

import com.mobileread.ixtab.jbpatch.Descriptor;
import com.mobileread.ixtab.jbpatch.Patch;


public class MarginsPatch extends Patch {

	public static final String MD5_READERUIIMPL_510 = "e2cd2f0631d4a75bf278c9c3a1008216";
	public static final String MD5_READERSTATEDATA_510 = "62eae0a2e91163cdbe392de862598955";
	public static final String MD5_FONTDIALOG_510 = "ac4fc5fad698a4c27e7fd495513b1ed1";
	public static final String MD5_PROGRESSBARIMPL_510 = "4b168e63797ccaf1f642872b71c09ef9";

	static MarginsPatch instance;
	
	public MarginsPatch() {
		instance = this;
	}

	protected Descriptor[] getDescriptors() {
		return new Descriptor[] {
				new Descriptor("com.amazon.ebook.booklet.reader.impl.n", new String[]{MD5_READERUIIMPL_510}),
				new Descriptor("com.amazon.ebook.booklet.reader.impl.u", new String[]{MD5_READERSTATEDATA_510}),
				new Descriptor("com.amazon.ebook.booklet.reader.impl.ui.FontDialog", new String[]{MD5_FONTDIALOG_510}),
				new Descriptor("com.amazon.ebook.booklet.reader.impl.ui.ProgressBarImpl", new String[]{MD5_PROGRESSBARIMPL_510}),
						};
	}
	
	
	protected URL getResourcesUrl() {
		return this.getClass().getResource("/ixtab-patch-margins.txt");
	}
	
	String getResource(String key) {
		String result = get(key);
		return key.equals(result) ? null : result;
	}

	

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_READERUIIMPL_510)) {
			return patchReaderUiImpl510(clazz);
		}
		if (md5.equals(MD5_READERSTATEDATA_510)) {
			return patchReaderStateData510(clazz);
		}
		if (md5.equals(MD5_FONTDIALOG_510)) {
			return patchFontDialog510(clazz);
		}
		if (md5.equals(MD5_PROGRESSBARIMPL_510)) {
			return patchProgressBarImpl510(clazz);
		}
		return "Unexpected error: unknown MD5 "+md5;
	}

	private String patchReaderUiImpl510(BCClass clazz) {
		Code c = clazz.getDeclaredMethod("<init>").getCode(false);
		c.before(42);
		((ConstantInstruction) c.next()).setValue(ReaderResources.class.getName());
		return null;
	}

	private String patchReaderStateData510(BCClass clazz) {
		Code c = clazz.getDeclaredMethod("I").getCode(false);
		c.before(1);
		((ConstantInstruction) c.next()).setValue(ReaderResources.class.getName());
		return null;
	}

	private String patchFontDialog510(BCClass clazz) {
		Code c = clazz.getDeclaredMethod("<init>").getCode(false);
		c.before(15);
		((ConstantInstruction) c.next()).setValue(ReaderResources.class.getName());
		return null;
	}

	private String patchProgressBarImpl510(BCClass clazz) {
		Code c = clazz.getDeclaredMethod("<init>").getCode(false);
		c.before(12);
		((ConstantInstruction) c.next()).setValue(ReaderResources.class.getName());
		return null;
	}

}
