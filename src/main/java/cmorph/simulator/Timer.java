package cmorph.simulator;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import cmorph.event.Event;

public class Timer {
    private static final PriorityQueue<ScheduledEvent> eventQueue = new PriorityQueue<>();

    private static long currentTime = 0;

    private static class ScheduledEvent implements Comparable<ScheduledEvent> {
        private final Event event;
        private final long scheduledTime;

        private ScheduledEvent(Event event, long scheduledTime) {
            this.event = event;
            this.scheduledTime = scheduledTime;
        }

        private Event getEvent() {
            return this.event;
        }

        private long getScheduledTime() {
            return this.scheduledTime;
        }

        public int compareTo(ScheduledEvent o) {
            if (this.equals(o)) {
                return 0;
            }
            int order = Long.signum(this.scheduledTime - o.scheduledTime);
            if (order != 0) {
                return order;
            }
            order = System.identityHashCode(this) - System.identityHashCode(o);
            return order;
        }
    }

    public static void runEvent() {
        if (eventQueue.size() > 0) {
            ScheduledEvent currentScheduledEvent = eventQueue.poll();
            Event currentEvent = currentScheduledEvent.getEvent();
            currentTime = currentScheduledEvent.getScheduledTime();
            currentEvent.run();
        }
    }

    public static Event getEvent() {
        if (eventQueue.size() > 0) {
            ScheduledEvent currentEvent = eventQueue.peek();
            return currentEvent.getEvent();
        } else {
            return null;
        }
    }

    public static void putEvent(Event event) {
        ScheduledEvent scheduledEvent = new ScheduledEvent(event, event.getTime());
        eventQueue.add(scheduledEvent);
    }

    public static long getCurrentTime() {
        return currentTime;
    }
}
