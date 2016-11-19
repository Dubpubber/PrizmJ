package com.prizmj.display.parts;

import com.badlogic.gdx.graphics.Color;
import com.prizmj.display.parts.abstracts.Room;

/**
 * com.prizmj.display.parts.Hallway in PrizmJ
 */
public class Hallway extends Room {

    // True = North/South
    // False = East/West
    private boolean updown;

    /**
     * 2d Representation of Room
     * <p>
     * Example implementation:
     * new Room("BigRoom", 0, 0, 25, 25);
     *
     * @param name
     * @param x
     * @param y
     * @param z
     * @param width
     * @param height
     * @param floorColor
     */
    public Hallway(String name, float x, float y, float z, float width, float height, Color floorColor, boolean updown) {
        super(name, x, y, z, width, height, floorColor);
        if(width == height) setWidth(width + 1);
        this.updown = updown;
    }

    public boolean getUpDown() {
        return updown;
    }
}
