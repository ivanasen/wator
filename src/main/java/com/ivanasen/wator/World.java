package com.ivanasen.wator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {
    public static record Position(int row, int col) {
    }

    public static final List<Position> VALID_TRANSITIONS = List.of(
            new World.Position(-1, 0), // North
            new World.Position(1, 0),  // South
            new World.Position(0, -1), // East
            new World.Position(0, 1)   // West
    );

    private final State state;

    public World(State initialState) {
        state = initialState;
    }

    public void updateState() {
        if (!hasNextState()) {
            return;
        }

        Map<Position, Creature> creatures = new HashMap<>(state.creatures());
        creatures.forEach((position, creature) -> {
            creature.updateState(state);
        });
    }

    public State getState() {
        return state;
    }

    public boolean hasNextState() {
        return !state.creatures().isEmpty();
    }
}