package com.mobileread.ixtab.jbpatch.conf.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import com.amazon.kindle.kindlet.input.keyboard.OnscreenKeyboardUtil;



public class TextSettingPanel extends SettingPanel implements SettingChangeListener {
	private static final long serialVersionUID = 1L;

	private final JBTextField text;
	
	/**
	 * Create a new SettingPanel which accepts text as input. 
	 * @param listener the {@link SettingChangeListener} associated with this panel.
	 * @param mode one of the modes defined in {@link OnscreenKeyboardUtil}
	 * @param disableAutoCompletion flag to disable autocompletion
	 */
	public TextSettingPanel(SettingChangeListener listener, String mode, boolean disableAutoCompletion) {
		super(listener);
		text = new JBTextField(this, mode, disableAutoCompletion);
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
