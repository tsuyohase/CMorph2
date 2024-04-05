package cmorph.logger;

import java.util.ArrayList;

import cmorph.entities.Link;
import cmorph.entities.Node;
import cmorph.entities.User;
import cmorph.setUp.UserSetUp.UserType;
import cmorph.settings.SimulationConfiguration;
import cmorph.simulator.Simulator;
import cmorph.utils.BasicLogger;
import cmorph.utils.Point;

public class AnalysisLogger {
    private static BasicLogger logger = BasicLogger.getLogger("cmorph.analysis");

    private static ArrayList<String> userTypeList = new ArrayList<>();

    private static ArrayList<Double> nodeXList = new ArrayList<>();
    private static ArrayList<Double> nodeYList = new ArrayList<>();
    private static ArrayList<Integer> nodeContainerNumList = new ArrayList<>();
    private static ArrayList<Double> nodeLoadThresholdList = new ArrayList<>();

    private static ArrayList<Integer> linkSrcList = new ArrayList<>();
    private static ArrayList<Integer> linkDstList = new ArrayList<>();
    private static ArrayList<Integer> linkCostList = new ArrayList<>();

    private static ArrayList<ArrayList<Double>> nodeLoadList = new ArrayList<>();

    private static ArrayList<ArrayList<Integer>> userConnectionList = new ArrayList<>();
    private static ArrayList<ArrayList<Double>> userXList = new ArrayList<>();
    private static ArrayList<ArrayList<Double>> userYList = new ArrayList<>();
    private static ArrayList<Double> userNetworkThresholdList = new ArrayList<>();
    private static ArrayList<ArrayList<Double>> userConnectionDistanceList = new ArrayList<>();

    private static long lastAddedTime = 0;

    public static void initLog() {
        logger.print("{\n");
    }

    public static void endLog() {
        logger.print("\n}");
    }

    public static void addConfigData() {
        logger.print("\"map-width\" :" + SimulationConfiguration.MAP_WIDTH + ",\n");
        logger.print("\"map-height\" :" + SimulationConfiguration.MAP_HEIGHT + ",\n");

        logger.print("\"user-num\" :" + SimulationConfiguration.USER_NUM + ",\n");
        logger.print(
                "\"user-location-scenario\" :" + "\"" + SimulationConfiguration.USER_LOCATION_SCENARIO + "\"" + ",\n");
        logger.print("\"user-spawn-scenario\" :" + "\"" + SimulationConfiguration.USER_SPAWN_SCENARIO + "\"" + ",\n");
        logger.print("\"is-roop\" :" + SimulationConfiguration.IS_ROOP + ",\n");

        logger.print("\"random-job-time-slot\" :" + SimulationConfiguration.RANDOM_JOB_TIME_SLOT + ",\n");
        logger.print("\"random-job-container-num\" :" + SimulationConfiguration.RANDOM_JOB_CONTAINER_NUM + ",\n");
        logger.print("\"randomize-rate\" :" + SimulationConfiguration.RANDOMIZE_RATE + ",\n");
        logger.print("\"ave-job-time-slot\" :" + SimulationConfiguration.AVE_JOB_TIME_SLOT + ",\n");
        logger.print("\"ave-job-container-num\" :" + SimulationConfiguration.AVE_JOB_CONTAINER_NUM + ",\n");
        logger.print("\"interactive-front-weight\" :" + SimulationConfiguration.INTERACTIVE_FRONT_WEIGHT + ",\n");
        logger.print("\"interactive-back-weight\" :" + SimulationConfiguration.INTERACTIVE_BACK_WEIGHT + ",\n");
        logger.print("\"data-incentive-front-weight\" :" + SimulationConfiguration.DATA_INCENTIVE_FRONT_WEIGHT + ",\n");
        logger.print("\"data-incentive-back-weight\" :" + SimulationConfiguration.DATA_INCENTIVE_BACK_WEIGHT + ",\n");
        logger.print(
                "\"data-object-target-type\" :" + "\"" + SimulationConfiguration.DATA_OBJECT_TARGET_TYPE.toString()
                        + "\"" + ",\n");
        logger.print("\"interactive-job-probability\" :" + SimulationConfiguration.INTERAXTIVE_JOB_PROBABILITY + ",\n");

        logger.print("\"micro-data-center-num\" :" + SimulationConfiguration.MICRO_DATA_CENTER_NUM + ",\n");
        logger.print("\"data-center-num\" :" + SimulationConfiguration.DATA_CENTER_NUM + ",\n");
        logger.print("\"total-load-rate\" :" + SimulationConfiguration.TOTAL_LOAD_RATE + ",\n");
        logger.print("\"ave-mdc-container-num\" :" + SimulationConfiguration.AVE_MDC_CONTAINER_NUM + ",\n");
        logger.print("\"ave-dc-container-num\" :" + SimulationConfiguration.AVE_DC_CONTAINER_NUM + ",\n");
        logger.print("\"random-dc-location\" :" + SimulationConfiguration.RANDOM_DC_LOCATION + ",\n");
        logger.print("\"node-cost-weight-type\" :" + "\"" + SimulationConfiguration.NODE_COST_WEIGHT_TYPE.toString()
                + "\"" + ",\n");

        logger.print("\"cost-dc-dc\" :" + SimulationConfiguration.COST_DC_DC + ",\n");
        logger.print("\"cost-dc-mdc\" :" + SimulationConfiguration.COST_DC_MDC + ",\n");
        logger.print("\"cost-mdc-user\" :" + SimulationConfiguration.COST_MDC_USER + ",\n");
        logger.print("\"network-type\" :" + "\"" + SimulationConfiguration.NETWORK_TYPE.toString() + "\"" + ",\n");

        logger.print(
                "\"load-cost-function-type\" :" + "\"" + SimulationConfiguration.LOAD_COST_FUNCTION_TYPE.toString()
                        + "\"" + ",\n");
        logger.print("\"random-threshold\" :" + SimulationConfiguration.RANDOM_NETWORK_THRESHOLD + ",\n");
        logger.print("\"load-cost-threshold\" :" + SimulationConfiguration.LOAD_COST_THRESHOLD + ",\n");
        logger.print("\"network-cost-function-type\" :" + "\""
                + SimulationConfiguration.NETWORK_COST_FUNCTION_TYPE.toString() + "\"" + ",\n");
        logger.print("\"network-distance-threshold\" :" + SimulationConfiguration.NETWORK_DISTANCE_THRESHOLD + ",\n");
        logger.print("\"pow-for-network\" :" + SimulationConfiguration.POW_FOR_NETWORK + ",\n");
        logger.print("\"random-network-threshold\" :" + SimulationConfiguration.RANDOM_NETWORK_THRESHOLD + ",\n");
        logger.print(
                "\"cost-migrate-type\" :" + "\"" + SimulationConfiguration.COST_MIGRATE_TYPE.toString() + "\"" + ",\n");
        logger.print("\"double-buffering\" :" + SimulationConfiguration.doubleBuffering + ",\n");
        logger.print("\"time-unit-num\" :" + SimulationConfiguration.TIME_UNIT_NUM + ",\n");
        logger.print("\"use-cost-dif-randomization\" :" + SimulationConfiguration.useCostDifRandomization + ",\n");
        logger.print("\"cost-gain-threshold\" :" + SimulationConfiguration.COST_GAIN_THRESHOLD + ",\n");
        logger.print("\"use-mig-time-randomization\" :" + SimulationConfiguration.useMigTimeRandomization + ",\n");
        logger.print("\"elapsed-time-threshold\" :" + SimulationConfiguration.ELAPSED_TIME_THRESHOLD + ",\n");

        logger.print("\"end-time\" :" + SimulationConfiguration.END_TIME + ",\n");
    }

    public static void addSimulationData() {
        for (Node node : Simulator.getSimulatedNodes()) {
            nodeXList.add(node.getLocation().getX());
            nodeYList.add(node.getLocation().getY());
            nodeContainerNumList.add(node.getContainerNum());
            nodeLoadThresholdList.add(node.getLoadThrethold());
        }

        for (Link link : Simulator.getSimulatedLinks()) {
            linkSrcList.add(link.getConnectNode1().getNodeId());
            linkDstList.add(link.getConnectNode2().getNodeId());
            linkCostList.add(link.getCost());
        }

        for (User user : Simulator.getSimulatedUsers()) {
            userTypeList.add("\"" + user.getUserType().toString() + "\"");
            userNetworkThresholdList.add(user.getNetworkThreshold());
        }

        // logger.print("\"node-x-list\" :" + nodeXList + ",\n");
        // logger.print("\"node-y-list\" :" + nodeYList + ",\n");
        // logger.print("\"node-load-threshold-list\" :" + nodeLoadThresholdList +
        // ",\n");
        // logger.print("\"link-src-list\" :" + linkSrcList + ",\n");
        // logger.print("\"link-dst-list\" :" + linkDstList + ",\n");
        // logger.print("\"link-cost-list\" :" + linkCostList + ",\n");
        // logger.print("\"user-type-list\" :" + userTypeList + ",\n");
        // logger.print("\"user-network-threshold-list\" :" + userNetworkThresholdList +
        // ",\n");
    }

    public static void addSimulationResult() {
        for (long time = lastAddedTime; time <= SimulationConfiguration.END_TIME; time++) {
            addTimeStepData(time);
        }
        logger.print("\"node-load-list\" :" + nodeLoadList + ",\n");
        // logger.print("\"user-x-list\" :" + userXList + ",\n");
        // logger.print("\"user-y-list\" :" + userYList + ",\n");
        logger.print("\"user-connection-list\" :" + userConnectionList + ",\n");
        logger.print("\"user-connection-distance-list\" :" + userConnectionDistanceList);
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
        ArrayList<Double> userX = new ArrayList<>();
        ArrayList<Double> userY = new ArrayList<>();
        ArrayList<Double> userConnectionDistance = new ArrayList<>();

        for (Node node : Simulator.getSimulatedNodes()) {
            nodeLoad.add(node.getLoad(time));
        }

        for (User user : Simulator.getSimulatedUsers()) {
            userConnction.add(user.getConnectNodeId(time));
            Point userPosition = user.getScenario().apply(time);
            userX.add(userPosition.getX());
            userY.add(userPosition.getY());
            userConnectionDistance.add(user.getConnectDistance(time));
        }

        nodeLoadList.add(nodeLoad);
        userConnectionList.add(userConnction);
        userXList.add(userX);
        userYList.add(userY);
        userConnectionDistanceList.add(userConnectionDistance);
    }
}
