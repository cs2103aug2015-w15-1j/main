package main.java.backend.Logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;

import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Task;
import main.java.backend.Storage.Task.Task.RecurrenceType;
import main.java.backend.Storage.Task.Task.TaskType;

public class AddCommand extends Command {
	
	private static final String EXECUTION_ADD_SUBTASK_SUCCESSFUL = "SubTask %1$s is added to Task %2$s";
	private static final String EXECUTION_ADD_TASK_SUCCESSFUL = "Task %1$s has been added";
	private static final String EXECUTION_COMMAND_SUCCESSFUL =  "Valid Command.";
	private static final String EXECUTION_COMMAND_UNSUCCESSFUL = "Invalid Command. Please try again.";
	private static final String CATEGORY_DEFAULT = "default";
	
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
	
	//@@author A0121284N
	public String execute() {	
//		System.out.println("Future state before execution: "+futureState);
//		System.out.println("History state before execution: "+historyState);
//		System.out.println("Current state before execution: "+currentState);
		String feedbackString = new String();
		System.out.println("execute commandObject.getCommandField: "+this.getCommandField());
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
//		System.out.println("History state after execution: "+historyState);
//		System.out.println("Future state after execution: "+futureState);
//		System.out.println("Current state after execution: "+currentState);
		System.out.println("feedbackString in AddCommandObject: "+ feedbackString);
		currentState = storageComponent.load();
		historySubComponent.push(currentState);
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
	
	//@@author A0121284N
	private String getCategoryName(String categoryName) {
		
		if(categoryName.isEmpty()) {
			return CATEGORY_DEFAULT;
		} else {
			return categoryName;
		}
	}
	//@@author A0126258A
	private Task getTask(TaskType taskType, Command command) {
		
		RecurrenceType recurrenceType = getRecurrence(command.getRecurrenceType());
		int recurrenceNumber = stringToInteger(command.getRecurrenceFrequency());
		int priority = stringToInteger(command.getPriority());
		String categoryName = getCategoryName(command.getCategory());
		String taskName = command.getTaskName();
		String taskDescription = command.getDescription();
		String startDate = command.getStartDateAndTime();
		String endDate = command.getEndDateAndTime();
		String reminderDate = command.getReminder();

		return new Task(taskType, recurrenceType, recurrenceNumber, priority, categoryName, 
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
