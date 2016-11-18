package com.prizmj.display.simulation.components;

import com.prizmj.display.parts.*;

import java.util.UUID;

/**
 * Created by GrimmityGrammity on 11/14/2016.
 */

// TODO: Possible inner class?
public class Node {

    private UUID id;

    private float smokeDensity;
    private float walkingSpeed;

    private float x;
    private float y;
    private float z;

    private Room room;

    public Node(float xCen, float yCen, float floor, Room rm) {
        x = xCen;
        y = yCen;
        z = floor;
        room = rm;
        smokeDensity = 0.0f;
        walkingSpeed = 1.5f;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
