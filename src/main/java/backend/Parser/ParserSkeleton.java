package main.java.backend.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ParserSkeleton {

	static DateParser dateParser = new DateParser();
	
	//List of all command words accepted by the program
	final ArrayList<String> COMMANDS = new ArrayList<String>( Arrays.asList(
	"add", "addcat", "category", "deadline", "description", "delete", "deleteAll", "done", "event", "every", 
	"exit", "priority", "redo", "reminder", "rename", "reset", "search", "setcol", "showcat",   
	"show", "showE", "showF", "showO", "showT", "sort", "sortD", "sortN", "sortP", "undo", "undone") );
	
	//Commands that work just by typing the command word (without additional content)
	final ArrayList<String> COMMANDS_NO_CONTENT = new ArrayList<String>( Arrays.asList(
	"deleteAll", "exit", "redo", "showE", "showF", "showO", "showT", "sortD", "sortN", "sortP", "undo") );
	
	//Commands that if appear first, will prevent other command keywords from having effect
	final ArrayList<String> COMMANDS_DOMINATING = new ArrayList<String>( Arrays.asList(
	"addcat", "delete", "done", "every", "reset", "search", "setcol", "show", "showcat", "sort", "undone") );
	
	//Commands that create a new item
	final ArrayList<String> COMMANDS_NO_FIELD = new ArrayList<String>( Arrays.asList(
	"add", "addcat", "delete", "done") );
	
	//Commands that can accept any amount of words
	final ArrayList<String> COMMANDS_NEED_WORDS = new ArrayList<String>( 
	Arrays.asList("add", "addcat", "category", "description", "search") );
	
	//Commands that cannot be part of a one-shot command
	final ArrayList<String> COMMANDS_NOT_ONE_SHOT = new ArrayList<String>( 
	Arrays.asList("delete", "done", "reset", "showcat", "undone") );
	
	//Command words that indicate that the command is one-shot
	final ArrayList<String> COMMANDS_ONE_SHOT = new ArrayList<String>( 
	Arrays.asList("add", "set") );	
	
	//Command fields that can be edited/reset
	final ArrayList<String> COMMANDS_CAN_RESET = new ArrayList<String>( 
	Arrays.asList("all", "description", "deadline", "event", "priority", "reminder", "category") );	
	
	//Contains the variants or short forms of some of the commands
    HashMap<String, ArrayList<String>> command_families = new HashMap<String, ArrayList<String>>(){
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

	String getLast(String[] array){
		if (array.length == 0) {
			return "";
		}
		return array[array.length-1];
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
	
	boolean isNumber(String token) {
		try {
			Integer.parseInt(token);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * This method merges all the tokens between the startIndex (including) and endIndex (excluding)
	 * @return the merged tokens as a string
	 */
	String mergeTokens(String[] tokens, int startIndex, int endIndex) {
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
	String removeEndSpaces(String token) {
		if (token.startsWith(" ")) {
			token =  token.substring(1, token.length());
		} 	
		if (token.endsWith(" ")) {
			return token.substring(0, token.length()-1);
		} else {
			return token;
		}
	}

	String getStatus(ArrayList<String> parseResult) {
		return getFirst(parseResult);
	}
	
	boolean isError(String content) {
		return content.equals("error");
	}

	boolean isErrorStatus(ArrayList<String> result) {
		return isError(getStatus(result));
	}

	boolean isIncompleteStatus(ArrayList<String> result) {
		return getStatus(result).equals("incomplete");
	}
	
	ArrayList<String> makeErrorResult(String error, String token) {
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
	
	boolean isCommand(String token){
		return COMMANDS.contains(token);
	}

	boolean isCommandThatNoNeedContent(String token){
		return COMMANDS_NO_CONTENT.contains(token);
	}

	boolean isDominatingCommand(String token){
		return COMMANDS_DOMINATING.contains(token);
	}

	boolean isCommandThatAddStuff(String token){
		return COMMANDS_NO_FIELD.contains(token);
	}

	boolean isCommandThatNeedWords(String token) {
		return COMMANDS_NEED_WORDS.contains(token);
	}

	boolean isCommandThatCanBeReset(String token) {
		return COMMANDS_CAN_RESET.contains(token);
	}

	boolean isCommandButCannotBeInOneShot(String token) {
		return COMMANDS_NOT_ONE_SHOT.contains(token) || COMMANDS_NO_CONTENT.contains(token);
	}

	boolean isCommandButHasNoContent(String token, int inputWordCount) {
		return isCommand(token) && inputWordCount == 1;
	}

	boolean isOneShotCommand(String token) {
		return COMMANDS_ONE_SHOT.contains(token);
	}
}
