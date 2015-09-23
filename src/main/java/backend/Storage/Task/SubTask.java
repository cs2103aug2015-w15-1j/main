package main.java.backend.Storage.Task;

public class SubTask {
	
	private String subTaskId;
	private String description;
	private boolean isDone;
	
	public SubTask() {
		
	}
	
	public SubTask(String subTaskId, String description, boolean isDone) {
		setSubTaskId(subTaskId);
		setDescription(description);
		setDone(isDone);
	}
	
	public String getSubTaskId() {
		return subTaskId;
	}
	
	public void setSubTaskId(String subTaskId) {
		this.subTaskId = subTaskId;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}
}
