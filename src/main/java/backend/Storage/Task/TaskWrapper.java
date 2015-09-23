package main.java.backend.Storage.Task;

import java.util.HashMap;

public class TaskWrapper {
	
	/* The following represents each individual task 
	 * where hash map key represents the unique id of task
	 * and value represents tasks belonging to unique id.
	 */
	private HashMap<String, Task> task;
	
	public TaskWrapper() {
		setTask(new HashMap<String, Task> ());
	}

	public HashMap<String, Task> getTask() {
		return task;
	}

	public void setTask(HashMap<String, Task> task) {
		this.task = task;
	}
}
