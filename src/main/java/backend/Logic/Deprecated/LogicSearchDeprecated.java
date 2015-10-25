package main.java.backend.Logic;

import java.util.ArrayList;

import main.java.backend.Storage.Task.Task;

public class LogicSearch {
	private static LogicSearch logicSearchObject;
	
	private LogicSearch() {
		
	}

	public static LogicSearch getInstance() {
		if (logicSearchObject == null) {
			logicSearchObject = new LogicSearch();
		}
		return logicSearchObject;
	}

	/*
	public ArrayList<Category> search(ArrayList<Category> currentState, String keyword) {
		// TODO Auto-generated method stub
		return currentState;
	}
	*/

}
