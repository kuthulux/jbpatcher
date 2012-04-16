package com.mobileread.ixtab.jbpatcher;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mobileread.ixtab.jbpatcher.builtin.DeviceInfoPatch;

import serp.bytecode.BCClass;
import serp.bytecode.Project;

public class Patches {

	private static final String PCL_CLASSNAME = "com.mobileread.ixtab.jbpatcher.bootstrap.PatchingClassLoader";
	private static final String PCL_DEFINECLASS_METHODNAME = "defineClass";

	private static final Map patches = new TreeMap();
	private static final PrintStream log = Log.INSTANCE;
	
	private Patches() {
	}
	
	private static void log(String msg) {
		log.println(msg);
	}

	public static Object[] get(String className) {
		List result = (List) patches.get(className);
		return result == null ? null : result.toArray();
	}

	private static void add(Patch patch) {
		Descriptor[] descriptors = validateDescriptors(patch);
		for (int i = 0; i < descriptors.length; ++i) {
			register(patch, descriptors[i]);
		}
	}

	private static Descriptor[] validateDescriptors(Patch patch) {
		Descriptor[] d = patch.descriptors();
		if (d == null || d.length == 0) {
			log("W: " + patch.id()
					+ " not considered because it does not provide descriptors");
			return new Descriptor[0];
		}
		return d;
	}

	private static void register(Patch patch, Descriptor d) {
		List handlers = getNonNullList(d.className);
		handlers.add(patch);
		log("I: Registered " + patch.id() + " for " + d.className);
	}

	private static List getNonNullList(String className) {
		List list = (List) patches.get(className);
		if (list == null) {
			list = new ArrayList();
			patches.put(className, list);
		}
		return list;
	}

	private static void add(File file) {
		try {
			add(file.getName(), new BufferedInputStream(new FileInputStream(
					file)));
		} catch (FileNotFoundException e) {
		}
	}

	private static void add(String id, InputStream stream) {
		ClassLoader cl = Patches.class.getClassLoader();
		if (isPatchingClassLoader(cl)) {
			try {
				Patch p = instantiate(stream, cl, id);
				if (p != null) {
					p.setId(id);
					add(p);
				}
			} catch (Throwable t) {
				log("E: while instantiating " + id + ": ");
				t.printStackTrace(log);
			}
		}
	}

	private static boolean isPatchingClassLoader(ClassLoader cl) {
		return cl.getClass().getName().equals(PCL_CLASSNAME);
	}

	private static Patch instantiate(InputStream stream, ClassLoader cl, String potentialId)
			throws Throwable {
		BCClass bc = new Project().loadClass(stream);
		String name = bc.getName();
		byte[] bytes = bc.toByteArray();
		try {
			return instantiate(name, bytes, cl);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause != null && cause instanceof LinkageError) {
				try {
					Class.forName(name);
					log("E: "+potentialId+" not instantiated: another class with name "+name+" is already loaded");
					return null;
				} catch (Throwable t) {
				}
			}
			throw e;
		}
	}

	private static Patch instantiate(String name, byte[] bytes, ClassLoader cl)
			throws Throwable {
		Class c = defineClass(cl, name, bytes);
		if (!Patch.class.isAssignableFrom(c)) {
			log("E: "+name+" is not a valid patch.");
			return null;
		}
		return (Patch) c.newInstance();
	}

	private static Method defineClass = null;

	private static synchronized Class defineClass(ClassLoader cl, String name,
			byte[] bytes) throws Throwable {

		ensureDefineClassMethodIsSet(cl);

		return (Class) defineClass.invoke(cl, new Object[] { name, bytes });
	}

	private static void ensureDefineClassMethodIsSet(ClassLoader cl)
			throws NoSuchMethodException {
		if (defineClass == null) {
			Class clc = cl.getClass();
			defineClass = clc.getDeclaredMethod(PCL_DEFINECLASS_METHODNAME,
					new Class[] { String.class, byte[].class });
			defineClass.setAccessible(true);
		}
	}

	private static boolean initialized = false;
	
	public static void init() {
		if (initialized) {
			return;
		}
		initialized = true;
		addBuiltins();
		addExternals();
	}

	private static void addBuiltins() {
		// why on earth is that cast needed here?!
		add(((Patch)new DeviceInfoPatch()).setId("(builtin) DeviceInfo"));
	}

	private static void addExternals() {
		List fileList = new PatcherConfiguration().getActiveFiles();
		Iterator files = fileList.iterator();
		while (files.hasNext()) {
			File file = (File) files.next();
			add(file);
		}
	}

	private static int active = 0;
	static void reportActive() {
		++active;
	}
	
	public static int getActiveCount() {
		return active;
	}
}
