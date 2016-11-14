package com.prizmj.display.buildingparts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import java.util.UUID;

/**
 * com.prizmj.display.buildingparts.Room in PrizmJ
 */
public abstract class Room {

    private UUID id;
    private String name;

    // TODO floor object
    private float smokeDensity;
    private float walkingSpeed;
    private boolean isStairs = false;

    // For display purposes only //
    private Vector3 p1; // Bottom right (+, -)
    private Vector3 p2; // Bottom left (-, -)
    private Vector3 p3; // Top right (-, +)
    private Vector3 p4; // Top left (+, +)

    private float x;
    private float y;
    private int width;
    private int height;

    private Color floorColor;
    // -- //

    /**
     * 2d Representation of Room
     *
     * Example implementation:
     *          new Room("BigRoom", 0, 0, 25, 25);
     */
    public Room(String name, float x, float y, int width, int height, Color floorColor) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.p1 = new Vector3(0, 0, 0);
        this.p2 = new Vector3(0 - width, 0, 0);
        this.p3 = new Vector3(0 - width, 0, height);
        this.p4 = new Vector3(0, 0, height);
        this.floorColor = floorColor;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getSmokeDensity() {
        return smokeDensity;
    }

    public void setSmokeDensity(float smokeDensity) {
        this.smokeDensity = smokeDensity;
    }

    public float getWalkingSpeed() {
        return walkingSpeed;
    }

    public void setWalkingSpeed(float walkingSpeed) {
        this.walkingSpeed = walkingSpeed;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Color getFloorColor() {
        return floorColor;
    }

    public Vector3 getP1() {
        return p1;
    }

    public void setP1(Vector3 p1) {
        this.p1 = p1;
    }

    public Vector3 getP2() {
        return p2;
    }

    public void setP2(Vector3 p2) {
        this.p2 = p2;
    }

    public Vector3 getP3() {
        return p3;
    }

    public void setP3(Vector3 p3) {
        this.p3 = p3;
    }

    public Vector3 getP4() {
        return p4;
    }

    public void setP4(Vector3 p4) {
        this.p4 = p4;
    }

    public String toString() {
        return String.format("p1: %s , p2: %s , p3: %s , p4: %s", p1.toString(), p2.toString(), p3.toString(), p4.toString());
    }
}