package main.java.backend.Logic;

import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Task;

import java.util.TreeMap;

public class LogicCreator {
	
	private static final String EXECUTION_ADD_SUBTASK_SUCCESSFUL = "SubTask %1$s is added to Task %2$s";
	private static final String EXECUTION_ADD_EVENT_SUCCESSFUL = "Event %1$s has been added";
	private static final String EXECUTION_ADD_TASK_SUCCESSFUL = "Task %1$s has been added";
	private static final String EXECUTION_COMMAND_UNSUCCESSFUL = "Invalid Command. Please try again.";
	
	private TreeMap<Integer, Task> taskList;

	private static LogicCreator logicCreator;
	private Storage storage;
	
	private LogicCreator(Storage storage) {
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
		}
		return feedbackString;
	}
	
	public TreeMap<Integer, Task> generateTaskId() {
		
		TreeMap<Integer, Task> taskList = storage.load();
		TreeMap<Integer, Task> newTaskList = new TreeMap<Integer, Task> ();
		int newTaskId = 0;
		
		for(Integer taskId : taskList.keySet()) {
			Task task = taskList.get(taskId);
			System.out.print(" New taskId for " + task.getName() + ": " + newTaskId);
			task.setTaskId(newTaskId);
			newTaskList.put(newTaskId, task);
			newTaskId++;
		}
		
		return newTaskList;
	}
	
	private String priorityChecker(int priority) {
		
		if (priority > 5 || priority < 1) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
		
		return null;
	}

	private String addFloating(Command commandObject) {
		
		String taskName = commandObject.getTaskName();
		String taskDescription = commandObject.getDescription();
		int priority = -1;
		
		if (!commandObject.getPriority().equals("")) {
			priority = Integer.parseInt(commandObject.getPriority());
			if (priorityChecker(priority) != null) {
				return priorityChecker(priority);
			}
		}
		String reminder = "";
		
		if (!commandObject.getReminder().equals("")) {
			reminder = commandObject.getReminder();
		}
		String categoryName = commandObject.getCategory();
		
		if(categoryName.equals("")) {
			categoryName = "default";
		}
		Task newFloat = new Task(categoryName, taskName, taskDescription, priority, reminder, false);
		//System.out.println(newFloat.getName());
		
		taskList = generateTaskId();
		newFloat.setTaskId(taskList.size() + 1);
		taskList.put(taskList.size() + 1, newFloat);
		storage.save(taskList);
		
		return String.format(EXECUTION_ADD_TASK_SUCCESSFUL, taskName);
	}

	private String addEvent(Command commandObject) {
		
		String eventName = commandObject.getTaskName();
		String eventDescription = commandObject.getDescription();
		String start = commandObject.getStartDateAndTime();
		String end = "";
		int priority = -1;
		String reminder = "";
		
		if (!commandObject.getEndDateAndTime().equals("")) {
			end = commandObject.getEndDateAndTime();
		}
		
		if (!commandObject.getPriority().equals("")) {
			priority = Integer.parseInt(commandObject.getPriority());
			if (priorityChecker(priority) != null) {
				return priorityChecker(priority);
			}
		}
		
		if (!commandObject.getReminder().equals("")) {
			reminder = commandObject.getReminder();
		}
		
		String categoryName = commandObject.getCategory();
		if(categoryName.equals("")) {
			categoryName = "default";
		}
		
		Task newEvent = new Task(categoryName, eventName, eventDescription, 
				start, end, priority, reminder);
		
		taskList = generateTaskId();
		newEvent.setTaskId(taskList.size() + 1);
		taskList.put(taskList.size() + 1, newEvent);
		storage.save(taskList);
		
//		System.out.println(newEvent.toString());
		return String.format(EXECUTION_ADD_EVENT_SUCCESSFUL, eventName);
	}

	private String addToDo(Command commandObject){
		
		String taskName = commandObject.getTaskName();
		String taskDescription = commandObject.getDescription();
		String deadline = commandObject.getDeadline();
		int priority = -1;
		String reminder = "";

		if (!commandObject.getPriority().equals("")) {
			priority = Integer.parseInt(commandObject.getPriority());
			if (priorityChecker(priority) != null) {
				return priorityChecker(priority);
			}
		}
		
		if (!commandObject.getReminder().equals("")) {
			reminder = commandObject.getReminder();
		}
		
		String categoryName = commandObject.getCategory();
		if(categoryName.equals("")) {
			categoryName = "default";
		}
		
		Task newToDo = new Task(categoryName, taskName, taskDescription, 
				deadline, priority, reminder, false);
//		System.out.println(newToDo.toString());
		
		taskList = generateTaskId();
		newToDo.setTaskId(taskList.size() + 1);
		taskList.put(taskList.size() + 1, newToDo);
		storage.save(taskList);
		
		return String.format(EXECUTION_ADD_TASK_SUCCESSFUL, taskName);
	}	

	private String addSubTask(Command commandObject) {
		
		String taskName = commandObject.getTaskName();
		String subTaskDescription = commandObject.getDescription();
//		storageObject.addSubTask(taskName,subTaskDescription);
		return String.format(EXECUTION_ADD_SUBTASK_SUCCESSFUL, subTaskDescription,taskName);
	}
	
}