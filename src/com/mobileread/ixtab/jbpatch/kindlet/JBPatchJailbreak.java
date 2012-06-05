package com.mobileread.ixtab.jbpatch.kindlet;

import ixtab.jailbreak.Jailbreak;

import java.security.AllPermission;

public class JBPatchJailbreak extends Jailbreak {

	public boolean requestPermissions() {
		boolean ok  = getContext().requestPermission(new AllPermission());
		return ok;
	}

}
