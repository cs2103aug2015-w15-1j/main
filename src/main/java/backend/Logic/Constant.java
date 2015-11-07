//@@author A0126258A
package main.java.backend.Logic;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constant {
	
	private static final SimpleDateFormat standardFormat = 
			new SimpleDateFormat("EEE, dd MMM yy, hh:mma");
	private static final SimpleDateFormat standardFormatNoMinute = 
			new SimpleDateFormat("EEE, dd MMM yy, hha");
	
	private static final String COLON = ":";
	
	private Constant() {
		// Prevent Constant from being instantiated
	}
	
	public static long stringToMillisecond(String dateTime) {
		
		long dateTimeMillisecond;
		Date tempDateTime = new Date();
		
		try {
			if(dateTime.contains(COLON)) {
				tempDateTime = standardFormat.parse(dateTime);
			} else {
				tempDateTime = standardFormatNoMinute.parse(dateTime);
			}
			dateTimeMillisecond = tempDateTime.getTime();
		} catch (java.text.ParseException e) {
			dateTimeMillisecond = -1;
		}

		return dateTimeMillisecond;
	}
 }
