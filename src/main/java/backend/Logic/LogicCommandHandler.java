package main.java.backend.Logic;

import java.util.ArrayList;
import java.util.Arrays;
import main.java.backend.Storage.Storage;

public class LogicCommandHandler {
	private static LogicCommandHandler commandHandler;
	private static Storage storageComponent;
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_SORT = "sort";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_VIEW = "view";
	private static final String COMMAND_EXIT = "exit";
	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_REDO = "redo";
	private static final String[] addKeywords = new String[] {"addF", "addT",
			"addE", "adds", "addcat"};
	private static final String[] editKeywords = new String[] {"set", "setT", 
			"setE", "deadline", "event", "description", "reminder", "done", "undone",
			"category","setCol","delete", "priority"};
	private static final String[] sortKeywords = new String[] {"sortp", "sortd"};
	private static final String[] viewKeywords = new String[] {"showCat", "show float",
			"show tasks", "show events", "show overdue"};
	private LogicCommandHandler(String filename, Storage storage) {
		storageComponent = storage;
		LogicHistory.getInstance();
	}

	public static LogicCommandHandler getInstance(String filename, Storage storage) {
		if (commandHandler == null) {
			commandHandler = new LogicCommandHandler(filename,storage);
		}
		return commandHandler;
	}

	public Command parse(ArrayList<String> parsedUserInput) {
		String determinedCommandType = determineCommandType(parsedUserInput.get(0));
//		System.out.println("determined Command Type: "+determinedCommandType);
		Command commandObject = new Command();
		switch (determinedCommandType) {
			case COMMAND_ADD:
				commandObject = initAddCommand(parsedUserInput);
				break;
			case COMMAND_EDIT:
				commandObject = initEditCommand(parsedUserInput);
				break;
			case COMMAND_SORT:
				commandObject= initSortCommand(parsedUserInput);
				break;
			case COMMAND_SEARCH:
				commandObject = initSearchCommand(parsedUserInput);
				break;
			case COMMAND_EXIT:
				commandObject = initExitCommand(parsedUserInput);
				break;
			case COMMAND_UNDO:
				commandObject = initUndoCommand(parsedUserInput);
				break;
			case COMMAND_REDO:
				commandObject = initRedoCommand(parsedUserInput);
				break;
			case COMMAND_VIEW:
				commandObject = initViewCommand(parsedUserInput);
				break;
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
		} else if (Arrays.asList(viewKeywords).contains(commandGiven)) {
			commandString = COMMAND_VIEW;
		} else if (commandGiven.contains(COMMAND_EXIT)) {
			commandString = COMMAND_EXIT;
		} else if (commandGiven.contains(COMMAND_UNDO)) {
			commandString = COMMAND_UNDO;
		} else if (commandGiven.contains(COMMAND_REDO)) {
			commandString = COMMAND_REDO;
		}
		return commandString;
	}

	private Command initViewCommand(ArrayList<String> parsedUserInput) {
		Command viewCommandObject = new ViewCommand(Command.Type.VIEW,storageComponent);
		viewCommandObject.setCommandField(parsedUserInput.get(0));
		return viewCommandObject;
	}
	
	private Command initRedoCommand(ArrayList<String> parsedUserInput) {
		Command redoCommandObject = new Command(Command.Type.REDO);
		redoCommandObject.setCommandField(parsedUserInput.get(0));
		return redoCommandObject;
	}

	private Command initUndoCommand(ArrayList<String> parsedUserInput) {
//		System.out.println("Initialising undoCommandObject");
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
		Command searchCommandObject = new Command(Command.Type.SEARCH);
		searchCommandObject.setCommandField(parsedUserInput.get(0));
		searchCommandObject.setKeywords(parsedUserInput.get(1));
		return searchCommandObject;
	}

	private Command initSortCommand(ArrayList<String> parsedUserInput) {
		Command sortCommandObject = new Command(Command.Type.SORT);
		sortCommandObject.setCommandField(parsedUserInput.get(0));
		return sortCommandObject;
	}

	private Command initAddCommand(ArrayList<String> parsedUserInput) {
		int inputLength = parsedUserInput.size();
		Command addCommandObject = new AddCommand(Command.Type.ADD,storageComponent);
		addCommandObject.setCommandField(parsedUserInput.get(0));
		addCommandObject.setTaskName(parsedUserInput.get(1));
		addCommandObject.setDescription(parsedUserInput.get(2));
		addCommandObject.setCategory(parsedUserInput.get(inputLength-1));
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
		Command editCommandObject = new EditCommand(Command.Type.EDIT,storageComponent);
		editCommandObject.setCommandField(parsedUserInput.get(0));
		editCommandObject.setTaskName(parsedUserInput.get(1));
		switch (parsedUserInput.get(0)) {
			case ("set") :
				editCommandObject.setDescription(parsedUserInput.get(2));
				editCommandObject.setPriority(parsedUserInput.get(3));
				editCommandObject.setReminder(parsedUserInput.get(4));
				editCommandObject.setCategory(parsedUserInput.get(5));
				break;
			case ("setT") :
				editCommandObject.setDescription(parsedUserInput.get(2));
				editCommandObject.setEndDateAndTime(parsedUserInput.get(3));
				editCommandObject.setPriority(parsedUserInput.get(4));
				editCommandObject.setReminder(parsedUserInput.get(5));
				editCommandObject.setCategory(parsedUserInput.get(6));
				break;
			case ("setE") :
				editCommandObject.setDescription(parsedUserInput.get(2));
				editCommandObject.setStartDateAndTime(parsedUserInput.get(3));
//				System.out.println("setE StartTime/Date: "+ parsedUserInput.get(3));
				editCommandObject.setEndDateAndTime(parsedUserInput.get(4));
//				System.out.println("setE EndTime/Date: "+ parsedUserInput.get(4));
				editCommandObject.setPriority(parsedUserInput.get(5));
				editCommandObject.setReminder(parsedUserInput.get(6));
				editCommandObject.setCategory(parsedUserInput.get(7));
				break;
			case ("deadline") :
				editCommandObject.setEndDateAndTime(parsedUserInput.get(2));
				break;
			case ("event") :
				editCommandObject.setStartDateAndTime(parsedUserInput.get(2));
				editCommandObject.setEndDateAndTime(parsedUserInput.get(3));
				break;
			case ("description") :
				editCommandObject.setDescription(parsedUserInput.get(2));
			 	break;
			case ("reminder") :
				editCommandObject.setReminder(parsedUserInput.get(2));
				break;
			case ("setcat") : 
				editCommandObject.setCategory(parsedUserInput.get(2));
			 	break;	
			case ("priority") :
				editCommandObject.setPriority(parsedUserInput.get(2));
		}
		return editCommandObject;
	}

}
