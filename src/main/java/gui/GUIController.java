package main.java.gui;

import java.util.ArrayList;

import main.java.backend.Logic.LogicFacade;
import main.java.backend.Storage.Task.Task;

//@@author A0126125R
public class GUIController {

	LogicFacade logicComponent;

	private final int NUM_TASKS = 1;
	private final int NUM_EVENTS = 2;
	private final int NUM_OVERDUE = 3;
	private final int NUM_FLOAT = 4;
	private final int NUM_TODAY_TASKS_EVENTS = 5;
	private final int NUM_SEARCH = 6;

	private ArrayList<Task> getTasks;
	private ArrayList<Task> getEvents;
	private ArrayList<Task> getOverdue;
	private ArrayList<Task> getFloat;
	private ArrayList<String> getCate;
	private ArrayList<Task> getTodayTasksEvents;
	private ArrayList<Task> getFocusList;
	private ArrayList<Task> getCompletedTasks;
	private ArrayList<Task> getCompletedEvents;
	private ArrayList<Task> getCompletedFloat;
	private ArrayList<Task> getSearch;
	private ArrayList<Task> getReminders;


	public GUIController() {

		logicComponent = LogicFacade.getInstance();


		retrieveAllData();
		getFocusList = logicComponent.retrieveTaskData("toDo"); //default as tasks
	}

	void retrieveAllData() {
		getTasks = logicComponent.retrieveTaskData("upcomingToDo");
		assert getTasks!=null;
		getEvents = logicComponent.retrieveTaskData("upcomingEvents");
		assert getEvents!=null;
		getOverdue = logicComponent.retrieveTaskData("overdueTasks");
		assert getOverdue!=null;
		getFloat = logicComponent.retrieveTaskData("floating");
		assert getFloat!=null;
		getCate = logicComponent.retrieveStringData("categories");
		assert getCate!=null;
		retrieveTodays();
		retrieveCompletes();
		retrieveSearch();
	}
	void retrieveSearch(){
		getSearch = logicComponent.retrieveSearchData();
		//System.out.println("Search results starts here");
		//System.out.println(getSearch);
		assert getSearch!=null;
	}

	void retrieveCompletes() {
		getCompletedTasks = logicComponent.retrieveTaskData("completedToDo");
		getCompletedEvents = logicComponent.retrieveTaskData("pastEvents");
		getCompletedFloat= logicComponent.retrieveTaskData("completedFloats");
	}

	void retrieveTodays() {
		//TO-DO
		getTodayTasksEvents = logicComponent.retrieveTaskData("today");
	}

	ArrayList<Task> retrieveTask() { //default view for most initialization
		ArrayList<Task> list = logicComponent.retrieveTaskData("upcomingToDo");
		assert list!= null;
		return list;
	}

	void determineList(int currentList) {
		if(currentList==NUM_OVERDUE){
			getFocusList = getOverdueList();
		} else if(currentList==NUM_TASKS) {
			getFocusList = getTasksList();
		} else if(currentList==NUM_EVENTS) {
			getFocusList = getEventsList();
		} else if (currentList==NUM_FLOAT) {
			getFocusList = getFloatList();
		} else if (currentList==NUM_TODAY_TASKS_EVENTS) {
			getFocusList = getTodayTasksEvents;
		} else if (currentList == NUM_SEARCH){
			getFocusList = getSearch;
		}
	}

	String executeCommand(String userInput) {
		String feedback = logicComponent.execute(userInput);
		return feedback;
	}

	ArrayList<Task> getCompletedTasks() {
		return getCompletedTasks;
	}

	ArrayList<Task> getCompletedEvents() {
		return getCompletedEvents;
	}

	ArrayList<Task> getCompletedFloat() {
		return getCompletedFloat;
	}

	ArrayList<Task> getTasksList() {
		return getTasks;
	}

	ArrayList<Task> getEventsList() {
		return getEvents;
	}

	ArrayList<Task> getOverdueList() {
		return getOverdue;
	}

	ArrayList<Task> getFloatList() {
		return getFloat;
	}

	ArrayList<String> getCateList() {
		return getCate;
	}
	ArrayList<Task> getFocusList() {
		return getFocusList;
	}

	ArrayList<Task> getToday() {
		return getTodayTasksEvents;
	}

	ArrayList<Task> getSearchList(){
		return getSearch;
	}

	boolean getNoti() {
		getReminders = logicComponent.retrieveTaskData("reminder");
		if (getReminders.isEmpty()){
			return false;
		}
		return true;
	}

	ArrayList<Task> getReminderList(){
		return getReminders;
	}
}