package edu.umich.wld;

import java.util.Map;

public class SummaryInfo {
	
	private Map<Integer, AnnotationInfo> annotationMap;
	private Boolean annotateRebinCluster = null;
	private String expFilePath = null;
	private String titleWithVersion = null;
	private String expFileCompCol = null;
	private String firstSamp = null;
	private String lastSamp = null;
	private Integer nSamps = null;
	private String expFileRTCol = null;
	private String expFileMassCol = null;
	private String libFilePath = null;
	private String libFileCompCol = null;
	private String libFileRTCol = null;
	private String libFileMassCol = null;
	private Double pctMissingCutoff = null;
	private Double outlierThreshold = null;
	private Integer nOutlierPts = null;
	private Integer totalFeatureCount = null;
	private Integer removedFeatureCount = null;
	private Integer analyzedFeatureCount = null;
	private Boolean doTransform = null;
	private Boolean doDeisotoping = null;
	private String isotopeMassTol = null;
	private String isotopeCorrCutoff = null;
	private String isotopeRTRange = null;
	private Integer isotopeGroupCount = null;
	private Integer isotopesFoundCount = null;
	private String annotFilePath = null;
	private String annotFileAnnotCol = null;
	private String annotFileMassCol = null;
	private String annotFileModeCol = null;
	private String annotFileChargeCol = null;
	private String annotFileTierCol = null;
	private Boolean useNMForChargeCarrierCall = null;
	private Boolean chargeCanVaryWithoutIsotopeInfo = null;
	private String ionizationMode = null;
	private String outputDirectory = null;
	private String gapBetweenBins = null;
	private String annotMassTol = null;
	private String annotRtTol = null;
	private Integer annotGroupCount = null;
	private Integer nonMolecularIonAnnotCount = null;
	private String clusteringRule = null;
	private String threshold = null;
	private String clusteringMethod = null;
	private String rtClusteringMethod = null;
	private String correlationType = null;
	private String nHistogramPoints = null, nAnnotatedHistogramPoints = null;
	private Boolean doHistogramDeisotoping = null;
	private String reclusteredClustersRule = null;
	private String rtGapRule = null;
	private Map<String, ChargeCarrierPreferences> chargeCarrierPrefs = null;
	private Boolean zeroMeansMissing = null;
	private String skippedChargeCarriers = null;
	private String isotopeAnnotationWarnings = null;
	
	public Boolean getAnnotateRebinCluster() {
		return annotateRebinCluster;
	}

	public void setAnnotateRebinCluster(Boolean annotateRebinCluster) {
		this.annotateRebinCluster = annotateRebinCluster;
	}

	
	public SummaryInfo() {
	}

	public String getExpFilePath() {
		return expFilePath;
	}

	public void setExpFilePath(String expFilePath) {
		this.expFilePath = expFilePath;
	}

	public String getTitleWithVersion() {
		return titleWithVersion;
	}

	public void setTitleWithVersion(String titleWithVersion) {
		this.titleWithVersion = titleWithVersion;
	}

	public String getExpFileCompCol() {
		return expFileCompCol;
	}

	public void setExpFileCompCol(String expFileCompCol) {
		this.expFileCompCol = expFileCompCol;
	}
	
	public Boolean getDoHistogramDeisotoping() {
		return doHistogramDeisotoping;
	}

	public void setDoHistogramDeisotoping(Boolean doHistogramDeisotoping) {
		this.doHistogramDeisotoping = doHistogramDeisotoping;
	}

	public String getFirstSamp() {
		return firstSamp;
	}

	public void setFirstSamp(String firstSamp) {
		this.firstSamp = firstSamp;
	}

	public String getLastSamp() {
		return lastSamp;
	}

	public void setLastSamp(String lastSamp) {
		this.lastSamp = lastSamp;
	}

	public Integer getNSamps() {
		return nSamps;
	}

	public void setNSamps(Integer nSamps) {
		this.nSamps = nSamps;
	}

	public String getExpFileRTCol() {
		return expFileRTCol;
	}

	public void setExpFileRTCol(String expFileRTCol) {
		this.expFileRTCol = expFileRTCol;
	}

	public String getExpFileMassCol() {
		return expFileMassCol;
	}

	public void setExpFileMassCol(String expFileMassCol) {
		this.expFileMassCol = expFileMassCol;
	}

	public String getLibFilePath() {
		return libFilePath;
	}

	public void setLibFilePath(String libFilePath) {
		this.libFilePath = libFilePath;
	}

	public String getLibFileCompCol() {
		return libFileCompCol;
	}

	public void setLibFileCompCol(String libFileCompCol) {
		this.libFileCompCol = libFileCompCol;
	}

	public String getLibFileRTCol() {
		return libFileRTCol;
	}

	public void setLibFileRTCol(String libFileRTCol) {
		this.libFileRTCol = libFileRTCol;
	}

	public String getLibFileMassCol() {
		return libFileMassCol;
	}

	public void setLibFileMassCol(String libFileMassCol) {
		this.libFileMassCol = libFileMassCol;
	}

	public Double getPctMissingCutoff() {
		return pctMissingCutoff;
	}

	public void setPctMissingCutoff(Double pctMissingCutoff) {
		this.pctMissingCutoff = pctMissingCutoff;
	}

	public Double getOutlierThreshold() {
		return outlierThreshold;
	}

	public void setOutlierThreshold(Double outlierThreshold) {
		this.outlierThreshold = outlierThreshold;
	}

	public Integer getTotalFeatureCount() {
		return totalFeatureCount;
	}

	public void setTotalFeatureCount(Integer totalFeatureCount) {
		this.totalFeatureCount = totalFeatureCount;
	}

	public Integer getRemovedFeatureCount() {
		return removedFeatureCount;
	}

	public void setRemovedFeatureCount(Integer removedFeatureCount) {
		this.removedFeatureCount = removedFeatureCount;
	}

	public Integer getAnalyzedFeatureCount() {
		return analyzedFeatureCount;
	}
	
	public void setAnalyzedFeatureCount(Integer analyzedFeatureCount) {
		this.analyzedFeatureCount = analyzedFeatureCount;
	}

	public Boolean getDoTransform() {
		return doTransform;
	}

	public void setDoTransform(Boolean doTransform) {
		this.doTransform = doTransform;
	}

	public Boolean getDoDeisotoping() {
		return doDeisotoping;
	}

	public void setDoDeisotoping(Boolean doDeisotoping) {
		this.doDeisotoping = doDeisotoping;
	}

	public String getIsotopeMassTol() {
		return isotopeMassTol;
	}

	public void setIsotopeMassTol(String isotopeMassTol) {
		this.isotopeMassTol = isotopeMassTol;
	}

	public String getIsotopeCorrCutoff() {
		return isotopeCorrCutoff;
	}

	public void setIsotopeCorrCutoff(String isotopeCorrCutoff) {
		this.isotopeCorrCutoff = isotopeCorrCutoff;
	}

	public String getIsotopeRTRange() {
		return isotopeRTRange;
	}

	public void setIsotopeRTRange(String isotopeRTRange) {
		this.isotopeRTRange = isotopeRTRange;
	}

	public Integer getIsotopeGroupCount() {
		return isotopeGroupCount;
	}

	public void setIsotopeGroupCount(Integer isotopeGroupCount) {
		this.isotopeGroupCount = isotopeGroupCount;
	}

	public Integer getIsotopesFoundCount() {
		return isotopesFoundCount;
	}

	public void setIsotopesFoundCount(Integer isotopesFoundCount) {
		this.isotopesFoundCount = isotopesFoundCount;
	}

	public String getAnnotFilePath() {
		return annotFilePath;
	}

	public void setAnnotFilePath(String annotFilePath) {
		this.annotFilePath = annotFilePath;
	}

	public String getAnnotFileAnnotCol() {
		return annotFileAnnotCol;
	}

	public void setAnnotFileAnnotCol(String annotFileAnnotCol) {
		this.annotFileAnnotCol = annotFileAnnotCol;
	}

	public String getAnnotFileMassCol() {
		return annotFileMassCol;
	}

	public void setAnnotFileMassCol(String annotFileMassCol) {
		this.annotFileMassCol = annotFileMassCol;
	}

	public String getAnnotFileModeCol() {
		return annotFileModeCol;
	}

	public void setAnnotFileModeCol(String annotFileModeCol) {
		this.annotFileModeCol = annotFileModeCol;
	}

	public String getAnnotFileChargeCol() {
		return annotFileChargeCol;
	}

	public void setAnnotFileChargeCol(String annotFileChargeCol) {
		this.annotFileChargeCol = annotFileChargeCol;
	}
	
	public String getAnnotFileTierCol() {
		return annotFileTierCol;
	}

	public void setAnnotFileTierCol(String annotFileTierCol) {
		this.annotFileTierCol = annotFileTierCol;
	}

	public String getIonizationMode() {
		return ionizationMode;
	}

	public void setIonizationMode(String ionizationMode) {
		this.ionizationMode = ionizationMode;
	}

	public String getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public String getGapBetweenBins() {
		return gapBetweenBins;
	}

	public void setGapBetweenBins(String gapBetweenBins) {
		this.gapBetweenBins = gapBetweenBins;
	}

	public String getAnnotMassTol() {
		return annotMassTol;
	}

	public void setAnnotMassTol(String annotMassTol) {
		this.annotMassTol = annotMassTol;
	}

	public String getAnnotRtTol() {
		return annotRtTol;
	}

	public void setAnnotRtTol(String annotRtTol) {
		this.annotRtTol = annotRtTol;
	}

	public Integer getAnnotGroupCount() {
		return annotGroupCount;
	}

	public void setAnnotGroupCount(Integer annotGroupCount) {
		this.annotGroupCount = annotGroupCount;
	}

	public Integer getNonMolecularIonAnnotCount() {
		return nonMolecularIonAnnotCount;
	}

	public void setNonMolecularIonAnnotCount(Integer nonMolecularIonAnnotCount) {
		this.nonMolecularIonAnnotCount = nonMolecularIonAnnotCount;
	}

	public String getClusteringRule() {
		return clusteringRule;
	}

	public void setClusteringRule(String clusteringRule) {
		this.clusteringRule = clusteringRule;
	}

	public String getRtClusteringMethod() {
		return rtClusteringMethod;
	}

	public void setRtClusteringMethod(String rtClusteringMethod) {
		this.rtClusteringMethod = rtClusteringMethod;
	}

	
	public String getReclusteredClustersRule() {
		return reclusteredClustersRule;
	}

	public void setReclusteredClustersRule(String reclusteredClustersRule) {
		this.reclusteredClustersRule = reclusteredClustersRule;
	}

	public String getRtGapRule() {
		return rtGapRule;
	}

	public void setRtGapRule(String rtGapRule) {
		this.rtGapRule = rtGapRule;
	}

	public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	public String getClusteringMethod() {
		return clusteringMethod;
	}

	public void setClusteringMethod(String clusteringMethod) {
		this.clusteringMethod = clusteringMethod;
	}

	public String getnHistogramPoints() {
		return nHistogramPoints;
	}

	public void setnHistogramPoints(String nHistogramfPoints) {
		this.nHistogramPoints = nHistogramfPoints;
	}

	public String getnAnnotatedHistogramPoints() {
		return nAnnotatedHistogramPoints;
	}

	public void setnAnnotatedHistogramPoints(String nAnnotatedHistogramPoints) {
		this.nAnnotatedHistogramPoints = nAnnotatedHistogramPoints;
	}

	public Integer getnOutlierPts() {
		return nOutlierPts;
	}

	public void setnOutlierPts(Integer nOutlierPts) {
		this.nOutlierPts = nOutlierPts;
	}

	public String getCorrelationType() {
		return correlationType;
	}

	public void setCorrelationType(String correlationType) {
		this.correlationType = correlationType;
	}

	public Map<String, ChargeCarrierPreferences> getChargeCarrierPrefs() {
		return chargeCarrierPrefs;
	}

	public void setChargeCarrierPrefs(Map<String, ChargeCarrierPreferences> chargeCarrierPrefs)  {
		this.chargeCarrierPrefs = chargeCarrierPrefs;
	}

	public Boolean getUseNMForChargeCarrierCall() {
		return useNMForChargeCarrierCall;
	}

	public void setUseNMForChargeCarrierCall(Boolean useNMForChargeCarrierCall) {
		this.useNMForChargeCarrierCall = useNMForChargeCarrierCall;
	}

	public Boolean getChargeCanVaryWithoutIsotopeInfo() {
		return chargeCanVaryWithoutIsotopeInfo;
	}

	public void setChargeCanVaryWithoutIsotopeInfo(Boolean chargeCanVaryWithoutIsotopeInfo) {
		this.chargeCanVaryWithoutIsotopeInfo = chargeCanVaryWithoutIsotopeInfo;
	}

	public Map<Integer, AnnotationInfo> getAnnotationMap() {
		return annotationMap;
	}

	public void setAnnotationMap(Map<Integer, AnnotationInfo> annotationMap) {
		this.annotationMap = annotationMap;
	}

	public Boolean getZeroMeansMissing() {
		return zeroMeansMissing;
	}

	public void setZeroMeansMissing(Boolean zeroMeansMissing) {
		this.zeroMeansMissing = zeroMeansMissing;
	}

	public String getSkippedChargeCarriers() {
		return skippedChargeCarriers;
	}

	public void setSkippedChargeCarriers(String skippedChargeCarriers) {
		this.skippedChargeCarriers = skippedChargeCarriers;
	}

	public String getIsotopeAnnotationWarnings() {
		return isotopeAnnotationWarnings == null ? "None" : isotopeAnnotationWarnings;
	}

	public void setIsotopeAnnotationWarnings(String isotopeAnnotationWarnings) {
		this.isotopeAnnotationWarnings = isotopeAnnotationWarnings;
	}
	
	
	public void initializeIsotopeAnnotationWarnings(Boolean useTierForChg2, Boolean useTierForChg3, Boolean forPos) {
		String msg =  "None";
		if (useTierForChg2 && useTierForChg3) 
		    msg = "Features with multiple charge were skipped by annotation engine because no tier one carriers with charge " + (forPos ? "> 1" : "< -1") + "were designated.";
		else if (useTierForChg2)
			msg = "Features with charge 2 were skipped by annotation engine because no tier one charge carriers with charge " + (forPos ? "" : "-") + "2 were designated. ";
			//msg = "No tier one charge carriers with charge 2 designated. Annotation base for doubly charged features restricted to existing tier 1 charge carriers (carrying charge 1)";
		else if (useTierForChg3) 
			msg = "Features with charge 3 were skipped by annotation engine because no tier one charge carriers with charge " + (forPos ? "" : "-") + " 3 were designated. ";
		
			//msg = "No tier one charge carriers with charge 3 designated. Annotation base for triply charged features restricted to existing tier 1 charge carriers (carrying charge 1)";
			
		setIsotopeAnnotationWarnings(msg);
	}
}
