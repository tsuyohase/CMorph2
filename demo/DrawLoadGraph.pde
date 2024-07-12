void drawLoadGraph(JSONArray servers) {
  int barWidth = loadGraphWidth / servers.size();
  int margin = barWidth;

  stroke(0);
  noFill();
  rect(loadGraphBase.x, loadGraphBase.y, loadGraphWidth, loadGraphHeight);
  text("Server Load Graph", loadGraphBase.x + 140, loadGraphBase.y - 10);

  int numTicks = 4;
  for (int i = 0; i <= numTicks; i++) {
    int tickY = (int) (loadGraphBase.y + loadGraphHeight - i * (loadGraphHeight / numTicks));
    float tickValue = (float) map(i, 0, numTicks, 0, 1);
    String formattedNum = nf(tickValue, 0, 2);
    text(formattedNum, loadGraphBase.x - margin - 5, tickY+10);
    line(loadGraphBase.x - 5, tickY, loadGraphBase.x, tickY);
  }

  for (int i = 0; i < servers.size(); i++) {
    JSONObject server = servers.getJSONObject(i);
    float load = server.getFloat("load");
    float barHeight = map(load, 0, 1, 0, loadGraphHeight);
    fillColorByLoad(load);
    rect(loadGraphBase.x + i * barWidth,loadGraphBase.y + loadGraphHeight - barHeight, barWidth - 10, barHeight);
    fill(0);
    text(i, loadGraphBase.x + i * barWidth + 10, loadGraphBase.y + loadGraphHeight + 30);
  }
}
