package main.java.gui;

import java.util.ArrayList;
import java.util.Scanner;

import main.java.backend.Logic.Logic;
import main.java.backend.Storage.Task.Task;

public class GUIStub {
	private static final String EXECUTION_SHOW_CATEGORY_SUCCESSFUL = "category shown";
	private static final String EXECUTION_SEARCH_SUCCESSFUL = "keyword found";
	private static final String EXECUTION_RETURN_COMMAND_SUCCESSFUL = "Returning to home";
	private static final String EXECUTION_SHOW_TASKS_SUCCESSFUL = "tasks shown";
	private static final String EXECUTION_SHOW_FLOATING_TASKS_SUCCESSFUL = "floating tasks shown";
	private static final String EXECUTION_SHOW_EVENT_SUCCESSFUL = "events shown";
	private static ArrayList<String> getCategoryArrayListlogicComponent;
	private static ArrayList<Task> getSearchResultsList;
	private static ArrayList<Task> getCurrentState;
	private static ArrayList<Task> getTasks;
	private static ArrayList<Task> getFloatingTasks;
	private static ArrayList<Task> getEvents;
	
	public static void main(String[] args) {
		Logic logicComponent = new Logic(args[0]);
		Scanner inputScanner = new Scanner(System.in);
		while(true){
			String getProcessedInput = logicComponent.executeCommand(inputScanner.nextLine());
			if(getProcessedInput.equals(EXECUTION_SHOW_CATEGORY_SUCCESSFUL)) {
				getCategoryArrayListlogicComponent = logicComponent.getCategories();
				displayStringArrayToScreen(getCategoryArrayListlogicComponent);
			} else if(getProcessedInput.equals(EXECUTION_SEARCH_SUCCESSFUL)) {
				getSearchResultsList = logicComponent.getSearchResultsList();
				displayTaskArrayToScreen(getSearchResultsList);
			} else if (getProcessedInput.equals(EXECUTION_RETURN_COMMAND_SUCCESSFUL)) {
				getCurrentState = logicComponent.getCurrentState();
				displayTaskArrayToScreen(getCurrentState);
			} else if (getProcessedInput.equals(EXECUTION_SHOW_TASKS_SUCCESSFUL)) {
				getTasks = logicComponent.getTasks();
				displayTaskArrayToScreen(getTasks);
			} else if (getProcessedInput.equals(EXECUTION_SHOW_FLOATING_TASKS_SUCCESSFUL)) {
				getFloatingTasks = logicComponent.getFloatingTasks();
				displayTaskArrayToScreen(getFloatingTasks);
			} else if (getProcessedInput.equals(EXECUTION_SHOW_EVENT_SUCCESSFUL)) {
				getEvents = logicComponent.getEvents();
				displayTaskArrayToScreen(getEvents);
			} else {
				displayStringToScreen(getProcessedInput);
			}
		}
	}

	private static void displayStringToScreen(String getProcessedInput) {
		System.out.println(getProcessedInput);
	}

	private static void displayStringArrayToScreen(ArrayList<String> inputArray) {
		for (int i = 0; i < inputArray.size(); i++){
			displayStringToScreen(inputArray.get(i));
		}
	}

	private static void displayTaskArrayToScreen(ArrayList<Task> inputArray) {
		for (int i = 0; i < inputArray.size(); i++){
			displayStringToScreen(inputArray.get(i).toString());
		}
	}
		
}
