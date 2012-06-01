package com.mobileread.ixtab.patch.hyphenation;

import serp.bytecode.BCClass;
import serp.bytecode.BCMethod;
import serp.bytecode.Code;
import serp.bytecode.Instruction;

import com.mobileread.ixtab.jbpatch.Descriptor;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobipocket.common.log.Logger;

public class HyphenationPatch extends Patch {

	public static final String MD5_BOOKVIEWMANAGER_AMAZON_510 = "f1f3def30134788fe23db6e57fab3788";
	public static final String MD5_HYPHENATIONMANAGER_510 = "630a7704149435140c4ef8406749160d";
	

	protected Descriptor[] getDescriptors() {
		return new Descriptor[] {
				
//				new Descriptor("com.amazon.ebook.booklet.mobireader.impl.MobiBookController", new String[] {"4f75460a6596ecba9d2e704a61083070"}),
				new Descriptor("com.mobipocket.common.library.reader.jb", new String[] {MD5_BOOKVIEWMANAGER_AMAZON_510}),
				new Descriptor("com.mobipocket.common.library.reader.hyphenation.j", new String[] {MD5_HYPHENATIONMANAGER_510})
		};
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_BOOKVIEWMANAGER_AMAZON_510)) {
			return patchBookViewManagerAmazon510(clazz);
		}
		if (md5.equals(MD5_HYPHENATIONMANAGER_510)) {
			return patchHyphenationManager510(clazz);
		}
		return "Unexpected error: unknown MD5 " + md5;
	}

	private String patchHyphenationManager510(BCClass clazz) throws Throwable {
		BCMethod m = clazz.getDeclaredMethod("D", new String[]{"int", "com.mobipocket.common.library.reader.hyphenation.HyphenationEngine"});
		Code c = m.getCode(false);
		
		c.beforeFirst();
		c.iload().setLocal(1);
		c.aload().setLocal(2);
		c.invokestatic().setMethod(HyphenationPatch.class.getMethod("onSetHyphenationEngine", new Class[]{int.class, Object.class}));
		return null;
	}

	private String patchBookViewManagerAmazon510(BCClass clazz)
			throws NoSuchMethodException {
		Code c = clazz.getDeclaredMethods("<init>")[0].getCode(false);
		c.before(11);
		for (int i=0; i < 5; ++i) {
			c.next(); c.remove();
		}
		c.invokestatic().setMethod(DummyHyphenationEngine.class.getMethod("instantiate", new Class[0]));
		return null;
	}

	private String patchMobiBookController(BCClass clazz)
			throws NoSuchMethodException {
		Code c = clazz.getDeclaredMethod("C").getCode(false);
		
		c.before(35);
		c.invokestatic().setMethod(HyphenationPatch.class.getMethod("onApplyDefaultSetting", new Class[0]));
		
		c.before(33);
		c.pop();
		c.constant().setValue(0);
//		dump(c);
		return null;
	}

	public static void onApplyDefaultSetting() {
		log("applyDefaultSetting");
	}
	
	public static void onSetHyphenationEngine(int language, Object engine) {
		com.mobipocket.common.log.j.D(mobiLogger);
		com.mobipocket.common.log.j.D(0xFFFF);
		log("setHyphenationEngine: language="+language+", instance class="+engine.getClass().getName());
	}
	
	private static final MobiLogger mobiLogger = new MobiLogger();
	private static class MobiLogger implements Logger {

		public void D() {
			// TODO Auto-generated method stub
			
		}

		public void D(String s) {
			log(s);
		}
		
	}
}
