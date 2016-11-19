package com.prizmj.display.simulation.components;

import java.util.UUID;

/**
 * Created by GrimmityGrammity on 11/14/2016.
 */

// TODO: Possible inner class?
public class Edge {

    private UUID id;

    private Vertex start;
    private Vertex end;

    private float length;
    private float walkingSpeed;
    private float traversalTime;


    public UUID getId() { return id; }

    public void setId(UUID id) {
        this.id = id;
    }

    public Vertex getStart() {
        return start;
    }

    public void setStart(Vertex start) {
        this.start = start;
    }

    public Vertex getEnd() {
        return end;
    }

    public void setEnd(Vertex end) {
        this.end = end;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWalkingSpeed() {
        return walkingSpeed;
    }

    public void setWalkingSpeed(float walkingSpeed) {
        this.walkingSpeed = walkingSpeed;
    }

    public float getTraversalTime() {
        return traversalTime;
    }

    public void setTraversalTime(float traversalTime) {
        this.traversalTime = traversalTime;
    }
}
