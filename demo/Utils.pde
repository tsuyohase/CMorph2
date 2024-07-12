Point convertCoordination(float x, float y) {
    float newX = x * sizeRatio + scenarioMapBase.x;
    float newY = y * sizeRatio + scenarioMapBase.y;
    return new Point(newX, newY);
}

void fillColorByLoad(float load){
    color c = getColor(load);
    fill(c);
    //  if (load == 0) {
    //   fill(150);  // 負荷が0なら灰色
    // } else if (load >= 0.9) {
    //   fill(50, 0, 0);
    // } else if (load >= 0.75) {
    //   fill(255, 0, 0);  // 負荷が75以上なら赤色
    // } else {
    //   fill(0, 0, 255);  // それ以外は青色
    // }
}

void fillColorByDistance(float distance){
    color c = getColor(distance / 200);
    fill(c);
    // if (distance >= 120) {
    //   fill(255,0,0);
    // }else {
    //   fill(0,0,255);
    // }
}

// カスタムカラーマップ関数
color getColor(float value) {
  float r, g, b;
  if (value <= 0) {
    return color(120);
  } else if (value <= 0.25) {
    // 緑から黄緑
    r = map(value, 0, 0.25, 0, 173);
    g = 255;
    b = map(value, 0, 0.25, 0, 47);
  } else if (value <= 0.5) {
    // 黄緑から黄
    r = 173;
    g = 255;
    b = map(value, 0.25, 0.5, 47, 0);
  } else if (value <= 0.75) {
    // 黄から橙
    r = map(value, 0.5, 0.75, 255, 255);
    g = map(value, 0.5, 0.75, 255, 165);
    b = 0;
  } else {
    // 橙から赤
    r = map(value, 0.75, 1.0, 255, 255);
    g = map(value, 0.75, 1.0, 165, 0);
    b = 0;
  }
  return color(r, g, b);
}