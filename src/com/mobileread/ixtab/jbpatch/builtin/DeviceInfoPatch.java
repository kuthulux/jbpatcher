package com.mobileread.ixtab.jbpatch.builtin;

import java.text.MessageFormat;
import java.util.Map;

import serp.bytecode.BCClass;
import serp.bytecode.Code;

import com.mobileread.ixtab.jbpatch.JBPatchMetadata;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;
import com.mobileread.ixtab.jbpatch.Patches;

public class DeviceInfoPatch extends Patch {

	private static final String MD5_KT510_IN = "9f393118b394eaa5ffcca7f44e47db2b";
	private static final String MD5_KT510_OUT = "d12a6b3fdcd14d39a8beb9cd4436e6cc";
	private static final String DIALOG_CLASS = "com.amazon.kindle.settings.dialog.DeviceInfoDialog";

	public int getVersion() {
		return 1;
	}

	public PatchMetadata getMetadata() {
		PatchableClass pc = new PatchableClass(DIALOG_CLASS).withChecksums(
				MD5_KT510_IN, MD5_KT510_OUT);
		return new PatchMetadata(this).withClass(pc);
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		Code c = clazz.getDeclaredMethods("h")[0].getCode(false);

		// substitute call to MessageFormat.format by call to ourselves
		c.after(51);
		c.invokestatic().setMethod(
				DeviceInfoPatch.class.getMethod("format", new Class[] {
						String.class, Object[].class }));
		c.next();
		c.remove();

		// increase the dialog height to allow for the added line
		c.after(108);
		c.constant().setValue(20);
		c.iadd();
		return null;
	}

	public static String format(String pattern, Object[] arguments) {
		if (pattern.endsWith("</html>")) {
			pattern = pattern.substring(0, pattern.length() - 7);
			pattern += "<br/><b>JBPatch:</b>&nbsp; {" + arguments.length
					+ ",number,integer}/{" + (arguments.length + 1)
					+ ",number,integer} (v" + JBPatchMetadata.VERSION
					+ ")</html>";
			Object[] copy = new Object[arguments.length + 2];
			System.arraycopy(arguments, 0, copy, 0, arguments.length);
			copy[arguments.length] = new Integer(Patches.getActiveCount());
			copy[arguments.length + 1] = new Integer(
					Patches.getAvailableCount());
			arguments = copy;
		}
		return MessageFormat.format(pattern, arguments);
	}

	protected void initLocalization(String locale, Map map) {
		// because this is a very special case, we can indeed afford to do
		// nothing here.
	}

}
