package ca.cosc3p91.a2.gameobjects;

import ca.cosc3p91.a2.util.Time;

public class SaulGoodMine extends ResourceBuilding {

    public static String resource = "gold";

    public SaulGoodMine(int lvl, ResourceStage baseStage) {
        setLevel(lvl);
        upgrade(baseStage);
    }

    @Override
    public void harvest(Village_Hall hall) {
        hall.addCurrentGold(getHarvestRate());
    }
}
