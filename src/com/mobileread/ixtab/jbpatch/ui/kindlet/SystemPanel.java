package com.mobileread.ixtab.jbpatch.ui.kindlet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.amazon.kindle.kindlet.ui.KOptionPane;
import com.mobileread.ixtab.jbpatch.JBPatchMetadata;
import com.mobileread.ixtab.jbpatch.KindleDirectories;
import com.mobileread.ixtab.jbpatch.Log;

public class SystemPanel extends JPanel implements LocalizationKeys {

	private static final long serialVersionUID = 2536364089885255120L;

	private final JTextArea logArea = new JTextArea(20, 20);

	public SystemPanel() {
		initUI();
	}

	private void initUI() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 10));

		this.add(createLogPanel(), BorderLayout.CENTER);

		JPanel bottom = new JPanel(new BorderLayout());
		bottom.add(createButtonsPanel(), BorderLayout.CENTER);
		bottom.add(createAboutPanel(), BorderLayout.SOUTH);
		this.add(bottom, BorderLayout.SOUTH);

	}

	private JPanel createLogPanel() {
		logArea.setEditable(false);

		JPanel panel = new JPanel(new BorderLayout(5, 5));

		logArea.setFont(new Font("Monospace", Font.PLAIN, logArea.getFont()
				.getSize() * 3 / 4));
		JScrollPane resultWrapper = new JScrollPane(logArea);
		resultWrapper.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK, 2),
				" " + JBPatchUI.localize(_SYSTEM_LOG_TITLE) + " "));
		panel.add(resultWrapper, BorderLayout.CENTER);
		refreshLog();
		return panel;
	}

	private void refreshLog() {
		Runnable async = new Runnable() {

			public void run() {
				final String log = getLogText();
				SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						logArea.setText(log);
					}

				});
			}

		};
		new Thread(async).start();
	}

	private String getLogText() {
		StringBuffer text = new StringBuffer();
		try {
			File file = new File(Log.LOGFILE);
			if (!file.exists()) {
				text.append("ERROR: " + file + " does not exist");
			} else {
				FileInputStream fis = new FileInputStream(Log.LOGFILE);
				BufferedReader r = new BufferedReader(new InputStreamReader(
						fis, "UTF-8"));
				for (String line = r.readLine(); line != null; line = r
						.readLine()) {
					text.append(line);
					text.append("\n");
				}
				r.close();
			}
		} catch (IOException io) {
			text.append("ERROR: " + io.getMessage());
		}

		return text.toString();

	}

	private JPanel createAboutPanel() {
		JPanel about = new JPanel();

		// about.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK,
		// 2), " " + JBPatchUI.localize(_SYSTEM_ABOUT_TITLE)+ " "));
		// about.setLayout(new GridLayout(1, 1));
		// about.add(new JLabel("   " +
		// JBPatchUI.localize(_SYSTEM_ABOUT_VERSION)+ ": "
		// +JBPatchMetadata.VERSION));

		about.setLayout(new BoxLayout(about, BoxLayout.X_AXIS));
		about.add(Box.createHorizontalGlue());
		about.add(
				new JLabel(JBPatchUI.localize(_SYSTEM_ABOUT_VERSION)
						+ " " + JBPatchMetadata.VERSION), BorderLayout.CENTER);
		about.add(Box.createHorizontalGlue());
		return about;
	}

	private JPanel createButtonsPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK, 2),
				" " + JBPatchUI.localize(_SYSTEM_ACTIONS_TITLE) + " "));

		JPanel inner = new JPanel();
		inner.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
		inner.setLayout(new GridLayout(2, 2, 10, 10));
		inner.add(createLogRefreshButton());
		inner.add(createRestartButton());
		inner.add(createSyncButton());
		inner.add(createCleanupButton());
		panel.add(inner, BorderLayout.CENTER);
		// panel.add(new JLabel("   " +
		// JBPatchUI.localize("system.about.version")+ ": "
		// +JBPatchMetadata.VERSION));

		return panel;
	}

	private Component createLogRefreshButton() {
		JButton button = new JButton(
				JBPatchUI.localize(_SYSTEM_ACTIONS_LOGREFRESH));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				refreshLog();
			}
		});
		return button;
	}

	private Component createRestartButton() {
		JButton button = new JButton(
				JBPatchUI.localize(_SYSTEM_ACTIONS_RESTART));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(confirm(_SYSTEM_ACTIONS_RESTART, _SYSTEM_CONFIRM_CONTINUE)) {
					performRestart();
				}
			}

		});
		return button;
	}
	
	private JButton createSyncButton() {
		final JButton button = new JButton(
				JBPatchUI.localize(_SYSTEM_ACTIONS_SYNC));
		button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if (confirm(_SYSTEM_ACTIONS_SYNC, _SYSTEM_CONFIRM_DANGEROUS)) {
					performSync(button);
				}
			}
		});
		return button;
	}

	private Component createCleanupButton() {
		final JButton button = new JButton(
				JBPatchUI.localize(_SYSTEM_ACTIONS_CLEANUP));
		button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if (confirm(_SYSTEM_ACTIONS_CLEANUP, _SYSTEM_CONFIRM_DANGEROUS)) {
					performCleanup(button);
				}
			}

		});
		return button;
	}

	private void performRestart() {
		try {
			Runtime.getRuntime().exec("/usr/bin/killall cvm");
		} catch (IOException e) {
			Log.INSTANCE.println("Failed to restart framework, stacktrace follows");
			e.printStackTrace(Log.INSTANCE);
		}
	}

	private void performSync(final JButton button) {
		Thread async = new Thread() {
			public void run() {
				final boolean ok = KindleDirectories.reverseSync();
				SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						button.setEnabled(true);
						SystemPanel.this.notify(_SYSTEM_ACTIONS_SYNC, ok);
					}
					
				});
			}
		};
		button.setEnabled(false);
		async.start();
	}

	private void performCleanup(final JButton button) {
		Thread async = new Thread() {
			public void run() {
				final boolean ok = KindleDirectories.cleanup();
				SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						button.setEnabled(true);
						SystemPanel.this.notify(_SYSTEM_ACTIONS_CLEANUP, ok);
					}
					
				});
			}
		};
		button.setEnabled(false);
		async.start();
	}
	
	private boolean confirm(String titleKey,
			String textKey) {
		return KOptionPane.OK_OPTION == KOptionPane.showConfirmDialog(this, JBPatchUI.localize(textKey), JBPatchUI.localize(titleKey), KOptionPane.CANCEL_OK_OPTION);
	}
	
	private void notify(String titleKey, boolean ok) {
		String msg = JBPatchUI.localize(ok ? _SYSTEM_ACTION_SUCCESS: _SYSTEM_ACTION_FAILED);
		KOptionPane.showMessageDialog(this, msg, JBPatchUI.localize(titleKey));
	}

}
