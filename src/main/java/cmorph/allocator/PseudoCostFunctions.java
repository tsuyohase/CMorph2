package cmorph.allocator;

import static cmorph.settings.SimulationConfiguration.NETWORK_DISTANCE_THRESHOLD;

public class PseudoCostFunctions {

    public static double convexPseudoCostFunction(double load) {
        return Math.pow(2 * load - 1, 2) / (1 - load) + 1;
    }

    public static double monotonicCostFunction(double load) {
        return Math.pow(load, 4.5) / (1 - load) + 1;
    }

    public static double binaryCostFunction(double load, double threshold) {
        if (load == 0) {
            return 1.5;
        } else if (load > threshold) {
            return 2;
        } else {
            return 1;
        }
    }

    public static double adjustableConvexPseudoCostFunction(double load, double threshold) {
        double k = (1 + Math.sqrt(1 - threshold)) / threshold;
        return Math.pow(k * load - 1, 2) / (1 - load) + 1;
    }

    public static double networkBinaryCostFunction(double distance, double threshold) {
        if (distance > threshold) {
            return 2;
        } else {
            return 0;
        }
    }

    public static double networkMonotonicBinaryCost(double distance, double threshold) {
        if (distance > threshold) {
            return monotonicCostFunction(Math.min(distance / threshold, 1)) + 1;
        } else {
            return monotonicCostFunction(Math.min(distance / threshold, 1));
        }
    }

    public static double networkPowCostFunction(double distance, double threshold, int pow) {
        return Math.pow(distance / threshold, pow) + 1;
    }

}
