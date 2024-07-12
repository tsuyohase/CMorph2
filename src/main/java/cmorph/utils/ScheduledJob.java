package cmorph.utils;

import cmorph.job.Job;

public class ScheduledJob implements Comparable<ScheduledJob> {
    private final Job job;
    private final long startTime;
    private final long endTime;

    public ScheduledJob(Job job, long startTime, long endTime) {
        this.job = job;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Job getJob() {
        return this.job;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public int compareTo(ScheduledJob o) {
        if (this.equals(o)) {
            return 0;
        }
        int order = Long.signum(o.endTime - this.endTime);
        if (order != 0) {
            return order;
        }
        order = System.identityHashCode(this) - System.identityHashCode(o);
        return order;
    }
}
