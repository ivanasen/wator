package com.ivanasen.wator;

import java.util.List;
import java.util.Random;

public class Fish extends Creature {
    public Fish(World.Position position) {
        super(position, -1, 0);
    }

    @Override
    public void updateState(State state, Random random) {
        if (state.atPosition(position) instanceof Shark) {
            return;
        }

        World.Position newPosition = position;

        List<World.Position> possibleTransitions = findEmptyCells(state);
        if (possibleTransitions.size() > 0) {
            newPosition = possibleTransitions.get(random.nextInt(possibleTransitions.size()));
            state.moveToPosition(newPosition, this);
        }

        age++;
        if (!position.equals(newPosition)) {
            if (age >= Constants.FISH_MAX_AGE) {
                age = 0;
                var child = new Fish(position);
                state.addCreature(child.position(), child);
            }
        }

        position = newPosition;
    }
}