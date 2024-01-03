package cmorph.entities;

import java.util.ArrayList;
import java.util.Collections;

import cmorph.job.Job;
import cmorph.simulator.Timer;
import cmorph.utils.ScheduledJob;

public class Link {
    private final int linkId;
    private final Node connectNode1;
    private final Node connectNode2;
    private final int bandwidth;

    private ArrayList<ScheduledJob> transferJobs = new ArrayList<>();

    /**
     * 
     * @param linkId
     * @param connectNode1
     * @param connectNode2
     * @param bandwidth
     */
    public Link(
            int linkId, Node connectNode1,
            Node connectNode2,
            int bandwidth) {
        this.linkId = linkId;
        this.connectNode1 = connectNode1;
        this.connectNode2 = connectNode2;
        this.bandwidth = bandwidth;
    }

    /**
     * ジョブを受け取る関数
     * 
     * @param job
     */
    public void receiveJob(Job job, long delay) {
        long startTime = Timer.getCurrentTime() + delay;
        long endTime = startTime + Math.max(1, job.getDataObjectSize() / this.bandwidth) - 1;
        this.transferJobs.add(new ScheduledJob(job, startTime, endTime));
        Collections.sort(this.transferJobs);
    }

    /**
     * timeの時点で終了しているジョブを削除する関数
     * 
     * @param time
     */
    public void removeEndJob(long time) {
        for (int i = 0; i < this.transferJobs.size(); i++) {
            if (this.transferJobs.get(i).getEndTime() < time) {
                this.transferJobs.remove(i);
                i--;
            }
        }
    }

    /**
     * timeの時点で実行中のジョブを取得する関数
     * 
     * @param time
     * @return
     */
    public ArrayList<Job> getRunningJobs(long time) {
        ArrayList<Job> runningJobs = new ArrayList<>();
        for (int i = 0; i < this.transferJobs.size(); i++) {
            ScheduledJob scheduledJob = this.transferJobs.get(i);
            if ((scheduledJob.getStartTime() <= time) && (time <= scheduledJob.getEndTime())) {
                if (scheduledJob.getJob().getUser().isActive(time)) {
                    runningJobs.add(scheduledJob.getJob());
                }
            }
        }
        return runningJobs;
    }

    /**
     * timeの時点での帯域使用率を取得する関数
     * 
     * @param time
     * @return
     */
    public double getLoad(long time) {
        ArrayList<Job> runningJobs = getRunningJobs(time);
        int transferDataSize = 0;
        for (int i = 0; i < runningJobs.size(); i++) {
            transferDataSize += runningJobs.get(i).getDataObjectSize();
        }
        double load = Math.min(1, (double) transferDataSize / (double) this.bandwidth);
        return load;
    }

    public int getTransferDataSize(long time) {
        int transferDataSize = 0;
        for (int i = 0; i < this.transferJobs.size(); i++) {
            ScheduledJob scheduledJob = this.transferJobs.get(i);
            if ((time <= scheduledJob.getEndTime())) {
                if (scheduledJob.getJob().getUser().isActive(time)) {
                    transferDataSize += scheduledJob.getJob().getDataObjectSize();
                }
            }
        }
        return transferDataSize;
    }

    public int getLinkId() {
        return this.linkId;
    }

    public Node getConnectNode1() {
        return this.connectNode1;
    }

    public Node getConnectNode2() {
        return this.connectNode2;
    }

    public int getBandWidth() {
        return this.bandwidth;
    }
}
