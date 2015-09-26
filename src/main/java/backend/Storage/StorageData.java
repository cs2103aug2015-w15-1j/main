package main.java.backend.Storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.CategoryWrapper;
import main.java.backend.Storage.Task.SubTask;
import main.java.backend.Storage.Task.Task;

public class StorageData extends Storage {

	private static final String CATEGORY_DEFAULT = "default";
	private static final String TYPE_TASK = "task";
	private static final String TYPE_FLOAT = "floatTask";
	private static final String TYPE_EVENT = "event";

	private HashMap<String, CategoryWrapper> allCategories;
	
	private StorageFile storageFile;
	
	public StorageData() throws FileNotFoundException, IOException  { 
		storageFile = new StorageFile();
		allCategories = storageFile.getAllDataFromFile();
	}

	public StorageData(String fileName) throws FileNotFoundException, IOException  { 
		storageFile = new StorageFile(fileName);
		allCategories = storageFile.getAllDataFromFile();
	}
	
	
	private void addDefaultCategory() throws IOException {
		CategoryWrapper categoryWrapper;
		
		if(storageFile.isFileEmpty()) {
			categoryWrapper = new CategoryWrapper(new Category(), CATEGORY_DEFAULT);
			allCategories.put(CATEGORY_DEFAULT, categoryWrapper);
		}
	}
	
	private HashMap<String, Task> addNewTask(String categoryName, String taskType, Task newTask) 
			throws JsonParseException, JsonMappingException, IOException, JSONException {

		CategoryWrapper categoryWrapper = addCategory(categoryName);
		Category category = categoryWrapper.getCategory();
		HashMap<String, Task> allTasks = getTargetTaskList(category, taskType);
		
		allTasks.put(newTask.getName(), newTask);
		categoryWrapper.setCategory(setTaskToCategory(category, allTasks, taskType));
		allCategories.put(categoryName, categoryWrapper);

		storageFile.setAllDataToFile(allCategories);
		
		return allTasks;
	}
	
	private void addSubTask(String taskName, SubTask subTask) 
			throws JsonParseException, JsonMappingException, IOException {
		
		Task targetTask = getAllTasks().get(taskName);
		HashMap<String, SubTask> subTaskList = targetTask.getSubTask();
		subTaskList.put(subTask.getDescription(), subTask);
		storageFile.setAllDataToFile(allCategories);
	}
	
	private void setDone(String taskName, boolean isDone) 
			throws JsonParseException, JsonMappingException, IOException {
		
		HashMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskName).setDone(isDone);
		storageFile.setAllDataToFile(allCategories);
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
			
			for(String taskName : category.getTasks().keySet()) {
				allTasks.put(taskName, category.getTasks().get(taskName));
			}
			
			for(String taskName : category.getFloatTasks().keySet()) {
				allTasks.put(taskName, category.getFloatTasks().get(taskName));
			}
			
			for(String taskName : category.getEvents().keySet()) {
				allTasks.put(taskName, category.getEvents().get(taskName));
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
	
	/****************************************************************************
	 * 									CREATE
	 ***************************************************************************/
	
	@Override
	public CategoryWrapper addCategory(String categoryName) 
			throws JsonParseException, JsonMappingException, IOException {
		
		CategoryWrapper categoryWrapper = allCategories.get(categoryName);
		addDefaultCategory();
		
		if(!isCategoryExist(categoryWrapper)) {
			categoryWrapper = new CategoryWrapper(new Category(), categoryName);
			allCategories.put(categoryName, categoryWrapper);
			storageFile.setAllDataToFile(allCategories);
		}
		
		return categoryWrapper;
	}
	
	@Override
	public void addFloatingTask(String taskName, String taskDescription, int priority, 
			long reminder, String category) throws JsonParseException, 
			JsonMappingException, IOException, JSONException {
		
		Task newFloatingTask = new Task(taskName, taskDescription, priority, reminder, false);
		addNewTask(category, TYPE_FLOAT, newFloatingTask);
	}
	
	@Override
	public void addTask(String taskName, String taskDescription, String deadline, 
			long endTime, int priority, long reminder, String category) 
					throws IOException, JSONException {	
		
		Task newTask = new Task(taskName, taskDescription, deadline, endTime, 
				priority, reminder, false);
		addNewTask(category, TYPE_TASK, newTask);
	}

	@Override
	public void addEvent(String eventName, String eventDescription, String startDate, 
			String endDate, long startDateMilliseconds, long endDateMilliseconds, int priority, 
			long reminder, String category) throws IOException, JSONException {
		
		Task newEvent = new Task(eventName, eventDescription, startDate, endDate, 
				startDateMilliseconds, endDateMilliseconds, priority, reminder, category);
		addNewTask(category, TYPE_EVENT, newEvent);
	}
	
	@Override
	public void addSubTask(String taskName, String subtaskDescription) 
			throws JsonParseException, JsonMappingException, IOException {

		SubTask subTask = new SubTask(subtaskDescription, false);
		addSubTask(taskName, subTask);
	}
	
	/****************************************************************************
	 * 									READ
	 ***************************************************************************/
	
	@Override
	public ArrayList<String> getCategories() {
		
		ArrayList<String> categories = new ArrayList<String> ();
		
		for(String name : allCategories.keySet()) {
			categories.add(name);
		}
		
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

		for(String taskName : allTasks.keySet()) {
			taskList.add(allTasks.get(taskName));
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
	
	/****************************************************************************
	 * 									UPDATE
	 ***************************************************************************/
	
	@Override
	public void setCategoryColour(String categoryName, String colourId) 
			throws JsonParseException, JsonMappingException, IOException {

		Category category = allCategories.get(categoryName).getCategory();
		category.setCategoryColour(colourId);
		storageFile.setAllDataToFile(allCategories);
	}
	
	@Override
	public void setCategory(String taskName, String categoryName) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setUndone(String taskName) 
			throws JsonParseException, JsonMappingException, JSONException, IOException {
		
		setDone(taskName, false);
	}
	
	@Override
	public void setDone(String taskName) 
			throws JsonParseException, JsonMappingException, IOException {
		
		setDone(taskName, true);
	}
	
	@Override
	public void setReminder(String taskName, long reminder) 
			throws JsonParseException, JsonMappingException, IOException {
		
		HashMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskName).setReminder(reminder);
		storageFile.setAllDataToFile(allCategories);
	}
	
	@Override
	public void setDescription(String taskName, String description) 
			throws JsonParseException, JsonMappingException, IOException {
		
		HashMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskName).setDescription(description);
		storageFile.setAllDataToFile(allCategories);
	}
	
	@Override
	public void setDeadline(String taskName, long deadline) 
			throws JsonParseException, JsonMappingException, IOException {
		
		HashMap<String, Task> targetTask = getAllTasks();
		// TODO: When updating enddate, update endtime as well
		targetTask.get(taskName).setEndTime(deadline);
		storageFile.setAllDataToFile(allCategories);
	}
	
	@Override
	public void setSubTaskUndone(String taskName) 
			throws JsonParseException, JsonMappingException, JSONException, IOException {
		
		
	}

	@Override
	public void setSubTaskDone(String taskName) 
			throws JsonParseException, JsonMappingException, IOException {
		
		
	}
	
	@Override
	public void setSubtaskDescription(String taskName, String description) 
			throws JsonParseException, JsonMappingException, IOException {
		
		
	}
	
	/****************************************************************************
	 * 									DELETE
	 ***************************************************************************/
	
	@Override
	public void deleteAll() throws IOException {
		storageFile.setAllDataToFile(new HashMap<String, CategoryWrapper> ());
		storageFile.clearTextFromFile();
	}
	
	@Override
	public void deleteCategory(String categoryName) throws IOException {
		allCategories.remove(categoryName);
		storageFile.setAllDataToFile(allCategories);
	}
	
	@Override
	public void deleteTaskTypeFromCategory(String categoryName, String taskType) throws IOException {
		HashMap<String, Task> resetData = new HashMap<String, Task>();
		switch(taskType) {
			case TYPE_TASK:
				allCategories.get(categoryName).getCategory().setTasks(resetData);
				break;
			case TYPE_FLOAT:
				allCategories.get(categoryName).getCategory().setTasks(resetData);
				break;
			case TYPE_EVENT:
				allCategories.get(categoryName).getCategory().setTasks(resetData);
				break;
		}
		storageFile.setAllDataToFile(allCategories);
	}

	@Override
	public void deleteTask(String taskName) {
		
	}
	
	@Override
	public void deleteSubTask(String taskName, String subtaskDescription) {
		// TODO Auto-generated method stub
		
	}
}