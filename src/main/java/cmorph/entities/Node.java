package cmorph.entities;

import cmorph.utils.Point;

public class Node {
    private final int nodeId;
    private final Point location;
    private final int containerNum;

    /**
     * ノード(MDC, DC)
     * 
     * @param nodeId
     * @param location
     * @param containerNum
     */
    public Node(int nodeId, Point location, int containerNum) {
        this.nodeId = nodeId;
        this.location = location;
        this.containerNum = containerNum;
    }

    public int getNodeId() {
        return this.nodeId;
    }

    public Point getLocation() {
        return this.location;
    }

    public int getContainerNum() {
        return this.containerNum;
    }
}
