package ca.cosc3p91.a2.gameobjects;

public class Worker implements Inhabitant {

    private boolean currentlyBuilding;

    static int cost = 2;

    private int lvl = 0;

    public void set_IsBuilding(boolean state) {
        currentlyBuilding = state;
    }

    public boolean isCurrentlyBuilding() {
        return currentlyBuilding;
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
