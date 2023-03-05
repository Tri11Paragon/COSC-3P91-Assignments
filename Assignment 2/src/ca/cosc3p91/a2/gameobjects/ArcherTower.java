package ca.cosc3p91.a2.gameobjects;

public class ArcherTower extends DefenseBuilding {

    public ArcherTower() {
        setLevel(1);
        upgrade(DefenseStages.archerTowerStages[0]);
    }
}