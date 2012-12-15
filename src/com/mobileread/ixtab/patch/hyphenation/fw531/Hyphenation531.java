package com.mobileread.ixtab.patch.hyphenation.fw531;
import com.mobileread.ixtab.patch.hyphenation.common.Hyphenation;
import com.mobipocket.common.library.reader.hyphenation.g;

public class Hyphenation531 extends g implements Hyphenation {

	public Hyphenation531(String a) {
		super(a);
	}

	public void addHyphenationPoint(int a) {
		super.XW(a);
	}

	public int[] getHyphenationPoints() {
		return super.WX();
	}

	public String getHyphenatedWord() {
		return super.dX();
	}

	public void setHyphenationPoints(int[] hyphenationPoints) {
		if (hyphenationPoints != null) {
			for (int i=0; i < hyphenationPoints.length; ++i) {
				addHyphenationPoint(hyphenationPoints[i]);
			}
		}
	}

	
}
