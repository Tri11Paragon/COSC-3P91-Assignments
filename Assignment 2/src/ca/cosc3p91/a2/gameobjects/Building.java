package ca.cosc3p91.a2.gameobjects;

import java.util.ArrayList;

public abstract class Building {

    // members
    private int level;
    private int health;

    private Stage stage;

    private int goldCost;
    private int ironCost;
    private int woodCost;

    private int buildTime;

    public ArrayList<Tile> tiles = new ArrayList<>();
    public ArrayList<Inhabitant> inhabitants = new ArrayList<>();

    // functions

    public int getLevel() {
        return level;
    }

    public int getHealth() {
        return health;
    }

    public int getCost(String type) {
        return (type.equals("gold")) ? (goldCost) :
                (type.equals("iron")) ? (ironCost) : woodCost;
    }

    public Stage getStage() {
        return stage;
    }

    public int getUpgradeCost() {
        return 0;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void addInhabitant(Inhabitant newMember) {
        inhabitants.add(newMember);
        // newMember.setBuilding(this);
    }

    public void upgrade(Stage stage) {
        this.stage = stage;
        this.health += stage.dHealth;
        // evil hack
        String name = stage.getClass().getSimpleName();
        this.level = Integer.parseInt(name.charAt(name.length()-1) + "") - 1;
        // interact with the timer regarding Upgrade time
    }

    public int getBuildTime() {
        return buildTime;
    }
}
