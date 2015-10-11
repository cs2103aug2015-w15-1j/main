package main.java.backend.History;

import java.util.ArrayList;
import java.util.Stack;

import main.java.backend.Logic.Command;
import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.Task;

public class History {
	
	Stack<ArrayList<Category>> stateStack = new Stack<ArrayList<Category>>();

	public void push(ArrayList<Category> currentState) {
		stateStack.push(currentState);
		System.out.println("History stack size after push: "+stateStack.size());
	}

	public ArrayList<Category> pop() {
		stateStack.pop();
		System.out.println("History stack size after pop: "+stateStack.size());
		return stateStack.peek();
	}
}
