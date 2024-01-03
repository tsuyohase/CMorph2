package cmorph.event;

import static cmorph.settings.SimulationConfiguration.AVE_JOB_TIME_SLOT;
import static cmorph.settings.SimulationConfiguration.RANDOM_JOB_TIME_SLOT;
import static cmorph.simulator.Main.random;

import cmorph.allocator.AllocationServer;
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

        AllocationServer.updateState();
        AllocationServer.allocateJob(this.job);

        // 次のJobを生成
        Job nextJob = job.generateNextJob();
        if (job.getUser().isActive(time + nextJob.getTimeSlotNum())) {
            JobEvent nextJobEvent = new JobEvent(time + nextJob.getTimeSlotNum(), nextJob);
            Timer.putEvent(nextJobEvent);
        }
    }

}
