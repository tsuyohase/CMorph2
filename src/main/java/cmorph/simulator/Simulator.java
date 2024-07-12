package cmorph.simulator;

import java.util.ArrayList;

import cmorph.entities.Link;
import cmorph.entities.Node;
import cmorph.entities.User;

public class Simulator {
    private static final ArrayList<Node> simulatedNodes = new ArrayList<Node>();
    private static final ArrayList<User> simulatedUsers = new ArrayList<User>();
    private static final ArrayList<Link> simulatedLinks = new ArrayList<Link>();
    private static final ArrayList<Node> simulatedStubs = new ArrayList<Node>();
    private static long currentTime = 0;

    public static void addNode(Node node) {
        simulatedNodes.add(node);
    }

    public static ArrayList<Node> getSimulatedNodes() {
        return simulatedNodes;
    }

    public static void addLink(Link link) {
        simulatedLinks.add(link);
    }

    public static ArrayList<Link> getSimulatedLinks() {
        return simulatedLinks;
    }

    public static void addUser(User user) {
        simulatedUsers.add(user);
    }

    public static ArrayList<User> getSimulatedUsers() {
        return simulatedUsers;
    }

    public static void addStub(Node stub) {
        simulatedStubs.add(stub);
    }

    public static ArrayList<Node> getSimulatedStubs() {
        return simulatedStubs;
    }
}
