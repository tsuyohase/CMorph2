package cmorph.logger;

import cmorph.entities.Node;
import cmorph.entities.User;
import cmorph.setUp.UserSetUp.UserType;

public class UserState {
    private int userId;
    private double x;
    private double y;
    private double connectedDistance;
    private int connectedNodeId;
    private UserType userType;

    public UserState() {
        this.userId = 0;
        this.x = 0.0;
        this.y = 0.0;
        this.connectedDistance = 0.0;
        this.connectedNodeId = 0;
        this.userType = UserType.INTERACTIVE;
    }

    public UserState(int userId, double x, double y, double connectedDistance, int connectedNodeId, UserType userType) {
        this.userId = userId;
        this.x = x;
        this.y = y;
        this.connectedDistance = connectedDistance;
        this.connectedNodeId = connectedNodeId;
        this.userType = userType;
    }

    public int getUserId() {
        return this.userId;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getConnectedDistance() {
        return this.connectedDistance;
    }

    public int getConnectedNodeId() {
        return this.connectedNodeId;
    }

    public UserType getUserType() {
        return this.userType;
    }
}
