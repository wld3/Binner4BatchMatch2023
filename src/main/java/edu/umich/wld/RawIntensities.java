package edu.umich.wld;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RawIntensities {
	private TextFile rawDataFile;
	private List<String> sampleNames = new ArrayList<String>();
	private Map<String, List<String>> intensityMap = new TreeMap<String, List<String>>();
	private Integer featureIndex;
	private Integer massIndex;
	private Integer firstSampleIndex = -1;
	
	public TextFile getRawDataFile() {
		return rawDataFile;
	}
	
	public void setRawDataFile(TextFile rawDataFile) {
		this.rawDataFile = rawDataFile;
	}
	
	public List<String> getSampleNames() {
		return sampleNames;
	}
	
	public void setSampleNames(List<String> sampleNames) {
		this.sampleNames = sampleNames;
	}
	
	public Map<String, List<String>> getIntensityMap() {
		return intensityMap;
	}
	
	public void setIntensityMap(Map<String, List<String>> intensityMap) {
		this.intensityMap = intensityMap;
	}

	public Integer getFeatureIndex() {
		return featureIndex;
	}

	public void setFeatureIndex(Integer featureIndex) {
		this.featureIndex = featureIndex;
	}

	public Integer getMassIndex() {
		return massIndex;
	}

	public void setMassIndex(Integer massIndex) {
		this.massIndex = massIndex;
	}

	public Integer getFirstSampleIndex() {
		return firstSampleIndex;
	}

	public void setFirstSampleIndex(Integer firstSampleIndex) {
		this.firstSampleIndex = firstSampleIndex;
	}
}
