package main.java.backend.Parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class DateParser extends ParserSkeleton{

	//List of months and their short-forms
	private final ArrayList<String> MONTHS = new ArrayList<String>( Arrays.asList(
	"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december", 
	"jan", "feb", "mar", "apr", "jun", "jul", "aug", "sep", "oct", "nov", "dec") );
	
	//List of days in a week and their short-forms
	private final ArrayList<String> DAYS_OF_WEEK = new ArrayList<String>( Arrays.asList(
	"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday",
	"mon", "tue", "wed", "thu", "fri", "sat", "sun") );
	
	private final com.joestelmach.natty.Parser NATTY = new com.joestelmach.natty.Parser();
	
	/**
	 * This method checks that a date string is valid and parses it into the default date format 
	 */
	String parseDate(String date) {
		if (date.isEmpty()) {
			return date;
		}
	
		date = swapDayAndMonth(date);
		date = removeLater(date);
		date = addSpaceBetweenDayAndMonth(date);
		
		String parsedDate = parseDateWithNatty(date);
		parsedDate = standardizeDateFormat(parsedDate);
		parsedDate = confirmDateIsInFuture(parsedDate);
		parsedDate = removeMinuteIfZero(parsedDate);
		
		return parsedDate;
	}

	ArrayList<String> parseEventStart(String eventStart) {
		if (isInvalidDate(eventStart)) {
			return makeErrorResult("InvalidDateError", eventStart);
		}
		
		String parsedStart = parseDate(eventStart);
		if (hasNoTime(eventStart)) {
			eventStart = getDayMonthAndYear(parsedStart) + ", 12pm";
		} else {
			eventStart = parsedStart;
		}
		return new ArrayList<String>( Arrays.asList("OK", eventStart));
	}

	ArrayList<String> parseEventEnd(String eventStart, String eventEnd) {
		if (isInvalidDate(eventEnd)) {
			return makeErrorResult("InvalidDateError", eventEnd);
		}
		String parsedEnd = parseDate(eventEnd);
		if (hasNoDate(eventEnd)) {
			eventEnd = getTime(parsedEnd);
		} else if (hasNoTime(eventEnd)) {
			eventEnd = getDayMonthAndYear(parsedEnd);
		} else {
			eventEnd = parsedEnd;
		}
		
		eventEnd = makeEventEndComplete(eventStart, eventEnd);
		
		return new ArrayList<String>( Arrays.asList("OK", eventEnd));
	}

	boolean isInvalidDate(String dateString){
		if (dateString.isEmpty()) {
			return false;
		}
		try {
			NATTY.parse(dateString).get(0);
			return false;
		} catch (Exception e){
			return true;
		}
	}

	boolean hasNoDate(String dateString) {
		if (dateString.split("/").length > 1 || dateString.split("-").length > 1) {
			return false;
		} else {
			ArrayList<String> tokens = new ArrayList<String>( Arrays.asList(dateString.split(" ") ));
			for (String token: tokens) {
				if (isMonth(token) || isDayOfWeek(token)) {
					return false;
				}
				if (isDateKeyword(token, tokens)) {
					return false;
				}
			}
		}
		return true;
	}

	private String addSpaceBetweenDayAndMonth(String dateString) {
		String[] dateTokens = dateString.split(" ");
		findMonth:
		for (int i = 0; i < dateTokens.length; i++) {
			String token = dateTokens[i];
			token = token.toLowerCase();
			for (String month: MONTHS) {
				if (containsMonth(token, month) && !getNumber(token).isEmpty()){
					dateTokens[i] = getNumber(token) + " " + month;
					break findMonth;
				}
			}
		}
		return mergeTokens(dateTokens, 0, dateTokens.length);
	}

	/**
	 * This method checks if the user have included the time in the date string
	 */
	boolean hasNoTime(String dateString){
		ArrayList<String> eventTokens = new ArrayList<String>( Arrays.asList(dateString.split(" ")));
		for (String token: eventTokens){
			if (isValid12HourTime(token, eventTokens)){
				return false;
			}
			if (isValid24HourTime(token)){
				return false;
			}
		}
		return true;
	}

	String changeToRecurFormat(String freq, String dateString) {
		/*SimpleDateFormat standardFormat = new SimpleDateFormat("EEE, dd MMM yy, hh:mma");
		SimpleDateFormat recurDayFormat = new SimpleDateFormat("hh:mma");
		SimpleDateFormat recurWeekFormat = new SimpleDateFormat("EEE hh:mma");
		SimpleDateFormat recurYearFormat = new SimpleDateFormat("dd MMM");*/
		
		switch (freq) {
			case "day":
				dateString = getTime(dateString);
				break;
			case "week":
				dateString = getDayOfWeek(dateString) + " " + getTime(dateString);
				break;
			case "year":
				dateString = getDayAndMonth(dateString);
				break;
			default:
				break;
		}
		
		return dateString;
	}

	private String parseDateWithNatty(String date) {
		return NATTY.parse(date).get(0).getDates().toString();
	}

	private String standardizeDateFormat(String dateString) {
		SimpleDateFormat nattyFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		SimpleDateFormat standardFormat = new SimpleDateFormat("EEE, dd MMM yy, h:mma");
		dateString = removeEndSpacesOrBrackets(dateString);
		Date tempDate = convertStringToDate(dateString, nattyFormat);
		dateString = standardFormat.format(tempDate);
		return dateString;
	}

	private Date convertStringToDate(String dateString, SimpleDateFormat sdf){
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			System.out.println("DateParsingError: problem parsing date string '"  + dateString + "' ");
			e.printStackTrace();
		}
		return date;
	}

	private Date convertStandardDateString(String dateString){
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yy, hh:mma");
		SimpleDateFormat sdfNoMinute = new SimpleDateFormat("EEE, dd MMM yy, hha");
		
		Date date;
		if (hasMinute(dateString)) {
			date = convertStringToDate(dateString, sdf);
		} else {
			date = convertStringToDate(dateString, sdfNoMinute);
		}
		
		return date;
	}

	private Date convertStandardTimeString(String timeString){
		SimpleDateFormat stf = new SimpleDateFormat("hh:mma");
		SimpleDateFormat stfNoMinute = new SimpleDateFormat("hha");
		
		Date time;
		if (hasMinute(timeString)) {
			time = convertStringToDate(timeString, stf);
		} else {
			time = convertStringToDate(timeString, stfNoMinute);
		}
		
		return time;
	}

	private int convertTimeStringToInt(String timeString){
		try {
			return Integer.parseInt(timeString); 
		} catch (NumberFormatException e) {
			System.out.println("TimeParsingError: problem converting time '" + timeString + "' to integer");
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * This method swaps the position of day and month (so that it will work correctly for the natty parser)
	 */
	private String swapDayAndMonth(String date) {
		String[] dateTokens = date.split(" ");
		String dateSymbol = getDateSymbol(date);
		if (dateSymbol.isEmpty()) {
			return date;
		}
		String[] ddmmyyDate = {};
		String mmddyyDate = "";
		
		for (String token: dateTokens) {
			if (token.contains(dateSymbol)) {
				ddmmyyDate = token.split(dateSymbol);
				
				if (ddmmyyDate.length >= 2) {
					String day = ddmmyyDate[0];
					String month = ddmmyyDate[1];
					String year = "";
					mmddyyDate += month + "/" + day;
					if (ddmmyyDate.length == 3) {
						year = ddmmyyDate[2];
						mmddyyDate += "/" + year;
					} 
					mmddyyDate += " ";
				}
				
			} else {
				mmddyyDate += token + " ";
			}
		}
		
		mmddyyDate = removeEndSpacesOrBrackets(mmddyyDate);
		return mmddyyDate;
	}

	private String removeLater(String date) {
		String[] dateTokens = date.split(" ");
		date = "";
		for (String token: dateTokens) {
			if (!token.equalsIgnoreCase("later")) {
				date += token + " ";
			}
		}
		return removeEndSpacesOrBrackets(date);
	}

	/**
	 * This method confirms that the date set by the parser is in the future
	 */
	private String confirmDateIsInFuture(String date) {
		if (isInThePast(date)) {
			int year = getYear(date);
			int currYear = getCurrentYear();
			if (year < currYear || year >= currYear + 5) {
				date = setToCurrentYear(date);
			}
			
			if (year <= currYear) {
				String dayMonth = getDayAndMonth(date);
				if (isInThePast(dayMonth)) {
					date = plusOneYear(date);
				}
			}
		}
		return date;
	}

	private String removeMinuteIfZero(String dateString) {
		if (dateString.contains(":")) {
			String time = getTime(dateString);
			int hour = getHour(time);
			int minute = getMinute(time);
			String period = getPeriod(time);
			
			if (minute == 0) {
				time = hour + period;
			} else {
				time = hour + ":" + minute + period;
			}
			
			String[] dateTokens = dateString.split(", ");
			return getFirst(dateTokens) + ", " + getSecond(dateTokens) + ", " + time;
		} 
		return dateString;
	}

	private String makeEventEndComplete(String eventStart, String eventEnd) {
		String startDate = getDayMonthAndYear(eventStart);
		String startTime = getTime(eventStart);
		
		if (eventEnd.isEmpty()) {
			eventEnd = startDate + ", 11:59pm";
		} else if (hasNoDate(eventEnd)) {
			String endTime = eventEnd;
			if (startTimeIsNotBeforeEndTime(startTime, endTime)) {
				startDate = plusOneDay(startDate);
			} 
			eventEnd = startDate + ", " + endTime;	
		} else if (hasNoTime(eventEnd)) {
			String endDate = eventEnd;
			eventEnd = endDate + ", " + startTime;
		}
		
		if (startDateIsAfterEndDate(eventStart, eventEnd)) {
			eventEnd = plusOneYear(eventEnd);
		}
		return eventEnd;
	}

	private boolean startDateIsAfterEndDate(String startDateString, String endDateString){
		Date startDate = convertStandardDateString(startDateString);
		Date endDate = convertStandardDateString(endDateString);
		return startDate.after(endDate);
	}

	private boolean startTimeIsNotBeforeEndTime(String startTime, String endTime){
		Date startTimeDate = convertStandardTimeString(startTime);
		Date endTimeDate = convertStandardTimeString(endTime);
		return !startTimeDate.before(endTimeDate);
	}

	private String plusOneDay(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yy");
		Date date = convertStringToDate(dateString, sdf);
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		Date newDate = c.getTime();
		
		return sdf.format(newDate);
	}

	private String plusOneYear(String dateString) {
		Date date = convertStandardDateString(dateString);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, 1);
		date = c.getTime();
		return removeMinuteIfZero(standardizeDateFormat(date.toString()));
	}

	private String setToCurrentYear(String dateString) {
		String currYear = Integer.toString(getCurrentYear());
		String[] dateTokens = dateString.split(", ");
		
		String ddMMMyy = getSecond(dateTokens);
		String[] ddMMMyyTokens = ddMMMyy.split(" ");
		ddMMMyy = getFirst(ddMMMyyTokens) + " " + getSecond(ddMMMyyTokens) + " " + currYear;
		
		String ddMMM = getFirst(ddMMMyyTokens) + " " + getSecond(ddMMMyyTokens);
		String EEE = getDayOfWeek(parseDate(ddMMM + " " + getLast(dateTokens)));
		
		return EEE + ", " + ddMMMyy + ", " + getLast(dateTokens);
	}

	private String getDateSymbol(String date) {
		if (date.contains("/")){
			return "/";
		} else if (date.contains("-")) {
			return "-";
		} else {
			return "";
		}
	}

	private String getDayOfWeek(String dateString) {
		String[] dateTokens = dateString.split(", ");
		return getFirst(dateTokens);
	}

	private String getDayAndMonth(String dateString) {
		String[] dateTokens = dateString.split(", ");
		String ddMMMyy = getSecond(dateTokens);
		String[] ddMMMyyTokens = ddMMMyy.split(" ");
		return getFirst(ddMMMyyTokens) + " " + getSecond(ddMMMyyTokens);
	}

	private int getYear(String dateString) {
		String[] dateTokens = dateString.split(", ");
		String ddMMMyy = getSecond(dateTokens);
		String[] ddMMMyyTokens = ddMMMyy.split(" ");
		return Integer.parseInt(getLast(ddMMMyyTokens));
	}

	private String getDayMonthAndYear(String dateString) {
		String[] dateTokens = dateString.split(", ");
		return getFirst(dateTokens) + ", " + getSecond(dateTokens);
	}

	private String getTime(String dateString) {
		String[] dateTokens = dateString.split(", ");
		return getLast(dateTokens);
	}

	private int getHour(String timeString) {
		String[] timeTokens;
		timeTokens = timeString.split(":");
		
		int hour = convertTimeStringToInt(getFirst(timeTokens));
		return hour;
	}

	private int getMinute(String timeString) {
		String[] timeTokens;
		timeTokens = timeString.split(":");
		if (timeTokens.length > 1) {
			String minuteWithPeriod = getSecond(timeTokens);
			String[] minuteToken;
			if (isAM(timeString)) {
				minuteToken = minuteWithPeriod.split("am");
				if (getFirst(minuteToken).equals(minuteWithPeriod)) {
					minuteToken = getSecond(timeTokens).split("AM");
				}
			} else {
				minuteToken = getSecond(timeTokens).split("pm");
				if (getFirst(minuteToken).equals(minuteWithPeriod)) {
					minuteToken = getSecond(timeTokens).split("PM");
				}
			}
			return convertTimeStringToInt(getFirst(minuteToken));
		} else {
			return 0;
		}
	}

	private String getPeriod(String timeString){
		if (isAM((timeString))) {
			return "am";
		}
		if (isPM((timeString))) {
			return "pm";
		}
		return "error";
	}

	private Date getCurrentDate() {
		Date now = new Date();
		return now;
	}

	private int getCurrentYear() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yy");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
		return Integer.parseInt(strDate);
	}



	private boolean hasMinute(String time){
		return time.split(":").length > 1;
	}

	private boolean isDayOfWeek(String token) {
		return DAYS_OF_WEEK.contains(token.toLowerCase());
	}

	private boolean isMonth(String token) {
		token = token.toLowerCase();
		if (MONTHS.contains(token)){
			return true;
		}
		for (String month: MONTHS) {
			if (containsMonth(token, month)) {
				return true;
			}
		}
		return false;
		//return MONTHS.contains(token.toLowerCase());
	}
	
	private boolean containsMonth (String token, String month){
		return (token.startsWith(month) || token.endsWith(month));
	}

	private boolean isDateKeyword(String token, ArrayList<String> tokenArray) {
		//List of date keywords recognised by the date parser Natty
		ArrayList<String> dateKeywords = new ArrayList<String>( Arrays.asList("today", "tomorrow", "tmr") );
		if (dateKeywords.contains(token.toLowerCase())) {
			return true;
		} 
		
		//List of date keywords recognised by the Natty if it follows a number or the word 'next'
		ArrayList<String> datePartialKeywords = new ArrayList<String>( Arrays.asList("day", "days", "week", "weeks") );
		if (datePartialKeywords.contains(token.toLowerCase())) {
			String previousToken = getPrevious(tokenArray, token);
			if (previousToken != null) {
				if (isNumber(previousToken) || previousToken.equalsIgnoreCase("next")) {
					return true;
				}
			}
		} 
		
		return false;
	}

	private boolean isInThePast(String dateString){
		Date date = new Date();
		if (dateString.split(" ").length == 2) {
			SimpleDateFormat dayAndMonthFormat = new SimpleDateFormat("dd MMM");
			date = convertStringToDate(dateString, dayAndMonthFormat);
		} else
			date = convertStandardDateString(dateString);
		
		Date now = getCurrentDate();
		return now.after(date);
	}

	private boolean isAM(String time){
		return time.toLowerCase().endsWith("am");
	}

	private boolean isPM(String time){
		return time.toLowerCase().endsWith("pm");
	}

	private boolean isValid12HourTime(String token, ArrayList<String> tokens) {
		String period;
		if (isAM(token)) {
			period = "am";
		} else if (isPM(token)) {
			period = "pm";
		} else {
			return false;
		}
		
		String[] timeTokens = token.split(period);
		if (timeTokens.length == 1 && isNumber(getFirst(timeTokens))) {
			return true;
		}
		if (timeTokens.length == 0 && isNumber(getPrevious(tokens, token))) {
			return true;
		}
		return false;
	}

	private boolean isValid24HourTime(String token) {
		String timeSymbol;
		if (token.contains(":")) {
			timeSymbol = ":";
		} else if (token.contains(".")) {
			timeSymbol = ".";
		} else {
			return false;
		}
		
		String[] timeTokens = token.split(timeSymbol);
		if (timeTokens.length == 2){
			String hour = getFirst(timeTokens);
			String minute = getLast(timeTokens);
			if (isAM(token)) {
				minute = getFirst(minute.split("am"));
			}
			if (isPM(token)) {
				minute = getFirst(minute.split("pm"));
			}
			if (isNumber(hour) && isNumber(minute)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	ArrayList<String> makeErrorResult(String error, String token) {
		ArrayList<String> result = new ArrayList<String>(); 
		result.add("error");
		
		switch (error) {
			case "InvalidDateError":
				result.add(error + ": '" + token + "' is not an acceptable date format");
				break;
			default:
				break; 
		}
		return result;
	}
}
