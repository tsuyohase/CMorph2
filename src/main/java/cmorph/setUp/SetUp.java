package cmorph.setUp;

import static cmorph.settings.SimulationConfiguration.DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.MICRO_DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.USER_NUM;

import cmorph.entities.Node;
import cmorph.entities.User;
import cmorph.event.JobEvent;
import cmorph.job.Job;
import cmorph.setUp.UserSetUp.Scenario;
import cmorph.simulator.Simulator;
import cmorph.simulator.Timer;
import cmorph.utils.Point;

public class SetUp {
    /**
     * NodeとUserを生成する
     */
    public static void setUp() {
        setUpNodes();
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
            Node node = new Node(id, nodeLocation, containerNum);
            Simulator.addNode(node);
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
