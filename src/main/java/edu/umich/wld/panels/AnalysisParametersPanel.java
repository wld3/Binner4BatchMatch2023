////////////////////////////////////////////////////
// AnalysisParametersPanel.java
// Created Dec 8, 2017
////////////////////////////////////////////////////
package edu.umich.wld.panels;

import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import edu.umich.wld.BinnerConstants;
import edu.umich.wld.LayoutGrid;
import edu.umich.wld.LayoutItem;
import edu.umich.wld.LayoutUtils;
import edu.umich.wld.NumberVerifier;


public class AnalysisParametersPanel extends BinnerPanel
	{
	private static final long serialVersionUID = -2524970021691995391L;

	private LayoutGrid panelGrid;
	private JPanel analysisParametersWrapPanel;
	private TitledBorder analysisParametersWrapBorder;
	private JPanel analysisParametersPanel;
	private JLabel gapLabel;
	private JTextField gapBox;
	private JLabel adductNLMassTolLabel;
	private JTextField adductNLMassTolBox;
	private JLabel adductNLMassTolUnitsLabel;
	
	
	public AnalysisParametersPanel() 
		{
		super();
		}
	
	public void setupPanel()
		{
		analysisParametersPanel = new JPanel();
		
		gapLabel = new JLabel("Gap Between Bins  ");
		gapBox = new JTextField(BinnerConstants.DEFAULT_RT_GAP_FOR_BIN_SEPARATION.toString());
		gapBox.setInputVerifier(new NumberVerifier(gapBox, "Gap between bins"));
		gapBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		adductNLMassTolLabel = new JLabel("Annotation Mass Tolerance  ");
		adductNLMassTolBox = new JTextField(BinnerConstants.DEFAULT_ADDUCT_NL_MASS_DIFFERENCE_TOL.toString());
		adductNLMassTolBox.setInputVerifier(new NumberVerifier(adductNLMassTolBox, "Adduct/NL mass tolerance"));
		adductNLMassTolBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		adductNLMassTolUnitsLabel = new JLabel(" Da ");
 			
		panelGrid = new LayoutGrid();
		LayoutUtils.addBlankLines(analysisParametersPanel, 1);
		panelGrid.addRow(Arrays.asList(new LayoutItem(gapLabel, 0.45), new LayoutItem(gapBox, 0.45)));
		panelGrid.addRow(Arrays.asList(new LayoutItem(adductNLMassTolLabel, 0.45), 
				new LayoutItem(adductNLMassTolBox, 0.45), new LayoutItem(adductNLMassTolUnitsLabel, 0.10)));
		LayoutUtils.doGridLayout(analysisParametersPanel, panelGrid);
		
		analysisParametersWrapPanel = new JPanel();
		analysisParametersWrapPanel.setLayout(new BoxLayout(analysisParametersWrapPanel, BoxLayout.Y_AXIS));
		analysisParametersWrapBorder = BorderFactory.createTitledBorder("Set Parameter Values");
		analysisParametersWrapBorder.setTitleFont(boldFontForTitlePanel(analysisParametersWrapBorder, false));
		analysisParametersWrapPanel.setBorder(analysisParametersWrapBorder);
		analysisParametersWrapPanel.add(analysisParametersPanel);
	
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(2));
		add(analysisParametersWrapPanel);
		add(Box.createVerticalStrut(2));
		}
	
	public String getAdductNLMassTol()
		{
		return adductNLMassTolBox.getText();
		}
	
	public String getGap()
		{
		return gapBox.getText();
		}
	}
