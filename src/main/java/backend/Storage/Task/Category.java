package main.java.backend.Storage.Task;

import java.util.HashMap;

public class Category {
	
	/* The following represents the list of task types 
	 * where hash map key represents the category name
	 * and value represents tasks under each category.
	 */
	private HashMap<Long, Task> tasks;
	private HashMap<Long, Task> floatTasks;
	private HashMap<Long, Task> events;
	
	private String categoryName;
	private String categoryColour;
	
	public Category() {
		setTasks(new HashMap<Long, Task>());
		setFloatTasks(new HashMap<Long, Task>());
		setEvents(new HashMap<Long, Task>());
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

	public HashMap<Long, Task> getTasks() {
		return tasks;
	}

	public void setTasks(HashMap<Long, Task> tasks) {
		this.tasks = tasks;
	}

	public HashMap<Long, Task> getFloatTasks() {
		return floatTasks;
	}

	public void setFloatTasks(HashMap<Long, Task> floatTasks) {
		this.floatTasks = floatTasks;
	}

	public HashMap<Long, Task> getEvents() {
		return events;
	}

	public void setEvents(HashMap<Long, Task> events) {
		this.events = events;
	}
}
