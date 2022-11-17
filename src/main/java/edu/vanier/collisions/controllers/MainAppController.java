package edu.vanier.collisions.controllers;

import edu.vanier.collisions.models.Ball;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainAppController {
    @FXML
    Rectangle car1, car2;
    @FXML
    Button stopBtn;
    PhysicsEngine physicsEngine = PhysicsEngine.getInstance();

    @FXML
    private void initialize() {
        System.out.println("MainAppController.initialize()...");
        Ball ball1 = new Ball(Color.PURPLE, 20, 100, -2, 20);
        Ball ball2 = new Ball(Color.BLUE, 20, 200, 40, 20);
        car1.setFill(ball1.getColor());
        car2.setFill(ball2.getColor());
        physicsEngine.addEntity(ball1);
        physicsEngine.addEntity(ball2);
        physicsEngine.setCoefficientOfFriction(0.1);
        physicsEngine.setCoefficientOfRestitution(0.8);

        // TODO: simulation loop (should it be managed by the physics engine?)
        stopBtn.setOnAction(e -> {
            physicsEngine.next();
            car1.setX(ball1.getPosX());
            car2.setX(ball2.getPosX());
        });
    }
}
