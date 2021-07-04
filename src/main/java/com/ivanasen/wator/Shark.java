package com.ivanasen.wator;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Shark extends Creature {

    public Shark(Position position) {
        super(position, Constants.SHARK_INITIAL_ENERGY, 0);
    }

    @Override
    public void updateState(State state, Random random) {
        if (energy != Constants.ENERGY_IMMORTAL && energy <= Constants.MIN_ENERGY) {
            state.removeAtPosition(position);
            return;
        }

        Position newPosition = position;

        List<Position> possibleTransitions = findCellsWithFish(state);
        if (possibleTransitions.isEmpty()) {
            possibleTransitions = findEmptyCells(state);
            energy--;
        } else {
            energy++;
        }


        if (!possibleTransitions.isEmpty()) {
            newPosition = possibleTransitions.get(random.nextInt(possibleTransitions.size()));
        }

        state.moveToPosition(newPosition, this);

        age++;
        if (!position.equals(newPosition)) {
            if (age >= Constants.SHARK_MAX_AGE) {
                age = 0;
                var child = new Shark(position);
                state.addCreature(child.position(), child);
            }
        }

        position = newPosition;
    }

    private List<Position> findCellsWithFish(State state) {
        return World.VALID_TRANSITIONS.stream()
                .map(tr -> state.addPositions(position, tr))
                .filter(p -> state.atPosition(p) instanceof Fish)
                .collect(Collectors.toList());
    }
}
