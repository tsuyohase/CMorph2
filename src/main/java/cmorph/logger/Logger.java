package cmorph.logger;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import cmorph.entities.Node;
import cmorph.entities.User;
import cmorph.settings.SimulationConfiguration;
import cmorph.simulator.Simulator;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Logger {

    private static long lastAddedTime = 0;
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
        String filePath = "src/dist/output/visualize/" + fileName;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(filePath), outputData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static OutputData getOutputData() {
        for (long time = lastAddedTime; time <= SimulationConfiguration.END_TIME; time++) {
            simulationData.addTimeStepData(getTimeStepData(time));
        }
        ConfigData configData = new ConfigData(Simulator.getSimulatedNodes());
        return new OutputData(configData, simulationData);
    }

    public static void addTimeStepData(long startTime, long endTime) {
        for (long time = startTime; time < endTime; time++) {
            simulationData.addTimeStepData(getTimeStepData(time));
        }
        lastAddedTime = endTime;
    }

    private static TimeStepData getTimeStepData(long time) {
        List<UserState> userStates = new ArrayList<>();
        List<NodeState> nodeStates = new ArrayList<>();
        for (User user : Simulator.getSimulatedUsers()) {
            userStates.add(new UserState(user.getUserId(), user.getScenario().apply(time).getX(),
                    user.getScenario().apply(time).getY(), user.getConnectDistance(time), user.getConnectNodeId(time),
                    user.getUserType()));
        }
        for (Node node : Simulator.getSimulatedNodes()) {
            nodeStates.add(new NodeState(node.getLoad(time)));
        }

        // for (Link link : Simulator.getSimulatedLinks()) {
        // double loadAve = 0;
        // for (int j = 0; j < NETWORK_TIME_UNIT_NUM; j++) {
        // if (time - j - 1 < 0) {
        // break;
        // }
        // loadAve += link.getLoad(time - j);
        // }
        // loadAve /= Math.min(NETWORK_TIME_UNIT_NUM, time + 1);
        // linkStates.add(new LinkState(loadAve));
        // }

        return new TimeStepData(userStates, nodeStates);
    }

}
