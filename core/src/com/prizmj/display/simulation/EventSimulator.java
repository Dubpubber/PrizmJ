/*
 * Copyright (c) 2016 Tyler Crowe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.prizmj.display.simulation;
/*
 * com.prizmj.display.simulation.EventSimulation in PrizmJPortable
 * Created by Tyler Crowe on 12/8/2016.
 * Website: https://loneboat.com
 * GitHub: https://github.com/Dubpubber
 * Machine Time: 4:24 PM
 */

import com.badlogic.gdx.utils.Timer;
import com.prizmj.display.simulation.events.Event;

import java.util.Arrays;
import java.util.PriorityQueue;

public class EventSimulator {

    private PriorityQueue<Event> eventQueue = new PriorityQueue<>();

    public void addEvent(Event event) {
        if(!eventQueue.contains(event)) eventQueue.add(event);
    }

    public boolean initiateSimulation(int simulationType, float rate, float eventdelay) throws Exception {
        if(simulationType < 0 || simulationType > 1) throw new Exception("Unknown simulation type.");
        // sort priority queue first
        Arrays.sort(eventQueue.toArray());
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Event topLevelEvent = eventQueue.peek();
                if(topLevelEvent != null && !topLevelEvent.isQueued()) {
                    topLevelEvent.setQueued(true);
                    switch (simulationType) {
                        case 0:
                            // Stepped simulation, 1 event at a time //
                            Timer.schedule(new Timer.Task() {
                                @Override
                                public void run() {
                                    Event event = eventQueue.poll();
                                    event.execute();
                                    event.setQueued(false);
                                    event.reset();
                                }
                            }, eventdelay);
                    }
                }
            }
        }, 0, rate);
        return true;
    }

}
