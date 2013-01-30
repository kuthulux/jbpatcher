package com.mobileread.ixtab.patch.hyphenation.fw533;
import com.mobileread.ixtab.patch.hyphenation.common.Hyphenation;
import com.mobipocket.common.library.reader.hyphenation.g;

public class Hyphenation533 extends g implements Hyphenation {

	public Hyphenation533(String a) {
		super(a);
	}

	public void addHyphenationPoint(int a) {
		super.GW(a);
	}

	public int[] getHyphenationPoints() {
		return super.rX();
	}

	public String getHyphenatedWord() {
		return super.sx();
	}

	public void setHyphenationPoints(int[] hyphenationPoints) {
		if (hyphenationPoints != null) {
			for (int i=0; i < hyphenationPoints.length; ++i) {
				addHyphenationPoint(hyphenationPoints[i]);
			}
		}
	}

	
}
