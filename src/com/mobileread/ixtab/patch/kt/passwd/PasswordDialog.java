package com.mobileread.ixtab.patch.kt.passwd;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.amazon.agui.swing.ButtonActionDialog;
import com.amazon.agui.swing.CommandBar;
import com.amazon.agui.swing.InputDialog;

public class PasswordDialog extends ButtonActionDialog {
	
	public static final String APP_ID = "persistant.app";
	
	public static final int MODE_OPEN = 0;
	public static final int MODE_SET = 1;
	public static final int MODE_VERIFY = 2;
	
	private static final long serialVersionUID = 1L;

	protected PasswordDialog(String a) {
		super(a);
	}

	public static class ButtonAction extends ButtonActionDialog.ButtonAction {
		private static final long serialVersionUID = 1L;

		public ButtonAction(String arg0, ButtonReference arg1) {
			super(arg0, arg1);
		}
		
	}
	
	public static class ButtonReference extends ButtonActionDialog.ButtonReference {
	}
	
	public static String showDialog(int mode, Password password) {
		String appid = APP_ID;
		
		String cancel = PasswordPatch.getResource("cancel");
		String ok = PasswordPatch.getResource("ok");
		
		String title = null;
		String description = null;
		switch (mode) {
		case MODE_OPEN:
			title = PasswordPatch.getResource("password.enter");
			description = PasswordPatch.getResource("item.protected");
			break;
		case MODE_SET:
			title = PasswordPatch.getResource("password.set");
			description = PasswordPatch.getResource("item.protecting");
			password = null;
			break;
		case MODE_VERIFY:
			title = PasswordPatch.getResource("password.verify");
			description = PasswordPatch.getResource("item.verify");
			break;
		default:
		}
		
		InputDialog d = new InputDialog(appid, 1);
		d.setTitleBarEnabled(true);
		d.setTitle(title);
		d.setDescription(description);
		d.setDescriptionRows(3);

		d.getCommandBar().setButtonPreferredSize(null);
		d.getCommandBar().setButtonWidthPreserved(false);
		
		ButtonReference bref = new ButtonReference();
		
		d.addButton(new ButtonAction(cancel, bref));
		ButtonAction okButton = new ButtonAction(ok, bref);
		d.addButton(okButton);
		
		d.setButtonDisablePolicy(InputDialog.DISABLE_IF_EMPTY);
		d.setInputText("");
		
		// Disable autocompletion. 
        d.getTextArea().putClientProperty("kindle.keyboard.properties", new Integer(0 & -2));
        PasswordAdapter adapter = new PasswordAdapter(password, d.getCommandBar(), d.getTextArea(), ok);
		d.postDialog(true);
		
		return cancel.equals(bref.pressedButtonName) ? null : adapter.password;
	}
	
	private static class PasswordAdapter extends KeyAdapter implements DocumentListener {
		
		private final JTextArea view;
		private final JButton confirmButton;
		private String password = "";
		private final Password encrypted;

		public PasswordAdapter(Password encrypted, CommandBar commandBar, JTextArea textArea, String confirmName) {
			this.encrypted = encrypted;
			confirmButton = findConfirmButton(commandBar, confirmName);
			view = textArea;
			view.addKeyListener(this);
			view.getDocument().addDocumentListener(this);
			confirmButton.setEnabled(false);
		}

		private JButton findConfirmButton(CommandBar commandBar,
				String confirmName) {
			/*
			 * No, don't ask me why it has to be done like this. Simply triggering the Action that a ButtonAction represents
			 * is obviously not working.
			 */
			Component[] children = commandBar.getComponents();
			for (int i=0; i < children.length; ++i) {
				Component child = children[i];
				if (child instanceof JButton) {
					JButton button = (JButton) child;
					if (confirmName.equals(button.getText())) {
						return button;
					}
				}
			}
			return null;
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == '\n') {
				e.consume();
				if (confirmButton != null && confirmButton.isEnabled()) {
					confirmButton.doClick();
				}
			}
		}

		public void insertUpdate(DocumentEvent e) {
			updateState();
		}

		public void removeUpdate(DocumentEvent e) {
			updateState();
		}

		public void changedUpdate(DocumentEvent e) {
			updateState();
		}

		private void updateState() {
			if (updating) return;
			
			// This code should be revised, as it's somewhat obscure.
			String visible = view.getText();
			int vis = visible.length();
			int clear = password.length();
			if (vis > 0) {
				// Yeah, weird corner case, I know.
				if (vis == 1 && (clear == 1 || clear > 2)) {
					password = visible;
				} else if (vis < clear) {
					password = password.substring(0, vis);
				} else {
					password = password + visible.substring(clear);
				}
			}
			final String secureVisible = vis > 0 ? obfuscate(password) : "";
			boolean update = !secureVisible.equals(visible);
			if (encrypted == null && !update) {
				update = confirmButton.isEnabled() ^ vis > 0;
			}
			if (encrypted != null && !update && !encrypted.matches(password)) {
				update = true;
			}
			if (update) {
				SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						setUpdating(true);
						view.setText(secureVisible);
						boolean enable = secureVisible.length() > 0;
						if (enable && encrypted != null) {
							enable = encrypted.matches(password);
						}
						confirmButton.setEnabled(enable);
						setUpdating(false);
					}
					
				});
			}
		}

		private boolean updating = false;
		
		private void setUpdating(boolean updating) {
			this.updating = updating;
		}
		
		private String obfuscate(String visible) {
			char[] secret = visible.toCharArray();
			for (int i=0; i < secret.length -1; ++i) {
				secret[i] = '*';
			}
			return new String(secret);
		}
	};
}
