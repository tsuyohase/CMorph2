package cmorph.visualizer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import cmorph.logger.ConfigData;
import cmorph.logger.TimeStepData;

import static cmorph.settings.SimulationConfiguration.END_TIME;

public class ChartPanelDrawer extends Thread {
    LoadChartPanel serverLoadChartPanel;
    TotalLoadChartPanel totalLoadPanel;
    int currentTime;
    List<TimeStepData> data;

    public ChartPanelDrawer(LoadChartPanel serverLoadChartPanel, TotalLoadChartPanel totalLoadPanel, int currentTime,
            int margin,
            List<TimeStepData> data) {
        this.serverLoadChartPanel = serverLoadChartPanel;
        this.currentTime = currentTime;
        this.data = data;
        this.totalLoadPanel = totalLoadPanel;
    }

    public void setData(List<TimeStepData> data, ConfigData configData) {
        this.data = data;
        this.currentTime = 0;
        serverLoadChartPanel.setData(data, configData);
        serverLoadChartPanel.repaint();
        totalLoadPanel.setData(data, configData);
        totalLoadPanel.repaint();
    }

    public void change(boolean isNode, boolean isLink) {
        serverLoadChartPanel.change(isNode, isLink);
        serverLoadChartPanel.repaint();
        totalLoadPanel.change(isNode, isLink);
        totalLoadPanel.repaint();
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
        serverLoadChartPanel.setChartLine(currentTime);
        totalLoadPanel.setChartLine(currentTime);

        serverLoadChartPanel.repaint();
        totalLoadPanel.repaint();
        // serverLoadChartPanel.setChartLine(currentTime);
        // serverLoadChartPanel.repaint();
        // totalLoadPanel.setChartLine(currentTime);
        // totalLoadPanel.repaint();
    }

    @Override
    public void run() {
        serverLoadChartPanel.setChartLine(currentTime);
        // serverLoadChartPanel.setData(data);
        serverLoadChartPanel.repaint();
        totalLoadPanel.setChartLine(currentTime);
        // totalLoadPanel.setData(data);
        totalLoadPanel.repaint();

    }

}
