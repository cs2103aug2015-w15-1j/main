package main.java.backend.Logic;

import main.java.backend.Storage.Storage;
import main.java.backend.GeneralFunctions.GeneralFunctions;

public class LogicEditor {
	
	private static LogicEditor logicEditorObject;
	private static Storage storageObject;
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
	
	private LogicEditor(Storage storageComponent) {
		storageObject = storageComponent;
	}

	public static LogicEditor getInstance(Storage storageComponent) {
		if (logicEditorObject == null) {
			logicEditorObject = new LogicEditor(storageComponent);
		}
		return logicEditorObject;
	}

	public String execute(Command commandObject){
		String feedbackString = "";
		System.out.println("Get Command Field: "+commandObject.getCommandField());
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
				feedbackString = setColour(commandObject);
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
		System.out.println("piority setted");
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
	
	private String delete(Command commandObject) {
		int taskId = Integer.parseInt(commandObject.getTaskName());
		storageObject.deleteTask(taskId);
		return String.format(EXECUTION_DELETE_SUCCESSFUL, taskId);
	}
	
	private String setPriority(Command commandObject){
		int taskId = Integer.parseInt(commandObject.getTaskName());
		System.out.println("taskId: "+taskId);
		int priority = Integer.parseInt(commandObject.getPriority());
		if (priorityChecker(priority) != null) {
			return priorityChecker(priority);
		}
		System.out.println("priority: "+priority);
		storageObject.setPriority(taskId, priority);
		return String.format(EXECUTION_SET_PRIORITY_SUCCESSFUL, taskId,priority);
	}
	
	private String setColour(Command commandObject) {
		String categoryName = commandObject.getCategory();
		String colourId = commandObject.getColour();
		storageObject.setCategoryColour(categoryName,colourId);
		return String.format(EXECUTION_SET_COLOUR_SUCCESSFUL, categoryName,colourId);
	}
	
	private String setCategory(Command commandObject) {
		int taskId = Integer.parseInt(commandObject.getTaskName());
		String categoryName = commandObject.getCategory();
		storageObject.setCategory(taskId, categoryName);
		return String.format(EXECUTION_SET_CATEGORY_SUCCESSFUL, taskId, categoryName);
	}
	private String setUndone(Command commandObject) {
		int taskId = Integer.parseInt(commandObject.getTaskName());
		storageObject.setUndone(taskId);
		return String.format(EXECUTION_DONE_COMMAND_SUCCESSFUL, taskId);
	}

	private String setDone(Command commandObject) {
		int taskId = Integer.parseInt(commandObject.getTaskName());
		System.out.println("taskId : "+taskId);
		storageObject.setDone(taskId);
		return String.format(EXECUTION_UNDONE_COMMAND_SUCCESSFUL, taskId);
	}
	
	private String setReminder(Command commandObject) {
		int taskId = Integer.parseInt(commandObject.getTaskName());
		String reminderDate = commandObject.getReminder();
		long reminderTime = GeneralFunctions.stringToMillisecond(reminderDate);
		storageObject.setReminder(taskId, reminderTime, reminderDate);
		return String.format(EXECUTION_SET_REMINDER_SUCCESSFUL,taskId,reminderDate);
	}

	private String setDescription(Command commandObject) {
		int taskId = Integer.parseInt(commandObject.getTaskName());
		System.out.println("taskID: " +taskId);
		String description = commandObject.getDescription();
		System.out.println("description: "+description);
		storageObject.setDescription(taskId, description);
		return String.format(EXECUTION_SET_DESCRIPTION_SUCCESSFUL, taskId);
	}

	private String setEventStartAndEndTime(Command commandObject) {
		int eventId = Integer.parseInt(commandObject.getTaskName());
		String startDate = commandObject.getStartDateAndTime();
		long startTime = GeneralFunctions.stringToMillisecond(startDate);
		String endDate = commandObject.getEndDateAndTime();
		long endTime = GeneralFunctions.stringToMillisecond(endDate);
		System.out.println("setEventStartAndEndTime: "+ startDate);
		System.out.println("setEventStartAndEndTime: "+ endDate);
		storageObject.setStartDate(eventId, startTime, startDate);
		storageObject.setDeadline(eventId, endTime, endDate);
		return String.format(EXECUTION_SET_EVENT_START_AND_END_TIME_SUCCESSFUL, eventId,startDate,endDate);
	}

	private String setDeadline(Command commandObject) {
		int taskId = Integer.parseInt(commandObject.getTaskName());
		String deadlineDate = commandObject.getDeadline();
		long deadlineTime = GeneralFunctions.stringToMillisecond(deadlineDate);
		storageObject.setDeadline(taskId, deadlineTime, deadlineDate);
		return String.format(EXECUTION_SET_DEADLINE_SUCCESSFUL, taskId,deadlineDate);
	}
	
	private String priorityChecker(int priority) {
		if (priority > 5 || priority < 1) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
		return null;
	}
}
