package main.java.backend.Storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

public class Storage {

	private static final String TYPE_TASK = "task";
	private static final String TYPE_FLOAT = "floatTask";
	private static final String TYPE_EVENT = "event";

	private HashMap<String, CategoryWrapper> allCategories;
	
	private StorageFile storageFile;
	
	public Storage() throws FileNotFoundException, IOException  { 
		storageFile = new StorageFile();
		allCategories = storageFile.getAllDataFromFile();
	}

	public Storage(String fileName) throws FileNotFoundException, IOException  { 
		storageFile = new StorageFile(fileName);
		allCategories = storageFile.getAllDataFromFile();
		System.out.println("Storage component initialised successfully");
	}
	
	/****************************************************************************
	 * 								HELPER METHODS
	 ***************************************************************************/

	private HashMap<String, Task> addNewTask(String categoryName, String taskType, Task newTask) 
			throws JsonParseException, JsonMappingException, IOException, JSONException {

		CategoryWrapper categoryWrapper = addCategory(categoryName);
		Category category = categoryWrapper.getCategory();
		HashMap<String, Task> allTasks = getTargetTaskList(category, taskType);

		allTasks.put(newTask.getTaskId(), newTask);
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
	
	private Category setTaskToCategory(Category category, HashMap<String, 
			Task> allTasks, String taskType) {
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
	
	private ArrayList<Task> getTargetTasks(String taskType) 
			throws ParseException, IOException, JSONException {
		
		ArrayList<Task> allTypeTasks = new ArrayList<Task> ();

		for(String category : allCategories.keySet()) {
			allTypeTasks.addAll(getTypeTaskArray(allCategories.get(category).getCategory(), taskType));
		}

		return allTypeTasks;
	}
	
	private ArrayList<Task> getTypeTaskArray(Category category, String taskType) {
		ArrayList<Task> typeTask = new ArrayList<Task> ();
		
		switch(taskType) {
			case TYPE_TASK:
				return getTasksInArray(category.getTasks());
			case TYPE_FLOAT:
				return getTasksInArray(category.getFloatTasks());
			case TYPE_EVENT:
				return getTasksInArray(category.getEvents());
		}
		
		return typeTask;
	}
	
	private ArrayList<Task> getTasksInArray(HashMap<String, Task> tasks) {
		
		ArrayList<Task> allTasks = new ArrayList<Task> ();
		
		for(String taskId : tasks.keySet()) {
			allTasks.add(tasks.get(taskId));
		}
		
		return allTasks;
	}
	
	private boolean isCategoryExist(CategoryWrapper categoryWrapper) {
		return categoryWrapper!= null && categoryWrapper.getCategory() != null;
	}
	
	private String getTaskId(int taskId) throws IOException, JSONException, ParseException {
		return getFloatingTasks().get(taskId).getTaskId();
	}
	
	private String standardDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		SimpleDateFormat standardFormat = new SimpleDateFormat("EEE, dd MMM HH:mma");
		
		try {
            Date tempDate = formatter.parse(date);
            return standardFormat.format(tempDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
		
        //Should not reach here
        return "";
    }
	
	/****************************************************************************
	 * 									CREATE
	 ***************************************************************************/
	

	public CategoryWrapper addCategory(String categoryName) 
			throws JsonParseException, JsonMappingException, IOException {
		
		CategoryWrapper categoryWrapper = allCategories.get(categoryName);
		
		if(!isCategoryExist(categoryWrapper)) {
			categoryWrapper = new CategoryWrapper(new Category(), categoryName);
			allCategories.put(categoryName, categoryWrapper);
			storageFile.setAllDataToFile(allCategories);
		}
		
		return categoryWrapper;
	}
	
	
	public String addFloatingTask(String taskName, String taskDescription, int priority, 
			String reminderDate, long reminder, String category) throws JsonParseException, 
			JsonMappingException, IOException, JSONException {
		
		Task newFloatingTask = new Task(UUID.randomUUID().toString(), taskName, taskDescription, 
				priority, standardDate(reminderDate), reminder, false);
		addNewTask(category, TYPE_FLOAT, newFloatingTask);
		
		return "";
	}
	
	
	public String addTask(String taskName, String taskDescription, String deadline, 
			long endTime, int priority, String reminderDate, long reminder, String category) 
					throws IOException, JSONException {	
		
		Task newTask = new Task(UUID.randomUUID().toString(), taskName, taskDescription, standardDate(deadline), 
				endTime, priority, standardDate(reminderDate), reminder, false);
		addNewTask(category, TYPE_TASK, newTask);
		
		return "";
	}

	
	public String addEvent(String eventName, String eventDescription, String startDate, 
			String endDate, long startDateMilliseconds, long endDateMilliseconds, int priority, 
			String reminderDate, long reminder, String category) throws IOException, JSONException {
		
		Task newEvent = new Task(UUID.randomUUID().toString(), eventName, eventDescription, standardDate(startDate), 
				standardDate(endDate), startDateMilliseconds, endDateMilliseconds, priority, 
				standardDate(reminderDate), reminder, category);
		addNewTask(category, TYPE_EVENT, newEvent);
		
		return "";
	}
	
	
	public void addSubTask(String taskName, String subtaskDescription) 
			throws JsonParseException, JsonMappingException, IOException {

		SubTask subTask = new SubTask(subtaskDescription, false);
		addSubTask(taskName, subTask);
	}
	
	/****************************************************************************
	 * 									READ
	 ***************************************************************************/
	
	
	public ArrayList<String> getCategories() {
		
		ArrayList<String> categories = new ArrayList<String> ();
		
		for(String name : allCategories.keySet()) {
			if(!name.isEmpty()) {
				categories.add(name);
			}
		}
		
		return categories;
	}
	
	
	public ArrayList<Category> getCategoryList() throws IOException {
		
		ArrayList<Category> categoryList = new ArrayList<Category> ();
		allCategories = storageFile.getAllDataFromFile();
		
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
		
		allCategoryTasks.addAll(getTasksInArray(category.getTasks()));
		allCategoryTasks.addAll(getTasksInArray(category.getFloatTasks()));
		allCategoryTasks.addAll(getTasksInArray(category.getEvents()));

		return allCategoryTasks;
	}
	
	
	public ArrayList<Task> getCategoryTasks(String categoryName) 
			throws IOException, JSONException, ParseException {

		Category category = allCategories.get(categoryName).getCategory();
		return getTasksInArray(getTargetTaskList(category, TYPE_TASK));
	}
	
	
	public ArrayList<Task> getCategoryFloatingTasks(String categoryName) 
			throws IOException, JSONException, ParseException {

		Category category = allCategories.get(categoryName).getCategory();
		return getTasksInArray(getTargetTaskList(category, TYPE_FLOAT));
	}
	
	
	public ArrayList<Task> getCategoryEvents(String categoryName) 
			throws IOException, JSONException, ParseException {

		Category category = allCategories.get(categoryName).getCategory();
		return getTasksInArray(getTargetTaskList(category, TYPE_EVENT));
	}
	
	
	public ArrayList<Task> getTasks() 
			throws IOException, JSONException, ParseException {
		
		return getTargetTasks(TYPE_TASK);
	}
	
	
	public ArrayList<Task> getFloatingTasks() 
			throws IOException, JSONException, ParseException {
		
		return getTargetTasks(TYPE_FLOAT);
	}
	
	
	public ArrayList<Task> getEvents() 
			throws IOException, JSONException, ParseException {
		
		return getTargetTasks(TYPE_EVENT);
	}
	
	
	public ArrayList<Task> getCompletedTasks() {
		ArrayList<Task> allTasks = getTasksInArray(getAllTasks());
		ArrayList<Task> completedTasks = new ArrayList<Task> ();
		
		for(Task task : allTasks) {
			if(task.getDone()) {
				completedTasks.add(task);
			}
		}
		
		return completedTasks;
	}

	
	public ArrayList<Task> getOverdueTasks() {
		ArrayList<Task> allTasks = getTasksInArray(getAllTasks());
		ArrayList<Task> overdueTasks = new ArrayList<Task> ();
		
		for(Task task : allTasks) {
			if(task.getEndTime() < System.currentTimeMillis()) {
				overdueTasks.add(task);
			}
		}
		
		return overdueTasks;
	}
	
	/****************************************************************************
	 * 									UPDATE
	 ***************************************************************************/
	
	
	public void setCategoryColour(String categoryName, String colourId) 
			throws JsonParseException, JsonMappingException, IOException {

		Category category = allCategories.get(categoryName).getCategory();
		category.setCategoryColour(colourId);
		storageFile.setAllDataToFile(allCategories);
	}
	
	
	public void setCategory(String taskName, String categoryName) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void setUndone(String taskName) 
			throws JsonParseException, JsonMappingException, JSONException, IOException {
		
		setDone(taskName, false);
	}
	
	
	public void setDone(String taskName) 
			throws JsonParseException, JsonMappingException, IOException {
		
		setDone(taskName, true);
	}
	
	
	public void setReminder(String taskName, String reminder) 
			throws JsonParseException, JsonMappingException, IOException {
		
		HashMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskName).setReminderDate(reminder);
		storageFile.setAllDataToFile(allCategories);
	}
	
	
	public void setReminder(String taskName, long reminder) 
			throws JsonParseException, JsonMappingException, IOException {
		
		HashMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskName).setReminder(reminder);
		storageFile.setAllDataToFile(allCategories);
	}
	
	
	public void setPriority(String taskName, int priority) throws IOException {
		HashMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskName).setPriority(priority);;
		storageFile.setAllDataToFile(allCategories);	
	}
	
	
	public void setDescription(String taskName, String description) 
			throws JsonParseException, JsonMappingException, IOException {
		
		HashMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskName).setDescription(description);
		storageFile.setAllDataToFile(allCategories);
	}
	
	
	public void setDeadline(String taskName, long deadline) 
			throws JsonParseException, JsonMappingException, IOException {
		
		HashMap<String, Task> targetTask = getAllTasks();
		// TODO: When updating enddate, update endtime as well
		targetTask.get(taskName).setEndTime(deadline);
		storageFile.setAllDataToFile(allCategories);
	}
	
	
	public void setSubTaskUndone(String taskName) 
			throws JsonParseException, JsonMappingException, JSONException, IOException {
		
		
	}

	
	public void setSubTaskDone(String taskName) 
			throws JsonParseException, JsonMappingException, IOException {
		
		
	}
	
	
	public void setSubtaskDescription(String taskName, String description) 
			throws JsonParseException, JsonMappingException, IOException {
		
		
	}
	
	/****************************************************************************
	 * 									DELETE
	 ***************************************************************************/
	
	
	public void deleteAll() throws IOException {
		storageFile.setAllDataToFile(new HashMap<String, CategoryWrapper> ());
		storageFile.clearTextFromFile();
	}
	
	
	public void deleteCategory(String categoryName) throws IOException {
		allCategories.remove(categoryName);
		storageFile.setAllDataToFile(allCategories);
	}
	
	
	public void deleteTaskTypeFromCategory(String categoryName, String taskType) throws IOException {
		HashMap<String, Task> resetData = new HashMap<String, Task>();
		switch(taskType) {
			case TYPE_TASK:
				allCategories.get(categoryName).getCategory().setTasks(resetData);
				break;
			case TYPE_FLOAT:
				allCategories.get(categoryName).getCategory().setFloatTasks(resetData);
				break;
			case TYPE_EVENT:
				allCategories.get(categoryName).getCategory().setEvents(resetData);
				break;
		}
		storageFile.setAllDataToFile(allCategories);
	}

	
	public void deleteTask(int taskIndex) throws IOException, JSONException, ParseException {
		
		String taskId = getTaskId(taskIndex);
		
		for(String categoryName : allCategories.keySet()) {
			Category category = allCategories.get(categoryName).getCategory();
			HashMap<String, Task> tasks = category.getTasks();
			HashMap<String, Task> floatTasks = category.getFloatTasks();
			HashMap<String, Task> events = category.getEvents();
			
			for(String targetTaskId : tasks.keySet()) {
				if(targetTaskId.equals(taskId)) {
					tasks.remove(taskId);
					break;
				}
			}
			
			for(String targetTaskId : floatTasks.keySet()) {
				if(targetTaskId.equals(taskId)) {
					floatTasks.remove(taskId);
					break;
				}
			}
			
			for(String targetTaskId : events.keySet()) {
				if(targetTaskId.equals(taskId)) {
					events.remove(taskId);
					break;
				}
			}
		}
		storageFile.setAllDataToFile(allCategories);
	}
	
	
	public void deleteSubTask(String taskName, String subtaskDescription) {
		// TODO Auto-generated method stub
		
	}
	

	
	public void updateFile(ArrayList<Category> categories) throws IOException {
		
		storageFile.clearTextFromFile();
		
		for(Category category : categories) {
			CategoryWrapper categoryWrapper = addCategory(category.getCategoryName());
			categoryWrapper.setCategory(category);
			allCategories.put(category.getCategoryName(), categoryWrapper);
		}
		
		storageFile.setAllDataToFile(allCategories);
	}

	
	public void exitProgram() throws IOException {
		storageFile.exitProgram();
	}
}