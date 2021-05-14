package com.ivanasen.wator;

import java.util.List;
import java.util.Random;

public class Fish extends Creature {
    public Fish(Random random, World.Position position) {
        super(random, position, -1, 0);
    }

    @Override
    public void updateState(State state) {
        if (state.atPosition(position) == State.GridCell.SHARK) {
            state.creatures().remove(this);
            return;
        }

        World.Position newPosition = position;

        List<World.Position> possibleTransitions = findEmptyCells(state);
        if (possibleTransitions.size() > 0) {
            newPosition = possibleTransitions.get(random.nextInt(possibleTransitions.size()));
            state.setAtPosition(newPosition, State.GridCell.FISH);
        }

        libido++;
        if (libido >= MAX_LIBIDO && !position.equals(newPosition)) {
            libido = 0;
            var child = new Fish(random, position);
            state.creatures().add(child);
        } else {
            state.setAtPosition(position, State.GridCell.OCEAN);
        }

        position = newPosition;
    }
}