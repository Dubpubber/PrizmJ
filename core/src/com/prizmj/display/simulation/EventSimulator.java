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

import java.util.PriorityQueue;

import static com.prizmj.display.simulation.EventSimulator.SimulationType.Stepped;
import static com.prizmj.display.simulation.EventSimulator.SimulationType.SteppedConstant;

/**
 * This EventSimulation IS NOT THREAD SAFE!!!
 * PriorityQueue shouldn't be used in a multithreaded environment.
 * PriorityBlockedQueue implementation sister coming soon. (Probably)
 */
public class EventSimulator {

    public enum SimulationType {
        Stepped, SteppedConstant
    }

    private SimulationType type;
    private boolean isSimulating = false;

    private PriorityQueue<Event> eventQueue = new PriorityQueue<>((e1, e2) -> Float.compare(e2.getEventPriority(), e1.getEventPriority()));

    /**
     * Automatically handles adding new events to the event queue based on what simulation type is active.
     * @param event - The event to be added.
     */
    public void addEvent(Event event) {
        if(!eventQueue.contains(event)) {
            if(type == null) eventQueue.add(event);
            else {
                switch (type) {
                    case Stepped:
                        // Since we don't want the stepped simulation to constantly check for newer updates,
                        // make sure the simulation isn't running before we add a new element.
                        if(!isSimulating) eventQueue.offer(event);
                        System.out.println(eventQueue.peek().getModel().getModelName());
                        break;
                    case SteppedConstant:
                        // Opposite of stepped, constant allows for new events to be added during simulation.
                        eventQueue.offer(event);
                        break;
                }
            }
        }
    }

    /**
     * Initiates a stepped simulation. A "stepped" simulation being that one by one, events are executed from the priority queue.
     * This simulation doesn't allow for adding events whilst running. Useful for demonstrations and debugging.
     * @param rate - The rate the top level timer will check for the next event to queue. (usually 1 second is fine)
     * @param eventDelay - The delay on the event defining when it'll be executed.
     * @return - If the simulation concluded successfully.
     */
    public boolean initiateSteppedSimulation(float rate, float eventDelay) throws Exception {
        if(!isSimulating) isSimulating = true;
        else return false;
        type = Stepped;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Event topLevelEvent = eventQueue.peek();
                scheduleEvent(topLevelEvent, eventDelay);
            }
        }, 0, rate);
        return true;
    }

    /**
     * Unlike the stepped simulation, everlasting stepped simulation allow for new events to be added to the queue.
     * In short, you can add events and expect priority to take effect. Useful for actual production.
     * @param rate - The rate the top level timer will check for the next event to queue. (usually 1 second is fine)
     * @param eventDelay - The delay on the event defining when it'll be executed.
     * @return - If the simulation concluded successfully.
     */
    public boolean initiateEverlastingSteppedSimulation(float rate, float eventDelay) {
        if(!isSimulating) isSimulating = true;
        else return false;
        type = SteppedConstant;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Event topLevelEvent = eventQueue.peek();
                scheduleEvent(topLevelEvent, eventDelay);
            }
        }, 0, rate);
        return true;
    }

    /**
     * Private method the schedules the event if it's not null and not queued already.
     * @param event - The event to be queued.
     * @param eventDelay - The delay before the event is executed.
     */
    private void scheduleEvent(Event event, float eventDelay) {
        if(event != null && !event.isQueued()) {
            event.setQueued(true);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    Event event = eventQueue.poll();
                    event.execute();
                    event.setQueued(false);
                    event.reset();
                }
            }, eventDelay);
        }
    }

}
