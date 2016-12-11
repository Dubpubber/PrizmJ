/*
 * Copyright (c) 2016 Tyler Crowe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.prizmj.display.simulation.events;
/*
 * com.prizmj.display.simulation.events.MoveEvent in PrizmJPortable
 * Created by Tyler Crowe on 12/8/2016.
 * Website: https://loneboat.com
 * GitHub: https://github.com/Dubpubber
 * Machine Time: 4:31 PM
 */

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.prizmj.display.models.IModel;

public class MoveEvent extends Event {

    // The position where the move event will move the attached model.
    private Vector3 position;

    // Boolean for smoothness, meaning, it'll ease to the location.
    private boolean smoothing = false;

    /* SMOOTHING OBJECTS */
    private int steps = 5;

    /**
     * Creates a new move event, which extends event and implements poolable.
     * Use reset instead of disposing for effective pooling use.
     * @param model    - The model to be attached to this event.
     * @param position - The position the model will move to.
     */
    public MoveEvent(IModel model, Vector3 position, float priority) {
        super(model, priority);
        this.position = position;
    }

    /**
     * Updates the position for when execute() is called on.
     * @param position - The position represented as a vector3
     */
    public void init(Vector3 position) {
        this.position = position;
    }

    @Override
    public void execute() {
        if(!smoothing) {
            getModel().translateBy(position);
        } else {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    Vector3 currentLocation = getModel().getLocation();
                    System.out.println();
                    position = new Vector3(
                            (currentLocation.x > 0) ? (currentLocation.x + position.x) / steps : (currentLocation.x - position.x) / steps,
                            (currentLocation.y > 0) ? (currentLocation.y + position.y) / steps : (currentLocation.y - position.y) / steps,
                            (currentLocation.z > 0) ? (currentLocation.z + position.z) / steps : (currentLocation.z - position.z) / steps
                    );
                    getModel().translateBy(position);
                    if(steps != 0) steps -= 1;
                }
            }, 0, 0.5f, steps);
        }
    }
}