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
	"add", "addcat", "category", "deadline", "description", "delete", "deleteAll", "done", "event", "every", 
	"exit", "priority", "redo", "reminder", "rename", "reset", "search", "setcol", "showcat",   
	"show", "showE", "showF", "showO", "showT", "sort", "sortD", "sortN", "sortP", "undo", "undone") );
	
	//Commands that work just by typing the command word (without additional content)
	private final ArrayList<String> COMMANDS_NO_CONTENT = new ArrayList<String>( Arrays.asList(
	"deleteAll", "exit", "redo", "showE", "showF", "showO", "showT", "sortD", "sortN", "sortP", "undo") );
	
	//Commands that if appear first, will prevent other command keywords from having effect
	private final ArrayList<String> COMMANDS_DOMINATING = new ArrayList<String>( Arrays.asList(
	"addcat", "delete", "done", "every", "reset", "search", "setcol", "show", "showcat", "sort", "undone") );
	
	//Commands that create a new item
	private final ArrayList<String> COMMANDS_NO_FIELD = new ArrayList<String>( Arrays.asList(
	"add", "addcat", "delete", "done") );
	
	//Commands that can accept any amount of words
	private final ArrayList<String> COMMANDS_NEED_WORDS = new ArrayList<String>( 
	Arrays.asList("add", "addcat", "category", "description", "search") );
	
	//Commands that cannot be part of a one-shot command
	private final ArrayList<String> COMMANDS_NOT_ONE_SHOT = new ArrayList<String>( 
	Arrays.asList("delete", "done", "reset", "showcat", "undone") );
	
	//Command words that indicate that the command is one-shot
	private final ArrayList<String> COMMANDS_ONE_SHOT = new ArrayList<String>( 
	Arrays.asList("add", "set") );	
	
	//Command fields that can be edited/reset
	private final ArrayList<String> COMMANDS_CAN_RESET = new ArrayList<String>( 
	Arrays.asList("all", "description", "deadline", "event", "priority", "reminder", "category") );	
	
	//The default list of fields and the order in which their contents are put into result
	private final ArrayList<String> FIELDS_DEFAULT = new ArrayList<String>( Arrays.asList(
	"command", "name", "description", "deadline", "eventStart", "eventEnd", "priority", "reminder", "category", "rename") );
	
	//How often a recurring task can recur
	private final ArrayList<String> RECUR_FREQUENCY = new ArrayList<String>( Arrays.asList(
	"day", "week", "month", "year") );
	
	//List of months and their short-forms
	private final ArrayList<String> MONTHS = new ArrayList<String>( Arrays.asList(
	"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december", 
	"jan", "feb", "mar", "apr", "jun", "jul", "aug", "sep", "oct", "nov", "dec") );
	
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
        put("priority",""); put("reminder", ""); put("category", ""); put("every", ""); put("rename", ""); put("reset", "");
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
		String[] inputTokens = input.split(" ");
		ArrayList<String> firstTwoWordsParsed = parseFirstTwoWords(inputTokens);
		
		if (getFirst(firstTwoWordsParsed).equals("PARSING NOT DONE YET")) {
			return parseRemaining(inputTokens);	
		} else {
			return firstTwoWordsParsed;
		}
	}

	private ArrayList<String> parseFirstTwoWords(String[] inputTokens) {
		String firstWordOriginal = getFirst(inputTokens);
		String firstWord = getDefaultCommand(firstWordOriginal);
		String secondWordOriginal = getSecond(inputTokens);
		String secondWord = getDefaultCommand(secondWordOriginal);
		int inputWordCount = inputTokens.length;
		
		if (isCommandAndNoNeedContent(firstWord)) {
			return makeCommandOnlyResult(firstWord);
		}
		if (isCommandButHasNoContent(firstWord, inputWordCount)) {
			return makeErrorResult("EmptyFieldError", firstWord);
		}
		if (isNotCommandOrIndex(firstWord)) {
			return makeErrorResult("InvalidWordError", firstWordOriginal);
		} 
		if (isDominatingCommand(firstWord)) {
			String content = mergeTokens(inputTokens, 1, inputTokens.length);
			ArrayList<String> dominantResult = makeDominantResult(firstWord, secondWord, content); 
			if (!getFirst(dominantResult).equals("PARSING NOT DONE YET")) {
				return dominantResult;
			}
		}
		if (isCommandThatCantBeInOneShot(secondWord)) {
			String content = mergeTokens(inputTokens, 1, inputTokens.length);
			ArrayList<String> dominantResult = makeDominantResult(secondWord, firstWord, content); 
			if (!getFirst(dominantResult).equals("PARSING NOT DONE YET")) {
				return dominantResult;
			}
		}
		if (isNumber(firstWord)) {
			if (secondWord.isEmpty()) {
				return makeErrorResult("NoCommandError", firstWord);
			} else if (!(isCommand(secondWord))) {
				return makeErrorResult("InvalidCommandError", secondWordOriginal);
			}
		}
		return new ArrayList<String>( Arrays.asList( "PARSING NOT DONE YET" ));
	}

	private ArrayList<String> parseRemaining(String[] inputTokens) {
		int inputWordCount = inputTokens.length;
		boolean hasQuote = containsQuotes(inputTokens);
		boolean quoteStart = false;
		for (int i = 0; i < inputWordCount; i++) {
			String token = inputTokens[i];
			String originalToken = token;
			token = getDefaultCommand(token);
			if (hasQuote && token.startsWith("\"")) {
				quoteStart = true;
			}
			
			if (quoteStart == true) {
				appendToken(originalToken);
				if (token.endsWith("\"")) {
					quoteStart = false;
				}
			} else if (isNotCommand(token) || isCommandRepressed(token)){
				appendToken(originalToken);	
			} else {
				putToken();
				String lastCommandSeen = getLast(seenCommands);
				
				if (isLastWord(i, inputWordCount) && isCommandThatNeedWords(lastCommandSeen)) {
					appendToken(originalToken);
				} else if (isLastWord(i, inputWordCount) && !isDominatingCommand(originalToken)){
					return makeErrorResult("EmptyFieldError", originalToken);
				} else if (isSeenCommand(token)) {
					if (isCommandThatNeedWords(lastCommandSeen)) {
						addToFieldContent(lastCommandSeen, originalToken);
					} else {
						return makeErrorResult("DuplicateCommandError", originalToken);
					}
				} else {
					if (isCommandRepressed(token)) {
						appendToken(originalToken);
					} else {
						if (!lastCommandSeen.isEmpty()) {
							String contentOfLastCommand = getContentOfLastCommand(lastCommandSeen);
							if (contentOfLastCommand.isEmpty()) {
								return makeErrorResult("EmptyFieldError", lastCommandSeen);
							}
						}
						putCommand(token);
					}
				}
			} 
		}
		putToken();
		String command = fieldContent.get("command");
		if (isOneShot(command)) {
			return makeMultiFieldResult();
		} else {
			return makeSingleFieldResult();
		}
	}

	private ArrayList<String> makeCommandOnlyResult(String command){
		return new ArrayList<String>( Arrays.asList( command ) );
	}

	private ArrayList<String> makeCommandAndContentResult(String command, String content){
		return new ArrayList<String>( Arrays.asList( command, content ) );
	}

	private ArrayList<String> makeDominantResult(String firstWord, String secondWord, String content) {
		String command = firstWord;
		String index = secondWord;
		System.out.println("content " + content);
		if (command.equals("show")) {
			return makeShowResult(command, content);
		}
		if (command.equals("sort")) {
			return makeSortResult(command, content);
		}
		if (command.equals("reset")) {
			return makeResetResult(command, index, content);
		}
		if (isCommandThatNeedWords(command)) { //if first word is addcat or search
			return makeCommandAndContentResult(command, content);
		}
		if (isCommandThatCantBeInOneShot(command)) { //if first word is delete, done, or undone 
			if (isNumber(index)) {
				return makeCommandAndContentResult(command, index);
			} else {
				return makeErrorResult("InvalidIndexError", content);
			}
		}
		return new ArrayList<String>( Arrays.asList( "PARSING NOT DONE YET" ));
	}

	private ArrayList<String> makeShowResult(String command, String content) {
		String taskType = getTaskType(content);
		if (taskType.equals("ERROR")) {
			return makeErrorResult("InvalidTaskTypeError", content);
		} else {
			return new ArrayList<String>( Arrays.asList( command+taskType ) );
		}
	}
	
	private ArrayList<String> makeSortResult(String command, String content) {
		String field = getSortField(getDefaultCommand(content));
		if (field.equals("ERROR")) {
			return makeErrorResult("InvalidSortFieldError", content);
		} else {
			return new ArrayList<String>( Arrays.asList( command+field ) );
		}
	}
	
	private ArrayList<String> makeResetResult(String command, String index, String content) {
		appendAndPutToken(index);
		putCommand(command);
		String[] contentTokens = content.split(" ");
		content = mergeTokens(contentTokens, 1, contentTokens.length);
		if (content.isEmpty()) {
			return makeErrorResult("EmptyFieldError", command);
		} else {
			String fieldToReset = getDefaultCommand(content);
			if (canReset(fieldToReset)) {
				appendAndPutToken(fieldToReset);
				return makeSingleFieldResult();
			} else {
				return makeErrorResult("InvalidResetError", content);
			}
		}
	}

	/**
	 * This method creates a short result (for non one-shot commands)
	 */
	private ArrayList<String> makeSingleFieldResult() {
		String command = fieldContent.get("command");
		String name = fieldContent.get("name");
		String content = fieldContent.get(command);
		resetTempContent();
		
		if (command.equals("every")) {
			return makeRecurringResult(command, name, content);
		} 	
		if (command.equals("event")) {
			if (content.endsWith("to")) {
				return makeErrorResult("NoEndDateError", content);
			}
			return makeEventResult(command, name, content);
		}
		
		if (command.equals("deadline") || command.equals("reminder")) {
			String date = parseDate(command, content);
			if (date.equals("ERROR")) {
				return makeErrorResult("InvalidDateError", content);
			}
			content = date;
		}
		if (command.equals("priority") && isNotValidPriority(content)) {
			return makeErrorResult("InvalidPriorityError", content);
		}
		return new ArrayList<String>( Arrays.asList(command, name, content) );
	}

	private ArrayList<String> makeEventResult(String command, String name, String event) {
		String[] eventStartEnd = getStartAndEnd(event);
		String eventStart = getFirst(eventStartEnd);
		String eventEnd = getLast(eventStartEnd);
		if (eventStart.equals("ERROR") ) {
			eventStart = removeEndSpaces(getFirst(event.split(" to ")));
			return makeErrorResult("InvalidDateError", eventStart);
		} 
		if (eventEnd.equals("ERROR") ){
			eventEnd = removeEndSpaces(getLast(event.split(" to ")));
			return makeErrorResult("InvalidDateError", eventEnd);
		}
		return new ArrayList<String>( Arrays.asList(command, name, eventStart, eventEnd) );
	}

	private ArrayList<String> makeRecurringResult(String command, String name, String newField){
		String freq = "";
		String[] fieldTokens = newField.split(" ");
		freq = getFirst(newField);
		if (isNotValidFrequency(freq)) {
			return makeErrorResult("InvalidFrequencyError", freq);
		} 
		newField = mergeTokens(fieldTokens, 1, fieldTokens.length);
		if (freq.equals("month")) {
			String dayOfMonth = getNumber(newField);
			if (isNotValidDayOfMonth(dayOfMonth)) {
				return makeErrorResult("InvalidDayOfMonthError", dayOfMonth);
			} else {
				dayOfMonth += " of month";
				return new ArrayList<String> ( Arrays.asList(command, name, dayOfMonth) );
			}
		}
		String date = parseDate(command, newField);
		if (date.equals("ERROR")) {
			return makeErrorResult("InvalidDateError", newField);
		}
		
		date = changeToRecurFormat(freq, date);
		return new ArrayList<String> ( Arrays.asList(command, name, date) );
	}
	
	/**
	 * This method creates a long result (for one-shot commands)
	 */
	private ArrayList<String> makeMultiFieldResult() {
		String command = fieldContent.get("command");
		String deadline = fieldContent.get("deadline");
		String event = fieldContent.get("event");
		String reminder = fieldContent.get("reminder");
		String priority = fieldContent.get("priority");
		ArrayList<String> result = new ArrayList<String>();
		
		if (command.equals("add")) {
			fields.remove("rename");
		}
		
		if (!priority.isEmpty() && isNotValidPriority(priority)) {
			return makeErrorResult("InvalidPriorityError", priority);
		} else {
			if (!reminder.isEmpty()) {
				String date = parseDate("reminder", reminder);
				if (date.equals("ERROR")) {
					return makeErrorResult("InvalidDateError", reminder);
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
					return makeErrorResult("InvalidDateError", deadline);
				} else {
					deadline = date;
				}
				fieldContent.put("deadline", deadline);
			} else if (!event.isEmpty()) {
				command += "E";
				fields.remove("deadline");
				if (event.endsWith("to")) {
					return makeErrorResult("NoEndDateError", event);
				} else {
					String[] eventStartEnd = getStartAndEnd(event);
					String eventStart = getFirst(eventStartEnd);
					String eventEnd = getLast(eventStartEnd);
					if (eventStart.equals("ERROR") ) {
						eventStart = removeEndSpaces(getFirst(event.split(" to ")));
						return makeErrorResult("InvalidDateError", eventStart);
					} else if (eventEnd.equals("ERROR") ){
						eventEnd = removeEndSpaces(getLast(event.split(" to ")));
						return makeErrorResult("InvalidDateError", eventEnd);
					}
				}
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
		resetTempContent();
		return result;
	}

	private ArrayList<String> makeErrorResult(String error, String token) {
		fields = new ArrayList<String>(FIELDS_DEFAULT);
		resetTempContent();
		ArrayList<String> result = new ArrayList<String>(); 
		result.add("error");
		//fieldContent.put("command", "ERROR");
		
		switch (error) {
			case "InvalidWordError":
				result.add(error + ": '" + token + "' is not recognised as a command or index");
				break;
			case "InvalidIndexError":
				result.add(error + ": '" + token + "' is not recognised as an index");
				break;
			case "InvalidCommandError":
				result.add(error + ": '" + token + "' is not recognised as a command");
				break;
			case "NoCommandError":
				result.add(error + ": please enter a command after the task index '" + token + "'");
				break;
			case "DuplicateCommandError":
				result.add(error + ": duplicate command '" + token + "'");
				break;
			case "EmptyFieldError":
				result.add(error + ": please enter content for the command '" + token + "'");
				break;
			case "NoEndDateError":
				result.add(error + ": please enter an end date after the command word 'to'");
				break;
			case "InvalidPriorityError":
				result.add(error + ": '" + token + "' is not between 1 to 5");
				break;
			case "InvalidDateError":
				result.add(error + ": '" + token + "' is not an acceptable date format");
				break;
			case "InvalidFrequencyError":
				result.add(error + ": please enter 'day'/'week'/'month'/'year' after 'every' to indicate the frequency");
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
				result.add(error + ": '" + token + "' is not a field that can be reset");
				break;
			default:
				break; 
		}
		return result;
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

	private String getContentOfLastCommand(String lastCommandSeen) {
		String content;
		if (isAddStuff(lastCommandSeen)) {
			content = fieldContent.get("name");
		} else {
			content = fieldContent.get(lastCommandSeen);
		}
		return content;
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

	private void appendToken(String token) {
		growingToken += token + " ";
	}

	/**
	 * This method stores the growingToken under its rightful field or appends it to an existing field content
	 */
	private void putToken() {
		if (!growingToken.isEmpty()) {
			//System.out.println("grow " + growingToken);
			growingToken = removeEndSpaces(growingToken);
			growingToken = removeQuotes(growingToken);
			String lastCommand = getLast(seenCommands);
			String field;
			//System.out.println("last: " + lastCommand);
			if (fieldContent.get("name").isEmpty()) {
				field = "name";
			} else if (lastCommand.isEmpty() || isAddStuff(lastCommand)) {
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
			//System.out.println(field + " " + fieldContent.get(field));
			growingToken = "";
		}
	}

	private void appendAndPutToken(String firstWord) {
		appendToken(firstWord);
		putToken();
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

	private void resetTempContent() {
		seenCommands.clear();
		fields = new ArrayList<String>(FIELDS_DEFAULT);
		for (String key: fieldContent.keySet()){
			fieldContent.put(key, "");
		}
	}

	private boolean isNotCommand(String token){
		return !isCommand(token);
	}

	private boolean isNotCommandOrIndex(String token) {
		return isNotCommand(token) && !isNumber(token);
	}
	
	private boolean isCommandButHasNoContent(String token, int inputWordCount) {
		return isCommand(token) && inputWordCount == 1;
	}
	
	private boolean isCommandRepressed(String token) {
		String mainCommand = fieldContent.get("command");
		return !mainCommand.isEmpty() && (isDominatingCommand(token) || isCommandAndNoNeedContent(token));
	}

	private boolean isLastWord(int i, int inputWordCount) {
		return i == inputWordCount-1;
	}

	private boolean containsQuotes(String[] inputTokens){
		int quoteCommaCount = 0;
		for (String token: inputTokens) {
			if (token.startsWith("\"")) {
				quoteCommaCount++;
			}
			if (token.endsWith("\"")) {
				quoteCommaCount++;
			}
		}
		return quoteCommaCount > 1 && quoteCommaCount % 2 == 0;
	}
	
	/**
	 * This method gets the individual start and end date/time of an event and put them into separate fields
	 * @return the start and end date/time in an array
	 */
	private String[] getStartAndEnd(String event) {
		String[] eventTokens = event.split(" to ", 2);
		String eventStart = getFirst(eventTokens);
		String eventEnd = "";
		if (eventTokens.length > 1) {
			eventEnd = removeEndSpaces(getLast(eventTokens));
		}
		
		if (parseDate("eventStart", eventStart).equals("ERROR")) {
			eventStart = "ERROR";
		} else if (parseDate("eventEnd", eventEnd).equals("ERROR")) {
			eventEnd = "ERROR";
		} else {
			String startDate = "";
			if (hasNoTime(eventStart)) {
				eventStart += " 12:00";
			}
			String[] startTokens = eventStart.split(" ");
			for (int i = 0; i < startTokens.length-1; i++) {
				startDate += startTokens[i] + " ";
			}
			String startTime = getLast(startTokens);
			
			if (eventEnd.isEmpty()) {
				eventEnd = startDate + "23:59";
			} else if (hasNoDate(eventEnd)) {
				String endTime = eventEnd;
				if (startTimeIsAfterEndTime(startTime, endTime)) {
					startDate = plusOneDay(startDate);
				} 
				eventEnd = startDate + " " + endTime;	
			} else if (hasNoTime(eventEnd)) {
				String endDate = eventEnd;
				eventEnd = endDate + " " + startTime;
			} 

			eventStart = parseDate("eventStart", removeEndSpaces(eventStart));
			eventEnd = parseDate("eventEnd", removeEndSpaces(eventEnd));
			
			if (startDateIsAfterEndDate(eventStart, eventEnd)) {
				eventEnd = plusOneYear(eventEnd);
			}
			
			fieldContent.put("eventStart", eventStart);
			fieldContent.put("eventEnd", eventEnd);
		}
		
		String[] result = new String[2];
		result[0] = eventStart;
		result[1] = eventEnd;
		return result;
	}

	private String plusOneDay(String dateString) {
		dateString = parseDate("event", dateString);
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yy, hh:mma");
		SimpleDateFormat sdfNoMinute = new SimpleDateFormat("EEE, dd MMM yy, hha");
		Date date = null;
		if (hasMinute(dateString)) {
			try {
				date = sdf.parse(dateString);
			} catch (ParseException e) {
				System.out.println("parseError");
				e.printStackTrace();
			}
		} else {
			try {
				date = sdfNoMinute.parse(dateString);
			} catch (ParseException e) {
				System.out.println("parseError");
				e.printStackTrace();
			}
		}
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		date = c.getTime();
		return date.toString().substring(4, 10);
	}

	private boolean startDateIsAfterEndDate(String start, String end){
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yy, hh:mma");
		SimpleDateFormat sdfNoMinute = new SimpleDateFormat("EEE, dd MMM yy, hha");
		Date startDate = null;
		Date endDate = null;
		if (hasMinute(start)) {
			try {
				startDate = sdf.parse(start);
			} catch (ParseException e) {
				System.out.println("parseError");
				e.printStackTrace();
			}
		} else {
			try {
				startDate = sdfNoMinute.parse(start);
			} catch (ParseException e) {
				System.out.println("parseError");
				e.printStackTrace();
			}
		}
		if (hasMinute(end)) {
			try {
				endDate = sdf.parse(end);
			} catch (ParseException e) {
				System.out.println("parseError");
				e.printStackTrace();
			}
		} else {
			try {
				endDate = sdfNoMinute.parse(end);
			} catch (ParseException e) {
				System.out.println("parseError");
				e.printStackTrace();
			}
		}
		return startDate.after(endDate);
	}
	
	private boolean startTimeIsAfterEndTime(String start, String end){
		if (isAM(start) && isPM(end)) {
			return false;
		} else if (isPM(start) && isAM(end)) {
			return true;
		} else  {
			String startHour;
			String endHour;
			String startMinute;
			String endMinute;
			if (isAM(start) && isAM(end)){
				startHour = getFirst(start.toLowerCase().split("am"));
				endHour = getFirst(end.toLowerCase().split("am"));
			} else {
				startHour = getFirst(start.toLowerCase().split("pm"));
				endHour = getFirst(end.toLowerCase().split("pm"));
			}
			String startTemp[] = startHour.toLowerCase().split(":");
			String endTemp[] = endHour.toLowerCase().split(":");		
			startHour = getFirst(startTemp);
			endHour = getFirst(endTemp);
			startMinute = getSecond(startTemp);
			endMinute = getSecond(endTemp);
			int startHourNum = 0;
			int endHourNum = 0;
			try {
				startHourNum = Integer.parseInt(startHour); 
				endHourNum = Integer.parseInt(endHour); 
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			if (startHourNum > endHourNum){
				return true;
			} else if (startHourNum < endHourNum){
				return false;
			} else {
				if (startMinute.isEmpty() && !endMinute.isEmpty()) {
					return false;
				} else if (startMinute.isEmpty() && endMinute.isEmpty()) {
					return true;
				} else if (!startMinute.isEmpty() && endMinute.isEmpty()) {
					return true;
				} else {
					int startMinuteNum = 0;
					int endMinuteNum = 0;
					try {
						startMinuteNum = Integer.parseInt(startMinute); 
						endMinuteNum = Integer.parseInt(endMinute); 
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
					return (startMinuteNum >= endMinuteNum);
				}
			}
		}
	}

	private boolean isAM(String time){
		return time.toLowerCase().endsWith("am");
	}
	
	private boolean isPM(String time){
		return time.toLowerCase().endsWith("pm");
	}
	
	private boolean hasNoDate(String eventEnd) {
		if (eventEnd.split("/").length > 1 || eventEnd.split("-").length > 1) {
			return false;
		} else {
			String[] tokens = eventEnd.split(" ");
			for (String token: tokens) {
				if (MONTHS.contains(token.toLowerCase())) {
					return false;
				}
			}
		}
		return true;
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
		dateString = removeMinuteIfZero(dateString);
		
		return dateString;
	}

	/**
	 * This method checks if the user have included the time in the date string
	 */
	private boolean hasNoTime(String date){
		String[] temp;
		String[] timeSymbols = {":", "."};
		for (String sym: timeSymbols) {
			temp = date.split(sym);
			if (temp.length > 1) {
				return false;
			}
		}
		temp = date.split(" ");
		for (String token: temp) {
			if (token.toLowerCase().endsWith("am") || token.toLowerCase().endsWith("pm")) {
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
			
			try {
				dateMilli = formatter.parse(dateString).getTime();
			} catch (ParseException e) {
				System.out.println("Parsing Error");
				e.printStackTrace();
			}
			if (year == currYear) {
				if (dayMonth < nowMilli) {
					if (dateMilli < nowMilli){
						date = plusOneYear(date);
						dateString = date.toString();
					} else {
						dateString = formatter.format(dateMilli);
					}
				}
			}
		}
		return dateString;
	}

	/**
	 * This method sets the date to the standardized format
	 */
	private String changeDateFormat(String dateString) {
		SimpleDateFormat nattyFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		SimpleDateFormat standardFormat = new SimpleDateFormat("EEE, dd MMM yy, h:mma");
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

	private String removeMinuteIfZero(String dateString) {
		String result = "";
		if (containLetter(dateString, ":")) {
			String[] dateTokens = dateString.split(" ");
			String time = getLast(dateTokens);
			if (dateTokens.length > 1) {
				result += mergeTokens(dateTokens, 0, dateTokens.length-1) + " ";
			}
			
			String[] timeTokens = time.split(":", 2);
			String hour = getFirst(timeTokens);
			if (hour.charAt(0) == '0'){
				result += hour.charAt(1);
			} else {
				result += hour;
			}
			
			String minute = getLast(timeTokens);
			if (minute.equals("00AM")) {
				result += "am";
			} else if (minute.equals("00PM")) {
				result += "pm";
			} else {
				minute = minute.replace("AM", "am");
				minute = minute.replace("PM", "pm");
				result += ":" + minute;
			}
		} else {
			result = dateString;
		}
		return result;
	}

	private String changeToRecurFormat(String freq, String dateString) {
		SimpleDateFormat standardFormat = new SimpleDateFormat("EEE, dd MMM yy, hh:mma");
		SimpleDateFormat standardFormatNoMinute = new SimpleDateFormat("EEE, dd MMM yy, hha");
		SimpleDateFormat recurDayFormat = new SimpleDateFormat("hh:mma");
		SimpleDateFormat recurWeekFormat = new SimpleDateFormat("EEE hh:mma");
		SimpleDateFormat recurYearFormat = new SimpleDateFormat("dd MMM");
		Date tempDate = null;
		
		dateString = dateString.replace("am", "AM");
		dateString = dateString.replace("pm", "PM");
		if (containLetter(dateString, ":")) {
			try {
				tempDate = standardFormat.parse(dateString);
			} catch (Exception e) {
				System.out.println("Parsing Error");
				e.printStackTrace();
			}
		} else {
			try {
				tempDate = standardFormatNoMinute.parse(dateString);
			} catch (Exception e) {
				System.out.println("Parsing Error");
				e.printStackTrace();
			}
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
		dateString = removeMinuteIfZero(dateString);
		return dateString;
	}
	
	private Date plusOneYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, 1);
		date = c.getTime();
		return date;
	}
	private String plusOneYear(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yy, hh:mma");
		SimpleDateFormat sdfNoMinute = new SimpleDateFormat("EEE, dd MMM yy, hha");
		Date date = null;
		if (hasMinute(dateString)) {
			try {
				date = sdf.parse(dateString);
			} catch (ParseException e) {
				System.out.println("parseError");
				e.printStackTrace();
			}
		} else {
			try {
				date = sdfNoMinute.parse(dateString);
			} catch (ParseException e) {
				System.out.println("parseError");
				e.printStackTrace();
			}
		}
		date = plusOneYear(date);
		return removeMinuteIfZero(changeDateFormat(date.toString()));
	}

	private long getDateLong(String date){
		SimpleDateFormat sdfDate = new SimpleDateFormat("MMM dd");
	    Date parseDate;
		try {
			parseDate = sdfDate.parse(date);
			return parseDate.getTime();
		} catch (ParseException e) {
			System.out.println("parseError");
			e.printStackTrace();
		}
		return 0;
	}
	
	private boolean hasMinute(String time){
		return time.split(":").length > 1;
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
	
	private String removeQuotes(String token){
		if (token.startsWith("\"") && token.endsWith("\"")) {
			token =  token.substring(1, token.length()-1);
		} 
		return token;
	}

	private boolean isCommand(String token){
		return COMMANDS.contains(token);
	}
	
	private boolean isDominatingCommand(String token){
		return COMMANDS_DOMINATING.contains(token);
	}
	
	private boolean isCommandAndNoNeedContent(String token){
		return COMMANDS_NO_CONTENT.contains(token);
	}
	
	private boolean isAddStuff(String token){
		return COMMANDS_NO_FIELD.contains(token);
	}
	
	private boolean isSeenCommand(String token) {
		return seenCommands.contains(token);
	}
	
	private boolean isCommandThatNeedWords(String token) {
		return COMMANDS_NEED_WORDS.contains(token);
	}
	
	private boolean isCommandThatCantBeInOneShot(String token) {
		return COMMANDS_NOT_ONE_SHOT.contains(token) || COMMANDS_NO_CONTENT.contains(token);
	}
	
	private boolean isOneShot(String token) {
		return COMMANDS_ONE_SHOT.contains(token);
	}
	
	private boolean canReset(String token) {
		return COMMANDS_CAN_RESET.contains(token);
	}
	
	private boolean isNumber(String token) {
		try {
			Integer.parseInt(token);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private boolean isNotValidPriority(String token){
		int[] priorityLevels = {1,2,3,4,5};
		if (isNumber(token)) {
			int intToken = Integer.parseInt(token);
			for (int i = 0; i < priorityLevels.length; i++) {
				if (intToken == priorityLevels[i]) {
					return false;
				}
			}
			return true;
		}
		return true;
	}
	
	private boolean isNotValidFrequency(String token){
		return !RECUR_FREQUENCY.contains(token);
	}
	
	private boolean isNotValidDayOfMonth(String token){
		if (isNumber(token)){
			if (Integer.parseInt(token) <= 31) {
				return false;
			}
		}
		return true;
	}
	
	private String getTaskType(String token){
		switch (token.toLowerCase()) {
		case "todo": 
		case "todos":
		case "t":
			return "T";
		case "event":
		case "events":
		case "e":
			return "E";
		case "floating":
		case "floatings":
		case "f":
			return "F";
		case "overdue":
		case "o":
			return "O";
		default:
			return "ERROR";
		}
	}
	
	private String getSortField(String token){
		switch (token.toLowerCase()) {
		case "deadline":
		case "by deadline":
		case "d":
			return "D";
		case "priority":
		case "by priority":
		case "p":
			return "P";
		case "name":
		case "by name":
		case "n":
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
	
	/*private boolean isLast(String[] array, String token) {
		String lastWord = getLast(array);
		return lastWord.equals(token);
	}*/

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
	
	private String getFirst(ArrayList<String> arrayList){
		return arrayList.get(0);
	}
	
	/*private String getSecond(String[] array){
		return array[1];
	}*/
	
	private String getSecond(String[] array){
		if (array.length < 2) {
			return "";
		}
		return array[1];
	}
	
	private String getSecond(String str){
		return str.split(" ")[1];
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
	
	/*private String getLast(String str){
		String[] temp = str.split(" ");
		return temp[temp.length-1];
	}*/
	
	private boolean containLetter(String str, String letter){
		return str.split(letter).length > 1;
	}
	
	/*private String getPrevious(ArrayList<String> list, String token) {
		return list.get( list.indexOf(token)-1 );
	}*/
	
}
