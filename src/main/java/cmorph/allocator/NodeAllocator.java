package cmorph.allocator;

import static cmorph.settings.SimulationConfiguration.AVE_JOB_CONTAINER_NUM;
import static cmorph.settings.SimulationConfiguration.AVE_MDC_CONTAINER_NUM;
import static cmorph.settings.SimulationConfiguration.DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.MICRO_DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.TIME_UNIT_NUM;
import static cmorph.settings.SimulationConfiguration.USER_NUM;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import cmorph.entities.Node;
import cmorph.simulator.Simulator;

public class NodeAllocator {
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

    public static double getNodeCost(int nodeId) {
        return PseudoCostFunctions.getLoadCost(nodeLoads.get(nodeId),
                Simulator.getSimulatedNodes().get(nodeId).getLoadThrethold())
                * Simulator.getSimulatedNodes().get(nodeId).getCostWeight();
    }

    public static ArrayList<Double> getNodeLoads() {
        return nodeLoads;
    }

    public static ArrayList<Integer> getRemainingContainerNums() {
        return remainingContainerNums;
    }

}
