package main.java.backend.Storage;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.CategoryWrapper;
import main.java.backend.Storage.Task.Task;

public abstract class Storage {
	
	public abstract void addFloatingTask(String taskName, String taskDescription, 
			int priority, long reminder, String category, boolean done)
					throws IOException, JSONException;

	public abstract void addTask(String taskName, String taskDescription, String deadline, long endTime, int priority, 
			int reminder, String category, boolean done) throws IOException, JSONException;

	public abstract void addEvent(String eventName, String eventDescription, String startDate, 
			String endDate, long startDateMilliseconds, long endDateMilliseconds, int priority, 
			long reminder, String category) throws IOException, JSONException;

	public abstract void addCategory(String categoryName) 
			throws JsonParseException, JsonMappingException, IOException, JSONException;

	public abstract void setCategoryColour(String categoryName, String colourId) 
			throws JsonParseException, JsonMappingException, IOException;

	// TODO: Not completed
	public abstract void setCategory(String taskId, String categoryName);

	public abstract void setUndone(String taskId) 
			throws JsonParseException, JsonMappingException, JSONException, IOException;

	public abstract void setDone(String taskId) 
			throws JsonParseException, JsonMappingException, IOException;

	public abstract void setReminder(String taskId, long reminder) 
			throws JsonParseException, JsonMappingException, IOException;

	public abstract void setDescription(String taskId, String description) 
			throws JsonParseException, JsonMappingException, IOException;

	public abstract void setDeadline(String taskId, long deadline) 
			throws JsonParseException, JsonMappingException, IOException;

	// TODO: Not completed
	public abstract void addSubTask(String taskId, String subtaskDescription);

	public abstract void deleteAll(String categoryName) throws IOException;

	// TODO: Not completed
	public abstract void deleteCategory(String categoryName);

	// TODO: Not completed
	public abstract void deleteTaskTypeFromCategory(String categoryName);

	// TODO: Not completed
	public abstract void deleteTaskFromCategory(String categoryName);

	/**
	 * This operation retrieves all categories from the file.
	 */
	public abstract ArrayList<Category> getCategoryList()
			throws JsonParseException, JsonMappingException, JSONException, IOException;
	
	/**
	 * This operation retrieves all tasks from the file.
	 */
	public abstract ArrayList<Task> getTaskList()
			throws JsonParseException, JsonMappingException, JSONException, IOException;
	
	/**
	 * This operation retrieves the tasks under target category.
	 * 
	 */
	public abstract ArrayList<Task> getCategoryAllTasks(String categoryName) 
			throws ParseException, IOException, JSONException;
	
	/**
	 * This operation retrieves the tasks of a specific type
	 * (either task, floating task or event) under target category.
	 * 
	 */
	public abstract ArrayList<Task> getCategoryTasks(String categoryName) 
			throws ParseException, IOException, JSONException;
	
	public abstract ArrayList<Task> getCategoryFloatingTasks(String categoryName) 
			throws ParseException, IOException, JSONException;
	
	public abstract ArrayList<Task> getCategoryEvents(String categoryName) 
			throws ParseException, IOException, JSONException;
	
	public abstract ArrayList<Task> getTasks() 
			throws IOException, JSONException, ParseException;
	
	public abstract ArrayList<Task> getFloatingTasks() 
			throws IOException, JSONException, ParseException;
	
	public abstract ArrayList<Task> getEvents() 
			throws IOException, JSONException, ParseException;
}