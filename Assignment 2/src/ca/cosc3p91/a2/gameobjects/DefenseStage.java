package ca.cosc3p91.a2.gameobjects;

class DefenseStage extends Stage {

<<<<<<< HEAD
  protected int dDamage;
=======
    protected int dDamge;
>>>>>>> abf784868daa920a9ca8b3b9d291a7cf521aa9c7

    protected int dRange;

<<<<<<< HEAD
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
=======
    public void getDamageChange() {
    }

    public void getRangeChange() {
    }
>>>>>>> abf784868daa920a9ca8b3b9d291a7cf521aa9c7

}