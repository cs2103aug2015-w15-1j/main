package main.java.backend.History;

import java.util.ArrayList;
import java.util.Stack;

import main.java.backend.Storage.Task.Task;

public class History {
	
	Stack<ArrayList<Task>> stateStack = new Stack<ArrayList<Task>>();

	public void push(ArrayList<Task> currentState) {
		stateStack.push(currentState);
	}

	public ArrayList<Task> pop() {
		return stateStack.pop();
	}

}
