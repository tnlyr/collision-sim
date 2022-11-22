package edu.vanier.collisions.models;

public enum Terrain {
    DESERT(0.5),
    GRASS(0.7),
    ICE(0.9),
    SAND(0.3),
    SNOW(0.8),
    WATER(0.1),
    MOON(0.5, 1.62);

    // TODO: implement terrain background images
    private final double EARTH_GRAVITY = 9.8;

    private final double frictionCoefficient; // in range [0, 1]
    private final double gravity; // in m/s^2

    Terrain(double frictionCoefficient, double gravity) {
        this.frictionCoefficient = frictionCoefficient;
        this.gravity = gravity;
    }

    Terrain(double frictionCoefficient) {
        this.frictionCoefficient = frictionCoefficient;
        this.gravity = EARTH_GRAVITY;
    }

    public double frictionCoefficient() { return frictionCoefficient; }
    public double gravity() { return gravity; }
}
