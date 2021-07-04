package com.ivanasen.wator;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class Creature {
    protected World.Position position;
    protected int energy;
    protected int age;

    public Creature(World.Position position, int energy, int age) {
        this.position = position;
        this.energy = energy;
        this.age = age;
    }

    public abstract void updateState(State state, Random random);

    protected List<World.Position> findEmptyCells(State state) {
        return World.VALID_TRANSITIONS.stream()
                .map(tr -> state.addPositions(position, tr))
                .filter(p -> state.atPosition(p) == null)
                .collect(Collectors.toList());
    }

    public World.Position position() {
        return position;
    }
}
