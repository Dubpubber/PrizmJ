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
 * com.prizmj.display.models.generic.GenericSphere in PrizmJPortable
 * Created by Tyler Crowe on 12/8/2016.
 * Website: https://loneboat.com
 * GitHub: https://github.com/Dubpubber
 * Machine Time: 5:24 AM
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.prizmj.display.PrizmJ;
import com.prizmj.display.models.generic.profiles.SphereProfile;

public class GenericSphere extends GenericModel {

    // Sphere profile object, please see SphereProfile.java for more information. [REQUIRED!!!]
    private SphereProfile profile;

    /**
     * Creates a new Generic sphere object.
     * @param prizmJ           - PrizmJ object (used for getting model builder)
     * @param genericModelName - The name of this sphere. (make it special or else an error will be thrown at you!)
     * @param profile          - The sphere profile that contains all the information for placement, color, and shape!
     */
    public GenericSphere(PrizmJ prizmJ, String genericModelName, SphereProfile profile) {
        super(prizmJ, genericModelName);
        this.profile = profile;
        create2DModel();
        create3DModel();
        // Honor profile //
        changeColor(profile.getColor());
        moveTo(profile.getPosition());
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
        getMaterial().set(ColorAttribute.createDiffuse(Color.RED));
        set3DModel(getModelBuilder().createSphere(profile.getSphereWidth(), profile.getSphereHeight(), profile.getSphereDepth(), profile.getSphereDivisionsU(), profile.getSphereDivisionsV(), getMaterial(), VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position));
    }
}
