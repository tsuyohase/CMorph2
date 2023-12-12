package cmorph.visualizer;

import javax.swing.*;

import static cmorph.settings.SimulationConfiguration.END_TIME;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;

import cmorph.logger.SimulationData;
import cmorph.logger.ConfigData;
import cmorph.logger.NodeState;
import cmorph.logger.TimeStepData;

public class LoadChartPanel extends JPanel {
    List<TimeStepData> data;
    ConfigData configData;
    List<Double> loads;
    BufferedImage bufferedImage;
    Graphics2D bufferedGraphics;
    int margin;
    List<Color> colors;
    List<String> legend;
    int currentTime;
    boolean changeData;

    public LoadChartPanel(int size, List<TimeStepData> data, ConfigData configData) {
        this.margin = size / 10;

        setPreferredSize(new Dimension((int) size, size / 2));
        bufferedImage = new BufferedImage(
                (int) size, size / 2, BufferedImage.TYPE_INT_RGB);
        bufferedGraphics = bufferedImage.createGraphics();
        bufferedGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        bufferedGraphics.setColor(Color.WHITE);
        bufferedGraphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

        this.data = data;
        this.configData = configData;
        List<NodeState> initLoads = this.data.get(0).getNodeStates();
        this.loads = new ArrayList<>();
        this.colors = new ArrayList<>();
        this.legend = new ArrayList<>();
        this.changeData = true;

        // サーバーごとに異なる色を設定
        Random rand = new Random(26);
        for (int i = 0; i < initLoads.size(); i++) {
            loads.add(initLoads.get(i).getLoad());
            colors.add(new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
            if (initLoads.get(i).getContainerNum() > configData.getAveMDCContainerNum()) {
                legend.add("DC " + i);
            } else {
                legend.add("MDC " + i);
            }
        }
    }

    private int convertLoad(double load, int graphHeight) {
        if (load <= 1) {
            return (int) (getHeight() - margin - load * graphHeight);
        } else {
            return getHeight() - margin;
        }
    }

    private int convertTime(int t, int graphWidth) {
        return (int) (margin + t * graphWidth / data.size());
    }

    public void setChartLine(int currentTime) {
        this.currentTime = currentTime;
    }

    public void setData(List<TimeStepData> data) {
        this.data = data;
        List<NodeState> initLoads = this.data.get(0).getNodeStates();
        this.loads = new ArrayList<>();
        this.colors = new ArrayList<>();
        this.legend = new ArrayList<>();
        this.changeData = true;

        // サーバーごとに異なる色を設定
        Random rand = new Random(26);
        for (int i = 0; i < initLoads.size(); i++) {
            loads.add(initLoads.get(i).getLoad());
            colors.add(new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
            if (initLoads.get(i).getContainerNum() > configData.getAveMDCContainerNum()) {
                legend.add("DC " + i);
            } else {
                legend.add("MDC " + i);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int graphWidth = getWidth() - 2 * margin;
        int graphHeight = getHeight() - 2 * margin;

        // グラフエリアを白で塗りつぶし
        g.setColor(Color.WHITE);
        g.fillRect(margin / 2, margin / 2, graphWidth + margin * 2, graphHeight + margin);

        g.setColor(Color.BLACK);

        g.drawLine(margin, getHeight() - margin, margin + graphWidth, getHeight() - margin);
        g.drawLine(margin, margin, margin, getHeight() - margin);
        g.drawString("time", getWidth() - margin, getHeight() - margin + 15);
        g.drawString("load", margin - 30, margin - 10);
        // X軸の目盛りとラベルを追加
        for (int t = 1; t < data.size(); t++) {
            if (t % (data.size() / 10) == 0) {
                int x = convertTime(t, graphWidth);
                g.drawLine(x, getHeight() - margin, x, getHeight() - margin + 5);
                g.drawString(Integer.toString(t), x - 10, getHeight() - margin + 25);
            }

        }

        // Y軸の目盛りとラベルを追加
        for (int i = 0; i <= 10; i++) {
            int y = getHeight() - margin - i * graphHeight / 10;
            g.drawLine(margin - 5, y, margin, y);
            g.drawString(Double.toString((double) i / 10), margin - 30, y + 5);
        }

        // 背景の塗りつぶし
        g.setColor(new Color(255, 230, 230)); // 薄い赤色
        g.fillRect(margin + 1, margin, graphWidth, (getHeight() - margin * 2) / 4);
        g.setColor(new Color(240, 255, 240)); // 薄い緑
        g.fillRect(margin + 1, convertLoad(0.25, graphHeight), graphWidth, (getHeight() - margin * 2) / 4);

        g.setColor(new Color(240, 240, 255)); // 薄い青色
        g.fillRect(margin + 1, convertLoad(0.75, graphHeight), graphWidth, (getHeight() - margin * 2) / 2);

        for (int t = 1; t < data.size(); t++) {
            for (int i = 0; i < data.get(t).getNodeStates().size(); i++) {
                double load = data.get(t).getNodeStates().get(i).getLoad();
                int x1 = convertTime(t - 1, graphWidth);
                int y1 = convertLoad(loads.get(i), graphHeight);
                int x2 = convertTime(t, graphWidth);
                int y2 = convertLoad(load, graphHeight);

                g.setColor(colors.get(i)); // サーバーごとに色を設定
                g.drawLine(x1, y1, x2, y2);
                g.drawLine(x1 + 1, y1, x2 + 1, y2);

                loads.set(i, load);
            }
        }

        // time line
        int currentTimeX = convertTime(currentTime, graphWidth);
        g.drawLine(currentTimeX, margin, currentTimeX, getHeight() - margin);

        // 凡例を描画
        g.setColor(Color.BLACK);
        int legendX = getWidth() - margin;
        int legendY = margin + 15;
        for (int i = 0; i < legend.size(); i++) {
            g.setColor(colors.get(i));
            g.fillRect(legendX, legendY, 10, 10);
            g.setColor(Color.BLACK);
            g.drawString(legend.get(i), legendX + 15, legendY + 10);
            legendY += 20;
        }
    }
}
