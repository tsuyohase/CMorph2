package cmorph.visualizer;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

import cmorph.logger.ConfigData;

import static cmorph.settings.SimulationConfiguration.MAP_HEIGHT;
import static cmorph.settings.SimulationConfiguration.MAP_WIDTH;;

public class MainPanel extends JPanel {
    BufferedImage bufferedImage;
    Graphics2D bufferedGraphics;

    public MainPanel(int size, ConfigData configData) {
        setPreferredSize(new Dimension(size, size));

        bufferedImage = new BufferedImage(
                size, size, BufferedImage.TYPE_INT_RGB);
        bufferedGraphics = bufferedImage.createGraphics();
        bufferedGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        bufferedGraphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

    }

    @Override
    public void paintComponent(Graphics myg) {
        // super.paintComponent(myg);
        int panelWidth = getSize().width;
        int imgHeight = bufferedImage.getHeight() * panelWidth / bufferedImage.getWidth();
        myg.drawImage(bufferedImage, 0, 0, panelWidth, imgHeight, this);
        // getSize().widthはMyPanelのインスタンスの幅
    }
}
