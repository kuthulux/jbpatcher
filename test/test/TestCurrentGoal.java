package test;


import java.io.File;
import java.util.HashMap;

import javax.swing.JScrollBar;

import junit.framework.TestCase;
import serp.bytecode.BCClass;
import serp.bytecode.BCMethod;
import serp.bytecode.Project;

import com.amazon.agui.swing.plaf.kindle.KindleTheme;
import com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources_de;
import com.amazon.kindle.restricted.device.impl.ScreenRotationServiceImpl;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.patch.AllRotationsPatch;
import com.mobileread.ixtab.patch.ScrollbarPatch;
import com.mobileread.ixtab.patch.TTSGermanDescriptionPatch;
import com.mobileread.ixtab.patch.TTSPatch;

/*
 * This isn't really a Unit Test, I know. It ended up just being a quick way to
 * gain some insight into serp, and to dump things for quick analysis using jad.
 */
public class TestCurrentGoal extends TestCase {
	
	public void testAndDump() throws Throwable {
//		BCClass cls = new Project().loadClass(new File(System.getProperty("user.home")+"/kindle-touch/java/classes/com/amazon/agui/swing/PagingContainer.class"));
		BCClass cls = new Project().loadClass(TTSResources_de.class);
		new TTSGermanDescriptionPatch().perform("cd9041a3105c19c2de0f61dd012872d3", cls);
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
