package main.java.backend.Sorter;

import java.util.ArrayList;
import java.util.Collections;

import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.Task;

public class Sorter {

	public ArrayList<Task> sort(String field, ArrayList<Task> currentState2) {
		ArrayList<Task> sortedCurrentState = null;
		switch (field) {
			case "sortP":
				sortedCurrentState = sortPriority(currentState2);
				break;
			case "sortD":
				sortedCurrentState = sortDate(currentState2);
				break;
		}
		return sortedCurrentState;
	}

	private ArrayList<Task> sortDate(ArrayList<Task> currentState2) {
		Collections.sort(currentState2, Task.sortPriority);
		return currentState2;
	}

	private ArrayList<Task> sortPriority(ArrayList<Task> currentState2) {
		Collections.sort(currentState2, Task.sortDeadline);
		return currentState2;
	}

}
