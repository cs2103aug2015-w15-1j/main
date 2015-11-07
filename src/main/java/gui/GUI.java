
package main.java.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import com.sun.javafx.application.LauncherImpl;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.backend.Storage.Task.Task;

//@@author A0126125R
public class GUI extends Application{
	
	//Possible messages
	private static final String MESSAGE_WELCOME = "Welcome to TankTask!";
	private static final String MESSAGE_EMPTY = "List is empty";
	private static final String MESSAGE_COMMAND_HELP = "Press F1 to see a list of commands";
	
	//Label contents constants
	private static final String LIST_OVERDUE = "Overdue Tasks:";
	private static final String LIST_TASKS = "ToDo:";
	private static final String LIST_EVENTS = "Events:";
	private static final String LIST_FLOATING = "Floating:";
	private static final String LIST_TODAY = "Today's / Tomorrow's: ";
	private static final String LIST_SEARCH = "Search Results:";
	
	//Possible commands retrieved from Logic.
	private static final String COMMAND_SHOW_TASKS = "show todo";
	private static final String COMMAND_SHOW_EVENTS = "show events";
	private static final String COMMAND_SHOW_OVERDUE = "show overdue"; 
	private static final String COMMAND_SHOW_FLOAT = "show float";
	private static final String COMMAND_SHOW_TODAY = "show today";
	private static final String COMMAND_SHOW_COMPLETE = "show complete";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_EXIT = "exit";
	
	//Hot key combinations
	private final KeyCombination undo = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
	private final KeyCombination redo = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
	private final KeyCombination shiftUp = new KeyCodeCombination(KeyCode.UP, KeyCombination.SHIFT_DOWN);
	private final KeyCombination shiftDown= new KeyCodeCombination(KeyCode.DOWN, KeyCombination.SHIFT_DOWN);
	
	//necessary variables for one-instance check
	private File file;
    private RandomAccessFile randomAccessFile;
    private FileLock fileLock;
    
	//necessary variables for nagivation purposes.
	private static final int SCENE_MAIN = 1;
	private static final int SCENE_FOCUS = 2;
	private static final int NUM_TASKS = 1;
	private static final int NUM_EVENTS = 2;
	private static final int NUM_OVERDUE = 3;
	private static final int NUM_FLOAT = 4;
	private static final int NUM_TODAY_TASKS_EVENTS = 5;
	private static final int NUM_SEARCH = 6;
	private static int currentList = 1;
	private static int currentPosition = 0;
	private static int currentScene = 1;
	private static boolean toggleFloat = false;
	private static boolean noti = false;
	private static boolean isHelp = false;
	private static int commandIndex;
	
	//List of nodes for the gridPane
	//nodes for both Focus and default view
	private static TextArea consoleText;
	private static TextField userInput;
	
	//Specific nodes for Default View
	private static Label tasks;
	private static Label events;
	private static Label floating;
	private static ListView<TextFlow> listFloat;
	private static ListView<TextFlow> listCate;
	private static ListView<TextFlow> listTasks;
	private static ListView<TextFlow> listEvents;
	private static ListView<TextFlow> listOverdue;

	//Specific nodes for Focus View
	private static Label focusHeading;
	private static Label detailsHeading;
	private static TextArea detailField;
	private static ListView<TextFlow> listFocus;
	
	private static final int COUNT_LIMIT = 10000;
	private static final int paneHeight = 600;
	private static final int paneWidth = 1000;
	private static GUIController controller;
	private static HelpView help;
	private static GridPane gridPane;
	private static Stage stage;
	private static Scene mainScene;	
	private static String userCommands;
	private static Console console;
	private static PrintStream ps;
	private static ArrayList<String> recentCommands;
	

	public static void main(String[] args) {
        LauncherImpl.launchApplication(GUI.class, GuiPreloader.class, args);
    }

	/**
	 * This method checks if TankTask is already running. 
	 * It will close the new window in 3 second or when user press okay, whichever is earlier.
	 */
	@Override
	public void init() throws Exception, FileNotFoundException, IOException, JSONException, ParseException{
		file = new File("flag");
		randomAccessFile = new RandomAccessFile(file, "rw");
		fileLock = randomAccessFile.getChannel().tryLock();
		//System.out.println(fileLock == null); //check whether file is locked.
		if (fileLock == null) {

			Platform.runLater(new Runnable(){

				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("WARNING!");
					alert.setHeaderText(null);
					alert.setContentText("TankTask is already opened in another window.");
					alert.showAndWait().ifPresent(response -> {
					     if (response == ButtonType.OK) {
							Platform.exit();
							System.exit(0);
					     }
					 });
				}
			});
			Thread.sleep(3000);
			Platform.exit();
			System.exit(0);
		}

		controller = new GUIController();
		for (int i = 0; i < COUNT_LIMIT; i++) {
            double progress = (100 * i) / COUNT_LIMIT;
            LauncherImpl.notifyPreloader(this, new GuiPreloader.ProgressNotification(progress));
        }
		help = new HelpView();
		consoleText = new TextArea();
		console = new Console(consoleText);
		ps = new PrintStream(console, true);
		recentCommands = new ArrayList<String>();
		listFocus = new ListView<TextFlow>();
		redirectOutput(ps);
		System.out.println(MESSAGE_WELCOME);
		System.out.println(MESSAGE_COMMAND_HELP);
		controller.retrieveAllData();
	}

	private static void redirectOutput(PrintStream stream){
		System.setOut(stream);
	//	System.setErr(stream); //error codes are not shown in console.
	}

	@Override
	public void start(Stage primaryStage) throws Exception {	
		primaryStage.setTitle("TankTask");
		setUpDefault();
		setupUserInput();
		setUpConsole();
		Image icon = new Image(getClass().getResourceAsStream("tank.png")); 
		primaryStage.getIcons().add(icon);

		primaryStage.setScene(mainScene);
		stage = primaryStage;
		stage.show();
		
		determineEvents();
		reminders();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent arg0) {
                try {
                	if (isHelp){
                		help.close();
                	}
                    fileLock.release();
                    randomAccessFile.close();
                    System.out.println("Closing");
                    System.exit(0);
                } catch (Exception ex) {
                   ex.printStackTrace();
                }

            }
        });	
	}
	
	private void reminders(){
		new Timer().schedule(
			    new TimerTask() {

			        @Override
			        public void run() {
			        	Platform.runLater(new Runnable(){

			    			@Override
			    			public void run() {
			    				noti = controller.getNoti();
			    				// System.out.println("reminder check"); //checks that it runs into the method every minute
						           if (noti){
						            	runNoti();
						            }
						           else {
						        	   refresh();
						           }
			    			}
			    			});
			           
			        }
			    }, 500L, 60000L);
	}

	protected void runNoti() {
		String content = "";
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("REMINDER!");
		alert.setHeaderText(null);
		ArrayList<Task> reminders = controller.getReminderList();
		for (int i=0;i<reminders.size();i++){
			content+=reminders.get(i).reminderPrint();
		}
		alert.setContentText(content);
		alert.showAndWait();
		refresh();
	}

	private void setUpDefault() throws IOException, JSONException, ParseException{
		setUpGrid(); //general info
		initGrid(); //setting up individual sizing
		setUpGridContents();//setting up contents;
		
		mainScene = new Scene(gridPane,paneWidth,paneHeight);
		mainScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
	}

	private static void setUpGridContents() throws IOException, JSONException, ParseException {
		gridPane.getChildren().removeAll(focusHeading, detailsHeading, listFocus, detailField);
		setUpHeadings();	
		setUpContents();
	}

	private static void setUpFocus(){
		gridPane.getChildren().removeAll(floating, tasks, events,
				listCate, listFloat,listTasks,listEvents);

		try {
			setUpFocusHeadings();
			setUpFocusContents();
		} catch (IOException | JSONException | ParseException e) {
			e.printStackTrace();
		}
	}

	private void setUpGrid() {
		gridPane = new GridPane();
		Image image = new Image(GUI.class.getResourceAsStream("Resources/background.png"));
		BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true,true);
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
		Background background = new Background(backgroundImage);
		gridPane.setBackground(background);
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
		column2.setPercentWidth(13);
		ColumnConstraints column3 = new ColumnConstraints();
		column3.setPercentWidth(39);
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
		userInput.getStyleClass().add("user");

		GridPane.setColumnSpan(userInput, 4);
		GridPane.setConstraints(userInput, 0, 3);
		gridPane.getChildren().add(userInput);
	}

	private static void setUpHeadings(){
		gridPane.getChildren().removeAll(floating, tasks, events);
		//Categories Heading
		floating = new Label(LIST_FLOATING);
		floating.setId("mylabel");
		GridPane.setConstraints(floating, 3, 0);
		//Task Heading
		tasks = new Label(LIST_TASKS);
		tasks.setId("mylabel");
		GridPane.setColumnSpan(tasks, 2);
		GridPane.setConstraints(tasks, 0, 0);
		//Events Heading
		events = new Label(LIST_EVENTS);
		events.setId("mylabel");
		GridPane.setConstraints(events, 2, 0);

		gridPane.getChildren().addAll(floating, tasks, events);
	}

	private static void setUpContents() {
		controller.retrieveAllData();
		gridPane.getChildren().removeAll(listTasks,listEvents,listOverdue,listCate);
		//tasks		
		listTasks = convertList(controller.getTasksList());
		GridPane.setColumnSpan(listTasks, 2);
		listTasks.setFocusTraversable( false );
		
		listTasks.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                event.consume();
            }
        });
		GridPane.setConstraints(listTasks, 0, 1);

		//Events
		listEvents = convertList(controller.getEventsList());
		listEvents.setFocusTraversable( false );
		listEvents.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                event.consume();
            }
        });
		GridPane.setConstraints(listEvents, 2, 1);

		gridPane.getChildren().addAll(listTasks,listEvents);

		//Overdue shows only if there is any overdue tasks
		if (controller.getOverdueList().size()!=0){
			toggleFloat = false;
			floating.setText("Overdue:");
			listOverdue = convertList(controller.getOverdueList());
			listOverdue.setFocusTraversable(false);
			listOverdue.setId("listOverdue");
			listOverdue.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

	            @Override
	            public void handle(MouseEvent event) {
	                event.consume();
	            }
	        });
			GridPane.setConstraints(listOverdue, 3,1);
			gridPane.getChildren().addAll(listOverdue);
		}
		else{
			//floating
			toggleFloat = true;
			floating.setText(LIST_FLOATING);
			listFloat = convertList(controller.getFloatList());
			listFloat.setFocusTraversable( false );
			listFloat.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

	            @Override
	            public void handle(MouseEvent event) {
	                event.consume();
	            }
	        });
			GridPane.setConstraints(listFloat, 3, 1);
			gridPane.getChildren().addAll(listFloat);
		}

	}

	private static void setUpFocusHeadings(){
		gridPane.getChildren().removeAll(focusHeading, detailsHeading);
		focusHeading = new Label(LIST_TASKS);
		focusHeading.setId("mylabel");
		GridPane.setConstraints(focusHeading, 0, 0);
		detailsHeading = new Label("Details");
		detailsHeading.setId("mylabel");
		GridPane.setConstraints(detailsHeading,1,0);
		GridPane.setColumnSpan(detailsHeading, 3);
		gridPane.getChildren().addAll(focusHeading,detailsHeading);
	}

	private static void setUpFocusContents() throws IOException, JSONException, ParseException {
		gridPane.getChildren().removeAll(listFocus, detailField);
		ArrayList<Task> getFocusList = controller.retrieveTask();
		listFocus = convertList(getFocusList);
		listFocus.setFocusTraversable(false);
		GridPane.setConstraints(listFocus, 0, 1);
		gridPane.getChildren().add(listFocus);	

		detailField = new TextArea();
		detailField.setEditable(false);
		detailField.setDisable(true);
		detailField.setId("custom");
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

	private static ListView<TextFlow> convertList(ArrayList<Task> list){
		assert list != null;
		ObservableList<TextFlow> tasks = FXCollections.observableArrayList();
		for (int i=0;i<list.size();i++){
			TextFlow flow = new TextFlow();
			Text newlabel = new Text();
			newlabel.setText(list.get(i).toString());
			if (i%2 == 1){
				newlabel.setFill(Color.WHITE);;
			}
			flow.getChildren().add(newlabel);
			flow.setMinWidth(1.0);
			flow.setPrefWidth(1.0);
			flow.setMaxWidth(10000.0);
			tasks.add(flow);
		}
		ListView<TextFlow> listTask = new ListView<TextFlow>(tasks);
		return listTask;
	}

	private static void refresh(){
		//System.out.println("refreshing");
		controller.retrieveAllData();
		if (currentScene == SCENE_MAIN){
			gridPane.getChildren().removeAll(tasks,events,floating,listOverdue, listFloat,listTasks,listEvents);
			setUpHeadings();
			setUpContents();
		}
		else{
			refreshingFocus(currentList);
		}
	}

	private static void refreshingFocus(int currentListNum){
		controller.determineList(currentListNum);
		try {
			changeFocusList(currentListNum);
		} catch (IOException | JSONException | ParseException e) {
			e.printStackTrace();
		}
		changeFocusListDetails(currentListNum);

	}

	private static void changeFocusList(int listNum) throws IOException, JSONException, ParseException{
		gridPane.getChildren().remove(listFocus);
		listFocus = convertList(controller.getFocusList());
		listFocus.setFocusTraversable(false);
		listFocus.setMouseTransparent( false );
		listFocus.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                event.consume();
            }
        });
		listFocus.scrollTo(currentPosition);
		listFocus.getSelectionModel().select(currentPosition);
		GridPane.setConstraints(listFocus, 0, 1);
		gridPane.getChildren().add(listFocus);	
	}

	private static void changeFocusListDetails(int headNum){

		if (headNum == NUM_OVERDUE){
			focusHeading.setText(LIST_OVERDUE);
		} else if (headNum == NUM_TASKS){
			focusHeading.setText(LIST_TASKS);
		} else if (headNum == NUM_EVENTS){
			focusHeading.setText(LIST_EVENTS);
		} else if (headNum == NUM_FLOAT){
			focusHeading.setText(LIST_FLOATING);
		} else if (headNum == NUM_TODAY_TASKS_EVENTS){
			focusHeading.setText(LIST_TODAY);
		} else if (headNum == NUM_SEARCH){
			focusHeading.setText(LIST_SEARCH);
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
					userInputCommands();	

				} else if(ke.getCode().equals(KeyCode.TAB)){
					changeScene();
				} else if(undo.match(ke)){
					String response = controller.executeCommand("undo");

					refresh();

					System.out.println(response);

				} else if(redo.match(ke)){
					String response = controller.executeCommand("redo");

					refresh();

					System.out.println(response);

				}else if (ke.getCode().equals(KeyCode.F1)){
					isHelp = true;
					help.helpPopUp();
				} else if(ke.getCode().equals(KeyCode.F2)){
					try {
						toggleFloat();
					} catch (IOException | JSONException | ParseException e) {
						e.printStackTrace();
					}
				} else if(ke.getCode().equals(KeyCode.F3)){
					showTodayTasksEvents();
				} else if(ke.getCode().equals(KeyCode.F4)){
					showCompleted();
				} else if (ke.getCode().equals(KeyCode.ESCAPE)){
				
					controller.executeCommand("exit");
				} else 	if (shiftUp.match(ke)){
						if (!recentCommands.isEmpty()){
							userInput.setText(recentCommands.get(commandIndex));
							commandIndex--;
							if (commandIndex<0){
								commandIndex = 0;
							}
						}
					} else if (shiftDown.match(ke)){
						if (!recentCommands.isEmpty()){
							commandIndex++;
							if (commandIndex>recentCommands.size()-1){
								commandIndex = recentCommands.size()-1;
							}
							userInput.setText(recentCommands.get(commandIndex));
						}
						if (currentScene==SCENE_FOCUS){
							try {
								eventDown();
							} catch (IOException | JSONException | ParseException e) {
								e.printStackTrace();
							}
						}
					}
					if (currentScene == SCENE_FOCUS){
						try {
							changeFocusList(currentList);
						} catch (IOException | JSONException | ParseException e2) {
							e2.printStackTrace();
						}
						changeFocusListDetails(currentList);

						refresh();

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

	protected static void showCompleted() {
		try {
			setUpGridContents();
		} catch (IOException | JSONException | ParseException e) {
			e.printStackTrace();
		}
		currentScene = SCENE_MAIN;
		tasks.setText("Completed Tasks: ");
		events.setText("Completed Events: ");
		floating.setText("Completed Floating: ");
		gridPane.getChildren().removeAll(listTasks,listEvents,listFloat);
		listTasks = convertList(controller.getCompletedTasks());
		listTasks.setFocusTraversable(false);
		listTasks.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                event.consume();
            }
        });
		listEvents = convertList(controller.getCompletedEvents());
		listEvents.setFocusTraversable(false);
		listEvents.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                event.consume();
            }
        });
		listFloat = convertList(controller.getCompletedFloat());
		listFloat.setFocusTraversable(false);
		listFloat.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                event.consume();
            }
        });
		GridPane.setConstraints(listTasks, 0, 1);
		GridPane.setColumnSpan(listTasks, 2);
		GridPane.setConstraints(listEvents, 2, 1);
		GridPane.setConstraints(listFloat, 3, 1);
		gridPane.getChildren().addAll(listTasks,listEvents,listFloat);
		
	}

	private void showTodayTasksEvents() {
		currentList = NUM_TODAY_TASKS_EVENTS;
		setUpFocus();
		refreshingFocus(currentList);
		currentScene = SCENE_FOCUS;
	}

	private static void changeScene() {
		if (currentScene == SCENE_MAIN){
			
			setUpFocus();
			refreshingFocus(currentList);

			currentScene = SCENE_FOCUS;

		} else {
			try {
				setUpGridContents();
			} catch (IOException | JSONException | ParseException e) {
				e.printStackTrace();
			}
			currentScene = SCENE_MAIN;
		}
	}

	private static void userInputCommands(){
		if (!userInput.getText().isEmpty()){
			userCommands = userInput.getText();
			userCommands = userCommands.trim();
			if (userCommands.length()!=0){
				recentCommands.add(userCommands);
				commandIndex = recentCommands.size()-1;
				userInput.clear();
				System.out.println("Command: "+ userCommands);
				String display = controller.executeCommand(userCommands);
				switch (display){
				case "change" :
					changeScene();
					break;
				case COMMAND_SHOW_OVERDUE:
				case "showO": 
					currentPosition = 0;
					currentList = NUM_OVERDUE;
					setUpFocus();
					refreshingFocus(currentList);
					currentScene = SCENE_FOCUS;
					break;
				case COMMAND_SHOW_TASKS:
				case "showT" : 
					currentPosition = 0;
					currentList = NUM_TASKS;
					setUpFocus();
					refreshingFocus(currentList);
					currentScene = SCENE_FOCUS;
					break;
				case COMMAND_SHOW_EVENTS:
				case "showE":
					currentPosition = 0;
					currentList = NUM_EVENTS;
					setUpFocus();
					refreshingFocus(currentList);
					currentScene = SCENE_FOCUS;
					break;
				case COMMAND_SHOW_FLOAT:
				case "showF":
					currentPosition = 0;
					currentList = NUM_FLOAT;
					setUpFocus();
					refreshingFocus(currentList);
					currentScene = SCENE_FOCUS;
					break;
				case COMMAND_SHOW_TODAY:
				case "showD":
					currentPosition = 0;
					currentList = NUM_TODAY_TASKS_EVENTS;
					setUpFocus();
					refreshingFocus(currentList);
					currentScene = SCENE_FOCUS;
					break;
				case COMMAND_SHOW_COMPLETE:
				case "showC":
					showCompleted();
					break;
				case COMMAND_SEARCH: 
					currentPosition = 0;
					currentList = NUM_SEARCH;
					setUpFocus();
					refreshingFocus(currentList);
					currentScene = SCENE_FOCUS;
					break;
				case COMMAND_EXIT: 
					exit();
					break;
				default:
					refresh();
					System.out.println(display);
				}	
				controller.retrieveAllData();
			}
		}
	}

	private static void exit(){
		if (isHelp){
			help.close();
			isHelp = false;
		}
		Platform.exit();
		System.exit(0);
	}

	private static void eventDown() throws IOException, JSONException, ParseException{
		currentPosition++;
		changeFocusList(currentList);

		if (currentPosition>=controller.getFocusList().size()){
			currentPosition = controller.getFocusList().size()-1;  
		}
		changeFocusListDetails(currentList);
		listFocus.scrollTo(currentPosition);
		listFocus.getSelectionModel().select(currentPosition);

	}

	private static void eventUp() throws IOException, JSONException, ParseException{
		currentPosition--;
		if (currentPosition<0){
			currentPosition = 0;  
		}
		refreshingFocus(currentList);
		listFocus.scrollTo(currentPosition);
		listFocus.getSelectionModel().select(currentPosition);
	}

	private static void eventLeft() throws IOException, JSONException, ParseException{
		currentList--;
		currentPosition=0;
		if(currentList<=0){
			currentList=1;
		}
		refreshingFocus(currentList);
		listFocus.scrollTo(currentPosition);
		listFocus.getSelectionModel().select(currentPosition);
	}

	private static void eventRight() throws IOException, JSONException, ParseException{
		currentList++;
		currentPosition=0;
		if(currentList>6){
			currentList=6;
		}
		refreshingFocus(currentList);
		listFocus.scrollTo(currentPosition);
		listFocus.getSelectionModel().select(currentPosition);
	}

	private static void toggleFloat() throws IOException, JSONException, ParseException{
		if (currentScene == SCENE_MAIN && !controller.getOverdueList().isEmpty()){
			controller.retrieveAllData();
			gridPane.getChildren().removeAll(listFloat);
			
			if (toggleFloat == true ){
				toggleFloat = false;
				floating.setText("Overdue:");
				listOverdue = convertList(controller.getOverdueList());
				listOverdue.setFocusTraversable(false);
				listOverdue.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

		            @Override
		            public void handle(MouseEvent event) {
		                event.consume();
		            }
		        });
				listOverdue.setId("listOverdue");
				GridPane.setConstraints(listOverdue, 3,1);
				gridPane.getChildren().addAll(listOverdue);
			}
			else{
				//floating
				toggleFloat = true;
				floating.setText(LIST_FLOATING);
				listFloat = convertList(controller.getFloatList());
				listFloat.setFocusTraversable( false );
				listFloat.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

		            @Override
		            public void handle(MouseEvent event) {
		                event.consume();
		            }
		        });
				GridPane.setConstraints(listFloat, 3, 1);
				gridPane.getChildren().addAll(listFloat);
			}
		}
	}

}