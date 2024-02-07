package cmorph.allocator;

import static cmorph.settings.SimulationConfiguration.COST_MDC_USER;
import static cmorph.settings.SimulationConfiguration.MAP_HEIGHT;
import static cmorph.settings.SimulationConfiguration.MAP_WIDTH;
import static cmorph.settings.SimulationConfiguration.NETWORK_TYPE;
import static cmorph.settings.SimulationConfiguration.TIME_UNIT_NUM;
import static cmorph.settings.SimulationConfiguration.USER_NUM;

import java.util.ArrayList;
import java.util.Collections;

import cmorph.entities.Link;
import cmorph.entities.User;
import cmorph.simulator.Simulator;
import cmorph.simulator.Timer;
import cmorph.utils.Point;

public class NetworkAllocator {
    // /**
    // * 各リンクの負荷を格納するリスト
    // */
    // private static final ArrayList<Double> linkLoads = new ArrayList<>(
    // Collections.nCopies(Simulator.getSimulatedLinks().size(), 0.0));

    // /**
    // * ある時間 のリンクの負荷を更新する関数
    // */
    // public static void updateLinkLoads(long time) {
    // ArrayList<Link> simulatedLinks = Simulator.getSimulatedLinks();
    // for (int i = 0; i < simulatedLinks.size(); i++) {
    // Link link = simulatedLinks.get(i);

    // // リンクの負荷を更新
    // double loadAverage = 0;
    // for (int j = 0; j < TIME_UNIT_NUM; j++) {
    // if (time - j >= 0) {
    // loadAverage += link.getLoad(time - j);
    // } else {
    // break;
    // }
    // }
    // loadAverage /= Math.min(TIME_UNIT_NUM, time + 1);
    // linkLoads.set(i, loadAverage);
    // }
    // }

    public static enum networkType {
        PATH,
        WIRELESS
    }

    public static double getBackendPathCost(int srcNodeId, int dstNodeId) {
        if (NETWORK_TYPE == networkType.PATH) {
            ArrayList<Integer> path = Dijkstra.calculate(srcNodeId, dstNodeId);
            return getCostByPath(path);
        } else if (NETWORK_TYPE == networkType.WIRELESS) {
            Point srcPoint = Simulator.getSimulatedNodes().get(srcNodeId).getLocation();
            Point dstPoint = Simulator.getSimulatedNodes().get(dstNodeId).getLocation();
            return getCostByDistance(srcPoint, dstPoint);
        } else {
            throw new Error("Network type is not defined");
        }
    }

    public static double getFrontendPathCost(User user, int dstNodeId) {
        if (NETWORK_TYPE == networkType.PATH) {
            ArrayList<Integer> path = Dijkstra.calculate(user.getConnectStubId(Timer.getCurrentTime()), dstNodeId);
            return COST_MDC_USER + getCostByPath(path);
        } else if (NETWORK_TYPE == networkType.WIRELESS) {
            Point userPoint = user.getCurrentLocation(Timer.getCurrentTime());
            Point dstPoint = Simulator.getSimulatedNodes().get(dstNodeId).getLocation();
            return getCostByDistance(userPoint, dstPoint);
        } else {
            throw new Error("Network type is not defined");
        }
    }

    private static double getCostByPath(ArrayList<Integer> path) {
        double cost = 0;
        ArrayList<Link> links = Simulator.getSimulatedLinks();
        if (path == null) {
            return Double.MAX_VALUE;
        }
        for (int i = 0; i < path.size(); i++) {
            cost += links.get(path.get(i)).getCost();
        }
        return cost;
    }

    private static double getCostByDistance(Point src, Point dst) {
        double distance = Math.sqrt(Math.pow(src.getX() - dst.getX(), 2) + Math.pow(src.getY() - dst.getY(), 2));
        double maxDistance = Math.sqrt(MAP_WIDTH * MAP_WIDTH + MAP_HEIGHT * MAP_HEIGHT);
        return PseudoCostFunctions.monotonicCostFunction(distance / maxDistance);
    }

    // public static ArrayList<Double> getLinkLoads() {
    // return linkLoads;
    // }

}
