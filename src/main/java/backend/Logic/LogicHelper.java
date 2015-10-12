package main.java.backend.Logic;

import java.util.ArrayList;
import java.util.TreeMap;

import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.Task;

public class LogicHelper {
	
	private static final String EXECUTION_COMMAND_UNSUCCESSFUL = "Invalid Command. Please try again.";
	
	private static final String CATEGORY_DEFAULT = "default"; 
	private static final String TYPE_TODO = "task";
	private static final String TYPE_FLOAT = "floatTask";
	private static final String TYPE_EVENT = "event";
	
	public LogicHelper() {
		
	}
	
	public TreeMap<String, Category> addNewTask(TreeMap<String, Category> allData, 
			String categoryName, String taskType, Task newTask) {

		if(categoryName.isEmpty()) {
			categoryName = CATEGORY_DEFAULT;
		}
		
		Category category = getCategory(allData, categoryName);
		setTargetTaskList(category, taskType, newTask);
		
		return allData;
	}
	
	public TreeMap<String, Category> addCategory(
			TreeMap<String, Category> allData, String categoryName) {

		Category category = allData.get(categoryName);

		if(!allData.containsKey(categoryName)) {
			category = new Category(categoryName);
			allData.put(categoryName, category);
		}

		return allData;
	}
	
	public Category getCategory(TreeMap<String, Category> allData, 
			String categoryName) {

		Category category = allData.get(categoryName);

		if(!allData.containsKey(categoryName)) {
			category = new Category(categoryName);
			allData.put(categoryName, category);
		}

		return category;
	}
	
	public String priorityChecker(int priority) {
		
		if (priority > 5 || priority < 1) {
			return EXECUTION_COMMAND_UNSUCCESSFUL;
		}
		
		return null;
	}
	
	public void setTargetTaskList(Category category, String taskType, Task newTask) {
		switch(taskType) {
			case TYPE_TODO:
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
		
		// !!!!!!
		//allData.put(category.getCategoryName(), category);
	}
	
	public TreeMap<String, Task> getTargetTaskList(Category category, String taskType) {
		switch(taskType) {
			case TYPE_TODO:
				return category.getTasks();
			case TYPE_FLOAT:
				return category.getFloatTasks();
			case TYPE_EVENT:
				return category.getEvents();
		}
		return new TreeMap<String, Task> ();
	}
	
	public TreeMap<String, Task> getAllTasks(TreeMap<String, Category> allData) {
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
	
	public ArrayList<Task> getTargetTasks(TreeMap<String, Category> allData, String taskType) {
		
		ArrayList<Task> allTypeTasks = new ArrayList<Task> ();

		for(String category : allData.keySet()) {
			allTypeTasks.addAll(getTypeTaskArray(allData.get(category), taskType));
		}

		return allTypeTasks;
	}
	
	public ArrayList<Task> getTargetTasksDone(TreeMap<String, Category> allData, String taskType) {
		
		ArrayList<Task> tasksDone = new ArrayList<Task> ();
		ArrayList<Task> allTasks = getTargetTasks(allData, taskType);

		int size = allTasks.size();

		for(int i = 0; i < size; i++) {
			Task task = allTasks.get(i);
			if(!task.getDone()) {
				tasksDone.add(task);
			}
		}
		
		return tasksDone;
	}
	
	public ArrayList<Task> getTypeTaskArray(Category category, String taskType) {
		ArrayList<Task> typeTask = new ArrayList<Task> ();
		
		switch(taskType) {
			case TYPE_TODO:
				return getTasksInArray(category.getTasks());
			case TYPE_FLOAT:
				return getTasksInArray(category.getFloatTasks());
			case TYPE_EVENT:
				return getTasksInArray(category.getEvents());
		}
		
		return typeTask;
	}
	
	public ArrayList<Task> getTasksInArray(TreeMap<String, Task> tasks) {
		
		ArrayList<Task> allTasks = new ArrayList<Task> ();
		
		for(String taskId : tasks.keySet()) {
			allTasks.add(tasks.get(taskId));
		}
		
		return allTasks;
	}
	
	public String checkTaskType(Task task) {
		
		String taskType = TYPE_FLOAT;
		
		if(!task.getStartDate().isEmpty()) {
			taskType = TYPE_EVENT;
		} else if(!task.getEndDate().isEmpty()) {
			taskType = TYPE_TODO;
		} 
		
		return taskType;
	}
	
	public String getTaskId(TreeMap<String, Category> allData, int index) {
		
		ArrayList<Task> allTasks = getTaskList(allData);
		int size = allTasks.size();
		
		for(int i = 0; i < size; i++) {
			if(allTasks.get(i).getIndex() == index) {
				return allTasks.get(i).getTaskId();
			}
		}
		
		// Should not reach here
		return new String();
	}
	
	public ArrayList<Task> getTaskList(TreeMap<String, Category> allData) {

		ArrayList<Task> taskList = new ArrayList<Task> ();
		TreeMap<String, Task> allTasks = getAllTasks(allData);

		for(String taskId : allTasks.keySet()) {
			taskList.add(allTasks.get(taskId));
		}

		return taskList;
	}

}
