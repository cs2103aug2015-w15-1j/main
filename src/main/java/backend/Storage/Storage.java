package main.java.backend.Storage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.SubTask;
import main.java.backend.Storage.Task.Task;

/**
 * This class performs all CRUD (Create, Read, Update, Delete) 
 * operations to manipulate the data stored in the text file.
 * 
 * @author A0126258A
 *
 */

public class Storage {

	private static final String CATEGORY_DEFAULT = "default"; 
	private static final String TYPE_TASK = "task";
	private static final String TYPE_FLOAT = "floatTask";
	private static final String TYPE_EVENT = "event";

	private TreeMap<String, Category> allData;
	
	private static final SimpleDateFormat formatterForDateTime = 
			new SimpleDateFormat("EEE, dd MMM hh:mma");
	
	private Data data;

	public Storage(String fileName) { 
		data = new Data(fileName);
		allData = data.load();
		System.out.println("Storage component initialised successfully");
	}
	
	/****************************************************************************
	 * 								HELPER METHODS
	 ***************************************************************************/
	
	private void addNewTask(String categoryName, String taskType, Task newTask) {

		if(categoryName.isEmpty()) {
			categoryName = CATEGORY_DEFAULT;
		}
		
		Category category = addCategory(categoryName);
		setTargetTaskList(category, taskType, newTask);
		data.save(allData);
	}
	
	private void addSubTask(String taskName, SubTask subTask) {
		
		Task targetTask = getAllTasks().get(taskName);
		TreeMap<String, SubTask> subTaskList = targetTask.getSubTask();
		subTaskList.put(subTask.getDescription(), subTask);
		data.save(allData);
	}
	
	private void setDone(int taskId, boolean isDone) {
		
		TreeMap<Integer, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setDone(isDone);
		data.save(allData);
	}
	
	@SuppressWarnings("static-access")
	private void setTargetTaskList(Category category, String taskType, Task newTask) {
		switch(taskType) {
			case TYPE_TASK:
				TreeMap<Integer, Task> allTasks = category.getTasks();
				allTasks.put(newTask.getTaskId(), newTask);
				category.setTasks(allTasks);
				break;
			case TYPE_FLOAT:
				TreeMap<Integer, Task> allFloatingTasks = category.getFloatTasks();
				allFloatingTasks.put(newTask.getTaskId(), newTask);
				category.setFloatTasks(allFloatingTasks);
				break;
			case TYPE_EVENT:
				TreeMap<Integer, Task> allEvents = category.getEvents();
				allEvents.put(newTask.getTaskId(), newTask);
				category.setEvents(allEvents);
				break;
		}
		
		allData.put(category.getCategoryName(), category);
	}
	
	private TreeMap<Integer, Task> getTargetTaskList(Category category, String taskType) {
		switch(taskType) {
			case TYPE_TASK:
				return category.getTasks();
			case TYPE_FLOAT:
				return category.getFloatTasks();
			case TYPE_EVENT:
				return category.getEvents();
		}
		return new TreeMap<Integer, Task> ();
	}
	
	private TreeMap<Integer, Task> getAllTasks() {
		TreeMap<Integer, Task> allTasks = new TreeMap<Integer, Task> ();
		
		for(String categoryName : allData.keySet()) {
			Category category = allData.get(categoryName);
			
			for(int taskId : category.getTasks().keySet()) {
				allTasks.put(taskId, category.getTasks().get(taskId));
			}
			
			for(int taskId : category.getFloatTasks().keySet()) {
				allTasks.put(taskId, category.getFloatTasks().get(taskId));
			}
			
			for(int taskId : category.getEvents().keySet()) {
				allTasks.put(taskId, category.getEvents().get(taskId));
			}
			
		}

		return allTasks;
	}
	
	private ArrayList<Task> getTargetTasks(String taskType) {
		
		ArrayList<Task> allTypeTasks = new ArrayList<Task> ();

		for(String category : allData.keySet()) {
			allTypeTasks.addAll(getTypeTaskArray(allData.get(category), taskType));
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
	
	private ArrayList<Task> getTasksInArray(TreeMap<Integer, Task> tasks) {
		
		ArrayList<Task> allTasks = new ArrayList<Task> ();
		
		for(int taskId : tasks.keySet()) {
			allTasks.add(tasks.get(taskId));
		}
		
		return allTasks;
	}
	
	public static long stringToMillisecond(String dateTime) {
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
	
	@SuppressWarnings("static-access")
	private int getTaskId(int taskId) {
		
		switch(taskType) {
			case TYPE_TASK:
				return getTasks().get(taskId - 1).getTaskId();
			case TYPE_FLOAT:
				return getFloatingTasks().get(taskId - 1).getTaskId();
			case TYPE_EVENT:
				return getEvents().get(taskId - 1).getTaskId();
		}
		
		// Should not reach here
		return -1;
	}
	
	/****************************************************************************
	 * 									CREATE
	 ***************************************************************************/
	

	public Category addCategory(String categoryName) {
		
		Category category = allData.get(categoryName);
		
		if(!allData.containsKey(categoryName)) {
			category = new Category(categoryName);
			allData.put(categoryName, category);
			data.save(allData);
		}
		
		return category;
	}
	
	
	public Task addFloatingTask(Task task) {
		
		addNewTask(task.getCategory(), TYPE_FLOAT, task);
		return task;
	}
	
	public Task addTask(Task task) {

		addNewTask(task.getCategory(), TYPE_TASK, task);
		return task;
	}
	
	public Task addEvent(Task task) {

		addNewTask(task.getCategory(), TYPE_EVENT, task);
		return task;
	}
	
	public void addSubTask(String taskName, String subtaskDescription) {

		SubTask subTask = new SubTask(subtaskDescription, false);
		addSubTask(taskName, subTask);
	}
	
	/****************************************************************************
	 * 									READ
	 ***************************************************************************/
	
	
	public ArrayList<String> getCategories() {
		
		ArrayList<String> categories = new ArrayList<String> ();
		
		for(String name : allData.keySet()) {
			if(!name.isEmpty()) {
				categories.add(name);
			}
		}
		
		return categories;
	}
	
	
	public ArrayList<Category> getCategoryList() {
		
		ArrayList<Category> categoryList = new ArrayList<Category> ();
		allData = data.load();
		
		for(String categoryName : allData.keySet()) {
			categoryList.add(allData.get(categoryName));
		}
		
		return categoryList;
	}

	
	public ArrayList<Task> getTaskList() {

		ArrayList<Task> taskList = new ArrayList<Task> ();
		TreeMap<Integer, Task> allTasks = getAllTasks();

		for(int taskId : allTasks.keySet()) {
			taskList.add(allTasks.get(taskId));
		}

		return taskList;
	}
	
	
	public ArrayList<Task> getCategoryAllTasks(String categoryName) {
		
		ArrayList<Task> allCategoryTasks = new ArrayList<Task> ();
		Category category = allData.get(categoryName);
		
		allCategoryTasks.addAll(getTasksInArray(category.getTasks()));
		allCategoryTasks.addAll(getTasksInArray(category.getFloatTasks()));
		allCategoryTasks.addAll(getTasksInArray(category.getEvents()));

		return allCategoryTasks;
	}
	
	
	public ArrayList<Task> getCategoryTasks(String categoryName) {

		Category category = allData.get(categoryName);
		return getTasksInArray(getTargetTaskList(category, TYPE_TASK));
	}
	
	
	public ArrayList<Task> getCategoryFloatingTasks(String categoryName) {

		Category category = allData.get(categoryName);
		return getTasksInArray(getTargetTaskList(category, TYPE_FLOAT));
	}
	
	
	public ArrayList<Task> getCategoryEvents(String categoryName) {

		Category category = allData.get(categoryName);
		return getTasksInArray(getTargetTaskList(category, TYPE_EVENT));
	}
	
	
	public ArrayList<Task> getTasks() {
		
		return getTargetTasks(TYPE_TASK);
	}
	
	
	public ArrayList<Task> getFloatingTasks() {
		
		return getTargetTasks(TYPE_FLOAT);
	}
	
	
	public ArrayList<Task> getEvents() {
		
		return getTargetTasks(TYPE_EVENT);
	}
	
	public ArrayList<Task> getUpcomingTasks() {
		ArrayList<Task> allTasks = getTasks();
		ArrayList<Task> upcomingTasks = new ArrayList<Task> ();
		
		for(Task task : allTasks) {
			if(task.getEndTime() >= getCurrentTime() && !task.getDone()) {
				upcomingTasks.add(task);
			}
		}
		
		return upcomingTasks;
	}
	
	public ArrayList<Task> getUpcomingEvents() {
		ArrayList<Task> allTasks = getEvents();
		ArrayList<Task> upcomingEvents = new ArrayList<Task> ();
		
		for(Task task : allTasks) {
			if(task.getStartTime() >= getCurrentTime() && !task.getDone()) {
				upcomingEvents.add(task);
			}
		}
		
		return upcomingEvents;
	}
	
	public ArrayList<Task> getCompletedTasks() {
		ArrayList<Task> allTasks = getTasks();
		ArrayList<Task> completedTasks = new ArrayList<Task> ();
		
		for(Task task : allTasks) {
			if(task.getDone()) {
				completedTasks.add(task);
			}
		}
		
		return completedTasks;
	}

	
	public ArrayList<Task> getOverdueTasks() {
		ArrayList<Task> allTasks = getTasks();
		ArrayList<Task> overdueTasks = new ArrayList<Task> ();
		
		for(Task task : allTasks) {
			if(task.getEndTime() < getCurrentTime()) {
				overdueTasks.add(task);
			}
		}
		
		return overdueTasks;
	}
	
	/****************************************************************************
	 * 									UPDATE
	 ***************************************************************************/
	
	
	public void setCategoryColour(String categoryName, String colourId) {

		Category category = allData.get(categoryName);
		category.setCategoryColour(colourId);
		data.save(allData);
	}
	
	
	public void setCategory(int taskIndex, String categoryName) {
		// TODO Auto-generated method stub
		
	}
	
	public void setDescription(int taskIndex, String description) {

		int taskId = getTaskId(taskIndex);
		TreeMap<Integer, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setDescription(description);
		data.save(allData);
	}
	
	public void setUndone(int taskIndex) {
		
		int taskId = getTaskId(taskIndex);
		setDone(taskId, false);
	}
	
	
	public void setDone(int taskIndex) {
		
		int taskId = getTaskId(taskIndex);
		setDone(taskId, true);
	}
	
	
	public void setReminder(int taskIndex, long reminderTime, String reminderDate) {
		
		int taskId = getTaskId(taskIndex);
		TreeMap<Integer, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setReminderDate(reminderDate);
		targetTask.get(taskId).setReminder(reminderTime);
		data.save(allData);
	}
	
	
	public void setPriority(int taskIndex, int priority) {
		
		int taskId = getTaskId(taskIndex);
		TreeMap<Integer, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setPriority(priority);
		data.save(allData);	
	}

	
	
	public void setDeadline(int taskIndex, long deadlineTime, String deadlineDate) {
		
		int taskId = getTaskId(taskIndex);
		TreeMap<Integer, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setEndDate(deadlineDate);
		targetTask.get(taskId).setEndTime(deadlineTime);
		data.save(allData);
	}
	
	
	public void setSubTaskUndone(int taskIndex) {
		
		
	}

	
	public void setSubTaskDone(int taskIndex) {
		
		
	}
	
	
	public void setSubtaskDescription(int taskIndex, String description) {
		
		
	}
	
	/****************************************************************************
	 * 									DELETE
	 ***************************************************************************/
	
	
	public void deleteAll() {
		data.save(new TreeMap<String, Category> ());
	}
	
	
	public void deleteCategory(String categoryName) {
		allData.remove(categoryName);
		data.save(allData);
	}
	
	
	public void deleteTaskTypeFromCategory(String categoryName, String taskType) {
		TreeMap<Integer, Task> resetData = new TreeMap<Integer, Task>();
		switch(taskType) {
			case TYPE_TASK:
				allData.get(categoryName).setTasks(resetData);
				break;
			case TYPE_FLOAT:
				allData.get(categoryName).setFloatTasks(resetData);
				break;
			case TYPE_EVENT:
				allData.get(categoryName).setEvents(resetData);
				break;
		}
		data.save(allData);
	}

	
	public void deleteTask(String taskType, int taskIndex) {
		
		int taskId = getTaskId(taskIndex);
		
		for(String categoryName : allData.keySet()) {
			Category category = allData.get(categoryName);
			TreeMap<Integer, Task> tasks = getTargetTaskList(category, taskType);
			
			if(tasks.containsKey(taskId)) {
				tasks.remove(taskId);
				break;
			}
		}
		data.save(allData);
	}
	
	
	public void deleteSubTask(String taskName, String subtaskDescription) {
		// TODO Auto-generated method stub
		
	}
	

	public void updateFile(ArrayList<Category> categories) {
		
		for(Category category : categories) {
			allData.get(category).setTasks(category.getTasks());
			allData.get(category).setFloatTasks(category.getFloatTasks());
			allData.get(category).setEvents(category.getFloatTasks());
			allData.put(category.getCategoryName(), category);
		}
		
		data.save(allData);
	}

	public void setIndex(Task task, int index) {
		task.setIndex(index);
	}
}