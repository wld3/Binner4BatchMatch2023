////////////////////////////////////////////////////
// BinnerRefMassSheetWriter.java
// Written by Jan Wigginton, March, 2018
////////////////////////////////////////////////////
package edu.umich.wld.sheetwriters;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import edu.umich.wld.AnalysisData;
import edu.umich.wld.BinnerConstants;
import edu.umich.wld.BinnerGroup;
import edu.umich.wld.Feature;
import edu.umich.wld.StringUtils;


public class BinnerRefMassSheetWriter extends BinnerSpreadSheetWriter implements Serializable {
	private static final long serialVersionUID = -118116585712037767L;

	private Workbook workBook;
	private int rowCt;
	private int rowIndex;

	public BinnerRefMassSheetWriter(Workbook workBook) {
		super();
		this.workBook = workBook;
	}

	public void  createRefMassTab(AnalysisData analysisData, BinnerOutput output,  Boolean usePutative, List<XSSFCellStyle> styleList, 
			Boolean onlyRebin) throws Exception {
		
		Sheet sheet = createEmptySheet(output.getTabName(), workBook);
		sizeColumns(sheet, output, analysisData, 18, 18);
		
		for (int j = 0; j < BinnerSpreadSheetWriter.CUSTOM_WIDTHS.length; j++)
			sheet.setColumnWidth(j, BinnerSpreadSheetWriter.CUSTOM_WIDTHS[j]);
		
		sheet.setColumnWidth(BinnerConstants.MASS_ERR_COL, 256); 
		
		Font font = workBook.createFont();
		font.setFontName("Courier");
		
		for (XSSFCellStyle style : styleList)
			style.setFont(font);
		
		XSSFCellStyle styleBoring = grabStyleBoring(workBook);
		XSSFCellStyle styleInteger = grabStyleInteger(workBook);
		XSSFCellStyle styleNumeric = grabStyleNumeric(workBook);
		XSSFCellStyle styleBoringLeft = grabStyleBoringLeft(workBook);
		
		styleNumeric.setFont(font);
		styleBoringLeft.setFont(font);
		styleInteger.setFont(font);
		styleBoring.setFont(font);
		
		createHeader(analysisData, output, sheet, styleList );
		for (BinnerGroup group : output.getGroups())
			createRefMassList(sheet, analysisData, group, usePutative, styleBoring, styleInteger, styleNumeric, styleBoringLeft, styleList, onlyRebin);
	}
		
	
	public void createRefMassList(Sheet sheet, AnalysisData analysisData, BinnerGroup group, Boolean usePutative, XSSFCellStyle styleBoring,
		XSSFCellStyle styleInteger, XSSFCellStyle styleNumeric, XSSFCellStyle styleBoringLeft, List<XSSFCellStyle> styleList, 
		Boolean onlyRebin) {
		List<Integer> featureIndices  = getFeatureIndexListSortedByFactor(analysisData, group, BinnerConstants.SORT_TYPE_INTENSITY);
						
		for (int j = featureIndices.size() - 1; j >= 0; j--)  {
			Feature feature = analysisData.getNonMissingFeaturesInOriginalOrder().get(featureIndices.get(j));
			
			if (usePutative)  {
				if (!feature.isPutativeMolecularIon())
					continue;
			}
			else {
				if (!(feature.getAdductOrNL().contains(BinnerConstants.SINGLETON_ANNOTATION)))
					continue;
			}
			sheet.createRow(rowCt);
			
			int i = 0;
			createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(rowIndex),
					 styleList.get(BinnerConstants.STYLE_INTEGER_CENTERED), styleList.get(BinnerConstants.STYLE_NUMERIC),
					 	styleList.get(BinnerConstants.STYLE_BORING));
			PoiUtils.createRowEntry(rowCt, i++, sheet, feature.getName(), styleBoringLeft);
			createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getMass()), styleInteger, styleNumeric, styleBoring);
			createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getRT()),
					 styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC_SHORTER),
					 	styleList.get(BinnerConstants.STYLE_BORING));		
			
			createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(Math.round(feature.getMedianIntensity())), styleInteger, styleNumeric, styleBoring);
			createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf((feature.getRmDefect())), styleInteger, styleNumeric, styleBoring);
			
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
					styleInteger, styleNumeric, styleBoring);
					
			createAppropriateRowEntry(rowCt, i++, sheet, feature.getMolecularIonNumber(),
					 styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC),
					 	styleList.get(BinnerConstants.STYLE_BORING));
			PoiUtils.createRowEntry(rowCt, i++, sheet, (StringUtils.isEmptyOrNull(feature.getChargeCarrier()) ? "-" : feature.getChargeCarrier()), styleBoring);
			PoiUtils.createRowEntry(rowCt, i++, sheet, (StringUtils.isEmptyOrNull(feature.getNeutralAnnotation()) ? "-" : feature.getNeutralAnnotation()), styleBoring);
			PoiUtils.createRowEntry(rowCt, i++, sheet, (StringUtils.isEmptyOrNull(feature.getReferenceMassString()) ? "-" :feature.getReferenceMassString()), styleBoring);
			createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getBinIndex() + 1), styleInteger, styleNumeric, styleBoring);
			createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getOldCluster()),  styleInteger, styleNumeric, styleBoring);
			createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(feature.getNewCluster()), styleInteger, styleNumeric, styleBoring);
			
			createAppropriateRowEntry(rowCt, i++, sheet, (onlyRebin ? "-" : String.valueOf(feature.getNewNewCluster())),
					 styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC),
					 	styleList.get(BinnerConstants.STYLE_BORING));

			createAppropriateRowEntry(rowCt, i++, sheet, "", styleList.get(BinnerConstants.STYLE_INTEGER), 
					styleList.get(BinnerConstants.STYLE_NUMERIC),
						styleList.get(BinnerConstants.STYLE_BORING));
						
			for (String value : feature.getAddedColValues())
				createAppropriateRowEntry(rowCt, i++, sheet, value, styleInteger, styleNumeric, styleBoring);

			createIntensitySection(feature, sheet, i, styleList);
			rowCt++;
			rowIndex++;
		}
	}
	
	public void createHeader(AnalysisData analysisData, BinnerOutput output, Sheet sheet, List<XSSFCellStyle> styleList) throws Exception {
		rowCt = 0;
		rowIndex = 1;
		int col = 0;
		sheet.createRow(rowCt);

		//XSSFCellStyle style = grabStyleBoringHeader(workBook, true);
		XSSFCellStyle style = styleList.get(BinnerConstants.STYLE_HEADER_WRAPPED); 
		
		for (String header : output.getHeaders()) {
		//	style.setAlignment(HorizontalAlignment.CENTER);
			PoiUtils.createRowEntry(rowCt, col++, sheet, header, style, 30);
		}
		rowCt += 2;
	}
	
	private void createIntensitySection(Feature feature, Sheet sheet, int i, List<XSSFCellStyle> styleList) {
		i++;
		Map<Integer, Double> outlierMap = feature.getOutlierMap();
		
		Boolean isAdjusted = false; //Arrays.asList(BinnerConstants.ADJ_OUTPUTS).contains(originalIdx);
		double [] valueList = feature.getUnadjustedIntensityListWithOutliers();  //getValueList(originalIdx, feature);
		Double featureMedIntensity = feature.getMedianIntensity();
		Double logFeatureMedIntensity = Math.log1p(featureMedIntensity);
		
		Integer sampleIdx = 0;
		for (Double dblValue : valueList) {
			if (outlierMap.containsKey(sampleIdx) && dblValue != null)
				createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(dblValue), 
					     !isAdjusted ? styleList.get(BinnerConstants.STYLE_LAVENDER) : styleList.get(BinnerConstants.STYLE_NUMERIC_LAVENDER), 
					     styleList.get(BinnerConstants.STYLE_NUMERIC_LAVENDER), styleList.get(BinnerConstants.STYLE_NUMERIC_LAVENDER));
						
			else if (dblValue < BinnerConstants.BIG_NEGATIVE / 2.0)
				createAppropriateRowEntry(rowCt, i++, sheet, ".", 
						styleList.get(BinnerConstants.STYLE_INTEGER_GREY_CENTERED),
							styleList.get(BinnerConstants.STYLE_NUMERIC_GREY_CENTERED), 
							styleList.get(BinnerConstants.STYLE_INTEGER_GREY_CENTERED));
			else if (isAdjusted && featureMedIntensity != null && ( Math.abs(dblValue - logFeatureMedIntensity) < 
			 BinnerConstants.EPSILON || Math.abs(dblValue - featureMedIntensity) < BinnerConstants.EPSILON)) {
				if (!sampleIdx.equals(feature.getMedianIntensityIdx()))
					createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(dblValue), 
							styleList.get(BinnerConstants.STYLE_INTEGER_GREY), styleList.get(BinnerConstants.STYLE_NUMERIC_GREY), 
								styleList.get(BinnerConstants.STYLE_NUMERIC_GREY));
				else
					createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(dblValue),
							styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC), 
								styleList.get(BinnerConstants.STYLE_NUMERIC));
			}
			else
				createAppropriateRowEntry(rowCt, i++, sheet, String.valueOf(dblValue), 
						styleList.get(BinnerConstants.STYLE_INTEGER), styleList.get(BinnerConstants.STYLE_NUMERIC), 
							styleList.get(BinnerConstants.STYLE_BORING));
			sampleIdx++;
		}
	}
}
