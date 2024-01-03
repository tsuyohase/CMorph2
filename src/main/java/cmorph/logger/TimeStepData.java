package cmorph.logger;

import java.util.List;

public class TimeStepData {
    // private List<UserState> userStates;
    private List<NodeState> nodeStates;
    private List<LinkState> linkStates;

    public TimeStepData() {
        // this.userStates = null;
        this.nodeStates = null;
        this.linkStates = null;
    }

    public TimeStepData(List<UserState> userStates, List<NodeState> nodeStates, List<LinkState> linkStates) {
        // this.userStates = userStates;
        this.nodeStates = nodeStates;
        this.linkStates = linkStates;
    }

    // public List<UserState> getUserStates() {
    // return this.userStates;
    // }

    public List<NodeState> getNodeStates() {
        return this.nodeStates;
    }

    public List<LinkState> getLinkStates() {
        return this.linkStates;
    }
}
