package cmorph.cost;

import static cmorph.settings.SimulationConfiguration.LOAD_COST_FUNCTION_TYPE;

public class PseudoCostFunctions {

    public static enum LoadCostFunctionType {
        CONVEX,
        MONOTONIC,
        CONSTANT
    }

    public static double getNetworkCost(double distanceRate) {
        return monotonicCostFunction(distanceRate);
    }

    public static double getLoadCost(double loadRate) {
        if (LOAD_COST_FUNCTION_TYPE == LoadCostFunctionType.CONVEX) {
            return convexPseudoCostFunction(loadRate);
        } else if (LOAD_COST_FUNCTION_TYPE == LoadCostFunctionType.MONOTONIC) {
            return monotonicCostFunction(loadRate);
        } else if (LOAD_COST_FUNCTION_TYPE == LoadCostFunctionType.CONSTANT) {
            return 0;
        }
        return 0;
    }

    public static double convexPseudoCostFunction(double loadRate) {
        return Math.pow(2 * loadRate - 1, 2) / (1 - loadRate) + 1;
    }

    private static double monotonicCostFunction(double loadRate) {
        return Math.pow(loadRate, 4.5) / (1 - loadRate) + 1;
    }

}
