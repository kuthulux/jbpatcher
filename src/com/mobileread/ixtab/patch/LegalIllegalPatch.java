package com.mobileread.ixtab.patch;

import java.util.Map;

import serp.bytecode.BCClass;
import serp.bytecode.Code;

import com.amazon.agui.swing.ConfirmationDialog;
import com.amazon.kindle.settings.SettingsBooklet;
import com.mobileread.ixtab.jbpatch.KindleDevice;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableDevice;

public class LegalIllegalPatch extends Patch {

	private static final String CLASS = "com.amazon.kindle.settings.menu.SettingsMenuItemFactory$5";
	private static final String MD5_BEFORE = "34d10aa93fe2252675a986e88a54ebb8";
	private static final String MD5_AFTER = "d2a85c3a36d54b190714761a39912e6b";

	public int getVersion() {
		return 20120708;
	}

	protected void initLocalization(String locale, Map map) {
		if (RESOURCE_ID_ENGLISH.equals(locale)) {
			map.put(I18N_JBPATCH_NAME, "Modify Legal Information");
			map.put(I18N_JBPATCH_DESCRIPTION, "This is a useless, proof-of-concept patch which modifies the behavior of the Legal Information display.");
		}
	}

	public PatchMetadata getMetadata() {
		PatchableClass pc = new PatchableClass(CLASS).withChecksums(MD5_BEFORE, MD5_AFTER);
		PatchableDevice pd = new PatchableDevice(KindleDevice.KT_510_1557760049).withClass(pc);
		return new PatchMetadata(this).withDevice(pd);
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		Code c = clazz.getDeclaredMethods("actionPerformed")[0].getCode(false);
		c.after(1);
		
		c.invokevirtual().setMethod(SettingsBooklet.class.getMethod("getName", new Class[0]));
		
		c.constant().setValue("Illegal?");
		c.constant().setValue("Legal?");
		
		c.constant().setValue(1);
		c.anewarray().setType(String.class);
		c.dup();
		c.constant().setValue(0);
		c.constant().setValue("Give me a break!");
		c.aastore();
		
		c.constant().setValue(1);
		c.newarray().setType(int.class);
		c.dup();
		c.constant().setValue(0);
		c.constant().setValue(42);
		c.iastore();
		
		c.invokestatic().setMethod(ConfirmationDialog.class.getMethod("showDialog", new Class[] {String.class, String.class, String.class,String[].class, int[].class}));
		c.pop();
		
		
		c.vreturn();
		while (c.hasNext()) {
			c.next();
			c.remove();
		}
		
        c.calculateMaxLocals();
        c.calculateMaxStack();
		return null;
	}

}
