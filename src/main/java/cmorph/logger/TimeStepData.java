package cmorph.logger;

import java.util.List;

public class TimeStepData {
    private List<UserState> userStates;
    private List<NodeState> nodeStates;

    public TimeStepData() {
        this.userStates = null;
        this.nodeStates = null;
    }

    public TimeStepData(List<UserState> userStates, List<NodeState> nodeStates) {
        this.userStates = userStates;
        this.nodeStates = nodeStates;
    }

    public List<UserState> getUserStates() {
        return this.userStates;
    }

    public List<NodeState> getNodeStates() {
        return this.nodeStates;
    }
}
