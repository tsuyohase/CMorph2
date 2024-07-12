package cmorph.allocator;

import static cmorph.settings.SimulationConfiguration.AVE_JOB_CONTAINER_NUM;
import static cmorph.settings.SimulationConfiguration.AVE_MDC_CONTAINER_NUM;
import static cmorph.settings.SimulationConfiguration.DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.LOAD_COST_FUNCTION_TYPE;
import static cmorph.settings.SimulationConfiguration.MICRO_DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.TIME_UNIT_NUM;
import static cmorph.settings.SimulationConfiguration.USER_NUM;
import static cmorph.settings.SimulationConfiguration.doubleBuffering;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import cmorph.entities.Node;
import cmorph.simulator.Simulator;
import cmorph.simulator.Timer;

public class NodeAllocator {
    public static enum LoadCostFunctionType {
        CONVEX,
        MONOTONIC,
        CONSTANT,
        BINARY,
    }

    /**
     * ノードの負荷を格納するリスト
     */
    private static final ArrayList<Double> nodeLoads = new ArrayList<>(
            Collections.nCopies(MICRO_DATA_CENTER_NUM + DATA_CENTER_NUM, 0.0));

    /**
     * ノードの残りコンテナ数を格納するリスト
     */
    private static final ArrayList<Integer> remainingContainerNums = new ArrayList<>(
            Collections.nCopies(MICRO_DATA_CENTER_NUM + DATA_CENTER_NUM, AVE_MDC_CONTAINER_NUM));

    /**
     * ある時間 のノードの負荷を更新する関数
     */
    public static void updateNodeLoads(long time) {
        ArrayList<Node> simulatedNodes = Simulator.getSimulatedNodes();
        for (int i = 0; i < simulatedNodes.size(); i++) {
            Node node = simulatedNodes.get(i);

            // ノードの負荷を更新
            double loadAverage = 0;
            for (int j = 0; j < TIME_UNIT_NUM; j++) {
                if (time - j >= 0) {
                    loadAverage += node.getLoad(time - j);
                } else {
                    break;
                }
            }
            loadAverage /= Math.min(TIME_UNIT_NUM, time + 1);
            nodeLoads.set(i, loadAverage);

            // ノードの残りコンテナ数を更新
            remainingContainerNums.set(i, node.getRemainingContainerNum(time));
        }
    }

    /**
     * ノードのコストを返す関数
     * 
     * @param nodeId
     * @return
     */
    public static double getNodeCost(int nodeId) {
        double nodeLoad = nodeLoads.get(nodeId);
        Node node = Simulator.getSimulatedNodes().get(nodeId);
        if (!doubleBuffering) {
            nodeLoad = node.getLoad(Timer.getCurrentTime());
        }
        double cost = 0;
        if (LOAD_COST_FUNCTION_TYPE == LoadCostFunctionType.CONVEX) {
            cost = PseudoCostFunctions.adjustableConvexPseudoCostFunction(nodeLoad, node.getLoadThrethold());
        } else if (LOAD_COST_FUNCTION_TYPE == LoadCostFunctionType.MONOTONIC) {
            cost = PseudoCostFunctions.monotonicCostFunction(nodeLoad);
        } else if (LOAD_COST_FUNCTION_TYPE == LoadCostFunctionType.CONSTANT) {
            cost = 0;
        } else if (LOAD_COST_FUNCTION_TYPE == LoadCostFunctionType.BINARY) {
            cost = PseudoCostFunctions.binaryCostFunction(nodeLoad, node.getLoadThrethold());
        }
        return cost * node.getCostWeight();
    }

    public static double getNodeLoads(int nodeId) {
        if (doubleBuffering) {
            return nodeLoads.get(nodeId);
        } else {
            return Simulator.getSimulatedNodes().get(nodeId).getLoad(Timer.getCurrentTime());
        }
    }

    public static int getRemainingContainerNums(int nodeId) {
        if (doubleBuffering) {
            return remainingContainerNums.get(nodeId);
        } else {
            return Simulator.getSimulatedNodes().get(nodeId).getRemainingContainerNum(Timer.getCurrentTime());
        }
    }

}
