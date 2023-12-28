package cmorph.entities;

import static cmorph.settings.SimulationConfiguration.DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.ELAPSED_TIME_THRESHOLD;
import static cmorph.settings.SimulationConfiguration.MICRO_DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.TIME_UNIT_NUM;
import static cmorph.settings.SimulationConfiguration.USER_NUM;
import static cmorph.settings.SimulationConfiguration.useCostDifRandomization;
import static cmorph.settings.SimulationConfiguration.useMigTimeRandomization;
import static cmorph.simulator.Main.random;
import static cmorph.settings.SimulationConfiguration.AVE_JOB_CONTAINER_NUM;
import static cmorph.settings.SimulationConfiguration.COST_GAIN_THRESHOLD;

import java.util.Collections;

import cmorph.cost.PseudoCostFunctions;
import cmorph.job.Job;
import cmorph.simulator.Simulator;
import cmorph.simulator.Timer;

import java.util.ArrayList;

public class AllocationServer {
    /**
     * ノードの負荷のexponentially weighted moving average
     */
    private static final ArrayList<Double> nodeLoadEWMA = new ArrayList<>(
            Collections.nCopies(MICRO_DATA_CENTER_NUM + DATA_CENTER_NUM, 0.0));

    /**
     * ノードの残りコンテナ数
     */
    private static final ArrayList<Integer> remainingContainerNums = new ArrayList<>(
            Collections.nCopies(MICRO_DATA_CENTER_NUM + DATA_CENTER_NUM, AVE_JOB_CONTAINER_NUM));

    /**
     * ノードの負荷を最後に更新した時間
     */
    private static long updateTime = 0;

    /**
     * ユーザごとの直近のジョブの割当先ノード
     */
    private static final ArrayList<Integer> allocateNodeList = new ArrayList<>(Collections.nCopies(USER_NUM, -1));

    /**
     * ユーザごとの直近の割当先変更時間
     */
    private static final ArrayList<Long> lastMigrateTimeList = new ArrayList<>(
            Collections.nCopies(USER_NUM, (long) -1));

    /**
     * ノードの負荷を更新する関数
     */
    public static void updateNodeLoads() {
        // 同時刻に更新されていないか確認
        if (Timer.getCurrentTime() > updateTime) {
            ArrayList<Node> nodes = Simulator.getSimulatedNodes();
            for (int i = 0; i < nodes.size(); i++) {
                // currentTime -1 の負荷を取得
                double load = nodes.get(i).getLoad(Timer.getCurrentTime() - 1);
                int remainingContainerNum = nodes.get(i).getRemainingContainerNum(Timer.getCurrentTime() - 1);
                // EWMAを計算し更新
                if (nodeLoadEWMA.get(i) == 0) {
                    nodeLoadEWMA.set(i, load);
                } else {
                    nodeLoadEWMA.set(i,
                            (nodeLoadEWMA.get(i) * (1 - (1.0 / TIME_UNIT_NUM))) + (load * (1.0 / TIME_UNIT_NUM)));
                }
                remainingContainerNums.set(i, remainingContainerNum);
            }
            updateTime = Timer.getCurrentTime();
        }
    }

    /**
     * ジョブを割り当てる関数
     * 
     * @param job
     */
    public static void allocateJob(Job job) {
        // パブリッシャーのIDを取得し, 前回のジョブの割当先ノードを取得
        int publisherId = job.getUser().getUserId();
        int previousAllocateNodeId = allocateNodeList.get(publisherId);

        // 最良のコストのノードと前回のジョブのノードのコストを計算
        double previousAllocateNodeCost = -1;
        double bestCost = Double.MAX_VALUE;
        Node bestNode = null;
        ArrayList<Node> nodes = Simulator.getSimulatedNodes();
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            double cost = Double.MAX_VALUE;
            if (remainingContainerNums.get(i) >= job.getUseContainerNum()) {
                // ノードの負荷を取得
                double nodeLoad = nodeLoadEWMA.get(i);
                double loadCost = PseudoCostFunctions.getLoadCost(nodeLoad);
                double frontNetworkCost = 0;
                double backNetworkCost = 0;
                cost = job.getUseContainerNum() * loadCost + job.getFrontWeight() * frontNetworkCost
                        + job.getBackWeight() * backNetworkCost;
                if (cost < bestCost) {
                    bestCost = cost;
                    bestNode = node;
                }
            }
            // 前回のノードのコスト
            if (i == previousAllocateNodeId) {
                previousAllocateNodeCost = cost;
            }
        }

        if (bestNode == null) {
            // 割当先がないとき
            System.err.println("Overflow");
        } else if (bestNode.getNodeId() == previousAllocateNodeId) {
            // 割当先の変更が必要ないとき
            bestNode.receiveJob(job);
        } else {
            // 割当先の変更を行う確率
            double migrationProbability = 1.0;

            // コストの差をもとに確率を変更
            if (useCostDifRandomization && previousAllocateNodeCost != -1) {
                double migrateGain = 1 - bestCost / previousAllocateNodeCost;
                if (migrateGain <= 0) {
                    migrationProbability = 0;
                } else if (migrateGain < COST_GAIN_THRESHOLD) {
                    migrationProbability *= migrateGain / COST_GAIN_THRESHOLD;
                }
            }

            // 前回の割当先からの経過時間をもとに確率を変更
            if (useMigTimeRandomization && lastMigrateTimeList.get(publisherId) != -1) {
                long elapsedTime = Timer.getCurrentTime() - lastMigrateTimeList.get(publisherId);
                if (elapsedTime < ELAPSED_TIME_THRESHOLD) {
                    migrationProbability *= (double) elapsedTime / ELAPSED_TIME_THRESHOLD;
                }
            }

            // 割当先の変更を確率的に行う
            if (random.nextDouble() < migrationProbability) {
                // 変更するとき
                lastMigrateTimeList.set(publisherId, Timer.getCurrentTime());
                allocateNodeList.set(publisherId, bestNode.getNodeId());
                bestNode.receiveJob(job);
            } else {
                // 変更しないとき
                nodes.get(previousAllocateNodeId).receiveJob(job);
            }
        }
    }
}
