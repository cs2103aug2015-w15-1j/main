package main.java.backend.Storage.Task;

import java.util.HashMap;

public class Category implements Comparable<Category> {
	
	/* The following represents the list of task types 
	 * where hash map key represents the category name
	 * and value represents tasks under each category.
	 */
	private HashMap<String, Task> tasks;
	private HashMap<String, Task> floatTasks;
	private HashMap<String, Task> events;
	
	private String categoryName;
	private String categoryColour;
	
	public Category() {
		setTasks(new HashMap<String, Task>());
		setFloatTasks(new HashMap<String, Task>());
		setEvents(new HashMap<String, Task>());
	}
	
	public Category(String categoryName) {
		setCategoryName(categoryName);
		setTasks(new HashMap<String, Task>());
		setFloatTasks(new HashMap<String, Task>());
		setEvents(new HashMap<String, Task>());
	}
	
	public Category(String categoryName, String categoryColour) {
		setCategoryName(categoryName);
		setCategoryColour(categoryColour);
		setTasks(new HashMap<String, Task>());
		setFloatTasks(new HashMap<String, Task>());
		setEvents(new HashMap<String, Task>());
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

	@Override
	public int compareTo(Category o) {
		
		if(this.categoryName.compareTo(o.categoryName) < 0) {
			return -1;
		} else if(this.categoryName.compareTo(o.categoryName) > 0) {
			return 1;
		} else {
			return 0;
		}
	}
}
