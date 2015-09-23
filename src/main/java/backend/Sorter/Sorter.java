package main.java.backend.Sorter;

import java.util.ArrayList;

import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.Task;

public class Sorter {

	public ArrayList<Category> sort(String field, ArrayList<Category> currentState2) {
		ArrayList<Category> sortedCurrentState = null;
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

	private ArrayList<Category> sortDate(ArrayList<Category> currentState2) {
		// TODO Auto-generated method stub
		return currentState2;
	}

	private ArrayList<Category> sortPriority(ArrayList<Category> currentState2) {
		// TODO Auto-generated method stub
		return currentState2;
	}

}
