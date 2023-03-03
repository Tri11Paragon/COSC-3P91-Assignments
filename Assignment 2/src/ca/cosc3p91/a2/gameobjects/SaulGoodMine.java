package ca.cosc3p91.a2.gameobjects;

public class SaulGoodMine extends ResourceBuilding {

    public static String resource = "gold";

    public SaulGoodMine(int lvl, ResourceStage baseStage) {
        setLevel(lvl);
        upgrade(baseStage);
    }

    @Override
    protected ResourceHarvestHandler getHarvestHandler() {
        return hall -> hall.addCurrentGold(getHarvestRate());
    }
}
