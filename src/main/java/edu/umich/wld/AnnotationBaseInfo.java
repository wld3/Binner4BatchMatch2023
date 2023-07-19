package edu.umich.wld;

public class AnnotationBaseInfo {
	private Double referenceMass = null;
	private String bestAdductAnnotation = null;
	private String bestAdductDerivation = null;
	private Double bestAdductMass = null;
	private Integer bestAdductAnnotationCount = null;
	private Integer charge = null;
	private Integer massMultiple = null;
	private Integer indexWithinCluster = null;
	
	public AnnotationBaseInfo() {
	}

	public Double getReferenceMass() {
		return referenceMass;
	}

	public void setReferenceMass(Double referenceMass) {
		this.referenceMass = referenceMass;
	}

	public String getBestAdductAnnotation() {
		return bestAdductAnnotation;
	}

	public void setBestAdductAnnotation(String bestAdductAnnotation) {
		this.bestAdductAnnotation = bestAdductAnnotation;
	}

	public String getBestAdductDerivation() {
		return bestAdductDerivation;
	}

	public void setBestAdductDerivation(String bestAdductDerivation) {
		this.bestAdductDerivation = bestAdductDerivation;
	}

	public Double getBestAdductMass() {
		return bestAdductMass;
	}

	public void setBestAdductMass(Double bestAdductMass) {
		this.bestAdductMass = bestAdductMass;
	}

	public Integer getBestAdductAnnotationCount() {
		return bestAdductAnnotationCount;
	}

	public void setBestAdductAnnotationCount(Integer bestAdductAnnotationCount) {
		this.bestAdductAnnotationCount = bestAdductAnnotationCount;
	}

	public Integer getCharge() {
		return charge;
	}

	public void setCharge(Integer charge) {
		this.charge = charge;
	}

	public Integer getMassMultiple() {
		return massMultiple;
	}

	public void setMassMultiple(Integer massMultiple) {
		this.massMultiple = massMultiple;
	}

	public Integer getIndexWithinCluster() {
		return indexWithinCluster;
	}

	public void setIndexWithinCluster(Integer indexWithinCluster) {
		this.indexWithinCluster = indexWithinCluster;
	}
	
	public String toString() { 
		return ObjectHandler.printObject(this);
	}
}
