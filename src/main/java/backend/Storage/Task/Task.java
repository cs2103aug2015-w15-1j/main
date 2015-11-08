//@@author A0126258A
package main.java.backend.Storage.Task;

import main.java.backend.Logic.Constant;

public class Task implements Comparable<Task> {
	
	public enum TaskType {
		FLOATING, TODO, EVENT;
	}
	
	public enum RecurrenceType {
		NONE, DAY, WEEK, MONTH, YEAR;
	}
	
	private static final String PRINT_PRIORITY_LEVEL = "*";
	private static final String PRINT_DESCRIPTION = "Description: ";
	private static final String PRINT_DEADLINE = "Deadline: ";
	private static final String PRINT_FROM = "From: ";
	private static final String PRINT_TO = "To: ";
	private static final String PRINT_REMINDER = "Reminder: ";
	private static final String PRINT_RECURRING = "Recurring every: ";
	private static final String PRINT_EMPTY = "";
	private static final String PRINT_SPACE = " ";
	private static final String PRINT_DOT = ". ";
	private static final String PRINT_PLURAL = "s";
	private static final String PRINT_NEW_LINE = System.getProperty("line.separator");
	
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
	
	public String printFull() {
		return toString();
	}
	
	public String reminderPrint() {
		StringBuilder sb = new StringBuilder();
		
		if(!name.isEmpty()){
			sb.append(name + PRINT_SPACE);
		}
		
		if(priority !=-1){
			for (int i = 0; i < priority; i++){
				sb.append(PRINT_PRIORITY_LEVEL);
			}
			sb.append(PRINT_NEW_LINE);
		}
		
		if(!start.isEmpty()){
			sb.append(PRINT_FROM + start + PRINT_NEW_LINE);
		}
		
		if (!end.isEmpty()){
			if(!start.isEmpty()){
				sb.append (PRINT_TO);
			}
			else{
				sb.append(PRINT_DEADLINE);
			}
			sb.append(end + PRINT_NEW_LINE);
		}
			
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if(indexForPrinting != -1) {
			sb.append(indexForPrinting + PRINT_DOT);
		}
		
		if(!name.isEmpty()){
			sb.append(name + PRINT_SPACE);
		}
		
		if(priority !=-1){
			for (int i = 0; i < priority; i++){
				sb.append(PRINT_PRIORITY_LEVEL);
			}
		}
		
		sb.append(PRINT_NEW_LINE);
		
		if (!description.isEmpty()){
			sb.append(PRINT_DESCRIPTION + description + PRINT_NEW_LINE);
		}
		
		if(!start.isEmpty()){
			sb.append(PRINT_FROM + start + PRINT_NEW_LINE);
		}
		
		if (!end.isEmpty()){
			if(!start.isEmpty()){
				sb.append (PRINT_TO);
			}
			else{
				sb.append(PRINT_DEADLINE);
			}
			sb.append(end + PRINT_NEW_LINE);
		}
		
		if (!reminder.isEmpty()){
			sb.append(PRINT_REMINDER + reminder + PRINT_NEW_LINE);
		}
		
		if(!recurrenceType.equals(RecurrenceType.NONE)) {
			
			String plural = PRINT_EMPTY;
			
			if(recurrenceFrequency > 1) {
				plural = PRINT_PLURAL;
			}
			
			sb.append(PRINT_RECURRING + recurrenceFrequency 
					+ PRINT_SPACE + recurrenceType.toString().toLowerCase() 
					+ plural + PRINT_NEW_LINE);
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