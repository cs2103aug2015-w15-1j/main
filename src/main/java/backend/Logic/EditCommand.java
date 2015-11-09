package main.java.backend.Logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.logging.Logger;

import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Task;
import main.java.backend.Storage.Task.Task.TaskType;
import main.java.backend.Util.LoggerGlobal;

public class EditCommand extends Command {

	private static final String EXECUTION_UNSUCCESSFUL = "Task index is invalid. Please try again.";
	private static final String EXECUTION_SET_PRIORITY_SUCCESSFUL = "Task %1$s has been set to priority %2$s";
	private static final String EXECUTION_SET_SUCCESSFUL = "Fields have been updated";
	private static final String EXECUTION_DELETE_SUCCESSFUL = "Task %1$s has been deleted";
	private static final String EXECUTION_SET_DEADLINE_SUCCESSFUL = "Task %1$s deadline has been set to %2$s";
	private static final String EXECUTION_SET_RECURRING_SUCCESSFUL = "Task %1$s recurring has been set to %2$s";
	private static final String EXECUTION_SET_RECURRING_UNSUCCESSFUL = "Unable to recur floating tasks";
	private static final String EXECUTION_SET_EVENT_START_AND_END_TIME_SUCCESSFUL = "Event %1$s has been setted to %2$s till %3$s";
	private static final String EXECUTION_SET_DESCRIPTION_SUCCESSFUL = "Description for task %1$s has been set";
	private static final String EXECUTION_SET_REMINDER_SUCCESSFUL = "Reminder for Task %1$s has been set to be at %2$s";
	private static final String EXECUTION_COMMAND_UNSUCCESSFUL = "Invalid Command. Please try again.";
	private static final String EXECUTION_UNDONE_COMMAND_SUCCESSFUL = "Task %1$s is completed";
	private static final String EXECUTION_DONE_COMMAND_SUCCESSFUL = "Task %1$s is not completed";
	private static final String EXECUTION_RESET_SUCCESSFUL = "Field %1$s has been reset";
	private static final String EXECUTION_RENAME_SUCCESSFUL = "Task %1$s has been renamed to %2$s";
	private static final String EXECUTION_DELETE_ALL_SUCCESSFUL = "Everything has been deleted";
	private static final String EXECUTION_REDO_SUCCESSFUL = "Redo successfully.";
	private static final String EXECUTION_UNDO_SUCCESSFUL = "Undo successfully.";
	
	private static final String LOGGER_COMMAND_FIELD = "Get CommandField: ";
	private static final String LOGGER_TASKID = "TaskId: ";
	private static final String LOGGER_DESCRIPTION = "Description: ";
	private static final String LOGGER_PRIORITY = "Priority: ";
	private static final String LOGGER_REMINDER = "Reminder: ";
	private static final String LOGGER_RECURRING = "Recurring: ";
	private static final String LOGGER_DEADLINE = "Deadline: ";
	private static final String LOGGER_STARTDATE = "Start date: ";
	private static final String LOGGER_ENDDATE = "End date: ";

	private static final String COMMAND_PRIORITY = "priority";
	private static final String COMMAND_ONESHOT_TASK = "setT";
	private static final String COMMAND_ONESHOT_EVENT = "setE";
	private static final String COMMAND_ONESHOT_FLOAT = "set";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_DELETE_ALL = "deleteAll";
	private static final String COMMAND_UNDONE = "undone";
	private static final String COMMAND_DONE = "done";
	private static final String COMMAND_REMINDER = "reminder";
	private static final String COMMAND_DESCRIPTION = "description";
	private static final String COMMAND_EVENT = "event";
	private static final String COMMAND_DEADLINE = "deadline";
	private static final String COMMAND_EVERY = "every";
	private static final String COMMAND_RENAME = "rename";
	private static final String COMMAND_RESET = "reset";
	private static final String COMMAND_DATE = "date";
	private static final String COMMAND_ALL = "all";

	private static final String RECURRING_DAY = "day";
	private static final String RECURRING_WEEK = "week";
	private static final String RECURRING_MONTH = "month";
	private static final String RECURRING_YEAR = "year";
	
	private static final String RESET = "";
	
	private static final Logger logicEditorLogger = LoggerGlobal.getLogger();

	private ArrayList<Task> taskList;
	private ArrayList<Task> currentState;

	private Storage storageComponent;
	private History historySubComponent;

	//@@author A0121284N
	public EditCommand(Type typeInput, Storage storage, History history) {

		super(typeInput);
		storageComponent = storage;
		historySubComponent = history;
		taskList = storageComponent.load();
	}

	//@@author A0121284N
	private String setMultipleFieldsForFloats(Command commandObject) {

		try {
			int taskId = Integer.parseInt(commandObject.getTaskName());
			logicEditorLogger.info(LOGGER_TASKID + taskId);
			
			if(!commandObject.getNewName().isEmpty()) {
				rename(commandObject);
			}
			if (!commandObject.getDescription().isEmpty()) {
				logicEditorLogger.info(LOGGER_DESCRIPTION + commandObject.getDescription());
				setDescription(commandObject);
			}
			if (!commandObject.getPriority().isEmpty()) {
				logicEditorLogger.info(LOGGER_PRIORITY + commandObject.getPriority());
				setPriority(commandObject);
			}
			if (!commandObject.getReminder().isEmpty()) {
				logicEditorLogger.info(LOGGER_REMINDER + commandObject.getReminder());
				setReminder(commandObject);
			}
			return EXECUTION_SET_SUCCESSFUL;
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}

	//@@author A0121284N
	private String setMultipleFieldsForTodo(Command commandObject) {

		try {
			setMultipleFieldsForFloats(commandObject);
			
			if(!commandObject.getRecurrence().isEmpty()) {
				logicEditorLogger.info(LOGGER_RECURRING + commandObject.getRecurrence());
				setRecurring(commandObject);
			}
			if (!commandObject.getEndDateAndTime().isEmpty()) {
				setDeadline(commandObject);
				logicEditorLogger.info(LOGGER_DEADLINE + commandObject.getEndDateAndTime());
			}
			return EXECUTION_SET_SUCCESSFUL;
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}

	//@@author A0121284N
	private String setMultipleFieldsForEvents(Command commandObject) {

		try {
			setMultipleFieldsForFloats(commandObject);
			
			if(!commandObject.getRecurrence().isEmpty()) {
				logicEditorLogger.info(LOGGER_RECURRING +commandObject.getRecurrence());
				setRecurring(commandObject);
			}
			if (!commandObject.getStartDateAndTime().isEmpty() && 
					!commandObject.getEndDateAndTime().isEmpty()) {
				logicEditorLogger.info(LOGGER_STARTDATE +commandObject.getStartDateAndTime());
				logicEditorLogger.info(LOGGER_ENDDATE +commandObject.getStartDateAndTime());
				setEventStartAndEndTime(commandObject);
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
	private boolean taskExist(Command command) {
		
		int taskIndex = -1;
		
		if(command.getTaskName().isEmpty()) {
			return true;
		} else {
			taskIndex = Integer.parseInt(this.getTaskName());
		}
		
		for(Task task : taskList) {
			if(taskIndex != -1 && task.getIndex() == taskIndex) {
				return true;
			}
		}
		
		return false;
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

		int taskId = -1;
		for(Task task : taskList) {
			if(task.getIndex() == taskIndex) {
				taskId = task.getTaskId();
			}
		}

		return taskId;
	}
	

	//@@author A0121284N
	private String reset(Command commandObject) {
		
		int taskIndex = Integer.parseInt(commandObject.getTaskName());
		int taskId = getTaskId(taskIndex);
		String resetField = commandObject.getResetField();
		
		switch(resetField) {
			case COMMAND_PRIORITY :
				taskList.get(taskId).setPriority(0);
				break;
			case COMMAND_DESCRIPTION :
				taskList.get(taskId).setDescription(RESET);
				break;
			case COMMAND_REMINDER :
				taskList.get(taskId).setReminder(RESET);
				break;
			case COMMAND_DATE :
				taskList.get(taskId).setTaskType(TaskType.FLOATING);
				taskList.get(taskId).setStart(RESET);
				taskList.get(taskId).setEnd(RESET);
				break;
			case COMMAND_ALL :
				taskList.get(taskId).setTaskType(TaskType.FLOATING);
				taskList.get(taskId).setStart(RESET);
				taskList.get(taskId).setEnd(RESET);
				taskList.get(taskId).setPriority(0);
				taskList.get(taskId).setDescription(RESET);
				taskList.get(taskId).setReminder(RESET);
				break;
		}
		
		storageComponent.save(taskList);
		return String.format(EXECUTION_RESET_SUCCESSFUL, resetField);
	}

	//@@author A0121284N
	private String delete(Command commandObject) {
		
		try{
			int taskIndex = Integer.parseInt(commandObject.getTaskName());
			int taskId = getTaskId(taskIndex);

			taskList.remove(taskId);
			setTaskId(taskList);
			storageComponent.save(taskList);
			
			return String.format(EXECUTION_DELETE_SUCCESSFUL, taskIndex);
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}

	//@@author A0121284N
	private String deleteAll(EditCommand editCommand) {
		
		storageComponent.save(null);
		return EXECUTION_DELETE_ALL_SUCCESSFUL;
	}

	//@@author A0121284N
	private String setPriority(Command commandObject){
		
		try {
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
		
		String newName = commandObject.getNewName();
		int taskIndex = Integer.parseInt(commandObject.getTaskName());
		int taskId = getTaskId(taskIndex);
		
		taskList.get(taskId).setName(newName);
		storageComponent.save(taskList);
		
		return String.format(EXECUTION_RENAME_SUCCESSFUL, taskIndex, newName);
	}

	//@@author A0121284N
	private String setUndone(Command commandObject) {
		
		try {
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
			int taskIndex = Integer.parseInt(commandObject.getTaskName());
			String reminder = commandObject.getReminder();
			int taskId = getTaskId(taskIndex);
			
			taskList.get(taskId).setReminder(reminder);
			storageComponent.save(taskList);

			return String.format(EXECUTION_SET_REMINDER_SUCCESSFUL, taskIndex, reminder);
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}

	//@@author A0121284N
	private String setDescription(Command commandObject) {
		
		try {
			String description = commandObject.getDescription();
			int taskIndex = Integer.parseInt(commandObject.getTaskName());
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
			String start = commandObject.getStartDateAndTime();
			String end = commandObject.getEndDateAndTime();
			int eventIndex = Integer.parseInt(commandObject.getTaskName());
			int taskId = getTaskId(eventIndex);
			
			Task task = taskList.get(taskId);
			task.setTaskType(Task.TaskType.EVENT);
			task.setStart(start);
			task.setEnd(end);
			setTaskId(taskList);
			storageComponent.save(taskList);

			return String.format(EXECUTION_SET_EVENT_START_AND_END_TIME_SUCCESSFUL, 
					eventIndex, start, end);
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}

	//@@author A0121284N
	private String setDeadline(Command commandObject) {
		
		try {
			String end = commandObject.getEndDateAndTime();
			int taskIndex = Integer.parseInt(commandObject.getTaskName());
			int taskId = getTaskId(taskIndex);
			Task task = taskList.get(taskId);

			task.setStart(RESET);
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
			String[] recurrence = commandObject.getRecurrence().split(" ");
			int recurrenceFrequency = Integer.parseInt(recurrence[0]);
			String recurrenceType = recurrence[1];
			int taskIndex = Integer.parseInt(commandObject.getTaskName());
			int taskId = getTaskId(taskIndex);
		
			Task task = taskList.get(taskId);
			
			if(!task.getTaskType().equals(TaskType.FLOATING)) {
				task.setRecurrenceFrequency(recurrenceFrequency);
				task.setRecurrenceType(getRecurrenceType(recurrenceType));
				setTaskId(taskList);
				storageComponent.save(taskList);
			} else {
				return EXECUTION_SET_RECURRING_UNSUCCESSFUL;
			}

			return String.format(EXECUTION_SET_RECURRING_SUCCESSFUL, taskIndex, 
					recurrenceFrequency + " " + recurrenceType);
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	//@@author A0121284N
	public String undo() {
		
		try {
			ArrayList<Task> historyState = historySubComponent.undo();
			storageComponent.save(historyState);
			return EXECUTION_UNDO_SUCCESSFUL;
		} catch (EmptyStackException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}

	//@@author A0121284N
	public String redo() {
		
		try {
			ArrayList<Task> futureState = historySubComponent.redo();
			storageComponent.save(futureState);
			return EXECUTION_REDO_SUCCESSFUL;
		} catch (EmptyStackException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	//@@author A0121284N
	public String execute(){

		String feedbackString = RESET;
		logicEditorLogger.info(LOGGER_COMMAND_FIELD + this.getCommandField());

		if(taskExist(this)) {
			switch(this.getCommandField()) {
				case (COMMAND_PRIORITY) :
					feedbackString = setPriority(this);
					break;
				case (COMMAND_ONESHOT_TASK) :
					feedbackString = setMultipleFieldsForTodo(this);
					break;
				case (COMMAND_ONESHOT_EVENT) :
					feedbackString = setMultipleFieldsForEvents(this);
					break;
				case (COMMAND_ONESHOT_FLOAT) :
					feedbackString = setMultipleFieldsForFloats(this);
					break;
				case (COMMAND_DELETE) :
					feedbackString = delete(this);
					break;
				case (COMMAND_DELETE_ALL):
					feedbackString = deleteAll(this);
					break;
				case (COMMAND_UNDONE) :
					feedbackString = setUndone(this);
					break;
				case (COMMAND_DONE) :
					feedbackString = setDone(this);
					break;
				case (COMMAND_REMINDER) :
					feedbackString = setReminder(this);
					break;
				case (COMMAND_DESCRIPTION) :
					feedbackString = setDescription(this);
					break;
				case (COMMAND_EVENT) :
					feedbackString = setEventStartAndEndTime(this);
					break;
				case (COMMAND_DEADLINE) :
					feedbackString = setDeadline(this);
					break;
				case (COMMAND_EVERY) :
					feedbackString = setRecurring(this);
					break;	
				case (COMMAND_RENAME):
					feedbackString = rename(this);
					break;
				case (COMMAND_RESET):
					feedbackString = reset(this);
					break;
			} 
		} else {
			feedbackString = EXECUTION_UNSUCCESSFUL;
		}

		currentState = taskList;
		historySubComponent.push(currentState);

		return feedbackString;
	}

}
