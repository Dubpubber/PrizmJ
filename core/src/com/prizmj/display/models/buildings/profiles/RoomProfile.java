/*
 * Copyright (c) 2016 Tyler Crowe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.prizmj.display.models.buildings.profiles;/*
 * com.prizmj.display.models.buildings.profiles.Room in PrizmJPortable
 * Created by Tyler Crowe on 12/11/2016.
 * Website: https://loneboat.com
 * GitHub: https://github.com/Dubpubber
 * Machine Time: 3:30 AM
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class RoomProfile {
    private Vector3 worldyLocation;
    private float width;
    private float height;

    private Color floorColor;

    public RoomProfile(float x, float y, float z, float width, float height, Color floorColor) {
        this.worldyLocation = new Vector3(x, y, z);
        this.width = width;
        this.height = height;
        this.floorColor = floorColor;
    }

    public void updatePosition(Vector3 worldyLocation) {
        this.worldyLocation = worldyLocation;
    }

    public void updatePosition(float x, float y, float z) {
        this.worldyLocation = new Vector3(x, y, z);
    }

    public Vector3 getWorldyLocation() {
        return worldyLocation;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Color getFloorColor() {
        return floorColor;
    }
}
