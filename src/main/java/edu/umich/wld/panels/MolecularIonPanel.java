////////////////////////////////////////////////////
// MolecularIonPanel.java
// Created Dec 9, 2017
////////////////////////////////////////////////////
package edu.umich.wld.panels;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


public class MolecularIonPanel extends BinnerPanel 
	{
	private static final long serialVersionUID = -3503557342088684323L;

	private JCheckBox reassignMolecularIonBox;

	public MolecularIonPanel()  {
		super();
	}
	
	public void setupPanel()  {
		reassignMolecularIonBox = new JCheckBox("Choose Less Intense Molecular Ion When Most Intense Is Sodiated  ");
		reassignMolecularIonBox.setSelected(false);
		
		JPanel molecularIonPanel = new JPanel();
		molecularIonPanel.setLayout(new BoxLayout(molecularIonPanel, BoxLayout.X_AXIS));
		molecularIonPanel.add(Box.createHorizontalGlue());
		molecularIonPanel.add(reassignMolecularIonBox);
		molecularIonPanel.add(Box.createHorizontalGlue());
		
		JPanel molecularIonWrapPanel = new JPanel();
		molecularIonWrapPanel.setLayout(new BoxLayout(molecularIonWrapPanel, BoxLayout.Y_AXIS));
		TitledBorder molecularIonWrapBorder = BorderFactory.createTitledBorder("Specify Molecular Ion Choice");
		molecularIonWrapBorder.setTitleFont(boldFontForTitlePanel(molecularIonWrapBorder, false));
		molecularIonWrapPanel.setBorder(molecularIonWrapBorder);
		molecularIonWrapPanel.add(molecularIonPanel);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(molecularIonWrapPanel);
	}
	
	public Boolean doReassignMolecularIon() {
		return reassignMolecularIonBox.isSelected();
	}
}

