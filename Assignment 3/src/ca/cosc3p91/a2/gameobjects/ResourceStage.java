package ca.cosc3p91.a2.gameobjects;

import ca.cosc3p91.a2.util.Time;

public class ResourceStage extends Stage {

    protected int harvestRateIncrease;

    public ResourceStage(int dHealth, int goldCost, int requiredVillageLevel, Time upgradeTime, int ironCost, int woodCost,
                         int harvestRateIncr) {
        super(dHealth, goldCost, requiredVillageLevel, upgradeTime, ironCost, woodCost);
        this.harvestRateIncrease = harvestRateIncr;
    }

    public int getHarvestRateIncrease() {
        return harvestRateIncrease;
    }

}
