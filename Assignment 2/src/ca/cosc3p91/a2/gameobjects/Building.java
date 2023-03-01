package ca.cosc3p91.a2.gameobjects;

import java.util.ArrayList;

public abstract class Building {

<<<<<<< HEAD
  // members
  private int level;
  private int health;

  private Stage stage;

  private int goldCost;
  private int ironCost;
  private int woodCost;
=======
    private static int level;

    private int health;

    private static Stage stages;

    private int goldCost;

    private int ironCost;

    private int woodCost;
>>>>>>> abf784868daa920a9ca8b3b9d291a7cf521aa9c7

    private int buildTime;

<<<<<<< HEAD
  public ArrayList<Tile> tiles = new ArrayList<>();
  public ArrayList<Inhabitant> inhabitants = new ArrayList<>();

  // functions
=======
    public List<Stage> stage;
    public Tile tile;

    public List<Inhabitant> inhabitant;
>>>>>>> abf784868daa920a9ca8b3b9d291a7cf521aa9c7

    public int getLevel() {
        return level;
    }

    public int getHealth() {
        return health;
    }

<<<<<<< HEAD
  public int getCost(String type) {
    return (type.equals("gold"))?(goldCost):
            (type.equals("iron"))?(ironCost):woodCost;
  }

  public Stage getStage() {
    return stage;
  }
=======
    public int getCost(String type) {
        return 0;
    }
>>>>>>> abf784868daa920a9ca8b3b9d291a7cf521aa9c7

    public int getUpgradeCost() {
        return 0;
    }

<<<<<<< HEAD
  public void setLevel(int level) {
    this.level = level;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public void setStage(Stage stage) {this.stage = stage; }

  public void addInhabitant(Inhabitant newMember) {
    inhabitants.add(newMember);
    // newMember.setBuilding(this);
  }

  public void upgrade(Stage stage) {
    this.stage = stage;
    this.health += stage.dHealth;
    // interact with the timer regarding Upgrade time
  }

  public int getBuildTime() {
    return buildTime;
  }
=======
    public void upgrade() {

    }

    public int getBuildTime() {
        return 0;
    }

>>>>>>> abf784868daa920a9ca8b3b9d291a7cf521aa9c7
}
