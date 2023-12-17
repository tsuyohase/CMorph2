package cmorph.visualizer;

import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import cmorph.logger.SimulationData;
import cmorph.logger.ConfigData;
import cmorph.logger.UserState;
import cmorph.logger.NodeState;
import cmorph.logger.TimeStepData;
import static cmorph.settings.SimulationConfiguration.END_TIME;
import static cmorph.settings.SimulationConfiguration.MAP_HEIGHT;
import static cmorph.settings.SimulationConfiguration.MAP_WIDTH;
import static cmorph.settings.SimulationConfiguration.USER_NUM;

public class MainPanelDrawer extends Thread {
    List<TimeStepData> data;
    ConfigData configData;
    Graphics2D threadGraphics;
    MainPanel mainPanel;
    BufferedImage bufferedImage;
    JSlider slider;
    int currentTime;
    boolean isPlaying;
    int userSize;
    int nodeSizeBase;

    public MainPanelDrawer(MainPanel mainPanel, int currentTime, boolean isPlaying,
            JSlider slider, List<TimeStepData> data, ConfigData configData) {
        this.data = data;
        this.configData = configData;
        this.currentTime = currentTime;
        this.isPlaying = isPlaying;
        this.slider = slider;
        this.mainPanel = mainPanel;
        this.bufferedImage = mainPanel.bufferedImage;
        this.threadGraphics = bufferedImage.createGraphics();
        threadGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        nodeSizeBase = Math.max(
                bufferedImage.getWidth() / ((configData.getDataCenterNum() + configData.getMicroDataCenterNum()) * 2),
                10);
        userSize = Math.max(bufferedImage.getWidth() / configData.getUserNum(), 5);

    }

    public static Color generateRandomColor(int input) {

        String str = String.valueOf(input);

        int hashCode = str.hashCode();

        Random random = new Random(hashCode);

        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        // Colorオブジェクトを生成
        return new Color(red, green, blue);
    }

    public static Color LoadColor(double load) {
        if (load == 0) {
            return Color.GRAY;
        } else if (load < 0.25) {
            return Color.GREEN;
        } else if (load < 0.75) {
            return Color.BLUE;
        } else {
            return Color.RED;
        }
    }

    public static Color connectionColor(int connectionNum) {
        int weight = (int) (256 * connectionNum / USER_NUM);
        return new Color(weight, weight, weight);
    }

    public void stopUser() {
        isPlaying = false;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }

    public int convertPoint(double point) {
        return (int) (0.9 * point * bufferedImage.getWidth() / MAP_WIDTH + bufferedImage.getHeight() * 0.05);
    }

    private void drawDotLine(int startX, int startY, int endX, int endY) {
        int lineLength = 5;
        double distX = endX - startX;
        double distY = endY - startY;
        double distance = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
        double sin = distX / distance;
        double cos = distY / distance;
        double x = startX;
        double y = startY;
        boolean draw = true;
        while (Math.abs(endX - x) > 5 && Math.abs(endY - y) > 5) {
            double nextX = x + sin * lineLength;
            double nextY = y + cos * lineLength;
            if (draw) {
                threadGraphics.drawLine((int) x, (int) y, (int) nextX, (int) nextY);
            }
            draw = !draw;

            x = nextX;
            y = nextY;
        }
        threadGraphics.drawLine((int) x, (int) y, endX, endY);
    }

    private UserState getUserStateById(List<UserState> userStates, int id) {
        for (int i = 0; i < userStates.size(); i++) {
            if (userStates.get(i).getUserId() == id) {
                return userStates.get(i);
            }
        }
        return null;
    }

    private void draw() {
        threadGraphics.setColor(Color.WHITE);
        threadGraphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        TimeStepData stepData = data.get(currentTime);

        // データを描画する処理
        // List<UserState> userStates = stepData.getUserStates();
        List<NodeState> nodeStates = stepData.getNodeStates();

        for (int i = 0; i < nodeStates.size(); i++) {
            int nodeSize = nodeSizeBase;
            if (nodeStates.get(i).getContainerNum() > configData.getAveMDCContainerNum()) {
                nodeSize = (int) (3 * nodeSizeBase / 2);
            }
            NodeState nodeState = nodeStates.get(i);

            int nodeX = convertPoint(configData.getNodeXList().get(i)) - nodeSize / 2;
            int nodeY = convertPoint(configData.getNodeYList().get(i)) - nodeSize / 2;

            threadGraphics.setColor(LoadColor(nodeState.getLoad()));
            threadGraphics.fillOval(nodeX, nodeY, nodeSize, nodeSize);
            threadGraphics.drawString(String.valueOf(i), nodeX, nodeY);

        }

        // for (int i = 0; i < userStates.size(); i++) {
        // UserState userState = userStates.get(i);

        // int userX = convertPoint(userState.getX());
        // int userY = convertPoint(userState.getY());

        // threadGraphics.setColor(Color.BLACK);
        // threadGraphics.fillOval(userX, userY,
        // userSize, userSize);
        // }

        mainPanel.repaint();

    }

    @Override
    public void run() {
        while (isPlaying && currentTime < data.size()) {
            draw();
            currentTime++;
            slider.setValue(currentTime);

            try {
                Thread.sleep(10000 / configData.getEndTime());
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
        if (currentTime < configData.getEndTime()) {
            draw();
        }
    }

}
