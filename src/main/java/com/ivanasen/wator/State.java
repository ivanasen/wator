package com.ivanasen.wator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class State {

    public static State empty(int height, int width) {
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException("World size must be positive");
        }

        var creatures = new Creature[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                creatures[i][j] = Creature.newSea(new Position(i, j));
            }
        }
        return new State(height, width, creatures);
    }

    public static State random(int height, int width, int fishCount, int sharkCount) {
        var random = new Random(0);
        State state = empty(height, width);

        for (int i = 0; i < fishCount; i++) {
            while (true) {
                var pos = new Position(random.nextInt(height), random.nextInt(width));
                if (state.atPosition(pos).type != Creature.Type.SEA) {
                    continue;
                }
                state.addCreature(pos, Creature.newFish(pos));
                break;
            }
        }

        for (int i = 0; i < sharkCount; i++) {
            while (true) {
                var pos = new Position(random.nextInt(height), random.nextInt(width));
                if (state.atPosition(pos).type != Creature.Type.SEA) {
                    continue;
                }
                state.addCreature(pos, Creature.newShark(pos));
                break;
            }
        }

        return state;
    }

    private final Creature[][] creatures;
    private final List<ReentrantLock> locks;
    private final int height;
    private final int width;

    private State(int height, int width, Creature[][] creatures) {
        this.height = height;
        this.width = width;
        this.creatures = creatures;
        this.locks = Stream.generate(ReentrantLock::new).limit(height).collect(Collectors.toList());
    }

    public void lockRow(int row) {
        locks.get(row).lock();
    }

    public void unlockRow(int row) {
        locks.get(row).unlock();
    }

    public Creature[][] creatures() {
        return creatures;
    }

    public Creature atPosition(Position position) {
        return creatures[position.row][position.col];
    }

    public void removeAtPosition(Position pos) {
        Creature creature = creatures[pos.row][pos.col];
        creature.clear();
    }

    public void addCreature(Position pos, Creature creature) {
        creatures[pos.row][pos.col].set(creature);
    }

    public void moveToPosition(Position pos, Creature creature) {
        removeAtPosition(creature.position());
        addCreature(pos, creature);
    }

    public int height() {
        return height;
    }

    public int width() {
        return width;
    }

    public Position addPositions(Position a, Position b) {
        int newRow = (a.row + b.row) % height();
        if (newRow < 0) {
            newRow = height() + newRow;
        }

        int newCol = (a.col + b.col) % width();
        if (newCol < 0) {
            newCol = width() + newCol;
        }

        return new Position(newRow, newCol);
    }
}
