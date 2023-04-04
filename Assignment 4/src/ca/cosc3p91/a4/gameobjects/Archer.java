package ca.cosc3p91.a4.gameobjects;

public class Archer extends Infantry {

    static int cost = 4;

    private int lvl = 0;

    public Archer() {
        super(90, 2, 10);
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