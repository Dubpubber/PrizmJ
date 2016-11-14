package com.prizmj.display;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
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

    private boolean toggle = true;

    public RoomModel(Room room, ModelBuilder builder, int dimension) {
        this.room = room;
        if(dimension == 2)
            create2DRoom(builder);
        else if(dimension == 3)
            create3DRoom(builder, 7);
    }

    public void render(ModelBatch modelBatch, Environment environment) {
        if(toggle) modelBatch.render(instance, environment);
    }

    public void create2DRoom(ModelBuilder builder) {
        model = builder.createRect(
                room.getP1().x, room.getP1().y, room.getP1().z,
                room.getP2().x, room.getP2().y, room.getP2().z,
                room.getP3().x, room.getP3().y, room.getP3().z,
                room.getP4().x, room.getP4().y, room.getP4().z,
                0, 1, 0, new Material(ColorAttribute.createDiffuse(room.getFloorColor())),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model, room.getX(), 0, room.getY());
    }

    public void create3DRoom(ModelBuilder builder, float wallWidth) {
        builder.begin();
        builder.node();
        MeshPartBuilder mpb = builder.part("basic_room", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(room.getFloorColor())));
        // BoxShapeBuilder.build(mpb, x1, y1, z1, width1, height1, depth1);
        // Create the floor of the room.
        BoxShapeBuilder.build(mpb, room.getP1().x, room.getP1().y, room.getP1().z, room.getWidth(), 0.25f, room.getHeight());
        // Wall coming from p1
        BoxShapeBuilder.build(mpb, room.getP1().x, wallWidth / 2, room.getP1().z + (room.getWidth() / 2) + 0.25f, room.getWidth(), wallWidth, 0.25f);
        model = builder.end();
        instance = new ModelInstance(model, (-room.getWidth() / 2) - 0.55f, 0, (room.getHeight() / 2) + 0.55f);
    }

}
