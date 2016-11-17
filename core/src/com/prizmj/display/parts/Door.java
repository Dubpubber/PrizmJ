package com.prizmj.display.parts;

/**
 * com.prizmj.display.buildingparts.Door in PrizmJ
 */
public class Door {

    private Room connection1;
    private Room connection2;

    // Since our rooms only have 4 walls, we'll allow a positional for better understanding the doors position in relation to the room(s) it occupies
    // This variable also only represents connection1's side.
    // 1-2 South and North respectively
    // 3-4 East and West respectively
    private int side;

    public Door(int side) {
        this.side = side;
    }

    public Room getConnection1() {
        return connection1;
    }

    public int getSide() {
        return side;
    }

    public Room getFirstRoom() {
        return connection1;
    }

    public void setInitialRoom(Room connection1) {
        this.connection1 = connection1;
    }

    public Room getSecondRoom() {
        return connection2;
    }

    public void setConnectedRoom(Room connection2) {
        this.connection2 = connection2;
    }
}