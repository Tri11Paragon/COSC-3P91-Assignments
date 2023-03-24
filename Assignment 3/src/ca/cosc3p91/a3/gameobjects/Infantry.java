package ca.cosc3p91.a3.gameobjects;

public abstract class Infantry implements Inhabitant {

    private int health;

    private int damage;

    private int range;

    public Infantry(int hitPoints, int damage, int range) {
        this.health = hitPoints;
        this.damage = damage;
        this.range = range;
    }

    public void attack(Building b) {
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setRange(int range) {
        this.range = range;
    }
}
