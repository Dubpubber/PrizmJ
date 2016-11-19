package com.prizmj.display.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.prizmj.display.Dimension;
import com.prizmj.display.PrizmJ;
import com.prizmj.display.parts.Door;
import com.prizmj.display.parts.abstracts.Room;

/**
 * com.prizmj.display.models in PrizmJ
 */
public class RoomModel {

    private Model model_2d;
    private Model model_3d;
    private ModelInstance instance_2d;
    private ModelInstance instance_3d;
    private Room room;

    private Dimension view = Dimension.Environment_3D;

    private Array<Door> doors;

    /* Simulation objects */
    private Model smokeCube;
    private ModelInstance smokeCubeInstance;
    private boolean showingSmoke = true;
    private float smokeDensity = 0;
    private boolean simulationRunning = false;

    public RoomModel(Room room, ModelBuilder builder) {
        this.room = room;
        this.doors = new Array<>();
        create2DRoom(builder);
        create3DRoom(builder);
        createSmokeCube(builder, .5f);
    }

    public RoomModel(Room room, ModelBuilder builder, Door... doors) {
        this.room = room;
        this.doors = new Array<>();
        create2DRoom(builder);
        create3DRoom(builder, doors);
        createSmokeCube(builder, .5f);
    }

    public void render(ModelBatch modelBatch, Environment environment) {
        if(view == Dimension.Environment_3D) {
            modelBatch.render(instance_3d, environment);
            if(showingSmoke) modelBatch.render(smokeCubeInstance, environment);
        } else
            modelBatch.render(instance_2d, environment);
    }

    public void create2DRoom(ModelBuilder builder) {
        builder.begin();
        builder.node();
        MeshPartBuilder mpb = builder.part("basic_room", GL20.GL_TRIANGLES, VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position, new Material(ColorAttribute.createDiffuse(room.getFloorColor())));
        BoxShapeBuilder.build(mpb, room.getWidth(), 0.001f, room.getHeight());
        model_2d = builder.end();
        instance_2d = new ModelInstance(model_2d, room.getX(), room.getY(), room.getZ());
    }

    public void create3DRoom(ModelBuilder builder, Door... doors) {
        builder.begin();
        MeshPartBuilder mpb = builder.part("basic_room", GL20.GL_TRIANGLES, VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position, new Material(ColorAttribute.createDiffuse(room.getFloorColor())));
        // BoxShapeBuilder.build(mpb, x1, y1, z1, width1, height1, depth1);
        // Create the floor of the room.
        BoxShapeBuilder.build(mpb, 0, 0, 0, room.getWidth(), 0.25f, room.getHeight());
        if(room.getHeight() % 2 == 0) {
            // WALL 1
            BoxShapeBuilder.build(mpb, 0, PrizmJ.WALL_HEIGHT / 2, ((room.getHeight() / 2) - PrizmJ.WALL_THICKNESS) + PrizmJ.WALL_OFFSET, room.getWidth(), PrizmJ.WALL_HEIGHT, PrizmJ.WALL_THICKNESS);
            // WALL 2
            BoxShapeBuilder.build(mpb, 0, PrizmJ.WALL_HEIGHT / 2, ((-room.getHeight() / 2) + PrizmJ.WALL_THICKNESS) - PrizmJ.WALL_OFFSET, room.getWidth(), PrizmJ.WALL_HEIGHT, PrizmJ.WALL_THICKNESS);
        } else {
            // WALL 1
            BoxShapeBuilder.build(mpb, 0, PrizmJ.WALL_HEIGHT / 2, ((room.getHeight() / 2) - PrizmJ.WALL_THICKNESS) + PrizmJ.WALL_OFFSET, room.getWidth(), PrizmJ.WALL_HEIGHT, PrizmJ.WALL_THICKNESS);
            // WALL 2
            BoxShapeBuilder.build(mpb, 0, PrizmJ.WALL_HEIGHT / 2, ((-room.getHeight() / 2) + PrizmJ.WALL_THICKNESS) - PrizmJ.WALL_OFFSET, room.getWidth(), PrizmJ.WALL_HEIGHT, PrizmJ.WALL_THICKNESS);
        }
        if(room.getWidth() % 2 == 0) {
            // WALL 3
            BoxShapeBuilder.build(mpb, ((-room.getWidth() / 2) + PrizmJ.WALL_THICKNESS) - PrizmJ.WALL_OFFSET, PrizmJ.WALL_HEIGHT / 2, 0, PrizmJ.WALL_THICKNESS, PrizmJ.WALL_HEIGHT, room.getHeight());
            // WALL 4
            BoxShapeBuilder.build(mpb, ((room.getWidth() / 2) - PrizmJ.WALL_THICKNESS) + PrizmJ.WALL_OFFSET, PrizmJ.WALL_HEIGHT / 2, 0, PrizmJ.WALL_THICKNESS, PrizmJ.WALL_HEIGHT, room.getHeight());
        } else {
            // WALL 3
            BoxShapeBuilder.build(mpb, ((-room.getWidth() / 2) + PrizmJ.WALL_THICKNESS) - PrizmJ.WALL_OFFSET, PrizmJ.WALL_HEIGHT / 2, 0, PrizmJ.WALL_THICKNESS, PrizmJ.WALL_HEIGHT, room.getHeight());
            // WALL 4
            BoxShapeBuilder.build(mpb, ((room.getWidth() / 2) - PrizmJ.WALL_THICKNESS) + PrizmJ.WALL_OFFSET, PrizmJ.WALL_HEIGHT / 2, 0, PrizmJ.WALL_THICKNESS, PrizmJ.WALL_HEIGHT, room.getHeight());
        }
        if(doors.length > 0)
            for(Door door : doors) createDoor(builder, door);
        model_3d = builder.end();
        instance_3d = new ModelInstance(model_3d);
        Node node = model_3d.nodes.first();
        node.globalTransform.translate(room.getX(), room.getY(), room.getZ());
        instance_3d.transform.set(node.globalTransform);
        instance_3d.calculateTransforms();
    }

    private void createDoor(ModelBuilder builder, Door door) {
        int side = door.getSide();
        MeshPartBuilder mpb = builder.part("basic_room", GL20.GL_TRIANGLES, VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position, new Material(ColorAttribute.createDiffuse(PrizmJ.DOOR_COLOR)));
        if(side < 1 || side > 4) side = MathUtils.random(1, 4);
        float x = 0;
        float y = 0;
        float z = 0;
        switch(side) {
            case 1: // Height case
                x = MathUtils.random((-room.getHeight() / 2) + (PrizmJ.DOOR_WIDTH / 2), (room.getHeight() / 2) - (PrizmJ.DOOR_HEIGHT / 2));
                y = (PrizmJ.WALL_HEIGHT / 2) - (PrizmJ.DOOR_HEIGHT / 2);
                z = ((room.getHeight() / 2) - PrizmJ.WALL_THICKNESS) + PrizmJ.WALL_OFFSET;
                // Wall 1
                BoxShapeBuilder.build(mpb,
                        x, y, z,
                        PrizmJ.DOOR_WIDTH,
                        PrizmJ.DOOR_HEIGHT,
                        PrizmJ.WALL_THICKNESS + 0.25f
                );
                break;
            case 2: // Height case
                x = MathUtils.random(-room.getHeight() / 2, room.getHeight() / 2);
                y = (PrizmJ.WALL_HEIGHT / 2) - (PrizmJ.DOOR_HEIGHT / 2);
                z = ((-room.getHeight() / 2) + PrizmJ.WALL_THICKNESS) - PrizmJ.WALL_OFFSET;
                // Wall 2
                BoxShapeBuilder.build(mpb,
                        x, y, z,
                        PrizmJ.DOOR_WIDTH,
                        PrizmJ.DOOR_HEIGHT,
                        PrizmJ.WALL_THICKNESS + 0.25f
                );
                break;
            case 3: // Width case
                x = ((-room.getWidth() / 2) + PrizmJ.WALL_THICKNESS) - PrizmJ.WALL_OFFSET;
                y = (PrizmJ.WALL_HEIGHT / 2) - (PrizmJ.DOOR_HEIGHT / 2);
                z = MathUtils.random(-room.getWidth() / 2, room.getWidth() / 2);
                // Wall 3
                BoxShapeBuilder.build(mpb,
                        x, y, z,
                        PrizmJ.WALL_THICKNESS + 0.25f,
                        PrizmJ.DOOR_HEIGHT,
                        PrizmJ.DOOR_WIDTH
                );
                break;
            case 4: // Width case
                x = ((room.getWidth() / 2) - PrizmJ.WALL_THICKNESS) + PrizmJ.WALL_OFFSET;
                y = (PrizmJ.WALL_HEIGHT / 2) - (PrizmJ.DOOR_HEIGHT / 2);
                z = MathUtils.random(-room.getWidth() / 2, room.getWidth() / 2);
                // Wall 4
                BoxShapeBuilder.build(mpb,
                        x, y, z,
                        PrizmJ.WALL_THICKNESS + 0.25f,
                        PrizmJ.DOOR_HEIGHT,
                        PrizmJ.DOOR_WIDTH
                );
                break;
        }
        door.updatePosition(x, y, z);
    }

    public void createSmokeCube(ModelBuilder builder, float smokeDensity) {
        Material mat = new Material();
        mat.set(ColorAttribute.createDiffuse(new Color(0.498f, 0.498f, 0.498f, 1)));
        mat.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, smokeDensity));
        this.smokeCube = builder.createBox(
                room.getWidth(),
                PrizmJ.WALL_HEIGHT,
                (room.getWidth() + room.getHeight()) / 2,
                mat,
                VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position
        );
        this.smokeCubeInstance = new ModelInstance(smokeCube);
        Node node = smokeCube.nodes.first();
        node.globalTransform.translate(room.getX(), room.getY() + (PrizmJ.WALL_HEIGHT / 2), room.getZ());
        smokeCubeInstance.transform.set(node.globalTransform);
        this.smokeDensity = smokeDensity;
    }

    public void moveTo(float x, float y, float z) {
        Node node = null;
        room.updatePosition(x, y, z);
        // Move 3d model first //
        node = model_3d.nodes.first();
        node.globalTransform.translate(room.getX(), room.getY(), room.getZ());
        instance_3d.transform.set(node.globalTransform);
        // Then move 2d model
        node = model_2d.nodes.first();
        node.globalTransform.translate(room.getX(), room.getY(), room.getZ());
        instance_2d.transform.set(node.globalTransform);
        // Finally, move the smoke cube
        node = smokeCube.nodes.first();
        node.globalTransform.translate(room.getX(), room.getY(), room.getZ());
        smokeCubeInstance.transform.set(node.globalTransform);
    }

    public void recreateRoom(ModelBuilder builder, Door... doors) {
        create2DRoom(builder);
        create3DRoom(builder, doors);
    }

    public void recreateRoomByAttachment(ModelBuilder builder, RoomModel attachingRoom, Door... allDoors) {
        create2DRoom(builder);
        create3DRoom(builder, allDoors);
        if(allDoors != null && allDoors.length > 0) for (Door door : allDoors) {
            door.setConnectedRoom(attachingRoom);
            door.setInitialRoom(this);
            // Your fault fag
            //attachingRoom.getDoors().add(door);
            doors.add(door);
        }
    }

    public void recreateSmokeCube(ModelBuilder builder, float smokeDensity) {
        createSmokeCube(builder, smokeDensity);
    }

    public void startSmokeSimulation(ModelBuilder builder, float apexPoint, float smokeSpeed, float repeatSpeed) {
        this.simulationRunning = true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                recreateSmokeCube(builder, getSmokeDensity() + smokeSpeed);
                if(getSmokeDensity() > apexPoint) {
                    simulationRunning = false;
                    System.out.println("Finished simulating smoke.");
                    cancel();
                }
            }
        }, 0, repeatSpeed);
    }

    public Array<Door> getDoors() {
        return doors;
    }

    public Room getRoom() {
        return room;
    }

    public void setDimensionView(Dimension dimensionView) {
        this.view = dimensionView;
    }

    public Dimension getDimensionView() {
        return view;
    }

    public float getSmokeDensity() {
        return smokeDensity;
    }

    public boolean isShowingSmoke() {
        return showingSmoke;
    }

    public void setShowingSmoke(boolean showingSmoke) {
        this.showingSmoke = showingSmoke;
    }

    public boolean isSimulationRunning() {
        return simulationRunning;
    }
}
