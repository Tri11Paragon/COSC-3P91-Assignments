package ca.cosc3p91.a2.gameobjects;

class DefenseStage extends Stage {

  protected int dDamage;

  protected int dRange;

  public DefenseStage(int dHealth, int goldCost, int requiredVillageLevel, int upgradeTime, int ironCost, int woodCost,
                      int damageIncrease, int rangeIncrease) {
    super(dHealth,goldCost,requiredVillageLevel,upgradeTime,ironCost,woodCost);
    this.dDamage = damageIncrease;
    this.dRange = rangeIncrease;
  }

  public int getDamageChange() {
    return dDamage;
  }

  public int getRangeChange() {
    return dRange;
  }

}