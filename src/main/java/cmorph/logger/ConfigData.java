package cmorph.logger;

import java.util.ArrayList;

import cmorph.allocator.PseudoCostFunctions.LoadCostFunctionType;
import cmorph.allocator.PseudoCostFunctions.NetworkCostFunctionType;
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

    private boolean randomJobTimeSlot;
    private double randomizeRate;
    private int aveJobTimeSlot;
    private int aveJobContainerNum;
    private int frontWeight;
    private int backWeight;
    private DataObjectTargetType dataObjectTargetType;
    private int aveDataObjectSize;

    private int microDataCenterNum;
    private int dataCenterNum;
    private int aveMDCContainerNum;
    private int aveDCContainerNum;
    private boolean randomNodeLocation;
    private NodeCostWeightType nodeCostWeightType;

    private NetworkCostFunctionType networkCostFunctionType;
    private int networkTimeUnitNum;

    private LoadCostFunctionType loadCostFunctionType;
    private int timeUnitNum;
    private boolean useCostDifRandomization;
    private double costGainThreshold;
    private boolean useMigTimeRandomization;
    private int elapsedTimeThreshold;

    private long endTime;

    private ArrayList<Double> nodeXList;
    private ArrayList<Double> nodeYList;
    private ArrayList<Integer> nodeContainerNumList;

    private ArrayList<Integer> linkSrcList;
    private ArrayList<Integer> linkDstList;
    private ArrayList<Integer> linkBandWidthList;

    public ConfigData() {
    }

    public ConfigData(ArrayList<Node> nodes) {
        this.mapWidth = SimulationConfiguration.MAP_WIDTH;
        this.mapHeight = SimulationConfiguration.MAP_HEIGHT;
        this.userNum = SimulationConfiguration.USER_NUM;
        this.userLocationScenario = SimulationConfiguration.USER_LOCATION_SCENARIO;
        this.userSpawnScenario = SimulationConfiguration.USER_SPAWN_SCENARIO;
        this.randomJobTimeSlot = SimulationConfiguration.RANDOM_JOB_TIME_SLOT;
        this.randomizeRate = SimulationConfiguration.RANDOMIZE_RATE;
        this.aveJobTimeSlot = SimulationConfiguration.AVE_JOB_TIME_SLOT;
        this.aveJobContainerNum = SimulationConfiguration.AVE_JOB_CONTAINER_NUM;
        this.frontWeight = SimulationConfiguration.FRONT_WEIGHT;
        this.backWeight = SimulationConfiguration.BACK_WEIGHT;
        this.dataObjectTargetType = SimulationConfiguration.DATA_OBJECT_TARGET_TYPE;
        this.aveDataObjectSize = SimulationConfiguration.AVE_DATA_OBJECT_SIZE;

        this.microDataCenterNum = SimulationConfiguration.MICRO_DATA_CENTER_NUM;
        this.dataCenterNum = SimulationConfiguration.DATA_CENTER_NUM;
        this.aveMDCContainerNum = SimulationConfiguration.AVE_MDC_CONTAINER_NUM;
        this.aveDCContainerNum = SimulationConfiguration.AVE_DC_CONTAINER_NUM;
        this.randomNodeLocation = SimulationConfiguration.RANDOM_NODE_LOCATION;
        this.nodeCostWeightType = SimulationConfiguration.NODE_COST_WEIGHT_TYPE;

        this.networkCostFunctionType = SimulationConfiguration.NETWORK_COST_FUNCTION_TYPE;
        this.networkTimeUnitNum = SimulationConfiguration.NETWORK_TIME_UNIT_NUM;

        this.loadCostFunctionType = SimulationConfiguration.LOAD_COST_FUNCTION_TYPE;
        this.timeUnitNum = SimulationConfiguration.TIME_UNIT_NUM;
        this.useCostDifRandomization = SimulationConfiguration.useCostDifRandomization;
        this.costGainThreshold = SimulationConfiguration.COST_GAIN_THRESHOLD;
        this.useMigTimeRandomization = SimulationConfiguration.useMigTimeRandomization;
        this.elapsedTimeThreshold = SimulationConfiguration.ELAPSED_TIME_THRESHOLD;
        this.endTime = SimulationConfiguration.END_TIME;

        this.nodeXList = new ArrayList<>();
        this.nodeYList = new ArrayList<>();
        this.nodeContainerNumList = new ArrayList<>();
        for (int i = 0; i < SimulationConfiguration.MICRO_DATA_CENTER_NUM
                + SimulationConfiguration.DATA_CENTER_NUM; i++) {
            this.nodeXList.add(nodes.get(i).getLocation().getX());
            this.nodeYList.add(nodes.get(i).getLocation().getY());
            this.nodeContainerNumList.add(nodes.get(i).getContainerNum());
        }
        this.linkSrcList = new ArrayList<>();
        this.linkDstList = new ArrayList<>();
        this.linkBandWidthList = new ArrayList<>();
        for (int i = 0; i < Simulator.getSimulatedLinks().size(); i++) {
            this.linkSrcList.add(Simulator.getSimulatedLinks().get(i).getConnectNode1().getNodeId());
            this.linkDstList.add(Simulator.getSimulatedLinks().get(i).getConnectNode2().getNodeId());
            this.linkBandWidthList.add(Simulator.getSimulatedLinks().get(i).getBandWidth());
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

    public UserSpawnScenario getUserSpawnScenario() {
        return userSpawnScenario;
    }

    public boolean getRandomJobTimeSlot() {
        return randomJobTimeSlot;
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

    public int getFrontWeight() {
        return frontWeight;
    }

    public int getBackWeight() {
        return backWeight;
    }

    public DataObjectTargetType getDataObjectTargetType() {
        return dataObjectTargetType;
    }

    public int getAveDataObjectSize() {
        return aveDataObjectSize;
    }

    public int getMicroDataCenterNum() {
        return microDataCenterNum;
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

    public NetworkCostFunctionType getNetworkCostFunctionType() {
        return networkCostFunctionType;
    }

    public int getNetworkTimeUnitNum() {
        return networkTimeUnitNum;
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

    public ArrayList<Integer> getLinkSrcList() {
        return linkSrcList;
    }

    public ArrayList<Integer> getLinkDstList() {
        return linkDstList;
    }

    public ArrayList<Integer> getLinkBandWidthList() {
        return linkBandWidthList;
    }
}
