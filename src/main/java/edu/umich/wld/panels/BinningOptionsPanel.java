package edu.umich.wld.panels;

import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.BorderFactory;
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


public class BinningOptionsPanel extends BinnerPanel
	{
	private static final long serialVersionUID = -1420526640503629312L;

	private JPanel binningGapOptionsWrapPanel;
	private JTextField gapBox;

	public BinningOptionsPanel() 
		{
		super();
		this.initializeStickySettings("binningOptions", BinnerConstants.DATA_ANALYSIS_PROPS_FILE);
		}
	
	public void setupPanel()
		{
		initializeArrays();
		setupGapOptionsPanel();
		setupTextFieldListeners();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(binningGapOptionsWrapPanel);
		}

	
	private void setupGapOptionsPanel()
		{
		JPanel binningGapOptionsPanel = new JPanel();
		
		JLabel gapLabel = new JLabel("     Retention Time Gap Size  ");

		gapBox = makeStickyTextField("rtGap", BinnerConstants.DEFAULT_RT_GAP_FOR_BIN_SEPARATION.toString(), true); 
		gapBox.setInputVerifier(new NumberVerifier(gapBox, "Retention Time Gap Size"));
		gapBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
				
		LayoutGrid panelGrid = new LayoutGrid();
		panelGrid.addRow(Arrays.asList(new LayoutItem(gapLabel, 0.5), new LayoutItem(gapBox, 0.5)));
		LayoutUtils.doGridLayout(binningGapOptionsPanel, panelGrid);
		
		binningGapOptionsWrapPanel = new JPanel();
		binningGapOptionsWrapPanel.setLayout(new BoxLayout(binningGapOptionsWrapPanel, BoxLayout.Y_AXIS));
		TitledBorder binningGapOptionsWrapBorder = BorderFactory.createTitledBorder("Set Binning Gap ");
		binningGapOptionsWrapBorder.setTitleFont(boldFontForTitlePanel(binningGapOptionsWrapBorder, false));
		binningGapOptionsWrapBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		binningGapOptionsWrapPanel.setBorder(binningGapOptionsWrapBorder);
		binningGapOptionsWrapPanel.add(binningGapOptionsPanel);
		}
	
	public String getGap()
		{
		return gapBox.getText();
		}
	}
