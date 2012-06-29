package com.mobileread.ixtab.jbpatch.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.mobileread.ixtab.jbpatch.KindleDirectories;
import com.mobileread.ixtab.jbpatch.Log;

public class JBPatchResource {
	private static final String FILE_EXTENSION = ".txt";
	private static final String FILE_DELIMITER = "-";
	public static final int TYPE_LOCALIZATION = 0;
	public static final int TYPE_CONFIGURATION = 1;

	private JBPatchResource() {
	}

	public static KeyValueResource getResource(ResourceMapProvider provider, int resourceType) {
		switch (resourceType) {
		case TYPE_LOCALIZATION:
			return new LocalizationResource(provider);
		}
		throw new IllegalArgumentException("Unsupported resource type "
				+ resourceType);
	}

	static File useFile(String baseName, String qualifier, ResourceMapProvider provider) {
		String name = KindleDirectories.LOCAL_DIRECTORY + "/" + baseName;
		if (qualifier != null) {
			name += FILE_DELIMITER + qualifier;
		}
		name += FILE_EXTENSION;
		File file = new File(name);
		if (file.exists()) {
			if (file.isFile() && file.canRead()) {
				return file;
			} else {
				Log.INSTANCE.println("E: "+ file +" is not a file, or unreadable: refusing to try to overwrite it");
				return null;
			}
		}
		
		Map entries = provider.getDefaultResourceMap(qualifier);
		if (entries != null && initializeFileFromMap(file, entries)) {
			return file;
		}
		return null;
	}

	private static boolean initializeFileFromMap(File file, Map map) {
		boolean ok = false;
		try {
			PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			// we are explicitly using CR/LF here, to make all those Windows folks happy.
			out.print("# This file is a configuration file, and may contain important settings.\r\n");
			out.print("# Change this file at your own risk, and only if you know what you are doing.\r\n\r\n");
			Iterator entries = map.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry entry = (Entry) entries.next();
				out.print((String) entry.getKey());
				out.print("=");
				out.print(KeyValueFile.escape((String) entry.getValue()));
				out.print("\r\n");
			}
			out.flush();
			out.close();
			ok = true;
		} catch (IOException io) {
			io.printStackTrace(Log.INSTANCE);
		}
		if (ok && file.exists() && file.isFile() && file.canRead()) {
			Log.INSTANCE.println("I: File "+file+" successfully initialized");
			return true;
		} else {
			Log.INSTANCE.println("E: Failed to initialize file "+file);
			file.delete();
			return false;
		}
	}

}
