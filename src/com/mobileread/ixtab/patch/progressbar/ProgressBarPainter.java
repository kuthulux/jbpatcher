package com.mobileread.ixtab.patch.progressbar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;

public class ProgressBarPainter {
	private static final Color COLOR_FOREGROUND = Color.BLACK;
	private static final Color COLOR_BACKGROUND = Color.WHITE;
	private static final Color COLOR_READ = Color.LIGHT_GRAY;
	private static final Color COLOR_TOC = Color.DARK_GRAY;

	// preferred size of the progressbar, relative to the total screen width
	public static final float PREFERRED_RELATIVE_WIDTH = 0.99f;
	// minimum pixels to each side of the progress bar
	public static final int MIN_PADDING_PX = 10;

	private static final int BOTTOM_MARGIN = 9;
	// for aesthetical reasons, this should be an even number.
	private static final int BAR_HEIGHT = 12;
	private static final int ARC_SIZE = 5;
	private static final int READ_PADDING = 1;
	private static final int MARKER_HEIGHT = 4;
	private static final int TOCENTRY0_RADIUS = 2;

	public static void paint(Graphics2D g, int graphicsWidth, int graphicsHeight, int bookEnd,
			TocEntry[] tocEntries, int position, int startReadingPosition) {
		
		if (BAR_HEIGHT > graphicsHeight) {
			// there is not enough space to draw the progress bar, so don't even bother.
			return;
		}
		
		int bottomMargin = Math.max(0, graphicsHeight - (BAR_HEIGHT + MARKER_HEIGHT));
		if (bottomMargin >= BOTTOM_MARGIN) {
			bottomMargin = BOTTOM_MARGIN;
		} else {
			bottomMargin = Math.max(bottomMargin, BOTTOM_MARGIN);
		}
		
		// just so we don't inadvertently mess something up (shouldn't happen though)
		Color originalColor = g.getColor();
		Stroke originalStroke = g.getStroke();
		
		g.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(COLOR_BACKGROUND);
		
		// clear screen
		g.fillRect(0, 0, graphicsWidth, graphicsHeight);
		
		// draw progress bar container
		g.setColor(COLOR_FOREGROUND);
		int x = 0;
		int y = graphicsHeight-bottomMargin-BAR_HEIGHT;
		int w = graphicsWidth-1;
		int h = BAR_HEIGHT -1;
		g.drawRoundRect(x, y, w, h, ARC_SIZE, ARC_SIZE);
		
		// draw marker
		int[] mx = new int[3];
		int[] my = new int[3];
		mx[0] = scale(startReadingPosition, bookEnd, graphicsWidth);
		my[0] = graphicsHeight - bottomMargin - BAR_HEIGHT;
		mx[1] = mx[0] - MARKER_HEIGHT;
		my[1] = my[0] - MARKER_HEIGHT;
		mx[2] = mx[0] + MARKER_HEIGHT;
		my[2] = my[1];
		g.fillPolygon(new Polygon(mx, my, 3));

		// draw progress indicator
		g.setColor(COLOR_READ);
		x = READ_PADDING;
		y = graphicsHeight - bottomMargin - BAR_HEIGHT + READ_PADDING;
		w = scale(position, bookEnd, graphicsWidth) - READ_PADDING -1 ;
		if (w >= graphicsWidth -1) {
			w -= READ_PADDING;
		}
		h = BAR_HEIGHT - READ_PADDING * 2;
		if (w >= 0 && h >= 0) {
			g.fillRect(x, y, w, h);
		}
		
		// draw toc entries
		
		g.setColor(COLOR_TOC);
		// y designates the *center* of each entry now
		y = graphicsHeight-bottomMargin-BAR_HEIGHT / 2;
		for (int i=0; i < tocEntries.length; ++i) {
			TocEntry entry = tocEntries[i];
			int radius = TOCENTRY0_RADIUS - entry.level;
			if (radius < 1) {
				continue;
			}
			x = scale(entry.position, bookEnd, graphicsWidth);
//			g.fillRect(x-radius, y-radius, 2*radius, 2*radius);
			g.fillOval(x-radius, y-radius, 2*radius, 2*radius);
		}
		
		
		g.setColor(originalColor);
		g.setStroke(originalStroke);
	}

	private static int scale(int position, int bookEnd, int width) {
		return Math.round(((float)position / bookEnd) * width);
	}
}
