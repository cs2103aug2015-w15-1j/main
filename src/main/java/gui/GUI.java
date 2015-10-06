package main.java.gui;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import main.java.backend.Logic.Logic;
import main.java.backend.Storage.Task.Category;
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
	private static final String MESSAGE_HELP = "Change view by typing \"change\" or pressing 'tab'";
	private static final String MESSAGE_SAMPLE_ADDTASK = "to insert a task: \"add [taskname] deadline [date & time] priority [1 to 5] category [name] reminder [date]\"";
	private static final String MESSAGE_SAMPLE_ADDEVENT = "to insert an event: \"add [taskname] event [starting date & time] [ending date & time] priority [1 to 5] category [name] reminder [date]\"";
	private static final String MESSAGE_SAMPLE_ADDFLOAT= "to insert a floating task: \"add [taskname] priority [1 to 5] category [name] reminder [date]\"";
	private static final int SCENE_MAIN = 1;
	private static final int SCENE_FOCUS = 2;
	private static final int NUM_OVERDUE = 1;
	private static final int NUM_TASKS = 2;
	private static final int NUM_EVENTS = 3;
	private static final int NUM_FLOAT = 4;
	private static int currentList = 2;
	private static int currentPosition = 0;
	private static int currentScene = 1;
	private static boolean toggleCate = false;

	private static Logic logicComponent;
	private static GridPane gridPane;
	private static Scene mainScene;	
	private static String userCommands;
	private static Console console;
	private static TextArea consoleText;
	private static PrintStream ps;
	private static TextField userInput;
	private static ArrayList<Task> getFloat;
	private static ArrayList<Task> getTasks;
	private static ArrayList<Task> getEvents;
	private static ArrayList<Task> getFocusList;
	
	private static Label tasks;
	private static Label events;
	private static Label floating;

	private static ListView<Task> listFloat;
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
	}
	
	private static void initGUI() throws IOException, JSONException, ParseException{
		logicComponent = new Logic(DEFAULT_FILENAME);
		System.out.println("GUI component initialised successfully");
		consoleText = new TextArea();
		console = new Console(consoleText);
		ps = new PrintStream(console, true);
		redirectOutput(ps);
		displayStringToScreen(MESSAGE_WELCOME);
		displayStringToScreen(MESSAGE_HELP);
		displayStringToScreen(MESSAGE_SAMPLE_ADDTASK);
		displayStringToScreen(MESSAGE_SAMPLE_ADDEVENT);
		displayStringToScreen(MESSAGE_SAMPLE_ADDFLOAT);
		getFloat= logicComponent.getFloatingTasks();
		getTasks = logicComponent.getUpcomingTasks();
		getEvents = logicComponent.getUpcomingEvents();
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
		gridPane.getChildren().removeAll(floating, tasks, events,
										listCate, listFloat,listTasks,listEvents);
		setUpFocusHeadings();	
		setUpFocusContents();
	}
	
	private void setUpGrid() {
		gridPane = new GridPane();
		gridPane.setFocusTraversable(false);
		gridPane.setGridLinesVisible(false); //checking
		gridPane.setVgap(4);
		gridPane.setHgap(10);
		gridPane.setPadding(new Insets(15,15,15,15));
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
		row1.setPercentHeight(7);
		RowConstraints row2 = new RowConstraints();
		row2.setPercentHeight(63);
		RowConstraints row3 = new RowConstraints();
		row3.setPercentHeight(25);
		gridPane.getRowConstraints().addAll(row1,row2,row3);
	}

	private static void setUpConsole() {
		
		consoleText.setEditable(false);
		consoleText.setDisable(true);
		consoleText.setFocusTraversable(false);
		consoleText.setStyle("-fx-focus-color: transparent;");
		consoleText.getStyleClass().add("txtarea");
		consoleText.getStyleClass().add("txtarea .scroll-pane");
		consoleText.setWrapText(true);
		GridPane.setColumnSpan(consoleText, 3);
		GridPane.setConstraints(consoleText, 0, 2);
		gridPane.getChildren().add(consoleText);
	}
	
	private static void setupUserInput() {
		//userInput
		userInput = new TextField();
		userInput.setStyle("-fx-focus-color: transparent;");
		
		GridPane.setColumnSpan(userInput, 3);
		GridPane.setConstraints(userInput, 0, 3);
		gridPane.getChildren().add(userInput);
	}
	
	private static void setUpHeadings(){
		//Categories Heading
		floating = new Label("Floating:");
		GridPane.setConstraints(floating, 0, 0);
		//Task Heading
		tasks = new Label("Upcoming Tasks:");
		GridPane.setConstraints(tasks, 1, 0);
		//Events Heading
		events = new Label("Upcoming Events:");
		GridPane.setConstraints(events, 2, 0);
		
		gridPane.getChildren().addAll(floating, tasks, events);
	}
	
	private static void setUpContents() throws IOException, JSONException, ParseException {
		
		//floating
		if (toggleCate == false){
			ArrayList<Task> getFloating = logicComponent.getFloatingTasks();	
			listFloat = getList(getFloating, "F");
			listFloat.setFocusTraversable( false );
		GridPane.setConstraints(listFloat, 0, 1);
		gridPane.getChildren().addAll(listFloat);
		}
		else{
			floating.setText("Categories:");
			ArrayList<String> getCate = logicComponent.getCategories();	
			listCate = getStringList(getCate);
			listCate.setFocusTraversable( false );
			GridPane.setConstraints(listCate, 0, 1);
			gridPane.getChildren().add(listCate);
		}
		//tasks
		getTasks = logicComponent.getUpcomingTasks();		
		listTasks = getList(getTasks,"D");
		listTasks.setFocusTraversable( false );
		GridPane.setConstraints(listTasks, 1, 1);
	
		//Events
		getEvents = logicComponent.getUpcomingEvents();
		listEvents = getList(getEvents,"E");
		listEvents.setFocusTraversable( false );
		GridPane.setConstraints(listEvents, 2, 1);
	
	
		gridPane.getChildren().addAll(listTasks,listEvents);
	}
	
	private static void setUpFocusHeadings(){
		gridPane.getChildren().removeAll(focusHeading, detailsHeading);
		focusHeading = new Label(LIST_TASKS);
		GridPane.setConstraints(focusHeading, 0, 0);
		detailsHeading = new Label("Details");
		GridPane.setConstraints(detailsHeading,1,0);
		GridPane.setColumnSpan(detailsHeading, 2);
		gridPane.getChildren().addAll(focusHeading,detailsHeading);
	}
	
	private static void setUpFocusContents() throws IOException, JSONException, ParseException {
		gridPane.getChildren().removeAll(listFocus, detailField);
		ArrayList<Task> getFocusList = logicComponent.getUpcomingTasks();
		listFocus = getList(getFocusList,"");
		listFocus.setFocusTraversable(false);
		GridPane.setConstraints(listFocus, 0, 1);
		gridPane.getChildren().add(listFocus);	

		detailField = new TextArea();
		detailField.setEditable(false);
		detailField.setDisable(true);
		detailField.getStyleClass().add("custom");
		detailField.getStyleClass().add("txtarea");
		detailField.getStyleClass().add("txtarea .scroll-pane");
		detailField.getStyleClass().add("text-area .scroll-bar");
		detailField.setWrapText(true);
		GridPane.setConstraints(detailField, 1, 1);
		GridPane.setColumnSpan(detailField, 2);
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
	
	private static ListView<Task> getList(ArrayList<Task> list, String index){
		if (list == null){
			list = new ArrayList<Task>();
		}	for (int i=0;i<list.size();i++){
			
		logicComponent.setindex(list,i,index);
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
	
	private static ListView<Category> getCateList(ArrayList<Category> list){
		if (list == null){
			list = new ArrayList<Category>();
		}
		ObservableList<Category> tasks = FXCollections.observableArrayList(list);
		ListView<Category> listString = new ListView<Category>(tasks);
		return listString;
	}
	
	private static void displayStringToScreen(String getProcessedInput) {
		System.out.println(getProcessedInput);
	}

	private static void refresh() throws IOException, JSONException, ParseException{
		//System.out.println("refreshing");
		if (currentScene == SCENE_MAIN){
			gridPane.getChildren().removeAll(listFloat,listTasks,listEvents);
			setUpContents();
		}
		else{
			refreshingFocus(currentList);
		}
	}
	
	private static void refreshingFocus(int currentListNum) throws IOException, JSONException, ParseException{
		getFocusList = new ArrayList<Task>();
		if(currentListNum==NUM_OVERDUE){
			getFocusList = logicComponent.getOverdueTasks();
		} else if(currentListNum==NUM_TASKS){
			getFocusList = logicComponent.getUpcomingTasks();
		} else if(currentListNum==NUM_EVENTS){
			getFocusList = logicComponent.getUpcomingEvents();
		} else if (currentListNum==NUM_FLOAT){
			getFocusList = logicComponent.getFloatingTasks();
		}
		changeList(currentListNum);
		changeListDetails(currentListNum);
	
	}
	
	private static void changeList(int listNum) throws IOException, JSONException, ParseException{
		String indexString="";
		gridPane.getChildren().remove(listFocus);
		if (listNum == NUM_OVERDUE){
			indexString = "O";
		}else if (listNum == NUM_TASKS){
			indexString = "D";
		} else if(listNum== NUM_EVENTS){
			indexString = "E";
		} else {
			indexString = "F";
		}
		listFocus = getList(getFocusList,indexString);
		listFocus.setFocusTraversable(false);
		GridPane.setConstraints(listFocus, 0, 1);
		gridPane.getChildren().add(listFocus);	
	}
	
	private static void changeListDetails(int headNum){
		
		if (headNum == NUM_OVERDUE){
			focusHeading.setText(LIST_OVERDUE);
		}
		else if (currentList == NUM_TASKS){
			focusHeading.setText(LIST_TASKS);
		}
		else if (currentList == NUM_EVENTS){
			focusHeading.setText(LIST_EVENTS);
		}
		else if (currentList == NUM_FLOAT){
			focusHeading.setText(LIST_FLOATING);
		}
		
		if(getFocusList==null||getFocusList.isEmpty()){
			detailField.setText(MESSAGE_EMPTY);
			return;
		}
		detailField.setText(getFocusList.get(currentPosition).printFull());
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
						userInputCommads();
					} catch (IOException | JSONException | ParseException e) {
						e.printStackTrace();
					}

				} else if(ke.getCode().equals(KeyCode.TAB)){
					try {
						changeScene();
					} catch (IOException | JSONException | ParseException e) {
						e.printStackTrace();
					}
				} else if(ke.getCode().equals(KeyCode.F1)){
					try {
						toggleCategory();
					} catch (IOException | JSONException | ParseException e) {
						e.printStackTrace();
					}
				}
				if (currentScene == SCENE_FOCUS){
					try {
						changeList(currentList);
					} catch (IOException | JSONException | ParseException e2) {
						e2.printStackTrace();
					}
					changeListDetails(currentList);


					try {
						refresh();
					} catch (IOException | JSONException | ParseException e1) {
						e1.printStackTrace();
					}
					if (ke.getCode().equals(KeyCode.DOWN))
					{	
						try {
							eventDown();
						} catch (IOException | JSONException | ParseException e) {
							e.printStackTrace();
						}
					}
					if (ke.getCode().equals(KeyCode.UP)){
						try {
							eventUp();
						} catch (IOException | JSONException | ParseException e) {
							e.printStackTrace();
						}
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
	
	private static void changeScene() throws IOException, JSONException, ParseException{
		if (currentScene == SCENE_MAIN){
			setUpFocus();
			refreshingFocus(currentList);
			currentScene = SCENE_FOCUS;

		} else {
			setUpMain();
			currentScene = SCENE_MAIN;
		}
	}
	
	private static void userInputCommads() throws JsonParseException, JsonMappingException, IOException, JSONException, ParseException{	
		userCommands = userInput.getText();
		userInput.clear();
		System.out.println("Command: "+ userCommands);
		if (userCommands.toLowerCase().equals("change")){
			changeScene();
		}
		else if(userCommands.toLowerCase().contains("show overdue")){
			currentList = NUM_OVERDUE;
			setUpFocus();
			refreshingFocus(currentList);
			currentScene = SCENE_FOCUS;
		}
		else if(userCommands.toLowerCase().contains("show task")){
			currentList = NUM_TASKS;
			setUpFocus();
			refreshingFocus(currentList);
			currentScene = SCENE_FOCUS;
		}else if(userCommands.toLowerCase().contains("show events")){
			currentList = NUM_EVENTS;
			setUpFocus();
			refreshingFocus(currentList);
			currentScene = SCENE_FOCUS;
		}else if(userCommands.toLowerCase().contains("show float")){
			currentList = NUM_FLOAT;
			setUpFocus();
			refreshingFocus(currentList);
			currentScene = SCENE_FOCUS;
		}
		else {
			String display = logicComponent.executeCommand(userCommands);
			refresh();
			if (display.equals("exit")){
				Platform.exit();
			}
			displayStringToScreen(display);
		}

	}
	
	private static void eventDown() throws IOException, JSONException, ParseException{
		currentPosition++;
		changeList(currentList);
		
		if (currentPosition>=getFocusList.size()){
			currentPosition = getFocusList.size()-1;  
		}
		changeListDetails(currentList);
		
	}
	
	private static void eventUp() throws IOException, JSONException, ParseException{
		currentPosition--;
		if (currentPosition<0){
			currentPosition = 0;  
		}
		refreshingFocus(currentList);
	}
	
	private static void eventLeft() throws IOException, JSONException, ParseException{
		currentList--;
		currentPosition=0;
		if(currentList<=0){
			currentList=1;
		}
		refreshingFocus(currentList);
	}
	
	private static void eventRight() throws IOException, JSONException, ParseException{
		currentList++;
		currentPosition=0;
		if(currentList>4){
			currentList=4;
		}
		refreshingFocus(currentList);
	}
	
	private static void toggleCategory() throws IOException, JSONException, ParseException{
		if (currentScene == SCENE_MAIN){
			gridPane.getChildren().removeAll(listFloat,listCate);
			if (toggleCate==false){
				toggleCate=true;
				floating.setText("Categories:");
				ArrayList<String> getCate = logicComponent.getCategories();	
				listCate = getStringList(getCate);
				listCate.setFocusTraversable( false );
				GridPane.setConstraints(listCate, 0, 1);
				gridPane.getChildren().add(listCate);
				
			} else{
				toggleCate = false;
				floating.setText("Floating:");
				ArrayList<Task> getFloating = logicComponent.getFloatingTasks();	
				listFloat = getList(getFloating,"");
				listFloat.setFocusTraversable( false );
				GridPane.setConstraints(listFloat, 0, 1);
				gridPane.getChildren().add(listFloat);
				
			}
		}else{
			//to-do
		}
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