package main.java.backend.Logic;

import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Task;

import java.util.ArrayList;
import java.util.TreeMap;

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

	private TreeMap<Integer, Task> taskList;

	private static LogicEditor logicEditorObject;
	private Storage storage;

	private LogicEditor(Storage storageComponent) {
		storage = storageComponent;
	}

	public static LogicEditor getInstance(Storage storageComponent) {

		if (logicEditorObject == null) {
			logicEditorObject = new LogicEditor(storageComponent);
		}
		return logicEditorObject;
	}

	public String execute(Command commandObject){

		String feedbackString = "";
		//		System.out.println("Get Command Field: "+commandObject.getCommandField());
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

		System.out.println("taskId: "+commandObject.getTaskName());
		int taskId = Integer.parseInt(commandObject.getTaskName());
		System.out.println("taskId: "+taskId);
		if (!commandObject.getDescription().equals("")) {
			System.out.println("Description: "+ commandObject.getDescription());
			setDescription(commandObject);
		}
		if (!commandObject.getPriority().equals("")) {
			System.out.println("priority :"+commandObject.getPriority());
			setPriority(commandObject);
		}
		if (!commandObject.getReminder().equals("")) {
			System.out.println("reminder: "+commandObject.getReminder());
			setReminder(commandObject);
		}
		if(!commandObject.getCategory().equals("")) {
			System.out.println("category: "+commandObject.getCategory());
			setCategory(commandObject);
		}
		if (!commandObject.getDeadline().equals("")) {
			setDeadline(commandObject);
			System.out.println("deadline :"+commandObject.getDeadline());
		}
		return EXECUTION_SET_SUCCESSFUL;
	}

	private String setMultipleFieldsForEvents(Command commandObject) {

		int taskId = Integer.parseInt(commandObject.getTaskName());
		System.out.println("taskId: "+taskId);
		if (!commandObject.getDescription().equals("")) {
			System.out.println("Description: "+ commandObject.getDescription());
			setDescription(commandObject);
		}
		if (!commandObject.getPriority().equals("")) {
			System.out.println("priority :"+commandObject.getPriority());
			setPriority(commandObject);
		}
		System.out.println("priority setted");
		if (!commandObject.getReminder().equals("")) {
			System.out.println("reminder: "+commandObject.getReminder());
			setReminder(commandObject);
		}
		if(!commandObject.getCategory().equals("")) {
			System.out.println("category: "+commandObject.getCategory());
			setCategory(commandObject);
		}
		if (!commandObject.getStartDateAndTime().equals("") && 
				!commandObject.getEndDateAndTime().equals("")) {
			setEventStartAndEndTime(commandObject);

		}
		return EXECUTION_SET_SUCCESSFUL;
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
		taskList.get(taskId).setCategory(categoryName);
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
		String reminderDate = commandObject.getReminder();
		int taskId = getTaskId(taskIndex);
		
		taskList = storage.load();
		taskList.get(taskId).setDone(false);
		taskList.get(taskId).setReminderDate(reminderDate);
		storage.save(taskList);

		return String.format(EXECUTION_SET_REMINDER_SUCCESSFUL,taskId,reminderDate);
	}

	private String setDescription(Command commandObject) {

		int taskIndex = Integer.parseInt(commandObject.getTaskName());
		String description = commandObject.getDescription();
		int taskId = getTaskId(taskIndex);
		
		taskList = storage.load();
		taskList.get(taskId).setDone(false);
		taskList.get(taskId).setDescription(description);
		storage.save(taskList);

		return String.format(EXECUTION_SET_DESCRIPTION_SUCCESSFUL, taskId);
	}

	private String setEventStartAndEndTime(Command commandObject) {

		int eventIndex = Integer.parseInt(commandObject.getTaskName());
		String startDate = commandObject.getStartDateAndTime();
		String endDate = commandObject.getEndDateAndTime();
		int taskId = getTaskId(eventIndex);
		Task task = taskList.get(taskId);

		Command command = new Command();
		command.setTaskName(Integer.toString(eventIndex));
		delete(command);

		taskList = storage.load();
		task.setStartDate(startDate);
		task.setEndDate(endDate);
		taskList.put(taskId, task);
		storage.save(taskList);

		return String.format(EXECUTION_SET_EVENT_START_AND_END_TIME_SUCCESSFUL, eventIndex,startDate,endDate);
	}

	private String setDeadline(Command commandObject) {

		int taskIndex = Integer.parseInt(commandObject.getTaskName());
		String deadline = commandObject.getDeadline();
		int taskId = getTaskId(taskIndex);
		Task task = taskList.get(taskId);

		Command command = new Command();
		command.setTaskName(Integer.toString(taskIndex));
		delete(command);

		taskList = storage.load();
		task.setEndDate(deadline);
		taskList.put(taskId, task);
		storage.save(taskList);
		
		return String.format(EXECUTION_SET_DEADLINE_SUCCESSFUL, taskId, deadline);
	}

	public void setindex(ArrayList<Task> list, int i, int taskIndex) {

		int taskId = list.get(i).getTaskId();
		taskList = storage.load();
		taskList.get(taskId).setIndex(taskIndex);
		storage.save(taskList);
	}
}