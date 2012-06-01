package com.mobileread.ixtab.jbpatch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public final class KindleDevice {
	private static final String VERSION_FILENAME = "/etc/version.txt";
	
	public static final String KT_510_1557760049 = "4d14b7af911080e6a073121eb3262e54";
	
	private static Map buildFirmwareMap() {
		Map map = new TreeMap();
		map.put(KT_510_1557760049, "Kindle 5.1.0 (1557760049)");
		return map;
	}
	
	public static String getFirmwareName() {
		String result = (String) firmwareMap.get(FIRMWARE_ID);
		if (result == null) {
			result = "UNKNOWN FIRMWARE WITH INTERNAL ID "+FIRMWARE_ID;
		}
		return result;
	}
	
	private static final Map firmwareMap = buildFirmwareMap();

	public static final String FIRMWARE_ID = getVersion();
	
	private static String getVersion() {
		/* The Amazon logic to determine the version seems to use the native
		 * libraries liblab126utilsjni and liblab126utils, to determine the
		 * "revision" of the Kindle from /etc/version.txt. Without having
		 * thoroughly decompiled everything, the device info
		 * "Kindle 5.1.0 (1557760049)" seems to stem from /etc/prettyversion.txt
		 * (for the first part), and the first line of /etc/version.txt:
		 * In this case it was:
		 * System Software Version: 049-juno_2_yoshi-155776
		 * So the "end" (155776), concatenated with the "beginning" (049), and
		 * some filler zeroes in between. We may need to actually parse this line
		 * at some later point in time, but for now, this method simply returns
		 * the string value of the MD5 hash of the first line in /etc/version.txt,
		 * or null on error.
		 */
		
		String md5 = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(VERSION_FILENAME));
			String line = reader.readLine();
			if (line != null) {
				md5 = MD5.getMd5String(line.getBytes());
			}
		} catch (IOException e) {
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {};
			}
		}
		return md5;
	}

}
