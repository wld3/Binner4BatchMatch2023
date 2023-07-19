////////////////////////////////////////////////////
// BinnerStickySettings.java
// Written by Jan Wigginton May 2018
////////////////////////////////////////////////////
package edu.umich.wld;

import java.util.HashMap;
import java.util.Map;


public class BinnerStickySettings  {
	Map<String, Boolean> outputTabPrefs;
	
	public BinnerStickySettings()  {
		this.outputTabPrefs = new HashMap<String, Boolean>();
	}

	public BinnerStickySettings(Map<String, Boolean> outputTabPrefs) {
		this.outputTabPrefs = outputTabPrefs;
	}

	public Map<String, Boolean> getOutputTabPrefs() {
		return outputTabPrefs;
	}
}
