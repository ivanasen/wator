package com.ivanasen.wator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelWorld extends World {
    private final int nThreads;

    public ParallelWorld(State initialState, int nThreads, int sleepBetweenIterations) {
        super(initialState, null, sleepBetweenIterations);
        this.nThreads = nThreads;
    }

    @Override
    public void updateState(long iterations) {
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        int size = state.height() / nThreads;

        for (int i = 0; i < nThreads; i++) {
            var worker = new ParallelWorker(state, i, size, nThreads, iterations, sleepBetweenIterations);
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
