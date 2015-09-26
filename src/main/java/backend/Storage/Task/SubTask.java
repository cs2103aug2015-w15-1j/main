package main.java.backend.Storage.Task;

public class SubTask {
	
	private String description;
	private boolean isDone;
	
	public SubTask() {
		
	}
	
	public SubTask(String description, boolean isDone) {
		setDescription(description);
		setDone(isDone);
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
