package com.prizmj.display;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Array;
import com.prizmj.display.models.RoomModel;
import com.prizmj.display.parts.BasicRoom;
import com.prizmj.display.parts.Door;
import com.prizmj.display.parts.Room;
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

    public void createBasicRoom2D3DPair(String room1name, float x, float y, float z, int width, int height, Color floorColor) {
        BasicRoom room1 = new BasicRoom(room1name + "_2d", x, y, z, width, height, floorColor);
        BasicRoom room2 = new BasicRoom(room1name + "_3d", x, y, z, width, height, floorColor);
        rooms.addAll(room1, room2);
        RoomModel roomModel1 = new RoomModel(room1, prizmJ.getModelBuilder(), 2);
        RoomModel roomModel2 = new RoomModel(room2, prizmJ.getModelBuilder(), 3);
        roomModel1.setVisibility(roomModel1.getDimension() == prizmJ.getCurrentDimension());
        roomModel2.setVisibility(roomModel2.getDimension() == prizmJ.getCurrentDimension());
        models.addAll(roomModel1, roomModel2);
    }

    public void createBasicRoom2D3DPair(String room1name, float x, float y, float z, int width, int height, Color floorColor, Door... doors) {
        BasicRoom room1 = new BasicRoom(room1name + "_2d", x, y, z, width, height, floorColor);
        BasicRoom room2 = new BasicRoom(room1name + "_3d", x, y, z, width, height, floorColor);
        rooms.addAll(room1, room2);
        for(Door door : doors) door.setInitialRoom(room1);
        RoomModel roomModel1 = new RoomModel(room1, prizmJ.getModelBuilder(), 2, doors);
        RoomModel roomModel2 = new RoomModel(room2, prizmJ.getModelBuilder(), 3, doors);
        roomModel1.setVisibility(roomModel1.getDimension() == prizmJ.getCurrentDimension());
        roomModel2.setVisibility(roomModel2.getDimension() == prizmJ.getCurrentDimension());
        models.addAll(roomModel1, roomModel2);
    }

    // TODO change everything about this, and do positive and negative checks
    // room3d_2.moveTo(((room1.getRoom().getX() + (room1.getRoom().getWidth() / 2)) + (room3d_2.getRoom().getWidth() / 2)) + PrizmJ.WALL_THICKNESS, room1.getRoom().getY(), room1.getRoom().getZ());
    // room3d_2.moveTo(room1.getRoom().getX(), room1.getRoom().getY(), ((room1.getRoom().getZ() + (room1.getRoom().getHeight() / 2)) + (room2.getRoom().getHeight() / 2)) + PrizmJ.WALL_THICKNESS)
    public void attachRoom(String existingRoom, String attachingRoom, Cardinal cardinal) throws Exception {
        RoomModel room2d_1 = getRoomModelByName(existingRoom + "_2d");
        RoomModel room2d_2 = getRoomModelByName(attachingRoom + "_2d");
        RoomModel room3d_1 = getRoomModelByName(existingRoom + "_3d");
        RoomModel room3d_2 = getRoomModelByName(attachingRoom + "_3d");
        if(room2d_1 != null && room2d_2 != null && room3d_1 != null && room3d_2 != null) {
            switch(cardinal) {
                case NORTH: // Wall 1
                    room3d_2.moveTo(room3d_1.getRoom().getX(), room3d_1.getRoom().getY(), ((room3d_1.getRoom().getZ() + (room3d_1.getRoom().getHeight() / 2)) + (room3d_2.getRoom().getHeight() / 2)) + PrizmJ.WALL_THICKNESS);
                    room2d_2.moveTo(room2d_1.getRoom().getX(), room2d_1.getRoom().getY(), ((room2d_1.getRoom().getZ() + (room2d_1.getRoom().getHeight() / 2)) + (room2d_2.getRoom().getHeight() / 2)) + PrizmJ.WALL_THICKNESS);
                    break;
                case SOUTH: // Wall 2
                    room3d_2.moveTo(room3d_1.getRoom().getX(), room3d_1.getRoom().getY(), ((room3d_1.getRoom().getZ() - (room3d_1.getRoom().getHeight() / 2)) - (room3d_2.getRoom().getHeight() / 2)) - PrizmJ.WALL_THICKNESS);
                    room2d_2.moveTo(room2d_1.getRoom().getX(), room2d_1.getRoom().getY(), ((room2d_1.getRoom().getZ() - (room2d_1.getRoom().getHeight() / 2)) - (room2d_2.getRoom().getHeight() / 2)) - PrizmJ.WALL_THICKNESS);
                    break;
                case EAST: // Wall 3
                    room3d_2.moveTo(((room3d_1.getRoom().getX() - (room3d_1.getRoom().getWidth() / 2)) - (room3d_2.getRoom().getWidth() / 2)) - PrizmJ.WALL_THICKNESS, room3d_1.getRoom().getY(), room3d_1.getRoom().getZ());
                    room2d_2.moveTo(((room2d_1.getRoom().getX() - (room2d_1.getRoom().getWidth() / 2)) - (room2d_2.getRoom().getWidth() / 2)) - PrizmJ.WALL_THICKNESS, room2d_1.getRoom().getY(), room2d_1.getRoom().getZ());
                    break;
                case WEST: // Wall 4
                    room3d_2.moveTo(((room3d_1.getRoom().getX() + (room3d_1.getRoom().getWidth() / 2)) + (room3d_2.getRoom().getWidth() / 2)) + PrizmJ.WALL_THICKNESS, room3d_1.getRoom().getY(), room3d_1.getRoom().getZ());
                    room2d_2.moveTo(((room2d_1.getRoom().getX() + (room2d_1.getRoom().getWidth() / 2)) + (room2d_2.getRoom().getWidth() / 2)) + PrizmJ.WALL_THICKNESS, room2d_1.getRoom().getY(), room2d_1.getRoom().getZ());
                    break;
            }
        } else throw new Exception("Can't attach a room to a nonexistent room.");
    }

    public RoomModel getRoomModelByName(String name) {
        final RoomModel[] room = {null};
        models.forEach(rm -> {
            if(rm.getRoom().getName().compareTo(name) == 0) room[0] = rm;
        });
        return room[0];
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
