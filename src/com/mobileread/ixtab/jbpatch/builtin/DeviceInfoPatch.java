package com.mobileread.ixtab.jbpatch.builtin;

import java.text.MessageFormat;
import java.util.Map;

import serp.bytecode.BCClass;
import serp.bytecode.Code;

import com.mobileread.ixtab.jbpatch.JBPatchMetadata;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;

public class DeviceInfoPatch extends Patch {

	private static final String FW510_CLASS = "com.amazon.kindle.settings.dialog.DeviceInfoDialog";
	private static final String FW510_MD5_IN = "9f393118b394eaa5ffcca7f44e47db2b";
	private static final String FW510_MD5_OUT = "d12a6b3fdcd14d39a8beb9cd4436e6cc";

	private static final String FW530_CLASS = "com.amazon.kindle.settings.e.X";
	private static final String FW530_MD5_IN = "5a65cdf8740e184fc18e8e4a31d0167a";
	private static final String FW530_MD5_OUT = "2c8d4c9a4f0ef88ae85f186fcc38790a";

	public int getVersion() {
		return 1;
	}

	public PatchMetadata getMetadata() {
		PatchableClass c510 = new PatchableClass(FW510_CLASS).withChecksums(
				FW510_MD5_IN, FW510_MD5_OUT);
		PatchableClass c530 = new PatchableClass(FW530_CLASS).withChecksums(
				FW530_MD5_IN, FW530_MD5_OUT);
		return new PatchMetadata(this).withClass(c510).withClass(c530);
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(FW510_MD5_IN)) {
			return patchFw510(clazz);
		} else if (md5.equals(FW530_MD5_IN)) {
			return patchFw530(clazz);
		} else {
			return "Unsupported MD5: md5";
		}
	}

	private String patchFw510(BCClass clazz) throws NoSuchMethodException {
		Code c = clazz.getDeclaredMethods("h")[0].getCode(false);

		// substitute call to MessageFormat.format by call to ourselves
		c.before(52);
		c.invokestatic().setMethod(
				DeviceInfoPatch.class.getMethod("format", new Class[] {
						String.class, Object[].class }));
		c.next();
		c.remove();

		// increase the dialog height to allow for the added line
		c.before(109);
		c.constant().setValue(20);
		c.iadd();
		return null;
	}

	private String patchFw530(BCClass clazz) throws NoSuchMethodException {
		Code c = clazz.getDeclaredMethod("WkA").getCode(false);

		// substitute call to MessageFormat.format by call to ourselves
		c.before(79);
		c.invokestatic().setMethod(
				DeviceInfoPatch.class.getMethod("format", new Class[] {
						String.class, Object[].class }));
		c.next();
		c.remove();

		// increase the dialog height to allow for the added line
		c.before(186);
		c.constant().setValue(20);
		c.iadd();
		return null;
	}


	public static String format(String pattern, Object[] arguments) {
		if (pattern.endsWith("</html>")) {
			pattern = pattern.substring(0, pattern.length() - 7);
			pattern += "<br/><b>JBPatch:</b>&nbsp;" + JBPatchMetadata.VERSION+"</html>";
//			pattern += "<br/><b>JBPatch:</b>&nbsp; v" + JBPatchMetadata.VERSION+ "({" + arguments.length
//					+ ",number,integer}/{" + (arguments.length + 1)
//					+ ",number,integer}"
//					+ ")</html>";
//			Object[] copy = new Object[arguments.length + 2];
//			System.arraycopy(arguments, 0, copy, 0, arguments.length);
//			copy[arguments.length] = new Integer(Patches.getActiveCount());
//			copy[arguments.length + 1] = new Integer(
//					Patches.getAvailableCount());
//			arguments = copy;
		}
		return MessageFormat.format(pattern, arguments);
	}

	protected void initLocalization(String locale, Map map) {
		// because this is a very special case, we can indeed afford to do
		// nothing here.
	}

}
