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
	VBox imageBox;
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
		currentView=-1;
		Stage pop = new Stage();
		pane = new GridPane();
		comp = new VBox();
		imageBox = new VBox();
		HBox leftNavi = new HBox();
		HBox rightNavi = new HBox();
		imageBox.setAlignment(Pos.CENTER);
		leftNavi.setAlignment(Pos.TOP_LEFT);
		rightNavi.setAlignment(Pos.TOP_RIGHT);
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(15);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(40);
		ColumnConstraints column3 = new ColumnConstraints();
		column3.setPercentWidth(45);

		pane.getColumnConstraints().addAll(column1, column2, column3);
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(95);
		pane.getRowConstraints().addAll(row1);
		comp.setPadding(new Insets(15,15,15,15));
		comp.setSpacing(8);
		pop.setTitle("help");
		
		hotKey(comp, imageBox,pane);
		leftSide(leftNavi);
		rightSide(rightNavi);
		
		GridPane.setConstraints(imageBox,0,0);
		GridPane.setConstraints(comp,1,0);
		GridPane.setConstraints(leftNavi,0,1);
		GridPane.setConstraints(rightNavi,2,1);
		GridPane.setColumnSpan(comp, 2);
		GridPane.setColumnSpan(leftNavi, 2);
		
		pane.setGridLinesVisible(false);
		pane.getChildren().addAll(leftNavi,rightNavi);
		Scene stageScene = new Scene(pane, 700, 600);
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
		rightText.setText("command help");
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
					if (currentView<-1){
						currentView = -1;
					}
					changeContents();
				}

			}

		});
	}
	private void changeContents() {
		
		pane.getChildren().removeAll(showList,comp,imageBox);
		if (currentView==-1){
			pane.getChildren().addAll(comp,imageBox);
			leftText.setText("");
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
	private void hotKey(VBox comp, VBox imageBox,GridPane pane) {
		ArrayList<String>imageNames = hotkey.getResourceList();	
		for (int i=0;i<imageNames.size()-2;i++){
			ImageView imv = new ImageView();
			Image image = new Image(GUI.class.getResourceAsStream(imageNames.get(i)));
			imv.setImage(image);
			imv.setFitWidth(45);
			imv.setPreserveRatio(true);
			imageBox.getChildren().add(imv);
		}
		HBox ctrlz = new HBox();
		ctrlz.setAlignment(Pos.CENTER);
		ImageView imv = new ImageView();
		Image image = new Image(GUI.class.getResourceAsStream("Resources/ctrl.png"));
		imv.setImage(image);
		imv.setFitWidth(45);
		imv.setPreserveRatio(true);
		ImageView imv2 = new ImageView();
		Image image2 = new Image(GUI.class.getResourceAsStream("Resources/z.png"));
		imv2.setImage(image2);
		imv2.setFitWidth(45);
		imv2.setPreserveRatio(true);
		ctrlz.getChildren().addAll(imv,imv2);
		imageBox.getChildren().add(ctrlz);
		

		ArrayList<String> help = hotkey.retrieveHotkey();
		for  (int i =0 ;i<help.size();i++) {
			Label label = new Label();
			label.setText(help.get(i));
			label.setWrapText(true);
			comp.getChildren().add(label);
		}
		pane.getChildren().addAll(imageBox,comp);
	}
}

