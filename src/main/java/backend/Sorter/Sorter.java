package main.java.backend.Sorter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
	
	private ArrayList<Task> sortStart(ArrayList<Task> taskList) {
		
		Collections.sort(taskList, new Comparator<Task> () {
			public int compare(Task left, Task right) {
				if(stringToMillisecond(left.getStart())
						< stringToMillisecond(right.getStart())) {
					return -1;
				} else if(stringToMillisecond(left.getStart()) 
						> stringToMillisecond(right.getStart())) {
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
				if(stringToMillisecond(left.getEnd())
						< stringToMillisecond(right.getEnd())) {
					return -1;
				} else if(stringToMillisecond(left.getEnd()) 
						> stringToMillisecond(right.getEnd())) {
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
				sortedTaskList = sortStart(taskList);
				break;
			case "sortD":
				sortedTaskList = sortDeadline(taskList);
				break;
		}
		return sortedTaskList;
	}

}
