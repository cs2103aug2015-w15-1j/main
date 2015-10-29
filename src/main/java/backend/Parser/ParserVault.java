package main.java.backend.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ParserVault extends ParserSkeleton{
	
	//The default list of fields and the order in which their contents are put into result
	private final ArrayList<String> FIELDS_DEFAULT = new ArrayList<String>( Arrays.asList(
	"command", "task", "description", "deadline", "eventStart", "eventEnd", "priority", "reminder", "category", "rename") );
	
	//List of fields that are used in the current result
	private ArrayList<String> fields = new ArrayList<String>(FIELDS_DEFAULT);
	
	//Stores the fields and their respective contents
	private HashMap<String, String> fieldContent = new HashMap<String, String>(){
		static final long serialVersionUID = 1L; {
	    put("command",""); put("task", ""); put("description", ""); put("deadline", ""); put("event", ""); 
	    put("priority",""); put("reminder", ""); put("category", ""); put("every", ""); put("rename", ""); put("reset", "");
	}};

	//How often a recurring task can recur
	private final ArrayList<String> RECUR_FREQUENCY = new ArrayList<String>( Arrays.asList(
	"day", "week", "month", "year") );
	
	//List of commands that have appeared in the current input
	private ArrayList<String> seenCommands = new ArrayList<String>();

	
	/**
	 * This method marks the command as seen and stores it as the main command (if there isn't one) 
	 */
	void storeCommand(String token) {
		String command = getFieldContent().get("command");
		getSeenCommands().add(token);
	
		if (command.isEmpty()) {
			getFieldContent().put("command", token);
		} else if (!isCommandThatAddStuff(command)) {
			getFieldContent().put("command", "set");
		}
	}

	/**
	 * This method stores the token into the field content and reset the growing token
	 */
	String storeToken(String token) {
		if (!token.isEmpty()) {
			token = removeEndSpaces(token);
			token = removeQuotes(token);
			String lastCommand = getLast(getSeenCommands());
			String field;
			
			if (getFieldContent().get("task").isEmpty()) {
				field = "task";
			} else if (isCommandThatAddStuff(lastCommand)) {
				field = "task";
			} else {
				field = lastCommand;
			}
			
			String content = getFieldContent().get(field);
			if (content.isEmpty()) {
				getFieldContent().put(field, token);
			} else {
				getFieldContent().put(field, content + " " + token);
			}
		}
		return ""; //reset growingToken
	}

	String getContentOfCommand(String lastCommandSeen) {
		if (isCommandThatAddStuff(lastCommandSeen)) {
			return getFieldContent().get("task");
		} else {
			return getFieldContent().get(lastCommandSeen);
		}
	}
	
	boolean isSeenCommand(String token) {
		return getSeenCommands().contains(token);
	}
	
	ArrayList<String> makeCommandOnlyResult(String command){
		return new ArrayList<String>( Arrays.asList( command ) );
	}

	ArrayList<String> makeCommandWithContentResult(String command, String content){
		return new ArrayList<String>( Arrays.asList( command, content ) );
	}

	ArrayList<String> makeDominantResult(String firstWord, String secondWord, String content) {
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

	ArrayList<String> makeShowResult(String command, String content) {
		String taskType = getTaskType(content);
		if (isError(taskType)) {
			return makeErrorResult("InvalidTaskTypeError", content);
		} else {
			return new ArrayList<String>( Arrays.asList( command+taskType ) );
		}
	}

	ArrayList<String> makeSortResult(String command, String content) {
		String field = getSortField(convertVariantToDefault(content));
		if (isError(field)) {
			return makeErrorResult("InvalidSortFieldError", content);
		} else {
			return new ArrayList<String>( Arrays.asList( command+field ) );
		}
	}

	ArrayList<String> makeResetResult(String command, String index, String content) {
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
	ArrayList<String> makeSingleFieldResult() {
		String command = getFieldContent().get("command");
		String index = getFieldContent().get("task");
		String content = getFieldContent().get(command);
		
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

	ArrayList<String> makeDateResult(String command, String date){
		if (dateParser.isInvalidDate(date)) {
			return makeErrorResult("InvalidDateError", date);
		}
		if (dateParser.hasNoTime(date)) {
			if (command.equals("deadline")) {
				date += " 23:59";
			} else if (command.equals("reminder")){
				date += " 12:00";
			}
		}
		String parsedDate = dateParser.parseDate(date);
		getFieldContent().put(command, parsedDate);
		return new ArrayList<String>( Arrays.asList("OK", parsedDate));
	}

	ArrayList<String> makeEventResult(String command, String index, String event) {
		if (event.endsWith("to")) {
			return makeErrorResult("NoEndDateError", event);
		}
		ArrayList<String> parsedEvent = dateParser.getEventStartAndEnd(event);
		if (isErrorStatus(parsedEvent)) {
			return parsedEvent;
		}
		
		String eventStart = getFirst(parsedEvent);
		String eventEnd = getLast(parsedEvent);
		getFieldContent().put("eventStart", eventStart);
		getFieldContent().put("eventEnd", eventEnd);
		
		return new ArrayList<String>( Arrays.asList(command, index, eventStart, eventEnd) );
	}

	ArrayList<String> makeRecurringResult(String command, String name, String content){
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
		if (dateParser.isInvalidDate(content)) {
			return makeErrorResult("InvalidDateError", content);
		}
		if (dateParser.hasNoTime(content)) {
			content += " 12:00";
		}
		String date = dateParser.parseDate(content);
		date = dateParser.changeToRecurFormat(freq, date);
		return new ArrayList<String> ( Arrays.asList(command, name, date) );
	}

	/**
	 * This method creates a long result (for one-shot commands)
	 */
	ArrayList<String> makeMultiFieldResult() {
		String command = getFieldContent().get("command");
		String index = getFieldContent().get("task");
		String deadline = getFieldContent().get("deadline");
		String event = getFieldContent().get("event");
		String reminder = getFieldContent().get("reminder");
		String priority = getFieldContent().get("priority");
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
				getFieldContent().put("command", command);
			}
			fields.remove("deadline");
			fields.remove("eventStart");
			fields.remove("eventEnd");
			return putFieldContentInResult();
		}
	}

	ArrayList<String> makeMultiFieldResultWithDeadline(String command, String deadline) {
		command += "T";
		getFieldContent().put("command", command);
		fields.remove("eventStart");
		fields.remove("eventEnd");
		
		ArrayList<String> parseResult = makeDateResult("deadline", deadline);
		if (isErrorStatus(parseResult)) {
			return parseResult;
		}
		return putFieldContentInResult();
	}

	ArrayList<String> makeMultiFieldResultWithEventDate(String command, String index, String event) {
		command += "E";
		getFieldContent().put("command", command);
		fields.remove("deadline");
		
		ArrayList<String> parseResult = makeEventResult("event", index, event);
		if (isErrorStatus(parseResult)) {
			return parseResult;
		}
		return putFieldContentInResult();
	}

	ArrayList<String> putFieldContentInResult() {
		ArrayList<String> result = new ArrayList<String>();
		for (String field: fields) {
			String para = getFieldContent().get(field);
			result.add(para);
		}
		return result;
	}

	void resetContents() {
		getSeenCommands().clear();
		fields = new ArrayList<String>(FIELDS_DEFAULT);
		for (String key: getFieldContent().keySet()){
			getFieldContent().put(key, "");
		}
	}

	String removeQuotes(String token){
		if (token.startsWith("\"") && token.endsWith("\"")) {
			token =  token.substring(1, token.length()-1);
		} 
		return token;
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
	
	boolean isCommandButRepressed(String token) {
		String mainCommand = getFieldContent().get("command");
		return !mainCommand.isEmpty() && (isDominatingCommand(token) || isCommandThatNoNeedContent(token));
	}

	ArrayList<String> getSeenCommands() {
		return seenCommands;
	}

	HashMap<String, String> getFieldContent() {
		return fieldContent;
	}
}
