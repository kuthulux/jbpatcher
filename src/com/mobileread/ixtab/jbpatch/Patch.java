package com.mobileread.ixtab.jbpatch;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.net.URL;
import java.security.Permission;

import com.mobileread.ixtab.jbpatch.conf.KeyValueResource;
import com.mobileread.ixtab.jbpatch.conf.PatchResource;

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
public abstract class Patch implements Comparable {

	public static final int RESOURCE_REQUIREMENT_NONE = 0;
	public static final int RESOURCE_REQUIREMENT_LOCALIZATION = 1;
	public static final int RESOURCE_REQUIREMENT_CONFIGURATION_STATIC = 2;
	public static final int RESOURCE_REQUIREMENT_CONFIGURATION_DYNAMIC = 4;
	
	/*
	 * ***********************************************************************
	 * This is what you need to implement, and a few methods which may come in
	 * handy.
	 * ***********************************************************************
	 */

	public int getResourceRequirements() {
		return RESOURCE_REQUIREMENT_NONE;
	}
	
	/**
	 * Returns an array of descriptors informing about which versions of which
	 * classes this patch can handle. For a patch to be considered valid, this
	 * must return a non-empty array.
	 */
//	protected abstract Descriptor[] getDescriptors();

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

	/**
	 * If the patch is parameterizable and/or localizable, it should return here
	 * the URL where the parameters can be found. The recommended procedure is
	 * to provide a URL which is INSIDE the same jar as the patch itself, in
	 * order to keep patches "self-contained". You could also use references to
	 * files (but note that future versions of jbpatch might be installed in a
	 * different directory), or even online URLs. I strongly recommend the first
	 * variant.
	 * 
	 * @return a URL pointing to the resources, if the patch is parameterizable,
	 *         or <tt>null</tt> otherwise
	 */
	protected URL getResourcesUrl() {
		return null;
	}
	
	private KeyValueResource localizationResource = null;
	
	private void initResources() {
		int req = getResourceRequirements();
		if ((req & RESOURCE_REQUIREMENT_LOCALIZATION) == RESOURCE_REQUIREMENT_LOCALIZATION) {
			localizationResource = PatchResource.getResource(this, PatchResource.TYPE_LOCALIZATION);
		}
	}

	/**
	 * Returns a localized parameterization item. This will simply return the
	 * parameter given if you do not override the getResourcesUrl() method.
	 * Otherwise, it will return the value associated with the given key,
	 * according to the lookup procedure explained in DEVELOPERS.txt. In short,
	 * lookup is attempted from "most specific" to "most general" locale,
	 * defaulting to english if no other value is found. If no value is found
	 * even for english, then the key is returned literally.
	 * 
	 * @param key
	 *            the name of the parameter to look up
	 * @return the localized value for the given key, or the key itself if no
	 *         value is found
	 */
	public final String localize(String key) {
		if (localizationResource != null) {
			return localizationResource.getValue(key);
		} else {
			return key;
		}
	}

	/**
	 * Returns all additional permissions that the patch requires in order to
	 * function properly. Override this method if your patch needs additional
	 * permissions. Be as specific as you can: if you only need to read/write to
	 * a single file, then only request a tailored FilePermission, etc.
	 * 
	 * @return additional permissions that the patch requires
	 */
	public Permission[] getRequiredPermissions() {
		return null;
	}

//	Descriptor[] descriptors() {
//		if (descriptors == null) {
//			descriptors = getDescriptors();
//		}
//		return descriptors;
//	}

	public final String id() {
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
	
	protected String md5(BCClass clazz) {
		return MD5.getMd5String(clazz.toByteArray());
	}



	/*
	 * *********************************************************************
	 * Everything below here is implementation details; you don't need to be
	 * concerned with it.
	 * *********************************************************************
	 */

	public final byte[] patch(String className, byte[] input, final String md5) {
		try {
			BCClass clazz = loadBCClass(input);
			// log("D: about to invoke "+id+" for "+className);
			String error = perform(md5, clazz);
			if (error == null) {
				log("I: " + id+ " applied to "+ className + " (" + md5 + ")");
				Patches.reportActive();
				return clazz.toByteArray();
			}
			log("E: " + id + " failed to patch " + className + " (" + md5
					+ "): " + error);
		} catch (Throwable t) {
			log("E: " + id + " failed to patch " + className + " (" + md5
					+ "):");
			t.printStackTrace(logger);
		}
		return input;
	}

	static BCClass loadBCClass(byte[] input) {
		return new Project().loadClass(new ByteArrayInputStream(input));
	}

	private String id;

	final Patch setId(String id) {
		this.id = id;

		initResources();

		// for chaining
		return this;
	}

	protected abstract int getPatchVersion();

	private String description = null;
	
	public final String description() {
		if (description == null) {
			description = getDescription();
		}
		return description;
	}

	protected final String getDescription() {
		return null;
	}
	
	public abstract String getPatchName();
	
	public abstract PatchMetadata getMetadata();

	private PatchMetadata metadata = null;
	
	public final PatchMetadata metadata() {
		if (metadata == null) {
			metadata = getMetadata();
		}
		return metadata;
	}
	
	public final Descriptor[] getDescriptors() {
		return null;
	}

	public int compareTo(Object arg0) {
		Patch other = (Patch)arg0;
		return id().compareTo(other.id());
	}

}
