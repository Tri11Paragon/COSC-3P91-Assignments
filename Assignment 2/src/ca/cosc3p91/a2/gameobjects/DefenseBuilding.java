package ca.cosc3p91.a2.gameobjects;

public class DefenseBuilding extends Building {

<<<<<<< HEAD
  private int damage;
  private int range;

  public void upgrade(DefenseStage stage) {
    super.upgrade(stage);
    this.damage += stage.getDamageChange();
    this.range += stage.getRangeChange();
  }
=======
    public int damage;

    public int range;
>>>>>>> abf784868daa920a9ca8b3b9d291a7cf521aa9c7

    public void attack(Infantry attacker) {

    }

}
