package com.mobileread.ixtab.patch.passwd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.mobileread.ixtab.jbpatch.Log;

public class PasswordStore {

	private static final File BACKEND = new File("/var/local/ixtab-patch-passwd.bin");
	private static final PasswordStore instance = new PasswordStore();
	private final Map map = load();

	private PasswordStore() {
		
	}
	
	public static Password get(String key) {
		if (key == null) {
			return null;
		}
		return (Password) instance.map.get(key);
	}
	
	public static synchronized boolean set(String key, Password passwd) {
		instance.map.put(key, passwd);
		boolean ok = instance.store();
		if (!ok) {
			instance.map.remove(key);
		} else {
//			Log.INSTANCE.println("I: password added for "+key);
		}
		return ok;
	}
	
	public static synchronized boolean remove(String key, Password passwd) {
		Password existing = (Password) instance.map.get(key);
		if (existing == null || !existing.equals(passwd)) {
			return false;
		}
		instance.map.remove(key);
		boolean ok = instance.store();
		if (!ok) {
			instance.map.put(key, passwd);
		} else {
//			Log.INSTANCE.println("I: password removed from "+key);
		}
		return ok;
	}
	private Map load() {
		InputStream fis = null, zis = null;
		ObjectInputStream ois = null;
		Map map = null;
		try {
			if (BACKEND.exists()) {
				fis = new FileInputStream(BACKEND);
				zis = new GZIPInputStream(fis);
				ois = new ObjectInputStream(zis);
				map = (Map) ois.readObject();
			}
		} catch (Throwable t) {
			Log.INSTANCE.println("E: Failed to load password store, stack trace follows.");
			t.printStackTrace(Log.INSTANCE);
		} finally {
			closeCarefully(ois);
			closeCarefully(zis);
			closeCarefully(fis);
		}
		if (map != null) {
//			Log.INSTANCE.println("I: Password store loaded; " + map.size()+" entries.");
			return map;
		}
		return new TreeMap();
	}

	private boolean store() {
		OutputStream fos = null, zos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(BACKEND);
			zos = new GZIPOutputStream(fos);
			oos = new ObjectOutputStream(zos);
			oos.writeObject(map);
			return true;
		} catch (Throwable t) {
			Log.INSTANCE.println("E: Failed to save the password store, stack trace follows.");
			t.printStackTrace(Log.INSTANCE);
			return false;
		} finally {
			closeCarefully(oos);
			closeCarefully(zos);
			closeCarefully(fos);
		}
	}
	
	private void closeCarefully(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {}
		}
	}
	
	private void closeCarefully(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {}
		}
	}

}
