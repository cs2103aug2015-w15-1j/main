 package main.java.gui;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.backend.Util.CommandHelp;
import main.java.backend.Util.HotkeyHelp;

//@@author A0126125R
public class HelpView {
	private final int numDoubleKeyInstruction = 4;
	Stage stage;
	Scene scene;
	CommandHelp command;
	HotkeyHelp hotKey;
	Text leftText;
	Text rightText;
	GridPane pane;
	VBox oneKey; //for one-key shortcuts
	VBox twoKey; //for two-key shortcuts
	VBox content; //content page
	ListView<String> showList;
	int currentView;
	ArrayList<ArrayList<String>> fullList;
	ArrayList<String> textList;
	
	public HelpView(){
		hotKey = new HotkeyHelp();
		leftText = new Text();
		rightText = new Text();
		fullList = new ArrayList<ArrayList<String>>();
		command = new CommandHelp();
		Platform.runLater(new Runnable(){
			
			@Override
			public void run() {
				setUpAllContents();
			}
		});
	}
	
	public void helpPopUp() {
		
		stage.show();
		determineEvents(stage, scene);
	}

	private void setUpAllContents() {
		stage = new Stage();
		currentView=-3;
		pane = new GridPane();
		oneKey = new VBox();
		twoKey = new VBox();
		content = new VBox();
		fullList = command.getSplitList();
		textList = command.getSplitNaming();
		assert fullList!=null && !fullList.isEmpty();
		assert textList!=null && !textList.isEmpty();
		HBox leftNavi = new HBox();
		HBox rightNavi = new HBox();
		leftNavi.setAlignment(Pos.TOP_LEFT);
		rightNavi.setAlignment(Pos.TOP_RIGHT);
		
		stage.setTitle("help");
		setUpBackground();
		setUpContentPage();
		setHotKeyContents(oneKey,twoKey, pane);
		setUpLeftText(leftNavi);
		setUpRightText(rightNavi);
		
		setUpGridConstraints(leftNavi, rightNavi);
		
		pane.setGridLinesVisible(false); //checking
		pane.getChildren().addAll(leftNavi,rightNavi);
		Scene stageScene = new Scene(pane, 800, 600);
		scene = stageScene;
		Image icon = new Image(getClass().getResourceAsStream("tank.png")); 
		stage.getIcons().add(icon);
		scene.getStylesheets().add(getClass().getResource("HelpStyle.css").toExternalForm());
		stage.setScene(scene);
	}

	private void setUpBackground() {
		Image image = new Image(Gui.class.getResourceAsStream("Resources/background.png"));
		BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true,true);
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
		Background background = new Background(backgroundImage);
		pane.setBackground(background);
	}

	private void setUpGridConstraints(HBox leftNavi, HBox rightNavi) {
		GridPane.setConstraints(content,0,0);
		GridPane.setConstraints(oneKey,0,0);
		GridPane.setConstraints(twoKey,0,0);
		GridPane.setConstraints(leftNavi,0,1);
		GridPane.setConstraints(rightNavi,1,1);
		GridPane.setColumnSpan(oneKey, 2);
		GridPane.setColumnSpan(twoKey, 2);
		GridPane.setColumnSpan(leftNavi, 2);
		
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(30);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(70);
		
		pane.getColumnConstraints().addAll(column1, column2);
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(95);
		pane.getRowConstraints().addAll(row1);
		content.setPadding(new Insets(20,20,20,20));
		oneKey.setPadding(new Insets(20,20,20,20));
		twoKey.setPadding(new Insets(20,20,20,20));
	}
	
	private void setUpContentPage(){
		Text contentText = new Text();
		contentText.setText("Help contents: \n");
		contentText.getStyleClass().add("customHeading");
		content.getChildren().add(contentText);
		for (int i =1 ; i<textList.size()-1;i++){
			Text newText = new Text();
			newText.getStyleClass().add("custom");
			Text titleText = new Text();
			titleText.getStyleClass().add("customTitle");
			if (i == 1){
			titleText.setText("Adding Tasks: ");
			content.getChildren().add(titleText);
			}else if (i == 2){
			titleText.setText("\nManaging your tasks: ");
			content.getChildren().add(titleText);
			}else if (i==6){
			titleText.setText("\nEditing your tasks: ");
			content.getChildren().add(titleText);
			}
			newText.setText("	"+i+". "+textList.get(i));
			content.getChildren().add(newText);
		}
		pane.getChildren().add(content);
	}
	
	private void setUpLeftText(HBox leftNavi) {
		Image image = new Image(Gui.class.getResourceAsStream("Resources/left.png"));
		ImageView imageview = new ImageView();
		imageview.setImage(image);
		imageview.setFitWidth(30);
		imageview.setPreserveRatio(true);
		leftText.setText("");
		leftNavi.getChildren().addAll(imageview,leftText);
	}
	private void setUpRightText(HBox rightNavi) {
		Image image2 = new Image(Gui.class.getResourceAsStream("Resources/right.png"));
		ImageView imageview2 = new ImageView();
		imageview2.setImage(image2);
		imageview2.setFitWidth(30);
		imageview2.setPreserveRatio(true);
		rightText.setText("2-keys shortcuts");
		rightNavi.getChildren().addAll(rightText, imageview2);
	}
	private void setHotKeyContents(VBox oneKey,VBox twoKey, GridPane pane) {
		ArrayList<String>imageNames = hotKey.getResourceList();	
		ArrayList<String> help = hotKey.retrieveHotkey();
		int j=0;
		for  (int i =0 ;i<help.size();i++) {
			HBox box = new HBox();
			if (i<help.size()-numDoubleKeyInstruction){ //only contains 1 key shortcuts
				ImageView imv = new ImageView();
				Image image = new Image(Gui.class.getResourceAsStream(imageNames.get(i)));
				imv.setImage(image);
				imv.setFitWidth(47);
				imv.setPreserveRatio(true);
				Label label = new Label();
				label.setText(help.get(i));
				label.setWrapText(true);
				box.getChildren().addAll(imv,label);
				oneKey.getChildren().add(box);
				j=i;
			} else { //only contains 2 key shortcuts
				HBox box2 = new HBox();
				box2.setAlignment(Pos.CENTER);
				ImageView newimv = new ImageView();
				Image newimage = new Image(HelpView.class.getResourceAsStream(imageNames.get(++j)));
				newimv.setImage(newimage);
				newimv.setFitWidth(45);
				newimv.setPreserveRatio(true);
				ImageView newimv2 = new ImageView();
				Image newimage2 = new Image(HelpView.class.getResourceAsStream(imageNames.get(++j)));
				newimv2.setImage(newimage2);
				newimv2.setFitWidth(45);
				newimv2.setPreserveRatio(true);
				Label label = new Label();
				label.setText(help.get(i));
				label.setWrapText(true);
				box2.getChildren().addAll(newimv,newimv2);
				box.getChildren().addAll(box2,label);
				twoKey.getChildren().add(box);
			}
			
		}
	}

	private void determineEvents(Stage stage, Scene stageScene) {
		stageScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
	
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.F1)){
					close();
				} else if (event.getCode().equals(KeyCode.RIGHT)){
					currentView++;
					if (currentView>=fullList.size()){
						currentView = fullList.size()-1;
					}
					changeContents();
				} else if (event.getCode().equals(KeyCode.LEFT)){
					currentView--;
					if (currentView<-3){
						currentView = -3;
					}
					changeContents();
				} else if (event.getCode().equals(KeyCode.DIGIT0)){
					currentView = -3;
					changeContents();
				} else if (event.getCode().equals(KeyCode.DIGIT1)){
					currentView = 0;
					changeContents();
				} else if (event.getCode().equals(KeyCode.DIGIT2)){
					currentView = 1;
					changeContents();
				} else if (event.getCode().equals(KeyCode.DIGIT3)){
					currentView = 2;
					changeContents();
				} else if (event.getCode().equals(KeyCode.DIGIT4)){
					currentView = 3;
					changeContents();
				} else if (event.getCode().equals(KeyCode.DIGIT5)){
					currentView = 4;
					changeContents();
				} else if (event.getCode().equals(KeyCode.DIGIT6)){
					currentView = 5;
					changeContents();
				} else if (event.getCode().equals(KeyCode.DIGIT7)){
					currentView = 6;
					changeContents();
				} else if (event.getCode().equals(KeyCode.DIGIT8)){
					currentView = 7;
					changeContents();
				}
	
			}
	
		});
	}

	private void changeContents() {
		
		pane.getChildren().removeAll(content,showList,oneKey,twoKey);
		if (currentView==-3){
			pane.getChildren().add(content);
			leftText.setText("");
			rightText.setText("1-key shortcuts");
			
		} else if (currentView==-2){
			pane.getChildren().addAll(oneKey);
			leftText.setText("Content Page");
			rightText.setText("2-keys shortcuts");
		}else if(currentView==-1){
			pane.getChildren().addAll(twoKey);
			leftText.setText("1-key shortcuts");
			rightText.setText("command help");
		}else{
			ObservableList<String> subList = FXCollections.observableArrayList(fullList.get(currentView));
			showList = new ListView<String>(subList);
			showList.setFocusTraversable( false );
			showList.setPadding(new Insets(15,15,15,15));
			GridPane.setConstraints(showList, 0, 0);
			GridPane.setColumnSpan(showList, 3);
			pane.getChildren().add(showList);
			leftText.setText(textList.get(currentView));
			rightText.setText(textList.get(currentView+2));
		}
	}

	public void close() {
		stage.close();
	}

	public void toFront() {
		stage.toFront();
	}
}