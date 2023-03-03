package ca.cosc3p91.a2.gameobjects;

public class LumberMine extends ResourceBuilding {

    public static String resource = "wood";

    public LumberMine(int lvl, ResourceStage baseStage) {
        setLevel(lvl);
        upgrade(baseStage);
    }

    @Override
    protected ResourceHarvestHandler getHarvestHandler() {
        return hall -> hall.addCurrentWood(getHarvestRate());
    }

}
