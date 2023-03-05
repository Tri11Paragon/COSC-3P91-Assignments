package ca.cosc3p91.a2.gameobjects;

public class Archer extends Infantry {

    static int cost = 4;

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
        return super.getLevel();
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public void setLevel(int level) {

    }

}