package edu.vanier.collisions.controllers;

import edu.vanier.collisions.models.PhysicsEntity;
import edu.vanier.collisions.models.Terrain;
import javafx.util.Duration;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class PhysicsEngine implements Serializable {
    private static PhysicsEngine instance = null;
    private double playbackSpeed = 1;
    private Terrain terrain;
    private double restitutionCoefficient;
    private ArrayList<PhysicsEntity> entities = new ArrayList<>();

    private PhysicsEngine() {
    }

    public static PhysicsEngine getInstance() {
        if (instance == null) {
            instance = new PhysicsEngine();
        }
        return instance;
    }

    // TODO: implement gravity & friction
    // TODO: create collision object (ugly return type currently)
    private double[] computeCollision() {
        double posXDelta = entities.get(0).getRelativePosition() - entities.get(1).getRelativePosition();
        double speedXDelta = entities.get(1).getSpeedX() - entities.get(0).getSpeedX();
        double timeOfCollision = posXDelta / speedXDelta;
        double posOfCollision = timeOfCollision * entities.get(0).getSpeedX() + entities.get(0).getRelativePosition();
        System.out.printf("Collision with delta %f at %f in %f seconds \n", posXDelta, posOfCollision, timeOfCollision);
        return new double[]{posOfCollision, timeOfCollision};
    }

    // TODO: disambiguate init and collision setup
    public void init() {
        double[] collision = computeCollision();
        double posOfCollision = collision[0];
        double timeOfCollision = collision[1];
        for (PhysicsEntity entity : entities) {
            entity.getTranslateTransition().setDuration(Duration.seconds(timeOfCollision / playbackSpeed));
            System.out.printf("Transition By %f X\n", (posOfCollision-entity.getRelativePosition()));
            entity.getTranslateTransition().setByX(posOfCollision-entity.getRelativePosition());
        }
        // TODO: implement collision restitution
    }

    public void play() {
        for (PhysicsEntity entity : entities) {
            entity.play();
        }
    }

    public void pause() {
        for (PhysicsEntity entity : entities) {
            entity.pause();
        }
    }

    public void reset() {
        for (PhysicsEntity entity : entities) {
            entity.reset();
        }
    }

    public double getPlaybackSpeed() {
        return playbackSpeed;
    }

    public void setPlaybackSpeed(double playbackSpeed) {
        this.playbackSpeed = playbackSpeed;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public double getRestitutionCoefficient() {
        return restitutionCoefficient;
    }

    public void setRestitutionCoefficient(double restitutionCoefficient) {
        this.restitutionCoefficient = restitutionCoefficient;
    }

    public ArrayList<PhysicsEntity> getEntities() {
        return entities;
    }

    public void setEntities(PhysicsEntity... entities) {
        this.entities = new ArrayList<>(entities.length);
        Collections.addAll(this.entities, entities);
    }
}
