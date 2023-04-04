package ca.cosc3p91.a4.gameobjects;

public class IronMine extends ResourceBuilding {

    public static String resource = "iron";

    public IronMine(ResourceStage baseStage) {
        upgrade(baseStage);
    }

    @Override
    protected ResourceHarvestHandler getHarvestHandler() {
        return hall -> hall.addIron(getHarvestRate());
    }

    @Override
    public Stage getUpgradeStage() {
        return ResourceStages.ironStages[getLevel()+1];
    }
}
