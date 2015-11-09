package main.java.gui;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//@@author A0126125R
public class GuiPreloader extends Preloader {

    private static final double WIDTH = 400;
    private static final double HEIGHT = 300;

    private Stage preloaderStage;
    private Scene scene;

    private Text progress;

    public GuiPreloader() {
    }
    
    //@@author A0126125R
    @Override
    public void init() throws Exception {
        Platform.runLater(() -> {
            Text title = new Text("Firing up your TankTask, please wait...");
            progress = new Text("0%");
            Image tank = new Image(getClass().getResourceAsStream("tank.png"));
            ImageView image = new ImageView(tank);
            image.setFitWidth(250);
            image.setPreserveRatio(true);
            VBox root = new VBox(image,title, progress);
            root.setAlignment(Pos.CENTER);
    		Image bgImage = new Image(Gui.class.getResourceAsStream("Resources/background.png"));
    		BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true,true);
    		BackgroundImage backgroundImage = new BackgroundImage(bgImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
    		Background background = new Background(backgroundImage);
    		root.setBackground(background);

            scene = new Scene(root, WIDTH, HEIGHT);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            root.setId("border");
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.preloaderStage = primaryStage;
        
        // Set preloader scene and show stage.
        preloaderStage.setScene(scene);
        preloaderStage.initStyle(StageStyle.UNDECORATED);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        preloaderStage.show();
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification percentage) {
        if (percentage instanceof ProgressNotification) {
            progress.setText(((ProgressNotification) percentage).getProgress() + "%");
        }
    }

    //to check current status
    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        // Handle state change notifications.
        StateChangeNotification.Type type = info.getType();
        switch (type) {
            case BEFORE_LOAD:
                break;
            case BEFORE_INIT:
                break;
            case BEFORE_START:
                preloaderStage.hide();
                break;
        }
    }
}

