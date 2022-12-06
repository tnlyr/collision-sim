package edu.vanier.collisions.controllers;

import edu.vanier.collisions.models.PhysicsEntity;
import edu.vanier.collisions.models.Terrain;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import edu.vanier.collisions.controllers.ResourceManager;

import java.io.*;
import java.time.format.ResolverStyle;
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
    @FXML
    Text statDuration, statCar1Vel, statCar2Vel, statEnergy;

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

    /**
     *
     */
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

    private double calculateEnergy(){
        double energy1 = physicsEngine.getEntity1().getMass() * Math.pow(physicsEngine.getEntity1().getVelocityX(), 2) / 2;
        double energy2 = physicsEngine.getEntity2().getMass() * Math.pow(physicsEngine.getEntity2().getVelocityX(), 2) / 2;
        double totalEnergy = (energy1 + energy2)/1000 ;
        return totalEnergy;
    }

    public void onPlay() {
        physicsEngine.play();
        disableParameters(true);
        playBtn.setText("Pause");
        isPlaying = true;
        statCar1Vel.setText(car1Velocity.getValue().toString() + " m/s");
        statCar2Vel.setText(car2Velocity.getValue().toString() + " m/s");
        statEnergy.setText(calculateEnergy() + " KJ");
        statDuration.setText(physicsEngine.getDuration() + " s");
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
        statCar1Vel.setText("0 m/s");
        statCar2Vel.setText("0 m/s");
        statEnergy.setText("0 J");
        statDuration.setText("0 s");
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
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setText("""
         • Simulation controls
            ◦ Play/pause button
                The play button allows you to start the simulation; once it is clicked and the car is moving, the button becomes a stop button that will enable you to stop the collision at any point while preserving the car's position.

            ◦ Reset button
                The reset button allows you to reset the simulation at its equilibrium stage.

        • Menu bar
            ◦ File Menu
                Import Simulation (Ctrl O) allows you to import any saved simulations from your computer.
                Export Simulation (Ctrl+S) allows you to save your current simulation on your computer.

        • Car Parameters
            ◦ Mass Spinner
                Allows you to select, for each car, the mass in kilograms up to 100 kilograms by either using the up and down arrows or entering the number manually.

            ◦ Velocity Spinner
                Allows you to select, for each car, the velocity in meters per second up to 1000 m/s by either using the up and down arrows or entering the number manually.

        • General Parameters
            ◦ Playback Speed
                Using the slider allows you to decide on the playback speed, either by choosing a slower or faster speed of interval between 0.25x to 1.75x, increasing by 0.25x between each tic.


            ◦ Car Elasticity
                Using the sliders allows you to decide how much you want the simulation to be elastic to inelastic, where 0 is a totally inelastic collision, and 1 is a total elastic collision. Consequently, everything in between is a mix of both types.

            ◦ Terrain Type
                Terrain type will allow you to change the type of terrain the cars are driving on, which will impact the kinetic friction coefficient. In addition, the backgrounds will adapt depending on the terrain type.

        • Status bar
            The status bar demonstrates the duration of the simulation, the active velocity of both cars and the system's total energy.
        """);
        Scene scene = new Scene(textArea, 300, 300);
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
