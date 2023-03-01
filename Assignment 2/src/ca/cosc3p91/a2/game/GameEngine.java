package ca.cosc3p91.a2.game;

import ca.cosc3p91.a2.player.Player;

public class GameEngine implements Runnable {

    private final boolean running = true;

    private Player player;

    private int pillageFactor;

    private int currentTime;

    public Map map;

    public void attackVIllage(Map map) {
    }

    public Map generateMap() {
        return null;
    }

    public void getScore(Map map) {
    }

    @Override
    public void run() {
        while (running) {

        }
    }
}
