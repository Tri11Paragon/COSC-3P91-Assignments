package ca.cosc3p91.a2.gameobjects;

public class DefenseBuilding extends Building {

    private int damage;
    private int range;

    public void upgrade(DefenseStage stage) {
        super.upgrade(stage);
        this.damage += stage.getDamageChange();
        this.range += stage.getRangeChange();
    }

    public void attack(Infantry attacker) {

    }

}
