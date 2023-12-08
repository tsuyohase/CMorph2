package cmorph.entities;

import static cmorph.settings.SimulationConfiguration.MAP_HEIGHT;
import static cmorph.settings.SimulationConfiguration.MAP_WIDTH;

import cmorph.setUp.UserSetUp.Scenario;
import cmorph.utils.Point;

public class User {
    private final int userId;
    private final Scenario scenario;

    public User(int userId, Scenario scenario) {
        this.userId = userId;
        this.scenario = scenario;
    }

    public int getUserId() {
        return this.userId;
    }

    public Scenario getScenario() {
        return this.scenario;
    }

    public Point getCurrentLocation(long time) {
        return this.scenario.apply(time);
    }

    public boolean isActivate(long time) {
        Point currentLocation = this.scenario.apply(time);
        return (0 <= currentLocation.getX() && currentLocation.getX() <= MAP_WIDTH && 0 <= currentLocation.getY()
                && currentLocation.getY() <= MAP_HEIGHT);
    }
}
