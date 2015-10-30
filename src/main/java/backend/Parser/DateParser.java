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
	
	private com.joestelmach.natty.Parser natty = new com.joestelmach.natty.Parser();
	
	/**
	 * This method checks that a date string is valid and parses it into the default date format 
	 */
	String parseDate(String date) {
		if (date.isEmpty()) {
			return date;
		}
	
		date = swapDayAndMonth(date);
		date = removeLater(date);
		
		String parsedDate = natty.parse(date).get(0).getDates().toString();
		parsedDate = parsedDate.substring(1, parsedDate.length()-1); //remove brackets
		parsedDate = standardizeDateFormat(parsedDate);
		parsedDate = confirmDateIsInFuture(parsedDate);
		parsedDate = removeMinuteIfZero(parsedDate);
		
		return parsedDate;
	}

	private String removeLater(String date) {
		String[] dateTokens = date.split(" ");
		date = "";
		for (String token: dateTokens) {
			if (!token.equalsIgnoreCase("later")) {
				date += token + " ";
			}
		}
		return removeEndSpaces(date);
	}

	String standardizeDateFormat(String dateString) {
		SimpleDateFormat nattyFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		SimpleDateFormat standardFormat = new SimpleDateFormat("EEE, dd MMM yy, h:mma");
		Date tempDate = convertStringToDate(dateString, nattyFormat);
		dateString = standardFormat.format(tempDate);
		return dateString;
	}

	Date convertStringToDate(String dateString, SimpleDateFormat sdf){
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			System.out.println("DateParsingError: problem parsing date string '"  + dateString + "' ");
			e.printStackTrace();
		}
		return date;
	}

	Date convertStandardDateString(String dateString){
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

	Date convertStandardTimeString(String timeString){
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

	int convertTimeStringToInt(String timeString){
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
	String swapDayAndMonth(String date) {
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
		
		mmddyyDate = removeEndSpaces(mmddyyDate);
		return mmddyyDate;
	}

	/**
	 * This method confirms that the date set by the parser is in the future
	 */
	String confirmDateIsInFuture(String date) {
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

	String removeMinuteIfZero(String dateString) {
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

	ArrayList<String> parseEventEnd(String eventEnd) {
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
		return new ArrayList<String>( Arrays.asList("OK", eventEnd));
	}

	String makeEventEndComplete(String eventStart, String eventEnd) {
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
		return eventEnd;
	}

	boolean startDateIsAfterEndDate(String startDateString, String endDateString){
		Date startDate = convertStandardDateString(startDateString);
		Date endDate = convertStandardDateString(endDateString);
		return startDate.after(endDate);
	}

	boolean startTimeIsNotBeforeEndTime(String startTime, String endTime){
		Date startTimeDate = convertStandardTimeString(startTime);
		Date endTimeDate = convertStandardTimeString(endTime);
		return !startTimeDate.before(endTimeDate);
	}

	String plusOneDay(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yy");
		Date date = convertStringToDate(dateString, sdf);
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		Date newDate = c.getTime();
		
		return sdf.format(newDate);
	}

	String plusOneYear(String dateString) {
		Date date = convertStandardDateString(dateString);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, 1);
		date = c.getTime();
		return removeMinuteIfZero(standardizeDateFormat(date.toString()));
	}

	String setToCurrentYear(String dateString) {
		String currYear = Integer.toString(getCurrentYear());
		String[] dateTokens = dateString.split(", ");
		
		String ddMMMyy = getSecond(dateTokens);
		String[] ddMMMyyTokens = ddMMMyy.split(" ");
		ddMMMyy = getFirst(ddMMMyyTokens) + " " + getSecond(ddMMMyyTokens) + " " + currYear;
		
		String ddMMM = getFirst(ddMMMyyTokens) + " " + getSecond(ddMMMyyTokens);
		String EEE = getDayOfWeek(parseDate(ddMMM + " " + getLast(dateTokens)));
		
		return EEE + ", " + ddMMMyy + ", " + getLast(dateTokens);
	}

	String getDateSymbol(String date) {
		if (date.contains("/")){
			return "/";
		} else if (date.contains("-")) {
			return "-";
		} else {
			return "";
		}
	}

	String getDayOfWeek(String dateString) {
		String[] dateTokens = dateString.split(", ");
		return getFirst(dateTokens);
	}

	String getDayAndMonth(String dateString) {
		String[] dateTokens = dateString.split(", ");
		String ddMMMyy = getSecond(dateTokens);
		String[] ddMMMyyTokens = ddMMMyy.split(" ");
		return getFirst(ddMMMyyTokens) + " " + getSecond(ddMMMyyTokens);
	}

	int getYear(String dateString) {
		String[] dateTokens = dateString.split(", ");
		String ddMMMyy = getSecond(dateTokens);
		String[] ddMMMyyTokens = ddMMMyy.split(" ");
		return Integer.parseInt(getLast(ddMMMyyTokens));
	}

	String getDayMonthAndYear(String dateString) {
		String[] dateTokens = dateString.split(", ");
		return getFirst(dateTokens) + ", " + getSecond(dateTokens);
	}

	String getTime(String dateString) {
		String[] dateTokens = dateString.split(", ");
		return getLast(dateTokens);
	}

	int getHour(String timeString) {
		String[] timeTokens;
		timeTokens = timeString.split(":");
		
		int hour = convertTimeStringToInt(getFirst(timeTokens));
		return hour;
	}

	int getMinute(String timeString) {
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

	String getPeriod(String timeString){
		if (isAM((timeString))) {
			return "am";
		}
		if (isPM((timeString))) {
			return "pm";
		}
		return "error";
	}

	Date getCurrentDate() {
		Date now = new Date();
		return now;
	}

	int getCurrentYear() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yy");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
		return Integer.parseInt(strDate);
	}

	/**
	 * This method gets the individual start and end date/time of an event and put them into separate fields
	 * @return the start and end date/time in an array
	 */
	ArrayList<String> getEventStartAndEnd(String event) {
		String[] eventTokens = event.split(" to ", 2);
		String eventStart = getFirst(eventTokens);
		String eventEnd = "";
		if (eventTokens.length > 1) {
			eventEnd = removeEndSpaces(getLast(eventTokens));
		}
		
		ArrayList<String> parsedStartResult = parseEventStart(eventStart);
		if (isErrorStatus(parsedStartResult)) {
			return parsedStartResult;
		}
		eventStart = getLast(parsedStartResult);
		
		ArrayList<String> parsedEndResult = parseEventEnd(eventEnd);
		if (isErrorStatus(parsedEndResult)) {
			return parsedEndResult;
		}
		eventEnd = getLast(parsedEndResult);
		
		eventEnd = makeEventEndComplete(eventStart, eventEnd);
		
		if (startDateIsAfterEndDate(eventStart, eventEnd)) {
			eventEnd = plusOneYear(eventEnd);
		}
		
		return new ArrayList<String>( Arrays.asList(eventStart, eventEnd));
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

	/**
	 * This method checks if the user have included the time in the date string
	 */
	boolean hasNoTime(String dateString){
		String[] eventTokens;
		eventTokens = dateString.split(" ");
		for (String token: eventTokens){
			if (isValid12HourTime(token)){
				return false;
			}
			if (isValid24HourTime(token)){
				return false;
			}
		}
		return true;
	}

	boolean hasMinute(String time){
		return time.split(":").length > 1;
	}

	boolean isInvalidDate(String dateString){
		if (dateString.isEmpty()) {
			return false;
		}
		try {
			natty.parse(dateString).get(0);
			return false;
		} catch (Exception e){
			return true;
		}
	}

	boolean isAM(String time){
		return time.toLowerCase().endsWith("am");
	}

	boolean isPM(String time){
		return time.toLowerCase().endsWith("pm");
	}

	boolean isDayOfWeek(String token) {
		return DAYS_OF_WEEK.contains(token.toLowerCase());
	}

	boolean isMonth(String token) {
		return MONTHS.contains(token.toLowerCase());
	}

	boolean isDateKeyword(String token, ArrayList<String> tokenArray) {
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

	boolean isInThePast(String dateString){
		Date date = new Date();
		if (dateString.split(" ").length == 2) {
			SimpleDateFormat dayAndMonthFormat = new SimpleDateFormat("dd MMM");
			date = convertStringToDate(dateString, dayAndMonthFormat);
		} else
			date = convertStandardDateString(dateString);
		
		Date now = getCurrentDate();
		return now.after(date);
	}

	boolean isValid12HourTime(String token) {
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
		return false;
	}

	boolean isValid24HourTime(String token) {
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
	
}
