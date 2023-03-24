package ca.cosc3p91.a3.gameobjects;

public class Cannon extends DefenseBuilding {

    public Cannon() {
        setLevel(1);
        upgrade(DefenseStages.cannonStages[0]);
    }

    @Override
    public Stage getUpgradeStage() {
        return DefenseStages.cannonStages[getLevel()+1];
    }
}