package ca.cosc3p91.a3.gameobjects;

import java.io.Serializable;

public interface Inhabitant extends Serializable {

    int lvl = 0;

    void move(Tile t);

    void getPosition();

    int getLevel();
    void setLevel(int level);
    int getCost();

}
