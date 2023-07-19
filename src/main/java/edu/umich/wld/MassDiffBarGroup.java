////////////////////////////////////////////////////
// MassDiffBarGroup.java
// Written by Jan Wigginton, Oct 27, 2017
////////////////////////////////////////////////////
package edu.umich.wld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MassDiffBarGroup
	{
	private List<MassDiffBar> groupBars;
	private double groupMax = Double.MIN_VALUE, groupMin = Double.MAX_VALUE , groupPeak, groupMedian;
	private int maxCount = 0; 
	private int binIdxForMax = 0, totalCount = 0;
	private String annotation = "No annotation", annotatedValue = "NA";
	private Boolean isForced = false;
	
	public MassDiffBarGroup() 
		{ 
		groupBars = new ArrayList<MassDiffBar>(); 
		totalCount = 0;
		}
	
	public void addSlot(MassDiffBar bar)
		{
		groupBars.add(bar);
		groupMax = Math.max(groupMax, bar.getBinValue());
		groupMin = Math.min(groupMin,  bar.getBinValue());
		
		if (bar.getCount() > maxCount) 
			{
			maxCount = bar.getCount();
			binIdxForMax = bar.getIndexInCounts();
			}
		totalCount += bar.getCount();
		if (bar.isForced())
			isForced = true;
		}
	
	
	public void fillInBarRange(int [] counts, Integer valueToTrimAt)
		{
		if (groupBars == null || groupBars.size() == 0)
			return;
		
		Collections.sort(groupBars, new MassDiffBarByMassComparator());
		
		MassDiffBar prevBar = groupBars.get(0);
		List<MassDiffBar> filledInBars = new ArrayList<MassDiffBar>();
		double skipStep = 0.001;
		
		Map<Integer, Integer> takenIndices = new HashMap<Integer, Integer>();
		
		for (int i = 1; i < groupBars.size(); i++)
			{
			MassDiffBar currBar = groupBars.get(i);
		    takenIndices.put(currBar.getIndexInCounts(), null);
		    int missedBars = 0;
		    int idx = 0;
			while (currBar.getIndexInCounts() > prevBar.getIndexInCounts() + missedBars + 1)
				{
				idx = prevBar.getIndexInCounts() + missedBars + 1;
				filledInBars.add(new MassDiffBar(prevBar.getBinValue() + skipStep, counts[idx], idx, prevBar.getBinGroup(), true));
				skipStep += 0.001;
				missedBars++;
				}
			prevBar = currBar;
			}
		
		for (int j = 0;  j < filledInBars.size(); j++) {
			takenIndices.put(filledInBars.get(j).getIndexInCounts(), null);
			groupBars.add(filledInBars.get(j));
		}
		
		Collections.sort(groupBars, new MassDiffBarByMassComparator());
	
		if (totalCount == 0) 
			this.binIdxForMax = groupBars.get(0).getIndexInCounts();
		}

	
	public int finalizeTotalCount() {
		int total = 0;
		
		for (MassDiffBar bar : getGroupBars()) {
			total += bar.getCount();
		}
		
		this.totalCount = total;
		return this.totalCount;
	}
	
	public List<MassDiffBar> getGroupBars()
		{
		return groupBars;
		}

	public double getGroupMax()
		{
		return groupMax;
		}

	public double getGroupMin()
		{
		return groupMin;
		}

	public double getGroupPeak()
		{
		return groupPeak;
		}

	public double getGroupMedian()
		{
		return groupMedian;
		}

	public int getMaxCount()
		{
		return maxCount;
		}

	public String getAnnotation()
		{
		return annotation;
		}

	public void setGroupBins(List<MassDiffBar> groupBars)
		{
		this.groupBars = groupBars;
		}

	public void setGroupMax(double groupMax)
		{
		this.groupMax = groupMax;
		}

	public void setGroupMin(double groupMin)
		{
		this.groupMin = groupMin;
		}

	public void setGroupPeak(double groupPeak)
		{
		this.groupPeak = groupPeak;
		}

	public void setGroupMedian(double groupMedian)
		{
		this.groupMedian = groupMedian;
		}

	public void setMaxCount(int maxCount)
		{
		this.maxCount = maxCount;
		}

	public void setAnnotation(String annotation)
		{
		this.annotation = annotation;
		}

	public int getBinIdxForMax()
		{
		return binIdxForMax;
		}

	public void setBinIdxForMax(int binIdxForMax)
		{
		this.binIdxForMax = binIdxForMax;
		}

	public int getTotalCount()
		{
		return totalCount;
		}

	public void setTotalCount(int totalCount)
		{
		this.totalCount = totalCount;
		}

	public String getAnnotatedValue()
		{
		return annotatedValue;
		}

	public void setAnnotatedValue(String annotatedValue)
		{
		this.annotatedValue = annotatedValue;
		}

	public Boolean getIsForced()
		{
		return isForced;
		}

	public void setIsForced(Boolean isForced)
		{
		this.isForced = isForced;
		}
	}
