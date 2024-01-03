package cmorph.allocator;

import static cmorph.settings.SimulationConfiguration.NETWORK_TIME_UNIT_NUM;
import static cmorph.settings.SimulationConfiguration.USER_NUM;

import java.util.ArrayList;
import java.util.Collections;

import cmorph.entities.Link;
import cmorph.entities.Node;
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
        ArrayList<Link> links = Simulator.getSimulatedLinks();
        for (int i = 0; i < links.size(); i++) {
            links.get(i).removeEndJob(Timer.getCurrentTime() - NETWORK_TIME_UNIT_NUM);
        }
    }

    /**
     * 状態を更新する関数
     */
    public static void updateState() {
        if (Timer.getCurrentTime() > updateTime) {
            // ノードの状態を更新
            NodeAllocator.updateNodeLoads(Timer.getCurrentTime() - 1);

            // ネットワークの状態を更新
            NetworkAllocator.updateLinkLoads(Timer.getCurrentTime() - 1);

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
        ArrayList<Integer> bestBackendPath = null;

        ArrayList<Node> simulatedNodes = Simulator.getSimulatedNodes();

        for (int i = 0; i < simulatedNodes.size(); i++) {
            double cost = Double.MAX_VALUE;
            if (NodeAllocator.getRemainingContainerNums().get(i) >= job.getUseContainerNum()) {
                double loadCost = NodeAllocator.getNodeCost(i);
                double frontendPathCost = 0;
                ArrayList<Integer> backendPath = NetworkAllocator.getBackendPath(i,
                        job.getDataObjectNode().getNodeId());
                double backendPathCost = NetworkAllocator.getBackendPathCost(backendPath);
                cost = loadCost * job.getUseContainerNum() + frontendPathCost * job.getFrontWeight()
                        + backendPathCost * job.getBackWeight();

                if (cost < bestCost) {
                    bestCost = cost;
                    bestNode = i;
                    bestBackendPath = backendPath;
                }

            }
        }

        // ジョブを転送
        Forwarder.forward(job, bestNode, bestBackendPath, bestCost);
    }
}
