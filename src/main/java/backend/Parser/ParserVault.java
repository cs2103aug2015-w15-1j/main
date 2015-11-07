package main.java.backend.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import main.java.backend.Storage.Task.Task.TaskType;

/**
 * ParserVault
 * Stores the contents of the parsed input and generates the result
 * @@author A0121795B
 */
public class ParserVault extends ParserSkeleton{
	
	private DateParser dateParser = new DateParser();

	ParserVault() {
		//Force Natty parser to be initialized by running dateParser once
    	String pi = "Mar 14 15 9.26pm";
    	dateParser.parseDate(pi);
	}

	private final String FIELD_ALL = "all";
	private final String FIELD_RESULTTYPE = "command";
	private final String FIELD_TASK = "task";
	private final String FIELD_DESCRIPTION = "description";
	private final String FIELD_DEADLINE = COMMAND_DEADLINE;
	private final String FIELD_EVENT = COMMAND_EVENT;
	private final String FIELD_EVENTSTART = "eventStart";
	private final String FIELD_EVENTEND = "eventEnd";
	private final String FIELD_PRIORITY = COMMAND_PRIORITY;
	private final String FIELD_REMINDER = COMMAND_REMINDER;
	private final String FIELD_RECUR = COMMAND_RECUR;
	private final String FIELD_RENAME = "rename";
	private final String FIELD_RESET = "reset";
	
	//The default list of fields and the order in which their contents are put into result
	private final ArrayList<String> FIELDS_DEFAULT = new ArrayList<String>( Arrays.asList(
	FIELD_RESULTTYPE, FIELD_TASK, FIELD_DESCRIPTION, FIELD_DEADLINE, FIELD_EVENTSTART, FIELD_EVENTEND, 
	FIELD_PRIORITY, FIELD_REMINDER, FIELD_RECUR, FIELD_RENAME ) ); 
	
	//List of fields that are used in the current result
	private ArrayList<String> fields = new ArrayList<String>(FIELDS_DEFAULT);
	
	//Stores the fields and their contents
	private HashMap<String, String> fieldContent = new HashMap<String, String>(){
		static final long serialVersionUID = 1L; {
		put(FIELD_RESULTTYPE, ""); put(FIELD_TASK, ""); put(FIELD_DESCRIPTION, ""); put(FIELD_DEADLINE, ""); 
		put(FIELD_EVENT, ""); put(FIELD_PRIORITY, ""); put(FIELD_REMINDER, ""); put(FIELD_RESET, "");
		put(FIELD_RECUR, ""); put(FIELD_RENAME, "");
	}};

	//List of commands that have appeared in the parsed tokens
	private ArrayList<String> seenCommands = new ArrayList<String>();

	//Commands that only need the task's name/index, no other content needed
	private final ArrayList<String> COMMANDS_NEED_TASK_ONLY = new ArrayList<String>( Arrays.asList(
	COMMAND_ADD, COMMAND_DELETE, COMMAND_DONE) );

	//Command fields that can be edited/reset
	private final ArrayList<String> FIELDS_CAN_RESET = new ArrayList<String>( 
	Arrays.asList(FIELD_ALL, FIELD_DESCRIPTION, FIELD_DEADLINE, FIELD_RECUR, FIELD_EVENT, FIELD_PRIORITY, FIELD_REMINDER) );	
	
	private final String FREQUENCY_DAY = "day";
	private final String FREQUENCY_WEEK = "week";
	private final String FREQUENCY_MONTH = "month";
	private final String FREQUENCY_YEAR = "year";
	
	//How often a recurring task can recur
	private final ArrayList<String> RECUR_FREQUENCY = new ArrayList<String>( Arrays.asList(
	FREQUENCY_DAY, FREQUENCY_WEEK, FREQUENCY_MONTH, FREQUENCY_YEAR) );
	
	//Contains the variants or short forms of some of the commands
    private HashMap<String, ArrayList<String>> command_families = new HashMap<String, ArrayList<String>>(){
		static final long serialVersionUID = 1L; {
		put(COMMAND_DEADLINE, new ArrayList<String>( Arrays.asList("by", "dea")));
		put(COMMAND_DELETE, new ArrayList<String>( Arrays.asList("del")));
        put(COMMAND_DESCRIPTION, new ArrayList<String>( Arrays.asList("des")));
        put(COMMAND_EVENT, new ArrayList<String>( Arrays.asList("from"))); 
        put(COMMAND_RECUR, new ArrayList<String>( Arrays.asList("recur"))); 
        put(COMMAND_FILEPATH, new ArrayList<String>( Arrays.asList("fp")));
        put(COMMAND_PRIORITY, new ArrayList<String>( Arrays.asList("pri")));
        put(COMMAND_REMINDER, new ArrayList<String>( Arrays.asList("rem")));
    }};
    
	private final String TASKTYPE_TODO = "todo";
	private final String TASKTYPE_EVENT = "event";
	private final String TASKTYPE_FLOATING = "floating";
	private final String TASKTYPE_OVERDUE = "overdue";
	private final String TASKTYPE_TODAY = "today";
	private final String TASKTYPE_DONE = "done";
    
	public void printCmd(){
		for (String s: FIELDS_CAN_RESET){
			//System.out.println("private final String FIELD_" + s.toUpperCase() + " = \"" + s + "\";");
			System.out.print("FIELD_" + s.toUpperCase() + ", ");
			//System.out.print("put(FIELD_" + s.toUpperCase() + ", \"\"); ");
		}
	}
    
	/**
	 * This method marks the command as seen and stores it as the main command (if there isn't one) 
	 */
	void storeCommand(String token) {
		String command = getContent(FIELD_RESULTTYPE);
		seenCommands.add(token);
	
		if (command.isEmpty()) {
			storeContent(FIELD_RESULTTYPE, token);
		} else if (!isCommandThatNeedTaskOnly(command)) {
			storeContent(FIELD_RESULTTYPE, RESULTTYPE_SET);
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
			
			if (getContent(FIELD_TASK).isEmpty()) {
				field = FIELD_TASK;
			} else if (isCommandThatNeedTaskOnly(lastCommand)) {
				field = FIELD_TASK;
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
			return getContent(FIELD_TASK);
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
		if (command.equals(COMMAND_SEARCH)) {
			content = parseSearchContent(content);
		}
		return new ArrayList<String>( Arrays.asList( command, content ) );
	}

	ArrayList<String> makeDominantResult(String firstWord, String secondWord, String content) {
		String command = firstWord;
		String index = secondWord;
		if (command.equals(COMMAND_SHOW)) {
			return makeShowResult(command, content);
		}
		if (command.equals(COMMAND_SORT)) {
			return makeSortResult(command, content);
		}
		if (command.equals(COMMAND_RESET)) {
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
		String resultType = getContent(FIELD_RESULTTYPE);
		String index = getContent(FIELD_TASK);
		String content = getContent(resultType);
		
		if (resultType.equals(FIELD_RECUR)) {
			return makeRecurringResult(resultType, index, content);
		} 	
		if (resultType.equals(FIELD_EVENT)) {
			return makeEventResult(resultType, index, content);
		}
		
		if (resultType.equals(FIELD_DEADLINE) || resultType.equals(FIELD_REMINDER)) {
			ArrayList<String> parseResult = makeDateResult(resultType, content);
			if (isErrorStatus(parseResult)) {
				return parseResult;
			}
			String parsedDate = getLast(parseResult);
			content = parsedDate;
		}
		if (resultType.equals(FIELD_PRIORITY) && isNotValidPriority(content)) {
			return makeErrorResult("InvalidPriorityError", content);
		}
		return new ArrayList<String>( Arrays.asList(resultType, index, content) );
	}

	/**
	 * This method creates a long result (for one-shot commands)
	 */
	ArrayList<String> makeMultiFieldResult() {
		String resultType = getContent(FIELD_RESULTTYPE);
		String index = getContent(FIELD_TASK);
		String deadline = getContent(FIELD_DEADLINE);
		String event = getContent(FIELD_EVENT);
		String reminder = getContent(FIELD_REMINDER);
		String priority = getContent(FIELD_PRIORITY);
		String recur = getContent(FIELD_RECUR);
		if (resultType.equals(RESULTTYPE_ADD)) {
			fields.remove(FIELD_RENAME);
		}
		
		if (!priority.isEmpty() && isNotValidPriority(priority)) {
			return makeErrorResult("InvalidPriorityError", priority);
		}
		if (!reminder.isEmpty()) {
			ArrayList<String> parseResult = makeDateResult(COMMAND_REMINDER, reminder);
			if (isErrorStatus(parseResult)) {
				return makeErrorResult("InvalidDateError", reminder);
			}
		}
		if (!recur.isEmpty()) {
			if (deadline.isEmpty() && event.isEmpty() && resultType.equals(RESULTTYPE_ADD)) {
				return makeErrorResult("NoDateForRecurrenceError", recur);
			}
			ArrayList<String> parsedResult = makeRecurringResult(resultType, index, recur);
			if (isErrorStatus(parsedResult)) {
				return parsedResult;
			} else {
				recur = getLast(parsedResult);
				storeContent(COMMAND_RECUR, recur);
			}
		}
		if (!deadline.isEmpty() && !event.isEmpty()){
			return makeErrorResult("ConflictingDatesError", event);
		}
		
		if (!deadline.isEmpty()) {
			return makeMultiFieldResultWithDeadline(resultType, deadline);
		} else if (!event.isEmpty()) {
			return makeMultiFieldResultWithEventDate(resultType, index, event);
		} else {
			if (resultType.equals(RESULTTYPE_ADD)) {
				resultType += "F";
				storeContent(FIELD_RESULTTYPE, resultType);
			}
			fields.remove(FIELD_DEADLINE);
			fields.remove(FIELD_EVENTSTART);
			fields.remove(FIELD_EVENTEND);
			return putFieldContentInResult();
		}
	}

	private void storeContent(String field, String content) {
		fieldContent.put(field, content);
	}

	private ArrayList<String> putFieldContentInResult() {
		ArrayList<String> result = new ArrayList<String>();
		for (String field: fields) {
			String content = getContent(field);
			result.add(content);
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
		ArrayList<String> dateValidity = dateParser.isInvalidDate(date);
		if (isErrorStatus(dateValidity)){
			return dateValidity;
		}
		if (dateParser.hasNoTime(date)) {
			if (command.equals(COMMAND_DEADLINE)) {
				date += " 23:59";
			} else if (command.equals(COMMAND_REMINDER)){
				date += " 9am";
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
		String interval = "";
		String freq = "";
		if (isNumber(getFirst(content))) {
			interval = getFirst(content);
			freq = getSecond(content);
		} else {
			interval = "1";
			freq = getFirst(content);
		}

		if (isNotValidFrequency(freq)) {
			return makeErrorResult("InvalidFrequencyError", freq);
		}
		freq = convertToDefaultFrequency(freq);
		return new ArrayList<String> ( Arrays.asList(command, name, interval + " " + freq) );
	}

	private ArrayList<String> makeMultiFieldResultWithDeadline(String resultType, String deadline) {
		resultType += "T";
		storeContent(FIELD_RESULTTYPE, resultType);
		fields.remove(FIELD_EVENTSTART);
		fields.remove(FIELD_EVENTEND);
		
		ArrayList<String> parseResult = makeDateResult(COMMAND_DEADLINE, deadline);
		if (isErrorStatus(parseResult)) {
			return parseResult;
		}
		return putFieldContentInResult();
	}

	private ArrayList<String> makeMultiFieldResultWithEventDate(String resultType, String index, String event) {
		resultType += "E";
		storeContent(FIELD_RESULTTYPE, resultType);
		fields.remove(COMMAND_DEADLINE);
		
		ArrayList<String> parseResult = makeEventResult(COMMAND_EVENT, index, event);
		if (isErrorStatus(parseResult)) {
			return parseResult;
		}
		return putFieldContentInResult();
	}

	private String parseSearchContent(String content) {
		String[] contentTokens = content.split(" ");
		String result = "";
		String date = "";
		for (String token: contentTokens) {
			if (isErrorStatus(dateParser.isInvalidDate(token))) {
				result += token + " ";
			} else {
				date += token + " ";
			}
		}
		if (!date.isEmpty()) {
			int dateLength = date.split(" ").length;
			if (dateParser.isDayOfWeek(getFirst(date)) && dateParser.hasTime(date) && dateLength == 2) {
				date = dateParser.parseAndGetDayOfWeekAndTime(date); 
			} else if (dateParser.hasDate(date) && dateParser.hasTime(date)) {
				date = dateParser.parseDate(date);
			} else if (dateParser.isMonth(date) && dateLength == 1){
				date = dateParser.parseAndGetMonth(date);
			} else if (dateParser.isDayOfWeek(date) && dateLength == 1){
				date = dateParser.parseAndGetDayOfWeek(date);
			} else if (dateParser.hasDate(date)) {
				date = dateParser.parseAndGetDayAndMonth(date);
			} else if (dateParser.hasTime(date)) {
				date = dateParser.parseAndGetTime(date);
			}
		}
		return removeEndSpacesOrBrackets(result + date);
	}

	private boolean isCommandThatCanBeReset(String token) {
		return FIELDS_CAN_RESET.contains(token);
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
		token = convertToDefaultFrequency(token);
		return !RECUR_FREQUENCY.contains(token);
	}

	private String convertToDefaultFrequency(String freq) {
		freq = freq.toLowerCase();
		if (freq.endsWith("s")) {
			freq = freq.substring(0, freq.length()-1);
		}
		return freq;
	}
	
	private String getTaskType(String token){
		if (token.endsWith("s")) {
			token = token.substring(0, token.length()-1);
		}
		switch (token.toLowerCase()) {
			case TASKTYPE_TODO: 
				return "T";
			case TASKTYPE_EVENT:
				return "E";
			case TASKTYPE_FLOATING:
				return "F";
			case TASKTYPE_OVERDUE:
				return "O";
			case TASKTYPE_TODAY:
				return "Today";
			case TASKTYPE_DONE:
				return "Done";
			default:
				return RESULTTYPE_ERROR;
		}
	}

	private String getSortField(String token){
		switch (token.toLowerCase()) {
		case COMMAND_DEADLINE:
		case "by deadline":
		case "d":
			return "D";
		case COMMAND_PRIORITY:
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
	
	//@Override
	ArrayList<String> makeErrorResult(String error, String token) {
		ArrayList<String> result = new ArrayList<String>(); 
		result.add(RESULTTYPE_ERROR);
		
		switch (error) {
			case "InvalidIndexError":
				result.add(error + ": '" + token + "' is not recognised as an index");
				break;
			case "EmptyFieldError":
				result.add(error + ": Please enter content for the command '" + token + "'");
				break;
			case "NoEndDateError":
				result.add(error + ": Please enter an end date after the command word 'to'");
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
				result.add(error + ": Please enter 'day'/'week'/'month'/'year' after 'every' to indicate the frequency");
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
			case "NoDateForRecurrenceError":
				result.add(error + ": Cannot make task recur. Please set a deadline or start date for the task");
				break;
			default:
				break; 
		}
		return result;
	}
}
