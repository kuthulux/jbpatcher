package com.mobileread.ixtab.jbpatcher;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;

import serp.bytecode.BCClass;
import serp.bytecode.Code;
import serp.bytecode.Instruction;
import serp.bytecode.Project;

/**
 * A <tt>Patch</tt> is a class that can modify one or more versions of one or
 * more class definitions, before the class definition is actually loaded by the
 * JVM.
 * 
 * <b>IMPORTANT</b>: because of the way the system is designed, a patch must
 * consist of a single file. The effect of this is that inner classes (whether
 * named or anonymous) are not allowed inside patches, because they would result
 * in additional files after compilation.
 * 
 * @author ixtab
 */
public abstract class Patch {

	/*
	 * ***********************************************************************
	 * This is what you need to implement, and a few methods which may come in
	 * handy.
	 * ***********************************************************************
	 */

	/**
	 * Returns an array of descriptors informing about which versions of which
	 * classes this patch can handle. For a patch to be considered valid, this
	 * must return a non-empty array.
	 */
	protected abstract Descriptor[] getDescriptors();

	/**
	 * Actually applies the patch to the given input. The class definition can
	 * be altered in whichever way you see fit.
	 * 
	 * @param md5
	 *            the MD5 sum of the original binary class content. This is
	 *            guaranteed to be among the supported MD5 values of either
	 *            descriptor this patch makes available.
	 * @param clazz
	 *            the class corresponding to the given MD5 sum.
	 * @return <b><tt>null</tt></b> if the input class has been
	 *         <b>successfully</b> modified, and the patch should be applied.
	 *         Any other string (error message) if the patch should not be
	 *         applied, for instance because something failed unexpectedly. In
	 *         the latter case, the message will be logged, and any changes to
	 *         the class definition are discarded, i.e. the result is the same
	 *         as if this method had never been invoked.
	 */
	public abstract String perform(String md5, BCClass clazz) throws Throwable;

	Descriptor[] descriptors() {
		if (descriptors == null) {
			descriptors = getDescriptors();
		}
		return descriptors;
	}

	protected final String id() {
		return id;
	}

	protected static final PrintStream logger = Log.INSTANCE;

	protected static final void log(String message) {
		logger.println(message);
	}

	// may be useful for debugging
	public static void dump(Code code) {
		if (code != null) {
			Instruction[] inss = code.getInstructions();
			for (int i = 0; i < inss.length; ++i) {
				Instruction ins = inss[i];
				log(i + " " + ins);
			}
		}
	}

	/*
	 * *********************************************************************
	 * Everything below here is implementation details; you don't need to be
	 * concerned with it.
	 * *********************************************************************
	 */

	public final byte[] patch(String className, byte[] input, final String md5) {
		try {
			Descriptor descriptor = getDescriptorFor(className);
			if (descriptor.matches(md5)) {
				BCClass clazz = loadBCClass(input);
				log("D: about to invoke "+id+" for "+className);
				String error = perform(md5, clazz);
				if (error == null) {
					log("I: Patched " + className + " (" + md5 + ") using "
							+ id);
					Patches.reportActive();
					return clazz.toByteArray();
				}
				log("E: " + id + " failed to patch " + className + " (" + md5
						+ "): " + error);
			} else {
				log("W: " + id + " does not support MD5 " + md5 + " for "
						+ className);
			}
		} catch (Throwable t) {
			log("E: " + id + " failed to patch " + className + " (" + md5
					+ "):");
			t.printStackTrace(logger);
		}
		return input;
	}

	private Descriptor getDescriptorFor(String className) {
		Descriptor[] descriptors = descriptors();
		if (descriptors != null && descriptors.length != 0) {
			for (int i = 0; i < descriptors.length; ++i) {
				if (className.equals(descriptors[i].className)) {
					return descriptors[i];
				}
			}
		}
		return null;
	}

	static BCClass loadBCClass(byte[] input) {
		return new Project().loadClass(new ByteArrayInputStream(input));
	}

	private String id;
	private Descriptor[] descriptors;

	final Patch setId(String id) {
		this.id = id;
		
		// for chaining
		return this;
	}

}
