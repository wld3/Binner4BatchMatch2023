////////////////////////////////////////////////////
// MassDiffHistogram.java
// Written by Jan Wigginton, October 14, 2017
////////////////////////////////////////////////////
package edu.umich.wld;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.linear.RealMatrix;

public class MassDiffHistogram
	{
	private double multiplier = 1000.0, upperThreshold = BinnerConstants.LARGEST_MASS_DIFF, trackingCutoff;
	private int maxSlotCount, secondMaxSlotCount, topPercentile, nextPercentile;
	private Integer nAnnotatedValues2 = 0, nValues = 0, nBarSlots = 0, nTotalPoints;
	
	// advanced tuning parameters
	// number of forced bins on each side of a forced annotation  (one with values not registering as frequent
	private int forcedAnnotationSlotTolerance = 10;
	private double peakPercentileThreshold = 0.02;
	private double significantBarThreshold = 0.1;
	private double groupTailPercentileThreshold = 0.25;
	private int groupGapCutoff = 3;
	
	private List<MassDiffBarGroup> histogramGroups;
	private int [] countsBySlot;
	private Boolean isDeisotoped = true;
	
	
	public MassDiffHistogram() 
		{ 
		countsBySlot = new int[(int) (1 + multiplier * upperThreshold)];
		}	
	
	public void addCounts(RealMatrix rawMassDiffs, Double min, Double max)
		{
		if (rawMassDiffs == null)
			return;
		
		int nRows = rawMassDiffs.getRowDimension();
		
		for (int i = 0; i < nRows; i++)
			for (int j = 0; j < i; j++)
				{
				int valueSlot = getHistogramSlotForMassDiffValue(rawMassDiffs.getEntry(i, j));
				
				if (valueSlot != -1 && valueSlot <= upperThreshold * multiplier)
					countsBySlot[valueSlot]++;
				}
		}	
	
	public int getHistogramSlotForMassDiffValue(double x)
		{
		double pos = Math.abs(x);
		
	    BigDecimal trunc = new BigDecimal(String.valueOf(pos)).setScale(3, BigDecimal.ROUND_FLOOR);
	    
	    String strVal = trunc.toString();
	    
		Double dblVal = Double.parseDouble(strVal);
		
		if (dblVal < Double.MIN_VALUE)
			return 0;
	
		dblVal *= 1000;
		
		// bump to deal with slight numeric discrepancy in parseDouble (leads to X.99999999999999...  instead of .0 for ints)
		dblVal += .000001;
		
		int returnVal = dblVal.intValue();
		
		return (returnVal > upperThreshold * multiplier ? -1  : returnVal);
		}
	
	public void generateHistogram(Map<Integer, AnnotationInfo> annotFileMap, Double minValue, Double maxValue)
		{
		Map<Integer, Integer> annotatedHistogramSlots = new HashMap<Integer, Integer>();
	
		int maxValueSlot = -1;
		if (annotFileMap != null)
			for (AnnotationInfo info: annotFileMap.values())
				{
				if (Math.abs(info.getMass()) > this.trackingCutoff)
					continue;
				
				Integer valueSlot = getHistogramSlotForMassDiffValue(Math.abs(info.getMass()));
				annotatedHistogramSlots.put(valueSlot, 0);
				for (Integer i = 1; i <= forcedAnnotationSlotTolerance; i++)
					{
					if (valueSlot < countsBySlot.length - this.forcedAnnotationSlotTolerance)
						annotatedHistogramSlots.put(valueSlot + i, 0);
					if (valueSlot > i)
						annotatedHistogramSlots.put(valueSlot - i , 0);
					}
				maxValueSlot = Math.max(maxValueSlot, valueSlot + forcedAnnotationSlotTolerance);
				}
		
		maxValueSlot = Math.min(countsBySlot.length, maxValueSlot);
		
		int denseCutoffIdx = Math.min(((int) (BinnerConstants.DENSE_MASS_DIFF_THRESHOLD * multiplier)), countsBySlot.length); 
		int trackingCutoffIdx = maxValueSlot; //(int) (((int) trackingCutoff) * multiplier);
		denseCutoffIdx = (int) Math.min(denseCutoffIdx, trackingCutoffIdx); 
			
		nTotalPoints = 0;
		int nDensePoints = 0;
		for (int i = 0; i < countsBySlot.length; i++) {
			if (i < denseCutoffIdx)
				nDensePoints ++;
			nTotalPoints += countsBySlot[i];
		}
		double expectedCount = nDensePoints/denseCutoffIdx + 0.5;
			
		BinnerTestUtils.printArray(countsBySlot);

		List<MassDiffBar> ungroupedBars = new ArrayList<MassDiffBar>();
		nValues = 0;
		for (int i = 0; i < countsBySlot.length; i++)
			{
			if (i > trackingCutoffIdx)
				break;
			
			if (countsBySlot[i] > 3 * expectedCount)
				{
				nValues += countsBySlot[i];
				ungroupedBars.add(new MassDiffBar(i/multiplier, countsBySlot[i], i));
				}
			else if (annotatedHistogramSlots.get(new Integer(i)) != null)
				{
				nValues += countsBySlot[i];
				ungroupedBars.add(new MassDiffBar(i/multiplier, countsBySlot[i], i));
				}
			}
		
		Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
		int maxPtsPerSlot = 0;
		for (MassDiffBar bar : ungroupedBars)
			{
			if (countMap.get(bar.getCount()) == null)
				countMap.put(bar.getCount(), 0);
			
			int nBars = countMap.get(bar.getCount());
			countMap.put(bar.getCount(), nBars + 1);
			
			maxPtsPerSlot = Math.max(bar.getCount(), maxPtsPerSlot);
			}
		//valueToTrim
		Integer cutoff25 = 0, cutoffGroupTail, cutoffSigBar, cutoffPeak;
		Integer slotMembers = maxPtsPerSlot;
		int totalLessThan = 0;
	     slotMembers--;
	     nValues -= countsBySlot[0];
	     
		while (totalLessThan < peakPercentileThreshold * nValues)
			{
			totalLessThan += (countMap.get(slotMembers) == null ? 0 : countMap.get(slotMembers) * slotMembers);
			slotMembers--;
			}
		cutoffPeak = slotMembers;
		this.topPercentile = cutoffPeak;
		
		while (totalLessThan < significantBarThreshold * nValues)
			{
			totalLessThan += countMap.get(slotMembers) == null ? 0 : countMap.get(slotMembers) * slotMembers;
			slotMembers--;
			}
		cutoffSigBar = slotMembers;
		this.nextPercentile = cutoffSigBar;
		
		while (totalLessThan < groupTailPercentileThreshold * nValues)
			{
			totalLessThan += countMap.get(slotMembers) == null ? 0 : countMap.get(slotMembers) * slotMembers;
			slotMembers--;
			}
		cutoffGroupTail = slotMembers;
		
		while (totalLessThan < 0.75 * nValues)
			{
			totalLessThan += countMap.get(slotMembers) == null ? 0 : countMap.get(slotMembers) * slotMembers;
			slotMembers--;
			}
		cutoff25 = slotMembers;
		
		nBarSlots = 0;
		ungroupedBars = new ArrayList<MassDiffBar>();
		double endCutoff = cutoffGroupTail; //(cutoffSigBar + cutoffGroupTail) * 0.5 *  .8;
		
		Boolean binning = true;
		int tries = 0;
		while (binning && tries < 10) 
			{
			nBarSlots = 0;
			for (int i = 0; i < countsBySlot.length; i++) {
				Boolean nearEnd = i < 3|| i > countsBySlot.length - 5;
			    
				double runningAvg = countsBySlot[i] * 1.0;
				if (!nearEnd)
					runningAvg = 1.0 * (countsBySlot[i -2] + countsBySlot[i-1] + countsBySlot[i] + countsBySlot[i+1] + countsBySlot[i+2])/5.0;
				else if (i < 3) {
					runningAvg = 0.0;
					int j = Math.max(0,  i - 2);
					int ct = 0;
					while (j <= (i + 2)) {
						runningAvg += (1.0) * countsBySlot[j];
						j++;
					}
					runningAvg /= (1.0 * j);
				}
					
				if (countsBySlot[i] > cutoffSigBar || runningAvg > endCutoff)
					{
					ungroupedBars.add(new MassDiffBar(i/multiplier, countsBySlot[i], i));
					nBarSlots += countsBySlot[i];
					}
				else if (annotatedHistogramSlots.get(new Integer(i)) != null)
					{
					ungroupedBars.add(new MassDiffBar(i/multiplier, countsBySlot[i], i,-1, true));
					}
			}
			groupBars(ungroupedBars, cutoffGroupTail);
			
			if (tries > 10)
				break;
			
			binning = getNEmpiricalBars() < 10 && endCutoff > 5;
			
			if (binning) 
				{
				endCutoff *= (tries < 5 ? 0.8 : 0.7);
				this.histogramGroups.clear();
				ungroupedBars.clear();
				}
			else {
				binning = getNEmpiricalBars() > 24 && endCutoff < 12;
			
				if (binning) 
					{
					endCutoff *= 1.1;
					this.histogramGroups.clear();
					ungroupedBars.clear();
					}
				}
			
			tries++;
			}
		if (annotFileMap != null)
			annotateGroups(annotFileMap);
		}

	private void annotateGroups(Map<Integer, AnnotationInfo> annotFileMap)
		{
		for (MassDiffBarGroup group : this.histogramGroups)
			{
			for (AnnotationInfo info : annotFileMap.values())
				{
				double annotatedValue = Math.abs(info.getMass());
				if (annotatedValue > trackingCutoff) 
					continue;
				if (annotatedValue < group.getGroupMin())
					continue;
				
				if (annotatedValue > group.getGroupMax())
					continue;
				
				group.setAnnotation(info.getAnnotation() + ("0".equals(info.getCharge()) ? "" : " (charge carrier)"));
				group.setAnnotatedValue(String.format("%.3f", info.getMass()));
				}
			group.finalizeTotalCount();
			}
		}
	
	private void groupBars(List<MassDiffBar> significantBars, Integer valueToTrimAt)
		{
		if (significantBars == null || significantBars.size() < 1) 
			return;
			
		Collections.sort(significantBars, new MassDiffBarByMassComparator());
	
		List<Integer> splitGroupMinIndices = new ArrayList<Integer>();
		
		for (int trials = 0; trials < 2; trials++)  {
			
			secondMaxSlotCount = -1;
			MassDiffBarGroup firstGroup = new MassDiffBarGroup();
			
			histogramGroups = new ArrayList<MassDiffBarGroup>();
			histogramGroups.add(firstGroup);
		
			firstGroup.addSlot(significantBars.get(0));
			MassDiffBar prevBar = significantBars.get(0);

			int nBarsInGroup = 0;
			Integer currGroupMin = Integer.MAX_VALUE, currMinIdx = 0;
		    int nGroups = 1;
		    maxSlotCount = 0;
		    Boolean foundDownhill = false, foundSaddle = false, foundSplit = false;
			
		 	for (int bar = 1; bar < significantBars.size(); bar++)
				{
				MassDiffBar currentBar = significantBars.get(bar);
				
				if (currentBar.getCount() < prevBar.getCount() ) { 
					foundDownhill = true;
				}
				
				if (trials < 1 && foundDownhill && nBarsInGroup > 5 && nBarsInGroup <= 20 && currentBar.getCount() < currGroupMin) {					
					currGroupMin = currentBar.getCount();
					currMinIdx = bar;
					foundSaddle = true;
				}
					
				if (!foundSplit && nBarsInGroup > 25 && trials < 1 && foundSaddle) {
					foundSplit = true;
					splitGroupMinIndices.add(currMinIdx);
				}
			
				if ((trials > 0 && splitGroupMinIndices.contains(bar)) || currentBar.getIndexInCounts() - prevBar.getIndexInCounts() > groupGapCutoff) // || dirChangeCt > 3)
					{
					
					foundSplit = false;
					nGroups++;
					nBarsInGroup = 0;
					histogramGroups.add(new MassDiffBarGroup());
					currGroupMin = Integer.MAX_VALUE;
					currMinIdx = 0;
					}
		         
		    	MassDiffBarGroup currentGroup = histogramGroups.get(nGroups - 1);
			    currentGroup.addSlot(currentBar);
			    if (currentBar.getCount() > maxSlotCount)
			    	{
			    	secondMaxSlotCount = maxSlotCount;
			    	maxSlotCount = currentBar.getCount();
			    	}
			    else 
			    	secondMaxSlotCount = Math.max(currentBar.getCount(), secondMaxSlotCount);
			    
			    nBarsInGroup++;
			 	prevBar = currentBar;
				}
		
			if (splitGroupMinIndices.size() < 1)
				break;
		}
		
	    for (int group = 0; group < histogramGroups.size(); group++)
	    	histogramGroups.get(group).fillInBarRange(countsBySlot, valueToTrimAt);
		}	
	
	public List<MassDiffBarGroup> getHistogramGroups()
		{
		return histogramGroups;
		}

	
	public int getMaxSlotCount()
		{
		return maxSlotCount;
		}
	
	public int getSecondMaxSlotCount()
		{
		return secondMaxSlotCount;
		}


	public int getTopPercentile()
		{
		return topPercentile;
		}

	public int getNextPercentile()
		{
		return nextPercentile;
		}

	
	public Integer getnValues() 
		{
		return nValues;
		}
	
	public Integer getnBarSlots() 
		{
		return nBarSlots;
		}

	public Integer getnTotalPoints() 
		{
		return nTotalPoints;
		}

	public Boolean getIsDeisotoped() 
		{
		return isDeisotoped;
		}

	public void setIsDeisotoped(Boolean isDeisotoped) 
		{
		this.isDeisotoped = isDeisotoped;
		}

	public double getTrackingCutoff() 
		{
		return trackingCutoff;
		}

	public void setTrackingCutoff(double trackingCutoff) 
		{
		this.trackingCutoff = Math.min(upperThreshold, trackingCutoff);
		}
	
	public Integer getNEmpiricalBars() { 
		if (histogramGroups == null || histogramGroups.size() == 0) 
			return 0;
		
		Integer nEmpiricalBars = 0;
		for (MassDiffBarGroup grp : histogramGroups) {
			if (!grp.getIsForced()) 
				nEmpiricalBars++;
		}
		return nEmpiricalBars;
	}
}


