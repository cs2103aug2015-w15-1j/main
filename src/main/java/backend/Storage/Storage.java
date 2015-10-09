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
import main.java.backend.Storage.Task.SubTask;
import main.java.backend.Storage.Task.Task;

public class Storage {

	private static final String CATEGORY_DEFAULT = "default"; 
	private static final String TYPE_TASK = "task";
	private static final String TYPE_FLOAT = "floatTask";
	private static final String TYPE_EVENT = "event";

	private HashMap<String, Category> allCategories;
	
	private static final SimpleDateFormat formatterForDateTime = 
			new SimpleDateFormat("EEE, dd MMM hh:mma");
	
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

	private void addNewTask(String categoryName, String taskType, Task newTask) 
			throws JsonParseException, JsonMappingException, IOException, JSONException {

		if(categoryName.isEmpty()) {
			categoryName = CATEGORY_DEFAULT;
		}
		
		Category category = addCategory(categoryName);
		setTargetTaskList(category, taskType, newTask);
		storageFile.setAllDataToFile(allCategories);
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
	
	private void setTargetTaskList(Category category, String taskType, Task newTask) {
		switch(taskType) {
			case TYPE_TASK:
				HashMap<String, Task> allTasks = category.getTasks();
				allTasks.put(newTask.getTaskId(), newTask);
				category.setTasks(allTasks);
				break;
			case TYPE_FLOAT:
				HashMap<String, Task> allFloatingTasks = category.getFloatTasks();
				allFloatingTasks.put(newTask.getTaskId(), newTask);
				category.setFloatTasks(allFloatingTasks);
				break;
			case TYPE_EVENT:
				HashMap<String, Task> allEvents = category.getEvents();
				allEvents.put(newTask.getTaskId(), newTask);
				category.setEvents(allEvents);
				break;
		}
		
		allCategories.put(category.getCategoryName(), category);
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
			Category category = allCategories.get(categoryName);
			
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
			allTypeTasks.addAll(getTypeTaskArray(allCategories.get(category), taskType));
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
	
	private long stringToMillisecond(String dateTime) {
        try {
            Date tempDateTime = formatterForDateTime.parse(dateTime);
            long dateTimeMillisecond = tempDateTime.getTime();
            return (dateTimeMillisecond);
        } catch (java.text.ParseException e) {
			e.printStackTrace();
		}

        //Should not reach here
        return -1;
    }
	
	private long getCurrentTime() {
		
		long currentMilliseconds = System.currentTimeMillis();
		SimpleDateFormat standardFormat = new SimpleDateFormat("EEE, dd MMM hh:mma yyyy");
		Date resultdate = new Date(currentMilliseconds);
		
		return stringToMillisecond(standardFormat.format(resultdate));
    }
	
	private String getTaskId(String taskType, int taskId) 
			throws IOException, JSONException, ParseException {
		
		switch(taskType) {
			case TYPE_TASK:
				return getTasks().get(taskId - 1).getTaskId();
			case TYPE_FLOAT:
				return getFloatingTasks().get(taskId - 1).getTaskId();
			case TYPE_EVENT:
				return getEvents().get(taskId - 1).getTaskId();
		}
		
		// Should not reach here
		return "";
	}
	
	/****************************************************************************
	 * 									CREATE
	 ***************************************************************************/
	

	public Category addCategory(String categoryName) 
			throws JsonParseException, JsonMappingException, IOException {
		
		Category category = allCategories.get(categoryName);
		
		if(!allCategories.containsKey(categoryName)) {
			category = new Category(categoryName);
			allCategories.put(categoryName, category);
			storageFile.setAllDataToFile(allCategories);
		}
		
		return category;
	}
	
	
	public void addFloatingTask(String taskName, String taskDescription, int priority, 
			String reminderDate, long reminder, String category) throws JsonParseException, 
			JsonMappingException, IOException, JSONException {
		
		Task newFloatingTask = new Task(UUID.randomUUID().toString(), taskName, 
				taskDescription, priority, reminderDate, reminder, false);
		addNewTask(category, TYPE_FLOAT, newFloatingTask);
	}
	
	
	public void addTask(String taskName, String taskDescription, String deadline, 
			long endTime, int priority, String reminderDate, long reminder, String category) 
					throws IOException, JSONException {	
		
		Task newTask = new Task(UUID.randomUUID().toString(), taskName, taskDescription, 
				deadline, endTime, priority, reminderDate, reminder, false);
		addNewTask(category, TYPE_TASK, newTask);
	}

	
	public void addEvent(String eventName, String eventDescription, String startDate, 
			String endDate, long startDateMilliseconds, long endDateMilliseconds, int priority, 
			String reminderDate, long reminder, String category) throws IOException, JSONException {
		
		Task newEvent = new Task(UUID.randomUUID().toString(), eventName, eventDescription, 
				startDate, endDate, startDateMilliseconds, endDateMilliseconds, priority, 
				reminderDate, reminder, category);
		addNewTask(category, TYPE_EVENT, newEvent);
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
			categoryList.add(allCategories.get(categoryName));
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
		Category category = allCategories.get(categoryName);
		
		allCategoryTasks.addAll(getTasksInArray(category.getTasks()));
		allCategoryTasks.addAll(getTasksInArray(category.getFloatTasks()));
		allCategoryTasks.addAll(getTasksInArray(category.getEvents()));

		return allCategoryTasks;
	}
	
	
	public ArrayList<Task> getCategoryTasks(String categoryName) 
			throws IOException, JSONException, ParseException {

		Category category = allCategories.get(categoryName);
		return getTasksInArray(getTargetTaskList(category, TYPE_TASK));
	}
	
	
	public ArrayList<Task> getCategoryFloatingTasks(String categoryName) 
			throws IOException, JSONException, ParseException {

		Category category = allCategories.get(categoryName);
		return getTasksInArray(getTargetTaskList(category, TYPE_FLOAT));
	}
	
	
	public ArrayList<Task> getCategoryEvents(String categoryName) 
			throws IOException, JSONException, ParseException {

		Category category = allCategories.get(categoryName);
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
	
	public ArrayList<Task> getUpcomingTasks() 
			throws IOException, JSONException, ParseException {
		ArrayList<Task> allTasks = getTasks();
		ArrayList<Task> upcomingTasks = new ArrayList<Task> ();
		
		for(Task task : allTasks) {
			if(task.getEndTime() >= getCurrentTime() && !task.getDone()) {
				upcomingTasks.add(task);
			}
		}
		
		return upcomingTasks;
	}
	
	public ArrayList<Task> getUpcomingEvents() 
			throws IOException, JSONException, ParseException {
		ArrayList<Task> allTasks = getEvents();
		ArrayList<Task> upcomingEvents = new ArrayList<Task> ();
		
		for(Task task : allTasks) {
			if(task.getStartTime() >= getCurrentTime() && !task.getDone()) {
				upcomingEvents.add(task);
			}
		}
		
		return upcomingEvents;
	}
	
	public ArrayList<Task> getCompletedTasks() 
			throws IOException, JSONException, ParseException {
		ArrayList<Task> allTasks = getTasks();
		ArrayList<Task> completedTasks = new ArrayList<Task> ();
		
		for(Task task : allTasks) {
			if(task.getDone()) {
				completedTasks.add(task);
			}
		}
		
		return completedTasks;
	}

	
	public ArrayList<Task> getOverdueTasks() 
			throws IOException, JSONException, ParseException {
		ArrayList<Task> allTasks = getTasks();
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

		Category category = allCategories.get(categoryName);
		category.setCategoryColour(colourId);
		storageFile.setAllDataToFile(allCategories);
	}
	
	
	public void setCategory(String taskType, int taskIndex, String categoryName) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void setUndone(String taskType, int taskIndex) 
			throws JsonParseException, JsonMappingException, JSONException, 
				IOException, ParseException {
		
		String taskId = getTaskId(taskType, taskIndex);
		setDone(taskId, false);
	}
	
	
	public void setDone(String taskType, int taskIndex) 
			throws JsonParseException, JsonMappingException, IOException, 
				JSONException, ParseException {
		
		String taskId = getTaskId(taskType, taskIndex);
		setDone(taskId, true);
	}
	
	
	public void setReminder(String taskType, int taskIndex, long reminderTime, String reminderDate) 
			throws JsonParseException, JsonMappingException, IOException, 
				JSONException, ParseException {
		
		String taskId = getTaskId(taskType, taskIndex);
		HashMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setReminderDate(reminderDate);
		targetTask.get(taskId).setReminder(reminderTime);
		storageFile.setAllDataToFile(allCategories);
	}
	
	
	public void setPriority(String taskType, int taskIndex, int priority) 
			throws IOException, JSONException, ParseException {
		
		String taskId = getTaskId(taskType, taskIndex);
		HashMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setPriority(priority);
		storageFile.setAllDataToFile(allCategories);	
	}
	
	
	public void setDescription(String taskType, int taskIndex, String description) 
			throws JsonParseException, JsonMappingException, IOException, JSONException, ParseException {
		
		String taskId = getTaskId(taskType, taskIndex);
		HashMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setDescription(description);
		storageFile.setAllDataToFile(allCategories);
	}
	
	
	public void setDeadline(String taskType, int taskIndex, long deadlineTime, String deadlineDate) 
			throws JsonParseException, JsonMappingException, IOException, 
				JSONException, ParseException {
		
		String taskId = getTaskId(taskType, taskIndex);
		HashMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setEndDate(deadlineDate);
		targetTask.get(taskId).setEndTime(deadlineTime);
		storageFile.setAllDataToFile(allCategories);
	}
	
	
	public void setSubTaskUndone(String taskType, int taskIndex) 
			throws JsonParseException, JsonMappingException, JSONException, IOException {
		
		
	}

	
	public void setSubTaskDone(String taskType, int taskIndex) 
			throws JsonParseException, JsonMappingException, IOException {
		
		
	}
	
	
	public void setSubtaskDescription(String taskType, int taskIndex, String description) 
			throws JsonParseException, JsonMappingException, IOException {
		
		
	}
	
	/****************************************************************************
	 * 									DELETE
	 ***************************************************************************/
	
	
	public void deleteAll() throws IOException {
		storageFile.setAllDataToFile(new HashMap<String, Category> ());
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
				allCategories.get(categoryName).setTasks(resetData);
				break;
			case TYPE_FLOAT:
				allCategories.get(categoryName).setFloatTasks(resetData);
				break;
			case TYPE_EVENT:
				allCategories.get(categoryName).setEvents(resetData);
				break;
		}
		storageFile.setAllDataToFile(allCategories);
	}

	
	public void deleteTask(String taskType, int taskIndex) throws IOException, JSONException, ParseException {
		
		String taskId = getTaskId(taskType, taskIndex);
		
		for(String categoryName : allCategories.keySet()) {
			Category category = allCategories.get(categoryName);
			HashMap<String, Task> tasks = category.getTasks();
			HashMap<String, Task> floatTasks = category.getFloatTasks();
			HashMap<String, Task> events = category.getEvents();
			
			switch(taskType) {
				case TYPE_TASK:
				for(String targetTaskId : tasks.keySet()) {
					if(targetTaskId.equals(taskId)) {
						tasks.remove(taskId);
						break;
					}
				}
				break;
				
				case TYPE_FLOAT:
				for(String targetTaskId : floatTasks.keySet()) {
					if(targetTaskId.equals(taskId)) {
						floatTasks.remove(taskId);
						break;
					}
				}
				break;
				
				case TYPE_EVENT:
				for(String targetTaskId : events.keySet()) {
					if(targetTaskId.equals(taskId)) {
						events.remove(taskId);
						break;
					}
				}
				break;
			}
		}
		storageFile.setAllDataToFile(allCategories);
	}
	
	
	public void deleteSubTask(String taskName, String subtaskDescription) {
		// TODO Auto-generated method stub
		
	}
	

	/*
	public void updateFile(ArrayList<Category> categories) throws IOException {
		
		storageFile.clearTextFromFile();
		
		for(Category category : categories) {
			CategoryWrapper categoryWrapper = addCategory(category.getCategoryName());
			categoryWrapper.setCategory(category);
			allCategories.put(category.getCategoryName(), categoryWrapper);
		}
		
		storageFile.setAllDataToFile(allCategories);
	}
	*/

	
	public void exitProgram() throws IOException {
		storageFile.exitProgram();
	}

	public void setIndex(Task task, String index) {
		task.setIndex(index);
	}
}