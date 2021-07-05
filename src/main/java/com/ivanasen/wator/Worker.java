package com.ivanasen.wator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public record Worker(
        State state, int index, int size, int nThreads, long totalIterations, int sleepBetweenIterations)
        implements Runnable {
    @Override
    public void run() {
        try {
            var random = new Random(index);

            // If (iterations == UPDATE_FOREVER) this will run forever as int will overflow
            for (int iteration = 0; iteration < totalIterations; iteration++) {
                runSingleIteration(random);
                try {
                    Thread.sleep(sleepBetweenIterations);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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

        for (int i = startRow; i <= endRow; i++) {
            int nextRow = (endRow + 1) % state.height();
            if (i == startRow) {
                state.lockRow(startRow);
            } else if (i == endRow) {
                state.waitForUpdate(nextRow);
                state.lockRow(nextRow);
            }

            List<Map<Position, Creature>> creatures = state.creatures();
            var row = new HashMap<>(creatures.get(i));
            row.forEach((k, v) -> v.updateState(state, random));

            if (i == startRow) {
                state.unlockRow(startRow);
                state.signalUpdated(startRow);
            } else if (i == endRow) {
                state.unlockRow(nextRow);
            }
        }
    }
}
