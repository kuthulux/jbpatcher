package test;

import java.io.File;

import junit.framework.TestCase;
import serp.bytecode.BCClass;
import serp.bytecode.BCClassLoader;
import serp.bytecode.Code;
import serp.bytecode.Project;

import com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources_de;
import com.amazon.ebook.booklet.reader.resources.ReaderResources_de;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.patch.kt.NoAdsPatch;
import com.mobileread.ixtab.patch.kt.NoStoreMenuPatch;
import com.mobileread.ixtab.patch.kt.collectioncount.CollectionCountPatch;
import com.mobileread.ixtab.patch.kt.coverview.CoverViewPatch;
import com.mobileread.ixtab.patch.kt.dictionaries.DictionariesPatch;
import com.mobileread.ixtab.patch.kt.fontsize.FontSizePatch;
import com.mobileread.ixtab.patch.kt.hyphenation.HyphenationPatch;
import com.mobileread.ixtab.patch.kt.margins.MarginsPatch;
import com.mobileread.ixtab.patch.kt.passwd.PasswordPatch;
import com.mobileread.ixtab.patch.kt.progressbar.ProgressBarPatch;

/*
 * This isn't really a Unit Test, I know. It ended up just being a quick way to
 * gain some insight into serp, and to dump things for quick analysis using jad.
 */
public class TestCurrentGoal extends TestCase {

	public void testAndDump() throws Throwable {
		Project p = new Project();

		BCClass cls = p
				.loadClass(new File(
						System.getProperty("user.home")
								+ "/kindle-touch/java.512/classes/com/amazon/ebook/booklet/reader/plugin/systemcards/DictionaryCard.class"));
		
		String[] locales = new String[] {"", "_de", "_en", "_en_GB", "_es", "_fr", "_it", "_pt"};
		String[] classes = new String[] {"com.amazon.ebook.booklet.reader.resources.ReaderResources","com.amazon.ebook.booklet.mobireader.impl.resources.MobiReaderImplResources","com.amazon.ebook.booklet.reader.utils.resources.ReaderUtilsResources"};
		
		for (int c=0; c < classes.length; ++c) {
			for (int l = 0; l < locales.length; ++l) {
				String cn = classes[c];
				cn += locales[l];
				try {
					Class k = Class.forName(cn);
					System.err.println(cn);
					cls = p.loadClass(k);
					new FontSizePatch().perform(FontSizePatch.MD5_BEFORE[0], cls);
				} catch (Throwable t) {
					//t.printStackTrace();
				}
			}
		}
		if (1 == 1) return;
		
		cls = p.loadClass(MobiReaderImplResources_de.class);
		String result = new FontSizePatch().perform(
				FontSizePatch.MD5_BEFORE[0], cls);
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

}
