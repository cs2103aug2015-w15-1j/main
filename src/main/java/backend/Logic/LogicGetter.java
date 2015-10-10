package main.java.backend.Logic;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

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

	public ArrayList<Task> retrieveTaskData(String dataType) throws IOException, JSONException, ParseException {
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

	public ArrayList<Task> getToDo() throws IOException, JSONException, ParseException {
		return storageObject.getTasks();
	}

	public ArrayList<Task> getFloatingTasks() throws IOException, JSONException, ParseException {
		return storageObject.getFloatingTasks();
	}

	public ArrayList<Task> getEvents() throws IOException, JSONException, ParseException {
		return storageObject.getEvents();
	}
	
	public ArrayList<Task> getUpcomingToDo() throws IOException, JSONException, ParseException {
		return storageObject.getUpcomingTasks();
	}
	
	public ArrayList<Task> getUpcomingEvents() throws IOException, JSONException, ParseException {
		return storageObject.getUpcomingEvents();
	}

	public ArrayList<Task> getOverdueTasks() throws IOException, JSONException, ParseException {
		return storageObject.getOverdueTasks();
	}

	public void setindex(ArrayList<Task> list, int i, int index) {
		storageObject.setIndex(list.get(i),index+(i+1));
	}
}
