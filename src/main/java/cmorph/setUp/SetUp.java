package cmorph.setUp;

import static cmorph.settings.SimulationConfiguration.DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.MICRO_DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.USER_NUM;

import cmorph.entities.Node;
import cmorph.entities.User;
import cmorph.job.Job;
import cmorph.setUp.UserSetUp.Scenario;
import cmorph.simulator.Simulator;
import cmorph.utils.Point;

public class SetUp {
    /**
     * Nodeを生成し, Simulatorのリストに追加する
     * 
     * @param
     * @return
     */
    public static void setUpNodes() {
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
    public static void setUpUsers() {
        for (int id = 0; id < USER_NUM; id++) {
            long spawnTime = UserSetUp.getSpawnTime(id);
            long despawnTime = UserSetUp.getDespawnTime(id);
            Scenario scenario = UserSetUp.getScenario(id, spawnTime, despawnTime);
            User user = new User(id, scenario);
            Simulator.addUser(user);

            Job initialJob = JobSetUp.getInitialJob(user);
            // Todo Event処理追加
        }
    }
}
