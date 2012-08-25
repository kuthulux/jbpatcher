package com.mobileread.ixtab.jbpatch.composition;

import java.util.LinkedList;
import java.util.List;

import com.mobileread.ixtab.jbpatch.Patch;

public class Transition {
	public final Patch patch;
	public final String fromMd5;
	public final String toMd5;
	public final List nextStates = new LinkedList();
	
	public Transition(Patch patch, String fromState, String toState) {
		super();
		this.patch = patch;
		this.fromMd5 = fromState;
		this.toMd5 = toState;
	}

	public String toString() {
		return "Transition ["+fromMd5+"->"+patch+"->"+toMd5+" ("+nextStates.size()+")]";
	}
	
	
}
