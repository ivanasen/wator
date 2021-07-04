package com.ivanasen.wator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class World {
    public static final List<Position> VALID_TRANSITIONS = List.of(
            new Position(-1, 0), // North
            new Position(1, 0),  // South
            new Position(0, -1), // East
            new Position(0, 1)   // West
    );

    public static final long UPDATE_FOREVER = Long.MAX_VALUE;

    protected final State state;
    private final Random random;
    protected final int sleepBetweenIterations;

    public World(State initialState, Random random, int sleepBetweenIterations) {
        state = initialState;
        this.random = random;
        this.sleepBetweenIterations = sleepBetweenIterations;
    }

    public void updateState(long iterations) {
        // If (iterations == UPDATE_FOREVER) this will run forever as int will overflow
        for (int i = 0; i < iterations; i++) {
            updateSingleIteration();
            try {
                Thread.sleep(sleepBetweenIterations);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateSingleIteration() {
//        if (!hasNextState()) {
//            return;
//        }

//        List<Map<Position, Creature>> creatures = state.creatures();
        Creature[] creatures = state.creatures();
        for (Creature head : creatures) {
            Creature current = head;
            while (current != null) {
                current.updated = false;
                current = current.next;
            }
        }

        for (Creature head : creatures) {
            Creature current = head;
            while (current != null) {
                Creature next = current.next;
                if (!current.updated) {
                    current.updateState(state, random);
                    current.updated = true;
                }
                current = next;
            }
        }
//        for (Map<Position, Creature> row : creatures) {
//            var rowCopy = new HashMap<>(row);
//            rowCopy.forEach((k, v) -> v.updateState(state, random));
//        }
    }

    public State getState() {
        return state;
    }

//    public boolean hasNextState() {
//        return !state.creatures().isEmpty();
//    }
}