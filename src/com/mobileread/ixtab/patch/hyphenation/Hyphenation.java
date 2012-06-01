package com.mobileread.ixtab.patch.hyphenation;
import com.mobileread.ixtab.jbpatch.Log;
import com.mobipocket.common.library.reader.hyphenation.e;

public class Hyphenation extends e {

	public Hyphenation(String a) {
		super(a);
		log("new Hyphenation("+a+")");
		for (int i=1; i < a.length()-1; ++i) {
			addHyphenationPoint(i);
		}
	}

	public void addHyphenationPoint(int a) {
		log("addHyphenationPoints("+this.toString()+")");
		super.D(a);
	}

	public int[] getHyphenationPoints() {
		log("getHyphenationPoints("+this.toString()+")");
		return super.B();
	}

	public String getHyphenatedWord() {
		log("getHyphenatedWord("+this.toString()+")");
		return super.D();
	}

	private void log(String msg) {
		Log.INSTANCE.println(msg);
	}

	
}
