package main.java.backend.Logic;

import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Task;
import main.java.backend.Storage.Task.TaskType;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogicEditor {

	private static final String EXECUTION_SET_PRIORITY_SUCCESSFUL = "Task %1$s has been set to priority %2$s";
	private static final String EXECUTION_SET_SUCCESSFUL = "Fields have been updated";
	private static final String EXECUTION_DELETE_SUCCESSFUL = "Task %1$s has been deleted";
	private static final String EXECUTION_SET_DEADLINE_SUCCESSFUL = "Task %1$s deadline has been set to %2$s";
	private static final String EXECUTION_SET_EVENT_START_AND_END_TIME_SUCCESSFUL = "Event %1$s has been setted to %2$s till %3$s";
	private static final String EXECUTION_SET_DESCRIPTION_SUCCESSFUL = "Description for task %1$s has been set";
	private static final String EXECUTION_SET_REMINDER_SUCCESSFUL = "Reminder for Task %1$s has been set to be at %2$s";
	private static final String EXECUTION_SET_CATEGORY_SUCCESSFUL = "Task %1$s is set to the category %2$s";
	private static final String EXECUTION_SET_COLOUR_SUCCESSFUL = "Category %1$s is set to the colour %2$s";
	private static final String EXECUTION_COMMAND_UNSUCCESSFUL = "Invalid Command. Please try again.";
	private static final String EXECUTION_UNDONE_COMMAND_SUCCESSFUL = "Task %1$s is completed";
	private static final String EXECUTION_DONE_COMMAND_SUCCESSFUL = "Task %1$s is not completed";
	private static Logger logicEditorLogger = Logger.getGlobal();	
	private FileHandler logHandler;
	
	private TreeMap<Integer, Task> taskList;

	private static LogicEditor logicEditorObject;
	private Storage storage;

	private LogicEditor(Storage storageComponent) {
		storage = storageComponent;
		initLogger();
	}

	public static LogicEditor getInstance(Storage storageComponent) {

		if (logicEditorObject == null) {
			logicEditorObject = new LogicEditor(storageComponent);
		}
		return logicEditorObject;
	}
	
	private void initLogger() {
			
			try {
				logHandler = new FileHandler("LogicEditorLog.txt",true);
				logHandler.setFormatter(new SimpleFormatter());
				logicEditorLogger.addHandler(logHandler);
				logicEditorLogger.setUseParentHandlers(false);
				
			} catch (SecurityException | IOException e) {
				logicEditorLogger.warning("Logger failed to initialise: " + e.getMessage());
			}
		}

	public String execute(Command commandObject){

		String feedbackString = "";
		logicEditorLogger.info("Get Command Field: "+commandObject.getCommandField());
		switch(commandObject.getCommandField()) {
			case ("priority") :
				feedbackString = setPriority(commandObject);
				break;
			case ("setT") :
				feedbackString = setMultipleFieldsForTask(commandObject);
				break;
			case ("setE") :
				feedbackString = setMultipleFieldsForEvents(commandObject);
				break;
			case ("delete") :
				feedbackString = delete(commandObject);
				break;
			case ("setCol") :
//				feedbackString = setColour(commandObject);
				break;
			case ("category") :
				feedbackString = setCategory(commandObject);
				break;
			case ("undone") :
				feedbackString = setUndone(commandObject);
				break;
			case ("done") :
				feedbackString = setDone(commandObject);
				break;
			case ("reminder") :
				feedbackString = setReminder(commandObject);
				break;
			case ("description") :
				feedbackString = setDescription(commandObject);
				break;
			case ("event") :
				feedbackString = setEventStartAndEndTime(commandObject);
				break;
			case ("deadline") :
				feedbackString = setDeadline(commandObject);
				break;
		}
		return feedbackString;
	}

	private String priorityChecker(int priority) {
		
		if (priority > 5 || priority < 1) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
		
		return null;
	}
	
	private String setMultipleFieldsForTask(Command commandObject) {

		logicEditorLogger.info("taskId: "+commandObject.getTaskName());
		int taskId = Integer.parseInt(commandObject.getTaskName());
		logicEditorLogger.info("taskId: "+taskId);
		if (!commandObject.getDescription().equals("")) {
			logicEditorLogger.info("Description: "+ commandObject.getDescription());
			setDescription(commandObject);
		}
		if (!commandObject.getPriority().equals("")) {
			logicEditorLogger.info("priority :"+commandObject.getPriority());
			setPriority(commandObject);
		}
		if (!commandObject.getReminder().equals("")) {
			logicEditorLogger.info("reminder: "+commandObject.getReminder());
			setReminder(commandObject);
		}
		if(!commandObject.getCategory().equals("")) {
			logicEditorLogger.info("category: "+commandObject.getCategory());
			setCategory(commandObject);
		}
		if (!commandObject.getEndDateAndTime().equals("")) {
			setDeadline(commandObject);
			logicEditorLogger.info("deadline :"+commandObject.getEndDateAndTime());
		}
		return EXECUTION_SET_SUCCESSFUL;
	}

	private String setMultipleFieldsForEvents(Command commandObject) {

		int taskId = Integer.parseInt(commandObject.getTaskName());
		logicEditorLogger.info("taskId: "+taskId);
		if (!commandObject.getDescription().equals("")) {
			logicEditorLogger.info("Description: "+ commandObject.getDescription());
			setDescription(commandObject);
		}
		if (!commandObject.getPriority().equals("")) {
			logicEditorLogger.info("priority :"+commandObject.getPriority());
			setPriority(commandObject);
		}
		System.out.println("priority setted");
		if (!commandObject.getReminder().equals("")) {
			logicEditorLogger.info("reminder: "+commandObject.getReminder());
			setReminder(commandObject);
		}
		if(!commandObject.getCategory().equals("")) {
			logicEditorLogger.info("category: "+commandObject.getCategory());
			setCategory(commandObject);
		}
		if (!commandObject.getStartDateAndTime().equals("") && 
				!commandObject.getEndDateAndTime().equals("")) {
			setEventStartAndEndTime(commandObject);

		}
		return EXECUTION_SET_SUCCESSFUL;
	}
	
	private TaskType getTaskType(Task task) {
		
		if(!task.getEnd().isEmpty()) {
			return TaskType.TODO;
		} else {
			return TaskType.FLOATING;
		}
	}
	
	private int getTaskId(int taskIndex) {
		
		for(Integer taskId : taskList.keySet()) {
			Task task = taskList.get(taskId);
			
			if(task.getIndex() == taskIndex) {
				return task.getTaskId();
			}
		}
		
		// Should not reach here
		return -1;
	}

	private String delete(Command commandObject) {

		taskList = storage.load();
		int taskIndex = Integer.parseInt(commandObject.getTaskName());
		int taskId = getTaskId(taskIndex);

		if(taskList.containsKey(taskId)) {
			taskList.remove(taskId);
		}

		storage.save(taskList);

		return String.format(EXECUTION_DELETE_SUCCESSFUL, taskIndex);
	}
	
	public void setIndex(ArrayList<Task> list, int i, int taskIndex) {

		int taskId = list.get(i).getTaskId();
		taskList = storage.load();
		taskList.get(taskId).setIndex(taskIndex);
		storage.save(taskList);
	}

	private String setPriority(Command commandObject){

		int taskIndex = Integer.parseInt(commandObject.getTaskName());
		int priority = Integer.parseInt(commandObject.getPriority());
		int taskId = getTaskId(taskIndex);

		if (priorityChecker(priority) != null) {
			return priorityChecker(priority);
		}
		
		taskList = storage.load();
		taskList.get(taskId).setPriority(priority);
		storage.save(taskList);	

		return String.format(EXECUTION_SET_PRIORITY_SUCCESSFUL, taskId, priority);
	}

	/*
	private String setColour(Command commandObject) {

		taskList = storage.load();
		String categoryName = commandObject.getCategory();
		String colourId = commandObject.getColour();

		Category category = taskList.get(categoryName);
		category.setCategoryColour(colourId);
		storage.save(taskList);

		return String.format(EXECUTION_SET_COLOUR_SUCCESSFUL, categoryName,colourId);
	}
	*/

	private String setCategory(Command commandObject) {

		int taskIndex = Integer.parseInt(commandObject.getTaskName());
		String categoryName = commandObject.getCategory();
		int taskId = getTaskId(taskIndex);

		taskList = storage.load();
		taskList.get(taskId).setCategoryName(categoryName);
		storage.save(taskList);

		return String.format(EXECUTION_SET_CATEGORY_SUCCESSFUL, taskId, categoryName);
	}

	private String setUndone(Command commandObject) {

		int taskIndex = Integer.parseInt(commandObject.getTaskName());
		int taskId = getTaskId(taskIndex);
		
		taskList = storage.load();
		taskList.get(taskId).setDone(false);
		storage.save(taskList);

		return String.format(EXECUTION_DONE_COMMAND_SUCCESSFUL, taskId);
	}

	private String setDone(Command commandObject) {

		int taskIndex = Integer.parseInt(commandObject.getTaskName());
		int taskId = getTaskId(taskIndex);
		
		taskList = storage.load();
		taskList.get(taskId).setDone(true);
		taskList.get(taskId).setIndex(-1);
		storage.save(taskList);

		return String.format(EXECUTION_UNDONE_COMMAND_SUCCESSFUL, taskId);
	}

	private String setReminder(Command commandObject) {

		int taskIndex = Integer.parseInt(commandObject.getTaskName());
		String reminder = commandObject.getReminder();
		int taskId = getTaskId(taskIndex);
		
		taskList = storage.load();
		taskList.get(taskId).setReminder(reminder);
		storage.save(taskList);

		return String.format(EXECUTION_SET_REMINDER_SUCCESSFUL,taskId,reminder);
	}

	private String setDescription(Command commandObject) {

		int taskIndex = Integer.parseInt(commandObject.getTaskName());
		String description = commandObject.getDescription();
		int taskId = getTaskId(taskIndex);
		
		taskList = storage.load();
		taskList.get(taskId).setDescription(description);
		storage.save(taskList);

		return String.format(EXECUTION_SET_DESCRIPTION_SUCCESSFUL, taskId);
	}

	private String setEventStartAndEndTime(Command commandObject) {

		int eventIndex = Integer.parseInt(commandObject.getTaskName());
		String start = commandObject.getStartDateAndTime();
		String end = commandObject.getEndDateAndTime();
		int taskId = getTaskId(eventIndex);
		Task task = taskList.get(taskId);

		Command command = new Command();
		command.setTaskName(Integer.toString(eventIndex));
		delete(command);

		taskList = storage.load();
		task.setTaskType(TaskType.EVENT);
		task.setStart(start);
		task.setEnd(end);
		taskList.put(taskId, task);
		storage.save(taskList);

		return String.format(EXECUTION_SET_EVENT_START_AND_END_TIME_SUCCESSFUL, eventIndex,start,end);
	}

	private String setDeadline(Command commandObject) {

		int eventIndex = Integer.parseInt(commandObject.getTaskName());
		String end = commandObject.getEndDateAndTime();
		int taskId = getTaskId(eventIndex);
		Task task = taskList.get(taskId);

		Command command = new Command();
		command.setTaskName(Integer.toString(eventIndex));
		delete(command);

		taskList = storage.load();
		task.setEnd(end);
		task.setTaskType(getTaskType(task));
		taskList.put(taskId, task);
		storage.save(taskList);
		
		return String.format(EXECUTION_SET_DEADLINE_SUCCESSFUL, taskId, end);
	}
	
}