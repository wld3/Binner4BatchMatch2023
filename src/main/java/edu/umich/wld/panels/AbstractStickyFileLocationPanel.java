////////////////////////////////////////////////////////
// AbstractStickyFileLocationPanel.java
// Written by Jan Wigginton 
// February 2022
////////////////////////////////////////////////////////////

package edu.umich.wld.panels;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import edu.umich.wld.PostProccessConstants;
import edu.umich.wld.BinnerUtils;
import edu.umich.wld.BatchMatchConstants;
import edu.umich.wld.BinnerConstants;
import edu.umich.wld.StringUtils;


public abstract class AbstractStickyFileLocationPanel extends StickySettingsPanel {

	private static final long serialVersionUID = 500752319418239107L;
	
	private JPanel fileLocWrapPanel;
	private TitledBorder fileLocWrapBorder;
	private JPanel fileLocPanel;
	private JComboBox<String> fileLocComboBox;
	private JButton saveBinningButton;
	private File outputDirectory =  null;
	private String panelTitle, panelTag;
	
	public AbstractStickyFileLocationPanel(String panelTitle) {
		this(panelTitle, PostProccessConstants.CLIENT_RESULTS_DIRECTORY_KEY);
	}
	
	public AbstractStickyFileLocationPanel(String panelTitle, String panelTag) {
		super();
		this.panelTitle = panelTitle;
		this.panelTag = panelTag;
	}
	
	public void setupPanel() {
		fileLocPanel = new JPanel();
		fileLocPanel.setLayout(new BoxLayout(fileLocPanel, BoxLayout.X_AXIS));
		fileLocComboBox = new JComboBox<String>();
		fileLocComboBox.setEditable(false);
		fileLocComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		
		String mruOutputDirname = BinnerUtils.getBinnerProp(BatchMatchConstants.PROPS_FILE, panelTag);

		if (StringUtils.isEmptyOrNull(mruOutputDirname))
			mruOutputDirname = BatchMatchConstants.HOME_DIRECTORY;	 
		
		setOutputDirectory(mruOutputDirname);
			  
		fileLocComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			}
		});
		saveBinningButton = new JButton("Browse...");
		saveBinningButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.showSaveDialog(null);

				outputDirectory = chooser.getSelectedFile();
				setOutputDirectory(outputDirectory);
				updateInterfaceForNewSelection();
			}
		});
		fileLocPanel.add(Box.createHorizontalStrut(8));
		fileLocPanel.add(fileLocComboBox);
		fileLocPanel.add(Box.createHorizontalStrut(8));
		fileLocPanel.add(saveBinningButton);
		fileLocPanel.add(Box.createHorizontalStrut(8));
		
		fileLocWrapPanel = new JPanel();
		fileLocWrapPanel.setLayout(new BoxLayout(fileLocWrapPanel, BoxLayout.Y_AXIS));
		fileLocWrapBorder = BorderFactory.createTitledBorder(panelTitle);
		fileLocWrapBorder.setTitleFont(boldFontForTitlePanel(fileLocWrapBorder, false));
	//	fileLocWrapBorder.setTitleColor(PostProccessConstants.TITLE_COLOR);
		fileLocWrapBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		
		fileLocWrapPanel.setBorder(fileLocWrapBorder);
		fileLocWrapPanel.add(fileLocPanel);	 
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(fileLocWrapPanel);
	}

	public File getOutputDirectory()  {
		return outputDirectory;
	}	

	public void setOutputDirectory(String outputDirectoryName)  {
		
		if (StringUtils.isEmptyOrNull(outputDirectoryName))
			return;
		
		File file = new File(outputDirectoryName);
		
		if (!file.exists()) {
			JOptionPane.showMessageDialog(null, 
				"The specified output directory (" + outputDirectoryName + ") does not exist. Output will be directed to your home directory.");
			file = new File(PostProccessConstants.HOME_DIRECTORY);
		}
		setOutputDirectory(file);
	}
	
	public String getOutputDirectoryPath() { 
		return outputDirectory.getAbsolutePath();
	}
	
	public void setOutputDirectory(File outputDirectory)  {
		
		this.outputDirectory = outputDirectory;
		if (outputDirectory != null) {
			fileLocComboBox.removeAllItems();
			fileLocComboBox.insertItemAt(outputDirectory.getAbsolutePath(), 0);
			fileLocComboBox.setSelectedIndex(0);
			BinnerUtils.setBinnerProp(BatchMatchConstants.PROPS_FILE, panelTag, outputDirectory.getAbsolutePath());
		}
	}
	
	public void initializeOutputDirectory() {
		if (getOutputDirectory().exists()) 
			getOutputDirectory().mkdirs();
	}
	
	public String getPanelTitle() {
		return panelTitle;
	}
	
	protected abstract void updateInterfaceForNewSelection();
}
