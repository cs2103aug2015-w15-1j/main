package main.java.backend.Logic;

import java.util.ArrayList;

import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.Task;

public class LogicGetter {
	
	private static LogicGetter logicGetterObject;
	private Storage storageObject;

	private LogicGetter(Storage storageComponent) {
		storageObject = storageComponent;
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
		return storageObject.getCategories();
	}

	public ArrayList<Category> getSearchResultsList() {
		return null;
	}

	public ArrayList<Task> getToDo() {
		return storageObject.getTasks();
	}

	public ArrayList<Task> getFloatingTasks() {
		return storageObject.getFloatingTasks();
	}

	public ArrayList<Task> getEvents() {
		return storageObject.getEvents();
	}
	
	public ArrayList<Task> getUpcomingToDo() {
		return storageObject.getUpcomingTasks();
	}
	
	public ArrayList<Task> getUpcomingEvents() {
		return storageObject.getUpcomingEvents();
	}

	public ArrayList<Task> getOverdueTasks() {
		return storageObject.getOverdueTasks();
	}

	public void setindex(ArrayList<Task> list, int i, int index) {
		storageObject.setIndex(list.get(i),index+(i+1));
	}
}
