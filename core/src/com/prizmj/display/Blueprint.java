package com.prizmj.display;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.prizmj.display.models.RoomModel;
import com.prizmj.display.parts.*;
import com.prizmj.display.simulation.GNM;
import com.prizmj.display.simulation.components.Vertex;

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

    private GNM geometricNetworkModel;

    public Blueprint(PrizmJ prizmJ, Level... floors) {
        this.prizmJ = prizmJ;
        this.floors = new Array<>(floors);
        this.models = new Array<>();
        this.geometricNetworkModel = new GNM(prizmJ, this);
        updateModels();
    }

    public Array<Level> getFloors() {
        return floors;
    }

    public void updateModels() {
        if(floors != null && floors.size > 0)
            floors.forEach(lvl -> models.addAll(lvl.getAllRooms()));
    }

    public void addRoomToSpecificFloor(int level, String roomName, float x, float y, float z, float width, float height, Color roomColor) {
        RoomModel roomModel = new RoomModel(new BasicRoom(roomName, x, y, z, width, height, roomColor), prizmJ.getModelBuilder());
        Optional.ofNullable(getSpecificFloor(level)).ifPresent(lvl -> lvl.addAll(roomModel));
        Vertex vertex = new Vertex(0, 0, 0, roomModel.getRoom());
        geometricNetworkModel.addVertex(vertex, new Vector3(0, 1.5f, 0));
    }

    @Deprecated
    public void addRoomToSpecificFloor(int level, String roomName, float x, float y, float z, float width, float height, Color roomColor, Door... doors) {
        RoomModel roomModel = new RoomModel(new BasicRoom(roomName, x, y, z, width, height, roomColor), prizmJ.getModelBuilder(), doors);
        if(doors != null && doors.length > 0) for(Door door : doors) door.setInitialRoom(roomModel);
        Optional.ofNullable(getSpecificFloor(level)).ifPresent(lvl -> lvl.addAll(roomModel));
    }

    public void addHallway(int level, String roomName, float x, float y, float z, float width, float height, Color roomColor, boolean updown) {
        RoomModel roomModel = new RoomModel(new Hallway(roomName, x, y, z, width, height, roomColor, updown), prizmJ.getModelBuilder());
        Optional.ofNullable(getSpecificFloor(level)).ifPresent(lvl -> lvl.addAll(roomModel));
    }

    public void addStairwell(int startingLevel, String stairwayName, float x, float y, float z) {
        RoomModel roomModel = new RoomModel(new Stairwell(stairwayName, x, y, z, PrizmJ.STAIRWELL_WIDTH, PrizmJ.STAIRWELL_HEIGHT, PrizmJ.STAIRWELL_COLOR), prizmJ.getModelBuilder());
        Optional.ofNullable(getSpecificFloor(startingLevel)).ifPresent(lvl -> lvl.addAll(roomModel));
    }

    public void addAndAttachStairwell(String downstairs, String stairwayName) {
        Stairwell well = new Stairwell(stairwayName, 0, 0, 0, PrizmJ.STAIRWELL_WIDTH, PrizmJ.STAIRWELL_HEIGHT, PrizmJ.STAIRWELL_COLOR);
        RoomModel roomModel = new RoomModel(well, prizmJ.getModelBuilder());
        Optional.ofNullable(getSpecificFloor(0)).ifPresent(lvl -> lvl.addAll(roomModel));
        if(downstairs != null) {
            well.setDownstairs(downstairs);
            RoomModel well2 = getRoomModelByName(downstairs);
            roomModel.moveTo(well2.getRoom().getX(), well2.getRoom().getY() + PrizmJ.WALL_HEIGHT, well2.getRoom().getZ());
        }
    }

    @Deprecated
    public void attachRoom(String existingRoom, String attachingRoom, Cardinal cardinal) throws Exception {
        RoomModel room1 = getRoomModelByName(existingRoom);
        RoomModel room2 = getRoomModelByName(attachingRoom);
        if(room1 != null && room2 != null) {
            switch(cardinal) {
                case NORTH: // Wall 1
                    room2.moveTo(room1.getRoom().getX(), room1.getRoom().getY(), ((room1.getRoom().getZ() + (room1.getRoom().getHeight() / 2)) + (room2.getRoom().getHeight() / 2)) + PrizmJ.WALL_THICKNESS);
                    break;
                case SOUTH: // Wall 2
                    room2.moveTo(room1.getRoom().getX(), room1.getRoom().getY(), ((room1.getRoom().getZ() - (room1.getRoom().getHeight() / 2)) - (room2.getRoom().getHeight() / 2)) - PrizmJ.WALL_THICKNESS);
                    break;
                case EAST: // Wall 3
                    room2.moveTo(((room1.getRoom().getX() - (room1.getRoom().getWidth() / 2)) - (room2.getRoom().getWidth() / 2)) - PrizmJ.WALL_THICKNESS, room1.getRoom().getY(), room1.getRoom().getZ());
                    break;
                case WEST: // Wall 4
                    room2.moveTo(((room1.getRoom().getX() + (room1.getRoom().getWidth() / 2)) + (room2.getRoom().getWidth() / 2)) + PrizmJ.WALL_THICKNESS, room1.getRoom().getY(), room1.getRoom().getZ());
                    break;
            }
        } else throw new Exception("Can't attach a room to a nonexistent room.");
    }

    /**
     * *Without the door execution process*, this method is faster.
     * Note, to use this method, both rooms must already have been created.
     * @param existingRoom - The initial room.
     * @param attachingRoom - The room that will attach to the initial room.
     * @param cardinal - The cardinal direction in which the attachingRoom will attach to the existing room.
     * @throws Exception - If the room doesn't exist in the blueprints room array. (remember to use updateModels())
     * Door mantra: Owned by B attaching to A
     */
    public void attachRoomByAxis(String existingRoom, String attachingRoom, Cardinal cardinal) throws Exception {
        RoomModel room1 = getRoomModelByName(existingRoom);
        RoomModel room2 = getRoomModelByName(attachingRoom);
        if(room1 != null && room2 != null) {
            switch(cardinal) {
                case NORTH: // Wall 1
                    room2.moveTo(room2.getRoom().getX(), room1.getRoom().getY(), ((room1.getRoom().getZ() + (room1.getRoom().getHeight() / 2)) + (room2.getRoom().getHeight() / 2)) - (PrizmJ.WALL_THICKNESS / 2) - (PrizmJ.WALL_OFFSET /2));
                    break;
                case SOUTH: // Wall 2
                    room2.moveTo(room2.getRoom().getX(), room1.getRoom().getY(), ((room1.getRoom().getZ() - (room1.getRoom().getHeight() / 2)) - (room2.getRoom().getHeight() / 2)) + (PrizmJ.WALL_THICKNESS / 2) + (PrizmJ.WALL_OFFSET /2));
                    break;
                case EAST: // Wall 3
                    room2.moveTo((((room1.getRoom().getX() - (room1.getRoom().getWidth() / 2)) - (room2.getRoom().getWidth() / 2)) + (PrizmJ.WALL_THICKNESS / 2) + (PrizmJ.WALL_OFFSET /2)), room1.getRoom().getY(), room2.getRoom().getZ());
                    break;
                case WEST: // Wall 4
                    room2.moveTo((((room1.getRoom().getX() + (room1.getRoom().getWidth() / 2)) + (room2.getRoom().getWidth() / 2)) - (PrizmJ.WALL_THICKNESS / 2) - (PrizmJ.WALL_OFFSET /2)), room1.getRoom().getY(), room2.getRoom().getZ());
                    break;
            }
            // Attach a door, on the cardinal wall, from room2 to room1
            /*room2.getDoors().forEach((door) -> {
                if (door.getCardinal() == cardinal && door.getSecondRoom() != null) {
                    door.setConnectedRoom(room1);
                } else {
                    System.out.println("No doors found for room " + room2.getRoom().getName());
                }
            });*/
            // IF you want to create a door in this process, you have to recreate the room. (updates the instance) It's actually easy:
            room2.recreateRoomByAttachment(prizmJ.getModelBuilder(), room1, new Door(Cardinal.getOpposite(cardinal)));
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
            // RoomModel model = new RoomModel(new BasicRoom("room_" + x + "_", x, 0, 5, num, num, Color.RED), prizmJ.getModelBuilder(), 3);
            // floor.addAll(model);
        }
        floors.add(floor);
    }

    public GNM getGeometricNetworkModel() {
        return geometricNetworkModel;
    }
}
