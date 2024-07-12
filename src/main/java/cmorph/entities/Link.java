package cmorph.entities;

import java.util.ArrayList;
import java.util.Collections;

import cmorph.job.Job;
import cmorph.simulator.Timer;
import cmorph.utils.ScheduledJob;

public class Link {
    private final int linkId;
    private final Node connectNode1;
    private final Node connectNode2;
    private final int cost;

    /**
     * 
     * @param linkId
     * @param connectNode1
     * @param connectNode2
     * @param cost
     */
    public Link(
            int linkId, Node connectNode1,
            Node connectNode2,
            int cost) {
        this.linkId = linkId;
        this.connectNode1 = connectNode1;
        this.connectNode2 = connectNode2;
        this.cost = cost;
    }

    public int getLinkId() {
        return this.linkId;
    }

    public Node getConnectNode1() {
        return this.connectNode1;
    }

    public Node getConnectNode2() {
        return this.connectNode2;
    }

    public int getCost() {
        return this.cost;
    }
}
