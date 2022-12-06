package edu.vanier.collisions.models;

import edu.vanier.collisions.controllers.ResourceManager;

public enum Terrain {
    DESERT(0.6, ResourceManager.DESERT),
    GRASS(0.35, ResourceManager.GRASS),
    ICE(0.09,  ResourceManager.ICE),
    CITY(1.35, ResourceManager.CITY),
    SNOW(0.15, ResourceManager.SNOW),
    WATER(0.1, ResourceManager.WATER),
    MOON(0.5, ResourceManager.MOON, 1.62);

    private final double frictionCoefficient; // in range [0, 1]
    private final String texturePath;
    private final double gravity; // in m/s^2

    /**
     *
     * @param frictionCoefficient
     * @param texturePath
     * @param gravity
     */
    Terrain(double frictionCoefficient, String texturePath, double gravity) {
        this.frictionCoefficient = frictionCoefficient;
        this.texturePath = texturePath;
        this.gravity = gravity;
    }

    /**
     *
     * @param frictionCoefficient
     * @param texturePath
     */
    Terrain(double frictionCoefficient, String texturePath) {
        this.frictionCoefficient = frictionCoefficient;
        this.texturePath = texturePath;
        this.gravity = 9.8;
    }

    /**
     *
     * @return the kinetic coefficient of friction
     */
    public double frictionCoefficient() { return frictionCoefficient; }

    /**
     *
     * @return the gravity of the terrain type
     */
    public double gravity() { return gravity; }

    /**
     *
     * @return the parameter of deceleration based on the friction coefficient and the gravity.
     */
    public double deceleration() { return frictionCoefficient * gravity; }

    /**
     *
     * @return the correct background based on the terrain type
     */
    public String texturePath() { return texturePath; }
}
