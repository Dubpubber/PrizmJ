package com.prizmj.display;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Array;
import com.prizmj.display.buildingparts.BasicRoom;
import com.prizmj.display.buildingparts.Room;

/**
 * com.prizmj.display.RenderManager in PrizmJ
 */
public class RenderManager {
    private PrizmJ prizmJ;
    // Holds all the rooms of the building.
    private Array<Room> rooms;

    // Holds all the RoomModels for actually displaying the building.
    private Array<RoomModel> models;

    public RenderManager(PrizmJ prizmJ) {
        this.prizmJ = prizmJ;
        this.rooms = new Array<>();
        this.models = new Array<>();
    }

    public void createBasicRoom(String name, float x, float y, int width, int height, Color floorColor, int dimension) {
        BasicRoom basicRoom = new BasicRoom(name, x, y, width, height, floorColor);
        rooms.add(basicRoom);
        models.add(new RoomModel(basicRoom, prizmJ.getModelBuilder(), dimension));
    }

    public void render(ModelBatch modelBatch, Environment environment) {
        models.forEach(rm -> rm.render(modelBatch, environment));
    }

    public Array getRooms() {
        return rooms;
    }

}
