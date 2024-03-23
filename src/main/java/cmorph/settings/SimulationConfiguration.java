package cmorph.settings;

import cmorph.allocator.NetworkAllocator.networkType;
import cmorph.allocator.PseudoCostFunctions.LoadCostFunctionType;
import cmorph.allocator.PseudoCostFunctions.NetworkCostFunctionType;
import cmorph.entities.Node;
import cmorph.setUp.JobSetUp.DataObjectTargetType;
import cmorph.setUp.NodeSetUp.NodeCostWeightType;
import cmorph.setUp.UserSetUp.Scenario;
import cmorph.setUp.UserSetUp.UserLocationScenario;
import cmorph.setUp.UserSetUp.UserSpawnScenario;

public class SimulationConfiguration {
        // Map
        public static final int MAP_WIDTH = 300;
        public static final int MAP_HEIGHT = 300;

        // User
        public static final int USER_NUM = 160;
        public static final UserLocationScenario USER_LOCATION_SCENARIO = UserLocationScenario.RANDOM_DOWN;
        public static final UserSpawnScenario USER_SPAWN_SCENARIO = UserSpawnScenario.RANDOM;

        // Job
        public static final boolean RANDOM_JOB_TIME_SLOT = false;
        public static final boolean RANDOM_JOB_CONTAINER_NUM = true;
        public static final double RANDOMIZE_RATE = 0.05;
        public static final int AVE_JOB_TIME_SLOT = 10;
        public static final int AVE_JOB_CONTAINER_NUM = 100;
        public static final int INTERACTIVE_FRONT_WEIGHT = 100;
        public static final int INTERACTIVE_BACK_WEIGHT = 0;
        public static final int DATA_INCENTIVE_FRONT_WEIGHT = 1;
        public static final int DATA_INCENTIVE_BACK_WEIGHT = 10;
        public static final DataObjectTargetType DATA_OBJECT_TARGET_TYPE = DataObjectTargetType.DC;
        public static final int AVE_DATA_OBJECT_SIZE = 15000 / USER_NUM; // Kbit
        public static final double INTERAXTIVE_JOB_PROBABILITY = 1;

        // Node
        public static final int MICRO_DATA_CENTER_NUM = 0;
        public static final int DATA_CENTER_NUM = 16;
        public static final double TOTAL_LOAD_RATE = 1;
        public static final double DC_MDC_RATE = 3;
        public static final int AVE_MDC_CONTAINER_NUM = (int) (USER_NUM * AVE_JOB_CONTAINER_NUM
                        / TOTAL_LOAD_RATE
                        / (MICRO_DATA_CENTER_NUM + DC_MDC_RATE * DATA_CENTER_NUM));
        public static final int AVE_DC_CONTAINER_NUM = (int) (USER_NUM * AVE_JOB_CONTAINER_NUM * DC_MDC_RATE
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
        public static final LoadCostFunctionType LOAD_COST_FUNCTION_TYPE = LoadCostFunctionType.CONVEX;
        public static final boolean RANDOM_THRETHOLD = false;
        public static final double LOAD_COST_THRESHOLD = 0.75;
        public static final NetworkCostFunctionType NETWORK_COST_FUNCTION_TYPE = NetworkCostFunctionType.MONOTONIC;
        public static final double NETWORK_DISTANCE_LIMIT = Math.sqrt(MAP_HEIGHT * MAP_HEIGHT + MAP_WIDTH * MAP_WIDTH);
        public static final boolean RANDOM_NETWORK_THRESHOLD = false;
        public static final int TIME_UNIT_NUM = 1;
        public static final boolean useCostDifRandomization = true;
        public static final double COST_GAIN_THRESHOLD = 0.2;
        public static final boolean useMigTimeRandomization = true;
        public static final int ELAPSED_TIME_THRESHOLD = 5000;
        // Simulator
        public static final long END_TIME = 10000; // msec
}
