package cmorph.allocator;

import static cmorph.settings.SimulationConfiguration.COST_GAIN_THRESHOLD;
import static cmorph.settings.SimulationConfiguration.COST_MDC_USER;
import static cmorph.settings.SimulationConfiguration.ELAPSED_TIME_THRESHOLD;
import static cmorph.settings.SimulationConfiguration.NETWORK_TYPE;
import static cmorph.settings.SimulationConfiguration.USER_NUM;
import static cmorph.settings.SimulationConfiguration.useCostDifRandomization;
import static cmorph.settings.SimulationConfiguration.useMigTimeRandomization;
import static cmorph.simulator.Main.random;

import java.util.ArrayList;
import java.util.Collections;

import cmorph.allocator.NetworkAllocator.networkType;
import cmorph.entities.Link;
import cmorph.job.Job;
import cmorph.simulator.Simulator;
import cmorph.simulator.Timer;

public class Forwarder {

    /**
     * ユーザごとのノードの割り当てを格納するリスト
     */
    private static final ArrayList<Integer> allocatedNodeList = new ArrayList<>(Collections.nCopies(USER_NUM, -1));

    /**
     * ユーザごとの直近の割当先変更時間を格納するリスト
     */
    private static final ArrayList<Long> lastMigrateTimeList = new ArrayList<>(Collections.nCopies(USER_NUM, 0L));

    /**
     * ジョブを転送する関数
     * 
     * @param job
     * @param userId
     * @param bestNodeId
     * @param bestBackendPath
     * @param bestCost
     */
    public static void forward(Job job, int bestNodeId, double bestCost) {
        if (bestNodeId == -1) {
            System.err.println("Node Overflow");
        } else {
            int userId = job.getUser().getUserId();
            double lastCost;
            if (allocatedNodeList.get(userId) == -1) {
                lastCost = Double.MAX_VALUE;
            } else {
                lastCost = AllocationServer.getCost(job, allocatedNodeList.get(userId));
            }
            // 割り当て先を変更する確率
            double migrateProbability = 1.0;

            // コストの差をもとに確率を更新
            if (useCostDifRandomization && (allocatedNodeList.get(userId) != -1)) {
                double costDif = 1 - bestCost / lastCost;
                if (costDif < COST_GAIN_THRESHOLD) {
                    migrateProbability *= costDif / COST_GAIN_THRESHOLD;
                }
            }

            // 前回の割当先変更時間をもとに確率を更新
            if (useMigTimeRandomization && (allocatedNodeList.get(userId) != -1)) {
                long elapsedTimeDif = Timer.getCurrentTime() - lastMigrateTimeList.get(userId);
                if (elapsedTimeDif < ELAPSED_TIME_THRESHOLD) {
                    migrateProbability *= (double) elapsedTimeDif / ELAPSED_TIME_THRESHOLD;
                }
            }

            // 割り当て先を変更するかどうか
            if (random.nextDouble() < migrateProbability) {
                // 変更する場合
                allocatedNodeList.set(userId, bestNodeId);
                lastMigrateTimeList.set(userId, Timer.getCurrentTime());

                Simulator.getSimulatedNodes().get(bestNodeId).receiveJob(job);

                if (NETWORK_TYPE == networkType.PATH) {
                    job.getUser().setConnectNodeId(job.getUser().getNearestNodeId(), Timer.getCurrentTime());
                } else if (NETWORK_TYPE == networkType.WIRELESS) {
                    job.getUser().setConnectNodeId(bestNodeId, Timer.getCurrentTime());
                }
                // forwardJobToNetwork(job, bestBackendPath);
            } else {
                // 変更しない場合
                Simulator.getSimulatedNodes().get(allocatedNodeList.get(userId)).receiveJob(job);
                // forwardJobToNetwork(job, allocatedBackendPathList.get(userId));
            }
        }
    }

    // private static void forwardJobToNetwork(Job job, ArrayList<Integer> backPath)
    // {
    // if (simulateNetwork) {
    // long delay = 0;
    // for (int i = 0; i < backPath.size(); i++) {
    // Link link = Simulator.getSimulatedLinks().get(backPath.get(i));
    // delay += link.getTransferDataSize(Timer.getCurrentTime()) /
    // link.getBandWidth();
    // link.receiveJob(job, delay);
    // delay += Math.max(1, job.getDataObjectSize() / link.getBandWidth());
    // }
    // }
    // }

}
