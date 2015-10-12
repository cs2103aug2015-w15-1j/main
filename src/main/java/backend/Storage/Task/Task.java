package main.java.backend.Storage.Task;

import java.util.TreeMap;
import java.util.UUID;

public class Task implements Comparable<Task> {
	
	private String taskId;
	private int indexForPrinting;
	private String category;
	private String name;
	private String description;
	private String startDate;
	private String endDate;
	private String reminderDate;
	private int priority;
	private boolean isDone;

	private TreeMap<String, SubTask> subTask;

	public Task() {

	}

	// Floating task
	public Task(String category, String taskName, String taskDescription, 
			int priority, String reminderDate, boolean isDone) {
		
		setTaskId(UUID.randomUUID().toString());
		setCategory(category);
		setName(taskName);
		setDescription(taskDescription);
		setStartDate("");
		setEndDate("");
		setReminderDate(reminderDate);
		setPriority(priority);
		setDone(isDone);
		setSubTask(new TreeMap<String, SubTask> ());
	}

	// Task
	public Task(String category, String taskName, String taskDescription, 
			String deadline, int priority, String reminderDate, boolean isDone) {
		setTaskId(UUID.randomUUID().toString());
		setCategory(category);
		setName(taskName);
		setDescription(taskDescription);
		setStartDate("");
		setEndDate(deadline);
		setPriority(priority);
		setReminderDate(reminderDate);
		setDone(isDone);
		setSubTask(new TreeMap<String, SubTask> ());
	}

	// Event
	public Task(String category, String eventName, String eventDescription, 
			String startDate, String endDate, int priority, String reminderDate) {
		
		setTaskId(UUID.randomUUID().toString());
		setCategory(category);
		setName(eventName);
		setDescription(eventDescription);
		setStartDate(startDate);
		setEndDate(endDate);
		setPriority(priority);
		setReminderDate(reminderDate);
		setSubTask(new TreeMap<String, SubTask> ());
	}
	
	public String getTaskId() {
		return taskId;
	}
	
	public void setTaskId(String newTaskId) {
		this.taskId = newTaskId;
	}
	
	public int getIndex() {
		return indexForPrinting;
	}
	
	public void setIndex(int index){
		indexForPrinting = index;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setReminderDate(String reminderDate) {
		this.reminderDate = reminderDate;
	}
	
	public String getReminderDate() {
		return reminderDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
		if (startDate!=""){
			sb.append(startDate + System.getProperty("line.separator"));
		}
		if(endDate!=""){
			sb.append(endDate + System.getProperty("line.separator"));
		}
		if(reminderDate!=""){
			sb.append(reminderDate + System.getProperty("line.separator"));
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
		if(startDate !=""){
			sb.append(startDate + System.getProperty("line.separator"));
		}
		if (endDate!=""){
			sb.append(endDate + System.getProperty("line.separator"));
		}
		if (reminderDate!=""){
			sb.append("reminder: "+ reminderDate + System.getProperty("line.separator"));
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
		
		if(this.name.compareTo(o.name) < 0) {
			return -1;
		} else if(this.name.compareTo(o.name) > 0) {
			return 1;
		} else {
			return 0;
		}
	}
}