package Task;

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
}
