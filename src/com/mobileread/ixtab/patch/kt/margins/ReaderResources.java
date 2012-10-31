package com.mobileread.ixtab.patch.kt.margins;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.mobileread.ixtab.jbpatch.Log;

public class ReaderResources extends ResourceBundle implements MarginsPatchKeys {

	private static final String MARGIN_LIST_KEY = "font.wordsperline.margin.list";

	private static final String[] SUPPORTED_KEYS = new String[] {
			KEY_READER_CONTENT_TOP_MARGIN_OFFSET,
			KEY_READER_PROGRESSBAR_FOOTER_HEIGHT, KEY_READER_CONTENT_LEFT_MARGIN,
			KEY_READER_CONTENT_RIGHT_MARGIN, KEY_READER_CONTENT_TOP_MARGIN,
			KEY_READER_CONTENT_BOTTOM_MARGIN, };

	private static final String delegateName = "com.amazon.ebook.booklet.reader.resources.ReaderResources";
	private static ResourceBundle delegate;

	private static final Map overridden = new HashMap();

	public Enumeration getKeys() {
		return delegate().getKeys();
	}

	protected Object handleGetObject(String key) {

		ResourceBundle original = delegate();
		Object value = overridden.get(key);
		if (value != null) {
			return value;
		}
		return original.getObject(key);
	}

	private ResourceBundle delegate() {
		if (delegate == null) {
			synchronized (overridden) {
				if (delegate == null) {
					delegate = ResourceBundle.getBundle(delegateName);
					populateOverridden();
				}
			}
		}
		return delegate;
	}

	private void populateOverridden() {
		populateConfiguredIntValues();
		populateMarginList();
	}

	private void populateConfiguredIntValues() {
		for (int i = 0; i < SUPPORTED_KEYS.length; ++i) {
			String key = SUPPORTED_KEYS[i];
			String defined = loadFromConfiguration(key);
			if (defined != null) {
				try {
					int value = parseAsPositiveInteger(defined);
					overridden.put(key, new Integer(value));
				} catch (Throwable t) {
					Log.INSTANCE.println("W: User settings for \"" + key
							+ "\" have been ignored because they are invalid.");
					t.printStackTrace(Log.INSTANCE);
				}
				;
			}
		}
	}

	private void populateMarginList() {
		try {
			int[] combined = new int[3];
			combined[0] = loadPositiveIntegerKey(KEY_FONT_WORDSPERLINE_MARGIN_LIST_FEWEST);
			combined[1] = loadPositiveIntegerKey(KEY_FONT_WORDSPERLINE_MARGIN_LIST_FEWER);
			combined[2] = loadPositiveIntegerKey(KEY_FONT_WORDSPERLINE_MARGIN_LIST_DEFAULT);
			overridden.put(MARGIN_LIST_KEY, combined);
		} catch (Throwable t) {
			Log.INSTANCE.println("W: User settings for \""
					+ MARGIN_LIST_KEY
					+ "\" have been ignored because they are invalid.");
		}
	}

	private int loadPositiveIntegerKey(String key) throws Throwable {
		return parseAsPositiveInteger(loadFromConfiguration(key));
	}

	private String loadFromConfiguration(String key) {
		return MarginsPatch.instance.getResource(key);
	}

	private int parseAsPositiveInteger(String s) throws Throwable {
		int value = Integer.parseInt(s);
		if (value < 0) {
			throw new IllegalArgumentException();
		}
		return value;
	}
}
