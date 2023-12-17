package cmorph.logger;

public class NodeState {
    private int nodeId;
    // private double x;
    // private double y;
    private double load;
    private int containerNum;

    public NodeState() {
        this.nodeId = 0;
        // this.x = 0.0;
        // this.y = 0.0;
        this.load = 0.0;
        this.containerNum = 0;
    }

    public NodeState(int nodeId, double x, double y, double load, int containerNum) {
        this.nodeId = nodeId;
        // this.x = x;
        // this.y = y;
        this.load = load;
        this.containerNum = containerNum;
    }

    public int getNodeId() {
        return this.nodeId;
    }

    // public double getX() {
    // return this.x;
    // }

    // public double getY() {
    // return this.y;
    // }

    public double getLoad() {
        return this.load;
    }

    public int getContainerNum() {
        return this.containerNum;
    }
}
