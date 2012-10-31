package com.mobileread.ixtab.patch.kt.dictionaries;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.amazon.agui.swing.KDialog;
import com.amazon.ebook.booklet.reader.plugin.systemcards.DictionaryCard;
import com.amazon.ebook.booklet.reader.sdk.ui.LinkPanel;

// This class only exists because if the patch itself refers to any of its patched classes,
// things are likely to screw up.

public class Backend implements ActionListener, PropertyChangeListener {
	
	private static final JButton previousButton = new JButton("<");
	private static final JButton nextButton = new JButton(">");
	private static DictionaryCard dictCard = null;
	private static int dictIndex = 0;
	private static JButton definitionButton = null;
	private static final Backend INSTANCE;
	
	static {
		INSTANCE = new Backend();
		previousButton.addActionListener(INSTANCE);
		nextButton.addActionListener(INSTANCE);
	}
	
	public static void modifyPanel(Object o, int currentIndex) {
		dictIndex = currentIndex;
		dictCard = (DictionaryCard) o;
		Component[] children = dictCard.getComponents();
		for (int i=0; i < children.length; ++i) {
			if (children[i] instanceof LinkPanel) {
				modifyLinkPanel((LinkPanel)children[i]);
				break;
			}
		}
		previousButton.setEnabled(getNextUsefulDictionaryIndex(dictIndex, -1) != null);
		nextButton.setEnabled(getNextUsefulDictionaryIndex(dictIndex, 1) != null);
	}
	
	private static void modifyLinkPanel(LinkPanel linkPanel) {
		Component children[] = linkPanel.getComponents();
		JButton theButton = null;
		for (int i=0; i < children.length; ++i) {
			if (children[i] instanceof ButtonsPanel) {
				return;
			}
			if (children[i] instanceof JButton) {
				theButton = (JButton) children[i];
			}
		}
		if (theButton != null) {
			if (theButton != definitionButton) {
				if (definitionButton != null) {
					definitionButton.removePropertyChangeListener(INSTANCE);
				}
				definitionButton = theButton;
				definitionButton.addPropertyChangeListener(INSTANCE);
			}
			linkPanel.remove(theButton);
			JPanel buttons = new ButtonsPanel(new BorderLayout(10, 10));
			JPanel left = new JPanel(new GridLayout(1, 2, 5, 5));
			left.add(previousButton);
			left.add(nextButton);
			buttons.add(left, BorderLayout.WEST);
			buttons.add(theButton, BorderLayout.CENTER);
			linkPanel.add(buttons, BorderLayout.SOUTH);
		}
	}

	public void actionPerformed(ActionEvent e) {
		final Object src = e.getSource();
		new Thread() {
			public void run() {
				if (src instanceof JButton) {
					JButton button = (JButton) src;
					if (button == previousButton) {
						Integer newPosition = getNextUsefulDictionaryIndex(dictIndex, -1);
						if (newPosition != null) {
							gotoDict(newPosition);
						}
					} else if (button == nextButton) {
						Integer newPosition = getNextUsefulDictionaryIndex(dictIndex, 1);
						if (newPosition != null) {
							gotoDict(newPosition);
						}
					}
				}
			}
		}.start();
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if ("enabled".equals(evt.getPropertyName())) {
			boolean enabled = ((Boolean) evt.getNewValue()).booleanValue();
			previousButton.setEnabled(enabled);
			nextButton.setEnabled(enabled);
		}
	}


	private void gotoDict(final Integer newPosition) {
		if (newPosition != null) {
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					dictCard.K(newPosition.intValue());
					Component dialog = dictCard;
					while (!(dialog instanceof KDialog)) {
						dialog = dialog.getParent();
						if (dialog == null) {
							// should never happen
							dialog = dictCard;
							break;
						}
					}
					if (dialog instanceof KDialog) {
						((KDialog)dialog).setTitle(dictCard.H(newPosition.intValue()));
					}
					dialog.validate();
					dialog.repaint();
				}
			});
		}
	}

	private static Integer getNextUsefulDictionaryIndex(int current, int delta) {
		// delta is assumed to be either +1, or -1.
		int total = dictCard.b();
		for (int i= current + delta; i >= 0 && i < total; i += delta) {
			if (dictCard.B(i)) {
				return new Integer(i);
			}
		}
		return null;
	}
	
	private static class ButtonsPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		public ButtonsPanel(LayoutManager layout) {
			super(layout);
		}
	}

}
