package gameobjects;

import java.util.List;

public abstract class Building {

  private static int level;

  private int health;

  private static Stage stages;

  private int goldCost;

  private int ironCost;

  private int woodCost;

  private int buildTime;

  public List<Stage> stage;
  public Tile tile;

  public List<Inhabitant> inhabitant;

  public int getLevel() {
    return level;
  }

  public int getHealth() {
    return health;
  }

  public int getCost(String type) {
    return 0;
  }

  public int getUpgradeCost() {
    return 0;
  }

  public void upgrade() {

  }

  public int getBuildTime() {
    return 0;
  }

}
