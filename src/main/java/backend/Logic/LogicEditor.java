package main.java.backend.Logic;

import java.io.IOException;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import main.java.backend.Storage.Storage;

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

	public String execute(Command commandObject) throws IOException, JSONException, ParseException {
		String feedbackString = "";
		switch(commandObject.getCommandField()) {
			case ("priority") :
				feedbackString = setPriority(commandObject);
				break;
			case ("set") :
				feedbackString = setMultipleFields(commandObject);
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

	private String setPriority(Command commandObject) throws IOException, JSONException, ParseException {
		int taskId = Integer.parseInt(commandObject.getTaskName());
		int priority = Integer.parseInt(commandObject.getPriority());
		if (priorityChecker(priority) != null) {
			return priorityChecker(priority);
		}
		storageObject.setPriority(taskId, priority);
		return String.format(EXECUTION_SET_PRIORITY_SUCCESSFUL, taskId,priority);
	}

	private String setMultipleFields(Command commandObject) throws JsonParseException, JsonMappingException, IOException, JSONException, ParseException {
		int taskId = Integer.parseInt(commandObject.getTaskName());
		if (!commandObject.getDescription().equals("")) {
			String taskDescription = commandObject.getDescription();
			storageObject.setDescription(taskId,taskDescription);
		}
		if (!commandObject.getPriority().equals("")) {
			int priority = Integer.parseInt(commandObject.getPriority());
			if (priorityChecker(priority) != null) {
				return priorityChecker(priority);
			}
			storageObject.setPriority(taskId,priority);
		}
		if (!commandObject.getReminder().equals("")) {
			String reminder = commandObject.getReminder();
			long reminderTime = GeneralFunctions.stringToMillisecond(reminder);
			storageObject.setReminder(taskId,reminderTime, reminder);
		}
		if(!commandObject.getCategory().equals("")) {
			String category = commandObject.getCategory();
			storageObject.setCategory(taskId,category);
		}
		return EXECUTION_SET_SUCCESSFUL;
	}
	private String delete(Command commandObject) throws IOException, JSONException, ParseException {
		int taskId = Integer.parseInt(commandObject.getTaskName());
		storageObject.deleteTask(taskId);
		return String.format(EXECUTION_DELETE_SUCCESSFUL, taskId);
	}
	private String setColour(Command commandObject) throws JsonParseException, JsonMappingException, IOException {
		String categoryName = commandObject.getCategory();
		String colourId = commandObject.getColour();
		storageObject.setCategoryColour(categoryName,colourId);
		return String.format(EXECUTION_SET_COLOUR_SUCCESSFUL, categoryName,colourId);
	}
	private String setCategory(Command commandObject) throws IOException {
		int taskId = Integer.parseInt(commandObject.getTaskName());
		String categoryName = commandObject.getCategory();
		storageObject.setCategory(taskId, categoryName);
		return String.format(EXECUTION_SET_CATEGORY_SUCCESSFUL, taskId, categoryName);
	}
	private String setUndone(Command commandObject) 
			throws JsonParseException, JsonMappingException, JSONException, IOException, ParseException {
		int taskId = Integer.parseInt(commandObject.getTaskName());
		storageObject.setUndone(taskId);
		return String.format(EXECUTION_DONE_COMMAND_SUCCESSFUL, taskId);
	}

	private String setDone(Command commandObject) 
			throws JsonParseException, JsonMappingException, IOException, JSONException, ParseException {
		int taskId = Integer.parseInt(commandObject.getTaskName());
		storageObject.setDone(taskId);
		return String.format(EXECUTION_UNDONE_COMMAND_SUCCESSFUL, taskId);
	}
	private String setReminder(Command commandObject) 
			throws JsonParseException, JsonMappingException, IOException, JSONException, ParseException {
		int taskId = Integer.parseInt(commandObject.getTaskName());
		String reminderDate = commandObject.getReminder();
		long reminderTime = GeneralFunctions.stringToMillisecond(reminderDate);
		storageObject.setReminder(taskId, reminderTime, reminderDate);
		return String.format(EXECUTION_SET_REMINDER_SUCCESSFUL,taskId,reminderDate);
	}

	private String setDescription(Command commandObject) 
			throws JsonParseException, JsonMappingException, IOException, JSONException, ParseException {
		int taskId = Integer.parseInt(commandObject.getTaskName());
		String description = commandObject.getDescription();
		storageObject.setDescription(taskId, description);
		return String.format(EXECUTION_SET_DESCRIPTION_SUCCESSFUL, taskId);
	}

	private String setEventStartAndEndTime(Command commandObject) throws IOException {
		int eventId = Integer.parseInt(commandObject.getTaskName());
		String startTime = commandObject.getStartDateAndTime();
		String endTime = commandObject.getEndDateAndTime();
		return String.format(EXECUTION_SET_EVENT_START_AND_END_TIME_SUCCESSFUL, eventId,startTime,endTime);
	}

	private String setDeadline(Command commandObject) 
			throws JsonParseException, JsonMappingException, IOException, JSONException, ParseException {
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
