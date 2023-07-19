////////////////////////////////////////////////////
// NormalizationPanel.java
// Created Dec 10, 2017
////////////////////////////////////////////////////
package edu.umich.wld.panels;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import edu.umich.wld.BinnerConstants;


public class NormalizationPanel extends BinnerPanel 
	{
	private static final long serialVersionUID = 8111837040543819993L;

	private JCheckBox transformationCheckBox;
	
	public NormalizationPanel() 
		{
		super();
		initializeStickySettings("stickyCleaningNormalization", BinnerConstants.DATA_CLEANING_PROPS_FILE);
		}
	
	public void setupPanel() 
		{
		initializeArrays();
		
		transformationCheckBox = makeStickyCheckBox("Log-Transform Data (using ln(1+x))  ", "doNormalize", true, true); 
		
		JPanel normalizationPanel = new JPanel();
		TitledBorder normalizationBorder = BorderFactory.createTitledBorder("Set Normalization Options");
		normalizationBorder.setTitleFont(boldFontForTitlePanel(normalizationBorder, false));
		normalizationBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		normalizationPanel.setBorder(normalizationBorder);
		normalizationPanel.setLayout(new BoxLayout(normalizationPanel, BoxLayout.X_AXIS));
		normalizationPanel.add(Box.createHorizontalGlue());
		normalizationPanel.add(transformationCheckBox);
		normalizationPanel.add(Box.createHorizontalGlue());
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(normalizationPanel);
		}
	
	public Boolean doTransformation() 
		{
		return transformationCheckBox.isSelected();
		}
	}

