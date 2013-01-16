package com.mobileread.ixtab.patch.dictionaries.fw532;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.amazon.agui.swing.KDialog;
import com.amazon.ebook.booklet.reader.plugin.systemcards.G;
import com.amazon.ebook.booklet.reader.sdk.ui.e;
import com.mobileread.ixtab.patch.dictionaries.Backend;

// This class only exists because if the patch itself refers to any of its patched classes,
// things are likely to screw up.

public class Backend532 implements ActionListener, PropertyChangeListener, Backend {
	
	private static final JButton previousButton = new JButton(" <<   ");
	private static final JButton nextButton = new JButton("   >> ");
	private static G dictCard = null;
	private static int dictIndex = 0;
	private static JButton definitionButton = null;
	private static Backend532 INSTANCE;
	
	public Backend532() {
		INSTANCE = this;
		previousButton.addActionListener(INSTANCE);
		nextButton.addActionListener(INSTANCE);
	}
	
	public void modifyPanel(Object o, int currentIndex) {
		dictIndex = currentIndex;
		dictCard = (G) o;
		Component[] children = dictCard.getComponents();
		for (int i=0; i < children.length; ++i) {
			if (children[i] instanceof e) {
				modifyLinkPanel((e)children[i]);
				break;
			}
		}
		previousButton.setEnabled(getNextUsefulDictionaryIndex(dictIndex, -1) != null);
		nextButton.setEnabled(getNextUsefulDictionaryIndex(dictIndex, 1) != null);
	}
	
	private static void modifyLinkPanel(e linkPanel) {
		Component children[] = linkPanel.getComponents();
		JButton theButton = null;
		JPanel thePanel = null;
		for (int i=0; i < children.length; ++i) {
			if (children[i] instanceof JPanel) {
				JPanel childPanel = (JPanel) children[i];
				Component[] childComponents = childPanel.getComponents();
				for (int ci=0; ci < childComponents.length; ++ci) {
					if (childComponents[ci] instanceof ButtonsPanel) {
						return;
					}
					if (childComponents[ci] instanceof JButton) {
						theButton = (JButton) childComponents[ci];
						thePanel = childPanel;
						break;
					}
				}
			}
		}
		if (theButton != null && thePanel != null) {
			if (theButton != definitionButton) {
				if (definitionButton != null) {
					definitionButton.removePropertyChangeListener(INSTANCE);
				}
				else {
					previousButton.setBorder(theButton.getBorder());
					nextButton.setBorder(theButton.getBorder());
					previousButton.setFont(theButton.getFont());
					nextButton.setFont(theButton.getFont());
				}
				definitionButton = theButton;
				definitionButton.addPropertyChangeListener(INSTANCE);
			}
			thePanel.remove(theButton);
			JPanel buttons = new ButtonsPanel(new BorderLayout(10, 10));
			buttons.add(previousButton, BorderLayout.WEST);
			buttons.add(theButton, BorderLayout.CENTER);
			buttons.add(nextButton, BorderLayout.EAST);
			thePanel.add(buttons, BorderLayout.SOUTH);
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
					dictCard.ON(newPosition.intValue());
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
						((KDialog)dialog).setTitle(dictCard.pN(newPosition.intValue()));
					}
					dialog.validate();
					dialog.repaint();
				}
			});
		}
	}
	
	private static Integer getNextUsefulDictionaryIndex(int current, int delta) {
		// delta is assumed to be either +1, or -1.
		int total = dictCard.Yn();
		for (int i= current + delta; i >= 0 && i < total; i += delta) {
			if (dictCard.Qn(i)) {
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
