package com.prizmj.display.buildingparts;

import com.badlogic.gdx.graphics.Color;

/**
 * com.prizmj.display.buildingparts.BasicRoom in PrizmJ
 */
public class BasicRoom extends Room {
    /**
     * 2d Representation of Room
     * <p>
     * Example implementation:
     * new Room("BigRoom", 0, 0, 25, 25);
     *
     * @param name - The name of the room
     * @param x - The x transform for the starting position in the 3d world.
     * @param y - ' y transform '
     * @param width - The width of the room. (approx.)
     * @param height - The height of the room. (approx.)
     * @param floorColor - The color the floor of the room.
     */
    public BasicRoom(String name, float x, float y, int width, int height, Color floorColor) {
        super(name, x, y, width, height, floorColor);
    }
}
