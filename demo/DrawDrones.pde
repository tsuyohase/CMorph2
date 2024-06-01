void drawDrones(JSONArray drones) {
  for (int i = 0; i < drones.size(); i++) {
    JSONObject drone = drones.getJSONObject(i);
    Point currentPoint = convertCoordination(drone.getInt("x"), drone.getInt("y"));
    int node_id = drone.getInt("connectedNodeId");
    fill(0, 0, 0);  // ドローンは黒色
    ellipse(currentPoint.x, currentPoint.y, 5, 5);
    fill(0);
    // text("Node: " + node_id, x + 10, y - 10);
  }
}
