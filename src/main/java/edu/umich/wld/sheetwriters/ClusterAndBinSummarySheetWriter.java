////////////////////////////////////////////////////
// ClusterAndBinSummarySheetWriter.java
// Written by Jan Wigginton May 2018
////////////////////////////////////////////////////
package edu.umich.wld.sheetwriters;

import java.io.Serializable;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import edu.umich.wld.BinStats;
import edu.umich.wld.GroupStatistics;
import edu.umich.wld.GroupStatistics.GroupStatType;


public class ClusterAndBinSummarySheetWriter extends BinnerInfoSheetWriter implements Serializable {
	
	private static final long serialVersionUID = 6417491361048719026L;
	
	public ClusterAndBinSummarySheetWriter(Workbook workBook) {
		super(workBook, 14);
	}
	
	public void fillSummaryTab(List<BinStats> binStats, Boolean useRebinClusters) {
		
		GroupStatistics groupStats = new GroupStatistics(binStats, useRebinClusters);
		groupStats.calculateDistributions();
		
		Sheet sheet = createEmptySheet("Bin Statistics", workBook);
		sheet.setColumnWidth(0, 5 * 256);
		sheet.setColumnWidth(1, 155 * 256);
		
		firstCol = 1;
		Integer rowNum = 0;
		
		try {
			PoiUtils.createBlankCleanRow(rowNum++, sheet, totalWidth, styleBoring);
			PoiUtils.createBlankCleanRow(rowNum++, sheet, totalWidth, styleBoring);
			PoiUtils.createBlankCleanRow(rowNum++, sheet, totalWidth, styleBoring);
		
			rowNum = createOverview(sheet, groupStats, rowNum,useRebinClusters);
			rowNum = createClustersPerBinDistribution(sheet, groupStats, rowNum, useRebinClusters);
			rowNum = createBinSizeDistribution(sheet, groupStats, rowNum);
			rowNum = createClusterSizeDistribution(sheet, groupStats, rowNum, useRebinClusters);
						
			for (int i = 0; i < 20; i++)
				PoiUtils.createBlankCleanRow(rowNum++, sheet, totalWidth, styleBoring);
			}
		catch (Exception e) { }
	}
		
	private int createOverview(Sheet sheet, GroupStatistics groupStats, int rowNum, Boolean useRebinClusters) {
		
		createSectionHeader(rowNum++, sheet, "Overview :"); 
		createTableEntry(rowNum++, sheet, "");
		createTableEntry(rowNum++, sheet, "");
		
		PoiUtils.createBlankCleanRow(rowNum, sheet, totalWidth, styleBoring);
		PoiUtils.createRowEntry(rowNum++, firstCol,  sheet, "Bins", styleBoringBoldUnderlinedEntry);
	
		String summaryString = "       Total: " + groupStats.getTotalNBins();
		createTableEntry(rowNum++, sheet, summaryString);
	
		Double avgCt = groupStats.getAverageBinSize();
		Integer minCt = groupStats.getMinBinSize(), maxCt = groupStats.getMaxBinSize();;
		String avgStr = String.format("%.2f", avgCt);
		
		summaryString = "       Features Per Bin: " + " Average " + avgStr + ", Max " + maxCt + ", Min " + minCt;
		createTableEntry(rowNum++, sheet, summaryString);
	   	
		avgCt = groupStats.getAverageClustersPerBin();
	    minCt = groupStats.getMinClustersPerBin();
	    maxCt = groupStats.getMaxClustersPerBin();
		avgStr = String.format("%.2f", avgCt);
		
		summaryString = "       Clusters Per Bin " +  (useRebinClusters ? "After Rebin: " : "After Subcluster: ") +" Average " + avgStr + ", Max " + maxCt + ", Min " + minCt;
		createTableEntry(rowNum++, sheet, summaryString);
		createTableEntry(rowNum++, sheet, "");
			
		PoiUtils.createBlankCleanRow(rowNum, sheet, totalWidth, styleBoring);
		PoiUtils.createRowEntry(rowNum++, firstCol,  sheet, "Clusters", styleBoringBoldUnderlinedEntry);
		
		summaryString = "       Total: " + groupStats.getTotalNClusters();
		createTableEntry(rowNum++, sheet, summaryString);
		
		avgCt = groupStats.getAverageClusterSize();
	    minCt = groupStats.getMinClusterSize();
	    maxCt = groupStats.getMaxClusterSize();
		
	    summaryString = "       Features Per Cluster " + (useRebinClusters ? "After Rebin: " : "After Subcluster: ") + " Average " + avgStr + ", Max " + maxCt + ", Min " + minCt;
		createTableEntry(rowNum++, sheet, summaryString);
	    createTableEntry(rowNum++, sheet, "");
		createTableEntry(rowNum++, sheet, "");
	
		return rowNum;
	}
	
	private int createClustersPerBinDistribution(Sheet sheet, GroupStatistics groupStats, int rowNum, Boolean useRebinClusters) {
		
		Integer ct = 1, binCt1 = null, binCt2 = null, binCt3 = null, ctIdx = null; 
			
		createSectionHeader(rowNum++, sheet, "Distribution :  Clusters Per Bin"); 
		createTableEntry(rowNum++, sheet, "");
		createTableEntry(rowNum++, sheet, "");

		String header = String.format("%1$50s", "                                              # Bins with N Clusters");
		createTableTitle(rowNum++, sheet, header);
		createTableEntry(rowNum++, sheet, "");
		
		header = String.format("%1$-20s%2$19s%3$30s%4$34s", "      N", "Before Subgrouping by RT", "After Rebin", "After Subcluster");
		PoiUtils.createBlankCleanRow(rowNum, sheet, totalWidth, styleBoring);
		PoiUtils.createRowEntry(rowNum++, firstCol,  sheet, header, styleBoringBoldUnderlinedEntry);
		
		int tot1 = 0, tot2 = 0, tot3 = 0;
		while (ct < groupStats.getOverallMaxNClustersPerBin()) {
			ctIdx = groupStats.getIthDataPointForStat(ct, GroupStatType.CLUSTERS_PER_BIN_BEFORE, true);
			if (ctIdx == null) {
				ct++;
				continue;
				}
			binCt1 = groupStats.getIthDataPointForStat(ct, GroupStatType.CLUSTERS_PER_BIN_BEFORE, false);
			binCt2 = groupStats.getIthDataPointForStat(ct, GroupStatType.CLUSTERS_PER_BIN_AFTER_REBIN, false);
			if (!useRebinClusters)
				binCt3 = groupStats.getIthDataPointForStat(ct, GroupStatType.CLUSTERS_PER_BIN_AFTER_SUBCLUSTER, false);
			 
			ct++;
			tot1 += binCt1 * ctIdx;
			tot2 += binCt2 * ctIdx;
			if (!useRebinClusters)
				tot3 += binCt3 * ctIdx;
			createTableEntry(rowNum++, sheet,  String.format("|     %1$-20s%2$13s%3$35s%4$30s     |", ctIdx,  binCt1, binCt2, useRebinClusters ? "-" : binCt3));
		}

		createUnderlineRow(rowNum++, sheet, 107);
		
		createTableTitle(rowNum++, sheet,  String.format("      %1$-20s%2$13s%3$35s%4$30s          ", "Total Clusters",  tot1, tot2, useRebinClusters ? "-" : tot3));
		createTableEntry(rowNum++, sheet, "");
		createTableEntry(rowNum++, sheet, "");
		
		return rowNum;
	}
	
	private int createClusterSizeDistribution(Sheet sheet, GroupStatistics groupStats, int rowNum, Boolean useRebinClusters) {
	
		Integer ct = 0, binCt1 = null, binCt2 = null, binCt3 = null, ctIdx = null; 
		
		createSectionHeader(rowNum++, sheet, "Cluster Size Distribution ");
		createTableEntry(rowNum++, sheet, "");
		createTableEntry(rowNum++, sheet, "");
		
		String header = String.format("%1$40s", "                                          # Clusters with N Features");
		createTableTitle(rowNum++, sheet, header);
		createTableEntry(rowNum++, sheet, "");
		
		header = String.format("%1$-20s%2$15s%3$26s%4$26s", "       N", "Before Subgrouping by RT", "After Rebin", "After Subcluster");
		PoiUtils.createBlankCleanRow(rowNum, sheet, totalWidth, styleBoring);
		PoiUtils.createRowEntry(rowNum++, firstCol,  sheet, header, styleBoringBoldUnderlinedEntry);
		
		// Need to replace with specific loop over keys
		int totalCt1 = 0, totalCt2 = 0, totalCt3 = 0;
		while (ct < groupStats.getOverallMaxClusterSize()) {
			ctIdx = groupStats.getIthDataPointForStat(ct, GroupStatType.FEATURES_PER_CLUSTER_BEFORE, true);
			if (ctIdx == null) {
				ct++;
				continue;
				}
			binCt1 = groupStats.getIthDataPointForStat(ct, GroupStatType.FEATURES_PER_CLUSTER_BEFORE, false);
			binCt2 = groupStats.getIthDataPointForStat(ct, GroupStatType.FEATURES_PER_CLUSTER_AFTER_REBIN, false);
			if (!useRebinClusters)
				binCt3 = groupStats.getIthDataPointForStat(ct, GroupStatType.FEATURES_PER_CLUSTER_AFTER_SUBCLUSTER, false);
			ct++;
			totalCt1 += (binCt1 * ctIdx);
			totalCt2 += (binCt2 * ctIdx);
			if (!useRebinClusters)
				totalCt3 += (binCt3 * ctIdx);
			createTableEntry(rowNum++, sheet,  String.format("|      %1$-20s%2$13s%3$24s%4$26s      |", ctIdx,  binCt1, binCt2, useRebinClusters ? "-" : binCt3));
		}
		
		createUnderlineRow(rowNum++, sheet, 94);
		createTableTitle(rowNum++, sheet,  String.format("      %1$-21s%2$13s%3$24s%4$26s       ", "Total Features",  totalCt1, 
				totalCt2, useRebinClusters ? "-" : totalCt3));
		createTableEntry(rowNum++, sheet, "");
		createTableEntry(rowNum++, sheet, "");
		createTableBottom(rowNum++, sheet);
	
		for (int i = 0; i < 2; i++)
			PoiUtils.createBlankCleanRow(rowNum++, sheet, totalWidth, styleBoring);
	
		return rowNum;
	}
	
	private int createBinSizeDistribution(Sheet sheet, GroupStatistics groupStats, int rowNum) {
	
		Integer binCt = null, ctIdx = null, ct = 0;
	
		createSectionHeader(rowNum++, sheet, "Bin Size Distribution");
		createTableEntry(rowNum++, sheet, "");
		createTableEntry(rowNum++, sheet, "");
		
		String header = String.format("%1$-13s", "       # Bins with N Features");
		createTableTitle(rowNum++, sheet, header);
		createTableEntry(rowNum++, sheet, "");
	
		header = String.format("%1$-20s%2$19s", "       N", "             # Bins       ");
		PoiUtils.createBlankCleanRow(rowNum, sheet, totalWidth, styleBoring);
		PoiUtils.createRowEntry(rowNum++, firstCol,  sheet, header, styleBoringBoldUnderlinedEntry);
	
		int tot = 0;
		while (ct < groupStats.getOverallMaxBinSize()) {
			ctIdx = groupStats.getIthDataPointForStat(ct, GroupStatType.FEATURES_PER_BIN, true);
			if (ctIdx == null) {
				ct++;
				continue;
				}
			binCt = groupStats.getIthDataPointForStat(ct++, GroupStatType.FEATURES_PER_BIN, false);
			createTableEntry(rowNum++, sheet,  String.format("|      %1$-20s%2$12s      |", ctIdx,  binCt));
			tot += (ctIdx * binCt);
		}
		
		createUnderlineRow(rowNum++, sheet, 43);
		
		createTableTitle(rowNum++, sheet,  String.format("  %1$-20s%2$17s  ", "Total Features",  tot));
		createTableEntry(rowNum++, sheet, "");
		createTableEntry(rowNum++, sheet, "");
		
		return rowNum;
	}
}


