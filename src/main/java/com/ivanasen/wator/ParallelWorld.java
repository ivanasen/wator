package com.ivanasen.wator;

import java.util.List;

public class ParallelWorld extends World {
    public ParallelWorld(State initialState) {
        super(initialState);
    }

    // TODO: Implement parallel updateState()
    @Override
    public void updateState() {
        if (!hasNextState()) {
            return;
        }

        State state = getState();
        List<List<Creature>> creatures = state.creatures();
        for (int i = 0; i < creatures.size(); i++) {
            List<Creature> row = creatures.get(i);
            for (int j = 0; j < row.size(); j++) {
                row.get(j).updateState(state);
            }
        }
    }
}
