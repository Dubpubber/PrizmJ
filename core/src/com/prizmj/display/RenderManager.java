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

    public void switchDimension(int dimension) {
        blueprint.getFloors().forEach(floor -> floor.getAllRooms().forEach(rm -> rm.setVisibility(rm.getDimension() == dimension)));
    }

    public void render(ModelBatch modelBatch, Environment environment) {
        blueprint.getFloors().forEach(floor -> floor.getAllRooms().forEach(rm -> {
            if(rm.isVisible()) rm.render(modelBatch, environment);
        }));
    }

}
