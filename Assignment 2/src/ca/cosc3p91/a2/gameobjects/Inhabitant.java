package ca.cosc3p91.a2.gameobjects;

import ca.cosc3p91.a2.game.Map;

public interface Inhabitant {

    Map map = null;
    Building building = null;
    int lvl = 0;

    void move(Tile t);

    void getPosition();

    int getLevel();
    void setLevel(int level);
    int getCost();

}
