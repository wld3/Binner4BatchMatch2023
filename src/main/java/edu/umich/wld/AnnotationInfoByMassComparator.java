////////////////////////////////////////////////////
// AnnotationInfoByMassComparator.java
// Created May 2018
////////////////////////////////////////////////////
package edu.umich.wld;

import java.io.Serializable;
import java.util.Comparator;


public class AnnotationInfoByMassComparator implements Comparator<AnnotationInfo>, Serializable {
	public int compare(AnnotationInfo o1, AnnotationInfo o2) {

		if (o1.getMass() == null || o2.getMass() == null)
			return -1;

		if (o1.getMode().equals(o2.getMode()))
			{
			Double value1 = Math.abs(o1.getMass().doubleValue());
			Double value2 = Math.abs(o2.getMass().doubleValue());
			return value1.compareTo(value2);
			}
		else if ("both".equals(o1.getMode().toLowerCase()))
			return 1;
		else if ("both".equals(o2.getMode().toLowerCase()))
			return -1;
		else if ("negative".equals(o1.getMode().toLowerCase()))
			return 1;
		
		return -1;
		}
	}
