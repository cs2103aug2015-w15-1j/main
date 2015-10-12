package main.java.backend.History;

import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.java.backend.Logic.LogicController;
import main.java.backend.Storage.Task.Category;

public class History {
	
	Stack<TreeMap<String, Category>> stateStack = new Stack<TreeMap<String, Category>>();
	private static Logger historyLogger = Logger.getGlobal();	
	private FileHandler logHandler;
	private final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static History historyObject;
	
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

	public void push(TreeMap<String, Category> currentState) {
		historyLogger.info("Received current state "+ currentState);
		stateStack.push(currentState);
		historyLogger.info("What's at the top of the stack? "+stateStack.peek());
		historyLogger.info("History stack size after push: "+stateStack.size());
	}

	public TreeMap<String, Category> pop() {
		if (stateStack.isEmpty() || stateStack.peek() == null) {
			return null;
		}
		try {
			historyLogger.info("What's at the top of the stack? "+stateStack.peek());
			stateStack.pop();
			historyLogger.info("what's left in stack"+stateStack.peek());
			historyLogger.info("History stack size after pop: "+stateStack.size());
		} catch(EmptyStackException e) {
			historyLogger.warning("Error Occured: Stack is empty");
			return null;
		}
		return stateStack.peek();
	}
}
