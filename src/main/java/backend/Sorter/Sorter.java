package main.java.backend.Sorter;

import java.util.ArrayList;

public class Sorter {

	public ArrayList<String> sort(String field, ArrayList<String> currentState) {
		switch (field) {
			case "sortP":
				ArrayList<String> sortedCurrentState = sortPriority(currentState);
				return sortedCurrentState;
			case "sortD":
				ArrayList<String> sortedCurrentState =sortDate(currentState);
				return sortedCurrentState;
		}
		return currentState;
	}

	private void sortDate(ArrayList<String> currentState) {
		// TODO Auto-generated method stub
		
	}

	private ArrayList<String> sortPriority(ArrayList<String> currentState) {
		// TODO Auto-generated method stub
		
	}

}
