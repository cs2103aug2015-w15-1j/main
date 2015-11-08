//@@author A0121284N
package main.java.backend.Logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import main.java.backend.Storage.Storage;
import main.java.backend.Util.LoggerGlobal;

public class LogicCommandHandler {
	
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_ADD_TODO = "addT";
	private static final String COMMAND_ADD_EVENT = "addE";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_SORT = "sort";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_VIEW = "view";
	private static final String COMMAND_EXIT = "exit";
	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_REDO = "redo";
	private static final String COMMAND_ERROR = "error";
	private static final String COMMAND_FILEPATH = "filepath";
	
	private static final String COMMAND_SET_FLOAT = "set";
	private static final String COMMAND_SET_TODO = "setT";
	private static final String COMMAND_SET_EVENT = "setE";
	private static final String COMMAND_DEADLINE = "deadline";
	private static final String COMMAND_EVENT = "event";
	private static final String COMMAND_DESCRIPTION = "description";
	private static final String COMMAND_REMINDER = "reminder";
	private static final String COMMAND_PRIORITY = "priority";
	private static final String COMMAND_RENAME = "rename";
	private static final String COMMAND_RESET = "reset";
	private static final String COMMAND_RECUR = "every";
	
	private static final String EMPTY = "";
	
	private static final String[] addKeywords = new String[] {"addF", "addT",
			"addE", "adds", "addcat"};
	private static final String[] editKeywords = new String[] {"set", "setT", 
			"setE", "deadline", "event", "description", "reminder", "done", "undone",
			"category","setCol","delete", "deleteAll", "priority", "reset", "rename","every"};
	private static final String[] sortKeywords = new String[] {"sortN", "sortP","sortD"};
	private static final String[] viewKeywords = new String[] {"showCat", "show floating", "showC",
			"showD", "show todo", "show events", "show overdue", "showT", "showE", "showO", "showF"};
	
	private static Logger logicCommandHandlerLogger = LoggerGlobal.getLogger();	
	
	private static LogicCommandHandler commandHandler;
	private static Storage storageComponent;
	private static History historySubComponent;
	
	private LogicCommandHandler(Storage storage, History history) {
		storageComponent = storage;
		historySubComponent = history;
	}

	public static LogicCommandHandler getInstance(Storage storage, History history) {
		if (commandHandler == null) {
			commandHandler = new LogicCommandHandler(storage,history);
		}
		return commandHandler;
	}
	
	public void exit() {
		System.exit(0);
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
		Command filePathCommandObject = new FilePathCommand(Command.Type.FILEPATH, storageComponent);
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
		ViewCommand viewCommandObject = new ViewCommand(Command.Type.VIEW, storageComponent);
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
		SearchCommand searchCommandObject = new SearchCommand(Command.Type.SEARCH, storageComponent);
		searchCommandObject.setCommandField(parsedUserInput.get(0));
		searchCommandObject.setKeywords(parsedUserInput.get(1));
		return searchCommandObject;
	}

	private Command initSortCommand(ArrayList<String> parsedUserInput) {
		SortCommand sortCommandObject = new SortCommand(Command.Type.SORT, storageComponent);
		sortCommandObject.setSortField(parsedUserInput.get(0));
		return sortCommandObject;
	}
	
	private void setCommand(Command editCommandObject, String description,
			String priority, String reminder, String startDateAndTime, String endDateAndTime, 
			String recurrence, String newName) {
		
		editCommandObject.setDescription(description);
		editCommandObject.setPriority(priority);
		editCommandObject.setEndDateAndTime(startDateAndTime);
		editCommandObject.setEndDateAndTime(endDateAndTime);
		editCommandObject.setReminder(reminder);
		editCommandObject.setRecurrence(recurrence);
		editCommandObject.setNewName(newName);	
	}

	private Command initAddCommand(ArrayList<String> parsedUserInput) {
		int inputLength = parsedUserInput.size();
		AddCommand addCommandObject = new AddCommand(Command.Type.ADD, 
				storageComponent, historySubComponent);
		addCommandObject.setCommandField(parsedUserInput.get(0));
		addCommandObject.setTaskName(parsedUserInput.get(1));
		
		setCommand(addCommandObject, parsedUserInput.get(2),
				parsedUserInput.get(inputLength-3), parsedUserInput.get(inputLength-2), 
				EMPTY, EMPTY, parsedUserInput.get(inputLength-1), EMPTY);

		switch (parsedUserInput.get(0)){
			case (COMMAND_ADD_TODO):
				addCommandObject.setEndDateAndTime(parsedUserInput.get(3));
				break;
			case (COMMAND_ADD_EVENT):
				addCommandObject.setStartDateAndTime(parsedUserInput.get(3));
				addCommandObject.setEndDateAndTime(parsedUserInput.get(4));
				break;
		}
		return addCommandObject;
	}

	private Command initEditCommand(ArrayList<String> parsedUserInput) {
		EditCommand editCommandObject = new EditCommand(Command.Type.EDIT, 
				storageComponent, historySubComponent);
		
		editCommandObject.setCommandField(parsedUserInput.get(0));
		if(parsedUserInput.size() > 1) {
			editCommandObject.setTaskName(parsedUserInput.get(1));
		}

		switch (parsedUserInput.get(0)) {
			case (COMMAND_SET_FLOAT) :
				setCommand(editCommandObject, parsedUserInput.get(2),
						parsedUserInput.get(3), parsedUserInput.get(4), EMPTY, EMPTY, 
						parsedUserInput.get(5), parsedUserInput.get(6));
				break;
			case (COMMAND_SET_TODO) :
				setCommand(editCommandObject, parsedUserInput.get(2),
						parsedUserInput.get(4), parsedUserInput.get(5), EMPTY, 
						parsedUserInput.get(3), parsedUserInput.get(6), parsedUserInput.get(7));
				break;
			case (COMMAND_SET_EVENT) :
				setCommand(editCommandObject, parsedUserInput.get(2),
						parsedUserInput.get(5), parsedUserInput.get(6), 
						parsedUserInput.get(3), parsedUserInput.get(4), 
						parsedUserInput.get(7), parsedUserInput.get(8));
				break;
			case (COMMAND_DEADLINE) :
				editCommandObject.setEndDateAndTime(parsedUserInput.get(2));
				break;
			case (COMMAND_EVENT) :
				editCommandObject.setStartDateAndTime(parsedUserInput.get(2));
				editCommandObject.setEndDateAndTime(parsedUserInput.get(3));
				break;
			case (COMMAND_DESCRIPTION) :
				editCommandObject.setDescription(parsedUserInput.get(2));
				break;
			case (COMMAND_REMINDER) :
				editCommandObject.setReminder(parsedUserInput.get(2));
				break;
			case (COMMAND_PRIORITY) :
				editCommandObject.setPriority(parsedUserInput.get(2));
				break;
			case (COMMAND_RENAME) :
				editCommandObject.setNewName(parsedUserInput.get(2));
				break;
			case(COMMAND_RESET) :
				editCommandObject.setResetField(parsedUserInput.get(2));
				break;
			case (COMMAND_RECUR) :
				editCommandObject.setRecurrence(parsedUserInput.get(2));
				break;
		}
		return editCommandObject;
	}

}
