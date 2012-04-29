package com.mobileread.ixtab.patch;

import serp.bytecode.BCClass;
import serp.bytecode.BCMethod;
import serp.bytecode.Code;
import serp.bytecode.ConstantInstruction;
import serp.bytecode.Instruction;

import com.mobileread.ixtab.jbpatch.Descriptor;
import com.mobileread.ixtab.jbpatch.Patch;

public class TTSPatch extends Patch {

	public static final String CLASS_TTSPROVIDER = "com.amazon.ebook.booklet.reader.plugin.tts.TTSProvider";
	public static final String CLASS_TTSACTION = "com.amazon.ebook.booklet.reader.plugin.tts.TTSProvider$TTSAction";
	private static final String MD5_TTSACTION = "7342c90af8a837f4632d62d74ea86242";
	private static final String MD5_TTSPROVIDER = "cc8caeac602d90fb459134c4b60ebf02";
	
	public Descriptor[] getDescriptors() {
		return new Descriptor[] {
				/* TTSAction patch subsumes this, and is more versatile */
				//new Descriptor(CLASS_TTSPROVIDER, new String[] {MD5_TTSPROVIDER}),
				new Descriptor(CLASS_TTSACTION, new String[] {MD5_TTSACTION}),
		};
	}

	public String perform(String md5, BCClass clazz) {
		if (md5.equals(MD5_TTSPROVIDER)) {
			patchTTSProviderCode(clazz);
			return null;
		} else if (md5.equals(MD5_TTSACTION)) {
			return patchTTSActionCode(clazz);
		}
		return "no handler for "+md5;
	}

	private void patchTTSProviderCode(BCClass clazz) {
		BCMethod[] methods = clazz.getDeclaredMethods("D", new String[] {"com.amazon.ebook.booklet.reader.sdk.content.q"});
		Code code = methods[0].getCode(false);
		code.beforeFirst();
		while (code.hasNext()) {
			code.next();
			code.remove();
		}
		code.constant().setValue(true);
		code.ireturn();
		code.beforeFirst();
		// this is some artifact coming from... I don't know where.
		if (code.next().getClass() == Instruction.class) {
			code.remove();
		}
		code.calculateMaxLocals();
        code.calculateMaxStack();
        //dump(code);
	}
	
	private String patchTTSActionCode(BCClass clazz) {
		BCMethod[] methods = clazz.getDeclaredMethods("<init>");
		Code c = methods[0].getCode(false);
		
		c.after(22);
		((ConstantInstruction)c.next()).setValue(true);
		
		c.calculateMaxLocals();
        c.calculateMaxStack();
        //dump(code);
		return null;
	}
}
