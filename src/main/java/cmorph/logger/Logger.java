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
        SimulationData simulationData = new SimulationData();
        for (long time = 0; time <= END_TIME; time++) {
            simulationData.addTimeStepData(getTimeStepData(time));
        }
        ConfigData configData = new ConfigData(Simulator.getSimulatedNodes());
        return new OutputData(configData, simulationData);
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
