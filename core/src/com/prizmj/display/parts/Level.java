package com.prizmj.display.parts;

import com.badlogic.gdx.utils.Array;
import com.prizmj.display.PrizmJ;
import com.prizmj.display.models.RoomModel;

import java.util.UUID;

/**
 * com.prizmj.display.parts.Floor in PrizmJ
 */
public class Level {
    // PrizmJ object
    private PrizmJ prizmJ;

    // Identifiers //
    private UUID uuid;
    private int level;

    // Memory
    private Array<RoomModel> allRooms;

    public Level(int level, RoomModel... models) {
        this.level = level;
        this.allRooms = new Array<>();
        if(models != null && models.length > 0)
            allRooms.addAll(models);
    }

    public Array<RoomModel> getAllRooms() {
        return new Array<>(allRooms);
    }

    public void addAll(RoomModel... models) {
        allRooms.addAll(models);
    }

}
