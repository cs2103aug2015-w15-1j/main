package main.java.backend.History;

import java.util.ArrayList;
import java.util.Stack;

public class History {
	
	Stack<ArrayList<String>> stateStack = new Stack<ArrayList<String>>();

	public void push(ArrayList<String> currentState) {
		stateStack.push(currentState);
		
	}

	public ArrayList<String> pop() {
		return stateStack.pop();
	}

}
