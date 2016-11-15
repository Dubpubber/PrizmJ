package com.prizmj.display;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.prizmj.display.buildingparts.Room;

/**
 * com.prizmj.display.Scene in PrizmJ
 */
public class RoomModel {

    private Model model;
    private ModelInstance instance;
    private Room room;

    private int dimension;
    private boolean toggle = true;

    public RoomModel(Room room, ModelBuilder builder, int dimension) {
        this.room = room;
        this.dimension = dimension;
        if(dimension == 2)
            create2DRoom(builder);
        else
            create3DRoom(builder);
    }

    public void render(ModelBatch modelBatch, Environment environment) {
        if(toggle) modelBatch.render(instance, environment);
    }

    public void create2DRoom(ModelBuilder builder) {
        /*model = builder.createRect(
                room.getP1().x, room.getP1().y, room.getP1().z,
                room.getP2().x, room.getP2().y, room.getP2().z,
                room.getP3().x, room.getP3().y, room.getP3().z,
                room.getP4().x, room.getP4().y, room.getP4().z,
                0, 1, 0, new Material(ColorAttribute.createDiffuse(room.getFloorColor())),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model, room.getX(), 0, room.getY());*/
        builder.begin();
        builder.node();
        MeshPartBuilder mpb = builder.part("basic_room", GL20.GL_TRIANGLES, VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position, new Material(ColorAttribute.createDiffuse(room.getFloorColor())));
        BoxShapeBuilder.build(mpb, room.getWidth(), 0.001f, room.getHeight());
        model = builder.end();
        instance = new ModelInstance(model, room.getX(), room.getY(), room.getZ());
    }

    public void create3DRoom(ModelBuilder builder) {
        builder.begin();
        MeshPartBuilder mpb = builder.part("basic_room", GL20.GL_TRIANGLES, VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position, new Material(ColorAttribute.createDiffuse(room.getFloorColor())));
        // BoxShapeBuilder.build(mpb, x1, y1, z1, width1, height1, depth1);
        // Create the floor of the room.
        BoxShapeBuilder.build(mpb, 0, 0, 0, room.getWidth(), 0.25f, room.getHeight());
        if(room.getHeight() % 2 == 0) {
            // Wall coming from p1
            BoxShapeBuilder.build(mpb, 0, PrizmJ.WALL_HEIGHT / 2, ((room.getHeight() / 2) - PrizmJ.WALL_THICKNESS) + PrizmJ.WALL_OFFSET, room.getWidth(), PrizmJ.WALL_HEIGHT, PrizmJ.WALL_THICKNESS);
            // Wall coming from p4
            BoxShapeBuilder.build(mpb, 0, PrizmJ.WALL_HEIGHT / 2, ((-room.getHeight() / 2) + PrizmJ.WALL_THICKNESS) - PrizmJ.WALL_OFFSET, room.getWidth(), PrizmJ.WALL_HEIGHT, PrizmJ.WALL_THICKNESS);
        } else {
            // Wall coming from p1
            BoxShapeBuilder.build(mpb, 0, PrizmJ.WALL_HEIGHT / 2, ((room.getHeight() / 2) + PrizmJ.WALL_THICKNESS) + PrizmJ.WALL_OFFSET, room.getWidth(), PrizmJ.WALL_HEIGHT, PrizmJ.WALL_THICKNESS);
            // Wall coming from p4
            BoxShapeBuilder.build(mpb, 0, PrizmJ.WALL_HEIGHT / 2, ((-room.getHeight() / 2) - PrizmJ.WALL_THICKNESS) - PrizmJ.WALL_OFFSET, room.getWidth(), PrizmJ.WALL_HEIGHT, PrizmJ.WALL_THICKNESS);
        }
        if(room.getWidth() % 2 == 0) {
            // Wall coming from p2
            BoxShapeBuilder.build(mpb, ((-room.getWidth() / 2) + PrizmJ.WALL_THICKNESS) - PrizmJ.WALL_OFFSET, PrizmJ.WALL_HEIGHT / 2, 0, PrizmJ.WALL_THICKNESS, PrizmJ.WALL_HEIGHT, room.getHeight());
            // Wall coming from p3
            BoxShapeBuilder.build(mpb, ((room.getWidth() / 2) - PrizmJ.WALL_THICKNESS) + PrizmJ.WALL_OFFSET, PrizmJ.WALL_HEIGHT / 2, 0, PrizmJ.WALL_THICKNESS, PrizmJ.WALL_HEIGHT, room.getHeight());
        } else {
            // Wall coming from p2
            BoxShapeBuilder.build(mpb, ((-room.getWidth() / 2) - PrizmJ.WALL_THICKNESS) - PrizmJ.WALL_OFFSET, PrizmJ.WALL_HEIGHT / 2, 0, PrizmJ.WALL_THICKNESS, PrizmJ.WALL_HEIGHT, room.getHeight());
            // Wall coming from p3
            BoxShapeBuilder.build(mpb, ((room.getWidth() / 2) + PrizmJ.WALL_THICKNESS) + PrizmJ.WALL_OFFSET, PrizmJ.WALL_HEIGHT / 2, 0, PrizmJ.WALL_THICKNESS, PrizmJ.WALL_HEIGHT, room.getHeight());
        }
        model = builder.end();
        instance = new ModelInstance(model);
        System.out.println(model.nodes.size);
        Node node = model.nodes.first();
        node.globalTransform.translate(room.getX(), room.getY(), room.getZ());
        instance.transform.set(node.globalTransform);
        instance.calculateTransforms();
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
