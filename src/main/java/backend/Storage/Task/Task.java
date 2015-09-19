package main.java.backend.Storage.Task;

public class Task {
	private int priority;
	
	private boolean done;
	
	private String name;
	private String description;
    private String startDate;
    private String endDate;
    
    private long startDateMilliseconds;
    private long endDateMilliseconds;
    private long remind;
    
    public Task() {
    	
    }
    
    public Task(String taskName, String taskDescription, String priority2, String reminder, String category) {
		// TODO Auto-generated constructor stub
	}

	public Task(String eventName, String eventDescription, String startDate2, String endDate2, String startTime,
			String endTime, String priority2, String reminder, String category) {
		// TODO Auto-generated constructor stub
	}

	public Task(String taskName, String taskDescription, String deadline, String priority2, String reminder,
			String category) {
		// TODO Auto-generated constructor stub
	}

	public int getPriority() {
    	return priority;
    }
    
    public void setPriority(int priority) {
    	this.priority = priority;
    }
    
    public boolean isDone() {
    	return done;
    }
    
    public void setDone(boolean done) {
    	this.done = done;
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
    
    public void setEndDate(String endDate) {
    	this.endDate = endDate;
    }
    
    public long getStartDateMilliseconds() {
    	return startDateMilliseconds;
    }
    
    public void setStartDateMilliseconds(long startDateMilliseconds) {
    	this.startDateMilliseconds = startDateMilliseconds;
    }
    
    public long getEndDateMilliseconds() {
    	return endDateMilliseconds;
    }
    
    public void setEndDateMilliseconds(long endDateMilliseconds) {
    	this.endDateMilliseconds = endDateMilliseconds;
    }
    
    public long getRemind() {
    	return remind;
    }
    
    public void setRemind(long remind) {
    	this.remind = remind;
    }
    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(name+System.getProperty("line.separator"));
    	sb.append(description+System.getProperty("line.separator"));
    	sb.append(startDate+System.getProperty("line.separator"));
    	sb.append(endDate+System.getProperty("line.separator"));
    	sb.append(startDateMilliseconds+System.getProperty("line.separator"));
    	sb.append(endDateMilliseconds+System.getProperty("line.separator"));
    	sb.append(remind+System.getProperty("line.separator"));
    	sb.append(priority+System.getProperty("line.separator"));
    	sb.append(done+System.getProperty("line.separator"));
    	return sb.toString();
    }
}
