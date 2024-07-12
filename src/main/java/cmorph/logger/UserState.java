package cmorph.logger;

import cmorph.entities.Node;
import cmorph.entities.User;
import cmorph.setUp.UserSetUp.UserType;

public class UserState {
    // private int userId;
    private double x;
    private double y;
    private double d;
    private int nid;
    // private UserType userType;

    public UserState() {
        // this.userId = 0;
        this.x = 0.0;
        this.y = 0.0;
        this.d = 0.0;
        this.nid = 0;
        // this.userType = UserType.INTERACTIVE;
    }

    public UserState(int userId, double x, double y, double connectedDistance, int connectedNodeId, UserType userType) {
        // this.userId = userId;
        this.x = Double.parseDouble(String.format("%.2f", x));
        this.y = Double.parseDouble(String.format("%.2f", y));
        this.d = Double.parseDouble(String.format("%.2f", connectedDistance));
        this.nid = connectedNodeId;
        // this.userType = userType;
    }

    // public int getUserId() {
    // return this.userId;
    // }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getD() {
        return this.d;
    }

    public int getNid() {
        return this.nid;
    }

    // public UserType getUserType() {
    // return this.userType;
    // }
}
