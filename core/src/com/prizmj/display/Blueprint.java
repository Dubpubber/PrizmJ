package com.prizmj.display;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.particles.values.PrimitiveSpawnShapeValue;
import com.badlogic.gdx.utils.Array;
import com.prizmj.display.models.RoomModel;
import com.prizmj.display.parts.BasicRoom;
import com.prizmj.display.parts.Door;
import com.prizmj.display.parts.Level;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by GrimmityGrammity on 11/16/2016.
 *
 * A Building is one of the three key components of PrizmJ.
 *
 * A Building contains a 2D/3D building structure, consisting of
 *  floors which, in turn, are comprised of rooms, hallways and stairways.
 *
 */
public class Blueprint {

    private PrizmJ prizmJ;

    // Array of all the floors //
    private Array<Level> floors;

    // Array of all models IN BUILDING. //
    private Array<RoomModel> models;

    public Blueprint(PrizmJ prizmJ, Level... floors) {
        this.prizmJ = prizmJ;
        this.floors = new Array<>(floors);
        this.models = new Array<>();
        updateModels();
    }

    public Array<Level> getFloors() {
        return floors;
    }

    public void updateModels() {
        if(floors != null && floors.size > 0)
            floors.forEach(lvl -> models.addAll(lvl.getAllRooms()));
    }

    public void addRoomPairToSpecificFloor(int level, String roomName, float x, float y, float z, float width, float height, Color roomColor) {
        RoomModel roomModel1 = new RoomModel(new BasicRoom(roomName + "_2d", x, y, z, width, height, roomColor), prizmJ.getModelBuilder(), 2);
        RoomModel roomModel2 = new RoomModel(new BasicRoom(roomName + "_3d", x, y, z, width, height, roomColor), prizmJ.getModelBuilder(), 3);
        roomModel1.setVisibility(roomModel1.getDimension() == prizmJ.getCurrentDimension());
        roomModel2.setVisibility(roomModel2.getDimension() == prizmJ.getCurrentDimension());
        Optional.ofNullable(getSpecificFloor(level)).ifPresent(lvl -> lvl.addAll(roomModel1, roomModel2));
    }

    public void addRoomPairToSpecificFloor(int level, String roomName, float x, float y, float z, float width, float height, Color roomColor, Door... doors) {
        BasicRoom room1 = new BasicRoom(roomName + "_2d", x, y, z, width, height, roomColor);
        BasicRoom room2 = new BasicRoom(roomName + "_3d", x, y, z, width, height, roomColor);
        RoomModel roomModel1 = new RoomModel(room1, prizmJ.getModelBuilder(), 2, doors);
        RoomModel roomModel2 = new RoomModel(room2, prizmJ.getModelBuilder(), 3, doors);
        if(doors != null && doors.length > 0) for(Door door : doors) door.setInitialRoom(roomModel2);
        roomModel1.setVisibility(roomModel1.getDimension() == prizmJ.getCurrentDimension());
        roomModel2.setVisibility(roomModel2.getDimension() == prizmJ.getCurrentDimension());
        Optional.ofNullable(getSpecificFloor(level)).ifPresent(lvl -> lvl.addAll(roomModel1, roomModel2));
    }

    // TODO change everything about this, and do positive and negative checks
    // Get this room and attach this room to it.
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


    // Attaches rooms together the same as above, however, it only moves the room along the axis of the
    // specified cardinal
    public void attachRoomByAxis(String existingRoom, String attachingRoom, Cardinal cardinal) throws Exception {
        RoomModel room2d_1 = getRoomModelByName(existingRoom + "_2d");
        RoomModel room2d_2 = getRoomModelByName(attachingRoom + "_2d");
        RoomModel room3d_1 = getRoomModelByName(existingRoom + "_3d");
        RoomModel room3d_2 = getRoomModelByName(attachingRoom + "_3d");
        if(room2d_1 != null && room2d_2 != null && room3d_1 != null && room3d_2 != null) {
            switch(cardinal) {
                case NORTH: // Wall 1
                    room3d_2.moveTo(room3d_2.getRoom().getX(), room3d_1.getRoom().getY(), ((room3d_1.getRoom().getZ() + (room3d_1.getRoom().getHeight() / 2)) + (room3d_2.getRoom().getHeight() / 2)) - (PrizmJ.WALL_THICKNESS / 2) - (PrizmJ.WALL_OFFSET /2));
                    room2d_2.moveTo(room2d_2.getRoom().getX(), room2d_1.getRoom().getY(), ((room2d_1.getRoom().getZ() + (room2d_1.getRoom().getHeight() / 2)) + (room2d_2.getRoom().getHeight() / 2)) - (PrizmJ.WALL_THICKNESS / 2) - (PrizmJ.WALL_OFFSET /2));
                    break;
                case SOUTH: // Wall 2
                    room3d_2.moveTo(room3d_2.getRoom().getX(), room3d_1.getRoom().getY(), ((room3d_1.getRoom().getZ() - (room3d_1.getRoom().getHeight() / 2)) - (room3d_2.getRoom().getHeight() / 2)) + (PrizmJ.WALL_THICKNESS / 2) + (PrizmJ.WALL_OFFSET /2));
                    room2d_2.moveTo(room2d_2.getRoom().getX(), room2d_1.getRoom().getY(), ((room2d_1.getRoom().getZ() - (room2d_1.getRoom().getHeight() / 2)) - (room2d_2.getRoom().getHeight() / 2)) + (PrizmJ.WALL_THICKNESS / 2) + (PrizmJ.WALL_OFFSET /2));
                    break;
                case EAST: // Wall 3
                    room3d_2.moveTo((((room3d_1.getRoom().getX() - (room3d_1.getRoom().getWidth() / 2)) - (room3d_2.getRoom().getWidth() / 2)) + (PrizmJ.WALL_THICKNESS / 2) + (PrizmJ.WALL_OFFSET /2)), room3d_1.getRoom().getY(), room3d_2.getRoom().getZ());
                    room2d_2.moveTo((((room2d_1.getRoom().getX() - (room2d_1.getRoom().getWidth() / 2)) - (room2d_2.getRoom().getWidth() / 2)) + (PrizmJ.WALL_THICKNESS / 2) + (PrizmJ.WALL_OFFSET /2)), room2d_1.getRoom().getY(), room2d_2.getRoom().getZ());
                    break;
                case WEST: // Wall 4
                    room3d_2.moveTo((((room3d_1.getRoom().getX() + (room3d_1.getRoom().getWidth() / 2)) + (room3d_2.getRoom().getWidth() / 2)) - (PrizmJ.WALL_THICKNESS / 2) - (PrizmJ.WALL_OFFSET /2)), room3d_1.getRoom().getY(), room3d_2.getRoom().getZ());
                    room2d_2.moveTo((((room2d_1.getRoom().getX() + (room2d_1.getRoom().getWidth() / 2)) + (room2d_2.getRoom().getWidth() / 2)) - (PrizmJ.WALL_THICKNESS / 2) - (PrizmJ.WALL_OFFSET /2)), room2d_1.getRoom().getY(), room2d_2.getRoom().getZ());
                    break;
            }
            // Attach a door, on the cardinal wall, from room2 to room1
            room3d_2.getDoors().forEach((door) -> {
                if (door.getCardinal() == cardinal && door.getSecondRoom() != null) {
                    System.out.println("Door:"+door.getFirstRoom());
                    door.setConnectedRoom(room3d_1);
                } else {
                    System.out.println("No doors found for room " + room3d_2.getRoom().getName());
                }
            });
        } else throw new Exception("Can't attach a room to a nonexistent room.");
    }


    public void attachRoomWithPrejudice(String existingRoom, String[] attachingRooms, Cardinal initial, Cardinal direction) throws Exception {
        RoomModel erm2D = getRoomModelByName(existingRoom + "_2d");
        RoomModel erm3D = getRoomModelByName(existingRoom + "_3d");
        if(erm2D != null && erm3D != null) {
            Array<RoomModel> rms = new Array<>();
            for (String str : attachingRooms) {
                rms.add(getRoomModelByName(str + "_2d"));
                rms.add(getRoomModelByName(str + "_3d"));
            }

            RoomModel irm2D = rms.get(0);
            RoomModel irm3D = rms.get(1);
            System.out.println("ByName:"+getRoomModelByName(existingRoom));
            System.out.println("RMS:"+rms.peek());
            // Attach the first room then move then use it to create a cascading effect.
            attachRoom(existingRoom, attachingRooms[0], initial);
            switch (initial) {
                case NORTH:
                    irm2D.moveTo(erm3D.getRoom().getX() + (erm3D.getRoom().getWidth() / 2) - (irm2D.getRoom().getWidth() / 2), erm3D.getRoom().getY(), erm3D.getRoom().getZ());
                    irm3D.moveTo(erm3D.getRoom().getX() + (erm3D.getRoom().getWidth() / 2) - (irm3D.getRoom().getWidth() / 2), erm3D.getRoom().getY(), erm3D.getRoom().getZ());
                    break;
                case SOUTH:
                    irm2D.moveTo(erm3D.getRoom().getX() - (erm3D.getRoom().getWidth() / 2) + (irm2D.getRoom().getWidth() / 2), erm3D.getRoom().getY(), erm3D.getRoom().getZ());
                    irm3D.moveTo(erm3D.getRoom().getX() - (erm3D.getRoom().getWidth() / 2) + (irm3D.getRoom().getWidth() / 2), erm3D.getRoom().getY(), erm3D.getRoom().getZ());
                    break;
                case EAST:
                    irm2D.moveTo(erm3D.getRoom().getX(), erm3D.getRoom().getY(), erm3D.getRoom().getZ() + (erm3D.getRoom().getWidth() / 2) - (irm2D.getRoom().getWidth() / 2));
                    irm3D.moveTo(erm3D.getRoom().getX(), erm3D.getRoom().getY(), erm3D.getRoom().getZ() + (erm3D.getRoom().getWidth() / 2) - (irm3D.getRoom().getWidth() / 2));
                    break;
                case WEST:
                    irm2D.moveTo(erm3D.getRoom().getX(), erm3D.getRoom().getY(), erm3D.getRoom().getZ() - (erm3D.getRoom().getWidth() / 2) + (irm2D.getRoom().getWidth() / 2));
                    irm3D.moveTo(erm3D.getRoom().getX(), erm3D.getRoom().getY(), erm3D.getRoom().getZ() - (erm3D.getRoom().getWidth() / 2) + (irm3D.getRoom().getWidth() / 2));
                    break;
            }
            rms.forEach(roomModel -> {
                if(!Objects.equals(roomModel.getRoom().getName(), attachingRooms[0] + "_3d")) {
                    try {
                        attachRoom(attachingRooms[0] + "_3d", roomModel.getRoom().getName(), direction);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (!Objects.equals(roomModel.getRoom().getName(), attachingRooms[0] + "_2d")) {
                    try {
                        attachRoom(attachingRooms[0] + "_2d", roomModel.getRoom().getName(), direction);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(roomModel.toString());
                roomModel.getDoors().forEach(door -> door.setConnectedRoom(erm3D));
            });
        }
    }

    public RoomModel getRoomModelByName(String name) {
        final RoomModel[] room = {null};
        models.forEach(roomModel -> {
            if(roomModel.getRoom().getName().compareTo(name) == 0) room[0] = roomModel;
        });
        return room[0];
    }

    private Level getSpecificFloor(int level) {
         return (floors.get(level) != null) ? floors.get(level) : null;
    }

    // Crazy method, ignore for now
    private void generateFloor(int numRooms, int minBuildingWidth, int maxBuildingWidth) {
        double sx,sy;

        double px = Math.ceil(Math.sqrt(numRooms * minBuildingWidth / maxBuildingWidth));
        if(Math.floor(px * maxBuildingWidth / minBuildingWidth) * px < numRooms)  //does not fit, y/(x/px)=px*y/x
            sx = maxBuildingWidth / Math.ceil(px * maxBuildingWidth / minBuildingWidth);
        else
            sx = minBuildingWidth / px;

        double py = Math.ceil(Math.sqrt(numRooms * maxBuildingWidth / minBuildingWidth));
        if(Math.floor(py * minBuildingWidth / maxBuildingWidth) * py < numRooms)  //does not fit
            sy = minBuildingWidth / Math.ceil(minBuildingWidth * py / maxBuildingWidth);
        else
            sy = minBuildingWidth / py;

        float num = (float) Math.max(sx,sy);
        Level floor = new Level(0);
        for(int x = 0; x < numRooms; x++) {
            RoomModel model = new RoomModel(new BasicRoom("room_" + x + "_", x, 0, 5, num, num, Color.RED), prizmJ.getModelBuilder(), 3);
            floor.addAll(model);
        }
        floors.add(floor);
    }

}
