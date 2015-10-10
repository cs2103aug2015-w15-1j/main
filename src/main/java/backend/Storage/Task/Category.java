package main.java.backend.Storage.Task;

import java.util.TreeMap;

public class Category implements Comparable<Category> {
	
	/* The following represents the list of task types 
	 * where hash map key represents the category name
	 * and value represents tasks under each category.
	 */
	private TreeMap<Integer, Task> tasks;
	private TreeMap<Integer, Task> floatTasks;
	private TreeMap<Integer, Task> events;
	
	private String categoryName;
	private String categoryColour;
	
	public Category() {
		setTasks(new TreeMap<Integer, Task>());
		setFloatTasks(new TreeMap<Integer, Task>());
		setEvents(new TreeMap<Integer, Task>());
	}
	
	public Category(String categoryName) {
		setCategoryName(categoryName);
		setTasks(new TreeMap<Integer, Task>());
		setFloatTasks(new TreeMap<Integer, Task>());
		setEvents(new TreeMap<Integer, Task>());
	}
	
	public Category(String categoryName, String categoryColour) {
		setCategoryName(categoryName);
		setCategoryColour(categoryColour);
		setTasks(new TreeMap<Integer, Task>());
		setFloatTasks(new TreeMap<Integer, Task>());
		setEvents(new TreeMap<Integer, Task>());
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

	public TreeMap<Integer, Task> getTasks() {
		return tasks;
	}

	public void setTasks(TreeMap<Integer, Task> tasks) {
		this.tasks = tasks;
	}

	public TreeMap<Integer, Task> getFloatTasks() {
		return floatTasks;
	}

	public void setFloatTasks(TreeMap<Integer, Task> floatTasks) {
		this.floatTasks = floatTasks;
	}

	public TreeMap<Integer, Task> getEvents() {
		return events;
	}

	public void setEvents(TreeMap<Integer, Task> events) {
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