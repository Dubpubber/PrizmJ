/*
 * Copyright (c) 2016 Tyler Crowe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.prizmj.display.models.buildings;/*
 * com.prizmj.display.models.buildings.BasicRoom in PrizmJPortable
 * Created by Tyler Crowe on 12/11/2016.
 * Website: https://loneboat.com
 * GitHub: https://github.com/Dubpubber
 * Machine Time: 3:25 AM
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.prizmj.display.PrizmJ;
import com.prizmj.display.models.GenericModel;
import com.prizmj.display.models.buildings.profiles.RoomProfile;

import java.util.UUID;

public class BasicRoom extends GenericModel {
    
    private RoomProfile roomProfileProfile;

    /**
     * Constructor for creating a new GenericModel.
     *
     * @param prizmJ           - The prizmj object that will be used temporarily for getting the model builder.
     * @param genericModelName - The name of the object builder (unique)
     */
    public BasicRoom(PrizmJ prizmJ, String genericModelName, RoomProfile roomProfileProfile) {
        super(prizmJ, genericModelName);
        this.roomProfileProfile = roomProfileProfile;
        changeColor(roomProfileProfile.getFloorColor());
        translateBy(roomProfileProfile.getWorldyLocation());
        create3DModel();
    }

    @Override
    public void render(ModelBatch batch, Environment environment) {
        batch.render(get3DModelInstance(), environment);
    }

    @Override
    public void create2DModel() {}

    @Override
    public void create3DModel() {
        getModelBuilder().begin();
        MeshPartBuilder mpb = getModelBuilder().part(UUID.randomUUID().toString(), GL20.GL_TRIANGLES, VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position, new Material(ColorAttribute.createDiffuse(Color.RED)));
        // BoxShapeBuilder.build(mpb, x1, y1, z1, width1, height1, depth1);
        // Create the floor of the roomProfile.
        BoxShapeBuilder.build(mpb, 0, 0, 0, roomProfileProfile.getWidth(), 0.25f, roomProfileProfile.getHeight());
        if(roomProfileProfile.getHeight() % 2 == 0) {
            // WALL 1
            BoxShapeBuilder.build(mpb, 0, RoomConstants.WALL_HEIGHT / 2, ((roomProfileProfile.getHeight() / 2) - RoomConstants.WALL_THICKNESS) + RoomConstants.WALL_OFFSET, roomProfileProfile.getWidth(), RoomConstants.WALL_HEIGHT, RoomConstants.WALL_THICKNESS);
            // WALL 2
            BoxShapeBuilder.build(mpb, 0, RoomConstants.WALL_HEIGHT / 2, ((-roomProfileProfile.getHeight() / 2) + RoomConstants.WALL_THICKNESS) - RoomConstants.WALL_OFFSET, roomProfileProfile.getWidth(), RoomConstants.WALL_HEIGHT, RoomConstants.WALL_THICKNESS);
        } else {
            // WALL 1
            BoxShapeBuilder.build(mpb, 0, RoomConstants.WALL_HEIGHT / 2, ((roomProfileProfile.getHeight() / 2) - RoomConstants.WALL_THICKNESS) + RoomConstants.WALL_OFFSET, roomProfileProfile.getWidth(), RoomConstants.WALL_HEIGHT, RoomConstants.WALL_THICKNESS);
            // WALL 2
            BoxShapeBuilder.build(mpb, 0, RoomConstants.WALL_HEIGHT / 2, ((-roomProfileProfile.getHeight() / 2) + RoomConstants.WALL_THICKNESS) - RoomConstants.WALL_OFFSET, roomProfileProfile.getWidth(), RoomConstants.WALL_HEIGHT, RoomConstants.WALL_THICKNESS);
        }
        if(roomProfileProfile.getWidth() % 2 == 0) {
            // WALL 3
            BoxShapeBuilder.build(mpb, ((-roomProfileProfile.getWidth() / 2) + RoomConstants.WALL_THICKNESS) - RoomConstants.WALL_OFFSET, RoomConstants.WALL_HEIGHT / 2, 0, RoomConstants.WALL_THICKNESS, RoomConstants.WALL_HEIGHT, roomProfileProfile.getHeight());
            // WALL 4
            BoxShapeBuilder.build(mpb, ((roomProfileProfile.getWidth() / 2) - RoomConstants.WALL_THICKNESS) + RoomConstants.WALL_OFFSET, RoomConstants.WALL_HEIGHT / 2, 0, RoomConstants.WALL_THICKNESS, RoomConstants.WALL_HEIGHT, roomProfileProfile.getHeight());
        } else {
            // WALL 3
            BoxShapeBuilder.build(mpb, ((-roomProfileProfile.getWidth() / 2) + RoomConstants.WALL_THICKNESS) - RoomConstants.WALL_OFFSET, RoomConstants.WALL_HEIGHT / 2, 0, RoomConstants.WALL_THICKNESS, RoomConstants.WALL_HEIGHT, roomProfileProfile.getHeight());
            // WALL 4
            BoxShapeBuilder.build(mpb, ((roomProfileProfile.getWidth() / 2) - RoomConstants.WALL_THICKNESS) + RoomConstants.WALL_OFFSET, RoomConstants.WALL_HEIGHT / 2, 0, RoomConstants.WALL_THICKNESS, RoomConstants.WALL_HEIGHT, roomProfileProfile.getHeight());
        }
        set3DModel(getModelBuilder().end());
    }

    public RoomProfile getRoomProfileProfile() {
        return roomProfileProfile;
    }
}
