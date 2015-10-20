package main.java.gui;

import java.util.ArrayList;

import main.java.backend.Logic.LogicFacade;
import main.java.backend.Storage.Task.Task;

public class GUIController {
	
	LogicFacade logicComponent;
	
	private final int NUM_TASKS = 1;
	private final int NUM_EVENTS = 2;
	private final int NUM_OVERDUE = 3;
	private final int NUM_FLOAT = 4;
	private final int NUM_TODAY_TASKS = 5;
	private final int NUM_TODAY_EVENTS = 6;
	
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
	}
	
	void retrieveCompletes() {
		getCompletedTasks = logicComponent.retrieveTaskData("completedToDo");
		getCompletedEvents = logicComponent.retrieveTaskData("pastEvents");
		getCompletedFloat= logicComponent.retrieveTaskData("completedFloats");
	}
	
	void retrieveTodays() {
		getTodayTasks = logicComponent.retrieveTaskData("todayToDos");
		getTodayEvents = logicComponent.retrieveTaskData("todayEvents");
	}
	
	ArrayList<Task> retrieveTask() { //default view for most initialization
		ArrayList<Task> list = logicComponent.retrieveTaskData("upcomingToDo");
		assert list!= null;
		return list;
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
	
	void determineList(int currentList) {
		if(currentList==NUM_OVERDUE){
			getFocusList = getOverdueList();
		} else if(currentList==NUM_TASKS) {
			getFocusList = getTasksList();
		} else if(currentList==NUM_EVENTS) {
			getFocusList = getEventsList();
		} else if (currentList==NUM_FLOAT) {
			getFocusList = getFloatList();
		} else if (currentList==NUM_TODAY_TASKS) {
			getFocusList = getTodayTasks;
		}else if (currentList==NUM_TODAY_EVENTS) {
			getFocusList = getTodayEvents;
		}
	}
	
	String executeCommand(String userInput) {
		String feedback = logicComponent.execute(userInput);
		return feedback;
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
	
	ArrayList<Task> getTodayT() {
		return getTodayTasks;
	}
	
	ArrayList<Task> getTodayE() {
		return getTodayEvents;
	}
}
