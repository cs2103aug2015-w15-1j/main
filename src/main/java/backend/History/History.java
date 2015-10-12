package main.java.backend.History;

import java.util.Stack;
import java.util.TreeMap;

import main.java.backend.Storage.Task.Category;

public class History {
	
	Stack<TreeMap<String, Category>> stateStack = new Stack<TreeMap<String, Category>>();

	public void push(TreeMap<String, Category> currentState) {
		System.out.println("Received current state "+ currentState);
		stateStack.push(currentState);
		System.out.println("What's at the top of the stack? "+stateStack.peek());
		System.out.println("History stack size after pop: "+stateStack.size());
	}

	public TreeMap<String, Category> pop() {
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
