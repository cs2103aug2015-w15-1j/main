package main.java.backend.Logic;

import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.Task;

import java.util.TreeMap;

import main.java.backend.GeneralFunctions.GeneralFunctions;

public class LogicCreator {
	
	private static final String EXECUTION_ADD_SUBTASK_SUCCESSFUL = "SubTask %1$s is added to Task %2$s";
	private static final String EXECUTION_ADD_CATEGORY_SUCCESSFUL = "Category %1$s has been added";
	private static final String EXECUTION_ADD_EVENT_SUCCESSFUL = "Event %1$s has been added";
	private static final String EXECUTION_ADD_TASK_SUCCESSFUL = "Task %1$s has been added";
	private static final String EXECUTION_COMMAND_UNSUCCESSFUL = "Invalid Command. Please try again.";
	
	private TreeMap<String, Category> allData;

	private static LogicCreator logicCreator;
	private LogicHelper logicHelper;
	private Storage storage;
	
	private LogicCreator(Storage storage) {
		logicHelper = new LogicHelper();
		this.storage = storage;
	}

	public static LogicCreator getInstance(Storage storageComponent) {
		if (logicCreator == null) {
			logicCreator = new LogicCreator(storageComponent);
		}
		return logicCreator;
	}

	public String execute(Command commandObject) {
		String feedbackString = "";
//		System.out.println(commandObject.getCommandField());
		switch (commandObject.getCommandField()) {
			case("addcat") :
				feedbackString = addCategory(commandObject);
				break;		
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
		}
		return feedbackString;
	}
	
	private String addCategory(Command commandObject) {
		
		String categoryName = commandObject.getTaskName();
		TreeMap<String, Category> allData = storage.load();
		logicHelper.addCategory(allData, categoryName);
		storage.save(allData);
		
		return String.format(EXECUTION_ADD_CATEGORY_SUCCESSFUL, categoryName);
	}

	private String addFloating(Command commandObject) {
		String taskName = commandObject.getTaskName();
//		System.out.println("taskName: "+taskName);
		String taskDescription = commandObject.getDescription();
		int priority = -1;
		if (!commandObject.getPriority().equals("")) {
			priority = Integer.parseInt(commandObject.getPriority());
			if (logicHelper.priorityChecker(priority) != null) {
				return logicHelper.priorityChecker(priority);
			}
		}
		String reminderDate = "";
		long reminderTime = -1L;
		if (!commandObject.getReminder().equals("")) {
			reminderDate = commandObject.getReminder();
			reminderTime = GeneralFunctions.stringToMillisecond(commandObject.getReminder());
		}
		String categoryName = commandObject.getCategory();
		if(categoryName.equals("")) {
			categoryName = "default";
		}
		Task newFloat = new Task(categoryName, taskName,taskDescription, priority, reminderDate, reminderTime, false);
		System.out.println(newFloat.getName());
		
		allData = storage.load();
		allData = logicHelper.addCategory(allData, categoryName);
		Category category = allData.get(categoryName);
		TreeMap<String, Task> allFloatingTasks = category.getFloatTasks();
		allFloatingTasks.put(newFloat.getTaskId(), newFloat);
		category.setFloatTasks(allFloatingTasks);
		storage.save(allData);
		
		return String.format(EXECUTION_ADD_TASK_SUCCESSFUL, taskName);
	}

	private String addEvent(Command commandObject) {
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
			if (logicHelper.priorityChecker(priority) != null) {
				return logicHelper.priorityChecker(priority);
			}
		}
		if (!commandObject.getReminder().equals("")) {
			reminderDate = commandObject.getReminder();
			reminder = GeneralFunctions.stringToMillisecond(commandObject.getReminder());
		}
		String categoryName = commandObject.getCategory();
		if(categoryName.equals("")) {
			categoryName = "default";
		}
		
		Task newEvent = new Task(categoryName, eventName, eventDescription, 
				startDate, endDate, startTime, endTime, 
				priority, reminderDate, reminder);
		
		allData = storage.load();
		allData = logicHelper.addCategory(allData, categoryName);
		Category category = allData.get(categoryName);
		TreeMap<String, Task> allEvents = category.getEvents();
		allEvents.put(newEvent.getTaskId(), newEvent);
		category.setEvents(allEvents);
		storage.save(allData);
		
//		System.out.println(newEvent.toString());
		return String.format(EXECUTION_ADD_EVENT_SUCCESSFUL, eventName);
	}

	private String addToDo(Command commandObject){
		String taskName = commandObject.getTaskName();
		String taskDescription = commandObject.getDescription();
		String deadlineString = commandObject.getDeadline();
		int priority = -1;
		String reminderDate = "";
		long reminderTime = -1L;
		long deadlineTime = GeneralFunctions.stringToMillisecond(deadlineString);
		
		if (!commandObject.getPriority().equals("")) {
			priority = Integer.parseInt(commandObject.getPriority());
			if (logicHelper.priorityChecker(priority) != null) {
				return logicHelper.priorityChecker(priority);
			}
		}
		
		if (!commandObject.getReminder().equals("")) {
			reminderDate = commandObject.getReminder();
			reminderTime = GeneralFunctions.stringToMillisecond(commandObject.getReminder());
		}
		
		String categoryName = commandObject.getCategory();
		if(categoryName.equals("")) {
			categoryName = "default";
		}
		
		Task newToDo = new Task(categoryName, taskName, taskDescription, deadlineString, 
				deadlineTime, priority, reminderDate, reminderTime, false);
//		System.out.println(newToDo.toString());
		
		allData = storage.load();
		allData = logicHelper.addCategory(allData, categoryName);
		Category category = allData.get(categoryName);
		TreeMap<String, Task> allToDos = category.getTasks();
		allToDos.put(newToDo.getTaskId(), newToDo);
		category.setTasks(allToDos);
		storage.save(allData);
		
		return String.format(EXECUTION_ADD_TASK_SUCCESSFUL, taskName);
	}	

	private String addSubTask(Command commandObject) {
		String taskName = commandObject.getTaskName();
		String subTaskDescription = commandObject.getDescription();
//		storageObject.addSubTask(taskName,subTaskDescription);
		return String.format(EXECUTION_ADD_SUBTASK_SUCCESSFUL, subTaskDescription,taskName);
	}
	
}