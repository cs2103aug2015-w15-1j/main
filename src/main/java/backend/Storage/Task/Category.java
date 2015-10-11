package main.java.backend.Storage.Task;

import java.util.TreeMap;

public class Category implements Comparable<Category> {
	
	/* The following represents the list of task types 
	 * where hash map key represents the category name
	 * and value represents tasks under each category.
	 */
	private TreeMap<String, Task> tasks;
	private TreeMap<String, Task> floatTasks;
	private TreeMap<String, Task> events;
	
	private String categoryName;
	private String categoryColour;
	
	public Category() {
		setTasks(new TreeMap<String, Task>());
		setFloatTasks(new TreeMap<String, Task>());
		setEvents(new TreeMap<String, Task>());
	}
	
	public Category(String categoryName) {
		setCategoryName(categoryName);
		setTasks(new TreeMap<String, Task>());
		setFloatTasks(new TreeMap<String, Task>());
		setEvents(new TreeMap<String, Task>());
	}
	
	public Category(String categoryName, String categoryColour) {
		setCategoryName(categoryName);
		setCategoryColour(categoryColour);
		setTasks(new TreeMap<String, Task>());
		setFloatTasks(new TreeMap<String, Task>());
		setEvents(new TreeMap<String, Task>());
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

	public TreeMap<String, Task> getTasks() {
		return tasks;
	}

	public void setTasks(TreeMap<String, Task> tasks) {
		this.tasks = tasks;
	}

	public TreeMap<String, Task> getFloatTasks() {
		return floatTasks;
	}

	public void setFloatTasks(TreeMap<String, Task> floatTasks) {
		this.floatTasks = floatTasks;
	}

	public TreeMap<String, Task> getEvents() {
		return events;
	}

	public void setEvents(TreeMap<String, Task> events) {
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