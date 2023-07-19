////////////////////////////////////////////////////
// ChargeCarrierPreferencesPopup.java
// Created May 2018
////////////////////////////////////////////////////
package edu.umich.wld;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import edu.umich.wld.panels.ChargeCarrierPreferencePanel;


public class ChargeCarrierPreferencesPopup extends JDialog {

	private static final long serialVersionUID = 4384224248678352709L;
	
	private boolean cancelled = false;

	private JDialog dialog;
	private JPanel outerPanel, buttonPanel;
	private ChargeCarrierPreferencePanel prefsPanel;
	private ChargeCarrierPreferences currentPreferences = null;
	
	
	public ChargeCarrierPreferencesPopup(JFrame parent, ChargeCarrierPreferences currentPreferences, boolean fShowDialog) {
		super(parent, true);
		this.currentPreferences = currentPreferences;
		dialog = new JDialog();
		createControls(dialog);
		dialog.setTitle("Charge Carrier Details");
		dialog.add(Box.createHorizontalStrut(8), BorderLayout.WEST);
		dialog.add(outerPanel, BorderLayout.CENTER);
		dialog.add(Box.createHorizontalStrut(8), BorderLayout.EAST);
		dialog.setModal(true);
		dialog.setSize(450,240);
		dialog.setLocationRelativeTo(getOwner());
		dialog.setVisible(fShowDialog);
	}
	
	private void createControls(JDialog dialog2) {	
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		outerPanel = new JPanel();
		outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));
		outerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		outerPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		prefsPanel = new ChargeCarrierPreferencePanel(currentPreferences);
		prefsPanel.setBounds(20, 20, 410, 250);
		buttonPanel = createButtonPanel();		
		
		outerPanel.add(Box.createVerticalStrut(3));
		outerPanel.add(prefsPanel);
		outerPanel.add(Box.createVerticalStrut(2));
		outerPanel.add(buttonPanel);
		outerPanel.add(Box.createVerticalStrut(3));
	}

	private JPanel createButtonPanel() {
		JPanel ret = new JPanel();
		ret.add(Box.createHorizontalStrut(10));
		ret.add(makeActionButton("   OK   ", makeOKActionListener(), true));
		ret.add(Box.createHorizontalStrut(25));
		ret.add(makeActionButton("Cancel", makeCancelActionListener(), false));
		ret.add(Box.createHorizontalStrut(10));
		return ret;
	}

	private JButton makeActionButton(String label, ActionListener l, boolean isDefault) {
		JButton button = new JButton(label);
		button.addActionListener(l);
		if (isDefault) {
			getRootPane().setDefaultButton(button);
		}
		return button;
	}

	private ActionListener makeCancelActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				setCancelled(true);
				dialog.setVisible(false);
			}
		};
	}

	private ActionListener makeOKActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				setCancelled(false);
				dialog.setVisible(false);
			}
		};
	}
	
	public Font boldFontForTitlePanel(TitledBorder border, boolean makeEvenLarger){
		//see http://bugs.sun.com/view_bug.do?bug_id=7022041 - getTitleFont() can return null - tew 8/14/12
		// A special thanks to zq (signed 'thomas') from gdufs.edu.cn and Dr. Zaho at kiz.ac.cn for spotting
		// the bug and assisting with the fix.
		Font font = border.getTitleFont();
		if (font == null) {
			font = UIManager.getDefaults().getFont("TitledBorder.font");
			if (font == null) {
				font = new Font("SansSerif", Font.BOLD, 12);
			} else {
				font = font.deriveFont(Font.BOLD);
			}
		} else {
			font = font.deriveFont(Font.BOLD);			
		}
		Font biggerFont = new Font(font.getName(), font.getStyle(), font.getSize() + (makeEvenLarger ? 3 : 1));
		return biggerFont;
	}
	
	public ChargeCarrierPreferences getPreferences() {
		if (isCancelled()) {
			return currentPreferences;
		}
		return prefsPanel.getPreferences();
	}

	
	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}

