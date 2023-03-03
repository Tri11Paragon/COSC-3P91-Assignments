package ca.cosc3p91.a2.gameobjects;

public class SaulGoodMine extends ResourceBuilding {

    public static String resource = "gold";

    public SaulGoodMine(ResourceStage baseStage) {
        upgrade(baseStage);
    }

    @Override
    protected ResourceHarvestHandler getHarvestHandler() {
        return hall -> hall.addGold(getHarvestRate());
    }
}
