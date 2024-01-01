package cmorph.settings;

import cmorph.cost.PseudoCostFunctions.LoadCostFunctionType;
import cmorph.setUp.JobSetUp.DataObjectTargetType;
import cmorph.setUp.UserSetUp.Scenario;
import cmorph.setUp.UserSetUp.UserLocationScenario;
import cmorph.setUp.UserSetUp.UserSpawnScenario;

public class SimulationConfiguration {
    // Map
    public static final int MAP_WIDTH = 300;
    public static final int MAP_HEIGHT = 300;

    // User
    public static final int USER_NUM = 10000;
    public static final UserLocationScenario USER_LOCATION_SCENARIO = UserLocationScenario.STAY;
    public static final UserSpawnScenario USER_SPAWN_SCENARIO = UserSpawnScenario.MOUNTAIN;

    // Job
    public static final boolean RANDOM_JOB_TIME_SLOT = false;
    public static final double RANDOMIZE_RATE = 0.05;
    public static final int AVE_JOB_TIME_SLOT = 10;
    public static final int AVE_JOB_CONTAINER_NUM = 10;
    public static final int FRONT_WEIGHT = 1;
    public static final int BACK_WEIGHT = 1;
    public static final DataObjectTargetType DATA_OBJECT_TARGET_TYPE = DataObjectTargetType.RANDOM;

    // Node
    public static final int MICRO_DATA_CENTER_NUM = 4;
    public static final int DATA_CENTER_NUM = 0;
    public static final int AVE_MDC_CONTAINER_NUM = 30000;
    public static final int AVE_DC_CONTAINER_NUM = 100000;
    public static final boolean RANDOM_NODE_LOCATION = false;

    // AllocationServer
    public static final LoadCostFunctionType LOAD_COST_FUNCTION_TYPE = LoadCostFunctionType.CONVEX;
    public static final int TIME_UNIT_NUM = 1;
    public static final boolean useCostDifRandomization = true;
    public static final double COST_GAIN_THRESHOLD = 0.2;
    public static final boolean useMigTimeRandomization = true;
    public static final int ELAPSED_TIME_THRESHOLD = 5000;
    // Simulator
    public static final long END_TIME = 100000;
}
