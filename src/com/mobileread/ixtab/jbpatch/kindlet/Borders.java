package com.mobileread.ixtab.jbpatch.kindlet;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class Borders {
	public static final Border WHITE3 = BorderFactory.createLineBorder(
			Color.WHITE, 3);
	public static final Border WHITE10 = BorderFactory.createLineBorder(
			Color.WHITE, 10);
	public static final Border GRAY4 = BorderFactory.createLineBorder(
			Color.LIGHT_GRAY, 4);
	public static final Border BLACK1 = BorderFactory.createLineBorder(
			Color.BLACK, 1);
	public static final Border BLACK1GRAY4 = BorderFactory
			.createCompoundBorder(BLACK1, GRAY4);
	public static final Border WHITE3BLACK1GRAY4 = BorderFactory
			.createCompoundBorder(WHITE3, BLACK1GRAY4);

}
