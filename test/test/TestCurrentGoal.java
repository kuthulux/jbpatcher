package test;


import java.io.File;

import junit.framework.TestCase;
import serp.bytecode.BCClass;
import serp.bytecode.BCMethod;
import serp.bytecode.Project;

import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.patch.TTSPatch;

/*
 * This isn't really a Unit Test, I know. It ended up just being a quick way to
 * gain some insight into serp, and to dump things for quick analysis using jad.
 */
public class TestCurrentGoal extends TestCase {
	
	public void testAndDump() throws Throwable {
		BCClass cls = new Project().loadClass(new File(System.getProperty("user.home")+"/kindle-touch/java/classes/com/amazon/ebook/booklet/reader/plugin/tts/TTSProvider$TTSAction.class"));
		new TTSPatch().perform("7342c90af8a837f4632d62d74ea86242", cls);
		BCMethod[] m = cls.getDeclaredMethods();
		for (int i=0; i  < m.length; ++i) {
			System.err.println(m[i].getName());
		}
		cls.write(new File("/tmp/test.class"));
	}
	
	public int reflected() {
		int x = 2;
		int y = 3;
		return  x+y;
	}
	
	public void testReflect() throws Throwable {
		BCClass cls = new Project().loadClass(TestCurrentGoal.class);
		Patch.dump(cls.getMethods("reflected")[0].getCode(false));
	}
	
}
