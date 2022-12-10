package edu.vanier.collisions.controllers;

import edu.vanier.collisions.models.PhysicsEntity;
import edu.vanier.collisions.models.Terrain;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import java.io.Serializable;

public class PhysicsEngine implements Serializable {
    private static PhysicsEngine instance = null;
    private double playbackSpeed = 1;
    private int collisionCount = 0;
    private double preCollisionDuration = 0;
    private Terrain terrain = Terrain.GRASS;
    private double restitutionCoefficient;
    private transient ParallelTransition parallelTransition;
    private PhysicsEntity entity1, entity2;

    private PhysicsEngine() {
    }

    /**
     * Singleton getter
     * @return the instance of the PhysicsEngine
     */
    public static PhysicsEngine getInstance() {
        if (instance == null) {
            instance = new PhysicsEngine();
        }
        return instance;
    }

    /**
     * Singleton setter
     * @param newInstance the new instance of the PhysicsEngine
     */
    public static void setInstance(PhysicsEngine newInstance) {
        instance = newInstance;
    }


    /**
     * Initiates the parallel transition
     */
    public void init() {
        parallelTransition = new ParallelTransition(entity1.getTranslateTransition(), entity2.getTranslateTransition());
        parallelTransition.setInterpolator(Interpolator.EASE_OUT);

        setTrajectoryToStandstill(entity1);
        setTrajectoryToStandstill(entity2);
    }

    /**
     * Should be called in the event of a collision
     * Resets animation and recalculates trajectory
     */
    public void onCollision() {
        if (collisionCount != 0) {
            return;
        }

        preCollisionDuration = parallelTransition.getCurrentTime().toSeconds();
        collisionCount++;

        AudioClip audioClip = new AudioClip(getClass().getResource(ResourceManager.COLLISION_SOUND).toString());
        audioClip.play();

        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double v1 = getVelocityXAfterCollision(entity1, entity2);
        double v2 = getVelocityXAfterCollision(entity2, entity1);

        entity1.setVelocityX(v1);
        entity2.setVelocityX(v2);

        setTrajectoryToStandstill(entity1);
        setTrajectoryToStandstill(entity2);

        // start new transitions
        parallelTransition.playFromStart();
    }

    public ParallelTransition getParallelTransition() {
        return parallelTransition;
    }

    /**
     * Sets the trajectory of the entity to its standstill point
     * @param entity the entity to set the trajectory for
     */
    private void setTrajectoryToStandstill(PhysicsEntity entity) {
        entity.setLayoutX(entity.getLayoutX() + entity.getTranslateX());
        entity.setTranslateX(0);
        double timeOfStandstill = entity.getTimeAtStandstill(getFrictionDeceleration(entity.getDirection()));
        double positionOfStandstill = entity.getPositionAtTime(getFrictionDeceleration(entity.getDirection()), timeOfStandstill)-entity.getRelativePosition();
        entity.getTranslateTransition().setDuration(Duration.seconds(timeOfStandstill / playbackSpeed));
        entity.getTranslateTransition().setByX(positionOfStandstill);
    }

    /**
     * Adjusts an entity's velocity to account for friction
     * @param entity the entity to adjust the velocity for
     * @param timeElapsed the time elapsed since the last update
     */
    public void adjustVelocityWithAcceleration(PhysicsEntity entity, double timeElapsed) {
        double newVel = entity.getVelocityAtTime(getFrictionDeceleration(entity.getDirection()), timeElapsed);
        entity.setVelocityX(newVel);
    }

    /**
     * Calculates total kinetic energy of the system
     * @return the total kinetic energy of the system in kJ
     */
    public double getTotalSystemEnergy(){
        double energy1 = entity1.getKineticEnergy();
        double energy2 = entity2.getKineticEnergy();
        return (energy1 + energy2)/1000;
    }

    /**
     * Calculates the velocity of entity1 after a collision with entity2
     * @param collidedEntity the entity that collided
     * @param collidingEntity the entity that collided with the first entity
     * @return the velocity of the collided entity after the collision
     */
    private double getVelocityXAfterCollision(PhysicsEntity collidedEntity, PhysicsEntity collidingEntity) {
        return (collidedEntity.getMass() * collidedEntity.getVelocityX() + collidingEntity.getMass() * collidingEntity.getVelocityX() + collidingEntity.getMass() * restitutionCoefficient * (collidingEntity.getVelocityX()-collidedEntity.getVelocityX())) / (collidedEntity.getMass() + collidingEntity.getMass());
    }

    /**
     * Calculates the friction deceleration of the entity
     * @param direction the direction of the entity
     * @return the friction deceleration of the entity
     */
    private double getFrictionDeceleration(short direction) {
        return -direction * terrain.deceleration();
    }

    /**
     * Calculates the time elapsed since animation start
     * @return the time elapsed since animation start
     */
    public double getCurrentTime() {
        return parallelTransition.getCurrentTime().toSeconds() + preCollisionDuration;
    }

    /**
     * Plays the animation
     */
    public void play() {
        init();
        parallelTransition.play();
    }

    /**
     * Pauses the animation
     */
    public void pause() {
        parallelTransition.pause();
    }

    /**
     * Stops & resets the animation
     */
    public void reset() {
        collisionCount = 0;
        parallelTransition.stop();
        parallelTransition.jumpTo(Duration.ZERO);
        entity1.reset();
        entity2.reset();
        init();
    }

    /**
     * Sets the playback speed of the animation
     * @return the playback speed of the animation
     */
    public double getPlaybackSpeed() {
        return playbackSpeed;
    }

    /**
     * Gets the playback speed of the animation
     * @param playbackSpeed the playback speed of the animation
     */
    public void setPlaybackSpeed(double playbackSpeed) {
        this.playbackSpeed = playbackSpeed;
    }

    /**
     * Gets the terrain of the animation
     * @return the terrain of the animation
     */
    public Terrain getTerrain() {
        return terrain;
    }

    /**
     * Sets the terrain of the animation
     * @param terrain the terrain of the animation
     */
    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    /**
     * Gets the restitution coefficient of the animation
     * @return the restitution coefficient of the animation
     */
    public double getRestitutionCoefficient() {
        return restitutionCoefficient;
    }

    /**
     * Sets the restitution coefficient of the animation
     * @param restitutionCoefficient the restitution coefficient of the animation
     */
    public void setRestitutionCoefficient(double restitutionCoefficient) {
        this.restitutionCoefficient = restitutionCoefficient;
    }

    /**
     * Gets the entity1 of the animation
     * @return the entity1 of the animation
     */
    public PhysicsEntity getEntity1() {
        return entity1;
    }

    /**
     * Sets the entity1 of the animation
     * @param entity1 the entity1 of the animation
     */
    public void setEntity1(PhysicsEntity entity1) {
        this.entity1 = entity1;
    }

    /**
     * Gets the entity2 of the animation
     * @return the entity2 of the animation
     */
    public PhysicsEntity getEntity2() {
        return entity2;
    }

    /**
     * Sets the entity2 of the animation
     * @param entity2 the entity2 of the animation
     */
    public void setEntity2(PhysicsEntity entity2) {
        this.entity2 = entity2;
    }

    /**
     * Sets the entities
     * @param entity1 the first entity
     * @param entity2 the second entity
     */
    public void setEntities(PhysicsEntity entity1, PhysicsEntity entity2) {
        this.entity1 = entity1;
        this.entity2 = entity2;
    }

    /**
     * Gets the collision count of the animation
     * @return the collision count of the animation
     */
    public int getCollisionCount() {
        return collisionCount;
    }

    /**
     * Sets the collision count of the animation
     * @param collisionCount the collision count of the animation
     */
    public void setCollisionCount(int collisionCount) {
        this.collisionCount = collisionCount;
    }
}
