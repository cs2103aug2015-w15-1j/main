package main.java.backend.Logic;

import java.util.ArrayList;

import main.java.backend.History.HistoryStub;
import main.java.backend.Parser.ParserStub;
import main.java.backend.Storage.StorageStub;

public class Logic {
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_SET_CATEGORY_COLOUR = "setCol";
	private static final String COMMAND_SHOW_CATEGORY = "showCat";
	private static final String COMMAND_SET_CATEGORY = "setCat";
	private static final String COMMAND_ADD_CATEGORY = "addCat";
	private static final String COMMAND_ADD_SUBTASK = "addS";
	private static final String COMMAND_SUBTASK = "subtask";
	private static final String COMMAND_SORT_DEADLINE = "sortD";
	private static final String COMMAND_SORT_PRIORITY = "sortP";
	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_UNDONE = "undone";
	private static final String COMMAND_DONE = "done";
	private static final String COMMAND_RETURN = "return";
	private static final String COMMAND_SET_REMINDER = "reminder";
	private static final String COMMAND_SET_DESCRIPTION = "description";
	private static final String COMMAND_SET_START_AND_END_TIME = "event";
	private static final String COMMAND_SET_DEADLINE = "deadline";
	private static final String COMMAND_ADD_FLOATING_TASK = "addFT";
	private static final String COMMAND_ADD_EVENT = "addE";
	private static final String COMMAND_ADD_TASK = "addT";
	private static HistoryStub historyComponent;
	private static ParserStub parserComponent;
	private static StorageStub storageComponent;
	private static String getFeedbackAfterCommandExecution;
	private static ArrayList<String> currentState;

	public Logic(String filename) {
		initLogic(filename);
	}

	private void initLogic(String filename) {
		historyComponent = new HistoryStub();
		parserComponent = new ParserStub();
		storageComponent = new StorageStub(filename);
	}

	public String executeCommand(String userInput) {
		ArrayList<String> getParsedInput = parserComponent.parseInput(userInput);
		String commandType = getParsedInput.get(0);
		switch (commandType) {
			case COMMAND_ADD_TASK:
				getFeedbackAfterCommandExecution = addTask(getParsedInput);
			case COMMAND_ADD_EVENT:
				getFeedbackAfterCommandExecution = addEvent(getParsedInput);
			case COMMAND_ADD_FLOATING_TASK:
				getFeedbackAfterCommandExecution = addFloatingTask(getParsedInput);
			case COMMAND_SET_DEADLINE:
				getFeedbackAfterCommandExecution = setDeadline(getParsedInput);
			case COMMAND_SET_START_AND_END_TIME:
				getFeedbackAfterCommandExecution = setEventStartAndEndTime(getParsedInput);
			case COMMAND_SET_DESCRIPTION:
				getFeedbackAfterCommandExecution = setDescription(getParsedInput);
			case COMMAND_SET_REMINDER:
				getFeedbackAfterCommandExecution = setReminder(getParsedInput);
			case COMMAND_RETURN:
				getFeedbackAfterCommandExecution = returnToHomeScreen(currentState);
			case COMMAND_DONE:
				getFeedbackAfterCommandExecution = setDone(getParsedInput);
			case COMMAND_UNDONE:
				getFeedbackAfterCommandExecution = setUndone(getParsedInput);
			case COMMAND_UNDO:
				getFeedbackAfterCommandExecution = undo(getParsedInput);
			case COMMAND_SORT_PRIORITY:
				getFeedbackAfterCommandExecution = sortPriority(getParsedInput);
			case COMMAND_SORT_DEADLINE:
				getFeedbackAfterCommandExecution = sortDate(getParsedInput);
			case COMMAND_SUBTASK:
				getFeedbackAfterCommandExecution = subtask(getParsedInput);
			case COMMAND_ADD_SUBTASK:
				getFeedbackAfterCommandExecution = addSubtask(getParsedInput);
			case COMMAND_ADD_CATEGORY:
				getFeedbackAfterCommandExecution = addCategory(getParsedInput);
			case COMMAND_SET_CATEGORY:
				getFeedbackAfterCommandExecution = setCategory(getParsedInput);
			case COMMAND_SHOW_CATEGORY:
				getFeedbackAfterCommandExecution = showCategory(getParsedInput);
			case COMMAND_SET_CATEGORY_COLOUR:
				getFeedbackAfterCommandExecution = setColour(getParsedInput);
			case COMMAND_SEARCH:
				getFeedbackAfterCommandExecution = search(getParsedInput);
		}
		return getFeedbackAfterCommandExecution;
		
	}

	private String search(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		return "found";
	}

	private String setColour(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		return "colour set";
	}

	private String showCategory(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		return "category shown";
	}

	private String setCategory(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		return "category set";
	}

	private String addCategory(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		return null;
	}

	private String addSubtask(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		return null;
	}

	private String subtask(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		return null;
	}

	private String sortDate(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		return null;
	}

	private String sortPriority(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		return null;
	}

	private String undo(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		return null;
	}

	private String setUndone(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		return null;
	}

	private String setDone(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		return null;
	}

	private String returnToHomeScreen(ArrayList<String> currentState2) {
		// TODO Auto-generated method stub
		return null;
	}

	private String setReminder(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		return null;
	}

	private String setDescription(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		return null;
	}

	private String setEventStartAndEndTime(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		return null;
	}

	private String setDeadline(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		return null;
	}

	private String addFloatingTask(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		return null;
	}

	private String addEvent(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		
	}

	private String addTask(ArrayList<String> getParsedInput) {
		// TODO Auto-generated method stub
		
	}
	
}
