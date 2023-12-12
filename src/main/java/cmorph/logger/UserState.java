package cmorph.logger;

import cmorph.entities.User;

public class UserState {
    private int userId;
    private double x;
    private double y;

    public UserState() {
        this.userId = 0;
        this.x = 0.0;
        this.y = 0.0;
    }

    public UserState(int userId, double x, double y) {
        this.userId = userId;
        this.x = x;
        this.y = y;
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
}
