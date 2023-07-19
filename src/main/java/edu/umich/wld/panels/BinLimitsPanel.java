package edu.umich.wld.panels;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import edu.umich.wld.BinnerConstants;
import edu.umich.wld.IntegerWithRangeVerifier;
import edu.umich.wld.LayoutGrid;
import edu.umich.wld.LayoutItem;
import edu.umich.wld.LayoutUtils;
import edu.umich.wld.NumberVerifier;


public class BinLimitsPanel extends BinnerPanel {
	
	private JCheckBox overrideMaxBinLimitChk, overrideMaxBinOutputChk;
	private JTextField maxBinLimit, maxBinOutputLimit;
	
	private JPanel binLimitsWrapPanel;
	private TitledBorder binLimitsWrapBorder;
	
	public BinLimitsPanel() {
		super();
		initializeStickySettings("binLimitOptions", BinnerConstants.DATA_ANALYSIS_PROPS_FILE);
	 }

	//longestDerivation
	public void setupPanel()  { 
		this.initializeArrays();
		maxBinLimit = makeStickyTextField("maxBinLimit", BinnerConstants.MAX_RECOMMENDED_BINSIZE.toString(), true); 
		maxBinLimit.setInputVerifier(new NumberVerifier(maxBinLimit, "Maximum Bin Size"));
		maxBinLimit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		maxBinLimit.setEnabled(false);
		maxBinLimit.setInputVerifier(new IntegerWithRangeVerifier(maxBinLimit, BinnerConstants.MAX_RECOMMENDED_BINSIZE, 
				10 * BinnerConstants.MAX_RECOMMENDED_BINSIZE));
		
		maxBinOutputLimit = makeStickyTextField("maxBinOutputLimit", BinnerConstants.MAX_BINSIZE_FOR_BIN_OUTPUT.toString(), true); 
		maxBinOutputLimit.setInputVerifier(new NumberVerifier(maxBinOutputLimit, "Maximum Bin Size For Output"));
		maxBinOutputLimit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		maxBinOutputLimit.setEnabled(false);
		maxBinOutputLimit.setInputVerifier(new IntegerWithRangeVerifier(maxBinOutputLimit, BinnerConstants.MAX_BINSIZE_FOR_BIN_OUTPUT, 
				10 * BinnerConstants.MAX_BINSIZE_FOR_BIN_OUTPUT));
		
		overrideMaxBinLimitChk = this.makeStickyCheckBox("Override Bin Size Limit for Analysis ", "overrideMaxBinLimit", false, true); 
		overrideMaxBinLimitChk.addActionListener(new ActionListener()  {
			public void actionPerformed(ActionEvent ae)  {
				maxBinLimit.setEnabled(overrideMaxBinLimitChk.isSelected());
				if (!overrideMaxBinLimitChk.isSelected()) {
					maxBinLimit.setText(BinnerConstants.MAX_RECOMMENDED_BINSIZE.toString());
				}
			}
		});
		maxBinLimit.setEnabled(overrideMaxBinLimitChk.isSelected());
		

		overrideMaxBinOutputChk = this.makeStickyCheckBox("Override Bin Size Limit for Binwise Output ", "overrideMaxBinOutputLimit", false, true); 
		overrideMaxBinOutputChk.addActionListener(new ActionListener() 
			{	
			public void actionPerformed(ActionEvent ae)  {
				maxBinOutputLimit.setEnabled(overrideMaxBinOutputChk.isSelected());
				if (!overrideMaxBinOutputChk.isSelected()) {
					maxBinOutputLimit.setText(BinnerConstants.MAX_BINSIZE_FOR_BIN_OUTPUT.toString());
				}
			}
		});
		maxBinOutputLimit.setEnabled(this.overrideMaxBinOutputChk.isSelected());
		
		JPanel binLimitsPanel = new JPanel();
		LayoutGrid binLimitsPanelGrid = new LayoutGrid();
		
		binLimitsPanelGrid.addRow(Arrays.asList(
			new LayoutItem(overrideMaxBinLimitChk, 0.5), new LayoutItem(maxBinLimit, 0.5)));
		binLimitsPanelGrid.addRow(Arrays.asList(
			new LayoutItem(overrideMaxBinOutputChk, 0.5), new LayoutItem(maxBinOutputLimit, 0.5)));
			
		LayoutUtils.doGridLayout(binLimitsPanel, binLimitsPanelGrid);
		
		binLimitsWrapPanel = new JPanel();
		binLimitsWrapPanel.setLayout(new BoxLayout(binLimitsWrapPanel, BoxLayout.Y_AXIS));
		binLimitsWrapBorder = BorderFactory.createTitledBorder("Override Bin Size Limits (Not Recommended)");
		binLimitsWrapBorder.setTitleFont(boldFontForTitlePanel(binLimitsWrapBorder, false));
		binLimitsWrapBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		binLimitsWrapPanel.setBorder(binLimitsWrapBorder);
		binLimitsWrapPanel.add(binLimitsPanel);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(2));
		add(binLimitsWrapPanel);
		add(Box.createVerticalStrut(2));
	
		this.setupTextFieldListeners();
	}
	
	public Boolean overrideMaxBinLimit() {
		return this.overrideMaxBinLimit();
	}
	
	public Boolean overrideMaxBinOutputLimit() {
		return this.overrideMaxBinOutputLimit();
	}
	
	public Integer getBinSizeLimit() { 
		
		Integer maxBinSize = BinnerConstants.MAX_RECOMMENDED_BINSIZE;
		try {
			maxBinSize = Integer.parseInt(maxBinLimit.getText());
		}
		catch (Exception e) { 
		} 
		
		return maxBinSize;
	}
	
	public Integer getBinSizeOutputLimit() { 
		
		Integer maxBinOutputSize = BinnerConstants.MAX_BINSIZE_FOR_BIN_OUTPUT;
		try {
			maxBinOutputSize = Integer.parseInt(maxBinOutputLimit.getText());
		}
		catch (Exception e) { 
		} 
		
		return maxBinOutputSize;
	}
}
	

