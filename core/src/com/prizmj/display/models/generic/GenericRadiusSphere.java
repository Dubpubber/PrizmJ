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
 * com.prizmj.display.models.generic.GenericRadiusSphere in PrizmJPortable
 * Created by Tyler Crowe on 12/8/2016.
 * Website: https://loneboat.com
 * GitHub: https://github.com/Dubpubber
 * Machine Time: 7:38 PM
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.prizmj.display.PrizmJ;

public class GenericRadiusSphere extends GenericModel {

    /**
     * Constructor for creating a new GenericModel.
     *
     * @param prizmJ           - The prizmj object that will be used temporarily for getting the model builder.
     * @param genericModelName - The name of the object builder (unique)
     */
    public GenericRadiusSphere(PrizmJ prizmJ, String genericModelName) {
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
        getMaterial().set(ColorAttribute.createDiffuse(new Color(0.498f, 0.498f, 0.498f, 0.25f)));
        getMaterial().set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, 0.25f));
        set3DModel(getModelBuilder().createSphere(5, 5, 5, 16, 16, getMaterial(), VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position));
    }
}
