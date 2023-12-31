package cmorph.allocator;

import static cmorph.settings.SimulationConfiguration.LOAD_COST_FUNCTION_TYPE;
import static cmorph.settings.SimulationConfiguration.NETWORK_COST_FUNCTION_TYPE;

import java.util.ArrayList;

import cmorph.entities.Node;

public class PseudoCostFunctions {

    public static enum LoadCostFunctionType {
        CONVEX,
        MONOTONIC,
        CONSTANT
    }

    public static enum NetworkCostFunctionType {
        CONVEX,
        MONOTONIC,
        CONSTANT
    }

    public static double getNetworkCost(double load) {
        if (NETWORK_COST_FUNCTION_TYPE == NetworkCostFunctionType.CONVEX) {
            return convexPseudoCostFunction(load);
        } else if (NETWORK_COST_FUNCTION_TYPE == NetworkCostFunctionType.MONOTONIC) {
            return monotonicCostFunction(load);
        } else if (NETWORK_COST_FUNCTION_TYPE == NetworkCostFunctionType.CONSTANT) {
            return 1;
        }
        return 0;
    }

    public static double getLoadCost(double load) {
        if (LOAD_COST_FUNCTION_TYPE == LoadCostFunctionType.CONVEX) {
            return convexPseudoCostFunction(load);
        } else if (LOAD_COST_FUNCTION_TYPE == LoadCostFunctionType.MONOTONIC) {
            return monotonicCostFunction(load);
        } else if (LOAD_COST_FUNCTION_TYPE == LoadCostFunctionType.CONSTANT) {
            return 0;
        }
        return 0;
    }

    public static double convexPseudoCostFunction(double load) {
        return Math.pow(2 * load - 1, 2) / (1 - load) + 1;
    }

    private static double monotonicCostFunction(double load) {
        return Math.pow(load, 4.5) / (1 - load) + 1;
    }

}
