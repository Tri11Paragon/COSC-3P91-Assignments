package ca.cosc3p91.a3.gameobjects;

public class ArcherTower extends DefenseBuilding {

    public ArcherTower() {
        setLevel(1);
        upgrade(DefenseStages.archerTowerStages[0]);
    }

    @Override
    public Stage getUpgradeStage() {
        return DefenseStages.archerTowerStages[getLevel()+1];
    }
}