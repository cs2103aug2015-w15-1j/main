package main.java.backend.Storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.CategoryWrapper;
import main.java.backend.Storage.Task.SubTask;
import main.java.backend.Storage.Task.Task;

public class StorageData extends Storage {

	private static final String TYPE_TASK = "task";
	private static final String TYPE_FLOAT = "floatTask";
	private static final String TYPE_EVENT = "event";

	private HashMap<String, CategoryWrapper> allCategories;
	
	private StorageJson storageJson;
	
	public StorageData() throws FileNotFoundException, IOException  { 
		storageJson = new StorageJson();
		allCategories = storageJson.getAllDataFromFile();
	}

	public StorageData(String fileName) throws FileNotFoundException, IOException  { 
		storageJson = new StorageJson(fileName);
		allCategories = storageJson.getAllDataFromFile();
	}
	
	@Override
	public void addFloatingTask(String taskName, String taskDescription, int priority, 
			long reminder, String category) throws JsonParseException, 
			JsonMappingException, IOException, JSONException {
		
		Task newFloatingTask = new Task(UUID.randomUUID().toString(), taskName, 
				taskDescription, priority, reminder, false);
		addNewTask(category, TYPE_FLOAT, newFloatingTask);
	}
	
	@Override
	public void addTask(String taskName, String taskDescription, String deadline, 
			long endTime, int priority, long reminder, String category) 
					throws IOException, JSONException {	
		
		Task newTask = new Task(UUID.randomUUID().toString(), taskName, 
				taskDescription, deadline, endTime, priority, reminder, false);
		addNewTask(category, TYPE_TASK, newTask);
	}

	@Override
	public void addEvent(String eventName, String eventDescription, String startDate, 
			String endDate, long startDateMilliseconds, long endDateMilliseconds, int priority, 
			long reminder, String category) throws IOException, JSONException {
		
		Task newEvent = new Task(UUID.randomUUID().toString(), eventName, eventDescription, startDate, 
				endDate, startDateMilliseconds, endDateMilliseconds, priority, reminder, category);
		addNewTask(category, TYPE_EVENT, newEvent);
	}
	
	@Override
	public void addSubTask(String taskId, String subtaskDescription) 
			throws JsonParseException, JsonMappingException, IOException {

		SubTask subTask = new SubTask(UUID.randomUUID().toString(), subtaskDescription, false);
		addSubTask(taskId, subTask);
	}
	
	@Override
	public CategoryWrapper addCategory(String categoryName) 
			throws JsonParseException, JsonMappingException, IOException {
		
		CategoryWrapper categoryWrapper = allCategories.get(categoryName);

		if(!isCategoryExist(categoryWrapper)) {
			categoryWrapper = new CategoryWrapper(new Category(), categoryName);
			allCategories.put(categoryName, categoryWrapper);
			storageJson.setAllDataToFile(allCategories);
		}
		
		return categoryWrapper;
	}
	
	@Override
	public void setCategoryColour(String categoryName, String colourId) 
			throws JsonParseException, JsonMappingException, IOException {

		Category category = allCategories.get(categoryName).getCategory();
		category.setCategoryColour(colourId);
		storageJson.setAllDataToFile(allCategories);
	}
	
	@Override
	public void setCategory(String taskId, String categoryName) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setUndone(String taskId) 
			throws JsonParseException, JsonMappingException, JSONException, IOException {
		
		setDone(taskId, false);
	}
	
	@Override
	public void setDone(String taskId) 
			throws JsonParseException, JsonMappingException, IOException {
		
		setDone(taskId, true);
	}
	
	@Override
	public void setReminder(String taskId, long reminder) 
			throws JsonParseException, JsonMappingException, IOException {
		
		HashMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setReminder(reminder);
		storageJson.setAllDataToFile(allCategories);
	}
	
	@Override
	public void setDescription(String taskId, String description) 
			throws JsonParseException, JsonMappingException, IOException {
		
		HashMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setDescription(description);
		storageJson.setAllDataToFile(allCategories);
	}
	
	@Override
	public void setDeadline(String taskId, long deadline) 
			throws JsonParseException, JsonMappingException, IOException {
		
		HashMap<String, Task> targetTask = getAllTasks();
		// TODO: When updating enddate, update endtime as well
		targetTask.get(taskId).setEndTime(deadline);
		storageJson.setAllDataToFile(allCategories);
	}
	
	@Override
	public void setSubTaskUndone(String taskId) 
			throws JsonParseException, JsonMappingException, JSONException, IOException {
		
		
	}

	@Override
	public void setSubTaskDone(String taskId) 
			throws JsonParseException, JsonMappingException, IOException {
		
		
	}
	
	@Override
	public void setSubtaskDescription(String taskId, String description) 
			throws JsonParseException, JsonMappingException, IOException {
		
		
	}
	
	@Override
	public void deleteSubTask(String taskId, String subtaskDescription) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void deleteAll() throws IOException {
		storageJson.setAllDataToFile(new HashMap<String, CategoryWrapper> ());
		storageJson.clearTextFromFile();
	}
	
	@Override
	public void deleteCategory(String categoryName) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void deleteTaskTypeFromCategory(String categoryName, String taskType) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void deleteTaskFromCategory(String categoryName, String taskId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteTask(String taskId) {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<String> getCategories() {
		
		ArrayList<String> categories = new ArrayList<String> ();
	
		
		return categories;
	}
	
	@Override
	public ArrayList<Category> getCategoryList() {
		
		ArrayList<Category> categoryList = new ArrayList<Category> ();
		
		for(String categoryName : allCategories.keySet()) {
			categoryList.add(allCategories.get(categoryName).getCategory());
		}
		
		return categoryList;
	}

	@Override
	public ArrayList<Task> getTaskList() {

		ArrayList<Task> taskList = new ArrayList<Task> ();
		HashMap<String, Task> allTasks = getAllTasks();

		for(String taskId : allTasks.keySet()) {
			taskList.add(allTasks.get(taskId));
		}

		return taskList;
	}
	
	@Override
	public ArrayList<Task> getCategoryAllTasks(String categoryName) 
			throws ParseException, IOException, JSONException {
		
		ArrayList<Task> allCategoryTasks = new ArrayList<Task> ();
		Category category = allCategories.get(categoryName).getCategory();
		
		allCategoryTasks.addAll(getTasks(category.getTasks()));
		allCategoryTasks.addAll(getTasks(category.getFloatTasks()));
		allCategoryTasks.addAll(getTasks(category.getEvents()));

		return allCategoryTasks;
	}
	
	@Override
	public ArrayList<Task> getCategoryTasks(String categoryName) 
			throws IOException, JSONException, ParseException {

		return getCategoryTaskTypes(categoryName, TYPE_TASK);
	}
	
	@Override
	public ArrayList<Task> getCategoryFloatingTasks(String categoryName) 
			throws IOException, JSONException, ParseException {

		return getCategoryTaskTypes(categoryName, TYPE_FLOAT);
	}
	
	@Override
	public ArrayList<Task> getCategoryEvents(String categoryName) 
			throws IOException, JSONException, ParseException {

		return getCategoryTaskTypes(categoryName, TYPE_EVENT);
	}
	
	@Override
	public ArrayList<Task> getTasks() 
			throws IOException, JSONException, ParseException {
		
		return getTargetTasks(TYPE_TASK);
	}
	
	@Override
	public ArrayList<Task> getFloatingTasks() 
			throws IOException, JSONException, ParseException {
		
		return getTargetTasks(TYPE_FLOAT);
	}
	
	@Override
	public ArrayList<Task> getEvents() 
			throws IOException, JSONException, ParseException {
		
		return getTargetTasks(TYPE_EVENT);
	}
	
	private HashMap<String, Task> addNewTask(String categoryName, String taskType, Task newTask) 
			throws JsonParseException, JsonMappingException, IOException, JSONException {

		CategoryWrapper categoryWrapper = addCategory(categoryName);
		Category category = categoryWrapper.getCategory();
		HashMap<String, Task> allTasks = getTargetTaskList(category, taskType);
		
		allTasks.put(newTask.getTaskId(), newTask);
		categoryWrapper.setCategory(setTaskToCategory(category, allTasks, taskType));
		allCategories.put(categoryName, categoryWrapper);

		storageJson.setAllDataToFile(allCategories);
		
		return allTasks;
	}
	
	private void addSubTask(String taskId, SubTask subTask) 
			throws JsonParseException, JsonMappingException, IOException {
		
		Task targetTask = getAllTasks().get(taskId);
		HashMap<String, SubTask> subTaskList = targetTask.getSubTask();
		subTaskList.put(subTask.getSubTaskId(), subTask);
		storageJson.setAllDataToFile(allCategories);
	}
	
	private void setDone(String taskId, boolean isDone) 
			throws JsonParseException, JsonMappingException, IOException {
		
		HashMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setDone(isDone);
		storageJson.setAllDataToFile(allCategories);
	}
	
	private Category setTaskToCategory(Category category, HashMap<String, Task> allTasks, String taskType) {
		switch(taskType) {
		case TYPE_TASK:
			category.setTasks(allTasks);
			return category;
		case TYPE_FLOAT:
			category.setFloatTasks(allTasks);
			return category;
		case TYPE_EVENT:
			category.setEvents(allTasks);
			return category;
		}
		return new Category();
	}
	
	private HashMap<String, Task> getTargetTaskList(Category category, String taskType) {
		switch(taskType) {
			case TYPE_TASK:
				return category.getTasks();
			case TYPE_FLOAT:
				return category.getFloatTasks();
			case TYPE_EVENT:
				return category.getEvents();
		}
		return new HashMap<String, Task> ();
	}
	
	private HashMap<String, Task> getAllTasks() {
		HashMap<String, Task> allTasks = new HashMap<String, Task> ();
		
		for(String categoryName : allCategories.keySet()) {
			Category category = allCategories.get(categoryName).getCategory();
			
			for(String taskId : category.getTasks().keySet()) {
				allTasks.put(taskId, category.getTasks().get(taskId));
			}
			
			for(String taskId : category.getFloatTasks().keySet()) {
				allTasks.put(taskId, category.getFloatTasks().get(taskId));
			}
			
			for(String taskId : category.getEvents().keySet()) {
				allTasks.put(taskId, category.getEvents().get(taskId));
			}
			
		}
		
		return allTasks;
	}
	
	private ArrayList<Task> getCategoryTaskTypes(String categoryName, String taskType) 
			throws ParseException, IOException, JSONException {
		
		Category category = allCategories.get(categoryName).getCategory();
		return getTasks(getTargetTaskList(category, taskType));
	}
	
	private ArrayList<Task> getTargetTasks(String taskType) 
			throws ParseException, IOException, JSONException {
		
		ArrayList<Task> allTypeTasks = new ArrayList<Task> ();
		int size = allCategories.size();
		
		for(int i = 0; i < size; i++) {
			allTypeTasks.addAll(getTypeTaskArray(allCategories.get(i).getCategory(), taskType));	
		}

		return allTypeTasks;
	}
	
	private ArrayList<Task> getTypeTaskArray(Category category, String taskType) {
		
		switch(taskType) {
			case TYPE_TASK:
				return getTasks(category.getTasks());
			case TYPE_FLOAT:
				return getTasks(category.getFloatTasks());
			case TYPE_EVENT:
				return getTasks(category.getEvents());
		}
		
		return new ArrayList<Task> ();
	}
	
	private ArrayList<Task> getTasks(HashMap<String, Task> tasks) {
		
		ArrayList<Task> allTasks = new ArrayList<Task> ();
		
		for(String taskName : tasks.keySet()) {
			allTasks.add(tasks.get(taskName));
		}
		
		return allTasks;
	}
	
	private boolean isCategoryExist(CategoryWrapper categoryWrapper) {
		return categoryWrapper!= null && categoryWrapper.getCategory() != null;
	}
}