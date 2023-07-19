///////////////////////////////////////////////////
// ReBinOptionsPanel.java
// Written by Jan Wigginton, May 2018
////////////////////////////////////////////////////
package edu.umich.wld.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import edu.umich.wld.BinnerConstants;
import edu.umich.wld.LayoutGrid;
import edu.umich.wld.LayoutItem;
import edu.umich.wld.LayoutUtils;
import edu.umich.wld.sheetwriters.BinnerDecimalTextFieldPair3;


public class ReBinOptionsPanel extends BinnerPanel {
	private static final long serialVersionUID = -1420526640503629312L;

	private BinnerDecimalTextFieldPair3 gapLimitsPair;
	
	private JPanel binningOptionsWrapPanel;
	private TitledBorder binningOptionsWrapBorder;
	private JPanel binningOptionsPanel;

	private JTextField gapBox;

	private JPanel rebinWrapPanel;
	private TitledBorder rebinWrapBorder;
	
	private JPanel gapParametersPanel;
	private JPanel groupingMethodPanel, groupingMethodWrapPanel;
	private TitledBorder groupingMethodWrapBorder;

	private JRadioButton rtClusterButton, rtBinButton;
    private JLabel preRTMinGapLabel, preRTAlwaysGapLabel;
	
    
	public ReBinOptionsPanel()  {
		super();
		this.initializeStickySettings("binningOptions", BinnerConstants.DATA_ANALYSIS_PROPS_FILE);
	}
	
	public void setupPanel() {
		initializeArrays();
		setupGroupingMethodPanel();
		setupGapPolicyPanel();
		
		binningOptionsPanel = new JPanel();
		binningOptionsPanel.setLayout(new BoxLayout(binningOptionsPanel, BoxLayout.Y_AXIS));
		
		binningOptionsPanel.add(groupingMethodWrapPanel);
		add(Box.createVerticalStrut(2));
		binningOptionsPanel.add(rebinWrapPanel);
		
		binningOptionsWrapPanel = new JPanel();
		binningOptionsWrapPanel.setLayout(new BoxLayout(binningOptionsWrapPanel, BoxLayout.Y_AXIS));
		binningOptionsWrapBorder = BorderFactory.createTitledBorder("Sub-Divide Clusters");
		binningOptionsWrapBorder.setTitleFont(boldFontForTitlePanel(binningOptionsWrapBorder, false));
		binningOptionsWrapBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		binningOptionsWrapPanel.setBorder(binningOptionsWrapBorder);
		binningOptionsWrapPanel.add(binningOptionsPanel);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(2));
		add(binningOptionsWrapPanel);
		add(Box.createVerticalStrut(2));

		Boolean enable = rtClusterButton.isSelected();
		preRTMinGapLabel.setVisible(enable); 
		preRTAlwaysGapLabel.setText(!enable ? "                           Always break on gaps larger than Retention Time Gap Size" 
				: "  Always break on gaps larger than");
	
		setupTextFieldListeners();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	
	private void setupGroupingMethodPanel() {
		groupingMethodPanel = new JPanel();
		groupingMethodPanel.setLayout(new BoxLayout(groupingMethodPanel, BoxLayout.X_AXIS));
	
		rtClusterButton = makeStickyRadioButton("Cluster on RT  ", "clusterByRt", true, true); 
		rtBinButton = makeStickyRadioButton("Rebin on RT  ", "binByRt", false, true); 
		
		rtClusterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae)  { 
				Boolean enable = rtClusterButton.isSelected();
				
				gapLimitsPair.getLeftField().setVisible(enable); 
				gapLimitsPair.getRightField().setVisible(enable); 
		
				preRTMinGapLabel.setVisible(enable); 
				preRTAlwaysGapLabel.setText(!enable ? "                           Always break on gaps larger than Retention Time Gap Size" 
						: "  Always break on gaps larger than");
				rebinWrapBorder.setTitle(!enable ? "Gap Policy" : "Set Gap Policy");
			} 
		});
		
		rtBinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae)  { 
				Boolean enable = rtBinButton.isSelected();
				gapLimitsPair.getLeftField().setVisible(!enable); 
				gapLimitsPair.getRightField().setVisible(!enable); 
				preRTMinGapLabel.setVisible(!enable); 
				
				preRTAlwaysGapLabel.setText(enable ? "                           Always break on gaps larger than Retention Time Gap Size"
						: "  Always break on gaps larger than");
				rebinWrapBorder.setTitle(enable ? "Gap Policy" : "Set Gap Policy");
			} 
		});
		
		ButtonGroup stopConditionGroup = new ButtonGroup();
		stopConditionGroup.add(rtClusterButton);	
		stopConditionGroup.add(rtBinButton);	
		
		groupingMethodPanel.add(Box.createHorizontalGlue());
		groupingMethodPanel.add(rtClusterButton);
		groupingMethodPanel.add(Box.createHorizontalGlue());
		groupingMethodPanel.add(rtBinButton);
		groupingMethodPanel.add(Box.createHorizontalGlue()); 
		
		groupingMethodWrapPanel = new JPanel();
		groupingMethodWrapPanel.setLayout(new BoxLayout(groupingMethodWrapPanel, BoxLayout.Y_AXIS));
		groupingMethodWrapBorder = BorderFactory.createTitledBorder("Select Grouping Method");
		groupingMethodWrapBorder.setTitleFont(boldFontForTitlePanel(groupingMethodWrapBorder, false));
		groupingMethodWrapPanel.setBorder(groupingMethodWrapBorder);
		groupingMethodWrapPanel.add(groupingMethodPanel);
	}
		
	
	public void setupGapPolicyPanel() {
		preRTMinGapLabel = new JLabel("No gaps smaller than  ");
		preRTAlwaysGapLabel = new JLabel("  Always break on gaps larger than  ");
			
		gapLimitsPair = makeStickyDecimalTextFieldPair("minRTGapThreshold", "alwaysGapThreshold", 
				BinnerConstants.OVERALL_MIN_RT_CLUSTER_GAP, BinnerConstants.DEFAULT_MIN_RT_CLUSTER_GAP, 
				BinnerConstants.DEFAULT_ALWAYS_RT_CLUSTER_GAP, BinnerConstants.OVERALL_MAX_RT_CLUSTER_GAP, true);
        	//gapLimitsPair.setDefaultDigits(3);
		Boolean enable = rtClusterButton.isSelected();
		gapLimitsPair.getLeftField().setVisible(enable);
		gapLimitsPair.getRightField().setVisible(enable);
		
		gapParametersPanel = new JPanel();
		LayoutGrid panelGrid = new LayoutGrid();
		
		panelGrid.addRow(Arrays.asList(
				new LayoutItem(preRTMinGapLabel, 0.25), new LayoutItem(gapLimitsPair.getLeftField(), 0.2), 
				new LayoutItem(preRTAlwaysGapLabel, 0.25), new LayoutItem(gapLimitsPair.getRightField(), 0.2)));
	
		LayoutUtils.doGridLayout(gapParametersPanel, panelGrid); 
		
		rebinWrapPanel = new JPanel();
		rebinWrapPanel.setLayout(new BoxLayout(rebinWrapPanel, BoxLayout.Y_AXIS));
		rebinWrapBorder = BorderFactory.createTitledBorder("Set Gap Policy");
		rebinWrapBorder.setTitleFont(boldFontForTitlePanel(rebinWrapBorder, false));
		rebinWrapPanel.setBorder(rebinWrapBorder);
		rebinWrapPanel.add(gapParametersPanel);
	}
	
	public String getDivisionMethod() {
		if (rebinIsSelected())
			return "Rebinning at RT gaps greater than ";
	
		return "Weighted Silhouette Clustering on RT ";
	}

	public Boolean doWeightedSilhouetteClustering() {
		return !rebinIsSelected(); 
	}
		
	public String getGap() {
		return gapBox.getText();
	}
	
	public Boolean rebinIsSelected() {
		return rtBinButton.isSelected();
	}
	
	public String getGapPolicy() {
		return "No gaps less than " + getMinRTGapThreshold() + ", always split on RT gaps greater than " + getAlwaysGapThreshold();
	}
	
	public String getAlwaysGapThreshold() {
		return gapLimitsPair.getRightField().getText();
	}
	
	public String getMinRTGapThreshold() {
		return gapLimitsPair.getLeftField().getText();
	}
}
