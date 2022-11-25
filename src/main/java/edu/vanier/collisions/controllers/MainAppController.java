package edu.vanier.collisions.controllers;

import edu.vanier.collisions.models.PhysicsEntity;
import edu.vanier.collisions.models.Terrain;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import java.io.*;
import java.util.Arrays;

public class MainAppController {
    @FXML
    Pane collisionContainer;
    @FXML
    HBox carsParameters, generalParameters;

    PhysicsEntity car1, car2;
    @FXML
    Button playBtn, resetBtn;
    @FXML
    Spinner<Double> car1Velocity, car1Mass, car2Velocity, car2Mass;
    @FXML
    Slider playbackSlider, physicSlider;
    @FXML
    MenuButton terrainType;
    MenuItem
    @FXML
    Menu fileMenuBarbtn, editMenuBarbtn, helpMenuBarbtn;
    @FXML
    MenuItem exportBtn, importBtn;

    boolean isPlaying = false;

    PhysicsEngine physicsEngine = PhysicsEngine.getInstance();

    @FXML
    private void initialize() {
        System.out.println("MainAppController.initialize()...");

        car1 = new PhysicsEntity();
        car2 = new PhysicsEntity();

        SpinnerValueFactory<Double> car1VelocitySlider = new SpinnerValueFactory.DoubleSpinnerValueFactory(50,500,250);
        car1Velocity.setValueFactory(car1VelocitySlider);
        SpinnerValueFactory<Double> car2VelocitySlider = new SpinnerValueFactory.DoubleSpinnerValueFactory(50,500,250);
        car2Velocity.setValueFactory(car2VelocitySlider);

        SpinnerValueFactory<Double> mass1Slider = new SpinnerValueFactory.DoubleSpinnerValueFactory(1,100,25);
        car1Mass.setValueFactory(mass1Slider);
        SpinnerValueFactory<Double> mass2Slider = new SpinnerValueFactory.DoubleSpinnerValueFactory(1,100,25);
        car2Mass.setValueFactory(mass2Slider);

        playBtn.setOnAction(e -> {
            if (!isPlaying){
                onPlay();
            }
            else{
                onPause();
            }
        });

        resetBtn.setOnAction(e -> {
            onReset();
        });

        importBtn.setOnAction(e -> {
            try {
                onImport();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        exportBtn.setOnAction(e -> {
            try {
                onExport();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        car1Velocity.valueProperty().addListener((obs, oldValue, newValue) -> {
            car1.setVelocityX(newValue);
        });

        car1Mass.valueProperty().addListener((obs, oldValue, newValue) -> {
            car1.setMass(newValue);
        });

        car2Velocity.valueProperty().addListener((obs, oldValue, newValue) -> {
            car2.setVelocityX(-newValue);
        });

        car2Mass.valueProperty().addListener((obs, oldValue, newValue) -> {
            car2.setMass(newValue);
        });

        playbackSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            onPlaybackSliderChange();
        });

        physicSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            onPhysicSliderChange();
        });

        collisionContainer.widthProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("New width: "+newValue);
            //- resize or reposition our cars.
        });

        Arrays.stream(Terrain.values()).forEach(terrain -> {
            MenuItem item = new MenuItem(terrain.toString());
            item.setOnAction(e -> {
                physicsEngine.setTerrain(terrain);
                terrainType.setText(terrain.toString());
            });
            terrainType.getItems().add(item);
        });
        defaultParameters();
    }

    private void defaultParameters() {
        car1 = new PhysicsEntity();
        car2 = new PhysicsEntity();

        car1.setFill(javafx.scene.paint.Color.valueOf("#30ab2c"));
        car1.setHeight(59.0);
        car1.setLayoutY(284.0);
        car1.setStroke(javafx.scene.paint.Color.valueOf("BLACK"));
        car1.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        car1.setWidth(177.0);
        car1.setCenterOffset(177/2);
        car1.setInitialPosX(100);
        car1.setVelocityX(car2Velocity.getValue());
        car1.setMass(car1Mass.getValue());
        car1.reset();

        car2.setFill(javafx.scene.paint.Color.valueOf("#d71e14"));
        car2.setHeight(59.0);
        car2.setLayoutY(284.0);
        car2.setStroke(javafx.scene.paint.Color.valueOf("BLACK"));
        car2.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        car2.setWidth(177.0);
        car2.setCenterOffset(-177/2);
        car2.setInitialPosX(800);
        car2.setVelocityX(-car2Velocity.getValue());
        car2.setMass(car2Mass.getValue());
        car2.reset();

        collisionContainer.getChildren().setAll(car1, car2);
        physicsEngine.setEntities(car1, car2);
        physicsEngine.setTerrain(Terrain.GRASS);
        physicsEngine.setRestitutionCoefficient(1);
        physicsEngine.init();
    }

    private void onPlay() {
        physicsEngine.play();
        toggleControls(true);
        playBtn.setText("Pause");
        isPlaying = true;
    }

    private void onPause() {
        physicsEngine.pause();
        playBtn.setText("Play");
        isPlaying = false;
    }

    private void onReset() {
        defaultParameters();
        physicsEngine.reset();
        toggleControls(false);
        playBtn.setText("Play");
        isPlaying = false;
    }

    private void toggleControls(boolean choice) {
        carsParameters.setDisable(choice);
        generalParameters.setDisable(choice);
    }

    private void onImport() throws IOException, ClassNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        // TODO: onCancel
        String filePath = fileChooser.showOpenDialog(null).getAbsolutePath();
        FileInputStream fileInt = new FileInputStream(filePath);
        ObjectInputStream objInt = new ObjectInputStream(fileInt);
        PhysicsEngine newPhysicsEngine = (PhysicsEngine) objInt.readObject();
        physicsEngine.setInstance(newPhysicsEngine);
        objInt.close();
        fileInt.close();
    }

    private void onExport() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Simulation");
        fileChooser.setInitialFileName("simulation.sim");
        // TODO: onCancel
        String filePath = fileChooser.showSaveDialog(null).getAbsolutePath();
        FileOutputStream fileOut = new FileOutputStream(filePath);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(physicsEngine);
        out.close();
        fileOut.close();
    }

    private void onHelp() { //TODO: implement help
        helpMenuBarbtn.setOnAction(e -> {
            throw new UnsupportedOperationException("TODO");
        });
    }

    private void onPlaybackSliderChange() {
        double sliderValue = playbackSlider.getValue();
        physicsEngine.setPlaybackSpeed(sliderValue);
        //System.out.println(sliderValue);
    }

    private void onPhysicSliderChange() {
        double sliderValue = physicSlider.getValue();
        physicsEngine.setRestitutionCoefficient(sliderValue);
        //System.out.println(sliderValue);
    }

    private void setCar1Velocity() {
        double velocity = car1Velocity.getValue();
        car1.setVelocityX(velocity);
    }

    private void setCar1Mass() {
        double mass = car1Mass.getValue();
        car1.setMass(mass);
    }

    private void setCar2Velocity() {
        double velocity = car2Velocity.getValue();
        car2.setVelocityX(velocity);
    }

    private void setCar2Mass() {
        double mass = car2Mass.getValue();
        car2.setMass(mass);
    }
}
