package ca.cosc3p91.a3.gameobjects;

public class Soldier extends Infantry {

    static int cost = 4;

    int lvl = 0;

    public Soldier() {
        super(100, 4, 4);
    }

    @Override
    public void move(Tile t) {

    }

    @Override
    public void getPosition() {

    }

    @Override
    public int getLevel() {
        return lvl;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public void setLevel(int level) {
        lvl = level;
    }
}