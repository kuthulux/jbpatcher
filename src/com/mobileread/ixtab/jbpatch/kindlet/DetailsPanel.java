package com.mobileread.ixtab.jbpatch.kindlet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.mobileread.ixtab.jbpatch.Patch;

public class DetailsPanel extends JPanel {
	static DetailsPanel INSTANCE;

	private static final long serialVersionUID = 1L;

	private final JTabbedPane tabContext;
	private final int tabIndex;

	DetailsPanel(JTabbedPane context, int ownIndex) {
		INSTANCE = this;
		this.tabContext = context;
		this.tabIndex = ownIndex;
		this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 10));

	}

	private Patch currentlyOnDisplay = null;

	void displayDetails(final Patch patch) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				tabContext.setSelectedIndex(tabIndex);
				if (currentlyOnDisplay == patch) {
					return;
				}
				currentlyOnDisplay = patch;
				removeAll();
				display(patch);
			}

		});
	}

	private void display(Patch patch) {
		String title = "Modify Something relevant";
		String subtitle = "For instance PDF file margins";
		String description = "This setting modifies the bottom margin (duh!), well, not really. Since the description could have been longer, but I don't have the patience to write anything right now, I'll just continue with y a d d a yadda yadda - and yeeeeehaaaaa.";

		// Yeah, manual layouts in Java are indeed a PITA.

		JPanel focused = new JPanel();
		focused.setBorder(BorderFactory.createTitledBorder(
				Borders.WHITE3BLACK1GRAY4, " " + subtitle + " "));

		focused.setLayout(new GridLayout(3, 3, 10, 10));
		for (int i = 0; i < 9; ++i) {
			if (i == 4) {
				focused.add(new JLabel("YOUR AD HERE?"));
			} else {
				focused.add(new JLabel(" " + i + " "));
			}
		}

		JPanel top = new JPanel(new BorderLayout());
		JPanel titleBox = new JPanel();
		titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.X_AXIS));
		titleBox.add(Box.createHorizontalGlue());

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(getOrCreateTitleFont(titleLabel.getFont()));
		titleBox.add(titleLabel);

		titleBox.add(Box.createHorizontalGlue());
		top.add(titleBox, BorderLayout.CENTER);
		// top.add(new JLabel(subtitle), BorderLayout.CENTER);

		JPanel bottom = new JPanel(new BorderLayout());
		JPanel descriptionBox = new JPanel(new BorderLayout());
		descriptionBox.add(new JMultiLineLabel(description), BorderLayout.CENTER);
		descriptionBox.setBorder(Borders.WHITE10);
		
		bottom.add(descriptionBox, BorderLayout.CENTER);
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		buttons.add(new JButton("button1"));
		buttons.add(new JButton("button1"));
		buttons.add(new JButton("button1"));
		buttons.add(new JButton("button1"));
		bottom.add(buttons, BorderLayout.SOUTH);

		this.setLayout(new BorderLayout());
		this.add(top, BorderLayout.NORTH);
		this.add(bottom, BorderLayout.SOUTH);
		this.add(focused, BorderLayout.CENTER);
	}

	private static Font titleFont = null;

	private Font getOrCreateTitleFont(Font font) {
		if (titleFont == null) {
			titleFont = new Font(font.getName(), font.getStyle(),
					(int) (font.getSize() * 1.3));
		}
		return titleFont;
	}
}
