package com.mobileread.ixtab.jbpatch.conf;

import java.io.File;

import com.mobileread.ixtab.jbpatch.KindleDirectories;
import com.mobileread.ixtab.jbpatch.Patch;

public class PatchResource {
	private static final String FILE_EXTENSION = ".txt";
	private static final String FILE_DELIMITER = "-";
	public static final int TYPE_LOCALIZATION = 0;

	private PatchResource() {
	}

	public static KeyValueResource getResource(Patch patch, int resourceType) {
		switch (resourceType) {
		case TYPE_LOCALIZATION:
			return new LocalizationResource(patch.id());
		}
		throw new IllegalArgumentException("Unsupported resource type "
				+ resourceType);
	}

	static File determineFile(String baseName, String qualifier) {
		String name = KindleDirectories.LOCAL_DIRECTORY + "/" + baseName;
		if (qualifier != null) {
			name += FILE_DELIMITER + qualifier;
		}
		name += FILE_EXTENSION;
		return new File(name);
	};
}
