package main.java.backend.Logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.java.backend.Parser.Parser;
import main.java.backend.Storage.Storage;
import main.java.backend.Storage.StorageDatabase;
import main.java.backend.Storage.Task.Task;

public class LogicFacade {
	private static LogicFacade logicFacade;
	private static Storage storageComponent;
	private static LogicGetter getterSubComponent;
	private static Logger logicControllerLogger = Logger.getGlobal();	
	private FileHandler logHandler;
	private final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static Parser parserComponent;
	private static LogicCommandHandler logicCommandHandler;
	private static TreeMap<Integer, Task> currentState;
	private static TreeMap<Integer, Task> receivedFromHistoryState;
	private static Stack<Command> historyStack;
	
	private LogicFacade(String filename) {
		initLogger();
		storageComponent = new StorageDatabase();
		logicCommandHandler = LogicCommandHandler.getInstance(filename,storageComponent);
		storageComponent.init(filename);
		parserComponent = new Parser();
		getterSubComponent = LogicGetter.getInstance(storageComponent);
		historyStack = new Stack<Command>();
	}
	
	public static LogicFacade getInstance(String filename) {
		if (logicFacade == null) {
			logicFacade = new LogicFacade(filename);
		}
		return logicFacade;
	}
	
	private void initLogger() {
		try {
			logHandler = new FileHandler("LogicFacadeLog.txt",true);
			logHandler.setFormatter(new SimpleFormatter());
			logicControllerLogger.addHandler(logHandler);
			logicControllerLogger.setUseParentHandlers(false);
			
		} catch (SecurityException | IOException e) {
			logicControllerLogger.warning("Logger failed to initialise: " + e.getMessage());
		}
	}
	
	public String execute(String userInput) {
		ArrayList<String> parsedUserInput = parserComponent.parseInput(userInput);
		Command commandObject = logicCommandHandler.parse(parsedUserInput);
		System.out.println("CommandObject type: "+commandObject.getType());
		String feedbackString = commandObject.execute();
		System.out.println("feedbackString: "+feedbackString);
		historyStack.push(commandObject);
		return feedbackString;
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
		historyStack.peek().setIndex(list, i, taskIndex);
	}
	
	private void updateCurrentState() {
		currentState = storageComponent.load();
	}
	
//	private void updateHistoryStack() {
//		historySubComponent.push(currentState);
//	}

}
