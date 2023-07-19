package edu.umich.wld;

public class BinnerIndexUtils {
	public static int getFeatureIndexFromRTSortedFullBin(int binIndex, int offsetWithinBin) {
		return AnalysisDialog.getAnalysisData().getBinContents().get(binIndex).get(offsetWithinBin);
	}
	
	public static Feature getFeatureFromRTSortedFullBin(int binIndex, int offsetWithinBin) {
		return AnalysisDialog.getAnalysisData().getNonMissingFeaturesInOriginalOrder().
				get(AnalysisDialog.getAnalysisData().getBinContents().get(binIndex).get(offsetWithinBin));
	}
	
	public static int getFeatureIndexFromClusterSortedDeisotopedBin(int binIndex, int offsetWithinBin) {
		return AnalysisDialog.getAnalysisData().getBinwiseFeaturesForClustering().get(binIndex).
				get(AnalysisDialog.getAnalysisData().getIndexedClustersForRebinning().get(offsetWithinBin).getIndex());
	}
	
	public static Feature getFeatureFromClusterSortedDeisotopedBin(int binIndex, int offsetWithinBin) {
		return AnalysisDialog.getAnalysisData().getNonMissingFeaturesInOriginalOrder().
				get(AnalysisDialog.getAnalysisData().getBinwiseFeaturesForClustering().get(binIndex).get(offsetWithinBin));
	}
	
	public static double [] getFeatureCorrelationsFromFeatureIndex(int featureIndex) {
		Feature feature = AnalysisDialog.getAnalysisData().getNonMissingFeaturesInOriginalOrder().get(featureIndex);
		return AnalysisDialog.getAnalysisData().getBinwiseCorrelations().get(feature.getBinIndex()).
				getRow(feature.getOffsetWithinBin());
	}
	
	public static double [] getFeatureCorrelationsFromFeature(Feature feature) {
		return AnalysisDialog.getAnalysisData().getBinwiseCorrelations().get(feature.getBinIndex()).
				getRow(feature.getOffsetWithinBin());
	}
	
	public static double [] getFeatureMassDiffsFromFeature(Feature feature) {
		return AnalysisDialog.getAnalysisData().getBinwiseMassDiffs().get(feature.getBinIndex()).
				getRow(feature.getOffsetWithinBin());
	}
	
	public static Integer [] getFeatureMassDiffClassesFromFeature(Feature feature) {
		return AnalysisDialog.getAnalysisData().getBinwiseMassDiffClasses().get(feature.getBinIndex())
				[feature.getOffsetWithinBin()];
	}
}
