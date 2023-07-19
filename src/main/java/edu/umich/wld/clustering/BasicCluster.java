////////////////////////////////////////////////////
// BasicCluster.java
// Written by Jan Wigginton, Jul 20, 2017
////////////////////////////////////////////////////
package edu.umich.wld.clustering;

import java.util.ArrayList;
import java.util.List;

public class BasicCluster
	{
	private int clusterIdx;
	private List<FeatureCorrelations> clusterMembers;
	
	public BasicCluster(int clusterIdx, FeatureCorrelations firstMember)
		{
		this.clusterIdx = clusterIdx;
		clusterMembers = new ArrayList<FeatureCorrelations>();
		clusterMembers.add(firstMember);
		}

	public void addMember(FeatureCorrelations pair)
		{
		clusterMembers.add(pair);
		}
	
	public int getClusterIdx()
		{
		return clusterIdx;
		}

	public List<FeatureCorrelations> getClusterMembers()
		{
		return clusterMembers;
		}

	public void setClusterIdx(int clusterIdx)
		{
		this.clusterIdx = clusterIdx;
		}
	
	public void setClusterMembers(List<FeatureCorrelations> clusterMembers)
		{
		this.clusterMembers = clusterMembers;
		}
	
	public String toString()
		{
		return clusterIdx + " with N members " + this.clusterMembers.size();
		}
	}
