package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.mobileread.ixtab.jbpatch.conf.KeyValueFile;

import junit.framework.TestCase;

public class TestKeyValueFile extends TestCase {

	private File primary;
	protected void setUp() throws Exception {
		primary = new File("/tmp/kvp.txt");
		setValues(new String[] {"A","B","C\\nlb1\\nlb2"});
	}

	private void setValues(String[] abc) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(primary), "UTF-8"));
		bw.write("b="+abc[1]+"\r\n");
		bw.write("a="+abc[0]+"\r\n");
		bw.write("c="+abc[2]+"\r\n");
		bw.close();
	}
	
	public void testReadOnly() throws Exception {
		KeyValueFile kvf = new KeyValueFile(KeyValueFile.FLAG_NONE, primary, null);
		assertEquals("A", kvf.getValue("a"));
		assertEquals("B", kvf.getValue("b"));
		assertEquals("C\nlb1\nlb2", kvf.getValue("c"));
		assertNull(kvf.getValue("d"));
		try {
			kvf.setValue("a", "X");
			fail("Expected IllegalStateException");
		} catch (IllegalStateException x) {
		}
		setValues(new String[] {"X","Y","Z"});
		assertEquals("A", kvf.getValue("a"));
		assertEquals("B", kvf.getValue("b"));
		assertEquals("C\nlb1\nlb2", kvf.getValue("c"));
	}
	
	public void testDynamicReadOnly() throws Exception {
		KeyValueFile kvf = new KeyValueFile(KeyValueFile.FLAG_DYNAMIC, primary, null);
		assertEquals("A", kvf.getValue("a"));
		assertEquals("B", kvf.getValue("b"));
		assertEquals("C\nlb1\nlb2", kvf.getValue("c"));
		assertNull(kvf.getValue("d"));
		try {
			kvf.setValue("a", "X");
			fail("Expected IllegalStateException");
		} catch (IllegalStateException x) {
		}
		Thread.sleep(1500);
		setValues(new String[] {"X","Y","Z"});
		assertEquals("X", kvf.getValue("a"));
		assertEquals("Y", kvf.getValue("b"));
		assertEquals("Z", kvf.getValue("c"));
	}
	
	public void testReadWrite() throws Exception {
		KeyValueFile kvf = new KeyValueFile(KeyValueFile.FLAG_WRITABLE, primary, null);
		assertEquals("A", kvf.getValue("a"));
		assertEquals("B", kvf.getValue("b"));
		assertEquals("C\nlb1\nlb2", kvf.getValue("c"));
		assertNull(kvf.getValue("bla"));
		try {
			assertTrue(kvf.setValue("bla", "X\nlb1\nlb2"));
		} catch (IllegalStateException x) {
			fail("Unexpected IllegalStateException");
		}
		assertEquals("A", kvf.getValue("a"));
		assertEquals("B", kvf.getValue("b"));
		assertEquals("C\nlb1\nlb2", kvf.getValue("c"));
		assertEquals("X\nlb1\nlb2", kvf.getValue("bla"));
		
		assertTrue(kvf.remove("inexistent"));
		assertTrue(kvf.remove("b"));
		assertTrue(kvf.remove("a"));
		assertTrue(kvf.remove("c"));
		assertEquals(1, kvf.listKeys().size());
	}
	
}
