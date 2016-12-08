/*
 * Copyright (c) 2016 Tyler Crowe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.prizmj.display.models.generic;
/*
 * com.prizmj.display.models.generic.GenericSensorDropEnv in PrizmJPortable
 * Created by Tyler Crowe on 12/8/2016.
 * Website: https://loneboat.com
 * GitHub: https://github.com/Dubpubber
 * Machine Time: 6:44 AM
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

/**
 * Example sensor drop implementation for Grimace
 */
public class GenericSensorDropEnv extends GenericModel {

    public static final float WALL_HEIGHT = 2.5f;
    public static final float WALL_THICKNESS = 0.25f;
    public static final float WALL_OFFSET = 0.139f;

    public static final Color ENVIRONMENT_COLOR = Color.GREEN;
    public static final float ENVIRONMENT_LENGTH = 5;

    /**
     * Constructor for creating a new GenericModel.
     *
     * @param prizmJ           - The prizmj object that will be used temporarily for getting the model builder.
     * @param genericModelName - The name of the object builder (unique)
     */
    public GenericSensorDropEnv(PrizmJ prizmJ, String genericModelName) {
        super(prizmJ, genericModelName);
        create3DModel();
    }

    @Override
    public void render(ModelBatch batch, Environment environment) {
        batch.render(get3DModelInstance(), environment);
    }

    @Override
    void create2DModel() {

    }

    @Override
    void create3DModel() {
        float length = ENVIRONMENT_LENGTH;
        float thickness = WALL_THICKNESS;

        getModelBuilder().begin();
        MeshPartBuilder mpb = getModelBuilder().part("linear_environment", GL20.GL_TRIANGLES, VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position, new Material(ColorAttribute.createDiffuse(Color.RED)));
        // WALL EAST
        BoxShapeBuilder.build(mpb, ((-length / 2) + WALL_THICKNESS) - WALL_OFFSET, 0, 0, WALL_THICKNESS, WALL_HEIGHT, thickness);
        // WALL WEST
        BoxShapeBuilder.build(mpb, ((length / 2) - WALL_THICKNESS) + WALL_OFFSET, 0, 0, WALL_THICKNESS, WALL_HEIGHT, thickness);

        // Model builder for lines
        mpb = getModelBuilder().part("linear_environment", GL20.GL_LINES, VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position, new Material(ColorAttribute.createDiffuse(ENVIRONMENT_COLOR)));
        mpb.line(-(length / 2), 0, 0, (length / 2), 0, 0);

        set3DModel(getModelBuilder().end());
    }
}
