package edu.vanier.collisions.controllers;

import edu.vanier.collisions.models.PhysicsEntity;
import edu.vanier.collisions.models.Terrain;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
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

    public void setInstance(PhysicsEngine newInstance) {
        instance = newInstance;
    }

    // TODO: disambiguate init and collision setup
    // TODO: cleanup code
    public void init() {
        for (PhysicsEntity entity : entities) {
            double nullTime = entity.getTimeAtNullVelocity(entity.getDirection() * getFrictionDeceleration());
            entity.getTranslateTransition().setDuration(Duration.seconds(nullTime / playbackSpeed));
            System.out.printf("Transition By %f X\n", (entity.getPositionAtTime(entity.getDirection() * getFrictionDeceleration(), nullTime)-entity.getRelativePosition()));
            entity.getTranslateTransition().setByX(entity.getPositionAtTime(entity.getDirection() * getFrictionDeceleration(), nullTime)-entity.getRelativePosition());
        }

        InvalidationListener onCollision = (Observable observable) -> {
            // check for collision
            boolean c  = entities.get(0).getBoundsInParent().intersects(entities.get(1).getBoundsInParent());
            if (c) {
                System.out.println("Collision!");
                double timeElapsed = Math.max(entities.get(0).getTranslateTransition().getCurrentTime().toSeconds(), entities.get(1).getTranslateTransition().getCurrentTime().toSeconds());
                System.out.println("Time Elapsed: " + timeElapsed);
                // TODO: find better solution
                if (timeElapsed < 0.5) {
                    return;
                }
                for (PhysicsEntity entity : entities) {
                    entity.getTranslateTransition().stop();
                }
                // update velocity properties
                for (PhysicsEntity entity : entities) {
                    double newVel = entity.getVelocityAtTime(entity.getDirection() * getFrictionDeceleration(), timeElapsed);
                    System.out.println("Old Velocity: " + entity.getVelocityX());
                    System.out.println("Adjusted Old Velocity: " + newVel);
                    System.out.println("Position: " + entity.getRelativePosition());
                    entity.setVelocityX(newVel);
                }
                System.out.println("initial position: " + entities.get(0).getRelativePosition());

                double v1 = getVelocityXAfterCollision(entities.get(0), entities.get(1));
                double v2 = getVelocityXAfterCollision(entities.get(1), entities.get(0));
                // calculate new velocities
                System.out.println("new v1: " + v1);
                System.out.println("new v2: " + v2);
                // set new velocities
                System.out.println("initial position: " + entities.get(0).getRelativePosition());

                entities.get(0).setVelocityX(v1);
                entities.get(1).setVelocityX(v2);
                entities.get(0).setInitialPosX(entities.get(0).getRelativePosition());
                entities.get(1).setInitialPosX(entities.get(1).getRelativePosition());
                System.out.println("initial position: " + entities.get(0).getRelativePosition());

                double nullTime1 = entities.get(0).getTimeAtNullVelocity(entities.get(0).getDirection() * getFrictionDeceleration());
                double nullTime2 = entities.get(1).getTimeAtNullVelocity(entities.get(1).getDirection() * getFrictionDeceleration());
                System.out.println("nullTime1: " + nullTime1 + " nullTime2: " + nullTime2);
                // set new positions
                System.out.println("initial position1: " + entities.get(0).getInitialPosX());
                System.out.println("center offset1: " + entities.get(0).getCenterOffset());
                System.out.println("sum1: " + (entities.get(0).getInitialPosX() + entities.get(0).getCenterOffset()));
                System.out.println("initial position2: " + entities.get(1).getInitialPosX());
                System.out.println("center offset2: " + entities.get(1).getCenterOffset());
                System.out.println("sum2: " + (entities.get(1).getInitialPosX() + entities.get(1).getCenterOffset()));
                double pos1 = entities.get(0).getPositionAtTime(entities.get(0).getDirection() * getFrictionDeceleration(), nullTime1);
                double pos2 = entities.get(1).getPositionAtTime(entities.get(1).getDirection() * getFrictionDeceleration(), nullTime2);
                System.out.println("pos1: " + pos1 + " pos2: " + pos2);
                // set new transitions
                entities.get(0).getTranslateTransition().setDuration(Duration.seconds(nullTime1 / playbackSpeed));
                entities.get(0).getTranslateTransition().setByX(pos1);
                entities.get(1).getTranslateTransition().setDuration(Duration.seconds(nullTime2 / playbackSpeed));
                entities.get(1).getTranslateTransition().setByX(pos2);
                // start new transitions
                for (PhysicsEntity entity : entities) {
                    entity.getTranslateTransition().play();
                }
                System.out.println("v1: " + v1 + " v2: " + v2);
            }

        };

        // get entity with biggest time to null velocity
        double maxTime = 0;
        PhysicsEntity trackedEntity = entities.get(0);
        for (PhysicsEntity entity : entities) {
            double nullTime = entity.getTimeAtNullVelocity(entity.getDirection() * getFrictionDeceleration());
            if (nullTime > maxTime) {
                maxTime = nullTime;
                trackedEntity = entity;
            }
        }
        // set collision listener
        trackedEntity.translateXProperty().addListener(onCollision);

        // on collision
        trackedEntity.getTranslateTransition().onFinishedProperty().set(event -> {

        });
    }

    private double getVelocityXAfterCollision(PhysicsEntity collidedEntity, PhysicsEntity collidingEntity) {
        double velocityXAfterCollision = (collidedEntity.getMass() * collidedEntity.getVelocityX() + collidingEntity.getMass() * collidingEntity.getVelocityX() + collidingEntity.getMass() * restitutionCoefficient * (collidingEntity.getVelocityX()-collidedEntity.getVelocityX())) / (collidedEntity.getMass() + collidingEntity.getMass());
        return velocityXAfterCollision;
    }

    private double getFrictionDeceleration() {
        return -1 * terrain.gravity() * terrain.frictionCoefficient();
    }

    public void play() {
        init();
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
