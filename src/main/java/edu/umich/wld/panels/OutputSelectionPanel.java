////////////////////////////////////////////////////
// OutputSelectionPanel.java
// Written by Jan Wigginton, March 2018
////////////////////////////////////////////////////
package edu.umich.wld.panels;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import edu.umich.wld.BinnerConstants;
import edu.umich.wld.BinnerStickySettings;
import edu.umich.wld.BinnerUtils;
import edu.umich.wld.LayoutGrid;
import edu.umich.wld.LayoutItem;
import edu.umich.wld.LayoutUtils;


public class OutputSelectionPanel  extends BinnerPanel implements ItemListener {
	
	private static final long serialVersionUID = -7741033264779112657L;
	
	private JCheckBox corrsByClustLocCheckBox, corrsByClustAbsCheckBox;
	private JCheckBox corrsByBinRtSortLocCheckBox, corrsByBinRtSortAbsCheckBox;
	private JCheckBox corrsByBinClustSortLocCheckBox, corrsByBinClustSortAbsCheckBox;
	
	private JCheckBox massDiffByClustCheckBox, massDiffByBinClustSortCheckBox, massDiffByBinRtSortCheckBox, massDiffByMassSortCheckBox;
	private JCheckBox intensitiesUnadjustCheckBox, intensitiesAdjustCheckBox;
	private JCheckBox massDiffDistCheckBox;
	private JCheckBox refMassPutativeCheckBox, nonAnnotatedCheckBox; //refMassProbableCheckBox;
	private List<JCheckBox> checkBoxes;
	
	private JPanel outputSelectionWrapPanel;
	private TitledBorder outputSelectionWrapBorder;
	private JPanel outputSelectionPanel;
	
	//Specify Ionization
	private JPanel corrSelectionWrapPanel, mdSelectionWrapPanel, intensitySelectionWrapPanel, refMassSelectionWrapPanel;
	private TitledBorder corrSelectionWrapBorder, mdSelectionWrapBorder, intensitySelectionWrapBorder, refMassSelectionWrapBorder;
	
	private MassDiffOutputPanel massDiffOutputWrapPanel;
	
	private Map<String, Boolean> stickyTabPrefs;
	
	public OutputSelectionPanel() {
		super();
		stickyTabPrefs = new HashMap<String, Boolean>();
		try {
			BinnerStickySettings stickySettings = ((BinnerStickySettings) BinnerUtils.getBinnerObjectProp(BinnerConstants.OUTPUT_PROPS_FILE, "stickyTabPrefs", BinnerStickySettings.class));
			stickyTabPrefs = stickySettings != null ?  stickySettings.getOutputTabPrefs() : new HashMap<String, Boolean>();
		}
		catch (Exception e) {  }
	}
	
	
	public void setupPanel() {
		massDiffOutputWrapPanel = new MassDiffOutputPanel();
		massDiffOutputWrapPanel.setupPanel();
		
		checkBoxes = new ArrayList<JCheckBox>();
		
		outputSelectionPanel = new JPanel();
		outputSelectionPanel.setLayout(new BoxLayout(outputSelectionPanel, BoxLayout.X_AXIS));
		
		corrSelectionWrapPanel = setupCorrPanel();
		mdSelectionWrapPanel = setupMassDiffPanel();
		intensitySelectionWrapPanel = setupIntensityPanel();		
		refMassSelectionWrapPanel = setupRefMassPanel();
	
		outputSelectionPanel.add(corrSelectionWrapPanel);
		outputSelectionWrapPanel = new JPanel();
		outputSelectionWrapPanel.setLayout(new BoxLayout(outputSelectionWrapPanel, BoxLayout.Y_AXIS));
		outputSelectionWrapBorder = BorderFactory.createTitledBorder("Customize Output Tabs");
		outputSelectionWrapBorder.setTitleFont(boldFontForTitlePanel(outputSelectionWrapBorder, false));
		outputSelectionWrapBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		outputSelectionWrapPanel.setBorder(outputSelectionWrapBorder);
		outputSelectionWrapPanel.add(outputSelectionPanel);
		outputSelectionWrapPanel.add(mdSelectionWrapPanel);
		outputSelectionWrapPanel.add(intensitySelectionWrapPanel);
		outputSelectionWrapPanel.add(refMassSelectionWrapPanel);
		outputSelectionWrapPanel.add(massDiffOutputWrapPanel);

		for (JCheckBox chk : checkBoxes)
			if (stickyTabPrefs.containsKey(chk.getName()))
				chk.setSelected(stickyTabPrefs.get(chk.getName()));

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(outputSelectionWrapPanel);
	}
	//Additional

	private JPanel setupIntensityPanel() {
		intensitiesUnadjustCheckBox = new JCheckBox("Unadjusted");
		intensitiesUnadjustCheckBox.setSelected(true);
		intensitiesUnadjustCheckBox.setName(BinnerConstants.OUTPUT_UNADJ_INTENSITIES.toString());
		intensitiesUnadjustCheckBox.addItemListener(this);
		checkBoxes.add(intensitiesUnadjustCheckBox);
		
		intensitiesAdjustCheckBox = new JCheckBox("Adjusted");
		intensitiesAdjustCheckBox.setSelected(true);
		intensitiesAdjustCheckBox.setName(BinnerConstants.OUTPUT_ADJ_INTENSITIES.toString());
		intensitiesAdjustCheckBox.addItemListener(this);
		checkBoxes.add(intensitiesAdjustCheckBox);
		
		JPanel intensitiesPanel = new JPanel();
		LayoutGrid intensityPanelGrid = new LayoutGrid();
		
		intensityPanelGrid.addRow(Arrays.asList(
			new LayoutItem(intensitiesUnadjustCheckBox, 0.5), new LayoutItem(intensitiesAdjustCheckBox, 0.5)));
		
		LayoutUtils.doGridLayout(intensitiesPanel, intensityPanelGrid);

		intensitySelectionWrapPanel = new JPanel();
		intensitySelectionWrapPanel.setLayout(new BoxLayout(intensitySelectionWrapPanel, BoxLayout.Y_AXIS));
		intensitySelectionWrapBorder = BorderFactory.createTitledBorder("Intensities ");
		intensitySelectionWrapBorder.setTitleFont(boldFontForTitlePanel(intensitySelectionWrapBorder, false));
		intensitySelectionWrapPanel.setBorder(intensitySelectionWrapBorder);
		intensitySelectionWrapPanel.add(intensitiesPanel);

		return intensitySelectionWrapPanel;
	}
	
	
	private JPanel setupMassDiffPanel() {
		massDiffByClustCheckBox = new JCheckBox("By Cluster");
		massDiffByClustCheckBox.setName(BinnerConstants.OUTPUT_MASS_DIFF_BY_CLUST.toString());
		massDiffByClustCheckBox.setSelected(true);
		massDiffByClustCheckBox.addItemListener(this);
		checkBoxes.add(massDiffByClustCheckBox);
		
		massDiffByBinClustSortCheckBox = new JCheckBox("By Bin, Cluster Sort");
		massDiffByBinClustSortCheckBox.setName(BinnerConstants.OUTPUT_MASS_DIFF_BY_BIN_CLUST_SORT.toString());
		massDiffByBinClustSortCheckBox.setSelected(false);
		massDiffByBinClustSortCheckBox.addItemListener(this);
		checkBoxes.add(massDiffByBinClustSortCheckBox);
		
		massDiffByBinRtSortCheckBox = new JCheckBox("By Bin, RT Sort");
		massDiffByBinRtSortCheckBox.setName(BinnerConstants.OUTPUT_MASS_DIFF_BY_BIN_RT_SORT.toString());
		massDiffByBinRtSortCheckBox.setSelected(true);
		massDiffByBinRtSortCheckBox.addItemListener(this);
		checkBoxes.add(massDiffByBinRtSortCheckBox);
		
		massDiffByMassSortCheckBox = new JCheckBox("By Bin, Mass Diff Sort");
		massDiffByMassSortCheckBox.setName(BinnerConstants.OUTPUT_MASS_DIFF_BY_BIN_MASS_SORT.toString());
		massDiffByMassSortCheckBox.setSelected(false);
		massDiffByMassSortCheckBox.addItemListener(this);
		massDiffByMassSortCheckBox.setEnabled(true);
		checkBoxes.add(massDiffByMassSortCheckBox);
		
		massDiffDistCheckBox = new JCheckBox("Mass Difference Distribution");
		massDiffDistCheckBox.setSelected(true);
		massDiffDistCheckBox.setEnabled(false);
		massDiffDistCheckBox.addItemListener(this);

		JPanel massDiffPanel = new JPanel();
		LayoutGrid mdPanelGrid = new LayoutGrid();
		
		mdPanelGrid.addRow(Arrays.asList(
				new LayoutItem(massDiffByClustCheckBox, 0.5), new LayoutItem(massDiffByMassSortCheckBox , 0.5)));
		mdPanelGrid.addRow(Arrays.asList(
				new LayoutItem(massDiffByBinRtSortCheckBox, 0.5), new LayoutItem(massDiffByBinClustSortCheckBox, 0.5)));
				
		LayoutUtils.doGridLayout(massDiffPanel, mdPanelGrid);
	
		mdSelectionWrapPanel = new JPanel();
		mdSelectionWrapPanel.setLayout(new BoxLayout(mdSelectionWrapPanel, BoxLayout.Y_AXIS));
		mdSelectionWrapBorder = BorderFactory.createTitledBorder("Mass Differences ");
		mdSelectionWrapBorder.setTitleFont(boldFontForTitlePanel(mdSelectionWrapBorder, false));
		mdSelectionWrapPanel.setBorder(mdSelectionWrapBorder);
		mdSelectionWrapPanel.add(massDiffPanel);
		
		return mdSelectionWrapPanel;
	}
	
	
	private JPanel setupCorrPanel() {
		corrsByClustLocCheckBox = new JCheckBox("By Cluster, Local Color");
		corrsByClustLocCheckBox.setSelected(true);
		corrsByClustLocCheckBox.setName(BinnerConstants.OUTPUT_CORR_BY_CLUST_LOC.toString());
		corrsByClustLocCheckBox.addItemListener(this);
		checkBoxes.add(corrsByClustLocCheckBox);
		
		corrsByClustAbsCheckBox = new JCheckBox("By Cluster, Absolute Color");
		corrsByClustAbsCheckBox.setSelected(false);
		corrsByClustAbsCheckBox.setName(BinnerConstants.OUTPUT_CORR_BY_CLUST_ABS.toString());
		corrsByClustAbsCheckBox.addItemListener(this);
		checkBoxes.add(corrsByClustAbsCheckBox);
		
		corrsByBinRtSortLocCheckBox =  new JCheckBox("By Bin, RT Sort, Local Color");
		corrsByBinRtSortLocCheckBox.setSelected(false);
		corrsByBinRtSortLocCheckBox.setName(BinnerConstants.OUTPUT_CORR_BY_BIN_RT_SORT_LOC.toString());
		corrsByBinRtSortLocCheckBox.addItemListener(this);
		checkBoxes.add(corrsByBinRtSortLocCheckBox);
		
		corrsByBinRtSortAbsCheckBox = new JCheckBox("By Bin, RT Sort, Absolute Color");
		corrsByBinRtSortAbsCheckBox.setSelected(true);
		corrsByBinRtSortAbsCheckBox.setName(BinnerConstants.OUTPUT_CORR_BY_BIN_RT_SORT_ABS.toString());
		corrsByBinRtSortAbsCheckBox.addItemListener(this);
		checkBoxes.add(corrsByBinRtSortAbsCheckBox);
		
		corrsByBinClustSortLocCheckBox =  new JCheckBox("By Bin, Cluster Sort, Local Color");
		corrsByBinClustSortLocCheckBox.setSelected(true);
		corrsByBinClustSortLocCheckBox.setName(BinnerConstants.OUTPUT_CORR_BY_BIN_CLUST_SORT_LOC.toString());
		corrsByBinClustSortLocCheckBox.addItemListener(this);
		checkBoxes.add(corrsByBinClustSortLocCheckBox);
		
		corrsByBinClustSortAbsCheckBox = new JCheckBox("By Bin, Cluster Sort, Absolute Color");
		corrsByBinClustSortAbsCheckBox.setSelected(false);
		corrsByBinClustSortAbsCheckBox.setName(BinnerConstants.OUTPUT_CORR_BY_BIN_CLUST_SORT_ABS.toString());
		corrsByBinClustSortAbsCheckBox.addItemListener(this);
		checkBoxes.add(corrsByBinClustSortAbsCheckBox);
		
		JCheckBox dummyCheckBox = new JCheckBox("");
		dummyCheckBox.setVisible(false);
		
		JCheckBox dummyCheckBox2 = new JCheckBox("");
		dummyCheckBox2.setVisible(false);

		JPanel corrPanel = new JPanel();
		LayoutGrid corrPanelGrid = new LayoutGrid();
		corrPanelGrid.addRow(Arrays.asList(
				new LayoutItem(corrsByClustLocCheckBox, 0.5), new LayoutItem(corrsByClustAbsCheckBox, 0.5)));

		corrPanelGrid.addRow(Arrays.asList(
				new LayoutItem(corrsByBinRtSortLocCheckBox, 0.5), new LayoutItem(corrsByBinRtSortAbsCheckBox, 0.5)));
		
		corrPanelGrid.addRow(Arrays.asList(
				new LayoutItem(dummyCheckBox, 0.5), new LayoutItem(dummyCheckBox2, 0.5)));
		
		corrPanelGrid.addRow(Arrays.asList(
				new LayoutItem(corrsByBinClustSortLocCheckBox, 0.5), new LayoutItem(corrsByBinClustSortAbsCheckBox, 0.5)));
		
		LayoutUtils.doGridLayout(corrPanel, corrPanelGrid);
		
		corrSelectionWrapPanel = new JPanel();
		corrSelectionWrapPanel.setLayout(new BoxLayout(corrSelectionWrapPanel, BoxLayout.Y_AXIS));
		corrSelectionWrapBorder = BorderFactory.createTitledBorder("Correlations ");
		corrSelectionWrapBorder.setTitleFont(boldFontForTitlePanel(corrSelectionWrapBorder, false));
		corrSelectionWrapPanel.setBorder(corrSelectionWrapBorder);
		corrSelectionWrapPanel.add(corrPanel);
	
		return corrSelectionWrapPanel;
	}
	
	
	private JPanel setupRefMassPanel() {
		refMassPutativeCheckBox = new JCheckBox("Principal Ions");
		refMassPutativeCheckBox.setSelected(true);
		refMassPutativeCheckBox.setName(BinnerConstants.OUTPUT_REF_MASS_PUTATIVE.toString());
		refMassPutativeCheckBox.addItemListener(this);
		checkBoxes.add(refMassPutativeCheckBox);
		
		//refMassProbableCheckBox = new JCheckBox("Possible");
		//refMassProbableCheckBox.setSelected(false);
		//refMassProbableCheckBox.setName(BinnerConstants.OUTPUT_REF_MASS_POSSIBLE.toString());
		//refMassProbableCheckBox.addItemListener(this);
		//checkBoxes.add(refMassProbableCheckBox);
		
		nonAnnotatedCheckBox = new JCheckBox("Unannotated Features");
		nonAnnotatedCheckBox.setSelected(false);
		nonAnnotatedCheckBox.setName(BinnerConstants.OUTPUT_NONANNOTATED.toString());
		nonAnnotatedCheckBox.addItemListener(this);
		checkBoxes.add(nonAnnotatedCheckBox);
		
		JPanel refMassPanel = new JPanel();
		LayoutGrid refMassPanelGrid = new LayoutGrid();
		
		refMassPanelGrid.addRow(Arrays.asList(
			new LayoutItem(refMassPutativeCheckBox, 0.5), new LayoutItem(nonAnnotatedCheckBox, 0.5)));
		
		LayoutUtils.doGridLayout(refMassPanel, refMassPanelGrid);
		
		refMassSelectionWrapPanel = new JPanel();
		refMassSelectionWrapPanel.setLayout(new BoxLayout(refMassSelectionWrapPanel, BoxLayout.Y_AXIS));
		refMassSelectionWrapBorder = BorderFactory.createTitledBorder("Features/Molecular Ions ");
		refMassSelectionWrapBorder.setTitleFont(boldFontForTitlePanel(refMassSelectionWrapBorder, false));
		refMassSelectionWrapPanel.setBorder(refMassSelectionWrapBorder);
		refMassSelectionWrapPanel.add(refMassPanel);
	
		return refMassSelectionWrapPanel;
	}
	
	public void itemStateChanged(ItemEvent e)  {
		Object source = e.getItemSelectable();
		stickyTabPrefs.put(((JCheckBox) source).getName(), ((JCheckBox) source).isSelected());
		BinnerStickySettings settings = new BinnerStickySettings(stickyTabPrefs);
		BinnerUtils.setBinnerObjectProp(BinnerConstants.OUTPUT_PROPS_FILE, "stickyTabPrefs", settings);
	}
	
	public List<Integer> getSelectedTabIds() {
		List<Integer> intList = new ArrayList<Integer>();
		
		for (JCheckBox chk : this.checkBoxes) {
			if (!chk.isSelected())
				continue;
			
			try {  
				Integer selectedId = Integer.parseInt(chk.getName()); 
				intList.add(selectedId);
			}
			catch (Exception e) { System.out.println("Skipping tab with id " + chk.getName());}
		}
		return intList;
	}

	public void setHistogramMax(Double value) {
		massDiffOutputWrapPanel.setHistogramMax(value);
		BinnerUtils.setBinnerProp(BinnerConstants.OUTPUT_PROPS_FILE, BinnerConstants.MASS_DIST_UPPER_LIMIT_KEY, value.toString());
	}
	
	public Double getHistogramMin() {
		return massDiffOutputWrapPanel.getHistogramMin();
	}
	
	public Double getHistogramMax() {
		return massDiffOutputWrapPanel.getHistogramMax();
	}
}

