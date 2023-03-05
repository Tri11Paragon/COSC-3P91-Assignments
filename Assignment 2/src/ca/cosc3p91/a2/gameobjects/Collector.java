package ca.cosc3p91.a2.gameobjects;

public class Collector implements Inhabitant  {

    private int averageCollectionRate;

    static int cost = 2;

    public int getCollectionRate() {
        return averageCollectionRate;
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
    public int getCost() {
        return cost;
    }

    @Override
    public void setLevel(int level) {

    }
}
