package com.mobileread.ixtab.patch.progressbar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.amazon.agui.swing.KDialog;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.conf.ui.SettingEntry;

public class ProgressBarDialog extends KDialog implements ItemListener {
	
	private static final long serialVersionUID = 1L;
	private static final String APP_ID = "persistant.app";
	
	private String selectedEntryKey = null;
	private final String[] keys;
	private final ButtonGroup buttons;
	private final JComponent view;
	private final ProgressBarPatch patch;

	public ProgressBarDialog(ProgressBarPatch patch, ExtendedSettingEntry[] entries, JComponent view) {
		super(APP_ID);
		setTitleBarEnabled(true);
		setTitle(patch.localize(Patch.I18N_JBPATCH_NAME));
		
		this.view = view;
		this.patch = patch;
		
		keys = new String[entries.length];
		
		JPanel root = (JPanel) getContentPane();
		root.setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(entries.length, 1, 20, 20));
		
		buttons = new ButtonGroup();
		for (int i=0; i < entries.length; ++i) {
			keys[i] = entries[i].key;
			JRadioButton button = new JRadioButton();
			button.addItemListener(this);
			button.setSelected(entries[i].selected);
			button.setEnabled(entries[i].enabled);
			buttons.add(button);
			JPanel entry = new JPanel(new BorderLayout());
			entry.add(button, BorderLayout.WEST);
			JLabel label = new JLabel(entries[i].displayValue);
			label.setEnabled(button.isEnabled());
			entry.add(label);
			panel.add(entry);
		}
		root.add(panel, BorderLayout.CENTER);
		
		// bounds seem to use some absolute values over which we have no real control.
		// so we just add a (hopfully) safe margin around them.
		root.setBorder(BorderFactory.createMatteBorder(20, 10, 0, 30, Color.WHITE));
		Dimension dim = root.getPreferredSize();
		int xdelta = 0;
		int ydelta = 0;
		Dimension dialog = new Dimension(dim.width+xdelta, dim.height+ydelta);
		setDialogBounds(dialog);
	}
	
	public void itemStateChanged(ItemEvent e) {
		// yes, this is a rather crude way to do this.
		if (e.getStateChange() == ItemEvent.SELECTED) {
			Enumeration en = buttons.getElements();
			int i=0;
			while (en.hasMoreElements()) {
				Object candidate = en.nextElement();
				if (candidate.equals(e.getSource())) {
					selectedEntryKey = keys[i];
					patch.setMode(selectedEntryKey);
					view.repaint();
					break;
				}
				++i;
			}
			
		}
	}
	
	public static void post(ProgressBarPatch patchInstance, ExtendedSettingEntry[] entries, JComponent view) {
		ProgressBarDialog d = new ProgressBarDialog(patchInstance, entries, view);
		d.postDialog(APP_ID, true);
	}
	
	public static class ExtendedSettingEntry extends SettingEntry {
		public final boolean enabled;
		public final boolean selected;
		
		public ExtendedSettingEntry(SettingEntry entry, boolean enabled, boolean selected) {
			super(entry.key, entry.displayValue);
			this.enabled = enabled;
			this.selected = selected;
		}
	}
}
