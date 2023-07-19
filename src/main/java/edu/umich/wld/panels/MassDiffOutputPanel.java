////////////////////////////////////////////////////
// MassDiffOutputPanel.java
// Written by Jan Wigginton, March 2018
////////////////////////////////////////////////////
package edu.umich.wld.panels;

import java.util.Arrays;
//MERGE 03/18

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.umich.wld.BinnerConstants;
import edu.umich.wld.BinnerDecimalTextFieldPair;
import edu.umich.wld.BinnerUtils;
import edu.umich.wld.LayoutGrid;
import edu.umich.wld.LayoutItem;
import edu.umich.wld.LayoutUtils;


public class MassDiffOutputPanel extends BinnerPanel {
	private static final long serialVersionUID = 1102907615249429763L;
	private BinnerDecimalTextFieldPair weightBoxes;
	private JPanel massDiffOutputWrapPanel;
	private TitledBorder massDiffOutputWrapBorder;
	private JLabel minLabel, maxLabel;
	public MassDiffOutputPanel() { }
	
	public void setupPanel() {
		minLabel = new JLabel("Show Mass Differences Between ");
		maxLabel = new JLabel("    And    ");
		minLabel.setAlignmentX(RIGHT_ALIGNMENT);
		
		String lastDefaultUpperLimit = BinnerUtils.getBinnerProp(BinnerConstants.OUTPUT_PROPS_FILE,
				BinnerConstants.MASS_DIST_UPPER_LIMIT_KEY);
		Double defaultMax = BinnerConstants.DEFAULT_MAX_MASS_DIFF;
		try { defaultMax = Double.parseDouble(lastDefaultUpperLimit); }
		catch (Exception e) { }
		
		weightBoxes = new BinnerDecimalTextFieldPair(0.0, 
			BinnerConstants.DEFAULT_MIN_MASS_DIFF, defaultMax, BinnerConstants.LARGEST_MASS_DIFF, true);
		
		LayoutGrid panelGrid = new LayoutGrid();	
		panelGrid.addRow(Arrays.asList(
				new LayoutItem(minLabel, 0.38), new LayoutItem(weightBoxes.getLeftField(), 0.12), 
				new LayoutItem(maxLabel, 0.38), new LayoutItem(weightBoxes.getRightField(), 0.12)));
	
		weightBoxes.getLeftField().setEnabled(false);
		
		weightBoxes.getRightField().getDocument().addDocumentListener(new DocumentListener()  {
		    @Override
		    public void insertUpdate(DocumentEvent e)  { 
		    	BinnerUtils.setBinnerProp(BinnerConstants.OUTPUT_PROPS_FILE, BinnerConstants.MASS_DIST_UPPER_LIMIT_KEY, weightBoxes.getRightField().getText());
		    } 
		    
		    @Override
		    public void removeUpdate(DocumentEvent e)  { 
		    	BinnerUtils.setBinnerProp(BinnerConstants.OUTPUT_PROPS_FILE, BinnerConstants.MASS_DIST_UPPER_LIMIT_KEY, weightBoxes.getRightField().getText());
		    } 
		
		    @Override
		    public void changedUpdate(DocumentEvent e) {  
		    		BinnerUtils.setBinnerProp(BinnerConstants.OUTPUT_PROPS_FILE, BinnerConstants.MASS_DIST_UPPER_LIMIT_KEY, weightBoxes.getRightField().getText());
		    	} 
			});
		
		JPanel massDiffOutputPanel = new JPanel();
		LayoutUtils.doGridLayout(massDiffOutputPanel, panelGrid);
			
		massDiffOutputWrapPanel = new JPanel();
		massDiffOutputWrapBorder = BorderFactory.createTitledBorder("Mass Diff Distribution");
		massDiffOutputWrapBorder.setTitleFont(boldFontForTitlePanel(massDiffOutputWrapBorder, false));
		
		massDiffOutputWrapPanel.setBorder(massDiffOutputWrapBorder);
		massDiffOutputWrapPanel.setLayout(new BoxLayout(massDiffOutputWrapPanel, BoxLayout.X_AXIS));
		massDiffOutputWrapPanel.add(Box.createHorizontalGlue());
		massDiffOutputWrapPanel.add(massDiffOutputPanel);
		massDiffOutputPanel.add(Box.createHorizontalGlue());

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(massDiffOutputWrapPanel);
	}
	
	public void setHistogramMax(Double value) {
		weightBoxes.getRightField().setText(value == null ? "" : value.toString());
		BinnerUtils.setBinnerProp(BinnerConstants.OUTPUT_PROPS_FILE, BinnerConstants.MASS_DIST_UPPER_LIMIT_KEY, value.toString());
	}
	
	public Double getHistogramMax() {
		try {
			return Double.parseDouble(weightBoxes.getRightField().getText());
		}
		catch (Exception e) { throw new NumberFormatException("Error while setting histogram max. Value should be a number."); }
	}
	
	public Double getHistogramMin() {
		try  {
			return Double.parseDouble(weightBoxes.getLeftField().getText());
		}
		catch (Exception e) { throw new NumberFormatException("Error while setting histogram min. Value should be a number."); }
	}
}



