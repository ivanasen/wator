package com.ivanasen.wator;

import java.util.List;
import java.util.Random;
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
    // TODO: Cleanup this code
    public void updateState(long iterations) {
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        final int size = getState().height() / nThreads;

        List<List<Creature>> creatures = state.creatures();

        for (int i = 0; i < nThreads; i++) {
            final int iFinal = i;
            executorService.submit(() -> {
                var random = new Random(iFinal);

                int startRow = iFinal * size;
                int endRow = startRow + size - 1;
                if (iFinal == nThreads - 1) {
                    endRow = state.height() - 1;
                }
//                System.out.println(iFinal + " " + startRow + " " + endRow + " " + (endRow + 1) % state.height());

                // If (iterations == UPDATE_FOREVER) this will run forever as int will overflow
                for (int it = 0; it < iterations; it++) {
//                    System.out.println("It: " + it + " thr: " + Thread.currentThread().getName());
                    for (int j = startRow; j <= endRow; j++) {
                        if (j == startRow) {
                            state.lockRow(startRow);
                        } else if (j == endRow) {
                            state.lockRow((endRow + 1) % state.height());
                        }

                        List<Creature> row = creatures.get(j);
                        for (int k = 0; k < row.size(); k++) {
                            row.get(k).updateState(state, random);
                        }

                        if (j == startRow) {
                            state.unlockRow(startRow);
                        } else if (j == endRow) {
                            state.unlockRow((endRow + 1) % state.height());
                        }
                    }

                    try {
                        Thread.sleep(sleepBetweenIterations);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
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
