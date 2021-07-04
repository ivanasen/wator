package com.ivanasen.wator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public record World(State state, int nThreads, int sleepBetweenIterations) {
    public static final List<Position> VALID_TRANSITIONS = List.of(
            new Position(-1, 0), // North
            new Position(1, 0),  // South
            new Position(0, -1), // East
            new Position(0, 1)   // West
    );

    public static final long UPDATE_FOREVER = Long.MAX_VALUE;

    public void updateState(long iterations) {
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        int size = state.height() / nThreads;

        for (int i = 0; i < nThreads; i++) {
            var worker = new Worker(state, i, size, nThreads, iterations, sleepBetweenIterations);
            executorService.submit(worker);
        }
        awaitTerminationAfterShutdown(executorService);
    }

    private void awaitTerminationAfterShutdown(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
