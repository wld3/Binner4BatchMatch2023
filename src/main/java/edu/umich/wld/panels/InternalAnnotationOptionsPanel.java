///////////////////////////////////////////////////
// DeisotopingPanel.java
// Created Dec 10, 2017
////////////////////////////////////////////////////
package edu.umich.wld.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class InternalAnnotationOptionsPanel extends BinnerPanel {
	private static final long serialVersionUID = -2064653819677955972L;
	private JPanel internalAnnotationOptionsWrapPanel;
	private TitledBorder internalAnnotationOptionsWrapBorder;
	private JPanel internalAnnotationOptionsCheckBoxPanel;
	private JCheckBox allowMultimerBaseIonsCheckBox;
	private JCheckBox allowComplexAnnotationsWithoutSimpleOnesCheckBox;
	private JCheckBox useNeutralMassesToGetBestChargeCarrierCheckBox;
	private JCheckBox allowVariableChargeWithoutIsotopeInfoCheckBox;
	
	public InternalAnnotationOptionsPanel() {
		super();
	}
	
	public void setupPanel() {
		allowMultimerBaseIonsCheckBox = new JCheckBox("Allow Multimer Bases   ");
		allowMultimerBaseIonsCheckBox.setSelected(false);
		allowMultimerBaseIonsCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			}
		});
		
		allowComplexAnnotationsWithoutSimpleOnesCheckBox = 
				new JCheckBox("Require Charge Carrier Alone Before Combining With Neutral Masses   ");
		allowComplexAnnotationsWithoutSimpleOnesCheckBox.setSelected(true);
		allowComplexAnnotationsWithoutSimpleOnesCheckBox.setEnabled(false);
		allowComplexAnnotationsWithoutSimpleOnesCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			}
		});
		
		useNeutralMassesToGetBestChargeCarrierCheckBox = 
				new JCheckBox("Use Neutral Masses To Help Determine Best Charge Carrier   ");
		useNeutralMassesToGetBestChargeCarrierCheckBox.setSelected(true);
	
		useNeutralMassesToGetBestChargeCarrierCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			}
		});
		
		allowVariableChargeWithoutIsotopeInfoCheckBox = 
				new JCheckBox("Allow Variable Charge Without Isotope Information   ");
		allowVariableChargeWithoutIsotopeInfoCheckBox.setSelected(false);
		allowVariableChargeWithoutIsotopeInfoCheckBox.setEnabled(false);
		allowVariableChargeWithoutIsotopeInfoCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			}
		});
		
		internalAnnotationOptionsCheckBoxPanel = new JPanel();
		internalAnnotationOptionsCheckBoxPanel.setLayout(new BoxLayout(internalAnnotationOptionsCheckBoxPanel, BoxLayout.Y_AXIS));
		internalAnnotationOptionsCheckBoxPanel.add(allowMultimerBaseIonsCheckBox);
		internalAnnotationOptionsCheckBoxPanel.add(allowComplexAnnotationsWithoutSimpleOnesCheckBox);
		internalAnnotationOptionsCheckBoxPanel.add(useNeutralMassesToGetBestChargeCarrierCheckBox);
		internalAnnotationOptionsCheckBoxPanel.add(allowVariableChargeWithoutIsotopeInfoCheckBox);
		
		internalAnnotationOptionsWrapPanel = new JPanel();
		internalAnnotationOptionsWrapPanel.setLayout(new BoxLayout(internalAnnotationOptionsWrapPanel, BoxLayout.Y_AXIS));
		internalAnnotationOptionsWrapBorder = BorderFactory.createTitledBorder("Annotation Options For Evaluation");
		internalAnnotationOptionsWrapBorder.setTitleFont(boldFontForTitlePanel(internalAnnotationOptionsWrapBorder, false));
		internalAnnotationOptionsWrapPanel.setBorder(internalAnnotationOptionsWrapBorder);
		internalAnnotationOptionsWrapPanel.add(internalAnnotationOptionsCheckBoxPanel);		
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(2));
		add(internalAnnotationOptionsWrapPanel);
		add(Box.createVerticalStrut(2));
	}
	
	public Boolean multimerBaseIonsAllowed() {
		return this.allowMultimerBaseIonsCheckBox.isSelected();
	}
	
	public Boolean complexAnnotationsAllowedWithoutSimpleOnes() {
		return this.allowComplexAnnotationsWithoutSimpleOnesCheckBox.isSelected();
	}
	
	public Boolean useNeutralMassesToGetBestChargeCarrier() {
		return this.useNeutralMassesToGetBestChargeCarrierCheckBox.isSelected();
	}
	
	public Boolean variableChargesAllowedWithoutIsotopeInfo() {
		return this.allowVariableChargeWithoutIsotopeInfoCheckBox.isSelected();
	}
}

