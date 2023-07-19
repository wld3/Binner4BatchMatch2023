package edu.umich.wld.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.TitledBorder;

import edu.umich.wld.AnnotationInfo;
import edu.umich.wld.Binner;
import edu.umich.wld.BinnerConstants;
import edu.umich.wld.BinnerFileUtils;
import edu.umich.wld.BinnerUtils;
import edu.umich.wld.FileData;
import edu.umich.wld.LayoutGrid;
import edu.umich.wld.LayoutItem;
import edu.umich.wld.LayoutUtils;
import edu.umich.wld.ListUtils;
import edu.umich.wld.StringUtils;
import edu.umich.wld.TextFile;

public abstract class AnnotationFilePanel extends BinnerPanel {
		
	private Boolean fInitializing;

	private JPanel annotFilePanel;
	private JPanel annotFileProgPanel;  
	private JProgressBar annotFileProgBar;
	private JPanel annotFileColMapWrapPanel;
	private Map<String, Integer> annotFileColTable = new HashMap<String, Integer>();
	
	private JComboBox<FileData> annotFileComboBox;
	
	private JComboBox<String> annotFileAnnotColComboBox;
	private JComboBox<String> annotFileMassColComboBox;
	private JComboBox<String> annotFileModeColComboBox;
	private JComboBox<String> annotFileChargeColComboBox;
	private JComboBox<String> annotFileTierColComboBox;

	private Map<Integer, Boolean> chargesLegallyMapped;
	private List<String> skippedChargeCarriers;
	
	public AnnotationFilePanel() {   }
	
	private void setupProgressPanel() {
		annotFileProgPanel = new JPanel();
		annotFileProgPanel.setLayout(new BoxLayout(annotFileProgPanel, BoxLayout.X_AXIS));
		annotFileProgPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		annotFileProgBar = new JProgressBar(0, 500);
		annotFileProgBar.setIndeterminate(true);
		annotFileProgPanel.add(annotFileProgBar);
		annotFileProgPanel.setVisible(false);
	}
	
	public void setupPanel() {
		
		setupProgressPanel(); 
		setupFilePanel();
		setupColMapPanel();
		
		JPanel annotFileWrapPanel = new JPanel();
		annotFileWrapPanel.setLayout(new BoxLayout(annotFileWrapPanel, BoxLayout.Y_AXIS));
		TitledBorder annotFileWrapBorder = BorderFactory.createTitledBorder("Select Annotation File (must be .txt)");
		annotFileWrapBorder.setTitleFont(boldFontForTitlePanel(annotFileWrapBorder, false));
		annotFileWrapBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		annotFileWrapPanel.setBorder(annotFileWrapBorder);
		annotFileWrapPanel.add(annotFilePanel);
		annotFileWrapPanel.add(annotFileProgPanel);
		annotFileWrapPanel.add(Box.createVerticalStrut(5));
		annotFileWrapPanel.add(annotFileColMapWrapPanel);
		
		annotFileProgPanel.setVisible(false);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(2));
		add(annotFileWrapPanel);
		add(Box.createVerticalStrut(2));
	}
	
	private void setupColMapPanel() { 
		JPanel annotFileColMapPanel = new JPanel();
		JLabel annotFileAnnotColLabel = new JLabel("Annotation          ");
		annotFileAnnotColComboBox = new JComboBox<String>();
		annotFileAnnotColComboBox.setEditable(false);
		annotFileAnnotColComboBox.addItem("(none)");
		annotFileAnnotColComboBox.setSelectedIndex(0);
		annotFileAnnotColComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	
		annotFileAnnotColComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if ("comboBoxChanged".equals(ae.getActionCommand())) {
				}
			}
		});
		
		JLabel annotFileMassColLabel = new JLabel("Mass                ");
		annotFileMassColComboBox = new JComboBox<String>();
		annotFileMassColComboBox.setEditable(false);
		annotFileMassColComboBox.addItem("(none)");
		annotFileMassColComboBox.setSelectedIndex(0);
		annotFileMassColComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		annotFileMassColComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if ("comboBoxChanged".equals(ae.getActionCommand())) {
				}
			}
		});
		
		JLabel annotFileModeColLabel = new JLabel("Mode               ");
		annotFileModeColComboBox = new JComboBox<String>();
		annotFileModeColComboBox.setEditable(false);
		
		annotFileModeColComboBox.addItem("(none)");
		annotFileModeColComboBox.setSelectedIndex(0);
		annotFileModeColComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		annotFileModeColComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if ("comboBoxChanged".equals(ae.getActionCommand())) {
				}
			}
		});
		
		JLabel annotFileChargeColLabel = new JLabel("Charge             ");
		annotFileChargeColComboBox = new JComboBox<String>();
		annotFileChargeColComboBox.setEditable(false);
		annotFileChargeColComboBox.addItem("(none)");
		annotFileChargeColComboBox.setSelectedIndex(0);
		annotFileChargeColComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		annotFileChargeColComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if ("comboBoxChanged".equals(ae.getActionCommand())) {
				}
			}
		});
		
		JLabel annotFileTierColLabel = new JLabel("Tier             ");
		annotFileTierColComboBox = new JComboBox<String>();
		annotFileTierColComboBox.setEditable(false);
		annotFileTierColComboBox.addItem("(none)");
		annotFileTierColComboBox.setSelectedIndex(0);
		annotFileTierColComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		annotFileTierColComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if ("comboBoxChanged".equals(ae.getActionCommand())) {
				}
			}
		});
		
		LayoutGrid panelGrid = new LayoutGrid();
		panelGrid.addRow(Arrays.asList(
				new LayoutItem(annotFileAnnotColLabel, 0.05), new LayoutItem(annotFileAnnotColComboBox, 0.95)));
		panelGrid.addRow(Arrays.asList(
				new LayoutItem(annotFileMassColLabel, 0.05), new LayoutItem(annotFileMassColComboBox, 0.95)));
		panelGrid.addRow(Arrays.asList(
				new LayoutItem(annotFileModeColLabel, 0.05), new LayoutItem(annotFileModeColComboBox, 0.95)));
		panelGrid.addRow(Arrays.asList(
				new LayoutItem(annotFileChargeColLabel, 0.05), new LayoutItem(annotFileChargeColComboBox, 0.95)));
		panelGrid.addRow(Arrays.asList(
				new LayoutItem(annotFileTierColLabel, 0.05), new LayoutItem(annotFileTierColComboBox, 0.95)));
	
		LayoutUtils.doGridLayout(annotFileColMapPanel, panelGrid);
		
		annotFileColMapWrapPanel = new JPanel();
		annotFileColMapWrapPanel.setLayout(new BoxLayout(annotFileColMapWrapPanel, BoxLayout.Y_AXIS));
		TitledBorder annotFileColMapWrapBorder = BorderFactory.createTitledBorder("Columns Analyzed");
		annotFileColMapWrapBorder.setTitleFont(boldFontForTitlePanel(annotFileColMapWrapBorder, false));
		annotFileColMapWrapPanel.setBorder(annotFileColMapWrapBorder);
		annotFileColMapWrapPanel.add(annotFileColMapPanel);
	}
	
	//Experimental
	public Boolean allInputColsMapped() { 

		Boolean fAllMapped = false;
		if (annotFileComboBox.getSelectedItem() == null)
			{
			annotFileComboBox.insertItemAt(new FileData(), 0);
			annotFileComboBox.setSelectedIndex(0);
			}
		
		if (!("(none)".equals(annotFileComboBox.getSelectedItem().toString()))) {
			fAllMapped = !("(none)".equals(annotFileAnnotColComboBox.getSelectedItem().toString()) ||
					"(none)".equals(annotFileMassColComboBox.getSelectedItem().toString()) ||
					"(none)".equals(annotFileModeColComboBox.getSelectedItem().toString()) ||
					"(none)".equals(annotFileChargeColComboBox.getSelectedItem().toString()) ||
					"(none)".equals(annotFileTierColComboBox.getSelectedItem().toString()));
		}
	return fAllMapped;
	}
	
	
	private void setupFilePanel() { 
		annotFilePanel = new JPanel();
		annotFilePanel.setLayout(new BoxLayout(annotFilePanel, BoxLayout.X_AXIS));
		annotFilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		annotFileComboBox = new JComboBox<FileData>();
		annotFileComboBox.setEditable(false);
		annotFileComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		String mruAnnotFilename = BinnerUtils.getBinnerProp(BinnerConstants.ANNOTATION_PROPS_FILE,
				 BinnerConstants.ANNOTATION_FILE_KEY);

		if (!StringUtils.isEmptyOrNull(mruAnnotFilename)) {
			File mruAnnotFile = new File(mruAnnotFilename);
			FileData fileData = new FileData();
			fileData.setName(mruAnnotFile.getName());
			fileData.setPath(mruAnnotFile.getPath());
			annotFileComboBox.insertItemAt(fileData, 0);
			Binner.getAppData().setAnnotFileData(fileData);
		}
		else
			{
			//clearAnnotFileHeadersOnError(new FileData());
			annotFileComboBox.insertItemAt(new FileData(), 0);
			annotFileComboBox.setSelectedItem(0);
			Binner.getAppData().setAnnotFileData(new FileData());
			}
	
		annotFileComboBox.setSelectedItem(Binner.getAppData().getAnnotFileData());
		annotFileComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if ("comboBoxChanged".equals(ae.getActionCommand())) {
					annotFileComboBox.setToolTipText(annotFileComboBox.getSelectedItem() == null ? "" : ((FileData) annotFileComboBox.getSelectedItem()).getName());
				}
			}
		});
		annotFileComboBox.setToolTipText(annotFileComboBox.getSelectedItem() == null ? "" : ((FileData) annotFileComboBox.getSelectedItem()).getName());
		
		JButton annotFileButton = new JButton("Import...");
		annotFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			
				File file = BinnerFileUtils.getFile("Select Annotation File", BinnerFileUtils.LOAD, "txt",
						"Text Files", null);
				
				if (file != null) {
					FileData fileData = new FileData();
					fileData.setName(file.getName());
					fileData.setPath(file.getPath());
					
					FileData curAnnotFileData = fileData; //(FileData) annotFileComboBox.getSelectedItem(); */
					
					if (openAnnotFileInner(curAnnotFileData)) {
						fInitializing = true;
						annotFileComboBox.removeAllItems();
						annotFileComboBox.insertItemAt(fileData, 0);
						annotFileComboBox.setSelectedIndex(0);
						updateInterfaceFromFileSelection(file);
					
						fInitializing = false;
					}
					
				}
			}
		});
		
		annotFilePanel.add(Box.createHorizontalStrut(8));
		annotFilePanel.add(annotFileComboBox);
		annotFilePanel.add(Box.createHorizontalStrut(8));
		annotFilePanel.add(annotFileButton);
		annotFilePanel.add(Box.createHorizontalStrut(8));
		
		annotFileProgPanel = new JPanel();
		annotFileProgPanel.setLayout(new BoxLayout(annotFileProgPanel, BoxLayout.X_AXIS));
		annotFileProgPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		annotFileProgBar = new JProgressBar(0, 500);
		annotFileProgBar.setIndeterminate(true);
		annotFileProgPanel.add(annotFileProgBar);
	}
	
	
	public void handleAnnotFileHeadersForDropdowns(TextFile file) {
		
		String [] annotFileHeaderLabels = null;
		
		int nAnnotFileHeaderCols = file.getEndColIndex(0) + 1; //inputFiles.get(BinnerConstants.ANNOTATION).getEndColIndex(0) + 1;
		
		annotFileHeaderLabels = new String[nAnnotFileHeaderCols];
		
		annotFileAnnotColComboBox.removeAllItems();
		annotFileMassColComboBox.removeAllItems();
		annotFileModeColComboBox.removeAllItems();
		annotFileChargeColComboBox.removeAllItems();
		annotFileTierColComboBox.removeAllItems();
		
		boolean missingHeaders = false;
		for (int i = 0; i < nAnnotFileHeaderCols; i++) {
			String colLabel = file.getString(0, i);
			if (colLabel == null || colLabel.isEmpty()) {
				colLabel = "(column " + (i + 1) + ")";
				missingHeaders = true;
			}
			annotFileHeaderLabels[i] = colLabel;
			annotFileColTable.put(colLabel, i);
		}
		
		boolean annotColMapped = false;
		boolean massColMapped = false;
		boolean modeColMapped = false;
		boolean chargeColMapped = false;
		boolean tierColMapped = false;
	
		List<Integer> mappedCols = new ArrayList<Integer>();
		for (int i = 0; i < nAnnotFileHeaderCols; i++) {
			String colLabel = annotFileHeaderLabels[i];
			annotFileAnnotColComboBox.addItem(colLabel);
			if (StringUtils.isMemberOfStringArray(colLabel, BinnerConstants.ANNOTATION_CHOICES)) {
				annotFileAnnotColComboBox.setSelectedItem(colLabel);
				annotColMapped = true;
				mappedCols.add(i);
			}
			annotFileMassColComboBox.addItem(colLabel);
			if (StringUtils.isMemberOfStringArray(colLabel, BinnerConstants.MASS_CHOICES)) {
				annotFileMassColComboBox.setSelectedItem(colLabel);
				massColMapped = true;
				mappedCols.add(i);
			}
			annotFileModeColComboBox.addItem(colLabel);
			if (StringUtils.isMemberOfStringArray(colLabel, BinnerConstants.MODE_CHOICES)) {
				annotFileModeColComboBox.setSelectedItem(colLabel);
				modeColMapped = true;
				mappedCols.add(i);
			}
			annotFileChargeColComboBox.addItem(colLabel);
			if (StringUtils.isMemberOfStringArray(colLabel, BinnerConstants.CHARGE_CHOICES)) {
				annotFileChargeColComboBox.setSelectedItem(colLabel);
				chargeColMapped = true;
				mappedCols.add(i);
			}
			annotFileTierColComboBox.addItem(colLabel);
			if (StringUtils.isMemberOfStringArray(colLabel, BinnerConstants.TIER_CHOICES)) {
				annotFileTierColComboBox.setSelectedItem(colLabel);
				tierColMapped = true;
				mappedCols.add(i);
			}
		}
		
		if (missingHeaders) {
			JOptionPane.showMessageDialog(null, "Warning:  One or more columns has a missing header    " +
					BinnerConstants.LINE_SEPARATOR + "and has been given one based on its column index.  ");
		}
		
		if (!annotColMapped) {
			int colIndex = ListUtils.firstAvailableCol(mappedCols, nAnnotFileHeaderCols);
			annotFileAnnotColComboBox.setSelectedItem(annotFileHeaderLabels[colIndex]);
		}
		if (!massColMapped) {
			int colIndex = ListUtils.firstAvailableCol(mappedCols, nAnnotFileHeaderCols);
			annotFileMassColComboBox.setSelectedItem(annotFileHeaderLabels[colIndex]);
		}
		if (!modeColMapped) {
			int colIndex = ListUtils.firstAvailableCol(mappedCols, nAnnotFileHeaderCols);
			annotFileModeColComboBox.setSelectedItem(annotFileHeaderLabels[colIndex]);
		}
		if (!chargeColMapped) {
			int colIndex = ListUtils.firstAvailableCol(mappedCols, nAnnotFileHeaderCols);
			annotFileChargeColComboBox.setSelectedItem(annotFileHeaderLabels[colIndex]);
		}
		if (!tierColMapped) {
			int colIndex = ListUtils.firstAvailableCol(mappedCols, nAnnotFileHeaderCols);
			annotFileTierColComboBox.setSelectedItem(annotFileHeaderLabels[colIndex]);
		}
	}
	
	public void clearAnnotFileHeadersOnError(FileData fileData) {
		annotFileAnnotColComboBox.removeAllItems();
		annotFileMassColComboBox.removeAllItems();
		annotFileModeColComboBox.removeAllItems();
		annotFileChargeColComboBox.removeAllItems();
		annotFileTierColComboBox.removeAllItems();
		
		annotFileComboBox.removeAllItems();
		annotFileComboBox.insertItemAt(new FileData(), 0);
		annotFileComboBox.setSelectedIndex(0);
		
		annotFileAnnotColComboBox.addItem("(none)");
		annotFileMassColComboBox.addItem("(none)");
		annotFileModeColComboBox.addItem("(none)");
		annotFileChargeColComboBox.addItem("(none)");
		annotFileTierColComboBox.addItem("(none)");
		
		annotFileAnnotColComboBox.setSelectedItem(0); 
		annotFileMassColComboBox.setSelectedItem(0);
		annotFileModeColComboBox.setSelectedItem(0);
		annotFileChargeColComboBox.setSelectedItem(0);
		annotFileTierColComboBox.setSelectedItem(0);
	}
	
	
	public Map<Integer, AnnotationInfo> readFileForMap(TextFile textFile, Boolean flagLoadErrors) { 
		
		Map<Integer, AnnotationInfo> annotationMap = new HashMap<Integer, AnnotationInfo>();
	
		String errMsg = null;
	 	chargesLegallyMapped = new HashMap<Integer, Boolean>();
	 	skippedChargeCarriers = new ArrayList<String>();
	 	
		if (!("(none)".equals(getFileSelectionName()))) { 
			Integer annotIndex = getAnnotIndex(); 
			Integer massIndex = getMassIndex(); 
			Integer modeIndex = getModeIndex(); 
			Integer chargeIndex = getChargeIndex(); 
			Integer tierIndex = getTierIndex(); 
			
			int nCols = textFile.getEndColIndex(0) + 1;
			if (nCols != BinnerConstants.N_ANNOTATION_FILE_COLS) {
				if (flagLoadErrors) {
					JOptionPane.showMessageDialog(null, "Error: Annotation file must contain      " + BinnerConstants.LINE_SEPARATOR +
						BinnerConstants.N_ANNOTATION_FILE_COLS + " column labels.     ");
					clearAnnotFileHeadersOnError(null);
				}	
				return null;
			}
			
			List<String> legalModes = Arrays.asList(BinnerConstants.ANNOTATION_MODES);
			List<Integer> legalCharges = Arrays.asList(BinnerConstants.ISOTOPE_CHARGES);
			
			for (int i = 1; i <= textFile.getEndRowIndex(); i++) {
				
	            int nColsInRow = textFile.getEndColIndex(i) + 1;
				
	            if (nColsInRow != BinnerConstants.N_ANNOTATION_FILE_COLS) {
					if (flagLoadErrors) {
						clearAnnotFileHeadersOnError(null);
						JOptionPane.showMessageDialog(null, "Error: Annotation file must contain " +
							BinnerConstants.N_ANNOTATION_FILE_COLS + " columns in every row, each separated by a single tab.     " + 
							BinnerConstants.LINE_SEPARATOR + BinnerConstants.LINE_SEPARATOR +
							"Row " + (i + 1) + " starting with " + textFile.getString(i, annotIndex) + 
							" appears to contain " + nColsInRow + " columns.     ");
					}
					return null;
				}
				
				errMsg = "";
				String mode = "";
				Integer tier = null, charge = null;
				try {
					tier = Integer.parseInt(textFile.getString(i,  tierIndex));
					charge = Integer.parseInt(textFile.getString(i, chargeIndex));
					mode = textFile.getString(i, modeIndex);
					
					if ("Skip".equals(mode))  {
						skippedChargeCarriers.add(textFile.getString(i, annotIndex));
						continue;
					}
					
					if (!tier.equals(1) && !tier.equals(2)) {
						errMsg = "Error: Tier column in annotation file must contain either a 1 or a 2.    " + BinnerConstants.LINE_SEPARATOR +
								"Row " + (i + 1) + " starting with " + textFile.getString(i, annotIndex) +
								" has value " + tier + "     ";
	                }
					else if (!legalModes.contains(mode)) {
							errMsg = "Error: Mode column in annotation file must contain a legal mode. Values allowed are " 
							+ BinnerConstants.LINE_SEPARATOR + BinnerConstants.LINE_SEPARATOR + "     " + legalModes 
							+ BinnerConstants.LINE_SEPARATOR + BinnerConstants.LINE_SEPARATOR
							+ "Row " + (i + 1) + " starting with " + textFile.getString(i, annotIndex) 
							+ " has mode " + mode + ".     ";
					}
					
					else if ("Negative".equals(mode) && charge > 0 || "Positive".equals(mode) && charge < 0) {
						errMsg = "Error: Mode column in annotation file must be consistent with charge carrier charge. " 
								+ BinnerConstants.LINE_SEPARATOR 
								+ "Row " + (i + 1) + " starting with " + textFile.getString(i, annotIndex) 
								+ " has charge " + charge + " and mode " + mode + "     ";
					}
					else if (!legalCharges.contains(charge) && !legalCharges.contains(-1 * charge) && !(charge.equals(0)))  {
							errMsg = "Error: Charge column in annotation file must contain either a 0 or a (+/-)1, (+/-)2 or (+/-)3 charge.    " + 
									BinnerConstants.LINE_SEPARATOR + "Row " + (i + 1) + " starting with " +
									textFile.getString(i, annotIndex) + " has value " + charge + "     ";
					}
				}
				catch (Exception e) {
					errMsg = "Error: Annotation file must contain an integer value for tier and charge and     " +
							BinnerConstants.LINE_SEPARATOR + "a double value for mass.  " +
							"Row " + (i + 1) + " starting with " + textFile.getString(i, annotIndex) 
							+ " contains an invalid entry.    ";
				}
				
				if (foundErrorCondition(errMsg, flagLoadErrors)) 
					return(null);
			
				if (!chargesLegallyMapped.containsKey(charge))
					chargesLegallyMapped.put(charge, tier.equals(1));
				else if (tier.equals(1))
					chargesLegallyMapped.put(charge, true);
			
				AnnotationInfo entry = new AnnotationInfo();
				entry.setAnnotation(textFile.getString(i, annotIndex));
				entry.setMass(textFile.getDouble(i, massIndex));
				entry.setMode(textFile.getString(i, modeIndex));
				entry.setCharge(textFile.getString(i, chargeIndex));
				entry.setTier(textFile.getString(i,  tierIndex));
				
			    annotationMap.put(i - 1, entry);
			}
		
		errMsg = "";
		
		if (foundErrorCondition(errMsg, flagLoadErrors)) 
			return(null);
		}
		return annotationMap;
	}

	
	private Boolean foundErrorCondition(String errMsg, Boolean flagLoadErrors) {
		
		if (flagLoadErrors && !StringUtils.isEmptyOrNull(errMsg))  {
			clearAnnotFileHeadersOnError(null);
			JOptionPane.showMessageDialog(null, errMsg );
			return true;
		}
		return false;
	}

	private Integer getAnnotIndex() { 
		return annotFileColTable.get(annotFileAnnotColComboBox.getSelectedItem().toString());
	}
	
	private Integer getMassIndex() { 
		return annotFileColTable.get(annotFileMassColComboBox.getSelectedItem().toString());
	}
	
	private Integer getModeIndex() { 
		return annotFileColTable.get(annotFileModeColComboBox.getSelectedItem().toString());
	}

	private Integer getChargeIndex() { 
		return annotFileColTable.get(annotFileChargeColComboBox.getSelectedItem().toString());
	}
	
	private Integer getTierIndex() { 
		return annotFileColTable.get(annotFileTierColComboBox.getSelectedItem().toString());
	}
	
	public String getTierColumnName() {
		return annotFileTierColComboBox.getSelectedItem().toString();
	}
	
	public String getMassColumnName() {
		return annotFileMassColComboBox.getSelectedItem().toString();
	}
	
	public String getAnnotColumnName() {
		return annotFileAnnotColComboBox.getSelectedItem().toString();
	}
	
	public String getChargeColumnName() {
		return annotFileChargeColComboBox.getSelectedItem().toString();
	}
	
	public String getModeColumnName() {
		return annotFileModeColComboBox.getSelectedItem().toString();
	}
	
	public Boolean hasNegModeChargeCarriers() {
		return foundCarrierForCharge(-1) || foundCarrierForCharge(-2) || foundCarrierForCharge(-3);
	}
	
	public Boolean hasChargeCarriersForMode(Boolean forPos) {
		if (forPos)
			return hasPosModeChargeCarriers();
		
		return hasNegModeChargeCarriers();
	}
	
	public Boolean hasPosModeChargeCarriers() {
		return foundCarrierForCharge(1) || foundCarrierForCharge(2) || foundCarrierForCharge(3);
	}
	public Boolean foundTierOneForCharge(Integer targetCharge) {
		return chargesLegallyMapped.containsKey(targetCharge) && chargesLegallyMapped.get(targetCharge);
	}

	public Boolean foundCarrierForCharge(Integer targetCharge) {
		return chargesLegallyMapped.containsKey(targetCharge);
	}
	
	public String getFileSelectionName() {
		return annotFileComboBox.getSelectedItem().toString();
	}
	
	public FileData getFileSelection() { 
		return (FileData) annotFileComboBox.getSelectedItem();
	}
	
	public String getSkippedCCList() {
		if (skippedChargeCarriers.size() == 0)
			return "None";
		
		return skippedChargeCarriers.toString();
	}

	protected abstract boolean openAnnotFileInner(FileData fileData);
	protected abstract boolean updateInterfaceFromFileSelection(File file);

}
