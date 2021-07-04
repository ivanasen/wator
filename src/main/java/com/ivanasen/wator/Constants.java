package com.ivanasen.wator;

import java.awt.Color;

public class Constants {
    public static final Color OCEAN_COLOR = new Color(33, 33, 33);
    public static final Color FISH_COLOR = new Color(102, 187, 106);
    public static final Color SHARK_COLOR = new Color(33, 150, 243);
    public static final int CELL_SIZE = 1;
    public static final int WORLD_HEIGHT = 1080;
    public static final int WORLD_WIDTH = 1920;
    public static final int FISH_COUNT = 1000000;
    public static final int SHARK_COUNT = 10000;
    public static final int FRAME_INTERVAL_MILLIS = 40;
    public static final int NUM_ITERATIONS = 100;
    public static final int NUM_THREADS = 8;
    public static final int FISH_MAX_AGE = 20;
    public static final int SHARK_MAX_AGE = 20;
    public static final int MIN_ENERGY = 0;
    public static final int ENERGY_IMMORTAL = -1;
    public static final int SHARK_INITIAL_ENERGY = 10;
    public static final int FISH_INITIAL_ENERGY = ENERGY_IMMORTAL;
}
