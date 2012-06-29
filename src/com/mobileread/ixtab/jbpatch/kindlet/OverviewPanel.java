package com.mobileread.ixtab.jbpatch.kindlet;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mobileread.ixtab.jbpatch.Patch;
import com.mobileread.ixtab.jbpatch.PatchRepository;

public class OverviewPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private static final int ENTRIES_PER_PAGE = 5;
	
	private final JPanel[] centerPanels;
	private final JPanel masterCentralPanel;
	
	private final JButton previousPageButton = new JButton("<<");
	private final JButton nextPageButton = new JButton(">>");
	private final JPanel bottomPanel;
	
	private int currentPage = 0;

	public OverviewPanel() {
		centerPanels = initCenterPanels();
		masterCentralPanel = initMasterCenterPanel();
		bottomPanel = initBottomPanel();
		setLayout(new BorderLayout());
		add(masterCentralPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		
		setPage(0);
		
		// this is actually used as a simple way to get some padding around
		// the edges.
		this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));

	}

	private JPanel[] initCenterPanels() {
		List panels = new ArrayList();
		
		JPanel currentPanel = null;
		int totalEntryCount = 0;
		
		Map patches = PatchRepository.getInstance().getAvailablePatches();
		Iterator it = patches.entrySet().iterator();
		
		while (it.hasNext()) {
			Map.Entry entry = (Entry) it.next();
			Patch patch = (Patch) entry.getKey();
			Boolean enabled = (Boolean) entry.getValue();
			
			OverviewEntry patchEntry = new OverviewEntry(patch, enabled.booleanValue());
			
			if (totalEntryCount % ENTRIES_PER_PAGE == 0) {
				currentPanel = new JPanel();
				currentPanel.setLayout(new BoxLayout(currentPanel, BoxLayout.Y_AXIS));
				panels.add(currentPanel);
			}
			currentPanel.add(patchEntry);
			++totalEntryCount;
		}
		
		if (currentPanel == null) {
			// no entries at all
			currentPanel = new JPanel(new GridBagLayout());
			
	        GridBagConstraints gbc = createFillingGridBagConstraints();
	        
			String text = JBPatchUI.localize(JBPatchUI._OVERVIEW_NOPATCHES);
			currentPanel.add(new JLabel(text), gbc);
			panels.add(currentPanel);
		} else {
			while (totalEntryCount % ENTRIES_PER_PAGE != 0) {
				JPanel filler = new JPanel();
				filler.setLayout(new GridBagLayout());
				filler.add(new JLabel(""), createFillingGridBagConstraints());
				++totalEntryCount;
				currentPanel.add(filler);
			}
		}
		JPanel[] array = new JPanel[panels.size()];
		panels.toArray(array);
		return array;
	}

	private GridBagConstraints createFillingGridBagConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.fill |= GridBagConstraints.VERTICAL;
		gbc.weightx  = 1.0;
		gbc.weighty  = 1.0;
		return gbc;
	}
	
	private JPanel initMasterCenterPanel() {
		JPanel panel = new JPanel(new CardLayout());
		for (int i=0; i < centerPanels.length; ++i) {
			panel.add(centerPanels[i], "");
		}
		return panel;
	}

	private JPanel initBottomPanel() {
		JPanel bottom = new JPanel(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx  = 1.0;
        gbc.weighty  = 1.0;

		bottom.add(previousPageButton, gbc);
		++gbc.gridx;
		bottom.add(nextPageButton, gbc);
		
		previousPageButton.addActionListener(this);
		nextPageButton.addActionListener(this);
		return bottom;
	}

	public void actionPerformed(ActionEvent e) {
		if (previousPageButton.equals(e.getSource())) {
			turnPage(-1);
		} else if (nextPageButton.equals(e.getSource())) {
			turnPage(+1);
		}
	}

	private void turnPage(int direction) {
		setPage(currentPage + direction);
	}

	private void setPage(int newPage) {
		if (newPage >= 0 && newPage <= centerPanels.length) {
			// the while loop should really be useless, because we only
			// expect offsets of at most -1 or 1
			while (newPage < currentPage) {
				((CardLayout)masterCentralPanel.getLayout()).previous(masterCentralPanel);
				--currentPage;
			}
			while (newPage > currentPage) {
				((CardLayout)masterCentralPanel.getLayout()).next(masterCentralPanel);
				++currentPage;
			}
		}
		currentPage = newPage;
		previousPageButton.setEnabled(newPage > 0);
		nextPageButton.setEnabled(currentPage < centerPanels.length -1 );
	}


}
