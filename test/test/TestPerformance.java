package test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mobileread.ixtab.jbpatch.bootstrap.Stage1;

import junit.framework.TestCase;

/* this test is really hacky, and will probably only work on ixtab's computer.
 * It will need actual patches (.jars) to be installed in /var/local.
 * In addition, you will need a file which lists all classes to be loaded, in a specific format.
 * A sample line is:
 * com/amazon/a/a/b.class                  lib/kindlePageNumbersDeviceReader.jar
 * Though only the first part is considered, and will be transformed to a class name.
 * So,
 * com.amazon.a.a.b
 * would do as well.
 */
public class TestPerformance extends TestCase {
	
	public static final String fileWithClassNames = "/home/ixtab/kindle-touch/fw-531/java/classes.txt";
	
	public void testPerformance() throws Exception {
		// for fairness reasons, we don't run both setups in a single test execution. Change the following line
		// to enable or disable patching.
		runTest(false);
	}
	
	private void runTest(boolean patch) throws Exception {
		List classNames = extractClassNames();
		if (patch) {
			new Stage1().start(null);
		}
		Iterator it = classNames.iterator();
		int ok = 0;
		int fail = 0;
		long t = System.currentTimeMillis();
		while (it.hasNext()) {
			String className = (String) it.next();
			try {
				Class.forName(className);
				++ok;
			} catch (Throwable e) {
				++fail;
			}
		}
		t = System.currentTimeMillis() - t;
		System.out.println("Patching: " + patch+"; time: "+t+" ms, "+ok+" ok, "+fail+" failed");
	}

	private List extractClassNames() throws Exception {
		ArrayList list = new ArrayList();
		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(fileWithClassNames)));
		for (String line = r.readLine(); line != null; line = r.readLine()) {
			line = line.replaceAll("\\s.*", "");
			line = line.replaceAll("\\.class$", "");
			line = line.replace('/', '.');
			if (line.indexOf('$') != -1) {
				continue;
			}
			list.add(line);
		}
		return list;
	}
}
