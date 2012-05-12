package test;


import java.io.File;

import javax.swing.JScrollBar;

import junit.framework.TestCase;
import serp.bytecode.BCClass;
import serp.bytecode.Project;

import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.patch.ScrollbarPatch;
import com.mobileread.ixtab.patch.tts.TTSPatch;

/*
 * This isn't really a Unit Test, I know. It ended up just being a quick way to
 * gain some insight into serp, and to dump things for quick analysis using jad.
 */
public class TestCurrentGoal extends TestCase {
	
	public void testAndDump() throws Throwable {
		BCClass cls = new Project().loadClass(new File(System.getProperty("user.home")+"/kindle-touch/java/classes/com/amazon/ebook/booklet/reader/plugin/tts/TTSProvider$TTSAction.class"));
//		BCClass cls = new Project().loadClass(HomeBooklet.class);
		new TTSPatch().perform(TTSPatch.MD5_TTSACTION, cls);
		cls.write(new File("/tmp/test.class"));
	}
	
	public void reflected() {
		JScrollBar scrollbar = null;
		ScrollbarPatch.INSTANCE.hook(scrollbar, this);
		
	}
	
	
	public void testReflect() throws Throwable {
		if (1 != 1) return;
		BCClass cls = new Project().loadClass(TestCurrentGoal.class);
		Patch.dump(cls.getMethods("reflected")[0].getCode(false));
	}
	
}
