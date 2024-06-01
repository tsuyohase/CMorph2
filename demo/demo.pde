import controlP5.*;
import java.io.File;

ControlP5 cp5;
int currentIndex = 0;
int maxIndex;

boolean showDashedLines = true;  

float sizeRatio =1;
Point scenarioMapBase;

JSONObject[] data;
JSONArray nodeXList;
JSONArray nodeYList;

String folderPath = sketchPath("demo/scenario");
DropdownList fileList;


void setup() {
  size(1980, 1080);
  String filePath = "scenario/random0.2-convex-pow4.json";
  JSONObject jsonObject = loadJSONObject(filePath);
  JSONObject configData = jsonObject.getJSONObject("configData");
  JSONArray timeStepData = jsonObject.getJSONObject("simulationData").getJSONArray("timeStepData");
  nodeXList = configData.getJSONArray("nodeXList");
  nodeYList = configData.getJSONArray("nodeYList");

  int mapHeight = configData.getInt("mapHeight");
  sizeRatio = height / mapHeight;
  scenarioMapBase = new Point(0,0);
  
  data = new JSONObject[timeStepData.size()];
  for (int i = 0; i < timeStepData.size(); i++) {
    data[i] = timeStepData.getJSONObject(i);
  }
  maxIndex = data.length - 1;
  
  frameRate(30);  // フレームレートを設定

  cp5 = new ControlP5(this);
  cp5.addSlider("currentIndex")
     .setPosition(width / 4 - 100, height-100)
     .setSize(width/2, 30)
     .setRange(0, maxIndex)
     .setValue(0)
     .setNumberOfTickMarks(maxIndex + 1)
     .snapToTickMarks(true);

  cp5.addButton("toggleDashedLines")
     .setLabel("Toggle Dashed Lines")
     .setPosition(20, height - 100)
     .setSize(200, 30)
     .onClick(new CallbackListener() {
       public void controlEvent(CallbackEvent event) {
         showDashedLines = !showDashedLines;
       }
     });

  // cp5.addButton("loadFiles")
  //    .setLabel("Load Files")
  //    .setPosition(240, 20)
  //    .setSize(200, 40)
  //    .onClick(new CallbackListener() {
  //      public void controlEvent(CallbackEvent event) {
  //        loadFileList(folderPath);
  //      }
  //    });

  fileList = cp5.addDropdownList("fileList")
                .setPosition(width - 300, 100)
                .setSize(200, 200)
                .onClick(new CallbackListener() {
                  public void controlEvent(CallbackEvent event) {
                    loadFileList(folderPath);
                  }
                });
  
  customizeDropdownList(fileList);
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
  File[] files;
  if (folder.isDirectory()) {
    files = folder.listFiles();
  }else {
    return;
  }
  if (files != null) {
    for (File file : files) {
      if (file.isFile() && file.getName().endsWith(".json")) {
        fileList.addItem(file.getName(), file.getAbsolutePath());
      }
    }
  }
}

void loadData(String filePath) {
  JSONObject jsonObject = loadJSONObject(filePath);
  JSONObject configData = jsonObject.getJSONObject("configData");
  JSONArray timeStepData = jsonObject.getJSONObject("simulationData").getJSONArray("timeStepData");
  nodeXList = configData.getJSONArray("nodeXList");
  nodeYList = configData.getJSONArray("nodeYList");

  int mapHeight = configData.getInt("mapHeight");
  sizeRatio = height / mapHeight;
  scenarioMapBase = new Point(0,0);
  
  data = new JSONObject[timeStepData.size()];
  for (int i = 0; i < timeStepData.size(); i++) {
    data[i] = timeStepData.getJSONObject(i);
  }
  maxIndex = data.length - 1;
  cp5.getController("currentIndex")
    //  .setRange(0, maxIndex)
    //  .setNumberOfTickMarks(maxIndex + 1)
     .setValue(0);
  currentIndex = 0;
}

void draw() {
  background(255);
  drawCurrentFrame();
}

void drawCurrentFrame() {
  if (currentIndex <= maxIndex) {
    JSONObject frameData = data[currentIndex];
    JSONArray drones = frameData.getJSONArray("userStates");
    JSONArray servers = frameData.getJSONArray("nodeStates");

    drawServers(servers, nodeXList, nodeYList);
    drawDrones(drones);
    if (showDashedLines){
      drawConnections(drones, nodeXList, nodeYList);
    }
    drawGraph(servers);
  }
}
