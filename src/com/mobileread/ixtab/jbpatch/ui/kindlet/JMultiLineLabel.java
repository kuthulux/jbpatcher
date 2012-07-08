package com.mobileread.ixtab.jbpatch.ui.kindlet;

import javax.swing.JTextArea;

/* This was shamelessly stolen from 
 * http://stackoverflow.com/questions/11033800/how-to-get-a-multilined-jlabel-or-a-jtextarea-looking-totally-the-same-without
 * 
 * So just to make sure: not my original work, not governed by any of the potential license agreements associated with this software.
 * You're totally free to use this code under the license of the original author (probably public domain).
 */

public class JMultiLineLabel extends JTextArea {
	private static final long serialVersionUID = 1L;

	public JMultiLineLabel(String text) {
		super(text);
		setEditable(false);
		setCursor(null);
		setOpaque(false);
		setFocusable(false);
		// setFont(UIManager.getFont("Label.font"));
		setWrapStyleWord(true);
		setLineWrap(true);
	}
}
