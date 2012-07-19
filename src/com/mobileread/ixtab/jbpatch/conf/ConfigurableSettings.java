package com.mobileread.ixtab.jbpatch.conf;

import java.util.LinkedHashMap;

import com.mobileread.ixtab.jbpatch.resources.KeyValueFile;
import com.mobileread.ixtab.jbpatch.resources.KeyValueResource;

/*
 * I know this could have been organized better, it's a little awkward right now.
 */
public class ConfigurableSettings extends LinkedHashMap implements KeyValueResource {
	private static final long serialVersionUID = 1L;

	private KeyValueFile backend;
	
	/* Just to make sure that we complain if something else is inserted */
	public Object put(Object key, Object value) {
		return super.put((String) key, (ConfigurableSetting) value);
	}

	public void add(ConfigurableSetting setting) {
		put (setting.key, setting);
	}

	public String getValue(String key) {
		if (backend != null) {
			return backend.getValue(key);
		}
		return ((ConfigurableSetting)get(key)).defaultValue;
	}

	public boolean setValue(String key, String value) {
		if (backend != null) {
			return backend.setValue(key, value);
		}
		return false;
	}

	public void setBackend(KeyValueFile keyValueFile) {
		backend = keyValueFile;
	}
}
