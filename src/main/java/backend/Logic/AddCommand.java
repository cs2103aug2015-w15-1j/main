package main.java.backend.Logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.logging.Logger;

import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Task;
import main.java.backend.Storage.Task.Task.RecurrenceType;
import main.java.backend.Storage.Task.Task.TaskType;
import main.java.backend.Util.LoggerGlobal;

public class AddCommand extends Command {

	private static final String EXECUTION_ADD_TASK_SUCCESSFUL = "Task %1$s has been added";
	private static final String EXECUTION_REDO_SUCCESSFUL = "Redo successfully.";
	private static final String EXECUTION_UNDO_SUCCESSFUL = "Undo successfully.";
	private static final String EXECUTION_COMMAND_UNSUCCESSFUL = "Invalid Command. Please try again.";

	private static final String LOGGER_COMMAND_EXECUTION = "Execute commandObject.getCommandField: ";
	private static final String LOGGER_COMMAND_ADD = "FeedbackString in AddCommandObject: ";
	
	private static final String COMMAND_ADD_FLOAT = "addF";
	private static final String COMMAND_ADD_TODO = "addT";
	private static final String COMMAND_ADD_EVENT = "addE";
	
	private static final String RECURRING_DAY = "day";
	private static final String RECURRING_WEEK = "week";
	private static final String RECURRING_MONTH = "month";
	private static final String RECURRING_YEAR = "year";

	private static final Logger logicAdderLogger = LoggerGlobal.getLogger();
	
	private ArrayList<Task> taskList;
	private ArrayList<Task> currentState;

	private Storage storageComponent;
	private History historySubComponent;

	//@@author A0121284N
	public AddCommand(Type typeInput, Storage storage, History history) {
		
		super(typeInput);
		storageComponent = storage;
		historySubComponent = history;
	}

	//@@author A0126258A
	private ArrayList<Task> generateTaskId() {

		ArrayList<Task> taskList = storageComponent.load();
		ArrayList<Task> newTaskList = new ArrayList<Task> ();
		int newTaskId = 0;

		for(Task task : taskList) {
			task.setTaskId(newTaskId);
			newTaskList.add(task);
			newTaskId++;
		}

		return newTaskList;
	}

	//@@author A0126258A
	private RecurrenceType getRecurrence(String recurrence) {

		if(recurrence.equals(RECURRING_DAY)) {
			return RecurrenceType.DAY;
		} else if(recurrence.equals(RECURRING_WEEK)) {
			return RecurrenceType.WEEK;
		} else if(recurrence.equals(RECURRING_MONTH)) {
			return RecurrenceType.MONTH;
		} else if(recurrence.equals(RECURRING_YEAR)) {
			return RecurrenceType.YEAR;
		}

		return RecurrenceType.NONE;
	}

	//@@author A0126258A
	private int stringToInteger(String string) {

		if (!string.isEmpty()) {
			return Integer.parseInt(string);
		} else {
			return -1;
		}
	}

	//@@author A0126258A
	private Task getTask(TaskType taskType, Command command) {
		String[] recurrence = command.getRecurrence().split(" ");
		RecurrenceType recurrenceType = getRecurrence(recurrence[1]);
		int recurrenceFrequency = stringToInteger(recurrence[0]);
		int priority = stringToInteger(command.getPriority());
		String taskName = command.getTaskName();
		String taskDescription = command.getDescription();
		String startDate = command.getStartDateAndTime();
		String endDate = command.getEndDateAndTime();
		String reminderDate = command.getReminder();

		return new Task(taskType, recurrenceType, recurrenceFrequency, priority,  
				taskName, taskDescription, startDate, endDate, reminderDate);
	}

	//@@author A0126258A
	private String addTask(TaskType taskType, Command command) {

		Task newTask = getTask(taskType, command);

		taskList = generateTaskId();
		newTask.setTaskId(taskList.size());
		taskList.add(newTask);
		Collections.sort(taskList);
		storageComponent.save(taskList);

		return String.format(EXECUTION_ADD_TASK_SUCCESSFUL, newTask.getName());
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
	public String execute() {	
		
		String feedbackString = new String();
		logicAdderLogger.info(LOGGER_COMMAND_EXECUTION + this.getCommandField());
		
		switch (this.getCommandField()) {
			case (COMMAND_ADD_FLOAT) :
				feedbackString = addTask(TaskType.FLOATING, this);
				break;
			case (COMMAND_ADD_TODO) :
				feedbackString = addTask(TaskType.TODO, this);
				break;	
			case (COMMAND_ADD_EVENT) :
				feedbackString = addTask(TaskType.EVENT, this);
				break;
		}
		
		logicAdderLogger.info(LOGGER_COMMAND_ADD + feedbackString);
		currentState = storageComponent.load();
		historySubComponent.push(currentState);
		
		return feedbackString;
	}
}
