package cmorph.logger;

import java.util.ArrayList;
import java.util.List;

public class SimulationData {
    private List<TimeStepData> data;

    public SimulationData() {
        data = new ArrayList<TimeStepData>();
    }

    public void addTimeStepData(TimeStepData timeStepData) {
        data.add(timeStepData);
    }

    public List<TimeStepData> getTimeStepData() {
        return data;
    }
}
