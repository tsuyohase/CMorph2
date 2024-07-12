void controlEvent(ControlEvent theEvent) {
  if (theEvent.isFrom(cp5.getController("currentIndex"))) {
    currentIndex = int(theEvent.getValue());
  } else if (theEvent.isFrom(cp5.getController("fileList"))) {
    int index = int(theEvent.getValue());
    String filePath = fileList.getItem(index).get("value").toString();
    loadData(filePath);
  }
}