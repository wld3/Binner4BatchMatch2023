
////////////////////////////////////////////////////
// IonizationModePanel.java
// Created Dec 9, 2017
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
import edu.umich.wld.BinnerConstants;


public abstract class IonizationModePanel extends BinnerPanel {
	
	private static final long serialVersionUID = -385664060785583897L;
	
	private JPanel ionizationModeWrapPanel;
	private TitledBorder ionizationModeWrapBorder;
	private JPanel ionizationModePanel;
	private ButtonGroup ionizationModeGroup;
	private JRadioButton positiveButton;
	private JRadioButton negativeButton;
	
//	private AnalysisDialog parent;
	
	public IonizationModePanel(AnalysisDialog parent) {
		super();
	//	this.parent = parent;
	}
	
	public void setupPanel() {
		ionizationModePanel = new JPanel();
		ionizationModePanel.setLayout(new BoxLayout(ionizationModePanel, BoxLayout.X_AXIS));
		ionizationModeGroup = new ButtonGroup();
		
		positiveButton = new JRadioButton("Positive   ");
		positiveButton.setSelected(true);
		positiveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae)  { 
				updateForChargeStateChange();
				
			}
		});
			
		negativeButton = new JRadioButton("Negative   ");			
		negativeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae)  { 
				updateForChargeStateChange();
			}
		});
		
		ionizationModeGroup.add(positiveButton);	
		ionizationModeGroup.add(negativeButton);
		ionizationModePanel.add(Box.createHorizontalGlue());
		ionizationModePanel.add(positiveButton);
		ionizationModePanel.add(Box.createHorizontalGlue());
		ionizationModePanel.add(negativeButton);
		ionizationModePanel.add(Box.createHorizontalGlue());
	
		ionizationModeWrapPanel = new JPanel();
		ionizationModeWrapPanel.setLayout(new BoxLayout(ionizationModeWrapPanel, BoxLayout.Y_AXIS));
		ionizationModeWrapBorder = BorderFactory.createTitledBorder("Specify Ionization Mode");
		ionizationModeWrapBorder.setTitleFont(boldFontForTitlePanel(ionizationModeWrapBorder, false));
		ionizationModeWrapBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		ionizationModeWrapPanel.setBorder(ionizationModeWrapBorder);
		ionizationModeWrapPanel.add(ionizationModePanel);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(ionizationModeWrapPanel);
	}
	
	public Boolean usePositiveMode() {
		return positiveButton.isSelected();
	}
	
	public void setPositiveMode() {
		positiveButton.setSelected(true);
	}
	
	public void setNegativeMode() {
		negativeButton.setSelected(true);
	}
	
	protected abstract void updateForChargeStateChange();
}


