package main.java.backend.Logic;

import java.io.IOException;
import java.util.EmptyStackException;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Task;

public class EditCommand extends Command {
	
	private static final String EXECUTION_SET_PRIORITY_SUCCESSFUL = "Task %1$s has been set to priority %2$s";
	private static final String EXECUTION_SET_SUCCESSFUL = "Fields have been updated";
	private static final String EXECUTION_DELETE_SUCCESSFUL = "Task %1$s has been deleted";
	private static final String EXECUTION_SET_DEADLINE_SUCCESSFUL = "Task %1$s deadline has been set to %2$s";
	private static final String EXECUTION_SET_RECURRING_SUCCESSFUL = "Task %1$s recurring has been set to %2$s";
	private static final String EXECUTION_SET_EVENT_START_AND_END_TIME_SUCCESSFUL = "Event %1$s has been setted to %2$s till %3$s";
	private static final String EXECUTION_SET_DESCRIPTION_SUCCESSFUL = "Description for task %1$s has been set";
	private static final String EXECUTION_SET_REMINDER_SUCCESSFUL = "Reminder for Task %1$s has been set to be at %2$s";
	private static final String EXECUTION_SET_CATEGORY_SUCCESSFUL = "Task %1$s is set to the category %2$s";
	private static final String EXECUTION_SET_COLOUR_SUCCESSFUL = "Category %1$s is set to the colour %2$s";
	private static final String EXECUTION_COMMAND_UNSUCCESSFUL = "Invalid Command. Please try again.";
	private static final String EXECUTION_UNDONE_COMMAND_SUCCESSFUL = "Task %1$s is completed";
	private static final String EXECUTION_DONE_COMMAND_SUCCESSFUL = "Task %1$s is not completed";
	
	private static final String RECURRING_DAY = "day";
	private static final String RECURRING_WEEK = "week";
	private static final String RECURRING_MONTH = "month";
	private static final String RECURRING_YEAR = "year";
	
	private static Logger logicEditorLogger = Logger.getGlobal();	
	private FileHandler logHandler;
	
	private TreeMap<Integer, Task> taskList;
	private TreeMap<Integer, Task> currentState;

	private Storage storageComponent;
	private History historySubComponent;
	
	public EditCommand(Type typeInput, Storage storage, History history) {
		super(typeInput);
		storageComponent = storage;
		historySubComponent = history;
		initLogger();
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

	public String execute(){
		String feedbackString = "";
		logicEditorLogger.info("Get Command Field: "+this.getCommandField());
//		System.out.println("Get Command Field: "+this.getCommandField());
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
			case ("setCol") :
//				feedbackString = setColour(this);
				break;
			case ("category") :
				feedbackString = setCategory(this);
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
			case ("recurring") :
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

	public String undo() {
		try {
			TreeMap<Integer, Task> historyState = historySubComponent.undo();
//			System.out.println("Future state: "+futureState);
//			System.out.println("Current state: "+currentState);
			storageComponent.save(historyState);
			return "Undo successfully";
		} catch (EmptyStackException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	public String redo() {
		try {
			TreeMap<Integer, Task> futureState = historySubComponent.redo();
			storageComponent.save(futureState);
			return "Redo successfully";
		} catch (EmptyStackException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}

	private String priorityChecker(int priority) {
		
		if (priority > 5 || priority < 1) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
		
		return null;
	}
	
	private String setMultipleFieldsForTask(Command commandObject) {

		logicEditorLogger.info("taskId: "+commandObject.getTaskName());
		try {
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
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}

	private String setMultipleFieldsForEvents(Command commandObject) {

		try {
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
//			System.out.println("priority setted");
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
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	private String setMultipleFieldsForFloats(Command commandObject) {

		try {
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
//			System.out.println("priority setted");
			if (!commandObject.getReminder().equals("")) {
				logicEditorLogger.info("reminder: "+commandObject.getReminder());
				setReminder(commandObject);
			}
			if(!commandObject.getCategory().equals("")) {
				logicEditorLogger.info("category: "+commandObject.getCategory());
				setCategory(commandObject);
			}
			return EXECUTION_SET_SUCCESSFUL;
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
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
	
	private Task.TaskType getTaskType(Task task) {
		
		if(!task.getEnd().isEmpty()) {
			return Task.TaskType.TODO;
		} else {
			return Task.TaskType.FLOATING;
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
		try{
			taskList = storageComponent.load();
			int taskIndex = Integer.parseInt(commandObject.getTaskName());
			int taskId = getTaskId(taskIndex);

			if(taskList.containsKey(taskId)) {
				taskList.remove(taskId);
			} else {
				return EXECUTION_COMMAND_UNSUCCESSFUL;
			}

			storageComponent.save(taskList);
			return String.format(EXECUTION_DELETE_SUCCESSFUL, taskIndex);
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
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
		}
		storageComponent.save(taskList);
		return String.format("Field %1$s has been reset", resetField);
	}
	
	private String deleteAll(EditCommand editCommand) {
		taskList = storageComponent.load();
		int numOfTasks = taskList.size();
		for (int taskId = 0; taskId < numOfTasks; taskId++) {
			taskList.remove(taskId);
		}
		storageComponent.save(taskList);
		return "Everything has been deleted";
	}

	private String setPriority(Command commandObject){
		try {
			taskList = storageComponent.load();
			int taskIndex = Integer.parseInt(commandObject.getTaskName());
			int priority = Integer.parseInt(commandObject.getPriority());
			int taskId = getTaskId(taskIndex);

			if (priorityChecker(priority) != null) {
				return priorityChecker(priority);
			}
			
			taskList.get(taskId).setPriority(priority);
			storageComponent.save(taskList);	

			return String.format(EXECUTION_SET_PRIORITY_SUCCESSFUL, taskIndex, priority);
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	private String rename(EditCommand commandObject) {
		taskList = storageComponent.load();
		int taskIndex = Integer.parseInt(commandObject.getTaskName());
		int taskId = getTaskId(taskIndex);
		String newName = commandObject.getNewName();
		taskList.get(taskId).setName(newName);
		storageComponent.save(taskList);
		return String.format("Task %1$s has been renamed to %2$s", taskIndex, newName);
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
		taskList = storageComponent.load();
		int taskIndex = Integer.parseInt(commandObject.getTaskName());
		String categoryName = commandObject.getCategory();
		int taskId = getTaskId(taskIndex);

		taskList.get(taskId).setCategoryName(categoryName);
		storageComponent.save(taskList);

		return String.format(EXECUTION_SET_CATEGORY_SUCCESSFUL, taskId, categoryName);
	}

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
			delete(command);
			task.setTaskType(Task.TaskType.EVENT);
			task.setStart(start);
			task.setEnd(end);
			taskList.put(taskId, task);
			storageComponent.save(taskList);

			return String.format(EXECUTION_SET_EVENT_START_AND_END_TIME_SUCCESSFUL, eventIndex,start,end);
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}

	private String setDeadline(Command commandObject) {
		try {
			taskList = storageComponent.load();
			int taskIndex = Integer.parseInt(commandObject.getTaskName());
			String end = commandObject.getEndDateAndTime();
			int taskId = getTaskId(taskIndex);
			Task task = taskList.get(taskId);

			Command command = new Command();
			command.setTaskName(Integer.toString(taskIndex));
			delete(command);
			task.setEnd(end);
			task.setTaskType(getTaskType(task));
			taskList.put(taskId, task);
			storageComponent.save(taskList);
			
			return String.format(EXECUTION_SET_DEADLINE_SUCCESSFUL, taskIndex, end);
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	
	private String setRecurring(Command commandObject) {
		try {
			taskList = storageComponent.load();
			int taskIndex = Integer.parseInt(commandObject.getTaskName());
			String recurrenceNumber = commandObject.getRecurrenceNumber();
			String recurrenceType = commandObject.getRecurrenceType();
			int taskId = getTaskId(taskIndex);
			Task task = taskList.get(taskId);

			Command command = new Command();
			command.setTaskName(Integer.toString(taskIndex));
			delete(command);
			task.setRecurrenceNumber(Integer.parseInt(recurrenceNumber));
			task.setRecurrenceType(getRecurrenceType(recurrenceType));
			taskList.put(taskId, task);
			storageComponent.save(taskList);
			
			return String.format(EXECUTION_SET_RECURRING_SUCCESSFUL, taskIndex, recurrenceType);
		} catch (NumberFormatException e) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
	}
	

}
