package com.ivanasen.wator;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Shark extends Creature {
    public Shark(Random random, World.Position position) {
        super(random, position, 11, 0);
    }

    @Override
    public void updateState(State state) {
        if (energy <= MIN_ENERGY) {
            state.setAtPosition(position, State.GridCell.OCEAN);
            state.creatures().remove(this);
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


        if (possibleTransitions.size() > 0) {
            newPosition = possibleTransitions.get(random.nextInt(possibleTransitions.size()));
        }

        state.setAtPosition(newPosition, State.GridCell.SHARK);

        libido++;
        if (libido >= MAX_LIBIDO && !position.equals(newPosition)) {
            libido = 0;
            var child = new Shark(random, position);
            state.creatures().add(child);
        } else {
            state.setAtPosition(position, State.GridCell.OCEAN);
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
