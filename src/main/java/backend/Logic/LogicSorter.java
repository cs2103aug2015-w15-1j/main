package main.java.backend.Logic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeMap;

import main.java.backend.Storage.Task.Task;

public class LogicSorter {
	
	private static LogicSorter logicSorterObject;
	private static final SimpleDateFormat formatterForDateTime = 
			new SimpleDateFormat("EEE, dd MMM hh:mma");
	
	private LogicSorter() {
		
	}
	
	public static LogicSorter getInstance() {
		if (logicSorterObject == null) {
			logicSorterObject = new LogicSorter();
		}
		return logicSorterObject;
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
			@Override
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
	
	public TreeMap<Integer, Task> sort(String field, TreeMap<Integer, Task> taskList) {
		
		TreeMap<Integer, Task> sortedTaskList = new TreeMap<Integer, Task> ();
		
		switch (field) {
			case "sortN":
				sortedTaskList = arrayListToTreeMap(sortName(new ArrayList<Task> (taskList.values())));
				break;
			case "sortP":
				sortedTaskList = arrayListToTreeMap(sortPriority(new ArrayList<Task> (taskList.values())));
				break;
			case "sortS":
				sortedTaskList = arrayListToTreeMap(sortStart(new ArrayList<Task> (taskList.values())));
				break;
			case "sortD":
				sortedTaskList = arrayListToTreeMap(sortDeadline(new ArrayList<Task> (taskList.values())));
				break;
		}
		return sortedTaskList;
	}
	
	private TreeMap<Integer, Task> arrayListToTreeMap(ArrayList<Task> taskList) {
		
		TreeMap<Integer, Task> allData = new TreeMap<Integer, Task> ();
		
		for(Task task : taskList) {
			allData.put(task.getTaskId(), task);
		}
		
		return allData;
	}
	
	private long stringToMillisecond(String dateTime) {
		try {
			Date tempDateTime = formatterForDateTime.parse(dateTime);
			long dateTimeMillisecond = tempDateTime.getTime();
			return (dateTimeMillisecond);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		//Should not reach here
		return -1;
	}

}
