package main.java.backend.History;

import java.util.ArrayList;
import java.util.Stack;

import main.java.backend.Logic.Command;
import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.Task;

public class History {
	
	Stack<ArrayList<Category>> stateStack = new Stack<ArrayList<Category>>();

	public void push(ArrayList<Category> currentState) {
		System.out.println("Received current state "+ currentState);
		stateStack.push(currentState);
		System.out.println("What's at the top of the stack? "+stateStack.peek());
		System.out.println("History stack size after pop: "+stateStack.size());
	}

	public ArrayList<Category> pop() {
		if (stateStack.isEmpty() || stateStack.peek() == null) {
			return null;
		}
		System.out.println("What's at the top of the stack? "+stateStack.peek());
		stateStack.pop();
		System.out.println("what's left in stack"+stateStack.peek());
		System.out.println("History stack size after pop: "+stateStack.size());
		return stateStack.peek();
	}
}
