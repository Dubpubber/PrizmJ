package com.prizmj.display.geometricnetworkmodel;

import java.util.UUID;

/**
 * Created by GrimmityGrammity on 11/14/2016.
 */

// TODO: Possible inner class?
public class Edge {

    private UUID id;

    private Node start;
    private Node end;

    private float length;
    private float walkingSpeed;
    private float traversalTime;


    public UUID getId() { return id; }

    public void setId(UUID id) {
        this.id = id;
    }

    public Node getStart() {
        return start;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public Node getEnd() {
        return end;
    }

    public void setEnd(Node end) {
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
