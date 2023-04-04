package ca.cosc3p91.a4.gameobjects;

public class Collector implements Inhabitant  {

    private int averageCollectionRate;

    static int cost = 2;

    private int lvl = 0;

    public int getCollectionRate() {
        return averageCollectionRate;
    }

    public void setCollectionRate(int rate) {
        averageCollectionRate = rate;
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
