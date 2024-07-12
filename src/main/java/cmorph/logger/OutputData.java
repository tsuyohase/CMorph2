package cmorph.logger;

public class OutputData {
    private ConfigData configData;
    private SimulationData simulationData;

    public OutputData() {
        this.configData = null;
        this.simulationData = null;
    }

    public OutputData(ConfigData configData, SimulationData simulationData) {
        this.configData = configData;
        this.simulationData = simulationData;
    }

    public ConfigData getConfigData() {
        return this.configData;
    }

    public SimulationData getSimulationData() {
        return this.simulationData;
    }
}
