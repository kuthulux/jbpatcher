package com.mobileread.ixtab.patch;

import serp.bytecode.BCClass;
import serp.bytecode.Code;

import com.amazon.agui.swing.ConfirmationDialog;
import com.amazon.kindle.settings.SettingsBooklet;
import com.mobileread.ixtab.jbpatcher.Descriptor;
import com.mobileread.ixtab.jbpatcher.Patch;

public class LegalIllegalPatch extends Patch {

	protected Descriptor[] getDescriptors() {
		return new Descriptor[] {
			new Descriptor("com.amazon.kindle.settings.menu.SettingsMenuItemFactory$5", new String[] {"34d10aa93fe2252675a986e88a54ebb8"})	
		};
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
		//dump(c);
		return null;
	}

}
