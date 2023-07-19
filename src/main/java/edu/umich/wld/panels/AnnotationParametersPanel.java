package edu.umich.wld.panels;

import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
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


public class AnnotationParametersPanel extends BinnerPanel
	{
	private static final long serialVersionUID = -6581375039587272807L;
	private JTextField adductNLMassTolBox, rtTolBox;
	
	public AnnotationParametersPanel() 
		{
		super();
		this.initializeStickySettings("annotationParameters", BinnerConstants.ANNOTATION_PROPS_FILE);
		}
	
	public void setupPanel() {
		initializeArrays();
		JPanel annotationParametersPanel = new JPanel();
		
		JLabel adductNLMassTolLabel = new JLabel("Annotation Mass Tolerance  ");
		adductNLMassTolBox  = makeStickyTextField("adductNLMassTol", BinnerConstants.DEFAULT_ADDUCT_NL_MASS_DIFFERENCE_TOL.toString(), true);
		adductNLMassTolBox.setInputVerifier(new NumberVerifier(adductNLMassTolBox, "Adduct/NL mass tolerance"));
		adductNLMassTolBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		JLabel adductNLMassTolUnitsLabel = new JLabel(" Da ");
		
		JLabel rtTolLabel = new JLabel("Annotation RT Tolerance  ");
		rtTolBox = makeStickyTextField("rtAnnotationTol", BinnerConstants.DEFAULT_RT_ANNOTATION_TOL.toString(), true); 
		rtTolBox.setInputVerifier(new NumberVerifier(rtTolBox, "Annotation RT tolerance"));
		rtTolBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		JLabel rtTolUnitsLabel = new JLabel("  ");
 		
 		LayoutGrid panelGrid = new LayoutGrid();
		panelGrid.addRow(Arrays.asList(new LayoutItem(adductNLMassTolLabel, 0.45), 
			new LayoutItem(adductNLMassTolBox, 0.45), new LayoutItem(adductNLMassTolUnitsLabel, 0.10)));
		panelGrid.addRow(Arrays.asList(new LayoutItem(rtTolLabel, 0.45), 
			new LayoutItem(rtTolBox, 0.45), new LayoutItem(rtTolUnitsLabel, 0.10)));
		
		//Details...
		LayoutUtils.doGridLayout(annotationParametersPanel, panelGrid);
		
		JPanel annotationParametersWrapPanel = new JPanel();
		annotationParametersWrapPanel.setLayout(new BoxLayout(annotationParametersWrapPanel, BoxLayout.Y_AXIS));
		TitledBorder annotationParametersWrapBorder = BorderFactory.createTitledBorder("Set Parameter Values");
		annotationParametersWrapBorder.setTitleFont(boldFontForTitlePanel(annotationParametersWrapBorder, false));
		annotationParametersWrapBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		annotationParametersWrapPanel.setBorder(annotationParametersWrapBorder);
		annotationParametersWrapPanel.add(annotationParametersPanel);
	
		setupTextFieldListeners();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(2));
		add(annotationParametersWrapPanel);
		add(Box.createVerticalStrut(2));
		}
	
	public String getAdductNLMassTol()
		{
		return adductNLMassTolBox.getText();
		}
	
	public String getRtTol()
		{
		return rtTolBox.getText();
		}
	}










/*package edu.umich.wld.panels;

import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
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


public class AnnotationParametersPanel extends BinnerPanel
	{
	private static final long serialVersionUID = -6581375039587272807L;
	private JTextField adductNLMassTolBox, rtTolBox;
	
	public AnnotationParametersPanel() 
		{
		super();
		this.initializeStickySettings("annotationParameters", BinnerConstants.ANNOTATION_PROPS_FILE);
		}
	
	public void setupPanel()
		{
		initializeArrays();
		JPanel annotationParametersPanel = new JPanel();
		
		JLabel adductNLMassTolLabel = new JLabel("Annotation Mass Tolerance  ");
		adductNLMassTolBox  = makeStickyTextField("adductNLMassTol", BinnerConstants.DEFAULT_ADDUCT_NL_MASS_DIFFERENCE_TOL.toString(), true);
		adductNLMassTolBox.setInputVerifier(new NumberVerifier(adductNLMassTolBox, "Adduct/NL mass tolerance"));
		adductNLMassTolBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		JLabel adductNLMassTolUnitsLabel = new JLabel(" Da ");
		
		JLabel rtTolLabel = new JLabel("Annotation RT Tolerance  ");
		rtTolBox = makeStickyTextField("rtAnnotationTol", BinnerConstants.DEFAULT_RT_ANNOTATION_TOL.toString(), true); 
		rtTolBox.setInputVerifier(new NumberVerifier(adductNLMassTolBox, "Adduct/NL mass tolerance"));
		rtTolBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		JLabel rtTolUnitsLabel = new JLabel("  ");
 		
 		LayoutGrid panelGrid = new LayoutGrid();
		panelGrid.addRow(Arrays.asList(new LayoutItem(adductNLMassTolLabel, 0.45), 
			new LayoutItem(adductNLMassTolBox, 0.45), new LayoutItem(adductNLMassTolUnitsLabel, 0.10)));
		panelGrid.addRow(Arrays.asList(new LayoutItem(rtTolLabel, 0.45), 
			new LayoutItem(rtTolBox, 0.45), new LayoutItem(rtTolUnitsLabel, 0.10)));
		
		LayoutUtils.doGridLayout(annotationParametersPanel, panelGrid);
		
		JPanel annotationParametersWrapPanel = new JPanel();
		annotationParametersWrapPanel.setLayout(new BoxLayout(annotationParametersWrapPanel, BoxLayout.Y_AXIS));
		TitledBorder annotationParametersWrapBorder = BorderFactory.createTitledBorder("Set Parameter Values");
		annotationParametersWrapBorder.setTitleFont(boldFontForTitlePanel(annotationParametersWrapBorder, false));
		annotationParametersWrapPanel.setBorder(annotationParametersWrapBorder);
		annotationParametersWrapPanel.add(annotationParametersPanel);
	
		setupTextFieldListeners();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(2));
		add(annotationParametersWrapPanel);
		add(Box.createVerticalStrut(2));
		}
	
	public String getAdductNLMassTol()
		{
		return adductNLMassTolBox.getText();
		}
	
	public String getRtTol()
		{
		return rtTolBox.getText();
		}
	}
*/