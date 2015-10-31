package main.java.backend.Logic;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constant {
	
	private static final String CONVERSION_STRING_TO_MILLISECOND_UNSUCCESSFUL = 
			"Unable to convert date string to milliseconds due to mismatch date format.";

	private Constant() {
		// Prevent Constant from being instantiated
	}
	
	public static long stringToMillisecond(String dateTime) {
		SimpleDateFormat standardFormat = 
				new SimpleDateFormat("EEE, dd MMM yy, hh:mma");
		SimpleDateFormat standardFormatNoMinute = 
				new SimpleDateFormat("EEE, dd MMM yy, hha");
		
		long dateTimeMillisecond = -1;
		Date tempDateTime = new Date();
		
		try {
			if(dateTime.contains(":")) {
				tempDateTime = standardFormat.parse(dateTime);
			} else {
				tempDateTime = standardFormatNoMinute.parse(dateTime);
			}
			dateTimeMillisecond = tempDateTime.getTime();
		} catch (java.text.ParseException e) {
			
		}

		return dateTimeMillisecond;
	}
 }
