package main.java.backend.Storage.Task;

import java.util.Comparator;
import java.util.HashMap;

public class Task implements Comparable<Task> {
	
	private String taskId;
	private int indexForPrinting;
	private String name;
	private String description;
	private String startDate;
	private String endDate;
	private String reminderDate;
	private long startTime;
	private long endTime;
	private long reminderTime;
	private int priority;
	private boolean isDone;

	private HashMap<String, SubTask> subTask;

	public Task() {

	}

	// Floating task
	public Task(String taskId, String taskName, String taskDescription, int priority, 
			String reminderDate, long reminderTime, boolean isDone) {
		setTaskId(taskId);
		setName(taskName);
		setDescription(taskDescription);
		setStartDate("");
		setEndDate("");
		setReminderDate(reminderDate);
		setStartTime(-1);
		setEndTime(-1);
		setPriority(priority);
		setReminder(reminderTime);
		setDone(isDone);
		setSubTask(new HashMap<String, SubTask> ());
	}

	// Task
	public Task(String taskId, String taskName, String taskDescription, String deadline, long endTime, 
			int priority, String reminderDate, long reminder, boolean isDone) {
		setTaskId(taskId);
		setName(taskName);
		setDescription(taskDescription);
		setStartDate("");
		setEndDate(deadline);
		setStartTime(-1);
		setEndTime(endTime);
		setPriority(priority);
		setReminderDate(reminderDate);
		setReminder(reminder);
		setDone(isDone);
		setSubTask(new HashMap<String, SubTask> ());
	}

	// Event
	public Task(String taskId, String eventName, String eventDescription, String startDate, 
			String endDate, long startTime, long endTime, int priority, 
			String reminderDate, long reminder, String categoryName) {
		setTaskId(taskId);
		setName(eventName);
		setDescription(eventDescription);
		setStartDate(startDate);
		setEndDate(endDate);
		setStartTime(startTime);
		setEndTime(endTime);
		setPriority(priority);
		setReminderDate(reminderDate);
		setReminder(reminder);
		setSubTask(new HashMap<String, SubTask> ());
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
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

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getReminder() {
		return reminderTime;
	}

	public void setReminder(long reminderTime) {
		this.reminderTime = reminderTime;
	}
	
	public HashMap<String, SubTask> getSubTask() {
		return subTask;
	}

	public void setSubTask(HashMap<String, SubTask> subTask) {
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
			sb.append(reminderDate + System.getProperty("line.separator"));
		}
		sb.append("is it completed? "+isDone + System.getProperty("line.separator"));
		return sb.toString();
	}

	public static Comparator<Task> sortPriority = new Comparator<Task> () {
		public int compare(Task left, Task right) {
			if(left.getPriority() < right.getPriority()) {
				return -1;
			} else if(left.getPriority() > right.getPriority()) {
				return 1;
			} else {
				return 0;
			}
		}
	};
	
	public static Comparator<Task> sortDeadline = new Comparator<Task> () {
		public int compare(Task left, Task right) {
			if(left.getEndTime() < right.getEndTime()) {
				return -1;
			} else if(left.getEndTime() > right.getEndTime()) {
				return 1;
			} else {
				return 0;
			}
		}
	};
	
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