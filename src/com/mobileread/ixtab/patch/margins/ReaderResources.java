package com.mobileread.ixtab.patch.margins;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import com.mobileread.ixtab.jbpatch.Log;

public class ReaderResources extends ResourceBundle {

	private static final String MARGIN_LIST_KEY = "font.wordsperline.margin.list";
	private static final int MARGIN_LIST_LENGTH = 3;

	private static final String[] SUPPORTED_KEYS = new String[] {
			"reader.content.topMarginOffset",
			"reader.progressbar.footer.height", "reader.content.leftMargin",
			"reader.content.rightMargin", "reader.content.topMargin",
			"reader.content.bottomMargin", };

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
		String defined = loadFromConfiguration(MARGIN_LIST_KEY);
		if (defined != null) {
			try {
				int[] margins = new int[MARGIN_LIST_LENGTH];
				StringTokenizer tokens = new StringTokenizer(defined.trim());
				int count = 0;
				while (tokens.hasMoreElements()) {
					margins[count++] = parseAsPositiveInteger(tokens
							.nextToken());
				}
				if (count != margins.length) {
					throw new IllegalArgumentException();
				}
				overridden.put(MARGIN_LIST_KEY, margins);
			} catch (Throwable t) {
				Log.INSTANCE.println("W: User settings for \""
						+ MARGIN_LIST_KEY
						+ "\" have been ignored because they are invalid.");
			}
		}
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
