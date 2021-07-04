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

//        var creatures = new ArrayList<Map<Position, Creature>>(height);
        var heads = new Creature[height];
//        for (int row = 0; row < height; row++) {
//            creatures.add(row, new HashMap<>());
//        }
//        return new State(height, width, creatures);
        return new State(height, width, heads);
    }

    public static State random(int height, int width, int fishCount, int sharkCount) {
        var random = new Random(0);
        State state = empty(height, width);

        for (int i = 0; i < fishCount; i++) {
            while (true) {
                var pos = new Position(random.nextInt(height), random.nextInt(width));
                if (state.atPosition(pos) != null) {
                    continue;
                }
                state.addCreature(pos, new Fish(pos));
                break;
            }
        }

        for (int i = 0; i < sharkCount; i++) {
            while (true) {
                var pos = new Position(random.nextInt(height), random.nextInt(width));
                if (state.atPosition(pos) != null) {
                    continue;
                }
                state.addCreature(pos, new Shark(pos));
                break;
            }
        }

        return state;
    }

    private final Creature[][] grid;
    private final Creature[] heads;
    //    private final List<Map<Position, Creature>> creatures;
    private final List<ReentrantLock> locks;
    private final int height;
    private final int width;

    private State(int height, int width, Creature[] heads) {
//    private State(int height, int width, List<Map<Position, Creature>> creatures) {
        this.height = height;
        this.width = width;
//        this.creatures = creatures;
        this.grid = new Creature[height][width];
        this.locks = Stream.generate(ReentrantLock::new).limit(height).collect(Collectors.toList());
        this.heads = heads;
        for (Creature head : heads) {
            Creature current = head;
            while (current != null) {
                grid[current.position.row][current.position.col] = current;
                current = current.next;
            }
        }
//        for (Map<Position, Creature> row : creatures) {
//            row.forEach((k, v) -> grid[k.row][k.col] = v);
//        }
//        this.grid = grid;
    }

    public void lockRow(int row) {
        locks.get(row).lock();
    }

    public void unlockRow(int row) {
        locks.get(row).unlock();
    }

    //    public List<Map<Position, Creature>> creatures() {
    public Creature[] creatures() {
        return heads;
    }

    public Creature atPosition(Position position) {
        return grid[position.row][position.col];
    }

    public void removeAtPosition(Position pos) {
        Creature creature = grid[pos.row][pos.col];
        if (creature == null) {
            return;
        }

        grid[pos.row][pos.col] = null;

        Creature prev = creature.prev;
        Creature next = creature.next;
        if (prev != null) {
            prev.next = next;
        }
        if (next != null) {
            next.prev = prev;
        }
        if (creature == heads[pos.row]) {
            heads[pos.row] = next;
        }
//        Map<Position, Creature> row = creatures.get(pos.row);
//        row.remove(pos);
    }

    public void addCreature(Position pos, Creature creature) {
        Creature oldCreature = grid[pos.row][pos.col];
        if (oldCreature != null) {
            removeAtPosition(pos);
        }

//        creatures.get(pos.row).put(pos, creature);
        grid[pos.row][pos.col] = creature;
        creature.prev = null;
        creature.next = heads[pos.row];
        if (heads[pos.row] != null) {
            heads[pos.row].prev = creature;
        }
        heads[pos.row] = creature;
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
