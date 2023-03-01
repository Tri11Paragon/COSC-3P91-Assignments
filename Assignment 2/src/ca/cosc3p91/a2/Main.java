package ca.cosc3p91.a2;

import ca.cosc3p91.a2.game.GameEngine;

public class Main {

    public static void main(String[] args) {
        GameEngine engine = new GameEngine();

        engine.printMap();
        engine.run();
    }

}
