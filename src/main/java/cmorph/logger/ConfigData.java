package cmorph.logger;

import java.util.ArrayList;

import cmorph.allocator.AllocationServer.CostMigrateType;
import cmorph.allocator.NetworkAllocator.NetworkCostFunctionType;
import cmorph.allocator.NetworkAllocator.networkType;
import cmorph.allocator.NodeAllocator.LoadCostFunctionType;
import cmorph.entities.Node;
import cmorph.setUp.JobSetUp.DataObjectTargetType;
import cmorph.setUp.NodeSetUp.NodeCostWeightType;
import cmorph.setUp.UserSetUp.UserLocationScenario;
import cmorph.setUp.UserSetUp.UserSpawnScenario;

import cmorph.settings.SimulationConfiguration;
import cmorph.simulator.Simulator;

public class ConfigData {
    private int mapWidth;
    private int mapHeight;

    private int userNum;
    private UserLocationScenario userLocationScenario;
    private UserSpawnScenario userSpawnScenario;
    private boolean isRoop;

    private boolean randomJobTimeSlot;
    private boolean randomJobContainerNum;
    private double randomizeRate;
    private int aveJobTimeSlot;
    private int aveJobContainerNum;
    private int interactiveFrontWeight;
    private int interactiveBackWeight;
    private int dataIncentiveFrontWeight;
    private int dataIncentiveBackWeight;
    private DataObjectTargetType dataObjectTargetType;
    // private int aveDataObjectSize;
    private double interactiveJobProbability;

    private int microDataCenterNum;
    private int dataCenterNum;
    private int aveMDCContainerNum;
    private int aveDCContainerNum;
    private boolean randomNodeLocation;
    private NodeCostWeightType nodeCostWeightType;

    private int costDCMDC;
    private int costDCDC;
    private int costMDCUser;
    private networkType networkType;

    private LoadCostFunctionType loadCostFunctionType;
    private double loadCostThreshold;
    private boolean randomThreshold;
    private NetworkCostFunctionType networkCostFunctionType;
    private int powForNetwork;
    private double networkDistanceThreshold;
    private boolean randomNetworkThreshold;

    private CostMigrateType costMigrateType;

    private boolean doubleBuffering;
    private int timeUnitNum;
    private boolean useCostDifRandomization;
    private double costGainThreshold;
    private boolean useMigTimeRandomization;
    private int elapsedTimeThreshold;

    private long endTime;

    private ArrayList<Double> nodeXList;
    private ArrayList<Double> nodeYList;
    private ArrayList<Integer> nodeContainerNumList;
    private ArrayList<Double> nodeLoadThresholdList;

    private ArrayList<Integer> linkSrcList;
    private ArrayList<Integer> linkDstList;
    private ArrayList<Integer> linkCostList;

    public ConfigData() {
    }

    public ConfigData(ArrayList<Node> nodes) {
        this.mapWidth = SimulationConfiguration.MAP_WIDTH;
        this.mapHeight = SimulationConfiguration.MAP_HEIGHT;

        this.userNum = SimulationConfiguration.USER_NUM;
        this.userLocationScenario = SimulationConfiguration.USER_LOCATION_SCENARIO;
        this.userSpawnScenario = SimulationConfiguration.USER_SPAWN_SCENARIO;
        this.isRoop = SimulationConfiguration.IS_ROOP;

        this.randomJobTimeSlot = SimulationConfiguration.RANDOM_JOB_TIME_SLOT;
        this.randomJobContainerNum = SimulationConfiguration.RANDOM_JOB_CONTAINER_NUM;
        this.randomizeRate = SimulationConfiguration.RANDOMIZE_RATE;
        this.aveJobTimeSlot = SimulationConfiguration.AVE_JOB_TIME_SLOT;
        this.aveJobContainerNum = SimulationConfiguration.AVE_JOB_CONTAINER_NUM;
        this.interactiveFrontWeight = SimulationConfiguration.INTERACTIVE_FRONT_WEIGHT;
        this.interactiveBackWeight = SimulationConfiguration.INTERACTIVE_BACK_WEIGHT;
        this.dataIncentiveFrontWeight = SimulationConfiguration.DATA_INCENTIVE_BACK_WEIGHT;
        this.dataIncentiveBackWeight = SimulationConfiguration.DATA_INCENTIVE_FRONT_WEIGHT;
        this.interactiveJobProbability = SimulationConfiguration.INTERAXTIVE_JOB_PROBABILITY;

        this.microDataCenterNum = SimulationConfiguration.MICRO_DATA_CENTER_NUM;
        this.dataCenterNum = SimulationConfiguration.DATA_CENTER_NUM;
        this.aveMDCContainerNum = SimulationConfiguration.AVE_MDC_CONTAINER_NUM;
        this.aveDCContainerNum = SimulationConfiguration.AVE_DC_CONTAINER_NUM;
        this.randomNodeLocation = SimulationConfiguration.RANDOM_DC_LOCATION;
        this.nodeCostWeightType = SimulationConfiguration.NODE_COST_WEIGHT_TYPE;

        this.costDCDC = SimulationConfiguration.COST_DC_DC;
        this.costDCMDC = SimulationConfiguration.COST_DC_MDC;
        this.costMDCUser = SimulationConfiguration.COST_MDC_USER;
        this.networkType = SimulationConfiguration.NETWORK_TYPE;

        this.loadCostFunctionType = SimulationConfiguration.LOAD_COST_FUNCTION_TYPE;
        this.loadCostThreshold = SimulationConfiguration.LOAD_COST_THRESHOLD;
        this.randomThreshold = SimulationConfiguration.RANDOM_THRETHOLD;
        this.networkCostFunctionType = SimulationConfiguration.NETWORK_COST_FUNCTION_TYPE;
        this.powForNetwork = SimulationConfiguration.POW_FOR_NETWORK;
        this.networkDistanceThreshold = SimulationConfiguration.NETWORK_DISTANCE_THRESHOLD;
        this.randomNetworkThreshold = SimulationConfiguration.RANDOM_NETWORK_THRESHOLD;

        this.costMigrateType = SimulationConfiguration.COST_MIGRATE_TYPE;

        this.doubleBuffering = SimulationConfiguration.doubleBuffering;
        this.timeUnitNum = SimulationConfiguration.TIME_UNIT_NUM;
        this.useCostDifRandomization = SimulationConfiguration.useCostDifRandomization;
        this.costGainThreshold = SimulationConfiguration.COST_GAIN_THRESHOLD;
        this.useMigTimeRandomization = SimulationConfiguration.useMigTimeRandomization;
        this.elapsedTimeThreshold = SimulationConfiguration.ELAPSED_TIME_THRESHOLD;
        this.endTime = SimulationConfiguration.END_TIME;

        this.nodeXList = new ArrayList<>();
        this.nodeYList = new ArrayList<>();
        this.nodeContainerNumList = new ArrayList<>();
        this.nodeLoadThresholdList = new ArrayList<>();
        for (int i = 0; i < SimulationConfiguration.MICRO_DATA_CENTER_NUM
                + SimulationConfiguration.DATA_CENTER_NUM; i++) {
            this.nodeXList.add(nodes.get(i).getLocation().getX());
            this.nodeYList.add(nodes.get(i).getLocation().getY());
            this.nodeContainerNumList.add(nodes.get(i).getContainerNum());
            this.nodeLoadThresholdList.add(nodes.get(i).getLoadThrethold());
        }
        this.linkSrcList = new ArrayList<>();
        this.linkDstList = new ArrayList<>();
        this.linkCostList = new ArrayList<>();
        for (int i = 0; i < Simulator.getSimulatedLinks().size(); i++) {
            this.linkSrcList.add(Simulator.getSimulatedLinks().get(i).getConnectNode1().getNodeId());
            this.linkDstList.add(Simulator.getSimulatedLinks().get(i).getConnectNode2().getNodeId());
            this.linkCostList.add(Simulator.getSimulatedLinks().get(i).getCost());
        }
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getUserNum() {
        return userNum;
    }

    public UserLocationScenario getUserLocationScenario() {
        return userLocationScenario;
    }

    public boolean getIsRoop() {
        return isRoop;
    }

    public UserSpawnScenario getUserSpawnScenario() {
        return userSpawnScenario;
    }

    public boolean getRandomJobTimeSlot() {
        return randomJobTimeSlot;
    }

    public boolean getRandomJobContainerNum() {
        return randomJobContainerNum;
    }

    public double getRandomizeRate() {
        return randomizeRate;
    }

    public int getAveJobTimeSlot() {
        return aveJobTimeSlot;
    }

    public int getAveJobContainerNum() {
        return aveJobContainerNum;
    }

    public int getInteractiveFrontWeight() {
        return interactiveFrontWeight;
    }

    public int getInteractiveBackWeight() {
        return interactiveBackWeight;
    }

    public int getDataIncentiveFrontWeight() {
        return dataIncentiveFrontWeight;
    }

    public int getDataIncentiveBackWeight() {
        return dataIncentiveBackWeight;
    }

    public DataObjectTargetType getDataObjectTargetType() {
        return dataObjectTargetType;
    }

    public double getInteractiveJobProbability() {
        return interactiveJobProbability;
    }

    public int getMicroDataCenterNum() {
        return microDataCenterNum;
    }

    public int getCostDCMDC() {
        return costDCMDC;
    }

    public int getCostDCDC() {
        return costDCDC;
    }

    public int getCostMDCUser() {
        return costMDCUser;
    }

    public networkType getNetworkType() {
        return networkType;
    }

    public int getDataCenterNum() {
        return dataCenterNum;
    }

    public int getAveMDCContainerNum() {
        return aveMDCContainerNum;
    }

    public int getAveDCContainerNum() {
        return aveDCContainerNum;
    }

    public boolean getRandomNodeLocation() {
        return randomNodeLocation;
    }

    public NodeCostWeightType getNodeCostWeightType() {
        return nodeCostWeightType;
    }

    public LoadCostFunctionType getLoadCostFunctionType() {
        return loadCostFunctionType;
    }

    public double getLoadCostThreshold() {
        return loadCostThreshold;
    }

    public boolean getRandomThreshold() {
        return randomThreshold;
    }

    public NetworkCostFunctionType getNetworkCostFunctionType() {
        return networkCostFunctionType;
    }

    public int getPowForNetwork() {
        return powForNetwork;
    }

    public double getNetworkDistanceThreshold() {
        return networkDistanceThreshold;
    }

    public boolean getRandomNetworkThreshold() {
        return randomNetworkThreshold;
    }

    public CostMigrateType getCostMigrateType() {
        return costMigrateType;
    }

    public boolean getDoubleBuffering() {
        return doubleBuffering;
    }

    public int getTimeUnitNum() {
        return timeUnitNum;
    }

    public boolean getUseCostDifRandomization() {
        return useCostDifRandomization;
    }

    public double getCostGainThreshold() {
        return costGainThreshold;
    }

    public boolean getUseMigTimeRandomization() {
        return useMigTimeRandomization;
    }

    public int getElapsedTimeThreshold() {
        return elapsedTimeThreshold;
    }

    public long getEndTime() {
        return endTime;
    }

    public ArrayList<Double> getNodeXList() {
        return nodeXList;
    }

    public ArrayList<Double> getNodeYList() {
        return nodeYList;
    }

    public ArrayList<Integer> getNodeContainerNumList() {
        return nodeContainerNumList;
    }

    public ArrayList<Double> getNodeLoadThresholdList() {
        return nodeLoadThresholdList;
    }

    public ArrayList<Integer> getLinkSrcList() {
        return linkSrcList;
    }

    public ArrayList<Integer> getLinkDstList() {
        return linkDstList;
    }

    public ArrayList<Integer> getLinkCostList() {
        return linkCostList;
    }
}
