////////////////////////////////////////////////////
// FeatureByBinClusterAndRtComparator.java
// Written by Jan Wigginton September 2018
////////////////////////////////////////////////////
package edu.umich.wld;

import java.io.Serializable;
import java.util.Comparator;

import edu.umich.wld.Feature;


public class FeatureByBinOffsetComparator  implements Comparator<Feature>, Serializable
	{
	public int compare(Feature o1, Feature o2) 
		{
		if (o1 == null || o2 == null)
			return -1;
		
		if (o1.getOffsetWithinBin() == null)
			{
			System.out.println("bin is null");
			//o1.setOffsetWithinBin(1);
			return -1;
			}
		
		if (o2.getOffsetWithinBin() == null)
			{
			System.out.println("bin is null");
			o2.setOffsetWithinBin(2);
			return 1;
			}
		
		return o1.getOffsetWithinBin().compareTo(o2.getOffsetWithinBin());
			
		/*if (o1.getOldCluster() == null || o2.getOldCluster() == null)
			{
			System.out.println("old cluster " + o1.getName() + " and " + o2.getName() + " is null");
			
			return -1;
			}
		
		if (o1.getNewCluster() == null || o2.getNewCluster() == null)
			{
			System.out.println("new cluster is null");
			return -1;
			}
		
		if (o1.getRT() == null || o2.getRT() == null)
			{
			System.out.println("rt cluser is null");
			return -1;
			}

		
		if (o1.getBinIndex().equals(o2.getBinIndex())) 
			{
			if (o1.getOldCluster().equals(o2.getOldCluster()))	
				{
				if (o1.getNewCluster().equals(o2.getNewCluster()))
					{
					if (o1.getNewNewCluster() == null || o2.getNewNewCluster() == null)
						{
						if (o1.getRT().equals(o2.getRT()))
							return -1;
						
						return o1.getRT().compareTo(o2.getRT());
						}
					
					if (o1.getNewNewCluster().equals(o2.getNewNewCluster()))
						{
						if (o1.getRT().equals(o2.getRT()))
							return -1;
						
						return o1.getRT().compareTo(o2.getRT());
						}
					
					return o1.getNewNewCluster().compareTo(o2.getNewNewCluster());
					}
				return o1.getNewCluster().compareTo(o2.getNewCluster());
				}
			
			return o1.getOldCluster().compareTo(o2.getOldCluster());
			}
			
		return o1.getBinIndex().compareTo(o2.getBinIndex()); */
		}
	}
	



