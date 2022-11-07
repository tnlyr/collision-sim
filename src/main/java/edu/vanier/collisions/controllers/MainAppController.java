package edu.vanier.collisions.controllers;

import edu.vanier.collisions.models.Ball;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainAppController {
    @FXML
    Rectangle cart1, cart2;
    @FXML
    Button playBtn;
    PhysicsEngine physicsEngine = PhysicsEngine.getInstance();

    @FXML
    private void initialize() {
        System.out.println("MainAppController.initialize()...");
        Ball ball1 = new Ball(Color.RED, 20, 100, 2, 20);
        Ball ball2 = new Ball(Color.BLUE, 20, 200, 40, 20);
        physicsEngine.addEntity(ball1);
        physicsEngine.addEntity(ball2);
        physicsEngine.setCoefficientOfFriction(0.1);
        physicsEngine.setCoefficientOfRestitution(0.8);

        // TODO: simulation loop (should it be managed by the physics engine?)
        playBtn.setOnAction(e -> {
            physicsEngine.next();
            cart1.setX(ball1.getPosX());
            cart2.setX(ball2.getPosX());
        });
    }
}
