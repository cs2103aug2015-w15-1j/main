package main.java.backend.Storage.Task;

import java.util.HashMap;

public class Category implements Comparable<Category> {
	
	/* The following represents the list of task types 
	 * where hash map key represents the category name
	 * and value represents tasks under each category.
	 */
	private HashMap<Integer, Task> tasks;
	private HashMap<Integer, Task> floatTasks;
	private HashMap<Integer, Task> events;
	
	private String categoryName;
	private String categoryColour;
	
	public Category() {
		setTasks(new HashMap<Integer, Task>());
		setFloatTasks(new HashMap<Integer, Task>());
		setEvents(new HashMap<Integer, Task>());
	}
	
	public Category(String categoryName) {
		setCategoryName(categoryName);
		setTasks(new HashMap<Integer, Task>());
		setFloatTasks(new HashMap<Integer, Task>());
		setEvents(new HashMap<Integer, Task>());
	}
	
	public Category(String categoryName, String categoryColour) {
		setCategoryName(categoryName);
		setCategoryColour(categoryColour);
		setTasks(new HashMap<Integer, Task>());
		setFloatTasks(new HashMap<Integer, Task>());
		setEvents(new HashMap<Integer, Task>());
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

	public HashMap<Integer, Task> getTasks() {
		return tasks;
	}

	public void setTasks(HashMap<Integer, Task> tasks) {
		this.tasks = tasks;
	}

	public HashMap<Integer, Task> getFloatTasks() {
		return floatTasks;
	}

	public void setFloatTasks(HashMap<Integer, Task> floatTasks) {
		this.floatTasks = floatTasks;
	}

	public HashMap<Integer, Task> getEvents() {
		return events;
	}

	public void setEvents(HashMap<Integer, Task> events) {
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