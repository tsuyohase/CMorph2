package cmorph.setUp;

import static cmorph.settings.SimulationConfiguration.AVE_DC_CONTAINER_NUM;
import static cmorph.settings.SimulationConfiguration.AVE_MDC_CONTAINER_NUM;
import static cmorph.settings.SimulationConfiguration.DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.MAP_HEIGHT;
import static cmorph.settings.SimulationConfiguration.MAP_WIDTH;
import static cmorph.settings.SimulationConfiguration.MICRO_DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.NODE_COST_WEIGHT_TYPE;
import static cmorph.settings.SimulationConfiguration.RANDOM_DC_LOCATION;

import cmorph.utils.Point;
import static cmorph.simulator.Main.random;

import cmorph.entities.Node;
import cmorph.simulator.Simulator;

public class NodeSetUp {

    public static enum NodeCostWeightType {
        EQUALITY,
        PROPORTIONAL,
    }

    /**
     * DCの位置を返す
     * 
     * @param id
     * @return Point
     */
    public static Point getDCLocation(int id) {
        if (RANDOM_DC_LOCATION) {
            return getDCLocationByRandom();
        } else {
            return getDCLocationByOrder(id);
        }
    }

    /**
     * ランダムにDCの位置を返す
     * 
     * @return Point
     */
    private static Point getDCLocationByRandom() {
        double x = random.nextDouble() * MAP_WIDTH;
        double y = random.nextDouble() * MAP_HEIGHT;
        return new Point(x, y);
    }

    /**
     * idをもとにDCの位置を返す
     * 
     * @param id
     * @return Point
     */
    private static Point getDCLocationByOrder(int id) {
        int nodeNum = DATA_CENTER_NUM;

        // 行, 列のnodeの数
        int nodesPerRow = (int) Math.ceil(Math.sqrt((double) nodeNum));
        int nodesPerColumn = (int) Math.ceil((double) nodeNum / nodesPerRow);

        // 表示の関係で外周に1マスずつ余白を設ける
        int locationNumPerRow = nodesPerRow + 1;
        int locationNumPerColumn = nodesPerColumn + 1;

        double x = (id % nodesPerRow + 1) * (MAP_WIDTH / locationNumPerRow);
        double y = (id / nodesPerRow + 1) * (MAP_HEIGHT / locationNumPerColumn);
        return new Point(x, y);
    }

    /**
     * TSモデルにおいてのMDCが繋がるDCノードを返す
     * 
     * @param id
     * @return Point
     */
    public static Node getTransitNode(int id) {
        return Simulator.getSimulatedNodes().get(id % DATA_CENTER_NUM);
    }

    /**
     * TSモデルをもとにMDCの位置を返す
     * 
     * @return Point
     */
    public static Point getMDCLocation(Node node) {
        double linkLengthBase = Simulator.getSimulatedNodes().get(0).getLocation().getDistance(
                Simulator.getSimulatedNodes().get(1).getLocation());
        double linkLength = linkLengthBase * (2 + random.nextDouble()) / 3;
        double theta = Math.signum(random.nextDouble() - 0.5) * Math.PI / 2 * (1 + 2 * random.nextDouble()) / 4;
        Point dcLocation = node.getLocation();
        double x = dcLocation.getX() + Math.signum(random.nextDouble() - 0.5) * linkLength * Math.sin(theta);
        double y = dcLocation.getY() + Math.signum(random.nextDouble() - 0.5) * linkLength * Math.cos(theta);
        return new Point(x, y);
    }

    public static int getNodeCostWeight(int id) {
        if (NODE_COST_WEIGHT_TYPE == NodeCostWeightType.EQUALITY) {
            return 1;
        } else if (NODE_COST_WEIGHT_TYPE == NodeCostWeightType.PROPORTIONAL) {
            return (int) Math.pow(2, id);
        } else {
            throw new Error("NodeCostWeightType is not defined.");
        }
    }
}
