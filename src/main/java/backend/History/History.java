package main.java.backend.History;

import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.java.backend.Logic.LogicController;
import main.java.backend.Storage.Task.Task;

public class History {
	
	Stack<TreeMap<Integer, Task>> stateUndo = new Stack<TreeMap<Integer, Task>>();
	Stack<TreeMap<Integer, Task>> stateRedo = new Stack<TreeMap<Integer, Task>>();
	
	private static Logger historyLogger = Logger.getGlobal();	
	private static History historyObject;
	
	private FileHandler logHandler;
	
	
	private History() {
		initLogger();
		historyLogger.info("Logic component initialised successfully");
	}
	
	public static History getInstance() {
		
		if (historyObject == null) {
			historyObject = new History();
		}
		return historyObject;
	}
	
	private void initLogger() {
		
		try {
			logHandler = new FileHandler("HistoryComponentLog.txt",true);
			logHandler.setFormatter(new SimpleFormatter());
			historyLogger.addHandler(logHandler);
			historyLogger.setUseParentHandlers(false);
		} catch (SecurityException | IOException e) {
			historyLogger.warning("History failed to initialise: " + e.getMessage());
		}
	}

	public void push(TreeMap<Integer, Task> currentState) {
		
		historyLogger.info("Received current state "+ currentState);
		stateUndo.push(currentState);
		historyLogger.info("What's at the top of the stack? "+stateUndo.peek());
		historyLogger.info("History stack size after push: "+stateUndo.size());
	}

	public TreeMap<Integer, Task> undo() {
		
		if (stateUndo.isEmpty() || stateUndo.peek() == null) {
			return null;
		}
		
		try {
			historyLogger.info("What's at the top of the stack? "+stateUndo.peek());
			stateRedo.push(stateUndo.pop());
			historyLogger.info("what's left in stack"+stateUndo.peek());
			historyLogger.info("History stack size after pop: "+stateUndo.size());
		} catch(EmptyStackException e) {
			historyLogger.warning("Error Occured: Stack is empty");
			return null;
		}
		
		return stateUndo.peek();
	}
	
	public TreeMap<Integer, Task> redo() {
		
		if (stateRedo.isEmpty() || stateRedo.peek() == null) {
			return null;
		}

		return stateRedo.pop();
	}
}
