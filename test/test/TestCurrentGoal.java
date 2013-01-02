package test;

import java.io.File;

import junit.framework.TestCase;
import serp.bytecode.BCClass;
import serp.bytecode.BCClassLoader;
import serp.bytecode.Code;
import serp.bytecode.Project;

import com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_de;
import com.amazon.ebook.booklet.reader.resources.ReaderResources_de;
import com.mobileread.ixtab.jbpatch.MD5;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.patch.NoAdsPatch;
import com.mobileread.ixtab.patch.NoStoreMenuPatch;
import com.mobileread.ixtab.patch.collectioncount.CollectionCountPatch;
import com.mobileread.ixtab.patch.coverview.CoverViewPatch;
import com.mobileread.ixtab.patch.dictionaries.DictionariesPatch;
import com.mobileread.ixtab.patch.fontsize.FWAdapter;
import com.mobileread.ixtab.patch.fontsize.FWAdapter512;
import com.mobileread.ixtab.patch.fontsize.FWAdapter531;
import com.mobileread.ixtab.patch.fontsize.FontSizePatch;
import com.mobileread.ixtab.patch.hyphenation.HyphenationPatch;
import com.mobileread.ixtab.patch.margins.MarginsPatch;
import com.mobileread.ixtab.patch.passwd.PasswordPatch;
import com.mobileread.ixtab.patch.progressbar.ProgressBarPatch;

/*
 * This isn't really a Unit Test, I know. It ended up just being a quick way to
 * gain some insight into serp, and to dump things for quick analysis using jad.
 */
public class TestCurrentGoal extends TestCase {

	public void testAndDump() throws Throwable {
		Project p = new Project();

//		BCClass cls = p
//				.loadClass(new File(
//						System.getProperty("user.home")
//								+ "/kindle-touch/fw-531/java/classes/com/amazon/ebook/booklet/reader/impl/E.class"));
//		
//		String[] locales = new String[] {"", "_de", "_en", "_en_GB", "_es", "_fr", "_it", "_pt"};
//		String[] classes = new String[] {"com.amazon.ebook.booklet.reader.resources.ReaderResources","com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources","com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources"};
//		
//		for (int c=0; c < classes.length; ++c) {
//			for (int l = 0; l < locales.length; ++l) {
//				String cn = classes[c];
//				cn += locales[l];
//				try {
//					Class k = Class.forName(cn);
//					System.err.println(cn);
//					cls = p.loadClass(k);
//					new FontSizePatch().perform(FontSizePatch.MD5_BEFORE[0], cls);
//				} catch (Throwable t) {
//					//t.printStackTrace();
//				}
//			}
//		}
//		if (1 == 1) return;
		
		BCClass cls = p.loadClass(com.amazon.ebook.booklet.reader.impl.E.class);
		String result = new MarginsPatch().perform(
				MarginsPatch.MD5_READERUIIMPL_531_BEFORE, cls);
		if (result != null) {
			System.err.println("patch failed to perform, error is: " + result);
			fail(result);
		}
		cls.write(new File("/tmp/test.class"));
		try {
			new BCClassLoader(p).loadClass(cls.getName());
		} catch (Throwable t) {
			System.err
					.println("class failed to load. This may be harmless... up to you to know");
			t.printStackTrace(System.err);
		}
	}

	public String reflected() throws Exception {
		return super.toString();
	}

	public void doThings(Object o) throws Exception {

	}

	public void testReflect() throws Throwable {
		if (1 == 1)
			return;
		BCClass cls = new Project().loadClass(TestCurrentGoal.class);
		Code c = cls.getDeclaredMethod("reflected").getCode(false);
		Patch.dump(c);
	}

	public void testThosePeskyTranslationsForFontSizePatch() throws Throwable {
		// not really a test either, just a method to ensure that we provide correct metadata (md5 sums) for all locales.
		
		Project project = new Project();
		
		// Make sure that you have the correct framework library on the classpath before running the test.
//		FWAdapter fontAdapter = new FWAdapter512();
		FWAdapter fontAdapter = new FWAdapter531();
		String[] classes = fontAdapter.getClasses();
		for (int i=0; i < classes.length; ++i) {
			String className = classes[i];
			BCClass clazz = project.loadClass(Class.forName(className));
			String isBefore = MD5.getMd5String(clazz.toByteArray());
			
//			if ("com.amazon.ebook.booklet.reader.resources.ReaderResources_it".equals(className)) {
//				System.err.println("break");
//			}
			String err = new FontSizePatch().perform(isBefore, clazz);
			if (err != null) {
				System.err.println("While patching "+ className+ " with MD5 "+isBefore);
				System.err.println(err);
				fail();
			}
			String isAfter = MD5.getMd5String(clazz.toByteArray());
			if (isBefore.equals(isAfter)) {
				System.err.println(className+" - nothing happened.");
			}
			
			String shouldBefore = fontAdapter.getMd5Before()[i];
			String shouldAfter = fontAdapter.getMd5After()[i];
			if (!(shouldBefore.equals(isBefore) && shouldAfter.equals(isAfter))) {
				System.out.println("DECLARED:    " + className+": " + shouldBefore +" -> " +shouldAfter);
				System.out.println("ACTUAL  :    " + className+": " + isBefore +" -> " +isAfter);
				fail();
			}
		}
	}
}
