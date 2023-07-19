////////////////////////////////////////////////////
// FilterPanel.java
// Created Dec 10, 2017
////////////////////////////////////////////////////
package edu.umich.wld.panels;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import edu.umich.wld.BinnerConstants;
import edu.umich.wld.NumberWithRangeVerifier;

public class FilterPanel extends BinnerPanel 
	{
	private static final long serialVersionUID = 5430833245317196050L;
	
	private JTextField missingnessPercentBox, outlierSDBox;
	private JCheckBox chkZeroAsMissingValue;
	
	public FilterPanel()  {
		super();
		initializeStickySettings("stickyCleaningFilter", BinnerConstants.DATA_CLEANING_PROPS_FILE);
	}
	
	public void setupPanel() {
		initializeArrays();
		JPanel outlierFilterWrapPanel = setupOutlierFilterPanel();
		JPanel missingnessFilterWrapPanel = setupMissingnessFilterPanel();
		JPanel zeroHandlingWrapPanel = this.setupZeroHandlingPanel();
		
		JPanel filterPanel = new JPanel();
		TitledBorder filterBorder = BorderFactory.createTitledBorder("Set Filtering Options");
		filterBorder.setTitleFont(boldFontForTitlePanel(filterBorder, false));
		filterBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		filterPanel.setBorder(filterBorder);
		filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
		filterPanel.add(Box.createVerticalStrut(2));
		filterPanel.add(outlierFilterWrapPanel);
		filterPanel.add(Box.createVerticalStrut(2));
		filterPanel.add(missingnessFilterWrapPanel);
		filterPanel.add(Box.createVerticalStrut(2));
		filterPanel.add(zeroHandlingWrapPanel);
		filterPanel.add(Box.createVerticalStrut(2));
		
		setupTextFieldListeners(); 
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(filterPanel);
	}
		
	private JPanel setupOutlierFilterPanel() {
		JPanel outlierFilterPanel = new JPanel();
		outlierFilterPanel.setLayout(new BoxLayout(outlierFilterPanel, BoxLayout.X_AXIS));
		JLabel preOutlierLabel = new JLabel("Remove Intensities More Than ");
	
		outlierSDBox = makeStickyTextField("outlierSDBox", BinnerConstants.DEFAULT_ALLOWABLE_OUTLIER_SD.toString(), true);
		outlierSDBox.setInputVerifier(new NumberWithRangeVerifier(outlierSDBox, 0.5, 10.0));
		outlierSDBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		
		JLabel postOutlierLabel = new JLabel("Std. Dev. From Feature Mean ");
		outlierFilterPanel.add(Box.createHorizontalGlue());
		outlierFilterPanel.add(preOutlierLabel);
		outlierFilterPanel.add(Box.createHorizontalStrut(2));
		outlierFilterPanel.add(outlierSDBox);
		outlierFilterPanel.add(Box.createHorizontalStrut(2));
		outlierFilterPanel.add(postOutlierLabel);
		outlierFilterPanel.add(Box.createHorizontalGlue());
		
		JPanel outlierFilterWrapPanel = new JPanel();
		outlierFilterWrapPanel.setLayout(new BoxLayout(outlierFilterWrapPanel, BoxLayout.Y_AXIS));
		TitledBorder outlierFilterWrapBorder = BorderFactory.createTitledBorder("Filter Outliers");
		outlierFilterWrapBorder.setTitleFont(boldFontForTitlePanel(outlierFilterWrapBorder, false));
		outlierFilterWrapPanel.setBorder(outlierFilterWrapBorder);
		outlierFilterWrapPanel.add(outlierFilterPanel);
		
		return outlierFilterWrapPanel; 
	}
	
	private JPanel setupMissingnessFilterPanel() {
		JPanel missingnessFilterPanel = new JPanel();
		missingnessFilterPanel.setLayout(new BoxLayout(missingnessFilterPanel, BoxLayout.X_AXIS));
		JLabel preMissingnessLabel = new JLabel("Remove Features Missing More Than ");
	
		missingnessPercentBox = makeStickyTextField("missingnessPct", BinnerConstants.DEFAULT_ALLOWABLE_MISSINGNESS_PERCENT.toString(), true); 
		missingnessPercentBox.setInputVerifier(new NumberWithRangeVerifier(missingnessPercentBox, 0.0, 100.0));
		missingnessPercentBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		
		JLabel postMissingnessLabel = new JLabel("% Of Intensity Measurements  ");
		missingnessFilterPanel.add(Box.createHorizontalGlue());
		missingnessFilterPanel.add(preMissingnessLabel);
		missingnessFilterPanel.add(Box.createHorizontalStrut(2));
		missingnessFilterPanel.add(missingnessPercentBox);
		missingnessFilterPanel.add(Box.createHorizontalStrut(2));
		missingnessFilterPanel.add(postMissingnessLabel);
		missingnessFilterPanel.add(Box.createHorizontalGlue());
		
		JPanel missingnessFilterWrapPanel = new JPanel();
		missingnessFilterWrapPanel.setLayout(new BoxLayout(missingnessFilterWrapPanel, BoxLayout.Y_AXIS));
		TitledBorder missingnessFilterWrapBorder = BorderFactory.createTitledBorder("Filter Features by Missingness");
		missingnessFilterWrapBorder.setTitleFont(boldFontForTitlePanel(missingnessFilterWrapBorder, false));
		missingnessFilterWrapPanel.setBorder(missingnessFilterWrapBorder);
		missingnessFilterWrapPanel.add(missingnessFilterPanel);
		
		return missingnessFilterWrapPanel;
	}
	
	//MERGE 08/19/18
	private JPanel setupZeroHandlingPanel() {
		
		this.chkZeroAsMissingValue = this.makeStickyCheckBox("Treat 0 Character as Missing Intensity Value", "doZeroes", false, true); 
		
		JPanel zeroHandlingPanel = new JPanel();
		zeroHandlingPanel.add(Box.createHorizontalStrut(2));
		zeroHandlingPanel.add(chkZeroAsMissingValue);
		zeroHandlingPanel.add(Box.createHorizontalStrut(2));
		
		JPanel zeroHandlingWrapPanel = new JPanel();
		zeroHandlingWrapPanel.setLayout(new BoxLayout(zeroHandlingWrapPanel, BoxLayout.Y_AXIS));
		TitledBorder zeroHandlingFilterWrapBorder = BorderFactory.createTitledBorder("Missing Symbols");
		zeroHandlingFilterWrapBorder.setTitleFont(boldFontForTitlePanel(zeroHandlingFilterWrapBorder, false));
		zeroHandlingWrapPanel.setBorder(zeroHandlingFilterWrapBorder);
		zeroHandlingWrapPanel.add(zeroHandlingPanel);
			
		return zeroHandlingWrapPanel;
	}
	
	//MERGE 08/19/18
	public Boolean treatZeroAsMissing() {
		return  chkZeroAsMissingValue.isSelected();
	}

	public String getMissingnessPct() {
		return missingnessPercentBox.getText();
	}
	
	public String getOutlierThreshold() {
		return outlierSDBox.getText();
	}
}


