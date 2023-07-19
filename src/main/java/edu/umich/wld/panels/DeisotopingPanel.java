////////////////////////////////////////////////////
// DeisotopingPanel.java
// Created Dec 10, 2017
////////////////////////////////////////////////////
package edu.umich.wld.panels;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import edu.umich.wld.BinnerConstants;
import edu.umich.wld.LayoutGrid;
import edu.umich.wld.LayoutItem;
import edu.umich.wld.LayoutUtils;
import edu.umich.wld.NumberVerifier;


public class DeisotopingPanel extends BinnerPanel
	{
	private static final long serialVersionUID = -2064653819677955972L;
	private JPanel deisotopingWrapPanel;
	private TitledBorder deisotopingWrapBorder;
	private JPanel deisotopingCheckBoxWrapPanel;
	private TitledBorder deisotopingCheckBoxWrapBorder;
	private JPanel deisotopingParametersWrapPanel;
	private TitledBorder deisotopingParametersWrapBorder;
	private JPanel deisotopingParametersPanel;
	private JPanel deisotopingPanel;
	private JCheckBox deisotopingCheckBox;
	private JCheckBox deisotopeMassDiffsCheckBox;
	private JLabel deisotopingMassTolLabel;
	private JTextField deisotopingMassTolBox;
	private JLabel deisotopingMassTolUnitsLabel;
	private JLabel deisotopingCorrCutoffLabel;
	private JTextField deisotopingCorrCutoffBox;
	private JLabel deisotopingRTDiffLabel;
	private JTextField deisotopingRTDiffBox;
	
	public DeisotopingPanel()
		{
		super();
		initializeStickySettings("stickyCleaningDeisotoping", BinnerConstants.DATA_CLEANING_PROPS_FILE);
		}
	
	public void setupPanel()
		{
		initializeArrays();
		
		deisotopingPanel = new JPanel();
		deisotopingPanel.setLayout(new BoxLayout(deisotopingPanel, BoxLayout.Y_AXIS));

		deisotopingCheckBox = this.makeStickyCheckBox("Deisotope Data  ", "doDeisotope", true, true); 
		deisotopingCheckBox.addActionListener(new ActionListener() 
			{
			public void actionPerformed(ActionEvent ae) 
				{
				updateForDeisotopingPreferences();
				}
			});
		//MERGE 05/08
		deisotopeMassDiffsCheckBox = makeStickyCheckBox("Deisotope Mass Difference Distribution  ", "doDeisotopeMD", true, true); //new JCheckBox("Log-Transform Data (using ln(1+x))  ");
		
		deisotopingMassTolLabel = new JLabel("Mass Tolerance  ");
		//MERGE 05/08
		deisotopingMassTolBox = makeStickyTextField("deisotopingMassTol", BinnerConstants.DEFAULT_DEISOTOPING_MASS_DIFFERENCE_TOL.toString(), true); // defaultValue, addToArray)new JTextField(BinnerConstants.DEFAULT_DEISOTOPING_MASS_DIFFERENCE_TOL.toString());
		deisotopingMassTolBox.setInputVerifier(new NumberVerifier(deisotopingMassTolBox, "Mass tolerance"));
		deisotopingMassTolBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		deisotopingMassTolUnitsLabel = new JLabel(" Da ");
		deisotopingCorrCutoffLabel = new JLabel("Correlation Cutoff  ");
		//MERGE 05/08
		deisotopingCorrCutoffBox = makeStickyTextField("deisotopingCorrCutoff", BinnerConstants.DEFAULT_DEISOTOPING_CORR_CUTOFF.toString(), true); // new JTextField(BinnerConstants.DEFAULT_DEISOTOPING_CORR_CUTOFF.toString());
		deisotopingCorrCutoffBox.setInputVerifier(new NumberVerifier(deisotopingMassTolBox, "Correlation cutoff "));
		deisotopingCorrCutoffBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		deisotopingRTDiffLabel = new JLabel("Maximum Retention Time Difference  ");
		//MERGE 05/08
		deisotopingRTDiffBox = makeStickyTextField("deisotopingRTDiff", BinnerConstants.DEFAULT_DEISOTOPING_RT_DIFF.toString(), true);
		deisotopingRTDiffBox.setInputVerifier(new NumberVerifier(deisotopingMassTolBox, "Maximum retention time difference"));
		deisotopingRTDiffBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		
		deisotopingParametersPanel = new JPanel();
		LayoutGrid panelGrid = new LayoutGrid();
		panelGrid.addRow(Arrays.asList(new LayoutItem(deisotopingMassTolLabel, 0.45), 
				new LayoutItem(deisotopingMassTolBox, 0.45), new LayoutItem(deisotopingMassTolUnitsLabel, 0.10)));
		panelGrid.addRow(Arrays.asList(
				new LayoutItem(deisotopingCorrCutoffLabel, 0.45), new LayoutItem(deisotopingCorrCutoffBox, 0.45)));
		panelGrid.addRow(Arrays.asList(
				new LayoutItem(deisotopingRTDiffLabel, 0.45), new LayoutItem(deisotopingRTDiffBox, 0.45)));
		LayoutUtils.doGridLayout(deisotopingParametersPanel, panelGrid);
		
		deisotopingCheckBoxWrapPanel = new JPanel();
		deisotopingCheckBoxWrapPanel.setLayout(new BoxLayout(deisotopingCheckBoxWrapPanel, BoxLayout.X_AXIS));
		deisotopingCheckBoxWrapBorder = BorderFactory.createTitledBorder("");
		deisotopingCheckBoxWrapPanel.setBorder(deisotopingCheckBoxWrapBorder);
		deisotopingCheckBoxWrapPanel.add(Box.createHorizontalGlue());
		LayoutGrid deisotopPanelGrid = new LayoutGrid();
		deisotopPanelGrid.addRow(Arrays.asList(
				new LayoutItem(deisotopingCheckBox, 0.5), new LayoutItem(deisotopeMassDiffsCheckBox, 0.5)));
		LayoutUtils.doGridLayout(deisotopingCheckBoxWrapPanel, deisotopPanelGrid);
		deisotopingCheckBoxWrapPanel.add(Box.createHorizontalGlue());
		
		deisotopingParametersWrapPanel = new JPanel();
		deisotopingParametersWrapPanel.setLayout(new BoxLayout(deisotopingParametersWrapPanel, BoxLayout.Y_AXIS));
		deisotopingParametersWrapBorder = BorderFactory.createTitledBorder("");
		deisotopingParametersWrapPanel.setBorder(deisotopingParametersWrapBorder);
		deisotopingParametersWrapPanel.add(deisotopingParametersPanel);
		
		deisotopingPanel.add(deisotopingCheckBoxWrapPanel);
		deisotopingPanel.add(Box.createVerticalStrut(4));
		deisotopingPanel.add(deisotopingParametersWrapPanel);
		
		deisotopingWrapPanel = new JPanel();
		deisotopingWrapPanel.setLayout(new BoxLayout(deisotopingWrapPanel, BoxLayout.Y_AXIS));
		deisotopingWrapBorder = BorderFactory.createTitledBorder("Set Deisotoping Options");
		deisotopingWrapBorder.setTitleFont(boldFontForTitlePanel(deisotopingWrapBorder, false));
		deisotopingWrapBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		deisotopingWrapPanel.setBorder(deisotopingWrapBorder);
		deisotopingWrapPanel.add(deisotopingPanel);
				
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(2));
		add(deisotopingWrapPanel);
		add(Box.createVerticalStrut(2));
		
		//MERGE 05/08
		setupTextFieldListeners();  
		updateForDeisotopingPreferences();
		}
	
	public void updateForDeisotopingPreferences()
		{
		deisotopingMassTolLabel.setEnabled(deisotopingCheckBox.isSelected());
	 	deisotopingMassTolBox.setEnabled(deisotopingCheckBox.isSelected());
	 	deisotopingMassTolUnitsLabel.setEnabled(deisotopingCheckBox.isSelected());
	 	deisotopingCorrCutoffLabel.setEnabled(deisotopingCheckBox.isSelected());
	  	deisotopingCorrCutoffBox.setEnabled(deisotopingCheckBox.isSelected());
	  	deisotopingRTDiffLabel.setEnabled(deisotopingCheckBox.isSelected());
	  	deisotopingRTDiffBox.setEnabled(deisotopingCheckBox.isSelected());
	  	deisotopeMassDiffsCheckBox.setEnabled(deisotopingCheckBox.isSelected());
	  	deisotopeMassDiffsCheckBox.setSelected(deisotopingCheckBox.isSelected());
		}
	
	public Boolean doDeisotoping()
		{
		return this.deisotopingCheckBox.isSelected();
		}
	
	public Boolean removeIsotopeFeaturesForDistribution()
		{
		return this.deisotopeMassDiffsCheckBox.isSelected();
		}
	
	public String getIsotopeMassTol()
		{
		return deisotopingMassTolBox.getText();
		}
	
	public String getCorrCutoff()
		{
		return deisotopingCorrCutoffBox.getText();
		}
	
	public String getIsotopeRTDiff()
		{
		return deisotopingRTDiffBox.getText();
		}
	}

