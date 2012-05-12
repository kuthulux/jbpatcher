package test;

import java.net.URL;
import java.util.Arrays;
import java.util.Locale;

import com.mobileread.ixtab.jbpatch.I18n;

import junit.framework.TestCase;

public class TestI18n extends TestCase {
	

	public void testNullSourceReturnsInput() throws Exception {
		I18n x = new I18n();
		assertEquals("one.two.three", x.i18n("one.two.three"));
	}
	
	public void testKeyNamesAreStripped() throws Exception {
		I18n x = new I18n();
		assertEquals("one", x.i18n(" one    "));
	}
	
	public void testLocalesAreOrderedCorrectly() throws Exception {
		assertTrue(Arrays.equals(new String[] {"en"} , new I18n((URL) null, Locale.ENGLISH).getOrderedLocales()));
		assertTrue(Arrays.equals(new String[] {"en_US", "en"} , new I18n((URL) null, Locale.US).getOrderedLocales()));
		assertTrue(Arrays.equals(new String[] {"fr", "en"} , new I18n((URL) null, Locale.FRENCH).getOrderedLocales()));
		assertTrue(Arrays.equals(new String[] {"fr_FR", "fr", "en"} , new I18n((URL) null, Locale.FRANCE).getOrderedLocales()));
	}
	
	public void testMultiLanguage() throws Exception {
		URL url = getClass().getResource("i18n/multilang.txt");
		assertEquals("dummy", new I18n(url, Locale.ENGLISH).i18n("dummy"));
		assertEquals("en", new I18n(url, Locale.ENGLISH).i18n("lang"));
		assertEquals("en", new I18n(url, Locale.US).i18n("lang"));
		assertEquals("en", new I18n(url, Locale.UK).i18n("lang"));
		assertEquals("de", new I18n(url, Locale.GERMAN).i18n("lang"));
		assertEquals("de", new I18n(url, Locale.GERMANY).i18n("lang"));
		assertEquals("fr", new I18n(url, Locale.FRENCH).i18n("lang"));
		assertEquals("fr_FR", new I18n(url, Locale.FRANCE).i18n("lang"));
		assertEquals("en", new I18n(url, Locale.ITALIAN).i18n("lang"));
		assertEquals("en", new I18n(url, Locale.ITALY).i18n("lang"));
	}
	
	public void testIxtabsSetup() throws Exception {
		// This test is ok to fail if you're not on a german OS.
		// It's merely there to make sure that the default locale is
		// really taken into account.
		URL url = getClass().getResource("i18n/multilang.txt");
		assertEquals("de", new I18n(url).i18n("lang"));
	}
}
