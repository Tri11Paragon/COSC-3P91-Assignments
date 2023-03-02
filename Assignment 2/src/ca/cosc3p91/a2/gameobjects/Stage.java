package ca.cosc3p91.a2.gameobjects;

abstract class Stage {

  protected int dHealth;

  protected int goldCost;

  protected int requiredVillageLevel;

  protected int upgradeTime;

  protected int ironCost;

  protected int woodCost;

  public Stage(int dHealth, int goldCost, int requiredVillageLevel, int upgradeTime, int ironCost, int woodCost) {
    this.dHealth = dHealth;
    this.goldCost = goldCost;
    this.requiredVillageLevel = requiredVillageLevel;
    this.upgradeTime = upgradeTime;
    this.ironCost = ironCost;
    this.woodCost = woodCost;
  }

  public int getHealthChange() {
    return dHealth;
  }

  public int getCost(String type) {
    return (type.equals("gold"))?(goldCost):
            (type.equals("iron"))?(ironCost):woodCost;
  }

  public int getRequiredVillageLevel() {
    return requiredVillageLevel;
  }

  public int getUpgradeTime() {
    return upgradeTime;
  }

}
