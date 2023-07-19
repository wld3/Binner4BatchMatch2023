////////////////////////////////////////////////////
// ChargeCarrierPreferences.java
// Created May 2018
////////////////////////////////////////////////////
package edu.umich.wld;

import java.io.Serializable;


public class ChargeCarrierPreferences implements Serializable
	{
	String groupName,  tier, charge;
	
	Boolean allowAsBase = true, allowAsMultimerBase = true;
	Boolean requireAloneBeforeCombined = true;
	
	public ChargeCarrierPreferences() { } 
	
	public ChargeCarrierPreferences(String groupName, String tier, String charge) 
		{  
		this.groupName = groupName;
		this.tier = tier;
		allowAsBase = true;
		allowAsMultimerBase = true;
		requireAloneBeforeCombined = true;
		this.charge = charge;
		}
	
	public void setAllowAsBase(Boolean allowAsBase) {
		this.allowAsBase = allowAsBase;
	}

	public void setAllowAsMultimerBase(Boolean allowAsMultimerBase) {
		this.allowAsMultimerBase = allowAsMultimerBase;
	}

	public void setRequireAloneBeforeCombined(Boolean requireAloneBeforeCombined) {
		this.requireAloneBeforeCombined = requireAloneBeforeCombined;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public Boolean getAllowAsBase() {
		return allowAsBase;
	}

	public void setAllowAsBase(boolean allowAsBase) {
		this.allowAsBase = allowAsBase;
	}

	public Boolean getAllowAsMultimerBase() {
		return allowAsMultimerBase;
	}

	public void setAllowAsMultimerBase(boolean allowAsMultimerBase) {
		this.allowAsMultimerBase = allowAsMultimerBase;
	}

	public Boolean getRequireAloneBeforeCombined() {
		return requireAloneBeforeCombined;
	}

	public void setRequireAloneBeforeCombined(boolean requireAloneBeforeCombined) {
		this.requireAloneBeforeCombined = requireAloneBeforeCombined;
	}
	
	public Boolean wasCustomized()
		{
		Boolean isCustomized = false;
		switch(getTier())
			{
			case "1" : 	isCustomized = 
					     !getAllowAsBase().equals(BinnerConstants.TIER1_DEFAULT_ALLOW_AS_BASE) ||
						 !getAllowAsMultimerBase().equals(BinnerConstants.TIER1_DEFAULT_ALLOW_MULTIMER_BASE) ||
						 !getRequireAloneBeforeCombined().equals(BinnerConstants.TIER1_REQUIRE_ALONE);
						break;
			case "2" : isCustomized = 
					!getAllowAsBase().equals(BinnerConstants.TIER2_DEFAULT_ALLOW_AS_BASE) ||
					 !getAllowAsMultimerBase().equals(BinnerConstants.TIER2_DEFAULT_ALLOW_MULTIMER_BASE) ||
					 !getRequireAloneBeforeCombined().equals(BinnerConstants.TIER2_REQUIRE_ALONE);
					break;
			}
		return isCustomized;
		}
	
	
	public void reset()
		{
		switch(getTier())
			{
			case "1" : 
					     setAllowAsBase(BinnerConstants.TIER1_DEFAULT_ALLOW_AS_BASE);
					     setAllowAsMultimerBase(BinnerConstants.TIER1_DEFAULT_ALLOW_MULTIMER_BASE);
					     setRequireAloneBeforeCombined(BinnerConstants.TIER1_REQUIRE_ALONE);
						 break;
			case "2" : 
				
				setAllowAsBase(BinnerConstants.TIER2_DEFAULT_ALLOW_AS_BASE);
			     setAllowAsMultimerBase(BinnerConstants.TIER2_DEFAULT_ALLOW_MULTIMER_BASE);
			     setRequireAloneBeforeCombined(BinnerConstants.TIER2_REQUIRE_ALONE);
				 break;
			}
		}
	
	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}
	
	@Override
	public String toString() 
		{
		return getGroupName() + (wasCustomized() ? "\t\t(Custom" : "\t\t(Tier" + getTier()) + ")";
		}
	
	public String printObject()
		{
		return ObjectHandler.printObject(this);
		}

	
	}
	
