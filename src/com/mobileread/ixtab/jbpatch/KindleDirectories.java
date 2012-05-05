package com.mobileread.ixtab.jbpatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KindleDirectories {
	public static final String USERSTORE_DIRECTORY = "/mnt/us/opt/jbpatch";
	public static final String LOCAL_DIRECTORY = "/var/local/jbpatch";

	public static void init() {
		File var = new File(LOCAL_DIRECTORY);
		if (!var.exists())
			var.mkdir();
		File us = new File(USERSTORE_DIRECTORY);
		new SynchThread(us, var).start();
		try {
			// give synch thread a chance to run before we try to read files
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}

	private static class SynchThread extends Thread {

		private final File sourceDir;
		private final File targetDir;
		private final ShutdownStatus shutdown = new ShutdownStatus();
		private final Map knownFiles = new HashMap();
		private final FilenameFilter fileFilter = new JBPatchFilter();
		private boolean targetOk = true;
		
		private long lastSourceTimestamp = 0;
		private long lastTargetTimestamp = 0;

		public SynchThread(File sourceDir, File targetDir) {
			super("JBPatchSynchronizerThread");
			this.setDaemon(true);
			this.sourceDir = sourceDir;
			this.targetDir = targetDir;
		}

		public void run() {
			Runtime.getRuntime().addShutdownHook(new ShutdownThread(this));
			synchronized (shutdown) {
				log("I: Directory synchronization thread started");
				while (!shutdown.isRequested) {
					try {
						synchronize();
					} catch (Throwable t) {
						log("E: "
								+ new Date()
								+ ": unexpected error while synchronizing directories:");
						t.printStackTrace(Log.INSTANCE);
					}
					try {
						shutdown.wait(5000);
					} catch (InterruptedException e) {
					}
				}
				log("I: Directory synchronization thread exiting");
			}
		}

		private void synchronize() {
			if (!timestampsDiffer())
				return;
			
			File[] targetFiles = listTargetFiles();
			if (targetFiles == null)
				return;
			
			File[] sourceFiles = sourceDir.listFiles(fileFilter);
			if (sourceFiles == null)
				return;
			
			removeObsoleteFilesInTarget(sourceFiles, targetFiles);
			for (int i = 0; i < sourceFiles.length; ++i) {
				File f = sourceFiles[i];
				if (!f.isFile() || !f.canRead()) {
					continue;
				}
				synchronize(f);
			}
		}

		private boolean timestampsDiffer() {
			long sourceTimestamp = lastSourceTimestamp;
			long targetTimestamp = lastSourceTimestamp;
			try {
				sourceTimestamp = sourceDir.lastModified();
				targetTimestamp = targetDir.lastModified();
			} catch (Throwable t) {
				// just in case
				sourceTimestamp = lastSourceTimestamp;
				targetTimestamp = lastSourceTimestamp;
			}
			if (sourceTimestamp != lastSourceTimestamp || targetTimestamp != lastTargetTimestamp) {
				lastSourceTimestamp = sourceTimestamp;
				lastTargetTimestamp = targetTimestamp;
				return true;
			}
			return false;
		}

		private void removeObsoleteFilesInTarget(File[] sourceFiles,
				File[] targetFiles) {
			Set sourceNames = new HashSet();
			for (int i=0; i < sourceFiles.length; ++i) {
				sourceNames.add(sourceFiles[i].getName());
			}
			for (int i=0; i< targetFiles.length; ++i) {
				File f = targetFiles[i];
				if (!sourceNames.contains(f.getName())) {
					knownFiles.remove(f.getName());
					if (f.delete()) {
						log("I: "+new Date()+": deleted obsolete file "+f);
					} else {
						log("E: "+new Date()+": unable to delete obsolete file "+f);
					}
				}
			}
		}

		private void synchronize(File source) {
			boolean copy = false;
			File target = new File(targetDir + File.separator
					+ source.getName());
			FileModificationInfo info = (FileModificationInfo) knownFiles
					.get(source.getName());
			if (info == null) {
				copy = true;
			} else {
				if (source.lastModified() != info.sourceTimestamp) {
					//log("I: " + source + " was modified");
					copy = true;
				} else if (!target.exists()
						|| target.lastModified() != info.targetTimestamp) {
					//log("I: " + target + " is out of sync with " + source);
					copy = true;
				}
			}
			if (copy) {
				copyIfNeeded(source, target, info == null);
			}
		}

		private void copyIfNeeded(File source, File target,
				boolean checkThoroughly) {
			if (checkThoroughly) {
				if (filesAreEqual(source, target)) {
					FileModificationInfo info = new FileModificationInfo(
							source.lastModified(), target.lastModified());
					knownFiles.put(source.getName(), info);
					return;
				}
			}
			if (target.exists()) {
				if (!target.delete()) {
					log("E: " + new Date() + ": unable to delete " + target);
				}
			}
			try {
				copy(source, target);
				FileModificationInfo info = new FileModificationInfo(
						source.lastModified(), target.lastModified());
				knownFiles.put(source.getName(), info);
				log("I: " + new Date() + ": synchronized " + source.getName());
			} catch (Throwable t) {
				log("E: " + new Date() + ": error while copying " + source+" :");
				t.printStackTrace(Log.INSTANCE);
			}
		}

		private void copy(File source, File target) throws IOException {

			FileInputStream from = null;
			FileOutputStream to = null;
			try {
				byte[] buffer = new byte[4096];
				from = new FileInputStream(source);
				to = new FileOutputStream(target);
				for (int read = from.read(buffer); read != -1; read = from.read(buffer)) {
					to.write(buffer, 0, read);
				}
			} finally {
				if (from != null) {
					from.close();
				}
				if (to != null) {
					to.close();
				}
			}

		}

		private boolean filesAreEqual(File source, File target) {
			String s = getMd5(source);
			String t = getMd5(target);
			return (s != null && t != null && s.equals(t));
		}

		private String getMd5(File f) {

			try {
				byte[] bytes = new byte[(int) f.length()];
				FileInputStream fis = new FileInputStream(f);
				int read = fis.read(bytes);
				if (read != bytes.length) {
					log("E: " + new Date() + ": error while reading " + f
							+ "; expected " + bytes.length + " bytes, but got "
							+ read);
					return null;
				}
				String md5 = MD5.getMd5String(bytes);
				return md5;
			} catch (Throwable t) {
				return null;
			}
		}

		private File[] listTargetFiles() {
			if (!targetDir.exists()) {
				knownFiles.clear();
				if (targetOk) {
					log("W: " + new Date() + ": " + LOCAL_DIRECTORY
							+ " does not exist, attempting to re-create it!");
				}
				if (targetDir.mkdir()) {
					log("I: " + new Date() + ": created " + LOCAL_DIRECTORY);
					targetOk = true;
				} else {
					if (targetOk) {
						log("E: " + new Date() + ": failed to create "
								+ LOCAL_DIRECTORY);
					}
					targetOk = false;
				}
			}
			return targetOk ? targetDir.listFiles(fileFilter) : null;
		}

		private void log(String msg) {
			Log.INSTANCE.println(msg);
		}
	}

	private static class ShutdownStatus {
		private volatile boolean isRequested = false;
	}

	private static class FileModificationInfo {
		private final long sourceTimestamp;
		private final long targetTimestamp;

		public FileModificationInfo(long sourceTimestamp, long targetTimestamp) {
			super();
			this.sourceTimestamp = sourceTimestamp;
			this.targetTimestamp = targetTimestamp;
		}

	}

	private static class ShutdownThread extends Thread {

		private final SynchThread sync;

		public ShutdownThread(SynchThread sync) {
			super("JBPatchSynchronizerShutdownThread");
			this.sync = sync;
		}

		public void run() {
			synchronized (sync.shutdown) {
				sync.shutdown.isRequested = true;
				sync.shutdown.notifyAll();
			}
		}
	}

	private static class JBPatchFilter implements FilenameFilter {

		public boolean accept(File dir, String name) {
			if (name.equals(PatcherConfiguration.CONFIGFILE_NAME)) {
				return true;
			}
			if (name.endsWith(PatcherConfiguration.PATCH_EXTENSION)) {
				if (PatcherConfiguration.checkForWrongCharacters(name) == null) {
					return true;
				}
			}
			return false;
		}

	};
}
