package edu.umich.wld;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.umich.wld.StringUtils;



public class FormatVerifier
	{
	public static boolean verifyFormat(String format, String input)
		{
		return verifyFormat(format, input, true);
		}
	
	public static boolean verifyFormat(String format, String input, boolean handleBlanks)
		{
		if (handleBlanks && StringUtils.isEmptyOrNull(input))
				return false;
		
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
		}


	public static boolean matchFormat(String format, String input)
		{
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(input);
		return matcher.find();
		}
	}