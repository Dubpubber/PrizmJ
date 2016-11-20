package com.prizmj.display.parts.abstracts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import java.util.UUID;

/**
 * com.prizmj.display.buildingparts.Room in PrizmJ
 */
public abstract class Room {

    private UUID id;
    private String name;

    // For display purposes only //
    /*private Vector3 p1; // Bottom right (+, -)
    private Vector3 p2; // Bottom left (-, -)
    private Vector3 p3; // Top right (-, +)
    private Vector3 p4; // Top left (+, +)*/

    private float x;
    private float y;
    private float z;
    private float width;
    private float height;

    private Color floorColor;
    // -- //

    // For Doors
    public Room(){}

    /**
     * 2d Representation of Room
     *
     * Example implementation:
     *          new Room("BigRoom", 0, 0, 25, 25);
     */
    public Room(String name, float x, float y, float z, float width, float height, Color floorColor) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.floorColor = floorColor;
    }

    public void updatePosition(Vector3 vector3) {
        this.x = vector3.x;
        this.y = vector3.y;
        this.z = vector3.z;
    }

    public void updatePosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Color getFloorColor() {
        return floorColor;
    }

    public float getZ() {
        return z;
    }

    @Override
    public String toString() {
        return getName() + ":\n " +
                "\tLocation: " + new Vector3(x, y, z).toString() + "\n" +
                "\t(W,H): " + String.format("(%f, %f)", width, height) + "\n" +
                "\tColor: " + floorColor;
    }

}