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

// Test

public abstract class Storage {

	/****************************************************************************
	 * 									CREATE
	 ***************************************************************************/

	public abstract CategoryWrapper addCategory(String categoryName) 
			throws JsonParseException, JsonMappingException, IOException;

	
	public abstract String addFloatingTask(String taskName, String taskDescription, int priority, 
			String reminderDate, long reminder, String category) throws JsonParseException, 
			JsonMappingException, IOException, JSONException;

	
	public abstract String addTask(String taskName, String taskDescription, String deadline, 
			long endTime, int priority, String reminderDate, long reminder, String category) 
					throws IOException, JSONException;

	
	public abstract String addEvent(String eventName, String eventDescription, String startDate, 
			String endDate, long startDateMilliseconds, long endDateMilliseconds, int priority, 
			String reminderDate, long reminder, String category) throws IOException, JSONException;
	
	
	public abstract void addSubTask(String taskName, String subtaskDescription) 
			throws JsonParseException, JsonMappingException, IOException;
	

	/****************************************************************************
	 * 									READ
	 ***************************************************************************/

	public abstract ArrayList<String> getCategories();

	public abstract ArrayList<Category> getCategoryList() throws IOException;

	public abstract ArrayList<Task> getTaskList();

	public abstract ArrayList<Task> getCategoryAllTasks(String categoryName) 
			throws ParseException, IOException, JSONException;

	public abstract ArrayList<Task> getCategoryTasks(String categoryName) 
			throws IOException, JSONException, ParseException;

	public abstract ArrayList<Task> getCategoryFloatingTasks(String categoryName) 
			throws IOException, JSONException, ParseException;

	public abstract ArrayList<Task> getCategoryEvents(String categoryName) 
			throws IOException, JSONException, ParseException;

	public abstract ArrayList<Task> getTasks() 
			throws IOException, JSONException, ParseException;

	public abstract ArrayList<Task> getFloatingTasks() 
			throws IOException, JSONException, ParseException;

	public abstract ArrayList<Task> getEvents() 
			throws IOException, JSONException, ParseException;
	
	public abstract ArrayList<Task> getCompletedTasks();

	public abstract ArrayList<Task> getOverdueTasks();
	
	/****************************************************************************
	 * 									UPDATE
	 ***************************************************************************/

	public abstract void setCategoryColour(String categoryName, String colourId) 
			throws JsonParseException, JsonMappingException, IOException;
	
	// TODO
	public abstract void setCategory(String taskName, String categoryName);

	public abstract void setUndone(String taskName) throws JsonParseException, 
			JsonMappingException, JSONException, IOException;

	public abstract void setDone(String taskName) 
			throws JsonParseException, JsonMappingException, IOException;

	public abstract void setReminder(String taskName, String reminder) 
			throws JsonParseException, JsonMappingException, IOException;
	
	public abstract void setPriority(String taskName, int priority) throws IOException;

	public abstract void setDescription(String taskName, String description) 
			throws JsonParseException, JsonMappingException, IOException;

	public abstract void setDeadline(String taskName, long deadline) 
			throws JsonParseException, JsonMappingException, IOException;

	// TODO
	public abstract void setSubTaskUndone(String taskName) 
			throws JsonParseException, JsonMappingException, JSONException, IOException;

	// TODO
	public abstract void setSubTaskDone(String taskName) 
			throws JsonParseException, JsonMappingException, IOException;

	// TODO
	public abstract void setSubtaskDescription(String taskName, String description) 
			throws JsonParseException, JsonMappingException, IOException;
	

	/****************************************************************************
	 * 									DELETE
	 ***************************************************************************/
	
	public abstract void deleteAll() throws IOException;

	public abstract void deleteCategory(String categoryName) throws IOException;

	public abstract void deleteTaskTypeFromCategory(String categoryName, 
			String taskType) throws IOException;

	public abstract void deleteTask(String taskName) throws IOException;

	// TODO
	public abstract void deleteSubTask(String taskName, String subtaskDescription);
	
	/****************************************************************************
	 * 								GOOD METHODS
	 ***************************************************************************/
	
	public abstract void updateFile(ArrayList<Category> categories) throws IOException;
	
	public abstract void exitProgram() throws IOException;
}