package main.java.backend.Logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.java.backend.History.History;
import main.java.backend.Parser.Parser;
import main.java.backend.Search.Search;
import main.java.backend.Sorter.Sorter;
import main.java.backend.Storage.Storage;
import main.java.backend.Storage.StorageDatabase;
import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.Task;

public class LogicController {
	
	private static LogicController logicObject;
	private static LogicCommandHandler commandHandlerSubComponent;
	private static LogicCreator creatorSubComponent;
	private static LogicEditor editorSubComponent;
	private static LogicGetter getterSubComponent;
	private static Sorter sorterComponent;
	private static Storage storageComponent;
	private static Parser parserComponent;
	private static Search searcherComponent;
	private static History historyComponent;
	private LogicToStorage logicToStorage;
	private static Logger logicControllerLogger = Logger.getGlobal();	
	private static TreeMap<String, Category> currentState;
	private ArrayList<Task> taskList;
	private FileHandler logHandler;
	private final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private LogicController(String fileName) {
		
		parserComponent = new Parser();
		storageComponent = new StorageDatabase();
		sorterComponent = new Sorter();
		searcherComponent = new Search();
		historyComponent = new History();
		logicToStorage = LogicToStorage.getInstance();
		storageComponent.init(fileName);
		commandHandlerSubComponent = new LogicCommandHandler(parserComponent);
		creatorSubComponent = LogicCreator.getInstance(storageComponent);
		editorSubComponent = LogicEditor.getInstance(storageComponent);
		getterSubComponent = LogicGetter.getInstance(storageComponent);
		updateCurrentState();
		updateHistoryStack();
		System.out.println("Logic component initialised successfully");
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
			logicControllerLogger.warning("Logger failed to initialise");
			e.printStackTrace();
		}
		
		
	}
	
	public String executeCommand(String userInput) {
		Command commandObject = commandHandlerSubComponent.parseCommand(userInput);
		assert (commandObject.getType()!= null);
		logicControllerLogger.info("Command Received: "+ LINE_SEPARATOR +commandObject.getType());
		String feedbackString = "";
		try {
			feedbackString = runParsedCommand(commandObject, feedbackString);
		} catch (NullPointerException e) {
			logicControllerLogger.info("Error caught" + e.getMessage());
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
			default:
				System.out.println("Invalid Command. Please try again");
				logicControllerLogger.warning("Unknown parameters"+commandObject.toString());
		}
		return feedbackString;
	}

	private String undo() {
		System.out.println("undoing");
		currentState = historyComponent.pop();
		if (currentState == null) {
			return "no more Undos";
		}
		System.out.println("received from history stack "+currentState);
		storageComponent.save(currentState);
		return "Undo successful";
	}

	private void exit() {
		System.exit(0);
	}
	
	public ArrayList<String> retrieveStringData(String dataType){
		return getterSubComponent.retrieveStringData(dataType);
	}
	
	public ArrayList<Category> retrieveCategoryData(String dataType) {
		return getterSubComponent.retrieveCategoryData(dataType);
	}
	
	public ArrayList<Task> retrieveTaskData(String dataType) {
		return getterSubComponent.retrieveTaskData(dataType);
	}
	
	public void updateTaskNumbering(ArrayList<Task> list, int i, int index) {
		editorSubComponent.setindex(list, i, index, currentState);
	}
	
	private void updateCurrentState() {
		currentState = storageComponent.load();
		taskList = logicToStorage.getTaskList(currentState);
	}
	
	private void updateHistoryStack() {
		historyComponent.push(currentState);
	}
}