package edu.umich.wld;

import java.util.ArrayList;
import java.util.List;

public class ClusterForAnnotation {
	
	private List<FeatureInfoForAnnotation> featureInfo = new ArrayList<FeatureInfoForAnnotation>();
	private Integer firstIndexWithinBin = null;
	private Integer lastIndexWithinBin = null;
	
	public ClusterForAnnotation() {
	}

	public List<FeatureInfoForAnnotation> getFeatureInfo() {
		return featureInfo;
	}

	public void setFeatureInfo(List<FeatureInfoForAnnotation> featureInfo) {
		this.featureInfo = featureInfo;
	}

	public Integer getFirstIndexWithinBin() {
		return firstIndexWithinBin;
	}

	public void setFirstIndexWithinBin(Integer firstIndexWithinBin) {
		this.firstIndexWithinBin = firstIndexWithinBin;
	}

	public Integer getLastIndexWithinBin() {
		return lastIndexWithinBin;
	}

	public void setLastIndexWithinBin(Integer lastIndexWithinBin) {
		this.lastIndexWithinBin = lastIndexWithinBin;
	}
}
