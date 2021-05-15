package com.ivanasen.wator;

import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Simulation extends JPanel {
    private static final Color OCEAN_COLOR = new Color(33, 33, 33);
    private static final Color FISH_COLOR = new Color(102, 187, 106);
    private static final Color SHARK_COLOR = new Color(33, 150, 243);
    private static final int CELL_SIZE = 4;
    private static final int WORLD_HEIGHT = 270;
    private static final int WORLD_WIDTH = 480;
    private static final int FISH_COUNT = 50000;
    private static final int SHARK_COUNT = 5000;
    private static final int FRAME_INTERVAL_MILLIS = 20;

    public static void main(String[] args) {
        var frame = new JFrame("Wa-Tor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Simulation());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private final World world;

    public Simulation() {
        var random = new Random();
        var initialState = State.random(WORLD_HEIGHT, WORLD_WIDTH, FISH_COUNT, SHARK_COUNT, random);
        world = new World(initialState);

        var timer = new Timer(FRAME_INTERVAL_MILLIS, e -> {
            updateState();
            repaint();
        });
        timer.start();
    }

    private void updateState() {
        if (world.hasNextState()) {
            world.updateState();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WORLD_WIDTH * CELL_SIZE, WORLD_HEIGHT * CELL_SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(OCEAN_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());


        State state = world.getState();
        for (int i = 0; i < state.height(); i++) {
            for (int j = 0; j < state.width(); j++) {
                var pos = new World.Position(i, j);
                paintCell(g, state, state.atPosition(pos), pos);
            }
        }
    }

    private void paintCell(Graphics g, State state, State.GridCell cell, World.Position position) {
        Color color = null;
        switch (cell) {
            case FISH -> color = FISH_COLOR;
            case SHARK -> {
//                Shark creature = (Shark) state.creatures().get(position);
//                if (creature == null) {
//                    color = OCEAN_COLOR;
//                } else if (creature.debug) {
//                    color = Color.BLUE;
//                } else {
//                    color = SHARK_COLOR;
//                }
                color = SHARK_COLOR;
            }
            case OCEAN -> color = OCEAN_COLOR;
        }
        g.setColor(color);

        int y = position.row() * CELL_SIZE;
        int x = position.col() * CELL_SIZE;
        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
    }
}
