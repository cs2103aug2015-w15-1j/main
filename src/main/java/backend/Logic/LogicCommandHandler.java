//@@author A0121284N
package main.java.backend.Logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.java.backend.Storage.Storage;

public class LogicCommandHandler {
	private static LogicCommandHandler commandHandler;
	private static Storage storageComponent;
	private static History historySubComponent;
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_SORT = "sort";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_VIEW = "view";
	private static final String COMMAND_EXIT = "exit";
	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_REDO = "redo";
	private static final String COMMAND_ERROR = "error";
	private static final String COMMAND_FILEPATH = "filepath";
	private static final String[] addKeywords = new String[] {"addF", "addT",
			"addE", "adds", "addcat"};
	private static final String[] editKeywords = new String[] {"set", "setT", 
			"setE", "deadline", "event", "description", "reminder", "done", "undone",
			"category","setCol","delete", "deleteAll", "priority", "reset", "rename","every"};
	private static final String[] sortKeywords = new String[] {"sortN", "sortP","sortD"};
	private static final String[] viewKeywords = new String[] {"showCat", "show floating", "showC",
			"showD", "show todo", "show events", "show overdue", "showT", "showE", "showO", "showF"};
	private static Logger logicCommandHandlerLogger = Logger.getGlobal();	
	private FileHandler logHandler;
	
	private LogicCommandHandler(Storage storage, History history) {
		storageComponent = storage;
		historySubComponent = history;
		initLogger();
	}

	public static LogicCommandHandler getInstance(Storage storage, History history) {
		if (commandHandler == null) {
			commandHandler = new LogicCommandHandler(storage,history);
		}
		return commandHandler;
	}
	
	public void exit() {
		logHandler.close();
		System.exit(0);
	}
	
	//@@author A0121284N
			private void initLogger() {
					
					try {
						logHandler = new FileHandler("TankTaskLog.txt",1000000000,10,true);
						logHandler.setFormatter(new SimpleFormatter());
						logicCommandHandlerLogger.addHandler(logHandler);
						logicCommandHandlerLogger.setUseParentHandlers(false);
						
					} catch (SecurityException | IOException e) {
						logicCommandHandlerLogger.warning("Logger failed to initialise: " + e.getMessage());
					}
				}

	public Command parse(ArrayList<String> parsedUserInput) {
		logicCommandHandlerLogger.info("Command from parser: "+parsedUserInput.get(0));
		String determinedCommandType = determineCommandType(parsedUserInput.get(0));
		logicCommandHandlerLogger.info("determined Command Type: "+determinedCommandType);
		Command commandObject = new Command();
		switch (determinedCommandType) {
			case COMMAND_ADD :
				commandObject = initAddCommand(parsedUserInput);
				break;
			case COMMAND_EDIT :
				commandObject = initEditCommand(parsedUserInput);
				break;
			case COMMAND_SORT :
				commandObject= initSortCommand(parsedUserInput);
				break;
			case COMMAND_SEARCH :
				commandObject = initSearchCommand(parsedUserInput);
				break;
			case COMMAND_EXIT :
				commandObject = initExitCommand(parsedUserInput);
				break;
			case COMMAND_UNDO :
				commandObject = initUndoCommand(parsedUserInput);
				break;
			case COMMAND_REDO :
				commandObject = initRedoCommand(parsedUserInput);
				break;
			case COMMAND_VIEW :
				commandObject = initViewCommand(parsedUserInput);
				break;
			case COMMAND_ERROR :
				commandObject = initErrorCommand(parsedUserInput);
				break;
			case COMMAND_FILEPATH :
				commandObject = initFilePathCommand(parsedUserInput);
		}
	return commandObject;
	}

	private String determineCommandType(String commandGiven) {
		String commandString = "";
		if (Arrays.asList(addKeywords).contains(commandGiven)){
			commandString =  COMMAND_ADD;
		} else if (Arrays.asList(editKeywords).contains(commandGiven)) {
			commandString = COMMAND_EDIT;
		} else if (Arrays.asList(sortKeywords).contains(commandGiven)) {
			commandString = COMMAND_SORT;
		} else if (commandGiven.contains(COMMAND_SEARCH)) {
			commandString = COMMAND_SEARCH;
		} else if (commandGiven.contains(COMMAND_ERROR)) {
			commandString = COMMAND_ERROR;
		} else if (Arrays.asList(viewKeywords).contains(commandGiven)) {
			commandString = COMMAND_VIEW;
		} else if (commandGiven.contains(COMMAND_EXIT)) {
			commandString = COMMAND_EXIT;
		} else if (commandGiven.contains(COMMAND_UNDO)) {
			commandString = COMMAND_UNDO;
		} else if (commandGiven.contains(COMMAND_REDO)) {
			commandString = COMMAND_REDO;
		} else if (commandGiven.contains(COMMAND_FILEPATH)) {
			commandString = COMMAND_FILEPATH;
		}
		return commandString;
	}
	
	private Command initFilePathCommand(ArrayList<String> parsedUserInput) {
		Command filePathCommandObject = new FilePathCommand(Command.Type.FILEPATH,storageComponent);
		filePathCommandObject.setFilePath(parsedUserInput.get(1));
		return filePathCommandObject;
	}
	
	private Command initErrorCommand(ArrayList<String> parsedUserInput) {
		ErrorCommand errorCommandObject = new ErrorCommand(Command.Type.ERROR);
		errorCommandObject.setCommandField(parsedUserInput.get(0));
		errorCommandObject.setErrorMessage(parsedUserInput.get(1));
		return errorCommandObject;
	}

	private Command initViewCommand(ArrayList<String> parsedUserInput) {
		ViewCommand viewCommandObject = new ViewCommand(Command.Type.VIEW,storageComponent);
		viewCommandObject.setCommandField(parsedUserInput.get(0));
		return viewCommandObject;
	}
	
	private Command initRedoCommand(ArrayList<String> parsedUserInput) {
		Command redoCommandObject = new Command(Command.Type.REDO);
		redoCommandObject.setCommandField(parsedUserInput.get(0));
		return redoCommandObject;
	}

	private Command initUndoCommand(ArrayList<String> parsedUserInput) {
		Command undoCommandObject = new Command(Command.Type.UNDO);
		undoCommandObject.setCommandField(parsedUserInput.get(0));
		return undoCommandObject;
	}
	
	private Command initExitCommand(ArrayList<String> parsedUserInput) {
		Command exitCommandObject = new Command(Command.Type.EXIT);
		exitCommandObject.setCommandField(parsedUserInput.get(0));
		return exitCommandObject;
	}

	private Command initSearchCommand(ArrayList<String> parsedUserInput) {
		SearchCommand searchCommandObject = new SearchCommand(Command.Type.SEARCH,storageComponent);
		searchCommandObject.setCommandField(parsedUserInput.get(0));
		searchCommandObject.setKeywords(parsedUserInput.get(1));
		return searchCommandObject;
	}

	private Command initSortCommand(ArrayList<String> parsedUserInput) {
		SortCommand sortCommandObject = new SortCommand(Command.Type.SORT,storageComponent);
		sortCommandObject.setSortField(parsedUserInput.get(0));
		return sortCommandObject;
	}

	private Command initAddCommand(ArrayList<String> parsedUserInput) {
		int inputLength = parsedUserInput.size();
		AddCommand addCommandObject = new AddCommand(Command.Type.ADD,storageComponent,historySubComponent);
		addCommandObject.setCommandField(parsedUserInput.get(0));
		addCommandObject.setTaskName(parsedUserInput.get(1));
		addCommandObject.setDescription(parsedUserInput.get(2));
		addCommandObject.setRecurrence(parsedUserInput.get(inputLength-1));
		addCommandObject.setReminder(parsedUserInput.get(inputLength-2));
		addCommandObject.setPriority(parsedUserInput.get(inputLength-3));
		switch (parsedUserInput.get(0)){
			case ("addT"):
				addCommandObject.setEndDateAndTime(parsedUserInput.get(3));
				break;
			case ("addE"):
				addCommandObject.setStartDateAndTime(parsedUserInput.get(3));
				addCommandObject.setEndDateAndTime(parsedUserInput.get(4));
				break;
		}
		return addCommandObject;
	}
	
	private Command initEditCommand(ArrayList<String> parsedUserInput) {
		EditCommand editCommandObject = new EditCommand(Command.Type.EDIT,storageComponent,historySubComponent);
		editCommandObject.setCommandField(parsedUserInput.get(0));
		switch (parsedUserInput.get(0)) {
			case ("set") :
				editCommandObject.setTaskName(parsedUserInput.get(1));
				editCommandObject.setDescription(parsedUserInput.get(2));
				editCommandObject.setPriority(parsedUserInput.get(3));
				editCommandObject.setReminder(parsedUserInput.get(4));
				editCommandObject.setRecurrence(parsedUserInput.get(5));
				editCommandObject.setNewName(parsedUserInput.get(6));
				break;
			case ("setT") :
				editCommandObject.setTaskName(parsedUserInput.get(1));
				editCommandObject.setDescription(parsedUserInput.get(2));
				editCommandObject.setEndDateAndTime(parsedUserInput.get(3));
				editCommandObject.setPriority(parsedUserInput.get(4));
				editCommandObject.setReminder(parsedUserInput.get(5));
				editCommandObject.setRecurrence(parsedUserInput.get(6));
				editCommandObject.setNewName(parsedUserInput.get(7));
				break;
			case ("setE") :
				editCommandObject.setTaskName(parsedUserInput.get(1));
				editCommandObject.setDescription(parsedUserInput.get(2));
				editCommandObject.setStartDateAndTime(parsedUserInput.get(3));
				editCommandObject.setEndDateAndTime(parsedUserInput.get(4));
				editCommandObject.setPriority(parsedUserInput.get(5));
				editCommandObject.setReminder(parsedUserInput.get(6));
				editCommandObject.setRecurrence(parsedUserInput.get(7));
				editCommandObject.setNewName(parsedUserInput.get(8));
				break;
			case ("deadline") :
				editCommandObject.setTaskName(parsedUserInput.get(1));
				editCommandObject.setEndDateAndTime(parsedUserInput.get(2));
				break;
			case ("event") :
				editCommandObject.setTaskName(parsedUserInput.get(1));
				editCommandObject.setStartDateAndTime(parsedUserInput.get(2));
				editCommandObject.setEndDateAndTime(parsedUserInput.get(3));
				break;
			case ("description") :
				editCommandObject.setTaskName(parsedUserInput.get(1));
				editCommandObject.setDescription(parsedUserInput.get(2));
			 	break;
			case ("reminder") :
				editCommandObject.setTaskName(parsedUserInput.get(1));
				editCommandObject.setReminder(parsedUserInput.get(2));
				break;
			case ("priority") :
				editCommandObject.setTaskName(parsedUserInput.get(1));
				editCommandObject.setPriority(parsedUserInput.get(2));
				break;
			case ("rename") :
				editCommandObject.setTaskName(parsedUserInput.get(1));
				editCommandObject.setNewName(parsedUserInput.get(2));
				break;
			case("reset") :
				editCommandObject.setTaskName(parsedUserInput.get(1));
				editCommandObject.setResetField(parsedUserInput.get(2));
				break;
			case("delete") :
				editCommandObject.setTaskName(parsedUserInput.get(1));
				break;
			case("done") :
				editCommandObject.setTaskName(parsedUserInput.get(1));
				break;
			case ("undone") :
				editCommandObject.setTaskName(parsedUserInput.get(1));
				break;
			case ("every") :
				editCommandObject.setTaskName(parsedUserInput.get(1));
				editCommandObject.setRecurrence(parsedUserInput.get(2));
				break;
		}
		return editCommandObject;
	}

}
