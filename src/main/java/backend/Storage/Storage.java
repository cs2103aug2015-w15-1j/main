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

// Change taskID to taskName
// Do all CRUD
// Test
// Logic - convert string to long for date

public abstract class Storage {
	
	public abstract void addFloatingTask(String taskName, String taskDescription, 
			int priority, long reminder, String category)
					throws JsonParseException, JsonMappingException, IOException, JSONException;

	public abstract void addTask(String taskName, String taskDescription, 
			String deadline, long endTime, int priority, long reminder, String category) 
					throws IOException, JSONException;

	public abstract void addEvent(String eventName, String eventDescription, String startDate, 
			String endDate, long startDateMilliseconds, long endDateMilliseconds, int priority, 
			long reminder, String category) throws IOException, JSONException;
	
	public abstract void addSubTask(String taskName, String subtaskDescription) 
			throws JsonParseException, JsonMappingException, IOException;

	public abstract CategoryWrapper addCategory(String categoryName) 
			throws JsonParseException, JsonMappingException, IOException, JSONException;

	public abstract void setCategoryColour(String categoryName, String colourId) 
			throws JsonParseException, JsonMappingException, IOException;

	// TODO: Not completed - note: set a default category if category is null
	public abstract void setCategory(String taskName, String categoryName);

	public abstract void setUndone(String taskName) 
			throws JsonParseException, JsonMappingException, JSONException, IOException;

	public abstract void setDone(String taskName) 
			throws JsonParseException, JsonMappingException, IOException;

	// long reminder should be string
	public abstract void setReminder(String taskName, long reminder) 
			throws JsonParseException, JsonMappingException, IOException;

	public abstract void setDescription(String taskName, String description) 
			throws JsonParseException, JsonMappingException, IOException;

	public abstract void setDeadline(String taskName, long deadline) 
			throws JsonParseException, JsonMappingException, IOException;
	
	// TODO: Not completed
	public abstract void setSubTaskUndone(String taskName) 
			throws JsonParseException, JsonMappingException, JSONException, IOException;

	// TODO: Not completed
	public abstract void setSubTaskDone(String taskName) 
			throws JsonParseException, JsonMappingException, IOException;
	
	// TODO: Not completed
	public abstract void setSubtaskDescription(String taskName, String description) 
			throws JsonParseException, JsonMappingException, IOException;
	
	// TODO: Not completed
	public abstract void deleteSubTask(String subtaskName, String subtaskDescription);

	public abstract void deleteAll() throws IOException;

	// TODO: Not completed
	public abstract void deleteCategory(String categoryName);

	// TODO: Not completed
	public abstract void deleteTaskTypeFromCategory(String categoryName, String taskType);
	
	// TODO: Not completed
	public abstract void deleteTaskFromCategory(String categoryName, String taskName);

	// TODO: Not completed
	public abstract void deleteTask(String taskName);
	
	public abstract ArrayList<String> getCategories();

	public abstract ArrayList<Category> getCategoryList()
			throws JsonParseException, JsonMappingException, JSONException, IOException;
	
	public abstract ArrayList<Task> getTaskList()
			throws JsonParseException, JsonMappingException, JSONException, IOException;
	
	public abstract ArrayList<Task> getCategoryAllTasks(String categoryName) 
			throws ParseException, IOException, JSONException;
	
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