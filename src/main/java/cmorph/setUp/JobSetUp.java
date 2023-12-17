package cmorph.setUp;

import static cmorph.settings.SimulationConfiguration.AVE_JOB_CONTAINER_NUM;
import static cmorph.settings.SimulationConfiguration.AVE_JOB_TIME_SLOT;
import static cmorph.settings.SimulationConfiguration.BACK_WEIGHT;
import static cmorph.settings.SimulationConfiguration.DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.DATA_OBJECT_TARGET_TYPE;
import static cmorph.settings.SimulationConfiguration.FRONT_WEIGHT;
import static cmorph.settings.SimulationConfiguration.MICRO_DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.RANDOMIZE_RATE;
import static cmorph.settings.SimulationConfiguration.RANDOM_JOB_TIME_SLOT;
import static cmorph.simulator.Main.random;

import cmorph.entities.Node;
import cmorph.entities.User;
import cmorph.job.Job;
import cmorph.simulator.Simulator;

public class JobSetUp {

    public static enum DataObjectTargetType {
        RANDOM,
        SINGLE_DC,
        SINGLE_MDC,
    }

    /**
     * 最初のJobを生成する
     * 
     * @param user
     * @return Job
     */
    public static Job getInitialJob(User user) {
        int jobTimeSlot;
        if (RANDOM_JOB_TIME_SLOT) {
            jobTimeSlot = (int) ((1 - RANDOMIZE_RATE + 2 * random.nextDouble() * RANDOMIZE_RATE) * AVE_JOB_TIME_SLOT);
        } else {
            jobTimeSlot = AVE_JOB_TIME_SLOT;
        }

        Node dataObjectNode;
        if (DATA_OBJECT_TARGET_TYPE == DataObjectTargetType.RANDOM) {
            dataObjectNode = Simulator.getSimulatedNodes().get(random.nextInt(MICRO_DATA_CENTER_NUM + DATA_CENTER_NUM));
        } else if (DATA_OBJECT_TARGET_TYPE == DataObjectTargetType.SINGLE_MDC) {
            dataObjectNode = Simulator.getSimulatedNodes().get(random.nextInt(MICRO_DATA_CENTER_NUM));
        } else if (DATA_OBJECT_TARGET_TYPE == DataObjectTargetType.SINGLE_DC) {
            dataObjectNode = Simulator.getSimulatedNodes().get(random.nextInt(DATA_CENTER_NUM) + MICRO_DATA_CENTER_NUM);
        } else {
            throw new Error("DataObjectTargetType is not defined");
        }
        Job job = new Job(user, dataObjectNode, AVE_JOB_CONTAINER_NUM, FRONT_WEIGHT, BACK_WEIGHT, jobTimeSlot);

        return job;
    }
}
