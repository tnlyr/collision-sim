package edu.vanier.collisions.views;

import edu.vanier.collisions.controllers.MainAppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * 
 * @link: https://openjfx.io/javadoc/18/
 * @see: Build Scripts/build.gradle
 * @author Bozos
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UIDesignFinal1.fxml"));
        loader.setController(new MainAppController());
        Pane root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Collisions Simulation");
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
