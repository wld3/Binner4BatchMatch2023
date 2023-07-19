////////////////////////////////////////////////////
// ChargeCarrierMapWrapper.java
// Created May 2018
////////////////////////////////////////////////////
package edu.umich.wld;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class ChargeCarrierMapWrapper implements Serializable
	{
	private Map<String, ChargeCarrierPreferences> map;
	
	public ChargeCarrierMapWrapper() 
		{
		this.map = new HashMap<String, ChargeCarrierPreferences>();
		}
	
	public ChargeCarrierMapWrapper(Map<String, ChargeCarrierPreferences> map)
		{
		this.map = map;
		}

	public Map<String, ChargeCarrierPreferences> getMap()
		{
		return map;
		}
	}
