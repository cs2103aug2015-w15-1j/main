package main.java.backend.Storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import main.java.backend.Storage.Task.Category;
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

	public StorageStub (String fileName) throws FileNotFoundException, IOException {
		storageData = new StorageData(fileName);
		storageFile = new StorageFile(fileName);
	}
	
	public void addFloatingTask(String taskName, String taskDescription, int priority, long reminder,
			String category, boolean done) throws IOException, JSONException {
		
		Task newFloatingTask = new Task(UUID.randomUUID().toString(), taskName, 
				taskDescription, priority, reminder, done);
		storageData.addNewTask(category, TYPE_FLOAT, newFloatingTask);
	}
	
	public void addTask(String taskName, String taskDescription, String deadline, long endTime, int priority, 
			int reminder, String category, boolean done) throws IOException, JSONException {	
		
		Task newTask = new Task(UUID.randomUUID().toString(), taskName, 
				taskDescription, deadline, endTime, priority, reminder, done);
		storageData.addNewTask(category, TYPE_TASK, newTask);
	}

	public void addEvent(String eventName, String eventDescription, String startDate, 
			String endDate, long startDateMilliseconds, long endDateMilliseconds, int priority, 
			long reminder, String category) throws IOException, JSONException {
		
		Task newEvent = new Task(UUID.randomUUID().toString(), eventName, eventDescription, startDate, 
				endDate, startDateMilliseconds, endDateMilliseconds, priority, reminder, category);
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
	
	public void setUndone(String taskId) 
			throws JsonParseException, JsonMappingException, JSONException, IOException {
		
		storageData.setDone(taskId, false);
	}

	public void setDone(String taskId) 
			throws JsonParseException, JsonMappingException, IOException {
		
		storageData.setDone(taskId, true);
	}

	public void setReminder(String taskId, long reminder) 
			throws JsonParseException, JsonMappingException, IOException {
		
		storageData.setReminder(taskId, reminder);
	}

	public void setDescription(String taskId, String description) 
			throws JsonParseException, JsonMappingException, IOException {
		
		storageData.setDescription(taskId, description);
	}

	public void setDeadline(String taskId, long deadline) 
			throws JsonParseException, JsonMappingException, IOException {
		
		storageData.setDeadline(taskId, deadline);
	}
	
	public void addSubTask(String taskId, String subtaskDescription) {
		// TODO Auto-generated method stub
		
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
	
	public ArrayList<Category> getCategoryList()
			throws JsonParseException, JsonMappingException, JSONException, IOException {
		
		return storageData.getCategoryList();
	}
	
	public ArrayList<Task> getTaskList()
			throws JsonParseException, JsonMappingException, JSONException, IOException {
		
		return storageData.getTaskList();
	}
	
	public ArrayList<Task> getCategoryAllTasks(String categoryName) 
			throws IOException, JSONException, ParseException {

		return storageData.getCategoryAllTasks(categoryName);
	}
	
	public ArrayList<Task> getCategoryTasks(String categoryName) 
			throws IOException, JSONException, ParseException {

		return storageData.getCategoryTaskTypes(categoryName, TYPE_TASK);
	}
	
	public ArrayList<Task> getCategoryFloatingTasks(String categoryName) 
			throws IOException, JSONException, ParseException {

		return storageData.getCategoryTaskTypes(categoryName, TYPE_FLOAT);
	}
	
	public ArrayList<Task> getCategoryEvents(String categoryName) 
			throws IOException, JSONException, ParseException {

		return storageData.getCategoryTaskTypes(categoryName, TYPE_EVENT);
	}
	
	public ArrayList<Task> getTasks() 
			throws IOException, JSONException, ParseException {
		
		return storageData.getTargetTasks(TYPE_TASK);
	}
	
	public ArrayList<Task> getFloatingTasks() 
			throws IOException, JSONException, ParseException {
		
		return storageData.getTargetTasks(TYPE_FLOAT);
	}
	
	public ArrayList<Task> getEvents() 
			throws IOException, JSONException, ParseException {
		
		return storageData.getTargetTasks(TYPE_EVENT);
	}
}