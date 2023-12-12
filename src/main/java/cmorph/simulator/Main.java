package cmorph.simulator;

import cmorph.logger.Logger;
import cmorph.logger.OutputData;
import cmorph.setUp.SetUp;
import java.util.Random;

public class Main {
    public static Random random = new Random();

    /**
     * Main 関数
     * 
     * @param args
     */
    public static void main(String[] args) {
        SetUp.setUp();

        while (Timer.getEvent() != null) {
            Timer.runEvent();
        }

        Logger.log();
    }
}