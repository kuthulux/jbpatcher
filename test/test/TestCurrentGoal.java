package test;


import java.io.File;

import junit.framework.TestCase;
import serp.bytecode.BCClass;
import serp.bytecode.BCClassLoader;
import serp.bytecode.Code;
import serp.bytecode.Project;

import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.patch.ads.NoAdsPatch;

/*
 * This isn't really a Unit Test, I know. It ended up just being a quick way to
 * gain some insight into serp, and to dump things for quick analysis using jad.
 */
public class TestCurrentGoal extends TestCase {
	
	public void testAndDump() throws Throwable {
		Project p = new Project();
		
//		BCClass cls = p.loadClass(new File(System.getProperty("user.home")+"/kindle-touch/java/classes/com/amazon/ebook/booklet/reader/impl/ui/ProgressBarImpl.class"));
		BCClass cls = p.loadClass(new File(System.getProperty("user.home")+"/kindle-touch/java/classes/com/amazon/kindle/restricted/ad/manager/AdManagerImpl.class"));
//		BCClass cls = p.loadClass(HomeBooklet.class);
//		String result = new MarginsPatch().perform(MarginsPatch.MD5_PROGRESSBARIMPL_510, cls);
		String result = new NoAdsPatch().perform(NoAdsPatch.MD5_ADMANAGERIMPL, cls);
		if (result != null) {
			System.err.println("patch failed to perform, error is: "+result);
			fail(result);
		}
		cls.write(new File("/tmp/test.class"));
		try {
			new BCClassLoader(p).loadClass(cls.getName());
		} catch (Throwable t) {
			System.err.println("class failed to load. This may be harmless... up to you to know");
			t.printStackTrace(System.err);
		}
	}
	
	public void reflected() throws Exception {
		// currently nothing.
	}
	
	public void doThings(Object o) throws Exception {
		
	}
	
	public void testReflect() throws Throwable {
		if (Math.sqrt(4.0) != 2.0) return;
		BCClass cls = new Project().loadClass(TestCurrentGoal.class);
		Code c = cls.getDeclaredMethod("reflected").getCode(false);
		Patch.dump(c);
	}
	
}
