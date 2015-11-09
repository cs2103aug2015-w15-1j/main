package main.java.backend.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * ParserVault
 * Stores the contents of the parsed input and generates the result
 * @@author A0121795B
 */
public class ParserVault extends ParserSkeleton{
	
	private DateParser dateParser = new DateParser();

	//List of command words that have appeared in the parsed tokens
	private ArrayList<String> seenCommands = new ArrayList<String>();

	//Command words that only need the task's name/index, no other content needed
	private final ArrayList<String> COMMANDS_NEED_TASK_ONLY = new ArrayList<String>( Arrays.asList(
	COMMAND_ADD, COMMAND_DELETE, COMMAND_DONE) );
	
	//Variants or abbreviations of some of the command words
    private HashMap<String, ArrayList<String>> command_families = new HashMap<String, ArrayList<String>>(){
		static final long serialVersionUID = 1L; {
		put(COMMAND_DEADLINE, new ArrayList<String>( Arrays.asList("by", "dea")));
		put(COMMAND_DELETE, new ArrayList<String>( Arrays.asList("del")));
        put(COMMAND_DESCRIPTION, new ArrayList<String>( Arrays.asList("des")));
        put(COMMAND_EVENTSTART, new ArrayList<String>( Arrays.asList("from"))); 
        put(COMMAND_RECUR, new ArrayList<String>( Arrays.asList("recur", "recurring"))); 
        put(COMMAND_FILEPATH, new ArrayList<String>( Arrays.asList("fp")));
        put(COMMAND_PRIORITY, new ArrayList<String>( Arrays.asList("pri")));
        put(COMMAND_REMINDER, new ArrayList<String>( Arrays.asList("rem")));
    }};
	
    //All the fields that are used in ParserVault
	private final String FIELD_ALL = "all";
	private final String FIELD_RESULTTYPE = "resultType";
	private final String FIELD_TASK = "task";
	private final String FIELD_EVENTSTART = "eventStart";
	private final String FIELD_EVENTEND = "eventEnd";
	private final String FIELD_DESCRIPTION = COMMAND_DESCRIPTION;
	private final String FIELD_DEADLINE = COMMAND_DEADLINE;
	private final String FIELD_EVENT = COMMAND_EVENTSTART;
	private final String FIELD_PRIORITY = COMMAND_PRIORITY;
	private final String FIELD_REMINDER = COMMAND_REMINDER;
	private final String FIELD_RECUR = COMMAND_RECUR;
	private final String FIELD_RENAME = COMMAND_RENAME;
	private final String FIELD_RESET = COMMAND_RESET;
	private final String FIELD_DATE = "date";
	private final String FIELD_DATE_ABBR = "D";
	private final String FIELD_NAME = "name";
	private final String FIELD_NAME_ABBR = "N";
	private final String FIELD_PRIORITY_ABBR = "P";
	
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

	//Fields that can be reset
	private final ArrayList<String> FIELDS_CAN_RESET = new ArrayList<String>( 
	Arrays.asList(FIELD_ALL, FIELD_DESCRIPTION, FIELD_DATE, FIELD_DEADLINE, FIELD_RECUR, FIELD_EVENT, FIELD_PRIORITY, FIELD_REMINDER) );	
    
	//The task types that are recognised by Parser
	private final String TASKTYPE_TODO = "TODO";
	private final String TASKTYPE_EVENT = "EVENT";
	private final String TASKTYPE_FLOATING = "FLOATING";
	private final String TASKTYPE_OVERDUE = "OVERDUE";
	private final String TASKTYPE_TODAY = "TODAY";
	private final String TASKTYPE_DONE = "DONE";
	private final String TASKTYPE_COMPLETE = "COMPLETE";
	
	//The abbreviations (ABBR) of the task types
	private final String TASKTYPE_TODO_ABBR = "T";
	private final String TASKTYPE_EVENT_ABBR = "E";
	private final String TASKTYPE_FLOATING_ABBR = "F";
	private final String TASKTYPE_OVERDUE_ABBR = "O";
	private final String TASKTYPE_TODAY_ABBR = "D";
	private final String TASKTYPE_COMPLETE_ABBR = "C";
    
	/**
	 * This method marks the command as seen and sets the result type
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
	 * This method stores the growing token into the field content and reset the growing token
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

	String getResultType() {
		return getContent(FIELD_RESULTTYPE);
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
		if (isCommandThatNeedWords(command)) { //if first word is filepath or search
			return makeCommandWithContentResult(command, content);
		} else { //if first word is delete, done, or undone 
			if (isNumber(index)) {
				return makeCommandWithContentResult(command, index);
			} else {
				return makeErrorResult(ERROR.INVALID_INDEX, content);
			}
		}
	}

	/**
	 * This method creates a result that only has one field (for non one-shot commands)
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
			return makeErrorResult(ERROR.INVALID_PRIORITY, content);
		}
		return new ArrayList<String>( Arrays.asList(resultType, index, content) );
	}

	/**
	 * This method creates a result that has multiple fields (for one-shot commands)
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
			return makeErrorResult(ERROR.INVALID_PRIORITY, priority);
		}
		if (!reminder.isEmpty()) {
			ArrayList<String> parseResult = makeDateResult(FIELD_REMINDER, reminder);
			if (isErrorStatus(parseResult)) {
				return makeErrorResult(ERROR.INVALID_DATE, reminder);
			}
		}
		if (!recur.isEmpty()) {
			if (deadline.isEmpty() && event.isEmpty() && resultType.equals(RESULTTYPE_ADD)) {
				return makeErrorResult(ERROR.NO_DATE_FOR_RECURRENCE, recur);
			}
			ArrayList<String> parsedResult = makeRecurringResult(resultType, index, recur);
			if (isErrorStatus(parsedResult)) {
				return parsedResult;
			} else {
				recur = getLast(parsedResult);
				storeContent(FIELD_RECUR, recur);
			}
		}
		if (!deadline.isEmpty() && !event.isEmpty()){
			return makeErrorResult(ERROR.CONFLICTING_DATES, event);
		}
		
		if (!deadline.isEmpty()) {
			return makeMultiFieldResultWithDeadline(resultType, deadline);
		} else if (!event.isEmpty()) {
			return makeMultiFieldResultWithEventDate(resultType, index, event);
		} else {
			if (resultType.equals(RESULTTYPE_ADD)) {
				resultType += TASKTYPE_FLOATING_ABBR;
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
	 * This method splits the event date/time to get the start and end date/time
	 * and stores them under separate fields
	 * @return the start and end date/time in an array
	 */
	private ArrayList<String> getEventStartAndEnd(String event) {
		String[] eventTokens = event.split(" " + COMMAND_EVENTEND + " ", 2);
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
		
		return new ArrayList<String>( Arrays.asList(eventStart, eventEnd));
	}

	private ArrayList<String> makeShowResult(String command, String content) {
		String taskType = getTaskType(content);
		if (isError(taskType)) {
			return makeErrorResult(ERROR.INVALID_TASKTYPE, content);
		} else {
			return new ArrayList<String>( Arrays.asList( command+taskType ) );
		}
	}

	private ArrayList<String> makeSortResult(String command, String content) {
		String field = getSortField(convertVariantToDefault(content));
		if (isError(field)) {
			return makeErrorResult(ERROR.INVALID_SORTFIELD, content);
		} else {
			return new ArrayList<String>( Arrays.asList( command+field ) );
		}
	}

	private ArrayList<String> makeResetResult(String command, String index, String content) {
		storeToken(index);
		storeCommand(command);
		String[] contentTokens = content.split(SPACE_OF_ANY_LENGTH);
		content = mergeTokens(contentTokens, 1, contentTokens.length);
		if (content.isEmpty()) {
			return makeErrorResult(ERROR.EMPTY_FIELD, command);
		} else {
			String fieldToReset = convertVariantToDefault(content);
			if (isCommandThatCanBeReset(fieldToReset)) {
				if (fieldToReset.equals(FIELD_DEADLINE) || fieldToReset.equals(FIELD_EVENT)) {
					fieldToReset = FIELD_DATE;
				}
				storeToken(fieldToReset);
				return makeSingleFieldResult();
			} else {
				return makeErrorResult(ERROR.INVALID_RESET, content);
			}
		}
	}

	private ArrayList<String> makeDateResult(String field, String date){
		ArrayList<String> dateValidity = dateParser.isInvalidDate(date);
		if (isErrorStatus(dateValidity)){
			return dateValidity;
		}
		if (dateParser.hasNoTime(date)) {
			if (field.equals(FIELD_DEADLINE)) {
				date += " " + DEFAULT_DEADLINE;
			} else if (field.equals(FIELD_REMINDER)){
				date += " " + DEFAULT_STARTTIME;
			}
		}
		
		String parsedDate = dateParser.parseDate(date);
		storeContent(field, parsedDate);
		return new ArrayList<String>( Arrays.asList(STATUS_OKAY, parsedDate));
	}

	private ArrayList<String> makeEventResult(String command, String index, String event) {
		if (event.endsWith(COMMAND_EVENTEND)) {
			return makeErrorResult(ERROR.NO_END_DATE, event);
		}
		ArrayList<String> parsedEvent = getEventStartAndEnd(event);
		if (isErrorStatus(parsedEvent)) {
			return parsedEvent;
		}
		
		String eventStart = getFirst(parsedEvent);
		String eventEnd = getLast(parsedEvent);
		storeContent(FIELD_EVENTSTART, eventStart);
		storeContent(FIELD_EVENTEND, eventEnd);
		
		return new ArrayList<String>( Arrays.asList(command, index, eventStart, eventEnd) );
	}

	private ArrayList<String> makeRecurringResult(String command, String name, String content){
		String interval = "";
		String freq = "";
		if (isNumber(getFirst(content))) {
			interval = getFirst(content);
			freq = getSecond(content);
		} else {
			interval = "1"; //eg. every day --> every 1 day
			freq = getFirst(content);
		}

		if (isNotValidFrequency(freq)) {
			return makeErrorResult(ERROR.INVALID_FREQUENCY, freq);
		}
		freq = removePluralOrPastTense(freq);
		return new ArrayList<String> ( Arrays.asList(command, name, interval + " " + freq) );
	}

	private ArrayList<String> makeMultiFieldResultWithDeadline(String resultType, String deadline) {
		resultType += TASKTYPE_TODO_ABBR;
		storeContent(FIELD_RESULTTYPE, resultType);
		fields.remove(FIELD_EVENTSTART);
		fields.remove(FIELD_EVENTEND);
		
		ArrayList<String> parseResult = makeDateResult(FIELD_DEADLINE, deadline);
		if (isErrorStatus(parseResult)) {
			return parseResult;
		}
		return putFieldContentInResult();
	}

	private ArrayList<String> makeMultiFieldResultWithEventDate(String resultType, String index, String event) {
		resultType += TASKTYPE_EVENT_ABBR;
		storeContent(FIELD_RESULTTYPE, resultType);
		fields.remove(FIELD_DEADLINE);
		
		ArrayList<String> parseResult = makeEventResult(FIELD_EVENTSTART, index, event);
		if (isErrorStatus(parseResult)) {
			return parseResult;
		}
		return putFieldContentInResult();
	}

	/**
	 * This method checks if any part of the search content can be interpreted as a date
	 * and convert that part into standardized date formats
	 */
	private String parseSearchContent(String content) {
		String[] contentTokens = content.split(SPACE_OF_ANY_LENGTH);
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
			int dateLength = date.split(SPACE_OF_ANY_LENGTH).length;
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
		token = removePluralOrPastTense(token);
		return !DATE_FREQUENCY.contains(token);
	}
	
	private String getTaskType(String token){
		token = removePluralOrPastTense(token);
		switch (token.toUpperCase()) {
			case TASKTYPE_TODO: 
			case TASKTYPE_TODO_ABBR:
				return TASKTYPE_TODO_ABBR;
			case TASKTYPE_EVENT:
			case TASKTYPE_EVENT_ABBR:
				return TASKTYPE_EVENT_ABBR;
			case TASKTYPE_FLOATING:
			case TASKTYPE_FLOATING_ABBR:
				return TASKTYPE_FLOATING_ABBR;
			case TASKTYPE_OVERDUE:
			case TASKTYPE_OVERDUE_ABBR:
				return TASKTYPE_OVERDUE_ABBR;
			case TASKTYPE_TODAY:
			case TASKTYPE_TODAY_ABBR:
				return TASKTYPE_TODAY_ABBR;
			case TASKTYPE_DONE:
			case TASKTYPE_COMPLETE:	
			case TASKTYPE_COMPLETE_ABBR:
				return TASKTYPE_COMPLETE_ABBR;
			default:
				return STATUS_ERROR;
		}
	}

	private String getSortField(String token){
		token = removePluralOrPastTense(token);
		switch (token) {
			case FIELD_DATE:
			case "by " + FIELD_DATE:
			case FIELD_DEADLINE:
			case "by " + FIELD_DEADLINE:
				return FIELD_DATE_ABBR;
			case FIELD_NAME:
			case "by " + FIELD_NAME:
				return FIELD_NAME_ABBR;
			case FIELD_PRIORITY:
			case "by " + FIELD_PRIORITY:
				return FIELD_PRIORITY_ABBR;
			default:
				switch (token.toUpperCase()) {
					case FIELD_DATE_ABBR:
						return FIELD_DATE_ABBR;
					case FIELD_NAME_ABBR:
						return FIELD_NAME_ABBR;
					case FIELD_PRIORITY_ABBR:
						return FIELD_PRIORITY_ABBR;
					default:
						return STATUS_ERROR;
				}
		}
	}

	private String removeQuotes(String token){
		if (token.startsWith(QUOTATION_MARK) && token.endsWith(QUOTATION_MARK)) {
			token =  token.substring(1, token.length()-1);
		} 
		return token;
	}
	
	@Override
	ArrayList<String> makeErrorResult(ERROR error, String token) {
		ArrayList<String> result = new ArrayList<String>(); 
		result.add(STATUS_ERROR);
		
		switch (error) {
			case INVALID_INDEX:
				result.add("InvalidIndexError: '" + token + "' is not recognised as an index");
				break;
			case EMPTY_FIELD:
				result.add("EmptyFieldError: Please enter content for the command '" + token + "'");
				break;
			case NO_END_DATE:
				result.add("NoEndDateError: Please enter an end date after the command word 'to'");
				break;
			case INVALID_PRIORITY:
				result.add("InvalidPriorityError: '" + token + "' is not between 1 to 5");
				break;
			case INVALID_DATE:
				result.add("InvalidDateError: '" + token + "' is not an acceptable date format");
				break;
			case CONFLICTING_DATES:
				result.add("ConflictingDatesError: Task cannot have both deadline and event date");
				break;
			case INVALID_FREQUENCY:
				result.add("InvalidFrequencyError: Please enter 'day'/'week'/'month'/'year' after 'every' to indicate the frequency");
				break;
			case INVALID_TASKTYPE:
				result.add("InvalidTaskTypeError: '" + token + "' is not a valid task type "
						+ "(please enter 'todo', 'event', 'floating', 'today', 'overdue' or 'complete')");
				break;
			case INVALID_SORTFIELD:
				result.add("InvalidSortFieldError: '" + token + "' is not a valid sort field "
						+ "(please enter 'date', 'name' or 'priority')");
				break;
			case INVALID_RESET:
				result.add("InvalidResetError: '" + token + "' is not a field that can be reset");
				break;
			case NO_DATE_FOR_RECURRENCE:
				result.add("NoDateForRecurrenceError: Cannot make floating task recur. Please set a deadline or start date for the task");
				break;
			default:
				break; 
		}
		return result;
	}
}
