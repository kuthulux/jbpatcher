package test;


import java.io.File;

import junit.framework.TestCase;
import serp.bytecode.BCClass;
import serp.bytecode.BCClassLoader;
import serp.bytecode.Code;
import serp.bytecode.Project;

import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.patch.NoAdsPatch;
import com.mobileread.ixtab.patch.NoStoreMenuPatch;
import com.mobileread.ixtab.patch.coverview.CoverViewPatch;
import com.mobileread.ixtab.patch.hyphenation.HyphenationPatch;
import com.mobileread.ixtab.patch.margins.MarginsPatch;
import com.mobileread.ixtab.patch.passwd.PasswordPatch;

/*
 * This isn't really a Unit Test, I know. It ended up just being a quick way to
 * gain some insight into serp, and to dump things for quick analysis using jad.
 */
public class TestCurrentGoal extends TestCase {
	
	public void testAndDump() throws Throwable {
		Project p = new Project();
		
		BCClass cls = p.loadClass(new File(System.getProperty("user.home")+"/kindle-touch/java.512/classes/com/amazon/kindle/home/view/browse/BrowseContentCell.class"));
//		BCClass cls = p.loadClass(new File(System.getProperty("user.home")+"/kindle-touch/java.512/classes/com/mobipocket/common/log/j.class"));
//		BCClass cls = p.loadClass(HomeBooklet.class);
		String result = new CoverViewPatch().perform(CoverViewPatch.MD5_J_BEFORE, cls);
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
	
	public String reflected() throws Exception {
		return super.toString();
	}
	
	public void doThings(Object o) throws Exception {
		
	}
	
	public void testReflect() throws Throwable {
		//if (1 == 1 ) return;
		BCClass cls = new Project().loadClass(TestCurrentGoal.class);
		Code c = cls.getDeclaredMethod("reflected").getCode(false);
		Patch.dump(c);
	}
	
}
