package cmorph.entities;

import static cmorph.settings.SimulationConfiguration.DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.MAP_HEIGHT;
import static cmorph.settings.SimulationConfiguration.MAP_WIDTH;

import java.util.ArrayList;

import cmorph.setUp.UserSetUp.Scenario;
import cmorph.setUp.UserSetUp.UserType;
import cmorph.simulator.Simulator;
import cmorph.utils.Point;

public class User {
    private final int userId;
    private final Scenario scenario;
    private final UserType userType;

    public User(int userId, Scenario scenario, UserType userType) {
        this.userId = userId;
        this.scenario = scenario;
        this.userType = userType;
    }

    public int getUserId() {
        return this.userId;
    }

    public Scenario getScenario() {
        return this.scenario;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public Point getCurrentLocation(long time) {
        return this.scenario.apply(time);
    }

    public boolean isActive(long time) {
        Point currentLocation = this.scenario.apply(time);
        return (0 <= currentLocation.getX() && currentLocation.getX() <= MAP_WIDTH && 0 <= currentLocation.getY()
                && currentLocation.getY() <= MAP_HEIGHT);
    }

    public int getConnectStubId(long time) {
        ArrayList<Node> stubNodes = Simulator.getSimulatedStubs();
        int connectNodeId = 0;
        double minDistance = Double.MAX_VALUE;
        Point currentLocation = this.scenario.apply(time);
        for (int i = 0; i < stubNodes.size(); i++) {
            double distance = currentLocation.getDistance(stubNodes.get(i).getLocation());
            if (distance < minDistance) {
                minDistance = distance;
                connectNodeId = i + DATA_CENTER_NUM;
            }
        }
        return connectNodeId;
    }
}
