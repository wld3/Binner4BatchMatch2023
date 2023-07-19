package edu.umich.wld.clustering;

import java.util.HashMap;
import java.util.Map;

public class RTClusteringPostProcessor 
	{
	RTClustering clustering = null;
	int smoothingAdjustment;
	int positiveClusterOffset, negativeClusterOffset;
	double minGap, maxGap;
	
	public RTClusteringPostProcessor() { } 
	
	
	public RTClusteringPostProcessor(RTClustering clustering, double minGap, double maxGap) 
		{
		this.clustering = clustering;
		this.minGap = minGap;
		this.maxGap = maxGap;
		}
	
	
	public Integer getSmoothedNClusters()
		{
		if (clustering.getNClusters() <= 0)
			return null;
		return clustering.getNClusters() + positiveClusterOffset + negativeClusterOffset;
		}
	

	public Integer[] getRenumberedClusterAssignments(Boolean info)
		{
		Integer [] initialClusterData = new Integer[clustering.getRetentionTimes().length];
		
		for (int clusterIndex = 0; clusterIndex < clustering.getRetentionTimes().length; clusterIndex++) {
			for (Integer featureIndex : clustering.getClusters().get(clusterIndex)) {
				initialClusterData[featureIndex] = clusterIndex;
			}
		}
		
		Integer [] newClusterData = new Integer[initialClusterData.length];
		Map<Integer, Integer> clusterMap = new HashMap<Integer, Integer>();
		int nextAvailableNumber = 1;
		for (int i = 0; i < initialClusterData.length; i++) 
			{
			Integer newClusterNumber = clusterMap.get(initialClusterData[i]);
			if (newClusterNumber == null) 
				{
				clusterMap.put(initialClusterData[i], nextAvailableNumber);
				newClusterData[i] = nextAvailableNumber;
				nextAvailableNumber++;
				} 
			else 
				newClusterData[i] = newClusterNumber;
			}
		
		if (info)
			{
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < newClusterData.length;i++)
				sb.append(newClusterData[i] + ", ");
			System.out.println(" Cluster assignments before smoothhhh : " + sb.toString());
			}
		Integer [] smoothedClusterData = smoothOrSplitClusters(info, newClusterData, true, this.minGap, this.maxGap);
		
		if (info)
			{
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < smoothedClusterData.length;i++)
				sb.append(smoothedClusterData[i] + ", ");
			System.out.println(" Cluster assignments before splitttt : " + sb.toString());
			}
		Integer [] splitClusterData = smoothOrSplitClusters(info, smoothedClusterData, false, this.minGap, this.maxGap);
		
		return splitClusterData;
		}
		
		
	private Integer [] smoothOrSplitClusters(Boolean info, Integer [] uncorrectedClusterData, Boolean smooth, 
		Double minAllowableGap, Double maxAllowableGap)
		{
		double [] retentionTimes = clustering.getRetentionTimes();
		Integer [] updatedClusterData = new Integer [uncorrectedClusterData.length];
		
		int clusterOffset = 0;
		
		if (uncorrectedClusterData.length > 0)
			updatedClusterData[0] = uncorrectedClusterData[0];
		for (int k = 1; k < uncorrectedClusterData.length; k++) 
			{
			if (smooth)
				{
				if (uncorrectedClusterData[k].equals(uncorrectedClusterData[k - 1]) && (retentionTimes[k] - retentionTimes[k-1]) > maxAllowableGap)
					{
					Double gap = retentionTimes[k] - retentionTimes[k - 1];
					if (info) System.out.println(" Greater than gap was " + gap);
					clusterOffset++;
					}
				}
			else
				{
				if (!uncorrectedClusterData[k].equals(uncorrectedClusterData[k - 1]) && (retentionTimes[k] - retentionTimes[k-1]) < minAllowableGap)
					{
					Double gap = retentionTimes[k] - retentionTimes[k - 1];
					if (info && gap < minAllowableGap ) System.out.println(" Less than gap was " + gap);
					clusterOffset--;
					}
				}
			updatedClusterData[k] = uncorrectedClusterData[k] + clusterOffset;
			if (updatedClusterData[k] < 1) updatedClusterData[k] = 1;
			}

		if (smooth) 
			this.positiveClusterOffset = clusterOffset;
		else
			this.negativeClusterOffset = clusterOffset;
		
		if (info)
			{
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < updatedClusterData.length;i++)
				sb.append(updatedClusterData[i] + ", ");
			 System.out.println("Cluster offset is " + clusterOffset + " Cluster assignments after "+ (smooth ? "smooth" : "split") + " : " + sb.toString());

			for (int i = 1; i < updatedClusterData.length;i++)
				if (updatedClusterData[i] - updatedClusterData[i- 1] > 1 )
					System.out.println("Illegal gap for cluster assignments");
			
			}
		return updatedClusterData;
		}
		}
