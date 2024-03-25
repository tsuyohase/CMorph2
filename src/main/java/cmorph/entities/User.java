package cmorph.entities;

import static cmorph.settings.SimulationConfiguration.DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.MAP_HEIGHT;
import static cmorph.settings.SimulationConfiguration.MAP_WIDTH;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import cmorph.setUp.UserSetUp.Scenario;
import cmorph.setUp.UserSetUp.UserType;
import cmorph.simulator.Simulator;
import cmorph.simulator.Timer;
import cmorph.utils.Point;

public class User {
    private final int userId;
    private final Scenario scenario;
    private final UserType userType;
    private int connectNodeId = 0;
    private LinkedHashMap<Long, Integer> connectNodeMap = new LinkedHashMap<Long, Integer>();
    private final double networkThreshold;

    public User(int userId, Scenario scenario, UserType userType, double networkThreshold) {
        this.userId = userId;
        this.scenario = scenario;
        this.userType = userType;
        this.networkThreshold = networkThreshold;
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

    public void setConnectNodeId(int connectNodeId, long time) {
        this.connectNodeMap.put(time, connectNodeId);
    }

    public int getConnectNodeId(long time) {
        int connectNodeId = 0;
        for (Map.Entry<Long, Integer> entry : this.connectNodeMap.entrySet()) {
            if (entry.getKey() <= time) {
                connectNodeId = entry.getValue();
            } else {
                break;
            }
        }
        return connectNodeId;
    }

    public double getConnectDistance(long time) {
        double distance = -1;
        for (Map.Entry<Long, Integer> entry : this.connectNodeMap.entrySet()) {
            if (entry.getKey() <= time) {
                connectNodeId = entry.getValue();
                Point connectedLocation = this.getCurrentLocation(entry.getKey());
                Point connectNodeLocation = Simulator.getSimulatedNodes().get(connectNodeId).getLocation();
                if (this.isActive(time)) {
                    distance = connectedLocation.getDistance(connectNodeLocation);
                }
            } else {
                break;
            }
        }
        return distance;
    }

    public void removeEndConnectNode(long time) {
        ArrayList<Long> removeKeyList = new ArrayList<Long>();
        for (Map.Entry<Long, Integer> entry : this.connectNodeMap.entrySet()) {
            if (entry.getKey() < time) {
                removeKeyList.add(entry.getKey());
            }
        }
        for (int i = 0; i < removeKeyList.size(); i++) {
            this.connectNodeMap.remove(removeKeyList.get(i));
        }
    }

    public int getNearestNodeId() {
        Point currentLocation = this.getCurrentLocation(Timer.getCurrentTime());
        ArrayList<Node> stubNodes = Simulator.getSimulatedStubs();
        int connectNodeId = 0;
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < stubNodes.size(); i++) {
            double distance = currentLocation.getDistance(stubNodes.get(i).getLocation());
            if (distance < minDistance) {
                minDistance = distance;
                connectNodeId = i + DATA_CENTER_NUM;
            }
        }
        return connectNodeId;
    }

    public double getNetworkThreshold() {
        return this.networkThreshold;
    }
}
