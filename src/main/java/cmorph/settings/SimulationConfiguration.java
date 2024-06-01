package cmorph.settings;

import cmorph.allocator.NetworkAllocator.networkType;
import cmorph.allocator.NodeAllocator.LoadCostFunctionType;

import java.net.CookieStore;

import cmorph.allocator.AllocationServer.CostMigrateType;
import cmorph.allocator.NetworkAllocator.NetworkCostFunctionType;
import cmorph.entities.Node;
import cmorph.setUp.JobSetUp.DataObjectTargetType;
import cmorph.setUp.NodeSetUp.NodeCostWeightType;
import cmorph.setUp.UserSetUp.Scenario;
import cmorph.setUp.UserSetUp.UserLocationScenario;
import cmorph.setUp.UserSetUp.UserSpawnScenario;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimulationConfiguration {
        // Map
        public static final int MAP_WIDTH = 300;
        public static final int MAP_HEIGHT = 300;

        // User
        public static final int USER_NUM = 160;
        // public static final int USER_NUM = 64;
        public static UserLocationScenario USER_LOCATION_SCENARIO = UserLocationScenario.RANDOM_DOWN;
        public static final UserSpawnScenario USER_SPAWN_SCENARIO = UserSpawnScenario.BEGINNING;
        public static final boolean IS_ROOP = true;

        // Job
        public static final boolean RANDOM_JOB_TIME_SLOT = true;
        public static final boolean RANDOM_JOB_CONTAINER_NUM = false;
        public static final double RANDOMIZE_RATE = 0.5;
        public static final int AVE_JOB_TIME_SLOT = 160;
        public static final int AVE_JOB_CONTAINER_NUM = 100;
        public static final int INTERACTIVE_FRONT_WEIGHT = 100;
        public static final int INTERACTIVE_BACK_WEIGHT = 0;
        public static final int DATA_INCENTIVE_FRONT_WEIGHT = 1;
        public static final int DATA_INCENTIVE_BACK_WEIGHT = 10;
        public static final DataObjectTargetType DATA_OBJECT_TARGET_TYPE = DataObjectTargetType.DC;
        // public static final int AVE_DATA_OBJECT_SIZE = 15000 / USER_NUM; // Kbit
        public static final double INTERAXTIVE_JOB_PROBABILITY = 1;

        // Node
        public static final int MICRO_DATA_CENTER_NUM = 0;
        public static final int DATA_CENTER_NUM = 16;
        public static double TOTAL_LOAD_RATE = 0.6;
        public static final double DC_MDC_RATE = 3;
        public static int AVE_MDC_CONTAINER_NUM = (int) (USER_NUM * AVE_JOB_CONTAINER_NUM
                        / TOTAL_LOAD_RATE
                        / (MICRO_DATA_CENTER_NUM + DC_MDC_RATE * DATA_CENTER_NUM));
        public static int AVE_DC_CONTAINER_NUM = (int) (USER_NUM * AVE_JOB_CONTAINER_NUM * DC_MDC_RATE
                        / TOTAL_LOAD_RATE
                        / (MICRO_DATA_CENTER_NUM + DC_MDC_RATE * DATA_CENTER_NUM));

        public static final boolean RANDOM_DC_LOCATION = false;
        public static final NodeCostWeightType NODE_COST_WEIGHT_TYPE = NodeCostWeightType.EQUALITY;

        // Network
        public static final int COST_DC_DC = 1;
        public static final int COST_DC_MDC = 2;
        public static final int COST_MDC_USER = 2;
        public static final networkType NETWORK_TYPE = networkType.WIRELESS;

        // AllocationServer
        public static LoadCostFunctionType LOAD_COST_FUNCTION_TYPE = LoadCostFunctionType.CONVEX;
        public static final boolean RANDOM_THRETHOLD = false;
        public static final double LOAD_COST_THRESHOLD = 0.75;
        public static NetworkCostFunctionType NETWORK_COST_FUNCTION_TYPE = NetworkCostFunctionType.CONSTANT;
        public static int POW_FOR_NETWORK = 4;
        public static final double NETWORK_DISTANCE_THRESHOLD = 2 * MAP_WIDTH / (Math.sqrt(DATA_CENTER_NUM) + 1);
        public static final boolean RANDOM_NETWORK_THRESHOLD = false;

        public static final CostMigrateType COST_MIGRATE_TYPE = CostMigrateType.SUM;

        public static final boolean doubleBuffering = false;
        public static final int TIME_UNIT_NUM = 1;
        public static final boolean useCostDifRandomization = false;
        public static final double COST_GAIN_THRESHOLD = 0.2;
        public static final boolean useMigTimeRandomization = false;
        public static final int ELAPSED_TIME_THRESHOLD = 5000;
        // Simulator
        public static final long END_TIME = 10000; // msec

        public static String parseOption(String[] args) {
                if (args.length == 0) {
                        LocalDateTime nowDate = LocalDateTime.now();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                        String formatNowDate = dtf.format(nowDate);
                        String fileName = "output-" + formatNowDate + ".json";
                        return fileName;
                }
                String fileName = "";
                for (int i = 0; i < args.length; i++) {
                        switch (args[i]) {
                                case "-scenario":
                                        if (i + 1 < args.length) {
                                                if (args[i + 1].equals("random")) {
                                                        USER_LOCATION_SCENARIO = UserLocationScenario.RANDOM;
                                                        fileName += "random";
                                                } else if (args[i + 1].equals("centerLeft")) {
                                                        USER_LOCATION_SCENARIO = UserLocationScenario.CENTER_LEFT_RANDOM_DOWN;
                                                        fileName += "centerLeft";
                                                }
                                                i++;
                                        }
                                        break;
                                case "-totalLoad":
                                        if (i + 1 < args.length) {
                                                TOTAL_LOAD_RATE = Double.parseDouble(args[i + 1]);
                                                AVE_MDC_CONTAINER_NUM = (int) (USER_NUM * AVE_JOB_CONTAINER_NUM
                                                                / TOTAL_LOAD_RATE
                                                                / (MICRO_DATA_CENTER_NUM
                                                                                + DC_MDC_RATE * DATA_CENTER_NUM));
                                                AVE_DC_CONTAINER_NUM = (int) (USER_NUM * AVE_JOB_CONTAINER_NUM
                                                                * DC_MDC_RATE
                                                                / TOTAL_LOAD_RATE
                                                                / (MICRO_DATA_CENTER_NUM
                                                                                + DC_MDC_RATE * DATA_CENTER_NUM));
                                                i++;
                                                fileName += args[i];
                                        }
                                        break;
                                case "-serverFunc":
                                        if (i + 1 < args.length) {
                                                if (args[i + 1].equals("monotonic")) {
                                                        LOAD_COST_FUNCTION_TYPE = LoadCostFunctionType.MONOTONIC;
                                                        fileName += "-monotonic";
                                                } else if (args[i + 1].equals("convex")) {
                                                        LOAD_COST_FUNCTION_TYPE = LoadCostFunctionType.CONVEX;
                                                        fileName += "-convex";
                                                } else if (args[i + 1].equals("constant")) {
                                                        LOAD_COST_FUNCTION_TYPE = LoadCostFunctionType.CONSTANT;
                                                        fileName += "-constant";
                                                }
                                                i++;
                                        }
                                        break;
                                case "-networkFunc":
                                        if (i + 1 < args.length) {
                                                if (args[i + 1].equals("constant")) {
                                                        NETWORK_COST_FUNCTION_TYPE = NetworkCostFunctionType.CONSTANT;
                                                        fileName += "-constant";
                                                } else if (args[i + 1].equals("pow")) {
                                                        NETWORK_COST_FUNCTION_TYPE = NetworkCostFunctionType.POW;
                                                        POW_FOR_NETWORK = Integer.parseInt(args[i + 2]);
                                                        fileName += "-pow" + args[i + 2];
                                                        i++;
                                                }
                                                i++;
                                        }
                                        break;
                                default:
                                        break;
                        }
                }
                return fileName + ".json";
        }
}
