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
import main.java.backend.Storage.Task.Task.RecurrenceType;
import main.java.backend.Storage.Task.Task.TaskType;

public class AddCommand extends Command {
	
	private static final String EXECUTION_ADD_TASK_SUCCESSFUL = "Task %1$s has been added";
	private static final String EXECUTION_COMMAND_UNSUCCESSFUL = "Invalid Command. Please try again.";
	
	private ArrayList<Task> taskList;
	private ArrayList<Task> currentState;

	private Storage storageComponent;
	private History historySubComponent;
	
	private static Logger logicAdderLogger = Logger.getGlobal();	
	private FileHandler logHandler;
	
	//@@author A0121284N
	public AddCommand(Type typeInput, Storage storage, History history) {
		super(typeInput);
		storageComponent = storage;
		historySubComponent = history;
		initLogger();
	}
	
	//@@author A0121284N
		private void initLogger() {
				
				try {
					logHandler = new FileHandler("TankTaskLog.txt",1000000000,10,true);
					logHandler.setFormatter(new SimpleFormatter());
					logicAdderLogger.addHandler(logHandler);
					logicAdderLogger.setUseParentHandlers(false);
					
				} catch (SecurityException | IOException e) {
					logicAdderLogger.warning("Logger failed to initialise: " + e.getMessage());
				}
			}
	
	//@@author A0121284N
	public String execute() {	
		String feedbackString = new String();
		logicAdderLogger.info("execute commandObject.getCommandField: "+this.getCommandField());
		switch (this.getCommandField()) {
			case ("addF") :
				feedbackString = addTask(TaskType.FLOATING, this);
				break;
			case ("addT") :
				feedbackString = addTask(TaskType.TODO, this);
				break;	
			case ("addE") :
				feedbackString = addTask(TaskType.EVENT, this);
				break;
		}
		logicAdderLogger.info("feedbackString in AddCommandObject: "+ feedbackString);
		currentState = storageComponent.load();
		historySubComponent.push(currentState);
		logHandler.close();
		return feedbackString;
	}
	
	//@@author A0121284N	
	public String undo() {
		try {
			ArrayList<Task> historyState = historySubComponent.undo();
//			System.out.println("Future state: "+futureState);
//			System.out.println("Current state: "+currentState);
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
		
		if(recurrence.equals("day")) {
			return RecurrenceType.DAY;
		} else if(recurrence.equals("week")) {
			return RecurrenceType.WEEK;
		} else if(recurrence.equals("month")) {
			return RecurrenceType.MONTH;
		} else if(recurrence.equals("year")) {
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
		
		RecurrenceType recurrenceType = getRecurrence(command.getRecurrenceType());
		int recurrenceNumber = stringToInteger(command.getRecurrenceFrequency());
		int priority = stringToInteger(command.getPriority());
		String taskName = command.getTaskName();
		String taskDescription = command.getDescription();
		String startDate = command.getStartDateAndTime();
		String endDate = command.getEndDateAndTime();
		String reminderDate = command.getReminder();

		return new Task(taskType, recurrenceType, recurrenceNumber, priority,  
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
}
