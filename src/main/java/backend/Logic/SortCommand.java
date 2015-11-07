package main.java.backend.Logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Task;

public class SortCommand extends Command {
	
	private static final String EXECUTION_SORT_SUCCESSFUL = "All items sorted";
	
	private static final String SORT_NAME = "sortN";
	private static final String SORT_PRIORITY = "sortP";
	private static final String SORT_DATE = "sortD";
	
	private Storage storageComponent;
	private String sortField = "";
	private ArrayList<Task> currentState;

	//@@author A0121284N
	public SortCommand(Type typeInput, Storage storage) {
		super(typeInput);
		storageComponent = storage;
		currentState = storageComponent.load();
	}

	//@@author A0121284N
	public void setSortField(String input) {
		this.sortField = input;
	}
	
	//@@author A0121284N
	public String execute() {
		ArrayList<Task> sortedEvent = sort(sortField, currentState);
		storageComponent.save(sortedEvent);
		return EXECUTION_SORT_SUCCESSFUL;
	}
	
	//@@author A0126258A
	private ArrayList<Task> generateTaskId(ArrayList<Task> taskList) {

		ArrayList<Task> newTaskList = new ArrayList<Task> ();
		int newTaskId = 0;

		for(Task task : taskList) {
			task.setTaskId(newTaskId);
			newTaskList.add(task);
			newTaskId++;
		}

		return newTaskList;
	}
	
	//@@author A0126258A
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
		
		return generateTaskId(taskList);
	}
	
	//@@author A0126258A 
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
		
		return generateTaskId(taskList);
	}
	
	//@@author A0126258A
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
		
		return generateTaskId(taskList);
	}
	
	//@@author A0126258A
	private ArrayList<Task> sort(String field, ArrayList<Task> taskList) {
		
		ArrayList<Task> sortedTaskList = new ArrayList<Task> ();
		
		switch (field) {
			case SORT_NAME:
				sortedTaskList = sortName(taskList);
				break;
			case SORT_PRIORITY:
				sortedTaskList = sortPriority(taskList);
				break;
			case SORT_DATE:
				sortedTaskList = sortDeadline(taskList);
				break;
		}
		return sortedTaskList;
	}

}
