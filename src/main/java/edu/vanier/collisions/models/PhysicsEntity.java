package edu.vanier.collisions.models;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.Rectangle;
import java.io.Serializable;

public class PhysicsEntity extends Rectangle implements Serializable {
    private transient TranslateTransition translateTransition;
    private double initialPosX, velocityX, centerOffset, mass;

    public PhysicsEntity() {
        super();
        initializeTranslateTransition();
    }

    /**
     * Configuring our translations
     */
    public void initializeTranslateTransition() {
        translateTransition = new TranslateTransition();
        translateTransition.setNode(this);
        translateTransition.setInterpolator(Interpolator.EASE_OUT);
    }

    /**
     *
     * @return the position of the translated note from one position to another one
     */
    public TranslateTransition getTranslateTransition() {
        return translateTransition;
    }

    /**
     *
     * @return the initial position of the cart
     */
    public double getInitialPosX() {
        return initialPosX;
    }

    /**
     *
     * @param initialPosX
     * Sets the initial position
     */
    public void setInitialPosX(double initialPosX) {
        this.initialPosX = initialPosX;
    }

    /**
     *
     * @return the velocity in X of the car
     */
    public double getVelocityX() {
        return velocityX;
    }

    /**
     *
     * @param velocityX
     * Sets the velocity for the car
     */
    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    /**
     *
     * @return the centerOffSet to make the collision at the tip and not center of the object
     */
    public double getCenterOffset() {
        return centerOffset;
    }

    /**
     *
     * @param centerOffset
     * Set the accurate centerOffset to make the collision at the tip and not center of the object
     */
    public void setCenterOffset(double centerOffset) {
        this.centerOffset = centerOffset;
    }

    /**
     *
     * @return the mass of the car
     */
    public double getMass() {
        return mass;
    }

    /**
     *
     * @param mass
     * set the mass of the car
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     *
     * @return allows to get the position in x while taking into account the centerOffset and the current position
     */
    public double getRelativePosition() {
        return getLayoutX() + getTranslateX() + centerOffset;
    }

    /**
     *
     * @param acceleration
     * @param time
     * @return The position of the car based on the time and acceleration taking into account your original position
     */
    public double getPositionAtTime(double acceleration, double time) {
        time = Math.min(time, getTimeAtStandstill(acceleration));
        return initialPosX + centerOffset + (velocityX * time) + (0.5 * acceleration * time * time);
    }

    /**
     *
     * @param acceleration
     * @param time
     * @return The velocity of the car based on the acceleration and time of the simulation
     */
    public double getVelocityAtTime(double acceleration, double time) {
        if (time > getTimeAtStandstill(acceleration)) {
            return 0;
        }
        return velocityX + 0.25 * acceleration * time;
    }

    /**
     *
     * @param acceleration
     * @return if they would not collide when would the car would stop based on the friction only.
     */
    public double getTimeAtStandstill(double acceleration) {
        double time = (0 - velocityX) / acceleration;
        return time < 0 ? Double.POSITIVE_INFINITY : time;
    }

    /**
     *
     * @return the direction of the car based on its velocityX
     */
    public short getDirection() {
        if (velocityX == 0) {
            return 0;
        }
        return (short) (velocityX /Math.abs(velocityX));
    }

    /**
     * Allows the transition (animation) to play
     */

    public void play() {
        translateTransition.play();
    }
    /**
     * Allows the transition (animation) to stop
     */
    public void pause() {
        translateTransition.pause();
    }

    /**
     * Allows the transition (animation) to reset
     */
    public void reset() {
        translateTransition.stop();
        setTranslateX(0);
        setLayoutX(initialPosX);
    }
}
