package ca.cosc3p91.a2.gameobjects;

public class Cannon extends DefenseBuilding {

    public Cannon() {
        setLevel(1);
        upgrade(DefenseStages.cannonStages[0]);
    }
}