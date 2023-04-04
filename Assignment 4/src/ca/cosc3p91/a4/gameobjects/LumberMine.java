package ca.cosc3p91.a4.gameobjects;

public class LumberMine extends ResourceBuilding {

    public static String resource = "wood";

    public LumberMine(ResourceStage baseStage) {
        upgrade(baseStage);
    }

    @Override
    protected ResourceHarvestHandler getHarvestHandler() {
        return hall -> hall.addWood(getHarvestRate());
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public Stage getUpgradeStage() {
        return ResourceStages.woodStages[getLevel()+1];
    }
}
