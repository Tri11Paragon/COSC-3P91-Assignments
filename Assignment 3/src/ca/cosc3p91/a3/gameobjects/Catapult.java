package ca.cosc3p91.a3.gameobjects;

public class Catapult extends Infantry {

    static int cost = 6;

    private int lvl = 0;

    public Catapult() {
        super(80, 12, 12);
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