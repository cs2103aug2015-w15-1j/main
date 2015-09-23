package main.java.backend.Parser;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
	
	//List of commands accepted by the program
	private final ArrayList<String> commands = new ArrayList<String>( 
											   Arrays.asList("add", "deadline", "description", 
											   "delete", "done", "event", "priority", "reminder", 
											   "return", "undo") );
	
	private final ArrayList<String> commandsWithoutParameter 
									= new ArrayList<String>( Arrays.asList("return", "undo") );
	
	private final ArrayList<String> commandsWithWordParameter
									= new ArrayList<String>( Arrays.asList("add", "description") );
	
	//The result that will be passed back to logic
	private ArrayList<String> parsed = new ArrayList<String>();
	
	//Temporary holder for the next parameter of result
	private String parameter = "";
	
	//List of commands that have appeared in the current input
	private final ArrayList<String> seenCommands = new ArrayList<String>();
	
	/**
	 * This method parses the user input and returns it as an arraylist
	 */
	public ArrayList<String> parseInput(String input){
		ArrayList<String> result;
		String[] inputTokens = input.split(" ");
		
		String firstWord = inputTokens[0];
		if (commandsWithoutParameter.contains(firstWord)) {
			result = new ArrayList<String>( Arrays.asList(firstWord) );
		
		} else {
			for (String token: inputTokens) {	
				if (commands.contains(token.toLowerCase())) {
					pushParameter();
					if (seenCommands.contains(token)) {
						if (parsed.indexOf(token) == 0) { //if original command is first word of input
							if (commandsWithWordParameter.contains(token)) {
								parameter += token + " ";
							} else {
								removeOriginalCommand(token);
								parsed.add(token.toLowerCase());
							}
						} else {
							if (seenCommands.indexOf(token) == 0) { //if original command is first command that appeared
								String merged = mergeWithParameter(token);
								appendToPrevParameter(merged);
							} else {
								String prevCommand = seenCommands.get( seenCommands.indexOf(token)-1 );
								if (commandsWithWordParameter.contains(prevCommand)) {
									String merged = mergeWithParameter(token);
									appendToPrevParameter(merged);
								} else {
									removeOriginalCommand(token);
								}
							}
							parsed.add(token.toLowerCase());
						}

					} else {
						seenCommands.add(token);
						parsed.add(token.toLowerCase());
					}
					
				} else {
					parameter += token + " ";
				}
			}
			pushParameter();
	
			result = new ArrayList<String>(parsed);
			parsed.clear();
			seenCommands.clear();
		}
		
		return result;
	}

	private String mergeWithParameter(String token) {
		int cmdIndex = parsed.indexOf(token);
		int paraIndex = cmdIndex + 1;
		String parameter = parsed.get(paraIndex);
		while (!commands.contains(parameter)) {	
			token += " " + parameter;
			parsed.remove(paraIndex);
			if (parsed.size() <= paraIndex) {
				break;
			}
			parameter = parsed.get(paraIndex);
		}
		parsed.set(cmdIndex, token);
		return token;
	}

	private void appendToPrevParameter(String token) {
		int tokenIndex = parsed.indexOf(token);
		int paraIndex = tokenIndex - 1;
		String prevParameter = parsed.get(paraIndex);
		
		if (commands.contains(prevParameter)) {
			return;
		}
		
		String currParameter = parsed.get(tokenIndex);
		prevParameter += " " + currParameter;
		parsed.remove(tokenIndex);
		parsed.set(paraIndex, prevParameter);
	}

	private void removeOriginalCommand(String token) {
		int commandIndex = parsed.indexOf(token);
		parsed.remove(commandIndex);
		while (parsed.size() > commandIndex){
			if (commands.contains( parsed.get(commandIndex) )) {
				break;
			}
			parsed.remove(commandIndex);
		}
	}
	
	/**
	 * This method adds parameter to result and resets parameter (if it is not empty)
	 */
	private void pushParameter() {
		if (!parameter.isEmpty()) {
			parameter = removeEndSpace(parameter);
			parsed.add(parameter);
			parameter = "";
		}
	}

	private String removeEndSpace(String token) {
		return token.substring(0, token.length()-1);
	}
}
