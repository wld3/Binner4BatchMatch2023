////////////////////////////////////////////////////
// FeatureClustering.java
// Written by Jan Wigginton, Jul 20, 2017
////////////////////////////////////////////////////
package edu.umich.wld.clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RTClustering
	{
	private List<List<Integer>> clusters;
	private double [][] distances, linkages;
	//private double [][] correlations;
	//MERGE 03/29
	private double [] retentionTimes;
	private Boolean doRTClustering;
	
	private double minClusters = 1;
	private int nClustersRemaining = 0, nFeatures = 0;
	private int smoothingAdjustment;
	
	private Boolean useSilhouette = false; 
	private Boolean doUnweighted = false;
	private Silhouette silhouette;
	private List<Map<Integer, Double>> featureByClusterAverages;
	private List<Double> featureWinClusterDistances;
	
	private Boolean reportingOn;
	private long msInMerge, msInUpdate;
	
	private Map<Integer, Integer> clusterCountsByBin; 
	
	
/*	public RTClustering(double [][] correlations)
    	{
    	//this.correlations = correlations;
    	this.retentionTimes = null;
    	this.clusterCountsByBin = new HashMap<Integer, Integer>();
    	this.doRTClustering = false;
    	} */
	
	public RTClustering(List<Double> rts)
		{
		this.retentionTimes = new double[rts.size()];
		for (int k = 0; k < rts.size(); k++)
			this.retentionTimes[k] = rts.get(k);
		
		this.doRTClustering = true;
		this.clusterCountsByBin = new HashMap<Integer, Integer>();
		}
	
	public RTClustering(double [] rts)
		{
		this.retentionTimes = rts;
		this.doRTClustering = true;
		//this.correlations = null;
		this.clusterCountsByBin = new HashMap<Integer, Integer>();
		}
	
	
	public void runClustering(boolean doUnweighted)  throws Exception
		{
		try
			{
			runClustering(false, doUnweighted);
			}
		catch (Exception e)
			{
			if (reportingOn)
				System.out.println(e.getMessage());
			throw e;
			}
		}
	
	public void runClustering(boolean report, boolean doUnweighted) throws Exception
		{
		//System.out.println("\nStarting a clustering cycle");
		this.reportingOn = report;
		this.doUnweighted = doUnweighted;
		
		long t0 = System.currentTimeMillis();
		msInMerge = 0L;
		msInUpdate = 0L;
		
		nFeatures = retentionTimes.length;
		
		if (nFeatures <= 2)
			{
			initializeClusters();
			return;
			}
		
		useSilhouette = true; //nFeatures < 400;
		int nReps = useSilhouette ? 2 : 1;
		minClusters = Math.ceil(Math.sqrt(nFeatures));
		
		for (int i = 0; i < nReps; i++)
			{
			if (i > 0) 
				{
				for (int j = 0; j < nFeatures; j++)
					this.featureByClusterAverages.get(j).clear();
				    
				this.featureByClusterAverages.clear();
				featureByClusterAverages = null;
				System.gc();
				}
			
			//System.out.println("Starting rep " + i + " with " + nFeatures + " features");
			nClustersRemaining = nFeatures;
			
			initializeClusters();
			initializeDistances();
			initializeSilhouette();
			initializeLinkages();
			
		    while (!isConverged())
		    	{
		    	if (useSilhouette && this.nClustersRemaining < 500)
		        	silhouette.updateForNewK(featureWinClusterDistances, featureByClusterAverages, nClustersRemaining, reportingOn);
		        
		    	Integer idxToUpdate = determineClosestClusterPairAndMerge();
		        updateLinkages(idxToUpdate);
		        
		     //   if (silhouette != null && silhouette.getAverageSilhouetteValues() != null && useSilhouette)
		      //  	{
		     //   	System.out.println("\n\nSilhouette values for k " + this.nClustersRemaining);
			//	    System.out.println("Max average silhouette is " + (silhouette.getMinAverageValue() == null ? " null " : silhouette.getMinAverageValue()));
		      //  	}
		    	}
		    
		    if (silhouette != null)
		    	{
		   // 	System.out.println("Final silhouette averages" + silhouette.getAverageSilhouetteValues());
		    //	System.out.println("K's selected were " + silhouette.getksForMaxAverage() + " of bin size " + nFeatures);
		    //	System.out.println("Max avearge silhouette value was " + silhouette.getMaxAverageValue());
		    	}
		    
		    useSilhouette = false;
			}
	    
	    long t1 = System.currentTimeMillis();
	//    System.out.println("Done!");
	 //   System.out.println("Merging took " + msInMerge / 1000.0 + " seconds overall.");
	  //  System.out.println("Updating took " + msInUpdate / 1000.0 + " seconds overall.");
	 //   System.out.println("runClustering() took " + (t1 - t0) / 1000.0 + " seconds.");
		}

	
	public void initializeSilhouette()
		{
		if (!useSilhouette) return;
			
		if (reportingOn)
			System.out.println("\nInitializing silhouette");
		this.silhouette = new Silhouette(nFeatures);
		this.featureByClusterAverages = new ArrayList<Map<Integer, Double>>();
		for (int i = 0; i < nFeatures; i++)
			{
			Map<Integer, Double> initialEntry = new HashMap<Integer, Double>();
			for (Integer j = 0; j < nFeatures; j++)
				{
				if (j == i) 
					continue;
				initialEntry.put(j, this.getDistanceBetween(i, j));
				}
			featureByClusterAverages.add(initialEntry);
			if (reportingOn)
				System.out.println("Initial distance btw feature " +  i + " and clusters are " + initialEntry);
			
		//	if (reportingOn)
		//		System.out.println("Initial entry for featuer " + i + " intercluster distances is is " + initialEntry);
			}
		
		featureWinClusterDistances = new ArrayList<Double>();
		for (int i = 0; i < nFeatures; i++)
			featureWinClusterDistances.add(i, 0.0);
		}
	
	
	public void initializeClusters()
    	{
    	if (reportingOn)
    		System.out.println("Initializing clusters");
		clusters = new ArrayList<List<Integer>>();
    	for (int i = 0; i < nFeatures; i++)
    		{
    		List<Integer> members = new ArrayList<Integer>();
    		members.add(i);
    		clusters.add(members);
    		}
    	
    	if (this.reportingOn) 
    		System.out.println(clusters);
    	}
		
	
	private void initializeDistances() throws Exception
    	{
    	if (reportingOn)
    		System.out.println("Initializing distances");
		long t0 = System.currentTimeMillis();
		
        distances = new double[nFeatures - 1][];
    	for (int i = 0; i < nFeatures - 1; i++) 
    		{
    		double [] row = new double[nFeatures - i - 1];
    		for (int j = i + 1; j < nFeatures; j++)
    	       row[j - i - 1] = Math.abs(retentionTimes[i] - retentionTimes[j]); //
    			//CustomCalculations.calculateEuclidianDistance(correlations[i], correlations[j]);      
    		distances[i] = row;
    		if (this.reportingOn)
    			{
    			StringBuilder sb = new StringBuilder();
    			sb.append(i + " has :");
    			for (int k = 0; k < row.length; k++)
    				sb.append(row[k] + " ");
    			
    			//System.out.println("Distances for feature " + i + " are " + sb.toString());
    			}
    		}
    		
    	long t1 = System.currentTimeMillis();
	    
    	//  System.out.println("createDistances() took " + (t1 - t0) / 1000.0 + " seconds.");
    	}
	
	private double getDistanceBetween(int feature, int otherFeature)
		{
		if (feature == otherFeature)
			return 0;
		if (feature < otherFeature)
			return distances[feature][otherFeature - feature - 1];
	
		//(otherFeature < feature)
		return distances[otherFeature][feature - otherFeature - 1];
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
//		System.out.println("initializeLinkages() took " + (t1 - t0) / 1000.0 + " seconds.");
		}
	
	
	private Boolean isConverged()
		{
		if (useSilhouette)
			{
			minClusters = determineCorrectKForWeightGrid();
			return nClustersRemaining == 1;
			}
		
		return nClustersRemaining <= minClusters;
		}

	private double determineCorrectKForWeightGrid()
		{
		List<Integer> ksToConsider = silhouette.getksForMaxAverage();

		Integer value = ksToConsider.get(0);

		if (value > 2 || this.doUnweighted)
			return value * 1.0;

		int step = 1;
		while (step < ksToConsider.size())
			{
			value = ksToConsider.get(step);
			if (value > 2) 
				break;
			step++;
			} 
		
		if (value > 100)
			return Math.ceil(Math.sqrt(nFeatures));

		if (step > 5)
			return 2.0;

		return value * 1.0;
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

		if (useSilhouette) 
			updateClusterDistancesForSilhouette(clusters.get(idx1ToMerge), clusters.get(idx2ToMerge), idx1ToMerge, idx2ToMerge); 
		
	//	if (reportingOn)
	//		System.out.println("Combining :  Clusters are " + clusters);
		combineClusters(clusters.get(idx1ToMerge), clusters.get(idx2ToMerge), idx2ToMerge);
		if (reportingOn)
			System.out.println("Combined :  Clusters are " + clusters);
		
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

	
	
	private void updateClusterDistancesForSilhouette(List<Integer> mergedItems, List<Integer> droppedItems, int mergedIdx, int droppedIdx)
		{
		if (!useSilhouette) 
			return;
		
		if (reportingOn)
			{
			System.out.println("\n\nUpdating between and within cluster distances.  Merged cluster is " + mergedIdx  +  " and dropped is " + droppedIdx);
			System.out.println("Features in merged cluster " + mergedItems);
			System.out.println("Features in dropped cluster " + droppedItems);
			System.out.println("Before update, intracluster distances (by feature)  are " + this.featureWinClusterDistances);
			}
		// calculate new average intracluster distance for feature (average of all distances from feature to all other cluster members (in same cluster)
		// This is only relevant for feature where cluster composition has changeed (i.e. features in former D and former M)
		Map<Integer, Double> newIntraclusterEntries = new HashMap<Integer, Double>();
		
		// # feature in dropped cluster
		int d = droppedItems.size();
		// # features in (old) merged cluster 
		int m = mergedItems.size();
		
		for (Integer i = 0; i < droppedItems.size(); i++)
			{
			int featureIdx = droppedItems.get(i);
			
			// get sum of distances between member of the dropped cluster (featureIdx) and all other items in the dropped cluster
			double oldIntraTotal = featureWinClusterDistances.get(featureIdx) * (d - 1);
			
			//also, need sum of distances between member of dropped cluster (at featureIdx) and all items in old merged cluster
			// map of all average distances between feature @ featureIdx and all other clusters, including mergedIdx
			Map<Integer, Double> featureByClusterMap = featureByClusterAverages.get(featureIdx);
			
			// pull the average distance between feature @ featureIdx and the old merged cluster, multiply by # of terms (m) to recover sum of distances
			double outsideDistance = featureByClusterMap.get(mergedIdx) * m;
			
			// new sum of distances between feature i and all items in the new merged cluster, divide by # of terms for average (m + d - 1)
			double newDistance = oldIntraTotal + outsideDistance;
			newDistance /= (1.0 * (m + d -1));

			// new averageIntraccluster distance feature i, formerly in cluster D
			newIntraclusterEntries.put(featureIdx, newDistance);
			}
		
		for (int i = 0; i < mergedItems.size(); i++)
			{
			int featureIdx = mergedItems.get(i);
			
			// old sum of intraccluster distances for this feature (formerly in M)
			double oldIntraTotal = featureWinClusterDistances.get(featureIdx) * (m - 1);
			
			// get additional terms from old cross cluster calculation 
			// pull map of distances between feature @ featureIdx and existing clusters
			Map<Integer, Double> featureByClusterMap = featureByClusterAverages.get(featureIdx);
			
			// average feature to cluster distance between feature @ featureIdx (in M)  and cluster D
			// multiply by # terms to retrieve summation
			double outsideDistance = featureByClusterMap.get(droppedIdx) * d;
			
			
			double newTotalDistance = oldIntraTotal + outsideDistance;
			double newAverageDistance = newTotalDistance / (1.0 * (m  + d -1));
			newIntraclusterEntries.put(featureIdx, newAverageDistance);
			}
	
		
		if (reportingOn)
			{
			// Haven't changed array but print values as they will be after intercluster distances are updated
			Map<Integer, Double> printedValues = new HashMap<Integer, Double>();
			for (Integer i = 0;  i < nFeatures; i++)
				{
			    if (newIntraclusterEntries.containsKey(i))
			    	printedValues.put(i, newIntraclusterEntries.get(i));
			    else
			    	printedValues.put(i, featureWinClusterDistances.get(i));
				}
			System.out.println("After update, intracluster distances (by feature)  are " + printedValues);
			}
			
		
		// before updating featureWinClusterDistances (featurewise intracluster distances), use old terms to recalculate new 
		// (intercluster) feature to cluster distances;
		
		updateInterClusterDistances(mergedItems, droppedItems, mergedIdx, droppedIdx);
		
		// finally update the feature-wise intraccluster distances
		for (Integer featureIdx : newIntraclusterEntries.keySet())
			{
			Double newDistance = newIntraclusterEntries.get(featureIdx);
			featureWinClusterDistances.set(featureIdx, newDistance);
			}
		}
	
	
	private void updateInterClusterDistances(List<Integer> mergedItems,  List<Integer> droppedItems, int mergedCluster, int droppedCluster)
		{
		
		// Things that change : (1) distances between all points and cluster D (go away, cluster doesn't exist)
		//                      (2) distances between points in D to cluster M  (also go away as they are subsumed into the intracluster disttance average)
		//                      (3) distances between points in M to cluster D (same as 2 -- no onger intercluster
		//                      (4) distances between points not in D or M and new merged cluster  (D \\union M)
	   //// if (reportingOn)
	   // 	System.out.println("N clusters remaining "+ this.nClustersRemaining);
	    
		int m = mergedItems.size();
		int d = droppedItems.size();
		
		// First update (4) : cluster distances for features not in D or M to D \\union M
		Map<Integer, Boolean> ignoreItems = new HashMap<Integer, Boolean>();
		for (Integer i = 0; i < mergedItems.size();i++)
			{
			Integer featureIdx = mergedItems.get(i);
			ignoreItems.put(featureIdx, null);
			}
		for (Integer i= 0; i < droppedItems.size(); i++)
			{
			Integer featureIdx = droppedItems.get(i);
			ignoreItems.put(featureIdx, null);
			}
		
			
		if (reportingOn)
		  System.out.println("\nGetting new intercluster distances Ignored items are " + ignoreItems + " because they are merged -- no distances change....\n");
		// for all features not in D or M,  average distance to cluster D \\union M can be computed as total distanctes to points 
		// formerly in D + total distances to points formerly in M divided by the number of terms (m +d)
		
		for (Integer i =0; i < nFeatures; i++)
			{
			Map<Integer, Double> featureByClusterMap = featureByClusterAverages.get(i);
				if (reportingOn)
					{
					System.out.println("Before updating distance to merged cluster (" + mergedCluster + "), feature by cluster intercluster distances  for feature " + i + ". Distance to dropped cluster " + droppedCluster + " not updated yet");
					System.out.println(featureByClusterMap);
					}
		
			// distance is intraCluster (no longer interCluster)
		    if (ignoreItems.containsKey(i))
		    	{
		    	if (reportingOn)
		    		{
		    		System.out.println("Do not update intercluster distances for feature " + i + " which is part of the deleted or dropped cluster");
		    		System.out.println("-------------------------------");
		    		}
		    	continue;
		    	}
			
			for (int w = 0; w < 1000; w++)
				{
				int j = 0; 
				j++;
				}
			
			double oldTotalDistanceToMergedCluster = featureByClusterMap.get(mergedCluster) * m;
			double oldTotalDistanceToDroppedCluster = featureByClusterMap.get(droppedCluster) * d;
			
			double newTotalDistanceToMerged = oldTotalDistanceToMergedCluster + oldTotalDistanceToDroppedCluster;
			double newAverageDistanceToMerged = newTotalDistanceToMerged / (m + d);
			featureByClusterMap.put(mergedCluster,newAverageDistanceToMerged);
			
			if (reportingOn)
				{
				System.out.println("After updating distance to merged cluster (" + mergedCluster + "), feature by cluster intercluster distances  for feature " + i + "Distance to dropped cluster " + droppedCluster + " not updated yet");
				System.out.println(featureByClusterMap);
				System.out.println("-------------------------------");
				}
			}
		
		// for all features, average distance to cluster D should be dropped (no longer a cluster)
		if (reportingOn)
			System.out.println("\nDropping intercluster distances to dropped cluster....");
		for (int i = 0; i < nFeatures; i++)
			{
			Map<Integer, Double> featureByClusterMap = featureByClusterAverages.get(i);
			featureByClusterMap.remove(droppedCluster);
			
			if (reportingOn)
				{
				System.out.println("Cluster distance map for feature " + i  + " after dropping entries for the dropped cluster (" + droppedCluster + ")");
				System.out.println(featureByClusterMap);
				System.out.println("-------------------");
				}
			
			}
		
		
		// for features in D, also drop distances to cluster M (these are now part of the intracluster distance)
		for (int i = 0; i< droppedItems.size(); i++)
			{
			int featureIdx = droppedItems.get(i);
			
			Map<Integer, Double> featureByClusterMap = featureByClusterAverages.get(featureIdx);
			//this is no longer intercluster;  goes into intracluster totals;
			featureByClusterMap.remove(mergedCluster);
			if (reportingOn)
				{
				System.out.println("Removing intercluster distance to cluster " + mergedCluster + " for feature " + featureIdx + " This feature was merged with cluster " + mergedCluster + " -- distance is intracluster");
				System.out.println(featureByClusterMap);
				}
			}
		}
	
	
	public void updateLinkages(Integer mergedClusterIdx) throws Exception
		{
		long t0 = System.currentTimeMillis();
	
		List<Integer> clusterFrom = clusters.get(mergedClusterIdx);
		
		for (int idx = 0; idx < mergedClusterIdx; idx++)
			{
			if (clusters.get(idx).isEmpty())
				continue;
			
			linkages[idx][mergedClusterIdx - idx - 1] = calculatePairLinkageWith(clusters.get(idx), clusterFrom, mergedClusterIdx);
			}
		
		for (int idx = mergedClusterIdx + 1; idx < nFeatures; idx++)
			{
			if (clusters.get(idx).isEmpty())
				continue;
			
			linkages[mergedClusterIdx][idx - mergedClusterIdx - 1] = calculatePairLinkageWith(clusterFrom, clusters.get(idx), mergedClusterIdx);
			}
		
		long t1 = System.currentTimeMillis();
		msInUpdate += t1 - t0;
		}
   
	
	public Double calculatePairLinkageWith(List<Integer> cluster, List<Integer> other, int clusterIdx)
		{
		double linkage = 0.0, minAvgLinkage;		
		double clusterAverageDistance  = 0.0;
		
		for (Integer featureIdx : cluster)
			{
			clusterAverageDistance = 0.0;
			minAvgLinkage = Double.MAX_VALUE;

			for (Integer otherFeatureIdx : other)
				clusterAverageDistance += (featureIdx < otherFeatureIdx ? distances[featureIdx][otherFeatureIdx - featureIdx - 1] :
						distances[otherFeatureIdx][featureIdx - otherFeatureIdx - 1]);
			linkage += clusterAverageDistance;
			}
		linkage /= (cluster.size() * other.size() + 0.0);
		
		return linkage;
		} 
	
	
	
	
	public Double getMinClusters()
		{
		return minClusters;
		}

	public void setMinClusters(double minClusters)
		{
		this.minClusters = minClusters;
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
	
	public Integer getNClusters()
		{
		List<Integer> clusterCts = getClusterCounts();
		return clusterCts.size();
		}
	
		
	public double [] getRetentionTimes()
		{
		return this.retentionTimes;
		}
	
	public Silhouette getSilhouette()
		{
		return silhouette;
		}
	}	