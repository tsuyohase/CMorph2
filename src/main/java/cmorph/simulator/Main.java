package cmorph.simulator;

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
        SetUp.setUpNodes();
        SetUp.setUpUsers();

        while (Timer.getEvent() != null) {
            Timer.runEvent();
        }
    }

    /**
     * セットアップ関数
     */
    private static void setup() {

    }
}