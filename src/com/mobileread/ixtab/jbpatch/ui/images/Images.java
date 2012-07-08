package com.mobileread.ixtab.jbpatch.ui.images;

import java.net.URL;

import javax.swing.ImageIcon;

public final class Images {
	public static final String CHECKMARK = "checkmark.png";
	public static final String CROSSMARK = "crossmark.png";

	private Images() {}

	public static ImageIcon loadAsImageIcon(String image) {
		return new ImageIcon(loadAsUrl(image));
	}

	private static URL loadAsUrl(String image) {
		return Images.class.getResource(image);
	}
	
	
}
