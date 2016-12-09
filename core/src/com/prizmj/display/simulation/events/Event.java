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
 * com.prizmj.display.simulation.events.Event in PrizmJPortable
 * Created by Tyler Crowe on 12/8/2016.
 * Website: https://loneboat.com
 * GitHub: https://github.com/Dubpubber
 * Machine Time: 4:25 PM
 */

import com.badlogic.gdx.utils.Pool;
import com.prizmj.display.models.IModel;

public abstract class Event implements Pool.Poolable, Comparable {

    // Stores how important the event is to execute, the higher the value, the first it'll be to execute.
    private float eventPriority;

    // The model attached to this event.
    private IModel model;

    // If the event is queued or not.
    private boolean queued ;

    public Event(IModel model, float eventPriority) {
        setModel(model);
        setEventPriority(eventPriority);
    }

    public IModel getModel() {
        return model;
    }

    public void setModel(IModel model) {
        this.model = model;
    }

    public float getEventPriority() {
        return eventPriority;
    }

    public void setEventPriority(float eventPriority) {
        this.eventPriority = eventPriority;
    }

    public boolean isQueued() {
        return queued;
    }

    public void setQueued(boolean queued) {
        this.queued = queued;
    }

    abstract public void execute();

    @Override
    abstract public void reset();
}
