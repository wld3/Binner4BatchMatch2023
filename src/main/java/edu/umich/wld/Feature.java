package edu.umich.wld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Feature {
	private String name = null;
	private Double mass = null;
	private Double rt = null;
	private Double percentDefect = null;
	private Double massDefectKendrick = null;
	private Integer rmDefect = null;
	private Double medianIntensity = null;
	private Integer medianIntensityIdx = null;
	private String isotope = "";
	private String furtherAnnotation = "";
	private Integer isotopeGroup = -1;
	private Boolean removeForClustering = false;
	private String adductOrNL = "";
	private String molecularIonNumber = "";
	private String chargeCarrier = "";
	private String neutralAnnotation = "";
	private String referenceMassString = null;
	private Double massError = null;
	private String derivation = "";
	private Integer binIndex = null;
	private Integer offsetWithinBin = null;
	private Integer oldCluster = null;
	private Integer newCluster = null;
	private Integer newNewCluster = null;
	private List<String> addedColValues = new ArrayList<String>();
	private List<Integer> isotopeGroupMembers = new ArrayList<Integer>();
	private List<String> annotationGroupMembers = new ArrayList<String>();
	private double [] unadjustedIntensityList = null;
	private double [] adjustedIntensityList = null;
	private Map<Integer, Double> outlierMap = new HashMap<Integer, Double>();
	private Boolean putativeMolecularIon = false;

	public Feature() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getMass() {
		return mass;
	}

	public void setMass(Double mass) {
		this.mass = mass;
	}

	public Double getRT() {
		return rt;
	}

	public void setRT(Double rt) {
		this.rt = rt;
	}

	public Double getPercentDefect() {
		return percentDefect;
	}

	public void setPercentDefect(Double percentDefect) {
		this.percentDefect = percentDefect;
	}
	
	public Double getMassDefectKendrick() {
		return massDefectKendrick;
	}

	public void setMassDefectKendrick(Double massDefectKendrick) {
		this.massDefectKendrick = massDefectKendrick;
	}

	public Double getMedianIntensity() {
		return medianIntensity;
	}

	public void setMedianIntensity(Double medianIntensity) {
		this.medianIntensity = medianIntensity;
	}

	public Integer getMedianIntensityIdx() {
		return medianIntensityIdx;
	}

	public void setMedianIntensityIdx(Integer medianIntensityIdx) {
		this.medianIntensityIdx = medianIntensityIdx;
	}

	public Integer getOldCluster() {
		return oldCluster;
	}

	public void setOldCluster(Integer oldCluster) {
		this.oldCluster = oldCluster;
	}

	public Integer getNewCluster() {
		return newCluster;
	}

	public void setNewCluster(Integer newCluster) {
		this.newCluster = newCluster;
	}
	
	public Integer getNewNewCluster() {
		return newNewCluster;
	}

	public void setNewNewCluster(Integer newNewCluster) {
		this.newNewCluster = newNewCluster;
	}

	public List<String> getAddedColValues() {
		return addedColValues;
	}

	public void setAddedColValues(List<String> addedColValues) {
		this.addedColValues = addedColValues;
	}

	public List<Integer> getIsotopeGroupMembers() {
		return isotopeGroupMembers;
	}

	public void setIsotopeGroupMembers(List<Integer> isotopeGroupMembers) {
		this.isotopeGroupMembers = isotopeGroupMembers;
	}

	public List<String> getAnnotationGroupMembers() {
		return annotationGroupMembers;
	}

	public void setAnnotationGroupMembers(List<String> annotationGroupMembers) {
		this.annotationGroupMembers = annotationGroupMembers;
	}

	public double [] getUnadjustedIntensityList() {
		return unadjustedIntensityList;
	}
	
	public void addOutlier(Integer key,  Double object) {
		if (outlierMap == null)
			outlierMap = new HashMap<Integer, Double>();
		
		outlierMap.put(key, object);
	}	
	
	public Map<Integer, Double> getOutlierMap() {
		return outlierMap;
	}

	public double [] getUnadjustedIntensityListWithOutliers()  {
		double [] withOutlierList = new double[unadjustedIntensityList.length];
		
		for (int i = 0; i < unadjustedIntensityList.length; i++)
			withOutlierList[i] = outlierMap.containsKey(i) ? outlierMap.get(i) : unadjustedIntensityList[i];
		
		return withOutlierList;
	}

	public void setUnadjustedIntensityList(double [] unadjustedIntensityList) {
		this.unadjustedIntensityList = unadjustedIntensityList;
	}

	public double [] getAdjustedIntensityList() {
		return adjustedIntensityList;
	}

	public void setAdjustedIntensityList(double [] adjustedIntensityList) {
		this.adjustedIntensityList = adjustedIntensityList;
	}

	public String getIsotope() {
		return isotope;
	}

	public void setIsotope(String isotope) {
		this.isotope = isotope;
	}

	public Integer getIsotopeGroup() {
		return isotopeGroup;
	}

	public void setIsotopeGroup(Integer isotopeGroup) {
		this.isotopeGroup = isotopeGroup;
	}

	public Boolean getRemoveForClustering() {
		return removeForClustering;
	}

	public void setRemoveForClustering(Boolean removeForClustering) {
		this.removeForClustering = removeForClustering;
	}

	public String getAdductOrNL() {
		return adductOrNL;
	}

	public void setAdductOrNL(String adductOrNL) {
		this.adductOrNL = adductOrNL;
	}
	
	public String getMolecularIonNumber() {
		return molecularIonNumber;
	}
	
	public void setMolecularIonNumber(String num) {
		this.molecularIonNumber = num;
	}
	
	public void setChargeCarrier(String chg) {
		this.chargeCarrier = chg;
	}
	
	public String getChargeCarrier() {
		return chargeCarrier;
		}
	
	public String getNeutralAnnotation() {
		return neutralAnnotation;
	}

	public void setNeutralAnnotation(String neutralAnnotation) {
		this.neutralAnnotation = neutralAnnotation;
	}

	public String getReferenceMassString() {
		return referenceMassString;
	}

	public void setReferenceMassString(String referenceMassString) {
		this.referenceMassString = referenceMassString;
	}
	
	public Double getMassError() {
		return massError;
	}

	public void setMassError(Double massError) {
		this.massError = massError;
	}

	public String getDerivation() {
		return derivation;
	}
	
	public void setDerivation(String derivation) {
		this.derivation = derivation;
	}

	public Integer getBinIndex() {
		return binIndex;
	}

	public void setBinIndex(Integer binIndex) {
		this.binIndex = binIndex;
	}

	public Integer getOffsetWithinBin() {
		return offsetWithinBin;
	}

	public void setOffsetWithinBin(Integer offsetWithinBin) {
		this.offsetWithinBin = offsetWithinBin;
	}
	
	public Boolean isPutativeMolecularIon() {
		return putativeMolecularIon;
	}

	public void markAsPutativeMolecularIon(Boolean putativeMolecularIon) {
		this.putativeMolecularIon = putativeMolecularIon;
	}

	public String getFurtherAnnotation() {
		return furtherAnnotation;
	}

	public void setFurtherAnnotation(String furtherAnnotation) {
		this.furtherAnnotation = furtherAnnotation;
	}

	public Integer getRmDefect() {
		return rmDefect;
	}

	public void setRmDefect(Integer rmDefect) {
		this.rmDefect = rmDefect;
	}
}
