Point convertCoordination(int x, int y) {
    int newX = (int) (x * sizeRatio + scenarioMapBase.x);
    int newY = (int) (y * sizeRatio + scenarioMapBase.y);
    return new Point(newX, newY);
}

void fillColorByLoad(float load){
     if (load == 0) {
      fill(150);  // 負荷が0なら灰色
    } else if (load >= 0.75) {
      fill(255, 0, 0);  // 負荷が75以上なら赤色
    } else {
      fill(0, 0, 255);  // それ以外は青色
    }
}