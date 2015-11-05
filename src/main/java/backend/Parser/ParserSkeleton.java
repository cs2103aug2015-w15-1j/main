package main.java.backend.Parser;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ParserSkeleton
 * Contains basic methods and data shared within Parser component
 * @@author A0121795B
 */
abstract class ParserSkeleton {
	
	//List of all command words accepted by the program
	final ArrayList<String> COMMANDS = new ArrayList<String>( Arrays.asList(
	"add", "addcat", "category", "deadline", "description", "delete", "deleteAll", "done", "event", "every", 
	"exit", "filepath", "priority", "redo", "reminder", "rename", "reset", "search", "setcol", "showcat",   
	"show", "showDone", "showE", "showF", "showO", "showT", "showToday", "sort", "sortD", "sortN", "sortP", "undo", "undone") );
	
	//Commands that work just by typing the command word (without additional content)
	final ArrayList<String> COMMANDS_NO_CONTENT = new ArrayList<String>( Arrays.asList(
	"deleteAll", "exit", "redo", "showDone", "showE", "showF", "showO", "showT", "showToday", "sortD", "sortN", "sortP", "undo") );
	
	//Commands that if appear first, will prevent other command keywords from having effect
	final ArrayList<String> COMMANDS_DOMINATING = new ArrayList<String>( Arrays.asList(
	"addcat", "delete", "done", "filepath", "reset", "search", "setcol", "show", "showcat", "sort", "undone") );
	
	//Commands that can accept any amount of words
	final ArrayList<String> COMMANDS_NEED_WORDS = new ArrayList<String>( 
	Arrays.asList("add", "addcat", "category", "description", "filepath", "search") );
	
	//Commands that cannot be part of a one-shot command
	final ArrayList<String> COMMANDS_NOT_ONE_SHOT = new ArrayList<String>( 
	Arrays.asList("delete", "done", "reset", "showcat", "undone") );	
	
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
	
	boolean isNumber(String token) {
		try {
			Integer.parseInt(token);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	int convertStringToInt(String str){
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			System.out.println("DateParsingError: problem converting string '" + str + "' to int");
			//e.printStackTrace();
			return -1;
		}
	}

	String removeFrontZero(String token){
		if (token.startsWith("0")) {
			return token.substring(1, token.length());
		}
		return token;
	}
	
	String capitalize(String word) {
		return Character.toUpperCase(word.charAt(0)) + word.substring(1);
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
		return removeEndSpacesOrBrackets(merged);
	}

	/**
	 * This method removes any spaces that are in front or at the back of the token
	 */
	String removeEndSpacesOrBrackets(String token) {
		if (token.startsWith(" ") || token.startsWith("[")) {
			token =  token.substring(1, token.length());
		} 	
		if (token.endsWith(" ") || token.startsWith("]")) {
			return token.substring(0, token.length()-1);
		} else {
			return token;
		}
	}

	boolean isSplitSuccessful(String token, String[] splitTokens) {
		return splitTokens.length == 1 && !token.equals(getFirst(splitTokens));
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

	boolean isParsingCompleted(ArrayList<String> result) {
		return !getStatus(result).equals("incomplete");
	}
	
	ArrayList<String> makeErrorResult(String error, String token) {
		ArrayList<String> result = new ArrayList<String>(); 
		result.add("error");
		return result;
	}
}
