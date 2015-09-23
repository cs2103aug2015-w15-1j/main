package main.java.gui;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import main.java.backend.Logic.Logic;
import main.java.backend.Storage.Task.Task;

public class GUI extends Application{
	//Possible messages
	private static final String MESSAGE_WELCOME = "Welcome to TankTask!";
	private static final String MESSAGE_EMPTY = "List is empty";
	private static final String DEFAULT_FILENAME="filename";
	private static final int SCENE_MAIN = 1;
	private static final int SCENE_FOCUS = 2;
	private static int currentList = 3;
	private static int currentPosition = 0;
	private static int currentScene = 1;

	static Logic logicComponent;
	static String userCommands;
	private static  Console console;
	private static TextArea consoleText;
	private static PrintStream ps;
	private static ArrayList<Task> getOverdue;
	private static ArrayList<Task> getFloating;
	private static ArrayList<Task> getTasks;
	private static ArrayList<Task> getEvents;
	private static Stage thestage;
	private static Scene mainScene;
	private static Scene focusScene;
	
	static GridPane gridPane;
	static GridPane gridPaneFocus; 
	Label overDue;
	Label categories;
	Label tasks;
	Label events;
	Label floating;
	static TextField userInput;
	static ListView<Task> listOverdue;
	static ListView<String> listCate;
	static ListView<Task> listFloats;
	static ListView<Task> listTasks;
	static ListView<Task> listEvents;
	

	public static void main(String[] args) throws IOException, JSONException, ParseException {
		initGUI();
		launch(args);

	}
	public static void initGUI() throws IOException, JSONException, ParseException{
		logicComponent = new Logic(DEFAULT_FILENAME);
		consoleText = new TextArea();
		console = new Console(consoleText);
		ps = new PrintStream(console, true);
		redirectOutput(ps);
		displayStringToScreen(MESSAGE_WELCOME);
		getOverdue= logicComponent.getOverdueTasks();
		getFloating = logicComponent.getFloatingTasks();
		getTasks = logicComponent.getTasks();
		getEvents = logicComponent.getEvents();
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("TankTask");
		thestage = primaryStage;
		
		setUpMainScene();
	//	setUpFocusScene();
		
		primaryStage.setScene(mainScene);
		primaryStage.show();
		userInputEvents();

	}
	private void setUpMainScene() throws IOException, JSONException, ParseException{
		//setting up general info on grid
		setUpGrid();

		//setting up individual size of grid panel
		initGrid();
		
		//setting up contents;
		setupUserInput();
		setUpHeadings();
		setUpConsole();
		setUpContents();
		
		mainScene = new Scene(gridPane);
		
		mainScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		
	}
	private void setUpGrid() {
		gridPane = new GridPane();
		gridPane.setFocusTraversable(false);
		gridPane.setGridLinesVisible(true); //checking
		gridPane.setVgap(4);
		gridPane.setHgap(10);
		gridPane.setPadding(new Insets(20,20,20,20));
		gridPane.setPrefSize(1000, 600);
		
		gridPaneFocus = new GridPane();
		gridPaneFocus.setFocusTraversable(false);
		gridPaneFocus.setGridLinesVisible(true); //checking
		gridPaneFocus.setVgap(4);
		gridPaneFocus.setHgap(10);
		gridPaneFocus.setPadding(new Insets(20,20,20,20));
		gridPaneFocus.setPrefSize(1000, 600);
	}

	private void initGrid() {

		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(24);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(38);
		ColumnConstraints column3 = new ColumnConstraints();
		column3.setPercentWidth(38);
		gridPane.getColumnConstraints().addAll(column1, column2, column3);

		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(5);
		RowConstraints row2 = new RowConstraints();
		row2.setPercentHeight(20);
		RowConstraints row3 = new RowConstraints();
		row3.setPercentHeight(5);
		RowConstraints row4 = new RowConstraints();
		row4.setPercentHeight(38);
		RowConstraints row5 = new RowConstraints();
		row5.setPercentHeight(27);
		gridPane.getRowConstraints().addAll(row1,row2,row3,row4,row5);
	}

	private void setUpConsole() {
		//console area
		consoleText.setEditable(false);
		consoleText.setFocusTraversable(false);
		consoleText.setStyle("-fx-focus-color: transparent;");
		GridPane.setColumnSpan(consoleText, 4);
		GridPane.setConstraints(consoleText, 0, 4);
		gridPane.getChildren().add(consoleText);
	}
	
	private void setupUserInput() {
		//userInput
		userInput = new TextField();
		userInput.setStyle("-fx-focus-color: transparent;");
		
		GridPane.setColumnSpan(userInput, 4);
		GridPane.setConstraints(userInput, 0, 5);
		gridPane.getChildren().add(userInput);
	}
	
	private void setUpHeadings(){
		//OverDue heading
		overDue = new Label("Overdue Task:");
		GridPane.setConstraints(overDue, 0, 2);
		
		//Categories Heading
		categories = new Label("Categories:");
		GridPane.setConstraints(categories, 0, 0);
		//Task Heading
		tasks = new Label("Upcoming Tasks:");
		GridPane.setConstraints(tasks, 1, 0);
		//Events Heading
		events = new Label("Upcoming Events:");
		GridPane.setConstraints(events, 2, 0);
	
		gridPane.getChildren().addAll(overDue,categories, tasks, events);
	}
	
	private static void setUpContents() throws IOException, JSONException, ParseException {

		//OverDue List
		getOverdue= logicComponent.getOverdueTasks();
		listOverdue = getList(getOverdue);
		listOverdue.setFocusTraversable( false );
		GridPane.setConstraints(listOverdue, 0, 3);
		
		//Categories
		ArrayList<String> getCate = logicComponent.getCategories();	
		listCate = getStringList(getCate);
		listCate.setFocusTraversable( false );
		GridPane.setConstraints(listCate, 0, 1);
		

		//tasks
		getTasks = logicComponent.getTasks();		
		listTasks = getList(getTasks);
		listTasks.setFocusTraversable( false );
		GridPane.setConstraints(listTasks, 1, 1);
		GridPane.setRowSpan(listTasks, 3);

		//Events
		getEvents = logicComponent.getEvents();
		listEvents = getList(getEvents);
		listEvents.setFocusTraversable( false );
		GridPane.setConstraints(listEvents, 2, 1);
		GridPane.setRowSpan(listEvents, 3);
	

		gridPane.getChildren().addAll(listOverdue,listCate,listTasks,listEvents);
	}
	
	private void setUpFocusScene(){
	
		//setting up individual size of grid panel
		initGridFocus();

		//setting up contents;
	//	setupUserInputFocus();
		setUpHeadingsFocus();
	//	setUpConsole();
	//	setUpContents();

		focusScene = new Scene(gridPaneFocus);

		focusScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());


	}
	
	private void setUpHeadingsFocus() {
		//Task Heading
		Label tasksHeading = new Label("Upcoming Tasks:");
		GridPane.setConstraints(tasksHeading, 1, 0);
		gridPaneFocus.getChildren().addAll(tasksHeading);

	}
	private void setupUserInputFocus() {
		//userInput
		userInput = new TextField();
		userInput.setStyle("-fx-focus-color: transparent;");

		GridPane.setConstraints(userInput, 0, 4);
		gridPaneFocus.getChildren().add(userInput);
	
	}
	private void initGridFocus() {

		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(30);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(70);
		
		gridPaneFocus.getColumnConstraints().addAll(column1, column2);

		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(5);
		RowConstraints row2 = new RowConstraints();
		row2.setPercentHeight(65);
		RowConstraints row3 = new RowConstraints();
		row3.setPercentHeight(25);
		RowConstraints row4 = new RowConstraints();
		row4.setPercentHeight(5);
		
		gridPaneFocus.getRowConstraints().addAll(row1,row2,row3,row4);
		
	}
	private static void redirectOutput(PrintStream stream){
		System.setOut(stream);
		System.setErr(stream);
	}
	
	private static ListView<Task> getList(ArrayList<Task> list){
		if (list == null){
			list = new ArrayList<Task>();
		}
		ObservableList<Task> tasks = FXCollections.observableArrayList(list);
		ListView<Task> listTask = new ListView<Task>(tasks);
		return listTask;
	}
	
	private static ListView<String> getStringList(ArrayList<String> list){
		if (list == null){
			list = new ArrayList<String>();
		}
		ObservableList<String> tasks = FXCollections.observableArrayList(list);
		ListView<String> listString = new ListView<String>(tasks);
		return listString;
	}
	
	private static void displayStringToScreen(String getProcessedInput) {
		System.out.println(getProcessedInput);
	}

	private static void refresh() throws IOException, JSONException, ParseException{
		//System.out.println("refreshing");
		gridPane.getChildren().removeAll(listOverdue,listCate,listFloats,listTasks,listEvents);
		setUpContents();
	}
	
	private static void userInputEvents(){
		System.out.println("top task:");
		if(getTasks == null || getTasks.isEmpty()){
			System.out.println(MESSAGE_EMPTY);
		} else {
			System.out.println(getTasks.get(currentPosition));
		}
			userInput.setOnKeyPressed(new EventHandler<KeyEvent>()
			{
				@Override
				public void handle(KeyEvent ke)
				{	
					if (ke.getCode().equals(KeyCode.ENTER))
					{
						try {
							try {
								userInputCommads();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (JsonParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JsonMappingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					if (ke.getCode().equals(KeyCode.DOWN))
					{	
						eventDown();
					}
					if (ke.getCode().equals(KeyCode.UP)){
						eventUp();
					}
					if (ke.getCode().equals(KeyCode.LEFT)){
						eventLeft();
					}
					if (ke.getCode().equals(KeyCode.RIGHT)){
						eventRight();
					}
				}
			});
	}
	private static void userInputCommads() throws JsonParseException, JsonMappingException, IOException, JSONException, ParseException{
		System.out.println("command found");	
		userCommands = userInput.getText();
			userInput.clear();
			System.out.println(userCommands);
			/*if (userCommands.equals("change view")){
				if (currentScene == SCENE_MAIN){
					thestage.setScene(focusScene);
					currentScene = SCENE_FOCUS;
				} else {
					thestage.setScene(mainScene);
					currentScene = SCENE_MAIN;
				}
			} else {*/
				String display = logicComponent.executeCommand(userCommands);
				refresh();
				displayStringToScreen(display);
			//}

	}
	private static void eventDown(){
		currentPosition++;
		if (currentList == 1){
			if (getOverdue==null||getOverdue.isEmpty()){
				System.out.println(MESSAGE_EMPTY);
				return;
			}
			if (currentPosition>=getOverdue.size()){
				currentPosition = getOverdue.size()-1;  
				}
			System.out.println(getOverdue.get(currentPosition));
		}
		else if (currentList == 2){
			if (getFloating==null||getFloating.isEmpty()){
				System.out.println(MESSAGE_EMPTY);
				return;
			}
			if (currentPosition>=getFloating.size()){
				currentPosition = getFloating.size()-1;  
			}
			System.out.println(getFloating.get(currentPosition));
		}
		else if (currentList == 3){
			if (getTasks==null||getTasks.isEmpty()){
				System.out.println(MESSAGE_EMPTY);
				return;
			}
			if (currentPosition>=getTasks.size()){
				currentPosition = getTasks.size()-1;  
			}
			System.out.println(getTasks.get(currentPosition));
		}
		else if (currentList == 4){
			if (getEvents==null||getEvents.isEmpty()){
				return;
			}
			if (currentPosition>=getEvents.size()){
				currentPosition = getEvents.size()-1;  
			}
			System.out.println(getEvents.get(currentPosition));
		}
	}
	
	private static void eventUp(){
		currentPosition--;
		if (currentList == 1){
			if (getOverdue==null||getOverdue.isEmpty()){
				System.out.println(MESSAGE_EMPTY);
				return;
			}
			if (currentPosition<0){
				currentPosition = 0;  
				}
			System.out.println(getOverdue.get(currentPosition));
		}
		else if (currentList == 2){
			if (getFloating==null||getFloating.isEmpty()){
				System.out.println(MESSAGE_EMPTY);
				return;
			}
			if (currentPosition<0){
				currentPosition = 0;  
			}
			System.out.println(getFloating.get(currentPosition));
		}
		else if (currentList == 3){
			if (getTasks==null||getTasks.isEmpty()){
				System.out.println(MESSAGE_EMPTY);
				return;
			}
			if (currentPosition<0){
				currentPosition = 0;  
			}
			System.out.println(getTasks.get(currentPosition));
		}
		else if (currentList == 4){
			if (getEvents == null || getEvents.isEmpty()){
				System.out.println(MESSAGE_EMPTY);
				return;
			}
			if (currentPosition<0){
				currentPosition = 0;  
			}
			System.out.println(getEvents.get(currentPosition));
		}
	}
	
	private static void eventLeft(){
		currentList--;
		currentPosition=0;
		if(currentList<0){
			currentList=0;
		}
		if (currentList == 1){
			if (getOverdue==null||getOverdue.isEmpty()){
				System.out.println(MESSAGE_EMPTY);
				return;
			}
			System.out.println(getOverdue.get(currentPosition));
		}
		else if (currentList == 2){
			if (getFloating==null||getFloating.isEmpty()){
				System.out.println(MESSAGE_EMPTY);
				return;
			}
			System.out.println(getFloating.get(currentPosition));
		}
		else if (currentList == 3){
			if (getTasks==null||getTasks.isEmpty()){
				System.out.println(MESSAGE_EMPTY);
				return;
			}
			System.out.println(getTasks.get(currentPosition));
		}
		else if (currentList == 4){
			if (getEvents==null||getEvents.isEmpty()){
				System.out.println(MESSAGE_EMPTY);
				return;
			}
			System.out.println(getEvents.get(currentPosition));
		}
	}
	
	private static void eventRight(){
		currentList++;
		currentPosition=0;
		if(currentList>4){
			currentList=4;
		}
		
		if (currentList == 1){
			if (getOverdue==null||getOverdue.isEmpty()){
				System.out.println(MESSAGE_EMPTY);
				return;
			}
			System.out.println(getOverdue.get(currentPosition));
		}
		else if (currentList == 2){
			if (getFloating==null||getFloating.isEmpty()){
				System.out.println(MESSAGE_EMPTY);
				return;
			}
			System.out.println(getFloating.get(currentPosition));
		}
		else if (currentList == 3){
			if (getTasks == null|| getTasks.isEmpty()){
				System.out.println(MESSAGE_EMPTY);
				return;
			}
			System.out.println(getTasks.get(currentPosition));
		}
		else if (currentList == 4){
			if (getEvents==null||getEvents.isEmpty()){
				System.out.println(MESSAGE_EMPTY);
				return;
			}
			System.out.println(getEvents.get(currentPosition));
		}
	}
}