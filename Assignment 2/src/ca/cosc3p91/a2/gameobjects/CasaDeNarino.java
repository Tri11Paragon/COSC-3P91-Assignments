package ca.cosc3p91.a2.gameobjects;

import java.util.ArrayList;

public class CasaDeNarino extends Building {

    private int goldCapacity = 0;
    private int ironCapacity = 0;
    private int woodCapacity = 0;

    private int currentGold;
    private int currentIron;
    private int currentWood;

    public CasaDeNarino(int lvl, VillageStage baseStage) {
        setLevel(lvl);
        upgrade(baseStage);
    }

    public void upgrade(VillageStage stage) {
        super.upgrade(stage);
        this.goldCapacity += stage.getGoldCapacityIncrease();
        this.ironCapacity += stage.getIronCapacityIncrease();
        this.woodCapacity += stage.getWoodCapacityIncrease();
    }


    public int getGoldCapacity() {
        return goldCapacity;
    }

    public int getIronCapacity() {
        return ironCapacity;
    }

    public int getWoodCapacity() {
        return woodCapacity;
    }

    public int getCurrentGold() {
        return currentGold;
    }

    public void addCurrentGold(int currentGold) {
        this.currentGold += currentGold;
    }

    public int getCurrentIron() {
        return currentIron;
    }

    public void addCurrentIron(int currentIron) {
        this.currentIron += currentIron;
    }

    public int getCurrentWood() {
        return currentWood;
    }

    public void addCurrentWood(int currentWood) {
        this.currentWood += currentWood;
    }
}
