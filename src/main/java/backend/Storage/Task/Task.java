package main.java.backend.Storage.Task;

import java.util.TreeMap;

public class Task implements Comparable<Task> {
	
	public enum TaskType {
		FLOATING, TODO, EVENT;
	}
	
	public enum RecurrenceType {
		NONE, DAILY, WEEKLY, MONTHLY, YEARLY;
	}
	
	private TaskType taskType;
	private RecurrenceType recurrenceType;
	private int recurrenceNumber;
	private int taskId;
	private int indexForPrinting;
	private int priority;
	
	private boolean isDone;
	
	private String categoryName;
	private String categoryColour;
	private String name;
	private String description;
	private String start;
	private String end;
	private String reminder;

	private TreeMap<String, SubTask> subTask;
	
	public Task () {
		
	}
	
	public Task(TaskType taskType, RecurrenceType recurrenceType, int recurrenceNumber, 
			int priority, String categoryName, String name, String description, 
			String start, String end, String reminder) {
		
		assert taskType != null;
		assert name != null;
		
		this.taskType = taskType;
		this.recurrenceType = recurrenceType;
		this.recurrenceNumber = recurrenceNumber;
		this.priority = priority;
		this.isDone = false;
		this.categoryName = categoryName;
		this.name = name;
		this.description = description;
		this.start = start;
		this.end = end;
		this.reminder = reminder;
		this.subTask = new TreeMap<String, SubTask> ();
	}
	
	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}
	
	public RecurrenceType getRecurrenceType() {
		return recurrenceType;
	}

	public void setRecurrenceType(RecurrenceType recurrenceType) {
		this.recurrenceType = recurrenceType;
	}
	
	public int getRecurrenceNumber() {
		return recurrenceNumber;
	}

	public void setRecurrenceNumber(int recurrenceNumber) {
		this.recurrenceNumber = recurrenceNumber;
	}
	
	public int getTaskId() {
		return taskId;
	}
	
	public void setTaskId(int newTaskId) {
		this.taskId = newTaskId;
	}
	
	public int getIndex() {
		return indexForPrinting;
	}
	
	public void setIndex(int index){
		indexForPrinting = index;
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean getDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public String getCategoryColour() {
		return categoryColour;
	}

	public void setCategoryColour(String categoryColour) {
		this.categoryColour = categoryColour;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setReminder(String reminder) {
		this.reminder = reminder;
	}
	
	public String getReminder() {
		return reminder;
	}

	public void setEnd(String end) {
		this.end = end;
	}
	
	public TreeMap<String, SubTask> getSubTask() {
		return subTask;
	}

	public void setSubTask(TreeMap<String, SubTask> subTask) {
		this.subTask = subTask;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(indexForPrinting+". ");
		if(name!=""){
			sb.append(name + " ");
		}
		if(priority !=-1){
			for (int i=0;i<priority;i++){
			sb.append("*");
			}
		}
		sb.append(System.getProperty("line.separator"));
		if (start!=""){
			sb.append(start + System.getProperty("line.separator"));
		}
		if(end!=""){
			sb.append(end + System.getProperty("line.separator"));
		}
		if(reminder!=""){
			sb.append("reminder has been set." + System.getProperty("line.separator"));
		}
		return sb.toString();
	}
	
	public String printFull() {
		StringBuilder sb = new StringBuilder();
		sb.append(indexForPrinting+". ");
		if(name!=""){
			sb.append(name + " ");
		}
		if(priority !=-1){
			for (int i=0;i<priority;i++){
			sb.append("*");
			}
		}
		sb.append(System.getProperty("line.separator"));
		if (description!=""){
			sb.append(description + System.getProperty("line.separator"));
		}
		if(start !=""){
			sb.append(start + System.getProperty("line.separator"));
		}
		if (end!=""){
			sb.append(end + System.getProperty("line.separator"));
		}
		if (reminder!=""){
			sb.append("reminder: "+ reminder + System.getProperty("line.separator"));
		}
		if (isDone){
		sb.append("It's completed!" + System.getProperty("line.separator"));
		} else {
		sb.append("It's not done yet." + System.getProperty("line.separator"));	
		}
		return sb.toString();
	}
	
	@Override
	public int compareTo(Task o) {
		
		if(this.taskId < o.getTaskId()) {
			return -1;
		} else if(this.taskId > o.getTaskId()) {
			return 1;
		} else {
			return 0;
		}
	}

}