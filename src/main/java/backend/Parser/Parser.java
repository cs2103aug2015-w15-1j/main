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
				if (seenCommands.contains(token)) {
					pushParameter();
					int commandIndex = parsed.indexOf(token);
					
					//Remove the original command and its parameter
					parsed.remove(commandIndex);
					while (parsed.size() > commandIndex){
						if (commands.contains( parsed.get(commandIndex) )) {
							break;
						}
						parsed.remove(commandIndex);
					}
				}
				
				if (commands.contains(token.toLowerCase())) {
					pushParameter();
					seenCommands.add(token);
					parsed.add(token.toLowerCase());
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
