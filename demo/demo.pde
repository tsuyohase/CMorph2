import controlP5.*;
import java.io.File;

ControlP5 cp5;
int currentIndex = 0;
int maxIndex;

boolean showDashedLines = true;  
boolean isPlaying = false;

float sizeRatio = 1;

int demoWidth = 1980;
int demoHeight = 1080;

int margin = 50;

Point scenarioMapBase = new Point(margin, margin);
int scenarioMapWidth = (int) (demoHeight * 0.7);

Point loadGraphBase = new Point(scenarioMapWidth + 3*margin, margin);
int loadGraphWidth = (int) (demoWidth / 2 *0.6);
int loadGraphHeight = (int) (demoHeight / 2 * 0.7);

Point connectionGraphBase = new Point(scenarioMapWidth + 3 * margin,loadGraphHeight + 3*margin);
int connectionGraphWidth = (int) (demoWidth / 2 * 0.6);
int connectionGraphHeight = (int)  (demoHeight / 2 *0.7);

Point controlButtonBase = new Point(scenarioMapWidth + loadGraphWidth + 4 * margin ,margin);
int controlButtonWidth = 200;
int controlButtonHeight = 50;

JSONObject[] data;
JSONArray nodeXList;
JSONArray nodeYList;

String folderPath = sketchPath("demo/scenario");
DropdownList fileList;

void settings() {
  size(demoWidth, demoHeight);
}

void setup() {
  textSize(32);
  String filePath = "scenario/read0.4-convex-pow8.json";
  loadData(filePath);  // データをロード
  

  frameRate(30);  // フレームレートを設定

  cp5 = new ControlP5(this);
  cp5.addSlider("currentIndex")
     .setPosition(width / 4 - 250, height - 110)
     .setSize(width / 2, 30)
     .setRange(0, maxIndex)
     .setValue(0)
     .setNumberOfTickMarks(maxIndex + 1)
     .snapToTickMarks(true);

  cp5.addButton("playPause")
     .setLabel("Play/Pause")
     .setPosition(controlButtonBase.x, controlButtonBase.y)
     .setSize(controlButtonWidth, controlButtonHeight)
     .onClick(new CallbackListener() {
       public void controlEvent(CallbackEvent event) {
         isPlaying = !isPlaying;
         println("isPlaying: " + isPlaying);  // デバッグ用
       }
     });
    
  cp5.addButton("toggleDashedLines")
     .setLabel("Toggle Dashed Lines")
     .setPosition(controlButtonBase.x, controlButtonBase.y + controlButtonHeight + margin)
     .setSize(controlButtonWidth, controlButtonHeight)
     .onClick(new CallbackListener() {
       public void controlEvent(CallbackEvent event) {
         showDashedLines = !showDashedLines;
       }
     });

  

  fileList = cp5.addDropdownList("fileList")
                .setPosition(controlButtonBase.x, controlButtonBase.y +2 * (controlButtonHeight + margin))
                .setSize(controlButtonWidth, 200)
                .onClick(new CallbackListener() {
                  public void controlEvent(CallbackEvent event) {
                    loadFileList(folderPath);
                  }
                });
  
  customizeDropdownList(fileList);
  loadFileList(folderPath);
}

void customizeDropdownList(DropdownList ddl) {
  ddl.setItemHeight(30);  // 各要素の高さを設定
  ddl.setBarHeight(30);  // ドロップダウンリストのバーの高さを設定
  ddl.setColorBackground(color(60));
  ddl.setColorActive(color(255, 128));
}

void loadFileList(String folderPath) {
  fileList.clear();
  File folder = new File(folderPath);
  if (!folder.exists() || !folder.isDirectory()) {
    println("Folder does not exist or is not a directory: " + folderPath);  // デバッグ用
    return;
  }

  File[] files = folder.listFiles();
  if (files != null) {
    for (File file : files) {
      if (file.isFile() && file.getName().endsWith(".json")) {
        fileList.addItem(file.getName(), file.getAbsolutePath());
      }
    }
  }
}

void loadData(String filePath) {
  println("Loading data from file: " + filePath);  // デバッグ用
  JSONObject jsonObject;
  try {
    jsonObject = loadJSONObject(filePath);
  } catch (Exception e) {
    println("Error loading JSON file: " + e.getMessage());
    e.printStackTrace();
    return;
  }

  JSONObject configData = jsonObject.getJSONObject("configData");
  JSONArray timeStepData = jsonObject.getJSONObject("simulationData").getJSONArray("timeStepData");
  nodeXList = configData.getJSONArray("nodeXList");
  nodeYList = configData.getJSONArray("nodeYList");

  int mapHeight = configData.getInt("mapHeight");
  println(scenarioMapWidth);
  sizeRatio = (float) scenarioMapWidth / (float) mapHeight;
  
  data = new JSONObject[timeStepData.size()];
  for (int i = 0; i < timeStepData.size(); i++) {
    data[i] = timeStepData.getJSONObject(i);
  }
  maxIndex = data.length -  1;
  if (cp5 != null) {
    Slider currentIndexSlider = (Slider) cp5.getController("currentIndex");
    currentIndexSlider.setRange(0, maxIndex);
    currentIndexSlider.setNumberOfTickMarks(maxIndex + 1);
    currentIndexSlider.setValue(0);
    println("Slider range set to 0 to " + maxIndex);  // デバッグ用
  } else {
    println("currentIndexSlider is null");  // デバッグ用
  }
  currentIndex = 0;
}

void draw() {
  background(255);
  if (data != null) {
    drawCurrentFrame();
    if (isPlaying) {
      currentIndex += 2;
      if (currentIndex > maxIndex) {
        currentIndex = 0;
      }
      cp5.getController("currentIndex").setValue(currentIndex);  // スライダーの位置を更新
      // println("Updated currentIndex: " + currentIndex);  // デバッグ用
    }
  }
}

void drawCurrentFrame() {
  if (currentIndex <= maxIndex) {
    JSONObject frameData = data[currentIndex];
    JSONArray drones = frameData.getJSONArray("userStates");
    JSONArray servers = frameData.getJSONArray("nodeStates");

    drawServers(servers, nodeXList, nodeYList);  // 別ファイルに定義されている関数
    drawDrones(drones);  // 別ファイルに定義されている関数
    if (showDashedLines) {
      drawConnections(drones, nodeXList, nodeYList);  // 別ファイルに定義されている関数
    }
    drawLoadGraph(servers);  // 別ファイルに定義されている関数
    drawConnectionGraph(drones);
  }
}
