package com.mobileread.ixtab.jbpatch;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mobileread.ixtab.jbpatch.conf.KeyValueFile;

public class PatchRepository {
	
	public static class FilenamesFilter implements FilenameFilter {
		
		private final boolean allowAuxiliaryFiles;

		public FilenamesFilter(boolean allowAuxiliaryFiles) {
			super();
			this.allowAuxiliaryFiles = allowAuxiliaryFiles;
		}

		public boolean accept(File dir, String name) {
			if (allowAuxiliaryFiles && name.endsWith(EXTENSION_TXT)) {
				return true;
			}
			if (name.endsWith(EXTENSION_PATCH_STANDALONE) || name.endsWith(EXTENSION_PATCH_JARRED)) {
				if (PatcherConfiguration.checkForWrongCharacters(name) == null) {
					return true;
				}
			}
			return false;
		}

	};

	public static class PatchAndFile {
		Patch patch;
		File file;
		
		public PatchAndFile(File file) {
			this.file = file;
		}
	}
	
	private static final String CONFIGFILE_NAME = "CONFIG2.txt";
	private static final String EXTENSION_PATCH_STANDALONE = ".class";
	private static final String EXTENSION_PATCH_JARRED = ".jar";
	private static final String EXTENSION_TXT = ".txt";
	
	private static final String VALUE_ENABLED = "enabled";
	private static final String VALUE_DISABLED = "disabled";
	
	private static final PatchRepository instance = new PatchRepository();
	
	private final KeyValueFile backend;
	
	public static PatchRepository getInstance() {
		return instance;
	}
	
	private PatchRepository() {
		File primary = new File(KindleDirectories.LOCAL_DIRECTORY + "/" + CONFIGFILE_NAME);
		File secondary = new File(KindleDirectories.USERSTORE_DIRECTORY + "/" + CONFIGFILE_NAME);
		backend = new KeyValueFile(KeyValueFile.FLAG_WRITABLE, primary, secondary);
	}
	
	Map initialize() {
		boolean updateBackend = false;
		File[] files = listPresentPatchFiles();
		List keysInBackend = new ArrayList(backend.listKeys());
		Map patchStates = new TreeMap();
		for (int i=0; i < files.length; ++i) {
			Boolean active = null;
			String id = getPatchId(files[i].getName());
			keysInBackend.remove(id);
			String status = backend.getValue(id);
			if (status == null) {
				log("I: "+id+" was not found in configuration, adding entry and enabling patch");
				if (!backend.setValue(id, VALUE_ENABLED, false)) {
					log("E: failed to add patch "+id+" to configuration!");
				}
				active = Boolean.TRUE;
				updateBackend = true;
			} else {
				active = parseStatus(status);
				if (active == null) {
					log("W: "+id+" has wrong entry in configuration file, fixing entry and enabling patch");
					if (!backend.setValue(id, VALUE_ENABLED, false)) {
						log("E: failed to fix state for patch "+id+" in configuration!");
					}
					active = Boolean.TRUE;
					updateBackend = true;
				}
			}
			patchStates.put(files[i], active);
		}
		for (int i=0; i < keysInBackend.size(); ++i) {
			String id = (String)keysInBackend.get(i);
			if (!backend.remove(id, false)) {
				log("E: failed to remove patch "+id+" from configuration!");
			}
			updateBackend = true;
		}
		if (updateBackend) {
			if (!backend.commitChanges()) {
				log("E: failed to update configuration file!");
			} else {
				log("I: updated configuration file");
			}
		}
		return patchStates;
	}
	
	private File[] listPresentPatchFiles() {
		File dir = new File(KindleDirectories.LOCAL_DIRECTORY);
		if (!dir.exists() || !dir.isDirectory()) {
			return new File[0];
		}
		return dir.listFiles(new FilenamesFilter(false));
	}

	private void log(String msg) {
		Log.INSTANCE.println(msg);
	}
	
	private Boolean parseStatus(String text) {
		if (VALUE_ENABLED.equals(text)) {
			return Boolean.TRUE;
		}
		if (VALUE_DISABLED.equals(text)) {
			return Boolean.FALSE;
		}
		return null;
	}
	
	private String getPatchId(String filename) {
		if (filename.endsWith(PatcherConfiguration.PATCH_EXTENSION_STANDALONE)) {
			return filename.substring(0, filename.length() - PatcherConfiguration.PATCH_EXTENSION_STANDALONE.length());
		} else if (filename.endsWith(PatcherConfiguration.PATCH_EXTENSION_JARRED)) {
			return filename.substring(0, filename.length() - PatcherConfiguration.PATCH_EXTENSION_JARRED.length());
		}
		throw new IllegalStateException();
	}

	void addAvailable(Patch patch, Boolean active) {
		// TODO Auto-generated method stub
		
	}
}
