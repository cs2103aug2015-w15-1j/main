package main.java.backend.Search;

import java.util.ArrayList;
import java.util.TreeMap;

import main.java.backend.Storage.Task.Task;
import main.java.backend.Storage.Task.TaskType;

public class Search {

	
	/*
	public ArrayList<Category> search(ArrayList<Category> currentState, String keyword) {
		// TODO Auto-generated method stub
		return currentState;
	}
	*/
	
	public ArrayList<Task> search(TreeMap<Integer, Task> taskList, String keyword) {
		
		ArrayList<Task> result = new ArrayList<Task> ();
		String[] tokenize = keyword.toLowerCase().split(" ");
		int wordSize = tokenize.length;
		
		for(int taskId : taskList.keySet()) {
			Task task = taskList.get(taskId);
			int passed = 0;
			for(String word : tokenize)
				if(task.getName().toLowerCase().contains(word)
					|| task.getDescription().toLowerCase().contains(word)
					|| task.getStart().toLowerCase().contains(word)
					|| task.getEnd().toLowerCase().contains(word)
					|| task.getReminder().toLowerCase().contains(word)) {
					passed++;
			}
			if(wordSize == passed) {
				result.add(task);
			}
		}
		
		return result;
	}

}
