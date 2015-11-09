package main.java.backend.Parser;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ParserSkeleton
 * This is an abstract class that contains basic methods and data shared within the Parser component
 * @@author A0121795B
 */
abstract class ParserSkeleton {
	
	//Command words that are accepted by the program
	final String COMMAND_ADD = "add";
	final String COMMAND_DEADLINE = "deadline";
	final String COMMAND_DESCRIPTION = "description";
	final String COMMAND_DELETE = "delete";
	final String COMMAND_DELETEALL = "deleteAll";
	final String COMMAND_DONE = "done";
	final String COMMAND_EVENTSTART = "event";
	final String COMMAND_EVENTEND = "to";
	final String COMMAND_RECUR = "every";
	final String COMMAND_EXIT = "exit";
	final String COMMAND_FILEPATH = "filepath";
	final String COMMAND_PRIORITY = "priority";
	final String COMMAND_REDO = "redo";
	final String COMMAND_REMINDER = "reminder";
	final String COMMAND_RENAME = "rename";
	final String COMMAND_RESET = "reset";
	final String COMMAND_SEARCH = "search";
	final String COMMAND_SHOW = "show";
	final String COMMAND_SHOWCOMPLETE = "showC";
	final String COMMAND_SHOWTODAY = "showD";
	final String COMMAND_SHOWEVENT = "showE";
	final String COMMAND_SHOWFLOATING = "showF";
	final String COMMAND_SHOWOVERDUE = "showO";
	final String cOMMAND_SHOWTODO = "showT";
	final String COMMAND_SORT = "sort";
	final String COMMAND_SORTD = "sortD";
	final String COMMAND_SORTN = "sortN";
	final String COMMAND_SORTP = "sortP";
	final String COMMAND_UNDO = "undo";
	final String COMMAND_UNDONE = "undone";
	
	//List of all command words accepted by the program
	final ArrayList<String> COMMANDS = new ArrayList<String>( Arrays.asList(
	COMMAND_ADD, COMMAND_DEADLINE, COMMAND_DELETE, COMMAND_DELETEALL, COMMAND_DESCRIPTION,
	COMMAND_DONE, COMMAND_EVENTSTART, COMMAND_RECUR, COMMAND_EXIT, COMMAND_FILEPATH, COMMAND_PRIORITY, 
	COMMAND_REDO, COMMAND_REMINDER, COMMAND_RENAME, COMMAND_RESET, COMMAND_SEARCH, COMMAND_SHOW, 
	COMMAND_SHOWCOMPLETE, COMMAND_SHOWEVENT, COMMAND_SHOWFLOATING, COMMAND_SHOWOVERDUE, cOMMAND_SHOWTODO, 
	COMMAND_SHOWTODAY, COMMAND_SORT, COMMAND_SORTD, COMMAND_SORTN, COMMAND_SORTP, COMMAND_UNDO, COMMAND_UNDONE ) );
	
	//Command words that work by just typing the command word (without additional content)
	final ArrayList<String> COMMANDS_NO_CONTENT = new ArrayList<String>( Arrays.asList(
	COMMAND_DELETEALL, COMMAND_EXIT, COMMAND_REDO, COMMAND_SHOWCOMPLETE, COMMAND_SHOWEVENT, COMMAND_SHOWFLOATING, 
	COMMAND_SHOWOVERDUE, cOMMAND_SHOWTODO, COMMAND_SHOWTODAY, COMMAND_SORTD, COMMAND_SORTN, COMMAND_SORTP, COMMAND_UNDO ) );
	
	//Command words that if appear first, will prevent other command words from having effect
	final ArrayList<String> COMMANDS_DOMINATING = new ArrayList<String>( Arrays.asList(
	COMMAND_DELETE, COMMAND_DONE, COMMAND_FILEPATH, COMMAND_RESET, COMMAND_SEARCH, COMMAND_SHOW, 
	COMMAND_SORT, COMMAND_UNDONE ) );
	
	//Command words that can accept any amount of words
	final ArrayList<String> COMMANDS_NEED_WORDS = new ArrayList<String>( 
	Arrays.asList(COMMAND_ADD, COMMAND_DESCRIPTION, COMMAND_FILEPATH, COMMAND_SEARCH) );
	
	//Command words that cannot be part of a one-shot command
	final ArrayList<String> COMMANDS_NOT_ONE_SHOT = new ArrayList<String>( 
	Arrays.asList(COMMAND_DELETE, COMMAND_DONE, COMMAND_RESET, COMMAND_UNDONE) );	
	
	//The default times set by Parser if user never indicates the time
	final String DEFAULT_DEADLINE = "11:59pm";
	final String DEFAULT_STARTTIME = "9am";
	final String DEFAULT_ENDTIME = "9pm";
	
	//The date frequencies that can be used to specify a date or a task's recurrence
	final String FREQUENCY_DAY = "day";
	final String FREQUENCY_WEEK = "week";
	final String FREQUENCY_MONTH = "month";
	final String FREQUENCY_YEAR = "year";
	final ArrayList<String> DATE_FREQUENCY = new ArrayList<String>( Arrays.asList(
	FREQUENCY_DAY, FREQUENCY_WEEK, FREQUENCY_MONTH, FREQUENCY_YEAR) );
	
	//The two result types that require a multi-field result
	final String RESULTTYPE_ADD = "add";
	final String RESULTTYPE_SET = "set";
	
	//Types of statuses that can be returned by a method
	final String STATUS_ERROR = "error";
	final String STATUS_INCOMPLETE = "incomplete";
	final String STATUS_OKAY = "okay";
	
	//Java data types that are converted by Parser
	final String DATATYPE_DATE = "date";
	final String DATATYPE_INTEGER = "integer";
	final String DATATYPE_STRING = "string";
	
	//Special characters that are noted by Parser
	final String SPACE_OF_ANY_LENGTH = " +";
	final String QUOTATION_MARK = "\"";
	final String ZERO = "0";
	
	//All the errors that can be detected by Parser
	enum ERROR {
		INVALID_WORD, INVALID_COMMAND, NO_COMMAND, DUPLICATE_COMMAND, EMPTY_FIELD,
		INVALID_INDEX, NO_END_DATE, INVALID_PRIORITY, INVALID_DATE, CONFLICTING_DATES,
		INVALID_FREQUENCY, INVALID_TASKTYPE, INVALID_SORTFIELD, INVALID_RESET, 
		NO_DATE_FOR_RECURRENCE, INVALID_TIME, INVALID_DAYOFMONTH;
	}
	
	String getFirst(String[] array){
		return array[0];
	}

	String getFirst(String str){
		return str.split(" ")[0];
	}

	String getFirst(ArrayList<String> arrayList){
		return arrayList.get(0);
	}

	String getSecond(String[] array){
		if (array.length < 2) {
			return "";
		}
		return array[1];
	}
	
	String getSecond(String str){
		return str.split(" ")[1];
	}

	String getPrevious(ArrayList<String> list, String token) {
		if (list.size() > 1 && list.indexOf(token) != 0) {
			return list.get( list.indexOf(token)-1 );
		}
		return null;
	}

	String getPrevious(String[] list, int count) {
		if (count != 0) {
			return list[count - 1];
		}
		return null;
	}
	
	String getNext(String[] list, int count) {
		if (count != list.length-1) {
			return list[count + 1];
		}
		return null;
	}

	String getLast(String[] array){
		if (array.length == 0) {
			return "";
		}
		return array[array.length-1];
	}

	String getLast(String str){
		return str.split(" ")[str.split(" ").length-1];
	}
	
	String getLast(ArrayList<String> arrayList){
		if (arrayList.isEmpty()) {
			return "";
		}
		return arrayList.get(arrayList.size()-1);
	}

	String getNumber(String token){
		if (isNumber(token)){
			return token;
		} else {
			String c;
			String number = "";
			for (int i = 0; i < token.length(); i++) {
				c = token.substring(i, i+1);
				if (isNumber(c)) {
					number += c;
				}
			}
			return number;
		}
	}
	
	String getStatus(ArrayList<String> parseResult) {
		return getFirst(parseResult);
	}

	int convertStringToInt(String str){
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			//printParsingError(DATATYPE_STRING, str, DATATYPE_INTEGER);
			return -1;
		}
	}

	String capitalize(String word) {
		return Character.toUpperCase(word.charAt(0)) + word.substring(1);
	}

	String mergeTokens(String[] tokens, int startIndex, int endIndex) {
		String merged = "";
		for (int i = startIndex; i < endIndex; i++) {
			String token = tokens[i];
			merged += token + " ";
		}
		return removeEndSpacesOrBrackets(merged);
	}

	String removeFrontZero(String token){
		if (token.startsWith(ZERO)) {
			return token.substring(1, token.length());
		}
		return token;
	}
	
	String removeEndSpacesOrBrackets(String token) {
		while (true){
			if (token.startsWith(" ") || token.startsWith("[")) {
				token = token.substring(1, token.length());
				continue;
			} 	
			if (token.endsWith(" ") || token.startsWith("]")) {
				token = token.substring(0, token.length()-1);
				continue;
			}
			return token;
		}
	}

	String removePluralOrPastTense(String token) {
		token = token.toLowerCase();
		if (token.length() > 1 && (token.endsWith("s") || token.endsWith("d"))) {
			token = token.substring(0, token.length()-1);
		}
		return token;
	}

	boolean isNumber(String token) {
		try {
			Integer.parseInt(token);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	boolean isCommandThatNoNeedContent(String token){
		return COMMANDS_NO_CONTENT.contains(token);
	}

	boolean isDominatingCommand(String token){
		return COMMANDS_DOMINATING.contains(token);
	}

	boolean isCommandThatNeedWords(String token) {
		return COMMANDS_NEED_WORDS.contains(token);
	}

	boolean isCommandButCannotBeInOneShot(String token) {
		return COMMANDS_NOT_ONE_SHOT.contains(token) || COMMANDS_NO_CONTENT.contains(token);
	}

	boolean hasOnlyOneWord(String s){
		return s.split(" ").length == 1;
	}
	
	boolean canBeSplit(String s1, String s2){
		return s1.split(s2).length > 1;
	}
	
	boolean isError(String content) {
		return content.equals(STATUS_ERROR);
	}

	boolean isErrorStatus(ArrayList<String> result) {
		return isError(getStatus(result));
	}

	boolean isParsingCompleted(ArrayList<String> result) {
		return !getStatus(result).equals(STATUS_INCOMPLETE);
	}
	
	void printParsingError(String src, String token, String dest){
		System.out.println(String.format("ParsingError: problem converting %1$s '%2$s' to %3$s", src, token, dest));
	}
	
	abstract ArrayList<String> makeErrorResult(ERROR error, String token);
}
