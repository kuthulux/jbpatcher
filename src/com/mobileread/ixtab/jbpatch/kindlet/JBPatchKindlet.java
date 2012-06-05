package com.mobileread.ixtab.jbpatch.kindlet;

import ixtab.jailbreak.Jailbreak;
import ixtab.jailbreak.SuicidalKindlet;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.amazon.kindle.kindlet.KindletContext;
import com.amazon.kindle.kindlet.ui.KOptionPane;

public class JBPatchKindlet extends SuicidalKindlet {

	private KindletContext context;
	private JLabel centerLabel;
	
	protected Jailbreak instantiateJailbreak() {
		return new JBPatchJailbreak();
	}


	public void onCreate(KindletContext kindletContext) {
		this.context = kindletContext;
		//final Container root = context.getRootContainer();
		String startupMessage = "mobileread.net";
		this.centerLabel = new JLabel(startupMessage);
		initUi();
		
		/* The reason for this is not only that it allows for a splash screen.
		 * More importantly, the initialization may take quite some time, and
		 * if we put everything directly into onCreate(), startup may time out,
		 * and fail to launch the application.
		 */
		new Thread() {
			public void run() {
				try {
					while (!(context.getRootContainer().isValid() && context.getRootContainer().isVisible())) {
						Thread.sleep(100);
					}
					// splash screen
					Thread.sleep(2500);
				} catch (Exception e) {};
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (jailbreak.isAvailable()) {
							if (((JBPatchJailbreak)jailbreak).requestPermissions()) {
								try {
									Class.forName("com.mobileread.ixtab.jbpatch.PatchRepository");
									context.getRootContainer().removeAll();
									JBPatchUI ui = new JBPatchUI(context.getRootContainer());
									ui.init();
								} catch (ClassNotFoundException e) {
									String title = "JBPatch not installed";
									setCentralMessage(title);
									String error = "You do not seem to have a working installation of JBPatch. Please install JBPatch before using this program.";
									KOptionPane.showMessageDialog(context.getRootContainer(), error, title);
								}
							} else {
								String title = "Kindlet Jailbreak Failed";
								setCentralMessage(title);
								String error = "The Kindlet Jailbreak failed to obtain all required permissions. Please report this error.";
								KOptionPane.showMessageDialog(context.getRootContainer(), error, title);
							}
						} else {
							String title = "Kindlet Jailbreak Required";
							String message = "This application requires the Kindlet Jailbreak to be installed. This is an additional jailbreak that must be installed on top of the Device Jailbreak, in order to allow Kindlets to get the required permissions. Please install the Kindlet Jailbreak before using this application.";
							setCentralMessage(title);
							KOptionPane.showMessageDialog(context.getRootContainer(), message, title);
						}
					}
				});
			}
		}.start();
	}


	private void setCentralMessage(String centered) {
		centerLabel.setText(centered);
	}

	
	private void initUi() {
		Container pane = context.getRootContainer();
		pane.removeAll();
		centerLabel.setFont(new Font(centerLabel.getFont().getName(), Font.BOLD, centerLabel.getFont().getSize() + 6));
		
		pane.setLayout(new GridBagLayout());
		
		// I still don't understand how GridBagLayout really works, but this centers the message.
		
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.fill |= GridBagConstraints.VERTICAL;
        gbc.weightx  = 1.0;
        gbc.weighty  = 1.0;
        
		pane.add(centerLabel, gbc);
	}

	

}