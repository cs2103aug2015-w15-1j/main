package main.java.backend.Logic;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import main.java.backend.GeneralFunctions.GeneralFunctions;
import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Task;

public class LogicGetter {
	
	public static final long DAY_IN_MILLISECOND = 86400000L;

	private static LogicGetter logicGetterObject;
	private Storage storage;
	
	private TreeMap<Integer, Task> taskList;
	
	public static LogicGetter getInstance(Storage storageComponent) {
		
		if (logicGetterObject == null) {
			logicGetterObject = new LogicGetter(storageComponent);
		}
		return logicGetterObject;
	}

	private LogicGetter(Storage storage) {
		this.storage = storage;
	}
	
	private long getTodayStartTime() {

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime midnight = now.toLocalDate().atStartOfDay();
		Date resultDate = Date.from(midnight
				.atZone(ZoneId.systemDefault()).toInstant());

		return GeneralFunctions.stringToMillisecond(
				GeneralFunctions.standardFormat.format(resultDate));
	}

	private long getTodayEndTime() {

		return getTodayStartTime() + DAY_IN_MILLISECOND;
	}

	private ArrayList<Task> getUpcoming(ArrayList<Task> allTasks) {

		ArrayList<Task> upcomingTasks = new ArrayList<Task> ();

		for(Task task : allTasks) {
			if(GeneralFunctions.stringToMillisecond(task.getEnd()) >= 
					GeneralFunctions.getCurrentTime() && !task.getDone()) {
				upcomingTasks.add(task);
			}
		}

		return upcomingTasks;
	}
	
	private ArrayList<Task> getCompleted(ArrayList<Task> allTasks) {
		
		ArrayList<Task> completedTasks = new ArrayList<Task> ();
		
		for(Task task : allTasks) {
			if(task.getDone()) {
				completedTasks.add(task);
			}
		}
		
		return completedTasks;
	}

	private ArrayList<Task> getOverdue(ArrayList<Task> allTasks) {

		ArrayList<Task> overdueTasks = new ArrayList<Task> ();

		for(Task task : allTasks) {
			if(GeneralFunctions.stringToMillisecond(task.getEnd()) 
					< GeneralFunctions.getCurrentTime()) {
				overdueTasks.add(task);
			}
		}

		return overdueTasks;
	}
	
	/*
	private ArrayList<String> getCategories() {
		
		ArrayList<String> categories = new ArrayList<String> ();

		for(String name : storage.load().keySet()) {
			if(!name.isEmpty()) {
				categories.add(name);
			}
		}

		return categories;
	}
	*/
	

	private ArrayList<Task> getSearchResultsList() {
		return null;
	}
	
	private ArrayList<Task> getFloatingTasks() {
		
		ArrayList<Task> allFloatingTasks = new ArrayList<Task> ();
		taskList = storage.load();
		
		for(int taskId : taskList.keySet()) {
			
			Task task = taskList.get(taskId);
			if(task.getStart().isEmpty() 
					&& task.getEnd().isEmpty()) {
				allFloatingTasks.add(task);
			}
		}
		
		return allFloatingTasks;
	}

	private ArrayList<Task> getToDos() {
		
		ArrayList<Task> allToDos = new ArrayList<Task> ();
		taskList = storage.load();
		
		for(int taskId : taskList.keySet()) {
			
			Task task = taskList.get(taskId);
			if(task.getStart().isEmpty() 
					&& !task.getEnd().isEmpty()) {
				allToDos.add(task);
			}
		}
		
		return allToDos;
	}

	private ArrayList<Task> getEvents() {
		
		ArrayList<Task> allEvents = new ArrayList<Task> ();
		taskList = storage.load();
		
		for(int taskId : taskList.keySet()) {
			
			Task task = taskList.get(taskId);
			if(!task.getStart().isEmpty()) {
				allEvents.add(task);
			}
		}
		
		return allEvents;
	}
	
	private ArrayList<Task> getTodayToDos() {

		ArrayList<Task> allToDos = getToDos();
		ArrayList<Task> todayToDos = new ArrayList<Task> ();

		for(Task task : allToDos) {
			if(GeneralFunctions.stringToMillisecond(task.getEnd()) 
					>= getTodayStartTime()
					&& GeneralFunctions.stringToMillisecond(task.getEnd()) 
					< getTodayEndTime()) {
				todayToDos.add(task);
			}
		}

		return todayToDos;
	}
	
	private ArrayList<Task> getTodayEvents() {

		ArrayList<Task> allEvents = getEvents();
		ArrayList<Task> todayEvents = new ArrayList<Task> ();

		for(Task task : allEvents) {
			if(GeneralFunctions.stringToMillisecond(task.getStart()) 
					>= getTodayStartTime()
					&& GeneralFunctions.stringToMillisecond(task.getStart()) 
					< getTodayEndTime()) {
				todayEvents.add(task);
			}
		}

		return todayEvents;
	}
	
	private ArrayList<Task> getAllToday() {
		
		ArrayList<Task> todayTasks = getTodayToDos();
		todayTasks.addAll(getTodayEvents());
		
		return todayTasks;
	}
	
	private ArrayList<Task> getAllOverdue() {

		ArrayList<Task> overdueTasks = getToDos();
		overdueTasks.addAll(getEvents());
		
		return getOverdue(overdueTasks);
	}
	
	private ArrayList<Task> getPastEvents() {
		
		ArrayList<Task> pastEvents = new ArrayList<Task> ();

		for(Task task : pastEvents) {
			if(task.getDone()) {
				pastEvents.add(task);
			}
		}

		return pastEvents;
	}
	
	private ArrayList<Task> getCompletedFloats() {

		return getCompleted(getFloatingTasks());
	}

	private ArrayList<Task> getCompletedToDos() {

		return getCompleted(getToDos());
	}
	
	private ArrayList<Task> getUpcomingToDos() {

		return getUpcoming(getToDos());
	}

	private ArrayList<Task> getUpcomingEvents() {

		return getUpcoming(getEvents());
	}
	
	public ArrayList<String> retrieveStringData(String dataType) {
		
		ArrayList<String> data = new ArrayList<String>();
		switch (dataType) {
			case ("categories") :
//				data = getCategories();
				break;
		}
		return data;
	}

	/*
	public ArrayList<Category> retrieveCategoryData(String dataType) {
		
		ArrayList<Category> data = new ArrayList<Category>();
		switch (dataType) {
			case ("searchResults") :
				data = getSearchResultsList();
				break;
		}
		return data;
	}
	*/

	public ArrayList<Task> retrieveTaskData(String dataType) {
		
		ArrayList<Task> data = new ArrayList<Task>();
		switch(dataType) {
			case ("ToDo") :
				data = getToDos();
				break;
			case ("floating") :
				data = getFloatingTasks();
				break;
			case ("events") :
				data = getEvents();
				break;
			case ("todayTasks") :
				data = getAllToday();
				break;
			case ("todayToDos") :
				data = getTodayToDos();
				break;
			case ("todayEvents") :
				data = getTodayEvents();
				break;
			case ("overdueTasks") :
				data = getAllOverdue();
				break;
			case ("pastEvents") :
				data = getPastEvents();
				break;
			case ("completedFloats") :
				data = getCompletedFloats();
				break;
			case ("completedToDo") :
				data = getCompletedToDos();
				break;
			case ("upcomingToDo") :
				data = getUpcomingToDos();
				break;
			case ("upcomingEvents") :
				data = getUpcomingEvents();
				break;
		}
		return data;
	}
	
}