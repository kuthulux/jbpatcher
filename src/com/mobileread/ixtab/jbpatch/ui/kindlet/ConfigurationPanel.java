package com.mobileread.ixtab.jbpatch.ui.kindlet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.amazon.kindle.kindlet.ui.KOptionPane;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.conf.ui.JMultiLineLabel;

public class ConfigurationPanel extends JPanel implements ChangeListener,
		LocalizationKeys {

	private static final double TITLEFONT_SCALE = 1.3;

	static ConfigurationPanel INSTANCE;

	private static final long serialVersionUID = 1L;

	private final JTabbedPane tabContext;
	private final int tabIndex;

	private final JLabel titleLabel = createTitleLabel();
	private final JPanel contentPanel = createContentPanel();
	private final JMultiLineLabel descriptionLabel = new JMultiLineLabel(
			"description");

	private final JButton previousSettingButton = createPreviousSettingButton();
	private final JButton nextSettingButton = createNextSettingButton();
	private final JButton allSettingsButton = createAllSettingsButton();

	private final JButton undoSettingsButton = createUndoSettingsButton();
	private final JButton commitSettingsButton = createCommitSettingsButton();
	private final JButton resetSettingsButton = createResetSettingsButton();

	private final ButtonsPanel noSettingsButtonPanel = new ButtonsPanel();
	private final ButtonsPanel allSettingsButtonPanel = createAllSettingsButtonPanel();
	private final ButtonsPanel itemSettingsButtonPanel = createItemButtonPanel();
	private final JPanel buttonPanelWrapper = new JPanel(new BorderLayout());
	private ButtonsPanel activeButtonPanel;

	private ConfigurationState currentState = null;

	ConfigurationPanel(JTabbedPane context, int ownIndex) {
		INSTANCE = this;
		this.tabContext = context;
		this.tabIndex = ownIndex;
		tabContext.addChangeListener(this);
		initUI();

	}

	private JButton createPreviousSettingButton() {
		JButton button = new JButton(" < ");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displaySetting(currentState.getCurrentSetting().previous());
			}
		});
		return button;
	}

	private JButton createNextSettingButton() {
		JButton button = new JButton(" > ");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displaySetting(currentState.getCurrentSetting().next());
			}
		});
		return button;
	}

	private JButton createAllSettingsButton() {
		JButton button = new JButton(JBPatchUI.localize(_CONFIG_BUTTON_ALL));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayPatch(currentState.patch);
			}
		});
		return button;
	}

	private JButton createUndoSettingsButton() {
		JButton button = new JButton(JBPatchUI.localize(_CONFIG_BUTTON_UNDO));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undoConfiguration();
			}
		});
		return button;
	}

	private JButton createCommitSettingsButton() {
		JButton button = new JButton(JBPatchUI.localize(_CONFIG_BUTTON_COMMIT));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveConfiguration(true);
			}
		});
		return button;
	}

	private JButton createResetSettingsButton() {
		JButton button = new JButton(JBPatchUI.localize(_CONFIG_BUTTON_RESET));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetConfiguration();
			}
		});
		return button;
	}

	private ButtonsPanel createAllSettingsButtonPanel() {
		ButtonsPanel panel = new ButtonsPanel() {
			private static final long serialVersionUID = 1L;

			public void updateState() {
				undoSettingsButton.setEnabled(currentState.isDirty());
				commitSettingsButton.setEnabled(currentState.isDirty());
				resetSettingsButton.setEnabled(!currentState.isDefault());
			}

		};
		panel.setLayout(new GridLayout(1, 3, 10, 0));
		panel.add(undoSettingsButton);
		panel.add(resetSettingsButton);
		panel.add(commitSettingsButton);
		return panel;
	}

	private ButtonsPanel createItemButtonPanel() {
		ButtonsPanel panel = new ButtonsPanel() {
			private static final long serialVersionUID = 1L;

			public void updateState() {
				previousSettingButton.setEnabled(currentState
						.getCurrentSetting().previous() != null);
				nextSettingButton.setEnabled(currentState.getCurrentSetting()
						.next() != null);
			}

		};
		panel.setLayout(new GridLayout(1, 3, 10, 0));
		panel.add(previousSettingButton);
		panel.add(allSettingsButton);
		panel.add(nextSettingButton);
		return panel;
	}

	private void initUI() {
		setBorder(BorderFactory.createLineBorder(Color.WHITE, 10));

		JPanel titleBox = new JPanel();
		titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.X_AXIS));
		titleBox.add(Box.createHorizontalGlue());
		titleBox.add(titleLabel);
		titleBox.add(Box.createHorizontalGlue());

		JPanel descriptionBox = new JPanel(new BorderLayout());
		descriptionBox.add(descriptionLabel, BorderLayout.CENTER);
		descriptionBox.setBorder(Borders.WHITE10);

		JPanel top = new JPanel(new BorderLayout());
		top.add(titleBox, BorderLayout.NORTH);
		top.add(descriptionBox, BorderLayout.CENTER);

		this.setLayout(new BorderLayout());
		this.add(top, BorderLayout.NORTH);
		this.add(contentPanel, BorderLayout.CENTER);
		this.add(buttonPanelWrapper, BorderLayout.SOUTH);

		displayPatch(null);
	}

	private JPanel createContentPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		return panel;
	}

	private void updateUI(String title, String subtitle, String description,
			Component content, ButtonsPanel buttons) {

		if (title != null) {
			titleLabel.setText(title);
		}

		contentPanel.setBorder(BorderFactory.createTitledBorder(
				Borders.WHITE3BLACK1GRAY4, " " + subtitle + " "));
		contentPanel.removeAll();

		if (content != null) {
			JScrollPane scroll = new JScrollPane(content,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			// Kindle-specific: scrollpane comes with a border
			scroll.setBorder(null);
			contentPanel.add(scroll, BorderLayout.CENTER);
		}

		descriptionLabel.setText(description);

		buttonPanelWrapper.removeAll();
		buttonPanelWrapper.add(buttons);
		activeButtonPanel = buttons;
		buttons.updateState();

		/*
		 * This is needed because some of the content (in terms of LayoutManager contained components) may change.
		 */

		this.validate();
		this.repaint();
	}

	private JLabel createTitleLabel() {
		JLabel label = new JLabel();
		Font font = label.getFont();
		label.setFont(new Font(font.getName(), font.getStyle(), (int) (font
				.getSize() * TITLEFONT_SCALE)));
		return label;
	}

	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == tabContext) {
			if (tabContext.getSelectedIndex() != tabIndex) {
				if (currentState != null) {
					if (currentState.isDirty()) {
						int result = KOptionPane.showConfirmDialog(this,
								JBPatchUI.localize(_CONFIG_UNSAVED_MESSAGE),
								JBPatchUI.localize(_CONFIG_UNSAVED_TITLE),
								KOptionPane.NO_YES_OPTIONS);
						if (result != KOptionPane.NO_OPTION) {
							saveConfiguration(false);
						}
					}
					displayPatch(null);
				}
			}
		}
	}

	void displayDetails(final Patch patch) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// order is important here. First update the UI, then actually
				// change the tab.
				displayPatch(patch);
				tabContext.setSelectedIndex(tabIndex);
			}

		});
	}

	private void displayPatch(Patch patch) {
		String title = null;
		String subtitle = null;
		String description = null;
		Component content = null;
		ButtonsPanel buttons = noSettingsButtonPanel;

		if (patch == null) {
			currentState = null;
			title = JBPatchUI.localize(_CONFIG_NOPATCH_TITLE);
			subtitle = JBPatchUI.localize(_CONFIG_NOPATCH_SUBTITLE);
			description = "";
			content = new JMultiLineLabel(
					JBPatchUI.localize(_CONFIG_NOPATCH_DESCRIPTION));
		} else {
			if (currentState == null || currentState.patch != patch) {
				currentState = new ConfigurationState(patch);
			}
			title = patch.getName();
			subtitle = JBPatchUI.localize(_CONFIG_PATCH_SUBTITLE);
			description = patch.getDescription();
			if (currentState.getSettingsCount() == 0) {
				buttons = noSettingsButtonPanel;
				content = new JMultiLineLabel(
						JBPatchUI.localize(_CONFIG_PATCH_NOTCONFIGURABLE));
			} else {
				buttons = allSettingsButtonPanel;
				content = new ConfigurableItemsPanel();
				currentState.createPanels(this, (JPanel) content);
			}
		}

		updateUI(title, subtitle, description, content, buttons);
	}

	void displaySetting(ConfigurationState.ConfiguredSetting setting) {
		setting.setAsCurrent();
		final String description = setting.getDescription();
		final String subtitle = setting.getName();
		final JPanel panel = setting.getPanel();
		
		updateUI(null, subtitle, description, panel, itemSettingsButtonPanel);
	}

	private void saveConfiguration(boolean updateState) {
		if (!currentState.commit()) {
			KOptionPane.showMessageDialog(this,
					JBPatchUI.localize(_CONFIG_SAVEFAILED_MESSAGE),
					JBPatchUI.localize(_CONFIG_SAVEFAILED_TITLE));
		}
		if (updateState) {
			activeButtonPanel.updateState();
		}
	}

	private void resetConfiguration() {
		currentState.reset();
		activeButtonPanel.updateState();
	}

	private void undoConfiguration() {
		currentState.undo();
		activeButtonPanel.updateState();
	}

	private class ButtonsPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public void updateState() {

		};
	}

	private class ConfigurableItemsPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		private ConfigurableItemsPanel() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		}
	}
}
