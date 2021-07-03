package com.ivanasen.wator;

import java.util.*;

public class State {
    public static State empty(int height, int width) {
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException("World size must be positive");
        }

        var creatures = new ArrayList<List<Creature>>(Collections.nCopies(height, null));
        for (int row = 0; row < height; row++) {
            creatures.set(row, new ArrayList<>());
        }
        return new State(height, width, creatures, 0, 0);
    }

    public static State random(int height, int width, int fishCount, int sharkCount, Random random) {
        State state = empty(height, width);

        for (int i = 0; i < fishCount; i++) {
            while (true) {
                var pos = new World.Position(random.nextInt(height), random.nextInt(width));
                if (state.atPosition(pos) != null) {
                    continue;
                }
                state.setAtPosition(pos, new Fish(random, pos));
                break;
            }
        }

        for (int i = 0; i < sharkCount; i++) {
            while (true) {
                var pos = new World.Position(random.nextInt(height), random.nextInt(width));
                if (state.atPosition(pos) != null) {
                    continue;
                }
                state.setAtPosition(pos, new Shark(random, pos));
                break;
            }
        }

        return state;
    }

    private final Creature[][] grid;
    private final List<List<Creature>> creatures;
    private final int height;
    private final int width;

    private State(int height, int width, List<List<Creature>> creatures, int fishCount, int sharkCount) {
        this.height = height;
        this.width = width;
        this.creatures = creatures;
        this.grid = new Creature[height][width];
        for (List<Creature> row : creatures) {
            for (Creature c : row) {
                grid[c.position().row][c.position().col] = c;
            }
        }
    }

    public List<List<Creature>> creatures() {
        return creatures;
    }

    public Creature atPosition(World.Position position) {
        return grid[position.row][position.col];
    }

    public boolean isValidPosition(World.Position position) {
        try {
            requireValidPosition(position.row, position.col);
            return true;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    public void removeAtPosition(World.Position pos) {
        Creature creature = grid[pos.row][pos.col];
        if (creature == null) {
            return;
        }

        grid[pos.row][pos.col] = null;
        List<Creature> row = creatures.get(pos.row);
        Creature last = row.get(row.size() - 1);
        last.index = creature.index;
        row.set(creature.index, last);
        row.remove(row.size() - 1);
    }

    public void setAtPosition(World.Position pos, Creature creature) {
        System.out.println(creature.position.row + ":" + creature.position.col + ":" + creature.index);
        Creature oldCreature = grid[pos.row][pos.col];
        if (oldCreature != null) {
            removeAtPosition(pos);
        }

        creature.index = creatures.get(pos.row).size();
        creatures.get(pos.row).add(creature);
        grid[pos.row][pos.col] = creature;
    }

    public void moveToPosition(World.Position pos, Creature creature) {
        removeAtPosition(creature.position());
        setAtPosition(pos, creature);
    }

    public int height() {
        return height;
    }

    public int width() {
        return width;
    }

    private void requireValidPosition(int row, int col) {
        if (row < 0 || row >= height()) {
            throw new IllegalArgumentException("Invalid row: " + row);
        }
        if (col < 0 || col >= width()) {
            throw new IllegalArgumentException("Invalid column: " + col);
        }
    }

    public World.Position addPositions(World.Position a, World.Position b) {
        int newRow = (a.row + b.row) % height();
        if (newRow < 0) {
            newRow = height() + newRow;
        }

        int newCol = (a.col + b.col) % width();
        if (newCol < 0) {
            newCol = width() + newCol;
        }

        return new World.Position(newRow, newCol);
    }
}
