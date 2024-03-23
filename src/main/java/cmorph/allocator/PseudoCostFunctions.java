package cmorph.allocator;

import static cmorph.settings.SimulationConfiguration.LOAD_COST_FUNCTION_TYPE;
import static cmorph.settings.SimulationConfiguration.LOAD_COST_THRESHOLD;
import static cmorph.settings.SimulationConfiguration.NETWORK_DISTANCE_LIMIT;
import static cmorph.settings.SimulationConfiguration.RANDOM_THRETHOLD;

import java.util.ArrayList;
import cmorph.entities.Node;
import cmorph.job.Job;

public class PseudoCostFunctions {

    public static enum LoadCostFunctionType {
        CONVEX,
        MONOTONIC,
        CONSTANT,
        BINARY,
    }

    public static enum NetworkCostFunctionType {
        CONVEX,
        MONOTONIC,
        CONSTANT,
        BINARY,
        MONOTONIC_BINARY
    }

    public static double getLoadCost(double load, double threthold) {
        if (LOAD_COST_FUNCTION_TYPE == LoadCostFunctionType.CONVEX) {
            return adjustableConvexPseudoCostFunction(load, threthold);
        } else if (LOAD_COST_FUNCTION_TYPE == LoadCostFunctionType.MONOTONIC) {
            return monotonicCostFunction(load);
        } else if (LOAD_COST_FUNCTION_TYPE == LoadCostFunctionType.CONSTANT) {
            return 0;
        } else if (LOAD_COST_FUNCTION_TYPE == LoadCostFunctionType.BINARY) {
            return binaryCostFunction(load);
        } else {
            return 0;
        }
    }

    public static double convexPseudoCostFunction(double load) {
        return Math.pow(2 * load - 1, 2) / (1 - load) + 1;
    }

    public static double monotonicCostFunction(double load) {
        return Math.pow(load, 4.5) / (1 - load) + 1;
    }

    public static double binaryCostFunction(double load) {
        if (load == 0) {
            return 1.5;
        } else if (load > LOAD_COST_THRESHOLD) {
            return 2;
        } else {
            return 1;
        }
    }

    public static double adjustableConvexPseudoCostFunction(double load, double threshold) {
        double k = (1 + Math.sqrt(1 - threshold)) / threshold;
        return Math.pow(k * load - 1, 2) / (1 - load) + 1;
    }

    public static double getNetworkBinaryCost(double distanceRate, double threshold) {
        if (distanceRate > threshold) {
            return 2;
        } else {
            return 0;
        }
    }

    public static double getNetworkMonotonicBinaryCost(double distance, double threshold) {
        if (distance > threshold) {
            return monotonicCostFunction(distance / NETWORK_DISTANCE_LIMIT) + 1;
        } else {
            return monotonicCostFunction(distance / NETWORK_DISTANCE_LIMIT);
        }
    }

}
