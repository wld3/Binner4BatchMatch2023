////////////////////////////////////////////////////
// FeatureCorrelations.java
// Written by Jan Wigginton, Jul 30, 2017
////////////////////////////////////////////////////
package edu.umich.wld.clustering;

public class FeatureCorrelations
	{
	private Integer featureNumber = null;
	private double [] correlations;
	
	public FeatureCorrelations() 
		{
		}
	
	public double [] getCorrelations()
		{
		return correlations;
		}
	
	public void setCorrelations(double [] correlations)
		{
		this.correlations = correlations;
		}
	
	public Integer getFeatureNumber()
		{
		return featureNumber;
		}
	
	public void setFeatureNumber(Integer number)
		{
		this.featureNumber = number;
		}
	
	public String toString()
		{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < correlations.length; i++)
			sb.append(correlations[i] + ", ");
		
		return sb.toString().substring(0, sb.length() - 2);
		}
	}
