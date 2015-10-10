package main.java.backend.Sorter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
	
	private Comparator<Task> sortPriority = new Comparator<Task> () {
		public int compare(Task left, Task right) {
			if(left.getPriority() < right.getPriority()) {
				return -1;
			} else if(left.getPriority() > right.getPriority()) {
				return 1;
			} else {
				return 0;
			}
		}
	};
	
	private Comparator<Task> sortDeadline = new Comparator<Task> () {
		public int compare(Task left, Task right) {
			if(left.getEndTime() < right.getEndTime()) {
				return -1;
			} else if(left.getEndTime() > right.getEndTime()) {
				return 1;
			} else {
				return 0;
			}
		}
	};

	private ArrayList<Task> sortDate(ArrayList<Task> currentState2) {
		Collections.sort(currentState2, sortPriority);
		return currentState2;
	}

	private ArrayList<Task> sortPriority(ArrayList<Task> currentState2) {
		Collections.sort(currentState2, sortDeadline);
		return currentState2;
	}

}
