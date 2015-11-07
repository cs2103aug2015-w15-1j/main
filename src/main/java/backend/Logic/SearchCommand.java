package main.java.backend.Logic;

import java.util.ArrayList;
import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Task;

public class SearchCommand extends Command {
	
	private static final String EXECUTION_SEARCH_SUCCESSFUL =  "search";
	private static final String EXECUTION_SEARCH_UNSUCCESSFUL = "Input not found";
	
	private ArrayList<Task> searchResults;
	private ArrayList<Task> taskList;
	
	private Storage storageComponent;

	//@@author A0121284N
	public SearchCommand(Type typeInput, Storage storage) {
		super(typeInput);
		storageComponent = storage;
	}
	
	//@@author A0121284N
	private String feedback(ArrayList<Task> searchResults) {
		
		if (searchResults.isEmpty()) {
			return EXECUTION_SEARCH_UNSUCCESSFUL;
		} else {
			return EXECUTION_SEARCH_SUCCESSFUL;
		}
	}
	
	//@@author A0121284N
	public ArrayList<Task> getSearchResults() {
		return this.searchResults;
	}
	
	//@@author A0126258A
	public String execute() {
		
		taskList = storageComponent.load();
		searchResults = new ArrayList<Task> ();
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
				searchResults.add(task);
			}
		}

		return feedback(searchResults);
	}

}
