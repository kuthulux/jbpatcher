package com.mobileread.ixtab.patch;

import serp.bytecode.BCClass;
import serp.bytecode.lowlevel.Entry;
import serp.bytecode.lowlevel.UTF8Entry;

import com.mobileread.ixtab.jbpatch.Descriptor;
import com.mobileread.ixtab.jbpatch.Patch;

/*
 * NOTE: This patch is superseded by com.mobileread.ixtab.patch.TTSPatch, which provides
 * the functionality of this patch, while also actually enabling the TTS feature for all books.
 */
public class TTSGermanDescriptionPatch extends Patch {

	private final String[] ORIGINAL = new String[] {"Weiblich", "Männlich"};
	private final String[] REPLACEMENT_1 = new String[] {"Weiblich (englisch)", "Männlich (deutsch)"};
	private final String[] REPLACEMENT_2 = new String[] {"Weiblich (deutsch)", "Männlich (englisch)"};
	
	private final String SUFFIX_1 = "_en_de.jbpatch";
	private final String SUFFIX_2 = "_de_en.jbpatch";
	
	protected Descriptor[] getDescriptors() {
		return new Descriptor[] { new Descriptor(
				"com.amazon.ebook.booklet.reader.plugin.tts.resources.TTSResources_de",
				new String[] { "cd9041a3105c19c2de0f61dd012872d3" }) };
	}

	public String perform(String md5, BCClass clazz) throws Throwable {
		String[] replacement = getReplacementFromId();
		if (replacement != null) {
			Entry[] entries = clazz.getPool().getEntries();
			for (int e=0; e < entries.length; ++e) {
				if (entries[e] instanceof UTF8Entry) {
					UTF8Entry entry = (UTF8Entry) entries[e];
					for (int r = 0; r < ORIGINAL.length; ++r) {
						if (ORIGINAL[r].equals(entry.getValue())) {
							entry.setValue(replacement[r]);
							break;
						}
					}
				}
			}
			return null;
		}
		return "W: " + id()+" not applied: filename must end either with \""+SUFFIX_1+"\", or with \""+SUFFIX_2+"\"";
	}

	private String[] getReplacementFromId() {
		String id = id().toLowerCase();
		if (id.endsWith(SUFFIX_1)) {
			return REPLACEMENT_1;
		}
		if (id.endsWith(SUFFIX_2)) {
			return REPLACEMENT_2;
		}
		return REPLACEMENT_2;
	}
}
