package com.mobileread.ixtab.jbpatch.conf.ui;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;


public class ComboBoxSettingPanel extends SettingPanel {
	private static final long serialVersionUID = 1L;

	private final SettingEntry[] entries;
	private final JComboBox combo = new JComboBox();
	
	public ComboBoxSettingPanel(final SettingChangeListener listener, SettingEntry[] entries) {
		super(listener);
		this.entries = entries;
		setLayout(new BorderLayout());
		add(combo);
		
		for (int i=0; i < entries.length; ++i) {
			combo.addItem(entries[i]);
		}
		combo.addItemListener(new ItemListener(){

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					valueChanged(((SettingEntry)e.getItem()).key);
				}
			}});
	}

	public void setValue(String value) {
		for (int i=0; i < entries.length; ++i) {
			if (entries[i].key.equals(value)) {
				combo.setSelectedItem(entries[i]);
				break;
			}
		}
	}
}
