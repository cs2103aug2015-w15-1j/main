package main.java.backend.Parser;

import java.util.ArrayList;

public class ParserSkeleton {

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
}
