package ca.cosc3p91.a2.gameobjects;

public abstract class DefenseBuilding extends Building {

    private int damage = 0;
    private int range = 0;

    public void upgrade(DefenseStage stage) {
        super.upgrade(stage);
        this.damage += stage.getDamageChange();
        this.range += stage.getRangeChange();
    }

    public void attack(Infantry attacker) {

    }

    public int getDamage(){
        return damage;
    }

}
