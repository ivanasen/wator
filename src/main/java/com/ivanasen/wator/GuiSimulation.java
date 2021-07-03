package com.ivanasen.wator;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GuiSimulation extends JPanel {
    public static void main(String[] args) {
        var frame = new JFrame("Wa-Tor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GuiSimulation());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private final World world;

    public GuiSimulation() {
        var initialState = State.random(Constants.WORLD_HEIGHT, Constants.WORLD_WIDTH, Constants.FISH_COUNT, Constants.SHARK_COUNT);
        world = new ParallelWorld(initialState, Constants.NUM_THREADS, Constants.FRAME_INTERVAL_MILLIS);

        new Timer(Constants.FRAME_INTERVAL_MILLIS, e -> repaint()).start();
        new Thread(() -> world.updateState(World.UPDATE_FOREVER)).start();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Constants.WORLD_WIDTH * Constants.CELL_SIZE, Constants.WORLD_HEIGHT * Constants.CELL_SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Constants.OCEAN_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        State state = world.getState();
        var pos = new World.Position(0, 0);
        for (int i = 0; i < state.height(); i++) {
            for (int j = 0; j < state.width(); j++) {
                pos.set(i, j);
                paintCell(g, state.atPosition(pos), pos);
            }
        }
    }

    private void paintCell(Graphics g, Creature creature, World.Position position) {
        var color = Constants.OCEAN_COLOR;
        if (creature instanceof Fish) {
            color = Constants.FISH_COLOR;
        } else if (creature instanceof Shark) {
            color = Constants.SHARK_COLOR;
        }
        g.setColor(color);

        int x = position.col * Constants.CELL_SIZE;
        int y = position.row * Constants.CELL_SIZE;
        g.fillRect(x, y, Constants.CELL_SIZE, Constants.CELL_SIZE);
    }
}
