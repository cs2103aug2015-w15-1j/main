package main.java.backend.Logic;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Task;
import main.java.backend.Storage.Task.Task.TaskType;

public class Observer {
	
	private static final SimpleDateFormat standardFormat = 
			new SimpleDateFormat("EEE, dd MMM yy, hh:mma");
	private static final SimpleDateFormat standardFormatNoMinute = 
			new SimpleDateFormat("EEE, dd MMM yy, hha");
	
	private static final long DAY_IN_MILLISECOND = 86400000L;

	private static Observer logicGetterObject;
	private Storage storage;
	
	private TreeMap<Integer, Task> taskList;
	private ArrayList<Task> getTasks;
	private ArrayList<Task> getEvents;
	private ArrayList<Task> getOverdue;
	private ArrayList<Task> getFloat;
	private ArrayList<String> getCate;
	private ArrayList<Task> getTodayTasks;
	private ArrayList<Task> getTodayEvents;
	private ArrayList<Task> getFocusList;
	
	private ArrayList<Task> getCompletedTasks;
	private ArrayList<Task> getCompletedEvents;
	private ArrayList<Task> getCompletedFloat;
	
	public static Observer getInstance(Storage storageComponent) {
		
		if (logicGetterObject == null) {
			logicGetterObject = new Observer(storageComponent);
		}
		return logicGetterObject;
	}

	private Observer(Storage storage) {
		this.storage = storage;
	}
	
	private long stringToMillisecond(String dateTime) {
		
		long dateTimeMillisecond = -1;
		Date tempDateTime = new Date();
		
		try {
			if(dateTime.contains(":")) {
				tempDateTime = standardFormat.parse(dateTime);
			} else {
				tempDateTime = standardFormatNoMinute.parse(dateTime);
			}
			dateTimeMillisecond = tempDateTime.getTime();
		} catch (java.text.ParseException e) {
			//e.printStackTrace();
		}

		return dateTimeMillisecond;
	}

	private long getCurrentTime() {

		long currentMilliseconds = System.currentTimeMillis();
		Date resultdate = new Date(currentMilliseconds);

		return stringToMillisecond(standardFormat.format(resultdate));
	}
	
	private long getTodayStartTime() {

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime midnight = now.toLocalDate().atStartOfDay();
		Date resultDate = Date.from(midnight
				.atZone(ZoneId.systemDefault()).toInstant());

		return stringToMillisecond(
				standardFormat.format(resultDate));
	}

	private long getTodayEndTime() {

		return getTodayStartTime() + DAY_IN_MILLISECOND;
	}

	private ArrayList<Task> getUpcoming(ArrayList<Task> allTasks, TaskType taskType) {

		ArrayList<Task> upcomingTasks = new ArrayList<Task> ();

		for(Task task : allTasks) {
			if(taskType == TaskType.TODO && stringToMillisecond(task.getEnd()) 
					> getCurrentTime() && !task.getDone()) {
				upcomingTasks.add(task);
			} else if(taskType == TaskType.EVENT && stringToMillisecond(task.getStart()) 
					> getCurrentTime() && !task.getDone()) {
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
	
	private String replace(String taskDate) {
		taskDate = taskDate.replace("am", "AM");
		taskDate = taskDate.replace("pm", "PM");
		return taskDate;
	}

	private ArrayList<Task> getOverdue(ArrayList<Task> allTasks, TaskType taskType) {

		ArrayList<Task> overdueTasks = new ArrayList<Task> ();

		for(Task task : allTasks) {
			if(taskType == TaskType.TODO && stringToMillisecond(replace(task.getEnd())) 
					<= getCurrentTime() && !task.getDone()) {
				overdueTasks.add(task);
			} else if(taskType == TaskType.EVENT && stringToMillisecond(replace(task.getStart())) 
					<= getCurrentTime() && !task.getDone()) {
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
	
	private ArrayList<Task> getTasks(TaskType taskType) {
		
		ArrayList<Task> tasks = new ArrayList<Task> ();
		taskList = storage.load();
		
		for(int taskId : taskList.keySet()) {
			
			Task task = taskList.get(taskId);
			if(task.getTaskType().equals(taskType)) {
				tasks.add(task);
			}
		}
		
		return tasks;
	}
	
	private ArrayList<Task> getTodayToDos() {

		ArrayList<Task> allToDos = getToDos();
		ArrayList<Task> todayToDos = new ArrayList<Task> ();

		for(Task task : allToDos) {
			if(stringToMillisecond(task.getEnd()) 
					>= getTodayStartTime()
					&& stringToMillisecond(task.getEnd()) 
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
			if(stringToMillisecond(task.getStart()) 
					>= getTodayStartTime()
					&& stringToMillisecond(task.getStart()) 
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

		ArrayList<Task> overdueTasks = getOverdue(getToDos(), TaskType.TODO);
		ArrayList<Task> overdueEvents = getOverdue(getEvents(), TaskType.EVENT);
		
		overdueTasks.addAll(overdueEvents);
		
		return overdueTasks;
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
	
	private ArrayList<Task> getUnDoneFloatingTasks() {
		
		ArrayList<Task> tasks = new ArrayList<Task> ();
		taskList = storage.load();
		
		for(int taskId : taskList.keySet()) {
			
			Task task = taskList.get(taskId);
			if(task.getTaskType().equals(TaskType.FLOATING) && !task.getDone()) {
				tasks.add(task);
			}
		}
		
		return tasks;
	}
	
	private ArrayList<Task> getFloatingTasks() {
		
		return getTasks(TaskType.FLOATING);
	}

	private ArrayList<Task> getToDos() {
		
		return getTasks(TaskType.TODO);
	}

	private ArrayList<Task> getEvents() {
		
		return getTasks(TaskType.EVENT);
	}
	
	private ArrayList<Task> getCompletedFloats() {

		return getCompleted(getFloatingTasks());
	}

	private ArrayList<Task> getCompletedToDos() {

		return getCompleted(getToDos());
	}
	
	private ArrayList<Task> getUpcomingToDos() {

		return getUpcoming(getToDos(), TaskType.TODO);
	}

	private ArrayList<Task> getUpcomingEvents() {

		return getUpcoming(getEvents(), TaskType.EVENT);
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
			case ("today") :
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
			case ("ToDo") :
				data = getToDos();
				break;
			case ("floating") :
				data = getUnDoneFloatingTasks();
				break;
			case ("events") :
				data = getEvents();
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
	
	void retrieveAllData(){
		getTasks = retrieveTaskData("upcomingToDo");
		assert getTasks!=null;
		getEvents = retrieveTaskData("upcomingEvents");
		assert getEvents!=null;
		getOverdue = retrieveTaskData("overdueTasks");
		assert getOverdue!=null;
		getFloat = retrieveTaskData("floating");
		assert getFloat!=null;
		getCate = retrieveStringData("categories");
		assert getCate!=null;
		retrieveTodays();
		retrieveCompletes();
	}
	
	void retrieveCompletes(){
		getCompletedTasks = retrieveTaskData("completedToDo");
		getCompletedEvents = retrieveTaskData("pastEvents");
		getCompletedFloat= retrieveTaskData("completedFloats");
	}
	void retrieveTodays(){
		getTodayTasks = retrieveTaskData("todayToDos");
		getTodayEvents = retrieveTaskData("todayEvents");
	}
	
	void updateIndex(){
		retrieveAllData();
		int a = getTasks.size(), b = getEvents.size(),c = getOverdue.size(),
				d = getFloat.size(), e = getCompletedTasks.size(), f = getCompletedEvents.size();
		setIndex(getTasks, 0);
		setIndex(getEvents,a);
		setIndex(getOverdue,a+b);
		setIndex(getFloat, a+b+c);
		setIndex(getCompletedTasks,a+b+c+d);
		setIndex(getCompletedEvents, a+b+c+d+e);
		setIndex(getCompletedFloat, a+b+c+d+e+f);
//		System.out.println("update Index a: "+a);
	}
	
	void setIndex(ArrayList<Task> list, int taskIndex) {
		for (int i=0;i<list.size();i++){ 
			int taskId = list.get(i).getTaskId();
			taskList = storage.load();
			taskList.get(taskId).setIndex(++taskIndex);
			storage.save(taskList);
		}
	}
	
}