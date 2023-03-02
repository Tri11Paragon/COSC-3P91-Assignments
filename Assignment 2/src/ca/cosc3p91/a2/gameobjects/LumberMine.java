package ca.cosc3p91.a2.gameobjects;

public class LumberMine extends ResourceBuilding {

    public static String resource = "wood";

    public LumberMine(int lvl, ResourceStage baseStage) {
        setLevel(lvl);
        upgrade(baseStage);
    }

    public void harvest(Village_Hall hall) {
        hall.addCurrentWood(getHarvestRate());
    }

}
