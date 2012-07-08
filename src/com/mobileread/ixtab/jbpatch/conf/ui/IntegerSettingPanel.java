package com.mobileread.ixtab.jbpatch.conf.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import com.amazon.kindle.kindlet.input.keyboard.OnscreenKeyboardUtil;



public class IntegerSettingPanel extends SettingPanel implements SettingChangeListener {
	private static final long serialVersionUID = 1L;

	private final JBTextField text = new JBTextField(this, OnscreenKeyboardUtil.KEYBOARD_MODE_NUMBERS_AND_SYMBOLS);
	
	public IntegerSettingPanel(SettingChangeListener listener) {
		super(listener);
		setLayout(new BorderLayout());
		add(text, BorderLayout.CENTER);
	}

	public void setValue(String value) {
		text.setText(value);
	}

	public void valueChanged(Component source, String newValue) {
		valueChanged(newValue);
	}

}
