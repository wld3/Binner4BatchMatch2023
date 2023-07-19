////////////////////////////////////////////////////
// GroupStatistics.java
// Written by Jan Wigginton May 2018
////////////////////////////////////////////////////
package edu.umich.wld;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class GroupStatistics implements Serializable
	{
	public enum GroupStatType {FEATURES_PER_BIN, CLUSTERS_PER_BIN_BEFORE, CLUSTERS_PER_BIN_AFTER_REBIN, CLUSTERS_PER_BIN_AFTER_SUBCLUSTER, 
		FEATURES_PER_CLUSTER_BEFORE,  FEATURES_PER_CLUSTER_AFTER_REBIN, FEATURES_PER_CLUSTER_AFTER_SUBCLUSTER};
		
	private List<BinStats> binStats;
	private Boolean useRebinStat;
	
	private HashMap<Integer, Integer> clusterSizeDistributionBefore, clusterSizeDistributionAfterRebin, clusterSizeDistributionAfterSubCluster; 
	private HashMap<Integer, Integer> binSizeDistribution; 
	private HashMap<Integer, Integer> clusterCtPerBinBefore, clusterCtPerBinAfterRebin, clusterCtPerBinAfterSubCluster;
	
	private Integer maxBinSize = null, maxClusterSize = null; 
	private Integer minBinSize = null, minClusterSize = null;
	private Integer maxClustersPerBin = null, minClustersPerBin = null;
	private Integer totalNBins = null, totalNClusters = null;
	
	private Integer overallMaxClusterSize = null, overallMaxBinSize = null, overallMaxNClustersPerBin = null;
	private Double averageBinSize = null, averageClusterSize = null, averageClustersPerBin = null;
	private List<Integer> binSizeKeys = null, clusterSizeKeys= null, clusterPerBinCtKeys = null;
	 
	public GroupStatistics(Boolean useRebinStatisticsForAnnotated)  {  
		this(null, useRebinStatisticsForAnnotated);
		useRebinStat = useRebinStatisticsForAnnotated;
	}
		
	public GroupStatistics(List<BinStats> binStats,Boolean useRebinStatisticsForAnnotated )  {
		this.binStats = binStats == null ? new ArrayList<BinStats>() : binStats;
		useRebinStat = useRebinStatisticsForAnnotated;
		clusterSizeDistributionBefore = new HashMap<Integer, Integer>();
		clusterSizeDistributionAfterRebin = new HashMap<Integer, Integer>();
		clusterSizeDistributionAfterSubCluster = new HashMap<Integer, Integer>();
		
		binSizeDistribution = new HashMap<Integer, Integer>();
		
		clusterCtPerBinBefore = new HashMap<Integer, Integer>();
		clusterCtPerBinAfterRebin = new HashMap<Integer, Integer>();
		clusterCtPerBinAfterSubCluster = new HashMap<Integer, Integer>();
		
		binSizeKeys = new ArrayList<Integer>();
		clusterSizeKeys = new ArrayList<Integer>();
		clusterPerBinCtKeys = new ArrayList<Integer>();
	}
	
	public Integer getIthDataPointForStat(int i, GroupStatType statType, Boolean getIdx) {
		List<Integer> idx = null;
		HashMap<Integer, Integer> targetDistribution = null;
	
		switch (statType) {
			case FEATURES_PER_BIN : 			        idx = binSizeKeys; targetDistribution = this.binSizeDistribution;  break;
 			case CLUSTERS_PER_BIN_BEFORE :  	        idx = clusterPerBinCtKeys;  targetDistribution =  clusterCtPerBinBefore; break;
			case CLUSTERS_PER_BIN_AFTER_REBIN : 		idx = clusterPerBinCtKeys; targetDistribution =  clusterCtPerBinAfterRebin; break; 
			case CLUSTERS_PER_BIN_AFTER_SUBCLUSTER :    idx = clusterPerBinCtKeys; targetDistribution =  clusterCtPerBinAfterSubCluster; break; 
			
			case FEATURES_PER_CLUSTER_BEFORE : 	         idx = clusterSizeKeys; targetDistribution = clusterSizeDistributionBefore; break;
			case FEATURES_PER_CLUSTER_AFTER_REBIN : 	 idx = clusterSizeKeys; targetDistribution = clusterSizeDistributionAfterRebin; break;
			case FEATURES_PER_CLUSTER_AFTER_SUBCLUSTER : idx = clusterSizeKeys; targetDistribution = clusterSizeDistributionAfterSubCluster; break;
		}
		
		if (idx == null || i >= idx.size())
			return null;
		
		if (getIdx)
			return idx.get(i);
		return targetDistribution.containsKey(idx.get(i)) ? targetDistribution.get(idx.get(i)) : 0;
	}
	
	public void setBinStatistics(List<BinStats> binStats) {
		this.binStats = binStats;
	}
	
	public void calculateDistributions() {
		calculateClusterSizeDistributions();
		calculateBinSizeDistribution();
		calculateClusterPerBinDistributions();
	 }
	
	public void calculateClusterPerBinDistributions() {
		clusterCtPerBinBefore = new HashMap<Integer, Integer>();
		clusterCtPerBinAfterRebin = new HashMap<Integer, Integer>();
		clusterCtPerBinAfterSubCluster = new HashMap<Integer, Integer>();
		
		Integer clusterPerBinSum = 0;
		maxClustersPerBin = Integer.MIN_VALUE;
		minClustersPerBin = Integer.MAX_VALUE;
		this.overallMaxNClustersPerBin = Integer.MIN_VALUE;
		
		for (BinStats stat : binStats)	{
			int currCt = stat.getOriginalClusterSizes().size();
			int nCurrInstances = clusterCtPerBinBefore.containsKey(currCt) ? clusterCtPerBinBefore.get(currCt) : 0;
		    clusterCtPerBinBefore.put(currCt, nCurrInstances + 1);

		    currCt = stat.getRebinClusterSizes().size();
			nCurrInstances = clusterCtPerBinAfterRebin.containsKey(currCt) ?  clusterCtPerBinAfterRebin.get(currCt) : 0;
			clusterCtPerBinAfterRebin.put(currCt, nCurrInstances + 1);
	
		    if (useRebinStat) {
		    	if (currCt > 0 && currCt < minClustersPerBin)
			    	minClustersPerBin = currCt;
			    
			    if (currCt > maxClustersPerBin)
			    	maxClustersPerBin = currCt;
			    
			    clusterPerBinSum += currCt;
		    }
		    
		    currCt = stat.getReclusterClusterSizes().size();
			nCurrInstances = clusterCtPerBinAfterSubCluster.containsKey(currCt) ?  clusterCtPerBinAfterSubCluster.get(currCt) : 0;
			clusterCtPerBinAfterSubCluster.put(currCt, nCurrInstances + 1);
		    
			if (!useRebinStat) {
			    if (currCt > 0 && currCt < minClustersPerBin)
			    	minClustersPerBin = currCt;
			    
			    if (currCt > maxClustersPerBin)
			    	maxClustersPerBin = currCt;
			    
			    clusterPerBinSum += currCt;
				}
		}
		this.totalNClusters = clusterPerBinSum;
		// Number per bin is same or greater after rebin
		overallMaxNClustersPerBin = maxClustersPerBin;
		averageClustersPerBin = clusterPerBinSum / (binStats.size() * 1.0);
		
		List<Integer> beforeCts = ListUtils.makeListFromCollection(clusterCtPerBinBefore.keySet());
		List<Integer> afterRebinCts = ListUtils.makeListFromCollection(clusterCtPerBinAfterRebin.keySet());
		List<Integer> afterSubClusterCts = ListUtils.makeListFromCollection(clusterCtPerBinAfterSubCluster.keySet());
		
		List<Integer> allSizes = new ArrayList<Integer>();
		for (int i = 0; i < beforeCts.size(); i++)
			allSizes.add(beforeCts.get(i));
		
		for (int i = 0; i < afterRebinCts.size(); i++)
			allSizes.add(afterRebinCts.get(i));
		
		if (!useRebinStat)
			for (int i = 0; i < afterSubClusterCts.size(); i++)
				allSizes.add(afterSubClusterCts.get(i));
		
		clusterPerBinCtKeys = ListUtils.uniqueEntries(allSizes);
		Collections.sort(clusterPerBinCtKeys);
	}
	
	
	public void calculateClusterSizeDistributions() {
		clusterSizeDistributionBefore = new HashMap<Integer, Integer>();
		clusterSizeDistributionAfterRebin = new HashMap<Integer, Integer>();
		clusterSizeDistributionAfterSubCluster = new HashMap<Integer, Integer>();
	    
	    Double clusterSizeSum = 0.0; 
	    maxClusterSize = Integer.MIN_VALUE;
	    minClusterSize = Integer.MAX_VALUE;
	    overallMaxClusterSize = Integer.MIN_VALUE;
		
	    if (binStats != null)
	    	for (BinStats stat :  binStats)  {
			
				stat.initializeClusterSizes();
				
				for (int j = 0; j < stat.getOriginalClusterSizes().size(); j++) {
					Integer currCt = stat.getOriginalClusterSizes().get(j);
				//	System.out.println("Current count i s " + currCt);
					Integer nCurrInstances =  clusterSizeDistributionBefore.containsKey(currCt) ? clusterSizeDistributionBefore.get(currCt) : 0;
				    clusterSizeDistributionBefore.put(currCt, nCurrInstances + 1);
				    if (currCt > overallMaxClusterSize)
				    	overallMaxClusterSize = currCt;
				}
				
				for (int j = 0; j < stat.getRebinClusterSizes().size(); j++) {
					int currCt = stat.getRebinClusterSizes().get(j);
				    int  nCurrInstances = clusterSizeDistributionAfterRebin.containsKey(currCt) ? clusterSizeDistributionAfterRebin.get(currCt) : 0;
				    clusterSizeDistributionAfterRebin.put(currCt, nCurrInstances + 1);
				    clusterSizeSum += currCt;
				    if (currCt > maxClusterSize)
				    	maxClusterSize = currCt;
				    
				    if (currCt > 0 && currCt < minClusterSize)
				    	minClusterSize = currCt;
				}
				
				
				if (!useRebinStat) {
					clusterSizeSum = 0.0;
					for (int j = 0; j < stat.getReclusterClusterSizes().size(); j++) {
						int currCt = stat.getReclusterClusterSizes().get(j);
					    int  nCurrInstances = clusterSizeDistributionAfterSubCluster.containsKey(currCt) ? clusterSizeDistributionAfterSubCluster.get(currCt) : 0;
					    clusterSizeDistributionAfterSubCluster.put(currCt, nCurrInstances + 1);
					    
					    clusterSizeSum += currCt;
					    if (currCt > maxClusterSize)
					    	maxClusterSize = currCt;
					    
					    if (currCt > 0 && currCt < minClusterSize)
					    	minClusterSize = currCt;
					}
				}
				
				
		this.averageClusterSize = clusterSizeSum / (binStats.size() * 1.0);	
		}
			
		List<Integer> beforeSizes = ListUtils.makeListFromCollection(clusterSizeDistributionBefore.keySet());
		List<Integer> afterRebinSizes = ListUtils.makeListFromCollection(clusterSizeDistributionAfterRebin.keySet());
		List<Integer> afterAfterSubClusterSizes = ListUtils.makeListFromCollection(clusterSizeDistributionAfterSubCluster.keySet());
		
		List<Integer> allSizes = new ArrayList<Integer>();
		for (int i = 0; i < beforeSizes.size(); i++)
			allSizes.add(beforeSizes.get(i));
		
		for (int i = 0; i < afterRebinSizes.size(); i++)
			allSizes.add(afterRebinSizes.get(i));
		
		for (int i = 0; i < afterAfterSubClusterSizes.size(); i++)
			allSizes.add(afterAfterSubClusterSizes.get(i));
		
		clusterSizeKeys = ListUtils.uniqueEntries(allSizes);
		Collections.sort(clusterSizeKeys);
	}	
	
	public void calculateBinSizeDistribution() {
		
		binSizeDistribution  = new HashMap<Integer, Integer>();
		Double binSizeSum = 0.0; 
		maxBinSize = Integer.MIN_VALUE;
		minBinSize = Integer.MAX_VALUE;
		
		if (binStats != null)
	    for (BinStats binstat : binStats) {
	    	int currCt = binstat.getRebinClusterIndices().size() + binstat.getRebinClusterIndicesForIsotopes().size();
	    	int nInstances =  binSizeDistribution.containsKey(currCt) ? binSizeDistribution.get(currCt) : 0;
	    	if (currCt > 0)
	    		binSizeDistribution.put(currCt, nInstances + 1);
	    	 
	    	binSizeSum += currCt;
	    	if (currCt > 0 && currCt < minBinSize)
	    		minBinSize = currCt;
	    	
	    	if (currCt > maxBinSize)
	    		maxBinSize = currCt;
	    	
	    }
	    averageBinSize = binSizeSum.doubleValue() / (binStats.size() * 1.0);
	    overallMaxBinSize = maxBinSize;
	    binSizeKeys = ListUtils.makeListFromCollection(binSizeDistribution.keySet());
	    Collections.sort(binSizeKeys);
	}

	public Integer getMaxBinSize() {
		if (maxBinSize == null)
			calculateBinSizeDistribution();
		return maxBinSize;
	}

	public Integer getMaxClusterSize() {
		if (maxClusterSize == null)
			calculateClusterSizeDistributions();
		return maxClusterSize;
	}

	public Integer getMinBinSize() {
		if (minBinSize == null)
			calculateBinSizeDistribution();
		return minBinSize;
	}

	public Integer getMinClusterSize() {
		if (minClusterSize == null)
			calculateClusterSizeDistributions();
		return minClusterSize;
	}

	public Integer getMaxClustersPerBin() {
		if (maxClustersPerBin == null)
			calculateClusterPerBinDistributions();
		return maxClustersPerBin;
	}

	public Integer getMinClustersPerBin() {
		if (minClustersPerBin == null)
			calculateClusterPerBinDistributions();
		return minClustersPerBin;
	}

	public Double getAverageBinSize() {
		if (averageBinSize == null)
			calculateBinSizeDistribution();
		return averageBinSize;
	}

	public Double getAverageClusterSize() {
		if (averageClusterSize == null)
			calculateClusterSizeDistributions();
		return averageClusterSize;
	}

	public Double getAverageClustersPerBin() {
		if (averageClustersPerBin == null)
			calculateClusterPerBinDistributions();
		return averageClustersPerBin;
	}

	public Integer getTotalNBins() {
		totalNBins = binStats.size();
		return totalNBins;
	}

	public Integer getTotalNClusters() {
		return totalNClusters;
	}

	public Integer getOverallMaxClusterSize() {
		if (overallMaxClusterSize == null)
			calculateClusterSizeDistributions();
		return overallMaxClusterSize;
	}

	public Integer getOverallMaxBinSize() {
		if (overallMaxBinSize == null)
			calculateBinSizeDistribution();
		return overallMaxBinSize;
	}

	public Integer getOverallMaxNClustersPerBin() {
		if (overallMaxNClustersPerBin == null)
			calculateClusterPerBinDistributions();
		return overallMaxNClustersPerBin;
	}

	public List<Integer> getBinSizeKeys() {
		if (ListUtils.isEmptyOrNull(binSizeKeys))
			calculateBinSizeDistribution();
		return binSizeKeys;
	}

	public List<Integer> getClusterSizeKeys() {
		if (ListUtils.isEmptyOrNull(clusterSizeKeys))
			calculateClusterSizeDistributions();
		return clusterSizeKeys;
	}

	public List<Integer> getClusterPerBinCtKeys() {
		if (ListUtils.isEmptyOrNull(clusterPerBinCtKeys))
			calculateClusterPerBinDistributions();
		return clusterPerBinCtKeys;
	}
	
	public String toString()
		{
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < binStats.size(); i++)
			sb.append("Bin " + i + " " + binStats.get(i));
	
		return sb.toString();
		}
	}
	