package com.ivanasen.wator;

import java.util.concurrent.TimeUnit;

public class Simulation {
    public static void main(String[] args) {
        int nThreads = Constants.NUM_THREADS;
        int iterations = Constants.NUM_ITERATIONS;
        int sharks = Constants.SHARK_COUNT;
        int fish = Constants.FISH_COUNT;
        int height = Constants.WORLD_HEIGHT;
        int width = Constants.WORLD_WIDTH;
        if (args.length > 0) {
            nThreads = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            iterations = Integer.parseInt(args[1]);
        }
        if (args.length > 2) {
            sharks = Integer.parseInt(args[2]);
        }
        if (args.length > 3) {
            fish = Integer.parseInt(args[3]);
        }
        if (args.length > 4) {
            height = Integer.parseInt(args[4]);
        }
        if (args.length > 5) {
            width = Integer.parseInt(args[5]);
        }

        System.out.printf("""
                        NThreads: %d
                        Iterations: %d
                        Shark count: %d
                        Fish count: %d
                        Height: %d
                        Width: %d
                        """,
                nThreads, iterations, sharks, fish, height, width);

        var initialState = State.random(height, width, fish, sharks);
        var world = new ParallelWorld(initialState, nThreads, 0);

        long startTime = System.nanoTime();

        world.updateState(iterations);

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        long timeElapsedMillis = TimeUnit.NANOSECONDS.toMillis(timeElapsed);
        System.out.println("Execution time millis: " + timeElapsedMillis);
    }
}
