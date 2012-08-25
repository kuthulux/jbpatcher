package com.mobileread.ixtab.jbpatch.composition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.mobileread.ixtab.jbpatch.Patch;

public class State implements Comparable {
	public final String md5;
	public final List appliedTransitions;
	public final List nextTransitions = new ArrayList();

	public State(String md5, List appliedTransitions) {
		this.md5 = md5;
		this.appliedTransitions = appliedTransitions;
	}

	public static State create(State previous, Transition via) {
		List applied = new ArrayList(previous.appliedTransitions);
		applied.add(via);
		return new State(via.toMd5, applied);
	}

	public String toString() {
		return "State [md5=" + md5 + ", appliedTransitions=" + appliedTransitions
				+ ", nextTransitions=" + nextTransitions + "]";
	}

	public int compareTo(Object o) {
		State other = (State) o;
		if (appliedTransitions.size() == other.appliedTransitions.size()) {
			// no preference, both seem to do the job. Just be deterministic:
			return toString().compareTo(other.toString());
		}
		int order = new Integer(appliedTransitions.size()).compareTo(new Integer(other.appliedTransitions.size()));
		// higher is better;
		return -order;
	}

	public boolean isEquivalent(State other) {
		Patch[] myPatches = getAppliedPatches(this);
		Patch[] othersPatches = getAppliedPatches(other);
		return Arrays.equals(myPatches, othersPatches);
	}

	private static Patch[] getAppliedPatches(State state) {
		Patch[] p = new Patch[state.appliedTransitions.size()];
		for (int i=0; i < p.length; ++i) {
			p[i] = ((Transition)state.appliedTransitions.get(i)).patch;
		}
		Arrays.sort(p);
		return p;
	}

	public boolean isPatchApplied(Patch patch) {
		Iterator it = appliedTransitions.iterator();
		while (it.hasNext()) {
			Transition t = (Transition) it.next();
			if (t.patch.equals(patch)) {
				return true;
			}
		}
		return false;
	}
	
	
}
