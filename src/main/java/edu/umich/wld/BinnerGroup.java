////////////////////////////////////////////////////
// BinnerCluster.java
// Written by Jan Wigginton, Jan 24, 2017
////////////////////////////////////////////////////
package edu.umich.wld;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BinnerGroup implements Serializable
	{
	private static final long serialVersionUID = 3025928671379351173L;
	
	private List<Integer> featureIndexList;
	private String title = "TITLE";
	
	public BinnerGroup() 
		{ 
		featureIndexList = new ArrayList<Integer>();
		}

	public List<Integer> getFeatureIndexList() 
		{
		return featureIndexList;
		}

	public void setFeatureIndexList(List<Integer> featureIndexList) 
		{
		this.featureIndexList = featureIndexList;
		}

	public String getTitle() 
		{
		return title;
		}

	public void setTitle(String title) 
		{
		this.title = title;
		}
	}
