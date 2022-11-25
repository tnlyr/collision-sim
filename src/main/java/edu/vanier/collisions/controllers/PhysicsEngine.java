package edu.vanier.collisions.controllers;

import edu.vanier.collisions.models.PhysicsEntity;
import edu.vanier.collisions.models.Terrain;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.util.Duration;
import java.io.Serializable;

public class PhysicsEngine implements Serializable {
    private static PhysicsEngine instance = null;
    private double playbackSpeed = 1;
    private Terrain terrain;
    private double restitutionCoefficient;
    private transient ParallelTransition parallelTransition;
    private PhysicsEntity entity1, entity2;

    private PhysicsEngine() {
    }

    public static PhysicsEngine getInstance() {
        if (instance == null) {
            instance = new PhysicsEngine();
        }
        return instance;
    }

    public static void setInstance(PhysicsEngine newInstance) {
        instance = newInstance;
    }

    public void init() {
        parallelTransition = new ParallelTransition(entity1.getTranslateTransition(), entity2.getTranslateTransition());
        parallelTransition.setCycleCount(1);
        parallelTransition.setInterpolator(Interpolator.EASE_OUT);

        setTrajectoryToStandstill(entity1);
        setTrajectoryToStandstill(entity2);

        InvalidationListener onCollision = (Observable observable) -> {
            boolean isColliding  = entity1.getBoundsInParent().intersects(entity2.getBoundsInParent());

            if (!isColliding) {
                return;
            }

            System.out.println("Collision!");

            double timeElapsed = parallelTransition.getCurrentTime().toSeconds();
            System.out.println("Time Elapsed: " + timeElapsed);

            // TODO: find better solution
            if (timeElapsed < 0.5) {
                return;
            }

            parallelTransition.stop();

            // TODO: update automatically instead
            adjustVelocity(entity1, timeElapsed);
            adjustVelocity(entity2, timeElapsed);

            double v1 = getVelocityXAfterCollision(entity1, entity2);
            double v2 = getVelocityXAfterCollision(entity2, entity1);

            entity1.setVelocityX(v1);
            entity2.setVelocityX(v2);

            setTrajectoryToStandstill(entity1);
            setTrajectoryToStandstill(entity2);

            // start new transitions
            parallelTransition.play();
            System.out.println("v1: " + v1 + " v2: " + v2);
        };

        // set collision listener
        // FIXME: glitch on first collision
        parallelTransition.currentTimeProperty().addListener(onCollision);

        // TODO: bounce with walls

        parallelTransition.onFinishedProperty().set((observable) -> {
            System.out.println("Finished!");
            // TODO: stop animation
        });
    }

    private void setTrajectoryToStandstill(PhysicsEntity entity) {
        entity.setInitialPosX(entity.getRelativePosition());
        if (entity.getVelocityX() == 0) {
            return;
        }
        double timeOfStandstill = entity.getTimeAtStandstill(getFrictionDeceleration(entity.getDirection()));
        double positionOfStandstill = entity.getPositionAtTime(getFrictionDeceleration(entity.getDirection()), timeOfStandstill)-entity.getRelativePosition();
        entity.getTranslateTransition().setDuration(Duration.seconds(timeOfStandstill / playbackSpeed));
        entity.getTranslateTransition().setByX(positionOfStandstill);
    }

    private void adjustVelocity(PhysicsEntity entity, double timeElapsed) {
        double newVel = entity.getVelocityAtTime(getFrictionDeceleration(entity.getDirection()), timeElapsed);
        entity.setVelocityX(newVel);
    }

    private double getVelocityXAfterCollision(PhysicsEntity collidedEntity, PhysicsEntity collidingEntity) {
        return (collidedEntity.getMass() * collidedEntity.getVelocityX() + collidingEntity.getMass() * collidingEntity.getVelocityX() + collidingEntity.getMass() * restitutionCoefficient * (collidingEntity.getVelocityX()-collidedEntity.getVelocityX())) / (collidedEntity.getMass() + collidingEntity.getMass());
    }

    private double getFrictionDeceleration(short direction) {
        return -direction * terrain.deceleration();
    }

    public void play() {
        init();
        parallelTransition.play();
    }

    public void pause() {
        parallelTransition.pause();
    }

    public void reset() {
        parallelTransition.stop();
        parallelTransition.jumpTo(Duration.ZERO);
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

    public PhysicsEntity getEntity1() {
        return entity1;
    }

    public void setEntity1(PhysicsEntity entity1) {
        this.entity1 = entity1;
    }

    public PhysicsEntity getEntity2() {
        return entity2;
    }

    public void setEntity2(PhysicsEntity entity2) {
        this.entity2 = entity2;
    }

    public void setEntities(PhysicsEntity entity1, PhysicsEntity entity2) {
        this.entity1 = entity1;
        this.entity2 = entity2;
    }
}
