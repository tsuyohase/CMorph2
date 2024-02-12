package cmorph.logger;

import java.util.ArrayList;

import cmorph.entities.Node;
import cmorph.entities.User;
import cmorph.settings.SimulationConfiguration;
import cmorph.simulator.Simulator;
import cmorph.utils.BasicLogger;

public class AnalysisLogger {
    private static BasicLogger logger = BasicLogger.getLogger("cmorph.analysis");

    private static ArrayList<ArrayList<Double>> nodeLoadList = new ArrayList<>();

    private static ArrayList<ArrayList<Integer>> userConnectionList = new ArrayList<>();

    private static long lastAddedTime = 0;

    public static void addSimulationResult() {
        for (long time = lastAddedTime; time <= SimulationConfiguration.END_TIME; time++) {
            addTimeStepData(time);
        }
        logger.print("{\n");
        logger.print("\"node-load-list\" :" + nodeLoadList + ",\n");
        logger.print("\"user-connection-list\" :" + userConnectionList);
        logger.print("\n}");
    }

    public static void addData(long startTime, long endTime) {
        for (long time = startTime; time < endTime; time++) {
            addTimeStepData(time);
        }
        lastAddedTime = endTime;
    }

    private static void addTimeStepData(long time) {
        ArrayList<Double> nodeLoad = new ArrayList<>();
        ArrayList<Integer> userConnction = new ArrayList<>();

        for (Node node : Simulator.getSimulatedNodes()) {
            nodeLoad.add(node.getLoad(time));
        }

        for (User user : Simulator.getSimulatedUsers()) {
            userConnction.add(user.getConnectNodeId(time));
        }

        nodeLoadList.add(nodeLoad);
        userConnectionList.add(userConnction);
    }
}
