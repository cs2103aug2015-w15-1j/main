package main.java.backend.Parser;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import com.joestelmach.natty.*;

public class Parser {
	public Parser() {
		System.out.println("Parser component initialised successfully");
	}
	//List of commands accepted by the program
	private final ArrayList<String> COMMANDS = new ArrayList<String>( Arrays.asList("add", "category", "deadline", "description", 
											   "delete", "done", "event", "priority", "reminder", "return", "undo") );
	
	private final ArrayList<String> COMMANDS_NO_PARAMETER = new ArrayList<String>( Arrays.asList("return", "undo") );
	
	private final ArrayList<String> COMMANDS_DOMINATING = new ArrayList<String>( Arrays.asList("delete", "done", 
														  "return", "search", "undo") );
	
	/*private final ArrayList<String> COMMANDS_WITH_TEXT_PARAMETER = new ArrayList<String>( Arrays.asList(
																   "add", "description") );*/
	
	private final ArrayList<String> COMMANDS_READ_TASKNAME = new ArrayList<String>( Arrays.asList(
			   														  "add", "delete", "done") );
	
	//The default list of fields and the order in which their parameters are put into result
	private final ArrayList<String> FIELDS_DEFAULT = new ArrayList<String>( Arrays.asList(
			"command", "task", "description", "deadline", "eventStart", "eventEnd", "priority", "reminder", "category") );
	
	//Temporary holder for the current parameter before it's being stored
	private String currParameter = "";
	
	//List of commands that have appeared in the current input
	private ArrayList<String> seenCommands = new ArrayList<String>();
	
	//List of fields that are included in the current result
	private ArrayList<String> fields = new ArrayList<String>(FIELDS_DEFAULT);
	
	//This stores the parameters under their respective fields
	private HashMap<String, String> parameters = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L; {
        put("command",""); put("task", ""); put("description", ""); put("deadline", ""); put("event", ""); 
        put("priority",""); put("reminder", ""); put("category", "");
    }};
    
    
	/**
	 * This method parses the user input and returns its components as an arraylist
	 */
	public ArrayList<String> parseInput(String input){
		ArrayList<String> result = new ArrayList<String>();
		String[] inputTokens = input.split(" ");
		String firstWord = getFirst(inputTokens).toLowerCase();
		
		if (isDominatingCommand(firstWord)) {
			result.add(firstWord);
			
			//if first word is "delete", "done" or "search"
			if (!hasNoParameter(firstWord)) {
				String parameter = mergeTokens(inputTokens, 1, inputTokens.length);
				result.add(parameter);
			}

		} else {
			for (String token: inputTokens) {
				String originalToken = token;
				token = token.toLowerCase();
				
				if (isCommand(token)) {
					putParameter();
					//Old code 1 goes here
					
					if (isSeenCommand(token)) {
						String oldCommand = token;
						mergeToPrevParameter(oldCommand);
						moveToBack(seenCommands, oldCommand);
						//Old code 3 goes here

					} else {
						String command = parameters.get("command");
						if (isDominatingCommand(token) && (!command.isEmpty() || !isLast(inputTokens, token))) {
							appendToParameter(originalToken);
						} else {
							putCommand(token);
						}
					}
					
				} else {
					appendToParameter(originalToken);
				}
			}
			putParameter();
			//Old code 2 goes here
			
			String command = parameters.get("command");
			if (command.equals("add") || command.equals("set")) {
				result = makeLongResult(result);
			} else {
				result = makeShortResult(result);
			}
			
		}	
		seenCommands.clear();
		fields = new ArrayList<String>(FIELDS_DEFAULT);
		parameters.replaceAll((field,parameter) -> "");
		return result;
	}
	
	private void appendToParameter(String token) {
		currParameter += token + " ";
	}
	
	/**
	 * This method marks the command as seen and stores it as the main command (if there isn't one) 
	 * @param token
	 */
	private void putCommand(String token) {
		String command = parameters.get("command");
		seenCommands.add(token);
	
		if (command.isEmpty()) {
			parameters.put("command", token);
		} else if (!isReadingTaskName(command)) {
			parameters.put("command", "set");
		}
	}
	
	/**
	 * This method stores the current parameter under its rightful field or appends it to an existing parameter
	 */
	private void putParameter() {
		if (!currParameter.isEmpty()) {
			currParameter = removeEndSpaces(currParameter);
			String lastCommand = getLast(seenCommands);
			String field;
			
			if (lastCommand.isEmpty() || isReadingTaskName(lastCommand)) {
				field = "task";
			} else {
				field = lastCommand;
			}
			
			/*if (lastCommand.equals("deadline") || lastCommand.equals("reminder")) {
				currParameter = parseDate(currParameter);
			}*/
			String parameter = parameters.get(field);
			String command = parameters.get("command");
			if (parameter.isEmpty()) {
				parameters.put(field, currParameter);
			} else if (!command.isEmpty() && !isReadingTaskName(lastCommand) && !isDominatingCommand(currParameter)) {
				parameters.put(field, currParameter);
			} else {
				parameters.put(field, parameter + " " + currParameter);
			}
			
			currParameter = "";
		}
	}

	/**
	 * This method creates a short arrayList for a result that contains 3 or less components
	 */
	private ArrayList<String> makeShortResult(ArrayList<String> result) {
		String command = parameters.get("command");
		String task = parameters.get("task");
		String newField = parameters.get(command);
		if (newField == null) {
			result.addAll( Arrays.asList(command, task) );
		} else {
			if (command.equals("deadline") || command.equals("reminder")) {
				newField = parseDate(newField);
			}
			if (command.equals("event")) {
				String[] eventStartEnd = getStartAndEnd(newField);
				String eventStart = eventStartEnd[0];
				String eventEnd = eventStartEnd[1];
				result.addAll( Arrays.asList(command, task, eventStart, eventEnd) );
			} else {
				result.addAll( Arrays.asList(command, task, newField) );
			}
		}
		return result;
	}

	/**
	 * This method creates a long arrayList for a result that contains more than 3 components
	 */
	private ArrayList<String> makeLongResult(ArrayList<String> result) {
		String command = parameters.get("command");
		String deadline = parameters.get("deadline");
		String event = parameters.get("event");
		String reminder = parameters.get("reminder");
		
		if (!reminder.isEmpty()) {
			parameters.put("reminder", parseDate(reminder));
		}
		if (!deadline.isEmpty()) {
			command += "T";
			fields.remove("eventStart");
			fields.remove("eventEnd");
			parameters.put("deadline", parseDate(deadline));
		} else if (!event.isEmpty()) {
			command += "E";
			fields.remove("deadline");
			getStartAndEnd(event);
		} else {
			if (command.equals("add")) {
				command += "F";
			}
			fields.remove("deadline");
			fields.remove("eventStart");
			fields.remove("eventEnd");
		}
		parameters.put("command", command);
		
		for (String field: fields) {
			String para = parameters.get(field);
			result.add(para);
		}
		return result;
	}

	/**
	 * This method gets the individual start and end date/time of an event and put them into separate fields
	 * @return the start and end date/time in an array
	 */
	private String[] getStartAndEnd(String event) {
		//String currYear = getCurrentYear();
		
		String[] eventTokens = event.split(" to ", 2);
		String eventStart = eventTokens[0];
		String eventEnd = "";
		if (eventTokens.length > 1) {
			eventEnd = removeEndSpaces(eventTokens[1]);
		}
		
		if (!eventEnd.contains(" ") && !eventEnd.isEmpty()) {
			String startDate = "";
			String[] startTokens = eventStart.split(" ");
			for (int i = 0; i < startTokens.length-1; i++) {
				startDate += startTokens[i];
				if (startTokens[0].contains("/")) {
					startDate += "/";
				} else if (startTokens[0].contains("-")){
					startDate += "-";
				} else {
					startDate += " ";
				}
			}
			/*if (!(startDate.contains(currYear) || startDate.contains(currDecade))) {
				startDate += currYear + " ";
			}*/
			String startTime = startTokens[startTokens.length-1];
			eventStart = startDate + " " + startTime;
			System.out.println(eventStart);
			String[] endTokens = eventEnd.split(" ", 2);
			String endDate = startDate;
			String endTime = endTokens[0];
			eventEnd = endDate + " " + endTime;
		} 
		eventStart = parseDate(removeEndSpaces(eventStart));
		eventEnd = parseDate(removeEndSpaces(eventEnd));
		parameters.put("eventStart", eventStart);
		parameters.put("eventEnd", eventEnd);
		
		String[] result = new String[2];
		result[0] = eventStart;
		result[1] = eventEnd;
		return result;
	}

	private String parseDate(String date) {
		//System.out.println(date);
		if (date.isEmpty()) {
			return date;
		}
		String[] dateTokens = date.split(" ");
		date = "";
		for (String token: dateTokens) {
			String[] ddmmyy = null;
			if (token.contains("/")){
				ddmmyy = token.split("/");
			} else if (token.contains("-")) {
				ddmmyy = token.split("-");
			}
			
			if (ddmmyy != null && ddmmyy.length >= 2) {
				String day = ddmmyy[0];
				String month = ddmmyy[1];
				String year = "";
				date += month + "/" + day;
				if (ddmmyy.length == 3) {
					year = ddmmyy[2];
					date += "/" + year;
				} 
				date += " ";
			} else {
				date += token + " ";
			}
		}
		date = removeEndSpaces(date);
		
		com.joestelmach.natty.Parser dateParser = new com.joestelmach.natty.Parser();
		DateGroup group = dateParser.parse(date).get(0);
		List<Date> dates = group.getDates();
		String dateString = dates.toString();
		dateString = dateString.substring(1, dateString.length()-1);
		return dateString;
	}

	/*private String getCurrentYear() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
		return strDate;
	}*/
	
	/**
	 * This method merges all the tokens between the startIndex (including) and endIndex (excluding)
	 * @return the merged tokens as a string
	 */
	private String mergeTokens(String[] inputTokens, int startIndex, int endIndex) {
		String parameter = "";
		for (int i = startIndex; i < endIndex; i++) {
			String token = inputTokens[i];
			parameter += token + " ";
		}
		return removeEndSpaces(parameter);
	}

	/**
	 * This method takes in an old command and merges the command and its parameter to the previous parameter
	 * @param oldCommand
	 */
	private void mergeToPrevParameter(String oldCommand) {
		String prevField;
		if (isFirst(seenCommands, oldCommand)) {
			prevField = "task";
		}
		else {
			prevField = getPrevious(seenCommands, oldCommand);
			if (prevField.equals("add")) {
				prevField = "task";
			}
		}	
		
		String prevParameter = parameters.get(prevField);
		String oldParameter = parameters.get(oldCommand);
		if (oldParameter == null) {
			oldParameter = "";
		}
		
		if (prevParameter.isEmpty() && oldParameter.isEmpty()) {
			parameters.put(prevField, oldCommand);
		} else if (oldParameter.isEmpty()) {
			parameters.put(prevField, prevParameter + " " + oldCommand);
		} else if (prevParameter.isEmpty()) {
			parameters.put(prevField, oldCommand + " " + oldParameter);
		} else {
			parameters.put(prevField, prevParameter + " " + oldCommand + " " + oldParameter);
		}
	}

	/**
	 * This method moves a token to the back of its arrayList
	 */
	private void moveToBack(ArrayList<String> arrayList, String token) {
		int index = arrayList.indexOf(token);
		arrayList.remove(index);
		arrayList.add(token);
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

	private boolean isCommand(String token){
		return COMMANDS.contains(token);
	}
	
	private boolean isDominatingCommand(String token){
		return COMMANDS_DOMINATING.contains(token);
	}
	
	private boolean hasNoParameter(String token){
		return COMMANDS_NO_PARAMETER.contains(token);
	}
	
	/*private boolean hasTextParameter(String token){
		return COMMANDS_WITH_TEXT_PARAMETER.contains(token);
	}*/
	
	private boolean isReadingTaskName(String token){
		return COMMANDS_READ_TASKNAME.contains(token);
	}
	
	private boolean isSeenCommand(String token) {
		return seenCommands.contains(token);
	}
	
	/*private boolean isFirst(String[] array, String token) {
		String firstWord = getFirst(array);
		return firstWord.equals(token);
	}*/
	
	private boolean isFirst(ArrayList<String> list, String token) {
		String firstWord = getFirst(list);
		return firstWord.equals(token);
	}
	
	private boolean isLast(String[] array, String token) {
		String lastWord = getLast(array);
		return lastWord.equals(token);
	}

	/*private boolean isLast(ArrayList<String> list, String token) {
		String lastWord = getLast(list);
		return lastWord.equals(token);
	}*/
	
	private String getFirst(String[] array){
		return array[0];
	}
	
	private String getFirst(ArrayList<String> arrayList){
		return arrayList.get(0);
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
	
	private String getPrevious(ArrayList<String> list, String token) {
		return list.get( list.indexOf(token)-1 );
	}
	
}
