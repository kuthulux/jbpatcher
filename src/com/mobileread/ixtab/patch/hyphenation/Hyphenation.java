package com.mobileread.ixtab.patch.hyphenation;
import com.mobipocket.common.library.reader.hyphenation.e;

public class Hyphenation extends e {

	public Hyphenation(String a) {
		super(a);
	}

	public void addHyphenationPoint(int a) {
		super.D(a);
	}

	public int[] getHyphenationPoints() {
		return super.B();
	}

	public String getHyphenatedWord() {
		return super.D();
	}

	public void setHyphenationPoints(int[] hyphenationPoints) {
		if (hyphenationPoints != null) {
			for (int i=0; i < hyphenationPoints.length; ++i) {
				addHyphenationPoint(hyphenationPoints[i]);
			}
		}
	}

	
}
