package main.java.backend.Sorter;

import java.util.ArrayList;

import main.java.backend.Storage.Task.Task;

public class Sorter {

	public ArrayList<Task> sort(String field, ArrayList<Task> currentState) {
		ArrayList<Task> sortedCurrentState = null;
		switch (field) {
			case "sortP":
				sortedCurrentState = sortPriority(currentState);
				break;
			case "sortD":
				sortedCurrentState = sortDate(currentState);
				break;
		}
		return sortedCurrentState;
	}

	private ArrayList<Task> sortDate(ArrayList<Task> currentState) {
		// TODO Auto-generated method stub
		return currentState;
	}

	private ArrayList<Task> sortPriority(ArrayList<Task> currentState) {
		// TODO Auto-generated method stub
		return currentState;
	}

}
