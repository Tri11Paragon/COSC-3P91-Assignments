package ca.cosc3p91.a2.gameobjects;

abstract class Stage {

    protected int dHealth;

    protected int goldCost;

    protected int requiredVillageLevel;

    protected int upgradeTime;

    protected int ironCost;

    protected int woodCost;

    public Building building;

    public void getHealthChange() {
    }

    public int getCost(String type) {
        return 0;
    }

    public int getRequiredVillageLevel() {
        return requiredVillageLevel;
    }

    public int getUpgradeTime() {
        return upgradeTime;
    }

}
