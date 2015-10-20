package main.java.backend.Logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import main.java.backend.History.History;
import main.java.backend.Parser.Parser;
import main.java.backend.Search.Search;
import main.java.backend.Sorter.Sorter;
import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.Task;

public class Logic {
	private static final String EXECUTION_COMMAND_UNSUCCESSFUL = "Invalid Command. Please try again.";
	private static final String EXECUTION_SET_PRIORITY_SUCCESSFUL = "Task %1$s has been set to priority %2$s";
	private static final String EXECUTION_SET_SUCCESSFUL = "Fields have been updated";
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
	private static final String COMMAND_SET = "set";
	private static final String COMMAND_SET_REMINDER = "reminder";
	private static final String COMMAND_SET_DESCRIPTION = "description";
	private static final String COMMAND_SET_START_AND_END_TIME = "event";
	private static final String COMMAND_SET_DEADLINE = "deadline";
	private static final String COMMAND_SET_PRIORITY = "priority";
	private static final String COMMAND_ADD_FLOATING_TASK = "addF";
	private static final String COMMAND_ADD_EVENT = "addE";
	private static final String COMMAND_ADD_TASK = "addT";
	private static final String COMMAND_SHOW_TASK = "showT";
	private static final String COMMAND_SHOW_FLOATING_TASK = "showFT";
	private static final String COMMAND_SHOW_EVENT = "showE";
	private static final String COMMAND_EXIT = "exit";
	private static final SimpleDateFormat formatterForDateTime = 
			new SimpleDateFormat("EEE, dd MMM hh:mma");
	private static Calendar calendar = Calendar.getInstance();
	private static LogicHistory historyComponent;
	private static Parser parserComponent;
	private static Storage storageComponent;
	private static LogicSorter sortComponent;
	private static LogicSearch searchComponent;
	private static String getFeedbackAfterCommandExecution;
	private static ArrayList<Category> currentState;
	private static ArrayList<Category> searchResults;
	private static ArrayList<Task> taskList;
	private static ArrayList<String> categoryList;

	public Logic(String filename) throws FileNotFoundException, IOException {
		initLogic(filename);
		System.out.println("Logic component initialised successfully");
	}

	void initLogic(String filename) throws FileNotFoundException, IOException {
		historyComponent = new LogicHistory();
		parserComponent = new Parser();
		storageComponent = new Storage(filename);
		searchComponent = new LogicSearch();
	}

	public String executeCommand(String userInput) throws JsonParseException, JsonMappingException, IOException, JSONException, ParseException {
		ArrayList<String> getParsedInput = parserComponent.parseInput(userInput);
//		arrayChecker(getParsedInput);
		System.out.println("Array got");
		System.out.println(getParsedInput);
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
			case COMMAND_SET_PRIORITY:
				getFeedbackAfterCommandExecution = setPriority(getParsedInput);
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
			case COMMAND_SET:
				getFeedbackAfterCommandExecution = setMultipleFields(getParsedInput);
				break;
			case COMMAND_EXIT:
				exitProgram();
			default:
				getFeedbackAfterCommandExecution = errorCommand();
		}
		return getFeedbackAfterCommandExecution;
		
	}

	private void exitProgram() throws IOException {
		storageComponent.exitProgram();
		System.exit(0);
	}

	private String errorCommand() {
		return EXECUTION_COMMAND_UNSUCCESSFUL;
	}

	private String setPriority(ArrayList<String> getParsedInput) throws IOException, JSONException, ParseException {
		System.out.println("PRIORITY");
		String taskNamept1 = getParsedInput.get(1);
		String taskNamept2 = getParsedInput.get(2);
		int priority = Integer.parseInt(getParsedInput.get(3));
		if (priorityChecker(priority) != null) {
			return priorityChecker(priority);
		}
		storageComponent.setPriority(taskNamept1, Integer.parseInt(taskNamept2), priority);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_SET_PRIORITY_SUCCESSFUL, taskNamept1,priority);
	}

	private String setMultipleFields(ArrayList<String> getParsedInput) throws JsonParseException, JsonMappingException, IOException, JSONException, ParseException {
		String taskName = getParsedInput.get(1);
//		System.out.println("taskName: "+ taskName);
		if (!getParsedInput.get(2).equals("")) {
			String taskDescription = getParsedInput.get(2);
			storageComponent.setDescription(taskName, 0, taskDescription);
		}
		if (!getParsedInput.get(3).equals("")) {
			int priority = Integer.parseInt(getParsedInput.get(3));
			if (priorityChecker(priority) != null) {
				return priorityChecker(priority);
			}
			storageComponent.setPriority(taskName,priority, priority);
		}
		if (!getParsedInput.get(4).equals("")) {
			String reminder = getParsedInput.get(4);
			storageComponent.setReminder(taskName, 0, stringToMillisecond(reminder), reminder);
		}
		if(!getParsedInput.get(5).equals("")) {
			String category = getParsedInput.get(5);
			storageComponent.setCategory(taskName, 0, category);
		}
		updateCurrentState();
		updateHistoryStack();
		return EXECUTION_SET_SUCCESSFUL;
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

	// TODO
	private String delete(ArrayList<String> getParsedInput) throws IOException, JSONException, ParseException {
		String taskIdpt1 = getParsedInput.get(1);
		int taskId = Integer.parseInt(getParsedInput.get(2));
		storageComponent.deleteTask(taskIdpt1, taskId);
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

	private String setColour(ArrayList<String> getParsedInput) throws JsonParseException, JsonMappingException, IOException {
		String categoryName = getParsedInput.get(1);
		String colourId = getParsedInput.get(2);
		storageComponent.setCategoryColour(categoryName,colourId);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_SET_COLOUR_SUCCESSFUL, categoryName,colourId);
	}

	private void updateHistoryStack() {
		historyComponent.push(currentState);
	}

	private void updateCurrentState() throws IOException {
		currentState = storageComponent.getCategoryList();
		taskList = storageComponent.getTaskList();
	}

	private String showCategory(ArrayList<String> getParsedInput) {
		categoryList = storageComponent.getCategories();
		return EXECUTION_SHOW_CATEGORY_SUCCESSFUL;
	}

	private String setCategory(ArrayList<String> getParsedInput) throws IOException {
		String taskNamept1 = getParsedInput.get(1);
		int taskNamept2 = Integer.parseInt(getParsedInput.get(2));
		String categoryName = getParsedInput.get(3);
		storageComponent.setCategory(taskNamept1,taskNamept2, categoryName);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_SET_CATEGORY_SUCCESSFUL, taskNamept1, categoryName);
	}

	private String addCategory(ArrayList<String> getParsedInput) throws JsonParseException, JsonMappingException, IOException {
		String categoryName = getParsedInput.get(1);
		storageComponent.addCategory(categoryName);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_ADD_CATEGORY_SUCCESSFUL, categoryName);
	}

	private String addSubTask(ArrayList<String> getParsedInput) throws JsonParseException, JsonMappingException, IOException {
		arrayChecker(getParsedInput);
		String taskName = getParsedInput.get(1);
		String subTaskDescription = getParsedInput.get(2);
		storageComponent.addSubTask(taskName,subTaskDescription);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_ADD_SUBTASK_SUCCESSFUL, subTaskDescription,taskName);
	}

	private String sort(String field,ArrayList<Category> currentState2) {
		System.out.println(currentState2);
		taskList = sortComponent.sort(field,taskList);
//		storageComponent.updateState(currentState2);
		updateHistoryStack();
		return String.format(EXECUTION_SORT_COMMAND_SUCCESSFUL, field);
	}

	private String undo() {
		currentState = historyComponent.pop();
		return EXECUTION_UNDO_COMMAND_SUCCESSFUL;
	}

	private String setUndone(ArrayList<String> getParsedInput) 
			throws JsonParseException, JsonMappingException, JSONException, IOException, ParseException {
		String taskName = getParsedInput.get(1);
		int taskNamept2 = Integer.parseInt(getParsedInput.get(2));
		storageComponent.setUndone(taskName, taskNamept2);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_DONE_COMMAND_SUCCESSFUL, taskName);
	}

	private String setDone(ArrayList<String> getParsedInput) 
			throws JsonParseException, JsonMappingException, IOException, JSONException, ParseException {
		String taskName = getParsedInput.get(1);
		int taskNamept2 = Integer.parseInt(getParsedInput.get(2));
		storageComponent.setDone(taskName, taskNamept2);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_UNDONE_COMMAND_SUCCESSFUL, taskName);
	}

	@SuppressWarnings("unused")
	private String returnToHomeScreen(ArrayList<Task> currentState2) {
		return EXECUTION_RETURN_COMMAND_SUCCESSFUL;
	}

	private String setReminder(ArrayList<String> getParsedInput) 
			throws JsonParseException, JsonMappingException, IOException, JSONException, ParseException {
		String taskName = getParsedInput.get(1);
		int taskNamept2 = Integer.parseInt(getParsedInput.get(2));
		String reminderDate = getParsedInput.get(3);
//		int reminderDatept2 = Integer.parseInt(getParsedInput.get(4));
		long reminder = stringToMillisecond(reminderDate);
		storageComponent.setReminder(taskName,taskNamept2, reminder, reminderDate);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_SET_REMINDER_SUCCESSFUL,taskName,reminder);
	}

	private String setDescription(ArrayList<String> getParsedInput) 
			throws JsonParseException, JsonMappingException, IOException, JSONException, ParseException {
		String taskName = getParsedInput.get(1);
		String description = getParsedInput.get(3);
		int descriptionpt2 = Integer.parseInt(getParsedInput.get(2));
		storageComponent.setDescription(taskName,descriptionpt2, description);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_SET_DESCRIPTION_SUCCESSFUL, taskName);
	}

	private String setEventStartAndEndTime(ArrayList<String> getParsedInput) throws IOException {
		String eventId = getParsedInput.get(1);
		String startTime = getParsedInput.get(2);
		String endTime = getParsedInput.get(3);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_SET_EVENT_START_AND_END_TIME_SUCCESSFUL, eventId,startTime,endTime);
	}

	private String setDeadline(ArrayList<String> getParsedInput) 
			throws JsonParseException, JsonMappingException, IOException, JSONException, ParseException {
		String taskName = getParsedInput.get(1);
		int taskNamept2 = Integer.parseInt(getParsedInput.get(2));
		String deadlineDate = getParsedInput.get(3);
		long deadlineTime = stringToMillisecond(deadlineDate);
		storageComponent.setDeadline(taskName,taskNamept2, deadlineTime, deadlineDate);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_SET_DEADLINE_SUCCESSFUL, taskName,deadlineDate);
	}
	
	private long stringToMillisecond(String dateTime) {
        try {
            Date tempDateTime = formatterForDateTime.parse(dateTime);
            long dateTimeMillisecond = tempDateTime.getTime();
            return (dateTimeMillisecond);
        } catch (java.text.ParseException e) {
			e.printStackTrace();
		}

        //Should not reach here
        return -1;
    }
	
    @SuppressWarnings("unused")
	private static String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        calendar.clear();
        calendar.setTimeInMillis(milliSeconds);

        return formatter.format(calendar.getTime());
    }

    @SuppressWarnings("unused")
	private static String getDate(long milliSeconds) {
        calendar.clear();
        calendar.setTimeInMillis(milliSeconds);

        return formatterForDateTime.format(calendar.getTime());
    }

	private String addFloatingTask(ArrayList<String> getParsedInput) throws JsonParseException, JsonMappingException, IOException, JSONException {
		String taskName = getParsedInput.get(1);
//		System.out.println("taskName: "+ taskName);
		String taskDescription = getParsedInput.get(2);
//		System.out.println("task Description: "+taskDescription);
		int priority = -1;
		if (!getParsedInput.get(3).equals("")) {
			priority = Integer.parseInt(getParsedInput.get(3));
			if (priorityChecker(priority) != null) {
				return priorityChecker(priority);
			}
		}
		String reminderDate = "";
		long reminder = -1L;
		if (!getParsedInput.get(4).equals("")) {
			reminderDate = getParsedInput.get(4);
			reminder = stringToMillisecond(getParsedInput.get(4));
		}
//		System.out.println("priority: "+priority);
//		System.out.println("reminder: "+reminder);
		String category = getParsedInput.get(5);
//		System.out.println("category: "+category);
		storageComponent.addFloatingTask(taskName,taskDescription,priority,reminderDate, reminder,category);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_ADD_TASK_SUCCESSFUL, getParsedInput.get(1));
	}

	private String addEvent(ArrayList<String> getParsedInput) throws IOException, JSONException {
		String eventName = getParsedInput.get(1);
		String eventDescription = getParsedInput.get(2);
		String startDate = getParsedInput.get(3);
		String endDate = "";
		long startTime = -1L;
		long endTime = -1L;
		int priority = -1;
		String reminderDate = "";
		long reminder = -1L;
		
		if (!getParsedInput.get(4).equals("")) {
			endDate = getParsedInput.get(4);
		}
		if (!startDate.equals("")){
			startTime = stringToMillisecond(startDate);
		}
		if (!endDate.equals("")) {
			endTime = stringToMillisecond(endDate);
		}
		if (!getParsedInput.get(5).equals("")) {
			priority = Integer.parseInt(getParsedInput.get(5));
			if (priorityChecker(priority) != null) {
				return priorityChecker(priority);
			}
		}
		if (!getParsedInput.get(6).equals("")) {
			reminderDate = getParsedInput.get(4);
			reminder = stringToMillisecond(getParsedInput.get(4));
		}
		String category = getParsedInput.get(7);
		storageComponent.addEvent(eventName,eventDescription,startDate,endDate,startTime,endTime,priority,reminderDate, reminder,category);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_ADD_EVENT_SUCCESSFUL, getParsedInput.get(1));
	}

	private String addTask(ArrayList<String> getParsedInput) throws IOException, JSONException {
		String taskName = getParsedInput.get(1);
		String taskDescription = getParsedInput.get(2);
		String deadline = getParsedInput.get(3);
		int priority = -1;
		String reminderDate = "";
		long reminder = -1L;
		
		long endTime = stringToMillisecond(deadline);
		if (!getParsedInput.get(4).equals("")) {
			priority = Integer.parseInt(getParsedInput.get(4));
			if (priorityChecker(priority) != null) {
				return priorityChecker(priority);
			}
		}
		if (!getParsedInput.get(5).equals("")) {
			reminderDate = getParsedInput.get(4);
			reminder = stringToMillisecond(getParsedInput.get(4));
		}
		String category = getParsedInput.get(6);
		storageComponent.addTask(taskName,taskDescription,deadline,endTime,priority,reminderDate,reminder,category);
		updateCurrentState();
		updateHistoryStack();
		return String.format(EXECUTION_ADD_TASK_SUCCESSFUL, getParsedInput.get(1));
	}

	private String priorityChecker(int priority) {
		if (priority > 5 || priority < 1) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
		return null;
	}

	public ArrayList<String> getCategories() {
		categoryList = storageComponent.getCategories();
		return categoryList;
	}

	public ArrayList<Category> getSearchResultsList() {
		return searchResults;
	}

	public ArrayList<Category> getCurrentState() {
		return currentState;
	}

	public ArrayList<Task> getTasks() throws IOException, JSONException, ParseException {
		return storageComponent.getTasks();
	}

	public ArrayList<Task> getFloatingTasks() throws IOException, JSONException, ParseException {
		return storageComponent.getFloatingTasks();
	}

	public ArrayList<Task> getEvents() throws IOException, JSONException, ParseException {
		return storageComponent.getEvents();
	}
	
	public ArrayList<Task> getUpcomingTasks() throws IOException, JSONException, ParseException {
		return storageComponent.getUpcomingTasks();
	}
	
	public ArrayList<Task> getUpcomingEvents() throws IOException, JSONException, ParseException {
		return storageComponent.getUpcomingEvents();
	}

	public ArrayList<Task> getOverdueTasks() throws IOException, JSONException, ParseException {
		return storageComponent.getOverdueTasks();
	}

	public void setindex(ArrayList<Task> list, int i, String index) {
		// TODO Auto-generated method stub
		storageComponent.setIndex(list.get(i),index+(i+1));
	}
	
	
	
}