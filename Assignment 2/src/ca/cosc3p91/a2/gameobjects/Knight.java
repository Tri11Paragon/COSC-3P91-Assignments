package ca.cosc3p91.a2.gameobjects;

public class Knight extends Infantry {

    static int cost = 6;

    public Knight() {
        super(150, 6, 6);
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