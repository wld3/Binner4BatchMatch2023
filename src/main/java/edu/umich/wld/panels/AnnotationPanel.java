////////////////////////////////////////////////////
// AnnotationPanel.java
////////////////////////////////////////////////////
package edu.umich.wld.panels;


/*import java.awt.Component;
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

import edu.umich.wld.AnalysisDialog;
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

*/

public class AnnotationPanel extends BinnerPanel
{

/**
* 
*/
private static final long serialVersionUID = 2510909230694153392L; 
}
/*	private Integer nAnnotFileHeaderCols = 0;

private AnnotationParametersPanel annotationParametersWrapPanel;
private String [] annotFileHeaderLabels = null;
private Map<String, Integer> annotFileColTable = new HashMap<String, Integer>();

private IonizationModePanel ionizationModeWrapPanel;
private MolecularIonPanel molecularIonWrapPanel;

private JPanel annotPanel;
private TitledBorder annotBorder;	
private JPanel annotFileWrapPanel;
private TitledBorder annotFileWrapBorder;
private JPanel annotFilePanel;
private JComboBox<FileData> annotFileComboBox;
private JButton annotFileButton;
private JPanel annotFileProgPanel;
private JProgressBar annotFileProgBar;
//Set Parameter Values
private JPanel annotFileColMapWrapPanel;
private TitledBorder annotFileColMapWrapBorder;
private JPanel annotFileColMapPanel;
private JLabel annotFileAnnotColLabel;
private JComboBox<String> annotFileAnnotColComboBox;
private JLabel annotFileMassColLabel;
private JComboBox<String> annotFileMassColComboBox;
private JLabel annotFileTypeColLabel;
private JComboBox<String> annotFileTypeColComboBox;
private JLabel annotFileModeColLabel;
private JComboBox<String> annotFileModeColComboBox;

public AnnotationPanel()
{
super();
}

public void setupPanel()
{
annotationParametersWrapPanel = new AnnotationParametersPanel();
annotationParametersWrapPanel.setupPanel();

ionizationModeWrapPanel = new IonizationModePanel();
ionizationModeWrapPanel.setupPanel();

molecularIonWrapPanel = new MolecularIonPanel();
molecularIonWrapPanel.setupPanel();
annotFilePanel = new JPanel();
annotFilePanel.setLayout(new BoxLayout(annotFilePanel, BoxLayout.X_AXIS));
annotFilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
annotFileComboBox = new JComboBox<FileData>();
annotFileComboBox.setEditable(false);
annotFileComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
String mruAnnotFilename = BinnerUtils.getBinnerProp(BinnerConstants.ANNOTATION_PROPS_FILE,
BinnerConstants.ANNOTATION_FILE_KEY);
if (mruAnnotFilename != null) {
File mruAnnotFile = new File(mruAnnotFilename);
FileData fileData = new FileData();
fileData.setName(mruAnnotFile.getName());
fileData.setPath(mruAnnotFile.getPath());
if (!Binner.getAppData().getAnnotFileDataStore().contains(fileData)) {
Binner.getAppData().getAnnotFileDataStore().add(fileData);
}
Binner.getAppData().setAnnotFileData(fileData);
}
for (FileData fileData: Binner.getAppData().getAnnotFileDataStore()) {
annotFileComboBox.insertItemAt(fileData, 0);
}
annotFileComboBox.setSelectedItem(Binner.getAppData().getAnnotFileData());
annotFileComboBox.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent ae) {
if ("comboBoxChanged".equals(ae.getActionCommand())) {
curAnnotFileData = (FileData) annotFileComboBox.getSelectedItem();
}
}
});
annotFileButton = new JButton("Browse...");
annotFileButton.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent ae) {
File file = BinnerFileUtils.getFile("Select Adduct/Fragment File", BinnerFileUtils.LOAD, "txt",
"Text Files", null);
if (file != null) {
FileData fileData = new FileData();
fileData.setName(file.getName());
fileData.setPath(file.getPath());
BinnerUtils.setBinnerProp(BinnerConstants.ANNOTATION_PROPS_FILE,
BinnerConstants.ANNOTATION_FILE_KEY, file.getPath());
if (!Binner.getAppData().getAnnotFileDataStore().contains(fileData)) {
Binner.getAppData().getAnnotFileDataStore().add(fileData);
}
annotFileComboBox.insertItemAt(fileData, 0);
annotFileComboBox.setSelectedIndex(0);
curAnnotFileData = (FileData) annotFileComboBox.getSelectedItem();
if (openAnnotFile()) {
fInitializing = true;
handleAnnotFileHeadersForDropdowns();
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

annotFileColMapPanel = new JPanel();
annotFileAnnotColLabel = new JLabel("Annotation          ");
annotFileAnnotColComboBox = new JComboBox<String>();
annotFileAnnotColComboBox.setEditable(false);
annotFileAnnotColComboBox.insertItemAt("(none)", 0);
annotFileAnnotColComboBox.setSelectedIndex(0);
annotFileAnnotColComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
annotFileAnnotColComboBox.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent ae) {
if ("comboBoxChanged".equals(ae.getActionCommand())) {
}
}
});

annotFileMassColLabel = new JLabel("Mass                ");
annotFileMassColComboBox = new JComboBox<String>();
annotFileMassColComboBox.setEditable(false);
annotFileMassColComboBox.insertItemAt("(none)", 0);
annotFileMassColComboBox.setSelectedIndex(0);
annotFileMassColComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
annotFileMassColComboBox.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent ae) {
if ("comboBoxChanged".equals(ae.getActionCommand())) {
}
}
});

annotFileTypeColLabel = new JLabel("Type               ");
annotFileTypeColComboBox = new JComboBox<String>();
annotFileTypeColComboBox.setEditable(false);
annotFileTypeColComboBox.insertItemAt("(none)", 0);
annotFileTypeColComboBox.setSelectedIndex(0);
annotFileTypeColComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
annotFileTypeColComboBox.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent ae) {
if ("comboBoxChanged".equals(ae.getActionCommand())) {
}
}
});

annotFileModeColLabel = new JLabel("Mode               ");
annotFileModeColComboBox = new JComboBox<String>();
annotFileModeColComboBox.setEditable(false);
annotFileModeColComboBox.insertItemAt("(none)", 0);
annotFileModeColComboBox.setSelectedIndex(0);
annotFileModeColComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
annotFileModeColComboBox.addActionListener(new ActionListener() {
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
new LayoutItem(annotFileTypeColLabel, 0.05), new LayoutItem(annotFileTypeColComboBox, 0.95)));
panelGrid.addRow(Arrays.asList(
new LayoutItem(annotFileModeColLabel, 0.05), new LayoutItem(annotFileModeColComboBox, 0.95)));
LayoutUtils.doGridLayout(annotFileColMapPanel, panelGrid);

annotFileColMapWrapPanel = new JPanel();
annotFileColMapWrapPanel.setLayout(new BoxLayout(annotFileColMapWrapPanel, BoxLayout.Y_AXIS));
annotFileColMapWrapBorder = BorderFactory.createTitledBorder("Specify Columns");
annotFileColMapWrapBorder.setTitleFont(boldFontForTitlePanel(annotFileColMapWrapBorder, false));
annotFileColMapWrapPanel.setBorder(annotFileColMapWrapBorder);
annotFileColMapWrapPanel.add(annotFileColMapPanel);

annotFileWrapPanel.setLayout(new BoxLayout(annotFileWrapPanel, BoxLayout.Y_AXIS));
annotFileWrapBorder = BorderFactory.createTitledBorder("Select Optional Adduct/Fragment File (must be .txt)");
annotFileWrapBorder.setTitleFont(boldFontForTitlePanel(annotFileWrapBorder, false));
annotFileWrapPanel.setBorder(annotFileWrapBorder);
annotFileWrapPanel.add(annotFilePanel);
annotFileWrapPanel.add(annotFileProgPanel);

annotPanel = new JPanel();
annotBorder = BorderFactory.createTitledBorder("Annotation");
annotBorder.setTitleFont(boldFontForTitlePanel(annotBorder, true));
annotBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
annotPanel.setBorder(annotBorder);
annotPanel.setLayout(new BoxLayout(annotPanel, BoxLayout.Y_AXIS));

annotPanel.add(annotationParametersWrapPanel);
annotPanel.add(Box.createVerticalStrut(2));
annotPanel.add(annotFileWrapPanel);
annotFileWrapPanel.add(Box.createVerticalStrut(5));
annotFileWrapPanel.add(annotFileColMapWrapPanel);
annotPanel.add(Box.createVerticalStrut(2));
annotPanel.add(ionizationModeWrapPanel);
annotPanel.add(Box.createVerticalStrut(2));
annotPanel.add(molecularIonWrapPanel);
annotPanel.add(Box.createVerticalStrut(2));

if (mruAnnotFilename != null) {
curAnnotFileData = (FileData) annotFileComboBox.getSelectedItem();
if (openAnnotFile()) {
fInitializing = true;
handleAnnotFileHeadersForDropdowns();
fInitializing = false;
}

annotFileWrapPanel = new JPanel();
annotFileWrapPanel.setLayout(new BoxLayout(annotFileWrapPanel, BoxLayout.Y_AXIS));
annotFileWrapBorder = BorderFactory.createTitledBorder("Select Optional Adduct/Fragment File (must be .txt)");
annotFileWrapBorder.setTitleFont(boldFontForTitlePanel(annotFileWrapBorder, false));
annotFileWrapPanel.setBorder(annotFileWrapBorder);
annotFileWrapPanel.add(annotFilePanel);
annotFileWrapPanel.add(annotFileProgPanel);

annotPanel = new JPanel();
annotBorder = BorderFactory.createTitledBorder("Annotation");
annotBorder.setTitleFont(boldFontForTitlePanel(annotBorder, true));
annotBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
annotPanel.setBorder(annotBorder);
annotPanel.setLayout(new BoxLayout(annotPanel, BoxLayout.Y_AXIS));

annotPanel.add(annotationParametersWrapPanel);
annotPanel.add(Box.createVerticalStrut(2));
annotPanel.add(annotFileWrapPanel);
annotFileWrapPanel.add(Box.createVerticalStrut(5));
annotFileWrapPanel.add(annotFileColMapWrapPanel);
annotPanel.add(Box.createVerticalStrut(2));
annotPanel.add(ionizationModeWrapPanel);
annotPanel.add(Box.createVerticalStrut(2));
annotPanel.add(molecularIonWrapPanel);
annotPanel.add(Box.createVerticalStrut(2));

if (mruAnnotFilename != null) {
curAnnotFileData = (FileData) annotFileComboBox.getSelectedItem();
if (openAnnotFile()) {
fInitializing = true;
handleAnnotFileHeadersForDropdowns();
fInitializing = false;
}
}

}


private void handleAnnotFileHeadersForDropdowns() 
{
nAnnotFileHeaderCols = inputFiles.get(BinnerConstants.ANNOTATION).getEndColIndex(0) + 1;
annotFileHeaderLabels = new String[nAnnotFileHeaderCols];

annotFileAnnotColComboBox.removeAllItems();
annotFileMassColComboBox.removeAllItems();
annotFileTypeColComboBox.removeAllItems();
annotFileModeColComboBox.removeAllItems();
boolean missingHeaders = false;
for (int i = 0; i < nAnnotFileHeaderCols; i++) {
String colLabel = inputFiles.get(BinnerConstants.ANNOTATION).getString(0, i);
if (colLabel == null || colLabel.isEmpty()) {
colLabel = "(column " + (i + 1) + ")";
missingHeaders = true;
}
annotFileHeaderLabels[i] = colLabel;
annotFileColTable.put(colLabel, i);
}

boolean annotColMapped = false;
boolean massColMapped = false;
boolean typeColMapped = false;
boolean modeColMapped = false;
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
annotFileTypeColComboBox.addItem(colLabel);
if (StringUtils.isMemberOfStringArray(colLabel, BinnerConstants.TYPE_CHOICES)) {
annotFileTypeColComboBox.setSelectedItem(colLabel);
typeColMapped = true;
mappedCols.add(i);
}
annotFileModeColComboBox.addItem(colLabel);
if (StringUtils.isMemberOfStringArray(colLabel, BinnerConstants.MODE_CHOICES)) {
annotFileModeColComboBox.setSelectedItem(colLabel);
modeColMapped = true;
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
if (!typeColMapped) {
int colIndex = ListUtils.firstAvailableCol(mappedCols, nAnnotFileHeaderCols);
annotFileTypeColComboBox.setSelectedItem(annotFileHeaderLabels[colIndex]);
}
if (!modeColMapped) {
int colIndex = ListUtils.firstAvailableCol(mappedCols, nAnnotFileHeaderCols);
annotFileModeColComboBox.setSelectedItem(annotFileHeaderLabels[colIndex]);
}
}


}

///annotFileMassColComboBox.getSelectedItem().toString()

public String getAdductNLMassTol()
{
return annotationParametersWrapPanel.getAdductNLMassTol();
}

public String getAnnotFileAnnotCol()
{
return annotFileAnnotColComboBox.getSelectedItem().toString();
}

public String getAnnotFileMassCol()
{
return annotFileMassColComboBox.getSelectedItem().toString();
}

public String getAnnotFileTypeCol()
{
return annotFileTypeColComboBox.getSelectedItem().toString();
}

public String getAnnotFileModeCol()
{
return annotFileModeColComboBox.getSelectedItem().toString();
}

public String getAnnotFileName()
{
return annotFileComboBox.getSelectedItem().toString();
}

public Integer getAnnotFileAnnotColIndex()
{
return annotFileColTable.get(getAnnotFileAnnotCol());
}


public Integer getAnnotFileAnnotTypeIndex()
{
return annotFileColTable.get(getAnnotFileTypeCol());
}

public Integer getAnnotFileAnnotMassIndex()
{
return annotFileColTable.get(getAnnotFileMassCol());
}

public Integer getAnnotFileAnnotModeIndex()
{
return annotFileColTable.get(getAnnotFileModeCol());
}

public JPanel getAnnotFileProgPanel()
{
return annotFileProgPanel;
}

public IonizationModePanel getIonizationModePanel()
{
return this.ionizationModeWrapPanel;
}

public MolecularIonPanel getMolecularIonPanel()
{
return this.molecularIonWrapPanel;
}

} */

/*import java.awt.Component;
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

import edu.umich.wld.AnalysisDialog;
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


public class AnnotationPanel extends BinnerPanel
	{

	/**
	 * 
	 
	private static final long serialVersionUID = 2510909230694153392L; 
	}
/*	private Integer nAnnotFileHeaderCols = 0;
	
	private AnnotationParametersPanel annotationParametersWrapPanel;
	private String [] annotFileHeaderLabels = null;
	private Map<String, Integer> annotFileColTable = new HashMap<String, Integer>();

	private IonizationModePanel ionizationModeWrapPanel;
	private MolecularIonPanel molecularIonWrapPanel;
	
	private JPanel annotPanel;
	private TitledBorder annotBorder;	
	private JPanel annotFileWrapPanel;
	private TitledBorder annotFileWrapBorder;
	private JPanel annotFilePanel;
	private JComboBox<FileData> annotFileComboBox;
	private JButton annotFileButton;
	private JPanel annotFileProgPanel;
	private JProgressBar annotFileProgBar;
	//Set Parameter Values
	private JPanel annotFileColMapWrapPanel;
	private TitledBorder annotFileColMapWrapBorder;
	private JPanel annotFileColMapPanel;
	private JLabel annotFileAnnotColLabel;
	private JComboBox<String> annotFileAnnotColComboBox;
	private JLabel annotFileMassColLabel;
	private JComboBox<String> annotFileMassColComboBox;
	private JLabel annotFileTypeColLabel;
	private JComboBox<String> annotFileTypeColComboBox;
	private JLabel annotFileModeColLabel;
	private JComboBox<String> annotFileModeColComboBox;

    public AnnotationPanel()
    	{
    	super();
    	}
    
    public void setupPanel()
    	{
    	annotationParametersWrapPanel = new AnnotationParametersPanel();
    	annotationParametersWrapPanel.setupPanel();

    	ionizationModeWrapPanel = new IonizationModePanel();
		ionizationModeWrapPanel.setupPanel();
		
		molecularIonWrapPanel = new MolecularIonPanel();
		molecularIonWrapPanel.setupPanel();
    	annotFilePanel = new JPanel();
		annotFilePanel.setLayout(new BoxLayout(annotFilePanel, BoxLayout.X_AXIS));
		annotFilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		annotFileComboBox = new JComboBox<FileData>();
		annotFileComboBox.setEditable(false);
		annotFileComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		String mruAnnotFilename = BinnerUtils.getBinnerProp(BinnerConstants.ANNOTATION_PROPS_FILE,
				BinnerConstants.ANNOTATION_FILE_KEY);
		if (mruAnnotFilename != null) {
			File mruAnnotFile = new File(mruAnnotFilename);
			FileData fileData = new FileData();
			fileData.setName(mruAnnotFile.getName());
			fileData.setPath(mruAnnotFile.getPath());
			if (!Binner.getAppData().getAnnotFileDataStore().contains(fileData)) {
				Binner.getAppData().getAnnotFileDataStore().add(fileData);
			}
			Binner.getAppData().setAnnotFileData(fileData);
		}
		for (FileData fileData: Binner.getAppData().getAnnotFileDataStore()) {
			annotFileComboBox.insertItemAt(fileData, 0);
		}
		annotFileComboBox.setSelectedItem(Binner.getAppData().getAnnotFileData());
		annotFileComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if ("comboBoxChanged".equals(ae.getActionCommand())) {
					curAnnotFileData = (FileData) annotFileComboBox.getSelectedItem();
				}
			}
		});
		annotFileButton = new JButton("Browse...");
		annotFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				File file = BinnerFileUtils.getFile("Select Adduct/Fragment File", BinnerFileUtils.LOAD, "txt",
						"Text Files", null);
				if (file != null) {
					FileData fileData = new FileData();
					fileData.setName(file.getName());
					fileData.setPath(file.getPath());
					BinnerUtils.setBinnerProp(BinnerConstants.ANNOTATION_PROPS_FILE,
							BinnerConstants.ANNOTATION_FILE_KEY, file.getPath());
					if (!Binner.getAppData().getAnnotFileDataStore().contains(fileData)) {
						Binner.getAppData().getAnnotFileDataStore().add(fileData);
					}
					annotFileComboBox.insertItemAt(fileData, 0);
					annotFileComboBox.setSelectedIndex(0);
					curAnnotFileData = (FileData) annotFileComboBox.getSelectedItem();
					if (openAnnotFile()) {
						fInitializing = true;
						handleAnnotFileHeadersForDropdowns();
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
		
		annotFileColMapPanel = new JPanel();
		annotFileAnnotColLabel = new JLabel("Annotation          ");
		annotFileAnnotColComboBox = new JComboBox<String>();
		annotFileAnnotColComboBox.setEditable(false);
		annotFileAnnotColComboBox.insertItemAt("(none)", 0);
		annotFileAnnotColComboBox.setSelectedIndex(0);
		annotFileAnnotColComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		annotFileAnnotColComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if ("comboBoxChanged".equals(ae.getActionCommand())) {
				}
			}
		});
		
		annotFileMassColLabel = new JLabel("Mass                ");
		annotFileMassColComboBox = new JComboBox<String>();
		annotFileMassColComboBox.setEditable(false);
		annotFileMassColComboBox.insertItemAt("(none)", 0);
		annotFileMassColComboBox.setSelectedIndex(0);
		annotFileMassColComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		annotFileMassColComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if ("comboBoxChanged".equals(ae.getActionCommand())) {
				}
			}
		});
		
		annotFileTypeColLabel = new JLabel("Type               ");
		annotFileTypeColComboBox = new JComboBox<String>();
		annotFileTypeColComboBox.setEditable(false);
		annotFileTypeColComboBox.insertItemAt("(none)", 0);
		annotFileTypeColComboBox.setSelectedIndex(0);
		annotFileTypeColComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		annotFileTypeColComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if ("comboBoxChanged".equals(ae.getActionCommand())) {
				}
			}
		});
		
		annotFileModeColLabel = new JLabel("Mode               ");
		annotFileModeColComboBox = new JComboBox<String>();
		annotFileModeColComboBox.setEditable(false);
		annotFileModeColComboBox.insertItemAt("(none)", 0);
		annotFileModeColComboBox.setSelectedIndex(0);
		annotFileModeColComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		annotFileModeColComboBox.addActionListener(new ActionListener() {
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
				new LayoutItem(annotFileTypeColLabel, 0.05), new LayoutItem(annotFileTypeColComboBox, 0.95)));
		panelGrid.addRow(Arrays.asList(
				new LayoutItem(annotFileModeColLabel, 0.05), new LayoutItem(annotFileModeColComboBox, 0.95)));
		LayoutUtils.doGridLayout(annotFileColMapPanel, panelGrid);
		
		annotFileColMapWrapPanel = new JPanel();
		annotFileColMapWrapPanel.setLayout(new BoxLayout(annotFileColMapWrapPanel, BoxLayout.Y_AXIS));
		annotFileColMapWrapBorder = BorderFactory.createTitledBorder("Specify Columns");
		annotFileColMapWrapBorder.setTitleFont(boldFontForTitlePanel(annotFileColMapWrapBorder, false));
		annotFileColMapWrapPanel.setBorder(annotFileColMapWrapBorder);
		annotFileColMapWrapPanel.add(annotFileColMapPanel);
		
		annotFileWrapPanel.setLayout(new BoxLayout(annotFileWrapPanel, BoxLayout.Y_AXIS));
		annotFileWrapBorder = BorderFactory.createTitledBorder("Select Optional Adduct/Fragment File (must be .txt)");
		annotFileWrapBorder.setTitleFont(boldFontForTitlePanel(annotFileWrapBorder, false));
		annotFileWrapPanel.setBorder(annotFileWrapBorder);
		annotFileWrapPanel.add(annotFilePanel);
		annotFileWrapPanel.add(annotFileProgPanel);
		
		annotPanel = new JPanel();
		annotBorder = BorderFactory.createTitledBorder("Annotation");
		annotBorder.setTitleFont(boldFontForTitlePanel(annotBorder, true));
		annotBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		annotPanel.setBorder(annotBorder);
		annotPanel.setLayout(new BoxLayout(annotPanel, BoxLayout.Y_AXIS));
		
		annotPanel.add(annotationParametersWrapPanel);
		annotPanel.add(Box.createVerticalStrut(2));
		annotPanel.add(annotFileWrapPanel);
		annotFileWrapPanel.add(Box.createVerticalStrut(5));
		annotFileWrapPanel.add(annotFileColMapWrapPanel);
		annotPanel.add(Box.createVerticalStrut(2));
		annotPanel.add(ionizationModeWrapPanel);
		annotPanel.add(Box.createVerticalStrut(2));
		annotPanel.add(molecularIonWrapPanel);
		annotPanel.add(Box.createVerticalStrut(2));
		
		if (mruAnnotFilename != null) {
			curAnnotFileData = (FileData) annotFileComboBox.getSelectedItem();
			if (openAnnotFile()) {
				fInitializing = true;
				handleAnnotFileHeadersForDropdowns();
				fInitializing = false;
			}
			
			annotFileWrapPanel = new JPanel();
			annotFileWrapPanel.setLayout(new BoxLayout(annotFileWrapPanel, BoxLayout.Y_AXIS));
			annotFileWrapBorder = BorderFactory.createTitledBorder("Select Optional Adduct/Fragment File (must be .txt)");
			annotFileWrapBorder.setTitleFont(boldFontForTitlePanel(annotFileWrapBorder, false));
			annotFileWrapPanel.setBorder(annotFileWrapBorder);
			annotFileWrapPanel.add(annotFilePanel);
			annotFileWrapPanel.add(annotFileProgPanel);
			
			annotPanel = new JPanel();
			annotBorder = BorderFactory.createTitledBorder("Annotation");
			annotBorder.setTitleFont(boldFontForTitlePanel(annotBorder, true));
			annotBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
			annotPanel.setBorder(annotBorder);
			annotPanel.setLayout(new BoxLayout(annotPanel, BoxLayout.Y_AXIS));
			
			annotPanel.add(annotationParametersWrapPanel);
			annotPanel.add(Box.createVerticalStrut(2));
			annotPanel.add(annotFileWrapPanel);
			annotFileWrapPanel.add(Box.createVerticalStrut(5));
			annotFileWrapPanel.add(annotFileColMapWrapPanel);
			annotPanel.add(Box.createVerticalStrut(2));
			annotPanel.add(ionizationModeWrapPanel);
			annotPanel.add(Box.createVerticalStrut(2));
			annotPanel.add(molecularIonWrapPanel);
			annotPanel.add(Box.createVerticalStrut(2));
			
			if (mruAnnotFilename != null) {
				curAnnotFileData = (FileData) annotFileComboBox.getSelectedItem();
				if (openAnnotFile()) {
					fInitializing = true;
					handleAnnotFileHeadersForDropdowns();
					fInitializing = false;
				}
			}
			
		}
		

		private void handleAnnotFileHeadersForDropdowns() 
			{
			nAnnotFileHeaderCols = inputFiles.get(BinnerConstants.ANNOTATION).getEndColIndex(0) + 1;
			annotFileHeaderLabels = new String[nAnnotFileHeaderCols];
			
			annotFileAnnotColComboBox.removeAllItems();
			annotFileMassColComboBox.removeAllItems();
			annotFileTypeColComboBox.removeAllItems();
			annotFileModeColComboBox.removeAllItems();
			boolean missingHeaders = false;
			for (int i = 0; i < nAnnotFileHeaderCols; i++) {
				String colLabel = inputFiles.get(BinnerConstants.ANNOTATION).getString(0, i);
				if (colLabel == null || colLabel.isEmpty()) {
					colLabel = "(column " + (i + 1) + ")";
					missingHeaders = true;
				}
				annotFileHeaderLabels[i] = colLabel;
				annotFileColTable.put(colLabel, i);
			}
			
			boolean annotColMapped = false;
			boolean massColMapped = false;
			boolean typeColMapped = false;
			boolean modeColMapped = false;
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
				annotFileTypeColComboBox.addItem(colLabel);
				if (StringUtils.isMemberOfStringArray(colLabel, BinnerConstants.TYPE_CHOICES)) {
					annotFileTypeColComboBox.setSelectedItem(colLabel);
					typeColMapped = true;
					mappedCols.add(i);
				}
				annotFileModeColComboBox.addItem(colLabel);
				if (StringUtils.isMemberOfStringArray(colLabel, BinnerConstants.MODE_CHOICES)) {
					annotFileModeColComboBox.setSelectedItem(colLabel);
					modeColMapped = true;
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
			if (!typeColMapped) {
				int colIndex = ListUtils.firstAvailableCol(mappedCols, nAnnotFileHeaderCols);
				annotFileTypeColComboBox.setSelectedItem(annotFileHeaderLabels[colIndex]);
			}
			if (!modeColMapped) {
				int colIndex = ListUtils.firstAvailableCol(mappedCols, nAnnotFileHeaderCols);
				annotFileModeColComboBox.setSelectedItem(annotFileHeaderLabels[colIndex]);
			}
		}

		
    	}
    
    ///annotFileMassColComboBox.getSelectedItem().toString()
    
    public String getAdductNLMassTol()
	    {
	    return annotationParametersWrapPanel.getAdductNLMassTol();
	    }
    
    public String getAnnotFileAnnotCol()
	    {
	    return annotFileAnnotColComboBox.getSelectedItem().toString();
	    }
    
    public String getAnnotFileMassCol()
	    {
	    return annotFileMassColComboBox.getSelectedItem().toString();
	    }
	    
    public String getAnnotFileTypeCol()
	    {
	    return annotFileTypeColComboBox.getSelectedItem().toString();
	    }
    
    public String getAnnotFileModeCol()
    	{
    	return annotFileModeColComboBox.getSelectedItem().toString();
    	}
    
    public String getAnnotFileName()
    	{
    	return annotFileComboBox.getSelectedItem().toString();
    	}
    
    public Integer getAnnotFileAnnotColIndex()
    	{
    	return annotFileColTable.get(getAnnotFileAnnotCol());
    	}
    
    
    public Integer getAnnotFileAnnotTypeIndex()
    	{
    	return annotFileColTable.get(getAnnotFileTypeCol());
    	}
    
    public Integer getAnnotFileAnnotMassIndex()
    	{
    	return annotFileColTable.get(getAnnotFileMassCol());
    	}
    
    public Integer getAnnotFileAnnotModeIndex()
    	{
    	return annotFileColTable.get(getAnnotFileModeCol());
    	}
    
    public JPanel getAnnotFileProgPanel()
    	{
    	return annotFileProgPanel;
    	}
		
    public IonizationModePanel getIonizationModePanel()
    	{
    	return this.ionizationModeWrapPanel;
    	}
    
    public MolecularIonPanel getMolecularIonPanel()
    	{
    	return this.molecularIonWrapPanel;
    	}
			
	} */
