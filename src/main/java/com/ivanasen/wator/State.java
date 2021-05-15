package com.ivanasen.wator;

import java.util.*;

public class State {
    public enum GridCell {
        OCEAN, FISH, SHARK
    }

    public static State random(int height, int width, int fishCount, int sharkCount, Random random) {
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException("World size must be positive");
        }

//        var grid = new GridCell[height][width];
//        Arrays.stream(grid).forEach(row -> Arrays.fill(row, GridCell.OCEAN));
        var creatures = new HashMap<World.Position, Creature>();

        for (int i = 0; i < fishCount; i++) {
            while (true) {
                int row = random.nextInt(height);
                int col = random.nextInt(width);
//                if (grid[row][col] != GridCell.OCEAN) {
//                    continue;
//                }
                if (creatures.get(new World.Position(row, col)) != null) {
                    continue;
                }

//                grid[row][col] = GridCell.FISH;
                var pos = new World.Position(row, col);
                creatures.put(pos, new Fish(random, pos));
                break;
            }
        }

        for (int i = 0; i < sharkCount; i++) {
            while (true) {
                int row = random.nextInt(height);
                int col = random.nextInt(width);
                if (creatures.get(new World.Position(row, col)) != null) {
                    continue;
                }

//                grid[row][col] = GridCell.SHARK;
                var pos = new World.Position(row, col);
                creatures.put(pos, new Shark(random, pos));
                break;
            }
        }

        return new State(height, width, creatures, fishCount, sharkCount);
    }

    //    private final GridCell[][] grid;
    private final Map<World.Position, Creature> creatures;
    private final int height;
    private final int width;

    private State(int height, int width, Map<World.Position, Creature> creatures, int fishCount, int sharkCount) {
//        this.grid = grid;
        this.height = height;
        this.width = width;
        this.creatures = creatures;
    }
//
//    public State(State state) {
//        cells = SerializationUtils.clone(state.cells);
//        fishCount = state.fishCount;
//        sharkCount = state.sharkCount;
//    }

//    public boolean killFish(int row, int col) {
//        requireValidPosition(row, col);
//
//        if (cells[row][col] instanceof Fish) {
//            cells[row][col] = null;
//            fishCount--;
//            return true;
//        }
//        return false;
//    }
//
//    public boolean spawnFish(int row, int col) {
//        requireValidPosition(row, col);
//
//        if (cells[row][col] == null) {
//            cells[row][col] = new Fish();
//            fishCount++;
//            return true;
//        }
//        return false;
//    }
//
//    public boolean killShark(int row, int col) {
//        requireValidPosition(row, col);
//
//        if (cells[row][col] instanceof Shark) {
//            cells[row][col] = null;
//            sharkCount--;
//            return true;
//        }
//        return false;
//    }
//
//    public boolean spawnShark(int row, int col) {
//        requireValidPosition(row, col);
//
//        // If there is a fish in that position automatically eat it
//        if (cells[row][col] instanceof Fish) {
//            killFish(row, col);
//        }
//
//        if (cells[row][col] == null) {
//            cells[row][col] = new Shark();
//            sharkCount++;
//            return true;
//        }
//        return false;
//    }

//    public void clear() {
//        fishCount = 0;
//        sharkCount = 0;
//        Arrays.stream(creatures).forEach(row -> Arrays.fill(row, null));
//    }

//    public Creature[][] animals() {
//        return creatures;
//    }

    public Map<World.Position, Creature> creatures() {
        return creatures;
    }

    public GridCell atPosition(World.Position position) {
        Creature creature = creatures.get(position);
        if (creature instanceof Shark) {
            return GridCell.SHARK;
        }
        if (creature instanceof Fish) {
            return GridCell.FISH;
        }
        return GridCell.OCEAN;
    }

    public boolean isValidPosition(World.Position position) {
        try {
            requireValidPosition(position.row(), position.col());
            return true;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    public void setAtPosition(World.Position pos, Creature creature) {
//        grid[pos.row()][pos.col()] = cell;
        if (creature == null) {
            creatures.remove(pos);
        } else {
            creatures.put(pos, creature);
        }
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

//    @Override
//    public String toString() {
//        var result = new StringBuilder();
//        for (GridCell[] row : grid) {
//            for (GridCell cell : row) {
//                switch (cell) {
//                    case FISH -> result.append('F');
//                    case SHARK -> result.append('S');
//                    case OCEAN -> result.append('~');
//                }
//            }
//            result.append('\n');
//        }
//        return result.toString();
//    }

    public World.Position addPositions(World.Position a, World.Position b) {
        int newRow = (a.row() + b.row()) % height();
        if (newRow < 0) {
            newRow = height() + newRow;
        }

        int newCol = (a.col() + b.col()) % width();
        if (newCol < 0) {
            newCol = width() + newCol;
        }

        return new World.Position(newRow, newCol);
    }

    public void addCreature(Creature creature) {
        creatures.put(creature.position(), creature);
    }

    public void removeCreature(Creature creature) {
        creatures.remove(creature.position());
    }

//    public void spawnNewCreatures() {
//        newCreatures.forEach(c -> creatures.put(c.position(), c));
//        newCreatures.clear();
//    }
}
