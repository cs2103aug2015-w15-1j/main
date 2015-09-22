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
import main.java.backend.Storage.Task.Task;

public class StorageData {

	private static final String TYPE_TASK = "task";
	private static final String TYPE_FLOAT = "floatTask";
	private static final String TYPE_EVENT = "event";

	private HashMap<String, CategoryWrapper> allCategories;
	
	private StorageFile storageFile;
	
	public StorageData() throws FileNotFoundException, IOException  { 
		storageFile = new StorageFile();
		allCategories = storageFile.getAllCategoriesFromFile();
	}

	public StorageData(String fileName) throws FileNotFoundException, IOException  { 
		storageFile = new StorageFile(fileName);
		allCategories = storageFile.getAllCategoriesFromFile();
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
	
	public CategoryWrapper addNewCategory(String categoryName) 
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		
		CategoryWrapper categoryWrapper = allCategories.get(categoryName);

		if(!isCategoryExist(categoryWrapper)) {
			categoryWrapper = new CategoryWrapper(new Category(), categoryName);
			allCategories.put(categoryName, categoryWrapper);
			storageFile.setAllCategoriesToFile(allCategories);
		}
		
		return categoryWrapper;
	}
	
	public HashMap<String, Task> addNewTask(String categoryName, String taskType, Task newTask) 
			throws JsonParseException, JsonMappingException, IOException, JSONException {

		CategoryWrapper categoryWrapper = addNewCategory(categoryName);
		Category category = categoryWrapper.getCategory();
		HashMap<String, Task> allTasks = getTargetTaskList(category, taskType);
		
		allTasks.put(newTask.getTaskId(), newTask);
		categoryWrapper.setCategory(setTaskToCategory(category, allTasks, taskType));
		allCategories.put(categoryName, categoryWrapper);

		storageFile.setAllCategoriesToFile(allCategories);
		
		return allTasks;
	}
	
	public void setCategoryColour(String categoryName, String colourId) 
			throws JsonParseException, JsonMappingException, IOException {

		Category category = allCategories.get(categoryName).getCategory();
		category.setCategoryColour(colourId);
		storageFile.setAllCategoriesToFile(allCategories);
	}

	public void setDone(String taskId, boolean isDone) 
			throws JsonParseException, JsonMappingException, IOException {
		
		HashMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setDone(isDone);
		storageFile.setAllCategoriesToFile(allCategories);
	}
	
	public void setReminder(String taskId, long reminder) 
			throws JsonParseException, JsonMappingException, IOException {
		
		HashMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setReminder(reminder);
		storageFile.setAllCategoriesToFile(allCategories);
	}
	
	public void setDescription(String taskId, String description) 
			throws JsonParseException, JsonMappingException, IOException {
		
		HashMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setDescription(description);
		storageFile.setAllCategoriesToFile(allCategories);
	}
	
	public void setDeadline(String taskId, long deadline) 
			throws JsonParseException, JsonMappingException, IOException {
		
		HashMap<String, Task> targetTask = getAllTasks();
		// TODO: When updating enddate, update endtime as well
		targetTask.get(taskId).setEndTime(deadline);
		storageFile.setAllCategoriesToFile(allCategories);
	}
	
	public ArrayList<Category> getCategoryList() {
		
		ArrayList<Category> categoryList = new ArrayList<Category> ();
		
		for(String categoryName : allCategories.keySet()) {
			categoryList.add(allCategories.get(categoryName).getCategory());
		}
		
		return categoryList;
	}

	public ArrayList<Task> getTaskList() {

		ArrayList<Task> taskList = new ArrayList<Task> ();
		HashMap<String, Task> allTasks = getAllTasks();

		for(String taskId : allTasks.keySet()) {
			taskList.add(allTasks.get(taskId));
		}

		return taskList;
	}
	
	public ArrayList<Task> getCategoryAllTasks(String categoryName) 
			throws ParseException, IOException, JSONException {
		
		ArrayList<Task> allCategoryTasks = new ArrayList<Task> ();
		Category category = allCategories.get(categoryName).getCategory();
		
		allCategoryTasks.addAll(getTasks(category.getTasks()));
		allCategoryTasks.addAll(getTasks(category.getFloatTasks()));
		allCategoryTasks.addAll(getTasks(category.getEvents()));

		return allCategoryTasks;
	}
	
	public ArrayList<Task> getCategoryTaskTypes(String categoryName, String taskType) 
			throws ParseException, IOException, JSONException {
		
		Category category = allCategories.get(categoryName).getCategory();
		return getTasks(getTargetTaskList(category, taskType));
	}
	
	public ArrayList<Task> getTargetTasks(String taskType) 
			throws ParseException, IOException, JSONException {
		
		ArrayList<Task> allTypeTasks = new ArrayList<Task> ();
		int size = allCategories.size();
		
		for(int i = 0; i < size; i++) {
			allTypeTasks.addAll(getTypeTaskArray(allCategories.get(i).getCategory(), taskType));	
		}

		return allTypeTasks;
	}
}
