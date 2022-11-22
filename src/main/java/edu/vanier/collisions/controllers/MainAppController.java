package edu.vanier.collisions.controllers;

import edu.vanier.collisions.models.PhysicsEntity;
import edu.vanier.collisions.models.Terrain;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class MainAppController {
    @FXML
    Pane collisionContainer;

    PhysicsEntity car1, car2;
    @FXML
    Button playBtn, resetBtn, importBtn, exportBtn;
    @FXML
    Spinner<Integer> car1Velocity, car1Mass, car1Position, car2Velocity, car2Mass, car2Position;
    @FXML
    Slider playbackSlider, physicSlider;
    @FXML
    MenuButton terrainType;
    MenuItem terrain1, terrain2, terrain3;
    @FXML
    Menu fileMenuBarbtn, editMenuBarbtn, helpMenuBarbtn;

    PhysicsEngine physicsEngine = PhysicsEngine.getInstance();

    @FXML
    private void initialize() {
        System.out.println("MainAppController.initialize()...");

        car1 = new PhysicsEntity();
        car1.setArcHeight(5.0);
        car1.setArcWidth(5.0);
        car1.setFill(javafx.scene.paint.Color.valueOf("#30ab2c"));
        car1.setHeight(59.0);
        car1.setLayoutX(148.0);
        car1.setLayoutY(284.0);
        car1.setStroke(javafx.scene.paint.Color.valueOf("BLACK"));
        car1.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        car1.setWidth(177.0);
        car1.setInitialPosX(100);
        car1.setSpeedX(20);
        car1.setMass(1000);

        car2 = new PhysicsEntity();
        car2.setArcHeight(5.0);
        car2.setArcWidth(5.0);
        car2.setFill(javafx.scene.paint.Color.valueOf("#d71e14"));
        car2.setHeight(59.0);
        car2.setLayoutX(882.0);
        car2.setLayoutY(284.0);
        car2.setStroke(javafx.scene.paint.Color.valueOf("BLACK"));
        car2.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        car2.setWidth(177.0);
        car2.setInitialPosX(200);
        car2.setSpeedX(-40);
        car2.setMass(800);

        collisionContainer.getChildren().addAll(car1, car2);
        // TODO: remove below test code once controls are implemented
        physicsEngine.setEntities(car1, car2);
        physicsEngine.setTerrain(Terrain.GRASS);
        physicsEngine.setRestitutionCoefficient(0.8);
        physicsEngine.init();

        playBtn.setOnAction(e -> {
            onPlay();
        });

        resetBtn.setOnAction(e -> {
            onReset();
        });
    }

    // TODO: implement action handlers
    // FIXME : physicsEngine.setPlaybackSpeed()
    // FIXME : physicsEngine.setTerrain()
    // FIXME : physicsEngine.setRestitutionCoefficient()

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

    private void onImport() { //TODO: implement import
        importBtn.setOnAction(e -> {
            throw new UnsupportedOperationException("TODO");
        });
    }

    private void onExport() { //TODO: implement export
        exportBtn.setOnAction(e -> {
            throw new UnsupportedOperationException("TODO");
        });
    }

    private void onHelp() { //TODO: implement help
        helpMenuBarbtn.setOnAction(e -> {
            throw new UnsupportedOperationException("TODO");
        });
    }

    private void onPlaybackSliderChange() {
        int sliderValue = (int) playbackSlider.getValue();
        physicsEngine.setPlaybackSpeed(sliderValue);
    }

    private void onPhysicSliderChange() {
        int sliderValue = (int) physicSlider.getValue();
        physicsEngine.setRestitutionCoefficient(sliderValue);
    }

    private void onTerrainChange() {
        terrainType.setOnAction(e -> {
            throw new UnsupportedOperationException("TODO");
        });
    }

    private void setCar1Velocity() {
        int velocity = car1Velocity.getValue();
        car1.setSpeedX(velocity);
    }

    private void setCar1Mass() {
        int mass = car1Mass.getValue();
        car1.setMass(mass);
    }

    private void setCar1Position() {
        int position = car1Position.getValue();
        car1.setInitialPosX(position);
    }

    private void setCar2Velocity() {
        int velocity = car2Velocity.getValue();
        car2.setSpeedX(velocity);
    }

    private void setCar2Mass() {
        int mass = car2Mass.getValue();
        car2.setMass(mass);
    }

    private void setCar2Position() {
        int position = car2Position.getValue();
        car2.setInitialPosX(position);
    }


}
