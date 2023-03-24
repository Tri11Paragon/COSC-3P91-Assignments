package ca.cosc3p91.a3.gameobjects;

public class SaulGoodMine extends ResourceBuilding {

    public static String resource = "gold";

    public SaulGoodMine(ResourceStage baseStage) {
        upgrade(baseStage);
    }

    @Override
    protected ResourceHarvestHandler getHarvestHandler() {
        return hall -> hall.addGold(getHarvestRate());
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public Stage getUpgradeStage() {
        return ResourceStages.goldStages[getLevel()+1];
    }
}
