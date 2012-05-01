package com.mobileread.ixtab.jbpatch.bootstrap;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedExceptionAction;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

import sun.misc.Resource;
import sun.misc.URLClassPath;

import com.mobileread.ixtab.jbpatch.MD5;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.Patches;

class PatchingClassLoader extends URLClassLoader {

	private static final String PACKAGE_NOFIND = "com.mobileread.ixtab.jbpatch.bootstrap.";
	private static final String PACKAGE_NOPATCH = "com.mobileread.ixtab.jbpatch.";
	private static final String LOG_CLASS_NAME = "com.mobileread.ixtab.jbpatch.Log";
	private static final String LOG_INSTANCE_NAME = "INSTANCE";

	private final AccessControlContext acc;
	private final URLClassPath ucp;
	private PrintStream log;
	private final Map avoidPackages;

	static PatchingClassLoader inject() throws Exception {
		ClassLoader replacedLoader = PatchingClassLoader.class.getClassLoader();
		if (!(replacedLoader instanceof URLClassLoader)) {
			throw new IllegalStateException();
		}

		ClassLoader parentLoader = replacedLoader.getParent();
		if (parentLoader instanceof PatchingClassLoader) {
			return (PatchingClassLoader) parentLoader;
		}

		PatchingClassLoader patchLoader = new PatchingClassLoader(
				((URLClassLoader) replacedLoader).getURLs(), parentLoader,
				replacedLoader);
		replaceParent(replacedLoader, patchLoader);
		return patchLoader;
	}

	private static void replaceParent(ClassLoader victim,
			PatchingClassLoader newParent) throws NoSuchFieldException,
			IllegalAccessException {
		Field f = ClassLoader.class.getDeclaredField("parent");
		f.setAccessible(true);
		f.set(victim, newParent);
	}

	PatchingClassLoader(URL[] urls, ClassLoader parent, ClassLoader child) {
		super(urls, parent);
		ucp = new URLClassPath(urls);
		acc = AccessController.getContext();
		avoidPackages = getPackagesToAvoid(child);
		log = loadLog();
		onInit();
	}

	private Map getPackagesToAvoid(ClassLoader child) {
		try {
			Field pkg = ClassLoader.class.getDeclaredField("packages");
			pkg.setAccessible(true);
			return (Map) pkg.get(child);
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	private PrintStream loadLog() {
		try {
			Class clazz = loadClass(LOG_CLASS_NAME, true);
			Field instance = clazz.getDeclaredField(LOG_INSTANCE_NAME);
			return (PrintStream) instance.get(null);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return System.err;
	}

	private void onInit() {
		log("");
		log(""+new Date());
		log("=== Patching class loader initialized.  ===");
		log("");
		log("!Packages still handled by original loader:");
		Iterator it = avoidPackages.keySet().iterator();
		while (it.hasNext()) {
			log(it.next().toString());
		}
		log("===========================================");
		log("");
	}

	protected Class findClass(final String name) throws ClassNotFoundException {
		if (name.startsWith(PACKAGE_NOFIND))
			throw new ClassNotFoundException();
		try {
			return (Class) AccessController.doPrivileged(
					new PrivilegedExceptionAction() {
						public Object run() throws ClassNotFoundException {
							String path = name.replace('.', '/').concat(
									".class");
							Resource res = ucp.getResource(path, false);
							if (res != null) {
								try {
									return defineClass(name, res);
								} catch (IOException e) {
									throw new ClassNotFoundException(name, e);
								}
							} else {
								throw new ClassNotFoundException(name);
							}
						}
					}, acc);
		} catch (java.security.PrivilegedActionException pae) {
			throw (ClassNotFoundException) pae.getException();
		}
	}

	private Class defineClass(String name, Resource res) throws IOException {
		URL url = res.getCodeSourceURL();
		String pkgname = getPackageName(name);
		if (pkgname != null) {
			if (avoidPackages.containsKey(pkgname)) {
				throw new IOException();
			}
			defineOrVerifyPackage(res, url, pkgname);
		}
		return defineClass(name, res, url);
	}

	private String getPackageName(String classname) {
		int i = classname.lastIndexOf('.');
		if (i == -1) {
			return null;
		}
		String pkgname = classname.substring(0, i);
		return pkgname;
	}

	private void defineOrVerifyPackage(Resource res, URL url, String pkgname)
			throws IOException {
		Manifest man = res.getManifest();
		Package pkg = getPackage(pkgname);
		if (pkg != null) {
			verifyPackageSecurity(url, pkgname, pkg, man);
		} else {
			definePackage(url, pkgname, man);
		}
	}

	private void verifyPackageSecurity(URL url, String pkgname, Package pkg,
			Manifest man) {
		// Package found, so check package sealing.
		if (pkg.isSealed()) {
			// Verify that code source URL is the same.
			if (!pkg.isSealed(url)) {
				throw new SecurityException("sealing violation: package "
						+ pkgname + " is sealed");
			}

		} else {
			// Make sure we are not attempting to seal the package
			// at this code source URL.
			if ((man != null) && isSealed(pkgname, man)) {
				throw new SecurityException(
						"sealing violation: can't seal package " + pkgname
								+ ": already loaded");
			}
		}
	}

	private void definePackage(URL url, String pkgname, Manifest man) {
		if (man != null) {
			definePackage(pkgname, man, url);
		} else {
			definePackage(pkgname, null, null, null, null, null, null, null);
		}
	}

	private Class defineClass(String name, Resource res, URL url)
			throws IOException {
		byte[] b = res.getBytes();
		if (okToPatch(name)) {
			b = patch(name, b);
		}
		java.security.cert.Certificate[] certs = res.getCertificates();
		CodeSource cs = new CodeSource(url, certs);
		return defineClass(name, b, 0, b.length, cs);
	}

	private boolean okToPatch(String name) {
		return !name.startsWith(PACKAGE_NOPATCH);
	}

	Class defineClass(String name, byte[] b) {
		return defineClass(name, b, 0, b.length, (CodeSource) null);
	}

	private boolean isSealed(String name, Manifest man) {
		String path = name.replace('.', '/').concat("/");
		Attributes attr = man.getAttributes(path);
		String sealed = null;
		if (attr != null) {
			sealed = attr.getValue(Name.SEALED);
		}
		if (sealed == null) {
			if ((attr = man.getMainAttributes()) != null) {
				sealed = attr.getValue(Name.SEALED);
			}
		}
		return "true".equalsIgnoreCase(sealed);
	}

	void log(String msg) {
		log.println(msg);
		log.flush();
	}

	private byte[] patch(String name, byte[] input) {
		Object[] patches = Patches.get(name);
		if (patches == null || patches.length == 0) {
			return input;
		}

		byte[] output = input;
		String md5 = MD5.getMd5String(input);

		for (int i = 0; i < patches.length; ++i) {
			Patch p = (Patch) patches[i];
			output = p.patch(name, input, md5);
			if (output != input && i < patches.length - 1) {
				// patches can be chained, so update current state
				input = output;
				md5 = MD5.getMd5String(input);
			}
		}
		return output;
	}

}
