package com.mobileread.ixtab.patch.kt.devcert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Permission;
import java.util.Map;

import serp.bytecode.BCClass;
import serp.bytecode.Code;
import serp.bytecode.Instruction;
import serp.bytecode.LocalVariable;

import com.amazon.kindle.kindlet.internal.KindletExecutionException;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchMetadata;
import com.mobileread.ixtab.jbpatch.PatchMetadata.PatchableClass;

public class DevCertInjectPatch extends Patch {

	private static final String KEY_INSTALL_SUCCEEDED = "install.succeeded";
	private static final String KEY_INSTALL_FAILED = "install.failed";
	private static final String KEY_ARCHIVE_INVALID = "archive.invalid";
	
	private static final String KEYSTORE_DIRECTORY = "/var/local/java/keystore/";
	private static final String KEYSTORE_FILE = "developer.keystore";

	private static final String CLASS = "com.amazon.kindle.kindlet.internal.security.b";
	private static final String MD5_BEFORE = "ec7e6cba592cdfdb1bdaf3fd7ae6f613";
	private static final String MD5_AFTER = "b1ea6c018cbddb9fde405e3947e612f5";

	public int getVersion() {
		return 20121007;
	}

	protected void initLocalization(String locale, Map map) {
		if (RESOURCE_ID_ENGLISH.equals(locale)) {
			map.put(I18N_JBPATCH_NAME, "Install mobileread.com Developer Certificates");
			map.put(I18N_JBPATCH_DESCRIPTION, "Kindlets must be signed with a developer certificate. This patch will automatically install a set of known certificates used by mobileread.com developers (Version: 2012-10-02), if required.");
			map.put(KEY_ARCHIVE_INVALID,"The patch to automatically install the mobileread.com developer certificates is invalid. Please re-download the patch.");
			map.put(KEY_INSTALL_FAILED,"The mobileread.com developer certificates could not be properly installed.");
			map.put(KEY_INSTALL_SUCCEEDED,"The mobileread.com developer certificates have been successfully installed. Please close this message and start the application again. If the application was signed with one of the included certificates, it will work.");
		}
	}

	public PatchMetadata getMetadata() {
		PatchableClass pc = new PatchableClass(CLASS).withChecksums(MD5_BEFORE, MD5_AFTER);
		return new PatchMetadata(this).withClass(pc);
	}

	static DevCertInjectPatch INSTANCE;

	public DevCertInjectPatch() {
		INSTANCE = this;
	}

	String getResource(String key) {
		return localize(key);
	}

	public Permission[] getRequiredPermissions() {
		return new Permission[] {new FilePermission(KEYSTORE_DIRECTORY+KEYSTORE_FILE, "read,write")};
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_BEFORE)) {
			return patchB510(clazz);
		}
		return "Unexpected error: unknown MD5 " + md5;
	}

	private String patchB510(BCClass clazz) throws Throwable {
		Code c = clazz.getDeclaredMethod("<init>").getCode(false);

		LocalVariable exception = c.getLocalVariableTable(false)
				.addLocalVariable("exception", Exception.class);
		exception.setLocal(9);
		c.beforeFirst();
		exception.setStart(c.next());
		c.afterLast();
		exception.setEnd(c.previous());

		c.after(86);
		Instruction tryStart = c.next();
		c.after(89);
		Instruction tryEnd = c.next();
		Instruction tryDone = c.next();
		c.after(tryEnd);
		c.go2().setTarget(tryDone);

		Instruction handlerStart = c.astore().setLocalVariable(exception);
		c.aload().setLocalVariable(exception);
		c.invokestatic().setMethod(
				DevCertInjectPatch.class.getMethod("patch510",
						new Class[] { Exception.class }));
		
		c.addExceptionHandler(tryStart, tryEnd, handlerStart, Exception.class);

		c.calculateMaxLocals();
		c.calculateMaxStack();

		return null;
	}

	private static boolean failedBefore = false;

	public static void patch510(Exception sourceException) throws Exception {
		if (!failedBefore && sourceException instanceof KindletExecutionException) {
			KindletExecutionException ke = (KindletExecutionException) sourceException;
			if ("kindlet.test.device.error".equals(ke.K())
					&& "com.amazon.kindle.kindlet.internal.security.KindletClassLoaderResources"
							.equals(ke.B())) {
				KindletExecutionException returnException = null;
				try {
					returnException = performInstallation();
				} catch (Throwable t) {
					t.printStackTrace(logger);
					throw buildException(true, "Unexpected exception", t.getClass().getName());
				}
				if (returnException != null) {
					throw returnException;
				}
			}
		}
		throw sourceException;
	}

	private static KindletExecutionException performInstallation() {
		File targetFile = getKeyStoreFile();
		if (targetFile != null) {
			InputStream source = DevCertInjectPatch.class
					.getResourceAsStream(KEYSTORE_FILE);
			if (source == null) {
				return buildException(true, KEYSTORE_FILE + " missing in bundle", KEY_ARCHIVE_INVALID);
			}
			OutputStream target = null;
			try {
				target = new FileOutputStream(targetFile);
				copy(source, target);
			} catch (IOException io) {
				io.printStackTrace(logger);
				return buildException(true, "Unexpected I/O exception", KEY_INSTALL_FAILED);
			} finally {
				closeCarefully(source);
				closeCarefully(target);
			}
			// reload keystore
			com.amazon.kindle.kindlet.internal.portability.g.d().b().K();
			log("I: ("+ DevCertInjectPatch.class.getName() + ") Developer certificates installed");
			return buildException(false, "Developer certificates installed", KEY_INSTALL_SUCCEEDED);
		}
		return null;
	}

	private static File getKeyStoreFile() {
		File f = new File(KEYSTORE_DIRECTORY + KEYSTORE_FILE);
		if (f.exists()) {
			log("I: ("+ DevCertInjectPatch.class.getName() + ") Not touching existing " + KEYSTORE_FILE);
			f = null;
		}
		return f;
	}

	private static KindletExecutionException buildException(
			boolean isActualFailure, String explanation, String key) {
		failedBefore = isActualFailure;
		return new KindletExecutionException(explanation,
				DevCertInjectPatch.class.getName(), key,
				DevCertInjectPatchResources.class.getName());
	}

	private static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[4096];
		for (int len = in.read(buffer); len != -1; len = in.read(buffer)) {
			out.write(buffer, 0, len);
		}
	}

	private static void closeCarefully(InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
			}
		}
	}

	private static void closeCarefully(OutputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
			}
		}
	}
}
