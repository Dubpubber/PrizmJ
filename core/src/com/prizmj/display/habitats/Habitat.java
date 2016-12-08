/*
 * Copyright (c) 2016 Tyler Crowe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.prizmj.display.habitats;

/**
 * com.prizmj.display.habitats.Habitat in PrizmJPortable
 * Created by Tyler Crowe on 12/8/2016.
 * Website: https://loneboat.com
 * GitHub: https://github.com/Dubpubber
 * Machine Time: 4:47 AM
 */

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Array;
import com.prizmj.display.models.IModel;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Habitats are simply put, are "environments" where algorithms can range and roam inside the PrizmJ Universe.
 */
public abstract class Habitat {
    // Name of the habitat
    private String name;

    // Array of all models inside this habitat.
    private Array<IModel> models;

    /* RENDERING SPECIFIC OBJECTS */
    private ModelBatch batch;
    private Environment environment;

    /**
     * Creates a new habitat.
     * @param name - The name of the habitat.
     * @param modelBatch - The batch used for rendering the models.
     * @param environment - The environment containing the models.
     */
    public Habitat(String name, ModelBatch modelBatch, Environment environment) {
        this.name = name;
        this.batch = modelBatch;
        this.environment = environment;
        this.models = new Array<>();
    }

    /**
     * Creates a new habitat and inserts any models received.
     * @param name - The name of the habitat.
     * @param modelBatch - The batch used for rendering the models.
     * @param environment - The environment containing the models.
     * @param models - n size array of models to be added to the habitat upon creation.
     * @throws Exception - If the model already exists in the array.
     */
    public Habitat(String name, ModelBatch modelBatch, Environment environment, IModel... models) throws Exception {
        this.name = name;
        this.batch = modelBatch;
        this.environment = environment;
        this.models = new Array<>();
        for (IModel im : models) addModel(im);
    }

    /**
     * Adds a new model to the habitat's array.
     * This method also checks for name uniqueness.
     * @param model - The model to be inserted into the array.
     * @return - Return's true if addition was successful, error if otherwise.
     * @throws Exception - If the model already exists in the array.
     */
    public boolean addModel(IModel model) throws Exception {
        if(model != null && !model.getModelName().isEmpty()) {
            if(Stream.of(models.toArray()).noneMatch(imodel -> Objects.equals(imodel.getModelName(), model.getModelName()))) {
                models.add(model);
                return true;
            } else
                throw new Exception("Unable to add " + model.getModelName() + "; model with that name already exists in habitat!");
        }
        return false;
    }

    /**
     * Gets an array of all the models in the habitat.
     * @return - An array of all models in habitat.
     */
    public IModel[] getModels() {
        return models.toArray();
    }

    /**
     * Called on to render all models of the habitat.
     */
    public void render() {
        models.forEach(model -> model.render(batch, environment));
    }

    /**
     * Disposes of the habitat.
     */
    public void dispose() {
        models.clear();
    }

}
