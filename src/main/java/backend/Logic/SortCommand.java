package main.java.backend.Logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Task;

public class SortCommand extends Command {
	
	private Storage storageComponent;
	private String sortField = "";
	ArrayList<Task> currentState;

	public SortCommand(Type typeInput, Storage storage) {
		super(typeInput);
		storageComponent = storage;
		currentState = storageComponent.load();
	}
	
	public void setSortField(String input) {
		this.sortField = input;
	}
	
	public String execute() {
//		System.out.println("sortField: "+sortField);
//		System.out.println("currentState+ "+currentState);
		ArrayList<Task> sortedEvent = sort(sortField,currentState);
//		System.out.println("sortedEvent "+sortedEvent);
		storageComponent.save(sortedEvent);
		return "All items sorted";
	}
	
	private ArrayList<Task> sortName(ArrayList<Task> taskList) {
		
		Collections.sort(taskList, new Comparator<Task> () {
			@Override
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
			@Override
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
			@Override
			public int compare(Task left, Task right) {
				if(Constant.stringToMillisecond(left.getStart())
						< Constant.stringToMillisecond(right.getStart())) {
					return -1;
				} else if(Constant.stringToMillisecond(left.getStart()) 
						> Constant.stringToMillisecond(right.getStart())) {
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
			@Override
			public int compare(Task left, Task right) {
				if(Constant.stringToMillisecond(left.getEnd())
						< Constant.stringToMillisecond(right.getEnd())) {
					return -1;
				} else if(Constant.stringToMillisecond(left.getEnd()) 
						> Constant.stringToMillisecond(right.getEnd())) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		return taskList;
	}
	
	private ArrayList<Task> sort(String field, ArrayList<Task> taskList) {
		
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
