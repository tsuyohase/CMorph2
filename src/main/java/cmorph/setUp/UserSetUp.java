package cmorph.setUp;

import static cmorph.settings.SimulationConfiguration.DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.END_TIME;
import static cmorph.settings.SimulationConfiguration.INTERAXTIVE_JOB_PROBABILITY;
import static cmorph.settings.SimulationConfiguration.MAP_HEIGHT;
import static cmorph.settings.SimulationConfiguration.MAP_WIDTH;
import static cmorph.settings.SimulationConfiguration.MICRO_DATA_CENTER_NUM;
import static cmorph.settings.SimulationConfiguration.NETWORK_DISTANCE_LIMIT;
import static cmorph.settings.SimulationConfiguration.RANDOM_NETWORK_THRESHOLD;
import static cmorph.settings.SimulationConfiguration.USER_LOCATION_SCENARIO;
import static cmorph.settings.SimulationConfiguration.USER_NUM;
import static cmorph.settings.SimulationConfiguration.USER_SPAWN_SCENARIO;
import static cmorph.simulator.Main.random;

import cmorph.entities.Node;
import cmorph.simulator.Simulator;
import cmorph.utils.Point;

public class UserSetUp {

    /**
     * time を受け取って、その時のユーザの位置を返す関数のinterface
     */
    public static interface Scenario {
        Point apply(long time);
    }

    /**
     * ユーザの位置のシナリオ
     */
    public static enum UserLocationScenario {
        STAY,
        UP_LEFT_STAY,
        RANDOM_LOCATION_SATY,
        STRAIGHT_DOWN,
        RANDOM_DOWN,
        RANDOM,
        TRANSIT_STUB,
    }

    /**
     * ユーザ出現のシナリオ
     */
    public static enum UserSpawnScenario {
        BEGINNING,
        MOUNTAIN,
        WAVE,
        RANDOM
    }

    public static enum UserType {
        INTERACTIVE,
        DATA_INCENTIVE
    }

    /**
     * ユーザの出現時間を返す
     * 
     * @param id
     * @return
     */
    public static long getSpawnTime(int id) {
        // ユーザの出現時間
        long spawnTime;
        if (USER_SPAWN_SCENARIO == UserSpawnScenario.BEGINNING) {
            spawnTime = 0;
        } else if (USER_SPAWN_SCENARIO == UserSpawnScenario.MOUNTAIN) {
            spawnTime = (END_TIME / 3) * id / USER_NUM;
        } else if (USER_SPAWN_SCENARIO == UserSpawnScenario.WAVE) {
            spawnTime = 0;
        } else if (USER_SPAWN_SCENARIO == UserSpawnScenario.RANDOM) {
            spawnTime = (long) (random.nextDouble() * END_TIME);
        } else {
            throw new Error("UserSpawnScenario is not defined.");
        }
        return spawnTime;
    }

    /**
     * ユーザの消滅時間を返す
     * 
     * @param id
     * @return
     */
    public static long getDespawnTime(int id) {
        // ユーザの消滅時間
        long despawnTime;
        if (USER_SPAWN_SCENARIO == UserSpawnScenario.BEGINNING) {
            despawnTime = END_TIME;
        } else if (USER_SPAWN_SCENARIO == UserSpawnScenario.MOUNTAIN) {
            despawnTime = (2 * END_TIME / 3) + (END_TIME / 3) * id / USER_NUM;
        } else if (USER_SPAWN_SCENARIO == UserSpawnScenario.WAVE) {
            despawnTime = END_TIME;
        } else if (USER_SPAWN_SCENARIO == UserSpawnScenario.RANDOM) {
            despawnTime = END_TIME;
        } else {
            throw new Error("UserSpawnScenario is not defined.");
        }
        return despawnTime;
    }

    public static UserType getUserType(int id) {
        if (MICRO_DATA_CENTER_NUM == 0) {
            return UserType.INTERACTIVE;
        }
        if (id % MICRO_DATA_CENTER_NUM <= (int) (MICRO_DATA_CENTER_NUM * INTERAXTIVE_JOB_PROBABILITY)) {
            return UserType.INTERACTIVE;
        } else {
            return UserType.DATA_INCENTIVE;
        }
    }

    public static double getNetworkThreshold(int id) {
        if (RANDOM_NETWORK_THRESHOLD) {
            return random.nextDouble() * NETWORK_DISTANCE_LIMIT * 0.5;
        } else {
            return NETWORK_DISTANCE_LIMIT;
        }
    }

    /**
     * ユーザのシナリオを返す
     * 
     * @param id,spawnTime,despawnTime
     * @return
     */
    public static Scenario getScenario(int id, long spawnTime, long despawnTime) {
        // ユーザの初期位置を決定しUserScenarioを返す
        Point initPoint;
        if (USER_LOCATION_SCENARIO == UserLocationScenario.STAY) {
            // 中心に配置
            initPoint = new Point(MAP_WIDTH / 2, MAP_WIDTH / 2);

            // 移動しないシナリオ
            return getStayScenario(initPoint, spawnTime, despawnTime);
        } else if (USER_LOCATION_SCENARIO == UserLocationScenario.UP_LEFT_STAY) {
            // 左上に配置
            initPoint = new Point(0, 0);

            // 移動しないシナリオ
            return getStayScenario(initPoint, spawnTime, despawnTime);
        } else if (USER_LOCATION_SCENARIO == UserLocationScenario.RANDOM_LOCATION_SATY) {
            // ランダムに配置
            initPoint = new Point(random.nextDouble() * MAP_WIDTH, random.nextDouble() * MAP_WIDTH);

            // 移動しないシナリオ
            return getStayScenario(initPoint, spawnTime, despawnTime);
        } else if (USER_LOCATION_SCENARIO == UserLocationScenario.STRAIGHT_DOWN) {
            // 上部に一直線に配置
            initPoint = new Point(MAP_WIDTH * ((double) id / USER_NUM), 0);

            // 下に一直線に移動するシナリオ
            return straightDownScenario(initPoint, spawnTime, despawnTime);
        } else if (USER_LOCATION_SCENARIO == UserLocationScenario.RANDOM_DOWN) {
            // ランダムに配置
            initPoint = new Point(Math.random() * MAP_WIDTH, 0);

            // 下に一直線に移動するシナリオ
            return randomDownScenario(initPoint, spawnTime, despawnTime);

        } else if (USER_LOCATION_SCENARIO == UserLocationScenario.RANDOM) {
            // ランダムに配置
            initPoint = new Point(Math.random() * MAP_WIDTH, Math.random() * MAP_WIDTH);

            // ランダムに移動するシナリオ
            return randomScenario(initPoint, spawnTime, despawnTime);
        } else if (USER_LOCATION_SCENARIO == UserLocationScenario.TRANSIT_STUB) {
            // Stubの周りに配置
            Node stubNode = Simulator.getSimulatedStubs().get(id % MICRO_DATA_CENTER_NUM);
            Point stubNodePoint = stubNode.getLocation();
            double linkLengthBase = Simulator.getSimulatedNodes().get(0).getLocation().getDistance(
                    Simulator.getSimulatedNodes().get(1).getLocation());
            double linkLength = linkLengthBase * (2 + random.nextDouble()) / 10;
            double theta = 2 * Math.PI * random.nextDouble();

            double x = stubNodePoint.getX() + linkLength * Math.sin(theta);
            double y = stubNodePoint.getY() + linkLength * Math.cos(theta);
            initPoint = new Point(x, y);
            return getStayScenario(initPoint, spawnTime, despawnTime);
        } else {
            throw new Error("UserLocationScenario is not defined.");
        }
    }

    /**
     * 動かないユーザのシナリオを返す
     * 
     * @param initPoint
     * @param spawnTime
     * @param despawnTime
     * @return Scenario
     */
    private static Scenario getStayScenario(Point initPoint, long spawnTime, long despawnTime) {
        return (time) -> {
            if ((time >= spawnTime) && (time <= despawnTime)) {
                return initPoint;
            } else {
                return new Point(-1, -1);
            }
        };
    }

    /**
     * 下に一直線に移動するユーザのシナリオを返す
     * 
     * @param initPoint
     * @param spawnTime
     * @param despawnTime
     * @return Scenario
     */
    private static Scenario straightDownScenario(Point initPoint, long spawnTime, long despawnTime) {
        return (time) -> {
            double velocity = (double) MAP_HEIGHT / END_TIME;
            if ((time >= spawnTime) && (time <= despawnTime)) {
                return new Point(initPoint.getX(), initPoint.getY() + (time - spawnTime) * velocity);
            } else {
                return new Point(-1, -1);
            }
        };
    }

    private static Scenario randomDownScenario(Point initPoint, long spawnTime, long despawnTime) {
        double velocity = (0.5 + random.nextDouble()) * 10 * MAP_HEIGHT / END_TIME;

        return (time) -> {
            if ((time >= spawnTime) && (time <= despawnTime)) {
                return new Point(initPoint.getX(), initPoint.getY() + (time - spawnTime) * velocity);
            } else {
                return new Point(-1, -1);
            }
        };
    }

    /**
     * ランダムに移動するユーザのシナリオを返す
     * 
     * @param initPoint
     * @param spawnTime
     * @param despawnTime
     * @return Scenario
     */
    private static Scenario randomScenario(Point initPoint, long spawnTime, long despawnTime) {
        double xVelocity = random.nextDouble() * 10 * MAP_WIDTH / END_TIME;
        double yVelocity = random.nextDouble() * 10 * MAP_HEIGHT / END_TIME;

        return (time) -> {
            if ((time >= spawnTime) && (time <= despawnTime)) {
                return new Point((initPoint.getX() + (time - spawnTime) * xVelocity),
                        (initPoint.getY() + (time - spawnTime) * yVelocity));
            } else {
                return new Point(-1, -1);
            }
        };
    }

}
