package main.java.backend.Parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

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
	"command", "task", "description", "deadline", "eventStart", "eventEnd", "priority", "reminder", "category", "rename") );
	
	//How often a recurring task can recur
	private final ArrayList<String> RECUR_FREQUENCY = new ArrayList<String>( Arrays.asList(
	"day", "week", "month", "year") );
	
	//List of months and their short-forms
	private final ArrayList<String> MONTHS = new ArrayList<String>( Arrays.asList(
	"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december", 
	"jan", "feb", "mar", "apr", "jun", "jul", "aug", "sep", "oct", "nov", "dec") );
	
	//List of days in a week and their short-forms
	private final ArrayList<String> DAYS_OF_WEEK = new ArrayList<String>( Arrays.asList(
	"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday",
	"mon", "tue", "wed", "thu", "fri", "sat", "sun") );
	
	//List of fields that are used in the current result
	private ArrayList<String> fields = new ArrayList<String>(FIELDS_DEFAULT);
	
	//Tokens are 'merged' into a growingToken until they are stored into field content
	private String growingToken = "";
	
	//List of commands that have appeared in the current input
	private ArrayList<String> seenCommands = new ArrayList<String>();
	
	//Stores the fields and their respective contents
	private HashMap<String, String> fieldContent = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L; {
        put("command",""); put("task", ""); put("description", ""); put("deadline", ""); put("event", ""); 
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
    
    private com.joestelmach.natty.Parser dateParser = new com.joestelmach.natty.Parser();
    
    public Parser(){
    	//Force Natty parser to initialize by running dateParser once
    	String pi = "31/4/15 9:26";
    	parseDate(pi);
    }
    
	/**
	 * This method parses the user input and returns an ArrayList of string tokens
	 */
	public ArrayList<String> parseInput(String input){
		resetContents();
		String[] inputTokens = input.split(" ");
		ArrayList<String> firstTwoWordsParsed = parseFirstTwoWords(inputTokens);
		
		if (isIncompleteStatus(firstTwoWordsParsed)) {
			return parseRemaining(inputTokens);	
		} else {
			return firstTwoWordsParsed;
		}
	}

	private ArrayList<String> parseFirstTwoWords(String[] inputTokens) {
		String firstWordOriginal = getFirst(inputTokens);
		String firstWord = convertVariantToDefault(firstWordOriginal);
		String secondWordOriginal = getSecond(inputTokens);
		String secondWord = convertVariantToDefault(secondWordOriginal);
		int inputWordCount = inputTokens.length;
		
		if (isCommandThatNoNeedContent(firstWord)) {
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
			if (!isIncompleteStatus(dominantResult)) {
				return dominantResult;
			}
		}
		if (isCommandButCannotBeInOneShot(secondWord)) {
			String content = mergeTokens(inputTokens, 1, inputTokens.length);
			ArrayList<String> dominantResult = makeDominantResult(secondWord, firstWord, content); 
			if (!isIncompleteStatus(dominantResult)) {
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
		return new ArrayList<String>( Arrays.asList( "incomplete" ));
	}

	private ArrayList<String> parseRemaining(String[] inputTokens) {
		int inputWordCount = inputTokens.length;
		boolean hasQuote = hasQuotes(inputTokens);
		boolean quoteStart = false;
		
		for (int i = 0; i < inputWordCount; i++) {
			String token = inputTokens[i];
			String originalToken = token;
			token = convertVariantToDefault(token);
			
			quoteStart = checkForQuotes(hasQuote, quoteStart, originalToken, i, inputTokens); 
			if (quoteStart) {
				growToken(originalToken);
				
			} else {
				ArrayList<String> parseResult = parseToken(token, originalToken, i, inputWordCount);
				if (isErrorStatus(parseResult)) {
					return parseResult;
				}
			}
		}
		
		storeToken(growingToken);
		String command = fieldContent.get("command");
		if (isOneShotCommand(command)) {
			return makeMultiFieldResult();
		} else {
			return makeSingleFieldResult();
		}
	}

	private ArrayList<String> parseToken(String token, String originalToken, int count, int total) {
		if (isNotCommand(token) || isCommandButRepressed(token)){
			growToken(originalToken);	
		} else {
			storeToken(growingToken);
			String lastCommandSeen = getLast(seenCommands);
			
			if (isLastWord(count, total) && isCommandThatNeedWords(lastCommandSeen)) {
				growToken(originalToken);
			} else if (isLastWord(count, total) && !isCommandButRepressed(token)){
				return makeErrorResult("EmptyFieldError", originalToken);
			} else if (isSeenCommand(token)) {
				if (isCommandThatNeedWords(lastCommandSeen)) {
					storeToken(originalToken);
				} else {
					return makeErrorResult("DuplicateCommandError", originalToken);
				}
			} else {
				if (!lastCommandSeen.isEmpty()) {
					String contentOfLastCommand = getContentOfCommand(lastCommandSeen);
					if (contentOfLastCommand.isEmpty()) {
						return makeErrorResult("EmptyFieldError", lastCommandSeen);
					}
				}
				storeCommand(token);
			}
		}
		return new ArrayList<String>( Arrays.asList( "OK" ));
	}

	private boolean checkForQuotes(boolean hasQuote, boolean quoteStart, String token, 
			int count, String[] inputTokens) {
		if (hasQuote && token.startsWith("\"")) {
			quoteStart = true;
		}
		
		if (quoteStart == true) {
			String previousToken = getPrevious(inputTokens, count);
			if (previousToken.endsWith("\"")) {
				quoteStart = false;
			}
		}
		
		return quoteStart;
	}

	/**
	 * This methods checks if token is a command variant (if yes, convert it to the default command)
	 */
	private String convertVariantToDefault(String token) {
		for (String command: COMMANDS) {
			if (token.equalsIgnoreCase(command)){
				return command;
			}
		}
		token = token.toLowerCase();
		for (String command: command_families.keySet()) {
			ArrayList<String> family = command_families.get(command);
			if (family.contains(token)) {
				return command;
			}
		}
		return token;
	}

	private String getContentOfCommand(String lastCommandSeen) {
		if (isCommandThatAddStuff(lastCommandSeen)) {
			return fieldContent.get("task");
		} else {
			return fieldContent.get(lastCommandSeen);
		}
	}

	private void growToken(String token) {
		growingToken += token + " ";
	}

	/**
	 * This method marks the command as seen and stores it as the main command (if there isn't one) 
	 */
	private void storeCommand(String token) {
		String command = fieldContent.get("command");
		seenCommands.add(token);
	
		if (command.isEmpty()) {
			fieldContent.put("command", token);
		} else if (!isCommandThatAddStuff(command)) {
			fieldContent.put("command", "set");
		}
	}

	/**
	 * This method stores the token into the field content and reset the growing token
	 */
	private void storeToken(String token) {
		if (!token.isEmpty()) {
			token = removeEndSpaces(token);
			token = removeQuotes(token);
			String lastCommand = getLast(seenCommands);
			String field;
			growingToken = ""; //reset growingToken
			
			if (fieldContent.get("task").isEmpty()) {
				field = "task";
			} else if (lastCommand.isEmpty() || isCommandThatAddStuff(lastCommand)) {
				field = "task";
			} else {
				field = lastCommand;
			}
			
			String content = fieldContent.get(field);
			if (content.isEmpty()) {
				fieldContent.put(field, token);
			} else {
				fieldContent.put(field, content + " " + token);
			}
		}
	}

	private String getFirst(String[] array){
		return array[0];
	}

	private String getFirst(String str){
		return str.split(" ")[0];
	}

	private String getFirst(ArrayList<String> arrayList){
		return arrayList.get(0);
	}

	private String getSecond(String[] array){
		if (array.length < 2) {
			return "";
		}
		return array[1];
	}

	private String getPrevious(ArrayList<String> list, String token) {
		if (list.size() > 1 && list.indexOf(token) != 0) {
			return list.get( list.indexOf(token)-1 );
		}
		return null;
	}
	
	private String getPrevious(String[] list, int count) {
		if (count != 0) {
			return list[count - 1];
		}
		return null;
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
			return "error";
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
			return "error";
		}
	}

	private String getStatus(ArrayList<String> parseResult) {
		return getFirst(parseResult);
	}

	private boolean isCommand(String token){
		return COMMANDS.contains(token);
	}

	private boolean isCommandThatNoNeedContent(String token){
		return COMMANDS_NO_CONTENT.contains(token);
	}

	private boolean isDominatingCommand(String token){
		return COMMANDS_DOMINATING.contains(token);
	}

	private boolean isCommandThatAddStuff(String token){
		return COMMANDS_NO_FIELD.contains(token);
	}

	private boolean isCommandThatNeedWords(String token) {
		return COMMANDS_NEED_WORDS.contains(token);
	}

	private boolean isCommandThatCanBeReset(String token) {
		return COMMANDS_CAN_RESET.contains(token);
	}

	private boolean isCommandButCannotBeInOneShot(String token) {
		return COMMANDS_NOT_ONE_SHOT.contains(token) || COMMANDS_NO_CONTENT.contains(token);
	}

	private boolean isCommandButHasNoContent(String token, int inputWordCount) {
		return isCommand(token) && inputWordCount == 1;
	}

	private boolean isCommandButRepressed(String token) {
		String mainCommand = fieldContent.get("command");
		return !mainCommand.isEmpty() && (isDominatingCommand(token) || isCommandThatNoNeedContent(token));
	}

	private boolean isOneShotCommand(String token) {
		return COMMANDS_ONE_SHOT.contains(token);
	}

	private boolean isNotCommand(String token){
		return !isCommand(token);
	}

	private boolean isNotCommandOrIndex(String token) {
		return isNotCommand(token) && !isNumber(token);
	}

	private boolean isSeenCommand(String token) {
		return seenCommands.contains(token);
	}

	private boolean isNumber(String token) {
		try {
			Integer.parseInt(token);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean isLastWord(int i, int inputWordCount) {
		return i == inputWordCount-1;
	}

	private boolean isError(String content) {
		return content.equals("error");
	}

	private boolean isErrorStatus(ArrayList<String> result) {
		return isError(getStatus(result));
	}
	
	private boolean isIncompleteStatus(ArrayList<String> result) {
		return getStatus(result).equals("incomplete");
	}
	
	private boolean isNotValidPriority(String token){
		int priorityLevelMin = 1;
		int priorityLevelMax = 5;
		if (isNumber(token)) {
			int intToken = Integer.parseInt(token);
			if (intToken >= priorityLevelMin && intToken <= priorityLevelMax) {
				return false;
			}
			return true;
		}
		return true;
	}

	private boolean isNotValidFrequency(String token){
		return !RECUR_FREQUENCY.contains(token);
	}

	private boolean isNotValidDayOfMonth(String token){
		int maxDayOfMonth = 31;
		if (isNumber(token)){
			if (Integer.parseInt(token) <= maxDayOfMonth) {
				return false;
			}
		}
		return true;
	}

	private boolean hasQuotes(String[] inputTokens){
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
	 * This method merges all the tokens between the startIndex (including) and endIndex (excluding)
	 * @return the merged tokens as a string
	 */
	private String mergeTokens(String[] tokens, int startIndex, int endIndex) {
		String merged = "";
		for (int i = startIndex; i < endIndex; i++) {
			String token = tokens[i];
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

	private ArrayList<String> makeCommandOnlyResult(String command){
		return new ArrayList<String>( Arrays.asList( command ) );
	}

	private ArrayList<String> makeCommandWithContentResult(String command, String content){
		return new ArrayList<String>( Arrays.asList( command, content ) );
	}

	private ArrayList<String> makeDominantResult(String firstWord, String secondWord, String content) {
		String command = firstWord;
		String index = secondWord;
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
			return makeCommandWithContentResult(command, content);
		}
		if (isCommandButCannotBeInOneShot(command)) { //if first word is delete, done, or undone 
			if (isNumber(index)) {
				return makeCommandWithContentResult(command, index);
			} else {
				return makeErrorResult("InvalidIndexError", content);
			}
		}
		return new ArrayList<String>( Arrays.asList( "incomplete" ));
	}

	private ArrayList<String> makeShowResult(String command, String content) {
		String taskType = getTaskType(content);
		if (isError(taskType)) {
			return makeErrorResult("InvalidTaskTypeError", content);
		} else {
			return new ArrayList<String>( Arrays.asList( command+taskType ) );
		}
	}

	private ArrayList<String> makeSortResult(String command, String content) {
		String field = getSortField(convertVariantToDefault(content));
		if (isError(field)) {
			return makeErrorResult("InvalidSortFieldError", content);
		} else {
			return new ArrayList<String>( Arrays.asList( command+field ) );
		}
	}
	
	private ArrayList<String> makeResetResult(String command, String index, String content) {
		storeToken(index);
		storeCommand(command);
		String[] contentTokens = content.split(" ");
		content = mergeTokens(contentTokens, 1, contentTokens.length);
		if (content.isEmpty()) {
			return makeErrorResult("EmptyFieldError", command);
		} else {
			String fieldToReset = convertVariantToDefault(content);
			if (isCommandThatCanBeReset(fieldToReset)) {
				storeToken(fieldToReset);
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
		String index = fieldContent.get("task");
		String content = fieldContent.get(command);
		
		if (command.equals("every")) {
			return makeRecurringResult(command, index, content);
		} 	
		if (command.equals("event")) {
			return makeEventResult(command, index, content);
		}
		
		if (command.equals("deadline") || command.equals("reminder")) {
			ArrayList<String> parseResult = makeDateResult(command, content);
			if (isErrorStatus(parseResult)) {
				return makeErrorResult("InvalidDateError", content);
			}
			String parsedDate = getLast(parseResult);
			content = parsedDate;
		}
		if (command.equals("priority") && isNotValidPriority(content)) {
			return makeErrorResult("InvalidPriorityError", content);
		}
		return new ArrayList<String>( Arrays.asList(command, index, content) );
	}

	private ArrayList<String> makeDateResult(String command, String date){
		if (isInvalidDate(date)) {
			return makeErrorResult("InvalidDateError", date);
		}
		if (hasNoTime(date)) {
			if (command.equals("deadline")) {
				date += " 23:59";
			} else if (command.equals("reminder")){
				date += " 12:00";
			}
		}
		String parsedDate = parseDate(date);
		fieldContent.put(command, parsedDate);
		return new ArrayList<String>( Arrays.asList("OK", parsedDate));
	}
	
	private ArrayList<String> makeEventResult(String command, String index, String event) {
		if (event.endsWith("to")) {
			return makeErrorResult("NoEndDateError", event);
		}
		ArrayList<String> parsedEvent = getEventStartAndEnd(event);
		if (isErrorStatus(parsedEvent)) {
			return parsedEvent;
		}
		
		String eventStart = getFirst(parsedEvent);
		String eventEnd = getLast(parsedEvent);
		fieldContent.put("eventStart", eventStart);
		fieldContent.put("eventEnd", eventEnd);
		
		return new ArrayList<String>( Arrays.asList(command, index, eventStart, eventEnd) );
	}

	private ArrayList<String> makeRecurringResult(String command, String name, String content){
		String freq = "";
		String[] fieldTokens = content.split(" ");
		freq = getFirst(content);
		if (isNotValidFrequency(freq)) {
			return makeErrorResult("InvalidFrequencyError", freq);
		} 
		content = mergeTokens(fieldTokens, 1, fieldTokens.length);
		if (freq.equals("month")) {
			String dayOfMonth = getNumber(content);
			if (isNotValidDayOfMonth(dayOfMonth)) {
				return makeErrorResult("InvalidDayOfMonthError", dayOfMonth);
			} else {
				dayOfMonth += " of month";
				return new ArrayList<String> ( Arrays.asList(command, name, dayOfMonth) );
			}
		}
		if (isInvalidDate(content)) {
			return makeErrorResult("InvalidDateError", content);
		}
		if (hasNoTime(content)) {
			content += " 12:00";
		}
		String date = parseDate(content);
		date = changeToRecurFormat(freq, date);
		return new ArrayList<String> ( Arrays.asList(command, name, date) );
	}
	
	/**
	 * This method creates a long result (for one-shot commands)
	 */
	private ArrayList<String> makeMultiFieldResult() {
		String command = fieldContent.get("command");
		String index = fieldContent.get("task");
		String deadline = fieldContent.get("deadline");
		String event = fieldContent.get("event");
		String reminder = fieldContent.get("reminder");
		String priority = fieldContent.get("priority");
		if (command.equals("add")) {
			fields.remove("rename");
		}
		
		if (!priority.isEmpty() && isNotValidPriority(priority)) {
			return makeErrorResult("InvalidPriorityError", priority);
		}
		if (!reminder.isEmpty()) {
			ArrayList<String> parseResult = makeDateResult("reminder", reminder);
			if (isErrorStatus(parseResult)) {
				return makeErrorResult("InvalidDateError", reminder);
			}
		}
		if (!deadline.isEmpty() && !event.isEmpty()){
			return makeErrorResult("ConflictingDatesError", event);
		}
		
		if (!deadline.isEmpty()) {
			return makeMultiFieldResultWithDeadline(command, deadline);
		} else if (!event.isEmpty()) {
			return makeMultiFieldResultWithEventDate(command, index, event);
		} else {
			if (command.equals("add")) {
				command += "F";
				fieldContent.put("command", command);
			}
			fields.remove("deadline");
			fields.remove("eventStart");
			fields.remove("eventEnd");
			return putFieldContentInResult();
		}
	}

	private ArrayList<String> makeMultiFieldResultWithDeadline(String command, String deadline) {
		command += "T";
		fieldContent.put("command", command);
		fields.remove("eventStart");
		fields.remove("eventEnd");
		
		ArrayList<String> parseResult = makeDateResult("deadline", deadline);
		if (isErrorStatus(parseResult)) {
			return parseResult;
		}
		return putFieldContentInResult();
	}

	private ArrayList<String> makeMultiFieldResultWithEventDate(String command, String index, String event) {
		command += "E";
		fieldContent.put("command", command);
		fields.remove("deadline");
		
		ArrayList<String> parseResult = makeEventResult("event", index, event);
		if (isErrorStatus(parseResult)) {
			return parseResult;
		}
		return putFieldContentInResult();
	}
	
	private ArrayList<String> makeErrorResult(String error, String token) {
		fields = new ArrayList<String>(FIELDS_DEFAULT);
		ArrayList<String> result = new ArrayList<String>(); 
		result.add("error");
		
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
			case "ConflictingDatesError":
				result.add(error + ": Task cannot have both deadline and event date");
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

	private ArrayList<String> putFieldContentInResult() {
		ArrayList<String> result = new ArrayList<String>();
		for (String field: fields) {
			String para = fieldContent.get(field);
			result.add(para);
		}
		return result;
	}

	private void resetContents() {
		seenCommands.clear();
		fields = new ArrayList<String>(FIELDS_DEFAULT);
		for (String key: fieldContent.keySet()){
			fieldContent.put(key, "");
		}
	}

	/**
	 * This method checks that a date string is valid and parses it into the default date format 
	 */
	private String parseDate(String date) {
		if (date.isEmpty()) {
			return date;
		}
		if (isInvalidDate(date)) {
			return "error";
		}
		date = swapDayAndMonth(date);
		
		String parsedDate = dateParser.parse(date).get(0).getDates().toString();
		parsedDate = parsedDate.substring(1, parsedDate.length()-1); //remove brackets
		parsedDate = standardizeDateFormat(parsedDate);
		parsedDate = confirmDateIsInFuture(parsedDate);
		parsedDate = removeMinuteIfZero(parsedDate);
		
		return parsedDate;
	}

	private String standardizeDateFormat(String dateString) {
		SimpleDateFormat nattyFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		SimpleDateFormat standardFormat = new SimpleDateFormat("EEE, dd MMM yy, h:mma");
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
		
		mmddyyDate = removeEndSpaces(mmddyyDate);
		return mmddyyDate;
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

	private String changeToRecurFormat(String freq, String dateString) {
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

	private ArrayList<String> parseEventStart(String eventStart) {
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

	private ArrayList<String> parseEventEnd(String eventEnd) {
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
		
		if (timeTokens.length == 1) {
			if (isAM(timeString)) {
				timeTokens = timeString.split("am");
			} else {
				timeTokens = timeString.split("pm");
			}
		}
		
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

	/**
	 * This method gets the individual start and end date/time of an event and put them into separate fields
	 * @return the start and end date/time in an array
	 */
	private ArrayList<String> getEventStartAndEnd(String event) {
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

	private boolean hasNoDate(String eventString) {
		if (eventString.split("/").length > 1 || eventString.split("-").length > 1) {
			return false;
		} else {
			ArrayList<String> tokens = new ArrayList<String>( Arrays.asList(eventString.split(" ") ));
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
	private boolean hasNoTime(String eventString){
		String[] eventTokens;
		eventTokens = eventString.split(" ");
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

	private boolean hasMinute(String time){
		return time.split(":").length > 1;
	}

	private boolean isInvalidDate(String dateString){
		if (dateString.isEmpty()) {
			return false;
		}
		try {
			dateParser.parse(dateString).get(0);
			return false;
		} catch (Exception e){
			return true;
		}
	}

	private boolean isAM(String time){
		return time.toLowerCase().endsWith("am");
	}

	private boolean isPM(String time){
		return time.toLowerCase().endsWith("pm");
	}

	private boolean isDayOfWeek(String token) {
		return DAYS_OF_WEEK.contains(token.toLowerCase());
	}

	private boolean isMonth(String token) {
		return MONTHS.contains(token.toLowerCase());
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

	private boolean isValid12HourTime(String token) {
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
}
