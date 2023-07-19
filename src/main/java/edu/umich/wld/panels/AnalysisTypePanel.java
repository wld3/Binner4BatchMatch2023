////////////////////////////////////////////////////
// AnalysisTypePanel.java
// Created Dec 9, 2017
////////////////////////////////////////////////////
package edu.umich.wld.panels;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import edu.umich.wld.BinnerConstants;


public class AnalysisTypePanel extends BinnerPanel
	{
	private static final long serialVersionUID = 204134160312115840L;
	private JPanel analysisTypeWrapPanel;
	private TitledBorder analysisTypeWrapBorder;
	private JPanel analysisTypePanel;
	
	private JRadioButton pearsonButton;
	private JRadioButton spearmanButton;
	private ButtonGroup analysisTypeGroup;
	
	public AnalysisTypePanel() 
		{
		super();
		this.initializeStickySettings("analysisTypeOptions", BinnerConstants.DATA_ANALYSIS_PROPS_FILE);
		}
		
	public void setupPanel()
		{
		initializeArrays();
		
		analysisTypePanel = new JPanel();
		analysisTypePanel.setLayout(new BoxLayout(analysisTypePanel, BoxLayout.X_AXIS));
		analysisTypeGroup = new ButtonGroup();
		
		pearsonButton = makeStickyRadioButton("Pearson's Correlation  ", "doPearson", true, true); 
		spearmanButton = makeStickyRadioButton("Spearman's Rank Correlation   ", "doSpearman", false, true);			

		analysisTypeGroup.add(pearsonButton);	
		analysisTypeGroup.add(spearmanButton);
		
		analysisTypePanel.add(Box.createHorizontalGlue());
		analysisTypePanel.add(pearsonButton);
		analysisTypePanel.add(Box.createHorizontalGlue());
		analysisTypePanel.add(spearmanButton);
		analysisTypePanel.add(Box.createHorizontalGlue());
		
		analysisTypeWrapPanel = new JPanel();
		analysisTypeWrapPanel.setLayout(new BoxLayout(analysisTypeWrapPanel, BoxLayout.Y_AXIS));
		analysisTypeWrapBorder = BorderFactory.createTitledBorder("Select Correlation Measure  ");
		analysisTypeWrapBorder.setTitleFont(boldFontForTitlePanel(analysisTypeWrapBorder, false));
		analysisTypeWrapBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		analysisTypeWrapPanel.setBorder(analysisTypeWrapBorder);
		analysisTypeWrapPanel.add(analysisTypePanel);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(analysisTypeWrapPanel);
		}
	
	public Boolean pearsonIsSelected()
		{
		return pearsonButton.isSelected();
		}
	
	public String getCorrelationType()
		{
		return pearsonIsSelected() ? "Pearson" : "Spearman";
		}
	}


