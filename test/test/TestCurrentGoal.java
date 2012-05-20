package test;


import java.io.File;

import junit.framework.TestCase;
import serp.bytecode.BCClass;
import serp.bytecode.BCClassLoader;
import serp.bytecode.Code;
import serp.bytecode.Project;

import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.patch.devcert.DevCertInjectPatch;
import com.mobileread.ixtab.patch.passwd.PasswordPatch;

/*
 * This isn't really a Unit Test, I know. It ended up just being a quick way to
 * gain some insight into serp, and to dump things for quick analysis using jad.
 */
public class TestCurrentGoal extends TestCase {
	
	public void testAndDump() throws Throwable {
		Project p = new Project();
		
//		BCClass cls = p.loadClass(new File(System.getProperty("user.home")+"/kindle-touch/java/classes/com/amazon/kindle/swing/DetailView.class"));
		BCClass cls = p.loadClass(new File(System.getProperty("user.home")+"/kindle-touch/java/classes/com/amazon/kindle/home/action/OpenDetailsAction.class"));
//		BCClass cls = p.loadClass(HomeBooklet.class);
		String result = new PasswordPatch().perform(PasswordPatch.MD5_OPENDETAILSACTION_510, cls);
		if (result != null) {
			System.err.println("patch failed to perform, error is: "+result);
		}
		cls.write(new File("/tmp/test.class"));
		try {
			Class loaded = new BCClassLoader(p).loadClass(cls.getName());
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
		if (1 == 1) return;
		BCClass cls = new Project().loadClass(TestCurrentGoal.class);
		Code c = cls.getDeclaredMethod("reflected").getCode(false);
		Patch.dump(c);
	}
	
}
