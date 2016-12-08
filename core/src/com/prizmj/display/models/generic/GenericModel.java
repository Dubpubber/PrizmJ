/*
 * Copyright (c) 2016 Tyler Crowe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.prizmj.display.models.generic;/*
 * com.prizmj.display.models.generic.GenericModel in PrizmJPortable
 * Created by Tyler Crowe on 12/8/2016.
 * Website: https://loneboat.com
 * GitHub: https://github.com/Dubpubber
 * Machine Time: 5:00 AM
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.prizmj.display.Dimension;
import com.prizmj.display.PrizmJ;
import com.prizmj.display.models.IModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic model class for all, well, generic models!
 *
 * This includes (but isn't limited too):
 *  - Cubes
 *  - Cylinders
 *  - Spheres
 *  - Edges
 */
public abstract class GenericModel implements IModel {
    // IDENTIFIERS //
    private String genericModelName;

    // MODEL BUILDER //
    private ModelBuilder modelBuilder;

    // MATERIAL //
    private Material material;

    // MODEL OBJECTS //
    private Model model_2d;
    private Model model_3d;

    // INSTANCE OBJECTS //
    private ModelInstance modelInstance_2d;
    private ModelInstance modelInstance_3d;

    // The location of the generic model in the habitat.
    private Vector3 worldyLocation;

    // The current dimension of the generic model. This judges which model the render manager will render.
    private Dimension dimension;

    // MetaData Map, can be used to store any type of information.
    private Map<String, Object> metadata;

    /**
     * Constructor for creating a new GenericModel.
     * @param prizmJ - The prizmj object that will be used temporarily for getting the model builder.
     * @param genericModelName - The name of the object builder (unique)
     */
    public GenericModel(PrizmJ prizmJ, String genericModelName) {
        this.genericModelName = genericModelName;
        this.modelBuilder = prizmJ.getModelBuilder();
        this.material = new Material();
        this.metadata = new HashMap<>();
    }

    /**
     * Gets the 3d model.
     * @return - The 3d model object.
     */
    public Model get3DModel() {
        return model_3d;
    }

    /**
     * Gets the 2d model.
     * @return - The 2d model object.
     */
    public Model get2DModel() {
        return model_2d;
    }

    /**
     * Sets the 3d model.
     * @param model_3d - The model to be set to the local variable.
     */
    public void set3DModel(Model model_3d) {
        this.model_3d = model_3d;
        set3DModelInstance(new ModelInstance(model_3d));
    }

    /**
     * Sets the 2d model.
     * @param model_2d - The model to be set to the local variable.
     */
    public void set2DModel(Model model_2d) {
        this.model_2d = model_2d;
        set2DModelInstance(new ModelInstance(model_2d));
    }

    /**
     * Gets the 3d model instance.
     * @return - The 3d model instance object.
     */
    public ModelInstance get3DModelInstance() {
        return modelInstance_3d;
    }

    /**
     * Gets the 2d model instance.
     * @return - The 2d model instance object.
     */
    public ModelInstance get2DModelInstance() {
        return modelInstance_2d;
    }

    /**
     * Sets the 3d model instance.
     * @param modelInstance_3d - The model instance to be set to the local variable.
     */
    private void set3DModelInstance(ModelInstance modelInstance_3d) {
        this.modelInstance_3d = modelInstance_3d;
    }

    /**
     * Sets the 2d model.
     * @param modelInstance_2d - The model instance to be set to the local variable.
     */
    private void set2DModelInstance(ModelInstance modelInstance_2d) {
        this.modelInstance_2d = modelInstance_2d;
    }

    /**
     * Gets the local model builder object.
     * @return - The model builder.
     */
    public ModelBuilder getModelBuilder() {
        return modelBuilder;
    }

    /**
     * Gets the pre-created material.
     * @return - The material object.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Move's the generic model to the specified coordinates.
     * It also updates both model instances and the worldly location.
     * @param x - The 'x' coordinate.
     * @param y - The 'y' coordinate.
     * @param z - The 'z' coordinate.
     */
    public void moveTo(float x, float y, float z) {
        Node node;
        this.worldyLocation = new Vector3(x, y, z);

        if(model_2d != null) {
            // Update the 2D model instance first.
            node = model_2d.nodes.first(); // Gets the first node of the 2d model.
            node.globalTransform.translate(x, y, z);
            modelInstance_2d.transform.set(node.globalTransform);
        }

        if(model_3d != null) {
            // Now, update the 3D model instance.
            node = model_3d.nodes.first();
            node.globalTransform.translate(x, y, z);
            modelInstance_3d.transform.set(node.globalTransform);
        }
    }

    /**
     * Helper method that extends onto original moveTo above.
     * @param position - A vector3 object containing the new coordinates for the generic model.
     */
    public void moveTo(Vector3 position) {
        moveTo(position.x, position.y, position.z);
    }

    /**
     * Changes the color of the generic model.
     * @param color - The new color of the generic model.
     */
    public void updateColor(Color color) {
        modelInstance_3d.materials.first().set(ColorAttribute.createDiffuse(color));
        modelInstance_3d.materials.first().set(ColorAttribute.createDiffuse(color));
    }

    /**
     * Gets the current location of the generic model in the current habitat.
     * @return - The worldy location.
     */
    public Vector3 getLocation() {
        return worldyLocation;
    }

    @Override
    public void dispose() {
        model_2d.dispose();
        model_3d.dispose();
    }

    @Override
    public String getModelName() {
        return genericModelName;
    }

    /**
     * Adds new metadata to the generic model.
     * @param id  - The id of the metadata.
     * @param obj - The object being stored.
     */
    public void addMetaData(String id, Object obj) {
        metadata.put(id, obj);
    }

    /**
     * Gets the metadata, if residing.
     * @param id - The metadata id to find.
     * @return - An object of unknown type, requires casting later.
     */
    public Object getMetaData(String id) {
        return metadata.get(id);
    }

    /* BEGIN ABSTRACT METHODS (Every generic model must implement this) */
    abstract void create2DModel(); // Each are pretty self-explanatory.
    abstract void create3DModel(); // But very important!
}