package ca.cosc3p91.a2.gameobjects;

import java.util.ArrayList;

public class Village_Hall extends Building {

<<<<<<< HEAD
  private int goldCapacity = 0;

  private int ironCapacity = 0;

  private int woodCapacity = 0;

  public Village_Hall (int lvl, VillageStage baseStage) {
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
=======
    private int goldCapacity;

    private int ironCapacity;

    private int woodCapacity;

    public int getGoldCapacity() {
        return goldCapacity;
    }

    public int getIronCapacity() {
        return ironCapacity;
    }

    public int getWoodCapacity() {
        return woodCapacity;
    }
>>>>>>> abf784868daa920a9ca8b3b9d291a7cf521aa9c7

}
