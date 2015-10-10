package main.java.backend.Logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import main.java.backend.History.History;
import main.java.backend.Parser.Parser;
import main.java.backend.Search.Search;
import main.java.backend.Sorter.Sorter;
import main.java.backend.Storage.Storage;
import main.java.backend.Storage.Task.Category;
import main.java.backend.Storage.Task.Task;

public class LogicController {
	private static LogicController logicObject;
	private static LogicCommandHandler commandHandlerSubComponent;
	private static LogicCreator creatorSubComponent;
	private static LogicEditor editorSubComponent;
	private static LogicGetter getterSubComponent;
	private static Sorter sorterComponent;
	private static Storage storageComponent;
	private static Parser parserComponent;
	private static Search searcherComponent;
	private static History historyComponent;
	private static ArrayList<Category> currentState;
	private ArrayList<Task> taskList;
	
	private LogicController(String filename) {
		parserComponent = new Parser();
		storageComponent = new Storage(filename);
		sorterComponent = new Sorter();
		searcherComponent = new Search();
		historyComponent = new History();
		commandHandlerSubComponent = new LogicCommandHandler(parserComponent);
		creatorSubComponent = LogicCreator.getInstance(storageComponent);
		editorSubComponent = LogicEditor.getInstance(storageComponent);
		getterSubComponent = LogicGetter.getInstance(storageComponent);
		System.out.println("Logic component initialised successfully");
	}

	public static LogicController getInstance(String filename) throws FileNotFoundException, IOException {
		if (LogicController.logicObject == null) {
			LogicController.logicObject = new LogicController(filename);
		}
		return LogicController.logicObject;
	}
	
	public String executeCommand(String userInput) throws JsonParseException, JsonMappingException, IOException, JSONException, ParseException {
		Command commandObject = commandHandlerSubComponent.parseCommand(userInput);
		String feedbackString = "";
		System.out.println(commandObject.getType());
		switch (commandObject.getType()) {
			case ADD :
				feedbackString = creatorSubComponent.execute(commandObject);
				break;
			case EDIT :
				feedbackString = editorSubComponent.execute(commandObject);
				break;
			case SORT :
//				feedbackString = sorterComponent.execute(commandObject);
				break;
			case SEARCH : 
//				feedbackString = searcherComponent.execute(commandObject);
				break;
			case EXIT :
				exit();
				break;
			case UNDO :
				feedbackString = undo();
				break;
		}
		updateCurrentState();
		updateHistoryStack();
		return feedbackString;
	}

	private String undo() {
		currentState = historyComponent.pop();
		storageComponent.saveData(currentState);
		return "Undo successful";
	}

	private void exit() {
		System.exit(0);
	}
	
	public void setindex(ArrayList<Task> list, int i, int index) {
		storageComponent.setIndex(list.get(i),index);
	}
	
	public ArrayList<String> retrieveStringData(String dataType){
		return getterSubComponent.retrieveStringData(dataType);
	}
	
	public ArrayList<Category> retrieveCategoryData(String dataType) {
		return getterSubComponent.retrieveCategoryData(dataType);
	}
	
	public ArrayList<Task> retrieveTaskData(String dataType) throws IOException, JSONException, ParseException {
		return getterSubComponent.retrieveTaskData(dataType);
	}
	
	private void updateCurrentState() throws IOException {
		currentState = storageComponent.getCategoryList();
		taskList = storageComponent.getTaskList();
	}
	
	private void updateHistoryStack() {
		historyComponent.push(currentState);
	}
}
