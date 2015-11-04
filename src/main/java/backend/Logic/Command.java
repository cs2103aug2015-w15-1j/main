//@@author A0121284N
package main.java.backend.Logic;

import java.util.ArrayList;
import java.util.TreeMap;

import main.java.backend.Storage.Task.Task;

public class Command {
	
	public enum Type {
		ADD, EDIT, SORT, SEARCH, EXIT, UNDO, REDO, VIEW, ERROR, FILEPATH
	}
	
	private Type type;
	private String commandField = "";
	private String taskName = "";
	private String recurrenceType = "";
	private String recurrenceFrequency = "";
	private String description = "";
	private String priority = "";
	private String reminder = ""; 
	private String category = "";
	private String startDateAndTime = "";
	private String endDateAndTime = "";
	private String keywords = "";
	private String colour = "";
	private String newName = "";
	private static TreeMap<Integer, Task> historyState;
	private static TreeMap<Integer, Task> currentState;
	private static TreeMap<Integer, Task> futureState;
	private final String LINE_SEPARATOR = System.getProperty("line.separator");
	private String resetField = "";

    // ================================================================
    // General set command methods
    // ================================================================
	
	public Command(Type typeInput) {
		this.type = typeInput;
	}
	

	public Command() {
		
	}


	public void setCommandField(String commandInput) {
		this.commandField = commandInput;
	}
	
	public void setTaskName(String taskNameInput) {
		this.taskName = taskNameInput;
	}
	
	public void setRecurrenceType(String recurrenceType) {
		this.recurrenceType = recurrenceType;
	}

	public void setRecurrenceFrequency(String recurrenceFrequency) {
		this.recurrenceFrequency = recurrenceFrequency;
	}
	
	public void setDescription(String descriptionInput) {
		this.description = descriptionInput;
	}
	
	public void setPriority(String priorityInput) {
		this.priority = priorityInput;
	}
	
	public void setReminder(String reminderInput) {
		this.reminder = reminderInput;
	}
	
	public void setCategory(String categoryInput) {
		this.category = categoryInput;
	}
	
    // ================================================================
    // Additional for events creation set command methods
    // ================================================================
	
	public void setStartDateAndTime(String startDateAndTimeInput) {
		this.startDateAndTime = startDateAndTimeInput;
	}
	
	public void setEndDateAndTime(String endDateAndTimeInput) {
		this.endDateAndTime = endDateAndTimeInput;
	}

    // ================================================================
    // Additional for tasks creation set command methods
    // ================================================================
	
	public void setKeywords(String keywordInput) {
		this.keywords = keywordInput;
	}
	
	public void setColour(String colourInput) {
		this.colour = colourInput;
	}
	
	public void setHistoryState(TreeMap<Integer, Task> history) {
		historyState = history;
	}
	
	public void setCurrentState(TreeMap<Integer, Task> current) {
		currentState = current;
	}
	
	public void setFutureState(TreeMap<Integer, Task> future) {
		futureState = future;
	}

	public Type getType() {
		return this.type;
	}
	
	public String getCommandField() {
		return this.commandField;
	}
	
	public String getTaskName() {
		return this.taskName;
	}
	
	public String getRecurrenceType() {
		return this.recurrenceType;
	}

	public String getRecurrenceFrequency() {
		return this.recurrenceFrequency;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getPriority() {
		return this.priority;
	}
	
	public String getReminder() {
		return this.reminder;
	}
	
	public String getCategory() {
		return this.category;
	}
	
	public String getStartDateAndTime() {
		return this.startDateAndTime;
	}
	
	public String getEndDateAndTime() {
		return this.endDateAndTime;
	}
	
	public String getKeywords() {
		return this.keywords;
	}
	
	public String getColour() {
		return this.colour;
	}
	
	
	public String getNewName() {
		return this.newName;
	}
	
	public String toString() {
		StringBuilder format = new StringBuilder();
		format.append("type: "+ getType() + LINE_SEPARATOR);
		format.append("commandField: "+ getCommandField() + LINE_SEPARATOR);
		format.append("taskName: "+ getTaskName() + LINE_SEPARATOR);
		format.append("description: "+ getDescription() + LINE_SEPARATOR);
		format.append("priority: "+ getPriority() + LINE_SEPARATOR);
		format.append("reminder: " + getReminder() + LINE_SEPARATOR);
		format.append("category: " + getCategory() + LINE_SEPARATOR);
		format.append("startDateAndTime " + getStartDateAndTime() + LINE_SEPARATOR);
		format.append("endDateAndTime :" + getEndDateAndTime() + LINE_SEPARATOR);
		format.append("keywords: " + getKeywords() + LINE_SEPARATOR);
		format.append("colour: "+ getColour() + LINE_SEPARATOR);
		return format.toString();
	}
	
	public TreeMap<Integer, Task> getHistoryState() {
		return historyState;
	}
	
	public TreeMap<Integer, Task> getCurrentState() {
		return currentState;
	}
	
	public TreeMap<Integer, Task> getFutureState() {
		return futureState;
	}


	public String execute() {
		// TODO Auto-generated method stub
		return null;
	}


	public void setIndex(ArrayList<Task> list, int i, int taskIndex) {
		// TODO Auto-generated method stub
		
	}


	public String undo() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String redo() {
		return null;
	}


	public void setNewName(String name) {
		this.newName = name;
		
	}


	public void setErrorMessage(String string) {
		// TODO Auto-generated method stub
		
	}


	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}


	public void setResetField(String command) {
		this.resetField = command;
	}
	
	public String getResetField() {
		return this.resetField;
	}


	public ArrayList<Task> getSearchResults() {
		// TODO Auto-generated method stub
		return null;
	}


	public void setFilePath(String string) {
		// TODO Auto-generated method stub
		
	}


	public String getFilePath() {
		// TODO Auto-generated method stub
		return null;
	}
}
