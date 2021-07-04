package com.ivanasen.wator;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Creature {
    public enum Type {
        SEA,
        FISH,
        SHARK
    }

    protected Position position;
    protected int energy;
    protected int age;
    protected Type type;

    public static Creature newShark(Position position) {
        return new Creature(position, Type.SHARK, Constants.SHARK_INITIAL_ENERGY, 0);
    }

    public static Creature newFish(Position position) {
        return new Creature(position, Type.FISH, Constants.FISH_INITIAL_ENERGY, 0);
    }

    public static Creature newSea(Position position) {
        return new Creature(position, Type.SEA, 0, 0);
    }

    public Creature(Position position, Type type, int energy, int age) {
        this.position = position;
        this.energy = energy;
        this.age = age;
        this.type = type;
    }

    public void set(Creature creature) {
        position = creature.position;
        energy = creature.energy;
        age = creature.age;
        type = creature.type;
    }

    public void toNewFish(Position pos) {
        position = pos;
        energy = Constants.FISH_INITIAL_ENERGY;
        age = 0;
        type = Type.FISH;
    }

    public void toNewShark(Position pos) {
        position = pos;
        energy = Constants.SHARK_INITIAL_ENERGY;
        age = 0;
        type = Type.SHARK;
    }

    public void clear() {
        type = Type.SEA;
    }

    public void updateState(State state, Random random) {
        switch (type) {
            case FISH -> {
                if (energy != Constants.ENERGY_IMMORTAL && energy <= Constants.MIN_ENERGY) {
                    state.removeAtPosition(position);
                    return;
                }

                if (state.atPosition(position).type == Type.SHARK) {
                    return;
                }

                if (energy != Constants.ENERGY_IMMORTAL) {
                    energy--;
                }

                Position newPosition = position;

                List<Position> possibleTransitions = findEmptyCells(state);
                if (possibleTransitions.size() > 0) {
                    newPosition = possibleTransitions.get(random.nextInt(possibleTransitions.size()));
                    int oldAge = age;
                    state.moveToPosition(newPosition, this);
                    state.creatures()[newPosition.row][newPosition.col].age = oldAge + 1;
                }

                if (!position.equals(newPosition)) {
                    if (age >= Constants.FISH_MAX_AGE) {
                        age = 0;
                        state.creatures()[position.row][position.col].toNewFish(position);
                    }
                }
            }
            case SHARK -> {
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

                int oldAge = age;
                state.moveToPosition(newPosition, this);
                state.creatures()[newPosition.row][newPosition.col].age = oldAge + 1;

                if (!position.equals(newPosition)) {
                    if (age >= Constants.SHARK_MAX_AGE) {
                        age = 0;
                        state.creatures()[position.row][position.col].toNewShark(position);
                    }
                }
            }
        }
    }

    protected List<Position> findEmptyCells(State state) {
        return World.VALID_TRANSITIONS.stream()
                .map(tr -> state.addPositions(position, tr))
                .filter(p -> state.atPosition(p).type == Type.SEA)
                .collect(Collectors.toList());
    }

    private List<Position> findCellsWithFish(State state) {
        return World.VALID_TRANSITIONS.stream()
                .map(tr -> state.addPositions(position, tr))
                .filter(p -> state.atPosition(p).type == Type.FISH)
                .collect(Collectors.toList());
    }

    public Position position() {
        return position;
    }
}
