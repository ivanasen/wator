package com.ivanasen.wator;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class World {
    public static class Position {
        public int row;
        public int col;

        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public void set(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Position position = (Position) o;
            return row == position.row && col == position.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    public static final List<Position> VALID_TRANSITIONS = List.of(
            new World.Position(-1, 0), // North
            new World.Position(1, 0),  // South
            new World.Position(0, -1), // East
            new World.Position(0, 1)   // West
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
        if (!hasNextState()) {
            return;
        }

        List<List<Creature>> creatures = state.creatures();
        for (int i = 0; i < creatures.size(); i++) {
            List<Creature> row = creatures.get(i);
            for (int j = 0; j < row.size(); j++) {
                row.get(j).updateState(state, random);
            }
        }
    }

    public State getState() {
        return state;
    }

    public boolean hasNextState() {
        return !state.creatures().isEmpty();
    }
}