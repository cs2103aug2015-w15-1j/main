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
 * Specialized parser that parses string to standard date format
 * @@author A0121795B
 */
public class DateParser extends ParserSkeleton{
	
	//List of days in a week
	private final ArrayList<String> DAYS_OF_WEEK = new ArrayList<String>( Arrays.asList(
	"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday" ) );
	
	//List of months and their abbreviations
	private final ArrayList<String> MONTHS = new ArrayList<String>( Arrays.asList(
	"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december", 
	"jan", "feb", "mar", "apr", "jun", "jul", "aug", "sep", "oct", "nov", "dec") );
	
	//Categorise the months by the number of days
	private final ArrayList<String> MONTHS_WITH_31_DAYS = new ArrayList<String>( Arrays.asList(
	"january", "march", "may", "july", "august", "october", "december") );
	private final ArrayList<String> MONTHS_WITH_30_DAYS = new ArrayList<String>( Arrays.asList(
	"april", "june", "september", "november") );
	private final String FEBRUARY = "february";
	
	//Abbreviations or numerical forms of the months
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
	
    //Date/time formats that are used by DateParser
	private final SimpleDateFormat DATEFORMAT_NATTY = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	private final SimpleDateFormat DATEFORMAT_STANDARD = new SimpleDateFormat("EEE, dd MMM yy, hh:mma");
	private final SimpleDateFormat DATEFORMAT_NO_MINUTE = new SimpleDateFormat("EEE, dd MMM yy, hha");
	private final SimpleDateFormat DATEFORMAT_NO_TIME = new SimpleDateFormat("EEE, dd MMM yy");
	private final SimpleDateFormat DATEFORMAT_DAY_AND_MONTH_ONLY = new SimpleDateFormat("dd MMM");
	private final SimpleDateFormat DATEFORMAT_YEAR_ONLY = new SimpleDateFormat("yyyy");
	private final SimpleDateFormat TIMEFORMAT_STANDARD  = new SimpleDateFormat("hh:mma");
	private final SimpleDateFormat TIMEFORMAT_NO_MINUTE  = new SimpleDateFormat("hha");
	
	//The minimum abbreviation length accepted (eg. abbreviation of 'monday' can be 'mon' but not 'mo')
	private final int MIN_LENGTH_OF_ABBR = 3;
    
	//Special characters/words that are noted by DateParser and recognised by NATTY
	private final String DATE_SEPARATOR_COMMA = ", ";
	private final String DATE_SEPARATOR_SLASH = "/";
	private final String DATE_SEPARATOR_HYPHEN = "-";
	private final String TIME_SEPARATOR_COLON = ":";
	private final String TIME_SEPARATOR_DOT = ".";
	private final String PERIOD_AM = "am";
	private final String PERIOD_PM = "pm";
	private final String DATE_KEYWORD_LATER = "later";
	private final String DATE_KEYWORD_NEXT = "next";
	private final String DATE_KEYWORD_TODAY = "today";
	private final String DATE_KEYWORD_TOMORROW = "tomorrow";
	private final String DATE_KEYWORD_TOMORROW_ABBR = "tmr";
	private final String TIME_KEYWORD_HOUR = "hour";
	private final String TIME_KEYWORD_MINUTE = "minute";
	ArrayList<String> DAY_RELATIVE = new ArrayList<String>( 
			Arrays.asList(DATE_KEYWORD_TODAY, DATE_KEYWORD_TOMORROW, DATE_KEYWORD_TOMORROW_ABBR) );
	
    //A natural language date parser. Taken from http://natty.joestelmach.com/  
	private final com.joestelmach.natty.Parser NATTY = new com.joestelmach.natty.Parser();
	
	public DateParser() {
		//Force NATTY to set up upon DateParser creation by running NATTY once
    	String pi = "Mar 14 15 9.26pm";
    	parseDateWithNatty(pi);
	}
	
	/**
	 * This method parses a string (that resembles a date) into a standardized date format
	 */
	String parseDate(String date) {
		if (date.isEmpty()) {
			return date;
		}
	
		date = swapDayAndMonth(date);
		date = removeLater(date);
		date = addSpaceBetweenDayAndMonth(date);
		date = addFirstTwoDigitsToYear(date);
		
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
			eventStart = getDayMonthAndYear(parsedStart) + DATE_SEPARATOR_COMMA + DEFAULT_STARTTIME;
		} else {
			eventStart = parsedStart;
		}
		return new ArrayList<String>( Arrays.asList(STATUS_OKAY, eventStart));
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
		
		return new ArrayList<String>( Arrays.asList(STATUS_OKAY, eventEnd));
	}

	ArrayList<String> isInvalidDate(String dateString){
		if (dateString.isEmpty()) {
			return new ArrayList<String>(Arrays.asList(STATUS_OKAY));
		}
		
		ArrayList<String> dateBoundCheck = checkDateBound(dateString);
		if (isErrorStatus(dateBoundCheck)) {
			return dateBoundCheck;
		}
		
		try {
			dateString = swapDayAndMonth(dateString);
			dateString = removeLater(dateString);
			dateString = addSpaceBetweenDayAndMonth(dateString);
			String parsedDate = parseDateWithNatty(dateString);
			parsedDate = removeMinuteIfZero(parsedDate);
			
			String parsedTime = getTime(parsedDate);
			String timeString = getTimeFromString(dateString);
			if (isNotMatchingTimes(parsedTime, timeString)) {
				return makeErrorResult(ERROR.INVALID_TIME, timeString);
			}
			if (!isValidHour(getHourFromTimeString(timeString))) {
				return makeErrorResult(ERROR.INVALID_TIME, timeString);
			}
			
			return new ArrayList<String>(Arrays.asList(STATUS_OKAY));
			
		} catch (Exception e){
			return makeErrorResult(ERROR.INVALID_DATE, dateString);
		}
	}

	boolean isDayOfWeek(String token) {
		token = removeEndSpacesOrBrackets(token.toLowerCase());
		if (DAYS_OF_WEEK.contains(token)){
			return true;
		}
		if (token.length() >= MIN_LENGTH_OF_ABBR) {
			for (String day: DAYS_OF_WEEK){
				if (day.startsWith(token)) {
					return true;
				}
			}
		}
		return false;
	}
	
	boolean isMonth(String token) {
		if (isNumber(token) && getDateSymbol(token).isEmpty() && hasOnlyOneWord(addSpaceBetweenDayAndMonth(token))) {
			return false;
		}
		token = convertMonthToDefault(token);
		if (MONTHS.contains(token)){
			return true;
		}
		for (String month: MONTHS) {
			if (hasMonth(token, month)) {
				return true;
			}
		}
		return false;
	}

	boolean hasNoDate(String dateString) {
		if (canBeSplit(dateString, DATE_SEPARATOR_SLASH) || canBeSplit(dateString, DATE_SEPARATOR_HYPHEN)) {
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
	
	String parseAndGetDayAndMonth(String dateString) {
		dateString = parseDate(dateString);
		return getDayAndMonth(dateString);
	}

	private String parseDateWithNatty(String date) {
		return NATTY.parse(date).get(0).getDates().toString();
	}

	private String standardizeDateFormat(String dateString) {
		dateString = removeEndSpacesOrBrackets(dateString);
		Date tempDate = convertStringToDate(dateString, DATEFORMAT_NATTY);
		return DATEFORMAT_STANDARD.format(tempDate);
	}

	private Date convertStringToDate(String dateString, SimpleDateFormat sdf){
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			printParsingError(DATATYPE_STRING, dateString, DATATYPE_DATE);
			e.printStackTrace();
		}
		return date;
	}

	private Date convertStandardDateString(String dateString){
		Date date;
		if (hasMinute(dateString)) {
			date = convertStringToDate(dateString, DATEFORMAT_STANDARD);
		} else {
			date = convertStringToDate(dateString, DATEFORMAT_NO_MINUTE);
		}
		
		return date;
	}

	private Date convertStandardTimeString(String timeString){
		Date time;
		if (hasMinute(timeString)) {
			time = convertStringToDate(timeString, TIMEFORMAT_STANDARD);
		} else {
			time = convertStringToDate(timeString, TIMEFORMAT_NO_MINUTE);
		}
		
		return time;
	}

	private int convertTimeStringToInt(String timeString){
		try {
			return Integer.parseInt(timeString); 
		} catch (NumberFormatException e) {
			//printParsingError(DATATYPE_STRING, timeString, DATATYPE_INTEGER);
			//e.printStackTrace();
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
					mmddyyDate += month + DATE_SEPARATOR_SLASH + day;
					if (ddmmyyDate.length == 3) {
						year = ddmmyyDate[2];
						mmddyyDate += DATE_SEPARATOR_SLASH + year;
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

	private String addFirstTwoDigitsToYear(String date) {
		String[] dateTokens = date.split(" ");
		String day = "";
		for (int i = 0; i < dateTokens.length; i++){
			String token = dateTokens[i];
			if (isNumber(token)) {
				if (day.isEmpty()) {
					day = token;
				} else if (token.length() != 4 && i != dateTokens.length-1 
						&& !getNext(dateTokens, i).toLowerCase().equals(PERIOD_AM) 
						&& !getNext(dateTokens, i).toLowerCase().equals(PERIOD_PM)){
					dateTokens[i] = getCurrentYearFirstTwoDigits() + token;
					break;
				}
			}
		}
		return mergeTokens(dateTokens, 0, dateTokens.length);
	}

	private String removeLater(String date) {
		String[] dateTokens = date.split(" ");
		date = "";
		for (String token: dateTokens) {
			if (!token.equalsIgnoreCase(DATE_KEYWORD_LATER)) {
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
			int currYear = getCurrentYearLastTwoDigits();
			if (year < currYear || year >= currYear + 5) {
				date = setToCurrentYear(date);
			}
			if (year <= currYear) {
				if (isInThePast(date)) {
					date = plusOneYear(date);
				}
			}
		}
		return date;
	}

	private String removeMinuteIfZero(String dateString) {
		if (dateString.contains(TIME_SEPARATOR_COLON)) {
			String time = getTime(dateString);
			int hour = getHour(time);
			int minute = getMinute(time);
			String period = getPeriod(time);
			
			if (minute == 0) {
				time = hour + period;
			} else {
				if (minute < 10) {
					time = hour + TIME_SEPARATOR_COLON + ZERO + minute + period;
				} else {
					time = hour + TIME_SEPARATOR_COLON + minute + period;
				}
			}
			
			String[] dateTokens = dateString.split(DATE_SEPARATOR_COMMA);
			return getFirst(dateTokens) + DATE_SEPARATOR_COMMA + getSecond(dateTokens) + DATE_SEPARATOR_COMMA + time;
		} 
		return dateString;
	}

	private String makeEventEndComplete(String eventStart, String eventEnd) {
		String startDate = getDayMonthAndYear(eventStart);
		String startTime = getTime(eventStart);
		
		if (eventEnd.isEmpty()) {
			eventEnd = startDate + DATE_SEPARATOR_COMMA + DEFAULT_ENDTIME;
		} else if (hasNoDate(eventEnd)) {
			String endTime = eventEnd;
			if (isStartTimeNotBeforeEndTime(startTime, endTime)) {
				startDate = plusOneDay(startDate, DATEFORMAT_NO_TIME);
			} 
			eventEnd = startDate + DATE_SEPARATOR_COMMA + endTime;	
		} else if (hasNoTime(eventEnd)) {
			String endDate = eventEnd;
			eventEnd = endDate + DATE_SEPARATOR_COMMA + DEFAULT_ENDTIME;
		}
		
		if (isStartDateAfterEndDate(eventStart, eventEnd)) {
			eventEnd = plusOneYear(eventEnd);
		}
		return eventEnd;
	}

	private Date adjustCalendar(int field, Date date, int length) {
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(field, length);
		Date newDate = c.getTime();
		return newDate;
	}
	
	private String plusOneDay(String dateString, SimpleDateFormat sdf) {
		Date date = new Date();
		date = convertStringToDate(dateString, sdf);
		date = adjustCalendar(Calendar.DATE, date, 1);
		return sdf.format(date);
	}
	
	private String plusOneYear(String dateString) {
		Date date = convertStandardDateString(dateString);
		date = adjustCalendar(Calendar.YEAR, date, 1);
		return removeMinuteIfZero(standardizeDateFormat(date.toString()));
	}

	private String setToCurrentYear(String dateString) {
		String currYear = Integer.toString(getCurrentYearLastTwoDigits());
		String[] dateTokens = dateString.split(DATE_SEPARATOR_COMMA);
		
		String ddMMMyy = getSecond(dateTokens);
		String[] ddMMMyyTokens = ddMMMyy.split(" ");
		ddMMMyy = getFirst(ddMMMyyTokens) + " " + getSecond(ddMMMyyTokens) + " " + currYear;
		
		String ddMMM = getFirst(ddMMMyyTokens) + " " + getSecond(ddMMMyyTokens);
		String EEE = getDayOfWeek(parseDate(ddMMM + " " + getLast(dateTokens)));
		
		return EEE + DATE_SEPARATOR_COMMA + ddMMMyy + DATE_SEPARATOR_COMMA + getLast(dateTokens);
	}

	private String getDateSymbol(String date) {
		if (date.contains(DATE_SEPARATOR_SLASH)){
			return DATE_SEPARATOR_SLASH;
		} else if (date.contains(DATE_SEPARATOR_HYPHEN)) {
			return DATE_SEPARATOR_HYPHEN;
		} else {
			return "";
		}
	}

	private String getDayOfWeek(String dateString) {
		String[] dateTokens = dateString.split(DATE_SEPARATOR_COMMA);
		return getFirst(dateTokens);
	}
	
	private String getDayAndMonth(String dateString) {
		String[] dateTokens = dateString.split(DATE_SEPARATOR_COMMA);
		String ddMMMyy = getSecond(dateTokens);
		String[] ddMMMyyTokens = ddMMMyy.split(" ");
		return getFirst(ddMMMyyTokens) + " " + getSecond(ddMMMyyTokens);
	}
	
	private String getMonth(String dateString) {
		String[] dateTokens = dateString.split(DATE_SEPARATOR_COMMA);
		String ddMMMyy = getSecond(dateTokens);
		String[] ddMMMyyTokens = ddMMMyy.split(" ");
		return getSecond(ddMMMyyTokens);
	}

	private int getYear(String dateString) {
		String[] dateTokens = dateString.split(DATE_SEPARATOR_COMMA);
		String ddMMMyy = getSecond(dateTokens);
		String[] ddMMMyyTokens = ddMMMyy.split(" ");
		return Integer.parseInt(getLast(ddMMMyyTokens));
	}

	private String getDayMonthAndYear(String dateString) {
		String[] dateTokens = dateString.split(DATE_SEPARATOR_COMMA);
		return getFirst(dateTokens) + DATE_SEPARATOR_COMMA + getSecond(dateTokens);
	}

	private String getTime(String dateString) {
		String[] dateTokens = dateString.split(DATE_SEPARATOR_COMMA);
		return getLast(dateTokens);
	}

	private String getTimeSymbol(String timeString) {
		if (timeString.contains(TIME_SEPARATOR_COLON)){
			return TIME_SEPARATOR_COLON;
		} else if (timeString.contains(TIME_SEPARATOR_DOT)) {
			return "\\" + TIME_SEPARATOR_DOT;
		} else {
			return "";
		}
	}

	private int getHour(String timeString) {
		String[] timeTokens;
		timeTokens = timeString.split(TIME_SEPARATOR_COLON);
		
		int hour = convertTimeStringToInt(getFirst(timeTokens));
		return hour;
	}

	private int getMinute(String timeString) {
		String[] timeTokens;
		timeTokens = timeString.split(TIME_SEPARATOR_COLON);
		if (timeTokens.length > 1) {
			String minuteWithPeriod = getSecond(timeTokens);
			String[] minuteToken;
			if (isAM(timeString)) {
				minuteToken = minuteWithPeriod.toLowerCase().split(PERIOD_AM);
			} else {
				minuteToken = getSecond(timeTokens).toLowerCase().split(PERIOD_PM);
			}
			return convertTimeStringToInt(getFirst(minuteToken));
		} else {
			return 0;
		}
	}

	private String getPeriod(String timeString){
		if (isAM((timeString))) {
			return PERIOD_AM;
		}
		if (isPM((timeString))) {
			return PERIOD_PM;
		}
		return STATUS_ERROR;
	}

	private Date getCurrentDate() {
		Date now = new Date();
		return now;
	}
	
	private String getCurrentYearFirstTwoDigits() {
	    Date now = new Date();
	    String strDate = DATEFORMAT_YEAR_ONLY.format(now);
	    return strDate.substring(0, 2);
	}
	
	private int getCurrentYearLastTwoDigits() {
	    Date now = new Date();
	    String strDate = DATEFORMAT_YEAR_ONLY.format(now);
	    strDate = strDate.substring(2, strDate.length());
		return Integer.parseInt(strDate);
	}
	
	private int getDayfromDateString(String token) {
		String day = getFirst(token.split(getDateSymbol(token)));
		try {
			return Integer.parseInt(day);
		} catch (NumberFormatException e) {
			printParsingError(FREQUENCY_DAY, day, DATATYPE_INTEGER);
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
				if (token.equalsIgnoreCase(PERIOD_AM) || token.equalsIgnoreCase(PERIOD_PM)) {
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
			String hour = getFirst(timeString.toLowerCase().split(PERIOD_AM));
			return removeEndSpacesOrBrackets(hour);
		}
		if (isPM(timeString)) {
			String hour = getFirst(timeString.toLowerCase().split(PERIOD_PM));
			return removeEndSpacesOrBrackets(hour);
		}
		return timeString;
	}

	private boolean isStartDateAfterEndDate(String startDateString, String endDateString){
		Date startDate = convertStandardDateString(startDateString);
		Date endDate = convertStandardDateString(endDateString);
		return startDate.after(endDate);
	}

	private boolean isStartTimeNotBeforeEndTime(String startTime, String endTime){
		Date startTimeDate = convertStandardTimeString(startTime);
		Date endTimeDate = convertStandardTimeString(endTime);
		return !startTimeDate.before(endTimeDate);
	}

	private boolean isInThePast(String dateString){
		Date date = new Date();
		if (dateString.split(" ").length == 2) {
			date = convertStringToDate(dateString, DATEFORMAT_DAY_AND_MONTH_ONLY);
		} else
			date = convertStandardDateString(dateString);
		
		Date now = getCurrentDate();
		return now.after(date);
	}

	private boolean isDayMonthFormat(String token) {
		return !getDateSymbol(token).isEmpty() && !getNumber(token).isEmpty();
	}

	private boolean isTimeFormat(String time){
		return isAM(time) || isPM(time) || !getTimeSymbol(time).isEmpty();
	}

	private boolean isDateKeyword(String token, ArrayList<String> tokenArray) {
		if (DAY_RELATIVE.contains(token.toLowerCase())) {
			return true;
		} 
		
		if (DATE_FREQUENCY.contains(removePluralOrPastTense(token.toLowerCase()))) {
			String previousToken = getPrevious(tokenArray, token);
			if (previousToken != null) {
				if (isNumber(previousToken) || previousToken.equalsIgnoreCase(DATE_KEYWORD_NEXT)) {
					return true;
				}
			}
		} 
		
		return false;
	}

	private boolean isValid12HourTime(String token, ArrayList<String> tokens) {
		String period;
		if (isAM(token)) {
			period = PERIOD_AM;
		} else if (isPM(token)) {
			period = PERIOD_PM;
		} else {
			return false;
		}
		String[] timeTokens = token.toLowerCase().split(period);
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
		if (token.contains(TIME_SEPARATOR_COLON)) {
			timeSymbol = TIME_SEPARATOR_COLON;
		} else if (token.contains(TIME_SEPARATOR_DOT)) {
			timeSymbol = TIME_SEPARATOR_DOT;
		} else {
			return false;
		}
		
		String[] timeTokens = token.split(timeSymbol);
		if (timeTokens.length == 2){
			String hour = getFirst(timeTokens);
			String minute = getLast(timeTokens);
			if (isAM(token)) {
				minute = getFirst(minute.split(PERIOD_AM));
			}
			if (isPM(token)) {
				minute = getFirst(minute.split(PERIOD_PM));
			}
			if (isValidHour(hour) && isValidMinute(minute)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAM(String time){
		return time.toLowerCase().endsWith(PERIOD_AM);
	}

	private boolean isPM(String time){
		return time.toLowerCase().endsWith(PERIOD_PM);
	}

	private boolean isValidHour(String hour){
		int minHour = 0;
		int maxHour = 23;
		if (hour.isEmpty()) {
			return true;
		}
		try {
			int min = Integer.parseInt(hour);
			return min >= minHour && min <= maxHour;
		} catch (NumberFormatException e) {
			printParsingError(TIME_KEYWORD_HOUR, hour, DATATYPE_INTEGER);
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean isValidMinute(String minute){
		try {
			int min = Integer.parseInt(minute);
			return min >= 0 && min <= 60;
		} catch (NumberFormatException e) {
			printParsingError(TIME_KEYWORD_MINUTE, minute, DATATYPE_INTEGER);
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean isNotMatchingTimes(String parsedTime, String timeString) {
		return !hasMinute(parsedTime) && (hasMinute(timeString) && !timeString.endsWith(ZERO));
	}

	private boolean hasMinute(String time){
		return canBeSplit(time, TIME_SEPARATOR_COLON);
	}

	private boolean hasMonth (String token, String month){
		return (token.startsWith(month) || token.endsWith(month));
	}

	private boolean hasNoSuchDayInMonth(int day, String month) {
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

	private String addSpaceBetweenDayAndMonth(String dateString) {
		String[] dateTokens = dateString.split(" ");
		findMonth:
		for (int i = 0; i < dateTokens.length; i++) {
			String token = dateTokens[i];
			token = token.toLowerCase();
			for (String month: MONTHS) {
				if (hasMonth(token, month) && !getNumber(token).isEmpty()){
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
			if (isDayMonthFormat(token)) {
				day = getDayfromDateString(token);
				month = getMonthfromDateString(token);
				if (hasNoSuchDayInMonth(day, month)) {
					return makeErrorResult(ERROR.INVALID_DAYOFMONTH, Integer.toString(day) + DATE_SEPARATOR_SLASH + month);
				}
			}
			if (isMonth(token)) {
				token = addSpaceBetweenDayAndMonth(token);
				if (canBeSplit(token, " ")) {
	
					day = convertStringToInt(getFirst(token));
					month = getLast(token);
				} else {
					String prev = getPrevious(dateTokens, i);
					month = token;
					if (isNumber(prev)) {
						day = convertStringToInt(prev);
						if (hasNoSuchDayInMonth(day, month)) {
							return makeErrorResult(ERROR.INVALID_DAYOFMONTH, Integer.toString(day) + " " + month);
						}
					} else {
						day = convertStringToInt(getNext(dateTokens, i));
						if (hasNoSuchDayInMonth(day, month)) {
							return makeErrorResult(ERROR.INVALID_DAYOFMONTH, month + " " + Integer.toString(day));
						}
					}
					
				}
			}
		}
		return new ArrayList<String>( Arrays.asList(STATUS_OKAY));
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
	
	@Override
	ArrayList<String> makeErrorResult(ERROR error, String token) {
		ArrayList<String> result = new ArrayList<String>(); 
		result.add(STATUS_ERROR);
		
		switch (error) {
			case INVALID_DATE:
				result.add("InvalidDateError: '" + token + "' is not an acceptable date format");
				break;
			case INVALID_TIME:
				result.add("InvalidTimeError: '" + token + "' is not an acceptable time format");
				break;
			case INVALID_DAYOFMONTH:
				String month = getMonthfromDateString(token);
				String defaultMonth = convertMonthToDefault(month);
				result.add("InvalidDayOfMonthError: The date '" + token + "' does not exist "
						+ "(" + capitalize(defaultMonth) + " only has " + getMaxDay(defaultMonth) + " days!)");
				break;
			default:
				break; 
		}
		return result;
	}
}
