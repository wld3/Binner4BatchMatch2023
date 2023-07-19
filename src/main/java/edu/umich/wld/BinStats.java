package edu.umich.wld;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BinStats  implements Serializable  {
	Integer nFeatures, nClustersAfterRebin, nClustersBeforeRebin;
	Integer nAnnotationGroups, nClusteredFeatures;
	Integer nClustersAfterRecluster;
	Double rtMin, rtMax, rtRange;
	Map<Integer, Integer> originalClusterSizeDistribution,  rebinClusterSizeDistribution, reclusterClusterSizeDistribution;
	List<Integer> originalClusterSizes, rebinClusterSizes, reclusterClusterSizes;
	
	List<Integer> rebinClusterIndices, rebinClusterIndicesForIsotopes;
	List<Integer> reclusterClusterIndices, reclusterClusterIndicesForIsotopes;
	List<Integer> originalClusterIndices, originalClusterIndicesForIsotopes;
	
	public BinStats()  { 
		nFeatures = nClustersAfterRebin = nClustersBeforeRebin = nClustersAfterRecluster = nAnnotationGroups = 0;
		rtMin = rtMax = rtRange = -1.0;
	
		reclusterClusterIndices = new ArrayList<Integer>();
		reclusterClusterIndicesForIsotopes = new ArrayList<Integer>();
		originalClusterIndices = new ArrayList<Integer>();
		originalClusterIndicesForIsotopes = new ArrayList<Integer>();
		rebinClusterIndices = new ArrayList<Integer>();
		rebinClusterIndicesForIsotopes = new ArrayList<Integer>();
		}
	
	public Map<Integer, Integer> getOriginalClusterSizeDistribution() {
		return originalClusterSizeDistribution;
	}

	public void setOriginalClusterSizeDistribution(Map<Integer, Integer> originalClusterSizeDistribution) {
		this.originalClusterSizeDistribution = originalClusterSizeDistribution;
	}
	
	public void initializeOriginalClusterSizesFromIndices() {
		Map<Integer, Integer> countingMap = new HashMap<Integer, Integer>();
		originalClusterSizes = new ArrayList<Integer>();
	
		if (originalClusterIndices != null)
			for (Integer i : originalClusterIndices) {
				if (!countingMap.containsKey(i))
					countingMap.put(i, 1);
				else {
					Integer currCtForI = countingMap.get(i);
					countingMap.put(i, currCtForI + 1);
				}
			}
		
		if (originalClusterIndicesForIsotopes != null)
			for (Integer i : originalClusterIndicesForIsotopes) {
				if (!countingMap.containsKey(i))
					countingMap.put(i, 1);
				else {
					int currCtForI = countingMap.get(i);
					countingMap.put(i, currCtForI + 1);
				}
			}
		
		originalClusterSizes = ListUtils.makeListFromCollection(countingMap.values());
	}

	
	public void initializeRebinClusterSizesFromIndices() {
		Map<Integer, Integer> countingMap = new HashMap<Integer, Integer>();
		rebinClusterSizes = new ArrayList<Integer>();

		if (rebinClusterIndices != null)
			for (Integer i : rebinClusterIndices) {
				if (!countingMap.containsKey(i))
					countingMap.put(i, 1);
				else {
					Integer currCtForI = countingMap.get(i);
					countingMap.put(i, currCtForI + 1);
				}
			}
		
		if (rebinClusterIndicesForIsotopes != null)
			for (Integer i : rebinClusterIndicesForIsotopes) {
				if (!countingMap.containsKey(i))
					countingMap.put(i, 1);
				else {
					int currCtForI = countingMap.get(i);
					countingMap.put(i, currCtForI + 1);
				}
			}
		
		rebinClusterSizes = ListUtils.makeListFromCollection(countingMap.values());
	}

	
	public void initializeReclusterClusterSizesFromIndices() {
		Map<Integer, Integer> countingMap = new HashMap<Integer, Integer>();
		reclusterClusterSizes = new ArrayList<Integer>();
		
		if (reclusterClusterIndices != null)
			for (Integer i : reclusterClusterIndices) {
				if (!countingMap.containsKey(i))
					countingMap.put(i, 1);
				else {
					int currCtForI = countingMap.get(i);
					countingMap.put(i, currCtForI + 1);
				}
			}
		
		if (reclusterClusterIndicesForIsotopes != null)
			for (Integer i : reclusterClusterIndicesForIsotopes) {
				if (!countingMap.containsKey(i))
					countingMap.put(i, 1);
				else {
					int currCtForI = countingMap.get(i);
					countingMap.put(i, currCtForI + 1);
				}
			}
		
		reclusterClusterSizes = ListUtils.makeListFromCollection(countingMap.values());
	}
	
	public void initializeClusterSizes() {
		initializeOriginalClusterSizesFromIndices();
		initializeRebinClusterSizesFromIndices();
		initializeReclusterClusterSizesFromIndices();
	}
		
	public Map<Integer, Integer> getRebinClusterSizeDistribution() {
		return rebinClusterSizeDistribution;
	}


	public Map<Integer, Integer> getReclusterClusterSizeDistribution() {
		return reclusterClusterSizeDistribution;
	}

	public List<Integer> getOriginalClusterSizes() {
		return originalClusterSizes;
	}

	public void setOriginalClusterSizes(List<Integer> originalClusterSizes) {
		this.originalClusterSizes = originalClusterSizes;
		this.nClustersBeforeRebin = originalClusterSizes.size();
	}
	
	
	public void initializeToNotClustered(int binSize)
		{
		this.originalClusterSizes = new ArrayList<Integer>();
		this.rebinClusterSizes = new ArrayList<Integer>();
		this.reclusterClusterSizes = new ArrayList<Integer>();
		
		this.nFeatures = binSize;
	
		originalClusterIndices = new ArrayList<Integer>();
		rebinClusterIndices = new ArrayList<Integer>();
		reclusterClusterIndices = new ArrayList<Integer>();
		
		for (int i = 0; i < binSize; i++)
			{
			originalClusterIndices.add(1);
			rebinClusterIndices.add(1);
			reclusterClusterIndices.add(1);
			}
		
		this.nClustersBeforeRebin = 1;
		this.nClustersAfterRebin = 1;
		this.nClustersAfterRecluster = 1;
		}

	public List<Integer> getRebinClusterSizes() {
		return rebinClusterSizes;
	}

	public void setRebinClusterSizes(List<Integer> rebinClusterSizes) {
		this.rebinClusterSizes = rebinClusterSizes;
		setnClustersAfterRebin(rebinClusterSizes.size());
	}

	
	public Integer getnClustersAfterRecluster() {
		return nClustersAfterRecluster;
	}

	public void setnClustersAfterRecluster(Integer nClustersAfterRecluster) {
		this.nClustersAfterRecluster = nClustersAfterRecluster;
	}

	
	public List<Integer> getReclusterClusterSizes() {
		return reclusterClusterSizes;
	}

	public void setReclusterClusterSizes(List<Integer> reclusterClusterSizes) {
		this.reclusterClusterSizes = reclusterClusterSizes;
		this.nClustersAfterRecluster = reclusterClusterSizes.size();
	}

	public Integer getnFeatures() {
		return nFeatures;
	}

	public void setnFeatures(Integer nFeatures) {
		this.nFeatures = nFeatures;
	}

	public Integer getnClusteredFeatures() {
		return nClusteredFeatures;
	}

	public void setnClusteredFeatures(Integer nClusteredFeatures) {
		this.nClusteredFeatures = nClusteredFeatures;
	}

	public Integer getnClustersAfterRebin() {
		return nClustersAfterRebin;
	}

	public void setnClustersAfterRebin(Integer nClustersAfterRebin) {
		this.nClustersAfterRebin = nClustersAfterRebin;
	}

	public Integer getnClustersBeforeRebin() {
		return nClustersBeforeRebin;
	}

	public void setnClustersBeforeRebin(Integer nClustersBeforeRebin) {
		this.nClustersBeforeRebin = nClustersBeforeRebin;
	}

	public Integer getnAnnotationGroups() {
		return nAnnotationGroups;
	}

	public void setnAnnotationGroups(Integer nAnnotationGroups) {
		this.nAnnotationGroups = nAnnotationGroups;
	}

	public Double getRtMin() {
		return rtMin;
	}

	public void setRtMin(Double rtMin) {
		this.rtMin = rtMin;
	}

	public Double getRtMax() {
		return rtMax;
	}

	public void setRtMax(Double rtMax) {
		this.rtMax = rtMax;
	}

	public Double getRtRange() {
		return rtRange;
	}

	public void setRtRange(Double rtRange) {
		this.rtRange = rtRange;
	}
	
	public List<Integer> getRebinClusterIndices() {
		return rebinClusterIndices;
	}
	
	
	public List<Integer> getOriginalClusterIndices() {
		return originalClusterIndices;
	}

	public void setOriginalClusterIndices(List<Integer> originalClusterIndices) {
		this.originalClusterIndices = originalClusterIndices;
	}

	public List<Integer> getOriginalClusterIndicesForIsotopes() {
		return originalClusterIndicesForIsotopes;
	}

	public void setOriginalClusterIndicesForIsotopes(List<Integer> originalClusterIndicesForIsotopes) {
		this.originalClusterIndicesForIsotopes = originalClusterIndicesForIsotopes;
	}

	public void setRebinClusterIndices(List<Integer> rebinClusterIndices) {
		this.rebinClusterIndices = rebinClusterIndices;
	}

	
	public List<Integer> getReclusterClusterIndices() {
		return reclusterClusterIndices;
	}

	public void setReclusterClusterIndices(List<Integer> reclusterClusterIndices) {
		this.reclusterClusterIndices = reclusterClusterIndices;
	}

	public List<Integer> getRebinClusterIndicesForIsotopes() {
		return rebinClusterIndicesForIsotopes;
	}

	public void setRebinClusterIndicesForIsotopes(List<Integer> rebinClusterIndicesForIsotopes) {
		this.rebinClusterIndicesForIsotopes = rebinClusterIndicesForIsotopes;
	}

	public List<Integer> getReclusterClusterIndicesForIsotopes() {
		return reclusterClusterIndicesForIsotopes;
	}

	public void setReclusterClusterIndicesForIsotopes(List<Integer> reclusterClusterIndicesForIsotopes) {
		this.reclusterClusterIndicesForIsotopes = reclusterClusterIndicesForIsotopes;
	}

	public String toString() {
		return ObjectHandler.printObject(this);
	}
}
