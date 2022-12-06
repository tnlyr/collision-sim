package edu.vanier.collisions.views;

import edu.vanier.collisions.controllers.MainAppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * TODO: is this necessary? bitches?
 * @link: https://openjfx.io/javadoc/18/
 * @see: Build Scripts/build.gradle
 * @author Bozos
 */
public class MainApp extends Application {
    MainAppController controller = new MainAppController();

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainApp_layout.fxml"));
        loader.setController(controller);
        Pane root = loader.load();
        setEventHandler(root);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Collision Simulator");
        stage.sizeToScene();
        stage.getIcons().getClass().getResource("");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setEventHandler(Node root){
        root.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.P && event.isControlDown()){
                if (controller.isPlaying){
                    controller.onPause();
                }
                else{
                    controller.onPlay();
                }
                event.consume();
            }
            if (event.getCode() == KeyCode.R && event.isControlDown()){
                controller.onReset();
                event.consume();
            }
            if (event.getCode() == KeyCode.ESCAPE){
                System.exit(0);
            }
            if (event.getCode() == KeyCode.F1){
                controller.onHelp();
                event.consume();
            }
            if (event.getCode() == KeyCode.O && event.isControlDown()){
                try {
                    controller.onImport();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                event.consume();
            }
            if (event.getCode() == KeyCode.S && event.isControlDown()){
                try {
                    controller.onExport();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                event.consume();
            }
        });
    }
}