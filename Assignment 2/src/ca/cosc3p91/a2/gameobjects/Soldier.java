package ca.cosc3p91.a2.gameobjects;

public class Soldier extends Infantry {

    static int cost = 4;

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