package cmorph.setUp;

import static cmorph.settings.SimulationConfiguration.AVE_DC_CONTAINER_NUM;
import static cmorph.settings.SimulationConfiguration.AVE_MDC_CONTAINER_NUM;
import static cmorph.settings.SimulationConfiguration.COST_DC_DC;
import static cmorph.settings.SimulationConfiguration.COST_DC_MDC;
import static cmorph.settings.SimulationConfiguration.DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.MICRO_DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.NETWORK_TYPE;
import static cmorph.settings.SimulationConfiguration.USER_LOCATION_SCENARIO;
import static cmorph.settings.SimulationConfiguration.USER_NUM;

import cmorph.allocator.NetworkAllocator.networkType;
import cmorph.entities.Link;
import cmorph.entities.Node;
import cmorph.entities.User;
import cmorph.event.JobEvent;
import cmorph.job.Job;
import cmorph.setUp.UserSetUp.Scenario;
import cmorph.setUp.UserSetUp.UserLocationScenario;
import cmorph.setUp.UserSetUp.UserType;
import cmorph.simulator.Simulator;
import cmorph.simulator.Timer;
import cmorph.utils.Point;
import java.util.ArrayList;

public class SetUp {
    /**
     * NodeとUserを生成する
     */
    public static void setUp() {
        setUpDC();
        setUpMDC();
        setUpUsers();
    }

    /**
     * DCを生成し, 相互に接続し、Simulatorのリストに追加する
     * 
     * @param
     * @return
     */
    private static void setUpDC() {

        for (int id = 0; id < DATA_CENTER_NUM; id++) {
            Point nodeLocation = NodeSetUp.getDCLocation(id, DATA_CENTER_NUM);
            int containerNum = AVE_DC_CONTAINER_NUM;
            int costWeight = NodeSetUp.getNodeCostWeight(id);
            double loadThreshold = NodeSetUp.getLoadThreshold(id);
            Node node = new Node(id, nodeLocation, containerNum, costWeight, loadThreshold);
            Simulator.addNode(node);
        }
        if (NETWORK_TYPE == networkType.PATH) {
            int linkId = 0;
            ArrayList<Node> nodes = Simulator.getSimulatedNodes();
            // DC(transit)ノードは相互に接続する
            for (int i = 0; i < DATA_CENTER_NUM; i++) {
                for (int j = 0; j < DATA_CENTER_NUM; j++) {
                    if (i == j) {
                        continue;
                    }
                    Link link = new Link(linkId, nodes.get(i), nodes.get(j), COST_DC_DC);
                    Simulator.addLink(link);
                    linkId++;
                }
            }
        }

    }

    private static void setUpMDC() {
        int nextNodeId = Simulator.getSimulatedNodes().size();
        int nextLinkId = Simulator.getSimulatedLinks().size();
        for (int id = nextNodeId; id < DATA_CENTER_NUM + MICRO_DATA_CENTER_NUM; id++) {
            Node transitNode = NodeSetUp.getTransitNode(id, DATA_CENTER_NUM);
            // Nodeを追加
            Point nodeLocation = NodeSetUp.getMDCLocation(id, transitNode);

            int containerNum = AVE_MDC_CONTAINER_NUM;
            int costWeight = NodeSetUp.getNodeCostWeight(id);
            double loadThreshold = NodeSetUp.getLoadThreshold(id);
            Node node = new Node(id, nodeLocation, containerNum, costWeight, loadThreshold);
            Simulator.addNode(node);
            Simulator.addStub(node);

            if (NETWORK_TYPE == networkType.PATH) {
                if (transitNode != null) {
                    // Linkを追加
                    Link link = new Link(nextLinkId, transitNode, node, COST_DC_MDC);
                    Simulator.addLink(link);
                    nextLinkId++;
                }
            }

        }
    }

    /**
     * Userを生成し, Simulatorのリストに追加する
     * 
     * @param
     * @return
     */
    private static void setUpUsers() {
        if (USER_LOCATION_SCENARIO == UserLocationScenario.READ) {
            UserSetUp.readUserLocations();
        }
        for (int id = 0; id < USER_NUM; id++) {
            // 出現時間と消滅時間を取得
            long spawnTime = UserSetUp.getSpawnTime(id);
            long despawnTime = UserSetUp.getDespawnTime(id);

            // シナリオを取得
            Scenario scenario = UserSetUp.getScenario(id, spawnTime, despawnTime);

            // usertypeを取得
            UserType userType = UserSetUp.getUserType(id);

            double networkThreshold = UserSetUp.getNetworkThreshold(id);

            User user = new User(id, scenario, userType, networkThreshold);
            Simulator.addUser(user);

            // ユーザごとに最初のJobを生成
            Job initialJob = JobSetUp.getInitialJob(user);
            JobEvent initialJobEvent = new JobEvent(spawnTime, initialJob);
            Timer.putEvent(initialJobEvent);
        }
    }
}
