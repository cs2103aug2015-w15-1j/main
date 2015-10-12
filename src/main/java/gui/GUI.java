package main.java.gui;

import java.io.FileNotFoundException;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import main.java.backend.Logic.LogicController;
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
	private static final String MESSAGE_COMMAND_HELP = "Press F1 to see a list of commands";
	private static final String MESSAGE_SAMPLE_ADDTASK = "to insert a task: \"add [taskname] deadline [date & time] priority [1 to 5] category [name] reminder [date]\"";
	private static final String MESSAGE_SAMPLE_ADDEVENT = "to insert an event: \"add [taskname] event [starting date & time] [ending date & time] priority [1 to 5] category [name] reminder [date]\"";
	private static final String MESSAGE_SAMPLE_ADDFLOAT= "to insert a floating task: \"add [taskname] priority [1 to 5] category [name] reminder [date]\"";
	private static final String COMMAND_SHOW_TASKS = "show tasks";
	private static final String COMMAND_SHOW_EVENTS = "show events";
	private static final String COMMAND_SHOW_OVERDUE = "show overdue"; 
	private static final String COMMAND_SHOW_FLOAT = "show float";
	private static final String COMMAND_EXIT = "exit";
	private final KeyCombination KC = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
	
	private static final int SCENE_MAIN = 1;
	private static final int SCENE_FOCUS = 2;
	private static final int NUM_TASKS = 1;
	private static final int NUM_EVENTS = 2;
	private static final int NUM_OVERDUE = 3;
	private static final int NUM_FLOAT = 4;
	private static int currentList = 1;
	private static int currentPosition = 0;
	private static int currentScene = 1;
	private static boolean toggleCate = false;
	private static boolean toggleToday = false;

	private static LogicController logicComponent;
	private static GUIController controller;
	private static GridPane gridPane;
	private static Scene mainScene;	
	private static String userCommands;
	private static Console console;
	private static TextArea consoleText;
	private static PrintStream ps;
	private static TextField userInput;
	
	private static Label tasks;
	private static Label events;
	private static Label floating;

	private static ListView<Task> listFloat;
	private static ListView<String> listCate;
	private static ListView<Task> listTasks;
	private static ListView<Task> listEvents;
	private static ListView<Task> listOverdue;
	
	private static Label focusHeading;
	private static Label detailsHeading;
	private static TextArea detailField;
	private static ListView<Task> listFocus;
	
	public static void main(String[] args) throws IOException, JSONException, ParseException {
		initGUI();
		launch(args);
	}
	
	private static void initGUI() throws FileNotFoundException, IOException, JSONException, ParseException{
		logicComponent = LogicController.getInstance(DEFAULT_FILENAME);
		controller = new GUIController();
		System.out.println("GUI component initialised successfully");
		consoleText = new TextArea();
		console = new Console(consoleText);
		ps = new PrintStream(console, true);
		redirectOutput(ps);
		displayStringToScreen(MESSAGE_WELCOME);
		displayStringToScreen(MESSAGE_COMMAND_HELP);
		controller.retrieveAllData();
	}

	private void retrieveToday(){
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("TankTask");
		setUpMainScene();
		setupUserInput();
		setUpConsole();
		
		primaryStage.setScene(mainScene);
		primaryStage.show();
		determineEvents();
	}
	
	private void setUpMainScene() throws IOException, JSONException, ParseException{
		setUpGrid(); //general info
		initGrid(); //setting up individual sizing
		setUpGridContents();//setting up contents;
		mainScene = new Scene(gridPane);
		mainScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		
	}
	
	private static void setUpGridContents() throws IOException, JSONException, ParseException {
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
		column2.setPercentWidth(14);
		ColumnConstraints column3 = new ColumnConstraints();
		column3.setPercentWidth(38);
		ColumnConstraints column4 = new ColumnConstraints();
		column4.setPercentWidth(24);
		gridPane.getColumnConstraints().addAll(column1, column2, column3,column4);

		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(7);
		RowConstraints row2 = new RowConstraints();
		row2.setPercentHeight(75);
		RowConstraints row3 = new RowConstraints();
		row3.setPercentHeight(12);
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
		GridPane.setColumnSpan(consoleText, 4);
		GridPane.setConstraints(consoleText, 0, 2);
		gridPane.getChildren().add(consoleText);
	}
	
	private static void setupUserInput() {
		//userInput
		userInput = new TextField();
		userInput.setStyle("-fx-focus-color: transparent;");
		
		GridPane.setColumnSpan(userInput, 4);
		GridPane.setConstraints(userInput, 0, 3);
		gridPane.getChildren().add(userInput);
	}
	
	private static void setUpHeadings(){
		//Categories Heading
		floating = new Label("Floating:");
		GridPane.setConstraints(floating, 3, 0);
		//Task Heading
		tasks = new Label("Upcoming Tasks:");
		GridPane.setColumnSpan(tasks, 2);
		GridPane.setConstraints(tasks, 0, 0);
		//Events Heading
		events = new Label("Upcoming Events:");
		GridPane.setConstraints(events, 2, 0);
		
		gridPane.getChildren().addAll(floating, tasks, events);
	}

	private static void setUpContents() {
		controller.updateIndex();
		controller.retrieveAllData();
		
		//tasks		
		listTasks = getList(controller.getTasksList());
		GridPane.setColumnSpan(listTasks, 2);
		listTasks.setFocusTraversable( false );
		GridPane.setConstraints(listTasks, 0, 1);

		//Events
		listEvents = getList(controller.getEventsList());
		listEvents.setFocusTraversable( false );
		GridPane.setConstraints(listEvents, 2, 1);

		gridPane.getChildren().addAll(listTasks,listEvents);
		
		//Overdue shows only if there is any overdue tasks
		if (controller.getOverdueList().size()!=0){
			floating.setText("Overdue:");
			listOverdue = getList(controller.getOverdueList());
			listOverdue.setFocusTraversable(false);
			listOverdue.setId("listOverdue");
			GridPane.setConstraints(listOverdue, 3,1);
			gridPane.getChildren().addAll(listOverdue);
		}
		else{
			//floating
			if (toggleCate == false){
				floating.setText("floating:");
				listFloat = getList(controller.getFloatList());
				listFloat.setFocusTraversable( false );
				GridPane.setConstraints(listFloat, 3, 1);
				gridPane.getChildren().addAll(listFloat);
			}
			else{
				floating.setText("Categories:");
				listCate = getStringList(controller.getCateList());
				listCate.setFocusTraversable( false );
				GridPane.setConstraints(listCate, 3, 1);
				gridPane.getChildren().add(listCate);
			}
		}

	}

	private static void setUpFocusHeadings(){
		gridPane.getChildren().removeAll(focusHeading, detailsHeading);
		focusHeading = new Label(LIST_TASKS);
		GridPane.setConstraints(focusHeading, 0, 0);
		detailsHeading = new Label("Details");
		GridPane.setConstraints(detailsHeading,1,0);
		GridPane.setColumnSpan(detailsHeading, 3);
		gridPane.getChildren().addAll(focusHeading,detailsHeading);
	}
	
	private static void setUpFocusContents() throws IOException, JSONException, ParseException {
		gridPane.getChildren().removeAll(listFocus, detailField);
		ArrayList<Task> getFocusList = controller.retrieveTask();
		listFocus = getList(getFocusList);
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
		GridPane.setColumnSpan(detailField, 3);
		if (getFocusList.isEmpty()){
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
			gridPane.getChildren().removeAll(listOverdue, listFloat,listTasks,listEvents);
			setUpContents();
		}
		else{
			refreshingFocus(currentList);
		}
	}
	
	private static void refreshingFocus(int currentListNum) throws IOException, JSONException, ParseException{
		controller.updateIndex();
		controller.retrieveAllData();
		controller.determineList(currentListNum);
		changeList(currentListNum);
		changeListDetails(currentListNum);
	
	}
	
	private static void changeList(int listNum) throws IOException, JSONException, ParseException{
		int index = 0;
		gridPane.getChildren().remove(listFocus);
		if (listNum == NUM_OVERDUE){
			index = controller.getTasksList().size()+controller.getEventsList().size();
		}else if (listNum == NUM_TASKS){
			index = 0;
		} else if(listNum== NUM_EVENTS){
			index = controller.getTasksList().size();
		} else {
			index = controller.getTasksList().size()+controller.getEventsList().size()+controller.getOverdueList().size();
		}
		controller.setIndex(controller.getFocusList(), index);
		listFocus = getList(controller.getFocusList());
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
		assert controller.getFocusList()!=null;
		
		if(controller.getFocusList().isEmpty()){
			detailField.setText(MESSAGE_EMPTY);
			return;
		}
		detailField.setText(controller.getFocusList().get(currentPosition).printFull());
	}
	
	private void determineEvents(){
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
				} else if(KC.match(ke)){
					logicComponent.executeCommand("undo");
					
				}				else if (ke.getCode().equals(KeyCode.F1)){
					helpPopUp();
				} else if(ke.getCode().equals(KeyCode.F2)){
					try {
						toggleCategory();
					} catch (IOException | JSONException | ParseException e) {
						e.printStackTrace();
					}
				} else if (ke.getCode().equals(KeyCode.F12)){
					logicComponent.executeCommand("exit");
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
	
	private void helpPopUp() {
		Stage pop = new Stage();
		VBox comp = new VBox();
		pop.setTitle("help");
		ArrayList<String> help = new ArrayList<String>();
		help.add(MESSAGE_HELP);
		help.add(MESSAGE_SAMPLE_ADDTASK);
		help.add(MESSAGE_SAMPLE_ADDEVENT);
		help.add(MESSAGE_SAMPLE_ADDFLOAT);
		ListView<String> helpList = getStringList(help);
		VBox.setVgrow(helpList, Priority.ALWAYS);
		comp.getChildren().add(helpList);
		

		Scene stageScene = new Scene(comp, 500, 500);
		stageScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		pop.setScene(stageScene);
		pop.show();
		comp.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.F1)){
					pop.close();
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
			setUpGridContents();
			currentScene = SCENE_MAIN;
		}
	}
	
	private static void userInputCommads() throws JsonParseException, JsonMappingException, IOException, JSONException, ParseException{	
		userCommands = userInput.getText();
		userInput.clear();
		System.out.println("Command: "+ userCommands);
		String display = logicComponent.executeCommand(userCommands);
		if (display.equals("change")){
			changeScene();
		}
		else if(display.equals(COMMAND_SHOW_OVERDUE)){
			currentList = NUM_OVERDUE;
			setUpFocus();
			refreshingFocus(currentList);
			currentScene = SCENE_FOCUS;
		}
		else if(display.equals(COMMAND_SHOW_TASKS)){
			currentList = NUM_TASKS;
			setUpFocus();
			refreshingFocus(currentList);
			currentScene = SCENE_FOCUS;
		}else if(display.equals(COMMAND_SHOW_EVENTS)){
			currentList = NUM_EVENTS;
			setUpFocus();
			refreshingFocus(currentList);
			currentScene = SCENE_FOCUS;
		}else if(display.equals(COMMAND_SHOW_FLOAT)){
			currentList = NUM_FLOAT;
			setUpFocus();
			refreshingFocus(currentList);
			currentScene = SCENE_FOCUS;
		} else if(display.equals(COMMAND_EXIT)){
			exit();
		} else {
			refresh();
			displayStringToScreen(display);
		}
	}
	
	private static void exit(){
		Platform.exit();
	}
	
	private static void eventDown() throws IOException, JSONException, ParseException{
		currentPosition++;
		changeList(currentList);
		
		if (currentPosition>=controller.getFocusList().size()){
			currentPosition = controller.getFocusList().size()-1;  
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
		controller.retrieveAllData();
		if (currentScene == SCENE_MAIN){
			gridPane.getChildren().removeAll(listFloat,listCate);
			if (toggleCate==false){
				toggleCate=true;
				floating.setText("Categories:");
				listCate = getStringList(controller.getCateList());
				listCate.setFocusTraversable( false );
				GridPane.setConstraints(listCate, 3, 1);
				gridPane.getChildren().add(listCate);
				
			} else{
				toggleCate = false;
				floating.setText("Floating:");
				listFloat = getList(controller.getFloatList());
				listFloat.setFocusTraversable( false );
				GridPane.setConstraints(listFloat, 3, 1);
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