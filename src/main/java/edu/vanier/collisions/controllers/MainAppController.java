package edu.vanier.collisions.controllers;

import edu.vanier.collisions.models.PhysicsEntity;
import edu.vanier.collisions.models.Terrain;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainAppController {
    @FXML
    PhysicsEntity car1, car2;
    @FXML
    Button playBtn, stopBtn, resetBtn, importBtn, exportBtn;
    @FXML
    // TODO: replace with number spinners
    TextField car1VelocityTextField, car1MassTextField, car2VelocityTextField, car2MassTextField;
    @FXML
    Slider playbackSlider, physicSlider;
    @FXML
    MenuButton terrainType;
    @FXML
    Menu fileMenuBarbtn, editMenuBarbtn, helpMenuBarbtn;

    PhysicsEngine physicsEngine = PhysicsEngine.getInstance();

    @FXML
    private void initialize() {
        System.out.println("MainAppController.initialize()...");
        // TODO: remove below test code once controls are implemented
        car1.setInitialPosX(100);
        car1.setSpeedX(20);
        car1.setMass(1000);
        car2.setInitialPosX(200);
        car2.setSpeedX(-40);
        car2.setMass(800);
        physicsEngine.setEntities(car1, car2);
        physicsEngine.setTerrain(Terrain.GRASS);
        physicsEngine.setRestitutionCoefficient(0.8);
        physicsEngine.init();

        playBtn.setOnAction(e -> {
            onPlay();
        });

        stopBtn.setOnAction(e -> {
            onPause();
        });
    }

    // TODO: bind the ui controls to the following methods
    // physicsEngine.setPlaybackSpeed()
    // physicsEngine.setTerrain()
    // physicsEngine.setRestitutionCoefficient()
    // car1.setInitialPosX();
    // car1.setSpeedX();
    // car1.setMass();
    // car2.setInitialPosX();
    // car2.setSpeedX();
    // car2.setMass();

    // TODO: create dummy methods for the following
    // onImport() (importBtn.setOnAction())
    // onExport() (exportBtn.setOnAction())
    // onHelp() (helpMenuBarbtn.setOnAction())


    private void onPlay() {
        physicsEngine.play();
    }

    // TODO: remove pause button, instead make play button toggleable (play/pause)
    private void onPause() {
        physicsEngine.pause();
    }

    private void onReset() {
        physicsEngine.reset();
    }
}
