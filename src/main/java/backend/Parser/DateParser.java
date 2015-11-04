package main.java.backend.Parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * DateParser
 * Specialized parser that parses date to standard format
 * @@author A0121795B
 */
public class DateParser extends ParserSkeleton{

	//List of months and their short-forms
	private final ArrayList<String> MONTHS = new ArrayList<String>( Arrays.asList(
	"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december", 
	"jan", "feb", "mar", "apr", "jun", "jul", "aug", "sep", "oct", "nov", "dec") );
	
	//List of days in a week and their short-forms
	private final ArrayList<String> DAYS_OF_WEEK = new ArrayList<String>( Arrays.asList(
	"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday" ) );
	//"mon", "tue", "wed", "thu", "fri", "sat", "sun") );
	
	private final ArrayList<String> MONTHS_WITH_31_DAYS = new ArrayList<String>( Arrays.asList(
	"january", "march", "may", "july", "august", "october", "december") );
	//"jan", "mar", "jul", "aug", "oct", "dec"
	//"1", "3", "5", "7", "8", "10", "12") );
	
	private final ArrayList<String> MONTHS_WITH_30_DAYS = new ArrayList<String>( Arrays.asList(
	"april", "june", "september", "november") );
	//"apr", "jun", "sep", "nov", "4", "6", "9", "11") );
	
	private final String FEBRUARY = "february";
	
    private HashMap<String, ArrayList<String>> month_families = new HashMap<String, ArrayList<String>>(){
		static final long serialVersionUID = 1L; {
		put("january", new ArrayList<String>( Arrays.asList("jan", "1")));
		put("february", new ArrayList<String>( Arrays.asList("feb", "2")));
		put("march", new ArrayList<String>( Arrays.asList("mar", "3")));
        put("april", new ArrayList<String>( Arrays.asList("apr", "4")));
        put("may", new ArrayList<String>( Arrays.asList("may", "5"))); 
        put("june", new ArrayList<String>( Arrays.asList("jun", "6")));
        put("july", new ArrayList<String>( Arrays.asList("jul", "7")));
        put("august", new ArrayList<String>( Arrays.asList("aug", "8")));        
        put("september", new ArrayList<String>( Arrays.asList("sep", "9"))); 
        put("october", new ArrayList<String>( Arrays.asList("oct", "10")));
        put("november", new ArrayList<String>( Arrays.asList("nov", "11")));
        put("december", new ArrayList<String>( Arrays.asList("dec", "12")));
    }};
	
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
		ArrayList<String> eventStartValidity = isInvalidDate(eventStart);
		if (isErrorStatus(eventStartValidity)){
			return eventStartValidity;
		}
		
		String parsedStart = parseDate(eventStart);
		if (hasNoTime(eventStart)) {
			eventStart = getDayMonthAndYear(parsedStart) + ", 9am";
		} else {
			eventStart = parsedStart;
		}
		return new ArrayList<String>( Arrays.asList("OK", eventStart));
	}

	ArrayList<String> parseEventEnd(String eventStart, String eventEnd) {
		ArrayList<String> eventEndValidity = isInvalidDate(eventEnd);
		if (isErrorStatus(eventEndValidity)){
			return eventEndValidity;
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

	ArrayList<String> isInvalidDate(String dateString){
		if (dateString.isEmpty()) {
			return new ArrayList<String>(Arrays.asList( "OK" ));
		}
		
		ArrayList<String> dateBoundCheck = checkDateBound(dateString);
		if (isErrorStatus(dateBoundCheck)) {
			return dateBoundCheck;
		}
		
		try {
			String parsedTime = getTime(parseDate(dateString));
			String timeString = getTimeFromString(dateString);
			if (isNotMatchingTimes(parsedTime, timeString)) {
				return makeErrorResult("InvalidTimeError", timeString);
			}
			if (!isValidHour(getHourFromTimeString(timeString))) {
				return makeErrorResult("InvalidTimeError", timeString);
			}
			
			return new ArrayList<String>(Arrays.asList( "OK" ));
			
		} catch (Exception e){
			return makeErrorResult("InvalidDateError", dateString);
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
	
	boolean hasDate(String dateString) {
		return !hasNoDate(dateString);
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

	boolean hasTime(String dateString) {
		return !hasNoTime(dateString);
	}
	
	String convertToRecurFormat(String freq, String dateString) {
		/*SimpleDateFormat standardFormat = new SimpleDateFormat("EEE, dd MMM yy, hh:mma");
		SimpleDateFormat recurDayFormat = new SimpleDateFormat("hh:mma");
		SimpleDateFormat recurWeekFormat = new SimpleDateFormat("EEE hh:mma");
		SimpleDateFormat recurYearFormat = new SimpleDateFormat("dd MMM");*/
		
		switch (freq.toLowerCase()) {
			case "day":
			case "days":
				//dateString = getTime(dateString);
				dateString = minusOneYear(dateString);
				if (isInThePast(dateString)) {
					dateString = removeMinuteIfZero(plusOneDay(dateString, null));
				}
				break;
			case "week":
			case "weeks":
				//dateString = getDayOfWeek(dateString) + " " + getTime(dateString);
				String oneWeekBefore = minusOneWeek(dateString);
				if (!isInThePast(oneWeekBefore)) {
					dateString = oneWeekBefore;
				}
				break;
			case "year":
			case "years":
				//dateString = getDayAndMonth(dateString);
				break;
			default:
				break;
		}
		
		return dateString;
	}

	String parseAndGetDayAndMonth(String dateString) {
		dateString = parseDate(dateString);
		return getDayAndMonth(dateString);
	}
	
	String parseAndGetTime(String dateString) {
		dateString = parseDate(dateString);
		return getTime(dateString);
	}
	
	String parseAndGetDayOfWeek(String dateString) {
		dateString = parseDate(dateString);
		return getDayOfWeek(dateString);
	}
	
	String parseAndGetDayOfWeekAndTime(String dateString) {
		dateString = parseDate(dateString);
		return getDayOfWeek(dateString) + " " + getTime(dateString);
	}
	
	String parseAndGetMonth(String dateString) {
		dateString = parseDate(dateString);
		return getMonth(dateString);
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
	
	private String formatStandardDateString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yy, hh:mma");
		SimpleDateFormat sdfNoMinute = new SimpleDateFormat("EEE, dd MMM yy, hha");
		
		String dateString = "";
		try {
			dateString = sdf.format(date);
		} catch (Exception e) {
			try {
				dateString = sdfNoMinute.format(date);
			} catch (Exception e2) {
				e.printStackTrace();
			}
		} 
		return dateString;
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
				if (minute < 10) {
					time = hour + ":0" + minute + period;
				} else {
					time = hour + ":" + minute + period;
				}
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
			eventEnd = startDate + ", 9pm";
		} else if (hasNoDate(eventEnd)) {
			String endTime = eventEnd;
			if (startTimeIsNotBeforeEndTime(startTime, endTime)) {
				SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yy");
				startDate = plusOneDay(startDate, sdf);
			} 
			eventEnd = startDate + ", " + endTime;	
		} else if (hasNoTime(eventEnd)) {
			String endDate = eventEnd;
			eventEnd = endDate + ", 9pm";
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

	private String plusOneDay(String dateString, SimpleDateFormat sdf) {
		Date date = new Date();
		if (sdf == null) {
			date = convertStandardDateString(dateString);
		} else {
			date = convertStringToDate(dateString, sdf);
		}
		System.out.println(date.toString());
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		Date newDate = c.getTime();
		
		if (sdf == null) {
			return formatStandardDateString(newDate);
		} else {
			return sdf.format(newDate);
		}
	}

	private String plusXDays(String dateString, int day) {
		Date date = convertStandardDateString(dateString);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, day);
		date = c.getTime();
		return removeMinuteIfZero(standardizeDateFormat(date.toString()));
	}
	
	public String plusOneMonth(String month) {
		Date date = convertStandardDateString(month);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		date = c.getTime();
		return removeMinuteIfZero(standardizeDateFormat(date.toString()));
	}
	
	private String plusOneYear(String dateString) {
		Date date = convertStandardDateString(dateString);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, 1);
		date = c.getTime();
		return removeMinuteIfZero(standardizeDateFormat(date.toString()));
	}
	
	private String minusOneYear(String dateString) {
		Date date = convertStandardDateString(dateString);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, -1);
		date = c.getTime();
		return removeMinuteIfZero(standardizeDateFormat(date.toString()));
	}
	
	private String minusOneWeek(String dateString) {
		Date date = convertStandardDateString(dateString);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.WEEK_OF_MONTH, -1);
		date = c.getTime();
		return removeMinuteIfZero(standardizeDateFormat(date.toString()));
	}

	String getNextNearestDate(int day){
		String currDate = parseDate(getTomorrowDate() + " 9am"); 
		int currDay;
		/*if (isInThePast(currDate)) {
			currDate = plusOneDay(currDate, null);
		}*/
		while ((currDay = getDayOfMonth(currDate)) != day) {
			if (currDay < day) {
				currDate = plusXDays(currDate, day-currDay);
			} else {
				String currMonth = getCurrentMonth();
				currDate = plusXDays(currDate, getMaxDay(currMonth)-currDay+day);
			}
		}
		return currDate;
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

	private int getDayOfMonth(String dateString) {
		String[] dateTokens = dateString.split(", ");
		String ddMMMyy = getSecond(dateTokens);
		String[] ddMMMyyTokens = ddMMMyy.split(" ");
		return convertStringToInt(getFirst(ddMMMyyTokens));
	}
	
	private String getDayAndMonth(String dateString) {
		String[] dateTokens = dateString.split(", ");
		String ddMMMyy = getSecond(dateTokens);
		String[] ddMMMyyTokens = ddMMMyy.split(" ");
		return getFirst(ddMMMyyTokens) + " " + getSecond(ddMMMyyTokens);
	}
	
	private String getMonth(String dateString) {
		String[] dateTokens = dateString.split(", ");
		String ddMMMyy = getSecond(dateTokens);
		String[] ddMMMyyTokens = ddMMMyy.split(" ");
		return getSecond(ddMMMyyTokens);
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

	private String getTimeSymbol(String timeString) {
		if (timeString.contains(":")){
			return ":";
		} else if (timeString.contains(".")) {
			return "\\.";
		} else {
			return "";
		}
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
	
	private String getTomorrowDate() {
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.DATE, 1);
		now = c.getTime();
		return removeMinuteIfZero(standardizeDateFormat(now.toString()));
	}

	String getCurrentMonth() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("MMM");
	    Date now = new Date();
	    return sdfDate.format(now);
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

	
	boolean isDayOfWeek(String token) {
		token = removeEndSpacesOrBrackets(token.toLowerCase());
		if (DAYS_OF_WEEK.contains(token)){
			return true;
		}
		if (token.length() >= 3) {
			for (String day: DAYS_OF_WEEK){
				if (day.startsWith(token)) {
					//System.out.println("t");
					return true;
				}
			}
		}
		return false;
	}
	/*private boolean isDayOfWeek(String token) {
		return DAYS_OF_WEEK.contains(token.toLowerCase());
	}*/

	boolean isMonth(String token) {
		if (isNumber(token) && getDateSymbol(token).isEmpty() && addSpaceBetweenDayAndMonth(token).split(" ").length == 1) {
			return false;
		}
		token = token.toLowerCase();
		token = convertMonthToDefault(token);
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
	
	private boolean isTimeFormat(String time){
		return isAM(time) || isPM(time) || !getTimeSymbol(time).isEmpty();
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
		if (timeTokens.length <= 1) {
			String time = "";
			if (timeTokens.length == 1){
				time = getFirst(timeTokens);
			} else if (timeTokens.length == 0){
				time = getPrevious(tokens, token);
			}
			if (isTimeFormat(time)) {
				return isValid24HourTime(time);
			} else {
				return isValidHour(time);
			}
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
			if (isValidHour(hour) && isValidMinute(minute)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isValidHour(String hour){
		if (hour.isEmpty()) {
			return true;
		}
		try {
			int min = Integer.parseInt(hour);
			return min >= 0 && min <= 23;
		} catch (NumberFormatException e) {
			System.out.println("TimeParsingError: problem converting hour '" + hour + "' to integer");
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean isValidMinute(String minute){
		try {
			int min = Integer.parseInt(minute);
			return min >= 0 && min <= 60;
		} catch (NumberFormatException e) {
			System.out.println("TimeParsingError: problem converting minute '" + minute + "' to integer");
			e.printStackTrace();
			return false;
		}
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

	private ArrayList<String> checkDateBound(String dateString) {
		String[] dateTokens = dateString.split(" ");
		for (int i = 0; i < dateTokens.length; i++) {
			String token = dateTokens[i];
			int day = -1;
			String month = "";
			if (isddmmFormat(token)) {
				day = getDayfromDateString(token);
				month = getMonthfromDateString(token);
				if (noSuchDayInMonth(day, month)) {
					return makeErrorResult("InvalidDayOfMonthError", Integer.toString(day) + "/" + month);
				}
			}
			if (isMonth(token)) {
				token = addSpaceBetweenDayAndMonth(token);
				if (token.split(" ").length > 1) {
	
					day = convertStringToInt(getFirst(token));
					month = getLast(token);
				} else {
					String prev = getPrevious(dateTokens, i);
					month = token;
					if (isNumber(prev)) {
						day = convertStringToInt(prev);
						if (noSuchDayInMonth(day, month)) {
							return makeErrorResult("InvalidDayOfMonthError", Integer.toString(day) + " " + month);
						}
					} else {
						day = convertStringToInt(getNext(dateTokens, i));
						if (noSuchDayInMonth(day, month)) {
							return makeErrorResult("InvalidDayOfMonthError", month + " " + Integer.toString(day));
						}
					}
					
				}
			}
		}
		return new ArrayList<String>( Arrays.asList("OK"));
	}

	private String convertMonthToDefault(String token) {
		token = token.toLowerCase();
		for (String month: month_families.keySet()) {
			ArrayList<String> family = month_families.get(month);
			if (family.contains(token)) {
				return month;
			}
		}
		return token;
	}
	
	boolean noSuchDayInMonth(int day, String month) {
		month = removeFrontZero(month.toLowerCase());
		if (has31Days(month)) {
			return day > 31;
		}
		if (has30Days(month)) {
			return day > 30;
		}
		if (isFebruary(month)) {
			return day > 29;
		}
		return true;
	}

	private boolean has31Days(String month){
		return MONTHS_WITH_31_DAYS.contains(convertMonthToDefault(month));
	}
	
	private boolean has30Days(String month){
		return MONTHS_WITH_30_DAYS.contains(convertMonthToDefault(month));
	}
	
	private boolean isFebruary(String month){
		return FEBRUARY.equals(convertMonthToDefault(month));
	}
	
	private int getMaxDay(String month){
		month = month.toLowerCase();
		if (has31Days(month)) {
			return 31;
		}
		if (has30Days(month)) {
			return 30;
		}
		if (isFebruary(month)) {
			return 29;
		}
		return -1;
	}
	
	private boolean isddmmFormat(String token) {
		return !getDateSymbol(token).isEmpty() && !getNumber(token).isEmpty();
	}

	private int getDayfromDateString(String token) {
		try {
			return Integer.parseInt(getFirst(token.split(getDateSymbol(token))));
		} catch (NumberFormatException e) {
			System.out.println("DateParsingError: problem converting day '" + getFirst(token.split(getDateSymbol(token))) + "' to integer");
			e.printStackTrace();
			return -1;
		}
	}
	
	private String getMonthfromDateString(String token) {
		String sym = getDateSymbol(token);
		if (!sym.isEmpty()) {
			return getSecond(token.split(getDateSymbol(token)));
		} else {
			String[] tokens = token.split(" ");
			if (isNumber(getFirst(tokens))) {
				return getSecond(tokens);
			} else {
				return getFirst(tokens);
			}
		}
	}
	
	private String getTimeFromString(String dateString) {
		ArrayList<String> dateTokens = new ArrayList<String>( Arrays.asList(dateString.split(" ")));
		for (String token: dateTokens) {
			if (isTimeFormat(token)) {
				if (token.equalsIgnoreCase("am") || token.equalsIgnoreCase("pm")) {
					return getPrevious(dateTokens, token) + " " + token;
				}
				return token;
			}
		}
		return "";
	}

	private String getHourFromTimeString(String timeString){
		String sym = getTimeSymbol(timeString);
		if (!sym.isEmpty()){
			return getFirst(timeString.split(sym));
		}
		if (isAM(timeString)) {
			String hour = getFirst(timeString.split("am"));
			if (hour.equals(timeString)) {
				hour = getFirst(timeString.split("AM"));
			}
			return removeEndSpacesOrBrackets(hour);
		}
		if (isPM(timeString)) {
			String hour = getFirst(timeString.split("pm"));
			if (hour.equals(timeString)) {
				hour = getFirst(timeString.split("PM"));
			}
			return removeEndSpacesOrBrackets(hour);
		}
		return timeString;
	}
	
	private boolean isNotMatchingTimes(String parsedTime, String timeString) {
		return !hasMinute(parsedTime) && (hasMinute(timeString) && !timeString.endsWith("00"));
	}

	@Override
	ArrayList<String> makeErrorResult(String error, String token) {
		ArrayList<String> result = new ArrayList<String>(); 
		result.add("error");
		
		switch (error) {
			case "InvalidDateError":
				result.add(error + ": '" + token + "' is not an acceptable date format");
				break;
			case "InvalidTimeError":
				result.add(error + ": '" + token + "' is not an acceptable time format");
				break;
			case "InvalidDayOfMonthError":
				String month = getMonthfromDateString(token);
				String defaultMonth = convertMonthToDefault(month);
				result.add(error + ": The date '" + token + "' does not exist "
						+ "(" + capitalize(defaultMonth) + " only has " + getMaxDay(defaultMonth) + " days!)");
				break;
			default:
				break; 
		}
		return result;
	}
}
