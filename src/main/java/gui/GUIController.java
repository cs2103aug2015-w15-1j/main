package main.java.gui;

import java.util.ArrayList;

import main.java.backend.Logic.LogicController;
import main.java.backend.Storage.Task.Task;

public class GUIController {
	private final String DEFAULT_FILENAME="filename.txt";
	LogicController logicComponent;
	
	private final int NUM_TASKS = 1;
	private final int NUM_EVENTS = 2;
	private final int NUM_OVERDUE = 3;
	private final int NUM_FLOAT = 4;
	
	private ArrayList<Task> getTasks;
	private ArrayList<Task> getEvents;
	private ArrayList<Task> getOverdue;
	private ArrayList<Task> getFloat;
	private ArrayList<String> getCate;
	private ArrayList<Task> getFocusList;
	
	public GUIController(){
		logicComponent = LogicController.getInstance(DEFAULT_FILENAME);
		retrieveAllData();
		getFocusList = logicComponent.retrieveTaskData("toDo"); //default as tasks
	}
	void retrieveAllData(){
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
	}
	ArrayList<Task> retrieveTask(){
		return logicComponent.retrieveTaskData("upcomingToDo");
	}
	void updateIndex(){
		retrieveAllData();
		setIndex(getTasks, 0);
		setIndex(getEvents,getTasks.size());
		setIndex(getOverdue,getTasks.size()+getEvents.size());
		setIndex(getFloat, getTasks.size()+getEvents.size()+getOverdue.size());
		
	}
	
	/**
	 * this operation adds the index into the list.
	 * numbering ordering are as follows:
	 * 0>task>events>overdue>float 
	 * 
	 * @param list
	 * @param index
	 */
	void setIndex(ArrayList<Task> list, int index) {
		assert index >= 0;
		for (int i=0;i<list.size();i++){ 
		logicComponent.updateTaskNumbering(list,i,(++index));
		}
		
	}
	
	void determineList(int currentList){
		if(currentList==NUM_OVERDUE){
			getFocusList = getOverdueList();
		} else if(currentList==NUM_TASKS){
			getFocusList = getTasksList();
		} else if(currentList==NUM_EVENTS){
			getFocusList = getEventsList();
		} else if (currentList==NUM_FLOAT){
			getFocusList = getFloatList();
		}
	}
	ArrayList<Task> getTasksList(){
		return getTasks;
	}
	ArrayList<Task> getEventsList(){
		return getEvents;
	}
	ArrayList<Task> getOverdueList(){
		return getOverdue;
	}
	ArrayList<Task> getFloatList(){
		return getFloat;
	}
	ArrayList<String> getCateList(){
		return getCate;
	}
	ArrayList<Task> getFocusList(){
		return getFocusList;
	}
}
