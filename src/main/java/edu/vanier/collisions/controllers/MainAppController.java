package edu.vanier.collisions.controllers;

import edu.vanier.collisions.models.PhysicsEntity;
import edu.vanier.collisions.models.Terrain;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Arrays;

public class MainAppController {
    @FXML
    Pane collisionContainer;
    @FXML
    HBox carsParameters, generalParameters;
    @FXML
    Button playBtn, resetBtn;
    @FXML
    Spinner<Double> car1Velocity, car1Mass, car2Velocity, car2Mass;
    @FXML
    Slider playbackSlider, elasticitySlider;
    @FXML
    MenuButton terrainType;
    @FXML
    MenuItem exportBtn, importBtn, aboutBtn;

    public static boolean isPlaying = false;

    PhysicsEngine physicsEngine = PhysicsEngine.getInstance();

    @FXML
    private void initialize() {
        System.out.println("MainAppController.initialize()...");

        SpinnerValueFactory<Double> car1VelocitySlider = new SpinnerValueFactory.DoubleSpinnerValueFactory(0,1000,250);
        car1Velocity.setValueFactory(car1VelocitySlider);
        SpinnerValueFactory<Double> car2VelocitySlider = new SpinnerValueFactory.DoubleSpinnerValueFactory(0,1000,250);
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
                disableParameters(false);
                ex.printStackTrace();
            }
        });

        exportBtn.setOnAction(e -> {
            try {
                onExport();
            } catch (IOException ex) {
                disableParameters(false);
                ex.printStackTrace();
            }
        });

        aboutBtn.setOnAction(e -> {
            onHelp();
        });

        car1Velocity.valueProperty().addListener((obs, oldValue, newValue) -> {
            physicsEngine.getEntity1().setVelocityX(newValue);
        });

        car1Mass.valueProperty().addListener((obs, oldValue, newValue) -> {
            physicsEngine.getEntity1().setMass(newValue);
        });

        car2Velocity.valueProperty().addListener((obs, oldValue, newValue) -> {
            physicsEngine.getEntity2().setVelocityX(-newValue);
        });

        car2Mass.valueProperty().addListener((obs, oldValue, newValue) -> {
            physicsEngine.getEntity2().setMass(newValue);
        });

        playbackSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            onPlaybackSliderChange();
        });

        elasticitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            onElasticitySliderChange();
        });

        loopAmbientSound();

        Arrays.stream(Terrain.values()).forEach(terrain -> {
            MenuItem item = new MenuItem(terrain.toString());
            item.setOnAction(e -> {
                physicsEngine.setTerrain(terrain);
                terrainType.setText(terrain.toString());
                setBackground(terrain.texturePath());
            });
            terrainType.getItems().add(item);
        });

        initEnvironment();
    }

    private void initEnvironment() {
        PhysicsEntity car1 = new PhysicsEntity();
        PhysicsEntity car2 = new PhysicsEntity();

        car1.setFill(javafx.scene.paint.Color.valueOf("#0075ff"));
        car1.setHeight(59.0);
        car1.setLayoutY(340.0);
        car1.setStroke(javafx.scene.paint.Color.valueOf("BLACK"));
        car1.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

        car2.setFill(javafx.scene.paint.Color.valueOf("#d71e14"));
        car2.setHeight(59.0);
        car2.setLayoutY(340.0);
        car2.setStroke(javafx.scene.paint.Color.valueOf("BLACK"));
        car2.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

        car1.setWidth(177.0);
        car1.setCenterOffset(88.5);
        car1.setInitialPosX(100);
        car1.setVelocityX(car1Velocity.getValue());
        car1.setMass(car1Mass.getValue());
        car1.reset();

        car2.setWidth(177.0);
        car2.setCenterOffset(-88.5);
        car2.setInitialPosX(1000);
        car2.setVelocityX(-car2Velocity.getValue());
        car2.setMass(car2Mass.getValue());
        car2.reset();

        physicsEngine.setTerrain(Terrain.GRASS);
        terrainType.setText(Terrain.GRASS.toString());
        setBackground(Terrain.GRASS.texturePath());

        collisionContainer.getChildren().setAll(car1, car2);
        physicsEngine.setEntities(car1, car2);
        physicsEngine.setRestitutionCoefficient(elasticitySlider.getValue());
        physicsEngine.init();
    }

    private void setParametersFromEngine() {
        car1Velocity.getValueFactory().setValue(Math.abs(physicsEngine.getEntity1().getVelocityX()));
        car2Velocity.getValueFactory().setValue(Math.abs(physicsEngine.getEntity2().getVelocityX()));
        car1Mass.getValueFactory().setValue(physicsEngine.getEntity1().getMass());
        car2Mass.getValueFactory().setValue(physicsEngine.getEntity2().getMass());
        playbackSlider.setValue(physicsEngine.getPlaybackSpeed());
        elasticitySlider.setValue(physicsEngine.getRestitutionCoefficient());
        terrainType.setText(physicsEngine.getTerrain().toString());
    }

    private void setBackground(String path) {
        collisionContainer.setStyle("-fx-background-image: url('"+path+"'); -fx-background-size: stretch; -fx-background-repeat: no-repeat; -fx-background-position: bottom bottom;");
    }

    private void loopAmbientSound() {
        AudioClip audioClip = new AudioClip(getClass().getResource(ResourceManager.AMBIENT_SOUND).toString());
        audioClip.setCycleCount(AudioClip.INDEFINITE);
        audioClip.play();
    }

    private void onPlay() {
        physicsEngine.play();
        disableParameters(true);
        playBtn.setText("Pause");
        isPlaying = true;
    }

    public void onPause() {
        physicsEngine.pause();
        playBtn.setText("Play");
        isPlaying = false;
    }

    public void onReset() {
        initEnvironment();
        physicsEngine.reset();
        disableParameters(false);
        playBtn.setText("Play");
        isPlaying = false;
    }

    private void disableParameters(boolean choice) {
        carsParameters.setDisable(choice);
        generalParameters.setDisable(choice);
    }

    public void onImport() throws IOException, ClassNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Simulation File");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            disableParameters(true);
            FileInputStream fileInt = new FileInputStream(file);
            ObjectInputStream objInt = new ObjectInputStream(fileInt);
            PhysicsEngine newPhysicsEngine = (PhysicsEngine) objInt.readObject();
            PhysicsEngine.setInstance(newPhysicsEngine);
            physicsEngine = PhysicsEngine.getInstance();
            objInt.close();
            fileInt.close();
        }
        physicsEngine.getEntity1().initializeTranslateTransition();
        physicsEngine.getEntity2().initializeTranslateTransition();
        setParametersFromEngine();
        initEnvironment();
        disableParameters(false);
    }

    public void onExport() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Simulation");
        fileChooser.setInitialFileName("simulation.sim");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(physicsEngine);
            out.close();
            fileOut.close();
        }
    }

    public void onHelp() { //TODO : Add text inside window
        Stage stage = new Stage();
        Scene scene = new Scene(new VBox(), 300, 300);
        stage.setTitle("Help Window");
        stage.setScene(scene);
        stage.show();
    }

    private void onPlaybackSliderChange() {
        double sliderValue = playbackSlider.getValue();
        physicsEngine.setPlaybackSpeed(sliderValue);
    }

    private void onElasticitySliderChange() {
        double sliderValue = elasticitySlider.getValue();
        physicsEngine.setRestitutionCoefficient(sliderValue);
    }
}
