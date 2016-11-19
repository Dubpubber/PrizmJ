package com.prizmj.display;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

/**
 * com.prizmj.display.RenderManager in PrizmJ
 */
public class RenderManager {
    private PrizmJ prizmJ;
    private Blueprint blueprint;

    public RenderManager(PrizmJ prizmJ, Blueprint blueprint) {
        this.prizmJ = prizmJ;
        this.blueprint = blueprint;
    }

    public void switchDimension(Dimension dimension) {
        blueprint.getFloors().forEach(floor ->
                floor.getAllRooms().forEach(rm -> rm.setDimensionView(dimension))
        );
    }

    public void render(ModelBatch modelBatch, Environment environment) {
        blueprint.getFloors().forEach(floor -> floor.getAllRooms().forEach(rm -> {
            rm.render(modelBatch, environment);
        }));
        blueprint.getGeometricNetworkModel().render(modelBatch, environment);
    }

}
