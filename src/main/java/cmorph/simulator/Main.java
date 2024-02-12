package cmorph.simulator;

import cmorph.logger.AnalysisLogger;
import cmorph.logger.Logger;
import cmorph.logger.OutputData;
import cmorph.setUp.SetUp;
import cmorph.utils.BasicLogger;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Main {
    public static Random random = new Random();

    static BasicLogger logger = BasicLogger.getLogger("cmorph.analysis");

    private static void setupLogger() {
        LocalDateTime nowDate = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formatNowDate = dtf.format(nowDate);
        String fileName = "output-" + formatNowDate + ".json";
        String filePath = "src/dist/output/analysis/" + fileName;
        try {
            logger.setFileWriter(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main 関数
     * 
     * @param args
     */
    public static void main(String[] args) {
        setupLogger();
        SetUp.setUp();
        while (Timer.getEvent() != null) {
            Timer.runEvent();
        }

        Logger.log();
        AnalysisLogger.addSimulationResult();
    }
}