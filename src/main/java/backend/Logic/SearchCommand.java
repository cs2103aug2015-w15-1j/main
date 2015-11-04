package main.java.backend.Logic;

import java.util.ArrayList;
import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Task;

public class SearchCommand extends Command {
	
	private ArrayList<Task> searchResults;
	private Storage storageComponent;
	private static ArrayList<Task> taskList;

	//@@author A0121284N
	public SearchCommand(Type typeInput, Storage storage) {
		super(typeInput);
		storageComponent = storage;
	}
	
	//@@author A0121284N
	public ArrayList<Task> getSearchResults() {
		return this.searchResults;
	}
	
	//@@author A0126258A
	public String execute() {
//		System.out.println("Keyword: "+this.getKeywords());
		taskList = storageComponent.load();
		ArrayList<Task> result = new ArrayList<Task> ();
		String[] tokenize = this.getKeywords().toLowerCase().split(" ");
		int wordSize = tokenize.length;
			
		for(Task task : taskList) {
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
		searchResults = result;	
		if (searchResults.isEmpty()) {
			return "Input not found";
		} else {
			return "search";
		}
	}

}
