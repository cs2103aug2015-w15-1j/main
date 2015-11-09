package main.java.backend.Parser;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Parser
 * Parses user input and returns result to Logic Component
 * @@author A0121795B
 */
public class Parser extends ParserSkeleton{
	
	ParserVault parserVault = new ParserVault();
	
	//Tokens are used to 'grow' (merged into) a growingToken until it is stored into field content
	private String growingToken = "";
	
	//Result types that include more than one field
	private final ArrayList<String> MULTIFIELD_RESULT_TYPE = new ArrayList<String>( 
	Arrays.asList(RESULTTYPE_ADD, RESULTTYPE_SET) );
	
	/**
	 * This method parses the user input and returns an ArrayList of string tokens
	 */
	public ArrayList<String> parseInput(String input){
		parserVault.resetContents();
		String[] inputTokens = input.split(SPACE_OF_ANY_LENGTH);
		ArrayList<String> firstTwoWordsParsed = parseFirstTwoWords(inputTokens);
		
		if (isParsingCompleted(firstTwoWordsParsed)) {
			return firstTwoWordsParsed;
		} else {
			return parseRemaining(inputTokens);	
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
			return makeErrorResult(ERROR.EMPTY_FIELD, firstWord);
		}
		if (isNotCommandOrIndex(firstWord)) {
			return makeErrorResult(ERROR.INVALID_WORD, firstWordOriginal);
		} 
		if (isDominatingCommand(firstWord)) {
			String content = mergeTokens(inputTokens, 1, inputTokens.length);
			ArrayList<String> dominantResult = parserVault.makeDominantResult(firstWord, secondWord, content); 
			if (isParsingCompleted(dominantResult)) {
				return dominantResult;
			}
		}
		if (isCommandButCannotBeInOneShot(secondWord)) {
			String content = mergeTokens(inputTokens, 1, inputTokens.length);
			ArrayList<String> dominantResult = parserVault.makeDominantResult(secondWord, firstWord, content); 
			if (isParsingCompleted(dominantResult)) {
				return dominantResult;
			}
		}
		if (isNumber(firstWord)) {
			if (secondWord.isEmpty()) {
				return makeErrorResult(ERROR.NO_COMMAND, firstWord);
			} else if (isNotCommand(secondWord)) {
				return makeErrorResult(ERROR.INVALID_COMMAND, secondWordOriginal);
			}
		}
		return new ArrayList<String>( Arrays.asList(STATUS_INCOMPLETE));
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
		String resultType = parserVault.getResultType();
		if (canHaveMultipleFields(resultType)) {
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
				return makeErrorResult(ERROR.EMPTY_FIELD, originalToken);
			} else if (parserVault.isSeenCommand(token)) {
				if (isCommandThatNeedWords(lastCommandSeen)) {
					growingToken = parserVault.storeToken(originalToken);
				} else if (!token.equals(COMMAND_RECUR)){
					return makeErrorResult(ERROR.DUPLICATE_COMMAND, originalToken);
				}
			} else {
				if (!lastCommandSeen.isEmpty()) {
					String contentOfLastCommand = parserVault.getContentOfCommand(lastCommandSeen);
					if (contentOfLastCommand.isEmpty()) {
						return makeErrorResult(ERROR.EMPTY_FIELD, lastCommandSeen);
					}
				}
				parserVault.storeCommand(token);
			}
		}
		return new ArrayList<String>( Arrays.asList(STATUS_OKAY));
	}

	private void growToken(String token) {
		growingToken += token + " ";
	}

	private boolean checkForQuotes(boolean hasQuote, boolean quoteStart, String token, 
			int count, String[] inputTokens) {
		if (hasQuote && token.startsWith(QUOTATION_MARK)) {
			quoteStart = true;
		}
		
		if (quoteStart == true) {
			String previousToken = getPrevious(inputTokens, count);
			if (previousToken.endsWith(QUOTATION_MARK)) {
				quoteStart = false;
			}
		}
		
		return quoteStart;
	}

	private boolean hasQuotes(String[] inputTokens){
		int quoteCommaCount = 0;
		for (String token: inputTokens) {
			if (token.startsWith(QUOTATION_MARK)) {
				quoteCommaCount++;
			}
			if (token.endsWith(QUOTATION_MARK)) {
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
		String mainCommand = parserVault.getResultType();
		return !mainCommand.isEmpty() && (isDominatingCommand(token) || isCommandThatNoNeedContent(token));
	}
	
	private boolean canHaveMultipleFields(String token) {
		return MULTIFIELD_RESULT_TYPE.contains(token);
	}

	@Override
	ArrayList<String> makeErrorResult(ERROR error, String token) {
		
		ArrayList<String> result = new ArrayList<String>(); 
		result.add(STATUS_ERROR);
		switch (error) {
			case INVALID_WORD:
				result.add("InvalidWordError: '" + token + "' is not recognised as a command or index");
				break;
			case INVALID_COMMAND:
				result.add("InvalidCommandError: '" + token + "' is not recognised as a command");
				break;
			case NO_COMMAND:
				result.add("NoCommandError: Please enter a command after the task index '" + token + "'");
				break;
			case DUPLICATE_COMMAND:
				result.add("DuplicateCommandError: Duplicate command '" + token + "'");
				break;
			case EMPTY_FIELD:
				result.add("EmptyFieldError: Please enter content for the command '" + token + "'");
				break;
			default:
				break; 
		}
		return result;
	}
}
