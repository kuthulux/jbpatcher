package com.mobileread.ixtab.patch.gotohome;

import serp.bytecode.BCClass;
import serp.bytecode.lowlevel.Entry;
import serp.bytecode.lowlevel.UTF8Entry;
import serp.bytecode.Code;
import serp.bytecode.ConstantInstruction;

import com.mobileread.ixtab.jbpatch.Environment;
import com.mobileread.ixtab.jbpatch.Log;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;

import java.util.Map;

public class GoToHomePatch extends Patch {

	public final String CLASS = "com.amazon.ebook.booklet.reader.impl.E";
	public static final String MD5_BEFORE = "4a34db7e06eb8e6351b5d4124e441174";
	public static final String MD5_AFTER = "6c4fb6da6392b6a6fe619065acc63ab3";
	
	java.util.ResourceBundle p = java.util.ResourceBundle.getBundle("com.amazon.ebook.booklet.reader.resources.ReaderResources");
	com.amazon.ebook.booklet.reader.impl.ReaderSDKImpl t;
	
	public PatchMetadata getMetadata() {   	  
		PatchableClass pc = new PatchableClass(CLASS).withChecksums(MD5_BEFORE, MD5_AFTER);
		return new PatchMetadata(this).withClass(pc);
	}


	public int getVersion() {
		return 20210414;
	}

    public boolean isAvailable() {        
		int jb = Environment.getJBPatchVersionDate();
		String fw = Environment.getFirmware();
		return jb >= 20130328 && fw.compareTo("5.3.0") >= 0;
	}
	
	protected void initLocalization(String locale, Map map) {
		if (RESOURCE_ID_ENGLISH.equals(locale)) {
			map.put(I18N_JBPATCH_NAME, "Add menu item Go to Home");
			map.put(I18N_JBPATCH_DESCRIPTION, "On devices with malfunctioning home button, this provides a simple way to reach the home screen from the reader menu.");
		}
	}
	
	public String perform(String md5, BCClass clazz) throws Throwable {    	
	    addMenuItem(clazz);
	    return null;
    }

	
	public void addMenuItem(BCClass clazz) throws Throwable {
        Code c = clazz.getDeclaredMethod("wC").getCode(false);
				    	
    	/*
    	GoToHomeAction action = new GoToHomeAction();
    	e2.add(0, action);
        */
        
        c.after(3);    	
    	
    	c.anew().setType(GoToHomeAction.class);        
        c.dup();
        c.invokespecial().setMethod(GoToHomeAction.class.getConstructor(new Class[]{ }));	    
        c.astore().setLocal(2);
    	c.aload().setLocal(1);
    	c.constant().setValue(0);
    	c.aload().setLocal(2);
        c.invokevirtual().setMethod(java.util.ArrayList.class.getMethod("add", new Class[]{ int.class, Object.class} ));               
        
        c.calculateMaxLocals();
		c.calculateMaxStack();
	}

}
