package com.ivanasen.wator;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class Creature {
    protected static final int MAX_AGE = 12;
    protected static final int MIN_ENERGY = 0;

    protected World.Position position;
    protected int index;
    protected Random random;
    protected int energy;
    protected int age;

    public Creature(Random random, World.Position position, int energy, int age) {
        this.random = random;
        this.position = position;
        this.energy = energy;
        this.age = age;
    }

    public abstract void updateState(State state);

    protected List<World.Position> findEmptyCells(State state) {
        return World.VALID_TRANSITIONS.stream()
                .map(tr -> state.addPositions(position, tr))
                .filter(p -> state.atPosition(p) == null)
                .collect(Collectors.toList());
    }

    public World.Position position() {
        return position;
    }

    public int age() {
        return age;
    }

    public int index() {
        return index;
    }
}
