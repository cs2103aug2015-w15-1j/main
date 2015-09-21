package main.java.backend.Storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import main.java.backend.Storage.Task.CategoryWrapper;
import main.java.backend.Storage.Task.Task;

public class StorageStub extends Storage {
	
	private static final String TYPE_TASK = "task";
	private static final String TYPE_FLOAT = "floatTask";
	private static final String TYPE_EVENT = "event";
	
	private StorageData storageData;
	private StorageFile storageFile;

	public StorageStub () throws FileNotFoundException, IOException {
		storageData = new StorageData();
		storageFile = new StorageFile();
	}
	
	public void addFloatingTask(String taskName, String taskDescription, int priority, long reminder,
			String category, boolean done) throws IOException, JSONException {
		
		Task newFloatingTask = new Task(taskName, taskDescription, priority, reminder, done);
		storageData.addNewTask(category, TYPE_FLOAT, newFloatingTask);
	}
	
	public void addTask(String taskName, String taskDescription, String deadline, long endTime, int priority, 
			int reminder, String category, boolean done) throws IOException, JSONException {	
		
		Task newTask = new Task(taskName, taskDescription, deadline, endTime, priority, reminder, done);
		storageData.addNewTask(category, TYPE_TASK, newTask);
	}

	public void addEvent(String eventName, String eventDescription, String startDate, 
			String endDate, long startDateMilliseconds, long endDateMilliseconds, int priority, 
			long reminder, String category) throws IOException, JSONException {
		
		Task newEvent = new Task(eventName, eventDescription, startDate, endDate, startDateMilliseconds,
				endDateMilliseconds, priority, reminder, category);
		storageData.addNewTask(category, TYPE_EVENT, newEvent);
	}
	
	public void addCategory(String categoryName) 
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		
		storageData.addNewCategory(categoryName);
	}
	
	public void setCategoryColour(String categoryName, String colourId) 
			throws JsonParseException, JsonMappingException, IOException {
		
		storageData.setCategoryColour(categoryName, colourId);
	}

	public void setCategory(String taskId, String categoryName) {
		// TODO Auto-generated method stub
		
	}
	
	public void setUndone(String categoryName, String taskName) 
			throws JsonParseException, JsonMappingException, JSONException, IOException {
		
		storageData.setDone(categoryName, taskName, false);
	}

	public void setDone(String categoryName, String taskName) 
			throws JsonParseException, JsonMappingException, IOException {
		
		storageData.setDone(categoryName, taskName, true);
	}

	public void setReminder(String categoryName, String taskName, long reminder) 
			throws JsonParseException, JsonMappingException, IOException {
		
		storageData.setReminder(categoryName, taskName, reminder);
	}

	public void setDescription(String categoryName, String taskName, String description) 
			throws JsonParseException, JsonMappingException, IOException {
		
		storageData.setDescription(categoryName, taskName, description);
	}

	public void setDeadline(String categoryName, String taskName, long deadline) 
			throws JsonParseException, JsonMappingException, IOException {
		
		storageData.setDeadline(categoryName, taskName, deadline);
	}
	
	public void addSubTask(String taskId, String subtaskDescription) {
		// TODO Auto-generated method stub
		
	}
	
	public void searchTask(String taskName) {
		
	}
	
	public void deleteAll(String categoryName) throws IOException {
		
		storageFile.clearTextFromFile();
	}
	
	public void deleteCategory(String categoryName) {
		// TODO Auto-generated method stub
		
	}
	
	public void deleteTaskTypeFromCategory(String categoryName) {
		// TODO Auto-generated method stub
		
	}
	
	public void deleteTaskFromCategory(String categoryName) {
		// TODO Auto-generated method stub
		
	}
	
	public ArrayList<CategoryWrapper> getAllCategories()
			throws JsonParseException, JsonMappingException, JSONException, IOException {
		
		return storageData.getAllCategories();
	}
	
	public ArrayList<Task> getAllTargetCategoryTasks(String categoryName, String taskType) 
			throws IOException, JSONException, ParseException {

		return storageData.getAllTargetCategoryTasks(categoryName, taskType);
	}
	
	public ArrayList<Task> getAllTargetTypeTasks(String taskType) 
			throws IOException, JSONException, ParseException {
		
		return storageData.getAllTargetTypeTasks(taskType);
	}
}