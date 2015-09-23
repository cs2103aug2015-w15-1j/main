package main.java.backend.Logic;

import java.util.ArrayList;

import main.java.backend.History.History;
import main.java.backend.Parser.ParserStub;
import main.java.backend.Search.Search;
import main.java.backend.Sorter.Sorter;
import main.java.backend.Storage.StorageData;
import main.java.backend.Storage.Task.Task;

public class Logic {
	private static final String EXECUTION_DELETE_SUCCESSFUL = "Task %1$s has been deleted";
	private static final String COMMAND_DELETE = "delete";
	private static final String EXECUTION_SET_DEADLINE_SUCCESSFUL = "Task %1$s deadline has been set to %2$s";
	private static final String EXECUTION_SET_EVENT_START_AND_END_TIME_SUCCESSFUL = "Event %1$s has been setted to %2$s till %3$s";
	private static final String EXECUTION_SET_DESCRIPTION_SUCCESSFUL = "Description for task %1$s has been set";
	private static final String EXECUTION_SET_REMINDER_SUCCESSFUL = "Reminder for Task %1$s has been set to be at %2$s";
	private static final String EXECUTION_SUBTASK_SUCCESSFUL = "Task %1$s is now able to implement subtasks";
	private static final String EXECUTION_ADD_SUBTASK_SUCCESSFUL = "SubTask %1$s is added to Task %2$s";
	private static final String EXECUTION_ADD_CATEGORY_SUCCESSFUL = "Category %1$s has been added";
	private static final String EXECUTION_SET_CATEGORY_SUCCESSFUL = "Task %1$s is set to the category %2$s";
	private static final String EXECUTION_SHOW_CATEGORY_SUCCESSFUL = "category shown";
	private static final String EXECUTION_SHOW_TASKS_SUCCESSFUL = "tasks shown";
	private static final String EXECUTION_SHOW_FLOATING_TASKS_SUCCESSFUL = "floating tasks shown";
	private static final String EXECUTION_SHOW_EVENT_SUCCESSFUL = "events shown";
	private static final String EXECUTION_SET_COLOUR_SUCCESSFUL = "Category %1$s is set to the colour %2$s";
	private static final String EXECUTION_RETURN_COMMAND_SUCCESSFUL = "Returning to home";
	private static final String EXECUTION_UNDONE_COMMAND_SUCCESSFUL = "Task %1$s is completed";
	private static final String EXECUTION_DONE_COMMAND_SUCCESSFUL = "Task %1$s is not completed";
	private static final String EXECUTION_ADD_EVENT_SUCCESSFUL = "Event %1$s has been added";
	private static final String EXECUTION_ADD_TASK_SUCCESSFUL = "Task %1$s has been added";
	private static final String EXECUTION_UNDO_COMMAND_SUCCESSFUL = "undo successful";
	private static final String EXECUTION_SORT_COMMAND_SUCCESSFUL = "sorted by %1$s";
	private static final String EXECUTION_SEARCH_UNSUCCESSFUL = "Keyword %1$s not found";
	private static final String EXECUTION_SEARCH_SUCCESSFUL = "keyword found";
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
	private static final String COMMAND_SHOW_TASK = "showT";
	private static final String COMMAND_SHOW_FLOATING_TASK = "showFT";
	private static final String COMMAND_SHOW_EVENT = "showE";
	private static History historyComponent;
	private static ParserStub parserComponent;
	private static StorageData storageComponent;
	private static Sorter sortComponent;
	private static Search searchComponent;
	private static String getFeedbackAfterCommandExecution;
	private static ArrayList<Task> currentState;
	private static ArrayList<Task> searchResults;
	private static ArrayList<String> categoryList;

	public Logic(String filename) {
		initLogic(filename);
	}

	void initLogic(String filename) {
		historyComponent = new History();
		parserComponent = new ParserStub();
		storageComponent = new StorageData(filename);
		searchComponent = new Search();
	}

	public String executeCommand(String userInput) {
		ArrayList<String> getParsedInput = parserComponent.parseInput(userInput);
//		arrayChecker(getParsedInput);
//		System.out.println("Array got");
//		System.out.println(getParsedInput);
		String commandType = getParsedInput.get(0);
//		System.out.println("Command type: "+commandType);
		switch (commandType) {
			case COMMAND_ADD_TASK:
				getFeedbackAfterCommandExecution = addTask(getParsedInput);
				break;
			case COMMAND_ADD_EVENT:
				getFeedbackAfterCommandExecution = addEvent(getParsedInput);
				break;
			case COMMAND_ADD_FLOATING_TASK:
				getFeedbackAfterCommandExecution = addFloatingTask(getParsedInput);
				break;
			case COMMAND_SET_DEADLINE:
				getFeedbackAfterCommandExecution = setDeadline(getParsedInput);
				break;
			case COMMAND_SET_START_AND_END_TIME:
				getFeedbackAfterCommandExecution = setEventStartAndEndTime(getParsedInput);
				break;
			case COMMAND_SET_DESCRIPTION:
				getFeedbackAfterCommandExecution = setDescription(getParsedInput);
				break;
			case COMMAND_SET_REMINDER:
				getFeedbackAfterCommandExecution = setReminder(getParsedInput);
				break;
			case COMMAND_RETURN:
				getFeedbackAfterCommandExecution = returnToHomeScreen(currentState);
				break;
			case COMMAND_DONE:
				getFeedbackAfterCommandExecution = setDone(getParsedInput);
				break;
			case COMMAND_UNDONE:
				getFeedbackAfterCommandExecution = setUndone(getParsedInput);
				break;
			case COMMAND_UNDO:
				getFeedbackAfterCommandExecution = undo();
				break;
			case COMMAND_SORT_PRIORITY:
				getFeedbackAfterCommandExecution = sort(COMMAND_SORT_PRIORITY,currentState);
				break;
			case COMMAND_SORT_DEADLINE:
				getFeedbackAfterCommandExecution = sort(COMMAND_SORT_DEADLINE,currentState);
				break;
			case COMMAND_SUBTASK:
				getFeedbackAfterCommandExecution = subTask(getParsedInput);
				break;
			case COMMAND_ADD_SUBTASK:
				getFeedbackAfterCommandExecution = addSubTask(getParsedInput);
				break;
			case COMMAND_ADD_CATEGORY:
				getFeedbackAfterCommandExecution = addCategory(getParsedInput);
				break;
			case COMMAND_SET_CATEGORY:
				getFeedbackAfterCommandExecution = setCategory(getParsedInput);
				break;
			case COMMAND_SHOW_CATEGORY:
				getFeedbackAfterCommandExecution = showCategory(getParsedInput);
				break;
			case COMMAND_SET_CATEGORY_COLOUR:
				getFeedbackAfterCommandExecution = setColour(getParsedInput);
				break;
			case COMMAND_SEARCH:
				getFeedbackAfterCommandExecution = search(getParsedInput);
				break;
			case COMMAND_DELETE:
				getFeedbackAfterCommandExecution = delete(getParsedInput);
				break;
			case COMMAND_SHOW_TASK:
				getFeedbackAfterCommandExecution = showTask();
				break;
			case COMMAND_SHOW_FLOATING_TASK:
				getFeedbackAfterCommandExecution = showFloatingTask();
				break;
			case COMMAND_SHOW_EVENT:
				getFeedbackAfterCommandExecution = showEvent();
				break;
		}
		return getFeedbackAfterCommandExecution;
		
	}

	private void arrayChecker(ArrayList<String> getParsedInput) {
		System.out.println("Parsed Input Array Check");
		for (int i = 0; i < getParsedInput.size(); i++) {
			System.out.print(getParsedInput.get(i)+" ");
		}
		
	}

	private String showEvent() {
		return EXECUTION_SHOW_EVENT_SUCCESSFUL;
	}

	private String showFloatingTask() {
		return EXECUTION_SHOW_FLOATING_TASKS_SUCCESSFUL;
	}

	private String showTask() {
		return EXECUTION_SHOW_TASKS_SUCCESSFUL;
	}

	private String delete(ArrayList<String> getParsedInput) {
		String taskId = getParsedInput.get(1);
		storageComponent.delete(taskId);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_DELETE_SUCCESSFUL, taskId);
	}

	private String search(ArrayList<String> getParsedInput) {
		String keyword = getParsedInput.get(1);
		searchResults = searchComponent.search(currentState,keyword);
		if (searchResults.size()>0) {
			return String.format(EXECUTION_SEARCH_SUCCESSFUL, keyword);
		} else {
			return String.format(EXECUTION_SEARCH_UNSUCCESSFUL, keyword);
		}
	}

	private String setColour(ArrayList<String> getParsedInput) {
		String categoryName = getParsedInput.get(1);
		String colourId = getParsedInput.get(2);
		storageComponent.setCol(categoryName,colourId);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_SET_COLOUR_SUCCESSFUL, categoryName,colourId);
	}

	private void updateHistoryStack() {
		historyComponent.push(currentState);
	}

	private void updateCurrentState() {
		currentState = storageComponent.getCurrentState();
	}

	private String showCategory(ArrayList<String> getParsedInput) {
		categoryList = storageComponent.getCategoryList();
		return EXECUTION_SHOW_CATEGORY_SUCCESSFUL;
	}

	private String setCategory(ArrayList<String> getParsedInput) {
		String taskId = getParsedInput.get(1);
		String categoryName = getParsedInput.get(2);
		storageComponent.setCategory(taskId,categoryName);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_SET_CATEGORY_SUCCESSFUL, taskId, categoryName);
	}

	private String addCategory(ArrayList<String> getParsedInput) {
		String categoryName = getParsedInput.get(1);
		storageComponent.addCategory(categoryName);
		return String.format(EXECUTION_ADD_CATEGORY_SUCCESSFUL, categoryName);
	}

	private String addSubTask(ArrayList<String> getParsedInput) {
		String taskId = getParsedInput.get(1);
		String subTaskDescription = getParsedInput.get(2);
		storageComponent.addSubTask(taskId,subTaskDescription);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_ADD_SUBTASK_SUCCESSFUL, subTaskDescription,taskId);
	}

	private String subTask(ArrayList<String> getParsedInput) {
		String taskId = getParsedInput.get(1);
		storageComponent.subTask(taskId);
		return String.format(EXECUTION_SUBTASK_SUCCESSFUL, taskId);
	}

	private String sort(String field,ArrayList<Task> currentState) {
		System.out.println(currentState);
		currentState = sortComponent.sort(field,currentState);
		storageComponent.updateState(currentState);
		updateHistoryStack();
		return String.format(EXECUTION_SORT_COMMAND_SUCCESSFUL, field);
	}

	private String undo() {
		currentState = historyComponent.pop();
		return EXECUTION_UNDO_COMMAND_SUCCESSFUL;
	}

	private String setUndone(ArrayList<String> getParsedInput) {
		String taskId = getParsedInput.get(1);
		storageComponent.setUndone(taskId);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_DONE_COMMAND_SUCCESSFUL, taskId);
	}

	private String setDone(ArrayList<String> getParsedInput) {
		String taskId = getParsedInput.get(1);
		storageComponent.setDone(taskId);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_UNDONE_COMMAND_SUCCESSFUL, taskId);
	}

	private String returnToHomeScreen(ArrayList<Task> currentState2) {
		return EXECUTION_RETURN_COMMAND_SUCCESSFUL;
	}

	private String setReminder(ArrayList<String> getParsedInput) {
		String taskId = getParsedInput.get(1);
		String reminder = getParsedInput.get(2);
		storageComponent.setReminder(taskId,reminder);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_SET_REMINDER_SUCCESSFUL,taskId,reminder);
	}

	private String setDescription(ArrayList<String> getParsedInput) {
		String taskId = getParsedInput.get(1);
		String description = getParsedInput.get(2);
		storageComponent.setDescription(taskId,description);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_SET_DESCRIPTION_SUCCESSFUL, taskId);
	}

	private String setEventStartAndEndTime(ArrayList<String> getParsedInput) {
		String eventId = getParsedInput.get(1);
		String startTime = getParsedInput.get(2);
		String endTime = getParsedInput.get(3);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_SET_EVENT_START_AND_END_TIME_SUCCESSFUL, eventId,startTime,endTime);
	}

	private String setDeadline(ArrayList<String> getParsedInput) {
		String taskId = getParsedInput.get(1);
		String deadline = getParsedInput.get(2);
		storageComponent.setDeadline(taskId,deadline);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_SET_DEADLINE_SUCCESSFUL, taskId,deadline);
	}

	private String addFloatingTask(ArrayList<String> getParsedInput) {
		String taskName = getParsedInput.get(1);
//		System.out.println("taskName: "+ taskName);
		String taskDescription = getParsedInput.get(2);
//		System.out.println("task Description: "+taskDescription);
		String priority = getParsedInput.get(3);
//		System.out.println("priority: "+priority);
		String reminder = getParsedInput.get(4);
//		System.out.println("reminder: "+reminder);
		String category = getParsedInput.get(5);
//		System.out.println("category: "+category);
		storageComponent.addFloatingTask(taskName,taskDescription,priority,reminder,category);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_ADD_TASK_SUCCESSFUL, getParsedInput.get(1));
	}

	private String addEvent(ArrayList<String> getParsedInput) {
		String eventName = getParsedInput.get(1);
		String eventDescription = getParsedInput.get(2);
		String startDate = getParsedInput.get(3);
		String endDate = getParsedInput.get(4);
		String startTime = getParsedInput.get(5);
		String endTime = getParsedInput.get(6);
		String priority = getParsedInput.get(7);
		String reminder = getParsedInput.get(8);
		String category = getParsedInput.get(9);
		storageComponent.addEvent(eventName,eventDescription,startDate,endDate,startTime,endTime,priority,reminder,category);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_ADD_EVENT_SUCCESSFUL, getParsedInput.get(1));
	}

	private String addTask(ArrayList<String> getParsedInput) {
		String taskName = getParsedInput.get(1);
		String taskDescription = getParsedInput.get(2);
		String deadline = getParsedInput.get(3);
		String priority = getParsedInput.get(4);
		String reminder = getParsedInput.get(5);
		String category = getParsedInput.get(6);
		storageComponent.addTask(taskName,taskDescription,deadline,priority,reminder,category);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_ADD_TASK_SUCCESSFUL, getParsedInput.get(1));
	}

	public ArrayList<String> getCategories() {
		categoryList = storageComponent.getCategoryList();
		return categoryList;
	}

	public ArrayList<Task> getSearchResultsList() {
		return searchResults;
	}

	public ArrayList<Task> getCurrentState() {
		return currentState;
	}

	public ArrayList<Task> getTasks() {
		return storageComponent.getTasks();
	}

	public ArrayList<Task> getFloatingTasks() {
		return storageComponent.getFloatingTasks();
	}

	public ArrayList<Task> getEvents() {
		return storageComponent.getEvents();
	}
	
}
