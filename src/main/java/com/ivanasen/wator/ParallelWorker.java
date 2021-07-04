package com.ivanasen.wator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public record ParallelWorker(
        State state, int index, int size, int nThreads, long totalIterations, int sleepBetweenIterations)
        implements Runnable {
    @Override
    public void run() {
        try {
            var random = new Random(index);

            // If (iterations == UPDATE_FOREVER) this will run forever as int will overflow
            for (int it = 0; it < totalIterations; it++) {
                runSingleIteration(random);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runSingleIteration(Random random) {
        int startRow = index * size;
        int endRow = startRow + size - 1;
        if (index == nThreads - 1) {
            endRow = state.height() - 1;
        }

        for (int j = startRow; j <= endRow; j++) {
            if (j == startRow) {
                state.lockRow(startRow);
            } else if (j == endRow) {
                state.lockRow((endRow + 1) % state.height());
            }

            List<Map<Position, Creature>> creatures = state.creatures();
            var row = new HashMap<>(creatures.get(j));
            row.forEach((k, v) -> v.updateState(state, random));

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
}
