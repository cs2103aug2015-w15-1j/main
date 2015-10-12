package main.java.backend.Logic;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import main.java.backend.GeneralFunctions.GeneralFunctions;
import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.Task;

public class LogicGetter {
	
	public static final long DAY_IN_MILLISECOND = 86400000L;

	private static final String TYPE_TODO = "task";
	private static final String TYPE_FLOAT = "floatTask";
	private static final String TYPE_EVENT = "event";

	private static LogicGetter logicGetterObject;
	private LogicToStorage logicToStorage;
	private Storage storage;
	
	public static LogicGetter getInstance(Storage storageComponent) {
		
		if (logicGetterObject == null) {
			logicGetterObject = new LogicGetter(storageComponent);
		}
		return logicGetterObject;
	}

	private LogicGetter(Storage storage) {
		
		logicToStorage = LogicToStorage.getInstance();
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
			if(GeneralFunctions.stringToMillisecond(task.getEndDate()) >= 
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
			if(GeneralFunctions.stringToMillisecond(task.getEndDate()) 
					< GeneralFunctions.getCurrentTime()) {
				overdueTasks.add(task);
			}
		}

		return overdueTasks;
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
				data = getAllOverdue();
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
	
	public ArrayList<Task> getTodayEvents() {

		ArrayList<Task> allEvents = getEvents();
		ArrayList<Task> todayEvents = new ArrayList<Task> ();

		for(Task task : allEvents) {
			if(GeneralFunctions.stringToMillisecond(task.getStartDate()) 
					>= getTodayStartTime()
					&& GeneralFunctions.stringToMillisecond(task.getStartDate()) 
					< getTodayEndTime()) {
				todayEvents.add(task);
			}
		}

		return todayEvents;
	}
	
	public ArrayList<Task> getAllToday() {
		
		ArrayList<Task> todayTasks = getUpcomingToDo();
		todayTasks.addAll(getTodayEvents());
		
		return todayTasks;
	}
	
	public ArrayList<Task> getAllOverdue() {

		ArrayList<Task> overdueTasks = getToDo();
		overdueTasks.addAll(getEvents());
		
		return getOverdue(overdueTasks);
	}
	
	public ArrayList<Task> getPastEvents() {
		
		ArrayList<Task> pastEvents = new ArrayList<Task> ();

		for(Task task : pastEvents) {
			if(task.getDone()) {
				pastEvents.add(task);
			}
		}

		return pastEvents;
	}
	
	public ArrayList<Task> getCompletedFloat() {

		return getCompleted(getFloatingTasks());
	}

	public ArrayList<Task> getCompletedToDo() {

		return getCompleted(getToDo());
	}
	
	public ArrayList<Task> getUpcomingToDo() {

		return getUpcoming(getToDo());
	}

	public ArrayList<Task> getUpcomingEvents() {

		return getUpcoming(getEvents());
	}
	
}