void drawConnections(JSONArray drones, JSONArray nodeXList, JSONArray nodeYList) {
  for (int i = 0; i < drones.size(); i++) {
    JSONObject drone = drones.getJSONObject(i);
    int droneX = drone.getInt("x");
    int droneY = drone.getInt("y");
    Point dronePoint = convertCoordination(droneX, droneY);
    int serverId = drone.getInt("nid");

    for (int j = 0; j < nodeXList.size(); j++) {
        int serverX = nodeXList.getInt(serverId);
        int serverY = nodeYList.getInt(serverId);
        Point serverPoint = convertCoordination(serverX, serverY);
        drawDashedLine(dronePoint.x, dronePoint.y, serverPoint.x, serverPoint.y);
    }
  }
}

void drawDashedLine(float x1, float y1, float x2, float y2) {
  float dashLength = 5;
  float totalLength = dist(x1, y1, x2, y2);
  float numDashes = totalLength / dashLength;
  float dx = (x2 - x1) / numDashes;
  float dy = (y2 - y1) / numDashes;
  boolean drawDash = true;
  stroke(150, 150, 150); 
  for (float i = 0; i < numDashes; i++) {
    if (drawDash) {
      line(x1 + i * dx, y1 + i * dy, x1 + (i + 1) * dx, y1 + (i + 1) * dy);
    }
    drawDash = !drawDash;
  }
}