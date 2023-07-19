package edu.umich.wld.sheetwriters;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.umich.wld.BinnerConstants;

public class CSVWriterUtils {
	static private Pattern rxquote = Pattern.compile("\"");
	
	public static BufferedWriter createCSVFile(String filename) throws IOException {
		return (new BufferedWriter(new FileWriter(filename)));
	}
	
	public static void writeStringToCSVFile(String str, BufferedWriter csvOutputStream, Boolean moreToCome) throws IOException {
		csvOutputStream.append(quoteStringIfNeeded(str));
		csvOutputStream.append(moreToCome ? "," : BinnerConstants.LINE_SEPARATOR);
	}
	
	private static String quoteStringIfNeeded(String str) {
	    boolean needQuotes = (str.indexOf(',') != -1 || str.indexOf('"') != -1 ||
	         str.indexOf('\n') != -1 || str.indexOf('\r') != -1);
	      
	    Matcher m = rxquote.matcher(str);
	    needQuotes |= (m.find()) ;
	    str = m.replaceAll("\"\"");
	  
	   return (needQuotes ? "\"" + str + "\"" : str);
	}
}
