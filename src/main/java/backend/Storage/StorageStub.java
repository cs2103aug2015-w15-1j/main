package main.java.backend.Storage;

import java.util.ArrayList;

import main.java.backend.Storage.Task.Task;

public class StorageStub {
	private static ArrayList<Task> storageStub;
	private static ArrayList<String> categoryList;

	public StorageStub(String filename) {
		storageStub = new ArrayList<Task>();
		categoryList = new ArrayList<String>();
	}

	public void addFloatingTask(String taskName, String taskDescription, String priority, String reminder,
			String category) {
		Task newFloatingTask = new Task(taskName,taskDescription,priority,reminder,category);
		storageStub.add(newFloatingTask);
	}

	public void addEvent(String eventName, String eventDescription, String startDate, String endDate, String startTime,
			String endTime, String priority, String reminder, String category) {
		Task newEvent = new Task(eventName,eventDescription,startDate,endDate,startTime,endTime,priority,reminder,category);
		storageStub.add(newEvent);
	}

	public void addTask(String taskName, String taskDescription, String deadline, String priority, String reminder,
			String category) {
		Task newTask = new Task(taskName,taskDescription,deadline,priority,reminder,category);
		storageStub.add(newTask);
	}

	public void setUndone(String taskID) {
		// TODO Auto-generated method stub
		
	}

	public void setDone(String taskID) {
		// TODO Auto-generated method stub
		
	}

	public void setCol(String categoryName, String colourId) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<String> getCategoryList() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCategory(String taskId, String categoryName) {
		// TODO Auto-generated method stub
		
	}

	public void addCategory(String categoryName) {
		// TODO Auto-generated method stub
		
	}

	public void addSubTask(String taskId, String subtaskDescription) {
		// TODO Auto-generated method stub
		
	}

	public void subTask(String taskId) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Task> getCurrentState() {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateState(ArrayList<Task> currentState) {
		// TODO Auto-generated method stub
		
	}

	public void setReminder(String taskId, String reminder) {
		// TODO Auto-generated method stub
		
	}

	public void setDescription(String taskId, String description) {
		// TODO Auto-generated method stub
		
	}

	public void setDeadline(String taskId, String deadline) {
		// TODO Auto-generated method stub
		
	}

	public void delete(String taskId) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Task> getTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Task> getFloatingTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Task> getEvents() {
		// TODO Auto-generated method stub
		return null;
	}

}
