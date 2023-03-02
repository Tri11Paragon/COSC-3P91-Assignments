package ca.cosc3p91.a2.gameobjects;

import ca.cosc3p91.a2.game.Map;

public interface Inhabitant {

    Map map = null;
    Building building = null;
    int lvl = 1;

    void move(Tile t);

    void getPosition();

    default int getLevel() {
        return lvl;
    }

    default int setLevel(int level) {
        return lvl;
    }

}
