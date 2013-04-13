package com.mobileread.ixtab.patch.dictionaries.fw534;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.amazon.agui.swing.KDialog;
import com.amazon.ebook.booklet.reader.plugin.systemcards.H;
import com.amazon.ebook.booklet.reader.sdk.ui.f;
import com.mobileread.ixtab.patch.dictionaries.Backend;

public class Backend534 implements Backend, PropertyChangeListener,
		ActionListener {

	private static Backend534 INSTANCE;
	private static final JButton previousButton = new JButton(" <<   ");
	private static final JButton nextButton = new JButton("   >> ");
	private static JButton[] definitionButtons = new JButton[0];

	private static Field panelField = null;
	private static H currentCard = null;
	private static JPanel currentPanel = null;
	private static int currentIndex;

	public Backend534() {
		INSTANCE = this;
		previousButton.addActionListener(INSTANCE);
		nextButton.addActionListener(INSTANCE);
	}

	public void modifyPanel(Object o, int index) {
		currentCard = (H) o;
		currentPanel = getPanel(currentCard);
		currentIndex = index;

		if (currentPanel == null) {
			return;
		}
		Component[] children = currentPanel.getComponents();
		int fi = 0;
		for (int i = 0; i < children.length; ++i) {
			if (children[i] instanceof f) {
				modifyLinkPanel((f) children[i], fi++);
			}
		}
		previousButton.setEnabled(getNextUsefulDictionaryIndex(currentCard,
				currentIndex, -1) != null);
		nextButton.setEnabled(getNextUsefulDictionaryIndex(currentCard,
				currentIndex, 1) != null);
	}

	private synchronized JPanel getPanel(H h) {
		try {
			if (panelField == null) {
				panelField = h.getClass().getDeclaredField("D");
				panelField.setAccessible(true);
			}
			return (JPanel) panelField.get(h);
		} catch (Throwable t) {
			return null;
		}
	}

	private static synchronized void modifyLinkPanel(f linkPanel, int index) {
		Component children[] = linkPanel.getComponents();
		JPanel targetPanel = null;
		JButton targetButton = null;
		SEARCH: for (int i = 0; i < children.length; ++i) {
			if (children[i] instanceof JPanel) {
				JPanel childPanel = (JPanel) children[i];
				Component[] childComponents = childPanel.getComponents();
				for (int ci = 0; ci < childComponents.length; ++ci) {
					if (childComponents[ci] instanceof ButtonsPanel) {
						return;
					}
					if (childComponents[ci] instanceof JButton) {
						targetButton = (JButton) childComponents[ci];
						targetPanel = childPanel;
						break SEARCH;
					}
				}
			}
		}

		if (targetButton != null && targetPanel != null) {
			ensureArraysSupportIndex(index);
			if (targetButton != definitionButtons[index]) {
				if (definitionButtons[index] != null) {
					definitionButtons[index]
							.removePropertyChangeListener(INSTANCE);
				} else if (index == 0) {
					// we only need to do this once, on the first occurence
					previousButton.setBorder(targetButton.getBorder());
					nextButton.setBorder(targetButton.getBorder());
					previousButton.setFont(targetButton.getFont());
					nextButton.setFont(targetButton.getFont());
				}
				definitionButtons[index] = targetButton;
				definitionButtons[index].addPropertyChangeListener(INSTANCE);
			}
			targetPanel.remove(targetButton);
			JPanel buttons = new ButtonsPanel(new BorderLayout(10, 10));
			buttons.add(previousButton, BorderLayout.WEST);
			buttons.add(targetButton, BorderLayout.CENTER);
			buttons.add(nextButton, BorderLayout.EAST);
			targetPanel.add(buttons, BorderLayout.SOUTH);
		}
	}

	private static synchronized void ensureArraysSupportIndex(int index) {
		if (definitionButtons.length <= index) {
			int oldL = definitionButtons.length;
			int newL = index + 1;
			JButton[] def = new JButton[newL];
			System.arraycopy(definitionButtons, 0, def, 0, oldL);
			definitionButtons = def;
		}
	}

	public void actionPerformed(ActionEvent e) {
		final Object src = e.getSource();
		new Thread() {
			public void run() {
				if (src instanceof JButton) {
					JButton button = (JButton) src;
					int offset = 0;
					if (button == previousButton) {
						offset = -1;
					} else if (button == nextButton) {
						offset = 1;
					}
					if (offset != 0) {
						Integer newPosition = getNextUsefulDictionaryIndex(
								currentCard, currentIndex, offset);
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
					/*
					 * The return value isn't used. It's just to make sure that
					 * we're invoking the correct method and getting back
					 * objects of the expected class -- Thanks again, Amazon /
					 * Lab126, for all that lovely obfuscation! >:( The second
					 * side effect is that even while this method is not really
					 * used, it does trigger a UI update, somewhere, somehow.
					 */
					JComponent ignore = currentCard.So(newPosition.intValue());
					if (ignore != null)
						ignore = null;

					Component dialog = currentPanel;
					while (dialog != null && !(dialog instanceof KDialog)) {
						dialog = dialog.getParent();
					}
					if (dialog != null) {
						((KDialog) dialog).setTitle(currentCard.qo(newPosition
								.intValue()));
						dialog.validate();
						dialog.repaint();
					}
				}
			});
		}
	}

	private static Integer getNextUsefulDictionaryIndex(H card, int index,
			int delta) {
		// delta is assumed to be either +1, or -1.
		int total = card.ip();
		for (int i = index + delta; i >= 0 && i < total; i += delta) {
			if (card.PN(i)) {
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
