package edu.umich.wld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;

public class AnalysisData {
	private List<Feature> nonMissingFeaturesInOriginalOrder = new ArrayList<Feature>();
	private List<List<Integer>> binContents = new ArrayList<List<Integer>>();
	private List<RealMatrix> binwiseCorrelations = new ArrayList<RealMatrix>();
	private List<RealMatrix> binwiseCorrelationsMinusIsotopes = new ArrayList<RealMatrix>();
	private List<Double> binwiseMeanCorrelations = new ArrayList<Double>();
	private List<Double> binwiseRTRanges = new ArrayList<Double>();
	private List<RealMatrix> binwiseMassDiffs = new ArrayList<RealMatrix>();
	private List<RealMatrix> binwiseMassDiffsMinusIsotopes = new ArrayList<RealMatrix>();
	private MassDiffLimits massDiffRanges;
	private MassDiffHistogram overallHistogram;
	private List<List<IndexListItem<Double>>> outlierMap = null;
	private List<IndexListItem<Double>> indexedNonMissingRTs = null;
	private List<IndexListItem<Integer>> indexedClustersForRebinning = null;
	private List<IndexListItem<Integer>> indexedClustersFromRebinning = null;
	private List<IndexListItem<Integer>> indexedClustersFromRTClustering = null;
	private List<IndexListItem<Integer>> indexedClustersForAnnotation = null;
	private List<IndexListItem<Integer>> indexedClustersForOutput = null;
	private List<HashMap<Integer, IsotopeGroup>> binwiseIsotopeGroups = new ArrayList<HashMap<Integer, IsotopeGroup>>();
	private List<List<Integer>> binwiseFeaturesForClustering = new ArrayList<List<Integer>>();
	private List<Integer[][]> binwiseMassDiffClasses = new ArrayList<Integer[][]>();
	private List<Feature> missingFeaturesInOriginalOrder = new ArrayList<Feature>();
	private RawIntensities allRawIntensities = new RawIntensities();

	public AnalysisData() { 
		initialize(false);
	}

	public void initialize(Boolean keepOutlierMap) {
		nonMissingFeaturesInOriginalOrder = new ArrayList<Feature>();
		binContents = new ArrayList<List<Integer>>();
		binwiseCorrelations = new ArrayList<RealMatrix>();
		binwiseCorrelationsMinusIsotopes = new ArrayList<RealMatrix>();
		binwiseMeanCorrelations = new ArrayList<Double>();
		binwiseRTRanges = new ArrayList<Double>();
		binwiseMassDiffs = new ArrayList<RealMatrix>();
		binwiseMassDiffsMinusIsotopes = new ArrayList<RealMatrix>();
		//private MassDiffLimits massDiffRanges;
		//private MassDiffHistogram overallHistogram;
		if (!keepOutlierMap)
			outlierMap = null;
		indexedNonMissingRTs = null;
		indexedClustersForRebinning = null;
		indexedClustersFromRebinning = null;
		indexedClustersFromRTClustering = null;
		indexedClustersForAnnotation = null;
		indexedClustersForOutput = null;
		binwiseIsotopeGroups = new ArrayList<HashMap<Integer, IsotopeGroup>>();
		binwiseFeaturesForClustering = new ArrayList<List<Integer>>();
		binwiseMassDiffClasses = new ArrayList<Integer[][]>();
		missingFeaturesInOriginalOrder = new ArrayList<Feature>();
		allRawIntensities = new RawIntensities();
	}
	
	public List<Feature> getNonMissingFeaturesInOriginalOrder() {
		return nonMissingFeaturesInOriginalOrder;
	}

	public void setNonMissingFeaturesInOriginalOrder(List<Feature> nonMissingFeaturesInOriginalOrder) {
		this.nonMissingFeaturesInOriginalOrder = nonMissingFeaturesInOriginalOrder;
	}

	public List<List<Integer>> getBinContents() {
		return binContents;
	}

	public void setBinContents(List<List<Integer>> binContents) {
		this.binContents = binContents;
	}

	public List<RealMatrix> getBinwiseCorrelations() {
		return binwiseCorrelations;
	}

	public void setBinwiseCorrelations(List<RealMatrix> binwiseCorrelations) {
		this.binwiseCorrelations = binwiseCorrelations;
	}

	public List<RealMatrix> getBinwiseCorrelationsMinusIsotopes() {
		return binwiseCorrelationsMinusIsotopes;
	}

	public void setBinwiseCorrelationsMinusIsotopes(List<RealMatrix> binwiseCorrelationsMinusIsotopes) {
		this.binwiseCorrelationsMinusIsotopes = binwiseCorrelationsMinusIsotopes;
	}

	public List<Double> getBinwiseMeanCorrelations() {
		return binwiseMeanCorrelations;
	}
	public void setBinwiseMeanCorrelations(List<Double> binwiseMeanCorrelations) {
		this.binwiseMeanCorrelations = binwiseMeanCorrelations;
	}

	public List<Double> getBinwiseRTRanges() {
		return binwiseRTRanges;
	}

	public void setBinwiseRTRanges(List<Double> binwiseRTRanges) {
		this.binwiseRTRanges = binwiseRTRanges;
	}

	public List<RealMatrix> getBinwiseMassDiffs() {
		return binwiseMassDiffs;
	}

	public void setBinwiseMassDiffs(List<RealMatrix> binwiseMassDiffs) {
		this.binwiseMassDiffs = binwiseMassDiffs;
	}

	public List<RealMatrix> getBinwiseMassDiffsMinusIsotopes() {
		return binwiseMassDiffsMinusIsotopes;
	}

	public void setBinwiseMassDiffsMinusIsotopes(List<RealMatrix> binwiseMassDiffsMinusIsotopes) {
		this.binwiseMassDiffsMinusIsotopes = binwiseMassDiffsMinusIsotopes;
	}

	public MassDiffLimits getMassDiffRanges() {
		return massDiffRanges;
	}

	public void setMassDiffRanges(MassDiffLimits massDiffRanges) {
		this.massDiffRanges = massDiffRanges;
	}

	public MassDiffHistogram getOverallHistogram() {
		return overallHistogram;
	}

	public void setOverallHistogram(MassDiffHistogram overallHistogram) {
		this.overallHistogram = overallHistogram;
	}

	public List<List<IndexListItem<Double>>> getOutlierMap() {
		return outlierMap;
	}

	public void setOutlierMap(List<List<IndexListItem<Double>>> outlierMap) {
		this.outlierMap = outlierMap;
	}

	public List<IndexListItem<Double>> getIndexedNonMissingRTs() {
		return indexedNonMissingRTs;
	}

	public void setIndexedNonMissingRTs(List<IndexListItem<Double>> indexedNonMissingRTs) {
		this.indexedNonMissingRTs = indexedNonMissingRTs;
	}

	public List<IndexListItem<Integer>> getIndexedClustersForRebinning() {
		return indexedClustersForRebinning;
	}

	public void setIndexedClustersForRebinning(
			List<IndexListItem<Integer>> indexedClustersForRebinning) {
		this.indexedClustersForRebinning = indexedClustersForRebinning;
	}

	public List<IndexListItem<Integer>> getIndexedClustersFromRebinning() {
		return indexedClustersFromRebinning;
	}

	public void setIndexedClustersFromRebinning(
			List<IndexListItem<Integer>> indexedClustersFromRebinning) {
		this.indexedClustersFromRebinning = indexedClustersFromRebinning;
	}

	public List<IndexListItem<Integer>> getIndexedClustersFromRTClustering() {
		return indexedClustersFromRTClustering;
	}

	public void setIndexedClustersFromRTClustering(
			List<IndexListItem<Integer>> indexedClustersFromRTClustering) {
		this.indexedClustersFromRTClustering = indexedClustersFromRTClustering;
	}

	public List<IndexListItem<Integer>> getIndexedClustersForAnnotation() {
		return indexedClustersForAnnotation;
	}

	public void setIndexedClustersForAnnotation(
			List<IndexListItem<Integer>> indexedClustersForAnnotation) {
		this.indexedClustersForAnnotation = indexedClustersForAnnotation;
	}

	public List<IndexListItem<Integer>> getIndexedClustersForOutput() {
		return indexedClustersForOutput;
	}

	public void setIndexedClustersForOutput(List<IndexListItem<Integer>> indexedClustersForOutput) {
		this.indexedClustersForOutput = indexedClustersForOutput;
	}

	public List<HashMap<Integer, IsotopeGroup>> getBinwiseIsotopeGroups() {
		return binwiseIsotopeGroups;
	}

	public void setBinwiseIsotopeGroups(List<HashMap<Integer, IsotopeGroup>> binwiseIsotopeGroups) {
		this.binwiseIsotopeGroups = binwiseIsotopeGroups;
	}

	public List<List<Integer>> getBinwiseFeaturesForClustering() {
		return binwiseFeaturesForClustering;
	}

	public void setBinwiseFeaturesForClustering(List<List<Integer>> binwiseFeaturesForClustering) {
		this.binwiseFeaturesForClustering = binwiseFeaturesForClustering;
	}

	public List<Integer[][]> getBinwiseMassDiffClasses() {
		return binwiseMassDiffClasses;
	}

	public void setBinwiseMassDiffClasses(List<Integer[][]> binwiseMassDiffClasses) {
		this.binwiseMassDiffClasses = binwiseMassDiffClasses;
	}

	public List<Feature> getMissingFeaturesInOriginalOrder() {
		return missingFeaturesInOriginalOrder;
	}

	public void setMissingFeaturesInOriginalOrder(List<Feature> missingFeaturesInOriginalOrder) {
		this.missingFeaturesInOriginalOrder = missingFeaturesInOriginalOrder;
	}

	public RawIntensities getAllRawIntensities() {
		return allRawIntensities;
	}

	public void setAllRawIntensities(RawIntensities allRawIntensities) {
		this.allRawIntensities = allRawIntensities;
	}
}
