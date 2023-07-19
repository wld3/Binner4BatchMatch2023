////////////////////////////////////////////////////
// ClusteringOptionsPanel.java
// Created December 10, 2017
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


public class ClusteringOptionsPanel extends BinnerPanel
	{
	private static final long serialVersionUID = -302347700807652106L;

	private JPanel clusteringOptionsWrapPanel;
	private TitledBorder clusteringOptionsWrapBorder;
	private JPanel clusteringOptionsPanel;

	private JPanel stopConditionPanel;
	private JPanel stopConditionWrapPanel;
	private TitledBorder stopConditionWrapBorder;
	
	private ButtonGroup stopConditionGroup;
	private JRadioButton sqrtStopButton;
	private JRadioButton silhouetteStopButton, weightedSilhouetteStopButton;
			
	public ClusteringOptionsPanel()
		{
		super();
		this.initializeStickySettings("clusteringStopOptions", BinnerConstants.DATA_ANALYSIS_PROPS_FILE);
		}

	public void setupPanel() {
		initializeArrays();
		setupStopConditionPanel();
			
		clusteringOptionsPanel = new JPanel();
		clusteringOptionsPanel.setLayout(new BoxLayout(clusteringOptionsPanel, BoxLayout.Y_AXIS));
		clusteringOptionsPanel.add(stopConditionWrapPanel);
		
		clusteringOptionsWrapPanel = new JPanel();
		clusteringOptionsWrapPanel.setLayout(new BoxLayout(clusteringOptionsWrapPanel, BoxLayout.Y_AXIS));
		clusteringOptionsWrapBorder = BorderFactory.createTitledBorder("Set Clustering Options");
		clusteringOptionsWrapBorder.setTitleFont(boldFontForTitlePanel(clusteringOptionsWrapBorder, false));
		clusteringOptionsWrapBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		clusteringOptionsWrapPanel.setBorder(clusteringOptionsWrapBorder);
		clusteringOptionsWrapPanel.add(clusteringOptionsPanel);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(2));
		add(clusteringOptionsWrapPanel);
		add(Box.createVerticalStrut(2));
		}
	
	private void setupStopConditionPanel()
		{
		sqrtStopButton = makeStickyRadioButton("Sqrt(Bin Size)   ", "sqRtStopForCorrs", false, true); 
		silhouetteStopButton = makeStickyRadioButton("Unweighted Silhouette  ", "unWeightedSilhouetteForCorrs", false, true); 
		weightedSilhouetteStopButton = makeStickyRadioButton("Weighted Silhouette  ", "weightedSilhouetteForCorrs", true, true); 

		sqrtStopButton.setSelected(false);
		silhouetteStopButton.setSelected(false);
		weightedSilhouetteStopButton.setSelected(true);
		
		stopConditionGroup = new ButtonGroup();
		stopConditionGroup.add(weightedSilhouetteStopButton);	
		stopConditionGroup.add(silhouetteStopButton);	
		stopConditionGroup.add(sqrtStopButton);

		stopConditionPanel = new JPanel();
		stopConditionPanel.setLayout(new BoxLayout(stopConditionPanel, BoxLayout.X_AXIS));
		stopConditionPanel.add(Box.createHorizontalGlue());
		stopConditionPanel.add(Box.createHorizontalGlue());
		stopConditionPanel.add(weightedSilhouetteStopButton);
		stopConditionPanel.add(Box.createHorizontalGlue());
		stopConditionPanel.add(silhouetteStopButton);
		stopConditionPanel.add(Box.createHorizontalGlue());
		stopConditionPanel.add(sqrtStopButton);
		stopConditionPanel.add(Box.createHorizontalGlue());
		
		stopConditionWrapPanel = new JPanel();
		stopConditionWrapPanel.setLayout(new BoxLayout(stopConditionWrapPanel, BoxLayout.Y_AXIS));
		stopConditionWrapBorder = BorderFactory.createTitledBorder("Determine Clusters By ");
		stopConditionWrapBorder.setTitleFont(boldFontForTitlePanel(stopConditionWrapBorder, false));
		stopConditionWrapPanel.setBorder(stopConditionWrapBorder);
		stopConditionWrapPanel.add(stopConditionPanel);
		}
	
	public String getClusteringMethod()
		{
		if (weightedSilhouetteStopButton.isSelected())
			return "Weighted Silhouette Clustering  ";
	
		if (silhouetteStopButton.isSelected())
			return "Unweighted Silhouette Clustering  ";
	
		return "Sqrt(Bin Size) Clustering  ";
		}
	
	public Boolean doUnweightedSilhouetteClustering()
		{
		return this.silhouetteStopButton.isSelected();
		}
	
	public Boolean doSilhouetteClustering()
		{
		return this.silhouetteStopButton.isSelected() || weightedSilhouetteStopButton.isSelected();
		}
	}
