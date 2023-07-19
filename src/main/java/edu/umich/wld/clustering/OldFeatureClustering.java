package edu.umich.wld.clustering;

import java.util.ArrayList;
import java.util.List;

public class OldFeatureClustering
	{
	private List<List<Integer>> clusters;
	private double [][] distances, linkages;
	private double [][] correlations;
	private double minClusters = 1;
	private int nClustersRemaining = 0;
	private int nFeatures = 0;
	
	private long msInMerge, msInUpdate;
	
	public OldFeatureClustering(double [][] correlations)
    	{
    	this.correlations = correlations;
    	}
	
	public void runClustering() throws Exception
		{
		long t0 = System.currentTimeMillis();
		msInMerge = 0L;
		msInUpdate = 0L;
		
		nFeatures = correlations[0].length;
		System.out.println("Clustering a bin with " + nFeatures + " features ... ");
    	nClustersRemaining = nFeatures;
		minClusters = Math.ceil(Math.sqrt(nFeatures));
	 	initializeClusters();
		initializeDistances();
	    initializeLinkages();
	    while (!isConverged())
	    	{
	    	Integer idxToUpdate = determineClosestClusterPairAndMerge();
	        updateLinkages(idxToUpdate);
	    	}
	    
	    long t1 = System.currentTimeMillis();
	    
	    System.out.println("Done!");
	    System.out.println("Merging took " + msInMerge / 1000.0 + " seconds overall.");
	    System.out.println("Updating took " + msInUpdate / 1000.0 + " seconds overall.");
	    System.out.println("runClustering() took " + (t1 - t0) / 1000.0 + " seconds.");
		}

	
	public void initializeClusters()
    	{
		clusters = new ArrayList<List<Integer>>();
    	for (int i = 0; i < nFeatures; i++)
    		{
    		List<Integer> members = new ArrayList<Integer>();
    		members.add(i);
    		clusters.add(members);
    		}
    	}
		
	private void initializeDistances() throws Exception
    	{
		long t0 = System.currentTimeMillis();
		
        distances = new double[nFeatures - 1][];
    	for (int i = 0; i < nFeatures - 1; i++) 
    		{
    		double [] row = new double[nFeatures - i - 1];
    		for (int j = i + 1; j < nFeatures; j++)
    	       row[j - i - 1] = CustomCalculations.calculateEuclidianDistance(correlations[i], correlations[j]);      
    		distances[i] = row;
    		}
    		
    	long t1 = System.currentTimeMillis();
	    
	    System.out.println("createDistances() took " + (t1 - t0) / 1000.0 + " seconds.");
    	}
	
	public void initializeLinkages()
		{
		long t0 = System.currentTimeMillis();
	
		linkages = new double [nFeatures][];		
		for (int i = 0; i < nFeatures - 1; i++)
			{
			double [] linkRow = new double[nFeatures - i - 1];
			double [] distRow = distances[i];	
			for (int j = 0; j < nFeatures - i - 1; j++)
				linkRow[j] = distRow[j];
			linkages[i] = linkRow;
			}
	
		long t1 = System.currentTimeMillis();    
		System.out.println("initializeLinkages() took " + (t1 - t0) / 1000.0 + " seconds.");
		}
	
	private Boolean isConverged()
		{
		return nClustersRemaining <= minClusters;
		}
	
	public Integer determineClosestClusterPairAndMerge() throws Exception
		{
		long t0 = System.currentTimeMillis();
		
		double currentMin = Double.MAX_VALUE;
		Integer idx1ToMerge = null, idx2ToMerge = null;
	
		for (int i = 0; i < nFeatures - 1; i++)
			{
			if (clusters.get(i).isEmpty())
				continue;
			
			double [] row = linkages[i];
			for (int j = 0; j < nFeatures - i - 1; j++)
				{
				double currentLinkage = row[j];
				if (currentLinkage < currentMin && !clusters.get(j + i + 1).isEmpty())
					{
					idx1ToMerge = i;
					idx2ToMerge = j + i + 1;
					currentMin = currentLinkage;
					}
				}
			}

		combineClusters(clusters.get(idx1ToMerge), clusters.get(idx2ToMerge), idx2ToMerge);
  
		long t1 = System.currentTimeMillis();
		msInMerge += t1 - t0;
	
		return idx1ToMerge;
		}

	private void combineClusters(List<Integer> clusterToMerge1, List<Integer> clusterToMerge2, 
			Integer clusterIdxToRemove)
		{
		for (Integer member : clusterToMerge2)
			clusterToMerge1.add(member);
		   
		clusters.get(clusterIdxToRemove).clear();
		nClustersRemaining--;
		}

	public void updateLinkages(Integer mergedClusterIdx) throws Exception
		{
		long t0 = System.currentTimeMillis();
	
		List<Integer> clusterFrom = clusters.get(mergedClusterIdx);
		
		for (int idx = 0; idx < mergedClusterIdx; idx++)
			{
			if (clusters.get(idx).isEmpty())
				continue;
			
			linkages[idx][mergedClusterIdx - idx - 1] = calculatePairLinkageWith(clusters.get(idx), clusterFrom);
			}
		
		for (int idx = mergedClusterIdx + 1; idx < nFeatures; idx++)
			{
			if (clusters.get(idx).isEmpty())
				continue;
			
			linkages[mergedClusterIdx][idx - mergedClusterIdx - 1] = calculatePairLinkageWith(clusterFrom, clusters.get(idx));
			}
		
		long t1 = System.currentTimeMillis();
		msInUpdate += t1 - t0;
	}
   
	public Double calculatePairLinkageWith(List<Integer> cluster, List<Integer> other)
		{
		double linkage = 0.0;		
		
		for (Integer featureIdx : cluster)
			for (Integer otherFeatureIdx : other)
				linkage += (featureIdx < otherFeatureIdx ? distances[featureIdx][otherFeatureIdx - featureIdx - 1] :
						distances[otherFeatureIdx][featureIdx - otherFeatureIdx - 1]);
		
		linkage /= (cluster.size() * other.size() + 0.0);
		
		return linkage;
		} 
	
	public List<List<Integer>> getClusters()
		{
		return clusters;
		}
	
	//MERGE 05/08
	public List<Integer> getClusterCounts()
		{
		List<Integer> clusterSizes = new ArrayList<Integer>();
		
		for (int i = 0; i < getClusters().size(); i++)
		    if (clusters.get(i).size() > 0)
		    	clusterSizes.add(clusters.get(i).size());
		
		return clusterSizes;
		}
	}
