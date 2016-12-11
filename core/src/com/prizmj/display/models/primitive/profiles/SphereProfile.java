/*
 * Copyright (c) 2016 Tyler Crowe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.prizmj.display.models.primitive.profiles;/*
 * com.prizmj.display.models.generic.profiles.SphereProfile in PrizmJPortable
 * Created by Tyler Crowe on 12/8/2016.
 * Website: https://loneboat.com
 * GitHub: https://github.com/Dubpubber
 * Machine Time: 6:19 AM
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

/**
 * Sphere Profile contains all the information for generic sphere creation.
 *  [Base]
 *  - Initial starting position, vector3 or float x, y, z
 *  - Initial color
 *  [2D Specific (Remember, a sphere in 2d is really a cylinder on its side)]
 *  - Width
 *  - Height
 *  - Depth
 *  - Divisions
 *  [3D Specific]
 *  - Width
 *  - Height
 *  - Depth
 *  - DivisionsV
 *  - DivisionsU
 *
 */
public class SphereProfile {
    /* BEGIN BASE */
    private Vector3 position;
    private Color color;

    /* BEGIN CYLINDER PROFILE */
    private float cWidth;
    private float cHeight;
    private float cDepth;
    private int cDivisions;

    /* BEGIN SPHERE PROFILE */
    private float sWidth;
    private float sHeight;
    private float sDepth;
    private int sDivisionsV;
    private int sDivisionsU;

    public SphereProfile(Vector3 position, Color color, float cWidth, float cHeight, float cDepth, int cDivisions, float sWidth, float sHeight, float sDepth, int sDivisionsV, int sDivisionsU) {
        this.position = position;
        this.color = color;
        this.cWidth = cWidth;
        this.cHeight = cHeight;
        this.cDepth = cDepth;
        this.cDivisions = cDivisions;
        this.sWidth = sWidth;
        this.sHeight = sHeight;
        this.sDepth = sDepth;
        this.sDivisionsV = sDivisionsV;
        this.sDivisionsU = sDivisionsU;
    }

    public SphereProfile(float x, float y, float z, Color color, float cWidth, float cHeight, float cDepth, int cDivisions, float sWidth, float sHeight, float sDepth, int sDivisionsV, int sDivisionsU) {
        this.position = new Vector3(x, y, z);
        this.color = color;
        this.cWidth = cWidth;
        this.cHeight = cHeight;
        this.cDepth = cDepth;
        this.cDivisions = cDivisions;
        this.sWidth = sWidth;
        this.sHeight = sHeight;
        this.sDepth = sDepth;
        this.sDivisionsV = sDivisionsV;
        this.sDivisionsU = sDivisionsU;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getCylinderWidth() {
        return cWidth;
    }

    public void setCylinderWidth(float cWidth) {
        this.cWidth = cWidth;
    }

    public float getCylinderHeight() {
        return cHeight;
    }

    public void setCylinderHeight(float cHeight) {
        this.cHeight = cHeight;
    }

    public float getCylinderDepth() {
        return cDepth;
    }

    public void setCylinderDepth(float cDepth) {
        this.cDepth = cDepth;
    }

    public int getCylinderDivisions() {
        return cDivisions;
    }

    public void setCylinderDivisions(int cDivisions) {
        this.cDivisions = cDivisions;
    }

    public float getSphereWidth() {
        return sWidth;
    }

    public void setSphereWidth(float sWidth) {
        this.sWidth = sWidth;
    }

    public float getSphereHeight() {
        return sHeight;
    }

    public void setSphereHeight(float sHeight) {
        this.sHeight = sHeight;
    }

    public float getSphereDepth() {
        return sDepth;
    }

    public void setSphereDepth(float sDepth) {
        this.sDepth = sDepth;
    }

    public int getSphereDivisionsV() {
        return sDivisionsV;
    }

    public void setSphereDivisionsV(int sDivisionsV) {
        this.sDivisionsV = sDivisionsV;
    }

    public int getSphereDivisionsU() {
        return sDivisionsU;
    }

    public void setSphereDivisionsU(int sDivisionsU) {
        this.sDivisionsU = sDivisionsU;
    }
}
