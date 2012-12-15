package com.mobileread.ixtab.patch.hyphenation.common;

public interface Hyphenation {

	public void addHyphenationPoint(int a);

	public int[] getHyphenationPoints();

	public String getHyphenatedWord();

	public void setHyphenationPoints(int[] hyphenationPoints);
	
}
