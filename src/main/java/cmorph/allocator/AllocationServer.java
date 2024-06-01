package cmorph.allocator;

import static cmorph.settings.SimulationConfiguration.AVE_JOB_TIME_SLOT;
import static cmorph.settings.SimulationConfiguration.COST_MDC_USER;
import static cmorph.settings.SimulationConfiguration.COST_MIGRATE_TYPE;
import static cmorph.settings.SimulationConfiguration.USER_NUM;
import static cmorph.simulator.Main.random;

import java.util.ArrayList;
import java.util.Collections;

import cmorph.entities.Link;
import cmorph.entities.Node;
import cmorph.entities.User;
import cmorph.job.Job;
import cmorph.logger.AnalysisLogger;
import cmorph.logger.Logger;
import cmorph.simulator.Simulator;
import cmorph.simulator.Timer;

public class AllocationServer {
    public static enum CostMigrateType {
        SUM,
        MAX
    }

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
            AnalysisLogger.addData(updateTime, Timer.getCurrentTime());

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
        ArrayList<Integer> sameCostNodes = new ArrayList<Integer>();

        for (int i = 0; i < simulatedNodes.size(); i++) {

            double cost = Double.MAX_VALUE;
            if (NodeAllocator.getRemainingContainerNums(i) >= job.getUseContainerNum()) {
                cost = getCost(job, i);

                if (cost < bestCost) {
                    bestCost = cost;
                    bestNode = i;
                    sameCostNodes.clear();
                }
                if (cost == bestCost) {
                    sameCostNodes.add(i);
                }

            }
        }
        if (sameCostNodes.size() > 0) {
            bestNode = sameCostNodes.get(random.nextInt(sameCostNodes.size()));
        }

        // ジョブを転送
        Forwarder.forward(job, bestNode, bestCost);
    }

    public static double getCost(Job job, int nodeId) {
        double loadCost = NodeAllocator.getNodeCost(nodeId) * job.getUseContainerNum();
        double frontendPathCost = NetworkAllocator.getFrontendPathCost(
                job.getUser(), nodeId) * job.getFrontWeight();
        double backendPathCost = NetworkAllocator.getBackendPathCost(nodeId,
                job.getDataObjectNode().getNodeId()) * job.getBackWeight();
        double cost = 0;
        if (COST_MIGRATE_TYPE == CostMigrateType.MAX) {
            cost = Math.max(loadCost, frontendPathCost + backendPathCost);
        } else if (COST_MIGRATE_TYPE == CostMigrateType.SUM) {
            cost = loadCost + frontendPathCost + backendPathCost;
        }
        return cost;
    }
}
