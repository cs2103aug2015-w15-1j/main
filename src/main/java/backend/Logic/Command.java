//@@author A0121284N
package main.java.backend.Logic;

import java.util.ArrayList;

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
	private String errorMessage = "";
	private String filePath = "";
	private String resetField = "";

    // ================================================================
    // General set command methods
    // ================================================================
	
	public Command() {
		
	}
	
	public Command(Type typeInput) {
		this.type = typeInput;
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
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public void setKeywords(String keywordInput) {
		this.keywords = keywordInput;
	}
	
	public void setNewName(String name) {
		this.newName = name;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
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

	public void setResetField(String command) {
		this.resetField = command;
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
	
	public String getResetField() {
		return this.resetField;
	}

	public String getFilePath() {
		return filePath;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public ArrayList<Task> getSearchResults() {
		return null;
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
	
}
