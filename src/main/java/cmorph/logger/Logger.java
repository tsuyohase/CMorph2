package cmorph.logger;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import cmorph.entities.Node;
import cmorph.entities.User;
import cmorph.simulator.Simulator;

import static cmorph.settings.SimulationConfiguration.END_TIME;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Logger {

    private static SimulationData simulationData = new SimulationData();

    public static void log() {
        OutputData outputData = getOutputData();
        saveOutputFile(outputData);
    }

    private static void saveOutputFile(OutputData outputData) {
        LocalDateTime nowDate = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formatNowDate = dtf.format(nowDate);
        String fileName = "output-" + formatNowDate + ".json";
        String filePath = "src/dist/output/" + fileName;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(filePath), outputData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static OutputData getOutputData() {
        ConfigData configData = new ConfigData(Simulator.getSimulatedNodes());
        return new OutputData(configData, simulationData);
    }

    public static void addTimeStepData(long startTime, long endTime) {
        for (long time = startTime; time < endTime; time++) {
            simulationData.addTimeStepData(getTimeStepData(time));
        }
    }

    private static TimeStepData getTimeStepData(long time) {
        List<UserState> userStates = new ArrayList<>();
        List<NodeState> nodeStates = new ArrayList<>();
        for (User user : Simulator.getSimulatedUsers()) {
            userStates.add(new UserState(user.getUserId(), user.getScenario().apply(time).getX(),
                    user.getScenario().apply(time).getY()));
        }
        for (Node node : Simulator.getSimulatedNodes()) {
            nodeStates.add(new NodeState(node.getNodeId(), node.getLocation().getX(), node.getLocation().getY(),
                    node.getLoad(time), node.getContainerNum()));
        }
        return new TimeStepData(userStates, nodeStates);
    }

}
