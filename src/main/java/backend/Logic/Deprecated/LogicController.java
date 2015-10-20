package main.java.backend.Logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.java.backend.Parser.Parser;
import main.java.backend.Storage.Storage;
import main.java.backend.Storage.StorageDatabase;
import main.java.backend.Storage.Task.Task;

public class LogicController {
	
	private static LogicController logicObject;
	private static LogicCommandHandler commandHandlerSubComponent;
	private static LogicCreator creatorSubComponent;
	private static LogicEditor editorSubComponent;
	private static Observer getterSubComponent;
	private static LogicSorter sorterSubComponent;
	private static Storage storageComponent;
	private static Parser parserComponent;
	private static LogicSearch searcherSubComponent;
	private static LogicHistory historySubComponent;
	private static TreeMap<Integer, Task> currentState;
	private static TreeMap<Integer, Task> receivedFromHistoryState;
	private static Logger logicControllerLogger = Logger.getGlobal();	
	private FileHandler logHandler;
	private final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final String EXECUTION_COMMAND_UNSUCCESSFUL = "Invalid Command. Please try again.";
	
	private LogicController(String fileName) {
		
		parserComponent = new Parser();
		storageComponent = new StorageDatabase();
		sorterSubComponent = LogicSorter.getInstance();
		searcherSubComponent = LogicSearch.getInstance();
		historySubComponent = LogicHistory.getInstance();
		storageComponent.init(fileName);
		commandHandlerSubComponent = new LogicCommandHandler(parserComponent);
		creatorSubComponent = LogicCreator.getInstance(storageComponent);
		editorSubComponent = LogicEditor.getInstance(storageComponent);
		getterSubComponent = Observer.getInstance(storageComponent);
		updateCurrentState();
		updateHistoryStack();
		initLogger();
		logicControllerLogger.info("Logic component initialised successfully");
		
	}

	public static LogicController getInstance(String filename) {
		
		if (LogicController.logicObject == null) {
			LogicController.logicObject = new LogicController(filename);
		}
		return LogicController.logicObject;
	}
	
	private void initLogger() {
		
		try {
			logHandler = new FileHandler("LogicControllerLog.txt",true);
			logHandler.setFormatter(new SimpleFormatter());
			logicControllerLogger.addHandler(logHandler);
			logicControllerLogger.setUseParentHandlers(false);
			
		} catch (SecurityException | IOException e) {
			logicControllerLogger.warning("Logger failed to initialise: " + e.getMessage());
		}
	}
	
	public String executeCommand(String userInput) {
		
		assert (userInput != null);
		Command commandObject = commandHandlerSubComponent.parseCommand(userInput);
//		assert (commandObject.getType()!= null);
		logicControllerLogger.info("Command Received: "+ LINE_SEPARATOR +commandObject.getType());
		String feedbackString = "";
		
		try {
			feedbackString = runParsedCommand(commandObject, feedbackString);
		} catch (NullPointerException e) {
			feedbackString = EXECUTION_COMMAND_UNSUCCESSFUL;
			logicControllerLogger.info("Error caught " + e.getMessage());
		}
		return feedbackString;
	}

	private String runParsedCommand(Command commandObject, String feedbackString) {
		
		switch (commandObject.getType()) {
			case ADD :
				feedbackString = creatorSubComponent.execute(commandObject);
				updateCurrentState();
				updateHistoryStack();
				break;
			case EDIT :
				feedbackString = editorSubComponent.execute(commandObject);
				updateCurrentState();
				updateHistoryStack();
				break;
			case SORT :
//				feedbackString = sorterComponent.execute(commandObject);
				break;
			case SEARCH : 
//				feedbackString = searcherComponent.execute(commandObject);
				break;
			case EXIT :
				exit();
				break;
			case UNDO :
				feedbackString = undo();
				break;
			case REDO :
				feedbackString = redo();
				break;
			case VIEW: 
				feedbackString = commandObject.getCommandField();
				break;
			default:
				feedbackString = EXECUTION_COMMAND_UNSUCCESSFUL;
				logicControllerLogger.warning("Unknown parameters "+commandObject.toString());
		}
		logicControllerLogger.info("Execution successful: " + feedbackString);
		return feedbackString;
	}

	private String undo() {
		
		logicControllerLogger.fine("undoing");
		receivedFromHistoryState = historySubComponent.undo();
		assert(receivedFromHistoryState != null);
		if (receivedFromHistoryState == null) {
			return "There are no more Undos";
		}
		
		currentState = receivedFromHistoryState;
		logicControllerLogger.fine("received from history stack "+currentState);
		storageComponent.save(currentState);
		return "Undo successful";
	}
	
	private String redo() {
		System.out.println("redoing");
		logicControllerLogger.fine("redoing");
		receivedFromHistoryState = historySubComponent.redo();
		assert(receivedFromHistoryState != null);
		if (receivedFromHistoryState == null) {
			return "There are no more Redos";
		}
		currentState = receivedFromHistoryState;
		logicControllerLogger.fine("received from history stack "+currentState);
		storageComponent.save(currentState);
		return "Redo successful";
	}

	private void exit() {
		System.exit(0);
	}
	
	public ArrayList<String> retrieveStringData(String dataType){
		return getterSubComponent.retrieveStringData(dataType);
	}
	
	/*
	public ArrayList<Task> retrieveCategoryData(String dataType) {
		return getterSubComponent.retrieveCategoryData(dataType);
	}
	*/
	
	public ArrayList<Task> retrieveTaskData(String dataType) {
		return getterSubComponent.retrieveTaskData(dataType);
	}
	
	public void updateTaskNumbering(ArrayList<Task> list, int i, int taskIndex) {
		editorSubComponent.setIndex(list, i, taskIndex);
	}
	
	private void updateCurrentState() {
		currentState = storageComponent.load();
	}
	
	private void updateHistoryStack() {
		historySubComponent.push(currentState);
	}
}