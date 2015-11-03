package main.java.gui;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

//@@author A0126125R
public class GuiPreloader extends Preloader {

    private static final double WIDTH = 400;
    private static final double HEIGHT = 400;

    private Stage preloaderStage;
    private Scene scene;

    private Label progress;

    public GuiPreloader() {
        // Constructor is called before everything.
        System.out.println("MyPreloader constructor called, thread: " + Thread.currentThread().getName());
    }

    @Override
    public void init() throws Exception {
        System.out.println("MyPreloader#init (could be used to initialize preloader view), thread: " + Thread.currentThread().getName());

        // If preloader has complex UI it's initialization can be done in MyPreloader#init
        Platform.runLater(() -> {
            Label title = new Label("Firing up your TankTask, please wait...");
            title.setTextAlignment(TextAlignment.CENTER);
            progress = new Label("0%");
            Image tank = new Image(getClass().getResourceAsStream("tank.png"));
            ImageView image = new ImageView(tank);
            image.setFitWidth(250);
            image.setPreserveRatio(true);
            VBox root = new VBox(image,title, progress);
            root.setAlignment(Pos.CENTER);
    		Image bgImage = new Image(GUI.class.getResourceAsStream("Resources/background.png"));
    		BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true,true);
    		BackgroundImage backgroundImage = new BackgroundImage(bgImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
    		Background background = new Background(backgroundImage);
    		root.setBackground(background);

            scene = new Scene(root, WIDTH, HEIGHT);
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("MyPreloader#start (showing preloader stage), thread: " + Thread.currentThread().getName());

        this.preloaderStage = primaryStage;
        
        // Set preloader scene and show stage.
        preloaderStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        preloaderStage.show();
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        // Handle application notification in this point (see GUI#init).
        if (info instanceof ProgressNotification) {
            progress.setText(((ProgressNotification) info).getProgress() + "%");
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        // Handle state change notifications.
        StateChangeNotification.Type type = info.getType();
        switch (type) {
            case BEFORE_LOAD:
                // Called after MyPreloader#start is called.
                System.out.println("BEFORE_LOAD");
                break;
            case BEFORE_INIT:
                // Called before GUI#init is called.
                System.out.println("BEFORE_INIT");
                break;
            case BEFORE_START:
                // Called after GUI#init and before GUI#start is called.      
                preloaderStage.hide();
                break;
        }
    }
}

