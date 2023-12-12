package cmorph.event;

import cmorph.entities.AllocationServer;
import cmorph.job.Job;
import cmorph.simulator.Timer;

public class JobEvent implements Event {
    private long time;
    private Job job;

    public JobEvent(long time, Job job) {
        this.time = time;
        this.job = job;
    }

    public long getTime() {
        return this.time;
    }

    public void run() {
        AllocationServer.updateNodeLoads();
        AllocationServer.allocateJob(this.job);

        // 次のJobを生成
        if (job.getUser().isActive(time + job.getTimeSlotNum())) {
            Job nextJob = job.generateNextJob();
            JobEvent nextJobEvent = new JobEvent(time + job.getTimeSlotNum(), nextJob);
            Timer.putEvent(nextJobEvent);
        }
    }

}
