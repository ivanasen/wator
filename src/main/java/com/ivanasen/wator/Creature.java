package com.ivanasen.wator;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class Creature {
    protected static final int MAX_LIBIDO = 12;
    protected static final int MIN_ENERGY = 0;

    protected World.Position position;
    protected Random random;
    protected int energy;
    protected int libido;
    protected boolean dead;

    public Creature(Random random, World.Position position, int energy, int libido) {
        this.random = random;
        this.position = position;
        this.energy = energy;
        this.libido = libido;
    }

    public abstract void updateState(State state);

    public void setRandom(Random random) {
        this.random = random;
    }

    protected List<World.Position> findEmptyCells(State state) {
        return World.VALID_TRANSITIONS.stream()
                .map(tr -> state.addPositions(position, tr))
                .filter(p -> state.atPosition(p) == State.GridCell.OCEAN)
                .collect(Collectors.toList());
    }

    public World.Position position() {
        return position;
    }

    public boolean isDead() {
        return dead;
    }
}
