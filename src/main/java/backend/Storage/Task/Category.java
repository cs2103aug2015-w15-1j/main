package main.java.backend.Storage.Task;

import java.util.HashMap;

public class Category {
	
	/* The following represents the list of task types */
	private HashMap<String, Task> tasks;
	private HashMap<String, Task> floatTasks;
	private HashMap<String, Task> events;
	
	private String categoryColour;
	
	public Category() {
		setTasks(new HashMap<String, Task>());
		setFloatTasks(new HashMap<String, Task>());
		setEvents(new HashMap<String, Task>());
	}

	public String getCategoryColour() {
		return categoryColour;
	}

	public void setCategoryColour(String categoryColour) {
		this.categoryColour = categoryColour;
	}

	public HashMap<String, Task> getTasks() {
		return tasks;
	}

	public void setTasks(HashMap<String, Task> tasks) {
		this.tasks = tasks;
	}

	public HashMap<String, Task> getFloatTasks() {
		return floatTasks;
	}

	public void setFloatTasks(HashMap<String, Task> floatTasks) {
		this.floatTasks = floatTasks;
	}

	public HashMap<String, Task> getEvents() {
		return events;
	}

	public void setEvents(HashMap<String, Task> events) {
		this.events = events;
	}
}
