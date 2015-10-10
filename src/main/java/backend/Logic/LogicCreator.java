package main.java.backend.Logic;

import java.io.IOException;
import org.json.JSONException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Task;

public class LogicCreator {
	
	private Storage storageObject;
	private static LogicCreator logicCreator;
	private static final String EXECUTION_ADD_SUBTASK_SUCCESSFUL = "SubTask %1$s is added to Task %2$s";
	private static final String EXECUTION_ADD_CATEGORY_SUCCESSFUL = "Category %1$s has been added";
	private static final String EXECUTION_ADD_EVENT_SUCCESSFUL = "Event %1$s has been added";
	private static final String EXECUTION_ADD_TASK_SUCCESSFUL = "Task %1$s has been added";
	private static final String EXECUTION_COMMAND_UNSUCCESSFUL = "Invalid Command. Please try again.";
	

	private LogicCreator(Storage storageComponent) {
		storageObject = storageComponent;
	}

	public static LogicCreator getInstance(Storage storageComponent) {
		if (logicCreator == null) {
			logicCreator = new LogicCreator(storageComponent);
		}
		return logicCreator;
	}

	public String execute(Command commandObject) throws JsonParseException, JsonMappingException, IOException, JSONException {
		String feedbackString = "";
//		System.out.println(commandObject.getCommandField());
		switch (commandObject.getCommandField()) {
			case ("addF") :
				feedbackString = addFloating(commandObject);
				break;
			case ("addT") :
				feedbackString = addToDo(commandObject);
				break;
			case ("addE") :
				feedbackString = addEvent(commandObject);
				break;
			case ("adds"):
				feedbackString = addSubTask(commandObject);
				break;
			case("addcat") :
				feedbackString = addCategory(commandObject);
				break;		
		}
		return feedbackString;
	}
	

	private String addFloating(Command commandObject) throws JsonParseException, JsonMappingException, IOException, JSONException {
		String taskName = commandObject.getTaskName();
//		System.out.println("taskName: "+taskName);
		String taskDescription = commandObject.getDescription();
		int priority = -1;
		if (!commandObject.getPriority().equals("")) {
			priority = Integer.parseInt(commandObject.getPriority());
			if (priorityChecker(priority) != null) {
				return priorityChecker(priority);
			}
		}
		String reminderDate = "";
		long reminderTime = -1L;
		if (!commandObject.getReminder().equals("")) {
			reminderDate = commandObject.getReminder();
			reminderTime = GeneralFunctions.stringToMillisecond(commandObject.getReminder());
		}
		String category = commandObject.getCategory();
		Task newFloat = new Task(category, taskName,taskDescription, priority, reminderDate, reminderTime, false);
//		System.out.println(newFloat.toString());
		storageObject.addFloatingTask(newFloat);
		return String.format(EXECUTION_ADD_TASK_SUCCESSFUL, taskName);
	}

	private String addEvent(Command commandObject) throws IOException, JSONException {
		String eventName = commandObject.getTaskName();
		String eventDescription = commandObject.getDescription();
		String startDate = commandObject.getStartDateAndTime();
		String endDate = "";
		long startTime = -1L;
		long endTime = -1L;
		int priority = -1;
		String reminderDate = "";
		long reminder = -1L;
		
		if (!commandObject.getEndDateAndTime().equals("")) {
			endDate = commandObject.getEndDateAndTime();
		}
		if (!startDate.equals("")){
			startTime = GeneralFunctions.stringToMillisecond(startDate);
		}
		if (!endDate.equals("")) {
			endTime = GeneralFunctions.stringToMillisecond(endDate);
		}
		if (!commandObject.getPriority().equals("")) {
			priority = Integer.parseInt(commandObject.getPriority());
			if (priorityChecker(priority) != null) {
				return priorityChecker(priority);
			}
		}
		if (!commandObject.getReminder().equals("")) {
			reminderDate = commandObject.getReminder();
			reminder = GeneralFunctions.stringToMillisecond(commandObject.getReminder());
		}
		String category = commandObject.getCategory();
		Task newEvent = new Task(category, eventName, eventDescription, 
				startDate, endDate, startTime, endTime, 
				priority, reminderDate, reminder);
		storageObject.addEvent(newEvent);
//		System.out.println(newEvent.toString());
		return String.format(EXECUTION_ADD_EVENT_SUCCESSFUL, eventName);
	}

	private String addToDo(Command commandObject) throws IOException, JSONException {
		String taskName = commandObject.getTaskName();
		String taskDescription = commandObject.getDescription();
		String deadlineString = commandObject.getDeadline();
		int priority = -1;
		String reminderDate = "";
		long reminderTime = -1L;
		long deadlineTime = GeneralFunctions.stringToMillisecond(deadlineString);
		
		if (!commandObject.getPriority().equals("")) {
			priority = Integer.parseInt(commandObject.getPriority());
			if (priorityChecker(priority) != null) {
				return priorityChecker(priority);
			}
		}
		if (!commandObject.getReminder().equals("")) {
			reminderDate = commandObject.getReminder();
			reminderTime = GeneralFunctions.stringToMillisecond(commandObject.getReminder());
		}
		String category = commandObject.getCategory();
		Task newToDo = new Task(category, taskName, taskDescription, deadlineString, 
				deadlineTime, priority, reminderDate, reminderTime, false);
//		System.out.println(newToDo.toString());
		storageObject.addTask(newToDo);
		return String.format(EXECUTION_ADD_TASK_SUCCESSFUL, taskName);
	}	
	

	private String priorityChecker(int priority) {
		if (priority > 5 || priority < 1) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
		return null;
	}
	
	private String addCategory(Command commandObject) throws JsonParseException, JsonMappingException, IOException {
		String categoryName = commandObject.getTaskName();
		storageObject.addCategory(categoryName);
		return String.format(EXECUTION_ADD_CATEGORY_SUCCESSFUL, categoryName);
	}

	private String addSubTask(Command commandObject) throws JsonParseException, JsonMappingException, IOException {
		String taskName = commandObject.getTaskName();
		String subTaskDescription = commandObject.getDescription();
		storageObject.addSubTask(taskName,subTaskDescription);
		return String.format(EXECUTION_ADD_SUBTASK_SUCCESSFUL, subTaskDescription,taskName);
	}
}
