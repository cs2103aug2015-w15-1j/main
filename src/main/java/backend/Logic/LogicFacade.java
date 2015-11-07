//@@author A0121284N
package main.java.backend.Logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.java.backend.Parser.Parser;
import main.java.backend.Storage.Storage;
import main.java.backend.Storage.StorageFacade;
import main.java.backend.Storage.Task.Task;

public class LogicFacade {
	private static LogicFacade logicFacade;
	private static Storage storageComponent;
	private static Observer getterSubComponent;
	private static History historySubComponent;
	private static Logger logicControllerLogger = Logger.getGlobal();	
	private FileHandler logHandler;
	private static Parser parserComponent;
	private static LogicCommandHandler logicCommandHandler;
	private static Stack<Command> historyStack;
	private static Stack<Command> futureStack;
	private static Stack<Command> searchStack;
	private ArrayList<Task> currentState;
	private static final String EXECUTION_COMMAND_UNSUCCESSFUL = "Invalid Command. Please try again.";
	
	private LogicFacade() {
		initLogger();
		storageComponent = new StorageFacade();
		historySubComponent = History.getInstance();
		logicCommandHandler = LogicCommandHandler.getInstance(storageComponent,historySubComponent);
		storageComponent.init();
		parserComponent = new Parser();
		getterSubComponent = Observer.getInstance(storageComponent);
		historyStack = new Stack<Command>();
		futureStack = new Stack<Command>();
		searchStack = new Stack<Command>();
		currentState = storageComponent.load();
		historySubComponent.push(currentState);
	}
	
	public static LogicFacade getInstance() {
		if (logicFacade == null) {
			logicFacade = new LogicFacade();
		}
		return logicFacade;
	}
	
	private void initLogger() {
		try {
			logHandler = new FileHandler("TankTaskLog.txt",true);
			logHandler.setFormatter(new SimpleFormatter());
			logicControllerLogger.addHandler(logHandler);
			logicControllerLogger.setUseParentHandlers(false);
			
		} catch (SecurityException | IOException e) {
			logicControllerLogger.warning("Logger failed to initialise: " + e.getMessage());
		}
	}
	
	public String execute(String userInput) {
		try {
			ArrayList<String> parsedUserInput = parserComponent.parseInput(userInput);
			System.out.println("Parsed User Input" + parsedUserInput);
			Command commandObject = logicCommandHandler.parse(parsedUserInput);
			String feedbackString = "";
			switch (commandObject.getType()) {
				case UNDO :
					Command undoableCommand = historyStack.pop();
					feedbackString = undoableCommand.undo();
					futureStack.push(undoableCommand);
					break;
				case REDO :
					feedbackString = futureStack.peek().redo();
					historyStack.push(futureStack.pop());
					break;
				case VIEW :
					feedbackString = commandObject.getCommandField();
					break;
				case ERROR :
					feedbackString = commandObject.getErrorMessage();
					break;
				case SEARCH :
					feedbackString = commandObject.execute();
					searchStack.push(commandObject);
					break;
				case FILEPATH :
					feedbackString = commandObject.execute();
					break;
				case EXIT :
					System.exit(0);
				default :
					feedbackString = commandObject.execute();
					historyStack.push(commandObject);
					futureStack = new Stack<Command>();
			}
			getterSubComponent.updateIndex();
			return feedbackString;
		} catch (NullPointerException | EmptyStackException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	public ArrayList<String> retrieveStringData(String dataType){
		return getterSubComponent.retrieveStringData(dataType);
	}
	
	public ArrayList<Task> retrieveTaskData(String dataType) {
		return getterSubComponent.retrieveTaskData(dataType);
	}
	
	public ArrayList<Task> retrieveSearchData() {
		if(!searchStack.isEmpty()) {
			searchStack.peek().execute();
			ArrayList<Task> searchResults = searchStack.peek().getSearchResults();
			getterSubComponent.updateSearchResultsList(searchResults);
			
		}
		return getterSubComponent.getSearchResultsList();
	}
}
