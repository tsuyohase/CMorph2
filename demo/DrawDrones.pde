void drawDrones(JSONArray drones) {
  for (int i = 0; i < drones.size(); i++) {
    JSONObject drone = drones.getJSONObject(i);
    Point currentPoint = convertCoordination(drone.getFloat("x"), drone.getFloat("y"));
    int node_id = drone.getInt("nid");
    fill(0, 0, 0);  // ドローンは黒色
    ellipse(currentPoint.x, currentPoint.y, 5, 5);
    stroke(0);
    noFill();
    rect(scenarioMapBase.x-5, scenarioMapBase.y-5, scenarioMapWidth+10, scenarioMapWidth+10);
    // text("Node: " + node_id, x + 10, y - 10);
  }
}
