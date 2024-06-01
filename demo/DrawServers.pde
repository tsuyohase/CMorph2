void drawServers(JSONArray servers, JSONArray nodeXList, JSONArray nodeYList) {
  for (int i = 0; i < servers.size(); i++) {
    JSONObject server = servers.getJSONObject(i);
    Point point = convertCoordination(nodeXList.getInt(i), nodeYList.getInt(i));
    float load = server.getFloat("load");
    fillColorByLoad(load); // 負荷に応じて色を変える（赤）
    ellipse(point.x, point.y, 30, 30);
    fill(0);
    // text("Load: " + load, x + 30, y);
  }
}
