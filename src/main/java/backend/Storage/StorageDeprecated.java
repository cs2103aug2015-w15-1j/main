package main.java.backend.Storage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import main.java.backend.GeneralFunctions.GeneralFunctions;
import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.SubTask;
import main.java.backend.Storage.Task.Task;

public class StorageDeprecated {

	private static final String CATEGORY_DEFAULT = "default"; 
	private static final String TYPE_TASK = "task";
	private static final String TYPE_FLOAT = "floatTask";
	private static final String TYPE_EVENT = "event";
	private static final String RECUR_DAY = "day";
	private static final String RECUR_WEEK = "week";
	private static final String RECUR_MONTH = "month";
	private static final String RECUR_YEAR = "year";

	private TreeMap<String, Category> allData;
	
	private Data data;

	public StorageDeprecated(String fileName) { 
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
	
	private void setDone(String taskId, boolean isDone) {
		
		TreeMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setDone(isDone);
		
		if(isDone) {
			targetTask.get(taskId).setIndex(-1);
		}
		
		data.save(allData);
	}
	
	private void setTargetTaskList(Category category, String taskType, Task newTask) {
		switch(taskType) {
			case TYPE_TASK:
				TreeMap<String, Task> allTasks = category.getTasks();
				allTasks.put(newTask.getTaskId(), newTask);
				category.setTasks(allTasks);
				break;
			case TYPE_FLOAT:
				TreeMap<String, Task> allFloatingTasks = category.getFloatTasks();
				allFloatingTasks.put(newTask.getTaskId(), newTask);
				category.setFloatTasks(allFloatingTasks);
				break;
			case TYPE_EVENT:
				TreeMap<String, Task> allEvents = category.getEvents();
				allEvents.put(newTask.getTaskId(), newTask);
				category.setEvents(allEvents);
				break;
		}
		
		allData.put(category.getCategoryName(), category);
	}
	
	private TreeMap<String, Task> getTargetTaskList(Category category, String taskType) {
		switch(taskType) {
			case TYPE_TASK:
				return category.getTasks();
			case TYPE_FLOAT:
				return category.getFloatTasks();
			case TYPE_EVENT:
				return category.getEvents();
		}
		return new TreeMap<String, Task> ();
	}
	
	private TreeMap<String, Task> getAllTasks() {
		TreeMap<String, Task> allTasks = new TreeMap<String, Task> ();
		
		for(String categoryName : allData.keySet()) {
			Category category = allData.get(categoryName);
			
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
	
	private ArrayList<Task> getTargetTasks(String taskType) {
		
		ArrayList<Task> allTypeTasks = new ArrayList<Task> ();

		for(String category : allData.keySet()) {
			allTypeTasks.addAll(getTypeTaskArray(allData.get(category), taskType));
		}

		return allTypeTasks;
	}
	
	private ArrayList<Task> getTargetTasksDone(String taskType) {
		
		ArrayList<Task> tasksDone = new ArrayList<Task> ();
		ArrayList<Task> allTasks = getTargetTasks(taskType);

		int size = allTasks.size();

		for(int i = 0; i < size; i++) {
			Task task = allTasks.get(i);
			if(!task.getDone()) {
				tasksDone.add(task);
			}
		}
		
		return tasksDone;
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
	
	private ArrayList<Task> getTasksInArray(TreeMap<String, Task> tasks) {
		
		ArrayList<Task> allTasks = new ArrayList<Task> ();
		
		for(String taskId : tasks.keySet()) {
			allTasks.add(tasks.get(taskId));
		}
		
		return allTasks;
	}
	
	private String checkTaskType(Task task) {
		
		String taskType = TYPE_FLOAT;
		
		if(!task.getStartDate().isEmpty()) {
			taskType = TYPE_EVENT;
		} else if(!task.getEndDate().isEmpty()) {
			taskType = TYPE_TASK;
		} 
		
		return taskType;
	}
	
	private long getCurrentTime() {
		
		long currentMilliseconds = System.currentTimeMillis();
		SimpleDateFormat standardFormat = new SimpleDateFormat("EEE, dd MMM hh:mma yyyy");
		Date resultdate = new Date(currentMilliseconds);
		
		return GeneralFunctions.stringToMillisecond(standardFormat.format(resultdate));
    }
	
	private long getTime(Task task) {

		if(task.getEndTime() > 0) {
			return task.getEndTime();
		} else {
			return task.getStartTime();
		}
	}
	
	private String getTaskId(int index) {
		
		ArrayList<Task> allTasks = getTaskList();
		int size = allTasks.size();
		
		for(int i = 0; i < size; i++) {
			if(allTasks.get(i).getIndex() == index) {
				return allTasks.get(i).getTaskId();
			}
		}
		
		// Should not reach here
		return new String();
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
		TreeMap<String, Task> allTasks = getAllTasks();

		for(String taskId : allTasks.keySet()) {
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

		return getTargetTasksDone(TYPE_TASK);
	}
	
	
	public ArrayList<Task> getFloatingTasks() {

		return getTargetTasksDone(TYPE_FLOAT);
	}
	
	
	public ArrayList<Task> getEvents() {
		
		return getTargetTasksDone(TYPE_EVENT);
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
			
			if(getTime(task) >= getCurrentTime() && !task.getDone()) {
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
		
		String taskId = getTaskId(taskIndex);
		Task task = getAllTasks().get(taskId);
		String taskType = checkTaskType(task);
		
		deleteTask(taskIndex);
		
		task.setCategory(categoryName);
		addNewTask(categoryName, taskType, task);
		
		data.save(allData);
	}
	
	
	public void setName(int taskIndex, String name) {

		String taskId = getTaskId(taskIndex);
		TreeMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setName(name);
		data.save(allData);
	}
	
	
	public void setDescription(int taskIndex, String description) {

		String taskId = getTaskId(taskIndex);
		TreeMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setDescription(description);
		data.save(allData);
	}
	
	
	public void setStartDate(int taskIndex, long startTime, String startDate) {
		
		String taskId = getTaskId(taskIndex);
		TreeMap<String, Task> targetTask = getAllTasks();
		Task task = targetTask.get(taskId);
		
		deleteTask(taskIndex);
		task.setStartDate(startDate);
		task.setStartTime(startTime);
		
		addNewTask(task.getCategory(), checkTaskType(task), task);
		
		data.save(allData);
	}
	
	
	public void setDeadline(int taskIndex, long deadlineTime, String deadlineDate) {
		
		String taskId = getTaskId(taskIndex);
		TreeMap<String, Task> targetTask = getAllTasks();
		Task task = targetTask.get(taskId);
		
		deleteTask(taskIndex);
		task.setEndDate(deadlineDate);
		task.setEndTime(deadlineTime);
		
		addNewTask(task.getCategory(), checkTaskType(task), task);
		
		data.save(allData);
	}
	
	
	public void setReminder(int taskIndex, long reminderTime, String reminderDate) {
		
		String taskId = getTaskId(taskIndex);
		TreeMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setReminderDate(reminderDate);
		targetTask.get(taskId).setReminder(reminderTime);
		data.save(allData);
	}
	
	
	public void setPriority(int taskIndex, int priority) {
		
		String taskId = getTaskId(taskIndex);
		TreeMap<String, Task> targetTask = getAllTasks();
		targetTask.get(taskId).setPriority(priority);
		data.save(allData);	
	}
	
	
	public void setDone(int taskIndex) {
		
		String taskId = getTaskId(taskIndex);
		setDone(taskId, true);
	}
	
	
	public void setUndone(int taskIndex) {
		
		String taskId = getTaskId(taskIndex);
		setDone(taskId, false);
	}
	
	
	public void setSubTask(String taskIndex, String subtaskDescription) {

		SubTask subTask = new SubTask(subtaskDescription, false);
		addSubTask(taskIndex, subTask);
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
		data.save(null);
	}
	
	
	public void deleteCategory(String categoryName) {
		allData.remove(categoryName);
		data.save(allData);
	}
	
	
	public void deleteTaskTypeFromCategory(String categoryName, String taskType) {
		TreeMap<String, Task> resetData = new TreeMap<String, Task>();
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

	
	public void deleteTask(int taskIndex) {

		String taskId = getTaskId(taskIndex);
		Task task = getAllTasks().get(taskId);
		
		TreeMap<String, Task> tasks = getTargetTaskList
				(allData.get(task.getCategory()), checkTaskType(task));

		if(tasks.containsKey(taskId)) {
			tasks.remove(taskId);
		}
		
		data.save(allData);
	}
	
	
	public void deleteSubTask(String taskName, String subtaskDescription) {
		// TODO Auto-generated method stub
		
	}
	
	/****************************************************************************
	 * 									OTHERS
	 ***************************************************************************/

	public void saveData(ArrayList<Category> categories) {
		
		for(Category category : categories) {
			allData.get(category.getCategoryName()).setTasks(category.getTasks());
			allData.get(category.getCategoryName()).setFloatTasks(category.getFloatTasks());
			allData.get(category.getCategoryName()).setEvents(category.getEvents());
			allData.put(category.getCategoryName(), category);
		}
		
		data.save(allData);
	}

	public void setIndex(Task task, int index) {
		task.setIndex(index);
	}
}