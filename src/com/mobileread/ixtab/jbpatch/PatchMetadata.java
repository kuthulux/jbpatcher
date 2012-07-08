package com.mobileread.ixtab.jbpatch;

import java.util.ArrayList;
import java.util.List;

public final class PatchMetadata {
	

	public final Patch patch;
	public final List supportedDevices = new ArrayList();
	
	public PatchMetadata(Patch patch) {
		this.patch = patch;
	}
	
	/* for chaining */
	public PatchMetadata withDevice(PatchableDevice dev) 
	{
		supportedDevices.add(dev);
		return this;
	}

	public static final class PatchableDevice {
		public final KindleDevice device;
		public final List supportedClasses = new ArrayList();
		
		public PatchableDevice(KindleDevice device) {
			this.device = device;
		}
		
		/* for chaining */
		public PatchableDevice withClass(PatchableClass clazz) 
		{
			supportedClasses.add(clazz);
			return this;
		}

	}
	
	public static final class PatchableClass {
		public final String className;
		public final List checksums = new ArrayList();

		public PatchableClass(String className) {
			this.className = className;
		}
		
		/* for chaining */
		public PatchableClass withChecksums(String before, String after) {
			checksums.add(new ClassChecksum(before, after));
			return this;
		}
	}
	
	public static final class ClassChecksum {
		public final String beforePatch;
		public final String afterPatch;
		
		public ClassChecksum(String beforePatch, String afterPatch) {
			super();
			this.beforePatch = beforePatch;
			this.afterPatch = afterPatch;
		}
	}

}
