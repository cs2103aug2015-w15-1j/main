package main.java.backend.Parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import com.joestelmach.natty.*;

public class Parser {
	//List of commands accepted by the program
	private final ArrayList<String> COMMANDS = new ArrayList<String>( Arrays.asList("add", "addcat", "category", "deadline", "description", 
											   "delete", "done", "event", "exit", "priority", "reminder", "return", "setcol", "showcat", "showe", 
											   "showf", "showt", "sortd", "sortp", "undo", "undone") );
	
	private final ArrayList<String> COMMANDS_NO_PARAMETER = new ArrayList<String>( Arrays.asList("exit", "showcat", "showe", "showf", "showt", 
			   																"sortd", "sortp", "return", "undo") );
	
	private final ArrayList<String> COMMANDS_DOMINATING = new ArrayList<String>( Arrays.asList("addcat", "delete", "done", 
															"exit", "return", "search", "setcol", "showcat", "undo", "undone") );
	
	private final ArrayList<String> COMMANDS_READ_TASKNAME = new ArrayList<String>( Arrays.asList(
			   														  "add", "addcat", "delete", "done", "setcol", "showcat", "undone") );
	
	//The default list of fields and the order in which their parameters are put into result
	private final ArrayList<String> FIELDS_DEFAULT = new ArrayList<String>( Arrays.asList(
			"command", "task", "description", "deadline", "eventStart", "eventEnd", "priority", "reminder", "category") );
	
	//Temporary holder for the current parameter before it's being stored
	private String currParameter = "";
	
	//List of commands that have appeared in the current input
	private ArrayList<String> seenCommands = new ArrayList<String>();
	
	//List of fields that are included in the current result
	private ArrayList<String> fields = new ArrayList<String>(FIELDS_DEFAULT);
	
	//This stores the parameters under their respective fields
	private HashMap<String, String> parameters = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L; {
        put("command",""); put("task", ""); put("description", ""); put("deadline", ""); put("event", ""); 
        put("priority",""); put("reminder", ""); put("category", "");
    }};
    
    private HashMap<String, ArrayList<String>> command_families = new HashMap<String, ArrayList<String>>(){
		private static final long serialVersionUID = 1L; {
        put("add", new ArrayList<String>( Arrays.asList("adds", "newtask")));
        put("priority", new ArrayList<String>( Arrays.asList("pri"))); 
    }};
    
    public ArrayList<String> command_variants = gatherCommandVariants();
    
    private ArrayList<String> gatherCommandVariants(){
    	ArrayList<String> allCommands = new ArrayList<String>();
    	for (ArrayList<String> family: command_families.values()) {
			allCommands.addAll(0, family);
		}
    	return allCommands;
    }
    
    private final ArrayList<String> INDEX_LETTERS = new ArrayList<String>( Arrays.asList("C", "D", "E", "F", "O"));
    
	/**
	 * This method parses the user input and returns its components as an arraylist
	 */
	public ArrayList<String> parseInput(String input){
		ArrayList<String> result = new ArrayList<String>();
		String[] inputTokens = input.split(" ");
		String firstWord = getFirst(inputTokens).toLowerCase();
		
		if (isDominatingCommand(firstWord)) {
			result.add(firstWord);
			
			if (firstWord.equals("setcol")) {
				String catName = mergeTokens(inputTokens, 1, inputTokens.length-1);
				result.add(catName);
				String colour = inputTokens[inputTokens.length-1];
				result.add(colour);
			//if first word is "delete", "done" or "search"
			} else if (!hasNoParameter(firstWord)) {
				String parameter = mergeTokens(inputTokens, 1, inputTokens.length);
				result.add(parameter);
				result = convertNameToIndex(result);
			}  

		} else {
			for (String token: inputTokens) {
				String originalToken = token;
				token = token.toLowerCase();
				if (isCommandVariant(token)) {
					token = getDefaultCommand(token);
				}
				if (isDefaultCommand(token)) {
					putParameter();
					
					if (isSeenCommand(token)) {
						String oldCommand = token;
						mergeToPrevParameter(oldCommand);
						moveToBack(seenCommands, oldCommand);

					} else {
						String command = parameters.get("command");
						if (isDominatingCommand(token) && (!command.isEmpty() || !isLast(inputTokens, token))) {
							appendToParameter(originalToken);
						} else {
							putCommand(token);
						}
					}
					
				} else {
					appendToParameter(originalToken);
				}
			}
			putParameter();
			
			String command = parameters.get("command");
			if (command.equals("add") || command.equals("set")) {
				result = makeLongResult(result);
			} else {
				result = makeShortResult(result);
			}
			
		}	
		seenCommands.clear();
		fields = new ArrayList<String>(FIELDS_DEFAULT);
		parameters.replaceAll((field,parameter) -> "");
		return result;
	}
	
	private String getDefaultCommand(String token) {
    	for (String command: command_families.keySet()) {
    		ArrayList<String> family = command_families.get(command);
			if (family.contains(token)) {
				return command;
			}
		}
    	return null;
	}

	private void appendToParameter(String token) {
		currParameter += token + " ";
	}
	
	/**
	 * This method marks the command as seen and stores it as the main command (if there isn't one) 
	 * @param token
	 */
	private void putCommand(String token) {
		String command = parameters.get("command");
		seenCommands.add(token);
	
		if (command.isEmpty()) {
			parameters.put("command", token);
		} else if (!isReadingTaskName(command)) {
			parameters.put("command", "set");
		}
	}
	
	/**
	 * This method stores the current parameter under its rightful field or appends it to an existing parameter
	 */
	private void putParameter() {
		if (!currParameter.isEmpty()) {
			currParameter = removeEndSpaces(currParameter);
			String lastCommand = getLast(seenCommands);
			String field;
			
			if (lastCommand.isEmpty() || isReadingTaskName(lastCommand)) {
				field = "task";
			} else {
				field = lastCommand;
			}
			
			/*if (lastCommand.equals("deadline") || lastCommand.equals("reminder")) {
				currParameter = parseDate(currParameter);
			}*/
			String parameter = parameters.get(field);
			String command = parameters.get("command");
			if (parameter.isEmpty()) {
				parameters.put(field, currParameter);
			} else if (!command.isEmpty() && !isReadingTaskName(lastCommand) && !isDominatingCommand(currParameter)) {
				parameters.put(field, currParameter);
			} else {
				parameters.put(field, parameter + " " + currParameter);
			}
			
			currParameter = "";
		}
	}

	/**
	 * This method creates a short arrayList for a result that contains 3 or less components
	 */
	private ArrayList<String> makeShortResult(ArrayList<String> result) {
		String command = parameters.get("command");
		String task = parameters.get("task");
		String newField = parameters.get(command);
		if (newField == null) {
			result.addAll( Arrays.asList(command, task) );
		} else {
			if (command.equals("deadline") || command.equals("reminder")) {
				newField = parseDate(newField);
			}
			if (command.equals("event")) {
				String[] eventStartEnd = getStartAndEnd(newField);
				String eventStart = eventStartEnd[0];
				String eventEnd = eventStartEnd[1];
				result.addAll( Arrays.asList(command, task, eventStart, eventEnd) );
			} else {
				result.addAll( Arrays.asList(command, task, newField) );
			}
		}	
		
		result = convertNameToIndex(result);
		
		return result;
	}

	/**
	 * This method creates a long arrayList for a result that contains more than 3 components
	 */
	private ArrayList<String> makeLongResult(ArrayList<String> result) {
		String command = parameters.get("command");
		String deadline = parameters.get("deadline");
		String event = parameters.get("event");
		String reminder = parameters.get("reminder");
		
		if (!reminder.isEmpty()) {
			parameters.put("reminder", parseDate(reminder));
		}
		if (!deadline.isEmpty()) {
			command += "T";
			fields.remove("eventStart");
			fields.remove("eventEnd");
			parameters.put("deadline", parseDate(deadline));
		} else if (!event.isEmpty()) {
			command += "E";
			fields.remove("deadline");
			getStartAndEnd(event);
		} else {
			if (command.equals("add")) {
				command += "F";
			}
			fields.remove("deadline");
			fields.remove("eventStart");
			fields.remove("eventEnd");
		}
		parameters.put("command", command);
		
		for (String field: fields) {
			String para = parameters.get(field);
			result.add(para);
		}
		
		result = convertNameToIndex(result);
		
		return result;
	}

	private ArrayList<String> convertNameToIndex(ArrayList<String> result) {
		String command = result.get(0);
		String task = result.get(1);
		if (!(command.equals("add") || task.isEmpty())) {
			String firstChar = task.substring(0, 1);
			if (INDEX_LETTERS.contains(firstChar) ) {
				String stringIndex = task.substring(1, task.length());
				Integer index = null;
				try {
					index = Integer.parseInt(stringIndex);
				} catch (Exception e) {
					System.out.println("parseIntError");
				}
				if (index != null) {
					result.remove(1);
					result.add(1, firstChar);
					result.add(2, stringIndex);
				}
			}
		}
		return result;
	}

	/**
	 * This method gets the individual start and end date/time of an event and put them into separate fields
	 * @return the start and end date/time in an array
	 */
	private String[] getStartAndEnd(String event) {
		String[] eventTokens = event.split(" to ", 2);
		String eventStart = eventTokens[0];
		String eventEnd = "";
		if (eventTokens.length > 1) {
			eventEnd = removeEndSpaces(eventTokens[1]);
		}
		
		//If eventEnd has no date, set its date as eventStart date
		if (!eventEnd.contains(" ") && !eventEnd.isEmpty()) {
			String startDate = "";
			String[] startTokens = eventStart.split(" ");
			for (int i = 0; i < startTokens.length-1; i++) {
				startDate += startTokens[i] + " ";
			}
			String startTime = startTokens[startTokens.length-1];
			eventStart = startDate + " " + startTime;
			//System.out.println(eventStart);
			
			String[] endTokens = eventEnd.split(" ", 2);
			String endDate = startDate;
			String endTime = endTokens[0];
			eventEnd = endDate + " " + endTime;
		} 
		eventStart = parseDate(removeEndSpaces(eventStart));
		eventEnd = parseDate(removeEndSpaces(eventEnd));
		parameters.put("eventStart", eventStart);
		parameters.put("eventEnd", eventEnd);
		
		String[] result = new String[2];
		result[0] = eventStart;
		result[1] = eventEnd;
		return result;
	}

	private String parseDate(String date) {
		if (date.isEmpty()) {
			return date;
		}
		date = swapDayAndMonth(date);
		
		com.joestelmach.natty.Parser dateParser = new com.joestelmach.natty.Parser();
		DateGroup group = dateParser.parse(date).get(0);
		List<Date> dates = group.getDates();
		String dateString = dates.toString();
		
		dateString = dateString.substring(1, dateString.length()-1); //remove brackets
		dateString = confirmDateIsInFuture(dateString);
		dateString = changeDateFormat(dateString);
		
		return dateString;
	}

	/**
	 * This method swaps the position of day and month (so that it will work for natty)
	 */
	private String swapDayAndMonth(String date) {
		String[] dateTokens = date.split(" ");
		date = "";
		for (String token: dateTokens) {
			String[] ddmmyy = null;
			if (token.contains("/")){
				ddmmyy = token.split("/");
			} else if (token.contains("-")) {
				ddmmyy = token.split("-");
			}
			
			if (ddmmyy != null && ddmmyy.length >= 2) {
				String month = ddmmyy[0];
				String day = ddmmyy[1];
				String year = "";
				date += day + "/" + month;
				if (ddmmyy.length == 3) {
					year = ddmmyy[2];
					date += "/" + year;
				} 
				date += " ";
			} else {
				date += token + " ";
			}
		}
		date = removeEndSpaces(date);
		return date;
	}

	/**
	 * This method confirms that the date set by the parser is in the future
	 * If it's not, either add one year to the date
	 */
	private String confirmDateIsInFuture(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy"); 
		long dateMilli = -1;
		long nowMilli = getCurrentDateLong();
		Date date = new Date();
		try {
			date = formatter.parse(dateString);
			dateMilli = date.getTime();
		} catch (ParseException e) {
			System.out.println("parseError");
		}
		
		if (dateMilli != -1 && dateMilli < nowMilli) {
			System.out.println(dateString);
			String[] dateStringTokens = dateString.split(" ");
			long dayMonth = getDateLong(dateStringTokens[1] + " " + dateStringTokens[2]);
			int year = Integer.parseInt(getLast(dateStringTokens));
			int currYear = getCurrentYear();
			
			if (year < currYear) {
				year = currYear;
				dateString = "";
				for (int i = 0; i < dateStringTokens.length-1; i++) {
					dateString += dateStringTokens[i] += " ";
				}
				dateString += year;
			}
			//System.out.println(dateString);
			
			try {
				dateMilli = formatter.parse(dateString).getTime();
			} catch (ParseException e) {
				System.out.println("parseError");
			}
			if (year == currYear) {
				if (dayMonth < nowMilli) {
					//dateMilli = plusOneDay(dateMilli);
					if (dateMilli < nowMilli){
						date = plusOneYear(date);
						dateString = date.toString();
					} else {
						dateString = formatter.format(dateMilli);
					}
				}
			}
			//System.out.println(dateString);
		}
		return dateString;
	}

	private String changeDateFormat(String dateString) {
		SimpleDateFormat nattyFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		SimpleDateFormat standardFormat = new SimpleDateFormat("EEE, dd MMM hh:mma yyyy");
		Date tempDate = null;
		try {
			tempDate = nattyFormat.parse(dateString);
		} catch (Exception e) {
			System.out.println("parseError");
		}
		dateString = standardFormat.format(tempDate);
		dateString = dateString.substring(0, dateString.length()-5);
		return dateString;
	}

	private Date plusOneYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, 1);
		date = c.getTime();
		return date;
	}

	/*private long plusOneDay(long dateMilli) {
		dateMilli += (1000 * 60 * 60 * 24);
		return dateMilli;
	}*/

	private long getDateLong(String date){
		SimpleDateFormat sdfDate = new SimpleDateFormat("MMM dd");
	    Date parseDate;
		try {
			parseDate = sdfDate.parse(date);
			return parseDate.getTime();
		} catch (ParseException e) {
			System.out.println("parseError");
		}
		return 0;
	}
	
	private long getCurrentDateLong() {
		Date now = new Date();
		long milli = now.getTime();
		return milli;
	}
	
	/*private String getCurrentDate() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("MMM dd");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
		return strDate;
	}*/
	
	private int getCurrentYear() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
		return Integer.parseInt(strDate);
	}
	
	/**
	 * This method merges all the tokens between the startIndex (including) and endIndex (excluding)
	 * @return the merged tokens as a string
	 */
	private String mergeTokens(String[] inputTokens, int startIndex, int endIndex) {
		String parameter = "";
		for (int i = startIndex; i < endIndex; i++) {
			String token = inputTokens[i];
			parameter += token + " ";
		}
		return removeEndSpaces(parameter);
	}

	/**
	 * This method takes in an old command and merges the command and its parameter to the previous parameter
	 * @param oldCommand
	 */
	private void mergeToPrevParameter(String oldCommand) {
		String prevField;
		if (isFirst(seenCommands, oldCommand)) {
			prevField = "task";
		}
		else {
			prevField = getPrevious(seenCommands, oldCommand);
			if (prevField.equals("add")) {
				prevField = "task";
			}
		}	
		
		String prevParameter = parameters.get(prevField);
		String oldParameter = parameters.get(oldCommand);
		if (oldParameter == null) {
			oldParameter = "";
		}
		
		if (prevParameter.isEmpty() && oldParameter.isEmpty()) {
			parameters.put(prevField, oldCommand);
		} else if (oldParameter.isEmpty()) {
			parameters.put(prevField, prevParameter + " " + oldCommand);
		} else if (prevParameter.isEmpty()) {
			parameters.put(prevField, oldCommand + " " + oldParameter);
		} else {
			parameters.put(prevField, prevParameter + " " + oldCommand + " " + oldParameter);
		}
	}

	/**
	 * This method moves a token to the back of its arrayList
	 */
	private void moveToBack(ArrayList<String> arrayList, String token) {
		int index = arrayList.indexOf(token);
		arrayList.remove(index);
		arrayList.add(token);
	}

	/**
	 * This method removes any spaces that are in front or at the back of the token
	 */
	private String removeEndSpaces(String token) {
		if (token.startsWith(" ")) {
			token =  token.substring(1, token.length());
		} 	
		if (token.endsWith(" ")) {
			return token.substring(0, token.length()-1);
		} else {
			return token;
		}
	}

	private boolean isDefaultCommand(String token){
		return COMMANDS.contains(token);
	}
	
	private boolean isCommandVariant(String token){
		return command_variants.contains(token);
	}
	
	private boolean isDominatingCommand(String token){
		return COMMANDS_DOMINATING.contains(token);
	}
	
	private boolean hasNoParameter(String token){
		return COMMANDS_NO_PARAMETER.contains(token);
	}
	
	/*private boolean hasTextParameter(String token){
		return COMMANDS_WITH_TEXT_PARAMETER.contains(token);
	}*/
	
	private boolean isReadingTaskName(String token){
		return COMMANDS_READ_TASKNAME.contains(token);
	}
	
	private boolean isSeenCommand(String token) {
		return seenCommands.contains(token);
	}
	
	/*private boolean isFirst(String[] array, String token) {
		String firstWord = getFirst(array);
		return firstWord.equals(token);
	}*/
	
	private boolean isFirst(ArrayList<String> list, String token) {
		String firstWord = getFirst(list);
		return firstWord.equals(token);
	}
	
	private boolean isLast(String[] array, String token) {
		String lastWord = getLast(array);
		return lastWord.equals(token);
	}

	/*private boolean isLast(ArrayList<String> list, String token) {
		String lastWord = getLast(list);
		return lastWord.equals(token);
	}*/
	
	private String getFirst(String[] array){
		return array[0];
	}
	
	private String getFirst(ArrayList<String> arrayList){
		return arrayList.get(0);
	}
	
	private String getLast(String[] array){
		if (array.length == 0) {
			return "";
		}
		return array[array.length-1];
	}
	
	private String getLast(ArrayList<String> arrayList){
		if (arrayList.isEmpty()) {
			return "";
		}
		return arrayList.get(arrayList.size()-1);
	}
	
	private String getPrevious(ArrayList<String> list, String token) {
		return list.get( list.indexOf(token)-1 );
	}
	
}
