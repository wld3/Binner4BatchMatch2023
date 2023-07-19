package edu.umich.wld;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.descriptive.rank.Median;

import edu.umich.wld.panels.AbstractStickyFileLocationPanel;
import edu.umich.wld.clustering.FeatureClustering;
import edu.umich.wld.clustering.OldFeatureClustering;
import edu.umich.wld.clustering.RTClustering;
import edu.umich.wld.clustering.RTClusteringPostProcessor;
import edu.umich.wld.panels.AnalysisTypePanel;
import edu.umich.wld.panels.AnnotationFilePanel;
import edu.umich.wld.panels.AnnotationParametersPanel;
import edu.umich.wld.panels.AnnotationPreferencesPanel;
import edu.umich.wld.panels.BinLimitsPanel;
import edu.umich.wld.panels.BinningOptionsPanel;
import edu.umich.wld.panels.BinsToClusterPanel;
import edu.umich.wld.panels.CleaningPanel;
import edu.umich.wld.panels.ClusteringOptionsPanel;
import edu.umich.wld.panels.FileFormatPanel;
import edu.umich.wld.panels.IntegerPickerPanel;
import edu.umich.wld.panels.IonizationModePanel;
import edu.umich.wld.panels.MolecularIonPanel;
import edu.umich.wld.panels.OutputSelectionPanel;
import edu.umich.wld.panels.ReBinOptionsPanel;
import edu.umich.wld.panels.TempPanel;
import edu.umich.wld.sheetwriters.BinnerDataUtils;
import edu.umich.wld.sheetwriters.BinnerOutput;
import edu.umich.wld.sheetwriters.FancyBinnerOutputContainer;
import edu.umich.wld.sheetwriters.Palette;
import edu.umich.wld.sheetwriters.PaletteValues;



public class AnalysisDialog extends JDialog  {
	private static final long serialVersionUID = 8821642008453169492L;
 
	private static AnalysisData analysisData = null;
	private Integer [] sampColIndexRange = new Integer[2];
	private Integer nSampsSelected = 0;
	private String firstSampleColLabel = null, lastSampleColLabel = null;
	private List<String> sampExclusions = null;
	private Integer nAddedCols = 0;
	private List<String> addedCols = new ArrayList<String>();
	//private Integer [] addedColIndices = null;	
	private String [] sampsForAnalysis = null;
	private Integer [] sampIndicesForAnalysis = null;
	private Integer nFeatures = 0;
	private Integer nBins = 0;
	private List<TextFile> inputFiles = new ArrayList<TextFile>();
	private static FileData curExpFileData = null;
	private static FileData curAnnotFileData = null;
	
	private Integer nExpFileHeaderCols = 0;
	private String [] expFileHeaderLabels = null;
	private Map<String, Integer> expFileColTable = new HashMap<String, Integer>();
	private List<String> expFileFeatureNames = new ArrayList<String>();
	private List<Integer> missingFeatures = new ArrayList<Integer>();
	
	private List<BinStats> binStats = null;
	private SummaryInfo summaryInfo = null;
	
	private Integer nSampsForAnalysis = 0;
	private Integer annotationLabelNumber = 1;
	private Integer annotationsFoundCount = 0;
	private Integer isotopeLabelNumber = 1;
	private Integer isotopesFoundCount = 0;
	private Boolean noTierOneChg2Carrier = false, noTierOneChg3Carrier = false;
	
	private Map<Integer, AnnotationInfo> annotFileMap = new HashMap<Integer, AnnotationInfo>();
	private Map<Integer, List<AnnotationInfo>> annotationsByCharge = new HashMap<Integer, List<AnnotationInfo>>();
	private Map<Integer, List<AnnotationInfo>> annotationsUpToCharge = new HashMap<Integer, List<AnnotationInfo>>();
	private Map<String, AnnotationInfo> limitedComboAdducts = new HashMap<String, AnnotationInfo>();
		
	private File outputDirectory = null;
	private FancyBinnerOutputContainer fancyContainer = null;
	private Map<Integer, Integer> outputIndicesBySelectedTabMap;
	private Boolean fInitializing = false;
	private String titleWithVersion = null;
	private Boolean skipBinwiseOutput = false;
	
	private CleaningPanel cleaningWrapPanel;
	private OutputSelectionPanel outputSelectionWrapPanel;
	private AnnotationFilePanel annotationFileWrapPanel;
	private AnnotationPreferencesPanel annotationPreferencesWrapPanel;
    private AnnotationParametersPanel annotationParametersWrapPanel;
	private IonizationModePanel ionizationModeWrapPanel;
	private IntegerPickerPanel batchLabelWrapPanel;
	private BinLimitsPanel binLimitsWrapPanel;
	private MolecularIonPanel molecularIonWrapPanel;
	private AnalysisTypePanel analysisTypeWrapPanel;
	private BinningOptionsPanel binningParametersWrapPanel;
	private ReBinOptionsPanel rebinOptionsWrapPanel;
	private ClusteringOptionsPanel clusteringOptionsWrapPanel;
	private BinsToClusterPanel binsToClusterWrapPanel;
	
	private AbstractStickyFileLocationPanel outputDirectoryPanel;
		
	private JPanel outerPanel;
	private JTabbedPane tabbedPane;
	
	private JPanel buttonPanel;
	private JButton prevButton;
	private JButton nextButton;
	
	private TempPanel tempPanel;
	private JPanel inputPanel;
	
	private JPanel binningPanel;
	private JPanel binningProgPanel;
	private static JProgressBar binningProgBar;
	private static JLabel binningProgLabel;
	private static Double progressBarWeightMultiplier;

	private JPanel expFileWrapPanel;
	private JComboBox<FileData> expFileComboBox;
	private JPanel expFileProgPanel;
	private JProgressBar expFileProgBar;
	
	private JComboBox<String> compColComboBox;
	private JLabel massColLabel;
	private JComboBox<String> massColComboBox;
	private JLabel rtColLabel;
	private JComboBox<String> rtColComboBox;
	
	private JComboBox<String> filterColComboBox;
	private JComboBox<String> fileLocComboBox;
	
	private List<JComboBox<String>> sampColComboBoxPair = new ArrayList<JComboBox<String>>();
	private JLabel sampsSelectedLabel;
	private JLabel sampsExcludedLabel;
	
	
	public AnalysisDialog(String title) {
		titleWithVersion = title;
		JDialog dialog = new JDialog();
		dialog.setTitle(title);
		BinnerUtils.ensureBinnerConfigDirectoryExists();
		inputFiles.add(new TextFile());
		inputFiles.add(new TextFile());
		inputFiles.add(new TextFile());
	
		createControls(dialog);
		updateAnnotPrefsFromFile(true, false);
		setInitialVisibilityStates();
		setInitialEnabledStates();
	
		dialog.add(Box.createHorizontalStrut(8), BorderLayout.WEST);
		dialog.add(outerPanel, BorderLayout.CENTER);
		dialog.add(Box.createHorizontalStrut(8), BorderLayout.EAST);
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
			}
		});
		dialog.setModal(true);
		dialog.setSize(1200, 880);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
	
	public boolean updateAnnotPrefsFromFile(Boolean flagLoadErrors, Boolean checkForTierOneOnMode) {
		try {
			annotFileMap = createAnnotFileMap(flagLoadErrors, checkForTierOneOnMode);
			if (annotFileMap == null) {
				if (!flagLoadErrors)
					annotationFileWrapPanel.clearAnnotFileHeadersOnError(new FileData());  //annotFilePanel.
				return false; 
			}
		
		Integer modeSign = ionizationModeWrapPanel.usePositiveMode() ? 1 : -1;
		annotationPreferencesWrapPanel.updateForAnnotations(modeSign, annotFileMap);
		} 
		catch (Exception e) { return false; } 
		
		return true;
	}
	
	private void createControls(final JDialog dialog) {
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		
		outerPanel = new JPanel();
		outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));
				
		JScrollPane scrollPane = new JScrollPane(innerPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		outerPanel.add(scrollPane);
		
		//fileFormatWrapPanel = new FileFormatPanel(this);
		//fileFormatWrapPanel.setupPanel();
		//fileFormatWrapPanel.setVisible(false);
	
		ionizationModeWrapPanel = new IonizationModePanel(this) {

			@Override
			protected void updateForChargeStateChange() {
				updateAnnotPrefsFromFile(true, true);
			}
		};
		ionizationModeWrapPanel.setupPanel();
	
		outputDirectoryPanel = new AbstractStickyFileLocationPanel("Specify Directory for Binnerized Files", "latticefile.directory") {

			@Override
			protected void updateInterfaceForNewSelection() {
		//	    if (rawFileListLoaderPanel != null)
		//	    	rawFileListLoaderPanel.setInitialDirectoryForChooser(outputDirectoryPanel.getOutputDirectoryPath());
				
			  //  if (namedFileListLoaderPanel != null)
				//	namedFileListLoaderPanel.setInitialDirectoryForChooser(outputDirectoryPanel.getOutputDirectoryPath());
			}
		};
		outputDirectoryPanel.setupPanel();

		/*
		 rawFileListLoaderPanel = new FileListLoaderDisplayPanel() {

			@Override
			protected void updateInterfaceForNewData(int chosen, ArrayList<String> fileNames) {
				 if (rawFileListLoaderPanel != null)
					 rawFileListLoaderPanel.setInitialDirectoryForChooser(null);
			}

			@Override
			protected void processNewData() {
				Map<String, String> batchFileNameMap = rawFileListLoaderPanel.getBatchFileMap();
				FileData file = new FileData();
				file.setName(batchFileNameMap.get("1"));
				file.setPath(batchFileNameMap.get("1"));
				//createLatticeSet();
			}			
		};
		rawFileListLoaderPanel.setFileExtension("txt");
		rawFileListLoaderPanel.setShowBatchLabelBtn(false);
		rawFileListLoaderPanel.setupPanel("Select Raw Feature Data Files", 
			"Initialize Settings", null, false, "Update Labels");
*/
		//First Sample
		//Select File
		batchLabelWrapPanel = new IntegerPickerPanel("batchIdxLabel") {
			
			@Override
			protected Boolean updateForNewSelection(Integer newSelection) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		batchLabelWrapPanel.setupPanel("Specify Batch Label", "Batch Label for Output");
	
		binLimitsWrapPanel = new BinLimitsPanel();
		binLimitsWrapPanel.setupPanel();
		
		JPanel expFilePanel = new JPanel();
		expFilePanel.setLayout(new BoxLayout(expFilePanel, BoxLayout.X_AXIS));
		expFilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		expFileComboBox = new JComboBox<FileData>();
		expFileComboBox.setEditable(false);
		expFileComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		for (FileData fileData: Binner.getAppData().getExpFileDataStore()) {
			expFileComboBox.insertItemAt(fileData, 0);
		}
		expFileComboBox.setSelectedItem(Binner.getAppData().getExpFileData());
		expFileComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if ("comboBoxChanged".equals(ae.getActionCommand())) {
					curExpFileData = (FileData) expFileComboBox.getSelectedItem();
					expFileComboBox.setToolTipText(	curExpFileData == null ? "" : curExpFileData.getName());
				}
			}
		});
		
		expFileComboBox.setToolTipText(	curExpFileData == null ? "" : curExpFileData.getName());
		expFileComboBox.setPrototypeDisplayValue(new FileData("AAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAAA"));
		
		JButton expFileButton = new JButton("Import...");
		expFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				File file = BinnerFileUtils.getFile("Select Experimental Data File", BinnerFileUtils.LOAD, "txt",
						"Text Files", null);
				if (file != null) {
					FileData fileData = new FileData();
					fileData.setName(file.getName());
					fileData.setPath(file.getPath());
					Binner.getAppData().getExpFileDataStore().add(fileData);
					
					expFileComboBox.removeAllItems();
					expFileComboBox.insertItemAt(fileData, 0);
					expFileComboBox.setSelectedIndex(0);
					curExpFileData = (FileData) expFileComboBox.getSelectedItem();
					if (file.getName().toLowerCase().trim().contains("neg")) {
						ionizationModeWrapPanel.setNegativeMode();
					} else {
						ionizationModeWrapPanel.setPositiveMode();
					}
					outputDirectory = file.getParentFile();
					if (outputDirectory != null) {
						if(((DefaultComboBoxModel<String>)fileLocComboBox.getModel()).
								getIndexOf(outputDirectory.getAbsolutePath()) == -1) {
							fileLocComboBox.addItem(outputDirectory.getAbsolutePath());
						}
						fileLocComboBox.setSelectedItem(outputDirectory.getAbsolutePath());
					}
					if (openExpFile()) {
						if (screenForDuplicateExpCols()) {
							if (analysisData != null) {
								analysisData.setOutlierMap(null);
							}
							fInitializing = true;
							handleExpFileHeadersForDropdowns();
							
							massColComboBox.setSelectedItem("Monoisotopic M/Z");
							rtColComboBox.setSelectedItem("RT expected");
							compColComboBox.setSelectedItem("Feature name");
							sampColComboBoxPair.get(BinnerConstants.FIRST).setSelectedItem(firstSampleColLabel);
							sampColComboBoxPair.get(BinnerConstants.LAST).setSelectedItem(lastSampleColLabel);
							
							//String firstSampleName = findFirstSampleName()
							fInitializing = false;
						}
					}
				}
			}
		});
	
		expFilePanel.add(Box.createHorizontalStrut(8));
		expFilePanel.add(expFileComboBox);
		expFilePanel.add(Box.createHorizontalStrut(8));
		expFilePanel.add(expFileButton);
		expFilePanel.add(Box.createHorizontalStrut(8));
		
		expFileProgPanel = new JPanel();
		expFileProgPanel.setLayout(new BoxLayout(expFileProgPanel, BoxLayout.X_AXIS));
		expFileProgPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		expFileProgBar = new JProgressBar(0, 500);
		expFileProgBar.setIndeterminate(true);
		expFileProgPanel.add(expFileProgBar);
		
		JPanel expFileLocWrapPanel = new JPanel();
		expFileLocWrapPanel.setLayout(new BoxLayout(expFileLocWrapPanel, BoxLayout.Y_AXIS));
		TitledBorder expFileLocWrapBorder = BorderFactory.createTitledBorder("Feature Data File");
		expFileLocWrapBorder.setTitleFont(boldFontForTitlePanel(expFileLocWrapBorder, false));
		expFileLocWrapPanel.setBorder(expFileLocWrapBorder);
		expFileLocWrapPanel.add(expFilePanel);
		expFileLocWrapPanel.add(expFileProgPanel);
		
		JPanel expFileColMapPanel = new JPanel();
		JLabel compColLabel = new JLabel("Compound        ");
		compColComboBox = new JComboBox<String>();
		compColComboBox.setEditable(false);
		compColComboBox.insertItemAt("(none)", 0);
		compColComboBox.setSelectedIndex(0);
		compColComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		compColComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if ("comboBoxChanged".equals(ae.getActionCommand())) {
				}
			}
		});
		
		//Import Exp
		massColLabel = new JLabel("Mass (m/z)          ");
		massColComboBox = new JComboBox<String>();
		massColComboBox.setEditable(false);
		massColComboBox.insertItemAt("(none)", 0);
		massColComboBox.setSelectedIndex(0);
		massColComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		massColComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if ("comboBoxChanged".equals(ae.getActionCommand())) {
				}
			}
		});
		
		rtColLabel = new JLabel("Retention Time  ");
		rtColComboBox = new JComboBox<String>();
		rtColComboBox.setEditable(false);
		rtColComboBox.insertItemAt("(none)", 0);
		rtColComboBox.setSelectedIndex(0);
		rtColComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		rtColComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if ("comboBoxChanged".equals(ae.getActionCommand())) {
				}
			}
		});
	
		JLabel firstSampColLabel = new JLabel("First Sample      ");
		sampColComboBoxPair.add(new JComboBox<String>());
		sampColComboBoxPair.get(BinnerConstants.FIRST).setEditable(false);
		sampColComboBoxPair.get(BinnerConstants.FIRST).insertItemAt("(none)", 0);
		sampColComboBoxPair.get(BinnerConstants.FIRST).setSelectedIndex(0);
		sampColComboBoxPair.get(BinnerConstants.FIRST).setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		sampColComboBoxPair.get(BinnerConstants.FIRST).addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getStateChange() != ItemEvent.SELECTED ||
						fInitializing ||
						ie.getItem() == null ||
						sampColComboBoxPair.get(BinnerConstants.FIRST).getSelectedItem() == null ||
						sampColComboBoxPair.get(BinnerConstants.LAST).getSelectedItem() == null ||
						"(none)".equals(sampColComboBoxPair.get(BinnerConstants.FIRST).getSelectedItem().toString()) ||
						"(none)".equals(sampColComboBoxPair.get(BinnerConstants.LAST).getSelectedItem().toString())) {
					return;
				}
				handleSampSelectionChange((String) ie.getItem(), BinnerConstants.FIRST);
			}
		});
		
		JLabel lastSampColLabel = new JLabel("Last Sample      ");
		sampColComboBoxPair.add(new JComboBox<String>());
		sampColComboBoxPair.get(BinnerConstants.LAST).setEditable(false);
		sampColComboBoxPair.get(BinnerConstants.LAST).insertItemAt("(none)", 0);
		sampColComboBoxPair.get(BinnerConstants.LAST).setSelectedIndex(0);
		sampColComboBoxPair.get(BinnerConstants.LAST).setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		sampColComboBoxPair.get(BinnerConstants.LAST).addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getStateChange() != ItemEvent.SELECTED ||
						fInitializing ||
						ie.getItem() == null ||
						sampColComboBoxPair.get(BinnerConstants.FIRST).getSelectedItem() == null ||
						sampColComboBoxPair.get(BinnerConstants.LAST).getSelectedItem() == null ||
						"(none)".equals((String) sampColComboBoxPair.get(BinnerConstants.FIRST).
								getSelectedItem().toString()) ||
						"(none)".equals((String) sampColComboBoxPair.get(BinnerConstants.LAST).
								getSelectedItem().toString())) {
					return;
				}
				handleSampSelectionChange((String) ie.getItem(), BinnerConstants.LAST);
			}	
		});
		//sampColComboBoxPair.get(BinnerConstants.FIRST).setEnabled(false);
		//sampColComboBoxPair.get(BinnerConstants.LAST).setEnabled(false);

		LayoutGrid panelGrid = new LayoutGrid();
		panelGrid.addRow(Arrays.asList(
				new LayoutItem(compColLabel, 0.05), new LayoutItem(compColComboBox, 0.95)));
		panelGrid.addRow(Arrays.asList(
				new LayoutItem(massColLabel, 0.05), new LayoutItem(massColComboBox, 0.95)));
		panelGrid.addRow(Arrays.asList(
				new LayoutItem(rtColLabel, 0.05), new LayoutItem(rtColComboBox, 0.95)));
		panelGrid.addRow(Arrays.asList(
				new LayoutItem(firstSampColLabel, 0.05), new LayoutItem(sampColComboBoxPair.get(BinnerConstants.FIRST), 0.95)));
		panelGrid.addRow(Arrays.asList(
				new LayoutItem(lastSampColLabel, 0.05), new LayoutItem(sampColComboBoxPair.get(BinnerConstants.LAST), 0.95)));
		LayoutUtils.doGridLayout(expFileColMapPanel, panelGrid);
		
		sampsSelectedLabel = new JLabel("No samples will be analyzed   ");
		sampsSelectedLabel.setAlignmentX(CENTER_ALIGNMENT);
		Font italicFont = new Font(sampsSelectedLabel.getFont().getName(), Font.ITALIC, 
				sampsSelectedLabel.getFont().getSize());  
		sampsSelectedLabel.setFont(italicFont);
		
		JPanel excludeSampsPanel = new JPanel();
		excludeSampsPanel.setLayout(new BoxLayout(excludeSampsPanel, BoxLayout.Y_AXIS));
		excludeSampsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton excludeSampsButton = new JButton("Exclude Samples...");
		excludeSampsButton.setAlignmentX(CENTER_ALIGNMENT);
		excludeSampsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (!expFileImported()) {
					JOptionPane.showMessageDialog(null, "Please import experiment file before excluding    " +
							BinnerConstants.LINE_SEPARATOR + "any samples.    ");
					return;
				}
				adjustSampExclusions(true);
				if (allInputColsMapped()) {
					adjustOutputAdditions(false);
				}	
			}
		});
		sampsExcludedLabel = new JLabel("No samples excluded from analysis  ");
		sampsExcludedLabel.setAlignmentX(CENTER_ALIGNMENT);
		italicFont = new Font(sampsExcludedLabel.getFont().getName(), Font.ITALIC, 
				sampsExcludedLabel.getFont().getSize());  
		sampsExcludedLabel.setFont(italicFont);
		
		excludeSampsPanel.add(Box.createVerticalStrut(5));
		excludeSampsPanel.add(excludeSampsButton);
		excludeSampsPanel.add(Box.createVerticalStrut(5));
		excludeSampsPanel.add(sampsExcludedLabel);
		excludeSampsPanel.add(Box.createVerticalStrut(5));
		
		//Currently 
		JPanel expFileColMapWrapPanel = new JPanel();
		expFileColMapWrapPanel.setLayout(new BoxLayout(expFileColMapWrapPanel, BoxLayout.Y_AXIS));
		TitledBorder expFileColMapWrapBorder = BorderFactory.createTitledBorder("Column Selection");
		expFileColMapWrapBorder.setTitleFont(boldFontForTitlePanel(expFileColMapWrapBorder, false));
		expFileColMapWrapPanel.setBorder(expFileColMapWrapBorder);
		expFileColMapWrapPanel.add(expFileColMapPanel);
		expFileColMapWrapPanel.add(Box.createVerticalStrut(5));
		expFileColMapWrapPanel.add(sampsSelectedLabel);
		expFileColMapWrapPanel.add(Box.createVerticalStrut(5));
		expFileColMapWrapPanel.add(excludeSampsPanel);
		expFileColMapWrapPanel.add(Box.createVerticalStrut(2));
		
		//Select Raw
		expFileWrapPanel = new JPanel();
		expFileWrapPanel.setLayout(new BoxLayout(expFileWrapPanel, BoxLayout.Y_AXIS));
		TitledBorder expFileWrapBorder = BorderFactory.createTitledBorder("Currently Analyzing");
		expFileWrapBorder.setTitleFont(boldFontForTitlePanel(expFileWrapBorder, false));
		expFileWrapBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		expFileWrapPanel.setBorder(expFileWrapBorder);
		expFileWrapPanel.add(expFileLocWrapPanel);
		expFileWrapPanel.add(Box.createVerticalStrut(5));
		expFileWrapPanel.add(expFileColMapWrapPanel);
		expFileWrapPanel.add(Box.createVerticalStrut(2));
	
	
		inputPanel = new JPanel();
		inputPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		inputPanel.add(Box.createVerticalStrut(2));
	
		
		inputPanel.add(this.outputDirectoryPanel);
		inputPanel.add(Box.createVerticalStrut(2));
	
		//inputPanel.add(this.rawFileListLoaderPanel);
		//inputPanel.add(Box.createVerticalStrut(2));
		
		inputPanel.add(Box.createVerticalStrut(2));
		inputPanel.add(expFileWrapPanel);
		inputPanel.add(Box.createVerticalStrut(2));
		
		inputPanel.add(ionizationModeWrapPanel);
		inputPanel.add(Box.createVerticalStrut(2));
		
		inputPanel.add(batchLabelWrapPanel);
		inputPanel.add(Box.createVerticalStrut(2));
		
		//Currently Ana
		JPanel colFilterPanel = new JPanel();
		colFilterPanel.setLayout(new BoxLayout(colFilterPanel, BoxLayout.X_AXIS));
		JLabel filterColLabel = new JLabel("Filter By            ");
		filterColComboBox = new JComboBox<String>();
		filterColComboBox.setEditable(false);
		filterColComboBox.insertItemAt("(none)", 0);
		filterColComboBox.setSelectedIndex(0);
		filterColComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		filterColComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if ("comboBoxChanged".equals(ae.getActionCommand())) {
				}
			}
		});
		colFilterPanel.add(Box.createHorizontalGlue());
		colFilterPanel.add(filterColLabel);
		colFilterPanel.add(Box.createHorizontalGlue());
		colFilterPanel.add(filterColComboBox);
		colFilterPanel.add(Box.createHorizontalGlue());
		
		JPanel colFilterWrapPanel = new JPanel();
		colFilterWrapPanel.setLayout(new BoxLayout(colFilterWrapPanel, BoxLayout.Y_AXIS));
		TitledBorder colFilterWrapBorder = BorderFactory.createTitledBorder("Filter Features by Value");
		colFilterWrapBorder.setTitleFont(boldFontForTitlePanel(colFilterWrapBorder, false));
		colFilterWrapPanel.setBorder(colFilterWrapBorder);
		colFilterWrapPanel.add(colFilterPanel);
		
		cleaningWrapPanel = new CleaningPanel();
		cleaningWrapPanel.setupPanel();
		
		LayoutUtils.addBlankLines(cleaningWrapPanel, 2);
			
		annotationFileWrapPanel = new AnnotationFilePanel() {

			@Override
			protected boolean openAnnotFileInner(FileData fileData) {
				curAnnotFileData = fileData;
				return openAnnotFile();
			}

			@Override
			protected boolean updateInterfaceFromFileSelection(File file) {
			
				annotationFileWrapPanel.handleAnnotFileHeadersForDropdowns(inputFiles.get(BinnerConstants.ANNOTATION));
				if (updateAnnotPrefsFromFile(true, false)) {
					BinnerUtils.setBinnerProp(BinnerConstants.ANNOTATION_PROPS_FILE, BinnerConstants.ANNOTATION_FILE_KEY, file.getPath());
					outputSelectionWrapPanel.setHistogramMax(Math.max(BinnerConstants.DENSE_MASS_DIFF_THRESHOLD, BinnerConstants.DEFAULT_MAX_MASS_DIFF));
				} 
				
				return false;
			}
		};
		annotationFileWrapPanel.setupPanel();
		
		annotationPreferencesWrapPanel = new AnnotationPreferencesPanel(ionizationModeWrapPanel.usePositiveMode() ? 1 : -1);
		annotationPreferencesWrapPanel.setupPanel();
		
		annotationParametersWrapPanel = new AnnotationParametersPanel();
    	annotationParametersWrapPanel.setupPanel();
    	
		molecularIonWrapPanel = new MolecularIonPanel();
		molecularIonWrapPanel.setupPanel();
		molecularIonWrapPanel.setVisible(false);
		
		outputSelectionWrapPanel = new OutputSelectionPanel();
		outputSelectionWrapPanel.setupPanel();
		
	
		binningPanel = new JPanel();
		binningPanel.setLayout(new BoxLayout(binningPanel, BoxLayout.X_AXIS));
		JButton runBinningButton = new JButton("Run Analysis");
		runBinningButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (!allFilesImported()) {
					JOptionPane.showMessageDialog(null, "Please import data and annotation files before running     " +
							BinnerConstants.LINE_SEPARATOR + "the analysis.    ");
					return;
				}
				adjustSampExclusions(false);
				if (!allInputColsMapped()) {
					JOptionPane.showMessageDialog(null, "Please select all input columns before     " +
							BinnerConstants.LINE_SEPARATOR + "running the analysis.    ");
					return;
				} 
				adjustOutputAdditions(false);
				doBinningInWorkerThread();
			}
		});
		binningPanel.add(Box.createHorizontalGlue());
		binningPanel.add(runBinningButton);
		binningPanel.add(Box.createHorizontalGlue());
		
		binningProgPanel = new JPanel();
		binningProgPanel.setLayout(new BoxLayout(binningProgPanel, BoxLayout.Y_AXIS));
		binningProgPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		
		binningProgBar = new JProgressBar(0, BinnerConstants.PROGRESS_BAR_WIDTH.intValue());
		binningProgLabel = new JLabel();
		binningProgLabel.setAlignmentX(CENTER_ALIGNMENT);
		binningProgPanel.add(binningProgBar);
		binningProgPanel.add(Box.createVerticalStrut(2));
		binningProgPanel.add(binningProgLabel);
		
		JPanel binningWrapPanel = new JPanel();
		TitledBorder binningWrapBorder = BorderFactory.createTitledBorder("Generate Annotated Report");
		binningWrapBorder.setTitleFont(boldFontForTitlePanel(binningWrapBorder, false));
		binningWrapBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		binningWrapPanel.setBorder(binningWrapBorder);
		binningWrapPanel.setLayout(new BoxLayout(binningWrapPanel, BoxLayout.Y_AXIS));
		binningWrapPanel.add(binningPanel);
		binningWrapPanel.add(binningProgPanel);
		
		JPanel annotPanel = new JPanel();
		annotPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		annotPanel.setLayout(new BoxLayout(annotPanel, BoxLayout.Y_AXIS));
		annotPanel.add(Box.createVerticalStrut(2));
		annotPanel.add(annotationParametersWrapPanel);
		annotPanel.add(Box.createVerticalStrut(2));
		annotPanel.add(annotationFileWrapPanel);
		annotPanel.add(Box.createVerticalStrut(2));
		annotPanel.add(annotationPreferencesWrapPanel);
		annotPanel.add(Box.createVerticalStrut(2));
		annotPanel.add(binningWrapPanel);
		
		String mruAnnotFilename = BinnerUtils.getBinnerProp(BinnerConstants.ANNOTATION_PROPS_FILE,
				 BinnerConstants.ANNOTATION_FILE_KEY);

		if (!StringUtils.isEmptyOrNull(mruAnnotFilename)) {
			curAnnotFileData = annotationFileWrapPanel.getFileSelection(); //(FileData) annotFileComboBox.getSelectedItem();
			if (openAnnotFile()) {
				fInitializing = true;
				annotationFileWrapPanel.handleAnnotFileHeadersForDropdowns(inputFiles.get(BinnerConstants.ANNOTATION));
				fInitializing = false;
			}
		}
		
		analysisTypeWrapPanel = new AnalysisTypePanel();
		analysisTypeWrapPanel.setupPanel();
		
		binningParametersWrapPanel = new BinningOptionsPanel();
		binningParametersWrapPanel.setupPanel();
		
		rebinOptionsWrapPanel = new ReBinOptionsPanel();
		rebinOptionsWrapPanel.setupPanel();
		
		clusteringOptionsWrapPanel = new ClusteringOptionsPanel();
		clusteringOptionsWrapPanel.setupPanel();
		
		binsToClusterWrapPanel = new BinsToClusterPanel();
		binsToClusterWrapPanel.setupPanel();
		
		JPanel analysisPanel = new JPanel();
		analysisPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		analysisPanel.setLayout(new BoxLayout(analysisPanel, BoxLayout.Y_AXIS));
		
		analysisPanel.add(Box.createVerticalStrut(1));
		analysisPanel.add(binningParametersWrapPanel);
		analysisPanel.add(Box.createVerticalStrut(1));
		analysisPanel.add(binLimitsWrapPanel);
		analysisPanel.add(Box.createVerticalStrut(1));
		
		analysisPanel.add(analysisTypeWrapPanel);
		analysisPanel.add(Box.createVerticalStrut(1));
		analysisPanel.add(binsToClusterWrapPanel);
		analysisPanel.add(Box.createVerticalStrut(1));
		analysisPanel.add(rebinOptionsWrapPanel);
		
		JPanel fileLayoutPanel = new JPanel();
		fileLayoutPanel.setLayout(new BoxLayout(fileLayoutPanel, BoxLayout.X_AXIS));
		JButton includeColsButton = new JButton("Include Additional Columns...");
		includeColsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (!allExpInputColsMapped()) {
					JOptionPane.showMessageDialog(null, "Please select all input columns before     " +
							BinnerConstants.LINE_SEPARATOR + "adding additional columns to the output.    ");
					return;
				}
				adjustOutputAdditions(true);
			}
		});	
		
		fileLayoutPanel.add(Box.createHorizontalGlue());
		fileLayoutPanel.add(includeColsButton);
		fileLayoutPanel.add(Box.createHorizontalGlue());
		
		JPanel fileLayoutWrapPanel = new JPanel();
		fileLayoutWrapPanel.setLayout(new BoxLayout(fileLayoutWrapPanel, BoxLayout.Y_AXIS));
		TitledBorder fileLayoutWrapBorder = BorderFactory.createTitledBorder("Specify Additional Columns for Output");
		fileLayoutWrapBorder.setTitleFont(boldFontForTitlePanel(fileLayoutWrapBorder, false));
		fileLayoutWrapBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		fileLayoutWrapPanel.setBorder(fileLayoutWrapBorder);
		fileLayoutWrapPanel.add(fileLayoutPanel);
		
		JPanel fileLocPanel = new JPanel();
		fileLocPanel.setLayout(new BoxLayout(fileLocPanel, BoxLayout.X_AXIS));
		fileLocComboBox = new JComboBox<String>();
		fileLocComboBox.setEditable(false);
		fileLocComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		fileLocComboBox.addItem(BinnerConstants.HOME_DIRECTORY);
		outputDirectory = new File(BinnerConstants.HOME_DIRECTORY);
		fileLocComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				outputDirectory = new File(fileLocComboBox.getSelectedItem().toString());
			}
		});
		JButton saveBinningButton = new JButton("Browse...");
		saveBinningButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.showSaveDialog(null);
				outputDirectory = chooser.getSelectedFile();
				if (outputDirectory != null) {
					fileLocComboBox.addItem(outputDirectory.getAbsolutePath());
					fileLocComboBox.setSelectedItem(outputDirectory.getAbsolutePath());
				}
			}
		});
		fileLocPanel.add(Box.createHorizontalStrut(8));
		fileLocPanel.add(fileLocComboBox);
		fileLocPanel.add(Box.createHorizontalStrut(8));
		fileLocPanel.add(saveBinningButton);
		fileLocPanel.add(Box.createHorizontalStrut(8));
		
		JPanel fileLocWrapPanel = new JPanel();
		fileLocWrapPanel.setLayout(new BoxLayout(fileLocWrapPanel, BoxLayout.Y_AXIS));
		TitledBorder fileLocWrapBorder = BorderFactory.createTitledBorder("Select Location");
		fileLocWrapBorder.setTitleFont(boldFontForTitlePanel(fileLocWrapBorder, false));
		fileLocWrapBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		fileLocWrapPanel.setBorder(fileLocWrapBorder);
		fileLocWrapPanel.add(fileLocPanel);	
		
		JPanel outputPanel = new JPanel();
		outputPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
		outputPanel.add(Box.createVerticalStrut(2));
		outputPanel.add(fileLayoutWrapPanel);
		outputPanel.add(Box.createVerticalStrut(2));
		outputPanel.add(fileLocWrapPanel);
		outputPanel.add(Box.createVerticalStrut(2));
		outputPanel.add(outputSelectionWrapPanel);
		outputPanel.add(Box.createVerticalStrut(2));
		
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		prevButton = new JButton("<<  Previous");
		prevButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() - 1);
			}
		});
		
		nextButton = new JButton("Next  >>");
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() + 1);
			}
		});
		
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(prevButton);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(nextButton);
		buttonPanel.add(Box.createHorizontalGlue());
		
		JPanel inputWrapPanel = new JPanel();
		inputWrapPanel.setLayout(new BoxLayout(inputWrapPanel, BoxLayout.Y_AXIS));
		inputWrapPanel.add(inputPanel);
		LayoutUtils.addBlankLines(inputWrapPanel, 7);
		
		JPanel annotWrapPanel = new JPanel();
		annotWrapPanel.setLayout(new BoxLayout(annotWrapPanel, BoxLayout.Y_AXIS));
		annotWrapPanel.add(annotPanel);
		
		JPanel outputWrapPanel = new JPanel();
		outputWrapPanel.setLayout(new BoxLayout(outputWrapPanel, BoxLayout.Y_AXIS));
		outputWrapPanel.add(outputPanel);
		
		JPanel analysisWrapPanel = new JPanel();
		analysisWrapPanel.setLayout(new BoxLayout(analysisWrapPanel, BoxLayout.Y_AXIS));
		analysisWrapPanel.add(analysisPanel);
		
		final JPanel internalUseWrapPanel = new JPanel();
		internalUseWrapPanel.setLayout(new BoxLayout(internalUseWrapPanel, BoxLayout.Y_AXIS));
		
		internalUseWrapPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		internalUseWrapPanel.setLayout(new BoxLayout(internalUseWrapPanel, BoxLayout.Y_AXIS));
		
		tempPanel = new TempPanel();
		tempPanel.setupPanel();
		internalUseWrapPanel.add(tempPanel);
		
		JCheckBox hidePanelCheckBox = new JCheckBox("Hide this panel (need to relaunch to get it back)   ");
		hidePanelCheckBox.addActionListener(new ActionListener() 
			{
			public void actionPerformed(ActionEvent ae)  
				{ 
				tabbedPane.remove(internalUseWrapPanel);
				} 
			});
		internalUseWrapPanel.add(hidePanelCheckBox);
			
		//	internalUseWrapPanel.add(internalAnnotationOptionsWrapPanel)
		//	internalAnnotationOptionsWrapPanel = new InternalAnnotationOptionsPanel();
		//	internalAnnotationOptionsWrapPanel.setupPanel();
		//	internalUseWrapPanel.add(internalAnnotationOptionsWrapPanel);
		
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Input  ", null, inputWrapPanel, "Select Input File and Configuration   ");
		//tabbedPane.addTab("Output Options  ", null, outputWrapPanel, "Select Output Location and Additional Columns   ");
		//tabbedPane.addTab("Data Cleaning  ", null, cleaningWrapPanel, "Filter and Normalize Data   ");
		tabbedPane.addTab("Feature Grouping  ", null, analysisWrapPanel, "Choose Binning and Clustering Settings   ");
		tabbedPane.addTab("Annotation  ", null, annotWrapPanel, "Select Annotation File   ");

		tabbedPane.addChangeListener(new ChangeListener() {
	        public void stateChanged(ChangeEvent ce) {
	            if (tabbedPane.getSelectedIndex() == 0) {
	            	prevButton.setEnabled(false);
	            	nextButton.setEnabled(true);
	            } else if (tabbedPane.getSelectedIndex() == tabbedPane.getTabCount() - 1) {
	            	prevButton.setEnabled(true);
	            	nextButton.setEnabled(false);
	            } else {
	            	prevButton.setEnabled(true);
	            	nextButton.setEnabled(true);
	            }
	        }
	    });
		
		innerPanel.add(Box.createVerticalStrut(5));
		innerPanel.add(tabbedPane);
		innerPanel.add(Box.createVerticalStrut(5));
		
		outerPanel.add(Box.createVerticalStrut(5));
		outerPanel.add(buttonPanel);
		outerPanel.add(Box.createVerticalStrut(5));
	}
	
	private Boolean screenForDuplicateExpCols() {
		int  nExpFileHeaderCols = inputFiles.get(BinnerConstants.EXPERIMENT).getEndColIndex(0) + 1;
		
		String errMsg = "";
		String foundRtLabel = "", foundMassLabel = "",foundCompLabel = "", dupLabel = "";
		Map<String, String>  foundColLabels = new HashMap<String, String>();
	
		for (int i = 0; i < nExpFileHeaderCols; i++) {
			
			if (StringUtils.isEmptyOrNull(inputFiles.get(BinnerConstants.EXPERIMENT).getString(0, i)))
				continue;
			
			String colLabel = inputFiles.get(BinnerConstants.EXPERIMENT).getString(0, i).trim();
				
			if (isMemberOfStringArray(colLabel, BinnerConstants.COMPOUND_CHOICES)) {
				if (StringUtils.isEmptyOrNull(foundCompLabel))
					foundCompLabel = colLabel;
				else  {
					errMsg = "Error :  Your input file has two columns that might contain feature names (" + foundCompLabel + " and " + colLabel + ")." 
							+ BinnerConstants.LINE_SEPARATOR + BinnerConstants.LINE_SEPARATOR
							+  "Any of the following names will be interpreted as a label for the feature name column :" 
							+ BinnerConstants.LINE_SEPARATOR + "      " + Arrays.asList(BinnerConstants.COMPOUND_CHOICES)
							+ BinnerConstants.LINE_SEPARATOR + BinnerConstants.LINE_SEPARATOR 
							+  "Please be sure that only one column in your data set is labelled with one of these values.";
					break;
				}
			}
			massColComboBox.addItem(colLabel);
			if (isMemberOfStringArray(colLabel, BinnerConstants.MASS_CHOICES)) {
				
				if (StringUtils.isEmptyOrNull(foundMassLabel))
					foundMassLabel = colLabel;
				else  {
					errMsg = "Error :  Your input file has two columns that might contain mass values (" + foundMassLabel + " and " + colLabel + ")." 
							+ BinnerConstants.LINE_SEPARATOR + BinnerConstants.LINE_SEPARATOR
							+  "Any of the following names will be interpreted as a label for the mass column :" 
							+ BinnerConstants.LINE_SEPARATOR + "      " + Arrays.asList(BinnerConstants.MASS_CHOICES)
							+ BinnerConstants.LINE_SEPARATOR + BinnerConstants.LINE_SEPARATOR 
							+  "Please be sure that only one column in your data set is labelled with one of these values.";
					break;
				}
			}
				
			rtColComboBox.addItem(colLabel);
			if (isMemberOfStringArray(colLabel, BinnerConstants.RETENTION_TIME_CHOICES)) {
				if (StringUtils.isEmptyOrNull(foundRtLabel))
					foundRtLabel = colLabel;
				else  {
					
					errMsg = "Error :  Your input file has two columns that might correspond to retention time (" + foundRtLabel + " and " + colLabel + ")." 
							+ BinnerConstants.LINE_SEPARATOR + BinnerConstants.LINE_SEPARATOR
							+  "Any of the following names will be interpreted as a label for the retention time column : " 
							+ BinnerConstants.LINE_SEPARATOR + "      " + Arrays.asList(BinnerConstants.RETENTION_TIME_CHOICES)
							+ BinnerConstants.LINE_SEPARATOR + BinnerConstants.LINE_SEPARATOR 
							+  "Please be sure that only one column in your data set is labelled with one of these values.";
					break;
				}
			}
			
			//isSampleName
			if (MetabolomicsFileNameParser.isSampleName(colLabel)) {
				if (firstSampleColLabel == null) 
					firstSampleColLabel = colLabel;
				else 
					lastSampleColLabel = colLabel;
			}
			
			if (!foundColLabels.containsKey(colLabel))
				foundColLabels.put(colLabel, "");
			else  {
				errMsg = "Error :  Your input file has a duplicate column header (" + colLabel + ")." 
						+ BinnerConstants.LINE_SEPARATOR
						+  "Please update this header so your input file has a unique label for each column. ";
				break;
			}
		}
	
		if (!StringUtils.isEmptyOrNull(errMsg)) {
			JOptionPane.showMessageDialog(null, errMsg);
			compColComboBox.removeAllItems();
			massColComboBox.removeAllItems();
			rtColComboBox.removeAllItems();
			sampColComboBoxPair.get(BinnerConstants.FIRST).removeAllItems();
			sampColComboBoxPair.get(BinnerConstants.LAST).removeAllItems();
			
			expFileComboBox.removeAllItems();
			expFileComboBox.insertItemAt(new FileData(), 0);
			expFileComboBox.setSelectedIndex(0);
		}
		return StringUtils.isEmptyOrNull(errMsg);
	}

	private boolean openExpFile() {
		try {
			inputFiles.get(BinnerConstants.EXPERIMENT).open(new File(curExpFileData.getPath()), true);
			//inputFiles.get(BinnerConstants.EXPERIMENT).open(new File(curExpFileData.getPath());
		} catch (Exception e) {
			e.printStackTrace();
			printErrorMessage("Invalid file format.  Please remove any unmatched quote characters and any string "
					+ BinnerConstants.LINE_SEPARATOR
					+ "bracketed by a slash/quote ('\\\"') sequence from your input data.)");
			return false;
		}
		return true;
	}	
	
	public Font boldFontForTitlePanel(TitledBorder border, boolean makeEvenLarger) {
		//see http://bugs.sun.com/view_bug.do?bug_id=7022041 - getTitleFont() can return null - tew 8/14/12
		// A special thanks to zq (signed 'thomas') from gdufs.edu.cn and Dr. Zaho at kiz.ac.cn for spotting
		// the bug and assisting with the fix.
		Font font = border.getTitleFont();
		if (font == null) {
			font = UIManager.getDefaults().getFont("TitledBorder.font");
			if (font == null) {
				font = new Font("SansSerif", Font.BOLD, 12);
			} else {
				font = font.deriveFont(Font.BOLD);
			}
		} else {
			font = font.deriveFont(Font.BOLD);			
		}
		Font biggerFont = new Font(font.getName(), font.getStyle(), font.getSize() + (makeEvenLarger ? 3 : 1));
		return biggerFont;
	}

	private void printErrorMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}
	
	private boolean openAnnotFile() {
		try {
			if (curAnnotFileData.getPath() != null)
			    inputFiles.get(BinnerConstants.ANNOTATION).open(new File(curAnnotFileData.getPath()), true);
		} catch (Exception e) {
			e.printStackTrace();
			printErrorMessage("Invalid file format");
			return false;
		}
		
		return true;
	}
	
	private void handleExpFileHeadersForDropdowns() {
		nExpFileHeaderCols = inputFiles.get(BinnerConstants.EXPERIMENT).getEndColIndex(0) + 1;
		expFileHeaderLabels = new String[nExpFileHeaderCols];
		
		compColComboBox.removeAllItems();
		massColComboBox.removeAllItems();
		rtColComboBox.removeAllItems();
		sampColComboBoxPair.get(BinnerConstants.FIRST).removeAllItems();
		sampColComboBoxPair.get(BinnerConstants.LAST).removeAllItems();
		boolean missingHeaders = false;
		for (int i = 0; i < nExpFileHeaderCols; i++) {
			String colLabel = inputFiles.get(BinnerConstants.EXPERIMENT).getString(0, i);
			if (colLabel == null || colLabel.isEmpty()) {
				colLabel = "(column " + (i + 1) + ")";
				missingHeaders = true;
			}
			expFileHeaderLabels[i] = colLabel;
			expFileColTable.put(colLabel, i);
		}
		
		boolean compColMapped = false;
		boolean massColMapped = false;
		boolean rtColMapped = false;
		List<Integer> mappedCols = new ArrayList<Integer>();
		for (int i = 0; i < nExpFileHeaderCols; i++) {
			String colLabel = expFileHeaderLabels[i];
			compColComboBox.addItem(colLabel);
			if (isMemberOfStringArray(colLabel, BinnerConstants.COMPOUND_CHOICES)) {
				compColComboBox.setSelectedItem(colLabel);
				compColMapped = true;
				mappedCols.add(i);
			}
			massColComboBox.addItem(colLabel);
			if (isMemberOfStringArray(colLabel, BinnerConstants.MASS_CHOICES)) {
				massColComboBox.setSelectedItem(colLabel);
				massColMapped = true;
				mappedCols.add(i);
			}
			rtColComboBox.addItem(colLabel);
			if (isMemberOfStringArray(colLabel, BinnerConstants.RETENTION_TIME_CHOICES)) {
				rtColComboBox.setSelectedItem(colLabel);
				rtColMapped = true;
				mappedCols.add(i);
			}
			sampColComboBoxPair.get(BinnerConstants.FIRST).addItem(colLabel);
			sampColComboBoxPair.get(BinnerConstants.LAST).addItem(colLabel);
			filterColComboBox.addItem(colLabel);
		}
		
		//massColComboBox.setSelectedItem("Monoisotopic M/Z");
		//rtColComboBox.setSelectedItem("RT expected");
		//compColComboBox.setSelectedItem("Feature name");
		
		if (missingHeaders) {
			JOptionPane.showMessageDialog(null, "Warning:  One or more columns has a missing header    " +
					BinnerConstants.LINE_SEPARATOR + "and has been given one based on its column position.  ");
		}
		
		if (!compColMapped) {
			int colIndex = firstAvailableCol(mappedCols, nExpFileHeaderCols);
			compColComboBox.setSelectedItem(expFileHeaderLabels[colIndex]);
		}
		if (!massColMapped) {
			int colIndex = firstAvailableCol(mappedCols, nExpFileHeaderCols);
			massColComboBox.setSelectedItem(expFileHeaderLabels[colIndex]);
		}
		if (!rtColMapped) {
			int colIndex = firstAvailableCol(mappedCols, nExpFileHeaderCols);
			rtColComboBox.setSelectedItem(expFileHeaderLabels[colIndex]);
		}
		int colIndex = firstAvailableCol(mappedCols, nExpFileHeaderCols);
		sampColIndexRange[BinnerConstants.FIRST] = colIndex;
		sampColComboBoxPair.get(BinnerConstants.FIRST).setSelectedItem(expFileHeaderLabels[colIndex]);
		colIndex = lastAvailableCol(mappedCols, nExpFileHeaderCols);
		sampColIndexRange[BinnerConstants.LAST] = colIndex;
		sampColComboBoxPair.get(BinnerConstants.LAST).setSelectedItem(expFileHeaderLabels[colIndex]);
		nSampsSelected = sampColIndexRange[BinnerConstants.LAST] - sampColIndexRange[BinnerConstants.FIRST] + 1;
		
		adjustSampExclusions(false);
		if (allInputColsMapped()) {
			adjustOutputAdditions(false);
		}
	}
	
	private boolean isMemberOfStringArray(String choice, String [] choices) {
		for (int i = 0; i < choices.length; i++) {
			if (choice.equalsIgnoreCase(choices[i])) {
				return true;
			}
		}
		return false;
	}
	
	private Integer firstAvailableCol(List<Integer> mappedCols, Integer nCols) {
		for (int i = 0; i < nCols; i++) {
			if (!mappedCols.contains(i)) {
				mappedCols.add(i);
				return i;
			}
		}
		return 0;
	}
	
	private Integer lastAvailableCol(List<Integer> mappedCols, Integer nCols) {
		for (int i = nCols - 1; i >= 0; --i) {
			if (!mappedCols.contains(i)) {
				mappedCols.add(i);
				return i;
			}
		}
		return nCols - 1;
	}
	
	private void handleSampSelectionChange(String selectedItemName, Integer thisIndex) {
		Integer newColIndex = expFileColTable.get(selectedItemName);
		sampColIndexRange[thisIndex] = newColIndex;
		
		if (thisIndex == BinnerConstants.FIRST) {
			adjustDropdownEntries(BinnerConstants.LAST, newColIndex, nExpFileHeaderCols - 1);
			if (sampColIndexRange[BinnerConstants.LAST] == null) {
				return;
			}
		} else {
			adjustDropdownEntries(BinnerConstants.FIRST, 0, newColIndex);
			if (sampColIndexRange[BinnerConstants.FIRST] == null) {
				return;
			}
		}
		//openExpFile
		nSampsSelected = sampColIndexRange[BinnerConstants.LAST] - sampColIndexRange[BinnerConstants.FIRST] + 1;
		adjustSampExclusions(false);
		if (allInputColsMapped()) {
			adjustOutputAdditions(false);
		}
	}
	
	private void adjustDropdownEntries(Integer thisIndex, Integer firstColIndex, Integer lastColIndex) {
		Integer selectedIndex = sampColComboBoxPair.get(thisIndex).getSelectedIndex();
		for (int i = sampColComboBoxPair.get(thisIndex).getItemCount() - 1; i >= 0; --i) {
			if (i == selectedIndex) {
				continue;
			}
			sampColComboBoxPair.get(thisIndex).removeItemAt(i);
		}
					
		String selectedItem = sampColComboBoxPair.get(thisIndex).getSelectedItem().toString();
		Integer selectedColIndex = expFileColTable.get(selectedItem);
		for (int i = selectedColIndex - 1; i >= firstColIndex; --i) {
			sampColComboBoxPair.get(thisIndex).insertItemAt(expFileHeaderLabels[i], 0);
		}
		for (int i = selectedColIndex + 1; i <= lastColIndex; i++) {
			sampColComboBoxPair.get(thisIndex).addItem(expFileHeaderLabels[i]);
		}
	}
	
	private void adjustSampExclusions(boolean fShowDialog) {
		String [] sampListForDialog = new String[nSampsSelected];
		for (int i = sampColIndexRange[BinnerConstants.FIRST]; i <= sampColIndexRange[BinnerConstants.LAST]; i++) {
			sampListForDialog[i - sampColIndexRange[BinnerConstants.FIRST]] = expFileHeaderLabels[i];
		}
		ChecklistParameters params = new ChecklistParameters();
		params.setInputList(sampListForDialog);
		params.setDialogTitle("Exclude Samples");
		params.setPanelTitle("Select Samples to Exclude");
		params.setPropsFile("binner.recent.exclusions");
		params.setPropsValue("excluded");
		PopupChecklist checklist = new PopupChecklist(null, params, fShowDialog);
		sampExclusions = checklist.getSelections();
		Integer nSampsExcluded = sampExclusions.size();
		nSampsForAnalysis = nSampsSelected - nSampsExcluded;
		sampsSelectedLabel.setText(sampLabelFromNumber(nSampsForAnalysis) + " will be analyzed  ");
		sampsExcludedLabel.setText(sampLabelFromNumber(nSampsExcluded) + " excluded from analysis  ");
	}
	
	private String sampLabelFromNumber(Integer number) {
		String retStr = null;
		switch (number) {
			case 0:
				retStr = "No samples";
				break;
			case 1:
				retStr = "1 sample";
				break;
			default:
				retStr = number + " samples";
				break;
		}
		return retStr;
	}
	
	private void adjustOutputAdditions(boolean fShowDialog) {
		
		int nMappedCols = FileFormatPanel.useNewFormat() ? 3 : 1;
		List<String> mappedCols = new ArrayList<String>(nMappedCols);
		mappedCols.add(compColComboBox.getSelectedItem().toString());
		if (FileFormatPanel.useNewFormat()) {
			mappedCols.add(massColComboBox.getSelectedItem().toString());
			mappedCols.add(rtColComboBox.getSelectedItem().toString());
		}
		
		int nOverlapCols = 0;
		for (int i = sampColIndexRange[BinnerConstants.FIRST]; i <= sampColIndexRange[BinnerConstants.LAST]; i++) {
			if (mappedCols.contains(expFileHeaderLabels[i])) {
				nOverlapCols++;
			}
		}
		
		int nAddableCols = nExpFileHeaderCols - nSampsForAnalysis - nMappedCols + nOverlapCols;
		if (nAddableCols <= 0) {
			nAddedCols = 0;
			if (fShowDialog) {
				JOptionPane.showMessageDialog(null, "All columns will already be included in the output    ");
			}
			return;
		}
		
		String [] colListForDialog = new String[nAddableCols];		
		int k = 0;	
		for (int i = 0; i < sampColIndexRange[BinnerConstants.FIRST]; i++) {
			if (mappedCols.contains(expFileHeaderLabels[i])) {
				continue;
			}
			colListForDialog[k++] = expFileHeaderLabels[i];
		}
		
		for (int i = sampColIndexRange[BinnerConstants.FIRST]; i <= sampColIndexRange[BinnerConstants.LAST]; i++) {
			if (!(sampExclusions.contains(expFileHeaderLabels[i]))) {
				continue;
			}
			colListForDialog[k++] = expFileHeaderLabels[i];
		}
		
		for (int i = sampColIndexRange[BinnerConstants.LAST] + 1; i < nExpFileHeaderCols; i++) {
			if (mappedCols.contains(expFileHeaderLabels[i])) {
				continue;
			}
			colListForDialog[k++] = expFileHeaderLabels[i];
		}
		
		ChecklistParameters params = new ChecklistParameters();
		params.setInputList(colListForDialog);
		params.setDialogTitle("Add Columns");
		params.setPanelTitle("Select Columns to Add to Output");
		params.setPropsFile("binner.recent.additions");
		params.setPropsValue("added");
		PopupChecklist checklist = new PopupChecklist(null, params, fShowDialog);
		addedCols = checklist.getSelections();
		nAddedCols = addedCols.size();
	}
	
	private boolean expFileImported() {
		return !("(none)".equals(expFileComboBox.getSelectedItem().toString()));
	}
	
	private boolean allFilesImported() {
		return !("(none)".equals(expFileComboBox.getSelectedItem().toString()) ||
				"(none)".equals(annotationFileWrapPanel.getFileSelectionName()));
	}
	
	private boolean allExpInputColsMapped() { 
		boolean fAllMapped = !("(none)".equals(sampColComboBoxPair.get(BinnerConstants.FIRST).getSelectedItem().toString()) ||
				"(none)".equals(sampColComboBoxPair.get(BinnerConstants.LAST).getSelectedItem().toString()) ||
				"(none)".equals(compColComboBox.getSelectedItem().toString()) ||
				(("(none)".equals(massColComboBox.getSelectedItem().toString()) ||
				"(none)".equals(rtColComboBox.getSelectedItem().toString()))));
		
		return fAllMapped;
	
	}
	
	private boolean allInputColsMapped() {
		boolean fAllMapped = allExpInputColsMapped();
		
		fAllMapped &= annotationFileWrapPanel.allInputColsMapped();
		
		return fAllMapped;
	}
	
	private void doBinningInWorkerThread() {
		SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
		    @Override
		    public Boolean doInBackground() {
		    	binningProgPanel.setVisible(true);
				binningPanel.setVisible(false);
				binningProgBar.setValue(binningProgBar.getMinimum());
				binningProgLabel.setText("");

				try {
					return doBinning();
				} catch (BinnerException e) {
					JOptionPane.showMessageDialog(null, e.getBinnerMessage());
					return false;
				}
		    	catch (Exception e) {
		    		e.printStackTrace();
		    		JOptionPane.showMessageDialog(null, "Error doing binning  ");
		    		return false;
		    	}
		    	
		    }

		    @Override
		    public void done() {
		        try {
		        	if (get())//{
		        	//	JOptionPane.showMessageDialog(null, "Error doing binning  ");
		        	//} else {
		        		JOptionPane.showMessageDialog(null, "Binning complete.  ");
		        	//}
		        } catch (InterruptedException ignore) {
		        } catch (ExecutionException ee) {
		            String why = null;
		            Throwable cause = ee.getCause();
		            if (cause != null) {
		                why = cause.getMessage();
		            } else {
		                why = ee.getMessage();
		            }
		            System.err.println("Error doing binning: " + why);
		        }
		        binningPanel.setVisible(true);
				binningProgPanel.setVisible(false);
				binningProgLabel.setText("");
		    }
		};
		worker.execute();
	}
	
	private boolean doBinning() throws BinnerException  {
		skipBinwiseOutput = false;
		
		if (cleaningWrapPanel.doDeisotoping()) 
			binningProgLabel.setText("Collecting data and deisotoping ...  ");
		else 
			binningProgLabel.setText("Collecting data ...  ");
		
		binStats = new ArrayList<BinStats>();
		binStats.add(new BinStats());
		
		if (analysisData == null) {
			analysisData = new AnalysisData();
		} else {
			analysisData.initialize(true);
		}
		analysisData.setOverallHistogram(new MassDiffHistogram());
		setKnownSummaryInfo();
				
		int k = 0;
		sampsForAnalysis = new String[nSampsForAnalysis];
		sampIndicesForAnalysis = new Integer[nSampsForAnalysis];
		for (int i = sampColIndexRange[BinnerConstants.FIRST]; i <= sampColIndexRange[BinnerConstants.LAST]; i++) {
			if (sampExclusions.contains(expFileHeaderLabels[i])) {
				continue;
			}
			sampsForAnalysis[k] = expFileHeaderLabels[i];
			sampIndicesForAnalysis[k++] = expFileColTable.get(expFileHeaderLabels[i]);
		}
		
		Integer [] addedColIndices = new Integer[nAddedCols];
		for (int i = 0; i < nAddedCols; i++) {
			addedColIndices[i] = expFileColTable.get((String) addedCols.get(i));
		}
		
		int rtColIndex = expFileColTable.get(rtColComboBox.getSelectedItem().toString());
		int massColIndex = expFileColTable.get(massColComboBox.getSelectedItem().toString());
					
		expFileFeatureNames = getExpFileFeatureNames();
        if (expFileFeatureNames == null) {
            return false;
        }
        nFeatures = expFileFeatureNames.size();
        
		Map<String, FeatureAttributes> featureLookupTable = createFeatureLookupTable(FileFormatPanel.useNewFormat() ?
                BinnerConstants.EXPERIMENT : BinnerConstants.LOOKUP);
        if (featureLookupTable == null) {
            return false;
        }
        
        analysisData.getAllRawIntensities().setRawDataFile(inputFiles.get(BinnerConstants.EXPERIMENT));
        analysisData.getAllRawIntensities().setFeatureIndex(expFileColTable.get(compColComboBox.getSelectedItem().toString()));
		analysisData.getAllRawIntensities().setMassIndex(massColIndex);
		
        analysisData.setOutlierMap(removeOutliers());
		missingFeatures = getMissingExpFeatures();
        if (missingFeatures == null) {
            return false;
        }
		
		curAnnotFileData = annotationFileWrapPanel.getFileSelection();
		if (openAnnotFile()) {
			fInitializing = true;
			//handleAnnotFileHeadersForDropdowns();
			fInitializing = false;
		}
		annotFileMap = createAnnotFileMap(true, true);
		if (annotFileMap == null) 
			return false;
		
		
		Integer modeSign = ionizationModeWrapPanel.usePositiveMode() ? 1 : -1;
		if (annotFileMap != null)
			annotationPreferencesWrapPanel.updateForAnnotations(modeSign, annotFileMap);
	
		annotationsByCharge = createAnnotByChargeMap();
		if (annotationsByCharge == null) {
			return false;
		}
		annotationsUpToCharge = createAnnotUpToChargeMap();
		if (annotationsUpToCharge == null) {
			return false;
		}
		summaryInfo.setTotalFeatureCount(nFeatures);
		summaryInfo.setRemovedFeatureCount(missingFeatures.size());
		summaryInfo.setAnalyzedFeatureCount(nFeatures - missingFeatures.size());
	    summaryInfo.setSkippedChargeCarriers(annotationFileWrapPanel.getSkippedCCList());
	    summaryInfo.initializeIsotopeAnnotationWarnings(noTierOneChg2Carrier, noTierOneChg3Carrier, ionizationModeWrapPanel.usePositiveMode()); 
	    
		BinnerUtils.setBinnerProp(BinnerConstants.OUTPUT_PROPS_FILE, BinnerConstants.MASS_DIST_UPPER_LIMIT_KEY,
			outputSelectionWrapPanel.getHistogramMax().toString() );
		
		analysisData.getOverallHistogram().setTrackingCutoff(outputSelectionWrapPanel.getHistogramMax());
		Double massTolerance = BinnerNumUtils.toDouble(annotationParametersWrapPanel.getAdductNLMassTol());
		analysisData.setMassDiffRanges(new MassDiffLimits(annotFileMap, massTolerance));
		
		progressBarWeightMultiplier = calculateProgressBarWeightMultiplier();
		setProgressBar(BinnerConstants.DEISOTOPE_PROGRESS_WEIGHT * progressBarWeightMultiplier / 4.0);
		
		int nSkips = 0;
		for (int i = 0; i < nFeatures; i++) {
			if (missingFeatures.contains(i)) {
				Feature feature = new Feature();
				feature.setName(expFileFeatureNames.get(i));
				Double value = inputFiles.get(BinnerConstants.EXPERIMENT).getDouble(i + 1, massColIndex);
				if (value == null) {
					String val = inputFiles.get(BinnerConstants.EXPERIMENT).getString(i + 1, massColIndex);
					if (val == null) {
						inputFiles.get(BinnerConstants.EXPERIMENT).setValue("", i + 1, massColIndex);
					} else if (!val.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Error: Mass entry in row " + (i + 2) + 
								" of the experiment file is not a numeric value: " + val + "      ");
						return false;
					}
				}
				feature.setMass(value);
				value = inputFiles.get(BinnerConstants.EXPERIMENT).getDouble(i + 1, rtColIndex);
				if (value == null) {
					String val = inputFiles.get(BinnerConstants.EXPERIMENT).getString(i + 1, rtColIndex);
					if (val == null) {
						inputFiles.get(BinnerConstants.EXPERIMENT).setValue("", i + 1, rtColIndex);
					} else if (!val.isEmpty()) {
	 					JOptionPane.showMessageDialog(null, "Error: Retention time entry in row " + (i + 2) + 
								" of the experiment file is not a numeric value: " + val + "      ");
	                    return false;
					}
				}
				feature.setRT(value);
				analysisData.getMissingFeaturesInOriginalOrder().add(feature);
				nSkips++;
				continue;
			}
			Feature feature = new Feature();
			feature.setName(expFileFeatureNames.get(i));
			
			List<IndexListItem<Double>> featureOutlierMap = analysisData.getOutlierMap().get(i);
			for (IndexListItem<Double> item : featureOutlierMap)
				feature.addOutlier(item.getIndex(), (Double) item.getValue());
			
			if (FileFormatPanel.useNewFormat()) {
				Double value = inputFiles.get(BinnerConstants.EXPERIMENT).getDouble(i + 1, massColIndex);
				if (value == null) {
					String val = inputFiles.get(BinnerConstants.EXPERIMENT).getString(i + 1, massColIndex);
					if (val == null) {
						inputFiles.get(BinnerConstants.EXPERIMENT).setValue("", i + 1, massColIndex);
					} else if (!val.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Error: Mass entry in row " + (i + 2) + 
								" of the experiment file is not a numeric value: " + val + "      ");
						return false;
					}
				}
				feature.setMass(value);
				value = inputFiles.get(BinnerConstants.EXPERIMENT).getDouble(i + 1, rtColIndex);
				if (value == null) {
					String val = inputFiles.get(BinnerConstants.EXPERIMENT).getString(i + 1, rtColIndex);
					if (val == null) {
						inputFiles.get(BinnerConstants.EXPERIMENT).setValue("", i + 1, rtColIndex);
					} else if (!val.isEmpty()) {
	 					JOptionPane.showMessageDialog(null, "Error: Retention time entry in row " + (i + 2) + 
								" of the experiment file is not a numeric value: " + val + "      ");
	                    return false;
					}
				}
				feature.setRT(value);
			} else {
				if (featureLookupTable.get(feature.getName()) == null) {
					JOptionPane.showMessageDialog(null, "Warning: feature " + feature.getName() + 
							" not found in Mass/RT lib file -- skipping    ");
					nSkips++;
					continue;
				}
				feature.setMass(featureLookupTable.get(feature.getName()).getMass());
				feature.setRT(featureLookupTable.get(feature.getName()).getRT());
			}
			feature.setPercentDefect(massDefectFromParentMass(feature.getMass()));
			Double massKendrick = feature.getMass() * BinnerConstants.KENDRICK_FACTOR;
			feature.setMassDefectKendrick(Math.floor(feature.getMass()) - massKendrick);
			
			if (feature.getRT() > 0.0 && feature.getRT() < 0.7) {
				
		     	if (feature.getMassDefectKendrick() < 0.0 || (feature.getMass() > 600.0 && feature.getPercentDefect() < 0.3)){
				//	System.out.println("Annotating a salt"); 
				//	feature.setFurtherAnnotation("salt");
				}
			}
			//Batch Block
			Double rmd = 1000000 * (feature.getMass() - Math.floor(feature.getMass()))/feature.getMass();
			feature.setRmDefect((int) Math.round(rmd));
			
			feature.setUnadjustedIntensityList(new double[nSampsForAnalysis]);
		
			for (int j = 0; j < nSampsForAnalysis; j++) {
				Double value = inputFiles.get(BinnerConstants.EXPERIMENT).getDouble(i + 1, sampIndicesForAnalysis[j]);
				if (value == null) {
					value = BinnerConstants.BIG_NEGATIVE;
				}
				String strValue = inputFiles.get(BinnerConstants.EXPERIMENT).getString(i + 1, sampIndicesForAnalysis[j]);
				if (this.cleaningWrapPanel.treatZeroAsMissing() && strValue != null && "0".equals(strValue)) {
					value = BinnerConstants.BIG_NEGATIVE;
				}	
				feature.getUnadjustedIntensityList()[j] = value;
			}
			
			for (int j = 0; j < nAddedCols; j++) {
				String strValue = inputFiles.get(BinnerConstants.EXPERIMENT).getString(i + 1, addedColIndices[j]);
				if (strValue == null) {
					strValue = "";
				}
				feature.getAddedColValues().add(strValue);
			}
			analysisData.getNonMissingFeaturesInOriginalOrder().add(feature);
		}
		
		setProgressBar(BinnerConstants.DEISOTOPE_PROGRESS_WEIGHT * progressBarWeightMultiplier / 2.0);
		
		Integer nNonMissingFeatures = nFeatures - nSkips;
		Double [] nonMissingRTs = new Double[nNonMissingFeatures];
		for (int i = 0; i < nNonMissingFeatures; i++) {
			nonMissingRTs[i] = analysisData.getNonMissingFeaturesInOriginalOrder().get(i).getRT();
		}
		if (ListUtils.alreadySorted(nonMissingRTs, true)) {
			analysisData.setIndexedNonMissingRTs(ListUtils.identityList(nonMissingRTs));
		} else {
			analysisData.setIndexedNonMissingRTs(ListUtils.sortedList(nonMissingRTs));
		}
	
		setAdjustedIntensities(nNonMissingFeatures);
		
		int curBinIndex = -1;
		Double oldRT = -9999.0;
		Double gap = BinnerNumUtils.toDouble(binningParametersWrapPanel.getGap());
		analysisData.setBinContents(new ArrayList<List<Integer>>());
		int offsetWithinBin = 0;
		for (int i = 0; i < nNonMissingFeatures; i++) {
			int featureIndex = analysisData.getIndexedNonMissingRTs().get(i).getIndex();
			Double newRT = analysisData.getIndexedNonMissingRTs().get(i).getValue();
			if (newRT - oldRT > gap) {
				binStats.add(new BinStats());
				curBinIndex++;
				offsetWithinBin = 0;
			}
			if (analysisData.getBinContents().size() <= curBinIndex) {
				analysisData.getBinContents().add(new ArrayList<Integer>());
			}
			List<Integer> indexList = analysisData.getBinContents().get(curBinIndex);
			indexList.add(featureIndex);
			analysisData.getBinContents().set(curBinIndex, indexList);
			Feature feature = analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndex);
			feature.setBinIndex(curBinIndex);
			feature.setOffsetWithinBin(offsetWithinBin);
			oldRT = newRT;
			offsetWithinBin++;
		}
		
		nBins = curBinIndex + 1;
		double [][] binnedIntensityData;
		analysisData.setBinwiseCorrelations(new ArrayList<RealMatrix>());
		analysisData.setBinwiseMassDiffs(new ArrayList<RealMatrix>());
		for (int binIndex = 0; binIndex < nBins; binIndex++) {
			int fullBinSize = analysisData.getBinContents().get(binIndex).size();
			binStats.get(binIndex).setnFeatures(fullBinSize);
			
			if (fullBinSize == 1) {
				binStats.get(binIndex).initializeToNotClustered(1);
				double [][] singlePointMatrix = new double[1][1];
				singlePointMatrix[0][0] = 1;
				analysisData.getBinwiseCorrelations().add(MatrixUtils.createRealMatrix(singlePointMatrix));
				singlePointMatrix = new double[1][1];
				singlePointMatrix[0][0] = 0;
				analysisData.getBinwiseMassDiffs().add(MatrixUtils.createRealMatrix(singlePointMatrix));
				Integer [][] singlePointArray = new Integer[1][1];
				singlePointArray[0][0] = -1;
				analysisData.getBinwiseMassDiffClasses().add(singlePointArray);
				continue;
			}
			if (fullBinSize > binLimitsWrapPanel.getBinSizeLimit()) {
				JOptionPane.showMessageDialog(null, "Warning: One of the bins calculated for your dataset contains " + fullBinSize + " features "
			  +  "which exceeds the maximum allowable binsize (" + binLimitsWrapPanel.getBinSizeLimit() + ").     "
			  + BinnerConstants.LINE_SEPARATOR
			  +	"By default Binner will not process files exceeding this limit.  A smaller Retention Time Gap Size ("
			  + "currently set to " + binningParametersWrapPanel.getGap()+ ") may decrease bin sizes.    "
			  + BinnerConstants.LINE_SEPARATOR
			  + "Although it is not recommended, if you'd like to continue "
			  + "processsing your file with large bins in place and are sure that your computer has sufficient RAM    "
			  + BinnerConstants.LINE_SEPARATOR 
			  + "tp handle memory-intensive computations, the Bin Size Limit For Output parameter can be overridden on the Feature Grouping Panel.    ");
		      return false;
			}
		
			if (!skipBinwiseOutput && (fullBinSize >  binLimitsWrapPanel.getBinSizeOutputLimit())) {
				skipBinwiseOutput = true;
				JOptionPane.showMessageDialog(null, "Warning: One of the bins calculated for your dataset contains " + fullBinSize + " features "
						  +  "which exceeds the maximum recommended     " 
						  + BinnerConstants.LINE_SEPARATOR 
						  + "binsize for binwise output (currently set to " + binLimitsWrapPanel.getBinSizeOutputLimit() + "). "
						  +	"By default, Binner disables binwise output when this limit is exceeded.    " 
						  + BinnerConstants.LINE_SEPARATOR 
						  + "Decreasing the Retention Time Gap Size (currently set to "
						  +  binningParametersWrapPanel.getGap() + ") may decrease bin sizes.    " 
						  + BinnerConstants.LINE_SEPARATOR 
						  + "Alternatively, you may override the binsize output limit on the Feature Grouping panel. This option    "
						  + BinnerConstants.LINE_SEPARATOR 
						  + "is not recommended unless you run Binner on a computer with ample RAM (required by Excel to open large files).    ");
			}
				
			binnedIntensityData = new double[nSampsForAnalysis][fullBinSize];
			for (int featureIndex = 0; featureIndex < fullBinSize; featureIndex++) {
				Feature feature = BinnerIndexUtils.getFeatureFromRTSortedFullBin(binIndex, featureIndex);
				for (int sampleIndex = 0; sampleIndex < nSampsForAnalysis; sampleIndex++) {
					binnedIntensityData[sampleIndex][featureIndex] = feature.getAdjustedIntensityList()[sampleIndex];
				}
			}
			
			RealMatrix x = null;
			if (analysisTypeWrapPanel.pearsonIsSelected()) {
				PearsonsCorrelation corr = new PearsonsCorrelation(binnedIntensityData);
				x = corr.getCorrelationMatrix();
				analysisData.getBinwiseCorrelations().add(x);
			} else {
				SpearmansCorrelation corr = new SpearmansCorrelation();
				x = corr.computeCorrelationMatrix(binnedIntensityData);
				analysisData.getBinwiseCorrelations().add(x);
			}
			
			analysisData.getBinwiseMassDiffs().add(constructMassDiffMatrix(binIndex));
			analysisData.getBinwiseMassDiffClasses().add(calculateMassDiffClasses(binIndex));
		}
		
		setProgressBar(3.0 * BinnerConstants.DEISOTOPE_PROGRESS_WEIGHT * progressBarWeightMultiplier / 4.0);
		
		curBinIndex = 0;
		oldRT = Double.NEGATIVE_INFINITY;
		gap = BinnerNumUtils.toDouble(binningParametersWrapPanel.getGap());
		analysisData.setBinwiseIsotopeGroups(new ArrayList<HashMap<Integer, IsotopeGroup>>());
		analysisData.setBinwiseFeaturesForClustering(new ArrayList<List<Integer>>());
		analysisData.setBinwiseCorrelationsMinusIsotopes(new ArrayList<RealMatrix>());
		analysisData.setBinwiseMassDiffsMinusIsotopes(new ArrayList<RealMatrix>());
		isotopeLabelNumber = 1;
		isotopesFoundCount = 0;
		//BinnerOutput
		for (int i = 0; i < nNonMissingFeatures; i++) {
			Double newRT = analysisData.getIndexedNonMissingRTs().get(i).getValue();
			if (newRT - oldRT > gap) {
				analysisData.getBinwiseIsotopeGroups().add(curBinIndex, new HashMap<Integer, IsotopeGroup>());
				analysisData.getBinwiseFeaturesForClustering().add(curBinIndex, new ArrayList<Integer>());
				analysisData.getBinwiseCorrelationsMinusIsotopes().add(curBinIndex, new Array2DRowRealMatrix());
				analysisData.getBinwiseMassDiffsMinusIsotopes().add(curBinIndex, new Array2DRowRealMatrix());
				if (cleaningWrapPanel.doDeisotoping()) {
					deisotopeBin(curBinIndex);
				} else {
					analysisData.getBinwiseFeaturesForClustering().set(curBinIndex, 
							new ArrayList<Integer>(analysisData.getBinContents().get(curBinIndex)));
					analysisData.getBinwiseCorrelationsMinusIsotopes().set(curBinIndex, 
							analysisData.getBinwiseCorrelations().get(curBinIndex).copy());
					analysisData.getBinwiseMassDiffsMinusIsotopes().set(curBinIndex, 
							analysisData.getBinwiseMassDiffs().get(curBinIndex).copy());
				}
			curBinIndex++;
			}
			oldRT = newRT;
		}
		
		setProgressBar(BinnerConstants.DEISOTOPE_PROGRESS_WEIGHT * progressBarWeightMultiplier);
		
		analysisData.setBinwiseMeanCorrelations(new ArrayList<Double>());
		analysisData.setBinwiseRTRanges(new ArrayList<Double>());
		for (int binIndex = 0; binIndex < nBins; binIndex++) {
			int binSizeForClustering = analysisData.getBinwiseCorrelationsMinusIsotopes().get(binIndex).getRowDimension();
			if (binSizeForClustering == 1) {
				double [][] singlePointMatrix = new double[1][1];
				singlePointMatrix[0][0] = 1;
				analysisData.getBinwiseMeanCorrelations().add(0.0);
				analysisData.getBinwiseRTRanges().add(BinnerConstants.EPSILON);
				continue;
			}
			Double RTRange = 0.0;
			boolean atStart = true;
			Feature lastNonIsotope = null;
			for (offsetWithinBin = 0; offsetWithinBin < binSizeForClustering; offsetWithinBin++) {
				Feature feature = BinnerIndexUtils.getFeatureFromClusterSortedDeisotopedBin(binIndex, offsetWithinBin);
				if (atStart) {
					atStart = false;
					RTRange -= feature.getRT();
				}
				lastNonIsotope = feature;
			}
			RTRange += lastNonIsotope.getRT();
			analysisData.getBinwiseRTRanges().add(RTRange + BinnerConstants.EPSILON);
			
			Double sum = 0.0;
			Double denominator = 0.0;
			for (int j = 0; j < analysisData.getBinwiseCorrelationsMinusIsotopes().get(binIndex).getRowDimension(); j++) {
				for (k = j + 1; k < analysisData.getBinwiseCorrelationsMinusIsotopes().get(binIndex).getColumnDimension(); k++) {
					sum += analysisData.getBinwiseCorrelationsMinusIsotopes().get(binIndex).getEntry(j, k);
					denominator++;
				}
			}
			analysisData.getBinwiseMeanCorrelations().add(sum / denominator);
		}

		if (!outputResults()) {
			JOptionPane.showMessageDialog(null, "Error during clustering operation.");
            return false;
		}
		return true;
	}
	
	private Double calculateProgressBarWeightMultiplier() {
		Double weightSum = BinnerConstants.DEISOTOPE_PROGRESS_WEIGHT + BinnerConstants.CLUSTERING_PROGRESS_WEIGHT;
		List<Integer> idsForPrintedTabs = outputSelectionWrapPanel.getSelectedTabIds();
		for (Integer id : idsForPrintedTabs) {
			weightSum += (Arrays.asList(BinnerConstants.BY_BIN_OUTPUTS).contains(id)) ? 
					BinnerConstants.BINWISE_OUTPUT_WEIGHT : BinnerConstants.CLUSTERWISE_OUTPUT_WEIGHT;
		}
		
		return BinnerConstants.PROGRESS_BAR_WIDTH / weightSum;
	}

	private void setKnownSummaryInfo() {
		int nOutliersPrevRemoved = 0;
		if (summaryInfo != null && analysisData.getOutlierMap() != null)
			nOutliersPrevRemoved = summaryInfo.getnOutlierPts();
			
		summaryInfo = new SummaryInfo();
		
		summaryInfo.setAnnotateRebinCluster(rebinOptionsWrapPanel.rebinIsSelected());
		summaryInfo.setAnnotationMap(annotFileMap);
		summaryInfo.setnOutlierPts(nOutliersPrevRemoved);
		summaryInfo.setTitleWithVersion(titleWithVersion);
		summaryInfo.setExpFilePath(curExpFileData.getPath());
		summaryInfo.setExpFileCompCol(compColComboBox.getSelectedItem().toString());
		summaryInfo.setFirstSamp(sampColComboBoxPair.get(BinnerConstants.FIRST).getSelectedItem().toString());
		summaryInfo.setLastSamp(sampColComboBoxPair.get(BinnerConstants.LAST).getSelectedItem().toString());
		summaryInfo.setNSamps(nSampsForAnalysis);
		summaryInfo.setExpFileRTCol(rtColComboBox.getSelectedItem().toString());
		summaryInfo.setExpFileMassCol(massColComboBox.getSelectedItem().toString());
		summaryInfo.setPctMissingCutoff(Double.valueOf(cleaningWrapPanel.getMissingnessPct()));
		summaryInfo.setOutlierThreshold(Double.valueOf(cleaningWrapPanel.getOutlierThreshold()));
		summaryInfo.setDoTransform(cleaningWrapPanel.doTransformation());
		summaryInfo.setDoDeisotoping(cleaningWrapPanel.doDeisotoping());
		summaryInfo.setDoHistogramDeisotoping(cleaningWrapPanel.removeIsotopeFeaturesForDistribution());
		summaryInfo.setIsotopeMassTol(cleaningWrapPanel.getIsotopeMassTol());
		summaryInfo.setIsotopeCorrCutoff(cleaningWrapPanel.getCorrCutoff());
		summaryInfo.setIsotopeRTRange(cleaningWrapPanel.getIsotopeRTDiff());
		summaryInfo.setAnnotFilePath(curAnnotFileData.getPath());
		summaryInfo.setAnnotFileAnnotCol(annotationFileWrapPanel.getAnnotColumnName()); 
		summaryInfo.setAnnotFileMassCol(annotationFileWrapPanel.getMassColumnName()); 
		summaryInfo.setAnnotFileModeCol(annotationFileWrapPanel.getModeColumnName()); 
		summaryInfo.setAnnotFileChargeCol(annotationFileWrapPanel.getChargeColumnName()); 
		summaryInfo.setAnnotFileTierCol(annotationFileWrapPanel.getTierColumnName()); 
		summaryInfo.setChargeCanVaryWithoutIsotopeInfo(annotationPreferencesWrapPanel.allowChargeToVary());
		summaryInfo.setUseNMForChargeCarrierCall(annotationPreferencesWrapPanel.useNeutralMassesToGetBestChargeCarrier());
		summaryInfo.setIonizationMode(ionizationModeWrapPanel.usePositiveMode() ? "Positive" : "Negative");
		summaryInfo.setOutputDirectory(outputDirectory.getAbsolutePath());
		summaryInfo.setGapBetweenBins(binningParametersWrapPanel.getGap());
		summaryInfo.setAnnotMassTol(annotationParametersWrapPanel.getAdductNLMassTol());
		summaryInfo.setAnnotRtTol(annotationParametersWrapPanel.getRtTol());
		summaryInfo.setClusteringRule(binsToClusterWrapPanel.getRuleName());
		summaryInfo.setThreshold("" + binsToClusterWrapPanel.getCutoffThreshold());
		summaryInfo.setClusteringMethod(clusteringOptionsWrapPanel.getClusteringMethod());
		summaryInfo.setRtClusteringMethod(rebinOptionsWrapPanel.getDivisionMethod() 
				+ (rebinOptionsWrapPanel.rebinIsSelected() ? binningParametersWrapPanel.getGap() : ""));
		String reclusterGapRule = rebinOptionsWrapPanel.rebinIsSelected() ? " All clusters" : "Any containing RT gap greater than " + binningParametersWrapPanel.getGap();
		summaryInfo.setReclusteredClustersRule(reclusterGapRule +  (rebinOptionsWrapPanel.rebinIsSelected() ? "" :  tempPanel.getForcedClusteringPolicy()));
		summaryInfo.setRtGapRule(rebinOptionsWrapPanel.rebinIsSelected() ? "All gaps greater than " + binningParametersWrapPanel.getGap() : rebinOptionsWrapPanel.getGapPolicy());
		summaryInfo.setCorrelationType(analysisTypeWrapPanel.getCorrelationType());
		summaryInfo.setChargeCarrierPrefs(annotationPreferencesWrapPanel.getChargeCarrierPrefMap());
		summaryInfo.setZeroMeansMissing(cleaningWrapPanel.treatZeroAsMissing());
		}
	
	private void setAdjustedIntensities(int nNonMissingFeatures) {
		double [] validIntensities = new double[nSampsForAnalysis];
		Median median = new Median();
		
		for (int i = 0; i < nNonMissingFeatures; i++) {
			int k = 0;
			Feature feature = analysisData.getNonMissingFeaturesInOriginalOrder().get(i);
			
			double [] originalIntensities = feature.getUnadjustedIntensityList();
			for (int j = 0; j < nSampsForAnalysis; j++) {
				Double value = originalIntensities[j];
				if (value == null || value < 0.0) {
					continue;
				}
				validIntensities[k++] = value;
			}
			Double featureMedian = median.evaluate(Arrays.copyOfRange(validIntensities, 0, k));
			feature.setMedianIntensity(featureMedian);
			
			double [] adjustedIntensities = new double[nSampsForAnalysis];
			for (int j = 0; j < nSampsForAnalysis; j++) {
				Double value = originalIntensities[j];
				if (value == null || value < 0.0) {
					adjustedIntensities[j] = featureMedian;
				} else {
					if (Math.abs(value - featureMedian) < 1e-10)
						feature.setMedianIntensityIdx(j);
					adjustedIntensities[j] = value;
				}
				if (cleaningWrapPanel.doTransformation()) {
					adjustedIntensities[j] = Math.log1p(adjustedIntensities[j]);
				}
			}
			analysisData.getNonMissingFeaturesInOriginalOrder().get(i).setAdjustedIntensityList(adjustedIntensities);
		}
	}
	
	private List<String> getExpFileFeatureNames() {
		List<String> featureNames = new ArrayList<String>();
		Integer featureIndex = expFileColTable.get(compColComboBox.getSelectedItem().toString());
		
		for (int iRow = 1; iRow <= inputFiles.get(BinnerConstants.EXPERIMENT).getEndRowIndex(); iRow++) {
			try {
				String value = inputFiles.get(BinnerConstants.EXPERIMENT).getString(iRow, featureIndex);
				if (value == null || "".equals(value.trim())) {
					JOptionPane.showMessageDialog(null, "Error: Blank feature name in row " +
							(iRow + 1) + " of experiment file.");
					return null;
				}
				featureNames.add(value.trim());
			} catch (Exception e) {
				System.out.println("Error reading experiment feature list");
				e.printStackTrace();
                return null;
			}
		}
		
		return featureNames;
	}

	private Map<String, FeatureAttributes> createFeatureLookupTable(Integer inputFileIndex) {
		Map<String, FeatureAttributes> lookupTable = new HashMap<String, FeatureAttributes>();
		Integer massIndex = null;
		Integer rtIndex = null;
		Integer featureIndex = expFileColTable.get(compColComboBox.getSelectedItem().toString());
		if (inputFileIndex == BinnerConstants.EXPERIMENT) {
			massIndex = expFileColTable.get(massColComboBox.getSelectedItem().toString());
			rtIndex = expFileColTable.get(rtColComboBox.getSelectedItem().toString());
		} 
		
		for (int iRow = 1; ; iRow++) {
			try {
				String value = inputFiles.get(inputFileIndex).getString(iRow, featureIndex);
				if (value == null || "".equals(value.trim())) {
					break;
				}
				FeatureAttributes attributes = new FeatureAttributes();
				Double doubleVal = inputFiles.get(inputFileIndex).getDouble(iRow, massIndex);
				if (inputFileIndex == BinnerConstants.LOOKUP && doubleVal == null) {
					String val = inputFiles.get(inputFileIndex).getString(iRow, massIndex);
					if (val == null) {
						inputFiles.get(inputFileIndex).setValue("", iRow, massIndex);
					} else if (!val.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Error: Mass entry in row " + (iRow + 2) + 
								" of the lookup file is not a numeric value: " + val + "      ");
	                    return null;
					}
				}
				attributes.setMass(doubleVal);
				doubleVal = inputFiles.get(inputFileIndex).getDouble(iRow, rtIndex);
				if (inputFileIndex == BinnerConstants.LOOKUP && doubleVal == null) {
					String val = inputFiles.get(inputFileIndex).getString(iRow, rtIndex);
					if (val == null) {
						inputFiles.get(inputFileIndex).setValue("", iRow, rtIndex);
					} else if (!val.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Error: Retention time entry in row " + (iRow + 2) + 
								" of the lookup file is not a numeric value: " + val + "      ");
	                    return null;
					}
				}		
				attributes.setRT(doubleVal);
				lookupTable.put(value.trim(), attributes);
			} catch (Exception e) {
				System.out.println("Error creating lookup feature map");
				e.printStackTrace();
                return null;
            }
		}
		return lookupTable;
	}
	
	private List<List<IndexListItem<Double>>> removeOutliers() 
		{
		if (analysisData != null && analysisData.getOutlierMap() != null)
			return analysisData.getOutlierMap();
		
		List<List<IndexListItem<Double>>> newMap = new ArrayList<List<IndexListItem<Double>>>();
		
		Double outlierThreshold = null;
		Integer nOutliersIdentified = 0;
		try {
			outlierThreshold = Double.parseDouble(cleaningWrapPanel.getOutlierThreshold());
		} catch (NumberFormatException e)  {    }
		
		for (int i = 0; i < nFeatures; i++)  {
 			List<Double> unfilteredValuesForFeature = new ArrayList<Double>();
 			for (int j = 0; j < nSampsForAnalysis; j++)  {
				Double value = inputFiles.get(BinnerConstants.EXPERIMENT).getDouble(i + 1, sampIndicesForAnalysis[j]);
				unfilteredValuesForFeature.add(value);
 			}
		
 			List<Integer> outlierIndicesForFeature = BinnerDataUtils.getOutlierIndices(unfilteredValuesForFeature,  outlierThreshold);
			
 			nOutliersIdentified += outlierIndicesForFeature.size();
	 		List<IndexListItem<Double>> featureOutlierMap = new ArrayList<IndexListItem<Double>>();
	 		
	 		for (Integer outlierIdx : outlierIndicesForFeature)  {
 			 	Double value = inputFiles.get(BinnerConstants.EXPERIMENT).getDouble(i + 1, sampIndicesForAnalysis[outlierIdx]);
 			 	IndexListItem<Double> item = new IndexListItem<Double>(value, outlierIdx);
	 			featureOutlierMap.add(item);
	 			inputFiles.get(BinnerConstants.EXPERIMENT).setValue(".", i + 1, sampIndicesForAnalysis[outlierIdx]);
 		 	}
	 		newMap.add(featureOutlierMap);
	 	}
		summaryInfo.setnOutlierPts(nOutliersIdentified);
		return newMap;
	}

	private List<Integer> getMissingExpFeatures() {
		List<Integer> missingList = new ArrayList<Integer>();
		BinnerNumUtils.toDouble(binningParametersWrapPanel.getGap());
		Double threshold = nSampsForAnalysis * Double.valueOf(cleaningWrapPanel.getMissingnessPct()) / 100.0;
		for (int i = 0; i < nFeatures; i++)  {
			int nMissing = 0;
			for (int j = 0; j < nSampsForAnalysis; j++)  {
				Double value = inputFiles.get(BinnerConstants.EXPERIMENT).getDouble(i + 1, sampIndicesForAnalysis[j]);
				String strValue = inputFiles.get(BinnerConstants.EXPERIMENT).getString(i + 1, sampIndicesForAnalysis[j]);
				if (value == null || value < Double.MIN_VALUE || (strValue != null && 
						Arrays.asList(BinnerConstants.MISSINGNESS_LIST).contains("0") && strValue.trim().equals("0")))  { 
					if (value == null)   {
						if (strValue == null) {
							inputFiles.get(BinnerConstants.EXPERIMENT).setValue("", i + 1, sampIndicesForAnalysis[j]);
						} else if (!strValue.isEmpty() && !Arrays.asList(BinnerConstants.MISSINGNESS_LIST).
								contains(strValue))  {
							JOptionPane.showMessageDialog(null, "Error: Intensity measurement in row " + (i + 2) + ", "
									+ "column " + (sampIndicesForAnalysis[j] + 1) + " of the experiment file is not a "
										+ "numeric value: " + strValue + "      ");
							return null;
						}
					}
					
					if (++nMissing > threshold) {	
						missingList.add(i);
						break;
					}
				}
			}
		}
		return missingList;	
	} 
	
	
	private boolean clusterBinByCorrelations(int binIndex) {
		int binSizeForClustering = analysisData.getBinwiseCorrelationsMinusIsotopes().get(binIndex).getRowDimension();
		
		FeatureClustering clustering = new FeatureClustering(analysisData.getBinwiseCorrelationsMinusIsotopes().
				get(binIndex).getData());
		OldFeatureClustering oldClustering = new OldFeatureClustering(analysisData.getBinwiseCorrelationsMinusIsotopes().
				get(binIndex).getData());	
		
		try {	
			if (clusteringOptionsWrapPanel.doSilhouetteClustering())
				clustering.runClustering(clusteringOptionsWrapPanel.doUnweightedSilhouetteClustering());
			else
				oldClustering.runClustering();
			} catch (Exception e) {
			e.printStackTrace();
		}
	
		List<List<Integer>> clusters = clustering.getClusters();
		if (!clusteringOptionsWrapPanel.doSilhouetteClustering())
			clusters = oldClustering.getClusters();
		
		//List<Integer> clusterCounts = clustering.getClusterCounts();
		//System.out.println("\nCluster sizes for " + bin + ": " + clusterCounts);
		//binStats.get(bin).setOriginalClusterSizes(clustering.getClusterCounts());
		
		Integer [] initialClusterData = new Integer[binSizeForClustering];
		for (int clusterIndex = 0; clusterIndex < binSizeForClustering; clusterIndex++) {
			for (Integer featureIndex : clusters.get(clusterIndex)) {
				initialClusterData[featureIndex] = clusterIndex;
			}
		}
		
		//StringBuilder sb = new StringBuilder();
		//for (int i = 0; i < initialClusterData.length;i++)
		//	sb.append(initialClusterData[i] + ", ");
		//System.out.println("initialClusterData :" + sb.toString());
		
		Integer [] clusterData = renumberClusters(initialClusterData);
		//sb = new StringBuilder();
		//for (int i = 0; i < clusterData.length;i++)
		//	sb.append(clusterData[i] + ", ");
		//	System.out.println("renumbered :" + sb.toString());
		//	indexedClustersForRebinning contains the renumbered 1, 2, ...   cluster numbers, along with the index within the bin (0, binSizeForClustering)
		
		analysisData.setIndexedClustersForRebinning(ListUtils.sortedList(clusterData));
		// also initialize the placeholder for the results of binAgain()
		analysisData.setIndexedClustersFromRebinning(ListUtils.sortedList(clusterData));
		
		List<Integer> originalClusterIndices = new ArrayList<Integer>();
		for (int i = 0; i < binSizeForClustering; i++) {
			Feature feature = BinnerIndexUtils.getFeatureFromClusterSortedDeisotopedBin(binIndex, i);
			feature.setOldCluster(clusterData[i]);
			feature.setNewCluster(clusterData[i]);
			feature.setNewNewCluster(clusterData[i]);
			originalClusterIndices.add(clusterData[i]);
		}	
		
		binStats.get(binIndex).setOriginalClusterIndices(originalClusterIndices);
		//System.out.println("Original sizes from counts " + binStats.get(bin).getOriginalClusterSizes());
		return true;
	}
	
	private Integer [] renumberClusters(Integer [] clusterData) {
		Integer [] newClusterData = new Integer[clusterData.length];
		Map<Integer, Integer> clusterMap = new HashMap<Integer, Integer>();
		int nextAvailableNumber = 1;
		for (int i = 0; i < clusterData.length; i++) {
			Integer newClusterNumber = clusterMap.get(clusterData[i]);
			if (newClusterNumber == null) {
				clusterMap.put(clusterData[i], nextAvailableNumber);
				newClusterData[i] = nextAvailableNumber;
				nextAvailableNumber++;
			} else {
				newClusterData[i] = newClusterNumber;
			}
		}
		return newClusterData;
	}
	
	private void reClusterByRT(int binIndex, List<Integer> rebinnedClusters) {
		
		int binSizeForClustering = analysisData.getBinwiseCorrelationsMinusIsotopes().get(binIndex).getRowDimension();
		
		Integer [] preRebinClusters = new Integer[binSizeForClustering];
		Integer [] featureIndexes = new Integer[binSizeForClustering];
		Double [] binRTs = new Double[binSizeForClustering];
	
		for (int k = 0; k < binSizeForClustering; k++) {
			preRebinClusters[k] = analysisData.getIndexedClustersForRebinning().get(k).getValue();
			featureIndexes[k] = analysisData.getBinwiseFeaturesForClustering().get(binIndex).
					get(analysisData.getIndexedClustersForRebinning().get(k).getIndex());
			binRTs[k] = analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndexes[k]).getRT();
		}
		
		analysisData.setIndexedClustersFromRTClustering(analysisData.getIndexedClustersForRebinning());
		
		//Boolean info = false; 
		//if (info)  {
		//	System.out.println("Bin size for bin " + bin + " is " + binSizeForClustering);
        //  System.out.println("Rebinned clusters were " + rebinnedClusters);
		//	StringBuilder sb = new StringBuilder();
		//	for (int i = 0; i < binSizeForClustering;i++)
		//		sb.append(binClusters[i] + ", ");
		//	System.out.println("Bin clusters for bin " + bin + ": " + sb.toString());
		//}
			
		List<Double> rtsToCluster = new ArrayList<Double>();
		if (rebinnedClusters.contains(preRebinClusters[0]))
			rtsToCluster.add(binRTs[0]);
		
		List<Integer> clusteredKs = new ArrayList<Integer>();
		if (rebinnedClusters.contains(preRebinClusters[0]))
			clusteredKs.add(0);
	
		Integer clusterOffset = 0;
		try {
			for (int k = 1; k < binSizeForClustering; k++)  {
				Boolean atEnd = (k == binSizeForClustering - 1);
				Boolean atBoundary = atEnd || !(preRebinClusters[k + 1].equals(preRebinClusters[k])); 
				Boolean saveValue = (rebinnedClusters.contains(preRebinClusters[k]));
				Boolean doClustering = atBoundary && rebinnedClusters.contains(preRebinClusters[k]) && ((rtsToCluster.size() > 0));
				
				if (saveValue)  {
					rtsToCluster.add(binRTs[k]);
					clusteredKs.add(k);
				}
				 
				if (!doClustering)
					analysisData.getIndexedClustersFromRTClustering().get(k).setValue(preRebinClusters[k] + (clusterOffset));
				else {
					RTClustering rtClustering = new RTClustering(rtsToCluster);
					rtClustering.runClustering(false);
					
					double minRTGap = Double.parseDouble(rebinOptionsWrapPanel.getMinRTGapThreshold());
					double alwaysRTGap = Double.parseDouble(rebinOptionsWrapPanel.getAlwaysGapThreshold());
					
					RTClusteringPostProcessor postClusteringAdjustments = new RTClusteringPostProcessor(rtClustering, minRTGap, alwaysRTGap);
					Integer [] clusterAssignmentsForCluster = postClusteringAdjustments.getRenumberedClusterAssignments(false);
			        int nNewClusters = postClusteringAdjustments.getSmoothedNClusters() - 1;
					
					//if (info) {
					//	StringBuilder sb = new StringBuilder();
					//	for (int i = 0; i < clusterAssignmentsForCluster.length;i++)
					//		sb.append(clusterAssignmentsForCluster[i] + ", ");
					//  System.out.println("Cluster assignments after renumbering for bin " + bin + ": " + sb.toString());
					//}
					
					for (int j = 0; j < clusterAssignmentsForCluster.length; j++) {
						int indexInBin = clusteredKs.get(j);
						int newCluster = preRebinClusters[indexInBin] + (clusterAssignmentsForCluster[j] - 1) + clusterOffset;
						analysisData.getIndexedClustersFromRTClustering().get(indexInBin).setValue(newCluster);
						//if (binIndex == 14) {
						//	System.out.println("Assigned " + newCluster + " for indexInBin " + indexInBin);
						//}
						// if (info) {
						//  	System.out.println("Original Cluster is " + binClusters[indexInBin]);
						//		System.out.println("Cluster assignment : " + clusterAssignmentsForCluster[j] + " Offset " + clusterOffset + " gives " + (binClusters[indexInBin] + clusterAssignmentsForCluster[j] - 1 + clusterOffset));
						//	}
					}
			        clusterOffset += nNewClusters;
					rtsToCluster.clear();
					clusteredKs.clear();
				}
			}
		}
		catch ( Exception e) { System.out.println("Error while reclustering on RT"); }
		
		List<Integer> reclusterClusterIndices = new ArrayList<Integer>();
		for (int k = 0; k < binSizeForClustering; k++)  {
			int newCluster = analysisData.getIndexedClustersFromRTClustering().get(k).getValue();
			Feature feature = analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndexes[k]);
			feature.setNewNewCluster(newCluster);
			reclusterClusterIndices.add(newCluster);
		}
		binStats.get(binIndex).setReclusterClusterIndices(reclusterClusterIndices);
			
	//	System.out.println("Recluster cluster indices were " + reclusterClusterIndices);
	//	binStats.get(bin).setReclusterClusterSizes(getGroupSizesFromIndexedMembership(reclusterClusterIndices));
	//	System.out.println("Recluster cluster sizes for bin " + (binIndex + 1) + ": " + binStats.get(binIndex).getReclusterClusterSizes());
	}

	private List<Integer> binAgain(int binIndex) {
		int binSizeForClustering = analysisData.getBinwiseCorrelationsMinusIsotopes().get(binIndex).getRowDimension();
		Integer [] binClusters = new Integer[binSizeForClustering];
		Integer [] featureIndexes = new Integer[binSizeForClustering];
		Double [] binRTs = new Double[binSizeForClustering];
		
		for (int k = 0; k < binSizeForClustering; k++) {
			binClusters[k] = analysisData.getIndexedClustersForRebinning().get(k).getValue();
			featureIndexes[k] = BinnerIndexUtils.getFeatureIndexFromClusterSortedDeisotopedBin(binIndex, k);
			binRTs[k] = analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndexes[k]).getRT();
		}
		
		Double gap = BinnerNumUtils.toDouble(binningParametersWrapPanel.getGap());
		Double forcedClusterCutoff = Double.valueOf(tempPanel.getForcedClusterThreshold());
		
		Map<Integer, Integer> rebinnedClusters = new HashMap<Integer, Integer>();
		int newClusterSize = 1;
		Integer clusterOffset = 0;
		
		for (int k = 1; k < binSizeForClustering; k++) {
			if (binClusters[k].equals(binClusters[k - 1]) && binRTs[k] - binRTs[k - 1] > gap) {
				clusterOffset++;
				newClusterSize = 1;
				rebinnedClusters.put(binClusters[k], null);
			}
			else if (!binClusters[k].equals(binClusters[k - 1]))  {
				if (tempPanel.alwaysRTSubclusterLargeClusters() && newClusterSize > forcedClusterCutoff) 
					rebinnedClusters.put(binClusters[k - 1], null);
				newClusterSize = 1;
			}
			else
				newClusterSize++;
			
			analysisData.getIndexedClustersFromRebinning().get(k).setValue(binClusters[k] + clusterOffset);
		}
		if (tempPanel.alwaysRTSubclusterLargeClusters() && newClusterSize > forcedClusterCutoff) 
			rebinnedClusters.put(binClusters[binSizeForClustering - 1], null);
		
		List<Integer> rebinClusterIndices = new ArrayList<Integer>();
		for (int k = 0; k < binSizeForClustering; k++) {
			int newCluster = analysisData.getIndexedClustersFromRebinning().get(k).getValue();
			analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndexes[k]).setNewCluster(newCluster);
			rebinClusterIndices.add(newCluster);
		}
		
		binStats.get(binIndex).setRebinClusterIndices(rebinClusterIndices);
		binStats.get(binIndex).setReclusterClusterIndices(rebinClusterIndices);
	
		return ListUtils.makeListFromCollection(rebinnedClusters.keySet());
	}
	
	
	private void deisotopeBin(int binIndex) {
		int fullBinSize = analysisData.getBinContents().get(binIndex).size();
		
		double [] binMasses = new double[fullBinSize];
		double [] binRTs = new double[fullBinSize];
		double [] binMedianIntensities = new double[fullBinSize];
		double [][] binCorrelations = new double[fullBinSize][fullBinSize];
		int [] binFeatureIndexes = new int[fullBinSize];
		for (int k = 0; k < fullBinSize; k++) {
			binFeatureIndexes[k] = BinnerIndexUtils.getFeatureIndexFromRTSortedFullBin(binIndex, k);
			Feature feature_k = BinnerIndexUtils.getFeatureFromRTSortedFullBin(binIndex, k);
			binMasses[k] = feature_k.getMass();
			binRTs[k] = feature_k.getRT();
			binMedianIntensities[k] = feature_k.getMedianIntensity();
			binCorrelations[k] = analysisData.getBinwiseCorrelations().get(binIndex).getRow(k);
		}
			
		Double massTolerance = BinnerNumUtils.toDouble(cleaningWrapPanel.getIsotopeMassTol());
		Double corrCutoff = BinnerNumUtils.toDouble(cleaningWrapPanel.getCorrCutoff());
		Double RTWidth = BinnerNumUtils.toDouble(cleaningWrapPanel.getIsotopeRTDiff());
		for (int z = 0; z < BinnerConstants.MAX_ISOTOPE_CHARGE; z++) {
			for (int k = 0; k < fullBinSize - 1; k++) {
				for (int l = k + 1; l < fullBinSize; l++) {
					double massRemainder = Math.abs(Math.abs(binMasses[k] - binMasses[l]) - 
							BinnerConstants.ISOTOPE_MASS_DIFFS[z]);
					if (massRemainder < massTolerance) {
						double RTDiff = Math.abs(binRTs[k] - binRTs[l]);
						double corr = binCorrelations[k][l];
						double intensitySign = Math.signum(Math.log(binMedianIntensities[k] / binMedianIntensities[l]));
						double massDiffSign = Math.signum(binMasses[l] - binMasses[k]);
						if (RTDiff < RTWidth && corr > corrCutoff && intensitySign == massDiffSign) {				
							tagIsotopes(binIndex, binFeatureIndexes[k], binFeatureIndexes[l],
									BinnerConstants.ISOTOPE_CHARGES[z]);
						}
					}
				}
			}
		}
		
		if (analysisData.getBinwiseIsotopeGroups().get(binIndex).isEmpty()) {
			analysisData.getBinwiseCorrelationsMinusIsotopes().set(binIndex, 
					analysisData.getBinwiseCorrelations().get(binIndex).copy());
			analysisData.getBinwiseMassDiffsMinusIsotopes().set(binIndex, 
					analysisData.getBinwiseMassDiffs().get(binIndex).copy());
			analysisData.getBinwiseFeaturesForClustering().set(binIndex, 
					new ArrayList<Integer>(analysisData.getBinContents().get(binIndex)));
			return;
		}
		
		labelIsotopes(binIndex);
		
		boolean [] binClusteringRemovals = new boolean[fullBinSize];
		for (int k = 0; k < fullBinSize; k++) {
			Feature feature_k = analysisData.getNonMissingFeaturesInOriginalOrder().get(binFeatureIndexes[k]);
			binClusteringRemovals[k] = feature_k.getRemoveForClustering();
			if (!binClusteringRemovals[k]) {
				analysisData.getBinwiseFeaturesForClustering().get(binIndex).add(binFeatureIndexes[k]);
			}
		}
		
		RealMatrix correlationsForClustering = null;
		int newRow = 0;
		for (int k = 0; k < fullBinSize; k++) {
			if (binClusteringRemovals[k]) {
				continue;
			}
			List<Double> correlationsForFeatureMinusIsotopes_k = new ArrayList<Double>();
			for (int l = 0; l < binCorrelations[k].length; l++) {
				if (binClusteringRemovals[l]) {
					continue;
				}
				correlationsForFeatureMinusIsotopes_k.add(binCorrelations[k][l]);
			}
			if (correlationsForClustering == null) {
				int newSize = correlationsForFeatureMinusIsotopes_k.size();
				correlationsForClustering = new Array2DRowRealMatrix(newSize, newSize);
			}
			correlationsForClustering.setRow(newRow++, ArrayUtils.toPrimitive(
					correlationsForFeatureMinusIsotopes_k.toArray(new Double[0])));
		}
		if (correlationsForClustering == null) {
			correlationsForClustering = new Array2DRowRealMatrix();
		}
		analysisData.getBinwiseCorrelationsMinusIsotopes().set(binIndex, correlationsForClustering);
		analysisData.getBinwiseMassDiffsMinusIsotopes().set(binIndex, constructDeisotopedMassDiffMatrix(binIndex));
	}
	
	private void tagIsotopes(Integer binIndex, Integer featureIndex1, Integer featureIndex2, Integer charge) {
		Feature feature1 = analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndex1);
		Feature feature2 = analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndex2);
		int isotopeGroup1 = feature1.getIsotopeGroup();
		int isotopeGroup2 = feature2.getIsotopeGroup();
		if (isotopeGroup1 >= 0) {
			int charge1 = analysisData.getBinwiseIsotopeGroups().get(binIndex).get(isotopeGroup1).getCharge();
			if (charge1 != charge) {
				return;
			}
			if (isotopeGroup2 >= 0) {
				if (isotopeGroup1 == isotopeGroup2) {
					return;
				}
				int charge2 = analysisData.getBinwiseIsotopeGroups().get(binIndex).get(isotopeGroup2).getCharge();
				if (charge2 != charge) {
					return;
				}
				combineIsotopeGroups(binIndex, isotopeGroup1, isotopeGroup2);
			} else {
				addFeatureToIsotopeGroup(binIndex, featureIndex2, isotopeGroup1);
			}
		} else {
			if (isotopeGroup2 >= 0) {
				int charge2 = analysisData.getBinwiseIsotopeGroups().get(binIndex).get(isotopeGroup2).getCharge();
				if (charge2 != charge) {
					return;
				}
				addFeatureToIsotopeGroup(binIndex, featureIndex1, isotopeGroup2);
			} else {
				createIsotopeGroup(binIndex, featureIndex1, featureIndex2, charge);
			}
		}
	}
	
	private void combineIsotopeGroups(Integer binIndex, Integer isotopeGroup1, Integer isotopeGroup2) {		
		for (Integer featureIndex : analysisData.getBinwiseIsotopeGroups().get(binIndex).get(isotopeGroup2).
				getFeatureIndexList()) {
			addFeatureToIsotopeGroup(binIndex, featureIndex, isotopeGroup1);
		}
		analysisData.getBinwiseIsotopeGroups().get(binIndex).remove(isotopeGroup2);
	}
	
	private void addFeatureToIsotopeGroup(Integer binIndex, Integer featureIndex, Integer isotopeGroup) {	
		List<Integer> featureIndexList = analysisData.getBinwiseIsotopeGroups().get(binIndex).get(isotopeGroup).
				getFeatureIndexList();
		featureIndexList.add(featureIndex);
		analysisData.getBinwiseIsotopeGroups().get(binIndex).get(isotopeGroup).setFeatureIndexList(featureIndexList);
		analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndex).setIsotopeGroup(isotopeGroup);
	}
	
	private void createIsotopeGroup(Integer binIndex, Integer featureIndex1, Integer featureIndex2, Integer charge) {
		int newKey = getLowestAvailableKey(binIndex);
		List<Integer> featureIndexList = new ArrayList<Integer>();
		featureIndexList.add(featureIndex1);
		featureIndexList.add(featureIndex2);
		analysisData.getBinwiseIsotopeGroups().get(binIndex).put(newKey, new IsotopeGroup());
		analysisData.getBinwiseIsotopeGroups().get(binIndex).get(newKey).setFeatureIndexList(featureIndexList);
		analysisData.getBinwiseIsotopeGroups().get(binIndex).get(newKey).setCharge(charge);
		analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndex1).setIsotopeGroup(newKey);
		analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndex2).setIsotopeGroup(newKey);
	}
	
	private int getLowestAvailableKey(Integer binIndex) {
		Set<Integer> keys = analysisData.getBinwiseIsotopeGroups().get(binIndex).keySet();
		int key = 0;
		while (keys.contains(key)) {
			key++;
		}
		return key;
	}
	
	private void labelIsotopes(Integer binIndex) {
		
		for (int group : analysisData.getBinwiseIsotopeGroups().get(binIndex).keySet()) {
			
		//	List<Feature> featureList = new ArrayList<Feature>();
		//	Map<String, Integer> offsetToIndexMap = new HashMap<String, Integer>();
			
			Integer charge = analysisData.getBinwiseIsotopeGroups().get(binIndex).get(group).getCharge();
			Integer modeSign = ionizationModeWrapPanel.usePositiveMode() ? 1 : -1;
			String postscript = charge > 1 ? " (z=" + (charge * modeSign) + ")" : "";
			List<Integer> featureIndexList = analysisData.getBinwiseIsotopeGroups().get(binIndex).get(group).
					getFeatureIndexList();
			Double massMin = Double.MAX_VALUE;
			Integer featureIndexMin = 0;
			for (Integer featureIndex : featureIndexList) {
				Feature feature = analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndex);
				if (feature.getMass() < massMin) {
					massMin = feature.getMass();
					featureIndexMin = featureIndex;
				}
				//offsetToIndexMap.put(feature.getName(), featureIndex);
				//featureList.add(feature);
			}
			/*
		
			Collections.sort(featureList, new FeatureByBinClusterAndRtComparator());
			List<Integer> sortedFeatureIndexList = new ArrayList<Integer>();
			for (int i = 0; i < featureList.size(); i++) {
				Feature f = featureList.get(i);
				sortedFeatureIndexList.add(offsetToIndexMap.get(f.getName()));
				
			} */
			
			for (Integer featureIndex : featureIndexList) {
				Feature feature = analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndex);
				if (featureIndex.equals(featureIndexMin)) {
					feature.setIsotope("[i" + isotopeLabelNumber + "]" + postscript);
					analysisData.getBinwiseIsotopeGroups().get(binIndex).get(group).setBaseFeatureIndex(featureIndex);
				} else {
					int massDiff = (int) (Math.round(charge * Math.abs(feature.getMass() - massMin)));
					if (massDiff == 0) {
						feature.setIsotope("[i" + isotopeLabelNumber + "] (duplicate)" + postscript);
					} else {
						feature.setIsotope("[i" + isotopeLabelNumber + " + " + massDiff + "]" + postscript);
						feature.setRemoveForClustering(true);
						analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndexMin).
								getIsotopeGroupMembers().add(massDiff);
						isotopesFoundCount++;
					}
				}
			}
			Collections.sort(analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndexMin).
					getIsotopeGroupMembers());
			isotopeLabelNumber++;
		}
	}
	
	private void putIsotopesBack(int binIndex) {
		
		List<Integer> originalClusterIndicesForIsotopes = new ArrayList<Integer>();
		List<Integer> rebinClusterIndicesForIsotopes = new ArrayList<Integer>();
		List<Integer> reclusterClusterIndicesForIsotopes = new ArrayList<Integer>();
		
		if (analysisData.getBinwiseIsotopeGroups().get(binIndex).isEmpty()) {
			analysisData.setIndexedClustersForOutput(analysisData.getIndexedClustersForAnnotation());
			return;
		}
			
		for (Integer isotopeGroup : analysisData.getBinwiseIsotopeGroups().get(binIndex).keySet()) {
			Integer baseFeatureIndex = analysisData.getBinwiseIsotopeGroups().get(binIndex).get(isotopeGroup).
					getBaseFeatureIndex();
			Feature baseFeature = analysisData.getNonMissingFeaturesInOriginalOrder().get(baseFeatureIndex);
			Integer baseOldCluster = baseFeature.getOldCluster();
			Integer baseNewCluster = baseFeature.getNewCluster();
			Integer baseNewNewCluster = baseFeature.getNewNewCluster();
		
			for (Integer featureIndex : analysisData.getBinwiseIsotopeGroups().get(binIndex).get(isotopeGroup).
					getFeatureIndexList()) {
				Feature feature = analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndex);
				if (!feature.getRemoveForClustering()) {
					continue;
				}
				analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndex).setOldCluster(baseOldCluster);
				analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndex).setNewCluster(baseNewCluster);
				analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndex).setNewNewCluster(baseNewNewCluster);
				originalClusterIndicesForIsotopes.add(baseOldCluster);
				rebinClusterIndicesForIsotopes.add(baseNewCluster);
				reclusterClusterIndicesForIsotopes.add(baseNewNewCluster);
			}
		}
		
		int fullBinSize = analysisData.getBinContents().get(binIndex).size();
		Integer [] oldClusterData = new Integer[fullBinSize];
		Integer [] newClusterData = new Integer[fullBinSize];
		for (int k = 0; k < fullBinSize; k++) {
			Feature feature = BinnerIndexUtils.getFeatureFromRTSortedFullBin(binIndex, k);
			oldClusterData[k] = feature.getOldCluster();
			// Updated here
			if (rebinOptionsWrapPanel.rebinIsSelected()) 
				newClusterData[k] = feature.getNewCluster();
			else
				newClusterData[k] = feature.getNewNewCluster();
		}
		
		binStats.get(binIndex).setOriginalClusterIndicesForIsotopes(originalClusterIndicesForIsotopes);
		binStats.get(binIndex).setRebinClusterIndicesForIsotopes(rebinClusterIndicesForIsotopes);
		binStats.get(binIndex).setReclusterClusterIndicesForIsotopes(reclusterClusterIndicesForIsotopes);
		
		analysisData.setIndexedClustersForOutput(ListUtils.sortedList(newClusterData));
	}

	
	private void annotateBin(int binIndex) {
		
		limitedComboAdducts = new HashMap<String, AnnotationInfo>();
		Integer modeSign = ionizationModeWrapPanel.usePositiveMode() ? 1 : -1;
		
		for (AnnotationInfo info : annotationsUpToCharge.get(modeSign * BinnerConstants.MAX_ISOTOPE_CHARGE))
			if (annotationPreferencesWrapPanel.getPreferencesFor(info.getAnnotation()).getRequireAloneBeforeCombined())
				limitedComboAdducts.put(info.getAnnotation(), info);
	
		List<ClusterForAnnotation> clustersForAnnotation = getClusterBoundaries(binIndex);
		
		Boolean moreFeaturesToAnnotate = true;
		for (ClusterForAnnotation cluster : clustersForAnnotation) {
			collectAdditionalFeatureInfoForCluster(binIndex, cluster);
			Boolean firstPass = true;
			while (moreFeaturesToAnnotate) {
				Integer indexOfMostIntense = getMostIntenseNonAnnotatedFeatureIndex(cluster);
				if (indexOfMostIntense < 0) {
					break;
				}
				if (firstPass) {
					cluster.getFeatureInfo().get(indexOfMostIntense).markAsMostIntenseOfCluster(true);
					firstPass = false;
				}
				AnnotationBaseInfo annotationBase = determineBestAnnotationBase(cluster, indexOfMostIntense, limitedComboAdducts);
				if (annotationBase == null) {
					break;
				}
				annotateCluster(binIndex, cluster, annotationBase);
			}
		}
	}
	
	private Map<Integer, AnnotationInfo> createAnnotFileMap(Boolean flagLoadErrors, 
			Boolean checkForTierOneOnMode) throws BinnerException {
		
		Map<Integer, AnnotationInfo> annotationMap = annotationFileWrapPanel.readFileForMap(
				inputFiles.get(BinnerConstants.ANNOTATION), flagLoadErrors);
		
		Integer modeSign = ionizationModeWrapPanel.usePositiveMode() ? 1 : -1;
		if (flagLoadErrors && checkForTierOneOnMode)  {
			
			Boolean haveUnmatchedChg2 = annotationFileWrapPanel.foundCarrierForCharge(modeSign * 2) && 
					!annotationFileWrapPanel.foundTierOneForCharge(modeSign * 2);
			Boolean haveUnmatchedChg3 = annotationFileWrapPanel.foundCarrierForCharge(modeSign * 3) && 
					!annotationFileWrapPanel.foundTierOneForCharge(modeSign * 3);
			
			String warnMsg = "", errMsg = "";		
			
			String modeDesc = ionizationModeWrapPanel.usePositiveMode() ? "positive" : "negative";
			// Throw exception here without clearing annotation file settings -- likely a mode selection error.
			if (!annotationFileWrapPanel.hasChargeCarriersForMode(ionizationModeWrapPanel.usePositiveMode()))
				throw new BinnerException("To run a " + modeDesc + " mode analysis at least one " + 
						modeDesc + " mode charge carrier must be specified in annotation file.");
		
			else if (annotationFileWrapPanel.foundTierOneForCharge(modeSign))  {
				if (haveUnmatchedChg2)
					warnMsg += "Because there are no tier 1 charge carriers with charge "+ (modeSign * 2) + 
							", all features with putative charge of "+ (modeSign * 2) + 
								" will be ignored by the annotation engine ";
				noTierOneChg2Carrier = haveUnmatchedChg2;
				
				if (haveUnmatchedChg2 && haveUnmatchedChg3)
					warnMsg += BinnerConstants.LINE_SEPARATOR;
				
				if (haveUnmatchedChg3)
					warnMsg += "Because there are no tier 1 charge carriers with charge " + (modeSign * 3) + 
							", all features with putative charge of " + (modeSign * 3) + 
								" will be ignored by the annotation engine ";
				noTierOneChg3Carrier = haveUnmatchedChg3;
			}
			else if (annotationFileWrapPanel.foundCarrierForCharge(modeSign)) {  //not just multiply charged
				errMsg += "If any charge carriers have charge " + modeSign + " at least one must be tier one.";
			}  
			else { // only charge 2 or charge 3 carriers
				 if (haveUnmatchedChg2)
					errMsg += "If any charge carriers have charge " + (modeSign * 2) + 
							" at least one tier one carrier with charge "+ (modeSign * 1) + " or " + 
								(modeSign * 2) + " must exist.";
			     if (haveUnmatchedChg2 && haveUnmatchedChg3)
			    	 errMsg += BinnerConstants.LINE_SEPARATOR;
				 if (haveUnmatchedChg3)	
					errMsg += "If any charge carriers have charge " + (modeSign * 3) + 
							" at least one tier one carrier with charge " + (modeSign * 1) + " or " + 
								(modeSign * 3) + " must exist.";
			}
			   
			if (!StringUtils.isEmptyOrNull(warnMsg))
				JOptionPane.showMessageDialog(null, warnMsg);
			
			if (!StringUtils.isEmptyOrNull(errMsg)) {
				annotationMap = null;
				annotationFileWrapPanel.clearAnnotFileHeadersOnError(null);
				throw new BinnerException(errMsg);
			}	
		}
		
		Double upperMassLimit = -Double.MAX_VALUE;
		
		if (outputSelectionWrapPanel.getHistogramMax() <= BinnerConstants.DEFAULT_MAX_MASS_DIFF) {
			Double round = Math.round(upperMassLimit/10.0) * 10.0;
			upperMassLimit = round;	
			outputSelectionWrapPanel.setHistogramMax(Math.max(BinnerConstants.DENSE_MASS_DIFF_THRESHOLD, upperMassLimit));
			if (outputSelectionWrapPanel.getHistogramMax() > BinnerConstants.LARGEST_MASS_DIFF)
				outputSelectionWrapPanel.setHistogramMax(BinnerConstants.LARGEST_MASS_DIFF);
		}
		else {
			Double selectedMax = outputSelectionWrapPanel.getHistogramMax();
			Double round = Math.round(selectedMax/10.0) * 10.0;
			upperMassLimit = round; 
			outputSelectionWrapPanel.setHistogramMax(upperMassLimit);
		}

	   if (annotationMap != null) {
		   for (AnnotationInfo entry : annotationMap.values()) {
			   if (entry.getMass() != null)
				   	upperMassLimit = Math.max(upperMassLimit, Math.abs(entry.getMass()));
		   }
	   }
	   return annotationMap;
	}
	
	private Map<Integer, List<AnnotationInfo>> createAnnotByChargeMap() {
		Map<Integer, List<AnnotationInfo>> annotationByChargeMap = new HashMap<Integer, List<AnnotationInfo>>();
		for (Integer charge : BinnerConstants.ANNOTATION_CHARGES) {
			annotationByChargeMap.put(charge, new ArrayList<AnnotationInfo>());
		}
		
		boolean entryFound = false;
		for (AnnotationInfo annotation : annotFileMap.values()) {
			Integer charge = null;
			try {
				charge = Integer.parseInt(annotation.getCharge());
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
			if (charge == null || !ArrayUtils.contains(BinnerConstants.ANNOTATION_CHARGES, charge)) {
				continue;
			}
			entryFound = true;
			List<AnnotationInfo> updatedList = annotationByChargeMap.get(charge);
			updatedList.add(annotation);
			annotationByChargeMap.put(charge, updatedList);
		}
		
		if (!entryFound) {
			JOptionPane.showMessageDialog(null, "Error: At least one row in the annotation file must have" +
					" an entry of +/- 1, 2 or 3 in the charge column.       ");
			return null;
		}
		
		// Dummy in an entry corresponding to M + charged adduct (no neutral entry) case
		AnnotationInfo zeroMassEntry = new AnnotationInfo();
		zeroMassEntry.setAnnotation("I am not a real annotation!");
		zeroMassEntry.setCharge("0");
		zeroMassEntry.setMass(0.0);
		zeroMassEntry.setMode("BOTH");
		annotationByChargeMap.get(0).add(0, zeroMassEntry);
		
		return annotationByChargeMap;
	}
	
	private Map<Integer, List<AnnotationInfo>> createAnnotUpToChargeMap() {
		Map<Integer, List<AnnotationInfo>> annotationUpToChargeMap = new HashMap<Integer, List<AnnotationInfo>>();
		Integer maxPositiveCharge = -1;
		Integer minNegativeCharge = 1;
		for (Integer charge : BinnerConstants.ANNOTATION_CHARGES) {
			annotationUpToChargeMap.put(charge, new ArrayList<AnnotationInfo>());
			if (charge > maxPositiveCharge) {
				maxPositiveCharge = charge;
			}
			if (charge < minNegativeCharge) {
				minNegativeCharge = charge;
			}
		}
		
		for (Integer charge : annotationsByCharge.keySet()) {
			if (charge > 0) {
				for (int i = charge; i <= maxPositiveCharge; i++) {
					List<AnnotationInfo> updatedList = annotationUpToChargeMap.get(i);
					updatedList.addAll(annotationsByCharge.get(charge));
					annotationUpToChargeMap.put(i, updatedList);
				}
			} else if (charge < 0) {
				for (int i = charge; i >= minNegativeCharge; i--) {
					List<AnnotationInfo> updatedList = annotationUpToChargeMap.get(i);
					updatedList.addAll(annotationsByCharge.get(charge));
					annotationUpToChargeMap.put(i, updatedList);
				}
			}
		}
		return annotationUpToChargeMap;
	}
	
	private List<ClusterForAnnotation> getClusterBoundaries(int binIndex) {
		List<ClusterForAnnotation> clustersForAnnotation = new ArrayList<ClusterForAnnotation>();
		int binSizeForAnnotation = analysisData.getBinwiseCorrelationsMinusIsotopes().get(binIndex).getRowDimension();

		Integer curCluster = -1;
		for (int k = 0; k < binSizeForAnnotation; k++) {
			if (!analysisData.getIndexedClustersForAnnotation().get(k).getValue().equals(curCluster + 1)) {
				ClusterForAnnotation clusterForAnnotation = new ClusterForAnnotation();
				clustersForAnnotation.add(clusterForAnnotation);
				clusterForAnnotation.setFirstIndexWithinBin(k);
				curCluster++;
				if (curCluster > 0) {
					clustersForAnnotation.get(curCluster - 1).setLastIndexWithinBin(k - 1);
				}
			}
		}
		clustersForAnnotation.get(curCluster).setLastIndexWithinBin(binSizeForAnnotation - 1);

		return clustersForAnnotation;
	}
	
	private void collectAdditionalFeatureInfoForCluster(int binIndex, ClusterForAnnotation cluster) {
		for (int k = cluster.getFirstIndexWithinBin(); k <= cluster.getLastIndexWithinBin(); k++) {
			int featureIndex = BinnerIndexUtils.getFeatureIndexFromClusterSortedDeisotopedBin(binIndex, k);
			Feature feature = analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndex);
			FeatureInfoForAnnotation featureInfo = new FeatureInfoForAnnotation();		
			featureInfo.setMass(feature.getMass());
			featureInfo.setRt(feature.getRT());
			featureInfo.setMedianIntensity(feature.getMedianIntensity());
			Integer charge = 0;
			if (feature.getIsotopeGroup() >= 0) {
				charge = analysisData.getBinwiseIsotopeGroups().get(binIndex).get(feature.getIsotopeGroup()).getCharge();
			}
			featureInfo.setChargeBasedOnIsotope(charge);
			cluster.getFeatureInfo().add(featureInfo);
		}
	}
	
	private Integer getMostIntenseNonAnnotatedFeatureIndex(ClusterForAnnotation cluster) {
		int i = 0, indexOfMostIntense = -1;
		Double highestMedianIntensity = -1.0;
		for (int k = cluster.getFirstIndexWithinBin(); k <= cluster.getLastIndexWithinBin(); k++, i++) {
			if (cluster.getFeatureInfo().get(i).isAnnotated()) {
				continue;
			}
			if (cluster.getFeatureInfo().get(i).getMedianIntensity() > highestMedianIntensity) {
				highestMedianIntensity = cluster.getFeatureInfo().get(i).getMedianIntensity();
				indexOfMostIntense = i;
			}
		}
		return indexOfMostIntense;
	}
	
	private AnnotationBaseInfo determineBestAnnotationBase(ClusterForAnnotation cluster, Integer indexOfMostIntense, Map<String, AnnotationInfo> limitedComboAdducts) {		
		AnnotationBaseInfo bestAnnotationBase = new AnnotationBaseInfo();
		Integer highestAnnotationCount = -1;
		
		Integer chargeBasedOnIsotope = cluster.getFeatureInfo().get(indexOfMostIntense).getChargeBasedOnIsotope();
	 	Integer modeSign = ionizationModeWrapPanel.usePositiveMode() ? 1 : -1;
		Integer annotationBaseCharge = (chargeBasedOnIsotope == 0 ? 1 : chargeBasedOnIsotope) * modeSign;
		
		Boolean skipAnnotation = false;
		if (noTierOneChg2Carrier && chargeBasedOnIsotope.equals(2))
			skipAnnotation = true;
		else if (noTierOneChg3Carrier && chargeBasedOnIsotope.equals(3)) 
			skipAnnotation = true;
		
		List<AnnotationInfo> allBaseAdductsForCharge = annotationsByCharge.get(annotationBaseCharge);
		if (skipAnnotation || allBaseAdductsForCharge == null || allBaseAdductsForCharge.isEmpty()) {
			bestAnnotationBase.setReferenceMass(0.0);
			bestAnnotationBase.setMassMultiple(0);
			bestAnnotationBase.setCharge(annotationBaseCharge);
			bestAnnotationBase.setBestAdductMass(0.0);
			bestAnnotationBase.setBestAdductAnnotation	("No annotations available (z=" + annotationBaseCharge + ")");
			bestAnnotationBase.setBestAdductDerivation("");
			bestAnnotationBase.setBestAdductAnnotationCount(highestAnnotationCount);
			bestAnnotationBase.setIndexWithinCluster(indexOfMostIntense);
			return bestAnnotationBase;
		}
		
		bestAnnotationBase.setCharge(annotationBaseCharge);
		bestAnnotationBase.setIndexWithinCluster(indexOfMostIntense);
		Integer annotationCount = null;
	
		Double featureMass = cluster.getFeatureInfo().get(indexOfMostIntense).getMass();
		
		for (int massMultiple = 1; massMultiple <= 3; massMultiple++) {
			for (AnnotationInfo baseAdduct : allBaseAdductsForCharge) 
				{
				ChargeCarrierPreferences prefs = annotationPreferencesWrapPanel.getPreferencesFor(baseAdduct.getAnnotation());
	
				if (prefs == null) continue;
				if (!prefs.getAllowAsBase()) continue;
				if (massMultiple > 1 && !prefs.getAllowAsMultimerBase()) continue;
		
				annotationCount = getAnnotationCountForAdduct(cluster, indexOfMostIntense, baseAdduct, massMultiple, prefs, limitedComboAdducts);
				if (annotationCount == null) {
					continue;
				}
				if (annotationCount > highestAnnotationCount) {
					highestAnnotationCount = annotationCount;
					bestAnnotationBase.setMassMultiple(massMultiple);
					bestAnnotationBase.setBestAdductAnnotation(baseAdduct.getAnnotation());
					bestAnnotationBase.setBestAdductMass(baseAdduct.getMass());
					bestAnnotationBase.setReferenceMass(AnnotationUtils.getReferenceMass(featureMass, baseAdduct,  massMultiple));
				}
			}
		}
		
		bestAnnotationBase.setBestAdductAnnotationCount(highestAnnotationCount);
		if (highestAnnotationCount == -1) {
			bestAnnotationBase.setBestAdductAnnotation("[M]");
			bestAnnotationBase.setBestAdductDerivation("");
		}
		return bestAnnotationBase;
	}
		
	private Integer getAnnotationCountForAdduct(ClusterForAnnotation cluster, Integer indexOfMostIntense,
			AnnotationInfo baseAdduct, Integer massMultiple, ChargeCarrierPreferences prefs, 
			Map<String, AnnotationInfo> limitedComboAdducts) {
		
		Double featureMassOfMostIntense = cluster.getFeatureInfo().get(indexOfMostIntense).getMass();
		Double rtOfMostIntense = cluster.getFeatureInfo().get(indexOfMostIntense).getRt();
		Double referenceMassOfMostIntense = AnnotationUtils.getReferenceMass(featureMassOfMostIntense, baseAdduct,massMultiple);
		if (referenceMassOfMostIntense == null) {
			return 0;
		}
		
		int count = 0;
		Double massTolerance = BinnerNumUtils.toDouble(annotationParametersWrapPanel.getAdductNLMassTol());
		Double rtTolerance = BinnerNumUtils.toDouble(annotationParametersWrapPanel.getRtTol());
		Integer modeSign = ionizationModeWrapPanel.usePositiveMode() ? 1 : -1;
		Integer baseCharge = AnnotationUtils.getAdductCharge(baseAdduct);
	
		Map<String, String> adductsWithoutSingletonForReferenceMass = new HashMap<String, String>();
		if (annotationPreferencesWrapPanel.useNeutralMassesToGetBestChargeCarrier()) 
			adductsWithoutSingletonForReferenceMass = grabAdductsWithoutSingletonAmongThoseLimited(baseCharge, modeSign, rtTolerance, 
					rtOfMostIntense, indexOfMostIntense, referenceMassOfMostIntense, cluster, limitedComboAdducts);	

		for (int i = 0, k = cluster.getFirstIndexWithinBin(); k <= cluster.getLastIndexWithinBin(); i++, k++) {
			if (cluster.getFeatureInfo().get(i).isAnnotated()) {
				continue;
			}
			if (i == indexOfMostIntense) {
				count++;
				continue;
			}
			
			if (Math.abs(cluster.getFeatureInfo().get(i).getRt() - rtOfMostIntense) > rtTolerance) {
				continue;
			}			
			
			Integer chargeBasedOnIsotope = cluster.getFeatureInfo().get(i).getChargeBasedOnIsotope();
			Integer annotationCharge = (chargeBasedOnIsotope == 0 ? 1 : chargeBasedOnIsotope) * modeSign;
			
			List<AnnotationInfo> allAdductsForCharge = null;		
			if (Math.abs(baseCharge) > 1 && chargeBasedOnIsotope == 0) {
				allAdductsForCharge = annotationsUpToCharge.get(baseCharge);
			}  
			else if  (annotationPreferencesWrapPanel.allowChargeToVary()) { 
				allAdductsForCharge = annotationsUpToCharge.get(modeSign *  BinnerConstants.MAX_ISOTOPE_CHARGE);  
			} 
			else {
				allAdductsForCharge = annotationsByCharge.get(annotationCharge);
			}
			if (allAdductsForCharge == null) {
				return null;
			}

			Double featureMass = cluster.getFeatureInfo().get(i).getMass();
			Boolean foundAnnotationHit = false;
		
			List<AnnotationInfo> allNeutralEntries = null;
			if (annotationPreferencesWrapPanel.useNeutralMassesToGetBestChargeCarrier()) {
				allNeutralEntries = annotationsByCharge.get(0);
			} else {
				allNeutralEntries = Arrays.asList(annotationsByCharge.get(0).get(0));
			}
			for (AnnotationInfo neutralEntry : allNeutralEntries) {
				Double neutralMass = neutralEntry.getMass();
				for (int multiple = 1; multiple <= 3; multiple++) {
					if (foundAnnotationHit) {	
						break;
					}	
					for (AnnotationInfo adduct : allAdductsForCharge) {
						if (foundAnnotationHit) 
							break;
						
						if (neutralMass > 0 && adductsWithoutSingletonForReferenceMass.containsKey(adduct.getAnnotation()))
							continue;
						
						Double adductMass = adduct.getMass();
						Integer adductCharge = AnnotationUtils.getAdductCharge(adduct);
						Double targetMass = (multiple * referenceMassOfMostIntense + adductMass + neutralMass) / Math.abs(adductCharge);
						if (Math.abs(featureMass - targetMass) < massTolerance) {
							count++;
							foundAnnotationHit = true;
							break;
						}
					}
				}
			}			
		}
		return count;
	}
	
	public Map<String, String> grabAdductsWithoutSingletonAmongThoseLimited(double baseCharge, int modeSign, double rtTolerance, 
		double rtOfMostIntense, int indexOfMostIntense, Double referenceMassOfMostIntense, 
		ClusterForAnnotation cluster, Map<String, AnnotationInfo> limitedComboAdducts) {
		Double massTolerance = BinnerNumUtils.toDouble(annotationParametersWrapPanel.getAdductNLMassTol());		
		
		Map<String, String> adductsWithoutSingleton = new HashMap<String, String>();
		for (String adductName : limitedComboAdducts.keySet())
			adductsWithoutSingleton.put(adductName, null);
		
		for (int i = 0, k = cluster.getFirstIndexWithinBin(); k <= cluster.getLastIndexWithinBin(); i++, k++)  {
		    if (adductsWithoutSingleton == null || adductsWithoutSingleton.size() < 1)
		    	break;
		    
		    if (cluster.getFeatureInfo().get(i).isAnnotated()) 
		    	continue;				
			
			if (i == indexOfMostIntense) 
				continue;
			
			if (Math.abs(cluster.getFeatureInfo().get(i).getRt() - rtOfMostIntense) > rtTolerance) 
				continue;
			
			Integer chargeBasedOnIsotope = cluster.getFeatureInfo().get(i).getChargeBasedOnIsotope();
			Integer annotationCharge = (chargeBasedOnIsotope == 0 ? 1 : chargeBasedOnIsotope) * modeSign;
			
			List<AnnotationInfo> allAdductsForCharge = annotationsByCharge.get(annotationCharge);	
			if (Math.abs(baseCharge) > 1 && chargeBasedOnIsotope == 0) 
				allAdductsForCharge = annotationsUpToCharge.get(baseCharge);
			else if (annotationPreferencesWrapPanel.allowChargeToVary())
				allAdductsForCharge = annotationsUpToCharge.get(modeSign *  BinnerConstants.MAX_ISOTOPE_CHARGE);
		
			if (allAdductsForCharge == null)
				continue;
			
			Double featureMass = cluster.getFeatureInfo().get(i).getMass();
		    for (AnnotationInfo info : allAdductsForCharge) {
		    	if (adductsWithoutSingleton == null || adductsWithoutSingleton.size() < 1)
		    		break;
		    	
		    	Double adductMass = info.getMass();
	    		Integer adductCharge = AnnotationUtils.getAdductCharge(info);
	    		for (int multiple = 1; multiple <= 3; multiple++)  {
		    	
	    			if (adductsWithoutSingleton == null || adductsWithoutSingleton.size() < 1)
			    		break;
			    
	    			Double targetMass = (multiple * referenceMassOfMostIntense + adductMass) / Math.abs(adductCharge);
			    	if (Math.abs(featureMass - targetMass) < massTolerance)  {
			    		adductsWithoutSingleton.remove(info.getAnnotation());
			    		break;
					}
				}
		    }		
		}
		return adductsWithoutSingleton;
	}
		
	private void annotateCluster(int binIndex, ClusterForAnnotation cluster, AnnotationBaseInfo annotationBase) { //Map<String, AnnotationInfo> limitedComboAdducts)  {
		
		Integer baseFeatureIndex = annotationBase.getIndexWithinCluster();
		Double baseReferenceMass = annotationBase.getReferenceMass();
		String baseReferenceMassString = String.format("%.4f", baseReferenceMass);
		
		String baseAdductAnnotation = annotationBase.getBestAdductAnnotation();
		Double baseAdductMass = annotationBase.getBestAdductMass();
		Integer baseMassMultiple = annotationBase.getMassMultiple();
		
		Integer baseCharge = annotationBase.getCharge();
		String baseChargeAnnotation = (Math.abs(baseCharge) > 1 ? " (z=" + baseCharge + ")" : "");
		String baseChargeDerivation = (Math.abs(baseCharge) > 1 ? ") / " + Math.abs(baseCharge) : "");
		
		//String singletonAnnotation = "";
		//String singletonDerivation = "";
		String baseAnnotation = "[" + (baseMassMultiple > 1 ? baseMassMultiple : "") + "M" + 
				annotationLabelNumber + (baseAdductMass >= 0 ? " + " : " - ") + baseAdductAnnotation + "]" + 
				baseChargeAnnotation;
		String baseDerivation = "[" + (Math.abs(baseCharge) > 1 ? "(" : "") + (baseMassMultiple > 1 ? 
				baseMassMultiple + "(": "") + baseReferenceMassString + (baseMassMultiple > 1 ? ")" : "") + 
				(baseAdductMass >= 0 ? " + " : " - ") + Math.abs(baseAdductMass) + baseChargeDerivation + "]";
	
		Double baseFeatureMass = cluster.getFeatureInfo().get(baseFeatureIndex).getMass();
		Double baseRt = cluster.getFeatureInfo().get(baseFeatureIndex).getRt();
		Double massTolerance = BinnerNumUtils.toDouble(annotationParametersWrapPanel.getAdductNLMassTol());
		Double rtTolerance = BinnerNumUtils.toDouble(annotationParametersWrapPanel.getRtTol());
		Integer modeSign = ionizationModeWrapPanel.usePositiveMode() ? 1 : -1;
		Boolean isSingleton = true;
	
		Map<String, String> limitedAdductsWithoutSingletonForReferenceMass = new HashMap<String, String>();
		if (annotationPreferencesWrapPanel.useNeutralMassesToGetBestChargeCarrier()) 
			limitedAdductsWithoutSingletonForReferenceMass = grabAdductsWithoutSingletonAmongThoseLimited(baseCharge, modeSign, rtTolerance, 
					baseRt, baseFeatureIndex, baseReferenceMass, cluster, limitedComboAdducts);	

		//if (baseAnnotationCount == 1) {
		//	int featureIndex = binwiseFeaturesForClustering.get(bin - 1).get(indexedClustersForAnnotation.get(
		//			cluster.getFirstIndexWithinBin() + baseFeatureIndex).getIndex());
		//	Feature feature = analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndex);
		//	feature.setAdductOrNL(singletonAnnotation);
		//	feature.setDerivation(singletonDerivation);
		//	cluster.getFeatureInfo().get(baseFeatureIndex).markAsAnnotated();
		//	return;
		//}
		
		if (baseMassMultiple == 0) {
			// special case: multiple charge based on isotopes but no annotations with this charge in annotation file
			int featureIndex = BinnerIndexUtils.getFeatureIndexFromClusterSortedDeisotopedBin(binIndex, 
					cluster.getFirstIndexWithinBin() + baseFeatureIndex);
			Feature feature = analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndex);
			feature.setAdductOrNL(baseAdductAnnotation);
			feature.setMolecularIonNumber("");
			feature.setChargeCarrier("");
			feature.setNeutralAnnotation("");
			feature.setReferenceMassString("");
			feature.setDerivation("");	
			cluster.getFeatureInfo().get(baseFeatureIndex).markAsAnnotated();
			return;
		}	
		
		for (int i = 0, k = cluster.getFirstIndexWithinBin(); k <= cluster.getLastIndexWithinBin(); i++, k++) {
			if (cluster.getFeatureInfo().get(i).isAnnotated()) {
				continue;
			}
			int featureIndex = BinnerIndexUtils.getFeatureIndexFromClusterSortedDeisotopedBin(binIndex, k);
			Feature feature = analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndex);
			
			if (Math.abs(feature.getRT() - baseRt) > rtTolerance) { 
				continue;
			}
			
			StringBuilder annotationString = new StringBuilder("");
			StringBuilder derivationString = new StringBuilder("");
			Double featureMass = feature.getMass();
			Double massDiff = featureMass - baseFeatureMass;
			Integer chargeBasedOnIsotope = cluster.getFeatureInfo().get(i).getChargeBasedOnIsotope();
			Integer annotationCharge = (chargeBasedOnIsotope == 0 ? 1 : chargeBasedOnIsotope) * modeSign;
			
			if (i == baseFeatureIndex) {
				annotationString.append(baseAnnotation);
				derivationString.append(baseDerivation);
				feature.markAsPutativeMolecularIon(true);
				feature.setMolecularIonNumber(annotationLabelNumber.toString());
				feature.setChargeCarrier(baseAdductAnnotation);
				feature.setMassError(massDiff);
				feature.setNeutralAnnotation("");
				feature.setReferenceMassString(baseReferenceMassString);
			} else if (Math.abs(massDiff) < massTolerance) {
				annotationString.append(baseAnnotation + " (duplicate)");
				derivationString.append(baseDerivation);
				isSingleton = false;
				feature.setMolecularIonNumber(annotationLabelNumber.toString());
				feature.setChargeCarrier(baseAdductAnnotation);
				feature.setMassError(massDiff);
				feature.setNeutralAnnotation("");
				feature.setReferenceMassString(baseReferenceMassString);
				annotationsFoundCount++;
			} 
			else {		
				List<AnnotationInfo> allAdductsForCharge = null;		
				if (Math.abs(baseCharge) > 1 && chargeBasedOnIsotope == 0) {
					allAdductsForCharge = annotationsUpToCharge.get(baseCharge);
				} 
				else if (annotationPreferencesWrapPanel.allowChargeToVary()) {
					allAdductsForCharge = annotationsUpToCharge.get(modeSign * BinnerConstants.MAX_ISOTOPE_CHARGE);
				}
				else {
					allAdductsForCharge = annotationsByCharge.get(annotationCharge);
				}
				
				Boolean foundAdductHit = false;
				Boolean foundMassDiffHit = false;
				List<AnnotationInfo> allNeutralEntries = annotationsByCharge.get(0);
				
				for (AnnotationInfo neutralEntry : allNeutralEntries)  {
					Double neutralAnnotMass = neutralEntry.getMass();
					for (int multiple = 1; multiple <= 3; multiple++)  {
						if (foundAdductHit || foundMassDiffHit) {
							break;
						}
						for (AnnotationInfo adduct : allAdductsForCharge)  {
							if (neutralAnnotMass > 0 && limitedAdductsWithoutSingletonForReferenceMass.containsKey(adduct.getAnnotation()))
								continue;
							
							Double adductMass = adduct.getMass();
							Integer adductCharge = AnnotationUtils.getAdductCharge(adduct);
							Double targetMass = (multiple * baseReferenceMass + adductMass + neutralAnnotMass) / 
									Math.abs(adductCharge);
							
							if (Math.abs(featureMass - targetMass) < massTolerance) {
								
								String chargeAnnotation = (Math.abs(adductCharge) > 1 ? " (z=" + adductCharge + ")" : "");
								String preAnnotation = " + ";
								String postAnnotation = "]";
								if (neutralAnnotMass < 0.0) {
									if (hasMultipleTerms(neutralEntry.getAnnotation())) {
										preAnnotation = " - (";
										postAnnotation = ")]";
									} 
									else {
										preAnnotation = " - ";
										postAnnotation = "]";
									}
								}
								String neutralAnnotation = (neutralAnnotMass == 0.0 ? "]" : 
									preAnnotation + neutralEntry.getAnnotation() + postAnnotation);
								String annotation = "[" + (multiple > 1 ? multiple : "") + "M" + annotationLabelNumber + 
										(adductMass >= 0 ? " + " : " - ") + adduct.getAnnotation() + neutralAnnotation + 
										chargeAnnotation;
								String derivation = "[" + (Math.abs(adductCharge) > 1 ? "(" : "") + 
										(multiple > 1 ? multiple + "(" : "") + baseReferenceMassString + 
										(multiple > 1 ? ")" : "") + (adductMass >= 0 ? " + " : " - ") + 
										Math.abs(adduct.getMass()) + (neutralAnnotMass == 0 ? "" : 
										(neutralAnnotMass > 0 ? " + " : " - ") + Math.abs(neutralAnnotMass)) + 
										(Math.abs(adductCharge) > 1 ? ") / " + Math.abs(adductCharge) : "") + "]";
						
								annotationString.append(annotation);
								derivationString.append(derivation);
								feature.setMolecularIonNumber(annotationLabelNumber.toString());
								feature.setChargeCarrier(adduct.getAnnotation());
								String trim = neutralAnnotation == null ? null : neutralAnnotation.trim();
								String trim2 = "";
								if (trim != null && trim.endsWith("]"))
									trim2 = trim.length() <= 1 ? "" : trim.substring(0, trim.length() - 1);
								
								feature.setNeutralAnnotation(trim2);
								feature.setReferenceMassString(baseReferenceMassString);
								feature.setMassError(Math.abs(featureMass - targetMass));
								isSingleton = false;
								annotationsFoundCount++;
								if (neutralAnnotMass == 0.0)  {
									foundAdductHit = true;
									break;
								} 
								else {
									foundMassDiffHit = true;
								}
							}
						}
					}
				}
			}
			feature.setAdductOrNL(annotationString.toString());
			feature.setDerivation(derivationString.toString());
			if (!annotationString.toString().isEmpty()) {
				cluster.getFeatureInfo().get(i).markAsAnnotated();
			}
		}
		
		int featureIndex = BinnerIndexUtils.getFeatureIndexFromClusterSortedDeisotopedBin(binIndex, 
				cluster.getFirstIndexWithinBin() + baseFeatureIndex);
		Feature feature = analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndex);
		if (isSingleton) {
			feature.setAdductOrNL("");
			feature.setDerivation("");
			feature.setMolecularIonNumber("");
			feature.setChargeCarrier("");
			feature.setNeutralAnnotation("");
			feature.setReferenceMassString("");
			feature.setMassError(null);
			feature.markAsPutativeMolecularIon(false);
		} else {
			if (feature.isPutativeMolecularIon()) {
				for (int i = 0, k = cluster.getFirstIndexWithinBin(); k <= cluster.getLastIndexWithinBin(); i++, k++) {
					if (!cluster.getFeatureInfo().get(i).isAnnotated()) {
						continue;
					}
					int subFeatureIndex = BinnerIndexUtils.getFeatureIndexFromClusterSortedDeisotopedBin(binIndex, k);
					if (subFeatureIndex == featureIndex) {
						continue;
					}
					Feature subFeature = analysisData.getNonMissingFeaturesInOriginalOrder().get(subFeatureIndex);
					if (subFeature.getAdductOrNL() != null && 
							(subFeature.getAdductOrNL().startsWith("[M" + annotationLabelNumber.toString()) ||
								subFeature.getAdductOrNL().startsWith("[2M" + annotationLabelNumber.toString()) ||
								subFeature.getAdductOrNL().startsWith("[3M" + annotationLabelNumber.toString()))) {
						feature.getAnnotationGroupMembers().add(subFeature.getAdductOrNL());
					}
				}	
			}
			annotationLabelNumber++;
		}
	}

	private boolean hasMultipleTerms(String str) {
		return (str.contains("+") || str.contains("-"));
	}
		
	private Double massDefectFromParentMass(Double parentMass) {
		Double closestMass = Math.round((parentMass - BinnerConstants.HC_BASE) / BinnerConstants.HC_STEP) * 
				BinnerConstants.HC_STEP + BinnerConstants.HC_BASE;		
		return 1e4 * (parentMass % 1 - closestMass % 1) / parentMass;
	}
	
	private boolean outputResults() {
		String outputFileBase = curExpFileData.getName();
		int dotIndex = outputFileBase.lastIndexOf('.');
		String outputFilePrefix = outputDirectory.getAbsolutePath() + BinnerConstants.FILE_SEPARATOR + 
				(dotIndex == -1 ? outputFileBase : outputFileBase.substring(0, dotIndex));
		
		List<Integer> idsForPrintedTabs = outputSelectionWrapPanel.getSelectedTabIds();
		outputIndicesBySelectedTabMap = new HashMap<Integer, Integer>();
		
		List<BinnerOutput> outputList = new ArrayList<BinnerOutput>();
		for (int i : BinnerConstants.ALL_OUTPUTS) {
			if (!idsForPrintedTabs.contains(i)) {
				System.out.println("Skipping tab " + BinnerConstants.OUTPUT_TAB_NAMES[i] + " for tab " + i);
				continue;
				}
			
		//	if (skipBinwiseOutput && Arrays.asList(BinnerConstants.BY_BIN_OUTPUTS).contains(i)) { 
		//		System.out.println("Skipping binwise tab " + BinnerConstants.OUTPUT_TAB_NAMES[i] + " for tab " + i);
		//		continue;
		//		}
			//BinnerGroup
			BinnerOutput output = new BinnerOutput();
			output.setOriginalSheetIdx(i);
			output.setTabName(BinnerConstants.OUTPUT_TAB_NAMES[i]);
			outputIndicesBySelectedTabMap.put(i, outputList.size());
			outputList.add(output);
		}
		
		fancyContainer = new FancyBinnerOutputContainer(outputList, outputFilePrefix + BinnerConstants.REPORT_FILE_EXTENSION, 
				new Palette(PaletteValues.paletteValues1, false),  new Palette(PaletteValues.anotherPalette, false),
						new Palette(PaletteValues.histogramPaletteValues, false), this.skipBinwiseOutput, true, batchLabelWrapPanel.getIntSelected().toString());
		
		if (!outputDirectory.exists()) {
			outputDirectory.mkdirs();
		}
		
		collectColLabels();
		
		if (!handleClustering()) {
			return false;
		}
		
		analysisData.setBinwiseIsotopeGroups(null);
		
		summaryInfo.setIsotopeGroupCount(isotopeLabelNumber);
		summaryInfo.setIsotopesFoundCount(isotopesFoundCount);
		summaryInfo.setAnnotGroupCount(annotationLabelNumber);
		summaryInfo.setNonMolecularIonAnnotCount(annotationsFoundCount);	
		
		FileOutputStream output = null;
		try {
			String baseName = fancyContainer.getFileName().substring(0, fancyContainer.getFileName().lastIndexOf('.'));
	   
	        String parLabel = binningParametersWrapPanel.getGap() + "_"
	         + rebinOptionsWrapPanel.getMinRTGapThreshold() + "_" + rebinOptionsWrapPanel.getAlwaysGapThreshold();
	          
			String altName = baseName + "_"+  parLabel + ".xlsx";
			File outputWithName = new File(altName);
			
			Integer suffix = 1;
			while (outputWithName.length() > 0L)
				{
				altName = baseName + "." + suffix + ".xlsx";
				outputWithName = new File(altName);
				suffix++;
				}
			output = new FileOutputStream(altName);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		

		analysisData.getOverallHistogram().setIsDeisotoped(cleaningWrapPanel.removeIsotopeFeaturesForDistribution());
		analysisData.getOverallHistogram().setTrackingCutoff(outputSelectionWrapPanel.getHistogramMax());
		analysisData.getOverallHistogram().generateHistogram(annotFileMap, outputSelectionWrapPanel.getHistogramMin(), outputSelectionWrapPanel.getHistogramMax());	
		summaryInfo.setnAnnotatedHistogramPoints(analysisData.getOverallHistogram().getnBarSlots() == null ? "0 " : 
			analysisData.getOverallHistogram().getnBarSlots().toString());	
		summaryInfo.setnHistogramPoints(analysisData.getOverallHistogram().getnTotalPoints() == null ? "0 " : 
			analysisData.getOverallHistogram().getnTotalPoints().toString());	
	
		fancyContainer.generateExcelReport(analysisData, output, summaryInfo, analysisData.getOverallHistogram(), binStats);
		try {
			output.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		binningProgBar.setValue(binningProgBar.getMaximum());
		return true;
	}

	private boolean handleClustering() {
		int prevBinIndex = -1;
		int prevCluster = -1;
		int indexWithinBin = -1;
		int binStartIndex = -1;
		annotationLabelNumber = 1;
		annotationsFoundCount = 0;
		int nNonMissingFeatures = analysisData.getNonMissingFeaturesInOriginalOrder().size();
		Double progBarPerFeature = BinnerConstants.CLUSTERING_PROGRESS_WEIGHT / nNonMissingFeatures;
		BinnerGroup curBin = null;
		BinnerGroup curCluster = null;
		List<Integer> idsForPrintedTabs = outputSelectionWrapPanel.getSelectedTabIds();

		String progPrefix; 
		for (int i = 0; i < nNonMissingFeatures; i++) {
			int binIndex = analysisData.getNonMissingFeaturesInOriginalOrder().get(analysisData.getIndexedNonMissingRTs().
					get(i).getIndex()).getBinIndex();
			int fullBinSize = analysisData.getBinContents().get(binIndex).size();
			int binSizeForClustering = analysisData.getBinwiseCorrelationsMinusIsotopes().get(binIndex).getRowDimension();
			if (binIndex != prevBinIndex)
				{
				progPrefix = "Processing Bin " + (binIndex + 1) + " of " + nBins + " (size " + fullBinSize + "): ";
				binStartIndex = i;
				binningProgLabel.setText(progPrefix + "clustering ...  ");
				
				if (curBin != null) {
					for (int j : BinnerConstants.BY_BIN_OUTPUTS) {
						if (!idsForPrintedTabs.contains(j))
							continue;
					    Integer containerIdx = outputIndicesBySelectedTabMap.get(j);
					    fancyContainer.getOutputList().get(containerIdx).getGroups().add(curBin);
					}
				}
				curBin = new BinnerGroup();
				curBin.setTitle("BIN " + (binIndex + 1));
				boolean doClustering = binsToClusterWrapPanel.clusteringConditionsMet(binSizeForClustering, 
						analysisData.getBinwiseMeanCorrelations().get(binIndex), 
						Math.sqrt(analysisData.getBinwiseRTRanges().get(binIndex)));

				if (doClustering) {
					if (!clusterBinByCorrelations(binIndex)) {
						binningProgBar.setValue(binningProgBar.getMinimum());
						binningProgLabel.setText("");
						return false;
					}
					List<Integer> rebinnedClusters = binAgain(binIndex);
					if (rebinOptionsWrapPanel.rebinIsSelected()) {
						analysisData.setIndexedClustersForAnnotation(analysisData.getIndexedClustersFromRebinning());
					} else {
						reClusterByRT(binIndex, rebinnedClusters);
						analysisData.setIndexedClustersForAnnotation(analysisData.getIndexedClustersFromRTClustering());
					}
				} else {
					binStats.get(binIndex).initializeToNotClustered(fullBinSize);
					
					analysisData.setIndexedClustersForAnnotation(new ArrayList<IndexListItem<Integer>>());
					for (int j = 0; j < binSizeForClustering; j++) {
						IndexListItem<Integer> item = new IndexListItem<Integer>(1, j);
						analysisData.getIndexedClustersForAnnotation().add(item);
						analysisData.getNonMissingFeaturesInOriginalOrder().get(
								analysisData.getBinwiseFeaturesForClustering().get(binIndex).get(j)).setOldCluster(1);
						analysisData.getNonMissingFeaturesInOriginalOrder().get(
								analysisData.getBinwiseFeaturesForClustering().get(binIndex).get(j)).setNewCluster(1);
						analysisData.getNonMissingFeaturesInOriginalOrder().get(
								analysisData.getBinwiseFeaturesForClustering().get(binIndex).get(j)).setNewNewCluster(1);
					}
					analysisData.setIndexedClustersForRebinning(analysisData.getIndexedClustersForAnnotation());
					analysisData.setIndexedClustersFromRebinning(analysisData.getIndexedClustersForAnnotation());
					analysisData.setIndexedClustersFromRTClustering(analysisData.getIndexedClustersForAnnotation());
				}
				binningProgLabel.setText(progPrefix + "annotating ...  ");
				annotateBin(binIndex);
				try {
					if (cleaningWrapPanel.doDeisotoping()) {
						if (cleaningWrapPanel.removeIsotopeFeaturesForDistribution()) {
							analysisData.getOverallHistogram().addCounts(analysisData.getBinwiseMassDiffsMinusIsotopes().
									get(binIndex), outputSelectionWrapPanel.getHistogramMin(),  
									outputSelectionWrapPanel.getHistogramMax());
							Integer [][] slots = new Integer[analysisData.getBinwiseMassDiffsMinusIsotopes().get(binIndex).getRowDimension()]
									[analysisData.getBinwiseMassDiffsMinusIsotopes().get(binIndex).getColumnDimension()]; 
							for (int row = 0; row < analysisData.getBinwiseMassDiffsMinusIsotopes().get(binIndex).getRowDimension(); row++) {
								for (int col = 0; col <= row; col++) {
									slots[row][col] = analysisData.getOverallHistogram().getHistogramSlotForMassDiffValue(
											analysisData.getBinwiseMassDiffsMinusIsotopes().get(binIndex).getEntry(row, col));
								}
							}
							BinnerTestUtils.printRealMatrix(analysisData.getBinwiseMassDiffsMinusIsotopes().get(binIndex), slots, binIndex > 0);
						}
						putIsotopesBack(binIndex);
				
					} else {
						analysisData.setIndexedClustersForOutput(analysisData.getIndexedClustersForAnnotation());
					}
						
					if (!cleaningWrapPanel.doDeisotoping() || !cleaningWrapPanel.removeIsotopeFeaturesForDistribution()) {
						analysisData.getOverallHistogram().addCounts(analysisData.getBinwiseMassDiffs().get(binIndex), 
					    		outputSelectionWrapPanel.getHistogramMin(), 
					    		outputSelectionWrapPanel.getHistogramMax());
						Integer [][] slots = new Integer[analysisData.getBinwiseMassDiffs().get(binIndex).getRowDimension()]
								[analysisData.getBinwiseMassDiffs().get(binIndex).getColumnDimension()]; 
						for (int row = 0; row < analysisData.getBinwiseMassDiffs().get(binIndex).getRowDimension(); row++) {
							for (int col = 0; col <= row; col++) {
								slots[row][col] = analysisData.getOverallHistogram().getHistogramSlotForMassDiffValue(
										analysisData.getBinwiseMassDiffs().get(binIndex).getEntry(row, col));
							}
						}
						BinnerTestUtils.printRealMatrix(analysisData.getBinwiseMassDiffs().get(binIndex), slots, binIndex > 0);
					}
				}
				catch(Exception e) { 
					e.printStackTrace();
				}

				indexWithinBin = -1;
				prevBinIndex = binIndex;
				
				for (int j = 0; j < fullBinSize; j++) {
					int k = analysisData.getIndexedNonMissingRTs().get(binStartIndex + 
							analysisData.getIndexedClustersForOutput().get(j).getIndex()).getIndex();
					curBin.getFeatureIndexList().add(k);
				}
				prevCluster = -1;
			}
				
			indexWithinBin++;
			int cluster = analysisData.getIndexedClustersForOutput().get(indexWithinBin).getValue();
			
			if (cluster != prevCluster) {
				prevCluster = cluster;
				
				if (curCluster != null) {
					for (int j : BinnerConstants.BY_CLUST_OUTPUTS) {
						if (!idsForPrintedTabs.contains(j))
							continue;			
						Integer containerIdx = outputIndicesBySelectedTabMap.get(j);
						fancyContainer.getOutputList().get(containerIdx).getGroups().add(curCluster);
					}
				}
				curCluster = new BinnerGroup();
				int rowCluster = analysisData.getIndexedClustersForOutput().get(i - binStartIndex).getValue();
				for (int j = 0; j < fullBinSize; j++) {
					if (analysisData.getIndexedClustersForOutput().get(j).getValue() != rowCluster) {
						continue;
					}
					int k = analysisData.getIndexedNonMissingRTs().get(binStartIndex + 
							analysisData.getIndexedClustersForOutput().get(j).getIndex()).getIndex();
					curCluster.getFeatureIndexList().add(k);
				}	
				curCluster.setTitle(curBin.getTitle() + ", CLUSTER " + rowCluster );
				
			}
			
			if (i % 30 == 29) {
				setProgressBar(progressBarWeightMultiplier * 
						(BinnerConstants.DEISOTOPE_PROGRESS_WEIGHT + (i + 1) * progBarPerFeature));
			}
		}
		
		analysisData.setBinwiseCorrelationsMinusIsotopes(null);
		
		if (curBin != null && (!curBin.getFeatureIndexList().isEmpty())) {
			for (int j : BinnerConstants.BY_BIN_OUTPUTS) {
				if (!idsForPrintedTabs.contains(j))
					continue;
				Integer containerIdx = outputIndicesBySelectedTabMap.get(j);
				fancyContainer.getOutputList().get(containerIdx).getGroups().add(curBin);
			}
		}
		if (curCluster != null && (!curCluster.getFeatureIndexList().isEmpty())) {
			for (int j : BinnerConstants.BY_CLUST_OUTPUTS) {
				if (!idsForPrintedTabs.contains(j))
					continue;
				Integer containerIdx = outputIndicesBySelectedTabMap.get(j);
				fancyContainer.getOutputList().get(containerIdx).getGroups().add(curCluster);
			}
		}
		setProgressBar(progressBarWeightMultiplier * 
				(BinnerConstants.DEISOTOPE_PROGRESS_WEIGHT + BinnerConstants.CLUSTERING_PROGRESS_WEIGHT));
		return true;
	}
	
	private Array2DRowRealMatrix constructMassDiffMatrix(int binIndex) {	
		int fullBinSize = analysisData.getBinContents().get(binIndex).size();
		Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(fullBinSize, fullBinSize);
		double [] featureMasses = new double[fullBinSize];
		
		for (int k = 0; k < fullBinSize; k++) {
			Feature feature = BinnerIndexUtils.getFeatureFromRTSortedFullBin(binIndex, k);
			featureMasses[k] = feature.getMass();
		}
		
		for (int k = 0; k < fullBinSize; k++) {
			for (int l = 0; l < fullBinSize; l++) {
				matrix.setEntry(k, l, featureMasses[k] - featureMasses[l]);
			}
		}
		return matrix;
	}
	
	private Integer[][] calculateMassDiffClasses(int binIndex) {	
		int fullBinSize = analysisData.getBinContents().get(binIndex).size();
		Integer[][] massDiffClasses = new Integer[fullBinSize][fullBinSize];
		double [] massDiffsForFeature = new double[fullBinSize];
		
		for (int k = 0; k < fullBinSize; k++) {
			massDiffsForFeature = analysisData.getBinwiseMassDiffs().get(binIndex).getRow(k);
			for (int l = 0; l < fullBinSize; l++) {
				massDiffClasses[k][l] = analysisData.getMassDiffRanges().assignClass(Math.abs(massDiffsForFeature[l]));
			}
		}
		
		return massDiffClasses;
	}
	 
	private Array2DRowRealMatrix constructDeisotopedMassDiffMatrix(int binIndex)  {
		int binSize = analysisData.getBinwiseFeaturesForClustering().get(binIndex).size();
		Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(binSize, binSize);
		double [] featureMasses = new double[binSize];
		
		for (int k = 0; k < binSize; k++) {
			Feature feature = BinnerIndexUtils.getFeatureFromClusterSortedDeisotopedBin(binIndex, k);
			featureMasses[k] = feature.getMass();
		}
		
		for (int k = 0; k < binSize; k++) {
			for (int l = 0; l < binSize; l++) {
				matrix.setEntry(k, l, featureMasses[k] - featureMasses[l]);
			}
		}
		return matrix;
	}
	
	private void collectColLabels() {
		List<Integer> idsForPrintedTabs = outputSelectionWrapPanel.getSelectedTabIds();
		
		for (int i = 0; i < BinnerConstants.N_OUTPUT_FIXED_COLUMN_LABELS; i++) {
			for (int j : BinnerConstants.ALL_OUTPUTS) {
				if (!idsForPrintedTabs.contains(j))
					continue;
			    Integer containerIdx = outputIndicesBySelectedTabMap.get(j);
				
				fancyContainer.getOutputList().get(containerIdx).getHeaders().add(BinnerConstants.OUTPUT_FIXED_COLUMN_LABELS[i]);
				fancyContainer.getOutputList().get(containerIdx).setFirstBlankCol(BinnerConstants.N_OUTPUT_FIXED_COLUMN_LABELS);
				fancyContainer.getOutputList().get(containerIdx).setSecondBlankCol(BinnerConstants.N_OUTPUT_FIXED_COLUMN_LABELS + 
						nAddedCols + 1);
				fancyContainer.getOutputList().get(containerIdx).setDataStartCol(BinnerConstants.N_OUTPUT_FIXED_COLUMN_LABELS + 
						nAddedCols + 2);
			}
		}
		
		for (int i : BinnerConstants.ALL_OUTPUTS) {
			if (!idsForPrintedTabs.contains(i))
				continue;
		    Integer containerIdx = outputIndicesBySelectedTabMap.get(i);
			fancyContainer.getOutputList().get(containerIdx).getHeaders().add("");
		}
		
		for (int i = 0; i < nAddedCols; i++) {
			for (int j : BinnerConstants.ALL_OUTPUTS) {
				if (!idsForPrintedTabs.contains(j))
					continue;
			    Integer containerIdx = outputIndicesBySelectedTabMap.get(j);			
				fancyContainer.getOutputList().get(containerIdx).getHeaders().add(addedCols.get(i).toString());
			}
		}
		
		for (int i : BinnerConstants.ALL_OUTPUTS) {
			if (!idsForPrintedTabs.contains(i))
				continue;
		    Integer containerIdx = outputIndicesBySelectedTabMap.get(i);
			fancyContainer.getOutputList().get(containerIdx).getHeaders().add("");
		}
		
		for (int i : BinnerConstants.CORR_OUTPUTS) {
			if (!idsForPrintedTabs.contains(i))
				continue;
		    Integer containerIdx = outputIndicesBySelectedTabMap.get(i);
			fancyContainer.getOutputList().get(containerIdx).getHeaders().add(BinnerConstants.OUTPUT_CORRELATION_LABEL);
		}
		
		for (int i : BinnerConstants.MASS_DIFF_OUTPUTS) {
			if (!idsForPrintedTabs.contains(i))
				continue;
		    Integer containerIdx = outputIndicesBySelectedTabMap.get(i);
			fancyContainer.getOutputList().get(containerIdx).getHeaders().add(BinnerConstants.OUTPUT_MASS_DIFFS_LABEL);
		}
			
		List<String> sampLabels = new ArrayList<String>();
		for (int j = 0; j < nSampsForAnalysis; j++) 
			sampLabels.add(sampsForAnalysis[j]);
	
	
		for (int i : BinnerConstants.REF_MASS_OUTPUTS)  {
			if (!idsForPrintedTabs.contains(i))
				continue;
		    Integer containerIdx = outputIndicesBySelectedTabMap.get(i);
			fancyContainer.getOutputList().get(containerIdx).getHeaders().addAll(sampLabels);
		}
	
		for (int i : ArrayUtils.addAll(BinnerConstants.INTENSITY_OUTPUTS)) {
			if (!idsForPrintedTabs.contains(i))
				continue;
		    Integer containerIdx = outputIndicesBySelectedTabMap.get(i);
			fancyContainer.getOutputList().get(containerIdx).getHeaders().addAll(sampLabels);
		}
	}

	private void setInitialVisibilityStates() {
		expFileProgPanel.setVisible(false);
		binningProgPanel.setVisible(false);
	}
	
	private void setInitialEnabledStates() {
		enableInputPanel(true);
		enableButtonPanel(true);
	}
		
	private void enableInputPanel(boolean enable) {
		inputPanel.setEnabled(enable);
		enableInputFileWrapPanel(enable);
	}
	
	private void enableInputFileWrapPanel(boolean enable) {
		expFileWrapPanel.setEnabled(enable);
	//	enableExpFilePanel(enable);
		enableExpFileProgPanel(enable);
	}
	
	public void updateVisibilityForFileFormat(Boolean toOldFormat) {
		massColLabel.setVisible(!toOldFormat);
		massColComboBox.setVisible(!toOldFormat);
		rtColLabel.setVisible(!toOldFormat);
		rtColComboBox.setVisible(!toOldFormat);
		}
		
	private void enableExpFileProgPanel(boolean enable) {
		expFileProgPanel.setEnabled(enable);
		expFileProgBar.setEnabled(enable);
	}
	
	private void enableButtonPanel(boolean enable) {
		buttonPanel.setEnabled(enable);
		prevButton.setEnabled(false);
		nextButton.setEnabled(true);
	}
	
	public static void setProgressBar(Double value) {
		binningProgBar.setValue(Math.min((int) (binningProgBar.getMinimum() + value + 0.5),
				binningProgBar.getMaximum() - 5));
	}
	
	public static FileData getCurExpFileData() {
		return curExpFileData;
	}
	
	public static JLabel getBinningProgLabel() {
		return binningProgLabel;
	}
	
	public static Double getProgressBarWeightMultiplier() {
		return progressBarWeightMultiplier;
	}
	
	public static AnalysisData getAnalysisData() {
		return analysisData;
	}
}

