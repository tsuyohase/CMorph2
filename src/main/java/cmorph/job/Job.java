package cmorph.job;

import static cmorph.settings.SimulationConfiguration.AVE_JOB_TIME_SLOT;
import static cmorph.settings.SimulationConfiguration.RANDOM_JOB_TIME_SLOT;
import static cmorph.simulator.Main.random;

import cmorph.entities.Node;
import cmorph.entities.User;

public class Job {
    private User user;
    private Node dataObjectNode;
    private int useContainerNum;
    private double frontWeight;
    private double backWeight;
    private int timeSlotNum;

    /**
     * Job
     * 
     * @param user
     * @param dataObjectNode
     * @param useContainerNum
     * @param frontWeight
     * @param backWeight
     * @param timeSlotNum
     */
    public Job(User user, Node dataObjectNode, int useContainerNum, double frontWeight, double backWeight,
            int timeSlotNum) {
        this.user = user;
        this.dataObjectNode = dataObjectNode;
        this.useContainerNum = useContainerNum;
        this.frontWeight = frontWeight;
        this.backWeight = backWeight;
        this.timeSlotNum = timeSlotNum;
    }

    public User getUser() {
        return user;
    }

    public Node getDataObjectNode() {
        return dataObjectNode;
    }

    public int getUseContainerNum() {
        return useContainerNum;
    }

    public double getFrontWeight() {
        return frontWeight;
    }

    public double getBackWeight() {
        return backWeight;
    }

    public int getTimeSlotNum() {
        return timeSlotNum;
    }

    /**
     * 次のJobを生成する
     * 
     * @return Job
     */
    public Job generateNextJob() {
        int nextJobTimeSlot;
        if (RANDOM_JOB_TIME_SLOT) {
            nextJobTimeSlot = (int) (((random.nextDouble() + 1) / 2) * AVE_JOB_TIME_SLOT);
        } else {
            nextJobTimeSlot = AVE_JOB_TIME_SLOT;
        }

        return new Job(this.user, this.dataObjectNode, this.useContainerNum, this.frontWeight, this.backWeight,
                nextJobTimeSlot);
    }
}
