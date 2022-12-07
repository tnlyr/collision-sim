package edu.vanier.collisions.controllers;

import edu.vanier.collisions.models.PhysicsEntity;
import edu.vanier.collisions.models.Terrain;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;
import java.io.Serializable;

public class PhysicsEngine implements Serializable {
    private static PhysicsEngine instance = null;
    private double playbackSpeed = 1;
    private Terrain terrain = Terrain.GRASS;
    private double restitutionCoefficient;
    private transient ParallelTransition parallelTransition;
    private PhysicsEntity entity1, entity2;

    private PhysicsEngine() {
    }

    /**
     *
     * @return
     */
    public static PhysicsEngine getInstance() {
        if (instance == null) {
            instance = new PhysicsEngine();
        }
        return instance;
    }

    /**
     *
     * @param newInstance
     */
    public static void setInstance(PhysicsEngine newInstance) {
        instance = newInstance;
    }


    public void init() {
        parallelTransition = new ParallelTransition(entity1.getTranslateTransition(), entity2.getTranslateTransition());
        parallelTransition.setCycleCount(1);
        parallelTransition.setInterpolator(Interpolator.EASE_OUT);


        setTrajectoryToStandstill(entity1);
        setTrajectoryToStandstill(entity2);

        ChangeListener<Duration> onCollision = new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> obs, Duration ov, Duration nv)  {
                boolean isColliding  = entity1.getBoundsInParent().intersects(entity2.getBoundsInParent());

                if (!isColliding) {
                    return;
                }
                
                parallelTransition.currentTimeProperty().removeListener(this);
                parallelTransition = new ParallelTransition();

                double v1 = getVelocityXAfterCollision(entity1, entity2);
                double v2 = getVelocityXAfterCollision(entity2, entity1);

                entity1.setVelocityX(v1);
                entity2.setVelocityX(v2);

                setTrajectoryToStandstill(entity1);
                setTrajectoryToStandstill(entity2);

                // start new transitions
                parallelTransition = new ParallelTransition(entity1.getTranslateTransition(), entity2.getTranslateTransition());
                parallelTransition.play();

            };
        };

        parallelTransition.currentTimeProperty().addListener(onCollision);
    }

    public ParallelTransition getParallelTransition() {
        return parallelTransition;
    }

    /**
     *
     * @param entity
     */
    private void setTrajectoryToStandstill(PhysicsEntity entity) {
//        entity.setInitialPosX(entity.getRelativePosition());
        double timeOfStandstill = entity.getTimeAtStandstill(getFrictionDeceleration(entity.getDirection()));
        double positionOfStandstill = entity.getPositionAtTime(getFrictionDeceleration(entity.getDirection()), timeOfStandstill)-entity.getRelativePosition();
        entity.initializeTranslateTransition();
        entity.getTranslateTransition().setDuration(Duration.seconds(timeOfStandstill / playbackSpeed));
        entity.getTranslateTransition().setByX(positionOfStandstill);
    }

    /**
     *
     * @param entity
     * @param timeElapsed
     */
    public void adjustVelocityWithAcceleration(PhysicsEntity entity, double timeElapsed) {
        double newVel = entity.getVelocityAtTime(getFrictionDeceleration(entity.getDirection()), timeElapsed);
        entity.setVelocityX(newVel);
    }

    public double getTotalSystemEnergy(){
        double energy1 = entity1.getKineticEnergy();
        double energy2 = entity2.getKineticEnergy();
        return (energy1 + energy2)/1000;
    }

    /**
     *
     * @param collidedEntity
     * @param collidingEntity
     * @return
     */
    private double getVelocityXAfterCollision(PhysicsEntity collidedEntity, PhysicsEntity collidingEntity) {
        return (collidedEntity.getMass() * collidedEntity.getVelocityX() + collidingEntity.getMass() * collidingEntity.getVelocityX() + collidingEntity.getMass() * restitutionCoefficient * (collidingEntity.getVelocityX()-collidedEntity.getVelocityX())) / (collidedEntity.getMass() + collidingEntity.getMass());
    }

    /**
     *
     * @param direction
     * @return
     */
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
        entity1.reset();
        entity2.reset();
        init();
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

    /**
     *
     * @param restitutionCoefficient
     */
    public void setRestitutionCoefficient(double restitutionCoefficient) {
        this.restitutionCoefficient = restitutionCoefficient;
    }

    /**
     *
     * @return
     */
    public PhysicsEntity getEntity1() {
        return entity1;
    }

    /**
     *
     * @param entity1
     */
    public void setEntity1(PhysicsEntity entity1) {
        this.entity1 = entity1;
    }

    /**
     *
     * @return
     */
    public PhysicsEntity getEntity2() {
        return entity2;
    }

    /**
     *
     * @param entity2
     */
    public void setEntity2(PhysicsEntity entity2) {
        this.entity2 = entity2;
    }

    /**
     *
     * @param entity1
     * @param entity2
     */
    public void setEntities(PhysicsEntity entity1, PhysicsEntity entity2) {
        this.entity1 = entity1;
        this.entity2 = entity2;
    }
}
