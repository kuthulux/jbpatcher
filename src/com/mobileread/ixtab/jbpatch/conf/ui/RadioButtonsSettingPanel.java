package com.mobileread.ixtab.jbpatch.conf.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


public class RadioButtonsSettingPanel extends SettingPanel implements ItemListener {
	private static final long serialVersionUID = 1L;

	private final SettingEntry[] entries;
	private final JRadioButton[] buttons;
	
	public RadioButtonsSettingPanel(final SettingChangeListener listener, SettingEntry[] entries) {
		this(listener, entries, entries.length, 1, 5, 5);
	}
	
	public RadioButtonsSettingPanel(final SettingChangeListener listener, SettingEntry[] entries, int rows, int columns, int hgap, int vgap) {
		super(listener);
		this.entries = entries;
		setLayout(new BorderLayout());
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(rows, columns, hgap, vgap));
		
		ButtonGroup group = new ButtonGroup();
		buttons = new JRadioButton[entries.length];
		for (int i=0; i < entries.length; ++i) {
			buttons[i] = new JRadioButton();
			buttons[i].addItemListener(this);
			group.add(buttons[i]);
			JPanel entry = new JPanel();
			entry.setLayout(new BoxLayout(entry, BoxLayout.X_AXIS));
			entry.add(buttons[i]);
			entry.add(new JLabel(entries[i].displayValue));
			entry.add(Box.createHorizontalGlue());
			grid.add(entry);
		}
		this.add(grid);
	}

	public void setValue(String value) {
		for (int i=0; i < entries.length; ++i) {
			buttons[i].setSelected(entries[i].key.equals(value));
		}
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			for (int i=0; i < buttons.length; ++i) {
				if (buttons[i] == e.getSource()) {
					valueChanged(entries[i].key);
				}
			}
		}
	}
}
