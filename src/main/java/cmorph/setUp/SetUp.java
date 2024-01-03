package cmorph.setUp;

import static cmorph.settings.SimulationConfiguration.BAND_WIDTH;
import static cmorph.settings.SimulationConfiguration.DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.MICRO_DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.USER_NUM;

import cmorph.entities.Link;
import cmorph.entities.Node;
import cmorph.entities.User;
import cmorph.event.JobEvent;
import cmorph.job.Job;
import cmorph.setUp.UserSetUp.Scenario;
import cmorph.simulator.Simulator;
import cmorph.simulator.Timer;
import cmorph.utils.Point;
import java.util.ArrayList;

public class SetUp {
    /**
     * NodeとUserを生成する
     */
    public static void setUp() {
        setUpNodes();
        setUpLinks();
        setUpUsers();
    }

    /**
     * Nodeを生成し, Simulatorのリストに追加する
     * 
     * @param
     * @return
     */
    private static void setUpNodes() {
        for (int id = 0; id < MICRO_DATA_CENTER_NUM + DATA_CENTER_NUM; id++) {
            Point nodeLocation = NodeSetUp.getNodeLocation(id);
            int containerNum = NodeSetUp.getNodeContainerNum(id);
            int costWeight = NodeSetUp.getNodeCostWeight(id);
            Node node = new Node(id, nodeLocation, containerNum, costWeight);
            Simulator.addNode(node);
        }
    }

    private static void setUpLinks() {
        int id = 0;
        ArrayList<Node> nodes = Simulator.getSimulatedNodes();
        for (int i = 0; i < MICRO_DATA_CENTER_NUM + DATA_CENTER_NUM; i++) {
            for (int j = 0; j < MICRO_DATA_CENTER_NUM + DATA_CENTER_NUM; j++) {
                int bandwidth = BAND_WIDTH[i][j];
                if (bandwidth == 0) {
                    continue;
                }
                Link link = new Link(id, nodes.get(i), nodes.get(j), bandwidth);
                Simulator.addLink(link);
                id++;
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
        for (int id = 0; id < USER_NUM; id++) {
            // 出現時間と消滅時間を取得
            long spawnTime = UserSetUp.getSpawnTime(id);
            long despawnTime = UserSetUp.getDespawnTime(id);

            // シナリオを取得
            Scenario scenario = UserSetUp.getScenario(id, spawnTime, despawnTime);

            User user = new User(id, scenario);
            Simulator.addUser(user);

            // ユーザごとに最初のJobを生成
            Job initialJob = JobSetUp.getInitialJob(user);
            JobEvent initialJobEvent = new JobEvent(spawnTime, initialJob);
            Timer.putEvent(initialJobEvent);
        }
    }
}
