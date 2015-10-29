package main.java.backend.Parser;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser extends ParserSkeleton{
    
	private ParserVault parserVault = new ParserVault();
    
	//Tokens are 'merged' into a growingToken until they are stored into field content
	private String growingToken = "";
    
    public Parser(){
    	//Force Natty parser to initialize by running dateParser once
    	String pi = "31/4/15 9:26";
    	dateParser.parseDate(pi);
    }
    
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
	
	void growToken(String token) {
		growingToken += token + " ";
	}

	private ArrayList<String> parseFirstTwoWords(String[] inputTokens) {
		String firstWordOriginal = getFirst(inputTokens);
		String firstWord = convertVariantToDefault(firstWordOriginal);
		String secondWordOriginal = getSecond(inputTokens);
		String secondWord = convertVariantToDefault(secondWordOriginal);
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
		boolean hasQuote = hasQuotes(inputTokens);
		boolean quoteStart = false;
		
		for (int i = 0; i < inputWordCount; i++) {
			String token = inputTokens[i];
			String originalToken = token;
			token = convertVariantToDefault(token);
			
			quoteStart = checkForQuotes(hasQuote, quoteStart, originalToken, i, inputTokens); 
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
		String command = parserVault.getFieldContent().get("command");
		if (isOneShotCommand(command)) {
			return parserVault.makeMultiFieldResult();
		} else {
			return parserVault.makeSingleFieldResult();
		}
	}

	private ArrayList<String> parseToken(String token, String originalToken, int count, int total) {
		if (isNotCommand(token) || parserVault.isCommandButRepressed(token)){
			growToken(originalToken);	
		} else {
			growingToken = parserVault.storeToken(growingToken);
			String lastCommandSeen = getLast(parserVault.getSeenCommands());
			
			if (isLastWord(count, total) && isCommandThatNeedWords(lastCommandSeen)) {
				growToken(originalToken);
			} else if (isLastWord(count, total) && !parserVault.isCommandButRepressed(token)){
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

	private boolean isNotCommand(String token){
		return !isCommand(token);
	}

	private boolean isNotCommandOrIndex(String token) {
		return isNotCommand(token) && !isNumber(token);
	}

	private boolean isLastWord(int i, int inputWordCount) {
		return i == inputWordCount-1;
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
}
