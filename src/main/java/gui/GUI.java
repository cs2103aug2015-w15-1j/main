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
	private static final String DEFAULT_FILENAME="filename.txt";
	private static final String LIST_OVERDUE = "Overdue Tasks:";
	private static final String LIST_TASKS = "Upcoming Tasks:";
	private static final String LIST_EVENTS = "Upcoming Events:";
	private static final String LIST_FLOATING = "Floating Tasks:";
	private static final String MESSAGE_HELP = "Change view by typing \"changeview\"";
	private static final int SCENE_MAIN = 1;
	private static final int SCENE_FOCUS = 2;
	private static int currentList = 2;
	private static int currentPosition = 0;
	private static int currentScene = 1;

	private static Logic logicComponent;
	private static String userCommands;
	private static Console console;
	private static TextArea consoleText;
	private static PrintStream ps;
	private static ArrayList<Task> getOverdue;
	private static ArrayList<Task> getFloating;
	private static ArrayList<Task> getTasks;
	private static ArrayList<Task> getEvents;
	private static Scene mainScene;
	private static GridPane gridPane;
	private static Label overDue;
	private static Label categories;
	private static Label tasks;
	private static Label events;
	private static TextField userInput;
	private static ListView<Task> listOverdue;
	private static ListView<String> listCate;
	private static ListView<Task> listTasks;
	private static ListView<Task> listEvents;
	
	private static Label focusHeading;
	private static Label detailsHeading;
	private static TextArea detailField;
	private static ListView<Task> listFocus;
	
	public static void main(String[] args) throws IOException, JSONException, ParseException {
		initGUI();
		launch(args);
		System.out.println("GUI component initialised successfully");
	}
	private static void initGUI() throws IOException, JSONException, ParseException{
		logicComponent = new Logic(DEFAULT_FILENAME);
		
		consoleText = new TextArea();
		console = new Console(consoleText);
		ps = new PrintStream(console, true);
		redirectOutput(ps);
		displayStringToScreen(MESSAGE_WELCOME);
		displayStringToScreen(MESSAGE_HELP);
		getOverdue= logicComponent.getOverdueTasks();
		getFloating = logicComponent.getFloatingTasks();
		getTasks = logicComponent.getTasks();
		getEvents = logicComponent.getEvents();
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("TankTask");
		setUpMainScene();
		setupUserInput();
		setUpConsole();
		
		primaryStage.setScene(mainScene);
		primaryStage.show();
		userInputEvents();

	}
	private void setUpMainScene() throws IOException, JSONException, ParseException{
		setUpGrid(); //general info
		initGrid(); //setting up individual sizing
		setUpMain();//setting up contents;
		mainScene = new Scene(gridPane);
		mainScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		
	}
	private static void setUpMain() throws IOException, JSONException, ParseException {
		gridPane.getChildren().removeAll(focusHeading, detailsHeading, listFocus, detailField);
		setUpHeadings();	
		setUpContents();
		
	}
	private static void setUpFocus() throws IOException, JSONException, ParseException {
		gridPane.getChildren().removeAll(overDue,categories, tasks, events,
										listOverdue,listCate,listTasks,listEvents);
		setUpFocusHeadings();	
		setUpFocusContents();
		
	}
	
	private void setUpGrid() {
		gridPane = new GridPane();
		gridPane.setFocusTraversable(false);
		gridPane.setGridLinesVisible(false); //checking
		gridPane.setVgap(4);
		gridPane.setHgap(10);
		gridPane.setPadding(new Insets(20,20,20,20));
		gridPane.setPrefSize(1000, 600);
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

	private static void setUpConsole() {
		
		consoleText.setEditable(false);
		consoleText.setFocusTraversable(false);
		consoleText.setStyle("-fx-focus-color: transparent;");
		GridPane.setColumnSpan(consoleText, 3);
		GridPane.setConstraints(consoleText, 0, 4);
		gridPane.getChildren().add(consoleText);
	}
	
	private static void setupUserInput() {
		//userInput
		userInput = new TextField();
		userInput.setStyle("-fx-focus-color: transparent;");
		
		GridPane.setColumnSpan(userInput, 3);
		GridPane.setConstraints(userInput, 0, 5);
		gridPane.getChildren().add(userInput);
	}
	
	private static void setUpHeadings(){
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
	private static void setUpFocusHeadings(){
		focusHeading = new Label(LIST_TASKS);
		GridPane.setConstraints(focusHeading, 0, 0);
		detailsHeading = new Label("Details");
		GridPane.setConstraints(detailsHeading,1,0);
		GridPane.setColumnSpan(detailsHeading, 2);
		gridPane.getChildren().addAll(focusHeading,detailsHeading);
	}
	
	private static void setUpFocusContents() throws IOException, JSONException, ParseException {
		ArrayList<Task> getFocusList = logicComponent.getTasks();
		listFocus = getList(getFocusList);
		listFocus.setFocusTraversable(false);
		GridPane.setRowSpan(listFocus, 3);
		GridPane.setConstraints(listFocus, 0, 1);
		gridPane.getChildren().add(listFocus);	

		detailField = new TextArea();
		detailField.setEditable(false);
		detailField.getStyleClass().add("custom");
		GridPane.setConstraints(detailField, 1, 1);
		GridPane.setColumnSpan(detailField, 2);
		GridPane.setRowSpan(detailField, 3);
		if (getFocusList==null||getFocusList.isEmpty()){
			detailField.setText(MESSAGE_EMPTY);
		} else{
			detailField.setText(getFocusList.get(0).toString());
		}
			gridPane.getChildren().add(detailField);
	}
	
	private static void redirectOutput(PrintStream stream){
		System.setOut(stream);
		//System.setErr(stream);
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
		if (currentScene == SCENE_MAIN){
			gridPane.getChildren().removeAll(listOverdue,listCate,listTasks,listEvents);
			setUpContents();
		}
		else{
			gridPane.getChildren().removeAll(focusHeading, detailsHeading, listFocus, detailField);
			setUpFocus();
		}
	}
	
	private static void userInputEvents(){
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
								e.printStackTrace();
							}
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
					if (currentScene == SCENE_FOCUS){
						if (ke.getCode().equals(KeyCode.DOWN))
						{	
							eventDown();
						}
						if (ke.getCode().equals(KeyCode.UP)){
							eventUp();
						}
						if (ke.getCode().equals(KeyCode.LEFT)){
							try {
								eventLeft();
							} catch (IOException | JSONException | ParseException e) {
								e.printStackTrace();
							}
						}
						if (ke.getCode().equals(KeyCode.RIGHT)){
							try {
								eventRight();
							} catch (IOException | JSONException | ParseException e) {
								e.printStackTrace();
							}
						}
					}
				}
			});
	}
	private static void userInputCommads() throws JsonParseException, JsonMappingException, IOException, JSONException, ParseException{	
		userCommands = userInput.getText();
			userInput.clear();
			System.out.println("Command: "+ userCommands);
			if (userCommands.toLowerCase().equals("changeview")){
				if (currentScene == SCENE_MAIN){
				setUpFocus();
				currentScene = SCENE_FOCUS;
					
				} else {
					setUpMain();
					currentScene = SCENE_MAIN;
				}
			} else {
				String display = logicComponent.executeCommand(userCommands);
				refresh();
				displayStringToScreen(display);
			}
			
	}
	private static void eventDown(){
		currentPosition++;
		if (currentList == 1){
			focusHeading.setText(LIST_OVERDUE);
			if (getOverdue==null||getOverdue.isEmpty()){
				detailField.setText(MESSAGE_EMPTY);
				return;
			}
			if (currentPosition>=getOverdue.size()){
				currentPosition = getOverdue.size()-1;  
				}
			detailField.setText(getOverdue.get(currentPosition).printFull());
		}
		
		else if (currentList == 2){
			focusHeading.setText(LIST_TASKS);
			if (getTasks==null||getTasks.isEmpty()){
				detailField.setText(MESSAGE_EMPTY+" nope");
				return;
			}
			if (currentPosition>=getTasks.size()){
				currentPosition = getTasks.size()-1;  
			}
			detailField.setText(getTasks.get(currentPosition).printFull());
		}
		else if (currentList == 3){
			focusHeading.setText(LIST_EVENTS);
			if (getEvents==null||getEvents.isEmpty()){
				detailField.setText(MESSAGE_EMPTY);
				return;
			}
			if (currentPosition>=getEvents.size()){
				currentPosition = getEvents.size()-1;  
			}
			detailField.setText(getEvents.get(currentPosition).printFull());
		}
		else if (currentList == 4){
			focusHeading.setText(LIST_FLOATING);
			if (getFloating==null||getFloating.isEmpty()){
				detailField.setText(MESSAGE_EMPTY);
				return;
			}
			if (currentPosition>=getFloating.size()){
				currentPosition = getFloating.size()-1;  
			}
			detailField.setText(getFloating.get(currentPosition).printFull());
		}
	}
	
	private static void eventUp(){
		currentPosition--;
		if (currentPosition<0){
			currentPosition = 0;  
		}
		if (currentList == 1){
			focusHeading.setText(LIST_OVERDUE);
			if (getOverdue==null||getOverdue.isEmpty()){
				detailField.setText(MESSAGE_EMPTY);
				return;
			}
			detailField.setText(getOverdue.get(currentPosition).printFull());
		}
		
		else if (currentList == 2){
			focusHeading.setText(LIST_TASKS);
			if (getTasks==null||getTasks.isEmpty()){
				detailField.setText(MESSAGE_EMPTY+" YEAH");
				return;
			}
			detailField.setText(getTasks.get(currentPosition).printFull());
		}
		else if (currentList == 3){
			focusHeading.setText(LIST_EVENTS);
			if (getEvents == null || getEvents.isEmpty()){
				detailField.setText(MESSAGE_EMPTY);
				return;
			}
			detailField.setText(getEvents.get(currentPosition).printFull());
		}
		else if (currentList == 4){
			focusHeading.setText(LIST_FLOATING);
			if (getFloating==null||getFloating.isEmpty()){
				detailField.setText(MESSAGE_EMPTY);
				return;
			}
			detailField.setText(getFloating.get(currentPosition).printFull());
		}
	}
	
	private static void eventLeft() throws IOException, JSONException, ParseException{
		currentList--;
		currentPosition=0;
		if(currentList<0){
			currentList=1;
		}
		changeList(currentList);
		if (currentList == 1){
			focusHeading.setText(LIST_OVERDUE);
			if (getOverdue==null||getOverdue.isEmpty()){
				detailField.setText(MESSAGE_EMPTY);
				return;
			}
			
			detailField.setText(getOverdue.get(currentPosition).printFull());
		}
		
		else if (currentList == 2){
			focusHeading.setText(LIST_TASKS);
			if (getTasks==null||getTasks.isEmpty()){
				detailField.setText(MESSAGE_EMPTY);
				return;
			}
			detailField.setText(getTasks.get(currentPosition).printFull());
		}
		else if (currentList == 3){
			focusHeading.setText(LIST_EVENTS);
			if (getEvents==null||getEvents.isEmpty()){
				detailField.setText(MESSAGE_EMPTY);
				return;
			}
			detailField.setText(getEvents.get(currentPosition).printFull());
		}
		else if (currentList == 4){
			focusHeading.setText(LIST_FLOATING);
			if (getFloating==null||getFloating.isEmpty()){
				detailField.setText(MESSAGE_EMPTY);
				return;
			}
			detailField.setText(getFloating.get(currentPosition).printFull());
		}
	}
	
	private static void eventRight() throws IOException, JSONException, ParseException{
		currentList++;
		currentPosition=0;
		if(currentList>4){
			currentList=4;
		}
		changeList(currentList);
		if (currentList == 1){
			focusHeading.setText(LIST_OVERDUE);
			if (getOverdue==null||getOverdue.isEmpty()){
				detailField.setText(MESSAGE_EMPTY);
				return;
			}
			detailField.setText(getOverdue.get(currentPosition).printFull());
		}
		
		else if (currentList == 2){
			focusHeading.setText(LIST_TASKS);
			if (getTasks == null|| getTasks.isEmpty()){
				detailField.setText(MESSAGE_EMPTY);
				return;
			}
			detailField.setText(getTasks.get(currentPosition).printFull());
		}
		else if (currentList == 3){
			focusHeading.setText(LIST_EVENTS);
			if (getEvents==null||getEvents.isEmpty()){
				detailField.setText(MESSAGE_EMPTY);
				return;
			}
			detailField.setText(getEvents.get(currentPosition).printFull());
		}
		else if (currentList == 4){
			focusHeading.setText(LIST_FLOATING);
			if (getFloating==null||getFloating.isEmpty()){
				detailField.setText(MESSAGE_EMPTY);
				return;
			}
			detailField.setText(getFloating.get(currentPosition).printFull());
		}
	}
	private static void changeList(int listNum) throws IOException, JSONException, ParseException{
		gridPane.getChildren().remove(listFocus);
		ArrayList<Task> getFocusList = new ArrayList<Task>();
		if(listNum==1){
			getFocusList = logicComponent.getOverdueTasks();
		} else if(listNum==2){
			getFocusList = logicComponent.getTasks();
		} else if(listNum==3){
			getFocusList = logicComponent.getEvents();
		} else if (listNum==4){
			getFocusList = logicComponent.getFloatingTasks();
		}
		listFocus = getList(getFocusList);
		listFocus.setFocusTraversable(false);
		GridPane.setRowSpan(listFocus, 3);
		GridPane.setConstraints(listFocus, 0, 1);
		gridPane.getChildren().add(listFocus);	
	}
	private static void subTaskView(){
		if (currentScene == SCENE_MAIN){
			gridPane.getChildren().remove(listEvents);
			events.setText("SubTasks: ");
			//to-do
		}
		else {
			gridPane.getChildren().remove(detailField);
			detailsHeading.setText("SubTask List");
			//to-do
			//notes: either get logic to return the arraylist straight
			//without saying which specific task. 
			//or get the name of specific task and call logic again (waste time)
		}
	}
}