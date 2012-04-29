package com.mobileread.ixtab.jbpatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class PatcherConfiguration {
	private static final int KINDLE_MOUNT_WAIT_MAX_CYCLES = 60;
	private static final int KINDLE_MOUNT_WAIT_CYCLE_MS = 500;
	public static final String KINDLE_MOUNT_DIRECTORY = "/mnt/us";
	public static final String[] KINDLE_MOUNT_CHECK_CMD = new String[] {"/bin/cat", "/proc/mounts" };
	public static final String KINDLE_BASE_DIRECTORY = "/mnt/us/opt/jbpatch";
	public static final String CONFIGFILE_NAME = "CONFIG.TXT";
	public static final String PATCH_EXTENSION = ".jbpatch";
	private static final char[] SPECIAL_CHARS_OK = new char[] { '.', '_', '-',
			'@' };

	private final File base;

	PatcherConfiguration() {
		base = determineBaseDirectory();
	}

	private File determineBaseDirectory() {
		if (Environment.isKindle()) {
			waitUntilUserPartitionMounted();
			return new File(KINDLE_BASE_DIRECTORY);
		}
		try {
			File tmp = File.createTempFile("jbpatch", ".tmp");
			File dir = tmp.getParentFile();
			if (!tmp.delete()) {
				tmp.deleteOnExit();
			}
			log("I: Base directory is: " + dir);
			return dir;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void waitUntilUserPartitionMounted() {
		try {
			long timestamp = System.currentTimeMillis();
			boolean announcedWaiting = false;
			int cycles = 0;
			while (!isUserPartitionMounted()) {
				if (!announcedWaiting) {
					log("I: "+ KINDLE_MOUNT_DIRECTORY+" not mounted yet, waiting until it's mounted");
					announcedWaiting = true;
				}
				if (cycles++ >= KINDLE_MOUNT_WAIT_MAX_CYCLES) {
					log("E: "+ KINDLE_MOUNT_DIRECTORY + " still not mounted after " + (System.currentTimeMillis() - timestamp) + " ms, giving up");
					return;
				}
				try {
					Thread.sleep(KINDLE_MOUNT_WAIT_CYCLE_MS);
				} catch (InterruptedException e) {}
			}
			log("I: "+ KINDLE_MOUNT_DIRECTORY + " confirmed mounted after " + (System.currentTimeMillis() - timestamp) + " ms");
		} catch (IOException e) {
			e.printStackTrace(Log.INSTANCE);
		}
	}

	private boolean isUserPartitionMounted() throws IOException {
		Process p = Runtime.getRuntime().exec(KINDLE_MOUNT_CHECK_CMD);
		BufferedReader lines = new BufferedReader(new InputStreamReader(p.getInputStream()));
		return isPartitionNameInMounts(lines);
	}

	private boolean isPartitionNameInMounts(BufferedReader lines) throws IOException {
		// every line is expected to be in the format of /proc/mounts
		boolean mounted = false;
		for (String line = lines.readLine(); line != null; line = lines.readLine()) {
			StringTokenizer tokens = new StringTokenizer(line);
			// we're interested in the second token
			if (!tokens.hasMoreTokens()) continue;
			tokens.nextToken();
			if (!tokens.hasMoreTokens()) continue;
			String token = tokens.nextToken();
			if (KINDLE_MOUNT_DIRECTORY.equals(token)) {
				mounted = true;
				break;
			}
		}
		lines.close();
		return mounted;
	}

	private void log(String msg) {
		Log.INSTANCE.println(msg);
	}

	public List getActiveFiles() {
		List files = new ArrayList();
		if (!base.exists()) {
			log("W: " + base + " does not exist; no patches loaded.");
			return files;
		}
		if (!base.isDirectory() || !base.canRead()) {
			log("E: "
					+ base
					+ " is not a directory, or not readable; no patches loaded.");
			return files;
		}

		File conf = new File(base.getPath() + File.separator + CONFIGFILE_NAME);
		if (!conf.exists()) {
			log("W: " + conf + " does not exist; no patches loaded.");
			return files;
		}
		if (!conf.isFile() || !conf.canRead()) {
			log("E: " + conf
					+ " is not a file, or not readable; no patches loaded.");
			return files;
		}

		List filenames = parseConfigurationFile(conf);
		fillValidFiles(files, filenames);
		return files;
	}

	private List parseConfigurationFile(File conf) {
		List names = new ArrayList();
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(
					new FileInputStream(conf)));
			int ln = 0;
			for (String line = r.readLine(); line != null; line = r.readLine()) {
				++ln;
				line = line.trim();
				if (line.length() == 0 || line.startsWith("#")) {
					continue;
				}
				Character wrong = checkForWrongCharacters(line);
				if (wrong != null) {
					log("W: line " + ln + " in file " + conf
							+ " contains disallowed character: " + wrong);
					log("W: only the characters A-Z,a-z,0-9, and any of \""
							+ new String(SPECIAL_CHARS_OK)
							+ "\" are allowed for filenames.");
					continue;
				}
				if (!line.endsWith(PATCH_EXTENSION)) {
					log("W: line " + ln + " in file " + conf
							+ " ignored: patches must have " + PATCH_EXTENSION
							+ " extension");
					continue;
				}
				names.add(line);
			}
			r.close();
		} catch (IOException e) {
			log("E: error while reading " + conf + ", no patches loaded:");
			e.printStackTrace(Log.INSTANCE);
			return new ArrayList();
		}
		return names;

	}

	private Character checkForWrongCharacters(String line) {
		// no regex support, so do it manually
		char[] chars = line.toCharArray();
		for (int i = 0; i < chars.length; ++i) {
			char c = chars[i];
			if (c >= '0' && c <= '9')
				continue;
			if (c >= 'a' && c <= 'z')
				continue;
			if (c >= 'A' && c <= 'Z')
				continue;
			if (isAllowedSpecialChar(c))
				continue;
			return new Character(c);
		}
		return null;
	}

	private boolean isAllowedSpecialChar(char c) {
		for (int i = 0; i < SPECIAL_CHARS_OK.length; ++i) {
			if (c == SPECIAL_CHARS_OK[i])
				return true;
		}
		return false;
	}

	private void fillValidFiles(List files, List filenames) {
		Iterator names = filenames.iterator();
		while (names.hasNext()) {
			File file = new File(base.getPath() + File.separator
					+ (String) names.next());
			if (seemsValidFile(file)) {
				files.add(file);
			}
		}
	}

	private boolean seemsValidFile(File file) {
		if (!file.exists()) {
			log("W: " + file + " does not exist.");
			return false;
		}
		if (!file.isFile() || !file.canRead()) {
			log("E: " + file + " is not a file, or not readable.");
			return false;
		}
		return true;
	}
}
