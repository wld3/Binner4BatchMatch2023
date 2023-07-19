////////////////////////////////////////////////////
// ChargeCarrierPreferenceByGroupNameComparator.java
// Written by Jan Wigginton May 2018
////////////////////////////////////////////////////
package edu.umich.wld;

import java.io.Serializable;
import java.util.Comparator;


public class ChargeCarrierPreferenceByGroupNameComparator implements Comparator<ChargeCarrierPreferences>, Serializable {
	public int compare(ChargeCarrierPreferences o1, ChargeCarrierPreferences o2) {
		if (o1.getGroupName() == null || o2.getGroupName() == null)
			return -1;
		String value1 = o2.getGroupName().replaceAll("^[\\d]", "");
		String value2 = o1.getGroupName().replaceAll("^[\\d]", "");
		
		if (value1.substring(0, 1).equals(value2.substring(0, 1)))
				return o1.getGroupName().substring(0, 1).compareTo(o2.getGroupName().substring(0, 1));
		
		return value1.compareTo(value2);
	}
}
