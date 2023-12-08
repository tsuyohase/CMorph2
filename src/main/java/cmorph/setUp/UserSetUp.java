package cmorph.setUp;

import static cmorph.settings.SimulationConfiguration.END_TIME;
import static cmorph.settings.SimulationConfiguration.MAP_HEIGHT;
import static cmorph.settings.SimulationConfiguration.MAP_WIDTH;
import static cmorph.settings.SimulationConfiguration.USER_LOCATION_SCENARIO;
import static cmorph.settings.SimulationConfiguration.USER_NUM;
import static cmorph.settings.SimulationConfiguration.USER_SPAWN_SCENARIO;
import static cmorph.simulator.Main.random;

import org.omg.CORBA.SystemException;

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
        RANDOM_LOCATION_SATY,
        STRAIGHT_DOWN,
        RANDOM,
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
            spawnTime = 0;
        } else if (USER_SPAWN_SCENARIO == UserSpawnScenario.WAVE) {
            spawnTime = 0;
        } else if (USER_SPAWN_SCENARIO == UserSpawnScenario.RANDOM) {
            spawnTime = 0;
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
            despawnTime = END_TIME;
        } else if (USER_SPAWN_SCENARIO == UserSpawnScenario.WAVE) {
            despawnTime = END_TIME;
        } else if (USER_SPAWN_SCENARIO == UserSpawnScenario.RANDOM) {
            despawnTime = END_TIME;
        } else {
            throw new Error("UserSpawnScenario is not defined.");
        }
        return despawnTime;
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
        } else if (USER_LOCATION_SCENARIO == UserLocationScenario.RANDOM) {
            // ランダムに配置
            initPoint = new Point(Math.random() * MAP_WIDTH, Math.random() * MAP_WIDTH);

            // ランダムに移動するシナリオ
            return randomScenario(initPoint, spawnTime, despawnTime);
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

    /**
     * ランダムに移動するユーザのシナリオを返す
     * 
     * @param initPoint
     * @param spawnTime
     * @param despawnTime
     * @return Scenario
     */
    private static Scenario randomScenario(Point initPoint, long spawnTime, long despawnTime) {
        return (time) -> {
            double xVelocity = random.nextDouble() * MAP_WIDTH / END_TIME;
            double yVelocity = random.nextDouble() * MAP_HEIGHT / END_TIME;
            if ((time >= spawnTime) && (time <= despawnTime)) {
                return new Point(initPoint.getX() + (time - spawnTime) * xVelocity,
                        initPoint.getY() + (time - spawnTime) * yVelocity);
            } else {
                return new Point(-1, -1);
            }
        };
    }

}
