package com.mobileread.ixtab.patch.kt.fontsize;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.mobileread.ixtab.jbpatch.conf.ui.JMultiLineLabel;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingChangeListener;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingPanel;

public class FontSizesSettingPanel extends SettingPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	// anything smaller/larger is really unusable.
	private static final int MIN_FONT_SIZE = 4;
	private static final int MAX_FONT_SIZE = 37;
	
	private final JPanel settingsPanel = new JPanel();
	private final JPanel demoPanel = new JPanel();
	private final JButton moreButton = new JButton(">");
	private final JButton lessButton = new JButton("<");
	private final JLabel emptyLabel = new JLabel("");
	private FontSizeEntry[] entries;
	
	public FontSizesSettingPanel(SettingChangeListener settingChangeListener) {
		super(settingChangeListener);
		setLayout(new BorderLayout());
		add(settingsPanel, BorderLayout.CENTER);
		add(demoPanel, BorderLayout.SOUTH);
		if (!FontSizePatch.isDeviceRunningASupportedLocale()) {
			String warning = FontSizePatch.instance.localize(FontSizePatch.UI_UNSUPPORTED_LOCALE);
			add(new JMultiLineLabel(warning), BorderLayout.NORTH);
		}
		moreButton.addActionListener(this);
		lessButton.addActionListener(this);
	}

	public void setValue(String value) {
		int[] sizes = FontSizePatch.deserializeConfiguration(value);
		entries = new FontSizeEntry[sizes.length];
		
		// initialize entries
		for (int i=0; i < sizes.length; ++i) {
			entries[i] = new FontSizeEntry(i, sizes[i]);
		}
		
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				refreshUI();
			}
			
		});
		valueChanged(value);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (lessButton == e.getSource()) {
			FontSizeEntry[] newEntries = new FontSizeEntry[entries.length-1];
			System.arraycopy(entries, 0, newEntries, 0, newEntries.length);
			entries = newEntries;
			refreshUI();
			updateConfiguration();
		} else if (moreButton == e.getSource()) {
			FontSizeEntry[] newEntries = new FontSizeEntry[entries.length+1];
			System.arraycopy(entries, 0, newEntries, 0, entries.length);
			newEntries[entries.length] = new FontSizeEntry(entries.length, entries[entries.length-1].size+1);
			entries = newEntries;
			refreshUI();
			updateConfiguration();
		}
	}


	private void updateConfiguration() {
		StringBuffer serialized = new StringBuffer();
		for (int i=0; i < entries.length; ++i) {
			if (i > 0) {
				serialized.append(" ");
			}
			serialized.append(entries[i].size);
		}
		valueChanged(serialized.toString());
	}

	private void refreshUI() {
		
		lessButton.setEnabled(entries.length > FontSizePatch.MIN_SETTINGS);
		moreButton.setEnabled(entries.length < FontSizePatch.MAX_SETTINGS);
		
		settingsPanel.removeAll();
		settingsPanel.setLayout(new GridLayout(3, entries.length+1, 0, 0));
		
		// display "larger" buttons
		for (int i=0; i < entries.length; ++i) {
			settingsPanel.add(entries[i].larger);
		}
		settingsPanel.add(moreButton);
		
		// display size labels
		for (int i=0; i < entries.length; ++i) {
			settingsPanel.add(entries[i].labelSize);
		}
		settingsPanel.add(emptyLabel);
		
		// display "smaller" buttons
		for (int i=0; i < entries.length; ++i) {
			settingsPanel.add(entries[i].smaller);
		}
		settingsPanel.add(lessButton);
		
		for (int i=0; i < entries.length; ++i) {
			entries[i].refreshUI(false);
		}
		
		// display demo panel
		demoPanel.removeAll();
		demoPanel.setLayout(new BoxLayout(demoPanel, BoxLayout.X_AXIS));
		demoPanel.add(Box.createHorizontalGlue());
		for (int i=0; i < entries.length; ++i) {
			JComponent l = entries[i].demo;
			l.setAlignmentY(1.0F);
			demoPanel.add(l);
			demoPanel.add(Box.createHorizontalGlue());
		}
		
		this.validate();
		this.repaint();
	}

	private class FontSizeEntry implements MouseListener {
		private final JLabel larger = new JLabel("+", JLabel.CENTER);
		private final JLabel smaller = new JLabel("-", JLabel.CENTER);
		private final JLabel labelSize = new JLabel("", JLabel.CENTER);
		private final JToggleButton demo = new DummyToggleButton("Aa");
		private final int index;
		private int size;
		
		private FontSizeEntry(int index, int size) {
			this.index = index;
			larger.addMouseListener(this);
			smaller.addMouseListener(this);
			larger.setFont(new Font(larger.getFont().getName(), Font.BOLD, larger.getFont().getSize()+3));
			smaller.setFont(new Font(smaller.getFont().getName(), Font.BOLD, smaller.getFont().getSize()+3));
			demo.setFont(new Font("serif", Font.PLAIN, demo.getFont().getSize()));
			setSize(size);
		}

		public void refreshUI(boolean propagate) {
			if (index > 0) {
				FontSizeEntry previous = entries[index-1];
				smaller.setEnabled(size - 1 > previous.size);
			} else {
				smaller.setEnabled(size > MIN_FONT_SIZE);
			}
			if (index < entries.length-1) {
				FontSizeEntry next = entries[index+1];
				larger.setEnabled(size + 1 < next.size);
			} else {
				larger.setEnabled(size < MAX_FONT_SIZE);
				moreButton.setEnabled(size < MAX_FONT_SIZE && entries.length < FontSizePatch.MAX_SETTINGS);
			}
			demo.setFont(new Font(demo.getFont().getName(), demo.getFont().getStyle(), size));
			
			if (propagate) {
				if (index > 0) {
					entries[index-1].refreshUI(false);
				}
				if (index < entries.length - 1) {
					entries[index+1].refreshUI(false);
				}
				updateConfiguration();
			}

		}

		private void setSize(int size) {
			this.size = size;
			labelSize.setText(Integer.toString(size));
		}

		public void mouseClicked(MouseEvent e) {
			Object src = e.getSource();
			if (src == smaller && smaller.isEnabled()) {
				setSize(size-1);
				refreshUI(true);
			} else if (src == larger && larger.isEnabled()) {
				setSize(size+1);
				refreshUI(true);
			}
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}
	
	/* for some unknown reason, JLabels will not align
	 * properly to the bottom. So we use this (inspired by the
	 * FontDialog implementation).
	 */
	private class DummyToggleButton extends JToggleButton {
		private static final long serialVersionUID = 1L;

		public DummyToggleButton(String title) {
			super(title);
            putClientProperty("classic.look", Boolean.TRUE);
            putClientProperty("font.baseline.alignment", new Integer(0));
            putClientProperty("classic.look.button.roundborder", Boolean.TRUE);
            setBorder(null);
			setVerticalAlignment(SwingConstants.BOTTOM);
			setVerticalTextPosition(SwingConstants.BOTTOM);
		}
	}
}
