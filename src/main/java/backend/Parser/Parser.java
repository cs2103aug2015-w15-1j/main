package main.java.backend.Parser;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
	
	//List of commands accepted by the program
	private final ArrayList<String> commands = new ArrayList<String>( Arrays.asList("add", "deadline", "description", "priority") );
	
	//The result that will be passed back to logic
	private ArrayList<String> result = new ArrayList<String>();
	
	//Temporary holder for the next token that is to be added
	private String tokenHolder = "";
	
	/**
	 * This method parses the user input and returns it as an arraylist 
	 * @param input passed from logic
	 * @return parsed input as arraylist
	 */
	public ArrayList<String> parseInput(String input){
		String[] inputTokens = input.split(" ");
		
		for (String token: inputTokens) {
			if (commands.contains(token)) { //if token is a command
				push(tokenHolder);
				result.add(token);
			} else {
				tokenHolder += token + " ";
			}
		}
		push(tokenHolder);
		
		return result;
	}
	
	/**
	 * This method adds tokenHolder to result and resets tokenHolder (if it is not empty)
	 */
	private void push(String tokenHolder) {
		if (!tokenHolder.isEmpty()) {
			tokenHolder = removeEndSpace(tokenHolder);
			result.add(tokenHolder);
			tokenHolder = "";
		}
	}

	private String removeEndSpace(String token) {
		return token.substring(0, token.length()-1);
	}
}
