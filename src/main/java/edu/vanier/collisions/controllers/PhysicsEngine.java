package edu.vanier.collisions.controllers;

import edu.vanier.collisions.models.Entity;

import java.util.ArrayList;

public class PhysicsEngine {
    private static PhysicsEngine instance = null;
    private final double GRAVITY = 9.8;
    private final double TIME_STEP = 0.01;
    // TODO: implement restitutions
    private double coefficientOfRestitution;
    private double coefficientOfFriction;
    private ArrayList<Entity> entities = new ArrayList<>();

    private PhysicsEngine() {
    }

    public static PhysicsEngine getInstance() {
        if (instance == null) {
            instance = new PhysicsEngine();
        }
        return instance;
    }

    public double getCoefficientOfRestitution() {
        return coefficientOfRestitution;
    }

    public void setCoefficientOfRestitution(double coefficientOfRestitution) {
        this.coefficientOfRestitution = coefficientOfRestitution;
    }

    public double getCoefficientOfFriction() {
        return coefficientOfFriction;
    }

    public void setCoefficientOfFriction(double coefficientOfFriction) {
        this.coefficientOfFriction = coefficientOfFriction;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    // TODO: implement collisions

    public void next() {
        for (Entity entity : entities) {
            double friction = GRAVITY * coefficientOfFriction * entity.getMass();
            entity.setSpeedX(entity.getSpeedX() + friction * TIME_STEP);
            entity.setPosX(entity.getPosX() + entity.getSpeedX() * TIME_STEP);
        }
    }
}
