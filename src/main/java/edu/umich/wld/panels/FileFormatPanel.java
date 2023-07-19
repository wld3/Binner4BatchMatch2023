////////////////////////////////////////////////////
// FileFormatPanel.java
// Created, Dec 10, 2017
////////////////////////////////////////////////////
package edu.umich.wld.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import edu.umich.wld.AnalysisDialog;


public class FileFormatPanel extends BinnerPanel {
	private static final long serialVersionUID = -4040915910837867838L;

	private AnalysisDialog parentPanel;
	
	private TitledBorder fileFormatWrapBorder;
	private JPanel fileFormatPanel;
	private JPanel fileFormatWrapPanel;

	private ButtonGroup fileFormatGroup;
	private static JRadioButton newFormatButton;
	private static JRadioButton oldFormatButton;

	public FileFormatPanel(AnalysisDialog parent) {
		super();
		this.parentPanel = parent;
	}
	
	public void setupPanel() {
		fileFormatPanel = new JPanel();
		fileFormatPanel.setLayout(new BoxLayout(fileFormatPanel, BoxLayout.X_AXIS));
		fileFormatGroup = new ButtonGroup();
		newFormatButton = new JRadioButton("Single All-Inclusive File   ");
		newFormatButton.setSelected(true);
		newFormatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				parentPanel.updateVisibilityForFileFormat(false);
			}
		});
		oldFormatButton = new JRadioButton("Separate Experiment and Mass/RT Lookup Files   ");	
		oldFormatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				parentPanel.updateVisibilityForFileFormat(true);
			}
		});
		fileFormatGroup.add(newFormatButton);	
		fileFormatGroup.add(oldFormatButton);
		fileFormatPanel.add(Box.createHorizontalGlue());
		fileFormatPanel.add(newFormatButton);
		fileFormatPanel.add(Box.createHorizontalGlue());
		fileFormatPanel.add(oldFormatButton);
		fileFormatPanel.add(Box.createHorizontalGlue());
		
		fileFormatWrapPanel = new JPanel();
		fileFormatWrapPanel.setLayout(new BoxLayout(fileFormatWrapPanel, BoxLayout.Y_AXIS));
		fileFormatWrapBorder = BorderFactory.createTitledBorder("Select Configuration");
		fileFormatWrapBorder.setTitleFont(boldFontForTitlePanel(fileFormatWrapBorder, false));
		fileFormatWrapPanel.setBorder(fileFormatWrapBorder);
		fileFormatWrapPanel.add(fileFormatPanel);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(2));
		add(fileFormatWrapPanel);
		add(Box.createVerticalStrut(2));
	}
	
	public static Boolean useNewFormat() {
		return true;
	}
	
	public static Boolean useOldFormat() {
		return false; //oldFormatButton.isSelected();
	}
}

