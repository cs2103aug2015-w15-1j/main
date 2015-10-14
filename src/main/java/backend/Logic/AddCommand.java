package main.java.backend.Logic;

import java.util.TreeMap;

import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Task;
import main.java.backend.Storage.Task.TaskType;

public class AddCommand extends Command {
	
	private static final String EXECUTION_ADD_SUBTASK_SUCCESSFUL = "SubTask %1$s is added to Task %2$s";
	private static final String EXECUTION_ADD_TASK_SUCCESSFUL = "Task %1$s has been added";
	private static final String EXECUTION_COMMAND_SUCCESSFUL =  "Valid Command.";
	private static final String EXECUTION_COMMAND_UNSUCCESSFUL = "Invalid Command. Please try again.";
	private static final String CATEGORY_DEFAULT = "default";
	
	private TreeMap<Integer, Task> taskList;

	private Storage storageComponent;

	public AddCommand(Type typeInput, Storage storage) {
		super(typeInput);
		storageComponent = storage;
	}
	
	public String execute(Command commandObject) {
		
		String feedbackString = new String();
		
		switch (commandObject.getCommandField()) {
			case ("addF") :
				feedbackString = addTask(TaskType.FLOATING, commandObject);
				break;
			case ("addT") :
				feedbackString = addTask(TaskType.TODO, commandObject);
				break;	
			case ("addE") :
				feedbackString = addTask(TaskType.EVENT, commandObject);
				break;
			case ("addS"):
				feedbackString = addSubTask(commandObject);
				break;	
		}
		
		return feedbackString;
	}
	
	private TreeMap<Integer, Task> generateTaskId() {
		
		TreeMap<Integer, Task> taskList = storageComponent.load();
		TreeMap<Integer, Task> newTaskList = new TreeMap<Integer, Task> ();
		int newTaskId = 0;
		
		for(Integer taskId : taskList.keySet()) {
			Task task = taskList.get(taskId);
			task.setTaskId(newTaskId);
			newTaskList.put(newTaskId, task);
			newTaskId++;
		}
		
		return newTaskList;
	}
	
	private int getPriority(String priority) {
		
		if (!priority.isEmpty()) {
			return Integer.parseInt(priority);
		} else {
			return -1;
		}
	}
	
	private String priorityChecker(String priority) {
		
		if (!priority.isEmpty()) {
			int priorityInt = Integer.parseInt(priority);
			
			if (priorityInt < 1 || priorityInt > 5) {
				return EXECUTION_COMMAND_UNSUCCESSFUL;
			}
		} 
		
		return EXECUTION_COMMAND_SUCCESSFUL;
	}
	
	private String getCategoryName(String categoryName) {
		
		if(categoryName.isEmpty()) {
			return CATEGORY_DEFAULT;
		} else {
			return categoryName;
		}
	}

	private Task getTask(TaskType taskType, Command command) {
		
		int priority = getPriority(command.getPriority());
		String categoryName = getCategoryName(command.getCategory());
		String taskName = command.getTaskName();
		String taskDescription = command.getDescription();
		String startDate = command.getStartDateAndTime();
		String endDate = command.getEndDateAndTime();
		String reminderDate = command.getReminder();

		return new Task(taskType, priority, categoryName, taskName, 
				taskDescription, startDate, endDate, reminderDate);
	}

	private String addTask(TaskType taskType, Command command) {
		
		String execution = priorityChecker(command.getPriority());
		Task newTask = getTask(taskType, command);
		
		if(execution.equals(EXECUTION_COMMAND_SUCCESSFUL)) {
			taskList = generateTaskId();
			newTask.setTaskId(taskList.size());
			taskList.put(taskList.size(), newTask);
			storageComponent.save(taskList);
		}
		
		return String.format(EXECUTION_ADD_TASK_SUCCESSFUL, newTask.getName());
	}

	private String addSubTask(Command commandObject) {
		
		String taskName = commandObject.getTaskName();
		String subTaskDescription = commandObject.getDescription();
//		storageObject.addSubTask(taskName,subTaskDescription);
		return String.format(EXECUTION_ADD_SUBTASK_SUCCESSFUL, subTaskDescription,taskName);
	}
}
