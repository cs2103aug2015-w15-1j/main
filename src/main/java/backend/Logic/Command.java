package main.java.backend.Logic;

public class Command {
	
	public enum Type {
		ADD, EDIT, SORT, SEARCH, EXIT, UNDO
	}
	
	private Type type;
	private String commandField = "";
	private String taskName = "";
	private String description = "";
	private String priority = "";
	private String reminder = ""; 
	private String category = "";
	private String startDateAndTime = "";
	private String endDateAndTime = "";
	private String deadline = "";
	private String keywords = "";
	private String colour = "";
	private final String LINE_SEPARATOR = System.getProperty("line.separator");

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
	
	public void setDeadline(String deadlineInput) {
		this.deadline = deadlineInput;
	}
	
	public void setKeywords(String keywordInput) {
		this.keywords = keywordInput;
	}
	
	public void setColour(String colourInput) {
		this.colour = colourInput;
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
	
	public String getDeadline() {
		return this.deadline;
	}
	
	public String getKeywords() {
		return this.keywords;
	}
	
	public String getColour() {
		return this.colour;
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
		format.append("deadline: " + getDeadline() + LINE_SEPARATOR);
		format.append("keywords: " + getKeywords() + LINE_SEPARATOR);
		format.append("colour: "+ getColour() + LINE_SEPARATOR);
		return format.toString();
	}
}
