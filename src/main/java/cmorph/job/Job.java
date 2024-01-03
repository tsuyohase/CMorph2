package cmorph.job;

import static cmorph.settings.SimulationConfiguration.AVE_DATA_OBJECT_SIZE;
import static cmorph.settings.SimulationConfiguration.AVE_JOB_TIME_SLOT;
import static cmorph.settings.SimulationConfiguration.RANDOMIZE_RATE;
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
    private int dataObjectSize;

    /**
     * Job
     * 
     * @param user
     * @param dataObjectNode
     * @param useContainerNum
     * @param frontWeight
     * @param backWeight
     * @param timeSlotNum
     * @param dataObjectSize
     */
    public Job(User user, Node dataObjectNode, int useContainerNum, double frontWeight, double backWeight,
            int timeSlotNum, int dataObjectSize) {
        this.user = user;
        this.dataObjectNode = dataObjectNode;
        this.useContainerNum = useContainerNum;
        this.frontWeight = frontWeight;
        this.backWeight = backWeight;
        this.timeSlotNum = timeSlotNum;
        this.dataObjectSize = dataObjectSize;
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

    public int getDataObjectSize() {
        return dataObjectSize;
    }

    /**
     * 次のJobを生成する
     * 
     * @return Job
     */
    public Job generateNextJob() {
        int nextJobTimeSlot;
        if (RANDOM_JOB_TIME_SLOT) {
            nextJobTimeSlot = (int) ((1 - RANDOMIZE_RATE + 2 * random.nextDouble() * RANDOMIZE_RATE)
                    * AVE_JOB_TIME_SLOT);
        } else {
            nextJobTimeSlot = AVE_JOB_TIME_SLOT;
        }

        return new Job(this.user, this.dataObjectNode, this.useContainerNum, this.frontWeight, this.backWeight,
                nextJobTimeSlot, AVE_DATA_OBJECT_SIZE);
    }
}
