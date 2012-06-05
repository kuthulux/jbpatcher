package com.mobileread.ixtab.jbpatch.conf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.mobileread.ixtab.jbpatch.Log;

public class LocalizationResource implements KeyValueResource {

	/* This won't change during the JVM lifetime. */
	private static final String[] localeCodes = determineLocalesSearchOrder();
	
	private static String[] determineLocalesSearchOrder() {
		Locale current = Locale.getDefault();
		// at most 3.
		List list = new ArrayList(3);
		list.add(current);
		Locale last = current;
		current = new Locale(last.getLanguage());
		if (!current.equals(last)) {
			list.add(current);
		}
		if (!current.equals(Locale.ENGLISH)) {
			list.add(Locale.ENGLISH);
		}

		String[] result = new String[list.size()];
		for (int i = 0; i < result.length; ++i) {
			result[i] = ((Locale) list.get(i)).toString();
		}
		return result;
	}

	
	
	private final KeyValueFile[] localeFiles;

	LocalizationResource(String baseName) {
		KeyValueFile[] max = new KeyValueFile[localeCodes.length];
		int count =0;
		for (int i=0; i < max.length; ++i) {
			File file = PatchResource.determineFile(baseName, localeCodes[i]);
			if (file.exists() && file.isFile() && file.canRead()) {
				try {
					KeyValueFile loaded = new KeyValueFile(KeyValueFile.FLAG_NONE, file, null);
					max[count++] = loaded;
				} catch (IOException e) {
					Log.INSTANCE.println("E: Unable to load "+ file);
				}
			}
		}
		if (count == max.length) {
			localeFiles = max;
		} else {
			 localeFiles = new KeyValueFile[count];
			 System.arraycopy(max, 0, localeFiles, 0, count);
		}
	}

	public String getValue(String key) {
		for (int i=0; i < localeFiles.length; ++i) {
			String value = localeFiles[i].getValue(key);
			if (value != null) {
				return value;
			}
		}
		// nothing found: return literal key.
		return key;
	}

	public boolean setValue(String key, String resource) {
		throw new UnsupportedOperationException();
	}

}
