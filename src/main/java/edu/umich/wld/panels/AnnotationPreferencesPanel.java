////////////////////////////////////////////////////
// AnnotationPreferencesPanel.java
// Written by Jan Wigginton May 2018
////////////////////////////////////////////////////
package edu.umich.wld.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import edu.umich.wld.AnnotationInfo;
import edu.umich.wld.BinnerConstants;
import edu.umich.wld.BinnerUtils;
import edu.umich.wld.ChargeCarrierMapWrapper;
import edu.umich.wld.ChargeCarrierPreferenceByGroupNameComparator;
import edu.umich.wld.ChargeCarrierPreferences;
import edu.umich.wld.ChargeCarrierPreferencesPopup;
import edu.umich.wld.LayoutGrid;
import edu.umich.wld.LayoutItem;
import edu.umich.wld.LayoutUtils;
import edu.umich.wld.ListUtils;
import edu.umich.wld.StringUtils;


public class AnnotationPreferencesPanel extends BinnerPanel {
	private JPanel annotationPreferencesPanel;
	private TitledBorder annotationPreferencesBorder;
	
	private JPanel chargeCarrierPanel, chargeCarrierWrapPanel; 
	private TitledBorder chargeCarrierWrapBorder;

	private JPanel globalOptionsPanel, globalOptionsWrapPanel; 
	private TitledBorder globalOptionsWrapBorder;
	
	private JComboBox<ChargeCarrierPreferences> chargeCarrierComboBox;
	private JButton customizeButton, resetButton;
	private JCheckBox useNMForBestCtCheckBox, allowChargeToVaryCheckBox;
   
	private ChargeCarrierPreferences currPreferences;
	private Map<String, ChargeCarrierPreferences> chargeCarrierPrefMap = new HashMap<String, ChargeCarrierPreferences>();
    private Map<String, ChargeCarrierPreferences> anyModeMap = new HashMap<String, ChargeCarrierPreferences>();
    
    Integer modeSign = null;
    
	public AnnotationPreferencesPanel(Integer modeSign) {
		super();
		
		this.modeSign = modeSign;
		try {
			ChargeCarrierMapWrapper wrapper = 
					((ChargeCarrierMapWrapper) BinnerUtils.getBinnerObjectProp(BinnerConstants.ANNOTATION_PROPS_FILE, "carrierPrefs", 
							ChargeCarrierMapWrapper.class));
			
			anyModeMap = wrapper != null ?  wrapper.getMap() : new HashMap<String, ChargeCarrierPreferences>();
		}
		catch (Exception e) {  } 
		
		initializeStickySettings("globalPrefs", BinnerConstants.ANNOTATION_PROPS_FILE);
	}
	
	public void setupPanel() {
		initializeArrays();
		createChargeCarrierPanel();
		createGlobalPanel();
		
		annotationPreferencesPanel = new JPanel();
		annotationPreferencesBorder = BorderFactory.createTitledBorder("Set Annotation Preferences");
		annotationPreferencesBorder.setTitleFont(boldFontForTitlePanel(annotationPreferencesBorder, false));
		annotationPreferencesBorder.setTitleColor(BinnerConstants.TITLE_COLOR);
		annotationPreferencesPanel.setBorder(annotationPreferencesBorder);
		annotationPreferencesPanel.setLayout(new BoxLayout(annotationPreferencesPanel, BoxLayout.Y_AXIS));
		annotationPreferencesPanel.add(Box.createVerticalStrut(2));
		annotationPreferencesPanel.add(globalOptionsWrapPanel);
		annotationPreferencesPanel.add(Box.createVerticalStrut(2));
		annotationPreferencesPanel.add(chargeCarrierWrapPanel);
		annotationPreferencesPanel.add(Box.createVerticalStrut(2));
		
		setupTextFieldListeners(); 
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(annotationPreferencesPanel);
	}
		
	private void createGlobalPanel() {
		useNMForBestCtCheckBox = makeStickyCheckBox("", "useNMForCt", true, true);
		useNMForBestCtCheckBox.setHorizontalAlignment(SwingConstants.RIGHT);
		
		allowChargeToVaryCheckBox = makeStickyCheckBox("", "someOtherOption", false, true);
		allowChargeToVaryCheckBox.setHorizontalAlignment(SwingConstants.RIGHT);
		
		globalOptionsPanel = new JPanel();
		LayoutGrid globalOptionsGrid = new LayoutGrid();
		
		JLabel nmLabel = new JLabel("  Use Neutral Masses To Help Determine Best Charge Carrier", SwingConstants.LEFT);
		JLabel vcLabel = new JLabel("  Allow Variable Charge Without Isotope Information", SwingConstants.LEFT);
				
		globalOptionsGrid.addRow(Arrays.asList(
				new LayoutItem(useNMForBestCtCheckBox, 0.15), new LayoutItem(nmLabel, 0.35),   new LayoutItem(new JLabel(""), 0.50) ));
	
		globalOptionsGrid.addRow(Arrays.asList(
				new LayoutItem(allowChargeToVaryCheckBox, 0.15), new LayoutItem(vcLabel, 0.35),   new LayoutItem(new JLabel(""), 0.50) ));
	
		LayoutUtils.doGridLayout(globalOptionsPanel, globalOptionsGrid);	
		
		globalOptionsWrapPanel = new JPanel();
		globalOptionsWrapPanel.setLayout(new BoxLayout(globalOptionsWrapPanel, BoxLayout.Y_AXIS));
		globalOptionsWrapBorder = BorderFactory.createTitledBorder("");
		globalOptionsWrapBorder.setTitleFont(boldFontForTitlePanel(globalOptionsWrapBorder, false));
		globalOptionsWrapPanel.setBorder(globalOptionsWrapBorder);
		globalOptionsWrapPanel.add(globalOptionsPanel);
	}
	
	private void createChargeCarrierPanel() {
		chargeCarrierPanel = new JPanel();
		chargeCarrierPanel.setLayout(new BoxLayout(chargeCarrierPanel, BoxLayout.X_AXIS));
		chargeCarrierPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
	
		chargeCarrierComboBox = new JComboBox<ChargeCarrierPreferences>();
		chargeCarrierComboBox.setEditable(false);
		chargeCarrierComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		
		Map<String, ChargeCarrierPreferences> tempMap = new HashMap<String, ChargeCarrierPreferences>();
		if (anyModeMap == null)
			anyModeMap = new HashMap<String, ChargeCarrierPreferences>();
		
		if (anyModeMap != null)
			for (String key: anyModeMap.keySet())
				if (anyModeMap.get(key).wasCustomized()) {
					String recordKey = getKeyFor(anyModeMap.get(key).getGroupName());
					if (recordKey == null || recordKey.isEmpty())
						continue;
					ChargeCarrierPreferences prefs = anyModeMap.get(key);
					tempMap.put(recordKey, anyModeMap.get(key));
				}
		
		chargeCarrierPrefMap.clear();
		chargeCarrierComboBox.removeAllItems();
		List<ChargeCarrierPreferences> valueSet = ListUtils.makeListFromCollection(tempMap.values());
		Collections.sort(valueSet, new ChargeCarrierPreferenceByGroupNameComparator());
		
		if (valueSet.size() > 0)				
			for (ChargeCarrierPreferences value : valueSet) {
				String key = getKeyFor(value.getGroupName());
				chargeCarrierPrefMap.put(key, tempMap.get(key));
				chargeCarrierComboBox.insertItemAt((ChargeCarrierPreferences) tempMap.get(key), 0);
			}
		
		resetButton = new JButton("     Reset     ");
		resetButton.setEnabled(false); 
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				currPreferences = (ChargeCarrierPreferences) chargeCarrierComboBox.getSelectedItem();
				currPreferences.reset();
				updateCarrierPreferencesForCustomization(currPreferences);
			}
		});

		customizeButton = new JButton("Details...");
		customizeButton.setEnabled(false); 
		customizeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (currPreferences != null) {
					ChargeCarrierPreferencesPopup carrierPopup = new ChargeCarrierPreferencesPopup(null, currPreferences, true);
					currPreferences = carrierPopup.getPreferences();
					updateCarrierPreferencesForCustomization(currPreferences);
				}
			}
		});
	
		chargeCarrierComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if ("comboBoxChanged".equals(ae.getActionCommand())) {
					currPreferences = (ChargeCarrierPreferences) chargeCarrierComboBox.getSelectedItem();
					customizeButton.setEnabled(true); //currPreferences != null);
					resetButton.setEnabled(false); //currPreferences != null);
				}
			}
		});
		
		chargeCarrierComboBox.getComponent(0).setForeground(new Color(255, 0, 0));
		
		chargeCarrierPanel.add(Box.createHorizontalStrut(8));
		chargeCarrierPanel.add(chargeCarrierComboBox);
		chargeCarrierPanel.add(Box.createHorizontalStrut(8));
		chargeCarrierPanel.add(customizeButton);
		chargeCarrierPanel.add(Box.createHorizontalStrut(8));
		chargeCarrierPanel.add(resetButton);
		chargeCarrierPanel.add(Box.createHorizontalStrut(8));
	
		chargeCarrierWrapPanel = new JPanel();
		chargeCarrierWrapPanel.setLayout(new BoxLayout(chargeCarrierWrapPanel, BoxLayout.Y_AXIS));
		chargeCarrierWrapBorder = BorderFactory.createTitledBorder(" Charge Carriers for Current Mode  ");
		chargeCarrierWrapBorder.setTitleFont(boldFontForTitlePanel(chargeCarrierWrapBorder, false));
		chargeCarrierWrapPanel.setBorder(chargeCarrierWrapBorder);
		chargeCarrierWrapPanel.add(Box.createHorizontalStrut(8));
		chargeCarrierWrapPanel.add(chargeCarrierPanel);
		
		if (chargeCarrierComboBox.getItemCount() > 0)
			chargeCarrierComboBox.setSelectedIndex(chargeCarrierComboBox.getItemCount() - 1);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//add(chargeCarrierWrapPanel);
	}
	
	private void updateCarrierPreferencesForCustomization(ChargeCarrierPreferences updatedPreferences) {
		ChargeCarrierPreferences currentPref = ((ChargeCarrierPreferences) chargeCarrierComboBox.getSelectedItem());
		Map<String, ChargeCarrierPreferences> newMap = new HashMap<String, ChargeCarrierPreferences>();
		chargeCarrierComboBox.removeAllItems();
	
		List<ChargeCarrierPreferences> lst = ListUtils.makeListFromCollection(chargeCarrierPrefMap.values());
		Collections.sort(lst, new ChargeCarrierPreferenceByGroupNameComparator());
		for (ChargeCarrierPreferences pref : lst) {
			ChargeCarrierPreferences prefToRecord = !updatedPreferences.getGroupName().equals(pref.getGroupName()) ? pref : updatedPreferences;
			newMap.put(getKeyFor(pref.getGroupName()), prefToRecord);
			chargeCarrierComboBox.insertItemAt((ChargeCarrierPreferences) prefToRecord, 0);
		}
		
		if (chargeCarrierComboBox.getItemCount() > 0)
			chargeCarrierComboBox.setSelectedItem(updatedPreferences);
		
		chargeCarrierPrefMap.clear();
		for (String key : newMap.keySet()) {
			chargeCarrierPrefMap.put(key, newMap.get(key));
			anyModeMap.put(key, newMap.get(key));
		}
		ChargeCarrierMapWrapper wrapper = new ChargeCarrierMapWrapper(anyModeMap);
		BinnerUtils.setBinnerObjectProp(BinnerConstants.ANNOTATION_PROPS_FILE, "carrierPrefs", wrapper);
	
		customizeButton.setEnabled(true); //currPreferences != null);
	}
		
	public Map<String, ChargeCarrierPreferences> updateForAnnotations(Integer modeSign, Map<Integer, AnnotationInfo> annotFileMap) {
		this.modeSign = modeSign;
		
		Map<String, ChargeCarrierPreferences> tempMap = new HashMap<String, ChargeCarrierPreferences>();

		for (AnnotationInfo annotation: annotFileMap.values()) {
			Integer chg = -4; 
			try { chg = Integer.parseInt(annotation.getCharge()); } catch (Exception e) { }
			
			Boolean isChargeCarrier = !(chg.equals(0)) && ArrayUtils.contains(BinnerConstants.ANNOTATION_CHARGES, chg);
			if (!isChargeCarrier) 
				continue;
			
			String tier = annotation.getTier().trim();
			if (!ArrayUtils.contains(BinnerConstants.TIERS_ALLOWED, tier))
				return null;
			
			ChargeCarrierPreferences currPref = getAnyPreferencesFor(annotation.getAnnotation());
			if (currPref == null || !currPref.wasCustomized())
				currPref = createPreferenceForAnnotation(annotation);
			
			currPref.setTier(annotation.getTier());
			
			Boolean isCorrectMode = modeSign * chg > 0;
			String key = getKeyFor(currPref, isCorrectMode);
		 	
			if (isCorrectMode ) 
				tempMap.put(key, currPref);
		}
		
		ChargeCarrierPreferences currentPref = ((ChargeCarrierPreferences) chargeCarrierComboBox.getSelectedItem());
		chargeCarrierComboBox.removeAllItems();
		
		List<ChargeCarrierPreferences> values  = ListUtils.makeListFromCollection(tempMap.values());
		Collections.sort(values, new ChargeCarrierPreferenceByGroupNameComparator()); //, new ChargeCarrierPreferenceByGroupNameComparator());
		
		chargeCarrierPrefMap.clear();
		for (ChargeCarrierPreferences value : values) {
			String key = getKeyFor(value.getGroupName());
			recordPreferenceFor(value);
			chargeCarrierComboBox.insertItemAt((ChargeCarrierPreferences) value, 0);
		}
		
		chargeCarrierComboBox.setSelectedItem(currentPref);
		customizeButton.setEnabled(true); //currPreferences != null);
		
		ChargeCarrierMapWrapper wrapper = new ChargeCarrierMapWrapper(anyModeMap);
		BinnerUtils.setBinnerObjectProp(BinnerConstants.ANNOTATION_PROPS_FILE, "carrierPrefs", wrapper);
		
		return chargeCarrierPrefMap;
	}
	
	private String getKeyFor(ChargeCarrierPreferences pref) {
		if (pref == null)
			return null;
		
		return getKeyFor(pref.getGroupName(), true);
	}
	
	private String getKeyFor(ChargeCarrierPreferences pref, Boolean correctMode) {
		if (pref == null)
			return null;
		
		return getKeyFor(pref.getGroupName(), correctMode);
	}
	
	private String getKeyFor(String rawName) {
		return getKeyFor(rawName, true);
	}
	
	private String getKeyFor(String rawName, Boolean correctMode) {
		if (rawName == null)
			return null;
		
		String recordKey = StringUtils.cleanAndTrim(rawName);
		if (StringUtils.isEmpty(recordKey))
			return null;
		
		String value = (modeSign == 1 ? "+" : "-") + recordKey.toLowerCase();
		if (!correctMode)
			value = (modeSign == 1 ? "-" : "+") + recordKey.toLowerCase();
		
		return value;
	}
	
	private ChargeCarrierPreferences createPreferenceForAnnotation(AnnotationInfo annotation) {
		ChargeCarrierPreferences newPreference = new ChargeCarrierPreferences(annotation.getAnnotation(), annotation.getTier(), annotation.getCharge());
		switch(annotation.getTier()) {
			case "1" : 	newPreference.setAllowAsBase(BinnerConstants.TIER1_DEFAULT_ALLOW_AS_BASE);
						newPreference.setAllowAsMultimerBase(BinnerConstants.TIER1_DEFAULT_ALLOW_MULTIMER_BASE);
						newPreference.setRequireAloneBeforeCombined(BinnerConstants.TIER1_REQUIRE_ALONE);
						break;
						
			case "2" : 	newPreference.setAllowAsBase(BinnerConstants.TIER2_DEFAULT_ALLOW_AS_BASE);
						newPreference.setAllowAsMultimerBase(BinnerConstants.TIER2_DEFAULT_ALLOW_MULTIMER_BASE);
						newPreference.setRequireAloneBeforeCombined(BinnerConstants.TIER2_REQUIRE_ALONE);
						break;
		}
		return newPreference;
	}
			
	private Boolean recordPreferenceFor(ChargeCarrierPreferences pref) {
		String recordKey = getKeyFor(pref);
		
		if (recordKey == null)
			return false;
		
		chargeCarrierPrefMap.put(recordKey, pref);
		return true;
	}
	
	public Boolean preferencesRecordedFor(String key) {
		String searchStr = getKeyFor(key);
		return chargeCarrierPrefMap.containsKey(searchStr);
	}
	
	public ChargeCarrierPreferences getPreferencesFor(String key) {
		String searchStr = getKeyFor(key);
		return getChargeCarrierPrefMap().get(searchStr);
	}

	private ChargeCarrierPreferences getAnyPreferencesFor(String key) {
		String searchStr = getKeyFor(key);
		return anyModeMap.get(searchStr);
	}
	
	public Map<String, ChargeCarrierPreferences> getChargeCarrierPrefMap() {
		return chargeCarrierPrefMap;
	}
		
	public Boolean useNeutralMassesToGetBestChargeCarrier() {
		return useNMForBestCtCheckBox.isSelected();
	}
	
	public Boolean allowChargeToVary() {
		return allowChargeToVaryCheckBox.isSelected();
	}
	
	public Map<String, ChargeCarrierPreferences> getAnyModeMap() {
		return this.anyModeMap;
	}
}
	
