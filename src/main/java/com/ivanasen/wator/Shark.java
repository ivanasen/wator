package com.ivanasen.wator;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Shark extends Creature {
    public boolean debug;

    public Shark(Random random, World.Position position) {
        super(random, position, 11, 0);
    }

    @Override
    public void updateState(State state) {
        if (energy <= MIN_ENERGY) {
            state.removeCreature(this);
            return;
        }

        World.Position newPosition = position;

        List<World.Position> possibleTransitions = findCellsWithFish(state);
        if (possibleTransitions.isEmpty()) {
            possibleTransitions = findEmptyCells(state);
            energy--;
        } else {
            energy++;
        }


        if (!possibleTransitions.isEmpty()) {
            newPosition = possibleTransitions.get(random.nextInt(possibleTransitions.size()));
        }

        this.debug = newPosition.equals(position);
        state.setAtPosition(newPosition, this);

        age++;
        if (!position.equals(newPosition)) {
            if (age >= MAX_AGE) {
                age = 0;
                var child = new Shark(random, position);
                state.addCreature(child);
            } else {
                state.setAtPosition(position, null);
            }
        }

        position = newPosition;
    }

    private List<World.Position> findCellsWithFish(State state) {
        return World.VALID_TRANSITIONS.stream()
                .map(tr -> state.addPositions(position, tr))
                .filter(p -> state.atPosition(p) == State.GridCell.FISH)
                .collect(Collectors.toList());
    }
}
