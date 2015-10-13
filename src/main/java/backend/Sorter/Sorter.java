package main.java.backend.Sorter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import main.java.backend.GeneralFunctions.GeneralFunctions;
import main.java.backend.Logic.Command;
import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.Task;

public class Sorter {
	
	public Sorter() {
		
	}
	
	private ArrayList<Task> sortName(ArrayList<Task> taskList) {
		
		Collections.sort(taskList, new Comparator<Task> () {
			public int compare(Task left, Task right) {
				if(left.getName().compareTo(right.getName()) < 0) {
					return -1;
				} else if(left.getName().compareTo(right.getName()) > 0) {
					return 1;
				}  else {
					return 0;
				}
			}
		});
		return taskList;
	}

	private ArrayList<Task> sortPriority(ArrayList<Task> taskList) {
		
		Collections.sort(taskList, new Comparator<Task> () {
			public int compare(Task left, Task right) {
				if(left.getPriority() < right.getPriority()) {
					return 1;
				} else if(left.getPriority() > right.getPriority()) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		return taskList;
	}
	
	private ArrayList<Task> sortStartDate(ArrayList<Task> taskList) {
		
		Collections.sort(taskList, new Comparator<Task> () {
			public int compare(Task left, Task right) {
				if(GeneralFunctions.stringToMillisecond(left.getStartDate())
						< GeneralFunctions.stringToMillisecond(right.getStartDate())) {
					return -1;
				} else if(GeneralFunctions.stringToMillisecond(left.getStartDate()) 
						> GeneralFunctions.stringToMillisecond(right.getStartDate())) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		return taskList;
	}
	
	private ArrayList<Task> sortDeadline(ArrayList<Task> taskList) {
		
		Collections.sort(taskList, new Comparator<Task> () {
			public int compare(Task left, Task right) {
				if(GeneralFunctions.stringToMillisecond(left.getEndDate())
						< GeneralFunctions.stringToMillisecond(right.getEndDate())) {
					return -1;
				} else if(GeneralFunctions.stringToMillisecond(left.getEndDate()) 
						> GeneralFunctions.stringToMillisecond(right.getEndDate())) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		return taskList;
	}
	
	public ArrayList<Task> sort(String field, ArrayList<Task> taskList) {
		
		ArrayList<Task> sortedTaskList = new ArrayList<Task> ();
		
		switch (field) {
			case "sortN":
				sortedTaskList = sortName(taskList);
				break;
			case "sortP":
				sortedTaskList = sortPriority(taskList);
				break;
			case "sortS":
				sortedTaskList = sortStartDate(taskList);
				break;
			case "sortD":
				sortedTaskList = sortDeadline(taskList);
				break;
		}
		return sortedTaskList;
	}

}
