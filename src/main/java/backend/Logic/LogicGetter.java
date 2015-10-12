package main.java.backend.Logic;

import java.util.ArrayList;

import main.java.backend.GeneralFunctions.GeneralFunctions;
import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.Task;

public class LogicGetter {
	
	private static final String TYPE_TODO = "task";
	private static final String TYPE_FLOAT = "floatTask";
	private static final String TYPE_EVENT = "event";
	
	private static LogicGetter logicGetterObject;
	private LogicToStorage logicToStorage;
	private Storage storage;

	private LogicGetter(Storage storage) {
		logicToStorage = LogicToStorage.getInstance();
		this.storage = storage;
	}

	public static LogicGetter getInstance(Storage storageComponent) {
		if (logicGetterObject == null) {
			logicGetterObject = new LogicGetter(storageComponent);
		}
		return logicGetterObject;
	}
	
	public ArrayList<String> retrieveStringData(String dataType) {
		ArrayList<String> data = new ArrayList<String>();
		switch (dataType) {
			case ("categories") :
				data = getCategories();
				break;
		}
		return data;
	}

	public ArrayList<Category> retrieveCategoryData(String dataType) {
		ArrayList<Category> data = new ArrayList<Category>();
		switch (dataType) {
			case ("searchResults") :
				data = getSearchResultsList();
				break;
		}
		return data;
	}

	public ArrayList<Task> retrieveTaskData(String dataType) {
		ArrayList<Task> data = new ArrayList<Task>();
		switch(dataType) {
			case ("toDo") :
				data = getToDo();
				break;
			case ("floating") :
				data = getFloatingTasks();
				break;
			case ("events") :
				data = getEvents();
				break;
			case ("upcomingToDo") :
				data = getUpcomingToDo();
				break;
			case ("upcomingEvents") :
				data = getUpcomingEvents();
				break;
			case ("overdueTasks") :
				data = getOverdueTasks();
				break;
		}
		return data;
	}
	
	public ArrayList<String> getCategories() {
		ArrayList<String> categories = new ArrayList<String> ();

		for(String name : storage.load().keySet()) {
			if(!name.isEmpty()) {
				categories.add(name);
			}
		}

		return categories;
	}

	public ArrayList<Category> getSearchResultsList() {
		return null;
	}

	public ArrayList<Task> getToDo() {
		return logicToStorage.getTargetTasksDone(storage.load(), TYPE_TODO);	
	}

	public ArrayList<Task> getFloatingTasks() {
		return logicToStorage.getTargetTasksDone(storage.load(), TYPE_FLOAT);
	}

	public ArrayList<Task> getEvents() {
		return logicToStorage.getTargetTasksDone(storage.load(), TYPE_EVENT);
	}
	
	public ArrayList<Task> getUpcomingToDo() {
		
		ArrayList<Task> allTasks = getToDo();
		ArrayList<Task> upcomingTasks = new ArrayList<Task> ();
		
		for(Task task : allTasks) {
			if(task.getEndTime() >= GeneralFunctions.getCurrentTime() && !task.getDone()) {
				upcomingTasks.add(task);
			}
		}
		
		return upcomingTasks;
	}
	
	public ArrayList<Task> getUpcomingEvents() {
		
		ArrayList<Task> allTasks = getEvents();
		ArrayList<Task> upcomingEvents = new ArrayList<Task> ();
		
		for(Task task : allTasks) {
			
			if(GeneralFunctions.getTime(task) >= 
					GeneralFunctions.getCurrentTime() && !task.getDone()) {
				upcomingEvents.add(task);
			}
		}
		
		return upcomingEvents;
	}

	public ArrayList<Task> getOverdueTasks() {
		
		ArrayList<Task> allTasks = getToDo();
		ArrayList<Task> overdueTasks = new ArrayList<Task> ();
		
		for(Task task : allTasks) {
			if(task.getEndTime() < GeneralFunctions.getCurrentTime()) {
				overdueTasks.add(task);
			}
		}
		
		return overdueTasks;
	}
}