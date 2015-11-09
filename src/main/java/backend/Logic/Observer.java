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
	
	private static final String TIME_MINUTES = ":00";
	private static final String TIME_AM_CAPS = "AM";
	private static final String TIME_AM = "am";
	private static final String TIME_PM_CAPS = "PM";
	private static final String TIME_PM = "pm";
	
	private static final String RETRIEVE_TODAY = "today";
	private static final String RETRIEVE_TODAY_TODO = "todayToDos";
	private static final String RETRIEVE_TODAY_EVENTS = "todayEvents";
	private static final String RETRIEVE_OVERDUE = "overdueTasks";
	private static final String RETRIEVE_PAST_EVENTS = "pastEvents";
	private static final String RETRIEVE_TODO = "ToDo";
	private static final String RETRIEVE_FLOAT = "floating";
	private static final String RETRIEVE_EVENTS = "events";
	private static final String RETRIEVE_COMPLETED_FLOAT = "completedFloats";
	private static final String RETRIEVE_COMPLETED_TODO = "completedToDo";
	private static final String RETRIEVE_UPCOMING_TODO = "upcomingToDo";
	private static final String RETRIEVE_UPCOMING_EVENTS = "upcomingEvents";
	private static final String RETRIEVE_REMINDER = "reminder";
	
	private static final String RESET = "";
	
	private static final long DAY_IN_MILLISECOND = 86400000L;

	private static Observer logicGetterObject;
	private Storage storageComponent;
	
	private ArrayList<Task> taskList;
	private ArrayList<Task> getTasks;
	private ArrayList<Task> getEvents;
	private ArrayList<Task> getOverdue;
	private ArrayList<Task> getFloat;
	private ArrayList<Task> getTodayTasks;
	private ArrayList<Task> getTodayEvents;
	
	private ArrayList<Task> getCompletedTasks;
	private ArrayList<Task> getCompletedEvents;
	private ArrayList<Task> getCompletedFloat;
	
	private ArrayList<Task> searchResults;
	
	//@@author A0121284N
	public static Observer getInstance(Storage storageComponent) {
		
		if (logicGetterObject == null) {
			logicGetterObject = new Observer(storageComponent);
		}
		return logicGetterObject;
	}

	//@@author A0121284N
	private Observer(Storage storage) {
		this.storageComponent = storage;
		this.searchResults = new ArrayList<Task>();
		this.taskList = storage.load();
	}
	
	//@@author A0121284N
	private void retrieveAllData(){
		getTasks = retrieveTaskData(RETRIEVE_UPCOMING_TODO);
		assert getTasks != null;
		getEvents = retrieveTaskData(RETRIEVE_UPCOMING_EVENTS);
		assert getEvents != null;
		getOverdue = retrieveTaskData(RETRIEVE_OVERDUE);
		assert getOverdue != null;
		getFloat = retrieveTaskData(RETRIEVE_FLOAT);
		assert getFloat != null;
		retrieveTodays();
		retrieveCompletes();
	}
	
	//@@author A0121284N
	private void retrieveCompletes(){
		getCompletedTasks = retrieveTaskData(RETRIEVE_COMPLETED_TODO);
		getCompletedEvents = retrieveTaskData(RETRIEVE_PAST_EVENTS);
		getCompletedFloat = retrieveTaskData(RETRIEVE_COMPLETED_FLOAT);
	}
	
	//@@author A0121284N
	private void retrieveTodays(){
		getTodayTasks = retrieveTaskData(RETRIEVE_TODAY_TODO);
		getTodayEvents = retrieveTaskData(RETRIEVE_TODAY_EVENTS);
	}
	
	//@@author A0121284N
	private void setIndex(ArrayList<Task> list, int taskIndex) {
		
		taskList = storageComponent.load();
		for (Task task : list) { 
			task.setIndex(++taskIndex);
			taskList.set(task.getTaskId(), task);	
		}
		
		storageComponent.save(taskList);
	}

	//@@author A0126258A
	private ArrayList<Task> generateTaskId(ArrayList<Task> taskList) {

		ArrayList<Task> newTaskList = new ArrayList<Task> ();
		int newTaskId = 0;

		for(Task task : taskList) {
			task.setTaskId(newTaskId);
			newTaskList.add(task);
			newTaskId++;
		}

		return newTaskList;
	}
	
	//@@author A0126258A
	private String reformatDate(String date) {
		
		String newDate = date
				.replace(TIME_MINUTES, RESET)
				.replace(TIME_AM_CAPS, TIME_AM)
				.replace(TIME_PM_CAPS, TIME_PM);
		
		return newDate;
	}
	
	//@@author A0126258A
	private void resetRecurring() {

		ArrayList<Task> taskList = storageComponent.load();
		ArrayList<Task> recurringTaskList = new ArrayList<Task> ();
		
		for(Task task : taskList) {

			Task nextTask = new Task(task);
			
			// Only reset date when todo/event is over
			if(!task.isRecurred() && !task.getRecurrenceType().equals(RecurrenceType.NONE)
					&& !nextTask.getTaskType().equals(TaskType.FLOATING)
					&& (task.getDone() || Constant.stringToMillisecond(task.getEnd()) 
					<= getCurrentTime())) {
				
				task.setRecurred(true);
				nextTask.setEnd(getUpcomingDate(nextTask, nextTask.getEnd()));
				
				if(nextTask.getTaskType().equals(TaskType.EVENT)) {
					nextTask.setStart(getUpcomingDate(nextTask, nextTask.getStart()));
				}
				
				recurringTaskList.add(nextTask);
			}
		}
		
		taskList.addAll(recurringTaskList);
		storageComponent.save(generateTaskId(taskList));
	}
	
	//@@author A0126258A
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
	
	//@@author A0126258A
	private String getDate(long milliSeconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return standardFormat.format(calendar.getTime());
	}

	//@@author A0126258A
	private long getCurrentTime() {

		long currentMilliseconds = System.currentTimeMillis();
		Date resultdate = new Date(currentMilliseconds);

		return Constant.stringToMillisecond(standardFormat.format(resultdate));
	}
	
	//@@author A0126258A
	private long getTodayStartTime() {

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime midnight = now.toLocalDate().atStartOfDay();
		Date resultDate = Date.from(midnight
				.atZone(ZoneId.systemDefault()).toInstant());

		return Constant.stringToMillisecond(
				standardFormat.format(resultDate));
	}

	//@@author A0126258A
	private long getTodayEndTime() {

		return getTodayStartTime() + DAY_IN_MILLISECOND + DAY_IN_MILLISECOND;
	}

	//@@author A0126258A
	private ArrayList<Task> getUpcoming(ArrayList<Task> allTasks, TaskType taskType) {

		ArrayList<Task> upcomingTasks = new ArrayList<Task> ();

		for(Task task : allTasks) {
			if(taskType == TaskType.TODO && Constant.stringToMillisecond(task.getEnd()) 
					> getCurrentTime() && !task.getDone()) {
				upcomingTasks.add(task);
			} else if(taskType == TaskType.EVENT && Constant.stringToMillisecond(task.getEnd())
					> getCurrentTime() && !task.getDone()) {
				upcomingTasks.add(task);
			}
		}

		return upcomingTasks;
	}
	
	//@@author A0126258A
	private ArrayList<Task> getCompleted(ArrayList<Task> allTasks) {
		
		ArrayList<Task> completedTasks = new ArrayList<Task> ();
		
		for(Task task : allTasks) {
			if(task.getDone()) {
				completedTasks.add(task);
			}
		}
		
		return completedTasks;
	}

	//@@author A0126258A
	private ArrayList<Task> getOverdue(ArrayList<Task> allTasks, TaskType taskType) {

		ArrayList<Task> overdueTasks = new ArrayList<Task> ();

		for(Task task : allTasks) {
			if(Constant.stringToMillisecond(task.getEnd())
					<= getCurrentTime() && !task.getDone()) {
				overdueTasks.add(task);
			} 
		}

		return overdueTasks;
	}	

	//@@author A0121284N
	public ArrayList<Task> getSearchResultsList() {
		return this.searchResults;
	}
	
	//@@author A0121284N
	public void updateSearchResultsList(ArrayList<Task> searchList) {
		this.searchResults = searchList;
	}
	
	//@@author A0126258A
	private ArrayList<Task> getTasks(TaskType taskType) {
		
		ArrayList<Task> tasks = new ArrayList<Task> ();
		taskList = storageComponent.load();
		
		for(Task task : taskList) {
			if(task.getTaskType().equals(taskType)) {
				tasks.add(task);
			}
		}
		
		return tasks;
	}
	
	//@@author A0126258A
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
	
	//@@author A0126258A
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
	
	//@@author A0126258A
	private ArrayList<Task> getAllToday() {
		
		ArrayList<Task> todayTasks = getTodayToDos();
		todayTasks.addAll(getTodayEvents());
		
		return todayTasks;
	}
	
	//@@author A0126258A
	private ArrayList<Task> getAllOverdue() {

		ArrayList<Task> overdueTasks = getOverdue(getToDos(), TaskType.TODO);
		ArrayList<Task> overdueEvents = getOverdue(getEvents(), TaskType.EVENT);
		
		overdueTasks.addAll(overdueEvents);
		
		return overdueTasks;
	}
	
	//@@author A0126258A
	private ArrayList<Task> getPastEvents() {
		
		ArrayList<Task> allEvents = getEvents();
		ArrayList<Task> pastEvents = new ArrayList<Task> ();

		for(Task task : allEvents) {
			if(task.getDone()) {
				pastEvents.add(task);
			}
		}

		return pastEvents;
	}
	
	//@@author A0126258A
	private ArrayList<Task> getUnDoneFloatingTasks() {
		
		ArrayList<Task> tasks = new ArrayList<Task> ();
		taskList = storageComponent.load();
		
		for(Task task : taskList) {
			if(task.getTaskType().equals(TaskType.FLOATING) && !task.getDone()) {
				tasks.add(task);
			}
		}
		
		return tasks;
	}
	
	//@@author A0126258A
	private ArrayList<Task> getFloatingTasks() {
		
		return getTasks(TaskType.FLOATING);
	}

	//@@author A0126258A
	private ArrayList<Task> getToDos() {
		
		return getTasks(TaskType.TODO);
	}

	//@@author A0126258A
	private ArrayList<Task> getEvents() {
		
		return getTasks(TaskType.EVENT);
	}
	
	//@@author A0126258A
	private ArrayList<Task> getCompletedFloats() {

		return getCompleted(getFloatingTasks());
	}

	//@@author A0126258A
	private ArrayList<Task> getCompletedToDos() {

		return getCompleted(getToDos());
	}
	
	//@@author A0126258A
	private ArrayList<Task> getUpcomingToDos() {

		return getUpcoming(getToDos(), TaskType.TODO);
	}

	//@@author A0126258A
	private ArrayList<Task> getUpcomingEvents() {

		return getUpcoming(getEvents(), TaskType.EVENT);
	}
	
	//@@author A0126258A
	private ArrayList<Task> getAllReminder() {
		
		ArrayList<Task> remindTaskList = new ArrayList<Task> ();
		ArrayList<Task> allData = storageComponent.load();
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
	
		storageComponent.save(allData);
		return remindTaskList;
	}

	//@@author A0121284N
	public ArrayList<Task> retrieveTaskData(String dataType) {
		
		ArrayList<Task> data = new ArrayList<Task>();
		
		switch(dataType) {
			case (RETRIEVE_TODAY) :
				data = getAllToday();
				break;
			case (RETRIEVE_TODAY_TODO) :
				data = getTodayToDos();
				break;
			case (RETRIEVE_TODAY_EVENTS) :
				data = getTodayEvents();
				break;
			case (RETRIEVE_OVERDUE) :
				data = getAllOverdue();
				break;
			case (RETRIEVE_PAST_EVENTS) :
				data = getPastEvents();
				break;
			case (RETRIEVE_TODO) :
				data = getToDos();
				break;
			case (RETRIEVE_FLOAT) :
				data = getUnDoneFloatingTasks();
				break;
			case (RETRIEVE_EVENTS) :
				data = getEvents();
				break;
			case (RETRIEVE_COMPLETED_FLOAT) :
				data = getCompletedFloats();
				break;
			case (RETRIEVE_COMPLETED_TODO) :
				data = getCompletedToDos();
				break;
			case (RETRIEVE_UPCOMING_TODO) :
				data = getUpcomingToDos();
				break;
			case (RETRIEVE_UPCOMING_EVENTS) :
				data = getUpcomingEvents();
				break;
			case (RETRIEVE_REMINDER) :
				data = getAllReminder();
				break;
		}		
		
		if(!taskList.get(0).isSorted()) {
			Collections.sort(data);
		}
		return data;
	}
	
	//@@author A0121284N
	public void updateIndex() {

		resetRecurring();
		retrieveAllData();
		int taskSize = getTasks.size(), completedTaskSizeventSize = getEvents.size(), 
				overdueSize = getOverdue.size(), floatSize = getFloat.size(), 
				completedTaskSize = getCompletedTasks.size(), completedEventSize = getCompletedEvents.size();

		setIndex(getTasks, 0);
		setIndex(getEvents, taskSize);
		setIndex(getOverdue, taskSize + completedTaskSizeventSize);
		setIndex(getFloat, taskSize + completedTaskSizeventSize + overdueSize);
		setIndex(getCompletedTasks, taskSize + completedTaskSizeventSize + overdueSize + floatSize);
		setIndex(getCompletedEvents, taskSize + completedTaskSizeventSize + overdueSize 
				+ floatSize + completedTaskSize);
		setIndex(getCompletedFloat, taskSize + completedTaskSizeventSize + overdueSize 
				+ floatSize + completedTaskSize + completedEventSize);
	}
	
}