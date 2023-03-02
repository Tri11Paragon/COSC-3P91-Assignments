package ca.cosc3p91.a2.gameobjects;

public class Worker implements Inhabitant {

    private boolean currentlyBuilding;

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
        return Inhabitant.super.getLevel();
    }

    @Override
    public int setLevel(int level) {
        return Inhabitant.super.setLevel(level);
    }
}
