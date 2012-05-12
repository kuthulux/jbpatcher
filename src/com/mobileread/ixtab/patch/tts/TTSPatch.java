package com.mobileread.ixtab.patch.tts;

import java.net.URL;

import serp.bytecode.BCClass;
import serp.bytecode.Code;
import serp.bytecode.ConstantInstruction;
import serp.bytecode.lowlevel.Entry;
import serp.bytecode.lowlevel.UTF8Entry;

import com.mobileread.ixtab.jbpatch.Descriptor;
import com.mobileread.ixtab.jbpatch.Patch;

public class TTSPatch extends Patch {

	public static final String MD5_EN = "c92781d51b2ad1951e2c6d5279afe6d5";
	public static final String MD5_DE = "cd9041a3105c19c2de0f61dd012872d3";
	public static final String MD5_ES = "af8a8c3d465f54c79d7b1bc51fed018a";
	public static final String MD5_FR = "17a930c0e35f606ce087a468bbe3da31";
	public static final String MD5_IT = "1458ab0b2c750163f7f51b28cba26306";
	public static final String MD5_PT = "4e2abf106695447de436b6b4bc2ccad9";

	public static final String MD5_TTSACTION = "7342c90af8a837f4632d62d74ea86242";

	private static final String[] ORIGINAL_EN = new String[] {"Female", "Male"};
	private static final String[] ORIGINAL_DE = new String[] {"Weiblich", "Männlich"};
	private static final String[] ORIGINAL_ES = new String[] {"Femenina", "Masculina"};
	private static final String[] ORIGINAL_FR = new String[] {"Femme", "Homme"};
	private static final String[] ORIGINAL_IT = new String[] {"Femminile", "Maschile"};
	private static final String[] ORIGINAL_PT = new String[] {"Femino", "Masculino"};
	
	protected Descriptor[] getDescriptors() {
		return new Descriptor[] { 
				new Descriptor( "com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources", new String[] { MD5_EN }),
				new Descriptor( "com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources_de", new String[] { MD5_DE }),
				new Descriptor( "com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources_es", new String[] { MD5_ES }),
				new Descriptor( "com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources_fr", new String[] { MD5_FR }),
				new Descriptor( "com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources_it", new String[] { MD5_IT }),
				new Descriptor( "com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources_pt", new String[] { MD5_PT }),
				new Descriptor( "com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources_pt", new String[] { MD5_PT }),
				new Descriptor( "com.amazon.ebook.booklet.reader.plugin.tts.TTSProvider$TTSAction", new String[] { MD5_TTSACTION }),
		};
	}
	

	protected URL getResourcesUrl() {
		return getClass().getResource("/ttspatch.txt");
	}


	public String perform(String md5, BCClass clazz) throws Throwable {
		if (md5.equals(MD5_EN)) return patchDescription(clazz, ORIGINAL_EN);
		if (md5.equals(MD5_DE)) return patchDescription(clazz, ORIGINAL_DE);
		if (md5.equals(MD5_ES)) return patchDescription(clazz, ORIGINAL_ES);
		if (md5.equals(MD5_FR)) return patchDescription(clazz, ORIGINAL_FR);
		if (md5.equals(MD5_IT)) return patchDescription(clazz, ORIGINAL_IT);
		if (md5.equals(MD5_PT)) return patchDescription(clazz, ORIGINAL_PT);
		if (md5.equals(MD5_TTSACTION)) return patchTTSAction(clazz);
		return "unexpected error: unsupported MD5 "+md5;
	}


	private String patchDescription(BCClass clazz, String[] original) {
		String[] replacement = new String[] {get("female"), get("male")};
		Entry[] entries = clazz.getPool().getEntries();
		for (int e=0; e < entries.length; ++e) {
			if (entries[e] instanceof UTF8Entry) {
				UTF8Entry entry = (UTF8Entry) entries[e];
				for (int r = 0; r < original.length; ++r) {
					if (original[r].equals(entry.getValue())) {
						entry.setValue(replacement[r]);
						break;
					}
				}
			}
		}
		return null;
	}

	private String patchTTSAction(BCClass clazz) throws Throwable {
		Code c = clazz.getDeclaredMethod("<init>").getCode(false);
		
		c.after(22);
		((ConstantInstruction)c.next()).setValue(true);
		
		c.calculateMaxLocals();
        c.calculateMaxStack();
		return null;
	}

}
