////////////////////////////////////////////////////
//FancyBinnerOutputContainer.java
//Created Jan 24, 2017
////////////////////////////////////////////////////
package edu.umich.wld.sheetwriters;

import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
//import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import edu.umich.wld.AnalysisData;
import edu.umich.wld.AnalysisDialog;
import edu.umich.wld.BinStats;
import edu.umich.wld.BinnerConstants;
import edu.umich.wld.BinnerGroup;
import edu.umich.wld.BinnerIndexUtils;
import edu.umich.wld.Feature;
import edu.umich.wld.MassDiffHistogram;
import edu.umich.wld.StringUtils;
import edu.umich.wld.SummaryInfo;


public class FancyBinnerOutputContainer2 extends BinnerSpreadSheetWriter implements Serializable
	{
	private static final long serialVersionUID = 4283630477985403483L;
	
	private int nBinMassDiffAnnotations; 
	
	private String fileName;
	private List<BinnerOutput> outputList;
	private Palette colorPalette = null, mdColorPalette = null, histColorPalette = null;
	private int startCol = -1, rowCt = 0, rowIndex = 0;
	private SXSSFWorkbook workBook = null;
	private List<XSSFCellStyle> styleList = new ArrayList<XSSFCellStyle>();
	private Boolean skipBinwiseOutput; 
	
	public FancyBinnerOutputContainer2(List<BinnerOutput> outputList, String fileName) 
		{ 
		this(outputList, fileName, null, null, null, false);
		}	
		
	public FancyBinnerOutputContainer2(List<BinnerOutput> outputList, String fileName, Palette palette, 
		Palette mdPalette, Palette histogramPalette, Boolean skipBinwiseOutput)
		{
		this.outputList = outputList;
		this.fileName = fileName;
		this.colorPalette = palette;
		this.mdColorPalette = mdPalette;
		this.histColorPalette = histogramPalette;
		this.skipBinwiseOutput = skipBinwiseOutput;
		}
		
		
	public void generateExcelReport(AnalysisData analysisData, OutputStream output, SummaryInfo summaryInfo, 
	 MassDiffHistogram histogramData, List<BinStats> binStats)
		{
		try 
			{
			nBinMassDiffAnnotations = 0;
			
			workBook = new SXSSFWorkbook(100); 	
			workBook.setCompressTempFiles(true);
			
			Map<Integer, List<XSSFCellStyle>> styleMap = initializeStyleMap(workBook, colorPalette, false);
			Map<Integer, List<XSSFCellStyle>> mdStyleMap = initializeStyleMap(workBook, mdColorPalette, true);
			
			styleList.add(BinnerConstants.STYLE_BORING, grabStyleBoring(workBook));
			styleList.add(BinnerConstants.STYLE_INTEGER, grabStyleInteger(workBook));
			styleList.add(BinnerConstants.STYLE_NUMERIC, grabStyleNumeric(workBook));
			styleList.add(BinnerConstants.STYLE_YELLOW, grabStyleYellow(workBook, true, false, false, false));
			styleList.add(BinnerConstants.STYLE_LIGHT_BLUE, grabStyleLightBlue(workBook));
			styleList.add(BinnerConstants.STYLE_LIGHT_GREEN, grabStyleLightGreen(workBook));
			styleList.add(BinnerConstants.STYLE_LAVENDER, grabStyleLavender(workBook, true));
			styleList.add(BinnerConstants.STYLE_INTEGER_GREY, grabStyleIntegerGrey(workBook));
			styleList.add(BinnerConstants.STYLE_NUMERIC_GREY, grabStyleNumericGrey(workBook));
			styleList.add(BinnerConstants.STYLE_INTEGER_GREY_CENTERED, grabStyleIntegerGreyCentered(workBook));
			styleList.add(BinnerConstants.STYLE_NUMERIC_GREY_CENTERED, grabStyleNumericGreyCentered(workBook));
			styleList.add(BinnerConstants.STYLE_BORING_LEFT, grabStyleBoringLeft(workBook));
			styleList.add(BinnerConstants.STYLE_HEADER_WRAPPED, grabStyleBoringHeader(workBook, true));
			styleList.add(BinnerConstants.STYLE_NUMERIC_SHORTER, grabStyleNumericShorter(workBook));
			styleList.add(BinnerConstants.STYLE_BORING_RIGHT, grabStyleBoringRight(workBook));
			styleList.add(BinnerConstants.STYLE_NUMERIC_LAVENDER, grabStyleLavender(workBook, false));
			styleList.add(BinnerConstants.STYLE_LIGHT_BLUE_LEFT, grabStyleLightBlueLeft(workBook));
			styleList.add(BinnerConstants.STYLE_LIGHT_GREEN_LEFT, grabStyleLightGreenLeft(workBook));
			styleList.add(BinnerConstants.STYLE_INTEGER_CENTERED, grabStyleIntegerCentered(workBook));
			styleList.add(BinnerConstants.STYLE_LIGHTER_BLUE, grabStyleLighterBlue(workBook));
				
			
			Font font = workBook.createFont();
			font.setFontName("Courier");
			
			for (XSSFCellStyle style : styleList)
				style.setFont(font);
			
			for (List<XSSFCellStyle> styleList : styleMap.values())
			for (XSSFCellStyle style : styleList) {
				style.setFont(font);
				style.setAlignment(HorizontalAlignment.CENTER);
				style.setVerticalAlignment(VerticalAlignment.CENTER);
				}
			
			for (List<XSSFCellStyle> styleList : mdStyleMap.values())
				for (XSSFCellStyle style : styleList)
					style.setFont(font);
			
			//createFreezePane
			BinnerSummarySheetWriter summaryWriter = new BinnerSummarySheetWriter(workBook);
			summaryWriter.fillSummaryTab(summaryInfo);
			
			BinnerHistogramWriter histogramWriter = new BinnerHistogramWriter(histColorPalette);
			histogramWriter.createHistogramTab(workBook, histogramData);
			
			ClusterAndBinSummarySheetWriter binSummaryWriter = new ClusterAndBinSummarySheetWriter(workBook);
			binSummaryWriter.fillSummaryTab(binStats, summaryInfo.getAnnotateRebinCluster());
			initializeColWidths(analysisData);
			
			Double cumulativeProgressBarWeight = 0.0;
			for (int i = 0; i < outputList.size(); i++) {
				
				AnalysisDialog.getBinningProgLabel().setText("Creating formatted Excel tab " + (i + 3) + " of " +
				 (outputList.size() + 3) + " ...      ");
				
				Integer originalIdx = outputList.get(i).getOriginalSheetIdx();
				if (!skipBinwiseOutput || !Arrays.asList(BinnerConstants.BY_BIN_OUTPUTS).contains(originalIdx))
					createBinnerSheet(analysisData, i, styleMap, mdStyleMap, summaryInfo.getAnnotateRebinCluster());
				
				cumulativeProgressBarWeight += (Arrays.asList(BinnerConstants.BY_BIN_OUTPUTS).contains(originalIdx)) ?
					BinnerConstants.BINWISE_OUTPUT_WEIGHT : BinnerConstants.CLUSTERWISE_OUTPUT_WEIGHT;
				
				AnalysisDialog.setProgressBar(AnalysisDialog.getProgressBarWeightMultiplier() * 
					(BinnerConstants.DEISOTOPE_PROGRESS_WEIGHT + BinnerConstants.CLUSTERING_PROGRESS_WEIGHT + 
					cumulativeProgressBarWeight));
				}
			
			AnalysisDialog.getBinningProgLabel().setText("Finishing up ...   ");
			workBook.write(output);
			output.close();
			workBook.dispose();
			} 
		catch (Exception e)  {  e.printStackTrace(); }
		}
		
		
	public void createBinnerSheet(AnalysisData analysisData, Integer outputIndex, Map<Integer, 
			List<XSSFCellStyle>> map, Map<Integer, List<XSSFCellStyle>> mdMap, Boolean onlyRebin)  throws Exception
		{
		BinnerOutput output = outputList.get(outputIndex);
		Integer originalIdx = output.getOriginalSheetIdx();
		
		if (Arrays.asList(BinnerConstants.REF_MASS_OUTPUTS).contains(originalIdx)) {

			if (BinnerConstants.OUTPUT_NONANNOTATED.equals(originalIdx)) {
				BinnerNonAnnotatedFeatureWriter featureWriter = new BinnerNonAnnotatedFeatureWriter(workBook);
				featureWriter.createFeatureTab(analysisData, output, styleList, onlyRebin);
			    return;
				}
			else if (BinnerConstants.OUTPUT_REF_MASS_PUTATIVE.equals(originalIdx)) {
				BinnerRefMassSheetWriter refMassWriter = new BinnerRefMassSheetWriter(workBook);
				refMassWriter.createRefMassTab(analysisData, output, BinnerConstants.OUTPUT_REF_MASS_PUTATIVE.equals(originalIdx), styleList, onlyRebin);
				return;
				}
			}
		
		Sheet sheet = createEmptySheet(output.getTabName(), workBook);
		//sheet.setRandomAccessWindowSize(100);
		Integer dataWidth = 14;
		if (Arrays.asList(BinnerConstants.CORR_OUTPUTS).contains(originalIdx)) dataWidth = 8;
		else if (Arrays.asList(BinnerConstants.INTENSITY_OUTPUTS).contains(originalIdx)) dataWidth = 17;
		
		sizeColumns(sheet, output, analysisData, dataWidth, 30);
		
		for (int j = 0; j < BinnerSpreadSheetWriter.CUSTOM_WIDTHS.length; j++)
			sheet.setColumnWidth(j, BinnerSpreadSheetWriter.CUSTOM_WIDTHS[j]);
		
		createHeader(output, sheet);
		for (BinnerGroup group : output.getGroups())
			createGroup(analysisData, outputIndex, group, sheet, map, mdMap, onlyRebin);
		}
		
		
		public void createHeader(BinnerOutput output, Sheet sheet) throws Exception
			{
			rowCt = 0;
			rowIndex = 0;
			sheet.createRow(rowCt);
		
			int col = 0;
			for (String header : output.getHeaders())
				{
				Boolean left = header.equals("Correlations") || header.equals("Mass Diffs");
				PoiUtils.createRowEntry(rowCt, col++, sheet, header, left ? styleList.get(BinnerConstants.STYLE_BORING_LEFT) :
						styleList.get(BinnerConstants.STYLE_HEADER_WRAPPED), 30);
				}
			rowCt += 2;
			}	
		
		
		public void createGroup(AnalysisData analysisData, Integer outputIndex, BinnerGroup group, Sheet sheet, 
		Map<Integer, List<XSSFCellStyle>> map, Map<Integer, List<XSSFCellStyle>> mdMap, Boolean onlyRebin) throws Exception
			{
			List<Integer> featureIndices = new ArrayList<Integer>();
			BinnerOutput output = outputList.get(outputIndex);
			Integer originalIdx = output.getOriginalSheetIdx();
			
			if (Arrays.asList(BinnerConstants.RT_SORT_OUTPUTS).contains(originalIdx))
				featureIndices = getFeatureIndexListSortedByFactor(analysisData, group, BinnerConstants.SORT_TYPE_RT);
			else if (Arrays.asList(BinnerConstants.MASS_SORT_OUTPUTS).contains(originalIdx))
				featureIndices = getFeatureIndexListSortedByFactor(analysisData, group, BinnerConstants.SORT_TYPE_MASS);
			else
				featureIndices = group.getFeatureIndexList();
			
			startCol = output.getHeaders().indexOf("Correlations");
			if (startCol == -1)
				startCol = output.getHeaders().indexOf("Mass Diffs");
			
			createAppropriateRowEntry(rowCt, 0, sheet, String.valueOf(++rowIndex),
					 styleList.get(BinnerConstants.STYLE_INTEGER_CENTERED), styleList.get(BinnerConstants.STYLE_NUMERIC),
					 	styleList.get(BinnerConstants.STYLE_BORING));
			rowIndex++;
	
			if (startCol != -1)
				createGroupHeader(analysisData, sheet, featureIndices, startCol);
			rowCt++;
			createGroupBody(analysisData, outputIndex, sheet, featureIndices, map, mdMap, onlyRebin);
			rowCt++;
			}
			
		
		public void createGroupHeader(AnalysisData analysisData, Sheet sheet, List<Integer> featureIndices, 
		 int startCol) throws Exception
			{
			
			for (int i = 1; i < startCol; i++)
				PoiUtils.createRowEntry(rowCt, i, sheet, "", styleList.get(BinnerConstants.STYLE_BORING));
			
			int j = startCol;
			for (int i : featureIndices)
				PoiUtils.createRowEntry(rowCt, j++, sheet, analysisData.getNonMissingFeaturesInOriginalOrder().get(i).getName(), 
							styleList.get(BinnerConstants.STYLE_BORING));
			
			}	
		
		
		public void createGroupBody(AnalysisData analysisData, Integer outputIndex, Sheet sheet, List<Integer> featureIndices, 
		 Map<Integer, List<XSSFCellStyle>> map, Map<Integer, List<XSSFCellStyle>> mdMap, Boolean onlyRebin) throws Exception
			{
			BinnerOutput output = outputList.get(outputIndex);
			Integer originalIdx = output.getOriginalSheetIdx();
            double minCorr = calculateMinCorr(analysisData, featureIndices, originalIdx);
            
			for (int rowFeatureIndex : featureIndices)
				{
				Row row = sheet.createRow(rowCt);
				row.setHeightInPoints((short) 16); 
				Feature feature = analysisData.getNonMissingFeaturesInOriginalOrder().get(rowFeatureIndex);
				
				createStatisticsBlock(feature, sheet, mdMap, originalIdx, onlyRebin);
				
				int i = output.getFirstBlankCol() + 1;			
				for (String value : feature.getAddedColValues())
					createAppropriateRowEntry(rowCt, i++, sheet, value,styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC),
						 	styleList.get(BinnerConstants.STYLE_BORING));
				
					//PoiUtils.createRowEntry(rowCt, i++, sheet, value, styleList.get(BinnerConstants.STYLE_BORING_RIGHT));
				
				i = output.getDataStartCol();
				if (Arrays.asList(BinnerConstants.INTENSITY_OUTPUTS).contains(originalIdx))
					createIntensityRow(feature, sheet, originalIdx, output);
				else if (Arrays.asList(BinnerConstants.MASS_DIFF_OUTPUTS).contains(originalIdx))
					createMassDiffRow(feature, sheet, featureIndices, originalIdx, mdMap, output); 
				else if (!Arrays.asList(BinnerConstants.COLOR_OUTPUTS).contains(originalIdx)) 
				    createOddballRow(feature, sheet, featureIndices, originalIdx, mdMap, output); 
				else
				    createCorrelationRow(feature, sheet, featureIndices, originalIdx, map, minCorr, output); 

				rowCt++;
				createAppropriateRowEntry(rowCt, 0, sheet, String.valueOf(++rowIndex),
						 styleList.get(BinnerConstants.STYLE_INTEGER_CENTERED), styleList.get(BinnerConstants.STYLE_NUMERIC),
						 	styleList.get(BinnerConstants.STYLE_BORING));
				}
			}

		
		private double calculateMinCorr(AnalysisData analysisData, List<Integer> featureIndices, Integer originalIdx)
			{
			double minCorr = 1.0;
			if (Arrays.asList(BinnerConstants.CORR_OUTPUTS).contains(originalIdx))
				for (int rowFeatureIndex : featureIndices)
					{
					double [] correlationList = BinnerIndexUtils.getFeatureCorrelationsFromFeatureIndex(rowFeatureIndex);
					for (int colFeatureIndex : featureIndices)
						{
						Feature colFeature = AnalysisDialog.getAnalysisData().getNonMissingFeaturesInOriginalOrder().
								get(colFeatureIndex);
						double dblValue = correlationList[colFeature.getOffsetWithinBin()];
						if (dblValue < minCorr) minCorr = dblValue;
						}
					}
			return minCorr;
			}
		
		
		private void createCorrelationRow(Feature feature, Sheet sheet, List<Integer> featureIndices, 
			Integer originalIdx, Map<Integer, List<XSSFCellStyle>> map, double minCorr, BinnerOutput output)
			{
			int i = output.getDataStartCol();
			
			double [] valueList = getCalculatedValueList(originalIdx, feature);
			for (int colFeatureIndex : featureIndices)
				{
				Feature colFeature = AnalysisDialog.getAnalysisData().getNonMissingFeaturesInOriginalOrder().
						get(colFeatureIndex);
				double dblValue = valueList[colFeature.getOffsetWithinBin()];
			
				Double ratio = null;
				if (Arrays.asList(BinnerConstants.LOC_OUTPUTS).contains(originalIdx)) 
					ratio = (dblValue - minCorr) / (1.0 - minCorr);	
				else 
					ratio = dblValue < 0.0 ? 0.0 : dblValue;    
				
				int nColors = map.keySet().size();
				Integer scaledValue = new Double((1.0 - ratio) * nColors).intValue();
				if (scaledValue < 0) scaledValue = 0;
				if (scaledValue >= nColors) scaledValue = nColors - 1;
				
				if (dblValue == (int) dblValue)
					PoiUtils.createNumericRowEntry(rowCt, i++, sheet, String.valueOf(dblValue), 
				      map.get(scaledValue).get(STYLE_INTEGER));
				else
					PoiUtils.createNumericRowEntry(rowCt, i++, sheet, String.valueOf(dblValue), 
					 map.get(scaledValue).get(STYLE_NUMERIC));
				}
			}

		
		private void createOddballRow(Feature feature, Sheet sheet, List<Integer> featureIndices, 
			Integer originalIdx, Map<Integer, List<XSSFCellStyle>> mdMap, BinnerOutput output)
			{
			int i = output.getDataStartCol();
			
			double [] valueList = getCalculatedValueList(originalIdx, feature);
			for (int colFeatureIndex : featureIndices)
				{
				Feature colFeature = AnalysisDialog.getAnalysisData().getNonMissingFeaturesInOriginalOrder().
						get(colFeatureIndex);
				double dblValue = valueList[colFeature.getOffsetWithinBin()];
			
				createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(dblValue),
						styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC), 
							styleList.get(BinnerConstants.STYLE_BORING));
				}
			}

		
		private void createMassDiffRow(Feature feature, Sheet sheet, List<Integer> featureIndices, 
			Integer originalIdx, Map<Integer, List<XSSFCellStyle>> mdMap, BinnerOutput output)
			{
			int i = output.getDataStartCol();
			
			double [] valueList = getCalculatedValueList(originalIdx, feature);
			Integer [] massDiffClasses = BinnerIndexUtils.getFeatureMassDiffClassesFromFeature(feature);
			for (int colFeatureIndex : featureIndices)
				{
				Feature colFeature = AnalysisDialog.getAnalysisData().getNonMissingFeaturesInOriginalOrder().
						get(colFeatureIndex);
				double dblValue = valueList[colFeature.getOffsetWithinBin()];
			
				Integer massDiffClass = massDiffClasses[colFeature.getOffsetWithinBin()];
				if (massDiffClass == -1) 
					createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(dblValue),
							styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC), 
								styleList.get(BinnerConstants.STYLE_BORING));
				else
					{
					massDiffClass %= mdMap.keySet().size();
					
					if (dblValue  == (int) dblValue)
						PoiUtils.createNumericRowEntry(rowCt, i++, sheet, String.valueOf(dblValue), mdMap.get(massDiffClass).get(STYLE_INTEGER));
					else
						PoiUtils.createNumericRowEntry(rowCt, i++, sheet, String.valueOf(dblValue), mdMap.get(massDiffClass).get(STYLE_NUMERIC));
					
					if (originalIdx.equals(BinnerConstants.OUTPUT_MASS_DIFF_BY_BIN_RT_SORT))
						this.nBinMassDiffAnnotations++;
					}
				}
			}   
		

		private void createIntensityRow(Feature feature, Sheet sheet, Integer originalIdx, BinnerOutput output)
			{
			int i = output.getDataStartCol();
			Map<Integer, Double> outlierMap = feature.getOutlierMap();
			
			Boolean isAdjusted = Arrays.asList(BinnerConstants.ADJ_OUTPUTS).contains(originalIdx);
			double [] intensityList = getIntensityList(originalIdx, feature);
			Double featureMedIntensity = feature.getMedianIntensity();
			Double logFeatureMedIntensity = Math.log1p(featureMedIntensity);
			
			Integer sampleIdx = 0;
			for (double dblValue : intensityList)
				{	
				if (outlierMap.containsKey(sampleIdx))
					{
					createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(dblValue), !isAdjusted ? 
							styleList.get(BinnerConstants.STYLE_LAVENDER) : 
								styleList.get(BinnerConstants.STYLE_NUMERIC_LAVENDER), 
								styleList.get(BinnerConstants.STYLE_NUMERIC_LAVENDER), 
								styleList.get(BinnerConstants.STYLE_NUMERIC_LAVENDER));
					}
				else if (dblValue < BinnerConstants.BIG_NEGATIVE / 2.0)
					createAppropriateRowEntry(rowCt, i++, sheet, ".", 
							styleList.get(BinnerConstants.STYLE_INTEGER_GREY_CENTERED),
								styleList.get(BinnerConstants.STYLE_NUMERIC_GREY_CENTERED), 
								styleList.get(BinnerConstants.STYLE_INTEGER_GREY_CENTERED));
				else if (isAdjusted && featureMedIntensity != null && (Math.abs(dblValue - logFeatureMedIntensity) < 
				 BinnerConstants.EPSILON || Math.abs(dblValue - featureMedIntensity) < BinnerConstants.EPSILON))
					{
					if (!sampleIdx.equals(feature.getMedianIntensityIdx()))
						createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(dblValue), 
								styleList.get(BinnerConstants.STYLE_INTEGER_GREY), 
									styleList.get(BinnerConstants.STYLE_NUMERIC_GREY), 
									styleList.get(BinnerConstants.STYLE_NUMERIC_GREY));
					else
						createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(dblValue),
								styleList.get(BinnerConstants.STYLE_INTEGER), 
									styleList.get(BinnerConstants.STYLE_NUMERIC), 
									styleList.get(BinnerConstants.STYLE_NUMERIC));
					}
				else
					createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(dblValue), 
							styleList.get(BinnerConstants.STYLE_INTEGER), 
								styleList.get(BinnerConstants.STYLE_NUMERIC), 
								styleList.get(BinnerConstants.STYLE_BORING));
				sampleIdx++;
				}
			}

		
		private void createStatisticsBlock(Feature feature, Sheet sheet, Map<Integer, List<XSSFCellStyle>> mdMap, 
			Integer originalIdx, Boolean onlyRebin )
			{
			int i = 0;
			
			createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(rowIndex),
					 styleList.get(BinnerConstants.STYLE_INTEGER_CENTERED), styleList.get(BinnerConstants.STYLE_NUMERIC),
					 	styleList.get(BinnerConstants.STYLE_BORING));
			PoiUtils.createRowEntry(rowCt, i++, sheet, feature.getName(), styleList.get(BinnerConstants.STYLE_BORING_LEFT));			
			createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getMass()),
					styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC),
						styleList.get(BinnerConstants.STYLE_BORING));			
			createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getRT()),
					styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC_SHORTER),
						styleList.get(BinnerConstants.STYLE_BORING));		
			createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(Math.round(feature.getMedianIntensity())),
					styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC),
						styleList.get(BinnerConstants.STYLE_BORING));		
			createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getMassDefectKendrick()),
					styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC),
						styleList.get(BinnerConstants.STYLE_BORING));
			
			Boolean haveIsotope = !feature.getIsotope().isEmpty();
			Boolean baseIsotope = feature.getIsotope().indexOf('+') == -1;
			XSSFCellStyle isotopeStyle = haveIsotope ? (baseIsotope ? styleList.get(BinnerConstants.STYLE_LIGHT_BLUE) : 
					styleList.get(BinnerConstants.STYLE_LIGHTER_BLUE)) : styleList.get(BinnerConstants.STYLE_BORING);
			XSSFCellStyle isotopeStyleLeft = haveIsotope ? (baseIsotope ? styleList.get(BinnerConstants.STYLE_LIGHT_BLUE_LEFT) : 
				styleList.get(BinnerConstants.STYLE_LIGHTER_BLUE)) : styleList.get(BinnerConstants.STYLE_BORING);
			PoiUtils.createRowEntry(rowCt, i++, sheet, feature.getIsotope(), isotopeStyle);
			StringBuilder isotopeGroupMembers = new StringBuilder();
			if (haveIsotope && baseIsotope) {
				String isotopePrefix = feature.getIsotope().substring(0, feature.getIsotope().indexOf("]"));
				isotopeGroupMembers.append(feature.getIsotopeGroupMembers().size() + ": ");
				for (int iso = 0; iso < feature.getIsotopeGroupMembers().size(); iso++) {		
					isotopeGroupMembers.append(isotopePrefix + " + " + feature.getIsotopeGroupMembers().get(iso) + "]");
					if (iso < feature.getIsotopeGroupMembers().size() - 1) {
						isotopeGroupMembers.append(", ");
					}
				}
			}
			PoiUtils.createRowEntry(rowCt, i++, sheet, isotopeGroupMembers.toString(), isotopeStyleLeft);
			
			boolean isBlank = feature.getAdductOrNL().isEmpty();
			boolean isDegenerate = feature.getAdductOrNL().contains(BinnerConstants.SINGLETON_ANNOTATION);
			Boolean haveAnnotation = !(isBlank || isDegenerate);
			Boolean putativeMolecularIon = feature.isPutativeMolecularIon();
		
			XSSFCellStyle annotationStyle = haveAnnotation ? (putativeMolecularIon ? styleList.get(BinnerConstants.STYLE_LIGHT_GREEN) : 
					styleList.get(BinnerConstants.STYLE_YELLOW)) : styleList.get(BinnerConstants.STYLE_BORING);
		
			XSSFCellStyle annotationStyleLeft = haveAnnotation ? (putativeMolecularIon ? styleList.get(BinnerConstants.STYLE_LIGHT_GREEN_LEFT) : 
				styleList.get(BinnerConstants.STYLE_YELLOW)) : styleList.get(BinnerConstants.STYLE_BORING);
		
			PoiUtils.createRowEntry(rowCt, i++, sheet, feature.getAdductOrNL(), annotationStyle);
			StringBuilder annotationGroupMembers = new StringBuilder();
			if (putativeMolecularIon && feature.getAnnotationGroupMembers().size() > 0) {
				annotationGroupMembers.append(feature.getAnnotationGroupMembers().size() + ": ");
				for (String annotGroupMember : feature.getAnnotationGroupMembers()) {
					annotationGroupMembers.append(annotGroupMember + ", ");
				}
				annotationGroupMembers.deleteCharAt(annotationGroupMembers.lastIndexOf(","));
			}
			PoiUtils.createRowEntry(rowCt, i++, sheet, annotationGroupMembers.toString(), annotationStyleLeft);	
			PoiUtils.createRowEntry(rowCt, i++, sheet, feature.getDerivation(), annotationStyle);
			
			createAppropriateRowEntry(rowCt, i++, sheet, (feature.getMassError() == null ? "" : String.valueOf(feature.getMassError())),
					 styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC),
					 	styleList.get(BinnerConstants.STYLE_BORING));				
			createAppropriateRowEntry(rowCt, i++, sheet, feature.getMolecularIonNumber(),
					 styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC),
					 	styleList.get(BinnerConstants.STYLE_BORING));
			PoiUtils.createRowEntry(rowCt, i++, sheet, feature.getChargeCarrier(), styleList.get(BinnerConstants.STYLE_BORING));
			PoiUtils.createRowEntry(rowCt, i++, sheet, feature.getNeutralAnnotation(), styleList.get(BinnerConstants.STYLE_BORING));
			PoiUtils.createRowEntry(rowCt, i++, sheet, StringUtils.isEmptyOrNull(feature.getReferenceMassString()) ? "" : feature.getReferenceMassString(), styleList.get(BinnerConstants.STYLE_BORING));	
			
			createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getBinIndex() + 1),
					styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC),
						styleList.get(BinnerConstants.STYLE_BORING));
			
			Boolean diagnosticHighlighting = Arrays.asList(BinnerConstants.HIGHLIGHTED_REBIN_OUTPUTS).contains(originalIdx); 
			
			Integer clusterClass = feature.getOldCluster();
			clusterClass %= mdMap.keySet().size();
				
			if (diagnosticHighlighting)
				createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getOldCluster()), 
						mdMap.get(clusterClass).get(STYLE_INTEGER), mdMap.get(clusterClass).get(STYLE_INTEGER),
						mdMap.get(clusterClass).get(STYLE_INTEGER));
			else
				createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getOldCluster()), 
						styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC),
						styleList.get(BinnerConstants.STYLE_BORING));
				
			clusterClass = feature.getNewCluster();
			clusterClass %= mdMap.keySet().size();
			
			if (diagnosticHighlighting)
				createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getNewCluster()), mdMap.get(clusterClass).get(STYLE_INTEGER), mdMap.get(clusterClass).get(STYLE_INTEGER),
				 mdMap.get(clusterClass).get(STYLE_INTEGER));
			else
				createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getNewCluster()),
				styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC),
				styleList.get(BinnerConstants.STYLE_BORING));
			
			
			clusterClass = feature.getNewNewCluster();
			if (clusterClass == null || clusterClass < 0)
				clusterClass = 1;
			clusterClass %= mdMap.keySet().size();
			
			
			if (!onlyRebin && diagnosticHighlighting)
				createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getNewNewCluster()), 
				 mdMap.get(clusterClass).get(STYLE_INTEGER), mdMap.get(clusterClass).get(STYLE_INTEGER),
				 mdMap.get(clusterClass).get(STYLE_INTEGER));
			else
				createAppropriateRowEntry(rowCt, i++, sheet, (onlyRebin ? "-" : String.valueOf(feature.getNewNewCluster())),
				 styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC),
				 styleList.get(BinnerConstants.STYLE_BORING));
			
			/*
			//DIAGNOSTIC OUTPUT 
			Feature lastFeature = null;
			if (lastFeatureIdx != null)
				{
				lastFeature = analysisData.getNonMissingFeaturesInOriginalOrder().get(lastFeatureIdx);
			
				Double rtDiff = Math.abs(feature.getRT() - lastFeature.getRT());
				//Double rtRatio = rtDiff < 0.0 ? 0.0 : rtDiff;    
				Double rtRatio = rtDiff / 0.7;
			
				int nRTColors = map.keySet().size();
				Integer scaledRTValue = new Double((1.0 - rtRatio) * nRTColors).intValue();
				if (scaledRTValue < 0) scaledRTValue = 0;
				if (scaledRTValue >= nRTColors) scaledRTValue = nRTColors - 1;
			
				PoiUtils.createNumericRowEntry(rowCt, i++, sheet, String.valueOf(rtDiff),  
					map.get(scaledRTValue).get(STYLE_NUMERIC));
				}
			else
				createAppropriateRowEntry(rowCt, i++, sheet, "-" ,
						 styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC),
						 styleList.get(BinnerConstants.STYLE_BORING));
			
			// DIAGNOSTIC OUTPUT  
			*/

		}
			
		private double [] getCalculatedValueList(Integer outputIndex, Feature feature)
			{
			if (Arrays.asList(BinnerConstants.CORR_OUTPUTS).contains(outputIndex))
				return(BinnerIndexUtils.getFeatureCorrelationsFromFeature(feature));
			return(BinnerIndexUtils.getFeatureMassDiffsFromFeature(feature));
			}
	
		private double [] getIntensityList(Integer outputIndex, Feature feature)
			{
			if (Arrays.asList(BinnerConstants.ADJ_OUTPUTS).contains(outputIndex))
				return(feature.getAdjustedIntensityList());
			return feature.getUnadjustedIntensityListWithOutliers();
			}
		
		public String getFileName() 
			{
			return fileName;
			}
			
		public void setFileName(String fileName) 
			{
			this.fileName = fileName;
			}
		
		public List<BinnerOutput> getOutputList() 
			{
			return outputList;
			}
			
		public void setOutputList(List<BinnerOutput> outputList)
			{
			this.outputList = outputList;
			}
		
		public Palette getColorPalette()
			{
			return colorPalette;
			}
		
		public void setColorPalette(Palette palette)
			{
			this.colorPalette = palette;
			}
		
		public int getnBinMassDiffAnnotations()
			{
			return nBinMassDiffAnnotations;
			}
		
		public void setnBinMassDiffAnnotations(int nBinMassDiffAnnotations)
			{
			this.nBinMassDiffAnnotations = nBinMassDiffAnnotations;
			}
		
		public List<XSSFCellStyle> getStyleList() 
			{
			return styleList;
			}
		
		public void setStyleList(List<XSSFCellStyle> styleList) 
			{
			this.styleList = styleList;
			}
		}
		
		
		
