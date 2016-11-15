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

    @Deprecated
    public void createBasicRoom(String name, float x, float y, float z, int width, int height, Color floorColor, int dimension) {
        BasicRoom basicRoom = new BasicRoom(name, x, y, z, width, height, floorColor);
        rooms.add(basicRoom);
        RoomModel roomModel = new RoomModel(basicRoom, prizmJ.getModelBuilder(), dimension);
        roomModel.setVisibility(roomModel.getDimension() == dimension);
        models.add(roomModel);
    }

    public void createBasicRoomPair(String room1name, String room2name, float x, float y, float z, int width, int height, Color floorColor) {
        BasicRoom room1 = new BasicRoom(room1name, x, y, z, width, height, floorColor);
        BasicRoom room2 = new BasicRoom(room2name, x, y, z, width, height, floorColor);
        rooms.addAll(room1, room2);
        RoomModel roomModel1 = new RoomModel(room1, prizmJ.getModelBuilder(), 2);
        RoomModel roomModel2 = new RoomModel(room1, prizmJ.getModelBuilder(), 3);
        roomModel1.setVisibility(roomModel1.getDimension() == prizmJ.getCurrentDimension());
        roomModel2.setVisibility(roomModel2.getDimension() == prizmJ.getCurrentDimension());
        models.addAll(roomModel1, roomModel2);
    }

    public void moveRoomByName(String name) {
        //todo
    }

    public void switchDimension(int dimension) {
        models.forEach(rm -> rm.setVisibility(rm.getDimension() == dimension));
    }

    public void render(ModelBatch modelBatch, Environment environment) {
        models.forEach(rm -> {
            if(rm.isVisible()) rm.render(modelBatch, environment);
        });
    }

    public Array getRooms() {
        return rooms;
    }

    public int getNumberofRooms() {
        return rooms.size / 2;
    }

}
