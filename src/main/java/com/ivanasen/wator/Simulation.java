package com.ivanasen.wator;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Simulation {
    public static void main(String[] args) {
        var random = new Random(0);
        var initialState = State.random(Constants.WORLD_HEIGHT, Constants.WORLD_WIDTH, Constants.FISH_COUNT, Constants.SHARK_COUNT, random);
        var world = new World(initialState);

        long startTime = System.nanoTime();

        for (int i = 0; i < Constants.NUM_ITERATIONS; i++) {
            world.updateState();
        }

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        System.out.println("Execution time millis: " + TimeUnit.NANOSECONDS.toMillis(timeElapsed));
    }
}
