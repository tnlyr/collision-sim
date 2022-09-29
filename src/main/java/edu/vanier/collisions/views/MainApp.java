package edu.vanier.collisions.views;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO: fxml
//        Parent root = FXMLLoader.load(getClass().getResource("MainView.fxml"));

        VBox root = new VBox();
        // stage is the top level container
        primaryStage.setTitle("Collisions");
        primaryStage.show();

        // scene is the container for the content
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Collisions");
        primaryStage.show();
    }
}
