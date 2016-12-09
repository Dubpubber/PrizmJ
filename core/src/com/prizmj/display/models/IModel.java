/*
 * Copyright (c) 2016 Tyler Crowe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.prizmj.display.models;

/*
  com.prizmj.display.models.IModel in PrizmJPortable
  Created by Tyler Crowe on 12/8/2016.
  Website: https://loneboat.com
  GitHub: https://github.com/Dubpubber
  Machine Time: 4:51 AM
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;

/**
 * IModel or obviously "Interface Model" exists so the render manager can access all models, regardless of type, and render them accordingly.
 */
public interface IModel {

    /**
     * Gets the model's name.
     * @return - The model's name, used for fetching from system manager.
     */
    String getModelName();

    /**
     * Gets the worldly location of the model.
     * @return - A vector3 representing the worldly location.
     */
    Vector3 getLocation();

    /**
     * Changes the color of the attached model.
     * @param color - The new color.
     */
    void changeColor(Color color);

    /**
     * Method for moving the model to the specified position.
     * @param position - The position represented as a vector3
     */
    void moveTo(Vector3 position);

    /**
     * Render method that will be called on by the render manager according to libgdx's model batch.
     * See PrizmJ main render method for more information.
     */
    void render(ModelBatch batch, Environment environment);

    /**
     * Disposes of the Class:IModel object.
     */
    void dispose();
}
