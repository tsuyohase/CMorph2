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
import cmorph.logger.LinkState;
import cmorph.logger.UserState;
import cmorph.setUp.UserSetUp.UserType;
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
    int speed;
    boolean isLink;

    public MainPanelDrawer(MainPanel mainPanel, int currentTime, boolean isPlaying, int speed,
            JSlider slider, List<TimeStepData> data, ConfigData configData, boolean isLink) {
        this.data = data;
        this.configData = configData;
        this.currentTime = currentTime;
        this.isPlaying = isPlaying;
        this.speed = speed;
        this.slider = slider;
        this.mainPanel = mainPanel;
        this.bufferedImage = mainPanel.bufferedImage;
        this.threadGraphics = bufferedImage.createGraphics();
        threadGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        nodeSizeBase = Math.max(
                bufferedImage.getWidth() / ((configData.getDataCenterNum() + configData.getMicroDataCenterNum()) * 4),
                15);
        userSize = Math.max(bufferedImage.getWidth() / configData.getUserNum(), 5);
        this.isLink = isLink;

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
        int gray_r = 204;
        int gray_g = 204;
        int gray_b = 204;

        int green_r = 0;
        int green_g = 255;
        int green_b = 51;

        int blue_r = 51;
        int blue_g = 153;
        int blue_b = 255;

        int red_r = 255;
        int red_g = 51;
        int red_b = 51;

        int dred_r = 50;
        int dred_g = 0;
        int dred_b = 0;
        if (load == 0) {
            return new Color(gray_r, gray_g, gray_b);
        } else if (load < 0.25) {
            return new Color(green_r, green_g, green_b);
        } else if (load < 0.5) {
            double r = (load - 0.25) / 0.25;
            return new Color((int) (green_r * (1 - r) + r * blue_r), (int) (green_g * (1 - r) + blue_g * r),
                    (int) (green_b * (1 - r) + blue_b * r));
        } else if (load < 0.75) {
            double r = (load - 0.5) / 0.25;
            return new Color((int) (blue_r * (1 - r) + r * red_r), (int) (blue_g * (1 - r) + red_g * r),
                    (int) (blue_b * (1 - r) + red_b * r));
        } else {
            double r = (load - 0.75) / 0.25;
            return new Color((int) (red_r * (1 - r) + r * dred_r), (int) (red_g * (1 - r) + dred_g * r),
                    (int) (red_b * (1 - r) + dred_b * r));
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

    private void drawDotLine(int startX, int startY, int endX, int endY, int lineWidth) {
        threadGraphics.setStroke(new BasicStroke(lineWidth));
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
        threadGraphics.setStroke(new BasicStroke(1));
    }

    private void draw() {
        threadGraphics.setColor(Color.WHITE);
        threadGraphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        TimeStepData stepData = data.get(currentTime);

        // データを描画する処理
        List<UserState> userStates = stepData.getUserStates();
        List<NodeState> nodeStates = stepData.getNodeStates();

        for (int i = 0; i < configData.getLinkCostList().size(); i++) {
            int linkWidth = (int) 10 / configData.getLinkCostList().get(i);
            int nodeX1 = convertPoint(configData.getNodeXList().get(configData.getLinkSrcList().get(i)));
            int nodeY1 = convertPoint(configData.getNodeYList().get(configData.getLinkSrcList().get(i)));
            int nodeX2 = convertPoint(configData.getNodeXList().get(configData.getLinkDstList().get(i)));
            int nodeY2 = convertPoint(configData.getNodeYList().get(configData.getLinkDstList().get(i)));
            threadGraphics.setStroke(new BasicStroke(linkWidth));
            threadGraphics.drawLine(nodeX1, nodeY1, nodeX2, nodeY2);
            threadGraphics.setColor(Color.LIGHT_GRAY);
        }

        for (int i = 0; i < nodeStates.size(); i++) {
            int nodeSize = nodeSizeBase;
            if (configData.getNodeContainerNumList().get(i) > configData.getAveMDCContainerNum()) {
                nodeSize = (int) (3 * nodeSizeBase / 2);
            }
            NodeState nodeState = nodeStates.get(i);

            int nodeX = convertPoint(configData.getNodeXList().get(i)) - nodeSize / 2;
            int nodeY = convertPoint(configData.getNodeYList().get(i)) - nodeSize / 2;

            threadGraphics.setColor(LoadColor(nodeState.getLoad()));
            threadGraphics.fillRect(nodeX, nodeY, nodeSize, nodeSize);
            threadGraphics.drawString(String.valueOf(i), nodeX, nodeY);
        }
        ;

        threadGraphics.setColor(Color.RED);
        threadGraphics.fillOval(0, 10, userSize, userSize);
        threadGraphics.drawString("Interactive", userSize + 10, userSize / 2 + 10);

        threadGraphics.setColor(Color.BLUE);
        threadGraphics.fillOval(0, userSize + 20, userSize, userSize);
        threadGraphics.drawString("Data-Incentive", userSize + 10, userSize + userSize / 2 + 20);

        for (int i = 0; i < userStates.size(); i++) {
            UserState userState = userStates.get(i);

            int userX = convertPoint(userState.getX());
            int userY = convertPoint(userState.getY());

            if (userState.getX() >= 0 && userState.getY() >= 0 && userState.getX() <= MAP_WIDTH
                    && userState.getY() <= MAP_HEIGHT) {
                if (isLink) {
                    int connectedNodeId = userState.getConnectedNodeId();
                    double distance = userState.getConnectedDistance();
                    int lineWidth = 1;
                    if (distance > configData.getNetworkDistanceThreshold()) {
                        threadGraphics.setColor(Color.RED);
                        lineWidth = 3;
                    } else {
                        threadGraphics.setColor(Color.BLUE);
                    }
                    drawDotLine(userX + userSize / 2, userY + userSize / 2,
                            convertPoint(configData.getNodeXList().get(connectedNodeId)),
                            convertPoint(configData.getNodeYList().get(connectedNodeId)), lineWidth);
                }

                if (userState.getUserType() == UserType.INTERACTIVE) {
                    threadGraphics.setColor(Color.RED);
                } else {
                    threadGraphics.setColor(Color.BLUE);
                }
                threadGraphics.fillOval(userX, userY,
                        userSize, userSize);
            }

        }

        mainPanel.repaint();

    }

    @Override
    public void run() {
        while (isPlaying && currentTime < data.size()) {
            draw();
            currentTime += speed;
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
