package com.mobileread.ixtab.patch.hyphenation.fw534;
import com.mobileread.ixtab.patch.hyphenation.common.Hyphenation;
import com.mobipocket.common.library.reader.hyphenation.g;

public class Hyphenation534 extends g implements Hyphenation {

	public Hyphenation534(String a) {
		super(a);
	}

	public void addHyphenationPoint(int a) {
		super.Qv(a);
	}

	public int[] getHyphenationPoints() {
		return super.JV();
	}

	public String getHyphenatedWord() {
		return super.HX();
	}

	public void setHyphenationPoints(int[] hyphenationPoints) {
		if (hyphenationPoints != null) {
			for (int i=0; i < hyphenationPoints.length; ++i) {
				addHyphenationPoint(hyphenationPoints[i]);
			}
		}
	}

	
}
