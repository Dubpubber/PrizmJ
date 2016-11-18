package com.prizmj.display.models;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.prizmj.display.PrizmJ;
import com.prizmj.display.parts.Door;
import com.prizmj.display.parts.Room;

/**
 * com.prizmj.display.models in PrizmJ
 */
public class RoomModel {

    private Model model;
    private ModelInstance instance;
    private Room room;

    private int dimension;
    private boolean toggle = true;

    private Array<Door> doors;

    public RoomModel(Room room, ModelBuilder builder, int dimension) {
        this.room = room;
        this.dimension = dimension;
        this.doors = new Array<>();
        if(dimension == 2)
            create2DRoom(builder);
        else
            create3DRoom(builder);
    }

    public RoomModel(Room room, ModelBuilder builder, int dimension, Door... doors) {
        this.room = room;
        this.dimension = dimension;
        this.doors = new Array<>();
        if(dimension == 2)
            create2DRoom(builder);
        else
            create3DRoom(builder, doors);
    }

    public void render(ModelBatch modelBatch, Environment environment) {
        if(toggle) modelBatch.render(instance, environment);
    }

    public void create2DRoom(ModelBuilder builder) {
        builder.begin();
        builder.node();
        MeshPartBuilder mpb = builder.part("basic_room", GL20.GL_TRIANGLES, VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position, new Material(ColorAttribute.createDiffuse(room.getFloorColor())));
        BoxShapeBuilder.build(mpb, room.getWidth(), 0.001f, room.getHeight());
        model = builder.end();
        instance = new ModelInstance(model, room.getX(), room.getY(), room.getZ());
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
            BoxShapeBuilder.build(mpb, 0, PrizmJ.WALL_HEIGHT / 2, ((room.getHeight() / 2) + PrizmJ.WALL_THICKNESS) + PrizmJ.WALL_OFFSET, room.getWidth(), PrizmJ.WALL_HEIGHT, PrizmJ.WALL_THICKNESS);
            // WALL 2
            BoxShapeBuilder.build(mpb, 0, PrizmJ.WALL_HEIGHT / 2, ((-room.getHeight() / 2) - PrizmJ.WALL_THICKNESS) - PrizmJ.WALL_OFFSET, room.getWidth(), PrizmJ.WALL_HEIGHT, PrizmJ.WALL_THICKNESS);
        }
        if(room.getWidth() % 2 == 0) {
            // WALL 3
            BoxShapeBuilder.build(mpb, ((-room.getWidth() / 2) + PrizmJ.WALL_THICKNESS) - PrizmJ.WALL_OFFSET, PrizmJ.WALL_HEIGHT / 2, 0, PrizmJ.WALL_THICKNESS, PrizmJ.WALL_HEIGHT, room.getHeight());
            // WALL 4
            BoxShapeBuilder.build(mpb, ((room.getWidth() / 2) - PrizmJ.WALL_THICKNESS) + PrizmJ.WALL_OFFSET, PrizmJ.WALL_HEIGHT / 2, 0, PrizmJ.WALL_THICKNESS, PrizmJ.WALL_HEIGHT, room.getHeight());
        } else {
            // WALL 3
            BoxShapeBuilder.build(mpb, ((-room.getWidth() / 2) - PrizmJ.WALL_THICKNESS) - PrizmJ.WALL_OFFSET, PrizmJ.WALL_HEIGHT / 2, 0, PrizmJ.WALL_THICKNESS, PrizmJ.WALL_HEIGHT, room.getHeight());
            // WALL 4
            BoxShapeBuilder.build(mpb, ((room.getWidth() / 2) + PrizmJ.WALL_THICKNESS) + PrizmJ.WALL_OFFSET, PrizmJ.WALL_HEIGHT / 2, 0, PrizmJ.WALL_THICKNESS, PrizmJ.WALL_HEIGHT, room.getHeight());
        }
        if(doors.length > 0)
            for(Door door : doors) createDoor(builder, door);
        model = builder.end();
        instance = new ModelInstance(model);
        Node node = model.nodes.first();
        node.globalTransform.translate(room.getX(), room.getY(), room.getZ());
        instance.transform.set(node.globalTransform);
        instance.calculateTransforms();
    }

    private void createDoor(ModelBuilder builder, Door door) {
        int side = door.getSide();
        MeshPartBuilder mpb = builder.part("basic_room_room", GL20.GL_TRIANGLES, VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position, new Material(ColorAttribute.createDiffuse(PrizmJ.DOOR_COLOR)));
        if(side < 1 || side > 4) side = MathUtils.random(1, 4);
        if(side == 1 && room.getWidth() % 2 != 0) side = 5;
        else if(side == 2 && room.getWidth() % 2 != 0) side = 6;
        else if(side == 3 && room.getHeight() % 2 != 0) side = 7;
        else if(side == 4 && room.getHeight() % 2 != 0) side = 8;
        float x = 0;
        float y = 0;
        float z = 0;
        switch(side) {
            case 1: // Height case
                x = MathUtils.random(-room.getHeight() / 2, room.getHeight() / 2);
                y = (PrizmJ.WALL_HEIGHT / 2) - (PrizmJ.DOOR_HEIGHT / 2);
                z = ((room.getHeight() / 2) - PrizmJ.WALL_THICKNESS) + PrizmJ.WALL_OFFSET;
                // Wall 1 - Even
                BoxShapeBuilder.build(mpb,
                        x, y, z,
                        PrizmJ.DOOR_DEPTH,
                        PrizmJ.DOOR_HEIGHT,
                        PrizmJ.WALL_THICKNESS + 0.25f
                );
                break;
            case 2: // Height case
                x = MathUtils.random(-room.getHeight() / 2, room.getHeight() / 2);
                y = (PrizmJ.WALL_HEIGHT / 2) - (PrizmJ.DOOR_HEIGHT / 2);
                z = ((-room.getHeight() / 2) + PrizmJ.WALL_THICKNESS) - PrizmJ.WALL_OFFSET;
                // Wall 2 - Even
                BoxShapeBuilder.build(mpb,
                        x, y, z,
                        PrizmJ.DOOR_DEPTH,
                        PrizmJ.DOOR_HEIGHT,
                        PrizmJ.WALL_THICKNESS + 0.25f
                );
                break;
            case 3: // Width case
                x = ((-room.getWidth() / 2) + PrizmJ.WALL_THICKNESS) - PrizmJ.WALL_OFFSET;
                y = (PrizmJ.WALL_HEIGHT / 2) - (PrizmJ.DOOR_HEIGHT / 2);
                z = MathUtils.random(-room.getWidth() / 2, room.getWidth() / 2);
                // Wall 3 - Even
                BoxShapeBuilder.build(mpb,
                        x, y, z,
                        PrizmJ.WALL_THICKNESS + 0.25f,
                        PrizmJ.DOOR_HEIGHT,
                        PrizmJ.DOOR_DEPTH
                );
                break;
            case 4: // Width case
                x = ((room.getWidth() / 2) - PrizmJ.WALL_THICKNESS) + PrizmJ.WALL_OFFSET;
                y = (PrizmJ.WALL_HEIGHT / 2) - (PrizmJ.DOOR_HEIGHT / 2);
                z = MathUtils.random(-room.getWidth() / 2, room.getWidth() / 2);
                // Wall 4 - Even
                BoxShapeBuilder.build(mpb,
                        x, y, z,
                        PrizmJ.WALL_THICKNESS + 0.25f,
                        PrizmJ.DOOR_HEIGHT,
                        PrizmJ.DOOR_DEPTH
                );
                break;
            case 5: // Height case
                x = MathUtils.random(-room.getHeight() / 2, room.getHeight() / 2);
                y = (PrizmJ.WALL_HEIGHT / 2) - (PrizmJ.DOOR_HEIGHT / 2);
                z = ((room.getHeight() / 2) + PrizmJ.WALL_THICKNESS) + PrizmJ.WALL_OFFSET;
                // Wall 1 - Odd
                BoxShapeBuilder.build(mpb,
                        x, y, z,
                        PrizmJ.DOOR_DEPTH,
                        PrizmJ.DOOR_HEIGHT,
                        PrizmJ.WALL_THICKNESS + 0.25f
                );
                break;
            case 6: // Height case
                x = MathUtils.random(-room.getHeight() / 2, room.getHeight() / 2);
                y = (PrizmJ.WALL_HEIGHT / 2) - (PrizmJ.DOOR_HEIGHT / 2);
                z = ((-room.getHeight() / 2) - PrizmJ.WALL_THICKNESS) - PrizmJ.WALL_OFFSET;
                // Wall 2 - Odd
                BoxShapeBuilder.build(mpb,
                        x, y, z,
                        PrizmJ.DOOR_DEPTH,
                        PrizmJ.DOOR_HEIGHT,
                        PrizmJ.WALL_THICKNESS + 0.25f
                );
                break;
            case 7: // Width case
                x = ((-room.getWidth() / 2) - PrizmJ.WALL_THICKNESS) - PrizmJ.WALL_OFFSET;
                y = (PrizmJ.WALL_HEIGHT / 2) - (PrizmJ.DOOR_HEIGHT / 2);
                z = MathUtils.random(-room.getWidth() / 2, room.getWidth() / 2);
                // Wall 3 - Odd
                BoxShapeBuilder.build(mpb,
                        x, y, z,
                        PrizmJ.WALL_THICKNESS + 0.25f,
                        PrizmJ.DOOR_HEIGHT,
                        PrizmJ.DOOR_DEPTH
                );
                break;
            case 8: // Width case
                x = ((room.getWidth() / 2) + PrizmJ.WALL_THICKNESS) + PrizmJ.WALL_OFFSET;
                y = (PrizmJ.WALL_HEIGHT / 2) - (PrizmJ.DOOR_HEIGHT / 2);
                z = MathUtils.random(-room.getWidth() / 2, room.getWidth() / 2);
                // Wall 4 - Odd
                BoxShapeBuilder.build(mpb,
                        x, y, z,
                        PrizmJ.WALL_THICKNESS + 0.25f,
                        PrizmJ.DOOR_HEIGHT,
                        PrizmJ.DOOR_DEPTH)
                ;
                break;
        }
        door.updatePosition(x, y, z);
        doors.add(door);
    }

    public void moveTo(float x, float y, float z) {
        room.updatePosition(x, y, z);
        Node node = model.nodes.first();
        node.globalTransform.translate(room.getX(), room.getY(), room.getZ());
        instance.transform.set(node.globalTransform);
    }

    public Array<Door> getDoors() {
        return doors;
    }

    public Room getRoom() {
        return room;
    }

    public int getDimension() {
        return dimension;
    }

    public void setVisibility(boolean visible) {
        this.toggle = visible;
    }

    public boolean isVisible() {
        return toggle;
    }
}
