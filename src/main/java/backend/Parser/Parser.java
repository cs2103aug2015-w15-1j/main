package main.java.backend.Parser;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
	
	//List of commands accepted by the program
	private final ArrayList<String> COMMANDS = new ArrayList<String>( 
											   Arrays.asList("add", "deadline", "description", 
											   "delete", "done", "event", "priority", "reminder", 
											   "return", "undo") );
	
	private final ArrayList<String> COMMANDS_WITHOUT_PARAMETER 
									= new ArrayList<String>( Arrays.asList("return", "undo") );
	
	private final ArrayList<String> COMMANDS_WITH_WORD_PARAMETER
									= new ArrayList<String>( Arrays.asList("add", "description") );
	
	private final ArrayList<String> COMMANDS_SOLO_EFFECT
									= new ArrayList<String>( Arrays.asList("delete", "done") );
	
	//The result that will be passed back to logic
	private ArrayList<String> parsedList = new ArrayList<String>();
	
	//Temporary holder for the next parameter of result
	private String parameter = "";
	
	//List of commands that have appeared in the current input
	private final ArrayList<String> COMMANDS_SEEN = new ArrayList<String>();
	
	/**
	 * This method parses the user input and returns it as an arraylist
	 */
	public ArrayList<String> parseInput(String input){
		ArrayList<String> result;
		String[] inputTokens = input.split(" ");

		String firstWord = inputTokens[0];
		if (COMMANDS_WITHOUT_PARAMETER.contains(firstWord)) {
			result = new ArrayList<String>( Arrays.asList(firstWord) );
		} else if (COMMANDS_SOLO_EFFECT.contains(firstWord)) {
			parsedList.add(firstWord);
			for (int i = 1; i < inputTokens.length; i++) {
				String token = inputTokens[i];
				parameter += token + " ";
			}
			pushParameter();
			result = new ArrayList<String>(parsedList);
			parsedList.clear();
		
		} else {
			for (String token: inputTokens) {
				String tokenOriginal = token;
				token = token.toLowerCase();
				
				if (COMMANDS.contains(token)) {
					pushParameter();
					
					if (COMMANDS_SOLO_EFFECT.contains(token)) {
						parsedList.add(token);
						COMMANDS_SEEN.add(token);
						int startIndex; 
						int endIndex;
						if (!parsedList.get(0).equals("add")) {
							startIndex = 0;
							endIndex = COMMANDS_SEEN.size() - 1;
						} else {
							startIndex = 1;
							endIndex = COMMANDS_SEEN.size();
						}
						for (int i = startIndex; i < endIndex; i++) {
							String cmd = COMMANDS_SEEN.get(i);
							String merged = mergeWithParameter(cmd);
							appendToPrevToken(merged);
						}
						break;
						
					} else if (COMMANDS_SEEN.contains(token)) {
						if (parsedList.indexOf(token) == 0) { //if original command is first word of input
							if (COMMANDS_WITH_WORD_PARAMETER.contains(token)) {
								parameter += tokenOriginal + " ";
							} else {
								removeOriginal(token);
								parsedList.add(token);
								COMMANDS_SEEN.add(token);
							}
						} else {
							if (COMMANDS_SEEN.indexOf(token) == 0) { //if original command is first command that appeared
								String merged = mergeWithParameter(token);
								appendToPrevToken(merged);
							} else {
								String prevCommand = COMMANDS_SEEN.get( COMMANDS_SEEN.indexOf(token)-1 );
								if (COMMANDS_WITH_WORD_PARAMETER.contains(prevCommand)) {
									String merged = mergeWithParameter(token);
									appendToPrevToken(merged);
								} else {
									removeOriginal(token);
								}
							}
							parsedList.add(token);
							COMMANDS_SEEN.add(token);
						}

					} else {
						parsedList.add(token);
						COMMANDS_SEEN.add(token);
					}
					
				} else {
					parameter += tokenOriginal + " ";
				}
			}
			pushParameter();
			result = new ArrayList<String>(parsedList);
			parsedList.clear();
			COMMANDS_SEEN.clear();
		}	
		return result;
	}

	/**
	 * This method adds parameter to parsedInput (if it is not empty) and resets parameter 
	 */
	private void pushParameter() {
		if (!parameter.isEmpty()) {
			parameter = removeEndSpace(parameter);
			parsedList.add(parameter);
			parameter = "";
		}
	}

	private String removeEndSpace(String token) {
		return token.substring(0, token.length()-1);
	}
	
	/**
	 * This method merges a command with its parameter, and puts it back into parsedInput
	 * @param command
	 * @return the merged command & parameter
	 */
	private String mergeWithParameter(String command) {
		int cmdIndex = parsedList.indexOf(command.toLowerCase());
		int paraIndex = cmdIndex + 1;
		if (paraIndex >= parsedList.size()) {
			return command;
		} else {
			String parameter = parsedList.get(paraIndex);
			while (!COMMANDS.contains(parameter)) {	
				command += " " + parameter;
				parsedList.remove(paraIndex);
				if (parsedList.size() <= paraIndex) {
					break;
				}
				parameter = parsedList.get(paraIndex);
			}
			parsedList.set(cmdIndex, command);
			return command;
		}
	}

	/**
	 * This method appends a token to the previous token (if that token is not a command)
	 * @param token
	 */
	private void appendToPrevToken(String token) {
		int tokenIndex = parsedList.indexOf(token);
		int prevIndex = tokenIndex - 1;
		String prevToken = parsedList.get(prevIndex);
		
		//if previous token is a command, don't append
		if (COMMANDS.contains(prevToken)) {
			return;
		} else {
			String currParameter = parsedList.get(tokenIndex);
			prevToken += " " + currParameter;
			parsedList.remove(tokenIndex);
			parsedList.set(prevIndex, prevToken);
		}
	}

	/**
	 * This method removes the original command and its parameter
	 * @param command
	 */
	private void removeOriginal(String command) {
		int commandIndex = parsedList.indexOf(command.toLowerCase());
		parsedList.remove(commandIndex);
		while (parsedList.size() > commandIndex){
			if (COMMANDS.contains( parsedList.get(commandIndex) )) {
				break;
			}
			parsedList.remove(commandIndex);
		}
	}

}
