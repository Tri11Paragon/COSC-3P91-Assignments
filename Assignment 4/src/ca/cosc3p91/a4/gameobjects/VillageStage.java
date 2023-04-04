package ca.cosc3p91.a4.gameobjects;

import ca.cosc3p91.a4.util.Time;

public class VillageStage extends Stage {

    protected int goldCapacityIncrease;

    protected int ironCapacityIncrease;

    protected int woodCapacityIncrease;

    public VillageStage(int dHealth, int goldCost, int requiredVillageLevel, Time upgradeTime, int ironCost, int woodCost,
                        int goldCapIncrease, int ironCapIncrease, int woodCapIncrease) {
        super(dHealth, goldCost, requiredVillageLevel, upgradeTime, ironCost, woodCost);
        this.goldCapacityIncrease = goldCapIncrease;
        this.ironCapacityIncrease = ironCapIncrease;
        this.woodCapacityIncrease = woodCapIncrease;
    }

    public int getGoldCapacityIncrease() {
        return goldCapacityIncrease;
    }

    public int getIronCapacityIncrease() {
        return ironCapacityIncrease;
    }

    public int getWoodCapacityIncrease() {
        return woodCapacityIncrease;
    }

}
