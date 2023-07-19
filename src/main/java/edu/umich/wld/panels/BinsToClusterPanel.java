////////////////////////////////////////////////////
// BinsToClusterPanel.java
// Created December 11, 2017
////////////////////////////////////////////////////
package edu.umich.wld.panels;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;

//Please choose a numeric value between *
import edu.umich.wld.BinnerConstants;

public class BinsToClusterPanel extends BinnerPanel
	{
	private static final long serialVersionUID = -2432599052184746565L;

	private JPanel clusterRulesWrapPanel;
	private TitledBorder clusterRulesWrapBorder;
	private JPanel clusterRulesChoiceWrapPanel;
	private TitledBorder clusterRulesChoiceWrapBorder;
	private JPanel clusterRulesSliderWrapPanel;
	private TitledBorder clusterRulesSliderWrapBorder;
	
	private JPanel clusterRulesPanel;
	private JPanel clusterRulesSliderPanel; 
	private JPanel clusterRulesChoicePanel;

	private ButtonGroup clusterRulesGroup;
	private JRadioButton minSizeButton;
	private JRadioButton sqrtBinSizeButton;
	private JRadioButton sizeAdjCorrSqOverSqrtRTDiffButton;
	private JLabel sliderTitle;
	private JSlider cutoffSlider;
	
	
	public BinsToClusterPanel()
		{
		super();
		initializeStickySettings("binsToClusterOptions", BinnerConstants.DATA_ANALYSIS_PROPS_FILE);
		}	
	
	public void setupPanel()
		{
		initializeArrays();
		
		clusterRulesPanel = new JPanel();
		clusterRulesPanel.setLayout(new BoxLayout(clusterRulesPanel, BoxLayout.Y_AXIS));
		
		clusterRulesSliderPanel = new JPanel();
		clusterRulesSliderPanel.setLayout(new BoxLayout(clusterRulesSliderPanel, BoxLayout.Y_AXIS));
		sliderTitle = new JLabel("Cutoff Value   ", JLabel.CENTER);
		sliderTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		cutoffSlider = new JSlider(0, 10, 2);
		cutoffSlider.setMajorTickSpacing(1);
		cutoffSlider.setPaintTicks(true);
		cutoffSlider.setPaintLabels(true);
		
		sizeAdjCorrSqOverSqrtRTDiffButton = makeStickyRadioButton("Bins Below Cutoff Score  ", "binsBelowScoreClustered", true, true); 
		sizeAdjCorrSqOverSqrtRTDiffButton.setActionCommand(sizeAdjCorrSqOverSqrtRTDiffButton.getText());
		sizeAdjCorrSqOverSqrtRTDiffButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				sliderTitle.setEnabled(true);
				cutoffSlider.setEnabled(true);
				cutoffSlider.setValue(2);
			}
		});
		
		minSizeButton = makeStickyRadioButton("Bins Above Cutoff Size  ", "binsAboveSizeClustered", false, true); 
		minSizeButton.setActionCommand(minSizeButton.getText());
		minSizeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				sliderTitle.setEnabled(true);
				cutoffSlider.setEnabled(true);
				cutoffSlider.setValue(5);
			}
		});
		
		clusterRulesChoicePanel = new JPanel();
		clusterRulesGroup = new ButtonGroup();
	
		sqrtBinSizeButton = makeStickyRadioButton("All Bins  ", "allBinsClustered", false, true); 
		sqrtBinSizeButton.setActionCommand(sqrtBinSizeButton.getText());
		sqrtBinSizeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				sliderTitle.setEnabled(false);
				cutoffSlider.setEnabled(false);
			}
		});
		
		clusterRulesGroup.add(sizeAdjCorrSqOverSqrtRTDiffButton);
		clusterRulesGroup.add(minSizeButton);
		clusterRulesGroup.add(sqrtBinSizeButton);
		
		clusterRulesChoicePanel.add(sizeAdjCorrSqOverSqrtRTDiffButton);
		clusterRulesChoicePanel.add(Box.createHorizontalStrut(8));
		clusterRulesChoicePanel.add(minSizeButton);
		clusterRulesChoicePanel.add(Box.createHorizontalStrut(8));
		clusterRulesChoicePanel.add(sqrtBinSizeButton);
		
		clusterRulesSliderWrapPanel = new JPanel();
		clusterRulesSliderWrapPanel.setLayout(new BoxLayout(clusterRulesSliderWrapPanel, BoxLayout.Y_AXIS));
		clusterRulesSliderWrapBorder = BorderFactory.createTitledBorder("");
		clusterRulesSliderWrapPanel.setBorder(clusterRulesSliderWrapBorder);
		clusterRulesSliderWrapPanel.add(clusterRulesSliderPanel);
		
		clusterRulesSliderPanel.add(sliderTitle);
		clusterRulesSliderPanel.add(Box.createVerticalStrut(4));
		clusterRulesSliderPanel.add(cutoffSlider);
		
		clusterRulesChoiceWrapPanel = new JPanel();
		clusterRulesChoiceWrapPanel.setLayout(new BoxLayout(clusterRulesChoiceWrapPanel, BoxLayout.Y_AXIS));
		clusterRulesChoiceWrapBorder = BorderFactory.createTitledBorder("");
		clusterRulesChoiceWrapPanel.setBorder(clusterRulesChoiceWrapBorder);
		clusterRulesChoiceWrapPanel.add(clusterRulesChoicePanel);
		
		clusterRulesPanel.add(clusterRulesChoiceWrapPanel);
		clusterRulesPanel.add(Box.createVerticalStrut(4));
		clusterRulesPanel.add(clusterRulesSliderWrapPanel);
	
		clusterRulesWrapPanel = new JPanel();
		clusterRulesWrapPanel.setLayout(new BoxLayout(clusterRulesWrapPanel, BoxLayout.Y_AXIS));
		clusterRulesWrapBorder = BorderFactory.createTitledBorder("Choose Bins to Cluster ");
		clusterRulesWrapBorder.setTitleFont(boldFontForTitlePanel(clusterRulesWrapBorder, false));
		clusterRulesWrapBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		clusterRulesWrapPanel.setBorder(clusterRulesWrapBorder);
		clusterRulesWrapPanel.add(clusterRulesPanel);
	
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(2));
		add(clusterRulesWrapPanel);
		add(Box.createVerticalStrut(2));
		}
	
	public Boolean clusteringConditionsMet(int binsize, double mean, double sqrtRTDiff) 
		{
		if (getSqrtSelected())
			return true;

		if (getMinSizeSelected())
			return (binsize > getCutoffThreshold());
		
		double log2BinSize = Math.log(binsize) / Math.log(2);
		double binScore = mean * mean / (sqrtRTDiff * log2BinSize);
		if (mean < 0.0)  
			binScore = -binScore;

		return binScore < getCutoffThreshold();
		}
	
	public int getCutoffThreshold()
		{
		return cutoffSlider.getValue();
		}
	
	public String getRuleName()
		{
		if (getSqrtSelected())
			return "All bins  ";

		if (getMinSizeSelected())
			return "Only those with size greater than " + getCutoffThreshold() + "   ";

		return "Only those below " + getCutoffThreshold() + " for corr^2 / (sqrt(rtmax - rtmin) * log2(n))  ";
		}
	
	public Boolean getSqrtSelected()
		{
		return sqrtBinSizeButton.isSelected();
		}
	
	public Boolean getMinSizeSelected()
		{
		return minSizeButton.isSelected();
		}

	public Boolean getSizeAdjCorrSqOverSqrtRTDiffSelected()
		{
		return sizeAdjCorrSqOverSqrtRTDiffButton.isSelected();
		}
	}

