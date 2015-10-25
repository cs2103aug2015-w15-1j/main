package main.java.gui;

import java.io.FileNotFoundException;
import java.util.ArrayList;

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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.backend.Util.CommandHelp;
import main.java.backend.Util.HotkeyHelp;

public class HelpView {
	CommandHelp command;
	HotkeyHelp hotkey;
	Text leftText;
	Text rightText;
	GridPane pane;
	VBox comp;
	VBox comp2;
	ListView<String> showList;
	int currentView;
	ArrayList<ArrayList<String>> fullList;
	ArrayList<String> textList;
	
	public HelpView(){
		hotkey = new HotkeyHelp();
		leftText = new Text();
		rightText = new Text();
		fullList = new ArrayList<ArrayList<String>>();
		try {
			command = new CommandHelp();
		} catch (FileNotFoundException e) {
			System.out.print(e.getMessage());
		}
		fullList = command.getSplitList();
		textList = command.getSplitNaming();
		assert fullList!=null && !fullList.isEmpty();
		assert textList!=null && !textList.isEmpty();
	}
	
	public void helpPopUp() {
		currentView=-2;
		Stage pop = new Stage();
		pane = new GridPane();
		comp = new VBox();
		comp2 = new VBox();
		HBox leftNavi = new HBox();
		HBox rightNavi = new HBox();
		leftNavi.setAlignment(Pos.TOP_LEFT);
		rightNavi.setAlignment(Pos.TOP_RIGHT);
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(70);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(30);

		pane.getColumnConstraints().addAll(column1, column2);
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(95);
		pane.getRowConstraints().addAll(row1);
		comp.setPadding(new Insets(20,20,20,20));
		comp2.setPadding(new Insets(20,20,20,20));
		pop.setTitle("help");
		
		hotKey(comp,comp2, pane);
		leftSide(leftNavi);
		rightSide(rightNavi);
		
		GridPane.setConstraints(comp,0,0);
		GridPane.setConstraints(comp2,0,0);
		GridPane.setConstraints(leftNavi,0,1);
		GridPane.setConstraints(rightNavi,1,1);
		GridPane.setColumnSpan(comp, 2);
		GridPane.setColumnSpan(comp2, 2);
		GridPane.setColumnSpan(leftNavi, 2);
		
		pane.setGridLinesVisible(false); //checking
		pane.getChildren().addAll(leftNavi,rightNavi);
		Scene stageScene = new Scene(pane, 700, 650);
		Image icon = new Image(getClass().getResourceAsStream("tank.png")); 
		pop.getIcons().add(icon);
		stageScene.getStylesheets().add(getClass().getResource("HelpStyle.css").toExternalForm());
		pop.setScene(stageScene);
		pop.show();
		
		determineEvents(pop, stageScene);
	}
	private void leftSide(HBox leftNavi) {
		Image image = new Image(GUI.class.getResourceAsStream("Resources/left.png"));
		ImageView imageview = new ImageView();
		imageview.setImage(image);
		imageview.setFitWidth(30);
		imageview.setPreserveRatio(true);
		leftText.setText("");
		leftNavi.getChildren().addAll(imageview,leftText);
	}
	private void rightSide(HBox rightNavi) {
		Image image2 = new Image(GUI.class.getResourceAsStream("Resources/right.png"));
		ImageView imageview2 = new ImageView();
		imageview2.setImage(image2);
		imageview2.setFitWidth(30);
		imageview2.setPreserveRatio(true);
		rightText.setText("2-keys shortcuts");
		rightNavi.getChildren().addAll(rightText, imageview2);
	}
	private void determineEvents(Stage pop, Scene stageScene) {
		stageScene.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.F1)){
					pop.close();
				}
				if (event.getCode().equals(KeyCode.RIGHT)){
					currentView++;
					if (currentView>=fullList.size()){
						currentView = fullList.size()-1;
					}
					changeContents();
				}
				if (event.getCode().equals(KeyCode.LEFT)){
					currentView--;
					if (currentView<-2){
						currentView = -2;
					}
					changeContents();
				}

			}

		});
	}
	private void changeContents() {
		
		pane.getChildren().removeAll(showList,comp,comp2);
		if (currentView==-2){
			pane.getChildren().addAll(comp);
			leftText.setText("");
			rightText.setText("2-keys shortcuts");
		}else if(currentView==-1){
			pane.getChildren().addAll(comp2);
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
	private void hotKey(VBox comp,VBox comp2, GridPane pane) {
		ArrayList<String>imageNames = hotkey.getResourceList();	
		ArrayList<String> help = hotkey.retrieveHotkey();
		int j=0;
		for  (int i =0 ;i<help.size();i++) {
			HBox box = new HBox();
			if (i<help.size()-4){ //only contains 1 key shortcuts
				ImageView imv = new ImageView();
				Image image = new Image(GUI.class.getResourceAsStream(imageNames.get(i)));
				imv.setImage(image);
				imv.setFitWidth(47);
				imv.setPreserveRatio(true);
				Label label = new Label();
				label.setText(help.get(i));
				label.setWrapText(true);
				box.getChildren().addAll(imv,label);
				comp.getChildren().add(box);
				j=i;
			} else { //only contains 2 key shortcuts
				HBox box2 = new HBox();
				box2.setAlignment(Pos.CENTER);
				ImageView newimv = new ImageView();
				Image newimage = new Image(GUI.class.getResourceAsStream(imageNames.get(++j)));
				newimv.setImage(newimage);
				newimv.setFitWidth(45);
				newimv.setPreserveRatio(true);
				ImageView newimv2 = new ImageView();
				Image newimage2 = new Image(GUI.class.getResourceAsStream(imageNames.get(++j)));
				newimv2.setImage(newimage2);
				newimv2.setFitWidth(45);
				newimv2.setPreserveRatio(true);
				Label label = new Label();
				label.setText(help.get(i));
				label.setWrapText(true);
				box2.getChildren().addAll(newimv,newimv2);
				box.getChildren().addAll(box2,label);
				comp2.getChildren().add(box);
			}
			
		}
		
		pane.getChildren().addAll(comp);
	}
}

