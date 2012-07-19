package com.mobileread.ixtab.jbpatch.ui.kindlet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSetting;
import com.mobileread.ixtab.jbpatch.conf.ConfigurableSettings;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingChangeListener;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingPanel;
import com.mobileread.ixtab.jbpatch.ui.images.Images;

public class ConfigurationState implements LocalizationKeys {
	
	private static final ImageIcon ICON_CHECKMARK = Images.loadAsImageIcon(Images.CHECKMARK);
	private static final ImageIcon ICON_CROSSMARK = Images.loadAsImageIcon(Images.CROSSMARK);
	
	final Patch patch;
	private final ConfigurableSettings backend;
	private final ConfiguredSetting[] settings;
	private ConfiguredSetting currentSetting;
	private final SettingWrapperPanel settingWrapperPanel = new SettingWrapperPanel();
	
	public ConfigurationState(Patch patch) {
		this.patch = patch;
		this.backend = patch.getConfigurableSettings();
		this.settings = initSettings();
	}
	
	private ConfiguredSetting[] initSettings() {
		if (backend == null) {
			return new ConfiguredSetting[0];
		}
		ConfiguredSetting[] array = new ConfiguredSetting[backend.size()];
		Iterator it = backend.entrySet().iterator();
		int index = 0;
		while (it.hasNext()) {
			array[index] = new ConfiguredSetting(index, (Entry) it.next());
			++index;
		}
		return array;
	}
	
	int getSettingsCount() {
		return settings.length;
	}
	
	ConfiguredSetting getSetting(int index) {
		if (index < 0 || index >= settings.length) {
			return null;
		}
		return settings[index];
	}

	public ConfiguredSetting getCurrentSetting() {
		return currentSetting;
	}

	public boolean isDirty() {
		for (int i=0; i < settings.length; ++i) {
			if (settings[i].isDirty()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isDefault() {
		for (int i=0; i < settings.length; ++i) {
			if (!settings[i].isDefault()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean commit() {
		for (int i=0; i < settings.length; ++i) {
			if (settings[i].isDirty()) {
				if (!settings[i].commit()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void reset() {
		for (int i=0; i < settings.length; ++i) {
			settings[i].reset();
		}
	}
	
	public void undo() {
		for (int i=0; i < settings.length; ++i) {
			settings[i].undo();
		}
	}
	
	public final class ConfiguredSetting {
		private final String key;
		private final ConfigurableSetting setting;
		private final int index;
		private String originalValue;
		private String currentValue;
		private JLabel currentValuelabel;
		private SettingPanel settingPanel;
		
		public ConfiguredSetting(int index, Entry entry) {
			this.index = index;
			this.key = (String) entry.getKey();
			this.setting = (ConfigurableSetting) entry.getValue();
			this.originalValue = backend.getValue(this.key);
			setCurrentValue(originalValue, true);
		}

		public void setAsCurrent() {
			currentSetting = this;
		}

		public String getCurrentValue() {
			return currentValue;
		}

		public String getDefaultValue() {
			return setting.defaultValue;
		}

		public boolean isDefault() {
			return getDefaultValue().equals(currentValue);
		}

		public boolean isDirty() {
			// values should never be null, but we're just being defensive.
			if (originalValue != null) {
				return !originalValue.equals(currentValue);
			}
			return currentValue != null;
		}

		public boolean commit() {
			if (backend.setValue(key, currentValue)) {
				originalValue = currentValue;
				return true;
			}
			return false;
		}
		
		public void reset() {
			setCurrentValue(getDefaultValue(), true);
		}
		
		public void undo() {
			setCurrentValue(originalValue, true);
		}

		public String getName() {
			return setting.name;
		}

		public String getDescription() {
			return setting.description;
		}

		public String getHint() {
			return setting.hint;
		}

		public ConfiguredSetting previous() {
			return getSetting(index-1);
		}

		public ConfiguredSetting next() {
			return getSetting(index+1);
		}

		public void associateWith(JLabel label) {
			this.currentValuelabel = label;
			updateLabel();
		}
		
		private void setCurrentValue(String value, boolean updateSettingPanel) {
			currentValue = value;
			updateLabel();
			if (updateSettingPanel) {
				updateSettingPanel();
			}
		}

		private void updateLabel() {
			if (currentValuelabel != null) {
				currentValuelabel.setText(JBPatchUI.localize(_CONFIG_SETTING_CURRENT) + " "+getCurrentValue());
			}
		}

		private void updateSettingPanel() {
			if (settingPanel != null) {
				settingPanel.setValue(currentValue);
			}
		}

		public JPanel getPanel() {
			if (settingPanel == null) {
				settingPanel = (SettingPanel) setting.getPanel(settingWrapperPanel);
				settingPanel.setValue(currentValue);
			}
			settingWrapperPanel.setDelegate(settingPanel, this);
			return settingWrapperPanel;
		}

		public boolean isValid(String value) {
			return setting.isValid(value);
		}

	}

	public void createPanels(ConfigurationPanel context, JPanel container) {
		for (int i=0; i < settings.length; ++i) {
			createSettingDescriptionPanel(context, container, settings[i]);
		}
	}
	
	private void createSettingDescriptionPanel(final ConfigurationPanel context, JPanel container,
			final ConfiguredSetting setting) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(5,5));
		panel.setBorder(Borders.WHITE3BLACK1WHITE10);
		
		JLabel title = new JLabel(setting.getName());
		Font font = title.getFont();
		title.setFont(new Font(font.getName(), font.getStyle(),
				(int) (font.getSize() * 1.3)));

		JMultiLineLabel description = new JMultiLineLabel(setting.getDescription());
		JLabel currentSetting = new JLabel();
		setting.associateWith(currentSetting);
		JLabel defaultSetting = new JLabel(JBPatchUI.localize(_CONFIG_SETTING_DEFAULT) + " "+setting.getDefaultValue());
		defaultSetting.setForeground(Color.GRAY);
		
		JPanel settingsBox = new JPanel();
		settingsBox.setLayout(new BoxLayout(settingsBox, BoxLayout.Y_AXIS));
		settingsBox.add(currentSetting);
		settingsBox.add(defaultSetting);
		
		panel.add(title, BorderLayout.NORTH);
		panel.add(description, BorderLayout.CENTER);
		panel.add(settingsBox, BorderLayout.SOUTH);
		
		MouseAdapter listener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				context.displaySetting(setting);
			}
		};
		
		panel.addMouseListener(listener);
		// for some reason, JMultiLineLabel seems to not pass on clicks, maybe because it's not focusable(?).
		// Explicitly adding a listener fixes that.
		description.addMouseListener(listener);
		container.add(panel);
	}

	private class SettingWrapperPanel extends JPanel implements ActionListener, SettingChangeListener {
		private static final long serialVersionUID = 1L;
		private final JButton defaultButton = new JButton(JBPatchUI.localize(_CONFIG_BUTTON_RESET));
		private final JButton undoButton = new JButton(JBPatchUI.localize(_CONFIG_BUTTON_UNDO));
		
		private final JLabel validityHintIconLabel = createIconLabel(ICON_CHECKMARK);
		
		private final JMultiLineLabel hintLabel = new JMultiLineLabel("hint");
		private final JLabel activeLabel = new JLabel("active");
		private final JLabel previousLabel = new JLabel("previous");
		private final JLabel defaultLabel = new JLabel("default");
		private final JPanel settingPanel = new JPanel(new BorderLayout());
		
		private String originalValue;
		private SettingPanel delegate;
		private ConfiguredSetting setting;
		
		private SettingWrapperPanel() {
			setLayout(new BorderLayout());
			JPanel top = new JPanel(new BorderLayout(10,10));
			top.setBorder(Borders.WHITE10);
			top.add(hintLabel, BorderLayout.NORTH);
			top.add(settingPanel, BorderLayout.CENTER);
			
			JPanel buttons = new JPanel(new GridLayout(1, 2, 10, 0));
			buttons.add(undoButton);
			buttons.add(defaultButton);
			top.add(buttons, BorderLayout.SOUTH);
			
			JPanel labelsBox = new JPanel(new BorderLayout());
			labelsBox.setBorder(Borders.WHITE10);
			
			JPanel statusBox = new JPanel();
			statusBox.setBorder(Borders.WHITE10);
			statusBox.setLayout(new BoxLayout(statusBox, BoxLayout.Y_AXIS));
			statusBox.add(validityHintIconLabel);
			statusBox.add(Box.createVerticalGlue());
		
			JPanel labels = new JPanel();
			labels.setLayout(new BoxLayout(labels, BoxLayout.Y_AXIS));
			labels.add(activeLabel);
			labels.add(previousLabel);
			labels.add(defaultLabel);
			labels.add(Box.createVerticalGlue());
			
			labelsBox.add(labels, BorderLayout.CENTER);
			labelsBox.add(statusBox, BorderLayout.WEST);
			
			add(top, BorderLayout.NORTH);
			add(labelsBox, BorderLayout.CENTER);
			
			defaultLabel.setForeground(Color.GRAY);
			previousLabel.setForeground(Color.GRAY);
			
			defaultButton.addActionListener(this);
			undoButton.addActionListener(this);
		}
		
		private JLabel createIconLabel(ImageIcon icon) {
			JLabel label = new JLabel(icon);
			Dimension d = new Dimension(icon.getIconWidth(), icon.getIconHeight());
			label.setMinimumSize(d);
			label.setPreferredSize(d);
			label.setSize(d);
			label.setMaximumSize(d);
			return label;
		}

		private void setDelegate(SettingPanel delegate, ConfiguredSetting setting) {
			if (delegate == this.delegate) {
				return;
			}
			this.delegate = delegate;
			this.setting = setting;
			updateValidityHints(setting.getCurrentValue(), setting.isValid(setting.getCurrentValue()));
			previousLabel.setText(JBPatchUI.localize(_CONFIG_SETTING_PREVIOUS)+" "+setting.getCurrentValue());
			defaultLabel.setText(JBPatchUI.localize(_CONFIG_SETTING_DEFAULT)+" "+setting.getDefaultValue());
			hintLabel.setText(setting.getHint());
			originalValue = setting.getCurrentValue();
			reinitializeUI();
		}

		private void updateValidityHints(String value, boolean valid) {
			activeLabel.setText(JBPatchUI.localize(_CONFIG_SETTING_CURRENT)+" "+value);
			activeLabel.setForeground(valid ? Color.BLACK : Color.GRAY);
			validityHintIconLabel.setIcon(valid? ICON_CHECKMARK : ICON_CROSSMARK);
		}

		private void reinitializeUI() {
			settingPanel.removeAll();
			settingPanel.add((Component) delegate, BorderLayout.CENTER);
		}

		public void actionPerformed(ActionEvent e) {
			if (defaultButton.equals(e.getSource())) {
				setValue(setting.getDefaultValue());
			} else if (undoButton.equals(e.getSource())) {
				setValue(originalValue);
			}
		}

		private void setValue(String value) {
			delegate.setValue(value);
		}

		public void valueChanged(Component source, String newValue) {
			if (source == delegate) {
				if (setting.isValid(newValue)) {
					setting.setCurrentValue(newValue, false);
					updateValidityHints(newValue, true);
				} else {
					setting.setCurrentValue(originalValue, false);
					updateValidityHints(originalValue, false);
				}
			}
		}

	}
}
