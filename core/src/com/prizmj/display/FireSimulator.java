package com.prizmj.display;

/**
 * Created by GrimmityGrammity on 11/16/2016.
 *
 * A Fire Simulator is one of the three key components of PrizmJ.
 *
 * A Fire Simulator simulates the spreading of fire within a Building
 *  and performs optimal path analysis on the burning building.
 *  A Fire Simulator requires a Building to burn and a GNM of that building to
 *  perform analysis.
 *
 */
public class FireSimulator {

    private GNM gnm;

    public void generateGNM(Building building) {
        gnm = new GNM(building);
    }

    public GNM getGNM() {
        return gnm;
    }
}
