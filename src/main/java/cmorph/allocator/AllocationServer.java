package cmorph.allocator;

import static cmorph.settings.SimulationConfiguration.AVE_JOB_TIME_SLOT;
import static cmorph.settings.SimulationConfiguration.COST_MDC_USER;
import static cmorph.settings.SimulationConfiguration.USER_NUM;

import java.util.ArrayList;
import java.util.Collections;

import cmorph.entities.Link;
import cmorph.entities.Node;
import cmorph.entities.User;
import cmorph.job.Job;
import cmorph.logger.Logger;
import cmorph.simulator.Simulator;
import cmorph.simulator.Timer;

public class AllocationServer {
    /**
     * 状態を更新した時間
     */
    private static long updateTime = 0;

    private static void removeEndJob() {
        ArrayList<Node> nodes = Simulator.getSimulatedNodes();
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).removeEndJob(Timer.getCurrentTime());
        }
    }

    /**
     * 状態を更新する関数
     */
    public static void updateState() {
        if (Timer.getCurrentTime() > updateTime) {
            // ノードの状態を更新
            NodeAllocator.updateNodeLoads(Timer.getCurrentTime() - 1);

            // loggerにデータを追加
            Logger.addTimeStepData(updateTime, Timer.getCurrentTime());

            // 終了したジョブを削除
            removeEndJob();

            // 時間を更新
            updateTime = Timer.getCurrentTime();
        }
    }

    /**
     * ジョブを割り当てる関数
     * 
     * @param job
     */
    public static void allocateJob(Job job) {
        // 最良のコストを求める
        double bestCost = Double.MAX_VALUE;
        int bestNode = -1;
        ArrayList<Node> simulatedNodes = Simulator.getSimulatedNodes();

        for (int i = 0; i < simulatedNodes.size(); i++) {

            double cost = Double.MAX_VALUE;
            if (NodeAllocator.getRemainingContainerNums().get(i) >= job.getUseContainerNum()) {
                cost = getCost(job, i);

                if (cost < bestCost) {
                    bestCost = cost;
                    bestNode = i;
                }

            }
        }

        // ジョブを転送
        Forwarder.forward(job, bestNode, bestCost);
    }

    public static double getCost(Job job, int nodeId) {
        double loadCost = NodeAllocator.getNodeCost(nodeId);
        double frontendPathCost = NetworkAllocator.getFrontendPathCost(
                job.getUser(), nodeId);
        double backendPathCost = NetworkAllocator.getBackendPathCost(nodeId,
                job.getDataObjectNode().getNodeId());
        return loadCost * job.getUseContainerNum() + frontendPathCost * job.getFrontWeight()
                + backendPathCost * job.getBackWeight();
    }
}
