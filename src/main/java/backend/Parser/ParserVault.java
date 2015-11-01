package main.java.backend.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ParserVault extends ParserSkeleton{
	
	private DateParser dateParser = new DateParser();

	ParserVault() {
		//Force Natty parser to be initialized by running dateParser once
    	String pi = "31/4/15 9:26";
    	dateParser.parseDate(pi);
	}

	//The default list of fields and the order in which their contents are put into result
	private final ArrayList<String> FIELDS_DEFAULT = new ArrayList<String>( Arrays.asList(
	"command", "task", "description", "deadline", "eventStart", "eventEnd", "priority", "reminder", "category", "rename") );
	
	//List of fields that are used in the current result
	private ArrayList<String> fields = new ArrayList<String>(FIELDS_DEFAULT);
	
	//Stores the fields and their contents
	private HashMap<String, String> fieldContent = new HashMap<String, String>(){
		static final long serialVersionUID = 1L; {
	    put("command",""); put("task", ""); put("description", ""); put("deadline", ""); put("event", ""); 
	    put("priority",""); put("reminder", ""); put("category", ""); put("every", ""); put("rename", ""); put("reset", "");
	}};

	//List of commands that have appeared in the parsed tokens
	private ArrayList<String> seenCommands = new ArrayList<String>();

	//Commands that only need the task's name/index, no other content needed
	private final ArrayList<String> COMMANDS_NEED_TASK_ONLY = new ArrayList<String>( Arrays.asList(
	"add", "addcat", "delete", "done") );

	//Command fields that can be edited/reset
	private final ArrayList<String> COMMANDS_CAN_RESET = new ArrayList<String>( 
	Arrays.asList("all", "description", "deadline", "event", "priority", "reminder", "category") );	
	
	//How often a recurring task can recur
	private final ArrayList<String> RECUR_FREQUENCY = new ArrayList<String>( Arrays.asList(
	"day", "week", "month", "year") );
	
	//Contains the variants or short forms of some of the commands
    private HashMap<String, ArrayList<String>> command_families = new HashMap<String, ArrayList<String>>(){
		static final long serialVersionUID = 1L; {
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
	 * This method marks the command as seen and stores it as the main command (if there isn't one) 
	 */
	void storeCommand(String token) {
		String command = getContent("command");
		seenCommands.add(token);
	
		if (command.isEmpty()) {
			storeContent("command", token);
		} else if (!isCommandThatNeedTaskOnly(command)) {
			storeContent("command", "set");
		}
	}

	/**
	 * This method stores the token into the field content and reset the growing token
	 */
	String storeToken(String token) {
		if (!token.isEmpty()) {
			token = removeEndSpacesOrBrackets(token);
			token = removeQuotes(token);
			String lastCommand = getLast(seenCommands);
			String field;
			
			if (getContent("task").isEmpty()) {
				field = "task";
			} else if (isCommandThatNeedTaskOnly(lastCommand)) {
				field = "task";
			} else {
				field = lastCommand;
			}
			
			String content = getContent(field);
			if (content.isEmpty()) {
				storeContent(field, token);
			} else {
				storeContent(field, content + " " + token);
			}
		}
		return ""; //reset growingToken
	}

	void resetContents() {
		seenCommands.clear();
		fields = new ArrayList<String>(FIELDS_DEFAULT);
		for (String key: fieldContent.keySet()){
			storeContent(key, "");
		}
	}

	/**
	 * This methods checks if token is a command variant (if yes, convert it to the default command)
	 */
	String convertVariantToDefault(String token) {
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

	String getContent(String field) {
		return fieldContent.get(field);
	}
	
	String getContentOfCommand(String lastCommandSeen) {
		if (isCommandThatNeedTaskOnly(lastCommandSeen)) {
			return getContent("task");
		} else {
			return getContent(lastCommandSeen);
		}
	}

	String getLastSeenCommand(){
		return getLast(seenCommands);
	}

	boolean isSeenCommand(String token) {
		return seenCommands.contains(token);
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

	/**
	 * This method creates a short result (for non one-shot commands)
	 */
	ArrayList<String> makeSingleFieldResult() {
		String command = getContent("command");
		String index = getContent("task");
		String content = getContent(command);
		
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

	/**
	 * This method creates a long result (for one-shot commands)
	 */
	ArrayList<String> makeMultiFieldResult() {
		String command = getContent("command");
		String index = getContent("task");
		String deadline = getContent("deadline");
		String event = getContent("event");
		String reminder = getContent("reminder");
		String priority = getContent("priority");
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
				storeContent("command", command);
			}
			fields.remove("deadline");
			fields.remove("eventStart");
			fields.remove("eventEnd");
			return putFieldContentInResult();
		}
	}

	private void storeContent(String field, String content) {
		fieldContent.put(field, content);
	}

	private ArrayList<String> putFieldContentInResult() {
		ArrayList<String> result = new ArrayList<String>();
		for (String field: fields) {
			String para = getContent(field);
			result.add(para);
		}
		return result;
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
			eventEnd = removeEndSpacesOrBrackets(getLast(eventTokens));
		}
		
		ArrayList<String> parsedStartResult = dateParser.parseEventStart(eventStart);
		if (isErrorStatus(parsedStartResult)) {
			return parsedStartResult;
		}
		eventStart = getLast(parsedStartResult);
		
		ArrayList<String> parsedEndResult = dateParser.parseEventEnd(eventStart, eventEnd);
		if (isErrorStatus(parsedEndResult)) {
			return parsedEndResult;
		}
		eventEnd = getLast(parsedEndResult);
		
		//eventEnd = dateParser.makeEventEndComplete(eventStart, eventEnd);
		
		return new ArrayList<String>( Arrays.asList(eventStart, eventEnd));
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

	private ArrayList<String> makeDateResult(String command, String date){
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
		storeContent(command, parsedDate);
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
		storeContent("eventStart", eventStart);
		storeContent("eventEnd", eventEnd);
		
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
				return makeErrorResult("InvalidDayOfMonthError", content);
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

	private ArrayList<String> makeMultiFieldResultWithDeadline(String command, String deadline) {
		command += "T";
		storeContent("command", command);
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
		storeContent("command", command);
		fields.remove("deadline");
		
		ArrayList<String> parseResult = makeEventResult("event", index, event);
		if (isErrorStatus(parseResult)) {
			return parseResult;
		}
		return putFieldContentInResult();
	}

	private boolean isCommandThatCanBeReset(String token) {
		return COMMANDS_CAN_RESET.contains(token);
	}

	private boolean isCommandThatNeedTaskOnly(String token){
		return COMMANDS_NEED_TASK_ONLY.contains(token);
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

	private String removeQuotes(String token){
		if (token.startsWith("\"") && token.endsWith("\"")) {
			token =  token.substring(1, token.length()-1);
		} 
		return token;
	}
	
	@Override
	ArrayList<String> makeErrorResult(String error, String token) {
		ArrayList<String> result = new ArrayList<String>(); 
		result.add("error");
		
		switch (error) {
			case "InvalidIndexError":
				result.add(error + ": '" + token + "' is not recognised as an index");
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
}
