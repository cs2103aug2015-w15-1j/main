package main.java.backend.Logic;

import java.io.IOException;
import java.util.ArrayList;
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
	private static Logger logicControllerLogger = Logger.getGlobal();	
	private FileHandler logHandler;
	private static Parser parserComponent;
	private static LogicCommandHandler logicCommandHandler;
	private static Stack<Command> historyStack;
	private static Stack<Command> futureStack;
	private static final String EXECUTION_COMMAND_UNSUCCESSFUL = "Invalid Command. Please try again.";
	
	private LogicFacade(String filename) {
		initLogger();
		storageComponent = new StorageFacade();
		logicCommandHandler = LogicCommandHandler.getInstance(filename,storageComponent);
		storageComponent.init(filename);
		parserComponent = new Parser();
		getterSubComponent = Observer.getInstance(storageComponent);
		historyStack = new Stack<Command>();
		futureStack = new Stack<Command>();
		storageComponent.load();
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
		try {
			System.out.println("History stack size before command execution: "+historyStack.size());
			ArrayList<String> parsedUserInput = parserComponent.parseInput(userInput);
			Command commandObject = logicCommandHandler.parse(parsedUserInput);
//			System.out.println("CommandObject type: "+commandObject.getType());
			String feedbackString = "";
			switch (commandObject.getType()) {
				case UNDO:
//					System.out.println(feedbackString);
					Command undoableCommand = historyStack.pop();
					feedbackString = undoableCommand.undo();
					futureStack.push(undoableCommand);
//					futureStack.push(historyStack.pop());
					break;
				case REDO:
					feedbackString = futureStack.peek().redo();
					historyStack.push(futureStack.pop());
					break;
				case VIEW:
					feedbackString = commandObject.getCommandField();
				case EXIT:
					System.exit(0);
				default:
					feedbackString = commandObject.execute();
					historyStack.push(commandObject);
			}
			getterSubComponent.updateIndex();
//			System.out.println("feedbackString: "+feedbackString);
			System.out.println("History stack size after command execution: "+historyStack.size());
			return feedbackString;
		} catch (NullPointerException | EmptyStackException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
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
	
//	public void updateTaskNumbering(ArrayList<Task> list, int i, int taskIndex) {
//		getterSubComponent.setIndex(list, i, taskIndex);
//	}
	
//	public ArrayList<String> retrieveStringData(String dataType){
//		return getterSubComponent.retrieveStringData(dataType);
//}
	
	private void updateCurrentState() {
		storageComponent.load();
	}

}
