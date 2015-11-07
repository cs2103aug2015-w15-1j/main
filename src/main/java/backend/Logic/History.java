//@@author A0121284N
package main.java.backend.Logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.java.backend.Storage.Task.Task;

public class History {
	
	private static final String LOGGER_CURRENT_STATE = "Received current state: ";
	private static final String LOGGER_STACK_TOP = "Top of the stack: ";
	private static final String LOGGER_STACK_SIZE = "Stack size after push: ";
	private static final String LOGGER_STACK_SIZE_AFTER_POP = "Stack size after push: ";
	private static final String LOGGER_ERROR_EMPTY_STACK = "Error Occured: Stack is empty";
	
	private static Logger historyLogger = Logger.getGlobal();	
	private static History historyObject;
	
	private Stack<ArrayList<Task>> stateUndo = new Stack<ArrayList<Task>>();
	private Stack<ArrayList<Task>> stateRedo = new Stack<ArrayList<Task>>();
	
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
			logHandler = new FileHandler("TankTaskLog.txt",1000000000,10,true);
			logHandler.setFormatter(new SimpleFormatter());
			historyLogger.addHandler(logHandler);
			historyLogger.setUseParentHandlers(false);
		} catch (SecurityException | IOException e) {
			historyLogger.warning("History failed to initialise: " + e.getMessage());
		}
	}
	
	public void exit() {
		logHandler.close();
		System.exit(0);
	}

	public void push(ArrayList<Task> currentState) {
		
		historyLogger.info(LOGGER_CURRENT_STATE + currentState);
		stateUndo.push(currentState);
		historyLogger.info(LOGGER_STACK_TOP + stateUndo.peek());
		historyLogger.info(LOGGER_STACK_SIZE + stateUndo.size());
	}

	public ArrayList<Task> undo() {
		
		if (stateUndo.isEmpty() || stateUndo.peek() == null) {
			return null;
		}
		
		try {
			historyLogger.info(LOGGER_STACK_TOP + stateUndo.peek());
			stateRedo.push(stateUndo.pop());
			historyLogger.info(LOGGER_STACK_TOP + stateUndo.peek());
			historyLogger.info(LOGGER_STACK_SIZE_AFTER_POP + stateUndo.size());
		} catch(EmptyStackException e) {
			historyLogger.warning(LOGGER_ERROR_EMPTY_STACK);
			return null;
		}
		
		return stateUndo.peek();
	}
	
	public ArrayList<Task> redo() {
		
		if (stateRedo.isEmpty() || stateRedo.peek() == null) {
			return null;
		}
		
		try {
			historyLogger.info(LOGGER_STACK_TOP + stateRedo.peek());
			stateUndo.push(stateRedo.pop());
			historyLogger.info(LOGGER_STACK_TOP + stateUndo.peek());
			historyLogger.info(LOGGER_STACK_SIZE_AFTER_POP + stateUndo.size());
		} catch(EmptyStackException e) {
			historyLogger.warning(LOGGER_ERROR_EMPTY_STACK);
			return null;
		}

		return stateUndo.peek();
	}
}
