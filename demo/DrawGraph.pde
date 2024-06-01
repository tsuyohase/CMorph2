void drawGraph(JSONArray servers) {
  int graphX = width/2;  // グラフの開始位置
  int graphY =100;
  int graphWidth = width/4;
  int graphHeight = 200;
  int barWidth = graphWidth / servers.size();
  int margin = 20;

  stroke(0);
  noFill();
  rect(graphX, graphY, graphWidth, graphHeight);
  text("Server Load Graph", graphX, 50);

  int numTicks = 5;
  for (int i = 0; i <= numTicks; i++) {
    int tickY = graphY + graphHeight - i * (graphHeight / numTicks);
    float tickValue = (float) map(i, 0, numTicks, 0, 1);
    text(tickValue, graphX - margin, tickY);
    line(graphX - 5, tickY, graphX, tickY);
  }

  for (int i = 0; i < servers.size(); i++) {
    JSONObject server = servers.getJSONObject(i);
    float load = server.getFloat("load");
    float barHeight = map(load, 0, 1, 0, graphHeight);
    fillColorByLoad(load);
    rect(graphX + i * barWidth, 100 + graphHeight - barHeight, barWidth - 10, barHeight);
    fill(0);
    text(i, graphX + i * barWidth, 100 + graphHeight + 20);
  }
}
