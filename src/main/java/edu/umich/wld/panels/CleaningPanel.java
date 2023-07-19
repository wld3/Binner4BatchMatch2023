////////////////////////////////////////////////////
// CleaningPanel.java
////////////////////////////////////////////////////
package edu.umich.wld.panels;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import edu.umich.wld.LayoutUtils;

public class CleaningPanel extends BinnerPanel
	{
	private static final long serialVersionUID = -6952575133668334008L;
	private FilterPanel filterPanel;
	private NormalizationPanel normalizationPanel;
	private DeisotopingPanel deisotopingWrapPanel;
	
	private JPanel cleaningPanel;
	private TitledBorder cleaningBorder;
	
	public CleaningPanel()
		{
		super();
		}
	
	public void setupPanel()
		{
		filterPanel = new FilterPanel();
		filterPanel.setupPanel();

		normalizationPanel = new NormalizationPanel();
		normalizationPanel.setupPanel();
	
		deisotopingWrapPanel = new DeisotopingPanel();
		deisotopingWrapPanel.setupPanel();

		cleaningPanel = new JPanel();
		//cleaningBorder = BorderFactory.createTitledBorder("Data Cleaning");
		//cleaningBorder.setTitleFont(boldFontForTitlePanel(cleaningBorder, true));
		//cleaningBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		//cleaningPanel.setBorder(cleaningBorder);
		cleaningPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		cleaningPanel.setLayout(new BoxLayout(cleaningPanel, BoxLayout.Y_AXIS));
		cleaningPanel.add(Box.createVerticalStrut(2));
		cleaningPanel.add(filterPanel);
		cleaningPanel.add(Box.createVerticalStrut(2));
		cleaningPanel.add(normalizationPanel);
		cleaningPanel.add(Box.createVerticalStrut(2));
		cleaningPanel.add(deisotopingWrapPanel);
		cleaningPanel.add(Box.createVerticalStrut(2));

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(cleaningPanel);
		//LayoutUtils.addBlankLines(this, 1);
		}
	
	public Boolean doDeisotoping()
		{
		return deisotopingWrapPanel.doDeisotoping();
		}
	
	public String getMissingnessPct()
		{
		return filterPanel.getMissingnessPct();
		}
	
	public String getIsotopeMassTol()
		{
		return deisotopingWrapPanel.getIsotopeMassTol();
		}

	public String getCorrCutoff()
		{
		return deisotopingWrapPanel.getCorrCutoff();
		}
	
	public String getIsotopeRTDiff()
		{
		return deisotopingWrapPanel.getIsotopeRTDiff();
		}
	
	public Boolean doTransformation()
		{
		return normalizationPanel.doTransformation();
		}
	
	public String getOutlierThreshold()
		{
		return filterPanel.getOutlierThreshold();
		}
	
	public Boolean removeIsotopeFeaturesForDistribution()
		{
		return deisotopingWrapPanel.doDeisotoping() && deisotopingWrapPanel.removeIsotopeFeaturesForDistribution();
		}
	
	//MERGE 08/19/18
	public Boolean treatZeroAsMissing() 
		{
		return  filterPanel.treatZeroAsMissing();
		}
	}


