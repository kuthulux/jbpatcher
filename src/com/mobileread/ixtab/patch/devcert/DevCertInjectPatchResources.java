package com.mobileread.ixtab.patch.devcert;

import java.util.Enumeration;
import java.util.ResourceBundle;

public class DevCertInjectPatchResources extends ResourceBundle {

	protected Object handleGetObject(String key) {
		return DevCertInjectPatch.INSTANCE.getResource(key);
	}

	public Enumeration getKeys() {
		throw new UnsupportedOperationException();
	}

}
