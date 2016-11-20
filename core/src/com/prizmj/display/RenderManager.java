package com.prizmj.display;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

/**
 * com.prizmj.display.RenderManager in PrizmJ
 */
public class RenderManager {

    private boolean rooms = true;
    private boolean graph = true;

    private Blueprint blueprint;

    public RenderManager(Blueprint blueprint) {
        this.blueprint = blueprint;
    }

    public void switchDimension(Dimension dimension) {
        blueprint.getAllModels().forEach(model -> model.setDimensionView(dimension));
    }

    public void render(ModelBatch modelBatch, Environment environment) {
        if(rooms)
            blueprint.getAllModels().forEach(model -> model.render(modelBatch, environment));
        if(graph)
            blueprint.getGeometricNetworkModel().render(modelBatch, environment);
    }

    public void toggleRooms() {
        this.rooms = !rooms;
    }

    public void toggleGraph() {
        this.graph = !graph;
    }
}
