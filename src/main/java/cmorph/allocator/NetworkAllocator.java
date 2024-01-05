package cmorph.allocator;

import static cmorph.settings.SimulationConfiguration.TIME_UNIT_NUM;
import static cmorph.settings.SimulationConfiguration.USER_NUM;

import java.util.ArrayList;
import java.util.Collections;

import cmorph.entities.Link;
import cmorph.simulator.Simulator;

public class NetworkAllocator {
    /**
     * 各リンクの負荷を格納するリスト
     */
    private static final ArrayList<Double> linkLoads = new ArrayList<>(
            Collections.nCopies(Simulator.getSimulatedLinks().size(), 0.0));

    /**
     * ある時間 のリンクの負荷を更新する関数
     */
    public static void updateLinkLoads(long time) {
        ArrayList<Link> simulatedLinks = Simulator.getSimulatedLinks();
        for (int i = 0; i < simulatedLinks.size(); i++) {
            Link link = simulatedLinks.get(i);

            // リンクの負荷を更新
            double loadAverage = 0;
            for (int j = 0; j < TIME_UNIT_NUM; j++) {
                if (time - j >= 0) {
                    loadAverage += link.getLoad(time - j);
                } else {
                    break;
                }
            }
            loadAverage /= Math.min(TIME_UNIT_NUM, time + 1);
            linkLoads.set(i, loadAverage);
        }
    }

    /**
     * バックエンドパスを取得する関数
     * 
     * @return
     */
    public static ArrayList<Integer> getBackendPath(int srcNodeId, int dstNodeId) {
        ArrayList<Double> linkCosts = new ArrayList<>();
        for (int i = 0; i < linkLoads.size(); i++) {
            double cost = PseudoCostFunctions.getNetworkCost(linkLoads.get(i));
            linkCosts.add(cost);
        }
        ArrayList<Integer> path = Dijkstra.calculate(srcNodeId, dstNodeId, linkCosts);
        return path;
    }

    /**
     * バックエンドパスのコストを取得する関数
     * 
     * @param path
     * @return
     */
    public static double getBackendPathCost(ArrayList<Integer> path) {
        double cost = 0;
        if (path == null) {
            return Double.MAX_VALUE;
        }
        for (int i = 0; i < path.size(); i++) {
            cost += PseudoCostFunctions.getNetworkCost(linkLoads.get(path.get(i)));
        }
        return cost;
    }

    public static ArrayList<Double> getLinkLoads() {
        return linkLoads;
    }

}
