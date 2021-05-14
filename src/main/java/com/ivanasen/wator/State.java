package com.ivanasen.wator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class State {
    public enum GridCell {
        OCEAN, FISH, SHARK
    }

    public static State random(int height, int width, int fishCount, int sharkCount, Random random) {
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException("World size must be positive");
        }

        var grid = new GridCell[height][width];
        Arrays.stream(grid).forEach(row -> Arrays.fill(row, GridCell.OCEAN));
        var creatures = new ArrayList<Creature>();

        Runnable spawnFish = () -> {
            while (true) {
                int row = random.nextInt(height);
                int col = random.nextInt(width);
                if (grid[row][col] != GridCell.OCEAN) {
                    continue;
                }

                grid[row][col] = GridCell.FISH;
                creatures.add(new Fish(random, new World.Position(row, col)));
                break;
            }
        };

        Runnable spawnShark = () -> {
            while (true) {
                int row = random.nextInt(height);
                int col = random.nextInt(width);
                if (grid[row][col] != GridCell.OCEAN) {
                    continue;
                }

                grid[row][col] = GridCell.SHARK;
                creatures.add(new Shark(random, new World.Position(row, col)));
                break;
            }
        };

        for (int i = 0; i < fishCount; i++) {
            spawnFish.run();
        }

        for (int i = 0; i < sharkCount; i++) {
            spawnShark.run();
        }

        return new State(grid, creatures, fishCount, sharkCount);
    }

    private final GridCell[][] grid;
    private final List<Creature> creatures;

    private State(GridCell[][] grid, List<Creature> creatures, int fishCount, int sharkCount) {
        this.grid = grid;
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

    public List<Creature> creatures() {
        return creatures;
    }

    public GridCell atPosition(World.Position position) {
        return atPosition(position.row(), position.col());
    }

    public GridCell atPosition(int row, int col) {
        return grid[row][col];
    }

    public boolean isValidPosition(World.Position position) {
        try {
            requireValidPosition(position.row(), position.col());
            return true;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    public void setAtPosition(World.Position pos, GridCell cell) {
        grid[pos.row()][pos.col()] = cell;
    }

    public int height() {
        return grid.length;
    }

    public int width() {
        return grid[0].length;
    }

    private void requireValidPosition(int row, int col) {
        if (row < 0 || row >= height()) {
            throw new IllegalArgumentException("Invalid row: " + row);
        }
        if (col < 0 || col >= width()) {
            throw new IllegalArgumentException("Invalid column: " + col);
        }
    }

    @Override
    public String toString() {
        var result = new StringBuilder();
        for (GridCell[] row : grid) {
            for (GridCell cell : row) {
                switch (cell) {
                    case FISH -> result.append('F');
                    case SHARK -> result.append('S');
                    case OCEAN -> result.append('~');
                }
            }
            result.append('\n');
        }
        return result.toString();
    }

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
}
