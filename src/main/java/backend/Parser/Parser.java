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
	//List of all command words accepted by the program
	private final ArrayList<String> COMMANDS = new ArrayList<String>( Arrays.asList(
	"add", "addcat", "category", "deadline", "description", "delete", "done", "event", 
	"every", "exit", "priority", "reminder", "rename", "reset", "search", "setcol", "showcat", 
	"show", "showE", "showF", "showT", "sort", "sortD", "sortN", "sortP", "undo", "undone") );
	
	//Commands that work just by typing the command word (without additional content)
	private final ArrayList<String> COMMANDS_NO_CONTENT = new ArrayList<String>( Arrays.asList(
	"exit", "showE", "showF", "showT", "sortD", "sortN", "sortP", "undo") );
	
	//Commands that if appear first, will prevent other command keywords from having effect
	private final ArrayList<String> COMMANDS_DOMINATING = new ArrayList<String>( Arrays.asList(
	"addcat", "delete", "done", "every", "reset", "search", "setcol", "show", "showcat", "sort", "undone") );
	
	//Commands that create a new item
	private final ArrayList<String> COMMANDS_ADD_STUFF = new ArrayList<String>( Arrays.asList(
	"add", "addcat") );
	
	//Commands that can accept any amount of words
	private final ArrayList<String> COMMANDS_NEED_WORDS = new ArrayList<String>( 
	Arrays.asList("add", "addcat", "category", "description", "search") );
	
	//Commands that only accept an index
	private final ArrayList<String> COMMANDS_NEED_INDEX = new ArrayList<String>( 
	Arrays.asList("delete", "done", "reset", "undone", "showcat") );
	
	//Command words that indicate that the command is one-shot
	private final ArrayList<String> COMMANDS_ONE_SHOT = new ArrayList<String>( 
	Arrays.asList("add", "set") );	
	
	//Command fields that can be edited/resetted
	private final ArrayList<String> COMMANDS_CAN_EDIT = new ArrayList<String>( 
	Arrays.asList("all", "description", "deadline", "event", "priority", "reminder", "category") );	
	
	//The default list of fields and the order in which their contents are put into result
	private final ArrayList<String> FIELDS_DEFAULT = new ArrayList<String>( Arrays.asList(
	"command", "name", "description", "deadline", "eventStart", "eventEnd", "priority", "reminder", "category", "rename") );
	
	//How often a recurring task can recur
	private final ArrayList<String> RECUR_FREQUENCY = new ArrayList<String>( Arrays.asList(
	"day", "week", "month", "year") );
	
	//List of fields that are used in the current result
	private ArrayList<String> fields = new ArrayList<String>(FIELDS_DEFAULT);
	
	//Holds the merged tokens until they are stored into field content
	private String growingToken = "";
	
	//List of commands that have appeared in the current input
	private ArrayList<String> seenCommands = new ArrayList<String>();
	
	//Stores the fields and their respective contents
	private HashMap<String, String> fieldContent = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L; {
        put("command",""); put("name", ""); put("description", ""); put("deadline", ""); put("event", ""); 
        put("priority",""); put("reminder", ""); put("category", ""); put("every", ""); put("rename", "");
    }};
    
    //Contains the variants or short forms of some of the commands
    private HashMap<String, ArrayList<String>> command_families = new HashMap<String, ArrayList<String>>(){
		private static final long serialVersionUID = 1L; {
		put("category", new ArrayList<String>( Arrays.asList("cat")));
		put("deadline", new ArrayList<String>( Arrays.asList("by", "dea")));
		put("delete", new ArrayList<String>( Arrays.asList("del")));
        put("description", new ArrayList<String>( Arrays.asList("des")));
        put("event", new ArrayList<String>( Arrays.asList("from"))); 
        put("every", new ArrayList<String>( Arrays.asList("recur"))); 
        put("priority", new ArrayList<String>( Arrays.asList("pri")));
        put("reminder", new ArrayList<String>( Arrays.asList("rem")));
    }};
    
	/**
	 * This method parses the user input and returns an ArrayList of string tokens
	 */
	public ArrayList<String> parseInput(String input){
		ArrayList<String> result = new ArrayList<String>(); 
		
		String[] inputTokens = input.split(" ");
		String firstWordOriginal = getFirst(inputTokens);
		String firstWord = getDefaultCommand(firstWordOriginal);
		
		if (noNeedContent(firstWord)) {
			result.add(firstWord);
			return result;
		} else if (isCommand(firstWord) && inputTokens.length == 1) {
			result = makeErrorResult(result, "EmptyFieldError", firstWord);
		} else if (!(isCommand(firstWord) || isNumber(firstWord))) {
			result = makeErrorResult(result, "UnrecognisedFirstWordError", firstWordOriginal);
		} else if (isDominatingCommand(firstWord)) {
			result.add(firstWord);
			String content = mergeTokens(inputTokens, 1, inputTokens.length);
			if (firstWord.equals("show")) {
				String taskType = getTaskType(content);
				if (taskType.equals("ERROR")) {
					result = makeErrorResult(result, "InvalidTaskTypeError", content);
				} else {
					result.set(0, firstWord+taskType);
				}
			} else if (firstWord.equals("sort")) {
				String field = getSortField(getDefaultCommand(content));
				if (field.equals("ERROR")) {
					result = makeErrorResult(result, "InvalidSortFieldError", content);
				} else {
					result.set(0, firstWord+field);
				}
			} else if (needWords(firstWord)) { //if first word is addcat or search
				result.add(content);
			} else if (needIndex(firstWord)) { //if first word is delete, done, or undone 
				String nextWord = getFirst(content);
				if (isNumber(nextWord)) {
					result.add(nextWord);
				} else {
					result = makeErrorResult(result, "IndexError", content);
				}
			}

		} else {
			for (int i = 0; i < inputTokens.length; i++) {
				String token = inputTokens[i];
				String originalToken = token;
				//token = token.toLowerCase();
				token = getDefaultCommand(token);
				if (isCommand(token) && !noNeedContent(token)) {
					putToken();
					String lastCommand = getLast(seenCommands);
					
					if (isSeenCommand(token)) {
						if (needWords(lastCommand)) {
							addToFieldContent(lastCommand, originalToken);
						} else {
							result = makeErrorResult(result, "DuplicateCommandError", originalToken);
							break;
						}

					} else {
						String command = fieldContent.get("command");
						if (!command.isEmpty() && isDominatingCommand(token)) {
							appendToken(originalToken);
						} else {
							if (!lastCommand.isEmpty()) {
								String content;
								if (isAddStuff(lastCommand)) {
									content = fieldContent.get("name");
								} else {
									content = fieldContent.get(lastCommand);
								}
								if (content.isEmpty()) {
									result = makeErrorResult(result, "EmptyFieldError", lastCommand);
									break;
								}
							}
							putCommand(token);
							//if command is delete, done or reset, ends here
							if (isDominatingCommand(token) && !token.equals("every")) { 
								if (token.equals("reset")) {
									if (isLast(inputTokens, token)) {
										result = makeErrorResult(result, "EmptyFieldError", token);
									} else {
										String content = mergeTokens(inputTokens, i+1, inputTokens.length);
										String field = getDefaultCommand(content);
										if (isLast(inputTokens, content) && canEdit(field)) {
											fieldContent.put("reset", field);
										} else {
											result = makeErrorResult(result, "InvalidResetError", content);
										}
									}
								}
								break;
							}
						}
					}
					
				} else {
					appendToken(originalToken);
				}
			}
			putToken();
			
			String command = fieldContent.get("command");
			result = makeResult(result, command);
			
		}	
		seenCommands.clear();
		fields = new ArrayList<String>(FIELDS_DEFAULT);
		fieldContent.replaceAll((field,content) -> "");
		return result;
	}

	private void appendToken(String token) {
		growingToken += token + " ";
	}
	
	/**
	 * This methods checks if token is a command variant (if yes, convert it to the default command)
	 */
	private String getDefaultCommand(String token) {
		for (String command: COMMANDS) {
			if (token.equalsIgnoreCase(command)){
				return command;
			}
		}
    	for (String command: command_families.keySet()) {
    		ArrayList<String> family = command_families.get(command);
			if (family.contains(token)) {
				return command;
			}
		}
    	return token;
	}
	
	/**
	 * This method marks the command as seen and stores it as the main command (if there isn't one) 
	 */
	private void putCommand(String token) {
		String command = fieldContent.get("command");
		seenCommands.add(token);

		if (command.isEmpty()) {
			fieldContent.put("command", token);
		} else if (!isAddStuff(command)) {
			fieldContent.put("command", "set");
		}
	}
	
	/**
	 * This method stores the growingToken under its rightful field or appends it to an existing field content
	 */
	private void putToken() {
		if (!growingToken.isEmpty()) {
			growingToken = removeEndSpaces(growingToken);
			String lastCommand = getLast(seenCommands);
			String field;
			
			if (lastCommand.isEmpty() || isAddStuff(lastCommand)) {
				field = "name";
			} else {
				field = lastCommand;
			}
			
			String content = fieldContent.get(field);
			if (content.isEmpty()) {
				fieldContent.put(field, growingToken);
			} else {
				fieldContent.put(field, content + " " + growingToken);
			}
			
			growingToken = "";
		}
	}

	/**
	 * This method directly adds or appends a token to a field's content
	 */
	private void addToFieldContent(String command, String token){
		String content;
		String field;
		if (isAddStuff(command)) {
			field = "name";
			content = fieldContent.get(command);
		} else {
			field = command;
			content = fieldContent.get(command);
		}
		
		if (content != null) {
			fieldContent.put(field, removeEndSpaces(content + " " + token));
		} else {
			fieldContent.put(field, removeEndSpaces(token));
		}
	}

	private ArrayList<String> makeResult(ArrayList<String> result, String command) {
		if (command.equals("ERROR")) {
			return result;
		} else if (canBeOneShot(command)) {
			result = makeLongResult(result);
		} else {
			result = makeShortResult(result);
		}
		return result;
	}
	
	/**
	 * This method makes a result that indicates an error in the command
	 */
	private ArrayList<String> makeErrorResult(ArrayList<String> result, String error, String token) {
		result.clear();
		result.add("error");
		fieldContent.put("command", "ERROR");
		switch (error) {
			case "UnrecognisedFirstWordError":
				result.add(error + ": '" + token + "' is not recognised as a command or index");
				break;
			case "IndexError":
				result.add(error + ": '" + token + "' is not recognised as an index");
				break;
			case "DuplicateCommandError":
				result.add(error + ": duplicate command '" + token + "'");
				break;
			case "EmptyFieldError":
				result.add(error + ": please enter content for the command '" + token + "'");
				break;
			case "InvalidPriorityError":
				result.add(error + ": '" + token + "' is not between 1 to 5");
				break;
			case "InvalidDateError":
				result.add(error + ": '" + token + "' is not an acceptable date format");
				break;
			case "InvalidFrequencyError":
				result.add(error + ": '" + token + "' is not 'day', 'week', 'month' or 'year'");
				break;
			case "InvalidDayOfMonthError":
				result.add(error + ": '" + token + "' is not between 1 to 31");
				break;
			case "InvalidTaskTypeError":
				result.add(error + ": '" + token + "' is not 'todo', 'event' or 'floating'");
				break;
			case "InvalidSortFieldError":
				result.add(error + ": '" + token + "' is not 'deadline', 'name' or 'priority'");
				break;
			case "InvalidResetError":
				result.add(error + ": '" + token + "' is not a field that can be resetted");
				break;
			default:
				break; 
		}
		return result;
	}

	/**
	 * This method creates a short result (for non one-shot commands)
	 */
	private ArrayList<String> makeShortResult(ArrayList<String> result) {
		String command = fieldContent.get("command");
		String name = fieldContent.get("name");
		String newField = fieldContent.get(command);
		if (name.isEmpty()) {
			result = makeErrorResult(result, "EmptyFieldError", command);
		} else if (!needIndex(command) && newField.isEmpty()){
			result = makeErrorResult(result, "EmptyFieldError", command);
		} else if (newField == null) {
			result.addAll( Arrays.asList(command, name) );
		} else {
			if (command.equals("deadline") || command.equals("reminder") || command.equals("every")) {
				String freq = "";
				if (command.equals("every")) {
					String[] tokens = newField.split(" ");
					freq = getFirst(tokens);
					if (!isValidFrequency(freq)) {
						result = makeErrorResult(result, "InvalidFrequencyError", newField);
						return result;
					} 
					newField = mergeTokens(tokens, 1, tokens.length);
					if (freq.equals("month")) {
						newField = getNumber(newField);
						if (!isValidDayOfMonth(newField)) {
							result = makeErrorResult(result, "InvalidDayOfMonthError", newField);
						} else {
							/*if (newField.endsWith("1")) {
								newField += "st";
							} else if (newField.endsWith("2")) {
								newField += "nd";
							} else if (newField.endsWith("3")) {
								newField += "rd";
							} else {
								newField += "th";
							}*/
							newField += " of month";
							result.addAll( Arrays.asList(command, name, newField) );
						}
						return result;
					}
				} 		
				String date = parseDate(command, newField);

				if (date.equals("ERROR")) {
					result = makeErrorResult(result, "InvalidDateError", newField);
					return result;
				} else if (command.equals("every")){
					date = changeToRecurFormat(freq, date);
				}
				newField = date;
			}
			if (command.equals("priority") && !isValidPriority(newField)) {
				result = makeErrorResult(result, "InvalidPriorityError", newField);
			} else if (command.equals("event")) {
				String[] eventStartEnd = getStartAndEnd(newField);
				String eventStart = eventStartEnd[0];
				String eventEnd = eventStartEnd[1];
				if (eventStart.equals("ERROR") ) {
					eventStart = removeEndSpaces(newField.split("to")[0]);
					result = makeErrorResult(result, "InvalidDateError", eventStart);
					return result;
				} else if (eventEnd.equals("ERROR") ){
					eventEnd = removeEndSpaces(newField.split("to")[1]);
					result = makeErrorResult(result, "InvalidDateError", eventEnd);
					return result;
				}
				result.addAll( Arrays.asList(command, name, eventStart, eventEnd) );
			} else {
				result.addAll( Arrays.asList(command, name, newField) );
			}
		}
		
		return result;
	}

	/**
	 * This method creates a long result (for one-shot commands)
	 */
	private ArrayList<String> makeLongResult(ArrayList<String> result) {
		String command = fieldContent.get("command");
		String deadline = fieldContent.get("deadline");
		String event = fieldContent.get("event");
		String reminder = fieldContent.get("reminder");
		String priority = fieldContent.get("priority");
		
		if (command.equals("add")) {
			fields.remove("rename");
		}
		
		if (!priority.isEmpty() && !isValidPriority(priority)) {
			result = makeErrorResult(result, "InvalidPriorityError", priority);
		} else {
			if (!reminder.isEmpty()) {
				String date = parseDate("reminder", reminder);
				if (date.equals("ERROR")) {
					result = makeErrorResult(result, "InvalidDateError", reminder);
					return result;
				} else {
					reminder = date;
				}
				fieldContent.put("reminder", reminder);
			}
			if (!deadline.isEmpty()) {
				command += "T";
				fields.remove("eventStart");
				fields.remove("eventEnd");
				String date = parseDate("deadline", deadline);
				if (date.equals("ERROR")) {
					result = makeErrorResult(result, "InvalidDateError", deadline);
					return result;
				} else {
					deadline = date;
				}
				fieldContent.put("deadline", deadline);
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
			fieldContent.put("command", command);
			
			for (String field: fields) {
				String para = fieldContent.get(field);
				result.add(para);
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
		if (!eventEnd.contains(" ") && !eventEnd.isEmpty() && !parseDate("eventEnd", eventEnd).equals("ERROR")) {
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
		eventStart = parseDate("eventStart", removeEndSpaces(eventStart));
		eventEnd = parseDate("eventEnd", removeEndSpaces(eventEnd));
		
		fieldContent.put("eventStart", eventStart);
		fieldContent.put("eventEnd", eventEnd);
		
		String[] result = new String[2];
		result[0] = eventStart;
		result[1] = eventEnd;
		return result;
	}

	/**
	 * This method checks that a date string is valid and parses it into the default date format 
	 */
	private String parseDate(String field, String date) {
		if (date.isEmpty()) {
			return date;
		}
		date = swapDayAndMonth(date);
		
		com.joestelmach.natty.Parser dateParser = new com.joestelmach.natty.Parser();
		String dateString = "";
		DateGroup group;
		try {
			group = dateParser.parse(date).get(0);
		} catch (Exception e){
			return "ERROR";
		}
		
		if (hasNoTime(date)) {
			if (field.equals("deadline")) {
				date += " 23:59";
			} else {
				date += " 12:00";
			}
			group = dateParser.parse(date).get(0);
		}
		
		List<Date> dates = group.getDates();
		dateString = dates.toString();
		dateString = dateString.substring(1, dateString.length()-1); //remove brackets
		dateString = confirmDateIsInFuture(dateString);
		dateString = changeDateFormat(dateString);
		
		return dateString;
	}

	/**
	 * This method checks if the user have included the time in the date string
	 */
	private boolean hasNoTime(String date){
		String[] temp;
		String[] timeSymbols = {":", ".", "am", "pm", "AM", "PM"};
		for (String sym: timeSymbols) {
			temp = date.split(sym);
			if (temp.length > 1) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This method swaps the position of day and month (so that it will work correctly for the natty parser)
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
				System.out.println("Parsing Error");
				e.printStackTrace();
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

	/**
	 * This method sets the date to the standardized format
	 */
	private String changeDateFormat(String dateString) {
		SimpleDateFormat nattyFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		SimpleDateFormat standardFormat = new SimpleDateFormat("EEE, dd MMM hh:mma");
		Date tempDate = null;
		try {
			tempDate = nattyFormat.parse(dateString);
		} catch (Exception e) {
			System.out.println("Parsing Error");
			e.printStackTrace();
		}
		dateString = standardFormat.format(tempDate);
		return dateString;
	}

	private String changeToRecurFormat(String freq, String dateString) {
		SimpleDateFormat standardFormat = new SimpleDateFormat("EEE, dd MMM hh:mma");
		SimpleDateFormat recurDayFormat = new SimpleDateFormat("hh:mma");
		SimpleDateFormat recurWeekFormat = new SimpleDateFormat("EEE hh:mma");
		SimpleDateFormat recurYearFormat = new SimpleDateFormat("dd MMM");
		Date tempDate = null;
		try {
			tempDate = standardFormat.parse(dateString);
		} catch (Exception e) {
			System.out.println("Parsing Error");
			e.printStackTrace();
		}
		switch (freq) {
		case "day":
			dateString = recurDayFormat.format(tempDate);
			break;
		case "week":
			dateString = recurWeekFormat.format(tempDate);
			break;
		case "year":
			dateString = recurYearFormat.format(tempDate);
			break;
		default:
			dateString = standardFormat.format(tempDate);
			break;
		}
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
		String merged = "";
		for (int i = startIndex; i < endIndex; i++) {
			String token = inputTokens[i];
			merged += token + " ";
		}
		return removeEndSpaces(merged);
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

	private boolean isCommand(String token){
		return COMMANDS.contains(token);
	}
	
	private boolean isDominatingCommand(String token){
		return COMMANDS_DOMINATING.contains(token);
	}
	
	private boolean noNeedContent(String token){
		return COMMANDS_NO_CONTENT.contains(token);
	}
	
	private boolean isAddStuff(String token){
		return COMMANDS_ADD_STUFF.contains(token);
	}
	
	private boolean isSeenCommand(String token) {
		return seenCommands.contains(token);
	}
	
	private boolean needWords(String token) {
		return COMMANDS_NEED_WORDS.contains(token);
	}
	
	private boolean needIndex(String token) {
		return COMMANDS_NEED_INDEX.contains(token);
	}
	
	private boolean canBeOneShot(String token) {
		return COMMANDS_ONE_SHOT.contains(token);
	}
	
	private boolean canEdit(String token) {
		return COMMANDS_CAN_EDIT.contains(token);
	}
	
	private boolean isNumber(String token) {
		try {
			Integer.parseInt(token);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private boolean isValidPriority(String token){
		int[] priorityLevels = {1,2,3,4,5};
		if (isNumber(token)) {
			int intToken = Integer.parseInt(token);
			for (int i = 0; i < priorityLevels.length; i++) {
				if (intToken == priorityLevels[i]) {
					return true;
				}
			}
			return false;
		}
		return false;
	}
	
	private boolean isValidFrequency(String token){
		return RECUR_FREQUENCY.contains(token);
	}
	
	private boolean isValidDayOfMonth(String token){
		if (isNumber(token)){
			if (Integer.parseInt(token) <= 31) {
				return true;
			}
		}
		return false;
	}
	
	private String getTaskType(String token){
		switch (token.toLowerCase()) {
		case "todo":
			return "T";
		case "event":
			return "E";
		case "floating":
			return "F";
		default:
			return "ERROR";
		}
	}
	
	private String getSortField(String token){
		switch (token.toLowerCase()) {
		case "deadline":
			return "D";
		case "priority":
			return "P";
		case "name":
			return "N";
		default:
			return "ERROR";
		}
	}
	
	private String getNumber(String token){
		if (isNumber(token)){
			return token;
		} else {
			String c;
			String number = "";
			for (int i = 0; i < token.length(); i++) {
				c = token.substring(i, i+1);
				if (!isNumber(c)) {
					break;
				}
				number += c;
			}
			if (!number.isEmpty()) {
				return number;
			} else {
				return token;
			}
		}
	}
	
	/*private boolean isFirst(String[] array, String token) {
		String firstWord = getFirst(array);
		return firstWord.equals(token);
	}
	
	private boolean isFirst(ArrayList<String> list, String token) {
		String firstWord = getFirst(list);
		return firstWord.equals(token);
	}*/
	
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
	
	private String getFirst(String str){
		return str.split(" ")[0];
	}
	
	/*private String getFirst(ArrayList<String> arrayList){
		return arrayList.get(0);
	}*/
	
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
	
	/*private String getPrevious(ArrayList<String> list, String token) {
		return list.get( list.indexOf(token)-1 );
	}*/
	
}
