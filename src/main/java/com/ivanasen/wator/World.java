package com.ivanasen.wator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

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
//        Iterator<Map.Entry<Position, Creature>> iterator = state.creatures().entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<Position, Creature> item = iterator.next();
//            item.getValue().updateState(state);
//            if (item.getValue().isDead()) {
//                iterator.remove();
//                state.setAtPosition(item.getValue().position(), State.GridCell.OCEAN);
//            }
//        }
//        state.spawnNewCreatures();
    }

    public State getState() {
        return state;
    }

    public boolean hasNextState() {
        return !state.creatures().isEmpty();
    }
}