package ca.cosc3p91.a2.gameobjects;

public class LumberMine extends ResourceBuilding {

    public static String resource = "wood";

    public LumberMine(ResourceStage baseStage) {
        upgrade(baseStage);
    }

    @Override
    protected ResourceHarvestHandler getHarvestHandler() {
        return hall -> hall.addWood(getHarvestRate());
    }

}
