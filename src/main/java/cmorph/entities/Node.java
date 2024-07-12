package cmorph.entities;

import cmorph.job.Job;
import cmorph.simulator.Timer;
import cmorph.utils.Point;
import cmorph.utils.ScheduledJob;

import java.util.List;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class Node {
    private final int nodeId;
    private final Point location;
    private final int containerNum;
    private final int costWeight;
    private final double loadThreshold;

    /**
     * ノードに割り当てられたジョブを終了時間の遅い順で並べた配列
     */
    private ArrayList<ScheduledJob> allocatedJobs = new ArrayList<>();

    /**
     * ノード(MDC, DC)
     * 
     * @param nodeId
     * @param location
     * @param containerNum
     */
    public Node(int nodeId, Point location, int containerNum, int costWeight, double loadThreshold) {
        this.nodeId = nodeId;
        this.location = location;
        this.containerNum = containerNum;
        this.costWeight = costWeight;
        this.loadThreshold = loadThreshold;
    }

    /**
     * ジョブを受け取る関数
     * 
     * @param job
     */
    public void receiveJob(Job job) {
        long endTime = Timer.getCurrentTime() + job.getTimeSlotNum() - 1;
        this.allocatedJobs.add(new ScheduledJob(job, Timer.getCurrentTime(), endTime));
        Collections.sort(this.allocatedJobs);
    }

    /**
     * timeの時点で終了しているジョブを削除する関数
     * 
     * @param time
     */
    public void removeEndJob(long time) {
        for (int i = 0; i < this.allocatedJobs.size(); i++) {
            if (this.allocatedJobs.get(i).getEndTime() < time) {
                this.allocatedJobs.remove(i);
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
        for (int i = 0; i < this.allocatedJobs.size(); i++) {
            ScheduledJob scheduledJob = this.allocatedJobs.get(i);
            if ((scheduledJob.getStartTime() <= time) && (time <= scheduledJob.getEndTime())) {
                if (scheduledJob.getJob().getUser().isActive(time)) {
                    runningJobs.add(scheduledJob.getJob());
                }
            }
        }
        return runningJobs;
    }

    /**
     * timeの時点での負荷を取得する関数
     * 
     * @param time
     * @return
     */
    public double getLoad(long time) {
        ArrayList<Job> runningJobs = getRunningJobs(time);
        int runningContainerNum = 0;
        for (int i = 0; i < runningJobs.size(); i++) {
            runningContainerNum += runningJobs.get(i).getUseContainerNum();
        }
        double load = Math.min(1, (double) runningContainerNum / (double) this.containerNum);
        return load;
    }

    /**
     * timeの時点での残りコンテナ数を取得する関数
     * 
     * @param time
     * @return
     */
    public int getRemainingContainerNum(long time) {
        ArrayList<Job> runningJobs = getRunningJobs(time);
        int runningContainerNum = 0;
        for (int i = 0; i < runningJobs.size(); i++) {
            runningContainerNum += runningJobs.get(i).getUseContainerNum();
        }
        return this.containerNum - runningContainerNum;
    }

    public int getNodeId() {
        return this.nodeId;
    }

    public Point getLocation() {
        return this.location;
    }

    public int getContainerNum() {
        return this.containerNum;
    }

    public int getCostWeight() {
        return this.costWeight;
    }

    public double getLoadThrethold() {
        return this.loadThreshold;
    }
}
