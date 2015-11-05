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
	private String rename = ""; 
	private String category = "";
	private String startDateAndTime = "";
	private String endDateAndTime = "";
	private String keywords = "";
	private String newName = "";
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
		this.rename = reminderInput;
	}
	
	public void setRecurrence(String categoryInput) {
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
		return this.rename;
	}
	
	public String getRecurrence() {
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
	
	
	public String getNewName() {
		return this.newName;
	}

	public String execute() {
		return null;
	}

	public String undo() {
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
