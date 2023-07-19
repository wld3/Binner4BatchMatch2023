////////////////////////////////////////////////////
//FancyBinnerOutputContainer.java
//Created Jan 24, 2017
////////////////////////////////////////////////////
package edu.umich.wld.sheetwriters;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

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
import edu.umich.wld.Feature;
import edu.umich.wld.MassDiffHistogram;
import edu.umich.wld.RawIntensities;
import edu.umich.wld.StringUtils;
import edu.umich.wld.SummaryInfo;
import edu.umich.wld.TextFile;


public class FancyBinnerOutputContainer extends BinnerSpreadSheetWriter implements Serializable
	{
	private static final long serialVersionUID = 4283630477985403483L;
	
	private int nBinMassDiffAnnotations; 
	
	private String fileName;
	private String batchLabel;
	private List<BinnerOutput> outputList;
	private Palette colorPalette = null, mdColorPalette = null, histColorPalette = null;
	private int startCol = -1, rowCt = 0, rowIndex = 0;
	private SXSSFWorkbook workBook = null;
	private List<XSSFCellStyle> styleList = new ArrayList<XSSFCellStyle>();
	private Boolean skipBinwiseOutput; 
	private Boolean showGroupLabels;
	
	public FancyBinnerOutputContainer(List<BinnerOutput> outputList, String fileName, Boolean showGroupLabels, String batchLabel) 
		{ 
		this(outputList, fileName, null, null, null, false, showGroupLabels, batchLabel);
		}	
	
	public FancyBinnerOutputContainer(List<BinnerOutput> outputList, String fileName, Palette palette, 
		Palette mdPalette, Palette histogramPalette, Boolean skipBinwiseOutput, Boolean showGroupLabels, String batchLabel)
			{
			this.outputList = outputList;
			this.fileName = fileName;
			this.colorPalette = palette;
			this.mdColorPalette = mdPalette;
			this.histColorPalette = histogramPalette;
			this.batchLabel = batchLabel;
			this.skipBinwiseOutput = skipBinwiseOutput;
			this.showGroupLabels = showGroupLabels;
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
			styleList.add(BinnerConstants.STYLE_HEADER_WRAPPED, grabStyleBoringNewHeader(workBook, true, false));
			styleList.add(BinnerConstants.STYLE_NUMERIC_SHORTER, grabStyleNumericShorter(workBook));
			styleList.add(BinnerConstants.STYLE_BORING_RIGHT, grabStyleBoringRight(workBook));
			styleList.add(BinnerConstants.STYLE_NUMERIC_LAVENDER, grabStyleLavender(workBook, false));
			styleList.add(BinnerConstants.STYLE_LIGHT_BLUE_LEFT, grabStyleLightBlueLeft(workBook));
			styleList.add(BinnerConstants.STYLE_LIGHT_GREEN_LEFT, grabStyleLightGreenLeft(workBook));
			styleList.add(BinnerConstants.STYLE_INTEGER_CENTERED, grabStyleIntegerCentered(workBook));
			styleList.add(BinnerConstants.STYLE_LIGHTER_BLUE, grabStyleLighterBlue(workBook));
			styleList.add(BinnerConstants.STYLE_BLANK_BORING, grabStyleBlankBoring(workBook));
			styleList.add(BinnerConstants.STYLE_BLANK_BORING_TOP, grabStyleBlankBoringTop(workBook));
			styleList.add(BinnerConstants.STYLE_BLANK_BORING_BOTTOM, grabStyleBlankBoringBottom(workBook));	
			styleList.add(BinnerConstants.STYLE_BORING_BOTTOM_HEADER, grabStyleBlankBoringBottomHeader(workBook));	
			styleList.add(BinnerConstants.STYLE_HEADER_WRAPPED_RED, grabStyleBoringNewHeader(workBook, true, true));
				
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
			List<XSSFCellStyle>> map, Map<Integer, List<XSSFCellStyle>> mdMap, Boolean onlyRebin)  throws Exception {
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
		
		// hack for parsable text-like output -- wld 10/20/19
		Boolean fTextFormat = true;
		createHeader(output, sheet, fTextFormat);
		for (BinnerGroup group : output.getGroups())
			createGroup(analysisData, outputIndex, group, sheet, map, mdMap, onlyRebin, 0, //nFormattedCols,
					fTextFormat);
		tackOnMissingFeatures(analysisData, outputIndex, sheet, map, mdMap, onlyRebin);		
	
		// hack for actual text output -- wld 07/11/23
		String csvFileName = fileName.replace(".xlsx", "_no_intensities.csv");
		BufferedWriter csvOutputStream = null;
		try {
			csvOutputStream = CSVWriterUtils.createCSVFile(csvFileName);
			createCSVHeader(analysisData, output, csvOutputStream, false);
			for (BinnerGroup group : output.getGroups())
				createCSVGroup(analysisData, outputIndex, group, onlyRebin, csvOutputStream, false);
			tackOnMissingFeaturesForCSV(analysisData, onlyRebin, csvOutputStream, false);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			csvOutputStream.close();
		}
		
		RawIntensities allRawIntensities = analysisData.getAllRawIntensities();
		storeSampleNames(allRawIntensities);
		storeIntensities(allRawIntensities);	
		
		csvFileName = fileName.replace(".xlsx", "_with_intensities.csv");
		try {
			csvOutputStream = CSVWriterUtils.createCSVFile(csvFileName);
			createCSVHeader(analysisData, output, csvOutputStream, true);
			for (BinnerGroup group : output.getGroups())
				createCSVGroup(analysisData, outputIndex, group, onlyRebin, csvOutputStream, true);
			tackOnMissingFeaturesForCSV(analysisData, onlyRebin, csvOutputStream, true);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			csvOutputStream.close();
		}
	}
		
	public void storeSampleNames(RawIntensities allRawIntensities) {
		TextFile rawData = allRawIntensities.getRawDataFile();
		List<String> sampleNames = new ArrayList<String>();
		for (int iCol = 0; iCol <= rawData.getEndColIndex(0); iCol++) {
			if ("Charge".equalsIgnoreCase(rawData.getString(0, iCol))) {
				allRawIntensities.setFirstSampleIndex(iCol + 1);
				break;
			}
		}
		if (allRawIntensities.getFirstSampleIndex() == -1) {
			JOptionPane.showMessageDialog(null, "Error: Charge column not found in experiment file");
            return;
		}
		
		for (int iCol = allRawIntensities.getFirstSampleIndex(); iCol <= rawData.getEndColIndex(0); iCol++) {
			sampleNames.add(rawData.getString(0, iCol));
		}
		
		allRawIntensities.setSampleNames(sampleNames);
	}
	
	public void storeIntensities(RawIntensities allRawIntensities) {
		TextFile rawData = allRawIntensities.getRawDataFile();
		for (int iRow = 1; iRow <= rawData.getEndRowIndex(); iRow++) {
			String key = rawData.getString(iRow, allRawIntensities.getFeatureIndex()) + "-" + 
					String.valueOf(Math.round(rawData.getDouble(iRow, allRawIntensities.getMassIndex()) * 10000));
			List<String> featureIntensities = new ArrayList<String>();
			for (int iCol = allRawIntensities.getFirstSampleIndex(); iCol <= rawData.getEndColIndex(iRow); iCol++) {
				String str = rawData.getString(iRow, iCol);
				featureIntensities.add(str == null ? "" : str);
			}
			allRawIntensities.getIntensityMap().put(key, featureIntensities);
		}
	}
	
	public void tackOnMissingFeatures(AnalysisData analysisData, Integer outputIndex, Sheet sheet,
		 Map<Integer, List<XSSFCellStyle>> map, Map<Integer, List<XSSFCellStyle>> mdMap, Boolean onlyRebin) throws Exception
					{
					BinnerOutput output = outputList.get(outputIndex);
					Integer originalIdx = output.getOriginalSheetIdx();
					for (int rowFeatureIndex = 0; rowFeatureIndex < analysisData.getMissingFeaturesInOriginalOrder().size(); rowFeatureIndex++)
						{
						Row row = sheet.createRow(rowCt);
						row.setHeightInPoints((short) 16); 
						Feature feature = analysisData.getMissingFeaturesInOriginalOrder().get(rowFeatureIndex);
						
						createStatisticsBlock(feature, sheet, mdMap, originalIdx, onlyRebin, true);
						
						rowCt++;
					}
			}
		
		public void createHeader(BinnerOutput output, Sheet sheet, Boolean fTextFormat) throws Exception
			{
			rowCt = 0;
			rowIndex = 0;
			PoiUtils.createBlankCleanRow(rowCt, sheet, 200, styleList.get(BinnerConstants.STYLE_BORING));
			
			int col = 0;
			for (col = 0; col < output.getHeaders().size(); col++)
				{
				String header = output.getHeaders().get(col);
				Boolean left = header.equals(BinnerConstants.OUTPUT_CORRELATION_LABEL) || header.equals(BinnerConstants.OUTPUT_MASS_DIFFS_LABEL);
			
				if (fTextFormat) {
					if (!left)
						PoiUtils.createRowEntry(rowCt, col, sheet, header, styleList.get(BinnerConstants.STYLE_BORING), 30);
				} else {
					if (col > output.getFirstBlankCol() && !left)
						PoiUtils.createRowEntry(rowCt, col, sheet, header, styleList.get(BinnerConstants.STYLE_BORING), 30);
					else	
						PoiUtils.createRowEntry(rowCt, col, sheet, header, left ? styleList.get(BinnerConstants.STYLE_BORING) :
							styleList.get(BinnerConstants.STYLE_BORING), 30);
					}
				}
			
			rowCt++;
			}
		
		public void createGroup(AnalysisData analysisData, Integer outputIndex, BinnerGroup group, Sheet sheet, 
		Map<Integer, List<XSSFCellStyle>> map, Map<Integer, List<XSSFCellStyle>> mdMap, Boolean onlyRebin,
		int nFormattedCols, Boolean fTextFormat) throws Exception
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
			//BIN
			int firstHeaderCol = -1;
			firstHeaderCol = output.getHeaders().indexOf(BinnerConstants.OUTPUT_CORRELATION_LABEL);
			if (firstHeaderCol == -1)
				firstHeaderCol = output.getHeaders().indexOf(BinnerConstants.OUTPUT_MASS_DIFFS_LABEL);
				
			startCol = output.getDataStartCol();
			
			if (!fTextFormat)
				rowIndex++;
			
			createAppropriateRowEntry(rowCt, 0, sheet, String.valueOf(rowIndex),		
					 styleList.get(BinnerConstants.STYLE_BORING), styleList.get(BinnerConstants.STYLE_BORING),
					 	styleList.get(BinnerConstants.STYLE_BORING));
			//rowIndex++;  HERE .....
	
			if (startCol != -1 && !fTextFormat)
				createGroupHeader(analysisData, sheet, featureIndices, startCol, output.getFirstBlankCol(), group.getTitle(), firstHeaderCol, nFormattedCols);
			
			if (!fTextFormat)
				rowCt++;
			
			createGroupBody(analysisData, outputIndex, sheet, featureIndices, map, mdMap, onlyRebin, nFormattedCols,
					fTextFormat);
			}
			
		
		public void createGroupHeader(AnalysisData analysisData, Sheet sheet, List<Integer> featureIndices, 
		 int startCol, int firstBlankCol, String groupTitle, int firstHeaderCol, int nFormattedCols) throws Exception
			{
			if (showGroupLabels)
				PoiUtils.createRowEntry(rowCt, 1, sheet, groupTitle, styleList.get(BinnerConstants.STYLE_BORING));
			
			for (int i = 2; i < startCol - 1; i++)
				PoiUtils.createRowEntry(rowCt, i, sheet, "", styleList.get(BinnerConstants.STYLE_BORING));
			if (rowCt < 4 && showGroupLabels) 
				PoiUtils.createRowEntry(rowCt, firstBlankCol + 1, sheet, "USER COLUMNS:", styleList.get(BinnerConstants.STYLE_BORING));
			
			PoiUtils.createRowEntry(rowCt, firstBlankCol, sheet, "", styleList.get(BinnerConstants.STYLE_BORING));
			PoiUtils.createRowEntry(rowCt, startCol - 1, sheet, "", styleList.get(BinnerConstants.STYLE_BORING));
			
			int j = startCol;
			if (firstHeaderCol > 1) {
				for (int i : featureIndices)
					PoiUtils.createRowEntry(rowCt, j++, sheet, analysisData.getNonMissingFeaturesInOriginalOrder().get(i).getName(), 
								styleList.get(BinnerConstants.STYLE_BORING));
				
				for (int i = startCol + featureIndices.size(); i < nFormattedCols; i++) 
					PoiUtils.createRowEntry(rowCt, j++, sheet, "", 
							styleList.get(BinnerConstants.STYLE_BORING));
			}
			else {
				int nSamples = startCol + analysisData.getNonMissingFeaturesInOriginalOrder().get(0).getUnadjustedIntensityListWithOutliers().length;
			
				while (j < nSamples)
					PoiUtils.createRowEntry(rowCt, j++, sheet, "", 
								styleList.get(BinnerConstants.STYLE_BORING));
			}
		}
			
			
		public void createGroupBody(AnalysisData analysisData, Integer outputIndex, Sheet sheet, List<Integer> featureIndices, 
		 Map<Integer, List<XSSFCellStyle>> map, Map<Integer, List<XSSFCellStyle>> mdMap, Boolean onlyRebin, int nFormattedCols,
		 Boolean fTextFormat) throws Exception
			{
			BinnerOutput output = outputList.get(outputIndex);
			Integer originalIdx = output.getOriginalSheetIdx();
            int i = 0;
			for (int listIndex = 0; listIndex < featureIndices.size(); listIndex++) {
				Row row = sheet.createRow(rowCt);
				row.setHeightInPoints((short) 16); 
				int rowFeatureIndex = featureIndices.get(listIndex);
				Feature feature = analysisData.getNonMissingFeaturesInOriginalOrder().get(rowFeatureIndex);		
				createStatisticsBlock(feature, sheet, mdMap, originalIdx, onlyRebin, false);
				rowCt++;
			}
			
			if (fTextFormat)
				return;
			
			PoiUtils.createBlankCleanRow(rowCt, sheet, nFormattedCols, styleList.get(BinnerConstants.STYLE_BORING));
	
		
			createAppropriateRowEntry(rowCt, 0, sheet, String.valueOf(++rowIndex),
					styleList.get(BinnerConstants.STYLE_BORING), styleList.get(BinnerConstants.STYLE_BORING),
				 		styleList.get(BinnerConstants.STYLE_BORING));
		
			createAppropriateRowEntry(rowCt, output.getFirstBlankCol(), sheet, "",
					styleList.get(BinnerConstants.STYLE_BORING), styleList.get(BinnerConstants.STYLE_BORING),
					 	styleList.get(BinnerConstants.STYLE_BORING));
		
			createAppropriateRowEntry(rowCt, output.getDataStartCol() - 1, sheet, "",
					styleList.get(BinnerConstants.STYLE_BORING), styleList.get(BinnerConstants.STYLE_BORING),
					 	styleList.get(BinnerConstants.STYLE_BORING));
	
			while (i < nFormattedCols)
				createAppropriateRowEntry(rowCt, i++, sheet, "",
						styleList.get(BinnerConstants.STYLE_BORING), styleList.get(BinnerConstants.STYLE_BORING),
						 	styleList.get(BinnerConstants.STYLE_BORING));
		
			rowCt++;
			PoiUtils.createBlankCleanRow(rowCt, sheet, nFormattedCols, styleList.get(BinnerConstants.STYLE_BORING));
			}
		
		private void createStatisticsBlock(Feature feature, Sheet sheet, Map<Integer, List<XSSFCellStyle>> mdMap, 
			Integer originalIdx, Boolean onlyRebin, Boolean fAbbreviated)
			{
			int i = 0;
			String val = "";
			
		
			PoiUtils.createRowEntry(rowCt, i++, sheet, batchLabel, styleList.get(BinnerConstants.STYLE_BORING));			
			
			createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(++rowIndex),
					 styleList.get(BinnerConstants.STYLE_BORING), styleList.get(BinnerConstants.STYLE_BORING),
					 	styleList.get(BinnerConstants.STYLE_BORING));
			
			
			PoiUtils.createRowEntry(rowCt, i++, sheet, feature.getName(), styleList.get(BinnerConstants.STYLE_BORING));			
			createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getMass()),
					styleList.get(BinnerConstants.STYLE_BORING), styleList.get(BinnerConstants.STYLE_BORING),
						styleList.get(BinnerConstants.STYLE_BORING));			
			createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getRT()),
					styleList.get(BinnerConstants.STYLE_BORING), styleList.get(BinnerConstants.STYLE_BORING),
						styleList.get(BinnerConstants.STYLE_BORING));
			val = (feature.getMedianIntensity() == null ? "miss > tol" : String.valueOf(Math.round(feature.getMedianIntensity())));
			createAppropriateRowEntry(rowCt, i++, sheet, val,
					styleList.get(BinnerConstants.STYLE_BORING), styleList.get(BinnerConstants.STYLE_BORING),
						styleList.get(BinnerConstants.STYLE_BORING));
			val = (feature.getRmDefect() == null ? "" : String.valueOf(Math.round(feature.getRmDefect())));
			createAppropriateRowEntry(rowCt, i++, sheet, val,
					styleList.get(BinnerConstants.STYLE_BORING), styleList.get(BinnerConstants.STYLE_BORING),
						styleList.get(BinnerConstants.STYLE_BORING));
			
			if (fAbbreviated) {
				return;
			}
			
			Boolean haveIsotope = !feature.getIsotope().isEmpty();
			Boolean baseIsotope = feature.getIsotope().indexOf('+') == -1;
			XSSFCellStyle isotopeStyle = haveIsotope ? (baseIsotope ? styleList.get(BinnerConstants.STYLE_BORING) : 
					styleList.get(BinnerConstants.STYLE_BORING)) : styleList.get(BinnerConstants.STYLE_BORING);
			XSSFCellStyle isotopeStyleLeft = haveIsotope ? (baseIsotope ? styleList.get(BinnerConstants.STYLE_BORING) : 
				styleList.get(BinnerConstants.STYLE_BORING)) : styleList.get(BinnerConstants.STYLE_BORING);
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
		
			XSSFCellStyle annotationStyle = haveAnnotation ? (putativeMolecularIon ? styleList.get(BinnerConstants.STYLE_BORING) : 
					styleList.get(BinnerConstants.STYLE_BORING)) : styleList.get(BinnerConstants.STYLE_BORING);
		
			XSSFCellStyle annotationStyleLeft = haveAnnotation ? (putativeMolecularIon ? styleList.get(BinnerConstants.STYLE_BORING) : 
				styleList.get(BinnerConstants.STYLE_BORING)) : styleList.get(BinnerConstants.STYLE_BORING);
		
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
					 styleList.get(BinnerConstants.STYLE_BORING), styleList.get(BinnerConstants.STYLE_BORING),
					 	styleList.get(BinnerConstants.STYLE_BORING));				
			createAppropriateRowEntry(rowCt, i++, sheet, feature.getMolecularIonNumber(),
					 styleList.get(BinnerConstants.STYLE_BORING), styleList.get(BinnerConstants.STYLE_BORING),
					 	styleList.get(BinnerConstants.STYLE_BORING));
			PoiUtils.createRowEntry(rowCt, i++, sheet, feature.getChargeCarrier(), styleList.get(BinnerConstants.STYLE_BORING));
			PoiUtils.createRowEntry(rowCt, i++, sheet, feature.getNeutralAnnotation(), styleList.get(BinnerConstants.STYLE_BORING));
			PoiUtils.createRowEntry(rowCt, i++, sheet, StringUtils.isEmptyOrNull(feature.getReferenceMassString()) ? "" : feature.getReferenceMassString(), styleList.get(BinnerConstants.STYLE_BORING));	
			
			createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getBinIndex() + 1),
					styleList.get(BinnerConstants.STYLE_BORING), styleList.get(BinnerConstants.STYLE_BORING),
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
						styleList.get(BinnerConstants.STYLE_BORING), styleList.get(BinnerConstants.STYLE_BORING),
						styleList.get(BinnerConstants.STYLE_BORING));
				
			// hide whichever cluster entry isn't being used for rebin
			sheet.setColumnWidth(onlyRebin ? i + 1 : i, 256);

			clusterClass = feature.getNewCluster();
			clusterClass %= mdMap.keySet().size();
			
			if (diagnosticHighlighting)
				createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getNewCluster()), mdMap.get(clusterClass).get(STYLE_INTEGER), mdMap.get(clusterClass).get(STYLE_INTEGER),
				 mdMap.get(clusterClass).get(STYLE_INTEGER));
			else
				createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getNewCluster()),
				styleList.get(BinnerConstants.STYLE_BORING), styleList.get(BinnerConstants.STYLE_BORING),
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
				 styleList.get(BinnerConstants.STYLE_BORING), styleList.get(BinnerConstants.STYLE_BORING),
				 styleList.get(BinnerConstants.STYLE_BORING));
		}
		
		public void createCSVHeader(AnalysisData analysisData, BinnerOutput output, BufferedWriter csvOutputStream, 
				Boolean includeIntensities) throws IOException {
			rowCt = 0;			
			for (int col = 0; col < output.getHeaders().size(); col++) {
				String header = output.getHeaders().get(col);				
				if (header.equals(BinnerConstants.OUTPUT_CORRELATION_LABEL) || header.equals(BinnerConstants.OUTPUT_MASS_DIFFS_LABEL)) {
					if (includeIntensities) {
						RawIntensities allRawIntensities = analysisData.getAllRawIntensities();
						for (int listIndex = 0; listIndex < allRawIntensities.getSampleNames().size(); listIndex++)
							CSVWriterUtils.writeStringToCSVFile(allRawIntensities.getSampleNames().get(listIndex), csvOutputStream, 
									listIndex < allRawIntensities.getSampleNames().size() - 1);
					} else
						CSVWriterUtils.writeStringToCSVFile("", csvOutputStream, false);
					break;
				}
			
				CSVWriterUtils.writeStringToCSVFile(header, csvOutputStream, true);
			}						
			rowCt++;
		}
		
		public void createCSVGroup(AnalysisData analysisData, Integer outputIndex, BinnerGroup group,
				Boolean onlyRebin, BufferedWriter csvOutputStream, Boolean includeIntensities) throws IOException {
			List<Integer> featureIndices = new ArrayList<Integer>();
			BinnerOutput output = outputList.get(outputIndex);
			Integer originalIdx = output.getOriginalSheetIdx();
			
			if (Arrays.asList(BinnerConstants.RT_SORT_OUTPUTS).contains(originalIdx))
				featureIndices = getFeatureIndexListSortedByFactor(analysisData, group, BinnerConstants.SORT_TYPE_RT);
			else if (Arrays.asList(BinnerConstants.MASS_SORT_OUTPUTS).contains(originalIdx))
				featureIndices = getFeatureIndexListSortedByFactor(analysisData, group, BinnerConstants.SORT_TYPE_MASS);
			else
				featureIndices = group.getFeatureIndexList();
								
			createCSVGroupBody(analysisData, featureIndices, onlyRebin, csvOutputStream, includeIntensities);
		}
		
		public void createCSVGroupBody(AnalysisData analysisData, List<Integer> featureIndices, 
				Boolean onlyRebin, BufferedWriter csvOutputStream, Boolean includeIntensities) throws IOException {
			for (int listIndex = 0; listIndex < featureIndices.size(); listIndex++) {
				int featureIndex = featureIndices.get(listIndex);
				Feature feature = analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndex);			
				createCSVStatisticsBlock(analysisData, feature, onlyRebin, false, csvOutputStream, includeIntensities);
				rowCt++;
			}
		}
		
		private void createCSVStatisticsBlock(AnalysisData analysisData, Feature feature, Boolean onlyRebin, Boolean fAbbreviated, 
				BufferedWriter csvOutputStream, Boolean includeIntensities) throws IOException {
			CSVWriterUtils.writeStringToCSVFile(batchLabel, csvOutputStream, true);
			CSVWriterUtils.writeStringToCSVFile(String.valueOf(rowCt), csvOutputStream, true);
			CSVWriterUtils.writeStringToCSVFile(feature.getName(), csvOutputStream, true);
			CSVWriterUtils.writeStringToCSVFile(String.valueOf(feature.getMass()), csvOutputStream, true);
			CSVWriterUtils.writeStringToCSVFile(String.valueOf(feature.getRT()), csvOutputStream, true);
			String val = (feature.getMedianIntensity() == null ? "miss > tol" : String.valueOf(Math.round(feature.getMedianIntensity())));
			CSVWriterUtils.writeStringToCSVFile(val, csvOutputStream, true);
			val = (feature.getRmDefect() == null ? "" : String.valueOf(Math.round(feature.getRmDefect())));
			CSVWriterUtils.writeStringToCSVFile(val, csvOutputStream, true);
			
			if (fAbbreviated) {
				if (includeIntensities) {
					for (int iBlank = 0; iBlank < BinnerConstants.N_OUTPUT_COLUMNS_SKIPPED_FOR_MISSING_FEATURES; iBlank++)
						CSVWriterUtils.writeStringToCSVFile("", csvOutputStream, true);
					writeIntensitiesToCSVFile(analysisData, feature, csvOutputStream);
				} else
					CSVWriterUtils.writeStringToCSVFile("", csvOutputStream, false);
				return;
			}
			
			CSVWriterUtils.writeStringToCSVFile(feature.getIsotope(), csvOutputStream, true);
			StringBuilder isotopeGroupMembers = new StringBuilder();
			Boolean haveIsotope = !feature.getIsotope().isEmpty();
			Boolean baseIsotope = feature.getIsotope().indexOf('+') == -1;
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
			CSVWriterUtils.writeStringToCSVFile(isotopeGroupMembers.toString(), csvOutputStream, true);
					
			CSVWriterUtils.writeStringToCSVFile(feature.getAdductOrNL(), csvOutputStream, true);
			StringBuilder annotationGroupMembers = new StringBuilder();
			if (feature.isPutativeMolecularIon() && feature.getAnnotationGroupMembers().size() > 0) {
				annotationGroupMembers.append(feature.getAnnotationGroupMembers().size() + ": ");
				for (String annotGroupMember : feature.getAnnotationGroupMembers()) {
					annotationGroupMembers.append(annotGroupMember + ", ");
				}
				annotationGroupMembers.deleteCharAt(annotationGroupMembers.lastIndexOf(","));
			}
			CSVWriterUtils.writeStringToCSVFile(annotationGroupMembers.toString(), csvOutputStream, true);
			CSVWriterUtils.writeStringToCSVFile(feature.getDerivation(), csvOutputStream, true);
			val = (feature.getMassError() == null ? "" : String.format("%.6f", feature.getMassError()));
			CSVWriterUtils.writeStringToCSVFile(val, csvOutputStream, true);
			CSVWriterUtils.writeStringToCSVFile(feature.getMolecularIonNumber(), csvOutputStream, true);
			CSVWriterUtils.writeStringToCSVFile(feature.getChargeCarrier(), csvOutputStream, true);
			CSVWriterUtils.writeStringToCSVFile(feature.getNeutralAnnotation(), csvOutputStream, true);
			CSVWriterUtils.writeStringToCSVFile(StringUtils.isEmptyOrNull(feature.getReferenceMassString()) ? "" : feature.getReferenceMassString(), csvOutputStream, true);
			
			CSVWriterUtils.writeStringToCSVFile(String.valueOf(feature.getBinIndex() + 1), csvOutputStream, true);
			CSVWriterUtils.writeStringToCSVFile(String.valueOf(feature.getOldCluster()), csvOutputStream, true);
			CSVWriterUtils.writeStringToCSVFile(String.valueOf(feature.getNewCluster()), csvOutputStream, true);
			CSVWriterUtils.writeStringToCSVFile(onlyRebin ? "-" : String.valueOf(feature.getNewNewCluster()), csvOutputStream, includeIntensities);
			if (includeIntensities)
				writeIntensitiesToCSVFile(analysisData, feature, csvOutputStream);
		}
		
		public void tackOnMissingFeaturesForCSV(AnalysisData analysisData, Boolean onlyRebin, BufferedWriter csvOutputStream,
				Boolean includeIntensities) throws IOException {						
			for (int rowFeatureIndex = 0; rowFeatureIndex < analysisData.getMissingFeaturesInOriginalOrder().size(); rowFeatureIndex++) {
				Feature feature = analysisData.getMissingFeaturesInOriginalOrder().get(rowFeatureIndex);
				createCSVStatisticsBlock(analysisData, feature, onlyRebin, true, csvOutputStream, includeIntensities);
				rowCt++;
			}
		}
		
		public void writeIntensitiesToCSVFile(AnalysisData analysisData, Feature feature, BufferedWriter csvOutputStream) throws IOException {
			CSVWriterUtils.writeStringToCSVFile("", csvOutputStream, true);
			CSVWriterUtils.writeStringToCSVFile("", csvOutputStream, true);
			RawIntensities allRawIntensities = analysisData.getAllRawIntensities();
			String key = feature.getName() + "-" + String.valueOf(Math.round(feature.getMass() * 10000.0));
			if (!allRawIntensities.getIntensityMap().containsKey(key)) {
				JOptionPane.showMessageDialog(null, "Error: No intensities found for key " + key);
	            return;
			}
			List<String> featureIntensities = allRawIntensities.getIntensityMap().get(key);
			for (int listIndex = 0; listIndex < featureIntensities.size(); listIndex++) {
				CSVWriterUtils.writeStringToCSVFile(featureIntensities.get(listIndex), csvOutputStream, listIndex < featureIntensities.size() - 1);	
			}
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
		
		
		
