////////////////////////////////////////////////////
// BinnerHistogramWriter.java
// Written by Jan Wigginton, December, 2017
////////////////////////////////////////////////////
package edu.umich.wld.sheetwriters;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;

import edu.umich.wld.MassDiffBar;
import edu.umich.wld.MassDiffBarGroup;
import edu.umich.wld.MassDiffHistogram;

public class BinnerHistogramWriter extends BinnerSpreadSheetWriter implements Serializable {
	
	private static final long serialVersionUID = -9067213716685963545L;
	private Palette histColorPalette;
	
	public BinnerHistogramWriter(Palette histColorPalette) {
		super();
		this.histColorPalette = histColorPalette;
	}
	
	public void createHistogramTab(Workbook workBook, MassDiffHistogram histData) throws Exception {
		Map<Integer, List<XSSFCellStyle>> histStyleMap = initializeStyleMap(workBook, histColorPalette, true);
		
		Sheet sheet = createEmptySheet("Mass Diff Distribution", workBook);
		int graphAreaWidth = Math.max(10, ((int) (histData.getSecondMaxSlotCount() * 1.5	 + 1) ) / 5  + 1);
		int totalWidth = graphAreaWidth + 21;
		
		sheet.setColumnWidth(0, 3 * 256);
		sheet.setColumnWidth(1, 15 * 256);
		for (int j = 2; j < graphAreaWidth + 7; j++)
			sheet.setColumnWidth(j, (j < graphAreaWidth + 1 ? 1 : 15) * 256);
		sheet.setColumnWidth(graphAreaWidth + 3, 30 * 256);
		sheet.setColumnWidth(graphAreaWidth + 4, 30 * 256);
		sheet.setColumnWidth(graphAreaWidth + 5, 60 * 256);
		sheet.createFreezePane(2, 4);
		
		XSSFColor colorGrey = ColorUtils.grabRGBColor(240, 240, 240);
		
		String chartNumberFormatString = "0.000";
		XSSFDataFormat chartDataFormat = (XSSFDataFormat) workBook.createDataFormat();
		short chartNumberFormatCode = chartDataFormat.getFormat(chartNumberFormatString);
	     
		XSSFCellStyle styleHeader = grabStyle(MyCellStyle.BLUE_TABLEHEADER_LARGE, workBook);
		styleHeader.setVerticalAlignment(VerticalAlignment.BOTTOM);
		
		XSSFCellStyle headerTitleStyle = (XSSFCellStyle) styleHeader.clone();
		headerTitleStyle.setAlignment(HorizontalAlignment.LEFT);
		
		XSSFCellStyle styleBoring = null;
		try { styleBoring = grabStyleBlankBoring(workBook); } 
		catch (Exception e) { e.printStackTrace(); }
		
		Font fontHeader = workBook.createFont();
		fontHeader.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		fontHeader.setFontHeightInPoints((short) 16);
		fontHeader.setColor(IndexedColors.WHITE.getIndex());
		
		Font fontBar = workBook.createFont();
		fontBar.setFontName("Courier");
		fontBar.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		fontBar.setFontHeightInPoints((short) 14);
		fontBar.setColor(IndexedColors.BLACK.getIndex());
		
		Font fontChart = workBook.createFont();
		fontChart.setFontName("Courier");
		fontChart.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		fontChart.setFontHeightInPoints((short) 14);
		fontChart.setColor(IndexedColors.BLACK.getIndex());
		
		styleHeader.setFont(fontHeader);
		
		XSSFCellStyle styleChart = (XSSFCellStyle) styleBoring.clone();
		styleChart.setAlignment(HorizontalAlignment.RIGHT);
		styleChart.setIndention((short) 1); 
		styleChart.setFont(fontChart);
		styleChart.setBorderColor(BorderSide.RIGHT, colorGrey);
		styleChart.setBorderColor(BorderSide.LEFT, colorGrey);
		
		XSSFCellStyle styleChartNumeric = (XSSFCellStyle) styleChart.clone();		
		styleChartNumeric.setDataFormat(chartNumberFormatCode);
		
		XSSFCellStyle styleChartBothBar = (XSSFCellStyle) styleBoring.clone();
		styleChartBothBar.setLocked(true);
		styleChartBothBar.setAlignment(HorizontalAlignment.RIGHT);
		styleChartBothBar.setIndention((short) 1); 
		styleChartBothBar.setFont(fontChart);
		styleChartBothBar.setBorderLeft(BorderStyle.THIN);
		styleChartBothBar.setBorderRight(BorderStyle.THIN);
		styleChartBothBar.setBorderColor(BorderSide.RIGHT, colorGrey);
		styleChartBothBar.setBorderColor(BorderSide.LEFT, colorGrey);
		
		XSSFCellStyle styleChartBothBarNumeric = (XSSFCellStyle) styleChartBothBar.clone();		
		styleChartBothBarNumeric.setDataFormat(chartNumberFormatCode);
		
		XSSFCellStyle styleChartRightBar = (XSSFCellStyle) styleChartBothBar.clone();
		styleChartRightBar.setAlignment(HorizontalAlignment.RIGHT);
		styleChartRightBar.setIndention((short) 1); 
		styleChartRightBar.setBorderRight(BorderStyle.THIN);
		styleChartRightBar.setBorderLeft(BorderStyle.NONE);
	    styleChartRightBar.setFont(fontChart);
	    styleChartRightBar.setBorderColor(BorderSide.RIGHT, colorGrey);
	    
	    XSSFCellStyle styleChartRightBarNumeric = (XSSFCellStyle) styleChartRightBar.clone();		
		styleChartRightBarNumeric.setDataFormat(chartNumberFormatCode);
		
		XSSFCellStyle noteStyle = (XSSFCellStyle) styleBoring.clone();
	    noteStyle.setAlignment(HorizontalAlignment.LEFT);
	    
		Font fontNote = workBook.createFont();
		fontNote.setFontName("Courier");
		fontNote.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
		fontNote.setFontHeightInPoints((short) 14);
		fontNote.setItalic(true);
		fontNote.setColor(IndexedColors.BLACK.getIndex());
		noteStyle.setFont(fontNote);
		
		int rowNum = 0;
		PoiUtils.createBlankCleanRow(rowNum++, sheet, totalWidth, styleBoring);
		PoiUtils.createBlankCleanRow(rowNum, sheet, totalWidth, styleBoring);
	
		PoiUtils.createBlankCleanRow(++rowNum, sheet, totalWidth, styleBoring);
		PoiUtils.createBlankCleanRow(++rowNum, sheet, totalWidth, styleBoring);
	
		PoiUtils.createRowEntry(rowNum, 1, sheet, "    Frequent Mass Differences (" 
				+ (histData.getIsDeisotoped() ? "without" : "including") + " isotopes) ", headerTitleStyle);
		CellRangeAddress address2 = new CellRangeAddress(rowNum,rowNum,1,  graphAreaWidth + 1); 
		sheet.addMergedRegion(address2);	
		
		int col = graphAreaWidth + 2;
		List<String> headers = Arrays.asList(new String [] {"Count", "Peak (Empirical)", "Peak (Annotation)", "Annotation", "Group Count"});
		for (String header : headers)
			PoiUtils.createRowEntry(rowNum, col++, sheet, header, styleHeader, 24);
		rowNum++;
		
		XSSFCellStyle styleBar  = null;
		Integer nStyles = 1, nColors = histStyleMap.keySet().size();
		
		for (MassDiffBarGroup group : histData.getHistogramGroups()) {
			
			//System.out.println("group " + group.getAnnotatedValue());
			
			int nEmpiricalBars = histData.getNEmpiricalBars();
			//if (group.getMaxCount() < histData.getNextPercentile() && !group.getIsForced() && nEmpiricalBars > 5)
			//	continue;
			
			if (nEmpiricalBars > 5 && group.getGroupBars().size() < 3 && group.getTotalCount() <  Math.min(30, histData.getTopPercentile())) {
				System.out.println("Filtering group of " + group.getTotalCount() + "with peak " + group.getGroupMax() + " at " + group.getGroupPeak());
				continue;
			  }
			
		//	if (nEmpiricalBars > 5 && group.getGroupBars().size() < 4 && group.getTotalCount() < Math.min(15, histData.getNextPercentile()) * group.getGroupBars().size()) {
		//		System.out.println("Filtering group of " + group.getTotalCount() + "with peak " + group.getGroupMax() + " at " + group.getGroupPeak());
		//		continue;
		//		}
			styleBar = histStyleMap.get((nStyles++)%nColors).	get(STYLE_NUMERIC);
			styleBar.setFont(fontBar);
	
			PoiUtils.createBlankCleanRow(rowNum, sheet, totalWidth, styleBoring);
			PoiUtils.createRowEntry(rowNum, 1, sheet, "", styleChartBothBar);
			PoiUtils.createRowEntry(rowNum, totalWidth - 1, sheet, "", styleChartRightBar);
			PoiUtils.createRowEntry(rowNum++, graphAreaWidth + 2, sheet, "", styleChartRightBar);
			
			for (MassDiffBar bar : group.getGroupBars()) {
				PoiUtils.createBlankCleanRow(rowNum, sheet, totalWidth, styleBoring);
				String label = String.format("%.3f", bar.getBinValue());
				PoiUtils.createNumericRowEntry(rowNum, 1, sheet, label, styleChartBothBarNumeric);
				
				int j = 0, col2 = 1;
				while (col2 < graphAreaWidth + 2) {
					if (j++%5 ==0)
						{
						col2++;
						
						if (j <= bar.getCount()) {
							if (col2 >= graphAreaWidth)
								sheet.setColumnWidth(col2, (10) * 256);
							Integer countLeft = bar.getCount() - j - 1;
							String extra = (col2 < graphAreaWidth ? "+" : (col2 == graphAreaWidth ? "... +" + countLeft : ""));
							PoiUtils.createRowEntry(rowNum, col2, sheet, (col2 < graphAreaWidth ? "+" : extra), 
									col2 < graphAreaWidth ? styleBar : styleBoring);
						
							if (col2 > graphAreaWidth + 1) {
								col++;
								break;
							}
						}
					}
				}
				
				label = new Integer(bar.getCount()).toString();
				PoiUtils.createNumericRowEntry(rowNum, col2++, sheet, label, styleChartRightBar);
				
				Boolean isAnnotationBar = bar.getIndexInCounts() == group.getBinIdxForMax();
				if (isAnnotationBar) {
					label = new Double(bar.getBinValue()).toString();
					PoiUtils.createNumericRowEntry(rowNum, col2++, sheet, label, styleChartNumeric);
					
					if ("NA".equals(group.getAnnotatedValue()))
						PoiUtils.createRowEntry(rowNum, col2++, sheet, "NA", styleChart);
					else
						PoiUtils.createNumericRowEntry(rowNum, col2++, sheet, group.getAnnotatedValue(), styleChartNumeric);
					PoiUtils.createRowEntry(rowNum, col2++, sheet, group.getAnnotation(), styleChart);
					
					label = new Integer(group.getTotalCount()).toString();
					PoiUtils.createNumericRowEntry(rowNum++, col2++, sheet, label, styleChartRightBar);
				}
				else {
					PoiUtils.createRowEntry(rowNum, col2++, sheet, "-", styleChart);
					PoiUtils.createRowEntry(rowNum, col2++, sheet, "-", styleChart);
					PoiUtils.createRowEntry(rowNum, col2++, sheet, "-", styleChart);
					PoiUtils.createRowEntry(rowNum++, col2++, sheet, "", styleChartRightBar);
				}
			}
		}
		for (int j = 0; j < 60; j++)
			PoiUtils.createBlankCleanRow(rowNum + j, sheet, totalWidth, styleBoring);
	}
}
