package com.mobileread.ixtab.jbpatch.conf.ui;

import javax.swing.JPanel;


public abstract class SettingPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final SettingChangeListener settingChangeListener;
	
	public SettingPanel(SettingChangeListener settingChangeListener) {
		super();
		this.settingChangeListener = settingChangeListener;
	}

	public abstract void setValue(String value);
	
	protected final void valueChanged(String newValue) {
		if (settingChangeListener != null) {
			settingChangeListener.valueChanged(this, newValue);
		}
	}
}
