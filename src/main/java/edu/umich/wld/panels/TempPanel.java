package edu.umich.wld.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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


public class TempPanel extends BinnerPanel
	{
	private static final long serialVersionUID = -2064653819677955972L;
	private JPanel internalRTOptionsWrapPanel;
	private TitledBorder internalRTOptionsWrapBorder;
	private JPanel internalRTOptionsPanel;
	
	private JCheckBox subClusterLargeClustersCheckBox, useRebinCheckBox;
	private JTextField forcedClusterThreshold, minRTGapThreshold, alwaysGapThreshold;
	
	private JPanel rebinWrapPanel;
	private TitledBorder rebinWrapBorder;
	
	
	public TempPanel()
		{
		super();
		initializeStickySettings("stickyInternalTemp", BinnerConstants.INTERNAL_USE_ONLY_PROPS_FILE);
		}
	
	public void setupPanel()
		{
		initializeArrays();
		
		subClusterLargeClustersCheckBox = makeStickyCheckBox("Always sub-cluster by RT when cluster has more than   ", "doLargeSubCluster", true, true); 
		subClusterLargeClustersCheckBox.addActionListener(new ActionListener() 
			{
			public void actionPerformed(ActionEvent ae)  
				{ 
				forcedClusterThreshold.setEnabled(subClusterLargeClustersCheckBox.isSelected());  
				} 
			});
		
		forcedClusterThreshold = makeStickyTextField("forcedSubClusterThreshold", BinnerConstants.DEFAULT_FORCED_SUBCLUSTER_THRESHOLD.toString(), true);
		forcedClusterThreshold.setInputVerifier(new NumberWithRangeVerifier(forcedClusterThreshold, 10.0, 2000.0));
		forcedClusterThreshold.setEnabled(subClusterLargeClustersCheckBox.isSelected());
		
		JLabel preRTMinGapLabel = new JLabel("Do not break on gaps smaller than  ");
		JLabel preRTMaxGapLabel = new JLabel("Always break on gaps larger than  ");
			
		minRTGapThreshold = makeStickyTextField("minRTGapThreshold", BinnerConstants.DEFAULT_MIN_RT_CLUSTER_GAP.toString(), true);
		minRTGapThreshold.setInputVerifier(new NumberWithRangeVerifier(minRTGapThreshold, 0.001, 2.0));
		
		alwaysGapThreshold = makeStickyTextField("maxRTGapThreshold", BinnerConstants.DEFAULT_ALWAYS_RT_CLUSTER_GAP.toString(), true);
		alwaysGapThreshold.setInputVerifier(new NumberWithRangeVerifier(alwaysGapThreshold, 0.001, 2.0));
		
		JLabel postMissingnessLabel = new JLabel("Features  ");
		JPanel missingnessFilterPanel = new JPanel();
		missingnessFilterPanel.add(Box.createHorizontalGlue());
		missingnessFilterPanel.add(Box.createHorizontalStrut(2));
		missingnessFilterPanel.add(subClusterLargeClustersCheckBox);
		missingnessFilterPanel.add(Box.createHorizontalStrut(2));
		missingnessFilterPanel.add(forcedClusterThreshold);
		missingnessFilterPanel.add(Box.createHorizontalGlue());
		missingnessFilterPanel.add(postMissingnessLabel);
	  //Made maps
		
		//JPanel gapParametersPanel = new JPanel();
		//L/ayoutGrid panelGrid = new LayoutGrid();
		//panelGrid.addRow(Arrays.asList(
		//		new LayoutItem(preRTMinGapLabel, 0.30), new LayoutItem(minRTGapThreshold, 0.70)));
		//panelGrid.addRow(Arrays.asList(
		//		new LayoutItem(preRTMaxGapLabel, 0.30), new LayoutItem(alwaysGapThreshold, 0.70)));
		//LayoutUtils.doGridLayout(gapParametersPanel, panelGrid);
	
		internalRTOptionsPanel = new JPanel();
		internalRTOptionsPanel.setLayout(new BoxLayout(internalRTOptionsPanel, BoxLayout.Y_AXIS));
		internalRTOptionsPanel.add(Box.createVerticalStrut(2));
		internalRTOptionsPanel.add(missingnessFilterPanel);
		internalRTOptionsPanel.add(Box.createVerticalStrut(1));
	//	internalRTOptionsPanel.add(gapParametersPanel);
	//	internalRTOptionsPanel.add(Box.createVerticalStrut(1));
		
		internalRTOptionsWrapPanel = new JPanel();
		internalRTOptionsWrapPanel.setLayout(new BoxLayout(internalRTOptionsWrapPanel, BoxLayout.Y_AXIS));
		internalRTOptionsWrapBorder = BorderFactory.createTitledBorder("Choose RT Clustering Options For Evaluation");
		internalRTOptionsWrapBorder.setTitleFont(boldFontForTitlePanel(internalRTOptionsWrapBorder, false));
		internalRTOptionsWrapBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		internalRTOptionsWrapPanel.setBorder(internalRTOptionsWrapBorder);
		internalRTOptionsWrapPanel.add(internalRTOptionsPanel);		
		
		setupTextFieldListeners();  
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(1));
		add(internalRTOptionsWrapPanel);
		add(Box.createVerticalStrut(1));
		}
	
	private void setupReBinPanel()
		{
		useRebinCheckBox = makeStickyCheckBox("> RT Binning Gap  ", "doRTRebin", false, true); 
		useRebinCheckBox.addActionListener(new ActionListener() 
			{
			public void actionPerformed(ActionEvent ae)  
				{ 
		//		silhouetteStopButton.setEnabled(!useRebinCheckBox.isSelected());  
		//		weightedSilhouetteStopButton.setEnabled(!useRebinCheckBox.isSelected());  
				} 
			});
		
		rebinWrapPanel = new JPanel();
		rebinWrapPanel.setLayout(new BoxLayout(rebinWrapPanel, BoxLayout.Y_AXIS));
		rebinWrapBorder = BorderFactory.createTitledBorder("At Gaps in RT");
		rebinWrapBorder.setTitleFont(boldFontForTitlePanel(rebinWrapBorder, false));
		rebinWrapPanel.setBorder(rebinWrapBorder);
		rebinWrapPanel.add(useRebinCheckBox);
		}
		
	public Boolean alwaysRTSubclusterLargeClusters()
		{
		return subClusterLargeClustersCheckBox.isSelected();
		}
	
	public String getForcedClusterThreshold()
		{
		return forcedClusterThreshold.getText();
		}
	
	public String getAlwaysGapThreshold()
		{
		return alwaysGapThreshold.getText();
		}
	
	public String getMinRTGapThreshold()
		{
		return minRTGapThreshold.getText();
		}
	
	public String getForcedClusteringPolicy()
		{
		String pt1 = "";
		if (subClusterLargeClustersCheckBox.isSelected())
			pt1 = ", all clusters larger than " + this.getForcedClusterThreshold() ;
		
		return pt1;
		}
	}
	
	
