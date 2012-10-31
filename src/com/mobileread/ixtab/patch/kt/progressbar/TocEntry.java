package com.mobileread.ixtab.patch.kt.progressbar;

public class TocEntry {
	public final int position;
	public final int level;
	
	public TocEntry(int position, int level) {
		super();
		this.position = position;
		this.level = level;
	}
	
	public String toString() {
		return Integer.toString(position)+"("+level+")";
	}
}
