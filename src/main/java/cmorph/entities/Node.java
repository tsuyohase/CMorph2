package cmorph.entities;

import cmorph.job.Job;
import cmorph.simulator.Timer;
import cmorph.utils.Point;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class Node {
    private final int nodeId;
    private final Point location;
    private final int containerNum;

    /**
     * ノードに割り当てられたジョブを終了時間の遅い順で並べたキュー
     */
    private PriorityQueue<ScheduledJob> allocatedJobs = new PriorityQueue<>();

    /**
     * 割り当てられたジョブを終了時間で管理するためのクラス
     */
    private static class ScheduledJob implements Comparable<ScheduledJob> {
        private final Job job;
        private final long endTime;

        private ScheduledJob(Job job, long endTime) {
            this.job = job;
            this.endTime = endTime;
        }

        private Job getJob() {
            return this.job;
        }

        private long getEndTime() {
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

    /**
     * ノード(MDC, DC)
     * 
     * @param nodeId
     * @param location
     * @param containerNum
     */
    public Node(int nodeId, Point location, int containerNum) {
        this.nodeId = nodeId;
        this.location = location;
        this.containerNum = containerNum;
    }

    /**
     * ジョブを受け取る関数
     * 
     * @param job
     */
    public void receiveJob(Job job) {
        long endTime = Timer.getCurrentTime() + job.getTimeSlotNum() - 1;
        this.allocatedJobs.add(new ScheduledJob(job, endTime));
    }

    /**
     * timeの時点で実行中のジョブを取得する関数
     * 
     * @param time
     * @return
     */
    public ArrayList<Job> getRunningJobs(long time) {
        ArrayList<Job> runningJobs = new ArrayList<>();
        for (ScheduledJob scheduledJob : this.allocatedJobs) {
            if (scheduledJob.getEndTime() >= time) {
                if (scheduledJob.getJob().getUser().isActive(time)) {
                    runningJobs.add(scheduledJob.getJob());
                }
            } else {
                break;
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
        return (double) runningContainerNum / (double) this.containerNum;
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
}
