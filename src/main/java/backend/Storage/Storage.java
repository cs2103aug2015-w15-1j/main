package main.java.backend.Storage;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import main.java.backend.Storage.Task.CategoryWrapper;
import main.java.backend.Storage.Task.Task;

public abstract class Storage {
	
	public abstract void addFloatingTask (String taskName, String taskDescription, 
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

	public abstract void setUndone(String categoryName, String taskName) 
			throws JsonParseException, JsonMappingException, JSONException, IOException;

	public abstract void setDone(String categoryName, String taskName) 
			throws JsonParseException, JsonMappingException, IOException;

	public abstract void setReminder(String categoryName, String taskName, long reminder) 
			throws JsonParseException, JsonMappingException, IOException;

	public abstract void setDescription(String categoryName, String taskName, String description) 
			throws JsonParseException, JsonMappingException, IOException;

	public abstract void setDeadline(String categoryName, String taskName, long deadline) 
			throws JsonParseException, JsonMappingException, IOException;

	// TODO: Not completed
	public abstract void addSubTask(String taskId, String subtaskDescription);

	// TODO: Not completed
	public abstract void searchTask(String taskName);

	public abstract void deleteAll(String categoryName) throws IOException;

	// TODO: Not completed
	public abstract void deleteCategory(String categoryName);

	// TODO: Not completed
	public abstract void deleteTaskTypeFromCategory(String categoryName);

	// TODO: Not completed
	public abstract void deleteTaskFromCategory(String categoryName);

	/**
	 * This operation retrieves all tasks from the file.
	 * TODO: change CategoryWrapper to task 
	 */
	public abstract ArrayList<CategoryWrapper> getCategoryList()
			throws JsonParseException, JsonMappingException, JSONException, IOException;
	
	/**
	 * This operation retrieves the tasks under target category.
	 * 
	 */
	public abstract ArrayList<Task> getCategoryTaskList(String categoryName) 
			throws ParseException, IOException, JSONException;
	
	/**
	 * This operation retrieves the tasks of a specific type
	 * (either task, floating task or event) under target category.
	 * 
	 */
	public abstract ArrayList<Task> getCategoryTaskTypeList(String categoryName, String taskType) 
			throws ParseException, IOException, JSONException;
	
	public abstract ArrayList<Task> getTasks() 
			throws IOException, JSONException, ParseException;
	
	public abstract ArrayList<Task> getFloatingTasks() 
			throws IOException, JSONException, ParseException;
	
	public abstract ArrayList<Task> getEvents() 
			throws IOException, JSONException, ParseException;
}