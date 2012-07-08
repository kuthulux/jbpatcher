package com.mobileread.ixtab.jbpatch.ui.kindlet;

import ixtab.jailbreak.Jailbreak;

import java.security.AllPermission;

public class JBPatchJailbreak extends Jailbreak {

	public boolean requestPermissions() {
		boolean ok  = getContext().requestPermission(new AllPermission());
		return ok;
	}

}
