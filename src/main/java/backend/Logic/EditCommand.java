package main.java.backend.Logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Task;
import main.java.backend.Storage.Task.Task.TaskType;

public class EditCommand extends Command {
	
	private static final String EXECUTION_SET_PRIORITY_SUCCESSFUL = "Task %1$s has been set to priority %2$s";
	private static final String EXECUTION_SET_SUCCESSFUL = "Fields have been updated";
	private static final String EXECUTION_DELETE_SUCCESSFUL = "Task %1$s has been deleted";
	private static final String EXECUTION_SET_DEADLINE_SUCCESSFUL = "Task %1$s deadline has been set to %2$s";
	private static final String EXECUTION_SET_RECURRING_SUCCESSFUL = "Task %1$s recurring has been set to %2$s";
	private static final String EXECUTION_SET_EVENT_START_AND_END_TIME_SUCCESSFUL = "Event %1$s has been setted to %2$s till %3$s";
	private static final String EXECUTION_SET_DESCRIPTION_SUCCESSFUL = "Description for task %1$s has been set";
	private static final String EXECUTION_SET_REMINDER_SUCCESSFUL = "Reminder for Task %1$s has been set to be at %2$s";
	private static final String EXECUTION_COMMAND_UNSUCCESSFUL = "Invalid Command. Please try again.";
	private static final String EXECUTION_UNDONE_COMMAND_SUCCESSFUL = "Task %1$s is completed";
	private static final String EXECUTION_DONE_COMMAND_SUCCESSFUL = "Task %1$s is not completed";
	
	private static final String RECURRING_DAY = "day";
	private static final String RECURRING_WEEK = "week";
	private static final String RECURRING_MONTH = "month";
	private static final String RECURRING_YEAR = "year";
	
	private static Logger logicEditorLogger = Logger.getGlobal();	
	private FileHandler logHandler;
	
	private ArrayList<Task> taskList;
	private ArrayList<Task> currentState;

	private Storage storageComponent;
	private History historySubComponent;
	
	//@@author A0121284N
	public EditCommand(Type typeInput, Storage storage, History history) {
		super(typeInput);
		storageComponent = storage;
		historySubComponent = history;
		initLogger();
	}
	
	//@@author A0121284N
	private void initLogger() {
			
			try {
				logHandler = new FileHandler("TankTaskLog.txt",true);
				logHandler.setFormatter(new SimpleFormatter());
				logicEditorLogger.addHandler(logHandler);
				logicEditorLogger.setUseParentHandlers(false);
				
			} catch (SecurityException | IOException e) {
				logicEditorLogger.warning("Logger failed to initialise: " + e.getMessage());
			}
		}
	
	//@@author A0121284N
	public String execute(){
		String feedbackString = "";
		logicEditorLogger.info("Get Command Field: "+this.getCommandField());
		switch(this.getCommandField()) {
			case ("priority") :
				feedbackString = setPriority(this);
				break;
			case ("setT") :
				feedbackString = setMultipleFieldsForTask(this);
				break;
			case ("setE") :
				feedbackString = setMultipleFieldsForEvents(this);
				break;
			case ("set") :
				feedbackString = setMultipleFieldsForFloats(this);
				break;
			case ("delete") :
				feedbackString = delete(this);
				break;
			case ("deleteAll"):
				feedbackString = deleteAll(this);
				break;
			case ("undone") :
				feedbackString = setUndone(this);
				break;
			case ("done") :
				feedbackString = setDone(this);
				break;
			case ("reminder") :
				feedbackString = setReminder(this);
				break;
			case ("description") :
				feedbackString = setDescription(this);
				break;
			case ("event") :
				feedbackString = setEventStartAndEndTime(this);
				break;
			case ("deadline") :
				feedbackString = setDeadline(this);
				break;
			case ("every") :
				feedbackString = setRecurring(this);
				break;	
			case ("rename"):
				feedbackString = rename(this);
				break;
			case ("reset"):
				feedbackString = reset(this);
		}
		currentState = storageComponent.load();
		historySubComponent.push(currentState);
		return feedbackString;
	}
	
	//@@author A0121284N
	public String undo() {
		try {
			ArrayList<Task> historyState = historySubComponent.undo();
			storageComponent.save(historyState);
			return "Undo successfully";
		} catch (EmptyStackException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	//@@author A0121284N
	public String redo() {
		try {
			ArrayList<Task> futureState = historySubComponent.redo();
			storageComponent.save(futureState);
			return "Redo successfully";
		} catch (EmptyStackException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	//@@author A0126258A
	private void setTaskId(ArrayList<Task> taskList) {
		
		Collections.sort(taskList);
	
		int newTaskId = 0;
		
		for(Task task : taskList) {
			task.setTaskId(newTaskId);
			newTaskId++;
		}
	}
	
	//@@author A0121284N
	private String setMultipleFieldsForTask(Command commandObject) {

		logicEditorLogger.info("taskId: "+commandObject.getTaskName());
		try {
			int taskId = Integer.parseInt(commandObject.getTaskName());
			logicEditorLogger.info("taskId: "+taskId);

			if(!commandObject.getNewName().equals("")) {
				logicEditorLogger.info("New Name: "+ commandObject.getNewName());
				rename(commandObject);
			}
			if (!commandObject.getDescription().equals("")) {
				logicEditorLogger.info("Description: "+ commandObject.getDescription());
				setDescription(commandObject);
			}
			if (!commandObject.getPriority().equals("")) {
				logicEditorLogger.info("Priority :"+commandObject.getPriority());
				setPriority(commandObject);
			}
			if (!commandObject.getReminder().equals("")) {
				logicEditorLogger.info("reminder: "+commandObject.getReminder());
				setReminder(commandObject);
			}
			if(!commandObject.getRecurrence().equals("")) {
				logicEditorLogger.info("category: "+commandObject.getRecurrence());
				setRecurring(commandObject);
			}
			if (!commandObject.getEndDateAndTime().equals("")) {
				setDeadline(commandObject);
				logicEditorLogger.info("deadline :"+commandObject.getEndDateAndTime());
			}
			return EXECUTION_SET_SUCCESSFUL;
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	//@@author A0121284N
	private String setMultipleFieldsForEvents(Command commandObject) {

		try {
			int taskId = Integer.parseInt(commandObject.getTaskName());
			logicEditorLogger.info("taskId: "+taskId);
			if(!commandObject.getNewName().equals("")) {
				rename(commandObject);
			}
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
			if(!commandObject.getRecurrence().equals("")) {
				logicEditorLogger.info("category: "+commandObject.getRecurrence());
				setRecurring(commandObject);
			}
			if (!commandObject.getStartDateAndTime().equals("") && 
					!commandObject.getEndDateAndTime().equals("")) {
				setEventStartAndEndTime(commandObject);
			}
			return EXECUTION_SET_SUCCESSFUL;
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	//@@author A0121284N
	private String setMultipleFieldsForFloats(Command commandObject) {

		try {
			int taskId = Integer.parseInt(commandObject.getTaskName());
			logicEditorLogger.info("taskId: "+taskId);
			if(!commandObject.getNewName().equals("")) {
				rename(commandObject);
			}
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
			return EXECUTION_SET_SUCCESSFUL;
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	//@@author A0126258A
	private Task.RecurrenceType getRecurrenceType(String recurrenceType) {

		if(recurrenceType.equals(RECURRING_DAY)) {
			return Task.RecurrenceType.DAY;
		} if(recurrenceType.equals(RECURRING_WEEK)) {
			return Task.RecurrenceType.WEEK;
		} if(recurrenceType.equals(RECURRING_MONTH)) {
			return Task.RecurrenceType.MONTH;
		} if(recurrenceType.equals(RECURRING_YEAR)) {
			return Task.RecurrenceType.YEAR;
		} else {
			return Task.RecurrenceType.NONE;
		} 
	}
	
	//@@author A0126258A
	private Task.TaskType getTaskType(Task task) {
		
		if(!task.getEnd().isEmpty()) {
			return Task.TaskType.TODO;
		} else {
			return Task.TaskType.FLOATING;
		}
	}
	
	//@@author A0126258A
	private int getTaskId(int taskIndex) {
		
		ArrayList<Task> taskList = storageComponent.load();
		
		int taskId = -1;
		for(Task task : taskList) {
			if(task.getIndex() == taskIndex) {
				taskId = task.getTaskId();
			}
		}
		
		return taskId;
	}
	
	//@@author A0121284N
	private String delete(Command commandObject) {
		try{
			int taskIndex = Integer.parseInt(commandObject.getTaskName());
			int taskId = getTaskId(taskIndex);

			taskList = storageComponent.load();
			Task toBeRemoved = taskList.remove(taskId);
			setTaskId(taskList);
			storageComponent.save(taskList);
			if(toBeRemoved != null) {
				return String.format(EXECUTION_DELETE_SUCCESSFUL, taskIndex);
			} else {
				return EXECUTION_COMMAND_UNSUCCESSFUL;
			}
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	//@@author A0121284N
	private String reset(Command commandObject) {
		taskList = storageComponent.load();
		int taskIndex = Integer.parseInt(commandObject.getTaskName());
		int taskId = getTaskId(taskIndex);
		String resetField = commandObject.getResetField();
//		System.out.println(resetField);
		switch(resetField) {
			case "priority" :
				taskList.get(taskId).setPriority(0);
				break;
			case "description" :
				taskList.get(taskId).setDescription("");
				break;
			case "reminder" :
				taskList.get(taskId).setReminder("");
				break;
			case "date" :
				taskList.get(taskId).setTaskType(TaskType.FLOATING);
				taskList.get(taskId).setStart("");
				taskList.get(taskId).setEnd("");
				break;
			case "all" :
				taskList.get(taskId).setTaskType(TaskType.FLOATING);
				taskList.get(taskId).setStart("");
				taskList.get(taskId).setEnd("");
				taskList.get(taskId).setPriority(0);
				taskList.get(taskId).setDescription("");
				taskList.get(taskId).setReminder("");
				break;
		}
		storageComponent.save(taskList);
		return String.format("Field %1$s has been reset", resetField);
	}
	
	//@@author A0121284N
	private String deleteAll(EditCommand editCommand) {
		taskList = storageComponent.load();
		storageComponent.save(null);
		return "Everything has been deleted";
	}

	//@@author A0121284N
	private String setPriority(Command commandObject){
		try {
			taskList = storageComponent.load();
			int taskIndex = Integer.parseInt(commandObject.getTaskName());
			int priority = Integer.parseInt(commandObject.getPriority());
			int taskId = getTaskId(taskIndex);
			
			taskList.get(taskId).setPriority(priority);
			storageComponent.save(taskList);	

			return String.format(EXECUTION_SET_PRIORITY_SUCCESSFUL, taskIndex, priority);
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	//@@author A0121284N
	private String rename(Command commandObject) {
		taskList = storageComponent.load();
		int taskIndex = Integer.parseInt(commandObject.getTaskName());
		int taskId = getTaskId(taskIndex);
		String newName = commandObject.getNewName();
		taskList.get(taskId).setName(newName);
		storageComponent.save(taskList);
		return String.format("Task %1$s has been renamed to %2$s", taskIndex, newName);
	}

	//@@author A0121284N
	private String setUndone(Command commandObject) {
		try {
			taskList = storageComponent.load();
			int taskIndex = Integer.parseInt(commandObject.getTaskName());
			int taskId = getTaskId(taskIndex);
			
			taskList.get(taskId).setDone(false);
			storageComponent.save(taskList);

			return String.format(EXECUTION_DONE_COMMAND_SUCCESSFUL, taskIndex);
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	//@@author A0121284N
	private String setDone(Command commandObject) {
		try {
			taskList = storageComponent.load();
			int taskIndex = Integer.parseInt(commandObject.getTaskName());
			int taskId = getTaskId(taskIndex);
			
			taskList.get(taskId).setDone(true);
			taskList.get(taskId).setIndex(-1);
			storageComponent.save(taskList);

			return String.format(EXECUTION_UNDONE_COMMAND_SUCCESSFUL, taskIndex);
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	//@@author A0121284N
	private String setReminder(Command commandObject) {
		try {
			taskList = storageComponent.load();
			int taskIndex = Integer.parseInt(commandObject.getTaskName());
			String reminder = commandObject.getReminder();
			int taskId = getTaskId(taskIndex);
			taskList.get(taskId).setReminder(reminder);
			storageComponent.save(taskList);

			return String.format(EXECUTION_SET_REMINDER_SUCCESSFUL,taskIndex,reminder);
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	//@@author A0121284N
	private String setDescription(Command commandObject) {
		try {
			taskList = storageComponent.load();
			int taskIndex = Integer.parseInt(commandObject.getTaskName());
			String description = commandObject.getDescription();
			int taskId = getTaskId(taskIndex);
			taskList.get(taskId).setDescription(description);
			storageComponent.save(taskList);

			return String.format(EXECUTION_SET_DESCRIPTION_SUCCESSFUL, taskIndex);
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	//@@author A0121284N
	private String setEventStartAndEndTime(Command commandObject) {
		try {
			taskList = storageComponent.load();
			int eventIndex = Integer.parseInt(commandObject.getTaskName());
			String start = commandObject.getStartDateAndTime();
			String end = commandObject.getEndDateAndTime();
			int taskId = getTaskId(eventIndex);
			Task task = taskList.get(taskId);

			Command command = new Command();
			command.setTaskName(Integer.toString(eventIndex));
			task.setTaskType(Task.TaskType.EVENT);
			task.setStart(start);
			task.setEnd(end);
			setTaskId(taskList);
			storageComponent.save(taskList);

			return String.format(EXECUTION_SET_EVENT_START_AND_END_TIME_SUCCESSFUL, eventIndex,start,end);
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	//@@author A0121284N
	private String setDeadline(Command commandObject) {
		try {
			taskList = storageComponent.load();
			int taskIndex = Integer.parseInt(commandObject.getTaskName());
			String end = commandObject.getEndDateAndTime();
			int taskId = getTaskId(taskIndex);
			Task task = taskList.get(taskId);

			Command command = new Command();
			command.setTaskName(Integer.toString(taskIndex));
			task.setStart("");
			task.setEnd(end);
			task.setTaskType(getTaskType(task));
			setTaskId(taskList);
			storageComponent.save(taskList);
			
			return String.format(EXECUTION_SET_DEADLINE_SUCCESSFUL, taskIndex, end);
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	//@@author A0126258A
	private String setRecurring(Command commandObject) {
		try {
			taskList = storageComponent.load();
			int taskIndex = Integer.parseInt(commandObject.getTaskName());
			String[] recurrence = commandObject.getRecurrence().split(" ");
			int recurrenceFrequency = Integer.parseInt(recurrence[0]);
			String recurrenceType = recurrence[1];
			int taskId = getTaskId(taskIndex);
			Task task = taskList.get(taskId);
			Command command = new Command();
			command.setTaskName(Integer.toString(taskIndex));
			task.setRecurrenceFrequency(recurrenceFrequency);
			task.setRecurrenceType(getRecurrenceType(recurrenceType));
			setTaskId(taskList);
			storageComponent.save(taskList);
			
			return String.format(EXECUTION_SET_RECURRING_SUCCESSFUL, taskIndex, recurrenceFrequency + " " + recurrenceType);
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}

}
