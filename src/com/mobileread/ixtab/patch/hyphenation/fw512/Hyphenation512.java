package com.mobileread.ixtab.patch.hyphenation.fw512;
import com.mobileread.ixtab.patch.hyphenation.common.Hyphenation;
import com.mobipocket.common.library.reader.hyphenation.e;

public class Hyphenation512 extends e implements Hyphenation {

	public Hyphenation512(String a) {
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
