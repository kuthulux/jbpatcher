package com.mobileread.ixtab.patch.margins;

import java.net.URL;
import java.util.TreeMap;

import serp.bytecode.BCClass;
import serp.bytecode.Code;
import serp.bytecode.ConstantInstruction;

import com.mobileread.ixtab.jbpatch.KindleDevice;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableDevice;


public class MarginsPatch extends Patch {

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

	static MarginsPatch instance;
	
	public MarginsPatch() {
		instance = this;
	}

	public int getVersion() {
		return 20120605;
	}
	
	public TreeMap getDefaultResourceMap(String resourceType) {
		if (RESOURCE_ID_ENGLISH.equals(resourceType)) {
			TreeMap map = new TreeMap();
			map.put(RESOURCE_JBPATCH_PATCHNAME, "Modify Reader Margins");
			return map;
		}
		return null;
	}



	public PatchMetadata getMetadata() {
		PatchableDevice pd = new PatchableDevice(KindleDevice.KT_510_1557760049);
		pd.withClass(new PatchableClass(CLASS_PROGRESSBARIMPL_510).withChecksums(MD5_PROGRESSBARIMPL_510_BEFORE, MD5_PROGRESSBARIMPL_510_AFTER));
		pd.withClass(new PatchableClass(CLASS_FONTDIALOG_510).withChecksums(MD5_FONTDIALOG_510_BEFORE, MD5_FONTDIALOG_510_AFTER));
		pd.withClass(new PatchableClass(CLASS_READERSTATEDATA_510).withChecksums(MD5_READERSTATEDATA_510_BEFORE, MD5_READERSTATEDATA_510_AFTER));
		pd.withClass(new PatchableClass(CLASS_READERUIIMPL_510).withChecksums(MD5_READERUIIMPL_510_BEFORE, MD5_READERUIIMPL_510_AFTER));
		return new PatchMetadata(this).withDevice(pd);
	}
	
	protected URL getResourcesUrl() {
		return this.getClass().getResource("/ixtab-patch-margins.txt");
	}
	
	String getResource(String key) {
		String result = localize(key);
		return key.equals(result) ? null : result;
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
