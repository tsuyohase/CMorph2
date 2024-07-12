void drawConnectionGraph(JSONArray drones) {
  float barWidth = (float) connectionGraphWidth / drones.size();
  int margin = 30;

  stroke(0);
  noFill();
  rect(connectionGraphBase.x, connectionGraphBase.y, connectionGraphWidth, connectionGraphHeight);
  text("Drone Connection Graph", connectionGraphBase.x +120, connectionGraphBase.y-10);

  int numTicks = 7;
  for (int i = 0; i <= numTicks; i++) {
    int tickY = (int) (connectionGraphBase.y + connectionGraphHeight - i * (connectionGraphHeight / numTicks));
    int tickValue = (int) map(i, 0, numTicks, 0, 420);
    text(tickValue, connectionGraphBase.x - 2*margin, tickY + 10);
    line(connectionGraphBase.x - 5, tickY, connectionGraphBase.x, tickY);
  }

  for (int i = 0; i <drones.size(); i++) {
    JSONObject drone = drones.getJSONObject(i);
    float distance = drone.getFloat("d");
    float barHeight = map(distance, 0, 420, 0, connectionGraphHeight);
    fillColorByDistance(distance);
    rect(connectionGraphBase.x + i * barWidth, connectionGraphBase.y+ connectionGraphHeight - barHeight, barWidth, barHeight);
    fill(0);
    // text(i, connectionGraphBase.x + i * barWidth, 100 + connectionGraphHeight + 20);
  }
}
