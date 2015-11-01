package main.java.backend.Logic;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Task;
import main.java.backend.Storage.Task.Task.RecurrenceType;
import main.java.backend.Storage.Task.Task.TaskType;

public class Observer {
	
	private static final SimpleDateFormat standardFormat = 
			new SimpleDateFormat("EEE, d MMM yy, h:mma");
	private static final String RESET = "";
	
	private static final long DAY_IN_MILLISECOND = 86400000L;

	private static Observer logicGetterObject;
	private Storage storage;
	
	private ArrayList<Task> taskList;
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
	private static ArrayList<Task> searchResults = new ArrayList<Task>();
	
	public static Observer getInstance(Storage storageComponent) {
		
		if (logicGetterObject == null) {
			logicGetterObject = new Observer(storageComponent);
		}
		return logicGetterObject;
	}

	private Observer(Storage storage) {
		this.storage = storage;
	}
	
	private String reformatDate(String date) {
		
		String newDate = date
				.replace(":00", "")
				.replace("AM", "am")
				.replace("PM", "");
		
		return newDate;
	}
	
	private void resetRecurring() {

		ArrayList<Task> taskList = storage.load();

		for(Task task : taskList) {

			// Only reset date when todo/event is over
			if(!task.getRecurrenceType().equals(RecurrenceType.NONE)
					&& Constant.stringToMillisecond(task.getEnd()) 
					<= getCurrentTime()) {
				
				if(task.getTaskType().equals(TaskType.EVENT)) {
					task.setEnd(getUpcomingDate(task, task.getEnd()));
				}
				if(!task.getTaskType().equals(TaskType.FLOATING)) {
					task.setStart(getUpcomingDate(task, task.getStart()));
				}
				taskList.set(task.getTaskId(), task);
			}
		}
		storage.save(taskList);
	}
	
	private String getUpcomingDate(Task task, String currentDate) {
		
		String upcomingDate = new String();
		long currentDateMilliseconds = Constant.stringToMillisecond(currentDate);
		int factor = task.getRecurrenceFrequency();
		Calendar date = Calendar.getInstance();
        date.setTimeInMillis(currentDateMilliseconds);
		
		switch(task.getRecurrenceType()) {
			case NONE:
				break;
			case DAY:
				date.add(Calendar.DATE, factor);
	            upcomingDate = getDate(date.getTimeInMillis());
				break;
			case WEEK:
				factor *= 7;
				date.add(Calendar.DATE, factor);
	            upcomingDate = getDate(date.getTimeInMillis());
				break;
			case MONTH:
				date.add(Calendar.MONTH, factor);
	            upcomingDate = getDate(date.getTimeInMillis());
				break;
			case YEAR:
				date.add(Calendar.YEAR, factor);
	            upcomingDate = getDate(date.getTimeInMillis());
				break;
		}
		return reformatDate(upcomingDate);
	}
	
	private String getDate(long milliSeconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return standardFormat.format(calendar.getTime());
	}

	private long getCurrentTime() {

		long currentMilliseconds = System.currentTimeMillis();
		Date resultdate = new Date(currentMilliseconds);

		return Constant.stringToMillisecond(standardFormat.format(resultdate));
	}
	
	private long getTodayStartTime() {

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime midnight = now.toLocalDate().atStartOfDay();
		Date resultDate = Date.from(midnight
				.atZone(ZoneId.systemDefault()).toInstant());

		return Constant.stringToMillisecond(
				standardFormat.format(resultDate));
	}

	private long getTodayEndTime() {

		return getTodayStartTime() + DAY_IN_MILLISECOND;
	}

	private ArrayList<Task> getUpcoming(ArrayList<Task> allTasks, TaskType taskType) {

		ArrayList<Task> upcomingTasks = new ArrayList<Task> ();

		for(Task task : allTasks) {
			if(taskType == TaskType.TODO && Constant.stringToMillisecond(task.getEnd()) 
					> getCurrentTime() && !task.getDone()) {
				upcomingTasks.add(task);
			} else if(taskType == TaskType.EVENT && Constant.stringToMillisecond(task.getStart())
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

	private ArrayList<Task> getOverdue(ArrayList<Task> allTasks, TaskType taskType) {

		ArrayList<Task> overdueTasks = new ArrayList<Task> ();

		for(Task task : allTasks) {
			if(taskType == TaskType.TODO && Constant.stringToMillisecond(task.getEnd())
					<= getCurrentTime() && !task.getDone()) {
				overdueTasks.add(task);
			} else if(taskType == TaskType.EVENT && Constant.stringToMillisecond(task.getStart())
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
	

	public ArrayList<Task> getSearchResultsList() {
		return this.searchResults;
	}
	
	public void updateSearchResultsList(ArrayList<Task> searchList) {
		this.searchResults = searchList;
	}
	
	private ArrayList<Task> getTasks(TaskType taskType) {
		
		ArrayList<Task> tasks = new ArrayList<Task> ();
		taskList = storage.load();
		
		for(Task task : taskList) {
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
			if(!task.getDone() && Constant.stringToMillisecond(task.getEnd())
					>= getTodayStartTime()
					&& Constant.stringToMillisecond(task.getEnd())
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
			if(!task.getDone() && Constant.stringToMillisecond(task.getStart()) 
					>= getTodayStartTime()
					&& Constant.stringToMillisecond(task.getStart()) 
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
		
		for(Task task : taskList) {
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
	
	private ArrayList<Task> getAllReminder() {
		
		ArrayList<Task> remindTaskList = new ArrayList<Task> ();
		ArrayList<Task> allData = storage.load();
		ArrayList<Task> taskList = getFloatingTasks();
		taskList.addAll(getToDos());
		taskList.addAll(getEvents());
		
		for(Task task : taskList) {
			long reminder = Constant.stringToMillisecond(task.getReminder());
			if(!task.getDone() && reminder != -1 && reminder <= getCurrentTime()) {
				remindTaskList.add(task);
				task.setReminder(RESET);
				allData.set(task.getTaskId(), task);
			}
		}
	
		storage.save(allData);
		return remindTaskList;
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
		resetRecurring();
		
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
			case ("reminder") :
				data = getAllReminder();
				break;
		}
		
		return data;
	}
	
	private void retrieveAllData(){
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
	
	private void retrieveCompletes(){
		getCompletedTasks = retrieveTaskData("completedToDo");
		getCompletedEvents = retrieveTaskData("pastEvents");
		getCompletedFloat= retrieveTaskData("completedFloats");
	}
	private void retrieveTodays(){
		getTodayTasks = retrieveTaskData("todayToDos");
		getTodayEvents = retrieveTaskData("todayEvents");
	}
	
	void updateIndex() {
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
	
	private void setIndex(ArrayList<Task> list, int taskIndex) {
		taskList = storage.load();
		for (Task task : list) { 
			task.setIndex(++taskIndex);
			taskList.set(task.getTaskId(), task);	
		}
		storage.save(taskList);
	}
	
}