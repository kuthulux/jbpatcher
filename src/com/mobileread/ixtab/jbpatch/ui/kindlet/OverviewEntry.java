package com.mobileread.ixtab.jbpatch.ui.kindlet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.mobileread.ixtab.jbpatch.Log;
import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchRepository;

class OverviewEntry extends JPanel {
	private static final long serialVersionUID = 1L;

	/* reuse borders, they only need to be created once. */
	private static final Color COLOR_ENABLED = Color.BLACK;
	private static final Color COLOR_DISABLED = Color.GRAY;
	private static final Color COLOR_INFO = Color.GRAY;
	
	private static final MouseAdapter entryClickAdapter = new MouseAdapter() {

		public void mouseClicked(MouseEvent e) {
			Object source = e.getSource();
			if (source instanceof OverviewEntry) {
				Patch p = ((OverviewEntry) source).patch;
				ConfigurationPanel.INSTANCE.displayDetails(p);
			}
		}
		
	};
	
	private static final ItemListener checkboxListener = new ItemListener() {
		
		public void itemStateChanged(ItemEvent e) {
			PatchCheckBox source = (PatchCheckBox) e.getSource();
			Patch p = source.patch;
			boolean enable = e.getStateChange() == ItemEvent.SELECTED;
			if (PatchRepository.getInstance().setPatchState(p, enable)) {
				Log.INSTANCE.println("I: "+ (enable ? "Enabled":"Disabled") +" patch: "+p.getName());
			} else {
				Log.INSTANCE.println("W: FAILED TO " + (enable ? "ENABLE":"DISABLE") +" PATCH: "+p.getName());
			}
			updateTitleDisplay(source.title, enable);
		}
	};
	
	
	private static void updateTitleDisplay(final JLabel title, boolean enabled) {
		final Color c = enabled ? COLOR_ENABLED : COLOR_DISABLED;
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				title.setForeground(c);
			}
		});
	}

	private final Patch patch;
	private final PatchCheckBox checkbox;
	private final JLabel title;
	
	OverviewEntry(Patch patch, boolean enabled) {
		this.patch = patch;
		this.setLayout(new BorderLayout());
		this.setBorder(Borders.WHITE3BLACK1GRAY4);
		
		this.title = new JLabel(patch.getName());
		updateTitleDisplay(title, enabled);
		
		checkbox = createCheckbox(enabled);
		checkbox.addItemListener(checkboxListener);
		JPanel checkboxPanel = new JPanel(new BorderLayout());
		checkboxPanel.setBorder(Borders.WHITE10);
		checkboxPanel.add(checkbox, BorderLayout.CENTER);
		
		
		this.add(checkboxPanel, BorderLayout.WEST);
		
		
		JPanel descriptionBox = new JPanel();
		descriptionBox.setLayout(new BoxLayout(descriptionBox, BoxLayout.X_AXIS));
		descriptionBox.add(createDescriptionPanel(title));
		descriptionBox.add(Box.createHorizontalGlue());
		this.add(descriptionBox, BorderLayout.CENTER);
		
		this.addMouseListener(entryClickAdapter);
	}

	private PatchCheckBox createCheckbox(boolean enabled) {
		PatchCheckBox box = new PatchCheckBox(patch, title);
		box.setSelected(enabled);
		return box;
	}

	private Component createDescriptionPanel(JLabel title) {
		title.setFont(getOrCreateNameFont(title.getFont()));
		
		JLabel description = new JLabel(JBPatchUI.localize(JBPatchUI._OVERVIEW_VERSION)+" "+ prettifyVersion(patch.getVersion()));
		description.setForeground(COLOR_INFO);
		
		JPanel info = new JPanel();
		info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
		info.add(title);
		info.add(description);
		return info;
	}

	private static Font nameFont = null;
	
	private Font getOrCreateNameFont(Font standard) {
		if (nameFont == null) {
			nameFont = new Font(standard.getName(), standard.getStyle(), (int) (standard.getSize() * 1.3));
		}
		
		return nameFont;
	}


	private String prettifyVersion(int version) {
		/* Yeah, this is stupid and inefficient, but simple. */
		int day = version % 100;
		int month = (version / 100) % 100;
		int year = (version / 10000) % 10000;
		
		String r = ""+day;
		while (r.length() < 2) r = "0" + r;
		r = month + "-" + r;
		while (r.length() < 5) r = "0" + r;
		r = year + "-" + r;
		while(r.length() < 10) r = "0" + r;
		
		return r;
	}

	private static class PatchCheckBox extends JCheckBox {
		private static final long serialVersionUID = 1L;
		
		private final Patch patch;
		private final JLabel title;
		public PatchCheckBox(Patch patch, JLabel title) {
			this.patch = patch;
			this.title = title;
		}
	}
}
