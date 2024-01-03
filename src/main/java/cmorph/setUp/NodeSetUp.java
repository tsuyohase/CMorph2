package cmorph.setUp;

import static cmorph.settings.SimulationConfiguration.AVE_DC_CONTAINER_NUM;
import static cmorph.settings.SimulationConfiguration.AVE_MDC_CONTAINER_NUM;
import static cmorph.settings.SimulationConfiguration.DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.MAP_HEIGHT;
import static cmorph.settings.SimulationConfiguration.MAP_WIDTH;
import static cmorph.settings.SimulationConfiguration.MICRO_DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.NODE_COST_WEIGHT_TYPE;
import static cmorph.settings.SimulationConfiguration.RANDOM_NODE_LOCATION;

import cmorph.utils.Point;
import static cmorph.simulator.Main.random;

public class NodeSetUp {

    public static enum NodeCostWeightType {
        EQUALITY,
        PROPORTIONAL,
    }

    /**
     * Nodeの位置を返す
     * 
     * @param id
     * @return Point
     */
    public static Point getNodeLocation(int id) {
        if (RANDOM_NODE_LOCATION) {
            return getNodeLocationByRandom();
        } else {
            return getNodeLocationByOrder(id);
        }
    }

    /**
     * ランダムにNodeの位置を返す
     * 
     * @return Point
     */
    private static Point getNodeLocationByRandom() {
        double x = random.nextDouble() * MAP_WIDTH;
        double y = random.nextDouble() * MAP_HEIGHT;
        return new Point(x, y);
    }

    /**
     * idをもとにNodeの位置を返す
     * 
     * @param id
     * @return Point
     */
    private static Point getNodeLocationByOrder(int id) {
        int nodeNum = MICRO_DATA_CENTER_NUM + DATA_CENTER_NUM;

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

    public static int getNodeContainerNum(int id) {
        if (id < MICRO_DATA_CENTER_NUM) {
            return AVE_MDC_CONTAINER_NUM;
        } else {
            return AVE_DC_CONTAINER_NUM;
        }
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
