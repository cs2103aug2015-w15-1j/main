//@@author A0121284N
package main.java.backend.Logic;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.logging.Logger;

import main.java.backend.Storage.Task.Task;
import main.java.backend.Util.LoggerGlobal;

public class History {
	
	private static final String LOGGER_CURRENT_STATE = "Received current state: ";
	private static final String LOGGER_STACK_TOP = "Top of the stack: ";
	private static final String LOGGER_STACK_SIZE = "Stack size after push: ";
	private static final String LOGGER_STACK_SIZE_AFTER_POP = "Stack size after push: ";
	private static final String LOGGER_ERROR_EMPTY_STACK = "Error Occured: Stack is empty";
	
	private static final Logger historyLogger = LoggerGlobal.getLogger();	
	private static History historyObject;
	
	private Stack<ArrayList<Task>> stateUndo = new Stack<ArrayList<Task>>();
	private Stack<ArrayList<Task>> stateRedo = new Stack<ArrayList<Task>>();
	
	private History() {
		
	}
	
	public static History getInstance() {
		
		if (historyObject == null) {
			historyObject = new History();
		}
		return historyObject;
	}
	
	public void exit() {
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
