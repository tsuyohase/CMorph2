package cmorph.visualizer;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fasterxml.jackson.databind.ObjectMapper;

import cmorph.logger.ConfigData;
import cmorph.logger.OutputData;
import cmorph.logger.SimulationData;
import cmorph.logger.TimeStepData;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class CMorphVisualizer extends JFrame implements ActionListener, ChangeListener {
    ObjectMapper mapper = new ObjectMapper();
    OutputData outputData;
    JButton startButton;
    JSlider slider;
    JSlider speedSlider;
    JComboBox<String> comboBox;
    JButton nodeButton;
    JButton linkButton;
    boolean isNode = false;
    boolean isLink = false;
    MainPanel mainPanel;
    LoadChartPanel loadChartPanel;
    TotalLoadChartPanel totalLoadPanel;
    JPanel configPanel;
    JPanel buttonPanel;
    JLabel valueLabel;
    JLabel speedLabel;
    MainPanelDrawer mainPanelDrawer;
    int currentTime = 0;
    int speed = 1;
    ChartPanelDrawer loadChartPanelDrawer;
    List<TimeStepData> data;
    ConfigData configData;
    String folderPath = "src/dist/output/";
    List<String> outputFileNames;

    public CMorphVisualizer() {
        setOutputFilePath();

        try {
            outputData = mapper.readValue(new File(folderPath + this.outputFileNames.get(0)),
                    OutputData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.data = outputData.getSimulationData().getTimeStepData();
        this.configData = outputData.getConfigData();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("CMorph Visualizer");

        int screenWidth = 1980;

        mainPanel = new MainPanel(screenWidth / 3, configData);
        loadChartPanel = new LoadChartPanel(screenWidth / 2, data, configData, isNode, isLink);
        totalLoadPanel = new TotalLoadChartPanel(screenWidth / 2, data, configData, isNode, isLink);

        loadChartPanelDrawer = new ChartPanelDrawer(loadChartPanel, totalLoadPanel, 0, 100, data);

        configPanel = new JPanel();
        setConfigPanel();

        buttonPanel = new JPanel();
        setButtonPanel();

        setLayout(new BorderLayout());

        JPanel mainPanelWrapper = new JPanel();
        mainPanelWrapper.setLayout(new BorderLayout());
        mainPanelWrapper.add(mainPanel, BorderLayout.CENTER);
        mainPanelWrapper.add(configPanel, BorderLayout.SOUTH);

        JPanel chartPanelWrapper = new JPanel();
        chartPanelWrapper.setLayout(new BorderLayout());

        chartPanelWrapper.add(loadChartPanel, BorderLayout.CENTER);
        chartPanelWrapper.add(totalLoadPanel, BorderLayout.SOUTH);
        // chartPanelWrapper.add(configPanel, BorderLayout.NORTH);

        add(mainPanelWrapper, BorderLayout.CENTER);
        add(chartPanelWrapper, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
        // add(configPanel, BorderLayout.NORTH);

        startButton.addActionListener(this);
        nodeButton.addActionListener(this);
        linkButton.addActionListener(this);
        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            if (this.mainPanelDrawer != null && this.mainPanelDrawer.getIsPlaying()) {
                this.currentTime = mainPanelDrawer.getCurrentTime();
                mainPanelDrawer.stopUser();
                startButton.setText("start");
            } else {
                mainPanelDrawer = new MainPanelDrawer(mainPanel, currentTime, true, this.speed, slider, data,
                        configData);
                mainPanelDrawer.start();
                startButton.setText("stop");

            }
        } else if (e.getSource() == comboBox) {
            String fileName = comboBox.getSelectedItem().toString();
            try {
                outputData = mapper.readValue(new File(folderPath + fileName),
                        OutputData.class);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            this.data = outputData.getSimulationData().getTimeStepData();
            this.configData = outputData.getConfigData();
            // loadChartPanelDrawer = new ChartPanelDrawer(loadChartPanel, totalLoadPanel,
            // currentTime, 100, data);
            loadChartPanelDrawer.setData(data, configData);
            // loadChartPanelDrawer.setCurrentTime(currentTime);
            // loadChartPanelDrawer.start();

            if (this.mainPanelDrawer != null) {
                mainPanelDrawer.stopUser();
            }
            currentTime = 0;
            mainPanelDrawer = new MainPanelDrawer(mainPanel, currentTime, false, this.speed, slider, data, configData);
            mainPanelDrawer.start();

            slider.setMaximum((int) configData.getEndTime());

            configPanel.removeAll();
            setConfigPanel();
            configPanel.revalidate();
            configPanel.repaint();

        } else if (e.getSource() == nodeButton) {
            isNode = !isNode;
            loadChartPanelDrawer.change(isNode, isLink);
        } else if (e.getSource() == linkButton) {
            isLink = !isLink;
            loadChartPanelDrawer.change(isNode, isLink);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == speedSlider) {
            JSlider source = (JSlider) e.getSource();
            this.speed = source.getValue();
            speedLabel.setText("speed: " + this.speed);
        } else if (e.getSource() == slider) {
            JSlider source = (JSlider) e.getSource();
            this.currentTime = source.getValue();
            valueLabel.setText("current time: " + this.currentTime);

            if (this.mainPanelDrawer == null) {
                mainPanelDrawer = new MainPanelDrawer(mainPanel, this.currentTime, false, this.speed, slider, data,
                        configData);
                mainPanelDrawer.start();
            } else if (this.mainPanelDrawer.currentTime != this.currentTime) {
                mainPanelDrawer.stopUser();
                mainPanelDrawer = new MainPanelDrawer(mainPanel, this.currentTime, false, this.speed, slider, data,
                        configData);
                mainPanelDrawer.start();
                startButton.setText("start");
            }
            loadChartPanelDrawer.setCurrentTime(currentTime);
            // loadChartPanelDrawer.start();
            if (this.currentTime == configData.getEndTime()) {
                this.currentTime = 0;
                mainPanelDrawer.stopUser();
                startButton.setText("start");
            }
        }

    }

    private void setOutputFilePath() {
        File folder = new File(this.folderPath);

        if (folder.exists() && folder.isDirectory()) {
            List<String> fileList = Arrays.asList(folder.list());
            this.outputFileNames = fileList;
        } else {
            System.out.println("folder not found");
        }
    }

    private void setConfigPanel() {
        configPanel.setLayout(new GridLayout(10, 10, 0, 0));
        // configPanel.add(new JLabel("MAP_WIDTH: " + configData.getMapWidth()));
        // configPanel.add(new JLabel("MAP_HEIGHT: " + configData.getMapHeight()));
        configPanel.add(new JLabel("MICRO_DATA_CENTER_NUM: " + configData.getMicroDataCenterNum()));
        configPanel.add(new JLabel("DATA_CENTER_NUM: " + configData.getDataCenterNum()));
        configPanel.add(new JLabel("USER_NUM: " + configData.getUserNum()));
        configPanel.add(new JLabel("RANDOM_JOB_TIME_SLOT: " + configData.getRandomJobTimeSlot()));
        configPanel.add(new JLabel("RANDOMIZE_RATE: " + configData.getRandomizeRate()));
        configPanel.add(new JLabel("AVE_JOB_TIME_SLOT: " + configData.getAveJobTimeSlot()));
        configPanel.add(new JLabel("AVE_JOB_CONTAINER_NUM: " + configData.getAveJobContainerNum()));
        // configPanel.add(new JLabel("FRONT_WEIGHT: " + configData.getFrontWeight()));
        // configPanel.add(new JLabel("BACK_WEIGHT: " + configData.getBackWeight()));
        // configPanel.add(new JLabel("DATA_OBJECT_TARGET_TYPE: " +
        // configData.getDataObjectTargetType()));
        configPanel.add(new JLabel("AVE_DATA_OBJECT_SIZE: " + configData.getAveDataObjectSize()));
        configPanel.add(new JLabel("AVE_MDC_CONTAINER_NUM: " + configData.getAveMDCContainerNum()));
        configPanel.add(new JLabel("AVE_DC_CONTAINER_NUM: " + configData.getAveDCContainerNum()));
        configPanel.add(new JLabel("NODE_COST_WEIGHT_TYPE: " + configData.getNodeCostWeightType()));
        // configPanel.add(new JLabel("RANDOM_NODE_LOCATION: " +
        // configData.getRandomNodeLocation()));
        configPanel.add(new JLabel("LOAD_COST_FUNCTION_TYPE: " + configData.getLoadCostFunctionType()));
        configPanel.add(new JLabel("NETWORK_COST_FUNCTION_TYPE: " + configData.getNetworkCostFunctionType()));
        configPanel.add(new JLabel("NETWORK_TIME_UNIT_NUM: " + configData.getNetworkTimeUnitNum()));
        configPanel.add(new JLabel("USER_SPAWN_SCENARIO: " + configData.getUserSpawnScenario()));
        configPanel.add(new JLabel("TIME_UNIT_NUM: " + configData.getTimeUnitNum()));
        configPanel.add(new JLabel("useCostDifRandomization: " + configData.getUseCostDifRandomization()));
        configPanel.add(new JLabel("COST_GAIN_THRESHOLD: " + configData.getCostGainThreshold()));
        configPanel.add(new JLabel("useMigTimeRandomization: " + configData.getUseMigTimeRandomization()));
        configPanel.add(new JLabel("ELAPSED_TIME_THRESHOLD: " + configData.getElapsedTimeThreshold()));
        configPanel.add(new JLabel("END_TIME: " + configData.getEndTime()));
    }

    private void setButtonPanel() {
        startButton = new JButton("start");

        buttonPanel.setLayout(new FlowLayout());

        slider = new JSlider(JSlider.HORIZONTAL, 0, (int) configData.getEndTime(), 0);
        slider.setMajorTickSpacing(10);
        valueLabel = new JLabel("current time: 0");

        slider.addChangeListener(this);

        speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 1);
        slider.setMajorTickSpacing(1);
        speedLabel = new JLabel("speed: 1");
        speedSlider.addChangeListener(this);

        nodeButton = new JButton("node");
        linkButton = new JButton("link");

        this.comboBox = new JComboBox<>();
        comboBox.addActionListener(this);
        Collections.sort(outputFileNames);
        for (int i = 0; i < outputFileNames.size(); i++) {
            comboBox.addItem(outputFileNames.get(i));
        }

        buttonPanel.add(startButton);
        buttonPanel.add(slider);
        buttonPanel.add(valueLabel);
        buttonPanel.add(speedSlider);
        buttonPanel.add(speedLabel);
        buttonPanel.add(nodeButton);
        buttonPanel.add(linkButton);
        buttonPanel.add(comboBox);
    }

    public static void main(String[] args) {
        CMorphVisualizer cMorphVisualizer = new CMorphVisualizer();
    }
}
