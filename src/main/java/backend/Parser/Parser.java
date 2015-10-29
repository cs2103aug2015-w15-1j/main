package main.java.backend.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Parser extends ParserSkeleton{
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
    
    private DateParser dateParser = new DateParser();
    
    public Parser(){
    	//Force Natty parser to initialize by running dateParser once
    	String pi = "31/4/15 9:26";
    	dateParser.parseDate(pi);
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
			} else if (isCommandThatAddStuff(lastCommand)) {
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

	private boolean isLastWord(int i, int inputWordCount) {
		return i == inputWordCount-1;
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
		fieldContent.put(command, parsedDate);
		return new ArrayList<String>( Arrays.asList("OK", parsedDate));
	}
	
	private ArrayList<String> makeEventResult(String command, String index, String event) {
		if (event.endsWith("to")) {
			return makeErrorResult("NoEndDateError", event);
		}
		ArrayList<String> parsedEvent = dateParser.getEventStartAndEnd(event);
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
}
