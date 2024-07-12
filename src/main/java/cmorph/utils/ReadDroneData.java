package cmorph.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static cmorph.settings.SimulationConfiguration.MAP_WIDTH;
import static cmorph.settings.SimulationConfiguration.USER_LOCATION_FILE;
import static cmorph.settings.SimulationConfiguration.USER_LOCATION_FILE_NAME;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadDroneData {
    // ドローンデータを表すクラス
    public static class DroneData {
        public double x;
        public double y;

        // コンストラクタ
        public DroneData() {
        }

        // コンストラクタ
        public DroneData(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "DroneData{" +
                    "x=" + x +
                    "y=" + y +
                    '}';
        }
    }

    public static Point[][] readDroneLocations() {
        // ObjectMapperインスタンスを作成
        ObjectMapper mapper = new ObjectMapper();

        try {
            // JSONファイルを読み込んでJsonNodeにマッピング

            JsonNode rootNode = mapper.readTree(new File(USER_LOCATION_FILE));

            double mapNumRatio = 3;

            // droneData配列ノードを取得
            JsonNode droneDataArrayNode = rootNode.get("droneData");

            // droneData配列の要素をリストに変換
            List<List<DroneData>> droneDataList = mapper.readValue(droneDataArrayNode.traverse(),
                    new TypeReference<List<List<DroneData>>>() {
                    });

            Point[][] droneDataArray = new Point[droneDataList.size()][];
            for (int i = 0; i < droneDataList.size(); i++) {
                List<DroneData> subList = droneDataList.get(i);
                Point[] pointList = new Point[subList.size()];
                for (int j = 0; j < subList.size(); j++) {
                    DroneData droneData = subList.get(j);
                    pointList[j] = new Point(droneData.x / mapNumRatio, droneData.y / mapNumRatio);
                }
                droneDataArray[i] = pointList;
            }
            return droneDataArray;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
