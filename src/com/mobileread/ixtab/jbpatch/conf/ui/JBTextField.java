package com.mobileread.ixtab.jbpatch.conf.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.amazon.kindle.kindlet.input.keyboard.OnscreenKeyboardUtil;

public class JBTextField extends JTextField implements KeyListener, DocumentListener {
	private static final long serialVersionUID = 1L;
	private final SettingChangeListener settingsListener;

	/**
	 * Creates a new JBTextField with the given mode
	 * @param listener
	 * @param mode one of the constants defined in {@link OnscreenKeyboardUtil}.
	 */
	public JBTextField(SettingChangeListener listener, String mode) {
		super();
		// disable autocompletion
		this.settingsListener = listener;
		this.addKeyListener(this);
		this.getDocument().addDocumentListener(this);
		putClientProperty("kindle.keyboard.properties", new Integer(0 & -2));
		OnscreenKeyboardUtil.configure(this, mode);
	}

	public void keyTyped(KeyEvent e) {
	}
	
	public void keyReleased(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()=='\n') {
			e.consume();
			// ugly, but working: close on-screen keyboard.
			this.setFocusable(false);
			this.setFocusable(true);
		}
	}

	public void insertUpdate(DocumentEvent e) {
		settingsListener.valueChanged(this, getText());
	}

	public void removeUpdate(DocumentEvent e) {
		settingsListener.valueChanged(this, getText());
	}

	public void changedUpdate(DocumentEvent e) {
		settingsListener.valueChanged(this, getText());
	}

}
