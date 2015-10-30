package main.java.backend.Parser;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser extends ParserSkeleton{
	
	private DateParser dateParser = new DateParser();
	private ParserVault parserVault = new ParserVault(dateParser);

	public Parser(){
    	//Force Natty parser to be initialized by running dateParser once
    	String pi = "31/4/15 9:26";
    	dateParser.parseDate(pi);
    }
    
	//Command that allow more than one field to be set at the same time
	private final ArrayList<String> COMMANDS_ALLOW_MULTIPLE_FIELDS = new ArrayList<String>( 
	Arrays.asList("add", "set") );
	
	//Tokens are 'merged' into a growingToken until they are stored into field content
	private String growingToken = "";

	/**
	 * This method parses the user input and returns an ArrayList of string tokens
	 */
	public ArrayList<String> parseInput(String input){
		parserVault.resetContents();
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
		String firstWord = parserVault.convertVariantToDefault(firstWordOriginal);
		String secondWordOriginal = getSecond(inputTokens);
		String secondWord = parserVault.convertVariantToDefault(secondWordOriginal);
		int inputWordCount = inputTokens.length;
		
		if (isCommandThatNoNeedContent(firstWord)) {
			return parserVault.makeCommandOnlyResult(firstWord);
		}
		if (isCommandButHasNoContent(firstWord, inputWordCount)) {
			return makeErrorResult("EmptyFieldError", firstWord);
		}
		if (isNotCommandOrIndex(firstWord)) {
			return makeErrorResult("InvalidWordError", firstWordOriginal);
		} 
		if (isDominatingCommand(firstWord)) {
			String content = mergeTokens(inputTokens, 1, inputTokens.length);
			ArrayList<String> dominantResult = parserVault.makeDominantResult(firstWord, secondWord, content); 
			if (!isIncompleteStatus(dominantResult)) {
				return dominantResult;
			}
		}
		if (isCommandButCannotBeInOneShot(secondWord)) {
			String content = mergeTokens(inputTokens, 1, inputTokens.length);
			ArrayList<String> dominantResult = parserVault.makeDominantResult(secondWord, firstWord, content); 
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
		boolean hasQuotes = hasQuotes(inputTokens);
		boolean quoteStart = false;
		
		for (int i = 0; i < inputWordCount; i++) {
			String token = inputTokens[i];
			String originalToken = token;
			token = parserVault.convertVariantToDefault(token);
			
			quoteStart = checkForQuotes(hasQuotes, quoteStart, originalToken, i, inputTokens); 
			if (quoteStart) {
				growToken(originalToken);
				
			} else {
				ArrayList<String> parseResult = parseToken(token, originalToken, i, inputWordCount);
				if (isErrorStatus(parseResult)) {
					return parseResult;
				}
			}
		}
		
		growingToken = parserVault.storeToken(growingToken);
		String command = parserVault.getContent("command");
		if (canHaveMultipleFields(command)) {
			return parserVault.makeMultiFieldResult();
		} else {
			return parserVault.makeSingleFieldResult();
		}
	}

	private ArrayList<String> parseToken(String token, String originalToken, int count, int total) {
		if (isNotCommand(token) || isCommandButRepressed(token)){
			growToken(originalToken);	
		} else {
			growingToken = parserVault.storeToken(growingToken);
			String lastCommandSeen = parserVault.getLastSeenCommand();
			
			if (isLastWord(count, total) && isCommandThatNeedWords(lastCommandSeen)) {
				growToken(originalToken);
			} else if (isLastWord(count, total) && !isCommandButRepressed(token)){
				return makeErrorResult("EmptyFieldError", originalToken);
			} else if (parserVault.isSeenCommand(token)) {
				if (isCommandThatNeedWords(lastCommandSeen)) {
					growingToken = parserVault.storeToken(originalToken);
				} else {
					return makeErrorResult("DuplicateCommandError", originalToken);
				}
			} else {
				if (!lastCommandSeen.isEmpty()) {
					String contentOfLastCommand = parserVault.getContentOfCommand(lastCommandSeen);
					if (contentOfLastCommand.isEmpty()) {
						return makeErrorResult("EmptyFieldError", lastCommandSeen);
					}
				}
				parserVault.storeCommand(token);
			}
		}
		return new ArrayList<String>( Arrays.asList( "OK" ));
	}

	private void growToken(String token) {
		growingToken += token + " ";
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

	private boolean isCommand(String token){
		return COMMANDS.contains(token);
	}

	private boolean isCommandButHasNoContent(String token, int inputWordCount) {
		return isCommand(token) && inputWordCount == 1;
	}

	private boolean isNotCommand(String token){
		return !isCommand(token);
	}

	private boolean isNotCommandOrIndex(String token) {
		return isNotCommand(token) && !isNumber(token);
	}

	private boolean isLastWord(int i, int inputWordCount) {
		return i == inputWordCount-1;
	}

	private boolean isCommandButRepressed(String token) {
		String mainCommand = parserVault.getContent("command");
		return !mainCommand.isEmpty() && (isDominatingCommand(token) || isCommandThatNoNeedContent(token));
	}
	
	private boolean canHaveMultipleFields(String token) {
		return COMMANDS_ALLOW_MULTIPLE_FIELDS.contains(token);
	}
	
	@Override
	ArrayList<String> makeErrorResult(String error, String token) {
		ArrayList<String> result = new ArrayList<String>(); 
		result.add("error");
		
		switch (error) {
			case "InvalidWordError":
				result.add(error + ": '" + token + "' is not recognised as a command or index");
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
			default:
				break; 
		}
		return result;
	}
}
