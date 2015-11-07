//@@author A0126258A
package main.java.backend.Storage.Task;

import java.util.TreeMap;

import main.java.backend.Logic.Constant;

public class Task implements Comparable<Task> {
	
	public enum TaskType {
		FLOATING, TODO, EVENT;
	}
	
	public enum RecurrenceType {
		NONE, DAY, WEEK, MONTH, YEAR;
	}
	
	private TaskType taskType;
	private RecurrenceType recurrenceType;
	private int recurrenceFrequency;
	private int taskId;
	private int indexForPrinting;
	private int priority;
	
	private boolean isDone;
	private boolean isRecurred;
	
	private String name;
	private String description;
	private String start;
	private String end;
	private String reminder;

//	private TreeMap<String, SubTask> subTask;
	
	public Task () {
		
	}
	
	public Task(Task task) {
		this(task.getTaskType(), task.getRecurrenceType(), task.getRecurrenceFrequency(),
				task.getPriority(), task.getName(), task.getDescription(), task.getStart(), 
				task.getEnd(), task.getReminder());
	}
	
	public Task(TaskType taskType, RecurrenceType recurrenceType, int recurrenceFrequency, 
			int priority, String name, String description, 
			String start, String end, String reminder) {
		
		assert taskType != null;
		assert name != null;
		
		this.taskType = taskType;
		this.recurrenceType = recurrenceType;
		this.recurrenceFrequency = recurrenceFrequency;
		this.priority = priority;
		this.isDone = false;
		this.isRecurred = false;
		this.name = name;
		this.description = description;
		this.start = start;
		this.end = end;
		this.reminder = reminder;
//		this.subTask = new TreeMap<String, SubTask> ();
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
	
	public int getRecurrenceFrequency() {
		return recurrenceFrequency;
	}

	public void setRecurrenceFrequency(int recurrenceFrequency) {
		this.recurrenceFrequency = recurrenceFrequency;
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
	
	public boolean isRecurred() {
		return isRecurred;
	}

	public void setRecurred(boolean isRecurred) {
		this.isRecurred = isRecurred;
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
	
//	public TreeMap<String, SubTask> getSubTask() {
//		return subTask;
//	}
//
//	public void setSubTask(TreeMap<String, SubTask> subTask) {
//		this.subTask = subTask;
//	}

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
			sb.append("Reminder has been set." + System.getProperty("line.separator"));
		}
		if(!recurrenceType.equals(recurrenceType.NONE)) {
			
			String plural = "";
			
			if(recurrenceFrequency > 1) {
				plural = "s";
			}
			
			sb.append("Recurring every: " + recurrenceFrequency 
					+ " " + recurrenceType.toString().toLowerCase() + plural
					+ System.getProperty("line.separator"));
		}
		return sb.toString();
	}
	
	public String printFull() {
		StringBuilder sb = new StringBuilder();
		//sb.append(indexForPrinting+". ");
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
			sb.append("Description: "+description + System.getProperty("line.separator"));
		}
		if(start !=""){
			sb.append("From: "+start + System.getProperty("line.separator"));
		}
		if (end!=""){
			if(start!=""){
				sb.append ("To: ");
			}
			else{
				sb.append("Deadline: ");
			}
			sb.append(end + System.getProperty("line.separator"));
		}
		if (reminder!=""){
			sb.append("Reminder: "+ reminder + System.getProperty("line.separator"));
		}
		
		if(!recurrenceType.equals(recurrenceType.NONE)) {
			
			String plural = "";
			
			if(recurrenceFrequency > 1) {
				plural = "s";
			}
			
			sb.append("Recurring every: " + recurrenceFrequency 
					+ " " + recurrenceType.toString().toLowerCase() + plural
					+ System.getProperty("line.separator"));
		}
		
		return sb.toString();
	}
	
	public String reminderPrint() {
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
		if(start !=""){
			sb.append("From: "+start + System.getProperty("line.separator"));
		}
		if (end!=""){
			if(start!=""){
				sb.append ("To: ");
			}
			else{
				sb.append("Deadline: ");
			}
			sb.append(end + System.getProperty("line.separator"));
		}
			
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (!(o instanceof Task)) {
			return false;
		}
		
		Task task = (Task) o;
		return this.taskType.equals(task.getTaskType()) 
				&& this.recurrenceType.equals(task.getRecurrenceType())
				&& this.recurrenceFrequency == task.getRecurrenceFrequency()
				&& this.taskId == task.getTaskId()
				&& this.indexForPrinting == task.getIndex()
				&& this.priority == task.getPriority()
				&& this.isDone == task.getDone()
				&& this.name.equals(task.getName())
				&& this.description.equals(task.getDescription())
				&& this.start.equals(task.getStart())
				&& this.end.equals(task.getEnd())
				&& this.reminder.equals(task.getReminder());
	}
	
	@Override
	public int compareTo(Task o) {
		if(Constant.stringToMillisecond(getStart())
				< Constant.stringToMillisecond(o.getStart())) {
			return -1;
		} else if(Constant.stringToMillisecond(getStart()) 
				> Constant.stringToMillisecond(o.getStart())) {
			return 1;
		} else if(Constant.stringToMillisecond(getEnd())
				< Constant.stringToMillisecond(o.getEnd())) {
			return -1;
		} else if(Constant.stringToMillisecond(getEnd()) 
				> Constant.stringToMillisecond(o.getEnd())) {
			return 1;
		} else {
			return 0;
		}
	}

}